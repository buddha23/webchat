package com.lld360.cnc.website.quartz;

import com.lld360.cnc.service.SearchWordsService;
import com.lld360.cnc.service.WorkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Author: James
 * Date: 2016-11-22 19:40
 */
public class SiteMapQuartz {

    private static final String SITE_MAP_FILE = "sitemap.xml";
    @Autowired
    private ServletContext context;

    @Autowired
    private WorkService workService;

    // 初始化完成需要执行的方法
//    public void doInitTask() {
//        genSiteMapFile();
//    }

    // 更新热搜词
    public void genSiteMapFile() {
        String websiteRootPath = context.getRealPath("/");
        if(StringUtils.isEmpty(websiteRootPath))
        {
            return;
        }
        String siteMapFilePath = websiteRootPath + SITE_MAP_FILE;
        workService.createSiteMapToFile(siteMapFilePath);
    }

}
