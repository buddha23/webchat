package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.WxGzh;
import com.lld360.cnc.service.WxGzhService;
import com.lld360.cnc.service.WxTempMsgService;
import com.lld360.cnc.vo.WxApiBack;
import com.lld360.cnc.vo.WxGzhButton;
import com.lld360.cnc.vo.WxGzhMessage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-07-12 14:35
 */
@RestController
@RequiresRoles("admin")
@RequestMapping("admin/wx")
public class AdmWxController extends AdmController {

    @Autowired
    private WxGzhService wxGzhService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    // 获取微信按钮
    @RequestMapping(value = "buttons", method = RequestMethod.GET)
    public List<WxGzhButton> menuButtonGet() {
        return wxGzhService.getWxGzhButtons();
    }

    // 设置微信按钮
    @OperateRecord("设置微信按钮")
    @RequestMapping(value = "buttons", method = RequestMethod.POST)
    public WxApiBack menuButtonPost(@RequestBody List<WxGzhButton> buttons) {
        return wxGzhService.setWxGzhButtons(buttons);
    }

    // 获取公众号回复消息默认配置
    @RequestMapping(value = "articles", method = RequestMethod.GET)
    public List<WxGzhMessage.Article> messageArticleSettingGet() {
        return wxGzhService.getWxGzhMessageArticleSetting();
    }

    // 获取公众号回复消息默认配置
    @OperateRecord("设置公众号回复消息配置")
    @RequestMapping(value = "articles", method = RequestMethod.POST)
    public List<WxGzhMessage.Article> messageArticleSettingPost(@RequestBody List<WxGzhMessage.Article> articles) {
        return wxGzhService.setWxGzhMessageArticleSetting(articles);
    }

    // 上传回复文章列表图标
    @OperateRecord("上传回复文章列表图标")
    @RequestMapping(value = "articleIcon/{idx}", method = RequestMethod.POST)
    public List<WxGzhMessage.Article> docFilePost(@PathVariable int idx, @RequestParam MultipartFile file) {
        return wxGzhService.setWxGzhMessageArticleImage(idx, file);
    }

    //********************* 公众平台调用接口 ***********************

    // 授权平台的微信公众号列表
    @RequiresPermissions("admin")
    @RequestMapping(value = "gzh", method = RequestMethod.GET)
    public Page<WxGzh> wxGzhGet() {
        Map<String, Object> params = getParamsPageMap(15);
        return wxGzhService.getWxGzhs(params);
    }


    //********************* 微信服务号调用接口 ***********************
    @RequestMapping(value = "user/download", method = RequestMethod.GET)
    public ResultOut downloadDataGet() {
        ResultOut out = new ResultOut();
        String result = wxTempMsgService.getWeixinFwUsers();
        out.setMessage(result);
        return out;
    }

    @RequestMapping(value = "user/getUserInfo", method = RequestMethod.GET)
    public ResultOut getUserInfo() {
        ResultOut out = new ResultOut();
        String result = wxTempMsgService.getWxfwUsersUnionid();
        out.setMessage(result);
        return out;
    }
}
