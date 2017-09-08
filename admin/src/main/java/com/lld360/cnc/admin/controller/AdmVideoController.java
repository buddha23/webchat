package com.lld360.cnc.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aliyuncs.mts.model.v20140618.SearchMediaWorkflowResponse;
import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.FileSaveInfo;
import com.lld360.cnc.dto.VolumeDto;
import com.lld360.cnc.model.ThirdAccount;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.VodBuys;
import com.lld360.cnc.model.VodTag;
import com.lld360.cnc.model.VodVolumes;
import com.lld360.cnc.service.FileUtilService;
import com.lld360.cnc.service.OssService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.VideoService;
import com.lld360.cnc.service.VodLecturerService;
import com.lld360.cnc.service.WxTempMsgService;

@RestController
@RequestMapping("admin/video")
public class AdmVideoController extends AdmController {
    @Autowired
    private VideoService videoService;

    @Autowired
    private FileUtilService fileUtilService;

    @Autowired
    private OssService ossService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    @Autowired
    private UserService userService;

    @Autowired
    private VodLecturerService vodLecturerService;

    @GetMapping("list")
    @RequiresPermissions("video:r")
    public Page<VolumeDto> volumesPageGet(@RequestParam(required = false) String sortCol, @RequestParam(required = false) String sortBy) {
        Map<String, Object> params = getParamsPageMap(12);
        if (!params.containsKey("state")) {
            params.put("state", Const.VIDEO_STATE_NORMAL);
        }
        if (sortBy == null || sortCol == null || !sortCol.matches("buys|createTime") || !sortBy.matches("asc|desc")) {
            params.remove("sortCol");
            params.remove("sortBy");
        }
        return videoService.getVolumeDtoPage(params);
    }

    @GetMapping("{id}")
    @RequiresPermissions("video:r")
    public VodVolumes volumeGet(@PathVariable int id) {
        return videoService.getVodVolume(id);
    }

    @OperateRecord("上传视频")
    @RequiresPermissions("video:w")
    @PostMapping("")
    public VodVolumes volumePost(@RequestBody @Valid VodVolumes volume, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = bindResult2Map(result);
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(errors);
        }
        if (volume.getId() == 0) {
            volume.setId(null);
        }
        if (volume.getCreater() == null) {
            volume.setCreater(getRequiredCurrentAdmin().getId());
        }
        return videoService.save(volume);
    }

    @OperateRecord("删除视频")
    @RequiresPermissions("video:w")
    @DeleteMapping("{id}")
    public void volumeDelete(@PathVariable int id) {
        VodVolumes volume = videoService.getVodVolumeById(id);
        if (volume != null) {
            videoService.changeState(volume.getId(), Const.DOC_STATE_DELETE);
        }
    }

    @OperateRecord("上传视频文件")
    @PostMapping("cover")
    public FileSaveInfo volumeCoverPost(MultipartFile cover) {
    	checkFileType(cover, Const.COURSE_NOT_GEN_PIC_TYPES);
        if (cover != null) {
            String relativePath = "volume/cover/" + new SimpleDateFormat("yyyy/MM").format(Calendar.getInstance(Locale.CHINA).getTime());
            return fileUtilService.upload(cover, relativePath, RandomStringUtils.randomAlphanumeric(8));
        }
        throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("上传的文件不能为空！");
    }

    @GetMapping("mediaWorkflows")
    public SearchMediaWorkflowResponse mediaWorkflowsGet() {
        return ossService.getMediaWorkflows();
    }

    @RequestMapping(value = "getAllTag", method = RequestMethod.GET)
    public List<VodTag> getAllTag() {
        return videoService.getAllTag();
    }

    @RequestMapping(value = "getVodTag/{id}", method = RequestMethod.GET)
    public List<VodTag> getVodTag(@PathVariable Integer id) {
        return videoService.getVodTag(id);
    }

    @RequestMapping(value = "getAllLecturer", method = RequestMethod.GET)
    public List<User> getAllLecturer() {
        return vodLecturerService.getAllVodLecturer();
    }

    @RequestMapping(value = "addVodTag/{id}", method = RequestMethod.POST)
    public ResponseEntity addVodTag(@PathVariable Integer id, @RequestBody Integer[] tags) {
        videoService.addVodTag(id, tags);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "vodBeginMsg/{id}", method = RequestMethod.GET)
    public ResponseEntity vodBeginMsg(@PathVariable Integer id) {
        VodVolumes vodVolumes = videoService.getVodVolume(id);
        List<ThirdAccount> thirdAccounts = userService.getVodBuyThirdaccount(id);
        if (!thirdAccounts.isEmpty() && vodVolumes != null) {
            for (ThirdAccount thirdAccount : thirdAccounts) {
                wxTempMsgService.sendVodChgWxMsg(thirdAccount, vodVolumes, "开课啦~");
            }
        }
        /*  //测试
        ThirdAccount thirdAccount = new ThirdAccount();
        thirdAccount.setUserId(89L);
        wxTempMsgService.sendVodChgWxMsg(thirdAccount, vodVolumes, "开课提醒");
        wxTempMsgService.sendParterWxMsg(thirdAccount);
        wxTempMsgService.sendPwdWxMsg(thirdAccount);
        wxTempMsgService.sendRegistWxMsg(thirdAccount);
        wxTempMsgService.sendVideoBuyWxMsg(thirdAccount,vodVolumes);
        wxTempMsgService.sendInviteWxMsg(thirdAccount);

        UserMember userMember= new UserMember();
        userMember.setUpdateTime(new Date());
        userMember.setEndTime(new Date());
        wxTempMsgService.sendMemberWxMsg(thirdAccount,userMember);
        */
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "vodUpdateMsg/{id}", method = RequestMethod.GET)
    public ResponseEntity vodUpdateMsg(@PathVariable Integer id) {
        VodVolumes vodVolumes = videoService.getVodVolume(id);
        List<ThirdAccount> thirdAccounts = userService.getVodBuyThirdaccount(id);
        if (!thirdAccounts.isEmpty() && vodVolumes != null) {
            for (ThirdAccount thirdAccount : thirdAccounts) {
                wxTempMsgService.sendVodChgWxMsg(thirdAccount, vodVolumes, "课程更新");
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "volumeBuys",method = RequestMethod.GET)
    public Page<VodBuys> getVodBuys(){
        Map<String, Object> params = getParamsPageMap(12);
        return videoService.getVodBuys(params);
    }
}
