package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import com.lld360.cnc.model.WxGzh;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * WxGzh 数据库操作
 */
@Repository
public interface WxGzhDao {

    List<WxGzh> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    WxGzh find(Long id);

    WxGzh findByAppId(String appId);

    void create(WxGzh wxGzh);

    int update(WxGzh wxGzh);

    int updateAuthorized(@Param("appId") String appId, @Param("authorized") int authorized);

    void delete(Long id);
}