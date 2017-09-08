package com.lld360.cnc.repository;

import com.lld360.cnc.model.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao {

    Admin findByAccount(String account);

    Admin findById(Long id);

    int update(Admin admin);

    void create(Admin admin);

    void delete(Long id);

    void freezeAdmin(Admin admin);

    int updateStatus(Admin admin);

    void unfreezeAllAdmin();

    List<Admin> admins();

    List<Admin> noSaltAdmins();

}