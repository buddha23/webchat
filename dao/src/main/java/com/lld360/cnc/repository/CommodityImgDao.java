package com.lld360.cnc.repository;

import com.lld360.cnc.model.CommodityImg;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityImgDao {

    void create(CommodityImg commodityImg);

}
