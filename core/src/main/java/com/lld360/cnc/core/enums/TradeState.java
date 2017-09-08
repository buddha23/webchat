package com.lld360.cnc.core.enums;

public enum TradeState {

    New("new"),   //新建
    Audited("audited"),   // 审核-提现
    Succ("succ"),
    Fail("fail");

    private final String value;

    TradeState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
