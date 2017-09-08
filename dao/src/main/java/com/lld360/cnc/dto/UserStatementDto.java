package com.lld360.cnc.dto;

import com.lld360.cnc.model.UserStatement;

public class UserStatementDto extends UserStatement{
    private String nickName;
    private String mobile;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
