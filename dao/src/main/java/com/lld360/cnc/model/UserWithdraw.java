package com.lld360.cnc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * UserWithdraw
 */
public class UserWithdraw implements Serializable {
    /**
     * id
     */
    @NotBlank(message = "id 不能为空。")
    private Long id;
    /**
     * 用户id
     */
    @NotBlank(message = "用户 不能为空。")
    private User user;

    private Long userId;


    /**
     * 交易金额
     */
    @NotBlank(message = "交易金额 不能为空。")
    private BigDecimal tradeAmount;

    private Integer tradeScore;
    /**
     * 创建时间
     */
    @NotBlank(message = "创建时间 不能为空。")
    private Date createTime;
    /**
     * 交易更新时间
     */
    private Date updateTime;
    /**
     * 交易状态
     */
    @NotBlank(message = "交易状态 不能为空。交易状态 1:审核中 2：处理中 3：成功 4：失败")
    private Byte tradeStatus;
    /**
     * 备注
     */
    @Length(max = 255, message = "备注 最大长度不能超过255个字符")
    private String tradeRemark;
    /**
     * innerTradeNo
     */
    @NotBlank(message = "innerTradeNo 不能为空。")
    @Length(max = 255, message = "innerTradeNo 最大长度不能超过255个字符")
    private String innerTradeNo;
    /**
     * 交易编号
     */
    @NotBlank(message = "交易编号 不能为空。")
    @Length(max = 255, message = "交易编号 最大长度不能超过255个字符")
    private String tradeNo;
    /**
     * 管理员id
     */
    private Admin withdrawDealer;

    private Integer dealer;
    /**
     * 提现账户
     */
    private UserAccount userAccount;
    private Long accountId;

    private String failReason;
    /**
     * successTime
     */
    private Date successTime;

    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public Integer getDealer() {
        return dealer;
    }

    public void setDealer(Integer dealer) {
        this.dealer = dealer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTradeAmount(BigDecimal value) {
        this.tradeAmount = value;
    }

    public BigDecimal getTradeAmount() {
        return this.tradeAmount;
    }

    public Integer getTradeScore() {
        return tradeScore;
    }

    public void setTradeScore(Integer tradeScore) {
        this.tradeScore = tradeScore;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUpdateTime(Date value) {
        this.updateTime = value;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setTradeStatus(Byte value) {
        this.tradeStatus = value;
    }

    public Byte getTradeStatus() {
        return this.tradeStatus;
    }

    public void setTradeRemark(String value) {
        this.tradeRemark = value;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTradeRemark() {
        return this.tradeRemark;
    }

    public void setInnerTradeNo(String value) {
        this.innerTradeNo = value;
    }

    public String getInnerTradeNo() {
        return this.innerTradeNo;
    }

    public void setTradeNo(String value) {
        this.tradeNo = value;
    }

    public String getTradeNo() {
        return this.tradeNo;
    }

    public Admin getWithdrawDealer() {
        return withdrawDealer;
    }

    public void setWithdrawDealer(Admin withdrawDealer) {
        this.withdrawDealer = withdrawDealer;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setSuccessTime(Date value) {
        this.successTime = value;
    }

    public Date getSuccessTime() {
        return this.successTime;
    }

    //交易状态 不能为空。交易状态 1:审核中 2：处理中 3：成功 4：失败 5、审核失败
    public enum TradeStatus {

        EXAMINE((byte) 1, "待审核"), HANDLE((byte) 2, "处理中"), SUCCESS((byte) 3, "成功"),
        FAIL((byte) 4, "失败"), AUDITFAIL((byte) 5, "审核失败"), SERUNAVAILABLE((byte) 6, "服务不可用");

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
}