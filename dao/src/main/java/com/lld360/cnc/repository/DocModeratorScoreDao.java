package com.lld360.cnc.repository;

import com.lld360.cnc.model.ModeratorScoreHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface DocModeratorScoreDao {

    void createModetatorScore(@Param("userId") Long userId, @Param("contributionScore") Integer contributionScore);

    void updateModetatorScore(@Param("userId") Long userId, @Param("scoreChange") Integer socreChange);

    boolean existModeratorScore(Long userId);

    void createModeratorScoreHistory(ModeratorScoreHistory moderatorScoreHistory);

    //查询日贡献值
    long getDailyScore(@Param("userId") Long userId, @Param("type") byte type, @Param("oneday") Date oneday);

    //查询某月贡献值
    long getMonthScore(@Param("userId") Long userId, @Param("oneday") Date oneday);

    List<ModeratorScoreHistory> getModeratorScoreHistory(Map<String,Object> params);

    long count(Map<String,Object> params);

    //查询某版主所有月贡献值
    List<ModeratorScoreHistory> getAllMonthScore(Map<String,Object> params);
    long count4MonthScore(Map<String,Object> params);
}
