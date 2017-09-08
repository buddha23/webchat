package com.lld360.cnc.website.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gonvan.kaptcha.Constants;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.PostsDto;
import com.lld360.cnc.dto.PostsInfos;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.Posts;
import com.lld360.cnc.model.PostsComment;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.PostsCategoryService;
import com.lld360.cnc.service.PostsService;
import com.lld360.cnc.service.UserClickHabitService;
import com.lld360.cnc.website.SiteController;

@Controller
@RequestMapping("post")
public class PostsController extends SiteController {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsCategoryService postsCategoryService;

    @Autowired
    private UserClickHabitService userClickHabitService;

    //帖子列表
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String postList(Model model) {
        Map<String, Object> params = getParamsPageMap(10);
        params.put("state", Const.POSTS_STATE_NORMAL);
        Page<PostsInfos> postsInfos = postsService.getPostsByPage(params);
        model.addAttribute("postsInfos", postsInfos);
        return "wPosts/posts";
    }

    //@我的提问
    @RequestMapping(value = "mypost", method = RequestMethod.GET)
    public String myPostList(Model model) {
        Map<String, Object> params = getParamsPageMap(10);
        UserDto user = getRequiredCurrentUser();

        params.put("userId", user.getId());
        params.put("state", Const.POSTS_STATE_NORMAL);
        Page<PostsInfos> postsInfos = postsService.getPostsByPage(params);
        model.addAttribute("postsInfos", postsInfos);
        return "wPosts/my-posts";
    }

    @ResponseBody
    @RequestMapping(value = "categories", method = RequestMethod.GET)
    public List categories() {
        return postsCategoryService.getAllCategories();
    }

    //详情
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String postDetail(Model model, @PathVariable long id) {
        Map<String, Object> params = getParamsPageMap(10);
        params.put("postsId", id);
        params.put("state", Const.POSTSCOMMENT_STATE_NORMAL);
        PostsInfos postsInfos = postsService.getPostsInfos(id);
        if (postsInfos == null) throw new ServerException(HttpStatus.NOT_FOUND);
        if (postsInfos.getPostsReward() != null && postsInfos.getPostsReward().getCommentId() != null)
            model.addAttribute("bestComment", postsService.findCommentById(postsInfos.getPostsReward().getCommentId()));
        model.addAttribute("postsInfos", postsInfos);
        userClickHabitService.createHabit(request);
        if (getCurrentUser() != null) params.put("currentUserId", getCurrentUser().getId());
        model.addAttribute("comments", postsService.getCommentsByPage(params));
        return "wPosts/posts-detail";
    }

    // 上传帖子内容里的图片
    @ResponseBody
    @RequestMapping(value = "upload_image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> uploadImagePost(MultipartFile file) {
    	checkFileType(file, Const.COURSE_NOT_GEN_PIC_TYPES);
        if (file == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("文件不存在");
        }
        String url = postsService.savePostContentImage(file);
        Map<String, String> result = new HashMap<>();
        result.put("link", request.getContextPath() + '/' + Const.FILE_PREFIX_PATH + url);
        return result;
    }

    // 检查帖子和评论验证码
    private void checkRequiredCaptcha(String captcha) {
        if (!ClientUtils.isMobileBrowser(request)) {
            String scaptcha = getSessionStringValue(Constants.KAPTCHA_SESSION_KEY);
            if (scaptcha == null) {
                throw new ServerException(HttpStatus.BAD_REQUEST, M.E10009);
            }
            if (!scaptcha.equalsIgnoreCase(captcha)) {
                throw new ServerException(HttpStatus.BAD_REQUEST, M.E10010);
            }
        }
    }

    // 发布帖子
    @ResponseBody
    @RequestMapping(value = "publishpost", method = RequestMethod.POST)
    public Posts pubPost(@Valid PostsDto postDto, @RequestParam(required = false) String captcha, BindingResult result) {
        checkRequiredCaptcha(captcha);
        if (result.hasErrors())
            throw new ServerException(HttpStatus.BAD_REQUEST, result.getFieldErrors().get(0).getDefaultMessage());
        if (postDto.getType() == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "悬赏类型错误");
        } else if (postDto.getType() != 0) {
            if (postDto.getAmount() == null || postDto.getAmount() < 0)
                throw new ServerException(HttpStatus.BAD_REQUEST, "悬赏金额必须大于0");
        }
        UserDto user = getRequiredCurrentUser();
        postDto.setUserId(user.getId());
        postsService.createPosts(postDto);
        return postDto;
    }

    // 评论帖子
    @RequestMapping(value = "addComment", method = RequestMethod.POST)
    public ResponseEntity addComment(@RequestParam String reply, @RequestParam(required = false) String captcha, Long postsId) {
        PostsComment postscomment = new PostsComment();
        UserDto user = getRequiredCurrentUser();
        checkRequiredCaptcha(captcha);
        if (StringUtils.isEmpty(reply) || reply.length() > 999) {
            return new ResponseEntity<>("回复内容错误", HttpStatus.BAD_REQUEST);
        }
        postscomment.setContent(reply);
        postscomment.setPostsId(postsId);
        postscomment.setUserId(user.getId());
        postsService.createComment(postscomment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 选择答案
    @RequestMapping(value = "choseBestComment", method = RequestMethod.POST)
    public ResponseEntity choseBestComment(@RequestParam Long postsId, @RequestParam Long commentId) {
        postsService.choseBestComment(postsId, commentId, Const.DOC_UPLOADERTYPE_USER);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 点赞
    @ResponseBody
    @RequestMapping(value = "doLike", method = RequestMethod.POST)
    public ResponseEntity doLikeComment(@RequestParam Long commentId) {
        User user = getRequiredCurrentUser();
        postsService.doLikeComment(user.getId(), commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
