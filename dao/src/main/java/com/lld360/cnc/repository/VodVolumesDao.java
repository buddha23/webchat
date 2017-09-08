package com.lld360.cnc.repository;

import com.lld360.cnc.dto.VodVolumesDto;
import com.lld360.cnc.dto.VolumeDto;
import com.lld360.cnc.model.VodVolumes;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * VodVolumes 数据库操作
 */
@Repository
public interface VodVolumesDao {

    List<VodVolumesDto> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    List<VolumeDto> searchVolumeDto(Map<String, Object> params);

    int countVolumeDto(Map<String, Object> params);

    VodVolumesDto find(Integer id);

    VodVolumes findWithSections(int id);

    List<VodVolumes> searchWithSections(Map<String, Object> params);

    void create(VodVolumes vodvolumes);

    int update(VodVolumes vodvolumes);

    int updateState(@Param("id") int id, @Param("state") byte state);

    int updateStateBySections(int id);

    int updateAllStateBySections();

    void delete(Long id);

    VodVolumes getVolumeBySectionId(Long sectionId);

    void addViews(@Param("id") int id);

    Long searchViews(Map<String, Object> params);

}