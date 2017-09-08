package com.lld360.cnc.dto;

import com.lld360.cnc.model.Posts;

public class PostsDto extends Posts{

    private Byte type;

    private Integer amount;

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
}
