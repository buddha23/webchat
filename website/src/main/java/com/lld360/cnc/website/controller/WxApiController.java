package com.lld360.cnc.website.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.enums.WxMsgType;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.ThirdAccount;
import com.lld360.cnc.model.WxGzh;
import com.lld360.cnc.repository.UserDao;
import com.lld360.cnc.service.SettingService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.WxGzhService;
import com.lld360.cnc.service.WxTempMsgService;
import com.lld360.cnc.vo.WxGzhJsConfig;
import com.lld360.cnc.vo.WxGzhMessage;
import com.lld360.cnc.website.SiteController;
import com.lld360.weixin.WxOpenUtils;
import com.lld360.weixin.vo.*;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-06-29 16:32
 */
@Controller
@RequestMapping("weixin")
public class WxApiController extends SiteController {

    @Autowired
    private XmlMapper xmlMapper;

    @Autowired
    private WXBizMsgCrypt wxBizMsgCrypt;

    @Autowired
    private WxGzhService wxGzhService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private UserDao userDao;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    @ResponseBody
    @RequestMapping(value = "signature", method = RequestMethod.POST)
    public WxGzhJsConfig getWxJsSignature(@RequestParam String url) {
        return wxGzhService.getWxGzhJsConfig(url);
    }

    @ResponseBody
    @RequestMapping(value = "paySignature", method = RequestMethod.POST)
    public WxGzhJsConfig getWxJsPaySignature(@RequestParam String url) {
        return wxGzhService.getWxPayJsConfig(url);
    }

    // 验证微信服务器
    @RequestMapping(value = "api", method = RequestMethod.GET)
    public void validGet(@RequestParam String signature, // 微信加密签名
                         @RequestParam String timestamp, // 时间戳
                         @RequestParam String nonce,     // 随机数
                         @RequestParam String echostr,   // 随机字符串
                         HttpServletResponse response) throws IOException {
        String result = wxGzhService.wxServerValid(signature, timestamp, nonce) ? echostr : "invalid request";
        outText(response, result);
    }

    /**
     * 回复消息
     *
     * @param message  接收到的消息
     * @param response 系统回函
     */
    @RequestMapping(value = "api", method = RequestMethod.POST)
    public void messagePost(@RequestBody WxGzhMessage message, HttpServletResponse response) {
        WxGzhMessage back = wxGzhService.getBackMessage(configer.getWxGzhAppID(), message);

        response.setContentType(MediaType.TEXT_XML_VALUE);
        response.setCharacterEncoding("utf-8");
        String result = "";
        try {
            result = xmlMapper.writeValueAsString(back).replace(" xmlns=\"\"", "");
        } catch (JsonProcessingException e) {
            logger.warn("转换对象错误：" + e.getMessage(), e);
        }
        outText(response, result);
    }

    /// 以下为微信第三方服务平台接口 ///
    @RequestMapping(value = "gzh/entry", method = RequestMethod.GET)
    public String wxGzhEntryPageGet(Model model) {
        Map<String, Object> params = getParamsPageMap(14);
        params.put("authorizered", true);
        model.addAttribute("gzhs", wxGzhService.getWxGzhs(params).getContent());
        model.addAttribute("content", settingService.getValue(Const.CNC_WXGZH_ENTRY_PAGE_CONTENT));
        return "wGzh/entry";
    }

    /**
     * 获取并更新微信公众号的信息
     *
     * @param authorizationCode 微信公众号授权码
     * @return 系统微信公众号对象
     */
    private WxGzh getAndSaveWxGzhInfo(String authorizationCode) {
        ComponentAccessToken token = getComponentAccessToken();
        if (token != null) {
            Gzh gzh = WxOpenUtils.getAuthorizationInfo(token.getContent(), configer.getWxComponenAppId(), authorizationCode);
            if (gzh != null) {
                return wxGzhService.save(gzh);
            }
        }
        return null;
    }

    /**
     * 接收微信定时推送的component_verify_ticket和公众号授权事件
     *
     * @param response 自动注入的HttpServletResponse对象
     */
    @RequestMapping(value = "open/auth", method = RequestMethod.POST)
    public void openAuthPost(HttpServletResponse response) {
        Event event = WxOpenUtils.decryptMsg2Event(request, wxBizMsgCrypt);
        if (event != null && event.getInfoType() != null) {
            switch (event.getInfoType()) {
                case "component_verify_ticket":
                    ComponentVerifyTicket ticket = (ComponentVerifyTicket) event;
                    logger.info("接收到微信服务器定时推送消息：" + ticket);
                    context.setAttribute(Const.COMPONENT_VERIFY_TICKET, ticket.getContent());
                    break;
                case "authorized":
                    ComponentAuthorized authorized = (ComponentAuthorized) event;
                    logger.info("新的授权信息：" + authorized);
                    getAndSaveWxGzhInfo(authorized.getAuthorizationCode());
                    break;
                case "unauthorized":
                    ComponentAuthorized unauthorized = (ComponentAuthorized) event;
                    logger.info("取消授权信息：AuthorizerAppid=" + unauthorized.getAuthorizerAppid());
                    wxGzhService.setWxGzhAuthorized(unauthorized.getAuthorizerAppid(), false);
                    break;
                case "updateauthorized":
                    ComponentAuthorized updateauthorized = (ComponentAuthorized) event;
                    logger.info("更新授权信息：AuthorizerAppid=" + updateauthorized.getAuthorizerAppid());
                    getAndSaveWxGzhInfo(updateauthorized.getAuthorizationCode());
                    break;
            }
            outText(response, "success");
        } else {
            outText(response, "failed");
        }
    }

    /**
     * 获取用于请求微信平台数据的AccessToken
     *
     * @return ComponentAccessToken对象
     */
    private ComponentAccessToken getComponentAccessToken() {
        String componentVerifyTicket = getContextAttribute(Const.COMPONENT_VERIFY_TICKET);
        if (componentVerifyTicket == null) {
            return null;
        }
        ComponentAccessToken token = getContextAttribute(Const.COMPONENT_ACCESS_TOKEN);
        if (token == null || token.getExpiredTime() < Calendar.getInstance().getTimeInMillis()) {
            token = WxOpenUtils.getComponentAccessToken(configer.getWxComponenAppId(), configer.getWxComponenAppSecret(), componentVerifyTicket);
            if (token == null || token.getContent() == null) {
                return null;
            }
            logger.info("获取到微信服务器ComponentAccessToken：" + token);
            context.setAttribute(Const.COMPONENT_ACCESS_TOKEN, token);
        }
        return token;
    }

    // 接收微信公众号授权后的授权码
    @RequestMapping(value = "open/auth", method = RequestMethod.GET)
    public String openAuthGet(@RequestParam(name = "auth_code") String authCode,
                              @RequestParam(name = "expires_in") Long expiresIn) {
        logger.info(String.format("收到授权码：authCode='%s', expiresIn=%d", authCode, expiresIn));
        return "wWeixin/gzh_authorized";
    }

    // 跳转到微信公众号服务平台授权页面
    @RequestMapping(value = "open", method = RequestMethod.GET)
    public String openGet() {
        ComponentAccessToken token = getComponentAccessToken();
        if (token != null) {
            PreAuthCode authCode = WxOpenUtils.getPreAuthCode(configer.getWxComponenAppId(), token.getContent());
            if (authCode != null) {
                logger.info("获取到微信服务器PreAuthCode：" + authCode);
                String backUrl = configer.getAppUrl() + "weixin/open/auth";
                return "redirect:" + WxOpenUtils.getAuthUrl(configer.getWxComponenAppId(), authCode.getContent(), backUrl);
            }
        }
        return "redirect:/";
    }

    // 微信第三方平台得到并回复消息
    @RequestMapping(value = "open/{appid}/message", method = RequestMethod.POST)
    public void openMessagePost(@PathVariable String appid, @RequestBody EncryptMessage msg, HttpServletResponse response) {
        try {
            // logger.info("收到加密消息体：\n" + msg);
            WxGzhMessage message = WxOpenUtils.decryptEncrypeMessage(request, msg, wxBizMsgCrypt, WxGzhMessage.class);
            WxGzhMessage back;

            if (appid.equals("wx570bc396a51b8ff8")) {   // 平台上线测试号
                back = new WxGzhMessage();
                back.setMsgType(WxMsgType.TEXT.getValue());
                back.setFromUserName(message.getToUserName());
                back.setToUserName(message.getFromUserName());
                back.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);
                if ("event".equals(message.getMsgType())) {
                    logger.info("微信平台测试事件：" + message.getEvent());
                    back.setContent(message.getEvent() + "from_callback");
                } else if ("text".equals(message.getMsgType())) {
                    logger.info("微信平台测试消息：" + message.getContent());
                    if ("TESTCOMPONENT_MSG_TYPE_TEXT".equals(message.getContent())) {
                        back.setContent("TESTCOMPONENT_MSG_TYPE_TEXT_callback");
                    } else if (message.getContent().startsWith("QUERY_AUTH_CODE:")) {
                        String queryCode = message.getContent().substring(16);
                        WxGzh wxGzh = wxGzhService.getByAppId(appid);
                        if (wxGzh != null && wxGzh.getAuthorizerAccessTokenExpiredTime() > Calendar.getInstance(Locale.CHINA).getTimeInMillis()) {
                            taskExecutor.execute(() -> {
                                try {
                                    Thread.sleep(1000);
                                    String backStr = wxGzhService.sendCustomTextMessage(wxGzh.getAuthorizerAccessToken(), message.getFromUserName(), queryCode + "_from_api");
                                    logger.info("微信平台测试Api消息返回：" + backStr);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        back = null;
                    }
                } else {
                    back = null;
                }
            } else {    // 处理消息
                back = wxGzhService.getBackMessage(appid, message);
            }

            response.setContentType(MediaType.TEXT_XML_VALUE);
            response.setCharacterEncoding("utf-8");

            String result = "";
            try {
                String xml = back == null ? "" : xmlMapper.writeValueAsString(back).replace(" xmlns=\"\"", "");
                result = wxBizMsgCrypt.encryptMsg(xml, request.getParameter("timestamp"), request.getParameter("nonce"));
            } catch (JsonProcessingException e) {
                logger.warn("转换对象错误：" + e.getMessage(), e);
            }
            outText(response, result);
        } catch (IOException | AesException e) {
            e.printStackTrace();
        }
    }

    // 服务号获取用户账户余额并push消息
    @RequestMapping(value = "user/score")
    public void getUserScore() {
        try {
            // xml请求解析
            Map<String, String> requestMap = ClientUtils.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserOpenId = requestMap.get("FromUserName");
            UserDto userDto = userDao.findByWxfwOpenId(fromUserOpenId);
            wxTempMsgService.sendScoreWxMsg(fromUserOpenId, userDto);
        } catch (Exception e) {
            logger.error("获取微信请求失败");
        }
    }

    // 服务号用户关注事件
    @RequestMapping(value = "user/attention")
    public void getAttention() {
        try {
            // xml请求解析
            Map<String, String> requestMap = ClientUtils.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserOpenId = requestMap.get("FromUserName");
            wxTempMsgService.updateWxfwUserInfo(fromUserOpenId);
        } catch (Exception e) {
            logger.error("获取微信请求失败");
        }
    }
}
