package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.ModeratorDto;
import com.lld360.cnc.dto.PostsCategoryDto;
import com.lld360.cnc.model.Moderator;
import com.lld360.cnc.model.PostsCategory;
import com.lld360.cnc.repository.PostsCategoryDao;
import com.lld360.cnc.repository.PostsModeratorDao;
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
public class PostsCategoryService extends BaseService {

    @Autowired
    private PostsCategoryDao postsCategoryDao;

    @Autowired
    private PostsModeratorDao postsModeratorDao;

    public List<PostsCategory> getAllCategories() {
        return postsCategoryDao.getAllCategories();
    }

    public PostsCategory get(Integer id) {
        return postsCategoryDao.getById(id);
    }

    public void delete(Integer id) {
        postsCategoryDao.delete(id);
    }

    // 保存类型
    public PostsCategory save(PostsCategory category, MultipartFile icon) {
        if (icon != null) {
            String relativeFile = "postsCategory/icon_" + RandomStringUtils.randomAlphanumeric(6)
                    + "." + FilenameUtils.getExtension(icon.getOriginalFilename());
            String absoluteFile = configer.getFileBasePath() + relativeFile;
            deleteOldIcon(category);
            try {
                File f = new File(absoluteFile);
                if (f.getParentFile().exists() || f.getParentFile().mkdirs())
                    icon.transferTo(f);
                category.setIcon(relativeFile);
            } catch (IOException e) {
                logger.warn("保存问答分类图标失败");
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
            }
        }
        if (category.getId() == null) {
            postsCategoryDao.create(category);
        } else {
            postsCategoryDao.update(category);
        }
        return category;
    }

    //删除旧图标
    private void deleteOldIcon(PostsCategory category) {
        if (category.getIcon() != null) {
            File oldIcon = new File(configer.getFileBasePath() + category.getIcon());
            if (oldIcon.exists())
                FileUtils.deleteQuietly(oldIcon);
        }
    }

    public List<PostsCategoryDto> getAllCategories4Wap() {
        return postsCategoryDao.getAllCategories4Wap();
    }

    public PostsCategoryDto getDtoById(Integer id) {
        return postsCategoryDao.getDtoById(id);
    }

    // 根据分类查询版主
    public List<ModeratorDto> getModeratorsByCategory(int categoryId) {
        return postsModeratorDao.findModeratorsByCategory(categoryId);
    }

    public ModeratorDto findModerator(int categoryId, long userId) {
        return postsModeratorDao.findModerator(categoryId, userId);
    }

    public ModeratorDto addModerator(Moderator moderator) {
        postsModeratorDao.create(moderator);
        return findModerator(moderator.getCategoryId(), moderator.getUserId());
    }

    public void updateModerator(Moderator moderator) {
        postsModeratorDao.update(moderator);
    }

    // 删除版主
    public void deleteModerator(int categoryId, long userId) {
        postsModeratorDao.delete(categoryId, userId);
    }

    public List<PostsCategory> getCategoriesByUser(Long userId) {
        return postsCategoryDao.getCategoriesByModerator(userId);
    }

}
