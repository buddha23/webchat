package com.lld360.cnc.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.lld360.cnc.core.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.model.AdminOperateLog;
import com.lld360.cnc.model.AdminPermission;
import com.lld360.cnc.model.AdminRole;
import com.lld360.cnc.service.AdminService;

@RestController
@RequiresPermissions("admin")
@RequestMapping(value = "admin/admin")
public class AdmAdminController extends AdmController {

    @Autowired
    private AdminService adminService;

    //ADMIN 管理员
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<Admin> adminList() {
        return adminService.adminList();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Admin adminDetail(@PathVariable Long id) {
        return adminService.getById(id);
    }

    @OperateRecord("创建admin")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public void adminPut(@Valid @RequestBody Admin admin) {
        adminService.create(admin);
    }

    @OperateRecord("修改admin")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void adminPut(@PathVariable long id, @Valid @RequestBody Admin admin) {
        adminService.update(admin);
    }

    @OperateRecord("删除admin")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void adminDelete(@PathVariable Long id) {
        adminService.deleteAdmin(id);
    }

    @OperateRecord("解冻admin")
    @RequestMapping(value = "unfreeze/{id}", method = RequestMethod.PUT)
    public void adminUnfreeze(@PathVariable long id, @Valid @RequestBody Admin admin) {
        admin.setStatus(Const.ADMIN_STATUS_NORMAL);
        adminService.updateStatus(admin);
    }

    @OperateRecord("冻结admin")
    @RequestMapping(value = "freeze/{id}", method = RequestMethod.PUT)
    public void adminFreeze(@PathVariable long id, @Valid @RequestBody Admin admin) {
        admin.setStatus(Const.ADMIN_STATUS_FREEZE);
        adminService.updateStatus(admin);
    }

    //ROLE 角色
    @RequestMapping(value = "roles", method = RequestMethod.GET)
    public List<AdminRole> rolesGet() {
        return adminService.adminRoles();
    }

    @RequestMapping(value = "role/{id}", method = RequestMethod.GET)
    public AdminRole adminRole(@PathVariable Long id) {
        return adminService.adminRole(id);
    }

    @OperateRecord("新增或修改admin角色")
    @RequestMapping(value = "role/{id}", method = RequestMethod.PUT)
    public void roleUpdate(@PathVariable Integer id, @Valid @RequestBody AdminRole adminRole) {
        if (id == 0) adminService.addAdminRole(adminRole);
        else adminService.updateAdminRole(adminRole);
    }

    @OperateRecord("删除admin角色")
    @RequestMapping(value = "role/{id}", method = RequestMethod.DELETE)
    public void adminRoleDelete(@PathVariable Long id) {
        adminService.deleteRole(id);
    }

    //PERMISSION 权限
    @RequestMapping(value = "permission", method = RequestMethod.GET)
    public List<AdminPermission> adminPermissionList() {
        return adminService.adminPermissionList();
    }

    @RequestMapping(value = "getPermissionsName", method = RequestMethod.GET)
    public List getPermissionsName(@RequestParam String permissions) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(permissions)) {
            String[] perms = permissions.split(",");
            for (String perm : perms) {
                AdminPermission p = adminService.getPermissionByPermit(perm);
                if (p != null) list.add(p.getName());
            }
        }
        return list;
    }

    @RequestMapping(value = "operateLog", method = RequestMethod.GET)
    public Page<AdminOperateLog> getOperateRecords() {
        Map<String, Object> params = getParamsPageMap(15);
        return adminService.operateRecordSearch(params);
    }
}
