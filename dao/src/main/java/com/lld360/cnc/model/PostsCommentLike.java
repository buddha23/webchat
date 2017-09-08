package com.lld360.cnc.model;

import java.io.Serializable;

public class PostsCommentLike implements Serializable {

    private Long commentId;

    private Long userId;

    public PostsCommentLike() {

    }

    public PostsCommentLike(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
