<jsp:include page="../header.jsp">
    <jsp:param name="title" value="用户协议"/>
</jsp:include>


<div class="content bg-white pd20">
    <div class="container clearfix">
        <ul class="leftnav">
            <%--<li class="node">关于我们</li>--%>
            <%--<li><a href="#">关于CNC数控文库</a></li>--%>
            <li><a href="${ctx}/auth/copyrightStatement">网站声明</a></li>
            <li><a href="${ctx}/auth/contactUs">联系我们</a></li>
            <%--<li><a href="#">招聘英才</a></li>--%>
            <%--<li><a href="#">协议/帮助</a></li>--%>
            <li class="node">用户协议</li>
            <li><a href="${ctx}/auth/scoreExplain">网站积分说明/常见问题</a></li>
            <li><a href="${ctx}/auth/aboutModerators">网站版主职责规范</a></li>
        </ul>
        <div class="content-right">
            <div class="about">
                <div class="about-header">
                    <h1>《${cw.title}》</h1>
                </div>
                <div class="margintop10">
                    ${cw.content}
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
