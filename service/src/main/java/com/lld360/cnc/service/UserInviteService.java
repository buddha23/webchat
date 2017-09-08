package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserInviteService extends BaseService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    private UserService userService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    @Autowired
    private UserMessageDao userMessageDao;

    // 根据邀请码查询用户
    public UserInvite findUserInvite(String inviteCode) {
        return userDao.findInviteByCode(inviteCode);
    }

    public UserInvite findInviteByUserId(Long userId) {
        return userDao.findInviteByUserId(userId);
    }

    @Transactional
    public void setParter(Long userId) {
        UserInvite userInvite = userDao.findInviteByUserId(userId);
        if (userInvite != null) {
            userInvite.setIsParter(true);
            userInvite.setParterId(userId);
            userInvite.setUpdateTime(Calendar.getInstance(Locale.CHINA).getTime());
            userDao.updateUserInvite(userInvite);
        } else {
            userInvite = new UserInvite(userId, true, userId, userId);
            userDao.createUserInvite(userInvite);
        }
        // push消息
        userMessageDao.create(new UserMessage(userId, UserMessage.TYPE_SCORE_ADD, userId, "成为合伙人", "恭喜成为新的合伙人, 邀请用户注册并消费将返还部分牛人币到您的账户"));
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(userId, Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount != null) wxTempMsgService.sendParterWxMsg(thirdAccount);
    }

    // 建立邀请关系(非合伙人)
    public void setInviteRelation(User user, String code) {
        UserInvite inviter = userDao.findInviteByCode(code);
        if (inviter == null || !inviter.getState().equals(Const.PARTER_STATE_NORMAL))
            throw new ServerException(HttpStatus.BAD_REQUEST, "无效的邀请码");
        UserInvite userInvite = new UserInvite(user.getId(), false, inviter.getUserId(), inviter.getParterId());
        userDao.createUserInvite(userInvite);
        // push消息
        ThirdAccount inviterThird = userService.getThirdAccountByUserid(inviter.getUserId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (inviterThird != null) wxTempMsgService.sendInviteWxMsg(inviterThird);
        ThirdAccount parterThird = userService.getThirdAccountByUserid(inviter.getParterId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (parterThird != null) wxTempMsgService.sendInviteWxMsg(parterThird);
    }

    // 根据邀请关系分利
    @Transactional
    public void doBonusByInvite(User user, Integer costScore) {
        UserInvite userInvite = userDao.findInviteByUserId(user.getId());
        if (userInvite != null && !userInvite.getIsParter() && !userInvite.getParterId().equals(user.getId())) {
            Double parterBonus = costScore * Const.INVITE_PARTER_BONUS_SCALE;
            Double inviterBonus = costScore * Const.INVITE_INVITER_BONUS_SCALE;
            String dd = "受邀用户“" + user.getNickname() + "”消费返利";
            if (userInvite.getInviter().equals(userInvite.getParterId())) {
                BigDecimal decimal = BigDecimal.valueOf(parterBonus + inviterBonus).setScale(0, BigDecimal.ROUND_HALF_UP);
                Integer bonus = decimal.intValue();
                if (bonus > 0) {
                    userScoreDao.updateScore(userInvite.getInviter(), bonus);
                    userScoreHistoryDao.create(new UserScoreHistory(userInvite.getInviter(), Const.USER_SCORE_HISTORY_TYPE_INVITER_BONUS, bonus, user.getId(), dd));
                }
            } else {
                BigDecimal pbd = new BigDecimal(parterBonus).setScale(0, BigDecimal.ROUND_HALF_UP);
                Integer pBonus = pbd.intValue();
                if (pBonus > 0) {
                    userScoreDao.updateScore(userInvite.getParterId(), pBonus);
                    userScoreHistoryDao.create(new UserScoreHistory(userInvite.getParterId(), Const.USER_SCORE_HISTORY_TYPE_INVITER_BONUS, pBonus, user.getId(), dd));
                }
                BigDecimal ibd = new BigDecimal(inviterBonus).setScale(0, BigDecimal.ROUND_HALF_UP);
                Integer iBonus = ibd.intValue();
                if (iBonus > 0) {
                    userScoreDao.updateScore(userInvite.getInviter(), iBonus);
                    userScoreHistoryDao.create(new UserScoreHistory(userInvite.getInviter(), Const.USER_SCORE_HISTORY_TYPE_INVITER_BONUS, iBonus, user.getId(), dd));
                }
            }
        }
    }

    // 查询区域合伙人
    public Page<UserDto> parterPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = userDao.parterCount(params);
        List<UserDto> userDtos = count > 0 ? userDao.parterList(params) : new ArrayList<>();
        userDtos.forEach(userDto -> {
            userDto.setInviteNum(userDao.getInviteNum(userDto.getId()));
            userDto.setAreaNum(userDao.getAreaNum(userDto.getId()));
        });
        return new PageImpl<>(userDtos, pageable, count);
    }

    // 删除区域合伙人
    public void deleteParter(Long userId) {
        UserInvite userInvite = userDao.findInviteByUserId(userId);
        if (userInvite == null) throw new ServerException(HttpStatus.NOT_FOUND);
        userInvite.setIsParter(false);
        userInvite.setState(Const.PARTER_STATE_BAN);
        userInvite.setUpdateTime(Calendar.getInstance(Locale.CHINA).getTime());
        userDao.updateUserInvite(userInvite);
    }

}
