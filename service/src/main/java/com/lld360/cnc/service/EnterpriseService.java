package com.lld360.cnc.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.EnterpriseCertification;
import com.lld360.cnc.model.EnterpriseInfo;
import com.lld360.cnc.repository.EnterpriseCertificationDao;
import com.lld360.cnc.repository.EnterpriseInfoDao;

@Service
public class EnterpriseService extends BaseService{
	
	@Autowired
	private EnterpriseCertificationDao enterpriseCertificationDao;
	
	@Autowired
	private EnterpriseInfoDao enterpriseInfoDao;
	@Autowired
	private Configer configer;

	public void settled(EnterpriseCertification certification) {
		enterpriseCertificationDao.insert(certification);
	}

	public EnterpriseCertification getCertificationByUserId(Long userId) {
		EnterpriseCertification enterpriseCertification = enterpriseCertificationDao.findByUserId(userId);
		if (null != enterpriseCertification){
			makeImgPaths(enterpriseCertification);
		}
		return enterpriseCertification;
	}
	
	public EnterpriseInfo getInfoByUserId(Long userId){
		return enterpriseInfoDao.findByUserId(userId);
	}

	public void updateCertification(EnterpriseCertification certification, Byte state) {
		if (state.byteValue() == Const.ENTERPRISE_CERTIFICATION_STATE_PASS){
			enterpriseCertificationDao.updatePassInfo(certification);
		}else if(state.byteValue() == Const.ENTERPRISE_CERTIFICATION_STATE_FAIL){
			certification.setState(Const.ENTERPRISE_CERTIFICATION_STATE_NEW);
			enterpriseCertificationDao.update(certification);
		}else{
			enterpriseCertificationDao.update(certification);
		}
	}

	public void setOrModifyEnterpriseInfo(EnterpriseInfo info) {
		enterpriseInfoDao.insertOrUpdateInfo(info);
	}

	public String uploadFile(MultipartFile file,String extension, byte type) {
		String relativeFile = "enterprise/" + type +"/"+ RandomStringUtils.randomAlphanumeric(6) + "."
				+ extension;
		String absoluteFile = configer.getFileBasePath() + relativeFile;
		try {
			File f = new File(absoluteFile);
			if (f.getParentFile().exists() || f.getParentFile().mkdirs())
				file.transferTo(f);
		} catch (IOException e) {
			throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
		}
		return relativeFile;
	}

	public Page<EnterpriseCertification> search(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = enterpriseCertificationDao.count(params);
        List<EnterpriseCertification> enterpriseCertifications = enterpriseCertificationDao.search(params);
        for (EnterpriseCertification enterpriseCertification :enterpriseCertifications){
        	makeImgPaths(enterpriseCertification);
        }
        return new PageImpl<>(enterpriseCertifications, pageable, count);
	}

	public void setCertificationState(long id, byte state) {
		EnterpriseCertification enterpriseCertification = enterpriseCertificationDao.get(id);
		if (null == enterpriseCertification){
			throw new ServerException(HttpStatus.NOT_FOUND);
		}
		enterpriseCertification.setState(state);
		enterpriseCertificationDao.update(enterpriseCertification);
	}
	
	private void makeImgPaths(EnterpriseCertification certification){
		certification.setImgPaths(certification.getRegistrationImg().split(","));
	}

	public Object infoList(Map<String, Object> params) {
		Pageable pageable = getPageable(params);
		long count = enterpriseInfoDao.count(params);
		List<EnterpriseInfo> infos = enterpriseInfoDao.searchWeb(params);
		return new PageImpl<>(infos, pageable, count);
	}

	public Object info(long id) {
		return enterpriseInfoDao.getInfoById(id);
	}
}
