package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.bean.AliSmsSender;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService extends BaseService {

    @Autowired
    UserDao userDao;

    @Autowired
    Configer configer;

    @Autowired
    ValidSmsService validSmsService;

    @Autowired
    UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    UserScoreDao userScoreDao;

    @Autowired
    private UserPointDao userPointDao;

    @Autowired
    private UserPointHistoryDao userPointHistoryDao;

    @Autowired
    AliSmsSender aliSmsSender;

    @Autowired
    SettingService settingService;

    @Autowired
    UserLoginHistoryDao userLoginHistoryDao;

    @Autowired
    private ThirdAccountDao thirdAccountDao;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    public void create(UserDto userDto) {
        userDao.create(userDto);
    }

    //列表-分页
    public Page<UserDto> getUsersByPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = userDao.count(params);
        List<UserDto> users = count > 0 ? userDao.search(params) : new ArrayList<>();
        return new PageImpl<>(users, pageable, count);
    }

    // 获取个人资料
    public User getUser(long id) {
        return userDao.find(id);
    }

    // 获取用户数量 暂时这样 待修改
    public long getCount(Map<String, Object> param) {
        return userDao.count(param);
    }

    //手机号码注册
    @Transactional
    public UserDto registerWithMobile(String mobile, String password, String validCode, HttpServletResponse response) {
//        int score = Integer.parseInt(settingService.getValue(Const.CNC_REGISTER_GIVE_SCORE));
        int score = Const.USER_ADDSCORE_REGIEST;
        if (StringUtils.isEmpty(mobile) || !mobile.matches("1\\d{10}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10001);
        }
        if (StringUtils.isEmpty(password) || !password.matches("\\w{6,20}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10002);
        }
        if (StringUtils.isEmpty(validCode) || !validCode.matches("\\d{6}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10003);
        }
        validSmsService.validSmsCode(mobile, Const.SMS_REGIST, validCode);

        UserDto user = new UserDto();
        user.setName("");
        user.setMobile(mobile);
        user.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + mobile));
        user.setCreateTime(new Date());
        setUserPlat(user);
        user.setState(Const.USER_STATE_NORMAL);
        userDao.create(user);
        user.setNickname("用户" + user.getId());
        userDao.updateById(user);
        //积分 以及 注册送牛人币
        userPointDao.create(new UserPoint(user.getId(), 0, 0, 0));
        userScoreDao.create(new UserScore(user.getId(), score, score, 0));
        UserScoreHistory history = new UserScoreHistory(user.getId(), Const.USER_SCORE_HISTORY_TYPE_REGIEST, score, user.getId());
        history.setDescription("手机号注册系统赠送");
        userScoreHistoryDao.create(history);
        return user;
    }

    // 新加:用户注册来源
    private void setUserPlat(UserDto user) {
        if (ClientUtils.isMobileBrowser(request)) {
            user.setPlatform(Const.REGIST_PLATFORM_MOBILE);
        } else {
            user.setPlatform(Const.REGIST_PLATFORM_WEB);
        }
        if (ClientUtils.getCookie(request, "qrCodeVisit") != null) user.setPlatform(Const.REGIST_PLATFORM_QRCODE);
    }

    // 第三方注册:创建网站账号并绑定
    @Transactional
    public UserDto registThirdAccount(ThirdAccount thirdAccount) {
        UserDto user = new UserDto();
        user.setCreateTime(new Date());
        setUserPlat(user);
        user.setState(Const.USER_STATE_NORMAL);
        user.setNickname(thirdAccount.getNickname());
        user.setAvatar(thirdAccount.getIcon());
        user.setAddress(thirdAccount.getProvince().concat(thirdAccount.getCity()));
        user.setLastLogin(Calendar.getInstance(Locale.CHINA).getTime());
        userDao.create(user);
        userScoreDao.create(new UserScore(user.getId(), 0, 0, 0));
        userPointDao.create(new UserPoint(user.getId(), 0, 0, 0));
        return user;
    }

    // 绑定手机号赠送积分
    public void bindMobileAddscore(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        params.put("type", Const.USER_SCORE_HISTORY_TYPE_REGIEST);
        if (userScoreHistoryDao.count(params) == 0) {
//            int score = Integer.parseInt(settingService.getValue(Const.CNC_REGISTER_GIVE_SCORE));
            int score = Const.USER_ADDSCORE_REGIEST;
            userScoreDao.updateScore(user.getId(), score);
            UserScoreHistory history = new UserScoreHistory(user.getId(), Const.USER_SCORE_HISTORY_TYPE_REGIEST, score, user.getId());
            history.setDescription("绑定手机号系统赠送");
            userScoreHistoryDao.create(history);
        }
    }

    public void adminAddScore(User user, int score) {
        userScoreDao.updateScore(user.getId(), score);
        UserScoreHistory history = new UserScoreHistory(user.getId(), Const.USER_SOCRE_HISTORY_TYPE_ADMIN_ADD, score, user.getId());
        history.setDescription("管理员赠送牛人币");
        userScoreHistoryDao.create(history);
    }

    // 用户登录
    public User doLogin(String username, String password, HttpServletResponse response) {
        UserDto user = getByMobile(username);
        if (user == null || !user.getPassword().equals(DigestUtils.md5Hex(StringUtils.reverse(password) + username))) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10012);
        }
        if (user.getState() != Const.USER_STATE_NORMAL)
            throw new ServerException(HttpStatus.FORBIDDEN, M.E10013);
        validateUserState(user);
        request.getSession().setAttribute(Const.SS_USER, user);
        Cookie cookie = new Cookie("rememberMe", StringUtils.reverse(user.getPassword()) + '/' + Long.toString(user.getId() + 10000, Character.MAX_RADIX));
        cookie.setPath("/");
        cookie.setMaxAge(Const.REMEMBER_ME_TIME);
        response.addCookie(cookie);
        addLoginHistory(user);
        return user;
    }

    // 添加用户登录历史
    public void addLoginHistory(UserDto user) {
        String platform = "Web";
        if (ClientUtils.isMobileBrowser(request)) {
            platform = "Mobile";
        }
        UserLoginHistory userLoginHistory = new UserLoginHistory(user.getId(), ClientUtils.getIp(request), platform);
        userLoginHistoryDao.create(userLoginHistory);
        user.setLastLogin(Calendar.getInstance(Locale.CHINA).getTime());
        userDao.updateById(user);
    }

    // 用户登出
    public void doLogout(HttpServletResponse response) {
        if (!request.getSession().isNew())
            request.getSession().invalidate();
        Cookie cookie = ClientUtils.getCookie(request, "rememberMe");
        if (cookie != null) {
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    // 删除用户
    public void delete(long id) {
        User user = userDao.find(id);
        if (user == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404).setMessage("用户不存在");
        } else {
            userDao.updateState(user.getId(), Const.USER_STATE_DELETED);
        }
    }

    // 编辑用户资料
    public User edit(User user) {
        if (user.getPassword() != null) {
            user.setPassword(DigestUtils.md5Hex(StringUtils.reverse(user.getPassword()) + user.getMobile()));
        }
        user.setCreateTime(null);
        return userDao.updateById(user) > 0 ? getUser(user.getId()) : user;
    }

    public void updateByMobile(String mobile, String pwd) {
        User user = new User();
        user.setPassword(DigestUtils.md5Hex(StringUtils.reverse(pwd) + mobile));
        user.setMobile(mobile);
        userDao.updateByMobile(user);
        // push密码变更消息
        List<ThirdAccount> thirdAccounts = thirdAccountDao.searchWxAccountByMobile(mobile);
        if (thirdAccounts.size() > 0) {
            wxTempMsgService.sendPwdWxMsg(thirdAccounts.get(0));
        }
    }

    //更新用户资料 yangb:可修改
    public boolean updateUser(User user) {
        return userDao.updateById(user) == 1;
    }

    // 丢弃账号
    public void desertUser(User desertUser, User operator) {
        desertUser.setState(Const.USER_STATE_DESERT);
        desertUser.setLastLogin(Calendar.getInstance(Locale.CHINA).getTime());
        desertUser.setDescription("时间:" + new java.sql.Timestamp(Calendar.getInstance(Locale.CHINA).getTimeInMillis()) + ",用户【" + operator.getNickname() + "】【userId:" + operator.getId() + "】选择丢弃账号");
        userDao.desertUser(desertUser);
    }

    public UserDto getByMobile(String loginName) {
        return userDao.findByMobile(loginName);
    }

    // 上传用户头像
    public User setAvatar(User user, MultipartFile file) {
        String relativePath = "user/" + user.getId() + "/";
        String relativeFile = relativePath + "avatar." + FilenameUtils.getExtension(file.getOriginalFilename());
        String absoluteFile = configer.getFileBasePath() + relativeFile;
        if (user.getAvatar() != null) {
            File oldAvatarFile = new File(configer.getFileBasePath() + user.getAvatar());
            if (oldAvatarFile.exists())
                FileUtils.deleteQuietly(oldAvatarFile);
        }
        try {
            File f = new File(absoluteFile);
            if (f.getParentFile().exists() || f.getParentFile().mkdirs())
                file.transferTo(f);
            userDao.updateAvatar(user.getId(), relativeFile);
            user.setAvatar(relativeFile);
            return user;
        } catch (IOException e) {
            logger.warn("保存用户头像失败");
            throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
        }
    }

    // 获取用户牛人币详情分页列表
    public Page<UserScoreHistory> getUserScoreHistoriesByPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long total = userScoreHistoryDao.count(params);
        List<UserScoreHistory> userScoreHistoryList = userScoreHistoryDao.search(params);
        return new PageImpl<>(userScoreHistoryList, pageable, total);
    }

    // 获取用户积分详情分页列表
    public Page<UserPointHistory> getUserPointHistoriesByPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long total = userPointHistoryDao.count(params);
        List<UserPointHistory> userPointHistories = userPointHistoryDao.search(params);
        return new PageImpl<>(userPointHistories, pageable, total);
    }

    // 获取用户详细信息
    public UserDto getUserDto(long id) {
        return userDao.findUserDto(id);
    }

    //获取用户牛人币信息
    public UserScore findUserScore(long id) {
        return userScoreDao.find(id);
    }

    //获取用户积分信息
    public UserPoint findUserPoint(Long id) {
        if (id == null) throw new ServerException(HttpStatus.BAD_REQUEST);
        if (userPointDao.find(id) == null) userPointDao.create(new UserPoint(id, 0, 0, 0));
        return userPointDao.find(id);
    }

    public List<UserScoreHistory> userScoreHistories(Map<String, Object> param) {
        return userScoreHistoryDao.search(param);
    }

    public void validateUserState(UserDto userDto) {
        byte state = userDto.getState();
        switch (state) {
            case Const.USER_STATE_DELETED:
                throw new ServerException(HttpStatus.BAD_REQUEST, M.E10018);
            default:
                break;
        }
    }

    public boolean isModerator(long id) {
        return userDao.isModerator(id);
    }

// 已更改为申请提现时冻结牛人币
//    public void withdrawalsReduceScore(long userId, int score) {
//        userScoreDao.updateScore(userId, -score);
//        UserScoreHistory history = new UserScoreHistory(userId, Const.USER_SCORE_HISTORY_TYPE_WITHDRAWALS, score, userId);
//        history.setDescription("用户转账提现扣除牛人币");
//        userScoreHistoryDao.create(history);
//    }

    public ThirdAccount getThirdAccountByUserid(Long userId, Integer type) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("type", type);
        List<ThirdAccount> list = thirdAccountDao.search(param);
        if (list.size() != 0)
            return list.get(list.size() - 1);
        return null;
    }

    public List<ThirdAccount> getVodBuyThirdaccount(Integer volumeId) {
        return thirdAccountDao.getVodBuyThirdaccount(volumeId);
    }

}
