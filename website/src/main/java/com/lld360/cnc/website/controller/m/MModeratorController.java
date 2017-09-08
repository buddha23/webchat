package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.Moderator;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.DocCategoryService;
import com.lld360.cnc.service.DocService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("m/moderator")
public class MModeratorController extends SiteController {

    @Autowired
    private DocCategoryService docCategoryService;

    @Autowired
    private DocService docService;

    @Autowired
    private UserService userService;

    // 获取用户管理的所有版块
    @RequestMapping(value = "apply", method = RequestMethod.GET)
    public String moderatorsGet(Model model) {
        User user = getRequiredCurrentUser();
        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getQq()) || StringUtils.isEmpty(user.getMailAddress())) {
            model.addAttribute("needUserInfo", true);
        } else {
            model.addAttribute("needUserInfo", false);
        }
        model.addAttribute("user", user);
        model.addAttribute("maxNum", Const.MODERATOR_MAX_APPLY_NUM);
        model.addAttribute("moderatorCategories", docCategoryService.getModeratorsByModerator(user.getId()));
        model.addAttribute("notModeratorCategories", docCategoryService.getModeratorsByNotModerator(user.getId()));
        return "m/moderator-apply";
    }

    //用户申请版主
    @Transactional
    @RequestMapping(value = "moderatorApply", method = RequestMethod.POST)
    public String moderatorApplyPost(@RequestParam Integer categoryId, @RequestParam String remarks,
                                     @RequestParam(required = false) String userName,
                                     @RequestParam(required = false) String userQq,
                                     @RequestParam(required = false) String userEmail) {
        if (categoryId == null) throw new ServerException(HttpStatus.BAD_REQUEST, "没有选择任何板块");

        UserDto user = getRequiredCurrentUser();
        if (StringUtils.isNotEmpty(userName)) user.setName(userName);
        if (StringUtils.isNotEmpty(userQq)) user.setQq(userQq);
        if (StringUtils.isNotEmpty(userQq)) user.setMailAddress(userEmail);
        userService.updateUser(user);
        request.getSession().setAttribute(Const.SS_USER, user);

        Moderator moderator = new Moderator(categoryId, user.getId(), null, Const.MODERATOR_STATE_APPLY);
        moderator.setRemarks(remarks);
        docCategoryService.addModerator(moderator);
        return "redirect:/m/moderator/apply";
    }

    // 获取用户管理版块的文档列表
    @RequestMapping(value = "manage/{c1}/{c2}", method = RequestMethod.GET)
    public String moderatorDocsGet(@PathVariable int c1, @PathVariable int c2, Model model) {
        User user = getRequiredCurrentUser();
        if (!docCategoryService.isModerator(c1, user.getId())) {
            model.addAttribute("error", "你不是当前版块的版主");
        } else {
            Map<String, Object> params = getParamsPageMap(20);
            // 只查询审核中的
            params.put("state", Const.DOC_STATE_REVIEWING);
            params.put("c1", c1);
            if (c2 != 0) {
                DocCategory category = docCategoryService.get(c2);
                if (c1 == category.getFid()) {
                    params.put("c2", c2);
                    model.addAttribute("category2", category);
                }
            }
            params.put("sortByCheck", Const.DOC_STATE_REVIEWING);
            model.addAttribute("docs", docService.searchWeb(params));
            model.addAttribute("category", docCategoryService.get(c1));
            model.addAttribute("category2s", docCategoryService.getByFid(c1));
        }
        return "m/moderator-docs";
    }
}
