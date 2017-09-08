package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.UserDao;
import com.lld360.cnc.repository.WxfwUserDao;
import com.lld360.cnc.vo.WxGzhAccessToken;
import com.lld360.cnc.wxmsg.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WxTempMsgService extends BaseService {

    public static final String WX_TEMP_MSG_TOKEN = "WXTEMPMSGTOKEN";

    public static final String REGIST_TEMPLATE_ID = "QfS6HocLoZly71KwMefh62UnqdfA5k1ZsiPCQZgK5nE"; //注册成功通知
    public static final String CHNAGEPSWD_TEMPLATE_ID = "gOgGus5dfplXQu3xApzjCF4JiRDw6TNFGZ0fKoDyc-U"; //密码修改通知
    public static final String MEMBER_TEMPLATE_ID = "o3W1-esRqjZd_er38YklHkA6O87MfJOPdq5IhTYMYis"; //开通成功通知
    public static final String MEMBEREND_TEMPLATE_ID = "nrptV2UOslC2hjCCct65Y8Fu9vArDu2ml8Wa_kmRI2o"; //会员资格失效通知
    public static final String PARTER_TEMPLATE_ID = "9awlgkIxdsXwuH7IXCSK-x34GX4i5VUSut6avDX-ZEo"; //成为合伙人提醒
    public static final String INVITE_TEMPLATE_ID = "4SwZmB7iXzhyL5uik0b0ZTc0aagHhz44pIPgKzSYWG4"; //邀请注册成功通知
    public static final String VODBUY_TEMPLATE_ID = "QR-Ob9qeaz2UXHv_RdxcXGKEwcb4ikCTsa_F6_Sznxg"; //购买成功通知
    public static final String VODCHG_TEMPLATE_ID = "CNYTZoXXWHQULERaavDvZKr6_OQuWaNp_eD1CUdgLtA"; //课程更新通知
    public static final String CHECK_TEMPLATE_ID = "X8DQEFC33phQtPz8ICdZObSwc7iJL0krIsAXKuCY6-w"; //审核结果通知
    public static final String SCORE_TEMPLATE_ID = "1ayx3Nz-Zzfx_5485Yqq5J4N0NXyxLi4CYzsRYQKTRk"; //账户余额通知
    public static final String MODERATOR_TEMPLATE_ID = "e_ufljDx83V_YLEj2BTfBUtI6vWbiGZlS8xS46VvTNE"; //版主审核结果
    public static final String SCORE_CHANGE_TEMPLATE_ID = "SC8cTBNiAhOfZeBvPgoFqaQHXk2egz70OhtLp2Jgk6w";    // 积分变动
    public static final String WITHDRAW_TEMPLATE_ID = "N3Gc379dc6vg55oazAV-NLg_AwvrspRILLgN87MyMk0";    //提现结果

    @Autowired
    private Configer coreConfiger;

    @Autowired
    private UserDao userDao;

    @Autowired
    private WxfwUserDao wxfwUserDao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
    private static String utf8 = "UTF-8";
    private static String iso88591 = "ISO-8859-1";
    private static String defaultFontColor = "#173177";
    private static String defaultTopColor = "#ff0000";
    private static String defaultUserOpenid = "oh4jlw96an3dbvhuSOevLBPO2jaQ";
    private static String unifiedorderGetPrepayIdError = "unifiedorder.getPrepayId error";
    private static String defaultUserCenterStr = "m/#usercenter";

    // TODO:具体业务逻辑
    // 注册成功-push消息
    public void sendRegistWxMsg(ThirdAccount thirdAccount) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                WxRegistMsgData registMsgData = new WxRegistMsgData();
                User user = userDao.findUserDto(thirdAccount.getUserId());

                String first = "欢迎注册大牛数控－国内最大的数控交流中心";
                String remark = "感谢您对我们的肯定,注册赠送50牛人币,祝您学习愉快！";
                String keyword1 = user.getNickname().isEmpty() ? thirdAccount.getNickname() : user.getNickname();
                String keyword2 = simpleDateFormat.format(Calendar.getInstance(Locale.CHINA).getTime());

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);

                registMsgData.setFirst(data_first);
                registMsgData.setRemark(data_remark);
                registMsgData.setKeyword1(data_kwd1);
                registMsgData.setKeyword2(data_kwd2);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(REGIST_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(registMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 更改密码-push消息
    public void sendPwdWxMsg(ThirdAccount thirdAccount) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                WxRegistMsgData registMsgData = new WxRegistMsgData();
                User user = userDao.findUserDto(thirdAccount.getUserId());
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                String nickName = user.getNickname().isEmpty() ? thirdAccount.getNickname() : user.getNickname();
                if (StringUtils.isEmpty(nickName)) nickName = "大牛用户";
                String first = "亲爱的" + nickName + "，您在大牛数控的账户密码已被修改，请知悉并确定这是您本人的操作。";
                String remark = "如非本人操作，请及时联系管理员处理。";
                String keyword1 = simpleDate.format(Calendar.getInstance(Locale.CHINA).getTime());
                String keyword2 = ClientUtils.getIp(request);

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);

                registMsgData.setFirst(data_first);
                registMsgData.setRemark(data_remark);
                registMsgData.setKeyword1(data_kwd1);
                registMsgData.setKeyword2(data_kwd2);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(CHNAGEPSWD_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(registMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 查询余额通知
    public void sendScoreWxMsg(String toUserOpenid, UserDto userDto) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                Wx4paramMsgData registMsgData = new Wx4paramMsgData();

                String first = "亲爱的大牛用户，您的账号信息如下：";
                String remark = "点击查看详情";
                String keyword1 = userDto.getNickname().isEmpty() ? userDto.getNickname() : userDto.getNickname();
                String keyword2 = userDto.getTotalScore() + " 牛人币";
                String keyword3 = simpleDateFormat.format(Calendar.getInstance(Locale.CHINA).getTime());

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);

                registMsgData.setFirst(data_first);
                registMsgData.setRemark(data_remark);
                registMsgData.setKeyword1(data_kwd1);
                registMsgData.setKeyword2(data_kwd2);
                registMsgData.setKeyword3(data_kwd3);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(SCORE_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(registMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }

    }

    // 高级会员-push消息
    public void sendMemberWxMsg(ThirdAccount thirdAccount, UserMember userMember) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {

            try {
                Wx4paramMsgData msgData = new Wx4paramMsgData();
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

                String first = "您的高级会员资格已开通成功啦";
                String remark = "感谢的您的支持";
                String keyword1 = "高级会员";
                String keyword2 = simpleDate.format(userMember.getUpdateTime());
                String keyword3 = simpleDate.format(userMember.getEndTime());

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);

                msgData.setFirst(data_first);
                msgData.setRemark(data_remark);
                msgData.setKeyword1(data_kwd1);
                msgData.setKeyword2(data_kwd2);
                msgData.setKeyword3(data_kwd3);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(MEMBER_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(msgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 高级会员到期-push消息
    public void sendMemberEndWxMsg(ThirdAccount thirdAccount, UserMember userMember) {
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {

            try {
                Wx4paramMsgData msgData = new Wx4paramMsgData();
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                User user = userDao.findUserDto(thirdAccount.getUserId());
                String nickName = user.getNickname().isEmpty() ? thirdAccount.getNickname() : user.getNickname();
                if (StringUtils.isEmpty(nickName)) nickName = "大牛用户";

                String first = "亲爱的大牛用户,您的高级会员即将过期";
                String remark = "如因忘记充值而丧失高级会员资格，请尽快充值！感谢您对大牛数控的支持~";
                String keyword1 = nickName;
                String keyword2 = "有效时间即将过期";
                String keyword3 = simpleDate.format(userMember.getEndTime());

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);

                msgData.setFirst(data_first);
                msgData.setRemark(data_remark);
                msgData.setKeyword1(data_kwd1);
                msgData.setKeyword2(data_kwd2);
                msgData.setKeyword3(data_kwd3);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(MEMBEREND_TEMPLATE_ID);
                wxTemplateMsg.setUrl(coreConfiger.getAppUrl() + "m/user/buyVip");
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(msgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 成为合伙人-push消息
    public void sendParterWxMsg(ThirdAccount thirdAccount) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                WxRegistMsgData registMsgData = new WxRegistMsgData();
                User user = userDao.findUserDto(thirdAccount.getUserId());
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                String nickName = user.getNickname().isEmpty() ? thirdAccount.getNickname() : user.getNickname();
                if (StringUtils.isEmpty(nickName)) nickName = "大牛用户";
                String first = "尊敬的" + nickName + "，恭喜您成为大牛成为合伙人";
                String remark = "祝您生活愉快";
                String keyword1 = nickName;
                String keyword2 = simpleDate.format(Calendar.getInstance(Locale.CHINA).getTime());

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);

                registMsgData.setFirst(data_first);
                registMsgData.setRemark(data_remark);
                registMsgData.setKeyword1(data_kwd1);
                registMsgData.setKeyword2(data_kwd2);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(PARTER_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(registMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 邀请注册成功通知
    public void sendInviteWxMsg(ThirdAccount thirdAccount) {
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                Wx4paramMsgData registMsgData = new Wx4paramMsgData();
                User user = userDao.findUserDto(thirdAccount.getUserId());

                String first = "有人通过您的邀请，并成功注册";
                String remark = "TA消费的牛人币会按比例打入您的账户";
                String keyword1 = user.getNickname().isEmpty() ? thirdAccount.getNickname() : user.getNickname();
                String keyword2 = user.getMobile().isEmpty() ? "151****5884" : user.getMobile();
                String keyword3 = simpleDateFormat.format(Calendar.getInstance(Locale.CHINA).getTime());

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);

                registMsgData.setFirst(data_first);
                registMsgData.setRemark(data_remark);
                registMsgData.setKeyword1(data_kwd1);
                registMsgData.setKeyword2(data_kwd2);
                registMsgData.setKeyword3(data_kwd3);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(INVITE_TEMPLATE_ID);
                wxTemplateMsg.setUrl(coreConfiger.getAppUrl() + "m/");
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(registMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 课程购买成功通知
    public void sendVideoBuyWxMsg(ThirdAccount thirdAccount, VodVolumes vodVolumes) {
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                Wx4paramMsgData registMsgData = new Wx4paramMsgData();
                SimpleDateFormat simpleDate2 = new SimpleDateFormat("yyMMddHHmm");

                String first = "您已成功购买课程《" + vodVolumes.getName() + "》";
                String remark = "感谢您的购买！,祝您学习愉快！";
                String keyword1 = simpleDate2.format(Calendar.getInstance(Locale.CHINA).getTime()) + vodVolumes.getId();
                String keyword2 = vodVolumes.getName();
                String keyword3 = vodVolumes.getCostScore() + " 牛人币";
                String keyword4 = simpleDateFormat.format(Calendar.getInstance(Locale.CHINA).getTime());

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd4 = new DataTemp(new String(keyword4.getBytes(utf8), iso88591), defaultFontColor);

                registMsgData.setFirst(data_first);
                registMsgData.setRemark(data_remark);
                registMsgData.setKeyword1(data_kwd1);
                registMsgData.setKeyword2(data_kwd2);
                registMsgData.setKeyword3(data_kwd3);
                registMsgData.setKeyword4(data_kwd4);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(VODBUY_TEMPLATE_ID);
                wxTemplateMsg.setUrl(coreConfiger.getAppUrl() + "m/video/intro/" + vodVolumes.getId());
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(registMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 课程更新通知
    public void sendVodChgWxMsg(ThirdAccount thirdAccount, VodVolumes vodVolumes, String changeType) {
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                WxRegistMsgData registMsgData = new WxRegistMsgData();
                User user = userDao.findUserDto(thirdAccount.getUserId());
                String nickName = user.getNickname().isEmpty() ? thirdAccount.getNickname() : user.getNickname();
                if (StringUtils.isEmpty(nickName)) nickName = "大牛用户";
                if (StringUtils.isEmpty(changeType)) changeType = "课程更新";

                String first = "亲爱的" + nickName + "，您报名的以下课程发生变更，敬请关注学习安排。";
                String remark = "更多相关课程，尽在大牛数控~";
                String keyword1 = "《" + vodVolumes.getName() + "》";
                String keyword2 = changeType;

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);

                registMsgData.setFirst(data_first);
                registMsgData.setRemark(data_remark);
                registMsgData.setKeyword1(data_kwd1);
                registMsgData.setKeyword2(data_kwd2);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(VODCHG_TEMPLATE_ID);
                wxTemplateMsg.setUrl(coreConfiger.getAppUrl() + "m/video/");
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(registMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 积分变动
    public void sendScoreChgWxMsg(ThirdAccount thirdAccount, String changeInfo, Integer socreChange, Integer totalScore) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                Wx4paramMsgData wx4paramMsgData = new Wx4paramMsgData();

                String first;
                first = changeInfo;
                String remark = "余额可提现和站内消费,查看个人中心>>>";
                String keyword1 = simpleDateFormat.format(Calendar.getInstance(Locale.CHINA).getTime());
                String keyword2 = socreChange + " 牛人币";
                String keyword3 = totalScore + " 牛人币";

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);

                wx4paramMsgData.setFirst(data_first);
                wx4paramMsgData.setRemark(data_remark);
                wx4paramMsgData.setKeyword1(data_kwd1);
                wx4paramMsgData.setKeyword2(data_kwd2);
                wx4paramMsgData.setKeyword3(data_kwd3);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(SCORE_CHANGE_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(wx4paramMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 提现通知
    public void withdrawWxMsg(ThirdAccount thirdAccount, UserWithdraw userWithdraw) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                Wx4paramMsgData wx4paramMsgData = new Wx4paramMsgData();

                String first = "亲爱的大牛用户,以下是您提现申请结果";
                String remark = "感谢您对我们的支持,祝您生活愉快！";
                String keyword1 = userWithdraw.getTradeAmount() + "元";
                String keyword2 = simpleDateFormat.format(Calendar.getInstance(Locale.CHINA).getTime());
                String keyword3 = UserWithdraw.TradeStatus.values()[userWithdraw.getTradeStatus() - 1].getName();

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);

                wx4paramMsgData.setFirst(data_first);
                wx4paramMsgData.setRemark(data_remark);
                wx4paramMsgData.setKeyword1(data_kwd1);
                wx4paramMsgData.setKeyword2(data_kwd2);
                wx4paramMsgData.setKeyword3(data_kwd3);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(WITHDRAW_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(wx4paramMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    /*
    * 审核结果通知
    * @param thirdAccount 账号
     * @param checkType 审核类型
     * @param checkTitle 审核标题
     * @param checkResult 审核结果
    * */
    public void sendCheckResultWxMsg(ThirdAccount thirdAccount, String checkType, DataTemp checkTitle, String checkResult) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {
            try {
                Wx4paramMsgData registMsgData = new Wx4paramMsgData();

                String first = "亲爱的大牛用户，以下是您申请信息的审核结果";
                String remark = "您可以进入个人中心查看，如有疑问请联系客服~";
                String keyword1 = checkType;
                String keyword3 = simpleDateFormat.format(Calendar.getInstance(Locale.CHINA).getTime());
                String keyword4 = checkResult;

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = checkTitle;
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd4 = new DataTemp(new String(keyword4.getBytes(utf8), iso88591), defaultFontColor);

                registMsgData.setFirst(data_first);
                registMsgData.setRemark(data_remark);
                registMsgData.setKeyword1(data_kwd1);
                registMsgData.setKeyword2(data_kwd2);
                registMsgData.setKeyword3(data_kwd3);
                registMsgData.setKeyword4(data_kwd4);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(CHECK_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(registMsgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 版主审核通知
    public void sendModeratorWxMsg(ThirdAccount thirdAccount, String checkResult) {
        String defaultJumpUrl = coreConfiger.getAppUrl() + defaultUserCenterStr;
        String toUserOpenid = getUserOpenId(thirdAccount);
        if (StringUtils.isNotEmpty(toUserOpenid)) {

            try {
                Wx4paramMsgData msgData = new Wx4paramMsgData();
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                String first = "版主申请结果通知";
                String remark = "感谢的您的支持";
                String keyword1 = "文档板块版主";
                String keyword2 = checkResult;
                String keyword3 = simpleDate.format(Calendar.getInstance(Locale.CHINA).getTime());

                DataTemp data_first = new DataTemp(new String(first.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_remark = new DataTemp(new String(remark.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd1 = new DataTemp(new String(keyword1.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd2 = new DataTemp(new String(keyword2.getBytes(utf8), iso88591), defaultFontColor);
                DataTemp data_kwd3 = new DataTemp(new String(keyword3.getBytes(utf8), iso88591), defaultFontColor);

                msgData.setFirst(data_first);
                msgData.setRemark(data_remark);
                msgData.setKeyword1(data_kwd1);
                msgData.setKeyword2(data_kwd2);
                msgData.setKeyword3(data_kwd3);

                WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
                wxTemplateMsg.setTouser(toUserOpenid);
                wxTemplateMsg.setTemplate_id(MODERATOR_TEMPLATE_ID);
                wxTemplateMsg.setUrl(defaultJumpUrl);
                wxTemplateMsg.setTopcolor(defaultTopColor);
                wxTemplateMsg.setData(msgData);

                postWxTempMsg(wxTemplateMsg);
            } catch (UnsupportedEncodingException e) {
                logger.error(unifiedorderGetPrepayIdError, e);
            }
        }
    }

    // 获取用户服务号相关openId
    private String getUserOpenId(ThirdAccount thirdAccount) {
        String toUserOpenid = "";
        if (thirdAccount.getUnionid() != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("unionid", thirdAccount.getUnionid());
            param.put("limit", 1);
            param.put("offset", 0);
            List<WxfwUser> users = wxfwUserDao.search(param);
            if (!users.isEmpty()) {
                toUserOpenid = users.get(0).getOpenid();
            }
        }
        return toUserOpenid;
    }

    // 发送post请求发送模板消息
    private void postWxTempMsg(WxTemplateMsg wxTemplateMsg) {
        WxGzhAccessToken accessToken = getWxGzhAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken.getAccessToken();
        RestTemplate template = new RestTemplate();
        String postData = JSONObject.fromObject(wxTemplateMsg).toString();
        JSONObject jsonObject = template.postForObject(url, postData, JSONObject.class);
        try {
            int errorCode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if (errorCode == 0) {
                logger.info("模版发送成功 errcode:{" + errorCode + "} errmsg:{" + errorMsg + "}");
            } else {
                logger.error("模版发送失败 errcode:{" + errorCode + "} errmsg:{" + errorMsg + "}");
            }
        } catch (Exception e) {
            logger.error("模版消息请求发送异常，" + e.getMessage());
        }
    }

    /**
     * 获取微信公众号的AccessToken
     *
     * @return WxGzhAccessToken
     */
    public WxGzhAccessToken getWxGzhAccessToken() {
        WxGzhAccessToken accessToken = getContextAttribute(WX_TEMP_MSG_TOKEN);
        if (accessToken == null || accessToken.isExpired()) {
            RestTemplate temp = new RestTemplate();
            try {
                // 凭证获取（GET）
                String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + coreConfiger.getWxFwAppid() + "&secret=" + coreConfiger.getWxFwScret();
                accessToken = temp.getForObject(tokenUrl, WxGzhAccessToken.class);
                if (accessToken.getAccessToken() != null) {
                    context.setAttribute(WX_TEMP_MSG_TOKEN, accessToken);
                    logger.info("获取微信模板消息AccessToken: " + accessToken);
                }
            } catch (RestClientException e) {
                logger.warn("获取微信模板消息AccessToken失败", e);
            }
        }
        return accessToken;
    }

    // GET获取服务号用户列表(openid)并存储入数据库
    public String getWeixinFwUsers() {
        String result = "";
        WxGzhAccessToken accessToken = getWxGzhAccessToken();
        // 如果要接着查询 url添加 "&next_openid=NEXT_OPENID"
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken.getAccessToken();
        WxfwUser lastUser = wxfwUserDao.getLastUser();
        if (lastUser != null && lastUser.getOpenid() != null) {
            url += "&next_openid=" + lastUser.getOpenid();
        }

        RestTemplate temp = new RestTemplate();
        try {
            JSONObject jsonObject = temp.getForObject(url, JSONObject.class);
            JSONObject data = jsonObject.getJSONObject("data");
            Integer count = jsonObject.getInt("count");
            if (count == 0) return "获取服务号用户数为0";

            JSONArray users = data.getJSONArray("openid");
            if (!users.isEmpty()) {
                Integer num = 0;
                for (Object userOpenId : users) {
                    String openid = userOpenId.toString();
                    List<WxfwUser> wxfwUsers = getWxfwUsersByOpenId(openid);
                    if (!wxfwUsers.isEmpty()) continue;
                    WxfwUser wxfwUser = new WxfwUser();
                    wxfwUser.setOpenid(openid);
                    wxfwUserDao.create(wxfwUser);
                    num++;
                }
                result = "成功获取服务号用户数为: " + count + "人,实际存储" + num + "人";
            } else {
                result = "获取服务号用户列表失败";
            }
            logger.info(result);
        } catch (RestClientException e) {
            logger.warn("获取服务号用户列表请求失败", e);
        }
        return result;
    }

    // 获取服务号用户信息(unionid)
    public String getWxfwUsersUnionid() {
        String result = "";
        List<WxfwUser> wxfwUsers = wxfwUserDao.searchNoUnionUser();
        if (wxfwUsers != null && !wxfwUsers.isEmpty()) {
            Integer num = 0;
            for (WxfwUser user : wxfwUsers) {
                updateWxfwUserInfo(user.getOpenid());
                num++;
            }
            result = "成功获取" + num + "条用户信息";
        }
        return result;

    }

    // 获取单个用户信息并更新数据库
    public void updateWxfwUserInfo(String openid) {
        WxGzhAccessToken accessToken = getWxGzhAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken.getAccessToken() + "&openid=" + openid + "&lang=zh_CN";
        RestTemplate temp = new RestTemplate();
        try {
            JSONObject jsonObject = temp.getForObject(url, JSONObject.class);
            if (jsonObject.get("unionid") != null) {
                String unionid = jsonObject.getString("unionid");

                WxfwUser user = new WxfwUser();
                user.setOpenid(openid);
                user.setUnionid(unionid);
                user.setUpdateTime(Calendar.getInstance(Locale.CHINA).getTime());

                List<WxfwUser> wxfwUsers = getWxfwUsersByOpenId(openid);
                if (wxfwUsers.isEmpty()) {
                    wxfwUserDao.create(user);
                } else {
                    wxfwUserDao.updateByOpenid(user);
                }
            } else if (jsonObject.get("errcode") != null) {
                logger.info("获取服务号用户信息失败:{errcode:" + jsonObject.getString("errcode") + " errmsg:" + jsonObject.getString("errmsg") + "}");
            } else {
                logger.info("获取服务号用户信息失败");
            }
        } catch (RestClientException e) {
            logger.info("获取服务号用户信息请求失败");
        }
    }

    private List<WxfwUser> getWxfwUsersByOpenId(String openid) {
        Map<String, Object> param = new HashMap<>();
        param.put("openid", openid);
        return wxfwUserDao.search(param);
    }


}
