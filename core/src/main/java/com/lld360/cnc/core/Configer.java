package com.lld360.cnc.core;

import com.taobao.api.TaobaoClient;

/**
 * Author: dhc
 * Date: 2016-06-29 11:32
 */
public class Configer {
    private String env;

    private String appUrl;

    private String wxGzhToken;
    private String wxGzhAppID;
    private String wxGzhAppSecret;
    private String wxGzhEncodingAESKey;

    private String wxSelfGzhs;

    private String wxComponenAppId;
    private String wxComponenAppSecret;
    private String wxComponenAppToken;
    private String wxComponenAppDecryptKey;

    private String fileBasePath;

    private TaobaoClient taobaoSmsClient;
    private Integer smsExpiredTime;

    private String wxAccountAppid;
    private String wxAccountScrect;

    private String qqAccountAppid;
    private String qqAccountScrect;

    private String wxFwAppid;
    private String wxFwScret;

    private Integer rechargeExchangeRate;

    public Integer getRechargeExchangeRate() {
        return rechargeExchangeRate;
    }

    public void setRechargeExchangeRate(Integer rechargeExchangeRate) {
        this.rechargeExchangeRate = rechargeExchangeRate;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getWxGzhToken() {
        return wxGzhToken;
    }

    public void setWxGzhToken(String wxGzhToken) {
        this.wxGzhToken = wxGzhToken;
    }

    public String getWxGzhAppID() {
        return wxGzhAppID;
    }

    public void setWxGzhAppID(String wxGzhAppID) {
        this.wxGzhAppID = wxGzhAppID;
    }

    public String getWxGzhAppSecret() {
        return wxGzhAppSecret;
    }

    public void setWxGzhAppSecret(String wxGzhAppSecret) {
        this.wxGzhAppSecret = wxGzhAppSecret;
    }

    public String getWxComponenAppId() {
        return wxComponenAppId;
    }

    public void setWxComponenAppId(String wxComponenAppId) {
        this.wxComponenAppId = wxComponenAppId;
    }

    public String getWxComponenAppSecret() {
        return wxComponenAppSecret;
    }

    public void setWxComponenAppSecret(String wxComponenAppSecret) {
        this.wxComponenAppSecret = wxComponenAppSecret;
    }

    public String getWxComponenAppToken() {
        return wxComponenAppToken;
    }

    public void setWxComponenAppToken(String wxComponenAppToken) {
        this.wxComponenAppToken = wxComponenAppToken;
    }

    public String getWxComponenAppDecryptKey() {
        return wxComponenAppDecryptKey;
    }

    public void setWxComponenAppDecryptKey(String wxComponenAppDecryptKey) {
        this.wxComponenAppDecryptKey = wxComponenAppDecryptKey;
    }

    public String getWxGzhEncodingAESKey() {
        return wxGzhEncodingAESKey;
    }

    public void setWxGzhEncodingAESKey(String wxGzhEncodingAESKey) {
        this.wxGzhEncodingAESKey = wxGzhEncodingAESKey;
    }

    public String getWxSelfGzhs() {
        return wxSelfGzhs;
    }

    public void setWxSelfGzhs(String wxSelfGzhs) {
        this.wxSelfGzhs = wxSelfGzhs;
    }

    public String getFileBasePath() {
        return fileBasePath;
    }

    public void setFileBasePath(String fileBasePath) {
        this.fileBasePath = fileBasePath;
    }

    public TaobaoClient getTaobaoSmsClient() {
        return taobaoSmsClient;
    }

    public void setTaobaoSmsClient(TaobaoClient taobaoSmsClient) {
        this.taobaoSmsClient = taobaoSmsClient;
    }

    public Integer getSmsExpiredTime() {
        return smsExpiredTime;
    }

    public void setSmsExpiredTime(Integer smsExpiredTime) {
        this.smsExpiredTime = smsExpiredTime;
    }

    public String getWxAccountAppid() {
        return wxAccountAppid;
    }

    public void setWxAccountAppid(String wxAccountAppid) {
        this.wxAccountAppid = wxAccountAppid;
    }

    public String getWxAccountScrect() {
        return wxAccountScrect;
    }

    public void setWxAccountScrect(String wxAccountScrect) {
        this.wxAccountScrect = wxAccountScrect;
    }

    public String getQqAccountAppid() {
        return qqAccountAppid;
    }

    public void setQqAccountAppid(String qqAccountAppid) {
        this.qqAccountAppid = qqAccountAppid;
    }

    public String getQqAccountScrect() {
        return qqAccountScrect;
    }

    public void setQqAccountScrect(String qqAccountScrect) {
        this.qqAccountScrect = qqAccountScrect;
    }

    public String getWxFwAppid() {
        return wxFwAppid;
    }

    public void setWxFwAppid(String wxFwAppid) {
        this.wxFwAppid = wxFwAppid;
    }

    public String getWxFwScret() {
        return wxFwScret;
    }

    public void setWxFwScret(String wxFwScret) {
        this.wxFwScret = wxFwScret;
    }
}
