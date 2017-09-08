package com.lld360.cnc.repository;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.SoftDoc;

@Repository
public interface SoftFileDao {
	
	void delete(Long id);

	int update(SoftDoc soft);
	
	void create(SoftDoc soft);
	
	SoftDoc find(Long id);

	List<SoftDoc> findByUuId(Long uuId);
}
