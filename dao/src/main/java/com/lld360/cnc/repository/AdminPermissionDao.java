package com.lld360.cnc.repository;

import com.lld360.cnc.model.AdminPermission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * AdminPermission 数据库操作
 */
@Repository
public interface AdminPermissionDao {

    List<AdminPermission> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    AdminPermission find(Long id);

    AdminPermission findByPermit(String permit);

    void create(AdminPermission adminpermission);

    int update(AdminPermission adminpermission);

    void delete(Long id);

}