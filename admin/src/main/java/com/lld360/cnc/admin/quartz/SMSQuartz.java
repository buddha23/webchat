package com.lld360.cnc.admin.quartz;

import com.lld360.cnc.service.AdminService;
import com.lld360.cnc.service.ValidSmsService;
import org.springframework.beans.factory.annotation.Autowired;

public class SMSQuartz {

    @Autowired
    private ValidSmsService validSmsService;

    public void clearMobileChecker() {
        validSmsService.clearMobileChecker();
    }

}
