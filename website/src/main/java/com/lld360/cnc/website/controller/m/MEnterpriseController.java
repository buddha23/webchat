package com.lld360.cnc.website.controller.m;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.service.EnterpriseService;
import com.lld360.cnc.website.SiteController;

@Controller
@RequestMapping("m/enterprise")
public class MEnterpriseController extends SiteController{
	
	@Autowired
	private EnterpriseService enterpriseService;
	
	@GetMapping("list")
	public String list(Model model){
		Map<String, Object> params = getParamsPageMap();
		params.put("state", Const.ENTERPRISE_CERTIFICATION_STATE_PASS);
		model.addAttribute("list", enterpriseService.infoList(params));
		return "m/enterprise-list";
	}
	
	@GetMapping("{id}")
	public String detail(@PathVariable long id,Model model ){
		model.addAttribute("info", enterpriseService.info(id));
		return "m/enterprise-home";
	}

}
