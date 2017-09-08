package com.lld360.cnc.website.tags;

import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class PaginationTagSite<T> extends SimpleTagSupport {

    private Page<T> data;
    private String basePath;
    private String styleClass;

    public void setData(Page<T> data) {
        this.data = data;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    @Override
    public void doTag() throws JspException, IOException {

        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        int currentPage = data.getNumber() + 1;
        int totalPages = data.getTotalPages();

        // 移除page参数
        String queryString = request.getQueryString();
        if (queryString != null) {
            queryString = queryString.replaceAll("(^|&)page=[^&^#]*", "").replaceAll("^[?&]|[?&]$", "");
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append("<div id=\"pagingDispatcher\" class=\"clearfix\"  style=\"align-content: center;\" >");
        if ( styleClass != null ){
            buffer.append("<div class=\" " + styleClass + "fl clearfix\" style=\"align-content: center\" >");
        }else{
            buffer.append("<div class=\"pagination fl clearfix\" style=\"align-content: center\" >");
        }
        //buffer.append("总数："+data.getTotalElements()+"  ");

        //上一页
        if (data.hasPrevious()) {
            buffer.append("<a class=\"paging\" href=\"?");
            if (queryString != null) buffer.append(queryString).append("&");
            buffer.append("page=").append(currentPage - 1).append("\" class=\"paging\">上一页</a>");
        }

        //当前页与上一页之间
        if (currentPage - 7 >= 1) {
            for (int i = 1; i <= 2; i++) {
                buffer.append("<a class=\"paging\" href=\"?");
                if (queryString != null) buffer.append(queryString).append("&");
                buffer.append("page=").append(i).append("\">").append(i).append("</a>");
            }
            buffer.append("<a class=\"paging ellipsis\">...</a>");
            for (int i = currentPage - 4; i < currentPage; i++) {
                buffer.append("<a class=\"paging\" href=\"?");
                if (queryString != null) buffer.append(queryString).append("&");
                buffer.append("page=").append(i).append("\">").append(i).append("</a>");
            }
        } else {
            for (int i = 1; i < currentPage; i++) {
                buffer.append("<a class=\"paging\" href=\"?");
                if (queryString != null) buffer.append(queryString).append("&");
                buffer.append("page=").append(i).append("\">").append(i).append("</a>");
            }
        }

        //当前页
        if (totalPages > 0) {//如果总页数为0不显示分页{
            buffer.append("<a class=\"paging current\">").append(currentPage).append("</a>");
        }
        //当前页与下一页之间
        if (currentPage + 7 <= totalPages) {
            for (int i = currentPage + 1; i <= currentPage + 4; i++) {
                buffer.append("<a class=\"paging\" href=\"?");
                if (queryString != null) buffer.append(queryString).append("&");
                buffer.append("page=").append(i).append("\">").append(i).append("</a>");
            }
            buffer.append("<a class=\"paging ellipsis\">...</a>");
            for (int i = totalPages - 1; i <= totalPages; i++) {
                buffer.append("<a class=\"paging\" href=\"?");
                if (queryString != null) buffer.append(queryString).append("&");
                buffer.append("page=").append(i).append("\">").append(i).append("</a>");
            }
        } else {
            for (int i = currentPage + 1; i <= totalPages; i++) {
                buffer.append("<a class=\"paging\" href=\"?");
                if (queryString != null) buffer.append(queryString).append("&");
                buffer.append("page=").append(i).append("\">").append(i).append("</a>");
            }
        }

        //下一页
        if (data.hasNext()) {
            buffer.append("<a href=\"?");
            if (queryString != null) buffer.append(queryString).append("&");
            buffer.append("page=").append(currentPage + 1).append("\" class=\"paging\">下一页</a>");
        }
        buffer.append("</div>");
        buffer.append("</div>");
        String result = buffer.toString();
        if(this.basePath != null) {
            result = result.replaceAll("href=\"", "href=\"" + basePath);
        }
        out.print(result);
    }
}
