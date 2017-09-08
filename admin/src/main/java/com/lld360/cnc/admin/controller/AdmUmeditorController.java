package com.lld360.cnc.admin.controller;

import com.google.gson.Gson;
import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-08 11:59
 */
@Controller
@RequestMapping("admin/ume")
public class AdmUmeditorController extends AdmController {
    @Autowired
    private Configer configer;

    private static final String UM_UPLOAD_PATH = "umfiles/";

    @RequestMapping(value = "images", method = RequestMethod.POST)
    public void imagesPost(@RequestParam MultipartFile upfile, HttpServletResponse response) {
    	checkFileType(upfile, Const.COURSE_NOT_GEN_PIC_TYPES);
        Map<String, Object> outData = new HashMap<>();
        String type = '.' + FilenameUtils.getExtension(upfile.getOriginalFilename());
        String fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS") + type;
        String filePath = UM_UPLOAD_PATH + fileName;
        File file = new File(configer.getFileBasePath() + filePath);
        try {
            File dir = file.getParentFile();
            if (dir.exists() || dir.mkdirs()) {
                upfile.transferTo(file);
                outData.put("name", fileName);
                outData.put("originalName", upfile.getOriginalFilename());
                outData.put("size", upfile.getSize());
                outData.put("state", "SUCCESS");
                outData.put("type", type);
                outData.put("url", filePath);
                Gson gson = new Gson();
                response.setContentType(MediaType.TEXT_HTML_VALUE);
                outText(response, gson.toJson(outData));
            } else {
                throw new ServerException(HttpStatus.INSUFFICIENT_STORAGE, "创建文件夹失败：" + dir.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(e.getMessage());
        }
    }
}
