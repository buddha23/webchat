package com.lld360.cnc.core.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.mts.model.v20140618.QueryMediaWorkflowListRequest;
import com.aliyuncs.mts.model.v20140618.QueryMediaWorkflowListResponse;
import com.aliyuncs.mts.model.v20140618.SearchMediaWorkflowRequest;
import com.aliyuncs.mts.model.v20140618.SearchMediaWorkflowResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import net.sf.json.JSONObject;
import org.apache.http.client.utils.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: dhc
 * Date: 2016-12-20 13:37
 */
public class AliYunOSSUtil {
    // 媒体工作流 9841b8e04f764e529eaa98291282714b 的固定输出格式
    /*下面两个常量暂未使用*/
    public final String mediaOutKeyFormat = "Act-ss-mp4-ld/{RunId}/{FileName}";
    private final String mediaBucket = "lldbucket2";

    private String accessKey;
    private String bucket;
    private String region;
    private String host;
    private String ossHost;
    private String ossUrl;
    private String bucketUrl;
    private OSSClient client;
    private IAcsClient acsClient;

    private Logger logger = LoggerFactory.getLogger(AliYunOSSUtil.class);

    public AliYunOSSUtil(String accessKey, String accessKeySecret, String bucket, String regionId) throws ClientException {
        this.accessKey = accessKey;
        this.region = regionId;
        this.bucket = bucket;
        this.host = region + ".aliyuncs.com";
        this.ossHost = "oss-" + host;
        this.ossUrl = "http://" + ossHost;
        this.bucketUrl = "http://" + bucket + "." + ossHost;
        this.client = new OSSClient(ossUrl, accessKey, accessKeySecret);

        DefaultProfile.addEndpoint(regionId, regionId, "Mts", "mts." + host);
        IClientProfile profile = DefaultProfile.getProfile(region, accessKey, accessKeySecret);
        this.acsClient = new DefaultAcsClient(profile);
    }

    public IAcsClient getAcsClient() {
        return this.acsClient;
    }

    public String getBucketUrl() {
        return bucketUrl;
    }

    public String getBucketUrl(String bucket) {
        return "http://" + bucket + "." + ossHost;
    }

    public String getRegion() {
        return region;
    }

    public static String getBucketUrl(String region, String bucket) {
        return String.format("http://%s.oss-%s.aliyuncs.com", bucket, region);
    }

    /**
     * 对OSS对象的URL进行URLEncoding处理
     *
     * @param key OSS对象的key
     * @return URLEncoding处理后的key
     */
    public static String urlEncodingKey(String key) {
        try {
            String[] ss = key.split("/");
            for (int i = 0; i < ss.length; i++) {
                ss[i] = URLEncoder.encode(ss[i], "utf-8");
            }
            return String.join("/", ss);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 根据所在区域获取Object的直接链接地址
     *
     * @param bucket 所属Bucket
     * @param key    Object的key
     * @return Object在OSS中的直接地址
     */
    public String getObjectUrl(String bucket, String key) {
        return getBucketUrl(bucket) + '/' + urlEncodingKey(key);
    }

    /**
     * 获取Object的直接链接地址
     *
     * @param key Object的Key
     * @return Object在OSS中的直接地址
     */
    public String getObjectUrl(String key) {
        return bucketUrl + "/" + urlEncodingKey(key);
    }

    /**
     * 获取Object在OSS中的授权访问地址
     *
     * @param key           Object的Key
     * @param expireMinutes 授权失效时间
     * @return Object在OSS中的授权访问地址
     */
    public URL getObjectEncodeUrl(String key, int expireMinutes) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.MINUTE, expireMinutes);
        return client.generatePresignedUrl(bucket, key, calendar.getTime());
    }

    /**
     * 获取Media在OSS中的授权访问地址
     *
     * @param key           Object的Key
     * @param expireMinutes 授权失效时间
     * @return Object在OSS中的授权访问地址
     */
    public URL getMediaEncodeUrl(String key, int expireMinutes) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.MINUTE, expireMinutes);
        return client.generatePresignedUrl(mediaBucket, key, calendar.getTime());
    }

    /**
     * 根据前缀获取文件列表 https://help.aliyun.com/document_detail/31965.html
     *
     * @param prefix 前缀
     * @return OSS中的文件列表
     */
    public ObjectListing getObjectListing(String prefix) {
        ObjectListing listing = client.listObjects(bucket, prefix);
        List<OSSObjectSummary> objects = listing.getObjectSummaries().stream().filter(o -> o.getSize() > 0).collect(Collectors.toList());
        listing.setObjectSummaries(objects);
        return listing;
    }

    private boolean doCheck(String content, byte[] sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            return signature.verify(sign);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean VerifyOSSCallbackRequest(HttpServletRequest request, String ossCallbackBody) throws NumberFormatException, IOException {
        String autorizationInput = request.getHeader("Authorization");
        String pubKeyInput = request.getHeader("x-oss-pub-key-url");
        byte[] authorization = BinaryUtil.fromBase64String(autorizationInput);
        byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput);
        String pubKeyAddr = new String(pubKey);
        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/") && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
            System.out.println("pub key addr must be oss addrss");
            return false;
        }
        RestTemplate template = new RestTemplate();
        String retString = template.getForObject(pubKeyAddr, String.class);

        retString = retString.replace("-----BEGIN PUBLIC KEY-----", "");
        retString = retString.replace("-----END PUBLIC KEY-----", "");
        String queryString = request.getQueryString();
        String uri = request.getRequestURI();
        String authStr = URLDecoder.decode(uri, "UTF-8");
        if (queryString != null && !queryString.equals("")) {
            authStr += "?" + queryString;
        }
        authStr += "\n" + ossCallbackBody;
        return doCheck(authStr, authorization, retString);
    }

    /**
     * 验证阿里云客户端上传文件回调是否正确
     *
     * @param request 回调请求
     * @return 回调是否正确
     */
    public boolean validUploadCallback(HttpServletRequest request) {
        try {
            if (request.getContentLength() > 0) {
                ServletInputStream in = request.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return VerifyOSSCallbackRequest(request, sb.toString());
            }
        } catch (IOException e) {
            logger.warn("验证阿里云上传回调异常", e);
        }
        return false;
    }

    /**
     * 客户端上传文件策略参数<br>
     * 设置自定义接收参数custom，建议使用json方式
     *
     * @param dir         OSS中bucket的相对目录
     * @param callbackUrl 设置阿里云上传后的回调地址
     * @return 策略组成内容
     */
    public Map<String, Object> ossClientUploadPolicy(String dir, String callbackUrl) {
        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        Map<String, Object> respMap = new HashMap<>();
        try {
            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            String encodedPolicy = BinaryUtil.toBase64String(postPolicy.getBytes("utf-8"));
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> callBackBody = new HashMap<>();
            callBackBody.put("callbackUrl", callbackUrl);
            callBackBody.put("callbackHost", URIUtils.extractHost(URI.create(callbackUrl)).getHostName());
            callBackBody.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&custom=${x:custom}");
            callBackBody.put("callbackBodyType", "application/json");

            String callbackBodyJson = JSONObject.fromObject(callBackBody).toString();

            respMap.put("accessid", accessKey);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            //respMap.put("expire", formatISO8601Date(expiration));
            respMap.put("dir", dir);
            respMap.put("host", bucketUrl);
            respMap.put("expire", expireEndTime);
            respMap.put("callback", BinaryUtil.toBase64String(callbackBodyJson.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return respMap;
    }

    /**
     * 查询媒体工作流
     *
     * @param ids 工作流ID，以逗号分隔，为null时忽略该项参数
     * @return 媒体工作流集合
     */
    public QueryMediaWorkflowListResponse getMediaWorkflows(String ids) {
        QueryMediaWorkflowListRequest request = new QueryMediaWorkflowListRequest();
        request.setMediaWorkflowIds(ids);
        try {
            return acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            logger.warn("查询媒体工作流失败", e);
        }
        return null;
    }

    /**
     * 获取媒体工作流
     *
     * @return 媒体工作流集合
     */
    public SearchMediaWorkflowResponse getMediaWorkflows() {
        SearchMediaWorkflowRequest request = new SearchMediaWorkflowRequest();
        request.setStateList("Active");
        try {
            return acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            logger.warn("获取媒体工作流失败", e);
        }
        return null;
    }

}
