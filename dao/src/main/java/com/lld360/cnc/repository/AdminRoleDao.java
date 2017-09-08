package com.lld360.cnc.repository;

import com.lld360.cnc.model.AdminRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * AdminRole 数据库操作
 */
@Repository
public interface AdminRoleDao {

    List<AdminRole> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    AdminRole find(Long id);

    void create(AdminRole adminrole);

    int update(AdminRole adminrole);

    void delete(Long id);

}