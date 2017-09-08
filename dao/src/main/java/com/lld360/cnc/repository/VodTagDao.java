package com.lld360.cnc.repository;

import com.lld360.cnc.model.VodTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VodTagDao {

    List<VodTag> getAllTag();

    List<VodTag> getVodTag(Integer volumeId);

    void delVodTag(Integer volumeId);

    void addVodTag(@Param("volumeId") Integer volumeId, @Param("tagId") Integer tagId);

}
