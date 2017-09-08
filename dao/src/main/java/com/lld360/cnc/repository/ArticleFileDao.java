package com.lld360.cnc.repository;

import com.lld360.cnc.model.ArticleFile;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleFileDao {

    ArticleFile find(long id);

    void create(ArticleFile articleFile);

    int update(ArticleFile articleFile);

    void delete(long id);
}