<%--<jsp:include page="../header.jsp"/>--%>
<%--<div class="content management pd20">--%>
    <%--<div class="container clearfix">--%>
        <%--<jsp:include page="../left.jsp"/>--%>

        <%--<div class="main" data-menu="moderator">--%>
            <%--<div class="box clearfix">--%>
                <%--<div class="box-header">--%>
                    <%--<h1>版块管理</h1>--%>
                <%--</div>--%>
                <%--<div class="box-content">--%>
                    <%--<div class="box-section">--%>
                        <%--<div class="section-header">我管理的版块</div>--%>
                        <%--<div class="section-content">--%>
                            <%--<ul class="category-list">--%>
                                <%--&lt;%&ndash;<c:if test="${empty categories}">&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;<div class="doc-empty">&ndash;%&gt;--%>
                                        <%--&lt;%&ndash;您暂无可管理版块 前往<a href="${ctx}/user/moderatorApply">申请版主</a>&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</c:if>&ndash;%&gt;--%>
                                <%--<c:forEach items="${categories}" var="c">--%>
                                    <%--<li class="clearfix">--%>
                                        <%--<img class="category-icon" src="${bt:url(c.icon,"/files/" ,"assets/images/logo.jpg" )}" alt=""/>--%>
                                        <%--<div class="category-info">--%>
                                            <%--<div>--%>
                                                <%--<span class="category-name">${c.name}</span>--%>
                                            <%--</div>--%>
                                            <%--<div class=""><a href="${ctx}/user/moderators/${c.id}">管理</a></div>--%>
                                        <%--</div>--%>
                                    <%--</li>--%>
                                <%--</c:forEach>--%>
                            <%--</ul>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--<div class="sd rightside">--%>

        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<%--<jsp:include page="../footer.jsp"/>--%>