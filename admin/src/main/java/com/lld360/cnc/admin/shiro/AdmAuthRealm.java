package com.lld360.cnc.admin.shiro;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.service.AdminService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Author: dhc
 * Date: 2016-07-13 14:06
 */
public class AdmAuthRealm extends AuthorizingRealm {
    @Autowired
    private AdminService adminService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Admin admin = (Admin) principalCollection.getPrimaryPrincipal();
        if (admin.getAdminRole() != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole(admin.getAdminRole().getRole());
            if (StringUtils.isNotEmpty(admin.getAdminRole().getPermissions())) {
                info.addStringPermissions(Arrays.asList(admin.getAdminRole().getPermissions().split(",")));
            }
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken){
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Admin admin = adminService.get(token.getUsername());
        if (admin != null) {
            if (admin.getStatus().equals(Const.ADMIN_STATUS_FREEZE)) {
                throw new AuthenticationException("账号已被冻结！请联系超级管理员");
            }
            String cryptPwd = DigestUtils.md5Hex(StringUtils.reverse(String.valueOf(token.getPassword())) + admin.getSalt());
            token.setPassword(cryptPwd.toCharArray());
            return new SimpleAuthenticationInfo(admin, admin.getPassword(), admin.getAccount());
        }
        return null;
    }
}
