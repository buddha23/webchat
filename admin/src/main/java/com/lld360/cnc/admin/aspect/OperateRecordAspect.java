package com.lld360.cnc.admin.aspect;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.validation.BeanPropertyBindingResult;

import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.utils.JsonUtils;
import com.lld360.cnc.core.utils.ServletUtils;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.model.AdminOperateLog;
import com.lld360.cnc.repository.AdminOperateLogDao;
import org.springframework.web.multipart.MultipartFile;


/**
 * Author: dhc
 * Date: 2016-03-24 15:45
 * 用于记录方法访问日志
 */
@Aspect
public class OperateRecordAspect {
    private Logger logger = LoggerFactory.getLogger(OperateRecordAspect.class);

    @Autowired
    AdminOperateLogDao adminOperateLogDao;

    @Autowired
    HttpServletRequest request;

    @Pointcut("@annotation(com.lld360.cnc.core.annotation.OperateRecord)")
    public void doMethod() {
        /**/
    }

    @AfterThrowing(value = "doMethod()", throwing = "e")
    public void doAfterThrowing(JoinPoint point, Exception e) {
        recordLog(point, e);
    }

    @AfterReturning(value = "doMethod()")
    public void doAfter(JoinPoint point) {
        recordLog(point, null);
    }

    private void recordLog(JoinPoint point, Exception e) {
        try {
            if (SecurityUtils.getSubject().getPrincipal() != null) {
                Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();

                MethodSignature signature = (MethodSignature) point.getSignature();
                OperateRecord record = signature.getMethod().getAnnotation(OperateRecord.class);

                AdminOperateLog log = new AdminOperateLog();
                log.setAdminId(admin.getId());
                log.setMethod(signature.getName());
                log.setName(StringUtils.isEmpty(record.value()) ? log.getMethod() : record.value());
                Object[] arags = point.getArgs();
                List<Object> temp = new ArrayList<>();
                for(Object arg : arags){
                	if (arg instanceof BeanPropertyBindingResult || arg instanceof InputStreamSource){
                		continue;
                	}
                	temp.add(arg);
                }
                if (temp.size() > 0) {
                    String args = null;
                    try{
                        args = JsonUtils.toJson(temp);
                    }catch (Exception e1){
                        //ignore
                    }
                    if (args!=null && args.length() > 500){
                    	if(args.contains("data")){
                    		 args = args.substring(0, args.indexOf("data"));
                    	}else{
                    		args = args.substring(0,500);
                    	}
                        log.setArgs(args);
                    }
                }
                if (e != null) {
                    log.setSuccessed(false);
                    log.setException(e.getClass().getName() + ": " + e.getMessage());
                } else {
                    log.setSuccessed(true);
                }
                if (request != null) {
                    log.setIp(ServletUtils.getRemoteAddr(request));
                    log.setUrl(request.getRequestURI());
                }
                log.setCreateTime(Calendar.getInstance().getTime());
                adminOperateLogDao.create(log);
            }
        } catch (Exception ex) {
            logger.warn("记录管理员操作异常：" + ex.getMessage(), ex);
        }
    }
}
