package com.lld360.cnc.dto;


import com.lld360.cnc.model.ReportData;

public class ReportDataDto extends ReportData{

    private Long pcNums;

    private Long mobileNum;

    public Long getPcNums() {
        return pcNums;
    }

    public void setPcNums(Long pcNums) {
        this.pcNums = pcNums;
    }

    public Long getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(Long mobileNum) {
        this.mobileNum = mobileNum;
    }
}
