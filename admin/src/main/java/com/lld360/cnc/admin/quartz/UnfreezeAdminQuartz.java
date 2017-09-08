package com.lld360.cnc.admin.quartz;

import com.lld360.cnc.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;


public class UnfreezeAdminQuartz {

    @Autowired
    private AdminService adminService;

    /*
    * 解冻冻结admin
    * */
    public void unfreezeAdmin() {
        adminService.unfreezeAllAdmin();
    }

}
