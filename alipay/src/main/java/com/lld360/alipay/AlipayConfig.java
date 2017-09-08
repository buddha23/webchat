package com.lld360.alipay;

public class AlipayConfig {

    //签名算法
    public static final String SIGN_TYPE = "RSA";
    
    public static final String SIGN_TYPE_RSA2 = "RSA2";
    //支付宝数据编码 目前支持 gbk 或 utf-8
    public static final String INPUT_CHARSET = "UTF-8";

    //支付宝支付方式
    public static final String PAYMENT_TYPE = "1";
    //商品类型 0 虚拟交易
    public static final String GOODS_TYPE = "0";
    //支付宝支付超时时间
    public static final String PAY_TIME = "1d";

    // 网页支付接口名
    public static String SERVICE = "create_direct_pay_by_user";

    //支付宝提供给商户的服务接入网关URL(新)
    public static final String WEB_GATEWAY = "https://mapi.alipay.com/gateway.do?";
    //wap 网关
    public static final String WAP_GATEWAY = "https://openapi.alipay.com/gateway.do";
    
    public static final String WAP_FROM = "json";

    private String alipayPid;
    private String alipayPrivateKey;
    private String alipayPublicKey;
    private String alipayNotifyUrl;
    private String alipayReturnUrl;

    private String alipayWapPrivateKey;
    private String alipayWapPublicKey;
    private String alipayWapAppId;
    private String alipayWapNotifyUrl;
    private String alipayWapReturnUrl;
    
    private String alipayTransferpid;
    private String alipayTransferprivateKey;
    private String alipayTransferpublicKey;

    public String getAlipayWapNotifyUrl() {
        return alipayWapNotifyUrl;
    }

    public void setAlipayWapNotifyUrl(String alipayWapNotifyUrl) {
        this.alipayWapNotifyUrl = alipayWapNotifyUrl;
    }

    public String getAlipayWapReturnUrl() {
        return alipayWapReturnUrl;
    }

    public void setAlipayWapReturnUrl(String alipayWapReturnUrl) {
        this.alipayWapReturnUrl = alipayWapReturnUrl;
    }

    public String getAlipayWapAppId() {
        return alipayWapAppId;
    }

    public void setAlipayWapAppId(String alipayWapAppId) {
        this.alipayWapAppId = alipayWapAppId;
    }

    public String getAlipayWapPrivateKey() {
        return alipayWapPrivateKey;
    }

    public void setAlipayWapPrivateKey(String alipayWapPrivateKey) {
        this.alipayWapPrivateKey = alipayWapPrivateKey;
    }

    public String getAlipayWapPublicKey() {
        return alipayWapPublicKey;
    }

    public void setAlipayWapPublicKey(String alipayWapPublicKey) {
        this.alipayWapPublicKey = alipayWapPublicKey;
    }

    public String getAlipayPid() {
        return alipayPid;
    }

    public void setAlipayPid(String alipayPid) {
        this.alipayPid = alipayPid;
    }

    public String getAlipayPrivateKey() {
        return alipayPrivateKey;
    }

    public void setAlipayPrivateKey(String alipayPrivateKey) {
        this.alipayPrivateKey = alipayPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getAlipayNotifyUrl() {
        return alipayNotifyUrl;
    }

    public void setAlipayNotifyUrl(String alipayNotifyUrl) {
        this.alipayNotifyUrl = alipayNotifyUrl;
    }

    public String getAlipayReturnUrl() {
        return alipayReturnUrl;
    }

    public void setAlipayReturnUrl(String alipayReturnUrl) {
        this.alipayReturnUrl = alipayReturnUrl;
    }

    public String getAlipayTransferpid() {
        return alipayTransferpid;
    }

    public void setAlipayTransferpid(String alipayTransferpid) {
        this.alipayTransferpid = alipayTransferpid;
    }

    public String getAlipayTransferprivateKey() {
		return alipayTransferprivateKey;
	}

	public void setAlipayTransferprivateKey(String alipayTransferprivateKey) {
		this.alipayTransferprivateKey = alipayTransferprivateKey;
	}

	public String getAlipayTransferpublicKey() {
		return alipayTransferpublicKey;
	}

	public void setAlipayTransferpublicKey(String alipayTransferpublicKey) {
		this.alipayTransferpublicKey = alipayTransferpublicKey;
	}
    
}
