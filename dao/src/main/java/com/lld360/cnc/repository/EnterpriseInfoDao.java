package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.EnterpriseInfo;

@Repository
public interface EnterpriseInfoDao {

	void insertOrUpdateInfo(EnterpriseInfo info);

	EnterpriseInfo findByUserId(Long userId);
	
	List<EnterpriseInfo> searchWeb(Map<String, Object> params);
	
	long count(Map<String, Object> params);

	EnterpriseInfo getInfoById(long id);

}
