package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.repository.*;
import com.lld360.cnc.util.SiteMapUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * Created by clb on 2016/11/22.
 */
@Service
public class WorkService extends BaseService {

    @Autowired
    private SiteMapDao siteMapDao;

    public boolean createSiteMapToFile(String filePath) {
        return createSiteMapFile(filePath, createSiteMapData());
    }

    public String createSiteMapData() {
        return SiteMapUtil.buildSiteMapXmlStr(siteMapDao.findDoc(), siteMapDao.findDocCategory(), siteMapDao.findCourse(), siteMapDao.findPosts());
    }

    private boolean createSiteMapFile(String filePath, String xmlStr) {
        if (StringUtils.isEmpty(xmlStr)) return false;

        try {
            File temp = new File(filePath + ".temp");
            FileUtils.deleteQuietly(temp);

            FileUtils.writeStringToFile(temp, xmlStr, "UTF-8");
            if (temp.exists() && temp.length() > 0) {
                FileUtils.copyFile(temp, new File(filePath));
            }
            FileUtils.deleteQuietly(temp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
