package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.ModeratorDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.PostsDao;
import com.lld360.cnc.service.PostsCategoryService;
import com.lld360.cnc.service.PostsService;
import com.lld360.cnc.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin/postsCategory")
public class AdmPostsCategoryController  extends AdmController{

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsCategoryService postsCategoryService;

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private UserService userService;

    // 分类
    @RequestMapping(value = "categories", method = RequestMethod.GET)
    public List<PostsCategory> postsCategories() {
        return postsCategoryService.getAllCategories();
    }

    @OperateRecord("修改论坛分类")
    @RequiresPermissions("posts:w")
    @RequestMapping(value = "saveCategory", method = RequestMethod.POST)
    public PostsCategory PostsCategoryPost(@Valid PostsCategory category, @RequestParam(required = false) MultipartFile file) {
        if (null != file) checkFileType(file, Const.COURSE_NOT_GEN_PIC_TYPES);
        return postsCategoryService.save(category, file);
    }

    @OperateRecord("删除论坛分类")
    @RequiresPermissions("posts:w")
    @RequestMapping(value = "delCategory/{id}", method = RequestMethod.DELETE)
    public ResultOut docCategoryDelete(@PathVariable int id) {
        postsCategoryService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    @RequestMapping(value = "changeCategory/{id}", method = RequestMethod.POST)
    public ResultOut changePostsCategory(@PathVariable int id, @RequestBody Integer categoryId) {
        PostsCategory postsCategory = postsCategoryService.get(categoryId);
        if (postsCategory == null) throw new ServerException(HttpStatus.BAD_REQUEST);
        Posts posts = postsDao.findPostsInfo(id);
        posts.setCategoryId(categoryId);
        postsService.updatePosts(posts);
        return getResultOut(M.I10200.getCode());
    }

    // 根据类型ID获取版主列表
    @RequestMapping(value = "{id}/moderators", method = RequestMethod.GET)
    public List<ModeratorDto> moderatorDtosGet(@PathVariable int id) {
        return postsCategoryService.getModeratorsByCategory(id);
    }

    // 添加版主
    @OperateRecord("添加版主")
    @RequiresPermissions("moderator")
    @RequestMapping(value = "{id}/moderators", method = RequestMethod.POST)
    public ModeratorDto moderatorPost(@PathVariable int id, @RequestParam String mobile) {
        User user = userService.getByMobile(mobile);
        if (user == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404).setMessage("指定的用户不存在");
        }
        ModeratorDto moderatorDto = postsCategoryService.findModerator(id, user.getId());
        if (moderatorDto != null && moderatorDto.getState().equals(Const.MODERATOR_STATE_NORMAL)) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "该用户已经是版主");
        } else if (moderatorDto != null && moderatorDto.getState().equals(Const.MODERATOR_STATE_APPLY)) {
            moderatorDto.setState(Const.MODERATOR_STATE_NORMAL);
            moderatorDto.setAuthorizerId(getRequiredCurrentAdmin().getId());
            postsCategoryService.updateModerator(moderatorDto);
            return moderatorDto;
        }
        Moderator moderator = new Moderator(id, user.getId(), getRequiredCurrentAdmin().getId(), Const.MODERATOR_STATE_NORMAL);
        return postsCategoryService.addModerator(moderator);
    }

    // 删除版主
    @OperateRecord("删除版主")
    @RequiresPermissions("moderator")
    @RequestMapping(value = "{id}/moderators/{userId}", method = RequestMethod.DELETE)
    public ResultOut moderatorDelete(@PathVariable int id, @PathVariable long userId) {
        postsCategoryService.deleteModerator(id, userId);
        return getResultOut(M.I10200.getCode());
    }
}
