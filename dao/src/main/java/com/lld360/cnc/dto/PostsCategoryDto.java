package com.lld360.cnc.dto;

import com.lld360.cnc.model.Posts;
import com.lld360.cnc.model.PostsCategory;

public class PostsCategoryDto extends PostsCategory {

    private Integer postsNum;

    private Integer commentNum;

    private Posts lastPosts;

    private Long lastPostsId;

    private String lastPostsTitle;

    public Integer getPostsNum() {
        return postsNum;
    }

    public void setPostsNum(Integer postsNum) {
        this.postsNum = postsNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Posts getLastPosts() {
        return lastPosts;
    }

    public void setLastPosts(Posts lastPosts) {
        this.lastPosts = lastPosts;
    }

    public Long getLastPostsId() {
        return lastPostsId;
    }

    public void setLastPostsId(Long lastPostsId) {
        this.lastPostsId = lastPostsId;
    }

    public String getLastPostsTitle() {
        return lastPostsTitle;
    }

    public void setLastPostsTitle(String lastPostsTitle) {
        this.lastPostsTitle = lastPostsTitle;
    }
}
