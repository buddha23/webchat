package com.lld360.cnc.repository;

import com.lld360.cnc.model.ImgFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ImgFilesDao {

    List<ImgFile> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    ImgFile find(Long id);

    ImgFile findByName(String name);

    void create(ImgFile obj);

    int update(ImgFile obj);

    void delete(Long id);

    void deleteurl(String  id);

    List<ImgFile> findbytype(long type);

    String maxName();

    int updateList(@Param("imgFiles")List<ImgFile> imgFiles);
}
