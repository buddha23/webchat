package com.lld360.cnc.repository;

import com.lld360.cnc.model.WxQa;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WxQaDao {

    void insert(WxQa qa);

    WxQa find(long id);

    WxQa findByQ(String q);

    int update(WxQa qa);

    long count(Map<String, Object> params);

    List<WxQa> search(Map<String, Object> params);

    boolean findInSetByQ(@Param("q") String q, @Param("msg") String msg);

    void delete(long id);

    void addVolume(long qaId);
}