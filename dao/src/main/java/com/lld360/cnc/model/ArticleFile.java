package com.lld360.cnc.model;

import java.io.Serializable;

public class ArticleFile implements Serializable {

    private Long id;
    private String name;            //名称
    private String path;            //存放路径
    private Integer downloads;      //下载量

    public ArticleFile() {
    }

    public ArticleFile(String name, String path, Integer downloads) {
        this.name = name;
        this.path = path;
        this.downloads = downloads;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
}
