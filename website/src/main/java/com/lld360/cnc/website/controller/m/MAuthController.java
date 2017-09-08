package com.lld360.cnc.website.controller.m;

import com.gonvan.kaptcha.Constants;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.CopyWritingService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("m/auth")
public class MAuthController extends SiteController {

    @Autowired
    private UserService userService;

    @Autowired
    Configer configer;

    @Autowired
    private CopyWritingService copyWritingService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(Model model) {
        if (getCurrentUser() != null) {
            return "redirect:/m/";
        }
        model.addAttribute("configer", configer);
        request.getSession().setAttribute("weixinState", "weixin_" + RandomStringUtils.randomAlphanumeric(12));
        request.getSession().setAttribute("qqState", "qq_" + RandomStringUtils.randomAlphanumeric(12));
        return "m/login";
    }

    // 用户Ajax登录
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity loginPost(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String captcha, @RequestParam Integer loginTimes, HttpServletResponse response) {
        String scaptcha = getSessionStringValue(Constants.KAPTCHA_SESSION_KEY);
        if (loginTimes == null && scaptcha == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10009);
        } else if (loginTimes != null && loginTimes > 3) {
            if (scaptcha == null || !scaptcha.equalsIgnoreCase(captcha))
                throw new ServerException(HttpStatus.BAD_REQUEST, M.E10010);
        }
        userService.doLogin(username, password, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletResponse response) {
        userService.doLogout(response);
        return "redirect:/m/auth/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String toRegisterPage() {
        return "m/register";
    }

    //用户协议
    @RequestMapping(value = "/agreement", method = RequestMethod.GET)
    public String agreement(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("yhxy"));
        return "m/agreement";
    }

    //版权申明
    @RequestMapping(value = "copyrightStatement", method = RequestMethod.GET)
    public String copyrightStatement(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("wzsm"));
        return "m/copyright-statement";
    }

    //积分说明
    @RequestMapping(value = "scoreExplain", method = RequestMethod.GET)
    public String scoreExplain(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("wznrb"));
        return "m/score-explain";
    }

    //联系我们
    @RequestMapping(value = "contactUs", method = RequestMethod.GET)
    public String contactUs(Model model) {
        model.addAttribute("cw", copyWritingService.getCopyWriteByKey("lxwm"));
        return "m/contact-us";
    }

    //注册填写信息
    @RequestMapping(value = "/userprofile", method = RequestMethod.GET)
    public String userprofile() {
        return "m/user-profile";
    }

    //忘记密码
    @RequestMapping(value = "forgetPwd", method = RequestMethod.GET)
    public String forgetPwd() {
        return "m/user-forgetPwd";
    }

    //忘记密码POST
    @RequestMapping(value = "resetForgetPwd", method = RequestMethod.POST)
    public String resetForgetPwd(RedirectAttributes attributes, @RequestParam String password, @RequestParam String mobile, @RequestParam String validCode) {
        if (!(mobile + "mobile" + Const.SMS_RWD_TYPE_OK).equals(request.getSession().getAttribute("resetPwdMobileType"))) {
            attributes.addFlashAttribute("error", "请先进行手机短信验证！");
            return "m/user-forgetPwd";
        }
        userService.updateByMobile(mobile, password);
        attributes.addFlashAttribute("messageSuccess", "密码修改成功,");
        attributes.addFlashAttribute("messageLogin", "请登录");
        request.getSession().removeAttribute("resetPwdMobileType");
        return "redirect:/m/auth/login";
    }

    // 绑定账号
    @RequestMapping(value = "bindingId", method = RequestMethod.GET)
    public String bingdingId() {
        return "m/bindingId";
    }

    /*添加扫码 coockies
    * params: cookyName(cooky名称) workTime(有效期/分钟)
    * retrun: HttpServletResponse response
    * */
    @ResponseBody
    @RequestMapping(value = "addCookies", method = RequestMethod.POST)
    public ResponseEntity addCoockies(@RequestParam String cookyName, @RequestParam int workTime, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookyName, StringUtils.reverse(cookyName) + Long.toString(workTime + 10000, Character.MAX_RADIX));
        cookie.setPath("/");
        cookie.setMaxAge(workTime * 60);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "getCookies", method = RequestMethod.POST)
    public ResponseEntity getCoockies(HttpServletRequest request) {
        Cookie cookie = ClientUtils.getCookie(request, "qrCodeVisit");
        System.out.println(cookie);
        return new ResponseEntity<>(cookie, HttpStatus.OK);
    }
}
