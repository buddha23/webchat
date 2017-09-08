package com.lld360.cnc.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.lld360.cnc.dto.SoftUploadDto;
import com.lld360.cnc.model.SoftUpload;
@Repository
public interface SoftUploadDao {
	SoftUpload find(long uuId);

	void create(SoftUpload upload);
	
	int update(SoftUpload upload);

	long countWeb(Map<String, Object> params);

	List<SoftUpload> searchWeb(Map<String, Object> params);

	List<SoftUpload> searchForIndex(Map<String, Object> params);

	SoftUpload findWeb(long uuId);

	List<SoftUploadDto> getDownloads(Map<String, Object> param);

	List<SoftUpload> search(Map<String, Object> param);

	List<SoftUploadDto> getCollects(Map<String, Object> param);

	int sumScoreOfSoft(long uuId);

	void delete(Long uuId);

	long count(Map<String, Object> parameters);

	void updateSoftState(@Param("uuId")Long uuId, @Param("state")byte state);

	long countUploadByUserAndMonth(@Param("userId")Long userId, @Param("month")Date month);

}
