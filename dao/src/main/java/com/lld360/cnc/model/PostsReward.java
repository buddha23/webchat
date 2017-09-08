package com.lld360.cnc.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

public class PostsReward implements Serializable {
    /*
    * 问题ID
    * */
    private Long postsId;

    /*
   * 悬赏类型 1:牛人币 2:积分
   * */
    @NotBlank(message = "类型 不能为空。")
    private Byte type;

    /*
   * 悬赏金额
   * */
    @NotBlank(message = "悬赏金额 不能为空。")
    private Integer amount;

    /*
   * 状态 1:未支付 2:已支付 3:已返还
   * */
    private Byte state;

    /*
   * 答案ID
   * */
    private Long commentId;

    /*
    * 完成时间
    * */
    private Date finishTime;

    public PostsReward() {

    }

    public PostsReward(Long postsId, Byte type, Integer amount, Byte state) {
        this.postsId = postsId;
        this.type = type;
        this.amount = amount;
        this.state = state;
    }

    public Long getPostsId() {
        return postsId;
    }

    public void setPostsId(Long postsId) {
        this.postsId = postsId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
