package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.WxQa;
import com.lld360.cnc.service.WxQaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-07-15 14:13
 */
@RestController
@RequestMapping("admin/wxqa")
public class AdmWxQaController extends AdmController {
    @Autowired
    private WxQaService wxQaService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<WxQa> wxQaPageGet() {
        Map<String, Object> params = getParamsPageMap(20);
        return wxQaService.getQas(params);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public WxQa wxQaGet(@PathVariable long id) {
        return wxQaService.get(id);
    }

    @OperateRecord("发布问题")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public WxQa wxQaPost(@RequestBody WxQa qa) {
        if (wxQaService.get(qa.getQ()) != null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "问题已经存在");
        }
        wxQaService.add(qa);
        return qa;
    }

    @OperateRecord("修改问题")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public WxQa wxQaPut(@RequestBody WxQa qa) {
        WxQa wxQa = wxQaService.get(qa.getQ());
        if (wxQa != null && !wxQa.getId().equals(qa.getId())) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "问题已经存在");
        }
        wxQaService.update(qa);
        return qa;
    }

    @OperateRecord("删除问题")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut wxQaDelete(@PathVariable long id) {
        wxQaService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

}
