package com.lld360.cnc.admin.quartz;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.VodSection;
import com.lld360.cnc.model.VodVolumes;
import com.lld360.cnc.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-12-28 17:07
 */
public class MediaQuartz {
    @Autowired
    private VideoService videoService;

    /**
     * 【定时任务调度】
     * 向阿里云服务器同步所有待处理视频集的处理中视频状态
     */
    public void queryMediaState() {
        Map<String, Object> params = new HashMap<>();
        params.put("state", Const.VIDEO_STATE_DEALING);
        params.put("sectionState", VodSection.ST_TRANSINT);
        List<VodVolumes> volumes = videoService.getVolumes(params);
        if (!volumes.isEmpty()) {
            for (VodVolumes volume : volumes) {
                videoService.queryMediaByVolume(volume);
            }
        }
        videoService.updateAllDealingVolumes();
    }
}
