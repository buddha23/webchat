package com.lld360.cnc.repository;

import com.lld360.cnc.model.Commodity;
import com.lld360.cnc.model.CommodityCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityCategoryDao {

    void create(CommodityCategory commodity);

    void update(CommodityCategory commodity);

    List<CommodityCategory> getAllCategories();

    CommodityCategory getCategoryById(Integer id);

}
