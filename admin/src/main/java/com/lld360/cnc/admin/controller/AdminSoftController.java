package com.lld360.cnc.admin.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.SoftDownloads;
import com.lld360.cnc.model.SoftDoc;
import com.lld360.cnc.model.SoftUpload;
import com.lld360.cnc.service.OssService;
import com.lld360.cnc.service.SoftService;

@RestController
@RequestMapping("admin/soft")
public class AdminSoftController extends AdmController{
	@Autowired
	private SoftService softService;
	@Autowired
	private OssService ossService;

    @RequiresPermissions("soft:r")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<SoftUpload> docPageGet(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortType) {
        Map<String, Object> params = getParamsPageMap(15);
        if (params.containsKey("key")) {
            params.put("content", params.get("key"));
            params.remove("key");
        }
        boolean sort = sortBy != null && sortType != null && sortBy.matches("views|downloads|createTime") && sortType.matches("asc|desc");
        if (!sort) {
            params.remove("sortBy");
            params.remove("sortType");
        }
        return softService.search(params);
    }

    @RequiresPermissions("soft:r")
    @RequestMapping(value = "/{uuId}", method = RequestMethod.GET)
    public SoftUpload softGet(@PathVariable long uuId) {
    	SoftUpload doc = softService.find(uuId);
    	if (null == doc){
    		throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
    	}
    	return doc;
    }
    
    @OperateRecord("编辑软件")
    @RequiresPermissions("soft:w")
    @RequestMapping(value = "/{uuId}", method = RequestMethod.PUT)
    public SoftUpload docPut(@PathVariable long uuId, @Valid @RequestBody SoftUpload soft, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(bindResult2Map(result));
        } else {
            soft.setUuId(uuId);
            return softService.update(soft) ? softService.find(uuId) : soft;
        }
    }

    @OperateRecord("删除软件")
    @RequiresPermissions("soft:w")
    @RequestMapping(value = "/{uuId}", method = RequestMethod.DELETE)
    public ResultOut softDelete(@PathVariable long uuId) {
    	SoftUpload soft = softService.find(uuId);
        if (soft != null) {
        	softService.delete(soft);
            return getResultOut(M.I10200.getCode());
        }
        throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("文档不存在");
    }
    
    // 对文档进行审核操作
    @OperateRecord("审核软件")
    @RequiresPermissions("soft:w")
    @RequestMapping(value = "/{uuId}/review", method = RequestMethod.POST)
    public ResultOut docReviewPatch(@PathVariable long uuId, boolean reviewOk) {
        byte state = softService.review(uuId, reviewOk);
        return new ResultOut(M.I10200, "OK").setData(state);
    }

    
    // 批量审核通过
    @OperateRecord("批量审核软件")
    @RequiresPermissions("soft:w")
    @RequestMapping(value = "review", method = RequestMethod.POST)
    public ResultOut reviewBatchPost(@RequestParam("ids[]") Long[] ids) {
        List<String> messages = new ArrayList<>();
        for (long id : ids) {
            try {
            	softService.review(id, true);
            } catch (ServerException e) {
                messages.add(id + " -> " + e.getMessage());
            }
        }
        return getResultOut(M.I10200.getCode()).setData(messages);
    }
    
    // 下载
    @RequestMapping(value = "/dl/{uuId}", method = RequestMethod.GET)
    public ResponseEntity<ResultOut> dlCodeGet(@PathVariable long uuId) {
        SoftUpload doc = softService.find(uuId);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }

        List<SoftDownloads> urls = ossService.getObjectUrl(doc);
        // 增加下载统计
        return new ResponseEntity<>(getResultOut(M.I10200.getCode())
                .setData(urls), HttpStatus.OK);
    }
    
    // 下载
    @RequestMapping(value = "/downloadIteam/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResultOut> downloadIteam(@PathVariable long id) {
    SoftDoc soft= softService.findFile(id);
       URL url = ossService.getObjectUrl(soft.getFilePath());
        // 增加下载统计
        return new ResponseEntity<>(getResultOut(M.I10200.getCode())
                .setData(url), HttpStatus.OK);
    }
    
}
