package com.lld360.cnc.website.controller;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.WeiXinPayConfiger;
import com.lld360.cnc.core.utils.AliYunOSSUtil;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.dto.VodVolumesDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserStatement;
import com.lld360.cnc.model.VodSection;
import com.lld360.cnc.repository.VodChaptersDao;
import com.lld360.cnc.service.*;
import com.lld360.cnc.util.FinanceUtil;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.dto.WCPayRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Controller
@RequestMapping("video")
public class VideoController extends SiteController {

    @Autowired
    private VodCategoryService vodCategoryService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VodChaptersDao chaptersDao;

    @Autowired
    private AliYunOSSUtil aliYunOSSUtil;

    @Autowired
    private UserClickHabitService userClickHabitService;

    @Autowired
    private UserStatementService userStatementService;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private WeiXinPayConfiger weiXinPayConfiger;

    @Autowired
    private UserMemberService userMemberService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String videoList(Model model, @RequestParam(required = false) Integer c1) {
        Map<String, Object> params = getParamsPageMap(10);
        model.addAttribute("c1s", vodCategoryService.getByFid(null));
        String c1Str = (String) params.get("c1");
        if (StringUtils.isNotEmpty(c1Str)) {
            model.addAttribute("c1Obj", vodCategoryService.get(Integer.valueOf(c1Str)));
        }
        if (c1 != null && c1 > 0) {
            model.addAttribute("c2s", vodCategoryService.getByFid(c1));
        } else {
            params.remove("c1");
            params.remove("c2");
        }
        model.addAttribute("videos", videoService.getVodVolumesPage(params));
        return "wVideo/videos";
    }

    @RequestMapping(value = "intro/{volumeId}", method = RequestMethod.GET)
    public String videoIntro(Model model, @PathVariable Integer volumeId) {
        VodVolumesDto vodVolumes = videoService.getVodVolumeById(volumeId);
        videoService.addViews(volumeId);
        Map<String, Object> params = new HashMap<>();
        params.put("volumeId", volumeId);
        model.addAttribute("firstSection", videoService.getFirstSectionByVolumesId(volumeId));
        model.addAttribute("chapters", videoService.getVodChapters(params));
        vodVolumes.setTotleNum(chaptersDao.sectionCount(params));
        model.addAttribute("vodVolumes", vodVolumes);
        userClickHabitService.createHabit(request);
        return "wVideo/video-intro";
    }

    @RequestMapping(value = "detail/{volumeId}/{sectionId}", method = RequestMethod.GET)
    public String videoDetail(@PathVariable Integer volumeId, @PathVariable long sectionId, Model model) {
        VodSection section = videoService.getVodSectionById(sectionId);
        model.addAttribute("section", section);
        Map<String, Object> params = new HashMap<>();
        params.put("volumeId", volumeId);
        model.addAttribute("chapters", videoService.getVodChapters(params));
        model.addAttribute("vodVolumes", videoService.getVodVolumeById(volumeId));
        videoService.addViews(volumeId);
        return "wVideo/video-detail";
    }

    @ResponseBody
    @RequestMapping(value = "getDealURL", method = RequestMethod.GET)
    public ResponseEntity getDealUrl(@RequestParam Long sectionId, @RequestParam Integer volumeId) {
        VodVolumesDto vodVolumes = videoService.getVodVolumeById(volumeId);
        VodSection section = videoService.getVodSectionById(sectionId);
        URL result = aliYunOSSUtil.getObjectEncodeUrl(section.getUrl(), Const.OSS_VIDEO_EXPIRETIME);
        if (section.getFree() || vodVolumes.getCostScore().equals(0)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        User user = getRequiredCurrentUser();
        if (user.getId() != null && videoService.isBuyVolume(user.getId(), volumeId)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOTBUY", HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "checkIsBuy", method = RequestMethod.GET)
    public boolean isBuy(@RequestParam Integer volumeId, @RequestParam Long sectionId) {
        VodVolumesDto vodVolumes = videoService.getVodVolumeById(volumeId);
        VodSection section = videoService.getVodSectionById(sectionId);
        UserDto user = getRequiredCurrentUser();
        return vodVolumes.getCostScore().equals(0) || section.getFree() || videoService.isBuyVolume(user.getId(), volumeId);
    }

    @ResponseBody
    @RequestMapping(value = "checkIsBuyVol", method = RequestMethod.GET)
    public boolean isBuy(@RequestParam Integer volumeId) {
        VodVolumesDto vodVolumes = videoService.getVodVolumeById(volumeId);
        UserDto user = getRequiredCurrentUser();
        return vodVolumes.getCostScore().equals(0) || videoService.isBuyVolume(user.getId(), volumeId);
    }

    // 购买视频
    @RequestMapping(value = "buy", method = RequestMethod.POST)
    public ResponseEntity buyVolume(@RequestParam Integer volumeId) {
        UserDto userDto = getRequiredCurrentUser();
        VodVolumesDto vodVolumes = videoService.getVodVolumeById(volumeId);
        if (userDto.getId() == null)
            return new ResponseEntity<>("请先绑定网站账号", HttpStatus.BAD_REQUEST);
        videoService.buyVolume(userDto, vodVolumes);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 微信购买
    @ResponseBody
    @RequestMapping(value = "weixinBuy", method = RequestMethod.POST)
    public WCPayRequest weixinRecharge(@RequestParam Integer volumeId) {
        UserDto user = getRequiredCurrentUser();
        VodVolumesDto vodVolumes = videoService.getVodVolumeById(volumeId);
        Integer costScore = vodVolumes.getCostScore();
        if (costScore == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        BigDecimal amount;
        if (userMemberService.isMember(user.getId())) {
            amount = BigDecimal.valueOf(costScore * Const.USER_MEMBER_DISCOUNT / configer.getRechargeExchangeRate());
        } else {
            amount = new BigDecimal(costScore / configer.getRechargeExchangeRate());
        }
        String openId = getSessionStringValue("WXGZ_openId");
        UserStatement userStatement = FinanceUtil.buildUserStatementForWeixin(user.getId(), amount, configer.getRechargeExchangeRate(), Const.PAY_VIDEO);
        userStatement.setObjectId(volumeId);
        userStatementService.saveTempData(userStatement);
        String prepayId = weixinPayService.unifiedorder(amount, userStatement.getInnerTradeNo(), ClientUtils.getIp(request), openId);
        WCPayRequest payRequest = makeUpWcPayRequest(prepayId, userStatement.getInnerTradeNo());
        logger.info("微信-wap-生成内部订单：" + userStatement.toString());
        return payRequest;
    }

    // 支付宝购买
    @RequestMapping(value = "alipay", method = RequestMethod.POST)
    public String alipayRecharge(RedirectAttributes attributes, @RequestParam Integer volumeId) {
        UserDto user = getRequiredCurrentUser();
        VodVolumesDto vodVolumes = videoService.getVodVolumeById(volumeId);
        Integer costScore = vodVolumes.getCostScore();
        BigDecimal amount;
        if (userMemberService.isMember(user.getId())) {
            amount = BigDecimal.valueOf(costScore * Const.USER_MEMBER_DISCOUNT / configer.getRechargeExchangeRate());
        } else {
            amount = new BigDecimal(costScore / configer.getRechargeExchangeRate());
        }

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            UserStatement userStatement = FinanceUtil.buildUserStatement(user.getId(), amount, configer.getRechargeExchangeRate(), Const.PAY_VIDEO);
            userStatementService.saveTempData(userStatement);
            logger.info("支付宝-wap-生成内部订单：" + userStatement.toString());
            attributes.addAttribute("innerTradeNo", userStatement.getInnerTradeNo());
        }
        return "redirect:/m/finance/alipay/gotoPayOrder";
    }

    private WCPayRequest makeUpWcPayRequest(String prepayId, String innerNo) {
        WCPayRequest request = new WCPayRequest();
        SortedMap<Object, Object> parameters = new TreeMap<>();
        request.setAppId(weiXinPayConfiger.getAppId());
        parameters.put("appId", weiXinPayConfiger.getAppId());
        String nonceStr = RandomStringUtils.randomAlphanumeric(18);
        request.setNonceStr(nonceStr);
        parameters.put("nonceStr", nonceStr);
        request.setSignType("MD5");
        parameters.put("signType", "MD5");
        request.setWxPackage("prepay_id=" + prepayId);
        parameters.put("package", "prepay_id=" + prepayId);
        long timeStamp = System.currentTimeMillis() / 1000;
        request.setTimeStamp(timeStamp);
        parameters.put("timeStamp", timeStamp);
        String sign = weixinPayService.createSign(parameters);
        request.setPaySign(sign);
        request.setInnerNo(innerNo);
        return request;
    }

}
