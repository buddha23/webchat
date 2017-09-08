package com.lld360.cnc.service;

import java.math.BigDecimal;
import java.util.*;

import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.VodVolumesDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.enums.TradeState;

@Service
public class UserStatementService extends BaseService {

    @Autowired
    private UserStatementDao userStatementDao;

    @Autowired
    private UserStatementTempDao userStatementTempDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    private UserPointDao userPointDao;

    @Autowired
    private UserPointHistoryDao userPointHistoryDao;

    @Autowired
    private UserAccountDao userAccountDao;

    @Autowired
    private UserMemberDao userMemberDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private VodVolumesDao vodVolumesDao;

    @Autowired
    private VodBuysDao vodBuysDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoService videoService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    public void saveTempData(UserStatement userStatement) {
        userStatementTempDao.create(userStatement);
    }

    public UserStatement getTempData(String innerTradeNo) {
        return userStatementTempDao.findByInnerTradeNo(innerTradeNo);
    }


    public boolean save(String tradeNo, String innerTradeNo, BigDecimal amount) {
        Map<String, Object> map = new HashMap<>();
        map.put("innerTradeNo", innerTradeNo);
        map.put("tradeNo", tradeNo);
        long count = userStatementDao.count(map);
        if (count > 0) return true;
        map.remove("tradeNo");
        count = userStatementTempDao.count(map);
        if (count != 1) return false;
        UserStatement temp = userStatementTempDao.findByInnerTradeNo(innerTradeNo);
        if (null == temp || temp.getTradeAmount().compareTo(amount) != 0) return false;
        temp.setTradeNo(tradeNo);
        Long oldId = temp.getId();
        temp.setId(null);
        temp.setTradeState(TradeState.Succ.getValue());
        userStatementDao.create(temp);
        userStatementTempDao.delete(oldId);
        switch (temp.getTradeType()) {
            case Const.PAY_SCORE:
                return balanceScore(temp);
            case Const.PAY_VIP:
                return balanceVip(temp);
            case Const.PAY_VIDEO:
                return balanceVideo(temp);
            default:
                return false;
        }
    }

    //获取用户充值记录
    public List<UserStatement> getUserRecharge(Map<String, Object> params) {
        return userStatementDao.search(params);
    }

    public void deleteTemp(long TempId) {
        userStatementTempDao.delete(TempId);
    }

    public Page<UserStatement> getUserRechargePage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        List<UserStatement> list = userStatementDao.searchDetail(params);
        long count = userStatementDao.count4Detail(params);
        return new PageImpl<>(list, pageable, count);
    }

    private boolean balanceScore(UserStatement statement) {
        int score = statement.getBuyScore();
        boolean hasPresent = null != statement.getPresentScore() && statement.getPresentScore() > 0;
        score = hasPresent ? score + statement.getPresentScore() : score;
        userScoreDao.updateScore(statement.getUserId(), score);
        String ushMoneyDesc = "购买牛人币 " + statement.getTradeAmount() + " 元";
        userScoreHistoryDao.create(new UserScoreHistory(statement.getUserId(), Const.USER_SCORE_HISTORY_TYPE_RECHARGE, statement.getBuyScore(), statement.getId(), ushMoneyDesc));
        if (hasPresent) {
            String ushPresentDesc = "购买牛人币系统奖励";
            userScoreHistoryDao.create(new UserScoreHistory(statement.getUserId(), Const.USER_SCORE_HISTORY_TYPE_RECHARGE_PRESENT, statement.getPresentScore(), statement.getId(), ushPresentDesc));
        }
        giveUserPoint(statement);
        // push消息
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(statement.getUserId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        String msg = "亲爱的大牛用户,恭喜你充值成功,购买牛人币:" + statement.getTradeAmount() + "元";
        if (thirdAccount != null)
            wxTempMsgService.sendScoreChgWxMsg(thirdAccount, msg, score, userScoreDao.find(statement.getUserId()).getTotalScore());
        return true;
    }

    private boolean balanceVip(UserStatement statement) {
        UserMember member = userMemberDao.get(statement.getUserId(), (byte) 1);
        Date now = new Date();
        if (null == member) {
            member = new UserMember();
            member.setStartTime(now);
            member.setUpdateTime(now);
            member.setUserId(statement.getUserId());
            member.setState((byte) 1);
            member.setType((byte) 1);
            member.setEndTime(DateUtils.addYears(now, 1));
            userMemberDao.create(member);
        } else {
            member.setUpdateTime(now);
            member.setEndTime(member.getEndTime().before(now) ? DateUtils.addYears(now, 1) : DateUtils.addYears(member.getEndTime(), 1));
            userMemberDao.update(member);
        }
        userInviteService.doBonusByInvite(userService.getUser(statement.getUserId()), 100 * configer.getRechargeExchangeRate());    // 分利 花费100RMB = 1000牛人币
        userScoreHistoryDao.create(new UserScoreHistory(statement.getUserId(), Const.USER_SCORE_HISTORY_TYPE_BUYVIP, 0, statement.getId(), "花费100元，购买会员"));
        // push消息
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(member.getUserId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount != null) wxTempMsgService.sendMemberWxMsg(thirdAccount, member);

        return true;
    }

    private boolean balanceVideo(UserStatement statement) {
        if (statement.getObjectId() == null) {
            return false;
        }
        VodVolumesDto vodVolumes = vodVolumesDao.find(statement.getObjectId());
        Integer costScore = statement.getTradeAmount().intValue() * configer.getRechargeExchangeRate();
        // 购买记录
        VodBuys vodBuys = new VodBuys(vodVolumes.getId(), statement.getUserId(), costScore, "现金购买", Const.VOD_BUY_TYPE_CASH);
        vodBuysDao.create(vodBuys);
        // 加积分
        userPointDao.updatePoint(statement.getUserId(), costScore * Const.USER_ADDPOINT_SCALE_SCORE_OPERATE);
        String dd = "购买视频集《" + vodVolumes.getName() + "》扣除牛人币";
        userPointHistoryDao.create(new UserPointHistory(statement.getUserId(), Const.USER_POINT_HISTORY_TYPE_VOD_BUY, costScore * Const.USER_ADDPOINT_SCALE_SCORE_OPERATE, vodVolumes.getId().longValue(), dd));
        //  给邀请者和区域合伙人  讲师分利
        userInviteService.doBonusByInvite(userDao.findUserDto(statement.getUserId()), costScore);
        videoService.doLecturerBonus(vodVolumes,vodVolumes.getCostScore());
        // push消息
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(statement.getUserId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount != null) wxTempMsgService.sendVideoBuyWxMsg(thirdAccount, vodVolumes);
        return true;
    }

    //赠送站内积分
    private void giveUserPoint(UserStatement temp) {
        Integer score = temp.getBuyScore();
        userPointDao.updatePoint(temp.getUserId(), score * Const.USER_ADDPOINT_SCALE_RECHARGE);
        userPointHistoryDao.create(new UserPointHistory(temp.getUserId(), Const.USER_POINT_HISTORY_TYPE_RECHARGE_PRESENT, score * Const.USER_ADDPOINT_SCALE_RECHARGE, temp.getId(), "购买牛人币系统奖励"));
    }


    //获取用户账户列表
    public List<UserAccount> getAccountList(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        return userAccountDao.search(map);
    }

    public void addAccount(UserAccount userAccount) {
        userAccountDao.create(userAccount);
    }

    public boolean isExist(UserAccount userAccount) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", Const.NORMAL);
        map.put("userId", userAccount.getUserId());
        if (userAccount.getBankName() == null || userAccount.getBankName().equals("")) {
            map.put("accountType", Const.ALIPAY);
            return (userAccountDao.count(map) > 0);
        } else {
            map.put("account", userAccount.getAccount());
            map.put("accountType", Const.BANKCARD);
            return userAccountDao.isExist(map);
        }


    }

    @Transactional
    public void updateUserAccount(UserAccount userAccount) {
        UserAccount oldUserAccount = userAccountDao.find(userAccount.getId());
        if (isChanged(userAccount, oldUserAccount)) {
            oldUserAccount.setStatus(Const.EXPIRE);
            userAccountDao.update(oldUserAccount);
            userAccount.setStatus(Const.NORMAL);
            userAccountDao.create(userAccount);
        }
    }

    public void deleteAccount(long id) {
        UserAccount userAccount = userAccountDao.find(id);
        userAccount.setStatus(Const.ABANDON);
        userAccountDao.update(userAccount);
    }

    public UserAccount getAccount(long id) {
        return userAccountDao.find(id);
    }

    public List<UserStatement> getTempUserStatement(Map<String, Object> parameters) {
        return userStatementTempDao.search(parameters);
    }

    private boolean isChanged(UserAccount userAccount, UserAccount oldUserAccount) {
        boolean isChange = false;
        if (!oldUserAccount.getAccount().equals(userAccount.getAccount())) {
            isChange = true;
        }
        if (!oldUserAccount.getAccountName().equals(userAccount.getAccountName())) {
            isChange = true;
        }
        if (userAccount.getAccountType().equals(Const.BANKCARD) && !oldUserAccount.getBankName().equals(userAccount.getBankName())) {
            isChange = true;
        }
        return isChange;

    }

    public BigDecimal dailyPayCount() {
        return userStatementDao.dailyPayCount(Calendar.getInstance(Locale.CHINA).getTime());
    }

    public BigDecimal monthPayCount() {
        return userStatementDao.monthPayCount(Calendar.getInstance(Locale.CHINA).getTime());
    }
}
