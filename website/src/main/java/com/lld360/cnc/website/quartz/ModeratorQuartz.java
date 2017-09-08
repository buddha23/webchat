package com.lld360.cnc.website.quartz;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.Moderator;
import com.lld360.cnc.model.UserMessage;
import com.lld360.cnc.model.UserScoreHistory;
import com.lld360.cnc.repository.DocModeratorDao;
import com.lld360.cnc.repository.DocModeratorScoreDao;
import com.lld360.cnc.repository.UserScoreDao;
import com.lld360.cnc.repository.UserScoreHistoryDao;
import com.lld360.cnc.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
 * Author: yangb
 * Date: 2017-2-16 10:08:57
* */
public class ModeratorQuartz {

    @Autowired
    UserScoreDao userScoreDao;

    @Autowired
    UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    DocModeratorDao moderatorDao;

    @Autowired
    DocModeratorScoreDao moderatorScoreDao;

    @Autowired
    UserMessageService userMessageService;

    //发工资
    @Transactional
    public void checkModeratorScore() {
        Date now = new Date(Calendar.getInstance(Locale.CHINA).getTimeInMillis() - 60 * 60 * 1000);
        Integer month = Calendar.getInstance().get(Calendar.MONTH);

        List<Moderator> moderators = moderatorDao.findAllModerators();
        moderators.forEach(moderator -> {
            long score = moderatorScoreDao.getMonthScore(moderator.getUserId(), now);
            UserMessage userMessage = new UserMessage();
            String message;
            if (month == 0) {
                message = "亲爱的版主，您12月份的贡献值为【" + score + "】，";
            } else {
                message = "亲爱的版主，您" + month + "月份的贡献值为【" + score + "】，";
            }

            if (score < Const.MODERATOR_SALARY_NEED_SCORE) {
                message = message + "暂未达到积分兑换要求，继续加油吧~";
                userMessage = new UserMessage(moderator.getUserId(), UserMessage.TYPE_MODERATOR_SCORE_NOTENOUGH, moderator.getUserId(), "月贡献值不足", message);
            } else {
                message = message + "已达到积分领取要求，赠送【" + Const.MODERATOR_SALARY + "】积分，再接再厉吧~";
                userMessage = new UserMessage(moderator.getUserId(), UserMessage.TYPE_MODERATOR_SCORE_ENOUGH, moderator.getUserId(), "月贡献值兑换积分", message);
                userScoreDao.updateScore(moderator.getUserId(), Const.MODERATOR_SALARY);
                userScoreHistoryDao.create(new UserScoreHistory(moderator.getUserId(), Const.USER_SOCRE_HISTORY_TYPE_MODERATOR_SALARY, Const.MODERATOR_SALARY, moderator.getUserId(), "版主月贡献值兑换积分"));
            }
            userMessageService.add(userMessage);
        });
    }
}
