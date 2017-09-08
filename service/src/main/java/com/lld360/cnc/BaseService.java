package com.lld360.cnc;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServletWired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: dhc
 * Date: 2016-06-30 10:06
 */
public abstract class BaseService extends ServletWired {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    protected Pageable getPageable(Map<String, Object> params) {
        int page = params.containsKey(Const.PAGE_PAGE) ? (int) params.get(Const.PAGE_PAGE) : 1;
        int size = params.containsKey(Const.PAGE_SIZE) ? (int) params.get(Const.PAGE_SIZE) : 15;
        return new PageRequest(page - 1, size);
    }

    //参数封装
    public Map<String, Object> getParameters(Object... params) {
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < params.length; i = i + 2) {
            parameters.put((String) params[i], params[i + 1]);
        }
        return parameters;
    }


//    @SuppressWarnings("unchecked")
//    protected <T> T setValueIfEmptyKey(Map<String, Object> map, String key, T t) {
//        if (!map.containsKey(key) || map.get(key) == null) {
//            map.put(key, t);
//            return t;
//        }
//        return (T) map.get(key);
//    }

    protected Object setValueIfEmptyKey(Map<String, Object> map, String key, Object o) {
        if (!map.containsKey(key) || map.get(key) == null) {
            map.put(key, o);
            return o;
        }
        return map.get(key);
    }

    /**
     * 获取完整路径
     *
     * @param absolutePath 相对路径
     * @return 完整路径
     */
    public String getFullUrl(String absolutePath) {
        if (absolutePath.startsWith("http[s]?://")) {
            return absolutePath;
        }
        if (absolutePath.startsWith("/\\w+"))
            absolutePath = absolutePath.substring(1);
        return configer.getAppUrl() + absolutePath;
    }

    public Map<String, Object> generateParamsMap(Object... params) {
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < params.length; i = i + 2) {
            parameters.put((String) params[i], params[i + 1]);
        }
        return parameters;
    }

    // 报表检测时间段
    public void checkTime(Map<String, Object> params) {
        Date end = Calendar.getInstance(Locale.CHINA).getTime();
        Date begin = Calendar.getInstance(Locale.CHINA).getTime();
        String endTimeStr = "endTime";
        String beginTimeStr = "beginTime";
        long monthTime = 30 * 24 * 60 * 60;
        Object endTime = params.get(endTimeStr);
        if (endTime == null) {
            params.put(endTimeStr, end);
        } else if (!(endTime instanceof Date)) {
            try {
                end = format.parse(String.valueOf(endTime));
            } catch (ParseException e) {
                params.remove(endTimeStr);
            }
        }
        Object beginTime = params.get(beginTimeStr);
        if (beginTime == null) {
            begin.setTime(end.getTime() - monthTime * 1000);
            params.put(beginTimeStr, begin);
        } else if (!(beginTime instanceof Date)) {
            try {
                begin = format.parse(String.valueOf(beginTime));
            } catch (ParseException e) {
                params.remove(beginTimeStr);
            }
        }
        if (params.get("unit") != null && params.get("unit").equals("DATE") && end.getTime() - begin.getTime() > monthTime * 1000) {
            end.setTime(begin.getTime() + monthTime * 1000);
            params.put(endTimeStr, end);
        }
    }
}
