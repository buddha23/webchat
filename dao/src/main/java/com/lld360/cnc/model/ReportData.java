package com.lld360.cnc.model;

import java.io.Serializable;

public class ReportData implements Serializable {

    /*
    * 查看时间
    * */
    private String lookTime;
    /*
    * 人数
    * */
    private Long userNum;

    public String getLookTime() {
        return lookTime;
    }

    public void setLookTime(String lookTime) {
        this.lookTime = lookTime;
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }
}
