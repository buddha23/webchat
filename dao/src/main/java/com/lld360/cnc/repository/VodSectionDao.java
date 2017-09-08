package com.lld360.cnc.repository;

import com.lld360.cnc.model.VodSection;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * VodSection 数据库操作
 */
@Repository
public interface VodSectionDao {

    List<VodSection> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    VodSection find(Long id);

    VodSection getFirstSectionByVolumesId(long volumesId);

    List<VodSection> findByState(byte state);

    List<VodSection> findByVolume(@Param("volumeId") int volumeId, @Param("state") Byte state);

    void create(VodSection vodsection);

    int update(VodSection vodsection);

    void delete(Long id);

    /**
     * 删除章中不再包含（即除开ids中）的节
     *
     * @param chapterId 所属章
     * @param ids       需要排除掉（不删除）的节
     */
    void deleteByNotBelongChapter(@Param("chapterId") int chapterId, @Param("ids") long[] ids);
}