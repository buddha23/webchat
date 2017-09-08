package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.PostsCommentDto;
import com.lld360.cnc.dto.PostsInfos;
import com.lld360.cnc.model.Posts;
import com.lld360.cnc.model.PostsCategory;
import com.lld360.cnc.repository.PostsDao;
import com.lld360.cnc.service.PostsCategoryService;
import com.lld360.cnc.service.PostsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 系统帖子投稿管理控制器
 */
@RestController
@RequestMapping("admin/posts")
public class AdmPostsController extends AdmController {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsCategoryService postsCategoryService;

    // 获取帖子列表
    @RequiresPermissions("posts:r")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<PostsInfos> postsInfosPageGet() {
        Map<String, Object> params = getParamsPageMap(20);
        params.put("state", Const.POSTS_STATE_NORMAL);
        return postsService.getPostsByPage(params);
    }

    // 获取帖子信息
    @RequiresPermissions("posts:r")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public PostsInfos postsInfoGet(@PathVariable long id) {
        PostsInfos info = postsService.getPostsInfos(id);
        if (info == null) {
            throw new ServerException(HttpStatus.NOT_FOUND);
        }
        return info;
    }

    // 删除帖子信息
    @OperateRecord("删除帖子信息")
    @RequiresPermissions("posts:w")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut postsInfoDelete(@PathVariable long id) {
        postsService.changePostsState(id, Const.POSTS_STATE_DELETE);
        return getResultOut(M.I10200.getCode());
    }

    // 删除帖子信息
    @OperateRecord("批量删除帖子信息")
    @RequiresPermissions("posts:w")
    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
    public ResultOut postsInfosDelete(@RequestBody long[] ids) {
        postsService.deletePosts(ids);
        return getResultOut(M.I10200.getCode());
    }

    // 获取帖子评论信息
    @RequestMapping(value = "{postsId}/comments", method = RequestMethod.GET)
    public Page<PostsCommentDto> postsCommentPageGet(@PathVariable long postsId) {
        Map<String, Object> params = getParamsPageMap(20);
        params.put("postsId", postsId);
        params.put("state", Const.POSTS_STATE_NORMAL);
        return postsService.getCommentsByPage(params);
    }

    // 获取某条评论信息
    @RequestMapping(value = "/comment/{commentId}", method = RequestMethod.GET)
    public PostsCommentDto postsCommentGet(@PathVariable long commentId) {
        return postsService.findCommentById(commentId);
    }

    // 删除帖子评论信息
    @RequiresPermissions("posts:w")
    @OperateRecord("删除帖子评论信息")
    @RequestMapping(value = "comments/{id}", method = RequestMethod.DELETE)
    public ResultOut postCommentDelete(@PathVariable long id) {
        postsService.deleteComment(id);
        return getResultOut(M.I10200.getCode());
    }

    // 选择答案
    @OperateRecord("选择答案")
    @RequiresPermissions("posts:w")
    @RequestMapping(value = "choseBestComment", method = RequestMethod.POST)
    public ResultOut choseBestComment(@RequestParam Long postsId, @RequestParam Long commentId) {
        postsService.choseBestComment(postsId, commentId, Const.DOC_UPLOADERTYPE_ADMIN);
        return getResultOut(M.I10200.getCode());
    }

    // 返回帖子悬赏
    @RequestMapping(value = "returnReward/{postsId}", method = RequestMethod.GET)
    public ResultOut returnReward(@PathVariable Long postsId) {
        postsService.returnPostsReward(postsId);
        return getResultOut(M.I10200.getCode());
    }



    @OperateRecord("修改论坛分类")
    @RequiresPermissions("posts:w")
    @RequestMapping(value = "saveCategory", method = RequestMethod.POST)
    public PostsCategory PostsCategoryPost(@Valid PostsCategory category, @RequestParam(required = false) MultipartFile file) {
    	if (null != file){
    		checkFileType(file, Const.COURSE_NOT_GEN_PIC_TYPES);
    	}
        return postsCategoryService.save(category, file);
    }

}
