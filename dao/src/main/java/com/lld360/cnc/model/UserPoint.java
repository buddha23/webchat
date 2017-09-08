package com.lld360.cnc.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/*用户积分*/
public class UserPoint implements Serializable {

    @NotBlank(message = "userId 不能为空。")
    private Long userId;

    @NotBlank(message = "总分 不能为空。")
    private Integer totalPoint;

    @NotBlank(message = "总收入分数 不能为空。")
    private Integer totalIn;

    @NotBlank(message = "总支出分数 不能为空。")
    private Integer totalOut;

    private Date updateTime;

    public UserPoint() {

    }

    public UserPoint(Long userId, Integer totalPoint, Integer totalIn, Integer totalOut) {
        this.userId = userId;
        this.totalPoint = totalPoint;
        this.totalIn = totalIn;
        this.totalOut = totalOut;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
