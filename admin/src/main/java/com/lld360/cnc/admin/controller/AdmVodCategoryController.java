package com.lld360.cnc.admin.controller;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.VodCategory;
import com.lld360.cnc.service.VodCategoryService;

@RestController
@RequiresPermissions("video:w")
@RequestMapping("admin/vodCategory")
public class AdmVodCategoryController extends AdmController {

    @Autowired
    VodCategoryService vodCategoryService;
    
    @OperateRecord("添加或修改视频类别")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public VodCategory docCategoryPost(@Valid VodCategory vodCategory, @RequestParam(required = false) MultipartFile file) {
    	if (null != file){
    		checkFileType(file, Const.COURSE_NOT_GEN_PIC_TYPES);
    	}
        vodCategoryService.save(vodCategory, file);
        return vodCategoryService.get(vodCategory.getId());
    }

    @OperateRecord("删除视频类别")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut vodCategoryDelete(@PathVariable int id) {
        vodCategoryService.delete(id);
        return getResultOut(M.I10200.getCode());
    }
}
