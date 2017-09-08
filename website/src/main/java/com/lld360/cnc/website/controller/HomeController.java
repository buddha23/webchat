package com.lld360.cnc.website.controller;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.model.*;
import com.lld360.cnc.service.*;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.service.ThirdAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-29 15:39
 */
@Controller
@RequestMapping("/")
public class HomeController extends SiteController {
    @Autowired
    private ThirdAccountService thirdAccountService;

    @Autowired
    private DocCategoryService docCategoryService;

    @Autowired
    private DocService docService;

    @Autowired
    private ImgFilesService imgFilesService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserSuggestsService userSuggestsService;

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        if (ClientUtils.isMobileBrowser(request)) return "redirect:/m/";
        String url = dealCodeState(code, state, "web");
        if (url != null) return url;

        model.addAttribute("configer", configer);

        Map<String, Object> paramForRecommend = new HashMap<>();
        model.addAttribute("userCount", userService.getCount(paramForRecommend));       //用户数量

        paramForRecommend.put("sortBy", "create_time");
        paramForRecommend.put("limit", 5);
        List<Doc> leftRecommendDocs = docService.searchForIndex(paramForRecommend);
        getMistiming(leftRecommendDocs);
        model.addAttribute("leftRecommendDocs", leftRecommendDocs);      //左边每日推荐

        paramForRecommend.put("offset", 5);
        List<Doc> rightRecommendDocs = docService.searchForIndex(paramForRecommend);
        getMistiming(rightRecommendDocs);
        model.addAttribute("rightRecommendDocs", rightRecommendDocs);     //右边每日推荐

        Map<String, Object> paramForHot = new HashMap<>();
        paramForHot.put("sortBy", "views");
        paramForHot.put("limit", 5);
        model.addAttribute("hotDocs", docService.searchForIndex(paramForHot));  //热门推荐

        String[] types = {"pdf", "doc", "xls", "ppt", "txt", "jpg"};        //分类列表
        paramForHot.replace("limit", 6);
        for (String fileType : types) {
            paramForHot.put("fileType", fileType);
            List<Doc> docs = docService.searchForIndex(paramForHot);
            for (Doc doc : docs) {
                List<DocImage> images = docService.findByDocId(doc.getId());
                if (images.size() != 0) {
                    doc.setImagePath(images.get(0).getPath());
                }
            }
            model.addAttribute(fileType + "Docs", docs);
        }

        model.addAttribute("docCategories", docCategoryService.getCategoryForIndex());      //推荐模块
        model.addAttribute("imgFileList", imgFilesService.findbytype(Const.FILE_TYPE_WEBSITE_INDEX_IMG));     //广而告之
        model.addAttribute("courses", courseService.search(getParamsPageMap(6)));

        return "index";
    }

    //获取已上传时间差
    private void getMistiming(List<Doc> docs) {
        for (Doc doc : docs) {
            Date createTime = doc.getCreateTime();
            Date nowTime = new Date();
            if (createTime == null) {
                doc.setDiffTime("一年");
                continue;
            }

            long diffTime = nowTime.getTime() - createTime.getTime();
            long minutes = diffTime / (1000 * 60);
            long hours = minutes / 60;
            long days = hours / 24;
            long months = days / 30;

            if (minutes == 0) {
                doc.setDiffTime("1分钟");
            } else if (hours == 0) {
                doc.setDiffTime(minutes + "分钟");
            } else if (days == 0) {
                doc.setDiffTime(hours + "小时");
            } else if (months == 0) {
                doc.setDiffTime(days + "天");
            } else if (months >= 12) {
                doc.setDiffTime("一年");
            } else {
                doc.setDiffTime(months + "个月");
            }
        }
    }

    @RequestMapping(value = "suggest", method = RequestMethod.GET)
    public String suggest() {
//        User user = getRequiredCurrentUser();
        return "wUser/user-suggest";
    }

    @RequestMapping(value = "userSuggests", method = RequestMethod.GET)
    public String userSuggests(Model model) {
        Map<String, Object> params = getParamsPageMap(10);
        model.addAttribute("suggests", userSuggestsService.getSuggestDtoPage(params));
        return "wUser/user-suggestsList";
    }

}
