package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Date;

/*版块日统计报表*/
public class DocCategoryDailyReport implements Serializable {

    private Integer id;
    /*
     * 统计时间
     * */
    private Date statisticsDate;

    /*
     * 版块Id
     * */
    private Integer categoryId;

    /*
     * 日浏览量
     * */
    private Long dailyViews;

    /*
     * 总浏览量
     * */
    private Long totleViews;

    /*
     * 日下载量
     * */
    private Long dailyDownloads;

    /*
     * 日上传量
     * */
    private Long dailyUploads;
    /*
   * 非数据库字段供页面展示
   * */
    private String lookTime;
    private String categoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Long getDailyViews() {
        return dailyViews;
    }

    public void setDailyViews(Long dailyViews) {
        this.dailyViews = dailyViews;
    }

    public Long getTotleViews() {
        return totleViews;
    }

    public void setTotleViews(Long totleViews) {
        this.totleViews = totleViews;
    }

    public Long getDailyDownloads() {
        return dailyDownloads;
    }

    public void setDailyDownloads(Long dailyDownloads) {
        this.dailyDownloads = dailyDownloads;
    }

    public Long getDailyUploadsdailyUploads() {
        return dailyUploads;
    }

    public void setDailyUploads(Long dailyUploads) {
        this.dailyUploads = dailyUploads;
    }

    public Long getDailyUploads() {
        return dailyUploads;
    }

    public String getLookTime() {
        return lookTime;
    }

    public void setLookTime(String lookTime) {
        this.lookTime = lookTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
