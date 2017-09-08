package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.CncDaysStatistics;
import com.lld360.cnc.repository.MonthReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MonthReportService extends BaseService {

    @Autowired
    private MonthReportDao monthReportDao;


    public Page<CncDaysStatistics> search(Map<String, Object> parameters) {

        return getCncDaysStatisticses(parameters);
    }

    private Page<CncDaysStatistics> getCncDaysStatisticses(Map<String, Object> parameters) {
        parameters.put("state", Const.DOC_STATE_NORMAL);
        Pageable pageable = getPageable(parameters);
        long count = monthReportDao.count(parameters);
        List<CncDaysStatistics> userJobFeeList = monthReportDao.search(parameters);
        return new PageImpl<>(userJobFeeList, pageable, count);
    }

    //获取总数
    public Page<CncDaysStatistics> totalSumsearch(Map<String, Object> parameters) {

        List<CncDaysStatistics> userJobFeeList = monthReportDao.totalSumsearch(parameters);
        return new PageImpl<>(userJobFeeList);
    }

    //增加文章月统计访问信息
    public void  addArticleViews(){
        CncDaysStatistics cncDaysStatistics =new CncDaysStatistics();
        cncDaysStatistics.setArticleViews(1);
        cncDaysStatistics.setArticleDownloads(0);
        cncDaysStatistics.setDocViews(0);
        cncDaysStatistics.setDocDownloads(0);
        cncDaysStatistics.setStatisticsDate(new java.sql.Date(System.currentTimeMillis()));
        cncDaysStatistics.setCreateDate(new java.sql.Date(System.currentTimeMillis()));

        //查询当天是否已有资料、文库统计量
        int count=monthReportDao.isHaveStatisticsInfo(cncDaysStatistics);
        if(count==0) {
            monthReportDao.addArticleMonthStatistics(cncDaysStatistics);
        }else{   //更新浏览量或下载量
            monthReportDao.updateArticleMonthStatistics(cncDaysStatistics);
        }
    }


    public void  addArticleDownloads(){

        //查询当天是否已有资料、文库统计量
        CncDaysStatistics cncDaysStatistics =new CncDaysStatistics();
        cncDaysStatistics.setArticleViews(0);
        cncDaysStatistics.setArticleDownloads(1);
        cncDaysStatistics.setDocViews(0);
        cncDaysStatistics.setDocDownloads(0);
        cncDaysStatistics.setStatisticsDate(new java.sql.Date(System.currentTimeMillis()));
        cncDaysStatistics.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
        //查询当天是否已有资料、文库统计量
        int count=monthReportDao.isHaveStatisticsInfo(cncDaysStatistics);
        if(count==0) {
            monthReportDao.addDocMonthStatistics(cncDaysStatistics);
        }else{   //更新浏览量或下载量
            monthReportDao.updateArticleDownMonthStatistics(cncDaysStatistics);
        }
    }

    //增加文章月统计访问信息
    public void  addDocViews(){
        //查询当天是否已有资料、文库统计量
        CncDaysStatistics cncDaysStatistics =new CncDaysStatistics();
        cncDaysStatistics.setArticleViews(0);
        cncDaysStatistics.setArticleDownloads(0);
        cncDaysStatistics.setDocViews(1);
        cncDaysStatistics.setDocDownloads(0);
        cncDaysStatistics.setStatisticsDate(new java.sql.Date(System.currentTimeMillis()));
        cncDaysStatistics.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
        //查询当天是否已有资料、文库统计量
        int count=monthReportDao.isHaveStatisticsInfo(cncDaysStatistics);
        if(count==0) {
            monthReportDao.addDocMonthStatistics(cncDaysStatistics);
        }else{   //更新浏览量或下载量
            monthReportDao.updateDocViewsMonthStatistics(cncDaysStatistics);
        }
    }


    //增加文章月统计下载信息

    public void  addDocDownloads(){
        //查询当天是否已有资料、文库统计量
        CncDaysStatistics cncDaysStatistics =new CncDaysStatistics();
        cncDaysStatistics.setArticleViews(0);
        cncDaysStatistics.setArticleDownloads(0);
        cncDaysStatistics.setDocViews(0);
        cncDaysStatistics.setDocDownloads(1);
        cncDaysStatistics.setStatisticsDate(new java.sql.Date(System.currentTimeMillis()));
        cncDaysStatistics.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
        //查询当天是否已有资料、文库统计量
        int count=monthReportDao.isHaveStatisticsInfo(cncDaysStatistics);
        if(count==0) {
            monthReportDao.addDocMonthStatistics(cncDaysStatistics);
        }else{   //更新浏览量或下载量
            monthReportDao.updateDocDownloadsMonthStatistics(cncDaysStatistics);
        }
    }

}
