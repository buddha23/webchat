package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.ModeratorDto;
import com.lld360.cnc.dto.ModeratorLogDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.DocCategoryDao;
import com.lld360.cnc.repository.DocModeratorDao;
import com.lld360.cnc.service.DocCategoryService;
import com.lld360.cnc.service.ModeratorService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.WxTempMsgService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("admin/docCategory")
public class AdmDocCategoryContronller extends AdmController {

    @Autowired
    private DocCategoryService docCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModeratorService moderatorService;

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private DocModeratorDao docModeratorDao;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    @RequiresPermissions("doc:r")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DocCategory> docTagPageGet() {
        return docCategoryService.getAllCategory();
    }

    @RequiresPermissions("doc:w")
    @OperateRecord("保存文档分类")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public DocCategory docCategoryPost(@Valid DocCategory category, @RequestParam(required = false) MultipartFile file) {
        if (null != file) checkFileType(file, Const.COURSE_NOT_GEN_PIC_TYPES);
        docCategoryService.save(category, file);
        return docCategoryService.get(category.getId());
    }

    @OperateRecord("删除文档分类")
    @RequiresPermissions("doc:w")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut docCategoryDelete(@PathVariable int id) {
        docCategoryService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    // 根据类型ID获取版主列表
    @RequestMapping(value = "{id}/moderators", method = RequestMethod.GET)
    public List<ModeratorDto> moderatorDtosGet(@PathVariable int id) {
        return docCategoryService.getModeratorsByCategory(id);
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
        ModeratorDto moderatorDto = docModeratorDao.findModerator(id, user.getId());
        if (moderatorDto != null && moderatorDto.getState().equals(Const.MODERATOR_STATE_NORMAL)) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "该用户已经是版主");
        } else if (moderatorDto != null && moderatorDto.getState().equals(Const.MODERATOR_STATE_APPLY)) {
            moderatorDto.setState(Const.MODERATOR_STATE_NORMAL);
            moderatorDto.setAuthorizerId(getRequiredCurrentAdmin().getId());
            docCategoryService.updateModerators(moderatorDto);
            return moderatorDto;
        }
        Moderator moderator = new Moderator(id, user.getId(), getRequiredCurrentAdmin().getId(), Const.MODERATOR_STATE_NORMAL);
        return docCategoryService.addModerator(moderator);
    }

    // 删除版主
    @OperateRecord("删除版主")
    @RequiresPermissions("moderator")
    @RequestMapping(value = "{id}/moderators/{userId}", method = RequestMethod.DELETE)
    public ResultOut moderatorDelete(@PathVariable int id, @PathVariable long userId) {
        docCategoryService.deleteModerator(id, userId);
        // push消息
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(userId, Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount != null) wxTempMsgService.sendModeratorWxMsg(thirdAccount, "版主申请审核不通过");
        return getResultOut(M.I10200.getCode());
    }

    // 获取当前版主列表
    @RequestMapping(value = "moderatorsList", method = RequestMethod.GET)
    public Page<ModeratorDto> moderatorsList() {
        Map<String, Object> param = getParamsPageMap();
        param.put("state", Const.MODERATOR_STATE_NORMAL);
        return docCategoryService.moderatorsPage(param);
    }

    //获取版主申请列表
    @RequestMapping(value = "moderatorsApplyList", method = RequestMethod.GET)
    public Page<ModeratorDto> moderatorsApplyList() {
        Map<String, Object> param = getParamsPageMap();
        param.put("state", Const.MODERATOR_STATE_APPLY);
        return docCategoryService.moderatorsPage(param);
    }

    //同意版主申请
    @OperateRecord("同意版主申请")
    @RequiresPermissions("moderator")
    @RequestMapping(value = "moderatorsAgree", method = RequestMethod.POST)
    public ResponseEntity moderatorsAgree(@RequestBody ModeratorDto moderatorDto) {
        moderatorDto.setState(Const.MODERATOR_STATE_NORMAL);
        moderatorDto.setCreateTime(new Timestamp(Calendar.getInstance(Locale.CHINA).getTimeInMillis()));
        moderatorDto.setAuthorizerId(getRequiredCurrentAdmin().getId());
        docCategoryService.updateModerators(moderatorDto);
        // push消息
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(moderatorDto.getUserId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount != null) wxTempMsgService.sendModeratorWxMsg(thirdAccount, "版主申请通过");
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    //批量删除版主申请记录
    @OperateRecord("批量删除版主申请记录")
    @RequiresPermissions("moderator")
    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
    public ResultOut deleteSome(@RequestBody ModeratorDto[] modS) {
        if (modS.length == 0) throw new ServerException(HttpStatus.BAD_REQUEST, "未进行选择");
        for (ModeratorDto moderatorDto : modS) {
            docCategoryService.deleteModerator(moderatorDto.getCategoryId(), moderatorDto.getUserId());
            // push消息
            ThirdAccount thirdAccount = userService.getThirdAccountByUserid(moderatorDto.getUserId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
            if (thirdAccount != null) wxTempMsgService.sendModeratorWxMsg(thirdAccount, "版主申请审核不通过");
        }
        return getResultOut(M.I10200.getCode());
    }

    //版主操作日志
    @RequiresPermissions("moderator")
    @RequestMapping(value = "moderatorsLog", method = RequestMethod.GET)
    public Page<ModeratorLogDto> moderatorsLog(@RequestParam(required = false) Integer userId) {
        Map<String, Object> params = getParamsPageMap();
        if (userId != null && userId > 0) params.put("moderatorId", userId);
        return moderatorService.getLogsByPage(params);
    }

    //删除版主操作历史
    @OperateRecord("删除版主操作历史")
    @RequiresPermissions("moderator")
    @RequestMapping(value = "moderatorsLogDelete/{id}", method = RequestMethod.DELETE)
    public ResultOut moderatorLogDelete(@PathVariable long id) {
        moderatorService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    @OperateRecord("批量删除版主操作历史")
    @RequiresPermissions("moderator")
    @RequestMapping(value = "moderatorsLogsDelete", method = RequestMethod.POST)
    public ResultOut deleteSome(@RequestBody long[] ids) {
        for (long id : ids)
            moderatorService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    // 版主贡献值历史
    @RequiresPermissions("moderator")
    @RequestMapping(value = "moderatorsScoreHistory", method = RequestMethod.GET)
    public Page<ModeratorScoreHistory> moderatorsScoreHistory() {
        Map<String, Object> params = getParamsPageMap();
        return moderatorService.getModeratorScoreHistory(params);
    }

}
