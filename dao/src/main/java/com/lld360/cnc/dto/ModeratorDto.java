package com.lld360.cnc.dto;

import com.lld360.cnc.model.Moderator;

/**
 * 版主表附件信息
 */
public class ModeratorDto extends Moderator {
    private String name;
    private String nickname;
    private String mobile;
    private String qq;
    private String mailAddress;
    private String authorizerName;
    private String categoryName;
    private String categoryIcon;
    /*
    * 版主贡献值
    * */
    private Integer moderatorScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getAuthorizerName() {
        return authorizerName;
    }

    public void setAuthorizerName(String authorizerName) {
        this.authorizerName = authorizerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public Integer getModeratorScore() {
        return moderatorScore;
    }

    public void setModeratorScore(Integer moderatorScore) {
        this.moderatorScore = moderatorScore;
    }
}
