package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.PostsCategoryDto;
import com.lld360.cnc.dto.PostsCommentDto;
import com.lld360.cnc.dto.PostsDto;
import com.lld360.cnc.dto.PostsInfos;
import com.lld360.cnc.model.PostsCategory;
import com.lld360.cnc.model.User;
import com.lld360.cnc.repository.PostsCommentDao;
import com.lld360.cnc.service.ImgFilesService;
import com.lld360.cnc.service.PostsCategoryService;
import com.lld360.cnc.service.PostsService;
import com.lld360.cnc.service.UserClickHabitService;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.service.ThirdAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("m/posts")
public class MPostsController extends SiteController {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsCommentDao postsCommentDao;

    @Autowired
    private UserClickHabitService userClickHabitService;

    @Autowired
    private ThirdAccountService thirdAccountService;

    @Autowired
    private PostsCategoryService postsCategoryService;

    @Autowired
    private ImgFilesService imgFilesService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String postsGet() {
        return "redirect:/m/posts/category";
    }

    @RequestMapping(value = "category", method = RequestMethod.GET)
    public String postCategory(Model model) {
        List<PostsCategoryDto> categories = postsCategoryService.getAllCategories4Wap();
        model.addAttribute("imgs", imgFilesService.findbytype(Const.FILE_TYPE_ADVERTISE_IMG));
        model.addAttribute("categories", categories);
        return "m/posts-category";
    }

    @RequestMapping(value = "list/{categoryId}", method = RequestMethod.GET)
    public String postList(Model model, @PathVariable Integer categoryId) {
        Map<String, Object> params = getParamsPageMap(6);
        List<PostsCategoryDto> categories = postsCategoryService.getAllCategories4Wap();
        model.addAttribute("categories", categories);
        params.put("state", Const.POSTS_STATE_NORMAL);
        params.put("categoryId", categoryId);
        params.put("orderBy", "sorting");
        Page<PostsInfos> postsInfos = postsService.getPostsByPage(params);
        model.addAttribute("category", postsCategoryService.getDtoById(categoryId));
        model.addAttribute("postsInfos", postsInfos);
        model.addAttribute("configer", configer);
        userClickHabitService.createHabit(request);
        return "m/posts-list";
    }

    @RequestMapping(value = "myposts", method = RequestMethod.GET)
    public String myPostList(Model model) {
        Map<String, Object> params = getParamsPageMap(6);
        User user = getRequiredCurrentUser();
        params.put("userId", user.getId());
        params.put("state", Const.POSTS_STATE_NORMAL);
        Page<PostsInfos> postsInfos = postsService.getPostsByPage(params);
        model.addAttribute("postsInfos", postsInfos);
        model.addAttribute("configer", configer);
        userClickHabitService.createHabit(request);
        return "m/posts-mylist";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String postDetail(Model model, @PathVariable long id, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        String url = dealCodeState(code, state, "web");
        if (url != null) return url;
        Map<String, Object> params = getParamsPageMap(6);
        params.put("postsId", id);
        params.put("state", Const.POSTSCOMMENT_STATE_NORMAL);
        PostsInfos postsInfos = postsService.getPostsInfos(id);
        if (postsInfos == null) throw new ServerException(HttpStatus.NOT_FOUND);
        if (postsInfos.getPostsReward() != null && postsInfos.getPostsReward().getCommentId() != null)
            model.addAttribute("bestComment", postsService.findCommentById(postsInfos.getPostsReward().getCommentId()));
        if (postsInfos.getCategoryId() != null)
            model.addAttribute("category", postsCategoryService.get(postsInfos.getCategoryId()));
        model.addAttribute("postsInfos", postsInfos);
        model.addAttribute("configer", configer);
        if (getCurrentUser() != null) params.put("currentUserId", getCurrentUser().getId());
        model.addAttribute("comments", postsService.getCommentsByPage(params));
        userClickHabitService.createHabit(request);
        return "m/posts-detail";
    }

    @RequestMapping(value = "getMoreComments", method = RequestMethod.GET)
    public ResponseEntity getMoreComments() {
        Map<String, Object> params = getParamsPageMap(6);
        List<PostsCommentDto> comments = postsService.getCommentsByPage(params).getContent();
        if (!comments.isEmpty()) {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "manage", method = RequestMethod.GET)
    public String postsManage(Model model) {
        User user = getRequiredCurrentUser();
        List<PostsCategory> postsCategories = postsCategoryService.getCategoriesByUser(user.getId());
        model.addAttribute("categories", postsCategories);
        return "m/posts-manage";
    }

    @RequestMapping(value = "doManage/{categoryId}", method = RequestMethod.GET)
    public String doPostsManage(@PathVariable Integer categoryId, Model model) {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("orderBy", "sorting");
        Page<PostsInfos> postsDtos = postsService.getPostsByPage(params);
        model.addAttribute("postsDtos", postsDtos);
        model.addAttribute("categories", postsCategoryService.getAllCategories());
        return "m/posts-manage-list";
    }

    @RequestMapping(value = "stick/{postsId}", method = RequestMethod.GET)
    public ResponseEntity postsStick(@PathVariable Long postsId) {
        postsService.doPostsStick(postsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "changeCategory", method = RequestMethod.POST)
    public ResponseEntity changeCategory(@RequestParam Integer categoryId, @RequestParam Long postsId) {
        postsService.changePostsCategory(postsId, categoryId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletePosts(@PathVariable Long id) {
        postsService.deletePosts(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
