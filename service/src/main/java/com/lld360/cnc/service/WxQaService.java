package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.model.WxQa;
import com.lld360.cnc.repository.WxQaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-07-15 13:50
 */
@Service
public class WxQaService extends BaseService {
    @Autowired
    WxQaDao wxQaDao;

    public Page<WxQa> getQas(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = wxQaDao.count(params);
        List<WxQa> qaList = count > 0 ? wxQaDao.search(params) : new ArrayList<>();
        return new PageImpl<>(qaList, pageable, count);
    }

    public WxQa get(long id) {
        return wxQaDao.find(id);
    }

    public WxQa get(String q) {
        return wxQaDao.findByQ(q);
    }

    public void add(WxQa qa) {
        wxQaDao.insert(qa);
    }

    public int update(WxQa qa) {
        return wxQaDao.update(qa);
    }

    public void delete(long id) {
        wxQaDao.delete(id);
    }
}
