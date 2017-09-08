package com.lld360.cnc.admin.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.dto.ReportDataDto;
import com.lld360.cnc.dto.UserLoginHistoryDto;
import com.lld360.cnc.model.ReportData;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.UserStatement;
import com.lld360.cnc.repository.UserDao;
import com.lld360.cnc.repository.UserScoreHistoryDao;
import com.lld360.cnc.service.DailyReportService;
import com.lld360.cnc.service.UserLoginHistoryService;
import com.lld360.cnc.service.UserReportService;
import com.lld360.cnc.service.UserStatementService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequiresPermissions("report")
@RequestMapping("admin/userreport")
public class AdmUserReportController extends BaseController {

    @Autowired
    UserDao userDao;

    @Autowired
    private UserReportService userReportService;

    @Autowired
    private UserStatementService userStatementService;

    @Autowired
    private UserLoginHistoryService userLoginHistoryService;

    @Autowired
    private DailyReportService dailyReportService;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<UserDto> wordPageGet() {
        Map<String, Object> params = getParamsPageMap(15);
        if (params.get("sortBy") == null) {
            params.put("sortBy", "create_time");
            params.put("sortType", "desc");
        }
        return userReportService.search(params);
    }

    @RequestMapping(value = "scorePay", method = RequestMethod.GET)
    public Page<UserStatement> scorePayGet() {
        Map<String, Object> params = getParamsPageMap();
        return userStatementService.getUserRechargePage(params);
    }

    @RequestMapping(value = "dailyPayCount", method = RequestMethod.GET)
    public BigDecimal dailyPayCount() {
        return userStatementService.dailyPayCount();
    }

    @RequestMapping(value = "monthPayCount", method = RequestMethod.GET)
    public BigDecimal monthPayCount() {
        return userStatementService.monthPayCount();
    }

    @RequestMapping(value = "adminGive", method = RequestMethod.GET)
    public Integer adminGive() {
        return userScoreHistoryDao.getAdminGive();
    }

    // 用户注册报表
    @RequestMapping(value = "userRegistReport", method = RequestMethod.GET)
    public List<ReportDataDto> userRegistReport() {
        // 参数
        Map<String, Object> params = getParamsPageMap();
        params.put("searchTimeType", "create_time");
        params.put("platform", Const.REGIST_PLATFORM_WEB);
        // 数据
        List<ReportData> webRegists = userReportService.findUserRegists(params);
        params.replace("platform", Const.REGIST_PLATFORM_MOBILE);
        List<ReportData> mobileRegists = userReportService.findUserRegists(params);
        // 获得x轴
        List<String> xs = new ArrayList<>();
        List<String> lookTimes = new ArrayList<>();
        setXs(xs, webRegists);
        setXs(xs, mobileRegists);
        if (xs.isEmpty()) return new ArrayList<>();
        for (String x : xs) {
            if (Collections.frequency(lookTimes, x) < 1) lookTimes.add(x);
        }
        Collections.sort(lookTimes);
        return getResultList(lookTimes, webRegists, mobileRegists);
    }

    @RequestMapping(value = "userRegisterNumer", method = RequestMethod.GET)
    public List<Integer> userRegisterNumer() {
        return userDao.searchRegistNum();
    }

    // 用户登录报表
    @RequestMapping(value = "userLoginReport", method = RequestMethod.GET)
    public List<ReportDataDto> userLoginReport() {
        // 参数
        Map<String, Object> params = getParamsPageMap();
        params.put("searchTimeType", "login_time");
        params.put("platform", Const.REGIST_PLATFORM_WEB);
        // 数据
        List<ReportData> webLogins = userReportService.findUserLogins(params);
        params.replace("platform", Const.REGIST_PLATFORM_MOBILE);
        List<ReportData> mobileLogins = userReportService.findUserLogins(params);
        // 获得x轴
        List<String> xs = new ArrayList<>();
        List<String> lookTimes = new ArrayList<>();
        setXs(xs, webLogins);
        setXs(xs, mobileLogins);
        if (xs.isEmpty()) return new ArrayList<>();
        for (String x : xs) if (Collections.frequency(lookTimes, x) < 1) lookTimes.add(x);
        Collections.sort(lookTimes);
        return getResultList(lookTimes, webLogins, mobileLogins);
    }

    // 整合X轴
    private static void setXs(List<String> xs, List<ReportData> list) {
        if (!list.isEmpty()) {
            list.forEach(reportData -> xs.add(reportData.getLookTime()));
        }
    }

    /* 获取相同时间PC端和M端人数list<ReportDataDto>
    * params: looktimeList
     * weblist
      * mobileList
      * */
    private static List<ReportDataDto> getResultList(List<String> lookTimes, List<ReportData> webList, List<ReportData> mobileList) {
        List<ReportDataDto> resultList = new ArrayList<>();
        for (String x : lookTimes) {
            ReportDataDto reportDataDto = new ReportDataDto();
            reportDataDto.setPcNums(0L);
            reportDataDto.setMobileNum(0L);
            reportDataDto.setLookTime(x);
            resultList.add(reportDataDto);

            for (ReportData dto : webList) {
                if (x.equals(dto.getLookTime())) {
                    reportDataDto.setPcNums(dto.getUserNum());
                    break;
                }
            }
            for (ReportData dto : mobileList) {
                if (x.equals(dto.getLookTime())) {
                    reportDataDto.setMobileNum(dto.getUserNum());
                    break;
                }
            }
        }
        return resultList;
    }

    //  用户登录列表
    @RequestMapping(value = "longinHistory", method = RequestMethod.GET)
    public Page<UserLoginHistoryDto> longinHistoryGet() {
        Map<String, Object> params = getParamsPageMap();
        return userLoginHistoryService.searchUserLoginReport(params);
    }

}
