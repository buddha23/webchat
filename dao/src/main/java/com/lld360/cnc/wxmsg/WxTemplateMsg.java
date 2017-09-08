package com.lld360.cnc.wxmsg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;

@JacksonXmlRootElement(localName = "xml")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxTemplateMsg implements Serializable {

    // 目标用户
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "touser")
    private String touser; //用户OpenID

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "template_id")
    private String template_id; //模板消息ID

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "url")
    private String url; //URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）。

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "topcolor")
    private String topcolor; //标题颜色

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "data")
    private Object data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
