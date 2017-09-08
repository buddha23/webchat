package com.lld360.cnc.core.enums;

public enum PayType {

    AliPay("alipay"),   // 支付宝
    WeiXin("wechatpay");   // 微信

    private final String value;

    PayType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
