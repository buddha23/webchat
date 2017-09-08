package com.lld360.cnc.admin.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: dhc
 * Date: 2016-12-20 16:09
 */
@RestController
@RequestMapping("admin/oss")
public class AdmOssController extends AdmController {
    @Autowired
    private OssService ossService;

    @GetMapping("list")
    public List<OSSObjectSummary> summariesGet(String dir) {
        return ossService.getVideosByDir(dir);
    }
}
