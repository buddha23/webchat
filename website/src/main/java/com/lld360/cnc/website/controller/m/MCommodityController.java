package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.CommodityDto;
import com.lld360.cnc.model.Commodity;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.CommodityService;
import com.lld360.cnc.service.ImgFilesService;
import com.lld360.cnc.website.SiteController;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("m/commodity")
public class MCommodityController extends SiteController {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ImgFilesService imgFilesService;

    @RequestMapping(value = "list")
    public String getCommodities(Model model) {
        Map<String, Object> params = getParamsPageMap(3);
        params.put("type", Const.COMMODITY_TYPE_NEED);
        params.put("orderBy", "create_time");
        params.put("sort", "desc");
        model.addAttribute("needs", commodityService.commodityPage(params).getContent());
        params.replace("type", Const.COMMODITY_TYPE_SELL);
        model.addAttribute("sells", commodityService.commodityPage(params).getContent());
        model.addAttribute("imgFileList", imgFilesService.findbytype(Const.FILE_TYPE_COMMODITY_IMG));     //广而告之
        model.addAttribute("configer", configer);
        return "m/commodity";
    }

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public String getDetail(@PathVariable long id, Model model) {
        CommodityDto commodity = commodityService.getCommodityDtoById(id);
        model.addAttribute("commodity", commodity);
        commodityService.addViews(id);
        return "m/commodity-detail";
    }

    @ResponseBody
    @RequestMapping(value = "getmore", method = RequestMethod.GET)
    public List getmore(@RequestParam String getType) {
        Map<String, Object> params = getParamsPageMap(3);
        if (getType.equals("needs")) params.put("type", Const.COMMODITY_TYPE_NEED);
        if (getType.equals("sells")) params.put("type", Const.COMMODITY_TYPE_SELL);
        return commodityService.commodityPage(params).getContent();
    }

    @RequestMapping(value = "publish", method = RequestMethod.GET)
    public String publishGet() {
        User user = getRequiredCurrentUser();
        if (user == null) throw new ServerException(HttpStatus.FORBIDDEN);
        return "m/commodity-publish";
    }

    @ResponseBody
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    public ResponseEntity publishPost(@Valid Commodity commodity) {
        if (StringUtils.isEmpty(commodity.getPhone()) || !commodity.getPhone().matches("1\\d{10}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10001);
        }
        User user = getRequiredCurrentUser();
        commodity.setUserId(user.getId());
        commodityService.createCommodity(commodity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "imgUpload", method = RequestMethod.POST)
    public ResponseEntity imgUpload(MultipartFile imagefile) {
        JSONObject json = new JSONObject();
        User user = getRequiredCurrentUser();
        if (imagefile != null) {
            String imgPath = saveCommodityImgs(imagefile, user);
            json.put("path", imgPath);
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // 保存帖子内容上传的图片
    private String saveCommodityImgs(MultipartFile file, User user) {
        String path = "/commodity/user" + user.getId() + "/temp/";
        String imgType = file.getContentType();
        if (imgType.contains("image")) {
            String ext = imgType.substring(imgType.indexOf("image/") + 6);
            String imgPath = path + RandomStringUtils.randomAlphanumeric(8) + "." + ext;
            File dic = new File(configer.getFileBasePath() + path);
            if (dic.isDirectory() || dic.mkdirs()) {
                try {
                    FileUtils.writeByteArrayToFile(new File(configer.getFileBasePath() + imgPath), file.getBytes());
                    return imgPath;
                } catch (IOException e) {
                    logger.warn("保存上传文件异常：" + e.getMessage(), e);
                    throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10101, imgPath).setData(e.getMessage());
                }
            }
        } else {
            throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10102, path);
    }

}
