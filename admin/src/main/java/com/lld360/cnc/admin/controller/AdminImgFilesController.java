package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.ImgFile;
import com.lld360.cnc.repository.ImgFilesDao;
import com.lld360.cnc.service.ImgFilesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiresPermissions("setting")
@RequestMapping("admin/imgFiles")
public class AdminImgFilesController extends AdmController {

    @Autowired
    ImgFilesService imgFilesService;

    @Autowired
    ImgFilesDao imgFilesDao;

    @RequestMapping(value = "browse", method = RequestMethod.GET)
    public List<ImgFile> imgFilesGet() {
        return imgFilesService.findbytype(Const.FILE_TYPE_WEBSITE_INDEX_IMG);
    }

    @RequestMapping(value = "advertiseImgs", method = RequestMethod.GET)
    public List<ImgFile> advertiseImgs() {
        return imgFilesService.findbytype(Const.FILE_TYPE_ADVERTISE_IMG);
    }

    @RequestMapping(value = "wapIndexImgs", method = RequestMethod.GET)
    public List<ImgFile> wapIndexImgs() {
        return imgFilesService.findbytype(Const.FILE_TYPE_WAP_INDEX_IMG);
    }

//    @RequestMapping(value = "commodityImgs", method = RequestMethod.GET)
//    public List<ImgFile> commodityImgs() {
//        return imgFilesService.findbytype(Const.FILE_TYPE_COMMODITY_IMG);
//    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut docCategoryDelete(@PathVariable Long id) {
        imgFilesService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    @OperateRecord("修改首页图片")
    @RequestMapping(value = "indeximgupdate", method = RequestMethod.POST)
    public List<ImgFile> indexImgPost(@RequestParam(required = false) Long id, @RequestParam String link, @RequestParam(required = false) String path, @RequestParam Byte type) {
        ImgFile imgFile = new ImgFile();
        if (id != null) imgFile.setId(id);
        imgFile.setLink(link);
        imgFile.setPath(path);
        imgFilesService.updateImg(imgFile, type);
        return imgFilesService.findbytype(type);
    }

    @RequestMapping(value = "updatename", method = RequestMethod.GET)
    public ResponseEntity updateName(@RequestParam Long[] ids, @RequestParam String[] names) {
        List<ImgFile> imgFiles = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImgFile btFile = new ImgFile();
            btFile.setId(ids[i]);
            btFile.setName(String.valueOf(i));
            btFile.setType(0);
            imgFiles.add(btFile);
        }
        imgFilesService.updateList(imgFiles);
        return new ResponseEntity(HttpStatus.OK);
    }

}
