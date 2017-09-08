package com.lld360.cnc.repository;

import com.lld360.cnc.model.DailyStatisticsReport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DailyStatisticsReportDao {

    void create(DailyStatisticsReport dailyStatisticsReport);

    List<DailyStatisticsReport> search(Map<String, Object> params);

    List<DailyStatisticsReport> searchPostsReport(Map<String, Object> params);

    DailyStatisticsReport searchLastReport();

}
