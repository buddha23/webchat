package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.model.SystemLog;
import com.lld360.cnc.repository.SystemLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemLogService extends BaseService{

    @Autowired
    SystemLogDao systemLogDao;

    // 获取错误列表
    public Page<SystemLog> getSystemLogPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long total = systemLogDao.count(params);
        List<SystemLog> logs = total > 0 ? systemLogDao.findList(params) : new ArrayList<>();
        return new PageImpl<>(logs, pageable, total);
    }

    public SystemLog findLogById(long id){
        return systemLogDao.find(id);
    }

    public void deleteSystemLog(long id) {
        systemLogDao.delete(id);
    }

    public void deleteSystemLogs(long[] ids) {
        systemLogDao.deleteSome(ids);
    }

    public long systemLogCount(Map<String, Object> params) {
        return systemLogDao.count(params);
    }

    public SystemLog systemLogLast() {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 1);
        params.put("offset", 0);
        List<SystemLog> logs = systemLogDao.findList(params);
        return null != logs && logs.size() > 0 ? logs.get(0) : null;
    }
}
