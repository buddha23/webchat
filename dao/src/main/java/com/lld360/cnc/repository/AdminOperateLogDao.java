package com.lld360.cnc.repository;


import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.lld360.cnc.model.AdminOperateLog;


/**
* 
*/
@Repository
public interface AdminOperateLogDao{
	
    List<AdminOperateLog> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    AdminOperateLog find(Long id);

    void create(AdminOperateLog obj);

    int update(AdminOperateLog obj);

    void delete(Long id);

}