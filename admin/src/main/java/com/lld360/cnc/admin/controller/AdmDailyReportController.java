package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.DailyStatisticsReport;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.DocCategoryDailyReport;
import com.lld360.cnc.service.DailyReportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiresPermissions("report")
@RequestMapping("admin/dailyReport")
public class AdmDailyReportController extends AdmController {

    @Autowired
    DailyReportService dailyReportService;

    // 日常报表
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<DailyStatisticsReport> getList() {
        Map<String, Object> params = getParamsMap();
        params.put("searchTimeType", "statistics_date");
        return dailyReportService.statisticsReports(params);
    }

    @RequestMapping(value = "categoryList", method = RequestMethod.GET)
    public List<DocCategory> getcategoryList() {
        return dailyReportService.searchCategoryList();
    }

    // 板块日常报表
    @RequestMapping(value = "categoryReport", method = RequestMethod.GET)
    public ResponseEntity getCategoryReport() {
        List<List<DocCategoryDailyReport>> listList = new ArrayList<>();
        Map<String, Object> params = getParamsMap();
        params.put("searchTimeType", "statistics_date");
        Object ids = params.get("ids");
        String[] stringIds = null;
        if (ids instanceof String) {
            stringIds = new String[1];
            stringIds[0] = (String) ids;
        } else if (ids instanceof String[]) {
            stringIds = (String[]) params.get("ids");
        }
        if (stringIds == null || stringIds.length == 0 || stringIds.length > 9) {
            return new ResponseEntity<>("板块数目为0或是超出10个~", HttpStatus.FORBIDDEN);
        }
        try {
            for (String stringId : stringIds) {
                params.put("categoryId", Long.parseLong(stringId));
                listList.add(dailyReportService.getCategoryDailyReport(params));
            }
        } catch (Exception e) {
            throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(listList, HttpStatus.OK);
    }

}
