package com.lld360.cnc.admin;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.Admin;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: dhc
 * Date: 2016-06-29 13:44
 */
public abstract class AdmController extends BaseController {

    protected Admin getCurrentAdmin() {
        Object object = SecurityUtils.getSubject().getPrincipal();
        if (object != null) {
            return (Admin) object;
        }
        return null;
    }

    protected Admin getRequiredCurrentAdmin() {
        Admin admin = getCurrentAdmin();
        if (admin == null) {
            throw new ServerException(HttpStatus.UNAUTHORIZED, M.S90401);
        }
        return admin;
    }

    // 拦截SpringMVC的常用异常信息
    // MissingServletRequestParameterException 400 参数不正确
    // NoSuchRequestHandlingMethodException 404 请求地址不存在
    // HttpRequestMethodNotSupportedException 405 方法不支持

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResultOut> msrpExceptionHandler(MissingServletRequestParameterException e, HttpServletResponse response) {
        ResultOut out = new ResultOut(M.S90400, e.getMessage());
        return new ResponseEntity<>(out, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResultOut> hrmnsExceptionHandler(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        ResultOut out = new ResultOut(M.S90405, e.getMessage());
        return new ResponseEntity<>(out, HttpStatus.BAD_REQUEST);
    }
}
