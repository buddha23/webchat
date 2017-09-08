package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.Article;
import com.lld360.cnc.service.ArticleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-07-14 11:49
 */
@RestController
@RequestMapping("admin/article")
public class AdmArticleController extends AdmController {

    @Autowired
    private ArticleService articleService;

    @RequiresPermissions("artic:r")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Article> articlePageGet(@RequestParam(required = false) String sortBy,
                                        @RequestParam(required = false) String sortType) {
        Map<String, Object> params = getParamsPageMap(15);
        boolean sort = sortBy != null && sortType != null && sortBy.matches("views|downloads|createTime") && sortType.matches("asc|desc");
        if (!sort) {
            params.remove("sortBy");
            params.remove("sortType");
        }
        return articleService.getArticles(params);
    }

    @RequiresPermissions("artic:r")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Article articleGet(@PathVariable long id) {
        return articleService.getArticle(id);
    }

    @OperateRecord("上传资料")
    @RequiresPermissions("artic:r")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Article articlePost(@RequestBody Article article) {
        return articleService.add(article);
    }

    @OperateRecord("编辑资料")
    @RequiresPermissions("artic:w")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Article articlePut(@RequestBody Article article) {
        articleService.update(article);
        return article;
    }

    @OperateRecord("删除资料")
    @RequiresPermissions("artic:w")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut articleDel(@PathVariable long id) {
        articleService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

//    // 批量删除
//    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
//    public ResultOut articlesDel(@RequestBody long[] ids) {
//        for (long id : ids)
//            articleService.delete(id);
//        return getResultOut(M.I10200.getCode());
//    }

    // 上传PDF
    @OperateRecord("上传PDF")
    @RequiresPermissions("artic:w")
    @RequestMapping(value = "{id}/pdf", method = RequestMethod.POST)
    public Article pdfUpload(@PathVariable long id, @RequestParam MultipartFile file) throws IOException {
        if (file == null) {
            return null;
        }
        checkFileType(file, "pdf");
        return articleService.uploadArticlePdf(id, file);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResultOut downloadDataGet() {
        ResultOut out = new ResultOut();
        Object o = context.getAttribute(Const.CTX_KEY_CNC_DOWNLOAD);
        if (o != null) {
            out.setMessage("数据下载正在进行中……");
        } else {
            articleService.ansyDownload();
            out.setMessage("数据下载已经启动……");
        }
        return out;
    }
}
