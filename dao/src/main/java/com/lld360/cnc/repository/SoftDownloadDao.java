package com.lld360.cnc.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.lld360.cnc.model.SoftDownload;

@Repository
public interface SoftDownloadDao {

	boolean hasDownload(@Param("userId")Long userId,@Param("uuId")long uuId);

	void create(SoftDownload download);

	long countBySoft(long uuId);

}
