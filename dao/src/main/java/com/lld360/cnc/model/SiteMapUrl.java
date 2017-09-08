package com.lld360.cnc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Doc
 */
@JacksonXmlRootElement(localName = "xml")
public class SiteMapUrl implements Serializable {

    @JacksonXmlProperty(localName = "url")
    @JacksonXmlElementWrapper(localName = "urlset")
    private List<Url> list;

    public List<Url> getList() {
        return list;
    }

    public void setList(List<Url> list) {
        this.list = list;
    }

    public void addUrl(Url url) {
        if (null == list) {
            list = new ArrayList<>();
        }
        list.add(url);
    }

    @JacksonXmlRootElement(localName = "url")
    public static class Url {

        @JacksonXmlProperty(localName = "loc")
        private String addrs;

        @JacksonXmlProperty(localName = "priority")
        private Float priority;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
        @JacksonXmlProperty(localName = "lastmod")
        private Date time;

        @JacksonXmlProperty(localName = "changefreq")
        private String changeFreq;

        public String getAddrs() {
            return addrs;
        }

        public void setAddrs(String addrs) {
            this.addrs = addrs;
        }

        public Float getPriority() {
            return priority;
        }

        public void setPriority(Float priority) {
            this.priority = priority;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getChangeFreq() {
            return changeFreq;
        }

        public void setChangeFreq(String changeFreq) {
            this.changeFreq = changeFreq;
        }
    }


}