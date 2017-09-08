<jsp:include page="../header.jsp">
    <jsp:param name="css" value="${assets}/plugins/froalaEditor/css/froala_editor.pkgd.min.css"/>
    <jsp:param name="title" value="问题反馈"/>
</jsp:include>

<div class="content bg-white pd20">
    <div class="container">
        <div class="feedback">
            <div class="feedback-header">感谢您的意见反馈，我们会努力进步！</div>
            <ul class="feedback-tab margintop10 clearfix">
                <a href="${ctx}/user/mySuggests">我的反馈</a>
                <a class="current" href="javascript:;">看看别人的反馈</a>
            </ul>
            <ul class="feedback-list">
                <c:forEach items="${suggests.content}" var="s">
                    <li>
                        <img class="feedback-avatar" src="${bt:url(s.uAvatar,'files/', 'assets/images/default_avatar.png')}" alt="${fn:escapeXml(s.uNickname)}">
                        <div class="feedback-content">
                            <div class="feedback-name"><strong>${fn:escapeXml(s.uNickname)}</strong> 反馈</div>
                            <div class="feedback-msg">${s.content}</div>
                            <div class="feedback-date"><i class="fa fa-clock-o"></i>
                                <fmt:formatDate value="${s.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                            <c:if test="${!empty s.reply}">
                                <div class="feedback-admin">
                                    <div class="feedback-admin-name"><strong>管理员</strong> 回复</div>
                                    <div class="feedback-admin-reply">${s.reply}</div>
                                    <div class="feedback-admin-date"><i class="fa fa-clock-o"></i>
                                        <fmt:formatDate value="${s.replyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                </div>
                            </c:if>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <!-- 分页 -->
            <div class="clearfix">
                <div class="pagination fl clearfix">
                    <bt:paginationSite data="${suggests}" basePath="${ctx}/userSuggests"/>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>