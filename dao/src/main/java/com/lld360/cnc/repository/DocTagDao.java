package com.lld360.cnc.repository;

import com.lld360.cnc.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DocTagDao {
    void create(DocTag tag);

    DocTag find(String tag);

    List<DocTag> findAll(Integer limit);

    void createDocXTag(DocXTag docXTag);

    void createDocXTags(List<DocXTag> docXTags);

    int deleteDocXTagsByDocId(long docId);

    List<DocTag> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    DocTag findById(Integer id);

    boolean delete(DocTag docTag);

    int update(DocTag docTag);
}