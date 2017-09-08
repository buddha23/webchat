package com.lld360.cnc.wxmsg;

// 两参数通用
public class WxRegistMsgData extends WxMsgData {

    private DataTemp keyword1;

    private DataTemp keyword2;

    public DataTemp getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(DataTemp keyword1) {
        this.keyword1 = keyword1;
    }

    public DataTemp getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(DataTemp keyword2) {
        this.keyword2 = keyword2;
    }
}
