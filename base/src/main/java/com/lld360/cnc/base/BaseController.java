package com.lld360.cnc.base;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.ServletWired;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: dhc
 * Date: 2016-06-29 11:27
 */
public abstract class BaseController extends ServletWired {

    //======================== 请求参数转换处理 ===============================

    /**
     * 获取数字类型的请求参数
     *
     * @param key 请求参数
     * @return 数字，参数不存在或不是Number则返回null
     */
    protected Number getNumberParam(String key) {
        String value = request.getParameter(key);
        if (StringUtils.isNotEmpty(value)) {
            try {
                return NumberFormat.getInstance().parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取数字类型的请求参数，如果参数不存在或不是Number则返回默认值
     *
     * @param key          请求参数
     * @param defaultValue 参数不存在或不是Number则返回的默认值
     * @return 数字
     */
    protected Number getNumberParam(String key, Number defaultValue) {
        Number number = getNumberParam(key);
        return number == null ? defaultValue : number;
    }

    /**
     * 根据请求的timesta类型获取Date对象
     *
     * @param key 请求参数
     * @return Date对象
     */
    protected Date getDateParamFromTimestamp(String key) {
        String value = request.getParameter(key);
        if (StringUtils.isNotEmpty(value) && value.matches("\\d{10,14}")) {
            Long time = Long.parseLong(value);
            if (value.length() < 13) {   // 不是毫秒
                time *= 1000;
            }
            return new Date(time);
        }
        return null;
    }

    /**
     * 获取当前的请求参数
     *
     * @return 请求参数
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> getParamsMap() {
        Map<String, String[]> reqParams = request.getParameterMap();
        Map<String, Object> params = new HashMap<>();
        for (String key : reqParams.keySet()) {
            String[] values = reqParams.get(key);
            if (values.length == 1 && StringUtils.isNotEmpty(values[0])) {
                params.put(key, values[0].trim());
            } else if (values.length > 1) {
                params.put(key, values);
            }
        }
        return params;
    }

    /**
     * 获取当前的请求参数并保证分页参数
     *
     * @return 包括分页参数的请求参数
     */
    protected Map<String, Object> getParamsPageMap() {
        return getParamsPageMap(15);
    }

    /**
     * 获取当前的请求参数并保证分页参数
     *
     * @param defSize 默认页数量
     * @return 包括分页参数的请求参数
     */
    protected Map<String, Object> getParamsPageMap(int defSize) {
        Map<String, Object> params = getParamsMap();
        int page = getNumberParam(Const.PAGE_PAGE, 1).intValue();
        if (page < 1) {
            page = 1;
        }
        int size = getNumberParam(Const.PAGE_SIZE, defSize).intValue();
        if (size < 2) {
            size = 2;
        }
        params.put(Const.PAGE_PAGE, page);
        params.put(Const.PAGE_SIZE, size);
        params.put(Const.PAGE_LIMIT, size);

        int offset = (page - 1) * size;
        params.put(Const.PAGE_OFFSET, offset);

        Date time = getDateParamFromTimestamp(Const.PAGE_TIME);
        if (offset != 0 && time != null) {
            params.put(Const.PAGE_TIME, time);
        } else {
            params.remove(Const.PAGE_TIME);
        }
        return params;
    }

    // 直接输出文字
    protected void outText(HttpServletResponse response, String s) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            logger.warn("输出异常：" + e.getMessage(), e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    // 验证Bean对象
    protected <T> Map<String, String> beanValidator(T t) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t);
        Map<String, String> errs = null;
        if (!violationSet.isEmpty()) {
            errs = new HashMap<>();
            for (ConstraintViolation<T> violation : violationSet) {
                errs.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return errs;
    }

    // 转换BindResult里的错误消息
    protected Map<String, String> bindResult2Map(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errs = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errs.put(error.getField(), error.getDefaultMessage());
            }
            return errs;
        }
        return null;
    }

    @ExceptionHandler(ServletException.class)
    public void servletExceptionResolver(ServletException e, HttpServletResponse response) {
        logger.info("请求错误：" + e.getMessage());
    }
    
    protected void checkFileType(MultipartFile file,String checkString){
    	String fileName = file.getOriginalFilename();
        String fileType = FilenameUtils.getExtension(fileName).toLowerCase();
        if (!fileType.matches(checkString.replaceAll(",", "|"))) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10107);
        }
    }
}
