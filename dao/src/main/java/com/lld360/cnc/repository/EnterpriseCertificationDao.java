package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.EnterpriseCertification;

@Repository
public interface EnterpriseCertificationDao {

	void insert(EnterpriseCertification certification);

	EnterpriseCertification findByUserId(Long userId);

	void update(EnterpriseCertification certification);

	long count(Map<String, Object> params);

	List<EnterpriseCertification> search(Map<String, Object> params);

	EnterpriseCertification get(long id);

	void updatePassInfo(EnterpriseCertification certification);


}
