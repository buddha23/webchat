package com.lld360.cnc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import com.lld360.cnc.wxmsg.DataTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.SoftUploadDto;

@Service
public class SoftService extends BaseService {

    @Autowired
    private SoftFileDao softFileDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    private UserPointDao userPointDao;

    @Autowired
    private UserPointHistoryDao userPointHistoryDao;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private OssService ossService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private SoftDownloadDao softDownloadDao;

    @Autowired
    private SynonymService synonymService;

    @Autowired
    private SoftUploadDao softUploadDao;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    public Page<SoftUpload> searchWeb(Map<String, Object> params) {
        if (!params.containsKey("state") && !params.containsKey("states"))
            params.put("state", Const.DOC_STATE_NORMAL);
        // 分词查询
        if (params.containsKey("content")) {
            String content = (String) params.get("content");
            if (!content.matches("\\w+"))
                content = synonymService.getSynonymsRegexp(content);
            params.put("contentSort", content.split("\\|"));
            params.put("content", content);
        }

        Pageable pageable = getPageable(params);
        long count = softUploadDao.countWeb(params);
        List<SoftUpload> userJobFeeList = softUploadDao.searchWeb(params);
        return new PageImpl<>(userJobFeeList, pageable, count);
    }

    public Page<SoftUpload> search(Map<String, Object> parameters) {
        Pageable pageable = getPageable(parameters);
        long count = softUploadDao.count(parameters);
        List<SoftUpload> userJobFeeList = softUploadDao.search(parameters);
        return new PageImpl<>(userJobFeeList, pageable, count);
    }

    public SoftUpload find(long uuId) {
        SoftUpload soft = softUploadDao.find(uuId);
        if (null == soft) {
            return null;
        }
        makeUpSoftFile(soft);
        return soft;
    }

    @Transactional
    public void delete(SoftUpload doc) {
        if (doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
            // 扣除用户通过该文档获得的积分
//            int scores = softUploadDao.sumScoreOfSoft(doc.getUuId());
//            int downloadScores = doc.getCostScore() * Integer.parseInt(doc.getDownloads().toString());
//            scores += downloadScores;
//            UserScore userScore = userScoreDao.find(doc.getUploader());
//            if (userScore != null && scores > 0 && userScore.getTotalScore() > 0) {
//                if (userScore.getTotalScore() - scores < 0) {
//                    scores = userScore.getTotalScore();
//                }
//                userScoreDao.updateScore(doc.getUploader(), -scores);
//
//                // 记录积分历史
//                UserScoreHistory history = new UserScoreHistory(doc.getUploader(), Const.USER_SOCRE_HISTORY_TYPE_SOFT_DELETE, -scores, doc.getUuId());
//                history.setDescription("删除软件《" + doc.getTitle() + "》扣除所得积分");
//                userScoreHistoryDao.create(history);
//                // 添加积分变动消息
//                String message = "上传的软件《" + doc.getTitle() + "》被删除，扣除该软件所得的[" + scores + "]分。";
//                userMessageService.add(new UserMessage(doc.getUploader(), UserMessage.TYPE_SCORE_MINUS, doc.getUuId(), "软件删除扣分", message));
//            }
            // 添加文档被删除消息
            UserMessage docDelMsg = new UserMessage(doc.getUploader(), UserMessage.TYPE_DOC_DELETE, doc.getUuId(), "软件被删除", "软件《" + doc.getTitle() + "》被删除。");
            userMessageService.add(docDelMsg);
        }
        ossService.deleteObject(doc);
        softUploadDao.delete(doc.getUuId());
    }

    @Transactional
    public byte review(long id, boolean reviewOk) {
        SoftUpload soft = softUploadDao.find(id);
        if (soft == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        if (soft.getState() != Const.DOC_STATE_REVIEWING) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("当前状态不可以执行审核操作").setData(soft.getState());
        }
        byte state = reviewOk ? Const.DOC_STATE_NORMAL : Const.DOC_STATE_REVIEW_DENY;
        softUploadDao.updateSoftState(soft.getUuId(), state);

        if (reviewOk && soft.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
            int point = Const.USER_ADDPOINT_FREE_OPERATE;
            userPointDao.updatePoint(soft.getUploader(), point);
            // 添加上传文档增加积分的消息
            String msg = "上传软件《" + soft.getTitle() + "》获得[" + point + "]积分。";
            UserPointHistory history = new UserPointHistory(soft.getUploader(), Const.USER_POINT_HISTORY_TYPE_SOFT_UPLOAD, point, soft.getUuId(), msg);
            userPointHistoryDao.create(history);
//            userMessageService.add(new UserMessage(soft.getUploader(), UserMessage.TYPE_SCORE_ADD, soft.getUuId(), "软件上传加分", msg));
            // 添加上传文档审核通过的消息
            userMessageService.add(new UserMessage(soft.getUploader(), UserMessage.TYPE_DOC_REVIEW_OK, soft.getUuId(), "软件审核通过", "上传的软件《" + soft.getTitle() + "》审核通过。"));
            // push消息
            ThirdAccount thirdAccount = userService.getThirdAccountByUserid(soft.getUploader(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
            if (thirdAccount != null)
                wxTempMsgService.sendCheckResultWxMsg(thirdAccount, "软件上传", new DataTemp(soft.getTitle(), ""), "文档审核通过");
        } else if (!reviewOk && soft.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
            // 添加上传文档审核未通过的消息
            userMessageService.add(new UserMessage(soft.getUploader(), UserMessage.TYPE_DOC_REVIEW_DENY, soft.getUuId(), "软件审核未通过", "上传的软件《" + soft.getTitle() + "》审核未通过。"));
            // push消息
            ThirdAccount thirdAccount = userService.getThirdAccountByUserid(soft.getUploader(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
            if (thirdAccount != null)
                wxTempMsgService.sendCheckResultWxMsg(thirdAccount, "软件上传", new DataTemp(soft.getTitle(), ""), "文档审核未通过");
        }
        return state;
    }

    // 添加下载记录和下载牛人币计算
    @Transactional
    public void downloadRecord(User user, SoftUpload doc) {
        if (!softDownloadDao.hasDownload(user.getId(), doc.getUuId())) {
            SoftDownload download = new SoftDownload(user.getId(), doc.getUuId(), doc.getCostScore());
            softDownloadDao.create(download);
            if (!user.getId().equals(doc.getUploader())) {
                doc.setDownloads(doc.getDownloads() + 1);
                softUploadDao.update(doc);
            }
            // 扣分条件：1.需要牛人币, 2.不是自己的文件
            Integer costScore = doc.getCostScore();
            if (userMemberService.isMember(user.getId())) {
                BigDecimal cs = new BigDecimal(costScore * Const.USER_MEMBER_DISCOUNT).setScale(0, BigDecimal.ROUND_HALF_UP);
                costScore = cs.intValue();
            }
            if (costScore > 0 && (doc.getUploaderType() != Const.DOC_UPLOADERTYPE_USER || !user.getId().equals(doc.getUploader()))) {
                UserScore userScore = userScoreDao.find(user.getId());
                if (userScore.getTotalScore() < costScore) {       //牛人币不足
                    throw new ServerException(HttpStatus.FORBIDDEN, M.E20001);
                }

                // 给下载者扣分
                userScoreDao.updateScore(user.getId(), -costScore);
                String dd = "下载软件《" + doc.getTitle() + "》扣除牛人币";
                userScoreHistoryDao.create(new UserScoreHistory(user.getId(), Const.USER_SCORE_HISTORY_TYPE_SOFT_DOWNLOAD, -costScore, download.getId(), dd));
                // 加积分
                userPointDao.updatePoint(user.getId(), costScore * Const.USER_ADDPOINT_SCALE_SCORE_OPERATE);
                userPointHistoryDao.create(new UserPointHistory(user.getId(), Const.USER_POINT_HISTORY_TYPE_SOFT_DOWNLOAD, costScore * Const.USER_ADDPOINT_SCALE_SCORE_OPERATE, download.getId(), dd));
                // 添加下载文档扣分的消息
                String msgDownload = "下载软件《" + doc.getTitle() + "》扣除[" + costScore + "]牛人币。";
                userMessageService.add(new UserMessage(user.getId(), UserMessage.TYPE_SCORE_MINUS, doc.getUuId(), "下载软件扣牛人币", msgDownload));

                // 给上传者加分
                if (doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
                    userScoreDao.updateScore(doc.getUploader(), costScore);
                    String du = "软件《" + doc.getTitle() + "》被下载";
                    userScoreHistoryDao.create(new UserScoreHistory(doc.getUploader(), Const.USER_SCORE_HISTORY_TYPE_SOFT_SALE, costScore, download.getId(), du));

                    // 添加文档被下载加分的消息
                    String msg = "软件《" + doc.getTitle() + "》被下载增加[" + costScore + "]牛人币。";
                    userMessageService.add(new UserMessage(doc.getUploader(), UserMessage.TYPE_SCORE_ADD, doc.getUuId(), "软件被下载加牛人币", msg));
                }
                //  给邀请者和区域合伙人分利
                userInviteService.doBonusByInvite(user, costScore);
            }
        }
    }


    @Transactional
    public boolean update(SoftUpload soft) {
        return softUploadDao.update(soft) > 0;
    }

    @Transactional
    public void create(SoftUpload upload, SoftDoc soft) {
        if (softUploadDao.find(upload.getUuId()) == null) {
            softUploadDao.create(upload);
        }
        soft.setUuId(upload.getUuId());
        softFileDao.create(soft);
    }

    //首页数据
    public List<SoftUpload> searchForIndex(Map<String, Object> params) {
        params.put("state", Const.DOC_STATE_NORMAL);
        return softUploadDao.searchForIndex(params);
    }

    public SoftUpload findWeb(long uuId) {
        SoftUpload soft = softUploadDao.findWeb(uuId);
        if (null == soft) {
            return null;
        }
        soft.setViews(soft.getViews() + 1);
        softUploadDao.update(soft);
        makeUpSoftFile(soft);
        return soft;
    }


    //获取用户收藏的文章
    public List<SoftUploadDto> getCollects(Map<String, Object> param) {
        return softUploadDao.getCollects(param);
    }

    //获取用户下载过的文章
    public List<SoftUploadDto> getDownloads(Map<String, Object> param) {
        return softUploadDao.getDownloads(param);
    }

    //获取用户上传的文章
    public List<SoftUpload> getUploads(Map<String, Object> param) {
        return softUploadDao.search(param);
    }

    public long countDocDownloads(long uuId) {
        return softDownloadDao.countBySoft(uuId);
    }

    private void makeUpSoftFile(List<SoftUpload> list) {
        for (SoftUpload upload : list) {
            makeUpSoftFile(upload);
        }
    }

    private void makeUpSoftFile(SoftUpload upload) {
        List<SoftDoc> softs = softFileDao.findByUuId(upload.getUuId());
        upload.setSofts(softs);
    }

    public SoftDoc findFile(long id) {

        return softFileDao.find(id);
    }
}
