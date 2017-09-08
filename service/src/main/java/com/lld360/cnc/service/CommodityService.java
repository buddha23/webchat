package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.CommodityDto;
import com.lld360.cnc.model.Commodity;
import com.lld360.cnc.model.CommodityCategory;
import com.lld360.cnc.model.CommodityImg;
import com.lld360.cnc.repository.CommodityCategoryDao;
import com.lld360.cnc.repository.CommodityDao;
import com.lld360.cnc.repository.CommodityImgDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CommodityService extends BaseService {

    private static Logger commodityLogger = LoggerFactory.getLogger(CommodityService.class);

    @Autowired
    private CommodityDao commodityDao;

    @Autowired
    private CommodityCategoryDao commodityCategoryDao;

    @Autowired
    private CommodityImgDao commodityImgDao;

    public Page<CommodityDto> commodityPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long total = commodityDao.count(params);
        List<CommodityDto> list = total > 0 ? commodityDao.search(params) : new ArrayList<>();
        return new PageImpl<>(list, pageable, total);
    }

    public CommodityDto getCommodityDtoById(Long commodityId) {
        return commodityDao.getDtoById(commodityId);
    }

    public void changeCommodityCategory(Long commodityId, Integer categoryId) {
        CommodityCategory category = commodityCategoryDao.getCategoryById(categoryId);
        if (category == null) throw new ServerException(HttpStatus.BAD_REQUEST);
        commodityDao.updateCategory(commodityId, categoryId);
    }

    public void deleteCommodity(Long commodityId) {
        commodityDao.updateState(commodityId, Const.COMMODITY_STATE_DELETE);
    }

    public List<CommodityCategory> getAllCommodityCategories() {
        return commodityCategoryDao.getAllCategories();
    }

    public CommodityCategory getCategoryById(Integer categoryId) {
        return commodityCategoryDao.getCategoryById(categoryId);
    }

    public void addViews(Long commodityId) {
        commodityDao.addViews(commodityId);
    }

    // 保存类型
    public CommodityCategory saveCategory(CommodityCategory category, MultipartFile icon) {
        if (icon != null) {
            String relativeFile = "commodityCategory/icon_" + RandomStringUtils.randomAlphanumeric(6)
                    + "." + FilenameUtils.getExtension(icon.getOriginalFilename());
            String absoluteFile = configer.getFileBasePath() + relativeFile;
            deleteOldIcon(category);
            try {
                File f = new File(absoluteFile);
                if (f.getParentFile().exists() || f.getParentFile().mkdirs())
                    icon.transferTo(f);
                category.setIcon(relativeFile);
            } catch (IOException e) {
                commodityLogger.error("保存商品分类图标失败");
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
            }
        }
        if (category.getId() == null) {
            commodityCategoryDao.create(category);
        } else {
            commodityCategoryDao.update(category);
        }
        return category;
    }

    //删除旧图标
    private void deleteOldIcon(CommodityCategory category) {
        if (category.getIcon() != null) {
            File oldIcon = new File(configer.getFileBasePath() + category.getIcon());
            if (oldIcon.exists())
                FileUtils.deleteQuietly(oldIcon);
        }
    }

    public void createCommodity(Commodity commodity) {
        commodity.setState(Const.COMMODITY_STATE_NORMAL);
        commodity.setViews(0);
        commodity.setCreateTime(Calendar.getInstance(Locale.CHINA).getTime());
        if (commodity.getPrice() == null) commodity.setPrice(BigDecimal.valueOf(0));
        commodityDao.create(commodity);
        if (StringUtils.isNotEmpty(commodity.getImgPath())) dealImgs(commodity);
    }

    private void dealImgs(Commodity commodity) {
        String[] imgs = commodity.getImgPath().split(",");
        updateFileName(commodity);
        commodity.setCover(imgs[0].replace("temp", commodity.getId().toString()));
        commodityDao.update(commodity);
        for (String img : imgs) {
            String imgPath = img.replace("temp", commodity.getId().toString());
            commodityImgDao.create(new CommodityImg(commodity.getId(), imgPath));
        }
    }

    public void updateFileName(Commodity commodity) {
        String s = configer.getFileBasePath() + File.separator + "commodity/user" + commodity.getUserId() + File.separator;
        File toBeRenamed = new File(s + "temp");
        File newFile = new File(s + String.valueOf(commodity.getId()));
        toBeRenamed.renameTo(newFile);
    }

}
