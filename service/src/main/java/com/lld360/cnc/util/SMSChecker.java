package com.lld360.cnc.util;

import java.util.Date;

/**
 * Created by James on 2015-12-28.
 */
public class SMSChecker {
    private int sendCount;

    private Date firstSendTime;

    public Date getFirstSendTime() {
        return firstSendTime;
    }

    public void setFirstSendTime(Date firstSendTime) {
        this.firstSendTime = firstSendTime;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

}
