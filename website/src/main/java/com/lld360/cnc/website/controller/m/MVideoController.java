package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.WeiXinPayConfiger;
import com.lld360.cnc.core.utils.AliYunOSSUtil;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.dto.VodVolumesDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.VodChaptersDao;
import com.lld360.cnc.service.InviteCardService;
import com.lld360.cnc.service.UserClickHabitService;
import com.lld360.cnc.service.VideoService;
import com.lld360.cnc.service.VodCategoryService;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.dto.WxAccountAccessToken;
import com.lld360.cnc.website.service.ThirdAccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("m/video")
public class MVideoController extends SiteController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VodCategoryService vodCategoryService;

    @Autowired
    private VodChaptersDao chaptersDao;

    @Autowired
    private AliYunOSSUtil aliYunOSSUtil;

    @Autowired
    private UserClickHabitService userClickHabitService;

    @Autowired
    private ThirdAccountService thirdAccountService;

    @Autowired
    private WeiXinPayConfiger weiXinPayConfiger;

    @Autowired
    private InviteCardService inviteCardService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String videos(Model model, @RequestParam(required = false) Integer c1, @RequestParam(required = false) Integer c2, @RequestParam(required = false) Integer tagId) {
        Map<String, Object> params = getParamsPageMap(10);
        model.addAttribute("c1s", vodCategoryService.getByFid(null));
        String c1Str = (String) params.get("c1");
        if (StringUtils.isNotEmpty(c1Str)) {
            model.addAttribute("c1Obj", vodCategoryService.get(Integer.valueOf(c1Str)));
        }
        if (c2 != null) {
            model.addAttribute("c2Obj", vodCategoryService.get(c2));
        }
        if (c1 != null && c1 > 0) {
            model.addAttribute("c2s", vodCategoryService.getByFid(c1));
        } else {
            params.remove("c1");
            params.remove("c2");
        }
        if (tagId != null) {
            params.put("tagId", tagId);
            model.addAttribute("tagId", tagId);
        }
        model.addAttribute("videos", videoService.getVodVolumesPage(params));
        userClickHabitService.createHabit(request);
        return "m/video-list";
    }

    @RequestMapping(value = "intro/{volumeId}", method = RequestMethod.GET)
    public String videoIntro(Model model, @PathVariable Integer volumeId, @RequestParam(required = false) Long sectionId) {
        if (getCurrentUser() == null && ClientUtils.isWechat(request)) {
            String state = "weixin_" + RandomStringUtils.randomAlphanumeric(12);
            request.getSession().setAttribute("weixinState", state);
            return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + configer.getWxAccountAppid()
                    + "&redirect_uri=" + configer.getAppUrl() + "m/video/weixin/" + volumeId + "&response_type=code"
                    + "&scope=snsapi_login&state=" + state + "#wechat_redirect&connect_redirect=1";
        }
        VodVolumesDto vodVolumes = videoService.getVodVolumeById(volumeId);
        videoService.addViews(volumeId);
        Map<String, Object> params = new HashMap<>();
        params.put("volumeId", volumeId);
        VodSection section;
        if (sectionId != null) section = videoService.getVodSectionById(sectionId);
        else section = videoService.getFirstSectionByVolumesId(volumeId);
        if (section != null)
            section.setDealURL(aliYunOSSUtil.getObjectEncodeUrl(section.getUrl(), Const.OSS_VIDEO_EXPIRETIME));
        model.addAttribute("section", section);
        model.addAttribute("configer", configer);
        model.addAttribute("chapters", videoService.getVodChapters(params));
        vodVolumes.setTotleNum(chaptersDao.sectionCount(params));
        model.addAttribute("vodVolumes", vodVolumes);
        userClickHabitService.createHabit(request);
        return "m/video-intro";
    }

    @RequestMapping(value = "weixin/{volumeId}", method = RequestMethod.GET)
    private String weixinSet(@PathVariable Integer volumeId, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        if (StringUtils.isNotEmpty(state) && StringUtils.isNotEmpty(code)) {
            // 提取公共方法
            String qqState = getSessionStringValue("qqState");
            if (qqState != null && state.equals(qqState)) {
                thirdAccountService.dealThirdLogin(code, Const.THIRD_ACCOUNT_TYPE_QQ);
            }
            String weixinState = getSessionStringValue("weixinState");
            if (weixinState != null && state.equals(weixinState)) {
                thirdAccountService.dealThirdLogin(code, Const.THIRD_ACCOUNT_TYPE_WEIXIN);
            }
        } else if (StringUtils.isEmpty(code) && StringUtils.isNotEmpty(state)) {
            return "redirect:/m/auth/login";
        }
        return "redirect:/m/video/intro/" + volumeId;
    }

//    @RequestMapping(value = "pay/{sectionId}", method = RequestMethod.GET)
//    public String videoPay(Model model, @PathVariable Long sectionId) {
//        UserDto userDto = getRequiredCurrentUser();
//        if (ClientUtils.isWechat(request) && null == getSessionStringValue("WXGZ_openId")) {
//            String wxstate = "weixinVip_" + RandomStringUtils.randomAlphanumeric(12);
//            request.getSession().setAttribute("weixinPayState", wxstate);
//            String location = "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + weiXinPayConfiger.getAppId()
//                    + "&redirect_uri=" + configer.getAppUrl() + "m/video/wxPayState/" + sectionId + "&response_type=code"
//                    + "&scope=snsapi_base&state=" + wxstate + "#wechat_redirect";
//            // logger.info("jhtest location = " + location);
//            return location;
//        }
//        VodSection vodSection = videoService.getVodSectionById(sectionId);
//        VodVolumes vodVolumes = videoService.getVolumeBySectionId(sectionId);
//        model.addAttribute("vodVolumes", vodVolumes);
//        model.addAttribute("vodSection", vodSection);
//        model.addAttribute("discount", Const.USER_MEMBER_DISCOUNT);
//        return "m/user-video-pay";
//    }
//
//    @RequestMapping(value = "wxPayState/{sectionId}", method = RequestMethod.GET)
//    public String request4payState(@PathVariable Long sectionId, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
//        logger.info("authWeixinGZ in ....");
//        String weixinState = getSessionStringValue("weixinPayState");
//        if (state != null && weixinState != null && state.equals(weixinState)) {
//            WxAccountAccessToken token = thirdAccountService.getWxPayAccessToken(code);
//            if (token != null) {
//                request.getSession().setAttribute("WXGZ_openId", token.getOpenid());
//            }
//        }
//        return "redirect:/m/video/pay/" + sectionId;
//    }

    @RequestMapping(value = "inviteCard", method = RequestMethod.GET)
    public String inviteCard(@RequestParam String inviteCode, Model model, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        if (StringUtils.isEmpty(code) && StringUtils.isNotEmpty(state)) {
            return "redirect:/m/auth/login";
        }
        // 提取公共方法
        String qqState = getSessionStringValue("qqState");
        if (state != null && qqState != null && state.equals(qqState)) {
            thirdAccountService.dealThirdLogin(code, Const.THIRD_ACCOUNT_TYPE_QQ);
            return "redirect:/m/video/activate?inviteCode=" + inviteCode;
        }
        String weixinState = getSessionStringValue("weixinState");
        if (state != null && weixinState != null && state.equals(weixinState)) {
            thirdAccountService.dealThirdLogin(code, Const.THIRD_ACCOUNT_TYPE_WEIXIN);
            return "redirect:/m/video/activate?inviteCode=" + inviteCode;
        }
        model.addAttribute("inviteCode", inviteCode);
        model.addAttribute("inviteCard", inviteCardService.getInviteCardByCode(inviteCode));
        model.addAttribute("configer", configer);
        return "m/inviteCard";
    }

    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public String activateCard(@RequestParam String inviteCode) {
        User user = getRequiredCurrentUser();
        InviteCard inviteCard = inviteCardService.getInviteCardByCode(inviteCode);
        inviteCardService.activiteCard(inviteCard, user);
        switch (inviteCard.getType()) {
            case Const.INVITE_CARD_TYPE_DOC:
                return "redirect:/m/doc/" + inviteCard.getObjectId();
            case Const.INVITE_CARD_TYPE_VOD:
                return "redirect:/m/video/intro/" + inviteCard.getObjectId();
            case Const.INVITE_CARD_TYPE_SOFT:
            case Const.INVITE_CARD_TYPE_MEMBER:
                return "redirect:/m";
            default:
                throw new ServerException(HttpStatus.BAD_REQUEST);
        }
    }

}
