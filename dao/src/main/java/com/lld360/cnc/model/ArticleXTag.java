package com.lld360.cnc.model;

public class ArticleXTag {

    private Long articleId;
    private Long tagId;

    public ArticleXTag() {
    }

    public ArticleXTag(Long articleId, Long tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
