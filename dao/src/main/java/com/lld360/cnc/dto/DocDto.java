package com.lld360.cnc.dto;

import com.lld360.cnc.model.Doc;

public class DocDto extends Doc {

    private String uploaderName;            //上传者姓名

    private String downloadTime;            //下载时间

    private String collectTime;             //搜藏时间

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }
}
