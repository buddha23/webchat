package com.lld360.cnc.repository;

import com.lld360.cnc.model.VodCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VodCategoryDao {

    void create(VodCategory vodCategory);

    void delete(Integer id);

    int update(VodCategory vodCategory);

    List<VodCategory> findAllC1();

    VodCategory findById(int id);

    List<VodCategory> findByFid(Integer fid);

}
