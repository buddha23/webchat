package com.lld360.cnc.dto;


import com.lld360.cnc.model.UserSuggest;

public class UserSuggestDto extends UserSuggest {
    private String uNickname;
    private String uAvatar;
    private String uMobile;

    public String getuNickname() {
        return uNickname;
    }

    public void setuNickname(String uNickname) {
        this.uNickname = uNickname;
    }

    public String getuAvatar() {
        return uAvatar;
    }

    public void setuAvatar(String uAvatar) {
        this.uAvatar = uAvatar;
    }

    public String getuMobile() {
        return uMobile;
    }

    public void setuMobile(String uMobile) {
        this.uMobile = uMobile;
    }
}
