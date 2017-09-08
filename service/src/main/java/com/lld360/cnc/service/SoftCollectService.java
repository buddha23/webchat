package com.lld360.cnc.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.SoftCollect;
import com.lld360.cnc.repository.SoftCollectDao;
@Service
public class SoftCollectService extends BaseService{

	@Autowired
	private SoftCollectDao softCollectDao;
	public String isCollect(Long userId, long uuId) {
		 return userId != null && softCollectDao.isCollected(userId, uuId) ? "collect" : "notCollect";
	}
	public void docCollect(Long userId, String uuId) {
	    SoftCollect softCollect = new SoftCollect();
	    softCollect.setUuId(uuId);
	    softCollect.setUserId(userId);
	    softCollect.setCreateTime(new Date());
	    softCollectDao.create(softCollect);
	}
	public void uncollect(Long userId, String uuId) {
		   Map<String, Object> params = generateParamsMap("userId", userId, "uuId", uuId);
	        if (softCollectDao.find(params) == null) {
	            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
	        } else {
	        	softCollectDao.delete(params);
	        }
	}

}
