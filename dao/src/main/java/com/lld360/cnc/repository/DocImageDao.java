package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.DocImage;

/**
 * DocImage 数据库操作
 */
@Repository
public interface DocImageDao{
	
    List<DocImage> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    DocImage find(Long id);

    List<DocImage> findByDocId(long docId);

    void create(DocImage docimage);

    int update(DocImage docimage);

    void delete(Long id);

    void deleteByDocId(long docId);

    void save(List<DocImage> images);
}