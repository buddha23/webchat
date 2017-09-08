package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.SystemLog;
import com.lld360.cnc.service.SystemLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiresPermissions("log")
@RequestMapping("admin/systemLog")
public class AdmSystemLogController extends AdmController {

    private Logger logger = LoggerFactory.getLogger(AdmSystemLogController.class);

    @Autowired
    SystemLogService systemLogService;

    // 系统错误日志
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<SystemLog> systemLogsGet() {
        Map<String, Object> params = getParamsPageMap();
        return systemLogService.getSystemLogPage(params);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public SystemLog logDetail(@PathVariable long id) {
        return systemLogService.findLogById(id);
    }

    // 删除日志
    @OperateRecord("删除日志")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut SystemLogDelete(@PathVariable long id) {
        systemLogService.deleteSystemLog(id);
        return getResultOut(M.I10200.getCode());
    }

    @OperateRecord("删除日志")
    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
    public ResultOut SystemLogsDelete(@RequestBody long[] ids) {
        if (ids.length != 0)
            systemLogService.deleteSystemLogs(ids);
        return getResultOut(M.I10200.getCode());
    }

    // 系统错误 信息
    @ResponseBody
    @RequestMapping(value = "monitor", method = RequestMethod.GET)
    public String systemLogsMonitor() {
        JSONObject jsonObject = new JSONObject();
        long count = systemLogService.systemLogCount(null);
        SystemLog systemLog = count > 0 ? systemLogService.systemLogLast() : null;
        try {
            jsonObject.put("count", count);
            if (null != systemLog) {
                jsonObject.put("lastLog", new JSONObject().put("time", systemLog.getOccurTime().getTime()));
            }
        } catch (JSONException e) {
            logger.info("CREATE NEW JSONOBJECT ERROR !");
        }
        return jsonObject.toString();
    }
}
