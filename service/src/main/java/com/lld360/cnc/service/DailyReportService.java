package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.DailyStatisticsReport;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.DocCategoryDailyReport;
import com.lld360.cnc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DailyReportService extends BaseService {

    @Autowired
    private DocCategoryDailyReportDao categoryDailyReportDao;

    @Autowired
    private DailyStatisticsReportDao dailyStatisticsReportDao;

    @Autowired
    private DocDao docDao;

    @Autowired
    private DocDownloadDao docDownloadDao;

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private PostsCommentDao commentDao;

    @Autowired
    private UserLoginHistoryDao userLoginHistoryDao;

    @Autowired
    private UserStatementDao userStatementDao;

    @Autowired
    private UserClickHabitDao userClickHabitDao;

    @Autowired
    private VodVolumesDao vodVolumesDao;

    // 生成版块日报表
    public void createCategoryDailyReport() {
        // 获取所有一级版块
        List<DocCategory> docCategories = docCategoryDao.findAll();
        if (docCategories.isEmpty())
            return;
        docCategories.forEach(docCategory -> {
            DocCategoryDailyReport categoryDailyReport = new DocCategoryDailyReport();
            categoryDailyReport.setCategoryId(docCategory.getId());

            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            Map<String, Object> params = new HashMap<>();
            params.put("categoryId", docCategory.getId());
            // 获取上传量&下载量
            calendar.add(Calendar.DATE, -1);    // 凌晨2点启动所以-1
            categoryDailyReport.setStatisticsDate(calendar.getTime());
            params.put("searchTime", calendar.getTime());
            categoryDailyReport.setDailyDownloads(docDownloadDao.searchDownloads(params));
            categoryDailyReport.setDailyUploads(docDao.searchUploadNum(params));
            // 获取总浏览量
            Long totleviews = docDao.searchViewsByCategoryId(docCategory.getId());
            // 获取前一天浏览量
            List<DocCategoryDailyReport> lastDailyReports = categoryDailyReportDao.searchByParam(params);
            if (lastDailyReports.isEmpty()) { //无前一天记录
                categoryDailyReport.setDailyViews(totleviews);
            } else {
                categoryDailyReport.setDailyViews(totleviews - lastDailyReports.get(0).getTotleViews());
            }
            categoryDailyReport.setTotleViews(totleviews);
            // 插入数据
            categoryDailyReportDao.create(categoryDailyReport);
        });
    }

    // 生成其他日统计报表
    public void createDailyStatisticsReport() {
        DailyStatisticsReport dailyStatisticsReport = new DailyStatisticsReport();
        Calendar calendar = Calendar.getInstance(Locale.CHINA); // 统计时间
        Map<String, Object> params = new HashMap<>();
        calendar.add(Calendar.DATE, -1);    // 凌晨2点启动所以-1
        dailyStatisticsReport.setStatisticsDate(calendar.getTime());

        params.put("searchTime", calendar.getTime());
        params.put("platform", Const.REGIST_PLATFORM_WEB);
        dailyStatisticsReport.setPcLogins(userLoginHistoryDao.count(params));   // pc登录&注册
        dailyStatisticsReport.setPcRegists(userDao.count(params));

        params.replace("platform", Const.REGIST_PLATFORM_MOBILE);   // M端登录&注册
        dailyStatisticsReport.setMobileLogins(userLoginHistoryDao.count(params));
        dailyStatisticsReport.setMobileRegists(userDao.count(params));

        dailyStatisticsReport.setScoreBuy(userStatementDao.searchTotleMoney(params));   // 充值金额

        Long courseTotleViews = courseDao.searchViews(params);
        dailyStatisticsReport.setCourseTotleViews(courseTotleViews);   // 教程浏览量

        Long postTotleViews = postsDao.searchViews(params);
        dailyStatisticsReport.setPostTotleViews(postTotleViews);  // 问答浏览量

        Long videoTotleView = vodVolumesDao.searchViews(params);
        dailyStatisticsReport.setVideoTotleView(videoTotleView);    // 视频浏览量

        DailyStatisticsReport lastReport = dailyStatisticsReportDao.searchLastReport();
        if (lastReport == null) {
            dailyStatisticsReport.setCourseDailyViews(courseTotleViews);
            dailyStatisticsReport.setPostDailyViews(postTotleViews);
            dailyStatisticsReport.setVideoDailyView(videoTotleView);
        } else {
            dailyStatisticsReport.setCourseDailyViews(courseTotleViews - lastReport.getCourseTotleViews());
            dailyStatisticsReport.setPostDailyViews(postTotleViews - lastReport.getPostTotleViews());
            dailyStatisticsReport.setVideoDailyView(videoTotleView - lastReport.getVideoTotleView());
        }

        dailyStatisticsReport.setPostDailyIncrements(postsDao.count(params));   // 问答增长
        dailyStatisticsReport.setCommentDailyIncrement(commentDao.count(params));   // 评论增长

        dailyStatisticsReport.setDocDailyActive(userClickHabitDao.getDocActive(params));
        dailyStatisticsReport.setPostsDailyActive(userClickHabitDao.getPostsActive(params));
        dailyStatisticsReport.setSoftDailyActive(userClickHabitDao.getSoftActive(params));
        dailyStatisticsReport.setVideoDailyActive(userClickHabitDao.getVideoActive(params));
        dailyStatisticsReport.setCourseDailyActive(userClickHabitDao.getCourseActive(params));

        dailyStatisticsReportDao.create(dailyStatisticsReport);
    }

    // 查询日统计报表
    public List<DailyStatisticsReport> statisticsReports(Map<String, Object> params) {
        setParams(params);
        checkTime(params);
        return dailyStatisticsReportDao.searchPostsReport(params);
    }

    // 查询板块
    public List<DocCategory> searchCategoryList() {
        return categoryDailyReportDao.searchCategoryList();
    }

    // 查询板块日常报表
    public List<DocCategoryDailyReport> getCategoryDailyReport(Map<String, Object> params) {
        setParams(params);
        checkTime(params);
        return categoryDailyReportDao.searchCategoryReport(params);
    }

    // 整合参数
    private void setParams(Map<String, Object> params) {
        String unitsStr = "units";
        String searchTimeTypeStr = "searchTimeType";
        String unit = setValueIfEmptyKey(params, "unit", "DATE").toString();
        switch (unit) {
            case "DATE":
                params.put(unitsStr, "CONCAT(DATE(" + params.get(searchTimeTypeStr) + "))");
                break;
            case "MONTH":
                params.put(unitsStr, "CONCAT(YEAR(" + params.get(searchTimeTypeStr) + "), '-', (CASE WHEN MONTH(" + params.get(searchTimeTypeStr) + ") >= 10 THEN MONTH(" + params.get(searchTimeTypeStr) + ") WHEN MONTH(" + params.get(searchTimeTypeStr) + ") < 10 THEN CONCAT('0', MONTH(" + params.get(searchTimeTypeStr) + ")) END))");
                break;
            case "YEAR":
                params.put(unitsStr, "CONCAT(YEAR(" + params.get(searchTimeTypeStr) + "))");
                break;
            default:
                throw new ServerException(HttpStatus.BAD_REQUEST);
        }
    }
}
