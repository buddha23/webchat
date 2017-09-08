package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.ModeratorDto;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.Moderator;
import com.lld360.cnc.repository.DocCategoryDao;
import com.lld360.cnc.repository.DocModeratorDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class DocCategoryService extends BaseService {

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private DocModeratorDao docModeratorDao;

    // 层级获取
    public List<DocCategory> getAll() {
        return docCategoryDao.findAll();
    }

    // 列表获取
    public List<DocCategory> getAllCategory() {
        return docCategoryDao.findAllCategory();
    }

    // 根据父ID获取，父ID为null的时候获取顶层类型
    public List<DocCategory> getByFid(Integer fid) {
        return docCategoryDao.findByFid(fid);
    }

    // 根据ID获取包括自己在内的所有父级列表
    public List<DocCategory> getSelfAndAllParents(int id) {
        return docCategoryDao.findSelfAndAllParents(id);
    }

    //首页推荐模块查询
    public List<DocCategory> getCategoryForIndex() {
        return docCategoryDao.findForIndex();
    }

    //删除旧图标
    private void deleteOldIcon(DocCategory category) {
        if (category.getIcon() != null) {
            File oldIcon = new File(configer.getFileBasePath() + category.getIcon());
            if (oldIcon.exists())
                FileUtils.deleteQuietly(oldIcon);
        }
    }

    // 保存类型
    public void save(DocCategory category, MultipartFile icon) {
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
                logger.warn("保存文档分类图标失败");
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
            }
        }
        if (category.getId() == null) {
            docCategoryDao.create(category);
        } else {
            docCategoryDao.update(category);
        }
    }

    public DocCategory get(Integer id) {
        return docCategoryDao.find(id);
    }

    public void delete(int id) {
        DocCategory category = docCategoryDao.find(id);
        if (category != null) {
            docCategoryDao.delete(id);
            deleteOldIcon(category);
        }
    }

    // 根据分类查询版主
    public List<ModeratorDto> getModeratorsByCategory(int categoryId) {
        return docModeratorDao.findModeratorsByCategory(categoryId);
    }

    // 根据版主查询版块
    public List<ModeratorDto> getModeratorsByModerator(long userId) {
        return docModeratorDao.findDocCategoriesByModerator(userId);
    }

    // 根据版主id查询非版主版块
    public List<DocCategory> getModeratorsByNotModerator(long userId) {
        List<ModeratorDto> moderatorDtos = docModeratorDao.findDocCategoriesByModerator(userId);
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(0);
        if (!moderatorDtos.isEmpty())
            for (ModeratorDto moderator : moderatorDtos) {
                ids.add(moderator.getCategoryId());
            }
        return docCategoryDao.findDocCategoriesByNotModerator(ids);
    }

    // 添加版主
    public ModeratorDto addModerator(Moderator moderator) {
        docModeratorDao.createModerator(moderator);
        return docModeratorDao.findModerator(moderator.getCategoryId(), moderator.getUserId());
    }

    // 删除版主
    public void deleteModerator(int categoryId, long userId) {
        docModeratorDao.deleteModerator(categoryId, userId);
    }

    // 是否版块版主
    public boolean isModerator(int categoryId, long userId) {
        return docModeratorDao.isCategoryModerator(categoryId, userId);
    }

    //是否文档版主
    public boolean isDocModerator(long docId, Long userId) {
        return docModeratorDao.isDocModerator(docId, userId);
    }

    //是否版主
    public boolean isNowModerator(Long userId) {
        return docModeratorDao.isNowModerator(Const.MODERATOR_STATE_NORMAL, userId);
    }

    //获取版主列表
    public Page<ModeratorDto> moderatorsPage(Map<String, Object> param) {
        Pageable pageable = getPageable(param);
        List<ModeratorDto> list = docModeratorDao.findModerators(param);
        long count = docModeratorDao.count4Moderators(param);
        return new PageImpl<>(list, pageable, count);
    }

    //修改版主
    public void updateModerators(ModeratorDto moderatorDto){
        docModeratorDao.updateModetators(moderatorDto);
    }
}
