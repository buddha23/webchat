package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.dto.ModeratorDto;
import com.lld360.cnc.dto.ModeratorLogDto;
import com.lld360.cnc.model.ModeratorScoreHistory;
import com.lld360.cnc.model.ModeratorLog;
import com.lld360.cnc.repository.CopyWritingDao;
import com.lld360.cnc.repository.DocModeratorDao;
import com.lld360.cnc.repository.DocModeratorLogDao;
import com.lld360.cnc.repository.DocModeratorScoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 版主操作日志处理
 */
@Service
public class ModeratorService extends BaseService {

    @Autowired
    private DocModeratorDao moderatorDao;

    @Autowired
    private DocModeratorLogDao moderatorLogDao;

    @Autowired
    private DocModeratorScoreDao moderatorScoreDao;

    @Autowired
    private CopyWritingDao copyWritingDao;

    // 添加操作日志
    public void add(ModeratorLog log) {
        Date now = Calendar.getInstance(Locale.CHINA).getTime();
        if (log.getCreateTime() == null) log.setCreateTime(now);
        // 增加贡献值
        long dailyScore = moderatorScoreDao.getDailyScore(log.getModeratorId(), Const.MODERATOR_CONTRIBUTIONS_TYPE_OPERATE, now);
        if (dailyScore < Const.MODERATOR_OPERATE_CONTRIBUTIONS_LIMIT) {
            if (moderatorScoreDao.existModeratorScore(log.getModeratorId())) {
                moderatorScoreDao.updateModetatorScore(log.getModeratorId(), Const.MODERATOR_OPERATE_CONTRIBUTIONS);
            } else {
                moderatorScoreDao.createModetatorScore(log.getModeratorId(), Const.MODERATOR_OPERATE_CONTRIBUTIONS);
            }
            ModeratorScoreHistory scoreHistory = new ModeratorScoreHistory(log.getModeratorId(), Const.MODERATOR_CONTRIBUTIONS_TYPE_OPERATE, Const.MODERATOR_SIGNIN_CONTRIBUTIONS, log.getObjectId(), "执行文档操作增加贡献值");
            moderatorScoreDao.createModeratorScoreHistory(scoreHistory);
        }
        moderatorLogDao.create(log);
    }

    // 分页获取操作日志
    public Page<ModeratorLogDto> getLogsByPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = moderatorLogDao.count(params);
        List<ModeratorLogDto> logDtoList = moderatorLogDao.search(params);
        return new PageImpl<>(logDtoList, pageable, count);
    }

    //删除操作日志
    public void delete(Long id) {
        moderatorLogDao.delete(id);
    }

    //获取版主积分
    public ModeratorDto getModeratorScore(long userId) {
        return moderatorDao.findModeratorByUserid(userId);
    }

    // 获取版主贡献值历史page
    public Page<ModeratorScoreHistory> getModeratorScoreHistory(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = moderatorScoreDao.count(params);
        List<ModeratorScoreHistory> moderatorScoreHistories = moderatorScoreDao.getModeratorScoreHistory(params);
        return new PageImpl<>(moderatorScoreHistories, pageable, count);
    }

    // 获取版主贡献值历史list
    public List<ModeratorScoreHistory> getHistoriesList(Map<String, Object> params) {
        return moderatorScoreDao.getModeratorScoreHistory(params);
    }

    // 获取版主月贡献值历史
    public Page<ModeratorScoreHistory> getModeratorMonthScore(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = moderatorScoreDao.count4MonthScore(params);
        List<ModeratorScoreHistory> moderatorScoreHistories = moderatorScoreDao.getAllMonthScore(params);
        return new PageImpl<>(moderatorScoreHistories, pageable, count);
    }

    public Boolean isModerator(Long userId) {
        return moderatorDao.isNowModerator(Const.MODERATOR_STATE_NORMAL, userId);
    }

}
