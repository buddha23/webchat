package com.lld360.cnc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.utils.CryptUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.model.AdminOperateLog;
import com.lld360.cnc.model.AdminPermission;
import com.lld360.cnc.model.AdminRole;
import com.lld360.cnc.repository.AdminDao;
import com.lld360.cnc.repository.AdminOperateLogDao;
import com.lld360.cnc.repository.AdminPermissionDao;
import com.lld360.cnc.repository.AdminRoleDao;

/**
 * Author: dhc
 * Date: 2016-07-13 14:15
 */
@Service
public class AdminService extends BaseService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AdminRoleDao roleDao;

    @Autowired
    private AdminPermissionDao permissionDao;

    @Autowired
    private AdminOperateLogDao operateLogDao;

    public Admin get(String account) {
        return adminDao.findByAccount(account);
    }

    public Admin getById(Long id) {
        return adminDao.findById(id);
    }

    public void create(Admin admin) {
        if (StringUtils.isEmpty(admin.getRePassword()) || !admin.getRePassword().matches("\\w{6,20}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10002);
        }
        Admin adm = get(admin.getAccount());
        if (adm != null) throw new ServerException(HttpStatus.BAD_REQUEST, "管理员账号不能为重复");
        admin.setSalt(CryptUtils.generateSalt(6));
        admin.setPassword(DigestUtils.md5Hex(StringUtils.reverse(admin.getRePassword()) + admin.getSalt()));
        admin.setFailedTimes(0);
        admin.setStatus(Const.ADMIN_STATUS_NORMAL);
        adminDao.create(admin);
    }

    public int update(Admin admin) {
        if (StringUtils.isNotEmpty(admin.getRePassword())) {
            if (!admin.getRePassword().matches("\\w{6,20}")) {
                throw new ServerException(HttpStatus.BAD_REQUEST, M.E10002);
            } else {
                admin.setPassword(DigestUtils.md5Hex(StringUtils.reverse(admin.getRePassword()) + admin.getSalt()));
            }
        }
        return adminDao.update(admin);
    }

    public int updateStatus(Admin admin) {
        return adminDao.updateStatus(admin);
    }

    public List<Admin> adminList() {
        return adminDao.admins();
    }

    public void deleteAdmin(Long id) {
        adminDao.delete(id);
    }

    public void unfreezeAllAdmin() {
        adminDao.unfreezeAllAdmin();
    }

    //role
    public List<AdminRole> adminRoles() {
        return roleDao.search(null);
    }

    public AdminRole adminRole(Long id) {
        return roleDao.find(id);
    }

    public void addAdminRole(AdminRole adminRole) {
        Map<String, Object> params = new HashMap<>();
        params.put("role", adminRole.getRole());
        if (roleDao.search(params).size() > 0) throw new ServerException(HttpStatus.BAD_REQUEST, "角色代码不能重复");
        roleDao.create(adminRole);
    }

    public void updateAdminRole(AdminRole adminRole) {
        roleDao.update(adminRole);
    }

    public void deleteRole(Long id) {
        roleDao.delete(id);
    }

    //permission
    public List<AdminPermission> adminPermissionList() {
        return permissionDao.search(null);
    }

    public AdminPermission getPermissionByPermit(String permit) {
        return permissionDao.findByPermit(permit);
    }

    public Page<AdminOperateLog> operateRecordSearch(Map<String, Object> parameters) {
        Pageable pageable = getPageable(parameters);
        long count = operateLogDao.count(parameters);
        List<AdminOperateLog> list = operateLogDao.search(parameters);
        return new PageImpl<>(list, pageable, count);
    }
}
