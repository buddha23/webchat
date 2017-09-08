package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

public class UserInvite implements Serializable {
    /*
    * 用户id
    * */
    private Long userId;
    /*
    * 用户邀请码(生成)
    * */
    private String inviteCode;
    /*
    * 是否为区域合伙人
    * */
    private Boolean isParter;
    /*
    * 邀请人ID
    * */
    private Long inviter;
    /*
    * 区域合伙人ID
    * */
    private Long parterId;
    /*
    * 邀请状态(可/禁用 邀请)
    * */
    private Byte state;
    /*
    * 创建时间(一般等于用户注册时间)
    * */
    private Date createTime;
    /*
    * 更新时间
    * */
    private Date updateTime;

    public UserInvite() {

    }

    public UserInvite(Long userId, Boolean isParter, Long inviter, Long parterId) {
        this.userId = userId;
        this.inviteCode = genInviteCode(userId);
        this.isParter = isParter;
        this.inviter = inviter;
        this.parterId = parterId;
        this.createTime = Calendar.getInstance(Locale.CHINA).getTime();
    }

    private String genInviteCode(Long id) {
        id += 1000000;
        String seed = "abcdefghijkmnpqrstuvwxyz23456789";
        StringBuilder str = new StringBuilder();
        Stack<Character> s = new Stack<>();
        int len = seed.length();
        do {
            s.push(seed.charAt((int) (id % len)));
            id /= len;
        } while (id != 0);
        while (!s.isEmpty()) {
            str.append(s.pop());
        }
        return str.toString();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Boolean getIsParter() {
        return isParter;
    }

    public void setIsParter(Boolean isParter) {
        this.isParter = isParter;
    }

    public Long getInviter() {
        return inviter;
    }

    public void setInviter(Long inviter) {
        this.inviter = inviter;
    }

    public Long getParterId() {
        return parterId;
    }

    public void setParterId(Long parterId) {
        this.parterId = parterId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
