package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.ModeratorScoreHistory;
import com.lld360.cnc.model.UserPointHistory;
import com.lld360.cnc.model.UserSignin;
import com.lld360.cnc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class UserSigninService extends BaseService {

    @Autowired
    UserDao userDao;

    @Autowired
    private UserSigninDao signinDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserPointDao userPointDao;

    @Autowired
    private UserPointHistoryDao userPointHistoryDao;

    @Autowired
    private DocModeratorDao docModeratorDao;

    @Autowired
    private DocModeratorScoreDao moderatorScoreDao;

    @Transactional
    public void createSignin(Long userId) {
        if (getIsSignin(userId)) {
            throw new ServerException(HttpStatus.CREATED, "今天已经签过到了哦~");
        } else {
            UserSignin userSignin = new UserSignin();
            userSignin.setUserId(userId);
            userSignin.setScoreAdd(Const.USER_ADDPOINT_SIGNIN);
            userSignin.setSigninTime(Calendar.getInstance(Locale.CHINA).getTime());
            signinDao.create(userSignin);

            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            Date now = new java.sql.Date(Calendar.getInstance(Locale.CHINA).getTimeInMillis());
            params.put("signinTime", now);
            userSignin = signinDao.findByParam(params).get(0);
            //签到送积分
            userPointDao.updatePoint(userId, Const.USER_ADDPOINT_SIGNIN);
            //添加积分历史记录
            userPointHistoryDao.create(new UserPointHistory(userId, Const.USER_POINT_HISTORY_TYPE_USER_SIGNIN, Const.USER_ADDPOINT_SIGNIN, userSignin.getId(), "用户签到"));
            //如果是版主添加贡献值历史~
            if (userDao.isModerator(userId)) {
                if (moderatorScoreDao.existModeratorScore(userId)) {
                    moderatorScoreDao.updateModetatorScore(userId, Const.MODERATOR_SIGNIN_CONTRIBUTIONS);
                } else {
                    moderatorScoreDao.createModetatorScore(userId, Const.MODERATOR_SIGNIN_CONTRIBUTIONS);
                }
                ModeratorScoreHistory scoreHistory = new ModeratorScoreHistory(userId, Const.MODERATOR_CONTRIBUTIONS_TYPE_SIGNIN, Const.MODERATOR_SIGNIN_CONTRIBUTIONS, userId, "签到增加贡献值");
                moderatorScoreDao.createModeratorScoreHistory(scoreHistory);
            }
        }
    }

    public Boolean getIsSignin(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        Date now = new java.sql.Date(Calendar.getInstance(Locale.CHINA).getTimeInMillis());
        params.put("signinTime", now);
        return signinDao.findByParam(params).size() > 0;
    }

}
