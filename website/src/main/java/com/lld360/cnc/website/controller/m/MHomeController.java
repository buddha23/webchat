package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.Course;
import com.lld360.cnc.model.PostsCategory;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.*;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.service.ThirdAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-01 14:40
 */
@Controller
@RequestMapping("m")
public class MHomeController extends SiteController {

    @Autowired
    private DocCategoryService docCategoryService;

    @Autowired
    private DocService docService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private PostsService postsService;

    @Autowired
    private ThirdAccountService thirdAccountService;

    @Autowired
    private WxGzhService wxGzhService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostsCategoryService postsCategoryService;

    @Autowired
    private ImgFilesService imgFilesService;

    /*之前*/
    @RequestMapping("index")
    public String indexV1(Model model, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        String url = dealCodeState(code, state, "web");
        if (url != null) return url;

        Map<String, Object> params = getParamsPageMap(10);
        params.put("sortBy", "create_time");
        params.put("hotType", "hotType");
        model.addAttribute("docDownLoads", docService.searchWeb(params).getContent());
        model.addAttribute("uploads", docService.searchForIndex(params));
        model.addAttribute("newPosts", postsService.getPostsByPage(params).getContent());

        params.put("sortCol", "createTime");
        params.put("sortBy", "DESC");
        model.addAttribute("videos", videoService.getVolumeDtoPage(params).getContent());

        model.addAttribute("configer", configer);
        model.addAttribute("categorys", docCategoryService.getCategoryForIndex());

        Map<String, Object> params4News = getParamsPageMap(3);
        params4News.put("type", Const.CNC_COURSE_TYPE_NEWS);
        model.addAttribute("news", courseService.search(params4News).getContent());
        return "m/index";
    }

    /*现用*/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String indexV2(Model model, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        String url = dealCodeState(code, state, "web");
        if (url != null) return url;

        Map<String, Object> params = getParamsPageMap(10);
        params.put("sortBy", "create_time");
        params.put("hotType", "hotType");
        model.addAttribute("docDownLoads", docService.searchWeb(params).getContent());
        model.addAttribute("uploads", docService.searchForIndex(params));
        model.addAttribute("newPosts", postsService.getPostsByPage(params).getContent());

        params.put("sortCol", "createTime");
        params.put("sortBy", "DESC");
        model.addAttribute("videos", videoService.getVolumeDtoPage(params).getContent());

        model.addAttribute("configer", configer);
        model.addAttribute("categorys", docCategoryService.getCategoryForIndex());

        Map<String, Object> params4News = getParamsPageMap(3);
        params4News.put("type", Const.CNC_COURSE_TYPE_NEWS);
        model.addAttribute("news", courseService.search(params4News).getContent());

        List<PostsCategory> categories = postsCategoryService.getAllCategories();
        model.addAttribute("postCate", categories.get(0));

        User user = getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("userScore", userService.findUserScore(user.getId()));
            model.addAttribute("userPoint", userService.findUserPoint(user.getId()));
            Map<String, Object> userparams = getParamsPageMap(5);
            userparams.put("userId", user.getId());
            userparams.put("uploader", user.getId());

            model.addAttribute("mycollects", docService.getCollects(userparams));
            model.addAttribute("mydownloads", docService.getDownloads(userparams));
            model.addAttribute("myuploads", docService.getUploads(userparams));
            model.addAttribute("myposts", postsService.getPostsByPage(userparams));
        }

        model.addAttribute("imgFileList", imgFilesService.findbytype(Const.FILE_TYPE_WAP_INDEX_IMG));     //广而告之
        return "m/new_index";
    }

    //微信公众号接入
    @RequestMapping(value = "gzh", method = RequestMethod.GET)
    public String gzhEntry(Model model) {
        Map<String, Object> params = getParamsPageMap(14);
        params.put("authorizered", true);
        model.addAttribute("gzhs", wxGzhService.getWxGzhs(params).getContent());
        String content = settingService.getValue(Const.CNC_WXGZH_ENTRY_MOBILE_PAGE_CONTENT);
        if (StringUtils.isNotEmpty(content)) {
            model.addAttribute("content", content);
        }
        return "m/gzh-entry";
    }

    @RequestMapping(value = "newsDetail/{id}", method = RequestMethod.GET)
    public String newsDetail(Model model, @PathVariable Long id) {
        Course course = courseService.find(id);
        model.addAttribute("news", course);
        return "m/news-detail";
    }

    @RequestMapping(value = "publish", method = RequestMethod.GET)
    public String getPublish(Model model) {
        List<PostsCategory> categories = postsCategoryService.getAllCategories();
        model.addAttribute("postCate", categories.get(0));
        return "m/new_publish";
    }

}
