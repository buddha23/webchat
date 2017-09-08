package com.lld360.cnc.dto;

import com.lld360.cnc.model.ModeratorLog;

/**
 * Author: dhc
 * Date: 2016-12-01 13:39
 */
public class ModeratorLogDto extends ModeratorLog {
    private String moderatorName;

    public String getModeratorName() {
        return moderatorName;
    }

    public void setModeratorName(String moderatorName) {
        this.moderatorName = moderatorName;
    }
}
