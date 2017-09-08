package com.lld360.cnc.repository;

import com.lld360.cnc.model.VodChapters;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * VodChapters 数据库操作
 */
@Repository
public interface VodChaptersDao {

    List<VodChapters> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    long sectionCount(Map<String, Object> parameters);

    VodChapters find(Long id);

    void create(VodChapters vodchapters);

    int update(VodChapters vodchapters);

    void delete(Long id);

    /**
     * 删除视频集中不再包含（即除开ids中）的章
     *
     * @param volumeId 所属视频集
     * @param ids      需要排除掉（不删除）的章
     */
    void deleteByNotBelongVolume(@Param("volumeId") int volumeId, @Param("ids") long[] ids);
}