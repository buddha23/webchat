package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * WxGzh
 */
public class WxGzh implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 微信公众号名称
     */
    @NotBlank(message = "微信公众号名称 不能为空。")
    @Length(max = 63, message = "微信公众号名称 最大长度不能超过63个字符")
    private String name;
    /**
     * 公众号头像
     */
    @Length(max = 255, message = "公众号头像 最大长度不能超过255个字符")
    private String icon;
    /**
     * 公众号二维码图片地址
     */
    @Length(max = 255, message = "公众号二维码图片地址 最大长度不能超过255个字符")
    private String qrcodeUrl;
    /**
     * 公众号的原始ID
     */
    @Length(max = 63, message = "公众号的原始ID 最大长度不能超过63个字符")
    private String userName;
    /**
     * 公众号所设置的微信号，可能为空
     */
    @Length(max = 63, message = "公众号所设置的微信号，可能为空 最大长度不能超过63个字符")
    private String alias;
    /**
     * 微信公众号AppId
     */
    @NotBlank(message = "微信公众号AppId 不能为空。")
    @Length(max = 63, message = "微信公众号AppId 最大长度不能超过63个字符")
    private String appid;
    /**
     * 微信公众号AppSecret
     */
    @NotBlank(message = "微信公众号AppSecret 不能为空。")
    @Length(max = 32, message = "微信公众号AppSecret 最大长度不能超过32个字符")
    private String appsecret;
    /**
     * 微信公众号EncodingAESKey
     */
    @NotBlank(message = "微信公众号EncodingAESKey 不能为空。")
    @Length(max = 63, message = "微信公众号EncodingAESKey 最大长度不能超过63个字符")
    private String encodingAesKey;
    /**
     * 授权方接口调用凭据
     */
    @Length(max = 255, message = "授权方接口调用凭据 最大长度不能超过255个字符")
    private String authorizerAccessToken;
    /**
     * 授权令牌过期时间
     */
    private Long authorizerAccessTokenExpiredTime;
    /**
     * 刷新令牌
     */
    @Length(max = 127, message = "刷新令牌 最大长度不能超过127个字符")
    private String authorizerRefreshToken;
    /**
     * 公众号授权给开发者的权限集（1-15，以逗号分开）
     */
    @Length(max = 127, message = "公众号授权给开发者的权限集 最大长度不能超过127个字符")
    private String permissionSet;
    /**
     * 公众号类型：0=订阅号，1=老帐号订阅号，2=服务号
     */
    private Byte serviceType;
    /**
     * 认证类型：-1=未认证，0=微信认证，1=新浪微博认证，2=腾讯微博认证……
     */
    private Byte verifyType;
    /**
     * 是否开通微信门店功能
     */
    @NotNull(message = "是否开通微信门店功能 不能为空。")
    private Boolean openStore;
    /**
     * 是否开通微信扫商品功能
     */
    @NotNull(message = "是否开通微信扫商品功能 不能为空。")
    private Boolean openScan;
    /**
     * 是否开通微信支付功能
     */
    @NotNull(message = "是否开通微信支付功能 不能为空。")
    private Boolean openPay;
    /**
     * 是否开通微信卡券功能
     */
    @NotNull(message = "是否开通微信卡券功能 不能为空。")
    private Boolean openCard;
    /**
     * 是否开通微信摇一摇功能
     */
    @NotNull(message = "是否开通微信摇一摇功能 不能为空。")
    private Boolean openShake;
    /**
     * 是否授权
     */
    @NotNull(message = "是否授权 不能为空。")
    private Boolean authorizered;
    /**
     * createTime
     */
    @NotNull(message = "createTime 不能为空。")
    private Date createTime;
    /**
     * updateTime
     */
    @NotNull(message = "updateTime 不能为空。")
    private Date updateTime;


    public void setId(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return this.id;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setIcon(String value) {
        this.icon = value;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setQrcodeUrl(String value) {
        this.qrcodeUrl = value;
    }

    public String getQrcodeUrl() {
        return this.qrcodeUrl;
    }

    public void setUserName(String value) {
        this.userName = value;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setAlias(String value) {
        this.alias = value;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAppid(String value) {
        this.appid = value;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppsecret(String value) {
        this.appsecret = value;
    }

    public String getAppsecret() {
        return this.appsecret;
    }

    public void setEncodingAesKey(String value) {
        this.encodingAesKey = value;
    }

    public String getEncodingAesKey() {
        return this.encodingAesKey;
    }

    public void setAuthorizerAccessToken(String value) {
        this.authorizerAccessToken = value;
    }

    public String getAuthorizerAccessToken() {
        return this.authorizerAccessToken;
    }

    public void setAuthorizerAccessTokenExpiredTime(Long value) {
        this.authorizerAccessTokenExpiredTime = value;
    }

    public Long getAuthorizerAccessTokenExpiredTime() {
        return this.authorizerAccessTokenExpiredTime;
    }

    public void setAuthorizerRefreshToken(String value) {
        this.authorizerRefreshToken = value;
    }

    public String getAuthorizerRefreshToken() {
        return this.authorizerRefreshToken;
    }

    public void setPermissionSet(String value) {
        this.permissionSet = value;
    }

    public String getPermissionSet() {
        return this.permissionSet;
    }

    public void setServiceType(Byte value) {
        this.serviceType = value;
    }

    public Byte getServiceType() {
        return this.serviceType;
    }

    public void setVerifyType(Byte value) {
        this.verifyType = value;
    }

    public Byte getVerifyType() {
        return this.verifyType;
    }

    public void setOpenStore(Boolean value) {
        this.openStore = value;
    }

    public Boolean getOpenStore() {
        return this.openStore;
    }

    public void setOpenScan(Boolean value) {
        this.openScan = value;
    }

    public Boolean getOpenScan() {
        return this.openScan;
    }

    public void setOpenPay(Boolean value) {
        this.openPay = value;
    }

    public Boolean getOpenPay() {
        return this.openPay;
    }

    public void setOpenCard(Boolean value) {
        this.openCard = value;
    }

    public Boolean getOpenCard() {
        return this.openCard;
    }

    public void setOpenShake(Boolean value) {
        this.openShake = value;
    }

    public Boolean getOpenShake() {
        return this.openShake;
    }

    public void setAuthorizered(Boolean value) {
        this.authorizered = value;
    }

    public Boolean getAuthorizered() {
        return this.authorizered;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setUpdateTime(Date value) {
        this.updateTime = value;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
}