package com.lld360.cnc.model;

import java.io.Serializable;
import java.sql.Date;

public class CncDaysStatistics implements Serializable {

    private Long id;
    private Integer articleViews;        //资料浏览量
    private Integer articleDownloads;    //资料下载量
    private Integer docViews;            //文库浏览量
    private Integer docDownloads;        //文库下载量
    private Date statisticsDate;  //统计日期
    private Date createDate;     //更改日期
    private Integer viewsSum;      //下载量总数

    @Override
    public String toString() {
        return "CncDaysStatistics{" +
                "id=" + id +
                ", articleViews=" + articleViews +
                ", articleDownloads=" + articleDownloads +
                ", docViews=" + docViews +
                ", docDownloads=" + docDownloads +
                ", statisticsDate=" + statisticsDate +
                ", createDate=" + createDate +
                ", viewsSum=" + viewsSum +
                '}';
    }

    public Integer getDocViews() {
        return docViews;
    }

    public void setDocViews(Integer docViews) {
        this.docViews = docViews;
    }

    public Integer getDocDownloads() {
        return docDownloads;
    }

    public void setDocDownloads(Integer docDownloads) {
        this.docDownloads = docDownloads;
    }

    public Integer getViewsSum() {
        return viewsSum;
    }

    public void setViewsSum(Integer viewsSum) {
        this.viewsSum = viewsSum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getArticleViews() {
        return articleViews;
    }

    public void setArticleViews(Integer articleViews) {
        this.articleViews = articleViews;
    }

    public Integer getArticleDownloads() {
        return articleDownloads;
    }

    public void setArticleDownloads(Integer articleDownloads) {
        this.articleDownloads = articleDownloads;
    }


    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
