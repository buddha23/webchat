package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.DocTag;
import com.lld360.cnc.service.DocTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("admin/docTag")
public class AdmDocTagContronller extends AdmController {

    @Autowired
    private DocTagService docTagService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<DocTag> docTagPageGet() {

        Map<String, Object> params = getParamsPageMap(15);
        return docTagService.search(params);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DocTag docTagGet(@PathVariable Integer id) {

        return docTagService.get(id);
    }

    @OperateRecord("添加标签")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public DocTag docTagPost(@Valid @RequestBody DocTag docTag, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = bindResult2Map(result);
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(errors);
        } else {

            docTagService.add(docTag);
            return docTag;
        }
    }

    @OperateRecord("编辑标签")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DocTag docTagPut(@PathVariable Integer id, @Valid @RequestBody DocTag docTag, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(bindResult2Map(result));
        } else {
            return docTagService.update(docTag) ? docTagService.get(id) : docTag;
        }
    }

    @OperateRecord("删除标签")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultOut docTagDelete(@PathVariable Integer id) {
        DocTag docTag = docTagService.get(id);
        if (docTag != null) {
            docTagService.delete(id);
            return getResultOut(M.I10200.getCode());
        }
        throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("标签不存在");
    }



}
