package com.lld360.cnc.website.tags;


import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class DurationToString extends SimpleTagSupport {

    private double duration;

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public void doTag() throws IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        int mins = (int) duration / 60;
        int hours = mins / 60;
        if (duration < 60) {
            out.print(duration + "秒");
        } else if (mins < 60) {
            out.print(mins + "分" + (duration - mins * 60) + "秒");
        } else {
            out.print(hours + "小时" + (mins - hours * 60) + "分" + (duration - mins * 60) + "秒");
        }
    }
}
