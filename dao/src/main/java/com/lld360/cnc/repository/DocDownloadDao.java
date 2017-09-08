package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.DocDownload;

@Repository
public interface DocDownloadDao {

    List<DocDownload> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    long countByDoc(long docId);

    DocDownload find(Long id);

    boolean hasDownload(@Param("userId") Long userId, @Param("docId") Long docId);

    void create(DocDownload docdownload);

    int update(DocDownload docdownload);

    void delete(Long id);

    long searchDownloads(Map<String, Object> parameters);
}