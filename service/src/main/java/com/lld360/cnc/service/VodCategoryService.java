package com.lld360.cnc.service;


import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.VodCategory;
import com.lld360.cnc.repository.VodCategoryDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class VodCategoryService  extends BaseService {

    @Autowired
    VodCategoryDao vodCategoryDao;

    //删除旧图标
    private void deleteOldIcon(VodCategory category) {
        if (category.getIcon() != null) {
            File oldIcon = new File(configer.getFileBasePath() + category.getIcon());
            if (oldIcon.exists())
                FileUtils.deleteQuietly(oldIcon);
        }
    }

    // 保存类型
    public void save(VodCategory category, MultipartFile icon) {
        if (icon != null) {
            String relativeFile = "docCategory/icon_" + RandomStringUtils.randomAlphanumeric(6)
                    + "." + FilenameUtils.getExtension(icon.getOriginalFilename());
            String absoluteFile = configer.getFileBasePath() + relativeFile;
            deleteOldIcon(category);
            try {
                File f = new File(absoluteFile);
                if (f.getParentFile().exists() || f.getParentFile().mkdirs())
                    icon.transferTo(f);
                category.setIcon(relativeFile);
            } catch (IOException e) {
                logger.warn("保存视频分类图标失败");
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
            }
        }
        if (category.getId() == null) {
            vodCategoryDao.create(category);
        } else {
            vodCategoryDao.update(category);
        }
    }

    public VodCategory get(Integer id) {
        return vodCategoryDao.findById(id);
    }

    public void delete(int id) {
        VodCategory category = vodCategoryDao.findById(id);
        if (category != null) {
            vodCategoryDao.delete(id);
            deleteOldIcon(category);
        }
    }

    // 根据父ID获取，父ID为null的时候获取顶层类型
    public List<VodCategory> getByFid(Integer fid) {
        return vodCategoryDao.findByFid(fid);
    }


}
