package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.ImgFile;
import com.lld360.cnc.repository.ImgFilesDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImgFilesService extends BaseService {

    @Autowired
    FileUtilService fileUtilService;

    @Autowired
    ImgFilesDao imgFilesDao;

    public void update(String[] srcs, String[] uploadImg, List<String> oldImgsDeleted) {
        // Image
        if (uploadImg != null) {
            try {
                uploadImgs(srcs, uploadImg, Const.FILE_TYPE_WEBSITE_INDEX_IMG);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (oldImgsDeleted.size() != 0) {
            for (String anOldImgsDeleted : oldImgsDeleted) {
                imgFilesDao.deleteurl(anOldImgsDeleted);
            }
        }
    }


    public void uploadImgs(String[] srcs, String[] imgs, byte type) throws IOException {
        File dir = new File(fileUtilService.getUserFilePath() + File.separator + "indeximg" + File.separator + type);

    }

    // 上传首页滚动图片
    public void updateImg(ImgFile imgFile, Byte type) {

        if (imgFile.getId() != null) {
            imgFile.setType(type);
            imgFilesDao.update(imgFile);
        } else {
            File dir = new File(fileUtilService.getUserFilePath() + File.separator + "indeximg" + File.separator + type);
            if (dir.exists() || dir.mkdirs()) {
                String imgStr = imgFile.getPath();
                String postfix = "." + imgStr.substring(imgStr.indexOf("/") + 1, imgStr.indexOf(";"));// 图片格式
                imgStr = imgStr.substring(imgStr.indexOf(",") + 1);
                String imgName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + postfix;
                File file = new File(fileUtilService.getUserFilePath() + File.separator + "indeximg" + File.separator + type + File.separator + imgName);
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] b;
                try (OutputStream out = new FileOutputStream(file)) {
                    b = decoder.decodeBuffer(imgStr);
                    out.write(b);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgFile.setPath(file.toString().substring(file.toString().lastIndexOf("indeximg")).replace("\\", "/"));
                imgFile.setCreateTime(new Date());
                imgFile.setType(type);
                Map<String, Object> param = new HashMap<>();
                param.put("type", type);
                if (imgFilesDao.maxName() != null) {
                    imgFile.setName(String.valueOf(new Long(imgFilesDao.maxName()) + 1));
                } else {
                    imgFile.setName("1");
                }
                imgFilesDao.create(imgFile);
            }
        }
    }

    public List<ImgFile> findbytype(long type) {
        return imgFilesDao.findbytype(type);
    }


    public ImgFile findByName(String name) {
        return imgFilesDao.findByName(name);
    }

    public void delete(Long id) {
        ImgFile imgFile = imgFilesDao.find(id);
        if (imgFile != null) {
            imgFilesDao.delete(id);
            delImg(imgFile.getPath());
        }
    }

    public void updateName(ImgFile imgFile) {
        imgFilesDao.update(imgFile);
    }

    public List<ImgFile> search(Map<String, Object> parameters) {
        return imgFilesDao.search(parameters);
    }

    public String maxName() {
        return imgFilesDao.maxName();
    }

    public void updateList(List<ImgFile> imgFiles) {
        for (ImgFile imgFile : imgFiles) {
            imgFilesDao.update(imgFile);
        }
    }

    //删除图片
    public void delImg(String targetFile) {
        if (StringUtils.isEmpty(targetFile)) {
            return;
        }
        File file = new File(fileUtilService.getUserFilePath() + File.separator + targetFile);
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                logger.warn("删除文件异常：" + e.getMessage(), e);
            }
        }
    }
}
