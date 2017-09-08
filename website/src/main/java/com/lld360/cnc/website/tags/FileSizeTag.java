package com.lld360.cnc.website.tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.DecimalFormat;

public class FileSizeTag extends SimpleTagSupport {

    private long size;

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public void doTag() throws IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        if (size < 1024) {
            out.print(size + "KB");
        } else if (size < 1024 * 1024) {
            out.print(new DecimalFormat("#.00").format(size / 1024.0) + "MB");
        } else {
            out.print(new DecimalFormat("#.00").format(size / (1024.0 * 1024.0)) + "GB");
        }
    }
}
