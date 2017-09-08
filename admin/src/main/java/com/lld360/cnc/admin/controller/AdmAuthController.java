package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Author: dhc
 * Date: 2016-07-18 15:26
 */
@Controller
@RequestMapping("admin")
public class AdmAuthController extends AdmController {

    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping(value = "login")
    public Admin loginPost() {
        return getRequiredCurrentAdmin();
    }

    @ResponseBody
    @RequestMapping(value = "changePwd", method = RequestMethod.POST)
    public ResponseEntity changePwd(@Valid @RequestBody Admin admin) {
        if (admin == null || StringUtils.isEmpty(admin.getRePassword()))
            throw new ServerException(HttpStatus.BAD_REQUEST, "密码不能为空");
        Admin currentAdmin = getCurrentAdmin();
        currentAdmin.setRePassword(admin.getRePassword());
        adminService.update(currentAdmin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
