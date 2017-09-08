package com.lld360.cnc.repository;

import com.lld360.cnc.model.ReportData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReportDao {

    List<ReportData> searchRegistReport(Map<String, Object> params);

}
