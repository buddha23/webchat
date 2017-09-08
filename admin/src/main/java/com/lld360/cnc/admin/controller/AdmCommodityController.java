package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.CommodityDto;
import com.lld360.cnc.model.Commodity;
import com.lld360.cnc.model.CommodityCategory;
import com.lld360.cnc.model.PostsCategory;
import com.lld360.cnc.service.CommodityService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/commodity")
public class AdmCommodityController extends AdmController {

    @Autowired
    private CommodityService commodityService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Page<CommodityDto> listGet() {
        Map<String, Object> params = getParamsPageMap(20);
        params.put("state", Const.COMMODITY_STATE_NORMAL);
        return commodityService.commodityPage(params);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Commodity getDetail(@PathVariable Long id) {
        return commodityService.getCommodityDtoById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut deleteCommodity(@PathVariable Long id) {
        commodityService.deleteCommodity(id);
        return getResultOut(M.I10200.getCode());
    }

    @RequestMapping(value = "categories", method = RequestMethod.GET)
    public List<CommodityCategory> categoriesGet() {
        return commodityService.getAllCommodityCategories();
    }

    @RequestMapping(value = "changeCategory/{id}", method = RequestMethod.POST)
    public ResultOut changeCommodityCategory(@PathVariable Long id, @RequestBody Integer categoryId) {
        commodityService.changeCommodityCategory(id, categoryId);
        return getResultOut(M.I10200.getCode());
    }

    //    @OperateRecord("修改商品分类")
    @RequestMapping(value = "saveCategory", method = RequestMethod.POST)
    public CommodityCategory commodityCategoryPost(@Valid CommodityCategory category, @RequestParam(required = false) MultipartFile file) {
        if (null != file) checkFileType(file, "pdf");
        return commodityService.saveCategory(category, file);
    }


}
