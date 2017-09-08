package com.lld360.cnc.repository;

import com.lld360.cnc.model.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ArticleDao {

    Article find(long id);

    void create(Article article);

    void update(Article article);

    List<Article> findByKeyWord(@Param("keyword") String keyword);

    long countByKeyWord(@Param("keyword") String keyword);

    long countByMap(Map<String, Object> params);

    List<Article> findByMap(Map<String, Object> params);

    void delete(long id);

    //添加阅读量
    void addViews(long id);


}