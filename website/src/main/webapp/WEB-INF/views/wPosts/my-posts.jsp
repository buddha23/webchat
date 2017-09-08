<jsp:include page="../header.jsp">
    <jsp:param name="css" value="plugins/froalaEditor/css/froala_editor.pkgd.min.css"/>
</jsp:include>


<div class="content bg-grey pd20">
    <div class="container">
        <div class="page-qa clearfix">
            <div class="qa-aside">
                <div class="userbar">
                    <a class="question-btn" onclick="OBJ_loginBox.checkLogin(operate.publishPosts())">
                        <i class="fa fa-question-circle"></i> 我要提问</a>
                </div>
            </div>

            <div class="qa-main">
                <div class="qa-tabs">
                    <a class="tab-item" href="${ctx}/post/">全部</a>
                    <a class="tab-item act" href="${ctx}/post/mypost">@我的提问</a>
                </div>

                <div class="qa-list">
                    <c:forEach items="${postsInfos.content}" var="postsInfo">
                        <a class="qa-list-item" href="${ctx}/post/${postsInfo.id}">
                            <div class="qa-item-profile">
                                <img class="avatar" src="${bt:url(postsInfo.userAvatar, 'files/', 'assets/images/default_avatar.png')}" alt="${fn:escapeXml(postsInfo.userNickname)}"/>
                            </div>
                            <div class="qa-item-info">
                                <div class="qa-title">${fn:escapeXml(postsInfo.title)}</div>
                                <div class="qa-meta">
                                    <span><i class="icon-user"></i> ${fn:escapeXml(postsInfo.userNickname)}</span>
                                    <span><i class="icon-calendar"></i>
                                        <fmt:formatDate value="${postsInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                    <span><i class="icon-eye"></i> ${postsInfo.views}</span>
                                    <span><i class="icon-bubble"></i> ${postsInfo.commentsNum}</span>
                                </div>
                                <div class="qa-content">
                                    <p>${bt:html2Text(postsInfo.content)}</p>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>

                <!-- 分页 -->
                <div class="qa-page clearfix">
                    <div class="pagination fl clearfix">
                        <%--贝师傅,干掉下面这个--%>
                        <c:if test="${postsInfos.totalElements==0}">
                            <center><img src="${assets}/images/11111.jpg" alt="大牛数控"/></center>
                        </c:if>
                        <bt:paginationSite data="${postsInfos}" basePath="${ctx}/post/mypost"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="_editor.jsp"/>

<jsp:include page="../footer.jsp"/>

<script type="text/javascript">
    $('.nav .menu li a[data-nav="qa"]').addClass('current');
</script>