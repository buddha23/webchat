<jsp:include page="header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(course.title)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(course.title)},数控学习资料,数控教程,数控技术交流学习中心"/>
    <jsp:param name="description" value="${fn:escapeXml(course.title)}-来自专业的数控教程,数控技术交流学习中心"/>
</jsp:include>

<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>

<div class="content bg-white pd20">
    <div class="container">
        <div class="page-tutorial">
            <div class="tutorial-detail clearfix">
                <div class="tutorial-header">
                    <h1>${course.title}</h1>
                    <div class="tutorial-meta"><i class="icon-clock"></i>
                        <fmt:formatDate value="${course.publishTime}" pattern="yyyy-MM-dd"/>
                    </div>
                </div>
                <div class="tutorial-content">
                    <div class="main">
                        ${course.content}
                    </div>
                    <div class="aside">
                        <div class="aside-wrapper">
                            <div class="widget">
                                <div class="widget-header">相关教程推荐</div>
                                <div class="widget-content">
                                    <c:forEach items="${courseList}" var="c">
                                        <div class="widget-item pic">
                                            <a class="linkcolor linkline" href="${ctx}/course/detail/${c.id}">${fn:escapeXml(c.title)}</a>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="widget margintop30">
                                <div class="widget-header">热门教程</div>
                                <div class="widget-content">
                                    <div class="hots-list">
                                        <c:forEach items="${hotCourse}" var="c" varStatus="docStatus">
                                            <dl class="clearfix">
                                                <dt>${docStatus.count}</dt>
                                                <dd>
                                                    <a href="${ctx}/course/detail/${c.id}" class="linkcolor linkover">
                                                        <div class="hots-title">${fn:escapeXml(c.title)}</div>
                                                    </a>
                                                    <div class="count">${c.visits}人学习</div>
                                                </dd>
                                            </dl>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
<script>
    $(function () {
        $('.nav .menu li a[data-nav="course"]').addClass('current');
    })
</script>