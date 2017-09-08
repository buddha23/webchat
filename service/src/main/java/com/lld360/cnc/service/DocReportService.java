package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.repository.DocDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DocReportService extends BaseService {

    @Autowired
    private DocService docService;

    @Autowired
    private DocDao docDao;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Page<Doc> search(Map<String, Object> parameters) {
        parameters.put("sortBy", "downloads");
        parameters.put("sortType", "desc");
        parameters.put("useFor", "report");
        setDateRange(parameters);
        return docService.search(parameters);
    }

    public long countDownloads(Map<String, Object> parameters) {
        return docDao.countDownloads(setDateRange(parameters));
    }

    private Map<String, Object> setDateRange(Map<String, Object> parameters) {
        String endTimeStr = "endTime";
        Object beginTime = parameters.get("beginTime");
        if (beginTime != null) {
            try {
                Date begin = format.parse(String.valueOf(beginTime));
            } catch (ParseException e) {
                parameters.remove("beginTime");
            }
        }
        Object endTime = parameters.get(endTimeStr);
        if (endTime != null) {
            try {
                Date end = format.parse(String.valueOf(endTime));
                end.setTime(end.getTime() + 24 * 60 * 60 * 1000);
                parameters.put(endTimeStr, format.format(end));
            } catch (ParseException e) {
                parameters.remove(endTimeStr);
            }
        }
        return parameters;
    }
}
