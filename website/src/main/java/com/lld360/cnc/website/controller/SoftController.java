package com.lld360.cnc.website.controller;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.lld360.cnc.repository.SoftDownloadDao;
import com.lld360.cnc.service.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.aliyuncs.exceptions.ClientException;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.bean.Credential;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.SoftDownloads;
import com.lld360.cnc.dto.SoftUploadDto;
import com.lld360.cnc.model.SoftDoc;
import com.lld360.cnc.model.SoftUpload;
import com.lld360.cnc.model.User;
import com.lld360.cnc.util.CredentialUtil;
import com.lld360.cnc.website.SiteController;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("soft")
public class SoftController extends SiteController {

    Logger logger = LoggerFactory.getLogger(SoftController.class);

    @Autowired
    private SoftService softService;

    @Autowired
    private SoftDownloadDao softDownloadDao;

    @Autowired
    private SoftCategoryService softCategoryService;

    @Autowired
    private OssService ossService;

    @Autowired
    private SearchWordsService searchWordsService;

    @Autowired
    private SoftCollectService softCollectService;

    @Autowired
    private UserClickHabitService userClickHabitService;

    //搜索
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String softsGet(Model model, @RequestParam(required = false) Integer c1) {
        Map<String, Object> params = getParamsPageMap(10);
        String c1Str = (String) params.get("c1");
        model.addAttribute("c1s", softCategoryService.getByFid(null));
        if (c1 != null && c1 > 0) {
            model.addAttribute("c2s", softCategoryService.getByFid(c1));
        } else {
            params.remove("c1");
            params.remove("c2");
        }

        model.addAttribute("softs", softService.searchWeb(params));
        Object content = getParamsPageMap().get("content");
        if (content != null) {
            searchWordsService.updateOrCreate(String.valueOf(content));
        }

        if (StringUtils.isNotEmpty(c1Str)) {
            model.addAttribute("c1Obj", softCategoryService.get(Integer.valueOf(c1Str)));
        }
        Map<String, Object> paramForHot = getParamsPageMap(3);
        paramForHot.put("sortBy", "views");
        model.addAttribute("hotSofts", softService.searchForIndex(paramForHot));  //热门文当推荐

        userClickHabitService.createHabit(request);
        return "wSoft/soft-search";
    }

    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public String docUploadFileGet() {
        return "wSoft/soft-upload";
    }

    @RequestMapping(value = "{uuId}", method = RequestMethod.GET)
    public String detailGet(@PathVariable long uuId, @RequestParam(required = false) Integer categoryId, Model model, HttpServletRequest request) {
        SoftUpload soft = softService.findWeb(uuId);
        if (null == soft) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        userClickHabitService.createHabit(request);
        boolean isExists = ossService.isExist(soft.getSofts());
        model.addAttribute("isExists", isExists);
        if (categoryId != null) model.addAttribute("category", softCategoryService.get(categoryId));
        // 增加访问统计
        // monthReportService.addDocViews();
        model.addAttribute("soft", soft);
        User user = getCurrentUser();
        model.addAttribute("isDownload", user != null && softDownloadDao.hasDownload(user.getId(), uuId));
        model.addAttribute("isCollect", user == null ? "unLogin" : softCollectService.isCollect(user.getId(), uuId));
        Map<String, Object> params = getParamsPageMap(6);
        params.put("content", soft.getTitle());
        Page<SoftUpload> docs = softService.searchWeb(params);
        List<SoftUpload> docList = docs.getContent();
        if (docs.getContent().isEmpty()) {
            params.put("sortBy", "views");
            docList = softService.searchForIndex(params);
        }
        model.addAttribute("recommendList", docList);
        Map<String, Object> paramForHot = getParamsPageMap(3);
        paramForHot.put("sortBy", "views");
        model.addAttribute("hotSofts", softService.searchForIndex(paramForHot));  //热门文当推荐
        return "wSoft/soft-detail";
    }

    // 下载
    @RequestMapping(value = "/dl/{uuId}", method = RequestMethod.GET)
    public ResponseEntity<ResultOut> dlCodeGet(@PathVariable long uuId) {
        SoftUpload doc = softService.find(uuId);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        User user = getRequiredCurrentUser();
        softService.downloadRecord(user, doc);
        List<SoftDownloads> urls = ossService.getObjectUrl(doc);
        // 增加下载统计
        //  monthReportService.addDocDownloads();
        return new ResponseEntity<>(getResultOut(M.I10200.getCode()).setData(urls), HttpStatus.OK);
    }


    @RequestMapping(value = "/collect", method = RequestMethod.GET)
    public ResponseEntity collectGet(@RequestParam String uuId) {
        softCollectService.docCollect(getRequiredCurrentUser().getId(), uuId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 取消收藏
    @RequestMapping(value = "/uncollect", method = RequestMethod.GET)
    public ResponseEntity uncollectPost(String uuId) {
        softCollectService.uncollect(getRequiredCurrentUser().getId(), uuId);
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "unConllect", method = RequestMethod.POST)
    public String doUnconllect() {
        String[] myconllects = request.getParameterValues("myconllects");
        if (myconllects != null && myconllects.length > 0) {
            for (String myConllectId : myconllects) {
                softCollectService.uncollect(getRequiredCurrentUser().getId(), myConllectId);
            }
        }
        return "redirect:/soft/myFav";
    }

    //我的下载
    @RequestMapping(value = "myDownload", method = RequestMethod.GET)
    public String myDownload(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> param = new HashMap<>();
        param.put("userId", user.getId());
        List<SoftUploadDto> myDownloads = softService.getDownloads(param);
        model.addAttribute("docs", myDownloads);
        return "wSoft/soft-mydownload";
    }

    @RequestMapping(value = "myUpload", method = RequestMethod.GET)
    public String myUpload(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> param = new HashMap<>();
        param.put("uploader", user.getId());
        param.put("unstate", 9);
        param.put("uploaderType", Const.DOC_UPLOADERTYPE_USER);
        List<SoftUpload> myUploads = softService.getUploads(param);
        model.addAttribute("docs", myUploads);
        return "wSoft/soft-myupload";
    }

    @RequestMapping(value = "myFav", method = RequestMethod.GET)
    public String myFav(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> param = new HashMap<>();
        param.put("userId", user.getId());
        List<SoftUploadDto> myFavs = softService.getCollects(param);
        model.addAttribute("docs", myFavs);
        return "wSoft/soft-myfav";
    }


    @RequestMapping(value = "/credential", method = RequestMethod.GET)
    public
    @ResponseBody
    Credential getCredential() throws ClientException {
        User user = getRequiredCurrentUser();
        Credential credential = ossService.getCredential();
        credential.setUserId(user.getId());
        credential.setFileDir(makeUpDir());
        credential.setHost(CredentialUtil.HOST);
        credential.setUuId(makeUpUuId());
        return credential;
    }


    @RequestMapping(value = "/callBack", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> uploadZipCallBack(HttpServletRequest request) {
        String str = request.getParameter("my_var");
        String object = request.getParameter("object");
        long size = Long.parseLong(request.getParameter("size"));
        String baseStr = "";
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            baseStr = new String(decoder.decode(str), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("********** BASE64解码异常 ******");
        }
        JSONObject jsonobject = JSONObject.fromObject(baseStr);
        String uuId = jsonobject.getString("uuId");
        String title = jsonobject.getString("title");
        long userId = jsonobject.getLong("userId");
        int costScore = jsonobject.getInt("costScore");
        String fileName = jsonobject.getString("fileName");
        String fileType = jsonobject.getString("fileType");
        String description = jsonobject.getString("description");
        String specification = jsonobject.getString("specification");
        String softCategoryId = jsonobject.getString("softCategoryId");
        SoftUpload upload = new SoftUpload(Long.parseLong(uuId), Integer.parseInt(softCategoryId), userId, title, costScore);
        upload.setState(Const.DOC_STATE_REVIEWING);
        upload.setDescription(description);
        upload.setSpecification(specification);
        upload.setCreateTime(new Date());
        upload.setViews(0L);
        upload.setDownloads(0L);
        upload.setUploaderType(Const.DOC_UPLOADERTYPE_USER);
        SoftDoc soft = new SoftDoc();
        soft.setFileName(fileName);
        soft.setFilePath(object);
        soft.setFileSize(size);
        soft.setFileType(fileType);
        soft.setCreateTime(new Date());
        softService.create(upload, soft);
        Map<String, String> map = new HashMap<String, String>();
        map.put("Status", "OK");
        return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
    }


    //删除文档
    @ResponseBody
    @RequestMapping(value = "softDelete", method = RequestMethod.POST)
    public ResultOut docDelete(@RequestParam("ids[]") long[] ids) {
        User user = getRequiredCurrentUser();
        List<String> msg = new ArrayList<>();
        for (long id : ids) {
            SoftUpload doc = softService.find(id);
            if (doc != null && doc.getState() != Const.DOC_STATE_LOADING) {
                if (!(doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER && (doc.getUploader().equals(user.getId())))) {
                    msg.add("软件【" + doc.getTitle() + "】不可删除，原因：没有权限");
                } else if (softService.countDocDownloads(id) > 0) {
                    msg.add("软件【" + doc.getTitle() + "】不可删除，原因：有下载记录");
                } else {
                    softService.delete(doc);
                }
            } else {
                msg.add("软件 " + id + " 不存在或当前不可删除");
            }
        }
        if (!msg.isEmpty()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(msg);
        }
        return getResultOut(M.I10200.getCode());
    }

    private String makeUpDir() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date now = new Date();
        String str = sdf.format(now);
        StringBuilder builder = new StringBuilder("lld_zip");
        builder.append(str);
        builder.append('/');
        String dirName = builder.toString();
        ossService.makeUpDir(dirName);
        return dirName;
    }

    private long makeUpUuId() {
        SecureRandom random = new SecureRandom();
        long temp = random.nextLong();
        return Math.abs(temp);
    }

    private String jsonStringConvert(String s) {
        char[] temp = s.toCharArray();
        int n = temp.length;
        for (int i = 0; i < n; i++) {
            if (temp[i] == ':' && temp[i + 1] == '"') {
                for (int j = i + 2; j < n; j++) {
                    if (temp[j] == '"') {
                        if (temp[j + 1] != ',' && temp[j + 1] != '}') {
                            temp[j] = '”';
                        } else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
                            break;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }
}