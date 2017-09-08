package com.lld360.cnc.dto;

import com.lld360.cnc.model.DocCategory;

import java.util.List;

/**
 * 带版主信息的文档分类
 */
public class DocCategoryModerators extends DocCategory {
    private List<ModeratorDto> moderatorDtos;

    public List<ModeratorDto> getModeratorDtos() {
        return moderatorDtos;
    }

    public void setModeratorDtos(List<ModeratorDto> moderatorDtos) {
        this.moderatorDtos = moderatorDtos;
    }
}
