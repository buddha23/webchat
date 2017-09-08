package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/*用户牛人币*/
public class UserScore implements Serializable {

    @NotBlank(message = "userId 不能为空。")
    private Long userId;

    @NotBlank(message = "总牛币 不能为空。")
    private Integer totalScore;

    @NotBlank(message = "总收入币数 不能为空。")
    private Integer totalIn;

    @NotBlank(message = "总支出币数 不能为空。")
    private Integer totalOut;

    public UserScore() {
    }

    public UserScore(Long userId, Integer totalScore, Integer totalIn, Integer totalOut) {
        this.userId = userId;
        this.totalScore = totalScore;
        this.totalIn = totalIn;
        this.totalOut = totalOut;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
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
}