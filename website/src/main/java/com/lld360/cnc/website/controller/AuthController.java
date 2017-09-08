package com.lld360.cnc.website.controller;

import com.gonvan.kaptcha.Constants;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserInvite;
import com.lld360.cnc.service.CopyWritingService;
import com.lld360.cnc.service.UserInviteService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.ValidSmsService;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("auth")
public class AuthController extends SiteController {

    private static final String RSTPSWDMBTP = "resetPwdMobileType";
    private static final String MOBILESTR = "mobile";

    @Autowired
    private UserService userService;

    @Autowired
    private ValidSmsService validSmsService;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private CopyWritingService copyWritingService;

    //web注册页面
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String registGet() {
        return "wAuth/register1";
    }

    // 进行注册
    @ResponseBody
    @Transactional
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public User registPost(@RequestParam String mobile, @RequestParam String password,
                           @RequestParam String validCode, @RequestParam(required = false) String inviteCode,
                           HttpServletResponse response) {
        UserDto user = userService.registerWithMobile(mobile, password, validCode, response);
        if (StringUtils.isNotEmpty(inviteCode) && user != null) {
            userInviteService.setInviteRelation(user, inviteCode);
        }
        request.getSession().setAttribute(Const.SS_USER, user);
        return user;
    }

    //web注册页面2
    @RequestMapping(value = "register2", method = RequestMethod.GET)
    public String regist2Get(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/auth/register";
        }
        model.addAttribute("user", user);
        return "wAuth/register2";
    }

    //web注册页面3
    @RequestMapping(value = "register3", method = RequestMethod.GET)
    public String regist3Get(Model model) {
        UserDto user = getRequiredCurrentUser();
        if (user == null) {
            return "redirect:/auth/register";
        }
        return "wAuth/register3";
    }

    //web2资料修改
    @RequestMapping(value = "register2", method = RequestMethod.POST)
    public String register2post(@RequestParam String nickname,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) String description,
                                @RequestParam String mailAddress,
                                RedirectAttributes attributes) {
        UserDto user = getRequiredCurrentUser();
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mailAddress)) {
            attributes.addFlashAttribute("err", "昵称和邮箱不能为空!");
            return "redirect:/auth/register2";
        }
        user.setNickname(nickname);
        user.setMailAddress(mailAddress);
        user.setAddress(address);
        user.setDescription(description);
        user.setPassword(null);
        userService.edit(user);

        user = userService.getUserDto(user.getId());
        request.getSession().setAttribute(Const.SS_USER, user);
        return "wAuth/register3";
    }

//    // 发送短信验证码
//    @ResponseBody
//    @RequestMapping(value = "smsCode", method = RequestMethod.GET)
//    public ResultOut smsCode(@RequestParam String mobile, @RequestParam byte type, @RequestParam(required = false) String validCode) {
//        if (type != Const.SMS_REGIST && type != Const.SMS_RESETPWD && type != Const.SMS_FORGOTTPWD && type != Const.SMS_BINDACCOUNT && type != Const.SMS_WITHDRAWALS) {
//            throw new ServerException(HttpStatus.BAD_REQUEST);
//        }
//        if (type == Const.SMS_FORGOTTPWD)
//            request.getSession().setAttribute(RSTPSWDMBTP, mobile + MOBILESTR + Const.SMS_RWD_TYPE_OK);//忘记密码验证标识
//        if (StringUtils.isNotEmpty(validCode)) {     // 仅验证短信验证码是否有效
//            validSmsService.validSmsCode(mobile, type, validCode, false);
//        } else {
//            UserDto user = userService.getByMobile(mobile);
//            if (user != null)
//                userService.validateUserState(user);
//            if (user != null && type == Const.SMS_REGIST) {  // 注册
//                throw new ServerException(HttpStatus.FORBIDDEN, M.E10007);
//            } else if (user == null && type == Const.SMS_RESETPWD) { // 找回密码
//                throw new ServerException(HttpStatus.FORBIDDEN, M.E10011);
//            } else if (user == null && type == Const.SMS_FORGOTTPWD) { // 忘记密码
//                throw new ServerException(HttpStatus.FORBIDDEN, M.E10011);
//            } else if (user == null && type == Const.SMS_WITHDRAWALS) {
//                throw new ServerException(HttpStatus.FORBIDDEN, M.E10011);
//            }
//            validSmsService.couSms(mobile, type);
//        }
//        return getResultOut(M.I10200.getCode());
//    }

    // 发送短信验证码
    @ResponseBody
    @RequestMapping(value = "smsCode", method = RequestMethod.GET)
    public ResultOut smsCode(@RequestParam String mobile, @RequestParam byte type, @RequestParam(required = false) String validCode) {
        if (StringUtils.isNotEmpty(validCode)) {     // 仅验证短信验证码是否有效
            validSmsService.validSmsCode(mobile, type, validCode, false);
        } else {
            UserDto user = userService.getByMobile(mobile);
            if (user != null) {
                userService.validateUserState(user);
                if (type == Const.SMS_REGIST) throw new ServerException(HttpStatus.FORBIDDEN, M.E10007);
                if (type == Const.SMS_FORGOTTPWD) {
                    /*忘记密码验证标识;*/
                    request.getSession().setAttribute(RSTPSWDMBTP, mobile + MOBILESTR + Const.SMS_RWD_TYPE_OK);
                }
            } else if (type != Const.SMS_REGIST) { // 找回密码
                throw new ServerException(HttpStatus.FORBIDDEN, M.E10011);
            }
            validSmsService.couSms(mobile, type);
        }
        return getResultOut(M.I10200.getCode());
    }

    @ResponseBody
    @RequestMapping(value = "smsCodeForRegist", method = RequestMethod.GET)
    public ResultOut smsCodeForRegist(@RequestParam String mobile, @RequestParam String picCaptcha) {
        String scaptcha = getSessionStringValue(Constants.KAPTCHA_SESSION_KEY);
        if (StringUtils.isEmpty(scaptcha) || !scaptcha.equalsIgnoreCase(picCaptcha)) {
            throw new ServerException(HttpStatus.FORBIDDEN, "验证码已过期,请重新输入！");
        }
        UserDto user = userService.getByMobile(mobile);
        if (user != null) {
            throw new ServerException(HttpStatus.FORBIDDEN, M.E10007);
        }
        validSmsService.couSms(mobile, Const.SMS_REGIST);
        return getResultOut(M.I10200.getCode());
    }


    //web登录
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginGet(Model model, @RequestParam(required = false) String mobile) {
        request.getSession().setAttribute("weixinState", "weixin_" + RandomStringUtils.randomAlphanumeric(12));
        request.getSession().setAttribute("qqState", "qq_" + RandomStringUtils.randomAlphanumeric(12));
        model.addAttribute("configer", configer);
        model.addAttribute(MOBILESTR, mobile);
        return "wAuth/login";
    }

    // 用户Ajax登录
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public User loginPost(@RequestParam String username, @RequestParam String password, @RequestParam String captcha, HttpServletResponse response) {
        String scaptcha = getSessionStringValue(Constants.KAPTCHA_SESSION_KEY);
        if (scaptcha == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10009);
        }
        if (!scaptcha.equalsIgnoreCase(captcha)) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10010);
        }
        return userService.doLogin(username, password, response);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletResponse response) {
        userService.doLogout(response);
        return "redirect:/auth/login";
    }

    //web忘记密码
    @RequestMapping(value = "forgotPwd", method = RequestMethod.GET)
    public String forgotPwd() {
        return "wAuth/forgot-pwd";
    }


    //忘记密码  重置密码
    @RequestMapping(value = "resetPwd", method = RequestMethod.GET)
    public String smsResetPwd(Model model, @RequestParam String mobile) {
        model.addAttribute(MOBILESTR, mobile);
        return "wAuth/reset-pwd";
    }

    //忘记密码  重置密码
    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    public String resetPwdPost(Model model, @RequestParam String password, @RequestParam String mobile) {
        if (!(mobile + MOBILESTR + Const.SMS_RWD_TYPE_OK).equals(request.getSession().getAttribute(RSTPSWDMBTP))) {
            model.addAttribute("error", "请先进行手机短信验证！");
            return "wAuth/reset-pwd";
        }
        userService.updateByMobile(mobile, password);
        model.addAttribute("messageSuccess", "密码修改成功,");
        model.addAttribute("messageLogin", "请登录");
        request.getSession().removeAttribute(RSTPSWDMBTP);
        return "wAuth/reset-pwd";
    }


    //用户是否注册
    @RequestMapping(value = "checkUser", method = RequestMethod.POST)
    public String checkUser(Model model, @RequestParam String password, @RequestParam String mobile) {
        userService.updateByMobile(mobile, password);
        model.addAttribute("messageSuccess", "密码修改成功,");
        model.addAttribute("messageLogin", "请登录");
        return "wAuth/reset-pwd";
    }

    //用户协议
    @RequestMapping(value = "agreement", method = RequestMethod.GET)
    public String agreement(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("yhxy"));
        return "wFootfile/agreement";
    }

    //版权申明
    @RequestMapping(value = "copyrightStatement", method = RequestMethod.GET)
    public String copyrightStatement(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("wzsm"));
        return "wFootfile/copyright-statement";
    }

    //联系我们
    @RequestMapping(value = "contactUs", method = RequestMethod.GET)
    public String contactUs(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("lxwm"));
        return "wFootfile/contact-us";
    }

    //积分说明
    @RequestMapping(value = "scoreExplain", method = RequestMethod.GET)
    public String scoreExplain(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("wznrb"));
        return "wFootfile/score-explain";
    }

    //版主说明
    @RequestMapping(value = "aboutModerators", method = RequestMethod.GET)
    public String aboutModerators(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("bz001"));
        return "wFootfile/about-moderator";
    }

    // 绑定账号
    @RequestMapping(value = "bindingId", method = RequestMethod.GET)
    public String bingdingId() {
        return "wAuth/bindingId";
    }

    // 查询邀请码是否存在
    @ResponseBody
    @RequestMapping(value = "inviteCodeCheck", method = RequestMethod.GET)
    public ResponseEntity inviteCodeCheck(@RequestParam String inviteCode) {
        UserInvite userInvite = userInviteService.findUserInvite(inviteCode);
        if (userInvite == null) throw new ServerException(HttpStatus.BAD_REQUEST, "邀请码错误");
        return new ResponseEntity(HttpStatus.OK);
    }
}
