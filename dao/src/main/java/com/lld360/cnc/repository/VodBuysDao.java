package com.lld360.cnc.repository;

import com.lld360.cnc.model.VodBuys;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * VodBuys 数据库操作
 */
@Repository
public interface VodBuysDao {

    List<VodBuys> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    VodBuys find(Long id);

    void create(VodBuys vodbuys);

    int update(VodBuys vodbuys);

    void delete(Long id);

    boolean hasBuy(@Param("userId") Long userId, @Param("volumeId") Long volumeId);

}