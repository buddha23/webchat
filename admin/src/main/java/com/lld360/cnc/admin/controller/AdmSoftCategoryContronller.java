

package com.lld360.cnc.admin.controller;

import javax.validation.Valid;

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
import com.lld360.cnc.model.SoftCategory;
import com.lld360.cnc.service.SoftCategoryService;

@RestController
@RequestMapping("admin/softCategory")
public class AdmSoftCategoryContronller extends AdmController{
	@Autowired
	private SoftCategoryService softCategoryService;  
	
	   @OperateRecord("添加或编辑软件类别")
	   @RequestMapping(value = "save", method = RequestMethod.POST)
	    public SoftCategory docCategoryPost(@Valid SoftCategory category, @RequestParam(required = false) MultipartFile file) {
		   softCategoryService.save(category, file);
	        return softCategoryService.get(category.getId());
	    }
	  
	   @OperateRecord("删除软件类别")
	    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	    public ResultOut docCategoryDelete(@PathVariable int id) {
	        softCategoryService.delete(id);
	        return getResultOut(M.I10200.getCode());
	    }
}
