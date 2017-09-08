package com.lld360.cnc.repository;

import com.lld360.cnc.dto.CommodityDto;
import com.lld360.cnc.model.Commodity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommodityDao {

    void create(Commodity commodity);

    void update(Commodity commodity);

    void updateCategory(@Param("commodityId") Long commodityId, @Param("categoryId") Integer categoryId);

    void updateState(@Param("commodityId") Long commodityId, @Param("state") Byte state);

    List<CommodityDto> search(Map<String, Object> params);

    long count(Map<String, Object> params);

    CommodityDto getDtoById(Long commodityId);

    void addViews(@Param("id") Long id);

}
