package com.lld360.cnc.website.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.WeiXinPayConfiger;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import com.lld360.cnc.service.SettingService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.WxTempMsgService;
import com.lld360.cnc.website.dto.QqAccountAccessToken;
import com.lld360.cnc.website.dto.WxAccountAccessToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class ThirdAccountService extends BaseService {

    Logger logger = LoggerFactory.getLogger(ThirdAccountService.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ThirdAccountDao thirdAccountDao;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    UserScoreDao userScoreDao;

    @Autowired
    UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    UserLoginHistoryDao userLoginHistoryDao;

    @Autowired
    DocDao docDao;

    @Autowired
    DocDownloadDao docDownloadDao;

    @Autowired
    PostsDao postsDao;

    @Autowired
    PostsCommentDao postsCommentDao;

    @Autowired
    SoftUploadDao softUploadDao;

    @Autowired
    SoftDownloadDao softDownloadDao;

    @Autowired
    VodBuysDao vodBuysDao;

    @Autowired
    SettingService settingService;

    @Autowired
    WeiXinPayConfiger weiXinPayConfiger;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    public ThirdAccount create(ThirdAccount thirdAccount) {
        thirdAccountDao.create(thirdAccount);
        return thirdAccount;
    }

    public ThirdAccount update(ThirdAccount thirdAccount) {
        thirdAccountDao.update(thirdAccount);
        return thirdAccount;
    }

    public void relieveBind(ThirdAccount thirdAccount) {
        thirdAccountDao.relieveBind(thirdAccount);
    }

    public boolean findByOpenidCount(String openid) {
        return thirdAccountDao.findByOpenidCount(openid) > 0;
    }

    public ThirdAccount findByOpenid(String openid) {
        return thirdAccountDao.findByOpenid(openid);
    }

    public WxAccountAccessToken getWxAccessToken(String code) {
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                configer.getWxAccountAppid(), configer.getWxAccountScrect(), code);

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        WxAccountAccessToken token = null;
        try {
            token = objectMapper.readValue(json, WxAccountAccessToken.class);
            if (token.getErrcode() != null) {
                logger.warn("获取微信登录AccessToken失败：%d --> %s", token.getErrcode(), token.getErrmsg());
            }
        } catch (IOException e) {
            logger.warn("转换微信登录AccessToken异常", e);
        }
        return token;
    }

    public WxAccountAccessToken getWxPayAccessToken(String code) {
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                weiXinPayConfiger.getAppId(), weiXinPayConfiger.getAppSecret(), code);

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        WxAccountAccessToken token = null;
        try {
            token = objectMapper.readValue(json, WxAccountAccessToken.class);
            if (token.getErrcode() != null) {
                logger.warn("获取微信登录AccessToken失败：%d --> %s", token.getErrcode(), token.getErrmsg());
            }
        } catch (IOException e) {
            logger.warn("转换微信登录AccessToken异常", e);
        }
        return token;
    }

    public ThirdAccount getWxUserinfo(WxAccountAccessToken token) {
        String url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
                token.getAccessToken(), token.getOpenid());

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(new String(json.getBytes("ISO-8859-1"), "UTF-8"));
            if (jsonObject.has("errcode") && jsonObject.getInt("errcode") != 0) {
                logger.warn("获取微信用户信息失败：%d --> %s", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
                return null;
            }
            String gender = String.valueOf(jsonObject.getInt("sex") == 1 ? (Const.THIRD_ACCOUNT_GENDER_MAN) : Const.THIRD_ACCOUNT_GENDER_WOMAN);
            jsonObject.put("icon", jsonObject.getString("headimgurl"));
            jsonObject.put("gender", gender);
            jsonObject.remove("sex");
            jsonObject.remove("country");
            jsonObject.remove("headimgurl");
            jsonObject.remove("language");
            jsonObject.remove("privilege");
            json = jsonObject.toString();

            ObjectMapper mapper = new ObjectMapper();
            ThirdAccount thirdAccount = mapper.readValue(json, ThirdAccount.class);
            thirdAccount.setType(Const.THIRD_ACCOUNT_TYPE_WEIXIN);
            thirdAccount.setCreateTime(new Date());
            thirdAccount.setAccessToken(token.getAccessToken());
            thirdAccount.setExpiresIn(token.getExpiresIn());
            thirdAccount.setRefreshToken(token.getRefreshToken());
            return thirdAccount;
        } catch (IOException e) {
            logger.warn("转换微信用户异常", e);
        }
        return null;
    }

    public QqAccountAccessToken getQqAccessToken(String code) {
        String url = String.format("https://graph.qq.com/oauth2.0/token?client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s",
                configer.getQqAccountAppid(), configer.getQqAccountScrect(), code, configer.getAppUrl());

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        QqAccountAccessToken token = new QqAccountAccessToken();
        if (json.contains("error")) {
            logger.warn("获取QQ登录AccessToken失败:" + json);
            return null;
        } else {
            String[] jsonArray = json.split("&");
            for (String s : jsonArray) {
                if (s.startsWith("access_token="))
                    token.setAccessToken(getSubstring(s));
                if (s.startsWith("expires_in="))
                    token.setExpiresIn(Integer.valueOf(getSubstring(s)));
                if (s.startsWith("refresh_token="))
                    token.setRefreshToken(getSubstring(s));
            }
        }
        return token;
    }

    private String getSubstring(String s) {
        return s.substring(s.indexOf('=') + 1, s.length());
    }

    public QqAccountAccessToken getQqOpenId(QqAccountAccessToken token) {
        String url = String.format("https://graph.qq.com/oauth2.0/me?access_token=%s", token.getAccessToken());
        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        try {
            json = json.replace("callback(", "").replace(");\n", "").trim();
            JSONObject jsonObject = new JSONObject(new String(json.getBytes("ISO-8859-1"), "UTF-8"));
            token.setOpenid(jsonObject.getString("openid"));
            if (token.getErrcode() != null) {
                logger.warn("获取QQ的openid信息失败：%d --> %s", token.getErrcode(), token.getErrmsg());
            }
        } catch (IOException e) {
            logger.warn("转换QQ的openid异常", e);
        }
        return token;
    }

    public ThirdAccount getQqUserinfo(QqAccountAccessToken token) {
        ThirdAccount thirdAccount = new ThirdAccount();
        String url = String.format("https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s",
                token.getAccessToken(), configer.getQqAccountAppid(), token.getOpenid());

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getInt("ret") != 0) {
                logger.warn("获取QQ用户信息失败：%d --> %s", jsonObject.getInt("ret"), jsonObject.getString("msg"));
                return null;
            }
            String gender = String.valueOf(jsonObject.getString("gender").equals("男") ? (Const.THIRD_ACCOUNT_GENDER_MAN) : Const.THIRD_ACCOUNT_GENDER_WOMAN);
            String icon = StringUtils.isNotEmpty(jsonObject.getString("figureurl_qq_2")) ? jsonObject.getString("figureurl_qq_2") : jsonObject.getString("figureurl_qq_1");
            jsonObject.put("icon", icon);
            jsonObject.put("gender", gender);
            jsonObject.put("nickname", jsonObject.getString("nickname"));
            String[] removeStrings = new String[]{"level", "year", "ret", "msg", "figureurl", "figureurl_1", "figureurl_2", "figureurl_qq_1", "figureurl_qq_2", "is_lost", "is_yellow_vip", "is_yellow_year_vip", "yellow_vip_level", "vip"};
            for (String removeString : removeStrings
                    ) {
                jsonObject.remove(removeString);
            }
            json = jsonObject.toString();
            ObjectMapper mapper = new ObjectMapper();
            thirdAccount = mapper.readValue(json, ThirdAccount.class);
        } catch (IOException e) {
            logger.warn("转换QQ用户异常", e);
        }
        thirdAccount.setOpenid(token.getOpenid());
        thirdAccount.setType(Const.THIRD_ACCOUNT_TYPE_QQ);
        thirdAccount.setCreateTime(new Date());
        thirdAccount.setAccessToken(token.getAccessToken());
        thirdAccount.setExpiresIn(token.getExpiresIn());
        thirdAccount.setRefreshToken(token.getRefreshToken());
        return thirdAccount;
    }

    public UserDto thirdAccountLogin(ThirdAccount thirdAccount) {
        UserDto user = new UserDto();
        user.setNickname(thirdAccount.getNickname());
        user.setAvatar(thirdAccount.getIcon());
        user.setCreateTime(thirdAccount.getCreateTime());
        user.setLastLogin(thirdAccount.getUpdateTime());
        user.setAddress(thirdAccount.getProvince() + "  " + thirdAccount.getCity());
        user.setState(Const.USER_STATE_NORMAL);
        user.setLoginType(thirdAccount.getType());
        user.setIsBind(thirdAccount.getUserId() != null);
        user.setOpenid(thirdAccount.getOpenid());
        user.setId(thirdAccount.getUserId());
        return user;
    }

    public void isBindThirdAccount(String mobile, byte type) {
        User user = userService.getByMobile(mobile);
        String accountType = "";
        if (type == Const.THIRD_ACCOUNT_TYPE_QQ) accountType = "QQ";
        if (type == Const.THIRD_ACCOUNT_TYPE_WEIXIN) accountType = "微信";
        if (user != null && thirdAccountDao.isBindThirdAccount(user.getId(), type) > 0)
            throw new ServerException(HttpStatus.BAD_REQUEST, "已有" + accountType + "账号绑定该手机！");
    }

    @Transactional
    public UserDto bindThirdAccount(String openid, String mobile, String password) {
//        int score = settingService.getNumber(Const.CNC_REGISTER_GIVE_SCORE).intValue();
        int score = Const.USER_ADDSCORE_REGIEST;
        ThirdAccount thirdAccount;
        UserDto userDto = userService.getByMobile(mobile);
        if (userDto != null) userService.validateUserState(userDto);
        if (userDto == null) {
            thirdAccount = thirdAccountDao.findByOpenid(openid);
            userDto = new UserDto();
            userDto.setName(mobile);
            userDto.setMobile(mobile);
            userDto.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + mobile));
            userDto.setNickname(thirdAccount.getNickname());
            userDto.setCreateTime(thirdAccount.getCreateTime());
            userDto.setLastLogin(thirdAccount.getUpdateTime() == null ? thirdAccount.getCreateTime() : thirdAccount.getUpdateTime());
            userDto.setAvatar(thirdAccount.getIcon());
            userDto.setAddress(thirdAccount.getProvince() + "  " + thirdAccount.getCity());
            userDto.setState(Const.USER_STATE_NORMAL);
            userService.create(userDto);
            userScoreDao.create(new UserScore(userDto.getId(), score, score, 0));
            userScoreHistoryDao.create(new UserScoreHistory(userDto.getId(), Const.USER_SCORE_HISTORY_TYPE_BIND_ACCOUNT, score, userDto.getId()));
            thirdAccount.setUserId(userDto.getId());
            thirdAccountDao.update(thirdAccount);
            // 模板消息
            String msg = "亲爱的大牛用户,电话绑定赠送牛人币,产生牛人币变动";
            wxTempMsgService.sendScoreChgWxMsg(thirdAccount, msg, score, userScoreDao.find(userDto.getId()).getTotalScore());
        } else {
            thirdAccount = thirdAccountDao.findByOpenid(openid);
            userDto.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + mobile));
            setUserDto(mobile, thirdAccount, userDto);
            userService.updateUser(userDto);
            thirdAccount.setUserId(userDto.getId());
            thirdAccountDao.update(thirdAccount);
        }

        UserDto sessionUserdto = (UserDto) request.getSession().getAttribute(Const.SS_USER);
        sessionUserdto.setIsBind(true);
        sessionUserdto.setId(userDto.getId());
        request.getSession().setAttribute(Const.SS_USER, sessionUserdto);
        return userDto;
    }

    private void setUserDto(String mobile, ThirdAccount thirdAccount, UserDto userDto) {
        if (userDto.getName() == null) userDto.setName(mobile);
        if (userDto.getNickname() == null) userDto.setNickname(thirdAccount.getNickname());
        if (userDto.getLastLogin() == null)
            userDto.setLastLogin(thirdAccount.getUpdateTime() == null ? thirdAccount.getCreateTime() : thirdAccount.getUpdateTime());
        if (userDto.getAvatar() == null) userDto.setAvatar(thirdAccount.getIcon());
        if (userDto.getAddress() == null)
            userDto.setAddress(thirdAccount.getProvince() + "  " + thirdAccount.getCity());
    }

    public ThirdAccount getThirdAccountByUserId(long userId, int type) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("type", type);
        List<ThirdAccount> list = thirdAccountDao.search(param);
        if (!list.isEmpty())
            return list.get(list.size() - 1);
        return null;
    }

    // 处理第三方登录
    @Transactional
    public Integer dealThirdLogin(String code, int state) {
        Integer isNew = 0;
        ThirdAccount thirdAccount = getThirdAccountByCode(code, state);
        if (thirdAccount != null) {
            if (findByOpenidCount(thirdAccount.getOpenid())) {
                thirdAccount = findByOpenid(thirdAccount.getOpenid());
                if (thirdAccount.getUserId() == null) {
                    UserDto user = userService.registThirdAccount(thirdAccount);
                    thirdAccount.setUserId(user.getId());
                    wxTempMsgService.sendRegistWxMsg(thirdAccount); // 发送注册模板消息
                    isNew = 1;
                }
                thirdAccount.setUpdateTime(Calendar.getInstance(Locale.CHINA).getTime());
                update(thirdAccount);
            } else {
                UserDto user = userService.registThirdAccount(thirdAccount);
                thirdAccount.setUserId(user.getId());
                create(thirdAccount);
                wxTempMsgService.sendRegistWxMsg(thirdAccount); // 发送注册模板消息
                isNew = 1;
            }
            UserDto currentUser = userService.getUserDto(thirdAccount.getUserId());
            userService.addLoginHistory(currentUser);
            /*
            /*测试模板消息
             *测试号:丿神斌丶 userId:2154
             * */
//            if (currentUser.getId() == 2154) {
//                wxTempMsgService.sendRegistWxMsg(thirdAccount);
//            }
            request.getSession().setAttribute(Const.SS_USER, currentUser);
        }
        return isNew;
    }

    public ThirdAccount getThirdAccountByCode(String code, int state) {
        ThirdAccount thirdAccount = null;
        if (state == Const.THIRD_ACCOUNT_TYPE_QQ) {
            QqAccountAccessToken token = getQqAccessToken(code);
            if (token != null) {
                token = getQqOpenId(token);
                if (token.getOpenid() != null) {
                    thirdAccount = getQqUserinfo(token);
                }
            }
        } else if (state == Const.THIRD_ACCOUNT_TYPE_WEIXIN) {
            WxAccountAccessToken token = getWxAccessToken(code);
            if (token != null) {
                thirdAccount = getWxUserinfo(token);
            }
        }
        return thirdAccount;
    }


    // 第三方账号更换绑定user
    public void changeThirdBind(UserDto reserveUser, UserDto cancelUser) {
        boolean qqExist = thirdAccountDao.isBindThirdAccount(reserveUser.getId(), (byte) Const.THIRD_ACCOUNT_TYPE_QQ) > 0 && thirdAccountDao.isBindThirdAccount(cancelUser.getId(), (byte) Const.THIRD_ACCOUNT_TYPE_QQ) > 0;
        boolean weixinExist = thirdAccountDao.isBindThirdAccount(reserveUser.getId(), (byte) Const.THIRD_ACCOUNT_TYPE_QQ) > 0 && thirdAccountDao.isBindThirdAccount(cancelUser.getId(), (byte) Const.THIRD_ACCOUNT_TYPE_QQ) > 0;
        if (qqExist || weixinExist) throw new ServerException(HttpStatus.BAD_REQUEST, "已有微信/QQ账号绑定该手机！请先登录该手机账号解绑");
        thirdAccountDao.updateThirUser(reserveUser.getId(), cancelUser.getId());
    }

}
