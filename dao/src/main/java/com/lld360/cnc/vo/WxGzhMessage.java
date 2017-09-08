package com.lld360.cnc.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: dhc
 * Date: 2016-06-30 17:09
 */
@JacksonXmlRootElement(localName = "xml")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxGzhMessage implements Serializable {
    // 消息ID
    @JacksonXmlProperty(localName = "MsgId")
    private Long msgId;

    // 目标用户
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;

    // 发送用户
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;

    // 消息类型
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;

    // 创建时间
    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;

    // 通用多媒体ID
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "MediaId")
    private String mediaId;

    // 文本消息 ▼
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Content")
    private String content;

    // 图片消息 ▼
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "PicUrl")
    private String picUrl;

    // 语音消息 ▼
    // 媒体格式
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Format")
    private String format;

    // 语音识别结果，UTF8编码
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Recognition")
    private String recognition;

    // 视频消息 ▼
    // 视频缩略图
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "ThumbMediaId")
    private String thumbMediaId;

    // 位置消息 ▼
    @JacksonXmlProperty(localName = "Location_X")
    private Double locationX;

    @JacksonXmlProperty(localName = "Location_Y")
    private Double locationY;

    @JacksonXmlProperty(localName = "Scale")
    private Integer scale;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Label")
    private String label;

    // 链接消息 ▼
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Title")
    private String title;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Description")
    private String description;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Url")
    private String url;

    // 事件消息 ▼
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Event")
    private String event;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "EventKey")
    private String eventKey;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Ticket")
    private String ticket;

    // 向微信发送消息时的对象 ▼
    // 发送的图片对象
    @JacksonXmlProperty(localName = "Image")
    private Image image;

    // 发送的声音对象
    @JacksonXmlProperty(localName = "Voice")
    private Voice voice;

    // 发送的视频对象
    @JacksonXmlProperty(localName = "Video")
    private Video video;

    // 发送的音乐对象
    @JacksonXmlProperty(localName = "Music")
    private Music music;

    // 发送的图文列表
    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(localName = "Articles")
    private List<Article> articles;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public Double getLocationX() {
        return locationX;
    }

    public void setLocationX(Double locationX) {
        this.locationX = locationX;
    }

    public Double getLocationY() {
        return locationY;
    }

    public void setLocationY(Double locationY) {
        this.locationY = locationY;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String mediaId) {
        if (this.image == null)
            this.image = new Image();
        this.image.setMediaId(mediaId);
    }

    public Voice getVoice() {
        return voice;
    }

    public void setVoice(String mediaId) {
        if (this.voice == null)
            this.voice = new Voice();
        this.voice.setMediaId(mediaId);
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(String mediaId, String title, String description) {
        if (this.video == null) {
            this.video = new Video();
        }
        video.setMediaId(mediaId);
        video.setTitle(title);
        video.setDescription(description);
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(String title, String description, String musicUrl, String hQMusicUrl, String thumbMediaId) {
        if (this.music == null) {
            this.music = new Music();
        }
        this.music.setTitle(title);
        this.music.setDescription(description);
        this.music.setMusicUrl(musicUrl);
        this.music.sethQMusicUrl(hQMusicUrl);
        this.music.setThumbMediaId(thumbMediaId);
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void addArticle(String title, String description, String picUrl, String url) {
        if (this.articles == null) {
            this.articles = new ArrayList<>();
        }
        this.articles.add(new Article(title, description, picUrl, url));
    }

    @JacksonXmlProperty(localName = "ArticleCount")
    public Integer getArticleCount() {
        return this.articles == null ? null : this.articles.size();
    }

    public static class Image {
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "MediaId")
        private String mediaId;

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }
    }

    public static class Voice {
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "MediaId")
        private String mediaId;

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }
    }

    public static class Video {
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "MediaId")
        private String mediaId;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "Title")
        private String title;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "Description")
        private String description;

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class Music {
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "Title")
        private String title;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "Description")
        private String description;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "MusicUrl")
        private String musicUrl;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "HQMusicUrl")
        private String hQMusicUrl;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "ThumbMediaId")
        private String thumbMediaId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMusicUrl() {
            return musicUrl;
        }

        public void setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
        }

        public String gethQMusicUrl() {
            return hQMusicUrl;
        }

        public void sethQMusicUrl(String hQMusicUrl) {
            this.hQMusicUrl = hQMusicUrl;
        }

        public String getThumbMediaId() {
            return thumbMediaId;
        }

        public void setThumbMediaId(String thumbMediaId) {
            this.thumbMediaId = thumbMediaId;
        }
    }

    @JacksonXmlRootElement(localName = "item")
    public static class Article {
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "Title")
        private String title;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "Description")
        private String description;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "PicUrl")
        private String picUrl;
        @JacksonXmlCData
        @JacksonXmlProperty(localName = "Url")
        private String url;

        public Article() {
        }

        public Article(String title, String description, String picUrl, String url) {
            this.title = title;
            this.description = description;
            this.picUrl = picUrl;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
