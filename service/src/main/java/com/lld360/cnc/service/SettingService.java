package com.lld360.cnc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.model.Setting;
import com.lld360.cnc.repository.SettingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

/**
 * Author: dhc
 * Date: 2016-07-12 14:42
 */
@Service
public class SettingService {
    Logger logger = LoggerFactory.getLogger(SettingService.class);

    @Autowired
    SettingDao settingDao;

    public void create(Setting setting) {
        settingDao.insert(setting);
    }

    public int update(Setting setting) {
        return settingDao.update(setting);
    }

    public void delete(String name) {
        Setting setting = settingDao.find(name);
        if (setting != null) {
            settingDao.delete(name);
        }
    }

    public List<Setting> search(){
        return  settingDao.search();
    }

    public Setting get(String code) {
        return settingDao.find(code);
    }

    public String getValue(String code) {
        Setting setting = settingDao.find(code);
        return setting == null ? null : setting.getContent();
    }

    // 获取一个数字值
    public Number getNumber(String code) {
        String value = getValue(code);
        try {
            return value == null ? null : NumberFormat.getInstance().parse(value);
        } catch (ParseException e) {
            logger.warn("获取数字设定异常：" + e.getMessage(), e);
            return null;
        }
    }

    public Number getNumber(String code, Number defaultValue) {
        Number number = getNumber(code);
        return number == null ? defaultValue : number;
    }

    // 获取设置的JSON对象
    public <T> T getObjectFormJsonValue(String code, Class<T> clz) {
        String value = getValue(code);
        if (code == null || code.trim().length() == 0) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(value, clz);
        } catch (IOException e) {
            logger.warn("读取设置对象异常：" + e.getMessage(), e);
            return null;
        }
    }

    // 获取设置的JSON对象
    public <T> T getObjectFormJsonValue(String code, TypeReference reference) {
        String value = getValue(code);
        if (code == null || code.trim().length() == 0) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(value, reference);
        } catch (IOException e) {
            logger.warn("读取设置对象异常：" + e.getMessage(), e);
            return null;
        }
    }

    // 保存setting对象
    public Setting save(Setting setting) {
        Setting s = settingDao.find(setting.getName());
        if (s != null) {
            settingDao.update(setting);
        } else {
            settingDao.insert(setting);
        }
        return setting;
    }
}
