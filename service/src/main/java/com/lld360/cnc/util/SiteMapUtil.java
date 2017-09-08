package com.lld360.cnc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lld360.cnc.model.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by clb on 2016/11/22.
 */
public class SiteMapUtil {
    /**
     * 首页 daily、1
     */
    private static final String URL_HOME = "http://www.d6sk.com/";
    /**
     * 文档：首页 daily、1 ，详情+id weekly、0.9
     */
    private static final String URL_DOC_HOME = URL_HOME + "doc";
    private static final String URL_DOC_DETAIL = URL_HOME + "doc/";
    private static final String URL_DOC_CATEGORY = URL_HOME + "doc?c1=";

    /**
     * 精品教程：首页 daily、1 ，详情+id weekly、0.9
     */
    private static final String URL_COURSE_HOME = URL_HOME + "course";
    private static final String URL_COURSE_DETAIL = URL_HOME + "course/detail/";
    /**
     * 问答：首页 daily、1 ，详情+id weekly、0.9
     */
    private static final String URL_POST_HOME = URL_HOME + "post/";
    private static final String URL_POST_DETAIL = URL_HOME + "post/";

    private static final String PRIORITY_HOME = "daily";
    private static final String PRIORITY_DETAIL = "weekly";
    private static final float CHANGEFREQ_HOME = 1.0f;
    private static final float CHANGEFREQ_DETAIL = 0.9f;


    public static String buildSiteMapXmlStr(List<Doc> docList, List<DocCategory> docCategoryList, List<Course> courseList, List<Posts> postsList) {
        SiteMapUrl siteMapUrl = new SiteMapUrl();
        Date date = new Date();

        //首页
        siteMapUrl.addUrl(buildSiteMapUrl(URL_HOME, CHANGEFREQ_HOME, date, PRIORITY_HOME));

        //问答
        siteMapUrl.addUrl(buildSiteMapUrl(URL_POST_HOME, CHANGEFREQ_HOME, date, PRIORITY_HOME));
        if (null != postsList) {
            postsList.forEach(posts -> siteMapUrl.addUrl(buildSiteMapUrl(posts)));
        }

        //文档
        siteMapUrl.addUrl(buildSiteMapUrl(URL_DOC_HOME, CHANGEFREQ_HOME, date, PRIORITY_HOME));
        //category
        if (null != docCategoryList) {
            docCategoryList.forEach(docCategory -> siteMapUrl.addUrl(buildSiteMapUrl(docCategory)));
        }
        //doc
        if (null != docList) {
            docList.forEach(doc -> siteMapUrl.addUrl(buildSiteMapUrl(doc)));
        }
        //精品教程
        siteMapUrl.addUrl(buildSiteMapUrl(URL_COURSE_HOME, CHANGEFREQ_HOME, date, PRIORITY_HOME));
        if (null != courseList) {
            courseList.forEach(course -> siteMapUrl.addUrl(buildSiteMapUrl(course)));
        }


        return buildXMLStr(siteMapUrl);
    }

    private static SiteMapUrl.Url buildSiteMapUrl(Doc doc) {
        return buildSiteMapUrl(URL_DOC_DETAIL + doc.getId(), CHANGEFREQ_DETAIL, doc.getCreateTime(), PRIORITY_DETAIL);
    }

    private static SiteMapUrl.Url buildSiteMapUrl(DocCategory docCategory) {
        return buildSiteMapUrl(URL_DOC_CATEGORY + docCategory.getId(), CHANGEFREQ_DETAIL, null, PRIORITY_HOME);
    }

    private static SiteMapUrl.Url buildSiteMapUrl(Course course) {
        return buildSiteMapUrl(URL_COURSE_DETAIL + course.getId(), CHANGEFREQ_DETAIL, course.getPublishTime(), PRIORITY_DETAIL);
    }

    private static SiteMapUrl.Url buildSiteMapUrl(Posts posts) {
        return buildSiteMapUrl(URL_POST_DETAIL + posts.getId(), CHANGEFREQ_DETAIL, posts.getCreateTime(), PRIORITY_DETAIL);
    }

    private static SiteMapUrl.Url buildSiteMapUrl(String addrs, float priority, Date date, String changefreq) {
        SiteMapUrl.Url url = new SiteMapUrl.Url();
        url.setAddrs(addrs);
        url.setPriority(priority);
        if(date!=null){
        url.setTime(date);
        }
        url.setChangeFreq(changefreq);
        return url;
    }

    private static String buildXMLStr(SiteMapUrl siteMapUrl) {
        String str = null;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            str = xmlMapper.writeValueAsString(siteMapUrl);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (!StringUtils.isEmpty(str)) {
            int start = str.indexOf("<urlset>");
            int end = str.lastIndexOf("</urlset>") + "</urlset>".length();
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + str.substring(start, end);
        }
        return null;
    }

}
