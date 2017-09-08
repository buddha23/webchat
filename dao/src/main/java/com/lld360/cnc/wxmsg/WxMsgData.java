package com.lld360.cnc.wxmsg;

import java.io.Serializable;

public class WxMsgData implements Serializable {

    private DataTemp first;

    private DataTemp remark;

    public DataTemp getFirst() {
        return first;
    }

    public void setFirst(DataTemp first) {
        this.first = first;
    }

    public DataTemp getRemark() {
        return remark;
    }

    public void setRemark(DataTemp remark) {
        this.remark = remark;
    }
}
