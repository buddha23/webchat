package com.lld360.weixin;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lld360.weixin.vo.*;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信开放平台调用工具
 */
public class WxOpenUtils {
    private static Logger logger = LoggerFactory.getLogger(WxOpenUtils.class);

    private static XmlMapper xmlMapper;

    private static XmlMapper getXmlMapper() {
        if (xmlMapper == null) {
            xmlMapper = new XmlMapper();
        }
        return xmlMapper;
    }

    /**
     * 解密微信加密的消息
     *
     * @param request 微信推送的请求
     * @param crypt   微信加解密对象
     * @return 解密后的字符串，解密失败则为null
     */
    public static String decryptMsg(HttpServletRequest request, WXBizMsgCrypt crypt) throws IOException, AesException {
        InputStream in = request.getInputStream();
        int size = request.getContentLength();
        byte[] bytes = new byte[size];
        if (size == in.read(bytes)) {
            String xml = new String(bytes);
            logger.info("微信事件推送的参数：" + request.getQueryString());
            logger.info("微信事件推送的内容：\n" + xml);
            String msgSignature = request.getParameter("msg_signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            return crypt.decryptMsg(msgSignature, timestamp, nonce, xml);
        }
        return null;
    }

    /**
     * 解密并解析接收到的微信事件
     *
     * @param request 微信推送的请求
     * @param crypt   微信加解密对象
     * @return Event事件
     */
    public static Event decryptMsg2Event(HttpServletRequest request, WXBizMsgCrypt crypt) {
        try {
            String xml = decryptMsg(request, crypt);
            if (xml != null) {
                if (xml.contains("[component_verify_ticket]")) {
                    return getXmlMapper().readValue(xml, ComponentVerifyTicket.class);
                } else if (xml.contains("[authorized]") || xml.contains("[unauthorized]") || xml.contains("[updateauthorized]")) {
                    return getXmlMapper().readValue(xml, ComponentAuthorized.class);
                } else {
                    logger.warn("未处理的微信事件：\n" + xml);
                }
            }
        } catch (IOException | AesException e) {
            logger.warn("微信事件解析失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密微信加密的消息内容
     *
     * @param request 消息请求
     * @param message 加密消息对象
     * @param crypt   加解密组件
     * @param clz     结果对象类
     * @param <T>     结果对象
     * @return 解密后的结果对象
     * @throws IOException  读取或解析XML失败
     * @throws AesException 解密失败
     */
    public static <T> T decryptEncrypeMessage(HttpServletRequest request, EncryptMessage message, WXBizMsgCrypt crypt, Class<T> clz) throws IOException, AesException {
        Assert.notNull(message, "消息加密对象不能为空！");
        Assert.hasText(message.getContent(), "消息加密内容不能为空！");
        // logger.info("微信事件推送的参数：" + request.getQueryString());
        // logger.info("微信事件推送的密文：\n" + message.getContent());
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String xml = crypt.decryptString(msgSignature, timestamp, nonce, message.getContent());
        return getXmlMapper().readValue(xml, clz);
    }

    /**
     * 检查从微信服务器获取的对象是否成功
     *
     * @param err 从微信服务器获取的对象
     * @return 是否成功
     */
    private static boolean checkSuccess(ErrCode err) {
        if (err != null) {
            if (err.getErrcode() == null || err.getErrcode() == 0) {
                return true;
            }
            logger.warn(String.format("获取%s错误消息：code=%d, message='%s'", err.getClass().getName(), err.getErrcode(), err.getErrmsg()));
        }
        return false;
    }

    /**
     * 创建一个以JSON对象为POST请求参数的请求实体
     *
     * @param url         请求地址
     * @param requestBody 请求对象
     * @param t           返回结果对象
     * @param <T>         返回结果对象类型
     * @return 返回结果对象
     */
    public static <T> T postForJsonObject(String url, Object requestBody, Class<T> t) {
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
        return template.postForObject(url, entity, t);
    }

    /**
     * 根据 ComponentVerifyTicket 从微信服务器获取 ComponentAccessToken
     *
     * @param componentAppid        平台的AppId
     * @param componentAppsecret    平台的ComponentAppsecret
     * @param componentVerifyTicket 微信定时推送的ComponentVerifyTicket
     * @return ComponentAccessToken对象，如果失败的话则为null
     */
    public static ComponentAccessToken getComponentAccessToken(String componentAppid,
                                                               String componentAppsecret,
                                                               String componentVerifyTicket) {
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";

        Map<String, String> params = new HashMap<>();
        params.put("component_appid", componentAppid);
        params.put("component_appsecret", componentAppsecret);
        params.put("component_verify_ticket", componentVerifyTicket);

        try {
            ComponentAccessToken accessToken = postForJsonObject(url, params, ComponentAccessToken.class);
            return checkSuccess(accessToken) ? accessToken : null;
        } catch (RestClientException e) {
            logger.warn("获取ComponentAccessToken失败", e);
        }
        return null;
    }

    /**
     * 根据 ComponentAccessToken 从微信服务器获取 PreAuthCode 对象
     *
     * @param componentAppid       平台的AppId
     * @param componentAccessToken 从微信服务器获取的ComponentAccessToken值
     * @return PreAuthCode对象，如果失败的话则为null
     */
    public static PreAuthCode getPreAuthCode(String componentAppid,
                                             String componentAccessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="
                + componentAccessToken;

        Map<String, String> params = new HashMap<>();
        params.put("component_appid", componentAppid);
        try {
            PreAuthCode authCode = postForJsonObject(url, params, PreAuthCode.class);
            return checkSuccess(authCode) ? authCode : null;
        } catch (RestClientException e) {
            logger.warn("获取PreAuthCode失败", e);
        }
        return null;
    }

    /**
     * 获取微信公众号服务平台授权页面地址
     *
     * @param componentAppid 平台AppId
     * @param preAuthCode    预授权码
     * @param backUrl        回调URL
     * @return 微信公众号服务平台授权页面地址
     */
    public static String getAuthUrl(String componentAppid,
                                    String preAuthCode,
                                    String backUrl) {
        String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=%s&pre_auth_code=%s&redirect_uri=%s";
        return String.format(url, componentAppid, preAuthCode, backUrl);
    }

    /**
     * 使用授权码换取公众号的接口调用凭据、授权信息和基本信息
     *
     * @param componentAccessToken 微信平台的AccessToken
     * @param componentAppid       微信平台的AppId
     * @param authorizationCode    微信公众号对平台的授权码
     * @return 微信公众号的接口调用凭据、授权信息和基本信息
     */
    public static Gzh getAuthorizationInfo(String componentAccessToken, String componentAppid, String authorizationCode) {
        Assert.hasText(componentAccessToken);
        // 请求授权信息
        String queryUrl = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="
                + componentAccessToken;
        Map<String, String> params = new HashMap<>();
        params.put("component_appid", componentAppid);
        params.put("authorization_code", authorizationCode);

        String authJson = postForJsonObject(queryUrl, params, String.class);
        logger.info("请求到的授权信息：\n" + authJson);
        if (authJson.contains("errcode")) {
            return null;
        }

        JSONObject authInfo = new JSONObject(authJson).getJSONObject("authorization_info");

        Gzh gzh = new Gzh();
        gzh.setAppId(authInfo.getString("authorizer_appid"));
        gzh.setAccessToken(authInfo.getString("authorizer_access_token"));
        gzh.setExpiresIn(authInfo.getInt("expires_in"));
        gzh.setRefreshToken(authInfo.getString("authorizer_refresh_token"));

        JSONArray funcs = authInfo.getJSONArray("func_info");
        for (int i = 0; i < funcs.length(); i++) {
            int id = funcs.getJSONObject(i).getJSONObject("funcscope_category").getInt("id");
            gzh.addFuncInfo(id);
        }

        // 请求基本信息
        String infoUrl = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token="
                + componentAccessToken;

        params.remove("authorization_code");
        params.put("authorizer_appid", gzh.getAppId());

        String infoJson = postForJsonObject(infoUrl, params, String.class);
        try {
            infoJson = new String(infoJson.getBytes("ISO8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("字符编码错误：" + e.getMessage());
        }

        logger.info("请求到公众号信息：\n" + infoJson);
        if (!infoJson.contains("errcode")) {
            JSONObject info = new JSONObject(infoJson).getJSONObject("authorizer_info");
            gzh.setNickName(info.getString("nick_name"));
            if (info.getString("head_img") != null) {
                gzh.setHeadImage(info.getString("head_img"));
            } else {
                gzh.setHeadImage("http://www.d6sk.com/assets/images/logo.jpg");
            }
            gzh.setServiceType(info.getJSONObject("service_type_info").getInt("id"));
            gzh.setVerifyType(info.getJSONObject("verify_type_info").getInt("id"));
            gzh.setUserName(info.getString("user_name"));
            gzh.setAlias(info.getString("alias"));
            gzh.setQrcodeUrl(info.getString("qrcode_url"));

            JSONObject bussInfo = info.getJSONObject("business_info");
            gzh.setOpenStore(bussInfo.getInt("open_store") == 1);
            gzh.setOpenScan(bussInfo.getInt("open_scan") == 1);
            gzh.setOpenPay(bussInfo.getInt("open_pay") == 1);
            gzh.setOpenCard(bussInfo.getInt("open_card") == 1);
            gzh.setOpenShake(bussInfo.getInt("open_shake") == 1);
        }

        return gzh;
    }

    /**
     * 刷新公众号的AccessToken
     *
     * @param componentAccessToken 微信平台的AccessToken
     * @param componentAppid       微信平台的AppId
     * @param appId                微信公众号的AppId
     * @param refreshToken         微信公众号的刷新令牌
     * @return 只包含公众号AppId、令牌、令牌过期时间、令牌刷新令牌属性的公众号信息对象<br>如果失败则返回null
     */
    public static Gzh refreshAccessToken(String componentAccessToken, String componentAppid, String appId, String refreshToken) {
        String url = "https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=" + componentAccessToken;
        Map<String, String> params = new HashMap<>();
        params.put("component_appid", componentAppid);
        params.put("authorizer_appid", appId);
        params.put("authorizer_refresh_token", refreshToken);
        String json = postForJsonObject(url, params, String.class);
        if (json != null && !json.contains("errcode")) {
            JSONObject object = new JSONObject(json);

            Gzh gzh = new Gzh();
            gzh.setAppId(appId);
            gzh.setAccessToken(object.getString("authorizer_access_token"));
            gzh.setExpiresIn(object.getInt("expires_in"));
            gzh.setRefreshToken(object.getString("authorizer_refresh_token"));

            return gzh;
        } else {
            logger.warn("刷新公众号AccessToken失败：" + json);
        }
        return null;
    }
}
