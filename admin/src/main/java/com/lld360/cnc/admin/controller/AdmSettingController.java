package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.Setting;
import com.lld360.cnc.service.SettingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Author: dhc
 * Date: 2016-08-08 15:46
 */
@RestController
@RequiresPermissions("setting")
@RequestMapping("admin/setting")
public class AdmSettingController extends AdmController {
    @Autowired
    private SettingService settingService;
    
    @OperateRecord("添加或编辑系统设置")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Setting settingPost(@Valid @RequestBody Setting setting) {
        return settingService.save(setting);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Setting settingGet(@RequestParam String code) {
        return settingService.get(code);
    }

    @OperateRecord("删除系统设置")
    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
    public ResultOut settingDelete(@PathVariable String name) {
        settingService.delete(name);
        return getResultOut(M.I10200.getCode());
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public List<Setting> settingListGet() {
        return settingService.search();
    }
}
