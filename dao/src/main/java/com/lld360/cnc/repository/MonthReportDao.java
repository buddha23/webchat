package com.lld360.cnc.repository;

import com.lld360.cnc.model.CncDaysStatistics;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MonthReportDao {

    List<CncDaysStatistics> search(Map<String, Object> parameters);

    List<CncDaysStatistics> totalSumsearch(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    //查询当天是否已有资料、文库统计量
    int isHaveStatisticsInfo(CncDaysStatistics cncDaysStatistics);

    //增加资料月统计信息
    void addArticleMonthStatistics(CncDaysStatistics cncDaysStatistics);

    //更新资料月统计资料浏览量信息

    void updateArticleMonthStatistics(CncDaysStatistics cncDaysStatistics);

    //更新资料月统计资料浏览量信息

    void updateArticleDownMonthStatistics(CncDaysStatistics cncDaysStatistics);

    //增加文库月统计信息
    void addDocMonthStatistics(CncDaysStatistics cncDaysStatistics);

    //更新文库月统计文库浏览量信息

    void updateDocViewsMonthStatistics(CncDaysStatistics cncDaysStatistics);

    //更新文库月统计文库下载量信息

    void updateDocDownloadsMonthStatistics(CncDaysStatistics cncDaysStatistics);
}