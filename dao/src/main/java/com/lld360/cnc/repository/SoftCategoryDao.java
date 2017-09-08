package com.lld360.cnc.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.SoftCategory;

@Repository
public interface SoftCategoryDao {

	List<SoftCategory> findAll();

	List<SoftCategory> findByFid(Integer fid);

	SoftCategory find(Integer id);

	void create(SoftCategory category);

	void update(SoftCategory category);

	void delete(int id);

}
