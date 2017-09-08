package com.lld360.cnc.repository;

import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.lld360.cnc.model.SoftCollect;


@Repository
public interface SoftCollectDao {
	SoftCollect find(Map<String, Object> parameters);

    void create(SoftCollect softCollect);

    void delete(Map<String, Object> parameters);

    Long count(Map<String, Object> parameters);

    boolean isCollected(@Param("userId") long userId, @Param("uuId") long uuId);
}
