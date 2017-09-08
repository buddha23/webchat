package com.lld360.cnc.admin.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.ThirdAccount;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserInvite;
import com.lld360.cnc.model.UserMember;
import com.lld360.cnc.model.UserPointHistory;
import com.lld360.cnc.model.UserScoreHistory;
import com.lld360.cnc.repository.UserMemberDao;
import com.lld360.cnc.service.ModeratorService;
import com.lld360.cnc.service.UserInviteService;
import com.lld360.cnc.service.UserMemberService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.VodLecturerService;
import com.lld360.cnc.service.WxTempMsgService;

@RestController
@RequestMapping(value = "admin/user")
public class AdmUserController extends AdmController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    @Autowired
    private UserMemberDao userMemberDao;

    @Autowired
    private ModeratorService moderatorService;

    @Autowired
    private VodLecturerService vodLecturerService;

    //列表-分页
    @RequiresPermissions("user:r")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<UserDto> usersGet(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortType) {
        Map<String, Object> params = getParamsPageMap(15);
        boolean sort = sortBy != null && sortType != null && sortBy.matches("total_score") && sortType.matches("asc|desc");
        if (!sort) {
            params.remove("sortBy");
            params.remove("sortType");
            params.put("sortBy", "create_time");
            params.put("sortType", "desc");
        }
        return userService.getUsersByPage(params);
    }

    @RequiresPermissions("user:r")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public UserDto userGet(@PathVariable long id) {
        UserDto userDto = userService.getUserDto(id);
        UserInvite userInvite = userInviteService.findInviteByUserId(id);
        if (userInvite != null && userInvite.getState().equals(Const.PARTER_STATE_NORMAL))
            userDto.setUserInvite(userInvite);
        userDto.setIsModerator(moderatorService.isModerator(id));
        userDto.setIsMember(userMemberService.isMember(id));
        userDto.setIsLecturer(vodLecturerService.checkIsLecturer(id));
        return userDto;
    }

    // 用户牛人币列表
    @RequiresPermissions("user:r")
    @RequestMapping(value = "{userId}/scores", method = RequestMethod.GET)
    public Page<UserScoreHistory> userScoreHistoriesGet(@PathVariable long userId) {
        Map<String, Object> params = getParamsPageMap(20);
        params.put("userId", userId);
        return userService.getUserScoreHistoriesByPage(params);
    }

    // 用户积分列表
    @RequiresPermissions("user:r")
    @RequestMapping(value = "point/{userId}", method = RequestMethod.GET)
    public Page<UserPointHistory> userPointHistoriesGet(@PathVariable long userId) {
        Map<String, Object> params = getParamsPageMap(20);
        if (userId != 0)
            params.put("userId", userId);
        return userService.getUserPointHistoriesByPage(params);
    }

    @OperateRecord("添加用户积分")
    @RequiresPermissions("user:w")
    @RequestMapping(value = "{userId}/{score}", method = RequestMethod.PUT)
    public ResultOut addScore(@PathVariable("userId") long userId, @PathVariable("score") int score) {
        User user = userService.getUser(userId);
        if (null == user) {
            return getResultOut(M.E10011.getCode());
        }
        userService.adminAddScore(user, score);
        return getResultOut(M.I10200.getCode());
    }

    // 删除用户
    @OperateRecord("删除用户")
    @RequiresPermissions("user:w")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut userDelete(@PathVariable long id) {
        userService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    // 批量删除
    @OperateRecord("批量删除")
    @RequiresPermissions("user:w")
    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
    public ResultOut usersDelete(@RequestBody long[] ids) {
        if (ids.length == 0) throw new ServerException(HttpStatus.BAD_REQUEST, "未进行选择");
        for (long id : ids)
            userService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    // 上传用户头像
    @OperateRecord("上传用户头像")
    @RequiresPermissions("user:w")
    @RequestMapping(value = "{id}/avatar", method = RequestMethod.POST)
    public User userAvatarPost(@PathVariable long id, MultipartFile file) {
    	checkFileType(file, Const.COURSE_NOT_GEN_PIC_TYPES);
        User user = userService.getUser(id);
        if (user != null) {
            return userService.setAvatar(user, file);
        }
        throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
    }

    //修改
    @OperateRecord("修改用户")
    @RequiresPermissions("user:w")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public User userPut(@PathVariable long id, @Valid @RequestBody UserDto user) {
        user.setId(id);
        return userService.edit(user);
    }

    //标记区域合伙人
    @OperateRecord("标记区域合伙人")
    @RequiresPermissions("parter:w")
    @RequestMapping(value = "parter/{id}", method = RequestMethod.PUT)
    public ResponseEntity setParter(@PathVariable long id) {
        User user = userService.getUser(id);
        if (user != null) userInviteService.setParter(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @OperateRecord("标记课程讲师")
    @RequiresPermissions("user:w")
    @RequestMapping(value = "lecturer/{id}", method = RequestMethod.PUT)
    public ResponseEntity setLecturer(@PathVariable long id) {
        User user = userService.getUser(id);
        if (user != null) vodLecturerService.createLecturer(id, getCurrentAdmin());
        return new ResponseEntity(HttpStatus.OK);
    }

    @OperateRecord("取消课程讲师")
    @RequiresPermissions("user:w")
    @RequestMapping(value = "lecturer/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteLecturer(@PathVariable long id) {
        vodLecturerService.deleteLecturer(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //列表-分页
    @RequiresPermissions("parter:r")
    @RequestMapping(value = "parter", method = RequestMethod.GET)
    public Page<UserDto> userParter(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortType) {
        Map<String, Object> params = getParamsPageMap(15);
        boolean sort = sortBy != null && sortType != null && sortBy.matches("total_score") && sortType.matches("asc|desc");
        if (!sort) {
            params.remove("sortBy");
            params.remove("sortType");
        }
        return userInviteService.parterPage(params);
    }

    // 删除用户
    @OperateRecord("删除合伙人")
    @RequiresPermissions("parter:w")
    @RequestMapping(value = "parter/{id}", method = RequestMethod.DELETE)
    public ResultOut parterDelete(@PathVariable long id) {
        userInviteService.deleteParter(id);
        return getResultOut(M.I10200.getCode());
    }

    //列表-分页
    @RequiresPermissions("user:r")
    @RequestMapping(value = "members", method = RequestMethod.GET)
    public Page<UserDto> membersGet(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortType) {
        Map<String, Object> params = getParamsPageMap(15);
        boolean sort = sortBy != null && sortType != null && sortBy.matches("total_score") && sortType.matches("asc|desc");
        if (!sort) {
            params.remove("sortBy");
            params.remove("sortType");
            params.put("sortBy", "update_time");
            params.put("sortType", "desc");
        }
        return userMemberService.getMembersByPage(params);
    }

    @RequiresPermissions("user:w")
    @RequestMapping(value = "memberEndMsg/{userId}", method = RequestMethod.GET)
    public ResponseEntity memberEndMsg(@PathVariable Long userId) {
        // push消息
        UserMember member = userMemberDao.get(userId, (byte) 1);
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(userId, Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "用户未关注服务号");
        } else {
            wxTempMsgService.sendMemberEndWxMsg(thirdAccount, member);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
