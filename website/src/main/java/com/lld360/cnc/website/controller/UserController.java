package com.lld360.cnc.website.controller;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.IdCardUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.service.*;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.service.ThirdAccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("user")
public class UserController extends SiteController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocService docService;

    @Autowired
    private DocCollectService docCollectService;

    @Autowired
    private FileUtilService fileUtilService;

    @Autowired
    private ValidSmsService validSmsService;

    @Autowired
    private ThirdAccountService thirdAccountService;

    @Autowired
    private UserSigninService userSigninService;

    @Autowired
    private DocCategoryService docCategoryService;

    @Autowired
    private UserSuggestsService userSuggestsService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private ModeratorService moderatorService;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private CopyWritingService copyWritingService;

    //用户注册时上传头像
    @ResponseBody
    @RequestMapping(value = "uploadAvatar", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User uploadAvatar(MultipartFile file, @RequestParam Double x, @RequestParam Double y, @RequestParam Double w, @RequestParam Double h) {
        if (file == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("文件不存在");
        }
        checkFileType(file, Const.COURSE_NOT_GEN_PIC_TYPES);
        Rectangle rectangle = new Rectangle(x.intValue(), y.intValue(), w.intValue(), h.intValue());
        String fileName = fileUtilService.cutFile(file, rectangle);

        UserDto currentUser = getRequiredCurrentUser();
        String avatar = fileUtilService.moveUploadedFile(fileName, Const.FILE_USER, currentUser.getId(), currentUser.getAvatar());

        User user = new User();
        user.setId(currentUser.getId());
        user.setAvatar(avatar);
        userService.updateUser(user);
        currentUser.setAvatar(avatar);
        request.getSession().setAttribute(Const.SS_USER, currentUser);
        return user;
    }

    // 获取用户积分
    @ResponseBody
    @RequestMapping(value = "score", method = RequestMethod.GET)
    public UserScore userScoreGet() {
        UserDto userDto = getCurrentUser();
        if (userDto != null && userDto.getId() != null) {
            return userService.findUserScore(userDto.getId());
        }
        return null;
    }

    // Ajax实时获取用户积分、签到、未读消息等信息
    @ResponseBody
    @RequestMapping(value = "infos", method = RequestMethod.GET)
    public Map<String, Object> userInfoGet() {
        Map<String, Object> infos = new HashMap<>();
        UserDto user = getRequiredCurrentUser();
        if (user.getId() == null) return null;
        Long userId = user.getId();
        infos.put("score", userService.findUserScore(userId));
        infos.put("point", userService.findUserPoint(userId));
        infos.put("isSigns", userSigninService.getIsSignin(userId));
        infos.put("isMember", userMemberService.isMember(userId));
        infos.put("isModerator", userService.isModerator(userId));
        infos.put("messages", userMessageService.noReadCount(userId));
        return infos;
    }

    // 用户消息列表
    @RequestMapping(value = "message", method = RequestMethod.GET)
    public String userMessagesGet(Model model) {
        UserDto user = getRequiredCurrentUser();
        Map<String, Object> params = getParamsPageMap(15);
        params.put("userId", user.getId());
        model.addAttribute("messages", userMessageService.search(params));
        userMessageService.setReadByUser(user.getId());
        return "wUser/message";
    }

    //删除用户消息
    @RequestMapping(value = "deleteMessage", method = RequestMethod.POST)
    public ResponseEntity deleteMessage(@RequestParam long id) {
        if (userMessageService.getMessageById(id) == null)
            throw new ServerException(HttpStatus.NOT_FOUND, "没有此条消息记录");
        userMessageService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //web用户中心
    @RequestMapping(value = "center", method = RequestMethod.GET)
    public String userCenter(Model model) {
        UserDto user = getRequiredCurrentUser();
        Map<String, Object> params = new HashMap<>();
        long myDownloadCount = 0, myUploadCount = 0, myConllectCount = 0;

        if (user.getId() != null) {
            params.put("userId", user.getId());
            params.put("uploader", user.getId());
            params.put("uploaderType", Const.DOC_UPLOADERTYPE_USER);
            myDownloadCount = docService.getDownloadCount(params);
            myUploadCount = docService.countWeb(params);
            myConllectCount = docCollectService.conllectCount(params);
        }
        UserInvite userInvite = userInviteService.findInviteByUserId(user.getId());
        if (userInvite != null && userInvite.getState().equals(Const.PARTER_STATE_NORMAL))
            user.setUserInvite(userInvite);
        model.addAttribute("user", user);
        model.addAttribute("myDownloadCount", myDownloadCount);
        model.addAttribute("myUploadCount", myUploadCount);
        model.addAttribute("myConllectCount", myConllectCount);
        model.addAttribute("configer", configer);
        return "wUser/user-center";
    }

    //web用户编辑
    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String userProfile(Model model) {
        User user = getRequiredCurrentUser();
        model.addAttribute("user", user);
        return "wUser/user-profile";
    }

    //web用户积分
    @RequestMapping(value = "myscore", method = RequestMethod.GET)
    public String userScore(Model model) {
        UserDto user = getRequiredCurrentUser();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        model.addAttribute("userScore", userService.findUserScore(user.getId()));
        model.addAttribute("scoreHistories", userService.userScoreHistories(params));
        return "wUser/user-score";
    }

    //web用户积分充值
    @RequestMapping(value = "myscore/pay", method = RequestMethod.GET)
    public String userPay(Model model) {
        User user = getRequiredCurrentUser();
        model.addAttribute("user", user);
        return "wUser/user-pay";
    }

    //个人用户信息修改
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public String updateUser(@RequestParam String name,
                             @RequestParam String nickname,
                             @RequestParam String qq,
                             @RequestParam String mailAddress,
                             @RequestParam(required = false) String address,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) String idcard,
                             RedirectAttributes attributes) {
        UserDto user = getRequiredCurrentUser();

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(name)) {
            sb.append("真实姓名不能为空<br>");
        }
        if (StringUtils.isEmpty(nickname)) {
            sb.append("昵称不能为空<br>");
        }
        if (StringUtils.isEmpty(mailAddress)) {
            sb.append("邮箱地址不能为空<br>");
        }
        if (StringUtils.isNotEmpty(qq) && !qq.matches("\\d{5,15}")) {
            sb.append("QQ号码必须是5至15位纯数字<br>");
        }
        if (StringUtils.isNotEmpty(qq) && !IdCardUtils.checkValidate(idcard)) {
            sb.append("身份证号错误<br>");
        }
        if (docCategoryService.isNowModerator(user.getId()) && StringUtils.isEmpty(qq)) {
            sb.append("版主QQ信息不能为空<br>");
        }
        if (sb.length() > 0) {
            attributes.addFlashAttribute("errMsg", sb.toString());
            return "redirect:/user/profile";
        }
        user.setName(name);
        user.setQq(qq);
        user.setIdcard(idcard);
        user.setNickname(nickname);
        user.setMailAddress(mailAddress);
        user.setAddress(address);
        user.setDescription(description);
        user.setPassword(null);
        userService.edit(user);

        user = userService.getUserDto(user.getId());
        request.getSession().setAttribute(Const.SS_USER, user);
        attributes.addFlashAttribute("message", "修改成功");
        return "redirect:/user/profile";
    }

    //web修改密码
    @RequestMapping(value = "pwd", method = RequestMethod.GET)
    public String userPwd() {
        if (getRequiredCurrentUser().getMobile() != null) return "wUser/user-pwd";
        else return "redirect:/user/center";
    }

    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    public String updatePwd(RedirectAttributes attributes, @RequestParam String password, @RequestParam String newpassword, HttpServletResponse response) {
        User user = getRequiredCurrentUser();
        if (user.getMobile() == null) {
            attributes.addFlashAttribute("errmsg", "请先绑定手机号");
            return "redirect:/user/accountManage";
        }
        if (!user.getPassword().equals(DigestUtils.md5Hex(StringUtils.reverse(password) + user.getMobile()))) {
            attributes.addFlashAttribute("errMsg", "原密码错误");
            return "redirect:/user/pwd";
        }
        if (!newpassword.matches("\\w{6,20}")) {
            attributes.addFlashAttribute("errMsg", "新建密码不符合规则");
            return "redirect:/user/pwd";
        }
        user.setPassword(newpassword);
        userService.edit(user);
        attributes.addFlashAttribute("message", "密码修改成功,请重新登录");
        request.setAttribute("mobile", user.getMobile());
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
    public String resetPwdGet(Model model, @RequestParam String mobile) {
        model.addAttribute("mobile", mobile);
        return "wAuth/reset-pwd";
    }

    //忘记密码  重置密码
    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    public String resetPwdPost(Model model, @RequestParam String password, @RequestParam String mobile) {
        if (userService.getByMobile(mobile) == null) {
            model.addAttribute("message", "重置密码成功,请登录");
            return "wAuth/login";
        }
        userService.updateByMobile(mobile, password);
        model.addAttribute("message", "重置密码成功,请登录");
        return "wAuth/login";
    }

    //绑定手机号
    @ResponseBody
    @RequestMapping(value = "bindAccount", method = RequestMethod.POST)
    public String bindAccount(@RequestParam String password, @RequestParam String openid, @RequestParam String code, @RequestParam String mobile, @RequestParam byte bindType, @RequestParam byte smsType) {
        String message;
        validSmsService.validSmsCode(mobile, smsType, code);
        thirdAccountService.isBindThirdAccount(mobile, bindType);
        try {
            thirdAccountService.bindThirdAccount(openid, mobile, password);
        } catch (Exception e) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10017);
        }
        message = "绑定手机号成功！";
        return message;
    }

    //用户签到
    @RequestMapping(value = "signin", method = RequestMethod.GET)
    public ResponseEntity userSignin() {
        UserDto user = getRequiredCurrentUser();
        userSigninService.createSignin(user.getId());
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    // 获取用户是否签到
    @ResponseBody
    @RequestMapping(value = "isSignin", method = RequestMethod.GET)
    public Boolean isSigninGet() {
        UserDto userDto = getRequiredCurrentUser();
        return userSigninService.getIsSignin(userDto.getId());
    }

    // 获取用户管理的所有版块
    @RequestMapping(value = "moderators", method = RequestMethod.GET)
    public String moderatorsGet(Model model) {
        User user = getRequiredCurrentUser();
        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getQq()) || StringUtils.isEmpty(user.getMailAddress())) {
            model.addAttribute("needUserInfo", true);
        } else {
            model.addAttribute("needUserInfo", false);
            model.addAttribute("maxNum", Const.MODERATOR_MAX_APPLY_NUM);
            model.addAttribute("moderatorCategories", docCategoryService.getModeratorsByModerator(user.getId()));
            model.addAttribute("notModeratorCategories", docCategoryService.getModeratorsByNotModerator(user.getId()));
        }
        return "wUser/moderator-apply";
    }

    // 获取用户管理版块的文档列表
    @RequestMapping(value = "moderators/{c1}", method = RequestMethod.GET)
    public String moderatorDocsGet(@PathVariable int c1, Model model) {
        User user = getRequiredCurrentUser();
        if (!docCategoryService.isModerator(c1, user.getId())) {
            model.addAttribute("error", "你不是当前版块的版主");
        } else {
            Map<String, Object> params = getParamsPageMap(20);
            // 只查询审核中的
            params.put("state", Const.DOC_STATE_REVIEWING);
            params.put("c1", c1);
            params.put("sortByCheck", Const.DOC_STATE_REVIEWING);
            model.addAttribute("docs", docService.searchWeb(params));
            model.addAttribute("category", docCategoryService.get(c1));
        }
        return "wUser/moderator-docs";
    }

    //用户申请版主
    @RequestMapping(value = "moderatorApply", method = RequestMethod.POST)
    public String moderatorApplyPost(@RequestParam Integer categoryId, @RequestParam String remarks) {
        if (categoryId == null)
            throw new ServerException(HttpStatus.BAD_REQUEST, "没有选择任何板块");
        UserDto user = getRequiredCurrentUser();
        Moderator moderator = new Moderator(categoryId, user.getId(), null, Const.MODERATOR_STATE_APPLY);
        moderator.setRemarks(remarks);
        docCategoryService.addModerator(moderator);
        return "redirect:/user/moderators";
    }

    // 版主贡献值历史
    @RequestMapping(value = "moderators/score", method = RequestMethod.GET)
    public String moderatorSscore(Model model) {
        User user = getRequiredCurrentUser();
        boolean isModerator = userService.isModerator(user.getId());
        Map<String, Object> param = getParamsMap();
        if (isModerator) {
            param.put("userId", user.getId());
            model.addAttribute("monthScores", moderatorService.getHistoriesList(param));
            model.addAttribute("moderatorScore", moderatorService.getModeratorScore(user.getId()));
        }
        model.addAttribute("isModerator", isModerator);
        return "wUser/moderator-score";
    }

    // 版主职责说明
    @RequestMapping(value = "moderators/duty", method = RequestMethod.GET)
    public String moderatorDuty(Model model) {
        model.addAttribute("duty", copyWritingService.getCopyWriteByKey("bz001"));
        return "wUser/moderator-duty";
    }

    // 版主贡献值说明
    @RequestMapping(value = "moderators/explain", method = RequestMethod.GET)
    public String moderatorExplain(Model model) {
        model.addAttribute("explain", copyWritingService.getCopyWriteByKey("bz002"));
        return "wUser/moderator-explain";
    }

    //用户反馈
    @ResponseBody
    @RequestMapping(value = "userSuggest", method = RequestMethod.POST)
    public ResponseEntity userSuggest(@Valid UserSuggest suggest, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, result.getFieldErrors().get(0).getDefaultMessage());
        }
        UserDto user = getRequiredCurrentUser();
        UserSuggest userSuggest = new UserSuggest();
        userSuggest.setUserId(user.getId());
        userSuggest.setContent(suggest.getContent());
        userSuggestsService.create(userSuggest);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    //我的反馈
    @RequestMapping(value = "mySuggests", method = RequestMethod.GET)
    public String mySuggest(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> params = getParamsPageMap(10);
        params.put("userId", user.getId());
        model.addAttribute("suggests", userSuggestsService.getSuggestDtoPage(params));
        return "wUser/user-mySuggestsList";
    }


    // 账号管理-绑定手机号get
    @RequestMapping(value = "accountManage", method = RequestMethod.GET)
    public String accountManage() {
        return "wUser/account-manage";
    }

    // 账号管理-绑定/修改手机号post
    @RequestMapping(value = "accountManage", method = RequestMethod.POST)
    public ResponseEntity accountManagePost(@RequestParam String bindMobile, @RequestParam String bindCode, @RequestParam String password, @RequestParam(required = false) Byte leaveId) {
        if (StringUtils.isEmpty(bindMobile) || !bindMobile.matches("1\\d{10}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "绑定失败,无效的手机号");
        }
        if (StringUtils.isEmpty(password) || !password.matches("\\w{6,20}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "绑定失败,无效的密码");
        }
        validSmsService.validSmsCode(bindMobile, Const.SMS_BINDACCOUNT, bindCode);
        UserDto currentUser = getRequiredCurrentUser();
        UserDto bindUser = userService.getByMobile(bindMobile);
        if (bindUser != null && bindUser.getState() == Const.USER_STATE_DELETED)
            throw new ServerException(HttpStatus.FORBIDDEN, M.E10016);
        String message;
        switch (leaveId) {
            case 0:     //手机号未注册
                if(bindUser!=null){
                    throw new ServerException(HttpStatus.FORBIDDEN, M.E10016);
                }
                if (currentUser.getMobile() == null) userService.bindMobileAddscore(currentUser);
                currentUser.setMobile(bindMobile);
                currentUser.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + bindMobile));
                userService.updateUser(currentUser);
                request.getSession().setAttribute(Const.SS_USER, currentUser);
                message = "绑定/更换手机号成功";
                break;
            case 1:     //保留当前账号
                thirdAccountService.changeThirdBind(currentUser, bindUser);
                userService.desertUser(bindUser, currentUser);
                currentUser.setMobile(bindMobile);
                currentUser.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + bindMobile));
                userService.updateUser(currentUser);
                request.getSession().setAttribute(Const.SS_USER, currentUser);
                message = "账号保留操作成功";
                break;
            case 2:     //保留手机号账号
                thirdAccountService.changeThirdBind(bindUser, currentUser);
                userService.desertUser(currentUser, bindUser);
                bindUser.setMobile(bindMobile);
                bindUser.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + bindMobile));
                userService.updateUser(bindUser);
                request.getSession().setAttribute(Const.SS_USER, bindUser);
                message = "账号保留操作成功";
                break;
            default:
                throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "checkBind", method = RequestMethod.GET)
    public ResponseEntity checkBind(@RequestParam String bindMobile) {
        if (StringUtils.isEmpty(bindMobile) || !bindMobile.matches("1\\d{10}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "绑定失败,无效的手机号");
        }
        UserDto currentUser = getRequiredCurrentUser();
        if (currentUser.getMobile() != null && currentUser.getMobile().equals(bindMobile))
            throw new ServerException(HttpStatus.BAD_REQUEST, "当前账号手机号码与所输号码一致,无需绑定");
        UserDto bindUser = userService.getByMobile(bindMobile);
        return new ResponseEntity<>(bindUser != null, HttpStatus.OK);
    }

    // 第三方账号绑定
    @RequestMapping(value = "accountRelevance", method = RequestMethod.GET)
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
        return "wUser/account-relevance";
    }

    // 第三方绑定保留
    @RequestMapping(value = "thirdLeave", method = RequestMethod.POST)
    public ResponseEntity thirdLeave(@RequestParam Byte leaveId, @RequestParam Long oldBindId) {
        UserDto oldBindUser = userService.getUserDto(oldBindId);
        UserDto currentUser = getRequiredCurrentUser();
        if (oldBindUser == null) throw new ServerException(HttpStatus.NOT_FOUND);
        switch (leaveId) {
            case 1:     //保留当前
                thirdAccountService.changeThirdBind(currentUser, oldBindUser);
                userService.desertUser(oldBindUser, currentUser);
                request.getSession().setAttribute(Const.SS_USER, currentUser);
                break;
            case 2:     //保留原绑定
                thirdAccountService.changeThirdBind(oldBindUser, currentUser);
                if (currentUser.getMobile() != null) {
                    oldBindUser.setMobile(currentUser.getMobile());
                    oldBindUser.setPassword(currentUser.getPassword());
                }
                userService.desertUser(currentUser, oldBindUser);
                userService.updateUser(oldBindUser);
                request.getSession().setAttribute(Const.SS_USER, oldBindUser);
                break;
            default:
                throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // 解除绑定
    @ResponseBody
    @RequestMapping(value = "relieve", method = RequestMethod.GET)
    public ResponseEntity relieveBind(@RequestParam byte type) {
        UserDto userDto = getRequiredCurrentUser();
        if (userDto.getMobile() == null)
            return new ResponseEntity<>("解绑失败,该账号尚未绑定手机号", HttpStatus.FORBIDDEN);
        ThirdAccount thirdAccount;
        switch (type) {
            case Const.THIRD_ACCOUNT_TYPE_QQ:
                thirdAccount = thirdAccountService.getThirdAccountByUserId(userDto.getId(), Const.THIRD_ACCOUNT_TYPE_QQ);
                break;
            case Const.THIRD_ACCOUNT_TYPE_WEIXIN:
                thirdAccount = thirdAccountService.getThirdAccountByUserId(userDto.getId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
                break;
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        thirdAccountService.relieveBind(thirdAccount);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "ewm", method = RequestMethod.GET)
    public void userQRCodeGet(HttpServletResponse response, @RequestParam(required = false, defaultValue = "300") int size) {
        UserDto user = getRequiredCurrentUser();
        UserInvite userInvite = userInviteService.findInviteByUserId(user.getId());
        if (userInvite != null && userInvite.getState().equals(Const.PARTER_STATE_NORMAL)) {
            String url = configer.getAppUrl() + "m/auth/register?inviteCode=" + userInvite.getInviteCode();
            try {
                response.setContentType("image/png");
                fileUtilService.outQRCode(response.getOutputStream(), url, size);
            } catch (IOException e) {
                logger.warn("获取输出流错误：" + e.getMessage(), e);
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
