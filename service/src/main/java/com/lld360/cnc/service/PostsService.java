package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.PostsCommentDto;
import com.lld360.cnc.dto.PostsDto;
import com.lld360.cnc.dto.PostsInfos;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import javafx.geometry.Pos;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.util.*;

/**
 * Author: dhc
 * Date: 2016-11-09 17:33
 */
@Service
public class PostsService extends BaseService {
    private Logger logger = LoggerFactory.getLogger(PostsService.class);

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private PostsCommentDao commentDao;

    @Autowired
    private PostsCommentLikeDao postsCommentLikeDao;

    @Autowired
    private PostsRewardDao postsRewardDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserPointDao userPointDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    private UserPointHistoryDao userPointHistoryDao;

    @Autowired
    private UserMessageDao userMessageDao;

    @Autowired
    private UserService userService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    @Autowired
    private PostsCategoryDao postsCategoryDao;

    public Page<PostsInfos> getPostsByPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        params.put("state", Const.POSTS_STATE_NORMAL);
        long total = postsDao.countPostsInfos(params);
        List<PostsInfos> postsInfosList = postsDao.findPostsInfos(params);
        return new PageImpl<>(postsInfosList, pageable, total);
    }

    public PostsInfos getPostsInfos(long id) {
        postsDao.addViews(id);
        return postsDao.findPostsInfo(id);
    }

    public int changePostsState(long id, byte state) {
        return postsDao.updateState(id, state);
    }

    public void updatePosts(Posts posts) {
        postsDao.update(posts);
    }

    public void deletePosts(long[] ids) {
        postsDao.deletePosts(ids);
    }

    //获取某帖子评论
    public Page<PostsCommentDto> getCommentsByPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long total = commentDao.count(params);
        List<PostsCommentDto> postsCommentList = commentDao.search(params);
        return new PageImpl<>(postsCommentList, pageable, total);
    }

    //添加帖子
    @Transactional
    public void createPosts(PostsDto postsDto) {
        Date now = Calendar.getInstance(Locale.CHINA).getTime();
        postsDto.setCreateTime(now);
        postsDto.setUpdateTime(now);
        postsDto.setState(Const.POSTS_STATE_NORMAL);
        postsDao.create(postsDto);

        createPostsReward(postsDto);

        userPointDao.updatePoint(postsDto.getUserId(), Const.USER_ADDPOINT_FREE_OPERATE);
        String dd = "发布问题【" + postsDto.getTitle() + "】增加积分";
        UserPointHistory userPointHistory = new UserPointHistory(postsDto.getUserId(), Const.USER_POINT_HISTORY_TYPE_POSTS_PUBLISH, Const.USER_ADDPOINT_FREE_OPERATE, postsDto.getId(), dd);
        userPointHistoryDao.create(userPointHistory);
    }

    //添加评论
    @Transactional
    public void createComment(PostsComment postscomment) {
        postscomment.setCreateTime(new Date());
        postscomment.setState(Const.POSTSCOMMENT_STATE_NORMAL);
        commentDao.create(postscomment);

        Posts posts = postsDao.find(postscomment.getPostsId());
        if (posts != null && !postscomment.getUserId().equals(posts.getUserId())) {
            userPointDao.updatePoint(postscomment.getUserId(), Const.USER_ADDPOINT_FREE_OPERATE);
            String dd = "评论问题【" + posts.getTitle() + "】增加积分";
            UserPointHistory userPointHistory = new UserPointHistory(postscomment.getUserId(), Const.USER_POINT_HISTORY_TYPE_POSTS_COMMENT, Const.USER_ADDPOINT_FREE_OPERATE, postscomment.getId(), dd);
            userPointHistoryDao.create(userPointHistory);
        }
    }

    // 获取评论信息
    public PostsCommentDto findCommentById(Long commentId) {
        return commentDao.find(commentId);
    }

    // 保存帖子内容上传的图片
    public String savePostContentImage(MultipartFile file) {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String path = "posts/" + Calendar.getInstance().get(Calendar.YEAR) + "/";
        String imgPath = path + RandomStringUtils.randomAlphanumeric(8) + "." + ext;
        File dic = new File(configer.getFileBasePath() + path);
        if (dic.isDirectory() || dic.mkdirs()) {
            try {
                FileUtils.writeByteArrayToFile(new File(configer.getFileBasePath() + imgPath), file.getBytes());
                return imgPath;
            } catch (IOException e) {
                logger.warn("保存上传文件异常：" + e.getMessage(), e);
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10101, imgPath).setData(e.getMessage());
            }
        }
        throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10102, path);
    }

    public void deleteComment(long id) {
        commentDao.delete(id);
    }

    //悬赏
    @Transactional
    public void createPostsReward(PostsDto postsDto) {
        String dd;
        switch (postsDto.getType()) {
            case Const.POSTS_REWARD_TYPE_FREE:
                break;
            case Const.POSTS_REWARD_TYPE_SCORE:
                UserScore userScore = userScoreDao.find(postsDto.getUserId());
                if (userScore == null || userScore.getTotalScore() < postsDto.getAmount())
                    throw new ServerException(HttpStatus.FORBIDDEN, "牛人币不足,无法发布");
                userScoreDao.updateScore(postsDto.getUserId(), -postsDto.getAmount());
                dd = "发布问题【" + postsDto.getTitle() + "】扣除牛人币";
                UserScoreHistory userScoreHistory = new UserScoreHistory(postsDto.getUserId(), Const.USER_SCORE_HISTORY_TYPE_POSTS_REWARD, -postsDto.getAmount(), postsDto.getId(), dd);
                userScoreHistoryDao.create(userScoreHistory);
                createReward(postsDto);
                break;
            case Const.POSTS_REWARD_TYPE_POINT:
                UserPoint userPoint = userPointDao.find(postsDto.getUserId());
                if (userPoint.getTotalPoint() < postsDto.getAmount())
                    throw new ServerException(HttpStatus.FORBIDDEN, "积分不足,无法发布");
                userPointDao.updatePoint(postsDto.getUserId(), -postsDto.getAmount());
                dd = "发布问题【" + postsDto.getTitle() + "】扣除积分";
                UserPointHistory userPointHistory = new UserPointHistory(postsDto.getUserId(), Const.USER_POINT_HISTORY_TYPE_POSTS_REWARD, -postsDto.getAmount(), postsDto.getId(), dd);
                userPointHistoryDao.create(userPointHistory);
                createReward(postsDto);
                break;
            default:
                throw new ServerException(HttpStatus.BAD_REQUEST, "悬赏类型错误");
        }
    }

    private void createReward(PostsDto postsDto) {
        PostsReward reward = new PostsReward(postsDto.getId(), postsDto.getType(), postsDto.getAmount(), Const.POSTS_REWARD_STATE_UNPAID);
        Date now = new Date(Calendar.getInstance(Locale.CHINA).getTimeInMillis() + 14 * 24 * 60 * 60 * 1000);
        reward.setFinishTime(now);
        postsRewardDao.create(reward);
    }

    //选择最佳答案
    @Transactional
    public void choseBestComment(Long postsId, Long commentId, Byte operatorType) {
        Posts posts = postsDao.find(postsId);
        PostsCommentDto commentDto = findCommentById(commentId);
        if (posts == null || commentDto == null) throw new ServerException(HttpStatus.BAD_REQUEST);
        Long commentUserId = commentDto.getUserId();
        if (posts.getUserId().equals(commentUserId))
            throw new ServerException(HttpStatus.BAD_REQUEST, "不能选择提问者答案为最佳");

        PostsReward reward = posts.getPostsReward();
        reward.setCommentId(commentId);
        reward.setFinishTime(Calendar.getInstance(Locale.CHINA).getTime());
        if (operatorType.equals(Const.DOC_UPLOADERTYPE_USER)) {
            reward.setState(Const.POSTS_REWARD_STATE_PAID);
        } else if (operatorType.equals(Const.DOC_UPLOADERTYPE_ADMIN)) {
            reward.setState(Const.POSTS_REWARD_STATE_ADMCHOSE);
        } else {
            throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        postsRewardDao.update(reward);

        String dd = "对问题【" + posts.getTitle() + "】的回答被采纳,获得悬赏";
        switch (reward.getType()) {
            case Const.POSTS_REWARD_TYPE_SCORE:
                userScoreDao.updateScore(commentUserId, reward.getAmount());
                UserScoreHistory userScoreHistory = new UserScoreHistory(commentUserId, Const.USER_SCORE_HISTORY_TYPE_COMMENT_ACCEPTED, reward.getAmount(), commentDto.getId(), dd);
                userScoreHistoryDao.create(userScoreHistory);
                // 消息
                userMessageDao.create(new UserMessage(commentUserId, UserMessage.TYPE_SCORE_ADD, commentId, "回答被采纳", dd));
                // push消息
                ThirdAccount thirdAccount = userService.getThirdAccountByUserid(commentUserId, Const.THIRD_ACCOUNT_TYPE_WEIXIN);
                if (thirdAccount != null)
                    wxTempMsgService.sendScoreChgWxMsg(thirdAccount, dd, reward.getAmount(), userScoreDao.find(commentUserId).getTotalScore());
                break;
            case Const.POSTS_REWARD_TYPE_POINT:
                userPointDao.updatePoint(commentUserId, reward.getAmount());
                UserPointHistory userPointHistory = new UserPointHistory(commentUserId, Const.USER_POINT_HISTORY_TYPE_COMMENT_ACCEPTED, reward.getAmount(), commentDto.getId(), dd);
                userPointHistoryDao.create(userPointHistory);
                // 消息
                userMessageDao.create(new UserMessage(commentUserId, UserMessage.TYPE_SCORE_ADD, commentId, "回答被采纳", dd));
                break;
            default:
                break;
        }
    }

    //返还悬赏
    @Transactional
    public void returnPostsReward(Long postsId) {
        Posts posts = postsDao.find(postsId);
        if (posts == null || posts.getPostsReward() == null) throw new ServerException(HttpStatus.BAD_REQUEST);
        String dd = "因问题【" + posts.getTitle() + "】无人获得悬赏,悬赏返还";
        PostsReward reward = posts.getPostsReward();

        reward.setFinishTime(Calendar.getInstance(Locale.CHINA).getTime());
        reward.setState(Const.POSTS_REWARD_STATE_RETURNED);
        postsRewardDao.update(reward);

        switch (reward.getType()) {
            case Const.POSTS_REWARD_TYPE_SCORE:
                userScoreDao.updateScore(posts.getUserId(), reward.getAmount());
                UserScoreHistory userScoreHistory = new UserScoreHistory(posts.getUserId(), Const.USER_SCORE_HISTORY_TYPE_REWARD_RETURN, reward.getAmount(), posts.getId(), dd);
                userScoreHistoryDao.create(userScoreHistory);
                break;
            case Const.POSTS_REWARD_TYPE_POINT:
                userPointDao.updatePoint(posts.getUserId(), reward.getAmount());
                UserPointHistory userPointHistory = new UserPointHistory(posts.getUserId(), Const.USER_POINT_HISTORY_TYPE_REWARD_RETURN, reward.getAmount(), posts.getId(), dd);
                userPointHistoryDao.create(userPointHistory);
                break;
            default:
                break;
        }
    }

    // 点赞
    public void doLikeComment(Long userId, Long commentId) {
        PostsCommentLike postsCommentLike = new PostsCommentLike(userId, commentId);
        if (postsCommentLikeDao.isExist(postsCommentLike)) throw new ServerException(HttpStatus.FORBIDDEN, "已点赞过了");
        postsCommentLikeDao.create(postsCommentLike);
    }

    // 置顶
    public void doPostsStick(Long postsId) {
        Posts p = postsDao.find(postsId);
        if (p == null) throw new ServerException(HttpStatus.BAD_REQUEST);
        if (p.getCategoryId() != null) {
            postsDao.zeroPostsByCategoryId(p.getCategoryId());
            postsDao.stickPosts(postsId);
        }
    }

    // 移动
    public void changePostsCategory(Long postsId, Integer categoryId) {
        if (postsDao.find(postsId) == null || postsCategoryDao.getById(categoryId) == null)
            throw new ServerException(HttpStatus.BAD_REQUEST);
        postsDao.changeCategory(postsId, categoryId);
    }

    // 删除
    public void deletePosts(Long id) {
        Posts posts = postsDao.find(id);
        if (posts == null) throw new ServerException(HttpStatus.NOT_FOUND);
        postsDao.updateState(id,Const.POSTS_STATE_DELETE);
    }

}
