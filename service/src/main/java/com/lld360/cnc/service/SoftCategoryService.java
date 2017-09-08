package com.lld360.cnc.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.SoftCategory;
import com.lld360.cnc.repository.SoftCategoryDao;

@Service
public class SoftCategoryService extends BaseService{

	@Autowired
	private SoftCategoryDao softCategoryDao;

    // 根据父ID获取，父ID为null的时候获取顶层类型
    public List<SoftCategory> getByFid(Integer fid) {
        return softCategoryDao.findByFid(fid);
    }

	public SoftCategory get(Integer id) {
		return softCategoryDao.find(id);
	}
	
    public void save(SoftCategory category, MultipartFile icon) {
        if (icon != null) {
            String relativeFile = "softCategory/icon_" + RandomStringUtils.randomAlphanumeric(6)
                    + "." + FilenameUtils.getExtension(icon.getOriginalFilename());
            String absoluteFile = configer.getFileBasePath() + relativeFile;
            deleteOldIcon(category);
            try {
                File f = new File(absoluteFile);
                if (f.getParentFile().exists() || f.getParentFile().mkdirs())
                    icon.transferTo(f);
                category.setIcon(relativeFile);
            } catch (IOException e) {
                logger.warn("保存文档分类图标失败");
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
            }
        }
        if (category.getId() == null) {
            softCategoryDao.create(category);
        } else {
            softCategoryDao.update(category);
        }
    }
    
    public void delete(int id) {
        SoftCategory category = softCategoryDao.find(id);
        if (category != null) {
            softCategoryDao.delete(id);
            deleteOldIcon(category);
        }
    }

    
    //删除旧图标
    private void deleteOldIcon(SoftCategory category) {
        if (category.getIcon() != null) {
            File oldIcon = new File(configer.getFileBasePath() + category.getIcon());
            if (oldIcon.exists())
                FileUtils.deleteQuietly(oldIcon);
        }
    }

}
