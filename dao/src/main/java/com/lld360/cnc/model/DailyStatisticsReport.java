package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Date;

/*日统计报表*/
public class DailyStatisticsReport implements Serializable {
    /*
    * 统计时间
    * */
    private Date statisticsDate;
    /*
    * pc端登陆人数
    * */
    private Long pcLogins;
    /*
    * 手机端登陆人数
    * */
    private Long mobileLogins;
    /*
    * pc端注册人数
    * */
    private Long pcRegists;
    /*
    * 手机端注册人数
    * */
    private Long mobileRegists;
    /*
    * 积分购买(元)
    * */
    private Long scoreBuy;
    /*
    * 教程日浏览量
    * */
    private Long courseDailyViews;
    /*
    * 教程总浏览量
    * */
    private Long courseTotleViews;
    /*
    * 问答日浏览量
    * */
    private Long postDailyViews;
    /*
    * 问答总浏览量
    * */
    private Long postTotleViews;
    /*
    * 问题日增长量
    * */
    private Long postDailyIncrements;
    /*
    * 回答日增长量
    * */
    private Long commentDailyIncrement;
    /*
    * 文档日活跃人数
    * */
    private Long docDailyActive;
    /*
    * 问答日活跃人数
    * */
    private Long postsDailyActive;
    /*
    * 软件日活跃人数
    * */
    private Long softDailyActive;
    /*
    * 视频日活跃人数
    * */
    private Long videoDailyActive;
    /*
    * 教程日活跃人数
    * */
    private Long videoDailyView;
    /*
    * 教程日活跃人数
    * */
    private Long videoTotleView;
    /*
    * 教程日活跃人数
    * */
    private Long courseDailyActive;
    /*
    * 非数据库字段供页面展示
    * */
    private String lookTime;

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Long getPcLogins() {
        return pcLogins;
    }

    public void setPcLogins(Long pcLogins) {
        this.pcLogins = pcLogins;
    }

    public Long getMobileLogins() {
        return mobileLogins;
    }

    public void setMobileLogins(Long mobileLogins) {
        this.mobileLogins = mobileLogins;
    }

    public Long getPcRegists() {
        return pcRegists;
    }

    public void setPcRegists(Long pcRegists) {
        this.pcRegists = pcRegists;
    }

    public Long getMobileRegists() {
        return mobileRegists;
    }

    public void setMobileRegists(Long mobileRegists) {
        this.mobileRegists = mobileRegists;
    }

    public Long getScoreBuy() {
        return scoreBuy;
    }

    public void setScoreBuy(Long scoreBuy) {
        this.scoreBuy = scoreBuy;
    }

    public Long getCourseDailyViews() {
        return courseDailyViews;
    }

    public void setCourseDailyViews(Long courseDailyViews) {
        this.courseDailyViews = courseDailyViews;
    }

    public Long getCourseTotleViews() {
        return courseTotleViews;
    }

    public void setCourseTotleViews(Long courseTotleViews) {
        this.courseTotleViews = courseTotleViews;
    }

    public Long getPostDailyViews() {
        return postDailyViews;
    }

    public void setPostDailyViews(Long postDailyViews) {
        this.postDailyViews = postDailyViews;
    }

    public Long getPostTotleViews() {
        return postTotleViews;
    }

    public void setPostTotleViews(Long postTotleViews) {
        this.postTotleViews = postTotleViews;
    }

    public Long getPostDailyIncrements() {
        return postDailyIncrements;
    }

    public void setPostDailyIncrements(Long postDailyIncrements) {
        this.postDailyIncrements = postDailyIncrements;
    }

    public Long getCommentDailyIncrement() {
        return commentDailyIncrement;
    }

    public void setCommentDailyIncrement(Long commentDailyIncrement) {
        this.commentDailyIncrement = commentDailyIncrement;
    }

    public Long getDocDailyActive() {
        return docDailyActive;
    }

    public void setDocDailyActive(Long docDailyActive) {
        this.docDailyActive = docDailyActive;
    }

    public Long getPostsDailyActive() {
        return postsDailyActive;
    }

    public void setPostsDailyActive(Long postsDailyActive) {
        this.postsDailyActive = postsDailyActive;
    }

    public Long getSoftDailyActive() {
        return softDailyActive;
    }

    public void setSoftDailyActive(Long softDailyActive) {
        this.softDailyActive = softDailyActive;
    }

    public Long getVideoDailyActive() {
        return videoDailyActive;
    }

    public void setVideoDailyActive(Long videoDailyActive) {
        this.videoDailyActive = videoDailyActive;
    }

    public Long getCourseDailyActive() {
        return courseDailyActive;
    }

    public void setCourseDailyActive(Long courseDailyActive) {
        this.courseDailyActive = courseDailyActive;
    }

    public Long getVideoDailyView() {
        return videoDailyView;
    }

    public void setVideoDailyView(Long videoDailyView) {
        this.videoDailyView = videoDailyView;
    }

    public Long getVideoTotleView() {
        return videoTotleView;
    }

    public void setVideoTotleView(Long videoTotleView) {
        this.videoTotleView = videoTotleView;
    }

    public String getLookTime() {
        return lookTime;
    }

    public void setLookTime(String lookTime) {
        this.lookTime = lookTime;
    }
}
