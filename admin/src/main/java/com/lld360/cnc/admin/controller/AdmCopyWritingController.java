package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.CopyWriting;
import com.lld360.cnc.service.CopyWritingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiresPermissions("site:w")
@RequestMapping("admin/copyWriting")
public class AdmCopyWritingController extends AdmController {

    @Autowired
    CopyWritingService copyWritingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<CopyWriting> copyWritingPage() {
        Map<String, Object> params = getParamsPageMap(15);
        return copyWritingService.search(params);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public CopyWriting copyWritingGet(@PathVariable long id) {
        return copyWritingService.findById(id);
    }

    @OperateRecord("新增文案")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public CopyWriting update(@RequestBody CopyWriting copyWriting) {
        copyWriting.setCreater(getRequiredCurrentAdmin().getId());
        return copyWritingService.createCopyWring(copyWriting);
    }

    @OperateRecord("修改文案")
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public CopyWriting create(@RequestBody CopyWriting copyWriting) {
        copyWritingService.update(copyWriting);
        return copyWriting;
    }

    @OperateRecord("删除文案")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut articleDel(@PathVariable long id) {
        copyWritingService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    @OperateRecord("批量删除文案")
    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
    public ResultOut deleteSome(@RequestBody long[] ids) {
        if (ids.length == 0) throw new ServerException(HttpStatus.BAD_REQUEST, "未进行选择任何");
        copyWritingService.deleteCopyWritings(ids);
        return getResultOut(M.I10200.getCode());
    }

}
