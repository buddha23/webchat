package com.lld360.cnc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.WeiXinPayConfiger;
import com.lld360.cnc.core.enums.WxMsgType;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.Setting;
import com.lld360.cnc.model.WxGzh;
import com.lld360.cnc.model.WxQa;
import com.lld360.cnc.repository.ArticleDao;
import com.lld360.cnc.repository.DocDao;
import com.lld360.cnc.repository.WxGzhDao;
import com.lld360.cnc.repository.WxQaDao;
import com.lld360.cnc.vo.*;
import com.lld360.weixin.WxOpenUtils;
import com.lld360.weixin.vo.Gzh;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: dhc
 * Date: 2016-06-30 09:39
 */
@Service
public class WxGzhService extends BaseService {
    private static final String WXGZH_HOST = "https://api.weixin.qq.com";
    private static final String WXGZH_ACCESS_TOKEN_PATH = "/cgi-bin/token";
    private static final String WXGZH_UPLOAD_MEDIA_PATH = "/cgi-bin/media/upload";

    @Autowired
    private Configer configer;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private DocDao docDao;

    @Autowired
    private WxGzhDao wxGzhDao;

    @Autowired
    private WxQaDao wxQaDao;

    @Autowired
    private SearchWordsService searchWordsService;

    @Autowired
    private SynonymService synonymService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private DocService docService;

    @Autowired
    private WeiXinPayConfiger weiXinPayConfiger;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    /**
     * 验证消息的确来自微信服务器
     *
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return 是否来自微信服务器
     */
    public boolean wxServerValid(String signature, String timestamp, String nonce) {
        String token = configer.getWxGzhToken();
        String[] tmp = {token, timestamp, nonce};
        Arrays.sort(tmp);
        String tmpStr = StringUtils.join(tmp);
        String sha1 = DigestUtils.sha1Hex(tmpStr);
        return sha1.equals(signature);
    }

    /**
     * 微信公众号JS-SDK使用权限签名算法
     *
     * @param url 验证URL
     * @return 配置对象
     */
    public WxGzhJsConfig getWxGzhJsConfig(String url) {
        String ticket = getWxGzhJsApiTicket().getTicket();
        if (ticket != null) {
            String noncestr = RandomStringUtils.randomAlphanumeric(16);
            long timestamp = Calendar.getInstance().getTimeInMillis() / 1000;
            String[] tmp = {"jsapi_ticket=" + ticket, "noncestr=" + noncestr, "timestamp=" + timestamp, "url=" + url};
            Arrays.sort(tmp);
            String tmpStr = StringUtils.join(tmp, '&');

            WxGzhJsConfig config = new WxGzhJsConfig();
            config.setAppId(configer.getWxGzhAppID());
            config.setNoncestr(noncestr);
            config.setTimestamp(timestamp);
            config.setUrl(url);
            config.setSignature(DigestUtils.sha1Hex(tmpStr));
            return config;
        }
        return null;
    }

    /**
     * 微信公众号JS-SDK使用权限签名算法
     *
     * @param url 验证URL
     * @return 配置对象
     */
    public WxGzhJsConfig getWxPayJsConfig(String url) {
        String ticket = getWxPayJsApiTicket().getTicket();
        if (ticket != null) {
            String noncestr = RandomStringUtils.randomAlphanumeric(16);
            long timestamp = Calendar.getInstance().getTimeInMillis() / 1000;
            String[] tmp = {"jsapi_ticket=" + ticket, "noncestr=" + noncestr, "timestamp=" + timestamp, "url=" + url};
            Arrays.sort(tmp);
            String tmpStr = StringUtils.join(tmp, '&');

            WxGzhJsConfig config = new WxGzhJsConfig();
            config.setAppId(weiXinPayConfiger.getAppId());
            config.setNoncestr(noncestr);
            config.setTimestamp(timestamp);
            config.setUrl(url);
            config.setSignature(DigestUtils.sha1Hex(tmpStr));
            return config;
        }
        return null;
    }

    /**
     * 获取微信公众号的AccessToken
     *
     * @return WxGzhAccessToken
     */
    public WxGzhAccessToken getWxGzhAccessToken() {
        WxGzhAccessToken accessToken = getContextAttribute(Const.CTX_KEY_WXGZH_ACCESS_TOKEN);
        if (accessToken == null || accessToken.isExpired()) {
            String url = WXGZH_HOST + WXGZH_ACCESS_TOKEN_PATH
                    + "?grant_type=client_credential&appid=" + configer.getWxGzhAppID()
                    + "&secret=" + configer.getWxGzhAppSecret();

            RestTemplate temp = new RestTemplate();
            try {
                accessToken = temp.getForObject(url, WxGzhAccessToken.class);
                if (accessToken.getAccessToken() != null) {
                    context.setAttribute(Const.CTX_KEY_WXGZH_ACCESS_TOKEN, accessToken);
                }
            } catch (RestClientException e) {
                logger.warn("获取微信AccessToken失败", e);
            }
        }
        return accessToken;
    }

    /**
     * 获取微信公众号的AccessToken
     *
     * @return WxGzhAccessToken
     */
    public WxGzhAccessToken getWxPayAccessToken() {
        WxGzhAccessToken accessToken = getContextAttribute(Const.CTX_KEY_WXPAY_ACCESS_TOKEN);
        if (accessToken == null || accessToken.isExpired()) {
            String url = WXGZH_HOST + WXGZH_ACCESS_TOKEN_PATH
                    + "?grant_type=client_credential&appid=" + weiXinPayConfiger.getAppId()
                    + "&secret=" + weiXinPayConfiger.getAppSecret();

            RestTemplate temp = new RestTemplate();
            try {
                accessToken = temp.getForObject(url, WxGzhAccessToken.class);
                if (accessToken.getAccessToken() != null) {
                    context.setAttribute(Const.CTX_KEY_WXPAY_ACCESS_TOKEN, accessToken);
                }
            } catch (RestClientException e) {
                logger.warn("获取微信AccessToken失败", e);
            }
        }
        return accessToken;
    }


    /**
     * 获取微信公众号的JsApiTicket
     *
     * @return WxGzhJsApiTicket
     */
    public WxGzhJsApiTicket getWxGzhJsApiTicket() {
        WxGzhJsApiTicket ticket = getContextAttribute(Const.CTX_KEY_WXGZH_JSAPI_TICKET);
        if (ticket == null || ticket.isExpired()) {
            String url = WXGZH_HOST + "/cgi-bin/ticket/getticket?type=jsapi&access_token=" + getWxGzhAccessToken().getAccessToken();
            RestTemplate temp = new RestTemplate();
            try {
                ticket = temp.getForObject(url, WxGzhJsApiTicket.class);
                if (ticket.getTicket() != null) {
                    context.setAttribute(Const.CTX_KEY_WXGZH_JSAPI_TICKET, ticket);
                }
            } catch (RestClientException e) {
                logger.warn("获取微信JsApiTicket失败", e);
            }
        }
        return ticket;
    }

    /**
     * 获取微信公众号的JsApiTicket
     *
     * @return WxGzhJsApiTicket
     */
    public WxGzhJsApiTicket getWxPayJsApiTicket() {
        WxGzhJsApiTicket ticket = getContextAttribute(Const.CTX_KEY_WXPAY_JSAPI_TICKET);
        if (ticket == null || ticket.isExpired()) {
            String url = WXGZH_HOST + "/cgi-bin/ticket/getticket?type=jsapi&access_token=" + getWxPayAccessToken().getAccessToken();
            RestTemplate temp = new RestTemplate();
            try {
                ticket = temp.getForObject(url, WxGzhJsApiTicket.class);
                if (ticket.getTicket() != null) {
                    context.setAttribute(Const.CTX_KEY_WXPAY_JSAPI_TICKET, ticket);
                }
            } catch (RestClientException e) {
                logger.warn("获取微信JsApiTicket失败", e);
            }
        }
        return ticket;
    }

    /**
     * 上传临时素材
     *
     * @param file 素材文件
     * @param type 素材类型
     * @return 上传结果，失败则为null
     */
    public WxGzhUploadMediaBack uploadMedia(File file, WxMsgType type) {
        WxGzhAccessToken token = getWxGzhAccessToken();
        if (token != null) {
            String uploadUrl = WXGZH_HOST + WXGZH_UPLOAD_MEDIA_PATH + "?access_token=" + token.getAccessToken() + "&type=" + type.getValue();
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("media", new FileSystemResource(file));
            RestTemplate temp = new RestTemplate();
            String result = temp.postForObject(uploadUrl, param, String.class);
            try {
                return objectMapper.readValue(result, WxGzhUploadMediaBack.class);
            } catch (IOException e) {
                logger.warn("转换上传素材返回结果错误：" + e.getMessage() + "\n\t返回结果：" + result, e);
            }
        }
        return null;
    }

    /**
     * 处理所有微信消息并返回消息
     *
     * @param message 收到的消息
     * @return 回复消息
     */
    public WxGzhMessage getBackMessage(String appId, WxGzhMessage message) {
        WxGzhMessage back = new WxGzhMessage();
        back.setMsgType(WxMsgType.TEXT.getValue());
        back.setFromUserName(message.getToUserName());
        back.setToUserName(message.getFromUserName());
        back.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);

        String msg = message.getContent();

        if ("event".equals(message.getMsgType()))
            logger.info(String.format("微信事件: [%s]%s -> %s {%s}", message.getEvent(), message.getFromUserName(),
                    message.getToUserName(), message.getEventKey()));
        else if ("text".equals(message.getMsgType()))
            logger.info(String.format("微信消息: %s -> %s {%s}", message.getFromUserName(),
                    message.getToUserName(), message.getContent()));

        switch (message.getMsgType()) {
            case "text":
                // 对于大牛数控自己的屏蔽词不回复
                if (configer.getWxSelfGzhs().contains(appId) && wxQaDao.findInSetByQ("_NO_ANSWER_", msg)) {
                    return null;
                } else {
                    WxQa qa = wxQaDao.findByQ(msg);
                    if (qa != null) {
                        back.setContent(qa.getA());
                        wxQaDao.addVolume(qa.getId());
                    } else {
                        setArticles(msg, back);
                    }
                    searchWordsService.updateOrCreate(msg);
                }
                break;
            case "event":
                if (message.getEvent().equals("subscribe")) {
                    // 更新服务号用户表
                    wxTempMsgService.updateWxfwUserInfo(message.getFromUserName());
                    WxQa wxQa = wxQaDao.findByQ("_SUBSCRIBE_");
                    if (wxQa != null) {
                        back.setContent(wxQa.getA());
                        wxQaDao.addVolume(wxQa.getId());
                    } else {
                        back.setContent("");
                    }
                } else if (message.getEvent().equals("CLICK")) {
                    WxQa wxQa = wxQaDao.findByQ(message.getEventKey());
                    if (wxQa != null) {
                        back.setContent(wxQa.getA());
                        wxQaDao.addVolume(wxQa.getId());
                    } else {
                        back.setContent("");
                    }
                }
                break;
            default:
                back.setContent("");
        }
        return back;
    }

    /**
     * 发送客服消息给指定用户
     *
     * @param accessToken 授权码
     * @param toUser      接收者OpenId
     * @param message     消息内容
     * @return 消息返回的文本信息
     */
    public String sendCustomTextMessage(String accessToken, String toUser, String message) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;

        RestTemplate template = new RestTemplate();
        String body = "{\"touser\":\"" + toUser + "\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + message + "\"}}";

        return template.postForObject(url, body, String.class);
    }

    /**
     * 发送CNC新闻内容
     *
     * @param key  搜索关键字
     * @param back 返回消息
     */
    public void setArticles(String key, WxGzhMessage back) {
        back.setMsgType(WxMsgType.NEWS.getValue());

        String regexKey = synonymService.getSynonymsRegexp(key);

        Map<String, Object> params = new HashMap<>();
        params.put("content", key);
        Page<Doc> docPage = docService.searchWeb(params);
        List<Doc> docs = docPage.getContent();

        List<WxGzhMessage.Article> tmpl = getWxGzhMessageArticleSetting();
        try {
            key = URLEncoder.encode(key, "utf-8");
            back.addArticle(String.format(tmpl.get(0).getTitle(), docPage.getTotalElements()), tmpl.get(0).getDescription(),
                    tmpl.get(0).getPicUrl(), getFullUrl("m/doc?content=" + key));
            back.addArticle(String.format(tmpl.get(1).getTitle(), articleDao.countByKeyWord(regexKey)), tmpl.get(1).getDescription(),
                    tmpl.get(1).getPicUrl(), getFullUrl("m/article?keyword=" + key));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (docs.size() > 0) {
            for (int i = 0; i < 2 && i < docs.size(); i++) {
                back.addArticle(docs.get(i).getTitle(), docs.get(i).getSummary(), tmpl.get(i + 2).getPicUrl(), getFullUrl("m/doc/" + docs.get(i).getId()));
            }
        }
        back.addArticle(tmpl.get(4).getTitle(), tmpl.get(4).getDescription(), tmpl.get(4).getPicUrl(), getFullUrl("m/article/help"));
    }

    // 获取自定义菜单
    public List<WxGzhButton> getWxGzhButtons() {
        if (!configer.getEnv().equals("prod")) {
            return new ArrayList<>();
        }
        String url = WXGZH_HOST + "/cgi-bin/menu/get?access_token=" + getWxGzhAccessToken().getAccessToken();
        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        try {
            json = new String(json.getBytes("ISO8859-1"), "utf-8");
            if (!json.contains("errcode")) {
                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.getJSONObject("menu").getJSONArray("button").toString();
                return objectMapper.readValue(json, new TypeReference<List<WxGzhButton>>() {
                });
            } else {
                logger.warn("获取微信按钮失败：" + json);
            }
        } catch (IOException e) {
            System.out.println("JSON = " + json);
            logger.warn("获取自定义菜单失败", e);
        }
        return new ArrayList<>();
    }

    // 设置自定义菜单
    public WxApiBack setWxGzhButtons(List<WxGzhButton> buttons) {
        if (!configer.getEnv().equals("prod")) {
            return new WxApiBack(99999, "不支持非正式环境设置微信菜单！");
        }
        String url = WXGZH_HOST + "/cgi-bin/menu/create?access_token=" + getWxGzhAccessToken().getAccessToken();
        Map<String, List<WxGzhButton>> menu = new HashMap<>();
        menu.put("button", buttons);

        return WxOpenUtils.postForJsonObject(url, menu, WxApiBack.class);
    }

    // 获取公众号回复消息默认配置
    public List<WxGzhMessage.Article> getWxGzhMessageArticleSetting() {
        String json = settingService.getValue(Const.SETTING_CNC_WXGZH_CXHFYS);
        List<WxGzhMessage.Article> articles = null;
        if (!StringUtils.isEmpty(json)) {
            try {
                articles = objectMapper.readValue(json, new TypeReference<List<WxGzhMessage.Article>>() {
                });
            } catch (IOException e) {
                settingService.update(new Setting(Const.SETTING_CNC_ARTICLE_LAST_ID, null));
            }
        }
        if (articles == null || articles.size() != 5) {
            articles = new ArrayList<>();
            articles.add(new WxGzhMessage.Article("找到 %d 条相关资料", "点击查看详细列表内容", getFullUrl("assets/img/wx/main.jpg"), null));
            articles.add(new WxGzhMessage.Article("找到 %d 个相关文档", "点击查看详细列表内容", getFullUrl("assets/img/wx/doc_0.jpg"), null));
            articles.add(new WxGzhMessage.Article("第一条资料数据", null, getFullUrl("assets/img/wx/doc_1.jpg"), null));
            articles.add(new WxGzhMessage.Article("第二条资料数据", null, getFullUrl("assets/img/wx/doc_2.jpg"), null));
            articles.add(new WxGzhMessage.Article("使用帮助", "查询帮助说明", getFullUrl("assets/img/wx/help.png"), getFullUrl("m/article/help")));
        }
        return articles;
    }

    // 设置公众号回复消息默认配置
    public List<WxGzhMessage.Article> setWxGzhMessageArticleSetting(List<WxGzhMessage.Article> articles) {
        if (articles.isEmpty() || articles.size() != 5) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "不合格的文章列表设置");
        } else {
            try {
                String json = objectMapper.writeValueAsString(articles);
                Setting setting = new Setting(Const.SETTING_CNC_WXGZH_CXHFYS, json);
                setting.setLable("微信公众号回复文章列表设置");
                settingService.save(setting);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }

    // 设置回复文章列表图片
    public List<WxGzhMessage.Article> setWxGzhMessageArticleImage(int idx, MultipartFile file) {
        List<WxGzhMessage.Article> articles = getWxGzhMessageArticleSetting();
        if (idx < 0 || idx >= articles.size()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "文章列表索引错误：" + idx);
        }
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!fileType.matches("jpg|jpeg|png|bmp|gif")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "不支持的文件类型：" + fileType);
        }
        String filePath = "wxArticleIcon/" + idx + "_" + RandomStringUtils.randomAlphanumeric(6) + "." + fileType;
        File imgFile = new File(configer.getFileBasePath() + filePath);
        if (imgFile.getParentFile().exists() || imgFile.getParentFile().mkdirs()) {
            try {
                file.transferTo(imgFile);
            } catch (IOException e) {
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, "上传失败：" + e.getMessage());
            }
        }
        articles.get(idx).setPicUrl(getFullUrl(Const.FILE_PREFIX_PATH + filePath));
        return setWxGzhMessageArticleSetting(articles);
    }

    // ********************* 功能测试 ***********************

    // 搜索图片
    private File searchImage(String key) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String searchUrl = "http://cn.bing.com/images";
            if (StringUtils.isNotEmpty(key)) {
                searchUrl = "http://cn.bing.com/images/search?q=" + URLEncoder.encode(key, "utf-8");
            }
            HttpGet httpGet = new HttpGet(searchUrl);
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            ResponseHandler<String> handler = new BasicResponseHandler();
            String result = httpclient.execute(httpGet, handler);
            Pattern pattern;
            if (StringUtils.isEmpty(key)) {
                pattern = Pattern.compile("class=\"ilp_[a-z]{2}\" src=\"([^\"]+)\"");
            } else {
                pattern = Pattern.compile("(http://tse\\d\\.mm\\.bing\\.net/th\\?id=OIP\\.\\w+&amp;w=\\d+&amp;h=\\d+&amp;c=\\d+&amp;rs=\\d+&amp;qlt=\\d+&amp;o=\\d+&amp;pid=\\d+\\.\\d+)");
            }
            Matcher matcher = pattern.matcher(result);
            List<String> imgs = new ArrayList<>();
            while (matcher.find()) {
                imgs.add(matcher.group(1));
            }
            if (!imgs.isEmpty()) {
                int index = RandomUtils.nextInt(0, imgs.size());
                RestTemplate template = new RestTemplate();
                byte[] file = template.getForObject(imgs.get(index), byte[].class);
                if (file != null && file.length > 0) {
                    File f = new File(configer.getFileBasePath() + Const.FILE_TEMP_PATH + Calendar.getInstance().getTimeInMillis() + ".jpg");
                    FileUtils.writeByteArrayToFile(f, file, false);
                    return f;
                }
            }
        } catch (IOException e) {
            logger.warn("下载图片异常：" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 发送图片类型，测试上传临时素材
     *
     * @param key  关键字
     * @param back 回复消息对象
     */
    private void sendImage(String key, WxGzhMessage back) {
        File file = searchImage(key);
        if (file != null) {
            WxGzhUploadMediaBack mediaBack = uploadMedia(file, WxMsgType.IMAGE);
            if (mediaBack.getMediaId() != null) {
                back.setMsgType(WxMsgType.IMAGE.getValue());
                back.setImage(mediaBack.getMediaId());
            } else {
                back.setContent("[图片展示失败]");
            }
        } else {
            back.setContent("[没有相关图片]");
        }
    }

    // ********************* 微信第三方平台调用 ***********************

    // 保存微信公众号信息
    public WxGzh save(Gzh gzh) {
        Assert.hasText(gzh.getAppId());
        Date now = Calendar.getInstance(Locale.CHINA).getTime();
        WxGzh wxGzh = wxGzhDao.findByAppId(gzh.getAppId());
        if (wxGzh == null) {
            wxGzh = new WxGzh();
            wxGzh.setAppid(gzh.getAppId());
            wxGzh.setCreateTime(now);
        }
        wxGzh.setName(gzh.getNickName());
        wxGzh.setIcon(gzh.getHeadImage());
        wxGzh.setQrcodeUrl(gzh.getQrcodeUrl());
        wxGzh.setUserName(gzh.getUserName());
        wxGzh.setAlias(gzh.getAlias());
        //wxGzh.setAppsecret(); 平台帐号无法获取
        //wxGzh.setEncodingAesKey();
        wxGzh.setAuthorizerAccessToken(gzh.getAccessToken());
        wxGzh.setAuthorizerAccessTokenExpiredTime(gzh.getExpiredTime());
        wxGzh.setAuthorizerRefreshToken(gzh.getRefreshToken());
        wxGzh.setPermissionSet(gzh.getFuncInfos() == null ? null : StringUtils.join(gzh.getFuncInfos(), ","));
        wxGzh.setServiceType(gzh.getServiceType().byteValue());
        wxGzh.setVerifyType(gzh.getVerifyType().byteValue());
        wxGzh.setOpenStore(gzh.getOpenStore());
        wxGzh.setOpenScan(gzh.getOpenScan());
        wxGzh.setOpenPay(gzh.getOpenPay());
        wxGzh.setOpenCard(gzh.getOpenCard());
        wxGzh.setOpenShake(gzh.getOpenShake());
        wxGzh.setAuthorizered(true);
        wxGzh.setUpdateTime(now);
        if (wxGzh.getId() == null) {
            wxGzhDao.create(wxGzh);
        } else {
            wxGzhDao.update(wxGzh);
        }
        return wxGzh;
    }

    // 设置微信公众号授权状态
    public int setWxGzhAuthorized(String appId, boolean authorized) {
        return wxGzhDao.updateAuthorized(appId, authorized ? 1 : 0);
    }

    // 获取授权的微信公众号列表
    public Page<WxGzh> getWxGzhs(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long total = wxGzhDao.count(params);
        List<WxGzh> wxGzhList = total > 0 ? wxGzhDao.search(params) : new ArrayList<>();
        return new PageImpl<>(wxGzhList, pageable, total);
    }

    // 根据AppId获取微信公众号
    public WxGzh getByAppId(String appId) {
        return wxGzhDao.findByAppId(appId);
    }
}
