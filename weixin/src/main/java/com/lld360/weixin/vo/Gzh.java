package com.lld360.weixin.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 微信公众号的基本信息和授权信息
 */
public class Gzh implements Serializable {
    private String appId;
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private List<Integer> funcInfos;

    private String nickName;
    private String headImage;
    private Integer serviceType;
    private Integer verifyType;
    private String userName;
    private String alias;
    private Boolean openStore;
    private Boolean openScan;
    private Boolean openPay;
    private Boolean openCard;
    private Boolean openShake;
    private String qrcodeUrl;

    private Long expiredTime;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        this.expiredTime = Calendar.getInstance(Locale.CHINA).getTimeInMillis() + expiresIn * 1000;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public List<Integer> getFuncInfos() {
        return funcInfos;
    }

    public void setFuncInfos(List<Integer> funcInfos) {
        this.funcInfos = funcInfos;
    }

    public void addFuncInfo(int id) {
        if (id > 0 && id < 16) {
            if (this.funcInfos == null) {
                this.funcInfos = new ArrayList<>();
            }
            this.funcInfos.add(id);
        }
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Boolean getOpenStore() {
        return openStore;
    }

    public void setOpenStore(Boolean openStore) {
        this.openStore = openStore;
    }

    public Boolean getOpenScan() {
        return openScan;
    }

    public void setOpenScan(Boolean openScan) {
        this.openScan = openScan;
    }

    public Boolean getOpenPay() {
        return openPay;
    }

    public void setOpenPay(Boolean openPay) {
        this.openPay = openPay;
    }

    public Boolean getOpenCard() {
        return openCard;
    }

    public void setOpenCard(Boolean openCard) {
        this.openCard = openCard;
    }

    public Boolean getOpenShake() {
        return openShake;
    }

    public void setOpenShake(Boolean openShake) {
        this.openShake = openShake;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }
}
