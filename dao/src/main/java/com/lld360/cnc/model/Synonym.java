package com.lld360.cnc.model;

import java.io.Serializable;

/**
 * 同义词表
 */
public class Synonym implements Serializable{
    /**
     * ID
     */
    private Integer id;
    /**
     * 同义词
     */
    private String words;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
