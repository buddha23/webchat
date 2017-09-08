package com.lld360.cnc.repository;

import com.lld360.cnc.model.SystemLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SystemLogDao {

    void create(SystemLog log);

    int update(SystemLog log);

    int delete(long id);

    SystemLog find(long id);

    long count(Map<String, Object> params);

    List<SystemLog> findList(Map<String, Object> params);

    int deleteSome(long[] ids);
}
