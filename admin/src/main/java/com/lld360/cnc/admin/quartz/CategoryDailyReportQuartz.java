package com.lld360.cnc.admin.quartz;

import com.lld360.cnc.service.DailyReportService;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryDailyReportQuartz {

    @Autowired
    DailyReportService dailyReportService;

    public void createCategoryDailyReport() {
        dailyReportService.createCategoryDailyReport();
    }

    public void dailyStatisticsReport() {
        dailyReportService.createDailyStatisticsReport();
    }
}
