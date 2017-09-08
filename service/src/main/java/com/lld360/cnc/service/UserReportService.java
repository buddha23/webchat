package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.model.ReportData;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.repository.UserDao;
import com.lld360.cnc.repository.UserLoginHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserReportService extends BaseService {

    @Autowired
    private UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    private UserLoginHistoryDao userLoginHistoryDao;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Page<UserDto> search(Map<String, Object> params) {
        Object beginTime = params.get("beginTime");
        if (beginTime != null) {
            try {
                Date begin = format.parse(String.valueOf(beginTime));
            } catch (ParseException e) {
                params.remove("beginTime");
            }
        }
        Object endTime = params.get("endTime");
        if (endTime != null) {
            try {
                Date end = format.parse(String.valueOf(endTime));
            } catch (ParseException e) {
                params.remove("endTime");
            }
        }
        return userService.getUsersByPage(params);
    }

    // 查询用户注册时间/人数
    public List<ReportData> findUserRegists(Map<String, Object> params) {
        setParams(params);
        checkTime(params);
        return userDao.searchRegistReport(params);
    }

    // 查询用户登录时间/人数
    public List<ReportData> findUserLogins(Map<String, Object> params) {
        setParams(params);
        checkTime(params);
        return userLoginHistoryDao.searchLoginReport(params);
    }

    // 整合参数
    private void setParams(Map<String, Object> params) {
        String unit = setValueIfEmptyKey(params, "unit", "DATE").toString();

        switch (unit) {
            case "DATE":
                params.put("units", "CONCAT(DATE(" + params.get("searchTimeType") + "))");
                break;
            case "MONTH":
                params.put("units", "CONCAT(YEAR(" + params.get("searchTimeType") + "), '-', (CASE WHEN MONTH(" + params.get("searchTimeType") + ") >= 10 THEN MONTH(" + params.get("searchTimeType") + ") WHEN MONTH(" + params.get("searchTimeType") + ") < 10 THEN CONCAT('0', MONTH(" + params.get("searchTimeType") + ")) END))");
                break;
            case "YEAR":
                params.put("units", "CONCAT(YEAR(" + params.get("searchTimeType") + "))");
                break;
            default:
                break;
        }
    }

}
