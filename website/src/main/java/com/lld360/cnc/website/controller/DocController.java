package com.lld360.cnc.website.controller;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.DocDto;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.ModeratorLog;
import com.lld360.cnc.model.User;
import com.lld360.cnc.repository.DocCategoryDao;
import com.lld360.cnc.repository.DocDownloadDao;
import com.lld360.cnc.service.*;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Author: dhc
 * Date: 2016-08-04 17:09
 */
@Controller
@RequestMapping("doc")
public class DocController extends SiteController {

    private static final int DID_BASE = 54876;          // 文档基础码
    private static final int UID_BASE = 84756;          // 用户基础码
    private static final int DOWNTIME = 30;             // 下载有效时间

    @Autowired
    private DocService docService;

    @Autowired
    private DocCollectService docCollectService;

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private MonthReportService monthReportService;

    @Autowired
    private SearchWordsService searchWordsService;

    @Autowired
    private DocCategoryService docCategoryService;

    @Autowired
    private ModeratorService moderatorService;

    @Autowired
    private UserClickHabitService userClickHabitService;

    @Autowired
    private DocDownloadDao docDownloadDao;

    //搜索
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String docsGet(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model, @RequestParam(required = false) Integer c1) {
        Map<String, Object> params = getParamsPageMap(10);
        String c1Str = (String) params.get("c1");
        String contentStr = "content";
        if (ClientUtils.isMobileBrowser(request)) {
            redirectAttributes.addAttribute(contentStr, params.get(contentStr));
            redirectAttributes.addAttribute("docType", params.get("docType"));
            redirectAttributes.addAttribute("c1", params.get("c1"));
            redirectAttributes.addAttribute("c2", params.get("c2"));
            return "redirect:/m/doc/";
        }
        model.addAttribute("c1s", docCategoryService.getByFid(null));
        if (c1 != null && c1 > 0) {
            model.addAttribute("c2s", docCategoryService.getByFid(c1));
        } else {
            params.remove("c1");
            params.remove("c2");
        }

        model.addAttribute("docs", docService.searchWeb(params));
        Object content = getParamsPageMap().get(contentStr);
        if (content != null) searchWordsService.updateOrCreate(String.valueOf(content));
        if (StringUtils.isNotEmpty(c1Str)) model.addAttribute("c1Obj", docCategoryService.get(Integer.valueOf(c1Str)));

        Map<String, Object> paramForHot = getParamsPageMap(3);
        paramForHot.put("sortBy", "views");
        model.addAttribute("hotDocs", docService.searchForIndex(paramForHot));  //热门文当推荐

        userClickHabitService.createHabit(request);
        return "wDoc/doc-search";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String detailGet(@PathVariable long id, @RequestParam(required = false) Integer categoryId, Model model, HttpServletRequest request) {
        if (ClientUtils.isMobileBrowser(request)) {
            return "redirect:/m/doc/" + id;
        }
        Doc doc = docService.findWeb(id);
        File docFile = new File(configer.getFileBasePath() + doc.getFilePath());
        model.addAttribute("isExists", docFile.exists());
        if (categoryId != null) model.addAttribute("category", docCategoryDao.find(categoryId));
        // 增加访问统计
        monthReportService.addDocViews();
        model.addAttribute("doc", doc);
        if (doc.getFileType().equals("txt")) {
            model.addAttribute("content", docService.getTxtContent(docFile, 2000));
        }
        model.addAttribute("images", docService.findByDocId(id));
        User user = getCurrentUser();
        model.addAttribute("isDownload", user != null && docDownloadDao.hasDownload(user.getId(), id));
        model.addAttribute("isCollect", user == null ? "unLogin" : docCollectService.isCollect(user.getId(), id));
        model.addAttribute("isModeratorUp", docCategoryService.isNowModerator(doc.getUploader()));

        Map<String, Object> params = getParamsPageMap(6);
        params.put("content", doc.getTitle());
        Page<Doc> docs = docService.searchWeb(params);
        List<Doc> docList = docs.getContent();
        if (docs.getContent().isEmpty()) {
            params.put("fileType", doc.getFileType());
            params.put("sortBy", "views");
            docList = docService.searchForIndex(params);
        }
        model.addAttribute("recommendList", docList);

        Map<String, Object> paramForHot = getParamsPageMap(3);
        paramForHot.put("sortBy", "views");
        model.addAttribute("hotDocs", docService.searchForIndex(paramForHot));  //热门文当推荐

        userClickHabitService.createHabit(request);
        return "wDoc/doc-detail";
    }

    // 加密ID
    private String enDownloadCode(long userId, long docId) {
        String dCode = Long.toString(docId + DID_BASE, Character.MAX_RADIX);
        String uCode = Long.toString(userId + UID_BASE, Character.MAX_RADIX);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, DOWNTIME);
        String timeCode = Long.toString(calendar.getTimeInMillis(), Character.MAX_RADIX);
        return StringUtils.reverse(uCode + "-" + dCode + "-" + timeCode);
    }

    // 解密ID
    private long deDownloadCode(String s) {
        String[] args = StringUtils.reverse(s).split("-");
        if (args.length == 3) {
            try {
                long time = Long.parseLong(args[2], Character.MAX_RADIX);
                if (time >= Calendar.getInstance().getTimeInMillis()) {
                    long uid = Long.parseLong(args[0], Character.MAX_RADIX) - UID_BASE;
                    if (getRequiredCurrentUser().getId() == null)
                        throw new ServerException(HttpStatus.FORBIDDEN, "请先注册并绑定账号");
                    if (uid == getRequiredCurrentUser().getId())
                        return Long.parseLong(args[1], Character.MAX_RADIX) - DID_BASE;
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    // 获取下载代码
    @RequestMapping(value = "/dl/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResultOut> dlCodeGet(@PathVariable long id) {
        Doc doc = docService.get(id);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        User user = getRequiredCurrentUser();
        docService.downloadRecord(user, doc);
        // 增加下载统计
        monthReportService.addDocDownloads();
        return new ResponseEntity<>(getResultOut(M.I10200.getCode()).setData(enDownloadCode(getRequiredCurrentUser().getId(), id)), HttpStatus.OK);
    }

    // 下载文件
    @RequestMapping(value = "/dl", method = RequestMethod.GET)
    public void docFileGet(@RequestParam String c, HttpServletResponse response) throws IOException {
        long id = deDownloadCode(c);
        if (id == 0) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        Doc doc = docService.find(id);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        File file = docService.download(doc);
        ClientUtils.outputFile(response, file, doc.getFileName());
    }

    @RequestMapping(value = "/collect", method = RequestMethod.GET)
    public ResponseEntity collectGet(@RequestParam long id) {
        docCollectService.docCollect(getRequiredCurrentUser().getId(), id);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 取消收藏
    @RequestMapping(value = "/uncollect", method = RequestMethod.GET)
    public ResponseEntity uncollectPost(long id) {
        docCollectService.uncollect(getRequiredCurrentUser().getId(), id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //我的下载
    @RequestMapping(value = "myDownload", method = RequestMethod.GET)
    public String myDownload(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> param = new HashMap<>();
        param.put("userId", user.getId());
        List<DocDto> myDownloads = docService.getDownloads(param);
        model.addAttribute("docs", myDownloads);
        return "wDoc/doc-mydownload";
    }

    @RequestMapping(value = "myUpload", method = RequestMethod.GET)
    public String myUpload(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> param = new HashMap<>();
        param.put("uploader", user.getId());
        param.put("unstate", 9);
        param.put("uploaderType", Const.DOC_UPLOADERTYPE_USER);
        List<Doc> myUploads = docService.getUploads(param);
        model.addAttribute("docs", myUploads);
        return "wDoc/doc-myupload";
    }

    @RequestMapping(value = "myFav", method = RequestMethod.GET)
    public String myFav(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> param = new HashMap<>();
        param.put("userId", user.getId());
        List<DocDto> myFavs = docService.getCollects(param);
        model.addAttribute("docs", myFavs);
        return "wDoc/doc-myfav";
    }

    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public String docUploadGet() {
        return "wDoc/doc-upload";
    }


    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String docEditGet(@PathVariable long id, Model model) {
        Doc doc = docService.get(id);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        model.addAttribute("doc", doc);
        return "wDoc/doc-edit";
    }

    // 文档上传
    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Doc docFilePost(@RequestParam MultipartFile file, @RequestParam(required = false) Long id) {
        User user = getRequiredCurrentUser();
        Doc doc;
        if (id != null) {
            doc = docService.get(id);
            if (doc == null) {
                throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
            }
        } else {
            doc = new Doc();
            doc.setTitle(FilenameUtils.getBaseName(file.getOriginalFilename()));
            doc.setUploader(user.getId());
            doc.setUploaderType(Const.DOC_UPLOADERTYPE_USER);
            doc.setCostScore(0);
        }
        try {
            return docService.save(file, doc);
        } catch (IOException e) {
            logger.error("用户上传文档出错", e);
            throw new ServerException(HttpStatus.SERVICE_UNAVAILABLE, M.S90500).setMessage(e.getMessage());
        }
    }

    // 文档信息编辑
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public Doc docInfoPost(@PathVariable long id, @Valid Doc doc, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(bindResult2Map(result));
        }
        Doc d = docService.get(id);
        if (d == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        d.setTitle(doc.getTitle());
        d.setCostScore(doc.getCostScore());
        d.setSummary(doc.getSummary());
        d.setCategoryId(doc.getCategoryId());
        docService.update(d);
        return d;
    }

    //批量取消收藏
    @RequestMapping(value = "unConllect", method = RequestMethod.POST)
    public String doUnconllect() {
        String[] myconllects = request.getParameterValues("myconllects");
        if (myconllects != null) {
            for (String myConllectId : myconllects) {
                long myconlelctid = Long.parseLong(myConllectId);
                uncollectPost(myconlelctid);
            }
        }
        return "redirect:/doc/myFav";
    }

    // 对文档进行审核操作
    @ResponseBody
    @RequestMapping(value = "{id}/review", method = RequestMethod.POST)
    public ResultOut docReviewPatch(@PathVariable long id, boolean reviewOk) {
        Doc doc = docService.find(id);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, "文档不存在！");
        }
        if (doc.getState() != Const.DOC_STATE_REVIEWING) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "当前文档不可执行该操作！");
        }
        User user = getRequiredCurrentUser();
        if (!docCategoryService.isDocModerator(id, user.getId())) {
            throw new ServerException(HttpStatus.FORBIDDEN, "没有操作权限！");
        }
        byte state = docService.docReview(id, reviewOk);

        // 记录版主审核文档的日志
        ModeratorLog log = new ModeratorLog(user.getId(), ModeratorLog.TYPE_VIEW, doc.getId(), "版主【" + user.getNickname() + "】审核" + (reviewOk ? "通过" : "拒绝") + "了文档《" + doc.getTitle() + "》。");
        moderatorService.add(log);

        return new ResultOut(M.I10200, "OK").setData(state);
    }

    // 更改文档分类
    @ResponseBody
    @RequestMapping(value = "{id}/category", method = RequestMethod.POST)
    public ResultOut docCategoryChangePost(@PathVariable long id, @RequestParam int cid) {
        User user = getRequiredCurrentUser();
        if (!docCategoryService.isDocModerator(id, user.getId())) {
            throw new ServerException(HttpStatus.FORBIDDEN, "没有权限执行当前操作！");
        }
        docService.changeCategory(id, cid);

        // 记录版主审核文档的日志
        Doc doc = docService.find(id);
        DocCategory category = docCategoryDao.find(cid);
        ModeratorLog log = new ModeratorLog(user.getId(), ModeratorLog.TYPE_MOVE, id, "版主【" + user.getNickname() + "】移动了文档《" + doc.getTitle() + "》至版块 [" + category.getName() + "]。");
        moderatorService.add(log);
        return getResultOut(M.I10200.getCode());
    }

    //删除文档
    @ResponseBody
    @RequestMapping(value = "docDelete", method = RequestMethod.POST)
    public ResultOut docDelete(@RequestParam("ids[]") long[] ids) {
        User user = getRequiredCurrentUser();
        List<String> msg = new ArrayList<>();
        for (long id : ids) {
            deleteErrMsg(user, msg, id);
        }
        if (!msg.isEmpty()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(msg);
        }
        return getResultOut(M.I10200.getCode());
    }

    private void deleteErrMsg(User user, List<String> msg, long id) {
        Doc doc = docService.find(id);
        boolean isDocModerator = docCategoryService.isDocModerator(id, user.getId());
        if (doc != null && doc.getState() != Const.DOC_STATE_LOADING) {
            if (!(doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER && (doc.getUploader().equals(user.getId()) || isDocModerator))) {
                msg.add("文档【" + doc.getTitle() + "】不可删除，原因：没有权限。");
            } else if (docService.countDocDownloads(id) > 0) {
                msg.add("文档【" + doc.getTitle() + "】不可删除，原因：有下载记录。");
            } else {
                docService.deleteLogic(doc);
                // 记录版主删除文档的日志
                if (isDocModerator) {
                    moderatorService.add(new ModeratorLog(user.getId(), ModeratorLog.TYPE_DELETE, doc.getId(), "版主【" + user.getNickname() + "】删除了文档《" + doc.getTitle() + "》。"));
                }
            }
        } else {
            msg.add("文档 " + id + " 不存在或当前不可删除！");
        }
    }

}
