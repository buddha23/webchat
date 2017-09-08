package com.lld360.cnc.repository;

import com.lld360.cnc.dto.UserLoginHistoryDto;
import com.lld360.cnc.model.ReportData;
import com.lld360.cnc.model.UserLoginHistory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserLoginHistoryDao {

    void create(UserLoginHistory userLoginHistory);

    List<UserLoginHistoryDto> search(Map<String, Object> params);

    long count(Map<String, Object> params);

    List<UserLoginHistoryDto> searchUserLoginHistory(Map<String, Object> params);

    long searchCount4LoginHistory();

    List<ReportData> searchLoginReport(Map<String, Object> params);

}
