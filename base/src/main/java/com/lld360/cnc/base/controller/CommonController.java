package com.lld360.cnc.base.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.DocTag;
import com.lld360.cnc.model.SoftCategory;
import com.lld360.cnc.model.VodCategory;
import com.lld360.cnc.repository.DocCategoryDao;
import com.lld360.cnc.repository.DocTagDao;
import com.lld360.cnc.repository.SoftCategoryDao;

import com.lld360.cnc.repository.VodCategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-01 13:41
 */
@RestController
@RequestMapping("i")
public class CommonController extends BaseController {
    @Autowired
    private Configer configer;

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private VodCategoryDao vodCategoryDao;

    @Autowired
    private DocTagDao docTagDao;
    
    @Autowired
    private SoftCategoryDao softCategoryDao;

    // 获取部分服务器配置
    @RequestMapping("config")
    public Map<String, Object> configGet() {
        Map<String, Object> items = new HashMap<>();
        items.put("title", getMessage("site.name"));
        items.put("ctx", request.getContextPath());
        items.put("env", configer.getEnv());
        return items;
    }

    // 获取所有文档类型
    @RequestMapping("doc_categories")
    public List<DocCategory> docCategoriesGet() {
        return docCategoryDao.findAll();
    }
    // 获取所有软件类型
    @RequestMapping("soft_categories")
    public List<SoftCategory> softCategoriesGet(){
    	return softCategoryDao.findAll();
    }
    // 获取所有视频类型
    @RequestMapping("vod_categories")
    public List<VodCategory> vodCategoriesGet() {
        return vodCategoryDao.findAllC1();
    }
    // 获取文档标签
    @RequestMapping("doc_tags")
    public List<DocTag> docTagsGet(@RequestParam(required = false) Integer limit) {
        return docTagDao.findAll(limit);
    }
}
