package com.lld360.cnc.core;

import java.math.BigDecimal;

/**
 * Author: dhc
 * Date: 2016-06-29 13:50
 */
public class Const {

    // Session Key
    public static final String SS_USER = "user";

    // Cookie
    public static final int REMEMBER_ME_TIME = 90 * 24 * 60 * 60;   // 记住帐号过期时间（90天）

    //admin status
    public static final Byte ADMIN_STATUS_NORMAL = 1;   //正常
    public static final Byte ADMIN_STATUS_FREEZE = 2;   //冻结

    //admin max failed times
    public static final int MAX_LOGIN_FAIL_TIMES = 3;

    // File belone
    public static final String FILE_USER = "user";
    
    public static final String FILE_ENTERPRISE = "enterprise";

    //third_account type
    public static final int THIRD_ACCOUNT_TYPE_QQ = 1;//QQ
    public static final int THIRD_ACCOUNT_TYPE_WEIXIN = 2;//微信

    //third_account agent
    public static final byte THIRD_ACCOUNT_GENDER_MAN = 1;//男
    public static final byte THIRD_ACCOUNT_GENDER_WOMAN = 0;//女

    //商品类别
    public static final byte PAY_SCORE = 1;//积分
    public static final byte PAY_VIP = 2;//vip会员
    public static final byte PAY_VIDEO = 3;//现金购买视频

    // Page Parameters
    public static final String PAGE_SIZE = "size";
    public static final String PAGE_PAGE = "page";
    public static final String PAGE_TIME = "time";
    public static final String PAGE_LIMIT = "limit";
    public static final String PAGE_OFFSET = "offset";

    // ServletContext Keys
    public static final String CTX_KEY_EXPIRE_TIME_SUFFIX = ":EXPIRE_TIME";
    public static final String CTX_KEY_WXGZH_ACCESS_TOKEN = "WXGZH:ACCESS_TOKEN";
    public static final String CTX_KEY_WXGZH_JSAPI_TICKET = "WXGZH:JSAPI_TICKET";
    public static final String CTX_KEY_CNC_DOWNLOAD = "CNC_DATA_DOWNLOAD";
    public static final String CNC_WX_DOC_DETAIL_FOOTER = "CNC_WX_DOC_DETAIL_FOOTER";
    public static final String CNC_WX_DOC_DETAIL_HEADER = "CNC_WX_DOC_DETAIL_HEADER";
    public static final String CTX_KEY_WXPAY_JSAPI_TICKET = "WXPAY:JSAPI_TICKET";
    public static final String CTX_KEY_WXPAY_ACCESS_TOKEN = "WXPAY:ACCESS_TOKEN";

    // File directory
    public static final String FILE_TEMP_PATH = "temp/";
    public static final String FILE_PREFIX_PATH = "files/";

    // User State
    public static final byte USER_STATE_NORMAL = 1;
    public static final byte USER_STATE_DELETED = -1;
    public static final byte USER_STATE_DESERT = -2;    //废弃账号

    //Valid Type
    public static final byte SMS_REGIST = 1;    //注册
    public static final byte SMS_RESETPWD = 2;  //找回密码
    public static final byte SMS_FORGOTTPWD = 3;  //忘记密码
    public static final byte SMS_BINDACCOUNT = 4;  //绑定手机号
    public static final byte SMS_WITHDRAWALS = 5;   //转账

    public static final String SMS_RWD_TYPE_OK = "OK";  //手机验证成功

    // Doc allow types
    public static final String DOC_ALLOW_TYPES = "pdf,doc,docx,xls,xlsx,ppt,pptx,txt,jpg,jpeg,png,bmp,gif,ico";
    public static final String DOC_NOT_GEN_PIC_TYPES = "txt|jpg|jpeg|png|bmp|gif|ico";
    
    public static final String COURSE_NOT_GEN_PIC_TYPES = "jpg|jpeg|png|bmp|gif|ico";

    // doc  State
    public static final byte DOC_STATE_NORMAL = 1;//正常
    public static final byte DOC_STATE_LOADING = 2;//处理中
    public static final byte DOC_STATE_LOADING_FAILE = 3;//处理失败
    public static final byte DOC_STATE_REVIEWING = 4; //审核中
    public static final byte DOC_STATE_REVIEW_DENY = 5; //审核不通过
    public static final byte DOC_STATE_DELETE = 9;//删除

    public static final String DOC_NAME_MORE = "更多";    //更多
    // doc  uploaderType
    public static final byte DOC_UPLOADERTYPE_ADMIN = 1;    //管理员
    public static final byte DOC_UPLOADERTYPE_USER = 2;     //用户

    public static final int DOC_IMG_PAGES = 50;  // 文档生成图片最大张数

    // posts state
    public static final byte POSTS_STATE_NORMAL = 1; // 正常
    public static final byte POSTS_STATE_DELETE = 0; // 删除

    //postsReward  type
    public static final byte POSTS_REWARD_TYPE_FREE = 0; //不悬赏
    public static final byte POSTS_REWARD_TYPE_SCORE = 1; //牛人币
    public static final byte POSTS_REWARD_TYPE_POINT = 2; //积分

    //postsReward state
    public static final byte POSTS_REWARD_STATE_UNPAID = 1; //未支付
    public static final byte POSTS_REWARD_STATE_PAID = 2;   //已支付
    public static final byte POSTS_REWARD_STATE_RETURNED = 3;   //已返还
    public static final byte POSTS_REWARD_STATE_ADMCHOSE = 4;   //大牛推荐

    // postsComments state
    public static final byte POSTSCOMMENT_STATE_NORMAL = 1; // 正常
    public static final byte POSTSCOMMENT_STATE_DELETE = 0; // 删除

    // video state
    public static final byte VIDEO_STATE_NORMAL = 1;    // 正常
    public static final byte VIDEO_STATE_DEALING = 2;    // 处理中
    public static final byte VIDEO_STATE_DELETE = 9;    // 处理中
    public static final int OSS_VIDEO_EXPIRETIME = 15;  // 视频OSS有效时间(分钟)
    public static final double VIDEO_FREE_SCALE = 0.3;  // 视频免费播放比例

    public static final byte VIDEO_BUY_WAY_SCORE = 1;     //牛人币购买
    public static final byte VIDEO_BUY_WAY_WEIXIN = 2;     //微信支付购买

    // System setting keys
    public static final String SETTING_CNC_ARTICLE_LAST_ID = "LAST_ARTICLE_ID_OF_CNCENGINEER";
    public static final String SETTING_CNC_HELP_MPAGE_CONTENT = "CNC_HELP_MPAGE_CONTENT";
    public static final String SETTING_CNC_WXGZH_CXHFYS = "CNC_WXGZH_CXHFYS";
    public static final String CNC_UPLOAD_DOC_GIVE_SCORE = "CNC_UPLOAD_DOC_GIVE_SCORE";
    public static final String CNC_REGISTER_GIVE_SCORE = "CNC_REGISTER_GIVE_SCORE";
    public static final String CNC_WXGZH_ENTRY_PAGE_CONTENT = "CNC_WXGZH_ENTRY_PAGE_CONTENT";
    public static final String CNC_WXGZH_ENTRY_MOBILE_PAGE_CONTENT = "CNC_WXGZH_ENTRY_MOBILE_PAGE_CONTENT";

    // user score 牛人币
    public static final int USER_ADDSCORE_REGIEST = 50;      //注册赠送 (绑定手机号)

    // User score history type 牛人币type
    public static final byte USER_SCORE_HISTORY_TYPE_DOC_DOWNLOAD = 2;  //下载doc扣nrb
    public static final byte USER_SCORE_HISTORY_TYPE_DOC_SALE = 3;      //doc被下载加nrb
    public static final byte USER_SCORE_HISTORY_TYPE_REGIEST = 4;   //注册赠送nrb
    public static final byte USER_SCORE_HISTORY_TYPE_BIND_ACCOUNT = 5;   //绑定第三方账号
    public static final byte USER_SCORE_HISTORY_TYPE_RECHARGE = 6;    //充值购买nrb
    public static final byte USER_SCORE_HISTORY_TYPE_RECHARGE_PRESENT = 7;      //充值赠送币
    public static final byte USER_SCORE_HISTORY_TYPE_SOFT_DOWNLOAD = 11;  //下载软件扣nrb
    public static final byte USER_SCORE_HISTORY_TYPE_SOFT_SALE = 12;      //软件被下载加nrb
    public static final byte USER_SOCRE_HISTORY_TYPE_MODERATOR_SALARY = 14; //版主月贡献值加nrb
    public static final byte USER_SOCRE_HISTORY_TYPE_ADMIN_ADD = 15;    //管理员手动添加
    public static final byte USER_SCORE_HISTORY_TYPE_WITHDRAWALS = 16;  //转账提现扣除nrb
    public static final byte USER_SCORE_HISTORY_TYPE_POSTS_REWARD = 17;  //发布问题悬赏扣除nrb
    public static final byte USER_SCORE_HISTORY_TYPE_COMMENT_ACCEPTED = 18;  //回答呗采纳加nrb
    public static final byte USER_SCORE_HISTORY_TYPE_REWARD_RETURN = 19;    //悬赏返还
    public static final byte USER_SCORE_HISTORY_TYPE_INVITER_BONUS = 20;    //受邀用户消费返利
    public static final byte USER_SCORE_HISTORY_TYPE_BUYVIP = 21;       //购买vip ,没有积分变动


    // user point 站内积分加分/比例
    public static final int USER_ADDPOINT_SIGNIN = 10;      //签到送积分
    public static final int USER_ADDPOINT_FREE_OPERATE = 10;        //站内操作送积分(免费)
    public static final int USER_ADDPOINT_SCALE_RECHARGE = 1;      //充值送积分比例 积分:牛人币
    public static final int USER_ADDPOINT_SCALE_SCORE_OPERATE = 10;      //花费牛人币送积分比例 积分:牛人币

    // User point history type 站内积分type
    public static final byte USER_POINT_HISTORY_TYPE_DOC_UPLOAD = 1;    //上传文档加分
    public static final byte USER_POINT_HISTORY_TYPE_USER_SIGNIN = 9;   //签到
    public static final byte USER_POINT_HISTORY_TYPE_SOFT_UPLOAD = 10;    //上传软件加分
    public static final byte USER_POINT_HISTORY_TYPE_RECHARGE_PRESENT = 31;    //充值赠送积分
    public static final byte USER_POINT_HISTORY_TYPE_DOC_DOWNLOAD = 32;    //下载文档加分
    public static final byte USER_POINT_HISTORY_TYPE_SOFT_DOWNLOAD = 33;    //下载软件加分
    public static final byte USER_POINT_HISTORY_TYPE_VOD_BUY = 34;    //购买视频加分
    public static final byte USER_POINT_HISTORY_TYPE_POSTS_PUBLISH = 35;    //发布问题加积分
    public static final byte USER_POINT_HISTORY_TYPE_POSTS_COMMENT = 36;    //发布评论加积分
    public static final byte USER_POINT_HISTORY_TYPE_POSTS_REWARD = 37;    //发布问题悬赏扣除积分
    public static final byte USER_POINT_HISTORY_TYPE_COMMENT_ACCEPTED = 38;    //回答被采纳
    public static final byte USER_POINT_HISTORY_TYPE_REWARD_RETURN = 39;    //悬赏返还

    // 版主贡献值modetator_contribution
    public static final byte MODERATOR_CONTRIBUTIONS_TYPE_SIGNIN = 1;   //签到
    public static final byte MODERATOR_CONTRIBUTIONS_TYPE_UPLOAD = 2;   //上传
    public static final byte MODERATOR_CONTRIBUTIONS_TYPE_OPERATE = 3;  //操作
    public static final byte MODERATOR_CONTRIBUTIONS_TYPE_DOCCANCEL = -2;  //上传文档被删除

    public static final int MODERATOR_SIGNIN_CONTRIBUTIONS = 1;     //签到贡献值
    public static final int MODERATOR_UPLOAD_CONTRIBUTIONS = 2;     //上传贡献值
    public static final int MODERATOR_OPERATE_CONTRIBUTIONS = 1;    //操作贡献值
    public static final int MODERATOR_CANCEL_CONTRIBUTIONS = -2;    //上传文档被删除扣贡献值

    public static final int MODERATOR_UPLOAD_CONTRIBUTIONS_LIMIT = 10;      //每日上传贡献值上限
    public static final int MODERATOR_OPERATE_CONTRIBUTIONS_LIMIT = 10;     //每日操作贡献值上限
    public static final int MODERATOR_SALARY_NEED_SCORE = 200;     //版主领取牛人币所需月贡献值
    public static final int MODERATOR_SALARY = 100;     //版主贡献值兑换牛人币

    //ImgFiles type
    public static final byte FILE_TYPE_WEBSITE_INDEX_IMG = 1;    // 门户首页滚动图
    public static final byte FILE_TYPE_ADVERTISE_IMG = 2;    // 广告图片 advertiseImg
    public static final byte FILE_TYPE_WAP_INDEX_IMG= 3;    // wap 首页banner
    public static final byte FILE_TYPE_COMMODITY_IMG = 4;    // 商品banner

    //course
    public static final byte CNC_COURSE_STATUS_UP = 1;//上架
    public static final byte CNC_COURSE_STATUS_DOWN = 0;//下架

    public static final byte CNC_COURSE_TYPE_COURSE = 1;
    public static final byte CNC_COURSE_TYPE_NEWS = 2;

    // 微信第三方服务平台
    public static final String COMPONENT_VERIFY_TICKET = "component_verify_ticket";
    public static final String COMPONENT_ACCESS_TOKEN = "component_access_token";

    //版主管理
    public static final int MODERATOR_STATE_NORMAL = 1;     //申请版主同意
    public static final int MODERATOR_STATE_APPLY = 2;      //申请版主审核中
    public static final int MODERATOR_MAX_APPLY_NUM = 3;    //版主最大申请数

    // 注册登录平台
    public static final String REGIST_PLATFORM_WEB = "Web";     // 网页注册登录
    public static final String REGIST_PLATFORM_MOBILE = "Mobile";   //手机端注册登录
    public static final String REGIST_PLATFORM_QRCODE = "Qrcode";   //手机端二维码注册登录

    // withdraw status 提现状态 -> userWithdraw enum
    public static final byte WITHDRAW_STATUS_NORMAL = 1;//审核中
    public static final byte WITHDRAW_STATUS_REVIEWING = 2;//处理中
    public static final byte WITHDRAW_STATUS_SUCCESS = 3;//提现成功
    public static final byte WITHDRAW_STATUS_FAIL = 4; //提现失败
    public static final byte WITHDRAW_STATUS_REVIEW_DENY = 5; //审核不通过

    //提现方式
    public static final byte ALIPAY = 1;//支付宝
    public static final byte BANKCARD = 2; //银行卡
    public static final byte WEIXIN = 3; //微信

    // WITHDRAW_REAL_AMOUNT 提现实得比例
    public static final BigDecimal WITHDRAW_REAL_AMOUNT = BigDecimal.valueOf(0.8);

    //账号状态
    public static final byte NORMAL = 1;//正常
    public static final byte ABANDON = 2; //废弃
    public static final byte EXPIRE = 3; //过期

    // 邀请关系分利
    public static final double INVITE_PARTER_BONUS_SCALE = 0.1;     // 区域合伙人分利比例
    public static final double INVITE_INVITER_BONUS_SCALE = 0.2;     // 邀请人分利比例

    // user_member 会员
    public static final double USER_MEMBER_DISCOUNT = 0.5;      //会员打折

    // parter state
    public static final byte PARTER_STATE_NORMAL = 1;       //正常
    public static final byte PARTER_STATE_BAN = 2;          //禁止

    // vodLecturer state
    public static final byte VODLECTURER_STATE_NORMAL = 1;       //正常
    public static final byte VODLECTURER_STATE_DELETE = 2;       //删除

    // vod buy type
    public static final byte VOD_BUY_TYPE_SCORE = 1;      // 牛人币
    public static final byte VOD_BUY_TYPE_CASH = 2;      // 现金
    public static final byte VOD_BUY_TYPE_INVITECARD = 3;      // 邀请卡

    // invite card type
    public static final byte INVITE_CARD_TYPE_VOD = 1;      // 视频集
    public static final byte INVITE_CARD_TYPE_DOC = 2;      // doc文档
    public static final byte INVITE_CARD_TYPE_SOFT = 3;      // soft软件
    public static final byte INVITE_CARD_TYPE_MEMBER = 4;      // 会员

    // invite card State
    public static final byte INVITE_CARD_STATE_NEW = 1;     // 新建
    public static final byte INVITE_CARD_STATE_USED = 2;     // 已使用
    public static final byte INVITE_CARD_STATE_DELETE = 3;     // 删除

    // commodity type
    public static final byte COMMODITY_TYPE_SELL = 1;   //出售
    public static final byte COMMODITY_TYPE_NEED = 2;   //求购

    public static final byte COMMODITY_STATE_NORMAL = 1;    //正常
    public static final byte COMMODITY_STATE_DELETE = 9;    //删除
    
    public static final byte ENTERPRISE_CERTIFICATION_STATE_NEW = 1; //新建
    public static final byte ENTERPRISE_CERTIFICATION_STATE_PASS = 2; //通过审核
    public static final byte ENTERPRISE_CERTIFICATION_STATE_FAIL = 3; //审核失败
    
    public static final byte ENTERPRISE_UPLOADPIC_TYPE_LOGO =1;
    public static final byte ENTERPRISE_UPLOADPIC_TYPE_QRCODE =2;
    public static final byte ENTERPRISE_UPLOADPIC_TYPE_REGISTRATION =3;

}
