package com.lld360.cnc.repository;

import com.lld360.cnc.dto.ModeratorLogDto;
import com.lld360.cnc.model.ModeratorLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * ModeratorLog 数据库操作
 */
@Repository
public interface DocModeratorLogDao {

    List<ModeratorLogDto> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    ModeratorLogDto find(Long id);

    void create(ModeratorLog moderatorlog);

    int update(ModeratorLog moderatorlog);

    void delete(Long id);

}