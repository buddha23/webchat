package com.lld360.cnc.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

public class UserSignin implements Serializable {

    private Long id;

    @NotBlank(message = "userId 不能为空。")
    private Long userId;

    /*
    * 签到增加积分
    * */
    @NotBlank(message = "增加积分不能为空")
    private Integer scoreAdd;
    /*
    * 签到时间
    * */
    @NotBlank(message = "签到时间不能为空")
    private Date signinTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getScoreAdd() {
        return scoreAdd;
    }

    public void setScoreAdd(Integer scoreAdd) {
        this.scoreAdd = scoreAdd;
    }

    public Date getSigninTime() {
        return signinTime;
    }

    public void setSigninTime(Date signinTime) {
        this.signinTime = signinTime;
    }
}
