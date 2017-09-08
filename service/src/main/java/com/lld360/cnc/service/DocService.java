package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.DocUtils;
import com.lld360.cnc.dto.DocDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import com.lld360.cnc.wxmsg.DataTemp;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DocService extends BaseService {

    // 文档水印透明度
    private static final float DOC_WATERMARK_OPACITY = 0.6f;

    private static final String DOC_WATERMARK_FILE = "/d6sk_watermark.png";

    private static final String STATE_STR = "state";

    @Autowired
    private MonthReportService monthReportService;

    @Autowired
    private DocDao docDao;

    @Autowired
    private DocTagDao docTagDao;

    @Autowired
    private DocDownloadDao docDownloadDao;

    @Autowired
    private DocImageDao docImageDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DocModeratorScoreDao moderatorScoreDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    private UserPointDao userPointDao;

    @Autowired
    private UserPointHistoryDao userPointHistoryDao;

    @Autowired
    private SynonymService synonymService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private SettingService settingService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    public Page<Doc> search(Map<String, Object> parameters) {
        if (parameters.containsKey(STATE_STR)) {
            if ("0".equals(parameters.get(STATE_STR).toString()))
                parameters.remove(STATE_STR);
        } else {
            parameters.put(STATE_STR, Const.DOC_STATE_NORMAL);
        }
        Pageable pageable = getPageable(parameters);
        long count = docDao.count(parameters);
        List<Doc> userJobFeeList = docDao.search(parameters);
        return new PageImpl<>(userJobFeeList, pageable, count);
    }

    public long count(Map<String, Object> parameters) {
        return docDao.count(parameters);
    }

    public Doc find(Long id) {
        //增加文库月统计访问信息
        monthReportService.addDocViews();
        return docDao.find(id);
    }

    public Doc get(Long id) {
        return docDao.find(id);
    }

    public Page<Doc> searchWeb(Map<String, Object> params) {
        if (!params.containsKey(STATE_STR) && !params.containsKey("states"))
            params.put(STATE_STR, Const.DOC_STATE_NORMAL);
        // 分词查询
        String contentStr = "content";
        if (params.containsKey(contentStr)) {
            String content = (String) params.get(contentStr);
            if (!content.matches("\\w+"))
                content = synonymService.getSynonymsRegexp(content);
            params.put("contentSort", content.split("\\|"));
            params.put(contentStr, content);
        }

        Pageable pageable = getPageable(params);
        long count = docDao.countWeb(params);
        List<Doc> userJobFeeList = docDao.searchWeb(params);
        return new PageImpl<>(userJobFeeList, pageable, count);
    }

    public long countWeb(Map<String, Object> parameters) {
        return docDao.countWeb(parameters);
    }

    public Doc findWeb(Long id) {
        Doc doc = docDao.findWeb(id);
        if (doc == null || doc.getId() == null)
            throw new ServerException(HttpStatus.NOT_FOUND);
        doc.setViews(doc.getViews() + 1);
        docDao.update(doc);
        return doc;
    }

    @Transactional
    public void create(Doc doc) {
        doc.setState(Const.DOC_STATE_LOADING);
        doc.setViews(0L);
        if (doc.getFilePages() == null) {
            doc.setFilePages(1);
        }
        doc.setCreateTime(new Date());
        docDao.create(doc);
        setDocTags(doc);
    }

    @Transactional
    public boolean update(Doc doc) {
        setDocTags(doc);
        return docDao.update(doc) > 0;
    }

    @Transactional
    public void deleteLogic(Doc doc) {
        docDao.updateDocState(doc.getId(), Const.DOC_STATE_DELETE);
        if (doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
            // 添加文档被删除消息
            UserMessage docDelMsg = new UserMessage(doc.getUploader(), UserMessage.TYPE_DOC_DELETE, doc.getId(), "文档被删除", "文档《" + doc.getTitle() + "》被删除。");
            userMessageService.add(docDelMsg);
        }

        // 版主贡献值相关
        if (doc.getState() == Const.DOC_STATE_NORMAL && userDao.isModerator(doc.getUploader())) {
            if (moderatorScoreDao.existModeratorScore(doc.getUploader())) {
                moderatorScoreDao.updateModetatorScore(doc.getUploader(), Const.MODERATOR_CANCEL_CONTRIBUTIONS);
            } else {
                moderatorScoreDao.createModetatorScore(doc.getUploader(), Const.MODERATOR_CANCEL_CONTRIBUTIONS);
            }
            ModeratorScoreHistory scoreHistory = new ModeratorScoreHistory(doc.getUploader(), Const.MODERATOR_CONTRIBUTIONS_TYPE_DOCCANCEL, Const.MODERATOR_CANCEL_CONTRIBUTIONS, doc.getId(), "文档《" + doc.getTitle() + "》被删除扣除贡献值");
            moderatorScoreDao.createModeratorScoreHistory(scoreHistory);
        }
    }

    @Transactional
    public void delete(Doc doc) {
        // 扣除用户通过该文档获得的积分
        if (doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
            /*扣除用户通过该文档获得的积分
            int scores = docDao.sumScoreOfDoc(doc.getId());
            UserScore userScore = userScoreDao.find(doc.getUploader());
            if (userScore != null && scores > 0 && userScore.getTotalScore() > 0) {
                if (userScore.getTotalScore() - scores < 0) {
                    scores = userScore.getTotalScore();
                }
                userScoreDao.updateScore(doc.getUploader(), -scores);

                // 记录积分历史
                UserScoreHistory history = new UserScoreHistory(doc.getUploader(), Const.USER_SOCRE_HISTORY_TYPE_DOC_DELETE, -scores, doc.getId());
                history.setDescription("删除文档《" + doc.getTitle() + "》扣除所得积分");
                userScoreHistoryDao.create(history);
                // 添加积分变动消息
                String message = "上传的文档《" + doc.getTitle() + "》被删除，扣除该文档所得的[" + scores + "]分。";
                userMessageService.add(new UserMessage(doc.getUploader(), UserMessage.TYPE_SCORE_MINUS, doc.getId(), "文档删除扣分", message));
            }*/
            if (doc.getState() != Const.DOC_STATE_DELETE) {
                // 添加文档被删除消息
                UserMessage docDelMsg = new UserMessage(doc.getUploader(), UserMessage.TYPE_DOC_DELETE, doc.getId(), "文档被删除", "文档《" + doc.getTitle() + "》被删除。");
                userMessageService.add(docDelMsg);
            }
        }

        // 版主贡献值相关
        if (userDao.isModerator(doc.getUploader())) {
            if (moderatorScoreDao.existModeratorScore(doc.getUploader())) {
                moderatorScoreDao.updateModetatorScore(doc.getUploader(), Const.MODERATOR_CANCEL_CONTRIBUTIONS);
            } else {
                moderatorScoreDao.createModetatorScore(doc.getUploader(), Const.MODERATOR_CANCEL_CONTRIBUTIONS);
            }
            ModeratorScoreHistory scoreHistory = new ModeratorScoreHistory(doc.getUploader(), Const.MODERATOR_CONTRIBUTIONS_TYPE_DOCCANCEL, Const.MODERATOR_CANCEL_CONTRIBUTIONS, doc.getId(), "文档《" + doc.getTitle() + "》被删除扣除贡献值");
            moderatorScoreDao.createModeratorScoreHistory(scoreHistory);
        }

        docDao.delete(doc.getId());

        File dir = new File(configer.getFileBasePath() + "doc/" + doc.getId());
        if (dir.isDirectory()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void setDocTags(Doc doc) {
        if (doc.getTags() == null) {
            return;
        }
        docTagDao.deleteDocXTagsByDocId(doc.getId());
        if (!doc.getTags().isEmpty()) {
            List<String> tags = doc.getTags().stream().filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            DocTag tag;
            List<DocXTag> axts = new ArrayList<>();
            for (String t : tags) {
                tag = docTagDao.find(t);
                if (tag == null) {
                    tag = new DocTag(t);
                    docTagDao.create(tag);
                }
                axts.add(new DocXTag(doc.getId(), tag.getId()));
            }
            if (!axts.isEmpty()) {
                docTagDao.createDocXTags(axts);
            }
        }
    }

    // 用户上传文档文件
    @Transactional
    public Doc save(MultipartFile file, Doc doc) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = FilenameUtils.getExtension(fileName).toLowerCase();
        if (!fileType.matches(Const.DOC_ALLOW_TYPES.replaceAll(",", "|"))) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10107);
        }
        doc.setFileMd5(DigestUtils.md5Hex(file.getBytes()));

        // 如果是上传过的文件则提示文件已存在
        List<Doc> olds = docDao.findByMd5(doc.getFileMd5());
        if (!olds.isEmpty()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10106);
        }
        if (doc.getId() == null) {
            create(doc);
        }

        if (fileType.equals("jpeg")) {
            fileType = "jpg";
        }

        String oldFile = doc.getFilePath();

        String docPath = "doc/" + doc.getId() + "/" + UUID.randomUUID().toString() + "." + fileType;
        File docFile = new File(configer.getFileBasePath() + docPath);
        if (docFile.getParentFile().isDirectory() || docFile.getParentFile().mkdirs()) {
            file.transferTo(docFile);
            // 给PDF添加水印
            if (fileType.equals("pdf")) {
                URL watermarkUrl = getClass().getResource(DOC_WATERMARK_FILE);
                String targetPdfFile = configer.getFileBasePath() + docPath.replaceAll(".pdf$", "_wm.pdf");
                DocUtils.addWatermarkToPdf(docFile, targetPdfFile, watermarkUrl, DOC_WATERMARK_OPACITY);
            }
        }
        doc.setFileName(file.getOriginalFilename());
        doc.setFilePath(docPath);
        Long fileSize = (long) Math.ceil(file.getSize() / 1024);
        doc.setFileSize(fileSize);
        doc.setFileType(fileType);
        doc.setState(Const.DOC_STATE_LOADING);
        update(doc);

        delDocFile(oldFile);

        // 使用异步线程分析保存Doc图片
        //taskExecutor.execute(() -> saveDocImages(doc, docFile));
        return doc;
    }

    // 删除旧文档
    private void delDocFile(String docRelativeFile) {
        if (docRelativeFile != null) {
            String filePath = configer.getFileBasePath() + docRelativeFile;
            FileUtils.deleteQuietly(new File(filePath));
            if (filePath.endsWith(".pdf")) {
                String wmFilePath = configer.getFileBasePath() + docRelativeFile.replaceAll(".pdf$", "_wm.pdf");
                FileUtils.deleteQuietly(new File(wmFilePath));
            }
        }
    }

    // 保存Doc图片
    @Transactional
    private void saveDocImages(Doc doc, File file) {

        List<String> images = new ArrayList<>();
        byte state = doc.getState() == Const.DOC_STATE_NORMAL ? doc.getState() : Const.DOC_STATE_REVIEWING;
        int totalPages = 1;
        try {
            String imagePath = configer.getFileBasePath() + "doc/" + doc.getId();
            switch (doc.getFileType()) {
                case "pdf":
                    deleteDocImages(doc);
                    totalPages = DocUtils.convertPdf2ImagesByAsposePdf(images, file, imagePath, Const.DOC_IMG_PAGES);
                    break;
                case "doc":
                case "docx":
                    deleteDocImages(doc);
                    totalPages = DocUtils.convertWords2ImageByAsposeWord(images, file, imagePath, Const.DOC_IMG_PAGES);
                    break;
                case "xls":
                case "xlsx":
                    deleteDocImages(doc);
                    totalPages = DocUtils.convertExcel2ImageByAsposeCells(images, file, imagePath, Const.DOC_IMG_PAGES);
                    break;
                case "ppt":
                case "pptx":
                    deleteDocImages(doc);
                    totalPages = DocUtils.convertPPT2ImageByAsposeSlides(images, file, imagePath, Const.DOC_IMG_PAGES);
                    break;
                default:
                    state = Const.DOC_STATE_REVIEWING;
                    break;
            }
        } catch (Exception e) {
            logger.error("解析文档<" + doc.getId() + ">为图片异常", e);
            state = Const.DOC_STATE_LOADING_FAILE;
        }
        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                String img = "doc/" + doc.getId() + "/" + images.get(i);
                doc.addImage(img, i + 1);
            }
            logger.info(String.format("《%s》共%d页，解析图片%d张。", doc.getTitle(), totalPages, images.size()));
        }
        if (docDao.find(doc.getId()) != null) {
            Doc doc2 = new Doc();
            doc2.setId(doc.getId());
            doc2.setState(state);
            doc2.setFilePages(totalPages);
            docDao.update(doc2);
            if (doc.getImages() != null && !doc.getImages().isEmpty())
                docImageDao.save(doc.getImages());
        } else {
            deleteDocImages(doc);
        }
    }

    /**
     * 文档审核
     *
     * @param id       文档ID
     * @param reviewOk 是否通过
     * @return 文档状态
     */
    @Transactional
    public byte docReview(long id, boolean reviewOk) {
        Doc doc = docDao.find(id);
        checkDocExist(doc);
        byte state = reviewOk ? Const.DOC_STATE_NORMAL : Const.DOC_STATE_REVIEW_DENY;
        docDao.updateDocState(doc.getId(), state);
        if (reviewOk && doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
            int point = Const.USER_ADDPOINT_FREE_OPERATE;
            userPointDao.updatePoint(doc.getUploader(), point);
            // 添加上传文档增加积分的消息
            String msg = "上传文档《" + doc.getTitle() + "》获得[" + point + "]积分。";
            UserPointHistory history = new UserPointHistory(doc.getUploader(), Const.USER_POINT_HISTORY_TYPE_DOC_UPLOAD, point, doc.getId(), msg);
            userPointHistoryDao.create(history);
            // 添加上传文档审核通过的消息
            userMessageService.add(new UserMessage(doc.getUploader(), UserMessage.TYPE_DOC_REVIEW_OK, doc.getId(), "文档审核通过", "上传的文档《" + doc.getTitle() + "》审核通过。"));
            // 版主贡献值相关
            Date now = Calendar.getInstance(Locale.CHINA).getTime();
            long dailyScore = moderatorScoreDao.getDailyScore(doc.getUploader(), Const.MODERATOR_CONTRIBUTIONS_TYPE_UPLOAD, now);
            if (userDao.isModerator(doc.getUploader()) && dailyScore < Const.MODERATOR_UPLOAD_CONTRIBUTIONS_LIMIT)
                docUploadAddModerScore(doc);
            // push消息
            ThirdAccount thirdAccount = userService.getThirdAccountByUserid(doc.getUploader(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
            if (thirdAccount != null)
                wxTempMsgService.sendCheckResultWxMsg(thirdAccount, "文档上传", new DataTemp(doc.getTitle(), ""), "文档审核通过");

        } else if (!reviewOk && doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
            // 添加上传文档审核未通过的消息
            userMessageService.add(new UserMessage(doc.getUploader(), UserMessage.TYPE_DOC_REVIEW_DENY, doc.getId(), "文档审核未通过", "上传的文档《" + doc.getTitle() + "》审核未通过。"));
            // push消息
            ThirdAccount thirdAccount = userService.getThirdAccountByUserid(doc.getUploader(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
            if (thirdAccount != null)
                wxTempMsgService.sendCheckResultWxMsg(thirdAccount, "文档上传", new DataTemp(doc.getTitle(), ""), "文档审核未通过");
        }
        return state;
    }

    /*添加版主贡献值*/
    private void docUploadAddModerScore(Doc doc) {
        if (moderatorScoreDao.existModeratorScore(doc.getUploader())) {
            moderatorScoreDao.updateModetatorScore(doc.getUploader(), Const.MODERATOR_UPLOAD_CONTRIBUTIONS);
        } else {
            moderatorScoreDao.createModetatorScore(doc.getUploader(), Const.MODERATOR_UPLOAD_CONTRIBUTIONS);
        }
        ModeratorScoreHistory scoreHistory = new ModeratorScoreHistory(doc.getUploader(), Const.MODERATOR_CONTRIBUTIONS_TYPE_UPLOAD, Const.MODERATOR_UPLOAD_CONTRIBUTIONS, doc.getId(), "上传文档《" + doc.getTitle() + "》审核通过增加贡献值");
        moderatorScoreDao.createModeratorScoreHistory(scoreHistory);
    }

    /*判断文档是否存在*/
    private void checkDocExist(Doc doc) {
        if (doc == null) throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        if (doc.getFileMd5() == null) throw new ServerException(HttpStatus.NOT_FOUND, M.E10105);
    }

    // 删除文档图片
    private void deleteDocImages(Doc doc) {
        List<DocImage> images = docImageDao.findByDocId(doc.getId());
        File file;
        for (DocImage image : images) {
            file = new File(configer.getFileBasePath() + image.getPath());
            if (file.exists() && file.isFile()) {
                FileUtils.deleteQuietly(file);
            }
        }
        docImageDao.deleteByDocId(doc.getId());
        doc.setImages(null);
    }

    // 读取txt文件内容
    public String getTxtContent(File file, int size) {
        try {
            String charset = DocUtils.getTxtFileCharset(file);
            String content = FileUtils.readFileToString(file, charset);
            if (content != null) {
                if (content.length() > size) {
                    content = content.substring(0, size) + "……";
                }
                return "<p>" + content.replaceAll("[\r\n]+", "</p><p>") + "</p>";
            }
        } catch (IOException e) {
            logger.warn("读取TXT文件内容失败！");
        }
        return null;
    }

    //获取用户收藏的文章
    public List<DocDto> getCollects(Map<String, Object> param) {
        return docDao.getCollects(param);
    }

    //获取用户下载过的文章
    public List<DocDto> getDownloads(Map<String, Object> param) {
        return docDao.getDownloads(param);
    }

    //获取用户上传的文章
    public List<Doc> getUploads(Map<String, Object> param) {
        return docDao.search(param);
    }

    // 用户下载文档文件
    @Transactional
    public File download(Doc doc) {
        if (doc.getFilePath() == null) {
            logger.warn("文档没有可下载的文件：" + doc.getId());
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        File file = new File(configer.getFileBasePath() + doc.getFilePath().replaceAll("." + doc.getFileType() + "$", "_wm." + doc.getFileType()));
        if (!file.exists()) {
            file = new File(configer.getFileBasePath() + doc.getFilePath());
            if (!file.exists()) {
                logger.warn("文档的文件不存在：" + doc.getFilePath());
                throw new ServerException(HttpStatus.NOT_FOUND, M.S90404).setData(file.getAbsoluteFile());
            }
        }
        return file;
    }

    // 添加下载记录和下载牛人币计算
    @Transactional
    public void downloadRecord(User user, Doc doc) {
        if (!docDownloadDao.hasDownload(user.getId(), doc.getId())) {
            DocDownload download = new DocDownload(user.getId(), doc.getId(), doc.getCostScore());
            docDownloadDao.create(download);

            // 扣分条件：1.需要牛人币, 2.不是自己的文件
            Integer costScore = doc.getCostScore();
            if (userMemberService.isMember(user.getId())) {
                BigDecimal cs = new BigDecimal(costScore * Const.USER_MEMBER_DISCOUNT).setScale(0, BigDecimal.ROUND_HALF_UP);
                costScore = cs.intValue();
            }
            if (costScore > 0 && (doc.getUploaderType() != Const.DOC_UPLOADERTYPE_USER || !user.getId().equals(doc.getUploader()))) {
                UserScore userScore = userScoreDao.find(user.getId());
                if (userScore.getTotalScore() < costScore) {       //积分不足
                    throw new ServerException(HttpStatus.FORBIDDEN, M.E20001);
                }

                // 给下载者扣牛人币
                userScoreDao.updateScore(user.getId(), -costScore);
                String dd = "下载文档《" + doc.getTitle() + "》扣除牛人币";
                userScoreHistoryDao.create(new UserScoreHistory(user.getId(), Const.USER_SCORE_HISTORY_TYPE_DOC_DOWNLOAD, -costScore, download.getId(), dd));
                // 加积分
                userPointDao.updatePoint(user.getId(), costScore * Const.USER_ADDPOINT_SCALE_SCORE_OPERATE);
                userPointHistoryDao.create(new UserPointHistory(user.getId(), Const.USER_POINT_HISTORY_TYPE_DOC_DOWNLOAD, costScore * Const.USER_ADDPOINT_SCALE_SCORE_OPERATE, download.getId(), dd));
                // 添加下载文档扣分的消息
                String msgDownload = "下载文档《" + doc.getTitle() + "》扣除[" + costScore + "]牛人币。";
                userMessageService.add(new UserMessage(user.getId(), UserMessage.TYPE_SCORE_MINUS, doc.getId(), "下载文档扣除牛人币", msgDownload));

                // 给上传者加牛人币
                if (doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
                    userScoreDao.updateScore(doc.getUploader(), costScore);
                    String du = "文档《" + doc.getTitle() + "》被下载";
                    userScoreHistoryDao.create(new UserScoreHistory(doc.getUploader(), Const.USER_SCORE_HISTORY_TYPE_DOC_SALE, costScore, download.getId(), du));

                    // 添加文档被下载加分的消息/模板消息
                    String msg = "文档《" + doc.getTitle() + "》被下载增加【" + costScore + "】牛人币。";
                    userMessageService.add(new UserMessage(doc.getUploader(), UserMessage.TYPE_SCORE_ADD, doc.getId(), "文档被下载加牛人币", msg));
                    // 模板消息
                    ThirdAccount thirdAccount = userService.getThirdAccountByUserid(doc.getUploader(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
                    if (thirdAccount != null) {
                        UserScore score = userScoreDao.find(doc.getUploader());
                        String msg2 = "亲爱的大牛用户, 您所传文档《" + doc.getTitle() + "》被下载, 产生新的牛人币变动";
                        wxTempMsgService.sendScoreChgWxMsg(thirdAccount, msg2, costScore, score.getTotalScore());
                    }
                }
                //  给邀请者和区域合伙人分利
                userInviteService.doBonusByInvite(user, costScore);
            }
        }
    }

    // 获取文档下载量
    public long countDocDownloads(long id) {
        return docDownloadDao.countByDoc(id);
    }

    public List<DocImage> findByDocId(long docId) {
        return docImageDao.findByDocId(docId);
    }

    public List<DocDownload> docDownloadsearch() {
        Map<String, Object> parameters = new HashMap<>();
        return docDownloadDao.search(parameters);
    }

    public long getDownloadCount(Map<String, Object> params) {
        return docDownloadDao.count(params);
    }

    //首页数据
    public List<Doc> searchForIndex(Map<String, Object> params) {
        params.put(STATE_STR, Const.DOC_STATE_NORMAL);
        return docDao.searchForIndex(params);
    }

    /**
     * 批量修改文档状态
     *
     * @param ids       需要改变的DOC的ID数组，如果为空，则对符合fromState的进行全部修改
     * @param toState   目标状态
     * @param fromState 原状态，为null时直接操作所有ids包含的文档，此项和ids同时为null时将不进行任何操作
     * @return 修改成功的数量
     */
    public long changeState(Long[] ids, byte toState, Byte fromState) {
        if (ids != null && ids.length == 0) {
            ids = null;
        }
        if (ids == null && fromState == null) {
            return 0;
        }
        return docDao.updateDocsState(ids, toState, fromState);
    }

    /**
     * 修改文档分类类型
     *
     * @param docId      文档ID
     * @param categoryId 分类ID
     * @return 影响行数
     */
    public int changeCategory(long docId, int categoryId) {
        DocCategory category = docCategoryDao.find(categoryId);
        if (category == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "目标分类不存在");
        }
        if (category.getFid() == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "目标类型只能是第二级分类");
        }
        Doc doc = docDao.find(docId);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, "文档不存在");
        }
        docDao.updateDocState(docId, Const.DOC_STATE_REVIEWING);
        return docDao.updateDocCategory(docId, categoryId);
    }

    /// <管理工具专用方法
    // 指定单个文档补全
    public Doc completionImagesAndPdfWatermarkFile(long id) {
        Doc doc = docDao.find(id);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND);
        }

        if (StringUtils.isEmpty(doc.getFilePath())) {
            logger.error(String.format("数据库中doc.id=<%d>文件不存在。", doc.getId()));
            return doc;
        }

        File file = new File(configer.getFileBasePath() + doc.getFilePath());
        if (!file.exists()) {
            logger.error(String.format("物理文件doc.id=<%d>文件不存在。", doc.getId()));
            return doc;
        }

        try {

            if (doc.getFileType().matches("pdf|doc|docx|xls|xlsx|ppt|pptx")) {
                logger.info("补全文档<" + doc.getId() + "> 类型：" + doc.getFileType());
                List<DocImage> images = docImageDao.findByDocId(doc.getId());
                if (images.size() < 50) {
                    saveDocImages(doc, file);
                } else {
                    if (doc.getState().equals(Const.DOC_STATE_LOADING)) {
                        doc.setState(Const.DOC_STATE_REVIEWING);
                        docDao.update(doc);
                    }
                }
                if (doc.getFileType().equals("pdf")) {
                    File wmFile = new File(configer.getFileBasePath() + doc.getFilePath().replaceAll(".pdf$", "_wm.pdf"));
                    if (!wmFile.exists()) {
                        URL watermarkUrl = getClass().getResource(DOC_WATERMARK_FILE);
                        DocUtils.addWatermarkToPdf(file, wmFile.getAbsolutePath(), watermarkUrl, DOC_WATERMARK_OPACITY);
                    }
                }

                logger.info(String.format("<%d>完成，共%d页，图片%d张。", doc.getId(), doc.getFilePages(), (doc.getImages() == null ? images.size() : doc.getImages().size())));
            } else if (doc.getState().equals(Const.DOC_STATE_LOADING)) {
                doc.setState(Const.DOC_STATE_REVIEWING);
                docDao.update(doc);
                logger.info(String.format("<%d>是%s文件，更新其异常状态。", doc.getId(), doc.getFileType()));
            }

        } catch (Exception e) {
            logger.warn("文档<" + doc.getId() + ">补全图片和添加水印异常：", e);
        }
        return doc;
    }

    // 默认所有文档检查补全
    public void completionImagesAndPdfWatermarkFile() {
        List<Doc> docs = docDao.findDisposingDocs();

        Calendar time = Calendar.getInstance();
        time.add(Calendar.DATE, -1);
        docs.stream().filter(d -> d.getCreateTime().before(time.getTime())).forEach(doc -> {
            completionImagesAndPdfWatermarkFile(doc.getId());
        });
    }
    /// >管理工具专用方法
}
