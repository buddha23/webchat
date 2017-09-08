package com.lld360.cnc.admin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.EnterpriseCertification;
import com.lld360.cnc.service.EnterpriseService;

@Controller
@RequestMapping("admin/enterprise")
public class AdmEnterpriseController extends AdmController{
	
	@Autowired
	private EnterpriseService enterpriseService;
	
	@GetMapping("")
	@ResponseBody
	public Page<EnterpriseCertification> docPageGet(){
		 Map<String, Object> params = getParamsPageMap(15);
		 return enterpriseService.search(params);
	}
	
	@PutMapping("pass/{id}")
	public ResponseEntity<String> pass(@PathVariable long id){
		getRequiredCurrentAdmin();
		enterpriseService.setCertificationState(id,Const.ENTERPRISE_CERTIFICATION_STATE_PASS);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("unpass/{id}")
	public ResponseEntity<String> unpass(@PathVariable long id){
		getRequiredCurrentAdmin();
		enterpriseService.setCertificationState(id,Const.ENTERPRISE_CERTIFICATION_STATE_FAIL);
	    return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
}
