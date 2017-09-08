package com.lld360.cnc.repository;

import com.lld360.cnc.model.DocCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DocCategory 数据库操作
 */
@Repository
public interface DocCategoryDao {

    DocCategory find(int id);

    List<DocCategory> findAll();

    List<DocCategory> findAllCategory();

    List<DocCategory> find4SiteMap();

    List<DocCategory> findForIndex();

    DocCategory findByName(String name);

    List<DocCategory> findByFid(Integer fid);

    List<DocCategory> findSelfAndAllParents(int id);

    void create(DocCategory doccategory);

    int update(DocCategory doccategory);

    void delete(int id);

    List<DocCategory> findDocCategoriesByNotModerator(List<Integer> ids);

}