<jsp:include page="header.jsp">
    <jsp:param name="title" value="数控教程,数控学习资料"/>
    <jsp:param name="keywords" value="数控学习资料,数控教程,数控技术交流学习中心"/>
    <jsp:param name="description" value="专业的数控教程,数控技术交流学习中心"/>
</jsp:include>

<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>
<div class="content bg-white pd20">
    <div class="container">
        <div class="page-tutorial">
            <c:if test="${courses!=null}">
                <ul class="tutorial-list clearfix">
                    <c:forEach items="${courses.content}" var="course">
                        <li class="clearfix">
                            <div class="tutorial-frame">
                                <div class="tutorial-read"><a href="${ctx}/course/detail/${course.id}">阅读</a></div>
                                <div class="tutorial-praise">
                                    <c:if test="${course.visits>99}"><div class="num">99+</div></c:if>
                                    <c:if test="${course.visits<99}"><div class="num">${course.visits}</div></c:if>
                                </div>
                            </div>
                            <div class="tutorial-preview"><a href="${ctx}/course/detail/${course.id}">
                                <img src="${files}/${course.imgUrl}" alt="${fn:escapeXml(course.title)}"></a></div>
                            <div class="tutorial-info">
                                <h4><a class="linkcolor linkline" href="${ctx}/course/detail/${course.id}">${fn:escapeXml(course.title)}</a></h4>
                                <div class="tutorial-meta clearfix">
                                    <div class="tutorial-date">
                                        <i class="icon-clock"></i> <fmt:formatDate value="${course.publishTime}" pattern="yyyy-MM-dd"/>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>

                <!-- 分页 -->
                <div class="clearfix">
                    <bt:paginationSite data="${courses}" basePath="${ctx}/course"/>
                </div>
            </c:if>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
<script>
    $(function () {
        $('.nav .menu li a[data-nav="course"]').addClass('current');
    })
</script>
