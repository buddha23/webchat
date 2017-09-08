package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.Article;
import com.lld360.cnc.model.ArticleFile;
import com.lld360.cnc.model.Setting;
import com.lld360.cnc.repository.ArticleFileDao;
import com.lld360.cnc.service.ArticleService;
import com.lld360.cnc.service.MonthReportService;
import com.lld360.cnc.service.SettingService;
import com.lld360.cnc.service.WxGzhService;
import com.lld360.cnc.website.SiteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Author: dhc
 * Date: 2016-07-07 17:29
 */
@Controller
@RequestMapping("m/article")
public class MArticleController extends SiteController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private MonthReportService monthReportService;

    @Autowired
    private ArticleFileDao articleFileDao;

    @Autowired
    private WxGzhService wxGzhService;

    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("articles", articleService.getArticlesByKeyWord(keyword));
        return "m/article-list";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String find(@PathVariable long id, Model model) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("images", article.getImages().split(","));
        model.addAttribute("jsTicket", wxGzhService.getWxGzhJsApiTicket().getTicket());
        model.addAttribute("headerContent", settingService.getValue(Const.CNC_WX_DOC_DETAIL_HEADER));
        model.addAttribute("footerContent", settingService.getValue(Const.CNC_WX_DOC_DETAIL_FOOTER));
        return "m/article-detail";
    }

    @RequestMapping(value = "help", method = RequestMethod.GET)
    public String help(Model model) {
        Setting s = settingService.get(Const.SETTING_CNC_HELP_MPAGE_CONTENT);
        model.addAttribute("s", s);
        return "m/help";
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public ResponseEntity articleDownload(@RequestParam long id) {
        ArticleFile articleFile = articleFileDao.find(id);
        articleFile.setDownloads(articleFile.getDownloads() + 1);
        articleService.updateArticleFile(articleFile);
        monthReportService.addArticleDownloads();
        return new ResponseEntity(HttpStatus.OK);
    }
}
