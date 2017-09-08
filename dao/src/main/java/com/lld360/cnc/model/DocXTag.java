package com.lld360.cnc.model;

public class DocXTag {

    private Long docId;
    private Integer tagId;

    public DocXTag() {
    }

    public DocXTag(Long docId, Integer tagId) {
        this.docId = docId;
        this.tagId = tagId;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
