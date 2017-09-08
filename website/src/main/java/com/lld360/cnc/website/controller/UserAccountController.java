package com.lld360.cnc.website.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.UserAccountService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.website.SiteController;

@RequestMapping("userAccount")
@Controller
public class UserAccountController extends SiteController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object acountInfo() {
        User user = userService.getUser(getRequiredCurrentUser().getId());
        if (null == user.getMobile()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "用户账户未绑定手机号，请先绑定！");
        }
        Map<String, Object> result = new HashMap<>();
        UserDto dto = userService.getByMobile(user.getMobile());
        result.put("user", dto);
        result.put("account", userAccountService.findAvaliableByUserId(user.getId()));
        return result;
    }
    
    @RequestMapping(value="vip",method=RequestMethod.GET)
    public String vip(){
    	getRequiredCurrentUser();
    	return "wUser/user-vip";
    }
    
    @RequestMapping(value="buyVip",method=RequestMethod.GET)
    public String buyVip(){
    	getRequiredCurrentUser();
    	return "wUser/user-vip-buy";
    }

}
