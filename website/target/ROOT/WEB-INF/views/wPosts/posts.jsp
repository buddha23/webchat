<jsp:include page="../header.jsp">
    <jsp:param name="title" value="数控问答，学数控有前途吗"/>
    <jsp:param name="keywords" value="学数控有前途吗,数控编程,数控技术工资"/>
    <jsp:param name="description" value="学数控有前途吗,数控工资多少,数控技术工资等问答"/>
    <jsp:param name="css" value="plugins/froalaEditor/css/froala_editor.pkgd.min.css"/>
</jsp:include>

<div class="content bg-grey pd20">
    <div class="container">
        <div class="page-qa clearfix">
            <div class="qa-aside">
                <div class="userbar">
                    <a class="question-btn" onclick="OBJ_loginBox.checkLogin(operate.publishPosts)">
                        <i class="fa fa-question-circle"></i> 我要提问</a>
                </div>
            </div>

            <div class="qa-main">
                <div class="qa-tabs">
                    <a class="tab-item act" href="${ctx}/post/">全部</a>
                    <a class="tab-item" href="javascript:;" onclick="OBJ_loginBox.checkLogin(operate.getMyPosts)">@我的提问</a>
                </div>

                <div class="qa-list">
                    <c:forEach items="${postsInfos.content}" var="postsInfo">
                        <a class="qa-list-item" href="${ctx}/post/${postsInfo.id}">
                            <div class="qa-item-profile">
                                <img class="avatar" src="${bt:url(postsInfo.userAvatar, 'files/', 'assets/images/default_avatar.png')}" alt="${fn:escapeXml(postsInfo.userNickname)}"/>
                            </div>
                            <div class="qa-item-info">
                                <div class="qa-title">
                                    <c:if test="${postsInfo.postsReward.state == 1 && postsInfo.postsReward.type == 1}">
                                        <div class="qa-reward qa-icon-offer">${postsInfo.postsReward.amount} 牛人币</div>
                                    </c:if>
                                    <c:if test="${postsInfo.postsReward.state == 1 && postsInfo.postsReward.type == 2}">
                                        <div class="qa-reward qa-icon-offer">${postsInfo.postsReward.amount} 积分</div>
                                    </c:if>
                                    <c:if test="${postsInfo.postsReward.state == 2 || postsInfo.postsReward.state == 4}">
                                        <div class="qa-reward qa-icon-end">已悬赏</div>
                                    </c:if>
                                    ${fn:escapeXml(postsInfo.title)}
                                </div>
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
                        <bt:paginationSite data="${postsInfos}" basePath="${ctx}/post"/>
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
