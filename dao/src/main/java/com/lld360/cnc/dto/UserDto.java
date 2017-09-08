package com.lld360.cnc.dto;

import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserInvite;

import java.util.Date;

public class UserDto extends User {

    private Integer totalScore;
    private Integer totalPoint;
    private Integer totalIn;
    private Integer totalOut;
    private Boolean isBind;
    private Integer loginType;
    private String openid;
    private String qqName;
    private String weixinName;
    private UserInvite userInvite;
    private Long inviteNum;
    private Long areaNum;

    private boolean isMember;
    private Date memberStartTime;
    private Date memberEndTime;

    private Boolean isLecturer;
    private Boolean isModerator;

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(Integer totalIn) {
        this.totalIn = totalIn;
    }

    public Integer getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(Integer totalOut) {
        this.totalOut = totalOut;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Boolean getIsBind() {
        return isBind;
    }

    public void setIsBind(Boolean bind) {
        isBind = bind;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getQqName() {
        return qqName;
    }

    public void setQqName(String qqName) {
        this.qqName = qqName;
    }

    public String getWeixinName() {
        return weixinName;
    }

    public void setWeixinName(String weixinName) {
        this.weixinName = weixinName;
    }

    public UserInvite getUserInvite() {
        return userInvite;
    }

    public void setUserInvite(UserInvite userInvite) {
        this.userInvite = userInvite;
    }

    public Long getInviteNum() {
        return inviteNum;
    }

    public void setInviteNum(Long inviteNum) {
        this.inviteNum = inviteNum;
    }

    public Long getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(Long areaNum) {
        this.areaNum = areaNum;
    }

    public boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    public Date getMemberStartTime() {
        return memberStartTime;
    }

    public void setMemberStartTime(Date memberStartTime) {
        this.memberStartTime = memberStartTime;
    }

    public Date getMemberEndTime() {
        return memberEndTime;
    }

    public void setMemberEndTime(Date memberEndTime) {
        this.memberEndTime = memberEndTime;
    }

    public Boolean getIsLecturer() {
        return isLecturer;
    }

    public void setIsLecturer(Boolean isLecturer) {
        this.isLecturer = isLecturer;
    }

    public Boolean getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(Boolean isModerator) {
        this.isModerator = isModerator;
    }
}
