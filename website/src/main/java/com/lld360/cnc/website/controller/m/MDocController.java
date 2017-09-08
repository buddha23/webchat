package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.User;
import com.lld360.cnc.repository.DocCategoryDao;
import com.lld360.cnc.repository.DocDownloadDao;
import com.lld360.cnc.service.*;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.service.ThirdAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-04 17:09
 */
@Controller
@RequestMapping("m/doc")
public class MDocController extends SiteController {

    @Autowired
    private DocService docService;

    @Autowired
    private DocCollectService docCollectService;

    @Autowired
    private DocCategoryService docCategoryService;

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private SearchWordsService searchWordsService;

    @Autowired
    private MonthReportService monthReportService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private UserClickHabitService userClickHabitService;

    @Autowired
    private DocDownloadDao docDownloadDao;

    @Autowired
    private ThirdAccountService thirdAccountService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String searchrecord(@RequestParam(required = false) String content, Model model) {
        model.addAttribute("docs", docService.searchWeb(getParamsPageMap()));
        if (StringUtils.isNotEmpty(content)) {
            searchWordsService.updateOrCreate(String.valueOf(content));
        }
        return "m/doc-search-record";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String detailGet(@PathVariable long id, @RequestParam(required = false) Integer categoryId, Model model,
                            @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        String url = dealCodeState(code, state, "web");
        if (url != null) return url;

        Doc doc = docService.findWeb(id);
        if (doc.getFileMd5() == null) {
            throw new ServerException(HttpStatus.NOT_FOUND);
        }
        File docFile = new File(configer.getFileBasePath() + doc.getFilePath());
        model.addAttribute("isExists", docFile.exists());
        DocCategory category = doc.getDocCategory();
        if (category != null) {
            model.addAttribute("category", category);
            if (category.getFid() != null) model.addAttribute("fcategory", docCategoryDao.find(category.getFid()));
        }
        // 增加文档访问统计
        monthReportService.addDocViews();
        userClickHabitService.createHabit(request);
        model.addAttribute("doc", doc);
        if (doc.getFileType().equals("txt")) {
            model.addAttribute("content", docService.getTxtContent(docFile, 2000));
        }
        model.addAttribute("configer", configer);
        model.addAttribute("images", docService.findByDocId(id));
        User user = getCurrentUser();
        model.addAttribute("isDownload", user != null && docDownloadDao.hasDownload(user.getId(), id));
        model.addAttribute("isCollect", user == null ? "unLogin" : docCollectService.isCollect(user.getId(), id));
        model.addAttribute("footerContent", settingService.getValue(Const.CNC_WX_DOC_DETAIL_FOOTER));
        return "m/doc-detail";
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String category(Model model) {
        model.addAttribute("categorys", docCategoryDao.findAll());
        return "m/category";
    }

    //获取二级分类
    @RequestMapping(value = "/categoryC2/{c1}", method = RequestMethod.GET)
    public String categoryC2Doc(@PathVariable Integer c1, Model model) {
        if (c1 != null && c1 > 0) {
            DocCategory category = docCategoryDao.find(c1);
            if (category != null) {
                model.addAttribute("category", category);
            }
            model.addAttribute("c2s", docCategoryService.getByFid(c1));
        }
        return "m/category-subclass";
    }

    @RequestMapping(value = "/category/{c2}", method = RequestMethod.GET)
    public String categoryDoc(@PathVariable int c2, Model model) {
        DocCategory category = docCategoryDao.find(c2);
        if (category != null) {
            model.addAttribute("category", category);
            Map<String, Object> params = getParamsPageMap();
            params.put("hotType", "hotType");
            params.put("c2", c2);
            model.addAttribute("docs", docService.searchWeb(params));
            if (category.getFid() != null)
                model.addAttribute("fCategory", docCategoryDao.find(category.getFid()));
            return "m/category-doc";
        }
        return "redirect:/m/doc/category";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadGet() {
        return "/m/doc-upload";
    }
}
