package com.lld360.cnc.dto;

import com.lld360.cnc.model.VodVolumes;

public class VodVolumesDto extends VodVolumes{
    /*
    * 上传者名字
    * */
    private String createrName;
    /*
    *  总课时
    *  */
    private Long totleNum;

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Long getTotleNum() {
        return totleNum;
    }

    public void setTotleNum(Long totleNum) {
        this.totleNum = totleNum;
    }
}
