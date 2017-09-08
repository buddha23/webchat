package com.lld360.cnc.dto;

import com.lld360.cnc.model.VodVolumes;

/**
 * 视频集扩展类
 */
public class VolumeDto extends VodVolumes {
    // 章总数
    private Integer chapterCount;
    // 节总数
    private Integer sectionCount;
    // 购买数
    private Integer buysCount;

    public Integer getChapterCount() {
        return chapterCount;
    }

    public void setChapterCount(Integer chapterCount) {
        this.chapterCount = chapterCount;
    }

    public Integer getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(Integer sectionCount) {
        this.sectionCount = sectionCount;
    }

    public Integer getBuysCount() {
        return buysCount;
    }

    public void setBuysCount(Integer buysCount) {
        this.buysCount = buysCount;
    }
}
