package com.lld360.cnc.repository;

import com.lld360.cnc.model.DocCollect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface DocCollectDao {

    DocCollect find(Map<String, Object> parameters);

    void create(DocCollect doccollect);

    void delete(Map<String, Object> parameters);

    Long count(Map<String, Object> parameters);

    boolean isCollected(@Param("userId") long userId, @Param("docId") long docId);

}