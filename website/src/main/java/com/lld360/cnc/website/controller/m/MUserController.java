package com.lld360.cnc.website.controller.m;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.*;
import com.lld360.cnc.service.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.WeiXinPayConfiger;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.dto.WxAccountAccessToken;
import com.lld360.cnc.website.service.ThirdAccountService;

@Controller
@RequestMapping("m/user")
public class MUserController extends SiteController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private DocService docService;

    @Autowired
    private UserStatementService userStatementService;

    @Autowired
    private PostsService postsService;

    @Autowired
    private ThirdAccountService thirdAccountService;

    @Autowired
    private WeiXinPayConfiger weiXinPayConfiger;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private VideoService videoService;

    Logger logger = LoggerFactory.getLogger(MUserController.class);

    @RequestMapping(value = "/center", method = RequestMethod.GET)
    public String centerGet(Model model) {
        return "redirect:/m/#usercenter";
        /*
        User user = getCurrentUser();
        if (user == null) {
//            if (ClientUtils.isWechat(request)) {
//                String state = "weixin_" + RandomStringUtils.randomAlphanumeric(12);
//                request.getSession().setAttribute("weixinState", state);
//                return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + configer.getWxAccountAppid()
//                        + "&redirect_uri=" + configer.getAppUrl() + "m/&response_type=code"
//                        + "&scope=snsapi_login&state=" + state + "#wechat_redirect&connect_redirect=1";
//            }
            return "redirect:/m/auth/login";
        }
        model.addAttribute("user", user);
        if (user.getId() != null) {
            model.addAttribute("userScore", userService.findUserScore(user.getId()));
            model.addAttribute("userPoint", userService.findUserPoint(user.getId()));
            Map<String, Object> params = getParamsPageMap(5);
            params.put("userId", user.getId());
            params.put("uploader", user.getId());

            model.addAttribute("collects", docService.getCollects(params));
            model.addAttribute("downloads", docService.getDownloads(params));
            model.addAttribute("uploads", docService.getUploads(params));
            model.addAttribute("posts", postsService.getPostsByPage(params));
        }
        return "m/user-center";
        */
    }

    //前往用户充值页面
    @RequestMapping(value = "/scoreRecharge", method = RequestMethod.GET)
    public String scoreRecharge() {
        logger.info("scoreRecharge in ....");
        if (null == getSessionStringValue("WXGZ_openId")) {
            String state = "weixinScore_" + RandomStringUtils.randomAlphanumeric(12);
            request.getSession().setAttribute("weixinPayState", state);
            String location = "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + weiXinPayConfiger.getAppId()
                    + "&redirect_uri=" + configer.getAppUrl() + "m/user/weixin/auth&response_type=code"
                    + "&scope=snsapi_base&state=" + state + "#wechat_redirect";
            // logger.info("jhtest location = " + location);
            return location;
        }
        return "m/user-score-buy";
    }

    @RequestMapping(value = "/buyVip", method = RequestMethod.GET)
    public String buyVip() {
        if (null == getSessionStringValue("WXGZ_openId")) {
            String state = "weixinVip_" + RandomStringUtils.randomAlphanumeric(12);
            request.getSession().setAttribute("weixinPayState", state);
            String location = "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + weiXinPayConfiger.getAppId()
                    + "&redirect_uri=" + configer.getAppUrl() + "m/user/weixin/auth&response_type=code"
                    + "&scope=snsapi_base&state=" + state + "#wechat_redirect";
            // logger.info("jhtest location = " + location);
            return location;
        }
        return "m/user-vip-buy";
    }

    @RequestMapping(value = "videoPay", method = RequestMethod.GET)
    public String videoPay(Model model, @RequestParam Long sectionId) {
        UserDto userDto = getRequiredCurrentUser();
        if (sectionId == null) throw new ServerException(HttpStatus.BAD_REQUEST);
        if (ClientUtils.isWechat(request) && null == getSessionStringValue("WXGZ_openId")) {
            String wxstate = "weixinVip_" + RandomStringUtils.randomAlphanumeric(12);
            request.getSession().setAttribute("weixinPayState", wxstate);
            String location = "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + weiXinPayConfiger.getAppId()
                    + "&redirect_uri=" + configer.getAppUrl() + "m/user/wxPayState/" + sectionId + "&response_type=code"
                    + "&scope=snsapi_base&state=" + wxstate + "#wechat_redirect";
            return location;
        }
        VodSection vodSection = videoService.getVodSectionById(sectionId);
        VodVolumes vodVolumes = videoService.getVolumeBySectionId(sectionId);
        model.addAttribute("vodVolumes", vodVolumes);
        model.addAttribute("vodSection", vodSection);
        model.addAttribute("discount", Const.USER_MEMBER_DISCOUNT);
        return "m/user-video-pay";
    }

    @RequestMapping(value = "wxPayState/{sectionId}", method = RequestMethod.GET)
    public String request4payState(@PathVariable Long sectionId, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        logger.info("authWeixinGZ in ....");
        String weixinState = getSessionStringValue("weixinPayState");
        if (state != null && weixinState != null && state.equals(weixinState)) {
            WxAccountAccessToken token = thirdAccountService.getWxPayAccessToken(code);
            if (token != null) {
                request.getSession().setAttribute("WXGZ_openId", token.getOpenid());
            }
        }
        return "redirect:/m/user/videoPay?sectionId=" + sectionId;
    }

    //用户查看更多
    @ResponseBody
    @RequestMapping(value = "getmore", method = RequestMethod.GET)
    public List getmore(@RequestParam String getType) {
        UserDto user = getRequiredCurrentUser();
        Map<String, Object> params = getParamsPageMap(5);
        params.put("userId", user.getId());
        switch (getType) {
            case "collects":
                return docService.getCollects(params);
            case "downloads":
                return docService.getDownloads(params);
            case "uploads":
                params.put("uploader", user.getId());
                return docService.getUploads(params);
            case "posts":
                return postsService.getPostsByPage(params).getContent();
            default:
                return new ArrayList<>();
        }
    }


    // 用户消息列表
    @RequestMapping(value = "message", method = RequestMethod.GET)
    public String messageGet(Model model) {
        UserDto user = getRequiredCurrentUser();
        Map<String, Object> params = getParamsPageMap(15);
        params.put("userId", user.getId());
        model.addAttribute("messages", userMessageService.search(params));
        userMessageService.setReadByUser(user.getId());
        return "m/user-message";
    }

    //前往用户功能列表页面
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listGet(Model model) {
        UserDto currentUser = getRequiredCurrentUser();
        UserInvite userInvite = userInviteService.findInviteByUserId(currentUser.getId());
        if (userInvite != null && userInvite.getState().equals(Const.PARTER_STATE_NORMAL))
            model.addAttribute("userInvite", userInvite);
        return "m/user-list";
    }

    // 修改密码
    @RequestMapping("repwd")
    public String rePwdGet(Model model, HttpServletResponse response) {
        if (request.getMethod().equalsIgnoreCase("POST")) {
            String op = request.getParameter("op");
            if (StringUtils.isEmpty(op)) {
                model.addAttribute("error", "原密码不能为空");
                return "m/user-repwd";
            }
            String np = request.getParameter("np");
            if (StringUtils.isEmpty(np)) {
                model.addAttribute("error", "新密码不能为空");
                return "m/user-repwd";
            }
            User user = userService.getUser(getRequiredCurrentUser().getId());
            if (!user.getPassword().equals(DigestUtils.md5Hex(StringUtils.reverse(op) + user.getMobile()))) {
                model.addAttribute("error", "原密码错误");
                return "m/user-repwd";
            }
            user.setPassword(DigestUtils.md5Hex(StringUtils.reverse(np) + user.getMobile()));
            userService.updateUser(user);
            userService.doLogout(response);
            model.addAttribute("success", "修改成功");
        }
        return "m/user-repwd";
    }

    //用户积分历史
    @RequestMapping(value = "scoreHistory", method = RequestMethod.GET)
    public String scoreHistory(Model model) {
        UserDto user = getRequiredCurrentUser();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        model.addAttribute("userScore", userService.findUserScore(user.getId()));
        model.addAttribute("scoreHistories", userService.userScoreHistories(params));
        return "m/score-history";
    }


    @RequestMapping(value = "/weixin/auth", method = RequestMethod.GET)
    public String authWeixinGZ(Model model, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        logger.info("authWeixinGZ in ....");
        if (StringUtils.isEmpty(code) && StringUtils.isNotEmpty(state)) {
            //return "redirect:/m/auth/login";
        }
        String weixinState = getSessionStringValue("weixinPayState");
        if (state != null && weixinState != null && state.equals(weixinState)) {
            WxAccountAccessToken token = thirdAccountService.getWxPayAccessToken(code);
            if (token != null) {
                request.getSession().setAttribute("WXGZ_openId", token.getOpenid());
            }
        }
        return weixinState.split("_")[0].equals("weixinScore") ? "redirect:/m/user/scoreRecharge" : "redirect:/m/user/buyVip";
    }

    //用户信息填写
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public String updateUser(@RequestParam String nickname,
                             @RequestParam String mailAddress,
                             @RequestParam(required = false) String address,
                             @RequestParam(required = false) String description,
                             RedirectAttributes attributes) {
        UserDto user = getRequiredCurrentUser();
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mailAddress)) {
            attributes.addFlashAttribute("err", "昵称和邮箱不能为空!");
            return "redirect:/m/auth/userProfile";
        }
        user.setNickname(nickname);
        user.setMailAddress(mailAddress);
        user.setAddress(address);
        user.setDescription(description);
        user.setPassword(null);
        userService.edit(user);

        user = userService.getUserDto(user.getId());
        request.getSession().setAttribute(Const.SS_USER, user);
        return "redirect:/m/";
    }

    //用户充值记录
    @RequestMapping(value = "rechargeHistory", method = RequestMethod.GET)
    public String rechargeHistory(Model model) {
        UserDto user = getRequiredCurrentUser();
        Map<String, Object> params = getParamsPageMap(10);
        params.put("userId", user.getId());
        model.addAttribute("userStatements", userStatementService.getUserRecharge(params));
        return "m/recharge-history";
    }

    // 用户修改绑定更改手机号
    @RequestMapping(value = "account", method = RequestMethod.GET)
    public String accountRelevance(Model model, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        UserDto currentUser = getRequiredCurrentUser();
        ThirdAccount thirdAccount = null;
        String qqState = getSessionStringValue("qqState");
        if (state != null && qqState != null && state.equals(qqState)) {
            thirdAccount = thirdAccountService.getThirdAccountByCode(code, Const.THIRD_ACCOUNT_TYPE_QQ);
        }
        String weixinState = getSessionStringValue("weixinState");
        if (state != null && weixinState != null && state.equals(weixinState)) {
            thirdAccount = thirdAccountService.getThirdAccountByCode(code, Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        }
        if (thirdAccount != null && currentUser.getId() != null) {
            if (thirdAccountService.findByOpenidCount(thirdAccount.getOpenid())) {
                thirdAccount = thirdAccountService.findByOpenid(thirdAccount.getOpenid());
                if (thirdAccount.getUserId() != null) {
                    model.addAttribute("checkleave", "绑定失败: QQ/微信号 " + thirdAccount.getNickname() +
                            " 已有账户，<br>您需选择保留一项，作为关联账号。<br>若选择当前账号，则QQ/微信将关联到当前账号；<br>若选择原关联账号，则当前账号停用。<br>此操作不可逆，请谨慎选择：");
                    model.addAttribute("oldBindId", thirdAccount.getUserId());
                } else {
                    thirdAccount.setUserId(currentUser.getId());
                    thirdAccount.setUpdateTime(new Date());
                    thirdAccountService.update(thirdAccount);
                    model.addAttribute("msg", "绑定成功");
                }
            } else {
                thirdAccount.setUserId(currentUser.getId());
                thirdAccountService.create(thirdAccount);
            }
        }
        model.addAttribute("configer", configer);
        model.addAttribute("qqThird", thirdAccountService.getThirdAccountByUserId(currentUser.getId(), Const.THIRD_ACCOUNT_TYPE_QQ));
        model.addAttribute("weixinThird", thirdAccountService.getThirdAccountByUserId(currentUser.getId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN));
        return "m/user-account";
    }

    // 邀请用户
    @RequestMapping(value = "invite", method = RequestMethod.GET)
    public String userInvite(Model model) {
        UserDto currentUser = getRequiredCurrentUser();
        UserInvite userInvite = userInviteService.findInviteByUserId(currentUser.getId());
        if (userInvite == null || !userInvite.getState().equals(Const.PARTER_STATE_NORMAL))
            throw new ServerException(HttpStatus.BAD_REQUEST, "您没有邀请权限");
        model.addAttribute("userInvite", userInvite);
        model.addAttribute("configer", configer);
        return "m/user-invite";
    }
}
