package com.lld360.cnc.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.lld360.cnc.core.bean.Credential;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CredentialUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialUtil.class);

    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";

    public static final String ENDPONIT = "http://oss-cn-hangzhou.aliyuncs.com";

    public static final String ACCESS_KEY_ID = "LTAINYUH1T43NbcS";

    public static final String HOST = "http://lldbucket1.oss-cn-hangzhou.aliyuncs.com";

    public static final String ACCESS_KEY_SECRET = "YoinxTl6cilyxFmnCvARY3PUtmRLOg";

    public static final String BUCKET = "lldbucket1";

    private static final String ROLE_ARN = "acs:ram::1995047127488038:role/aliyunosstokengeneratorrole";

    public static final String STS_API_VERSION = "2015-04-01";

    private static volatile CredentialUtil instance;

    public static CredentialUtil getInstance() {
        if (null == instance) {
            synchronized (CredentialUtil.class) {
                if (null == instance) {
                    instance = new CredentialUtil();
                }
            }
        }
        return instance;
    }


    private volatile OSSClient ossClient;

    public Credential getCredential() throws ClientException {
        try {
            String policy = "{\"Statement\": [{"
                    + "\"Action\": [\"oss:*\"],\"Effect\": \"Allow\","
                    + "\"Resource\": [\"acs:oss:*:*:*\"]}],\"Version\": \"1\"}";
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(ProtocolType.HTTPS);

            request.setRoleArn(ROLE_ARN);
            request.setRoleSessionName("jhtest");
            request.setPolicy(policy);
            request.setDurationSeconds((long) 900);


            Map<String, String> callBackBody = new HashMap<>();
            callBackBody.put("callbackUrl", "http://www.d6sk.com/soft/callBack");
            callBackBody.put("callbackHost", "www.d6sk.com");
            callBackBody.put("callbackBody", "bucket=${bucket}&object=${object}&etag=${etag}&size=${size}&mimeType=${mimeType}&imageInfo.height=${imageInfo.height}&imageInfo.width=${imageInfo.width}&imageInfo.format=${imageInfo.format}&my_var=${x:value}");
            String callbackBodyJson = JSONObject.fromObject(callBackBody).toString();

            String callBack = Base64.getEncoder().encodeToString(callbackBodyJson.getBytes());
            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
            Credential cre = new Credential();
            cre.setAccessKeyId(response.getCredentials().getAccessKeyId());
            cre.setAccessKeySecret(response.getCredentials().getAccessKeySecret());
            cre.setSecurityToken(response.getCredentials().getSecurityToken());
            cre.setCallBack(callBack);
            return cre;
        } catch (ClientException e) {
            LOGGER.error("ClientException:" + e.getMessage());
            throw e;
        }
    }

    public OSSClient getOssClient() {
        if (null == ossClient) {
            synchronized (CredentialUtil.class) {
                if (null == ossClient) {
                    ossClient = new OSSClient(ENDPONIT, ACCESS_KEY_ID,
                            ACCESS_KEY_SECRET);
                }
            }
        }
        return ossClient;

    }
}
