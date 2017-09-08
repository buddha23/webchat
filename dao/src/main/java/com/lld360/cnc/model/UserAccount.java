package com.lld360.cnc.model;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * UserAccount
 */
public class UserAccount implements Serializable {
    /**
     * id
     */
    @NotBlank(message = "id 不能为空。")
    private Long id;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id 不能为空。")
    private Long userId;
    /**
     * 用户账户
     */
    @NotBlank(message = "用户账户 不能为空。")
    @Length(max = 255, message = "用户账户 最大长度不能超过255个字符")
    private String account;
    /**
     * 账户类型 1:支付宝 2：银行 3：微信
     */
    @NotBlank(message = "账户类型 1:支付宝 2：银行 3：微信  不能为空。")
    private Byte accountType;
    /**
     * status
     */
    @NotBlank(message = "status 不能为空。")
    private Byte status;

    private String bankName;
    /**
     * 账户姓名
     */
    @NotBlank(message = "不能为空")
    private String accountName;

    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setUserId(Long value) {
        this.userId = value;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setAccount(String value) {
        this.account = value;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccountType(Byte value) {
        this.accountType = value;
    }

    public Byte getAccountType() {
        return this.accountType;
    }

    public void setStatus(Byte value) {
        this.status = value;
    }

    public Byte getStatus() {
        return this.status;
    }

    public enum TradeStatus {

        ALIPAY((byte) 1, "支付宝"), BANKCARD((byte) 2, "银行卡");

        private byte state;

        private String name;

        private TradeStatus(byte state, String name) {
            this.state = state;
            this.name = name;
        }

        public byte getState() {
            return state;
        }

        public String getName() {
            return name;
        }
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}