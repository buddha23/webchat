package com.lld360.cnc.repository;

import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.DocCategoryDailyReport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DocCategoryDailyReportDao {

    void create(DocCategoryDailyReport dailyReport);

    List<DocCategoryDailyReport> searchByParam(Map<String, Object> parmas);

    List<DocCategory> searchCategoryList();

    List<DocCategoryDailyReport> searchCategoryReport(Map<String, Object> params);

}
