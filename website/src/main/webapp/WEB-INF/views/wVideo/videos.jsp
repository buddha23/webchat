<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(c1Obj.name)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(c1Obj.name)}"/>
</jsp:include>

<div class="content bg-white pd20">
    <div class="container">
        <div class="page-video">
            <div class="video-toolbar">
                <form id="searchForm" action="${ctx}/video/" method="get" style="display: none;">
                    <input type="hidden" id="c1" name="c1" value="${fn:escapeXml(param.c1)}">
                    <input type="hidden" id="c2" name="c2" value="${fn:escapeXml(param.c2)}">
                </form>
                <ul class="check-toolbar">
                    <li class="clearfix">
                        <div class="row-header">版块：</div>
                        <div class="row-content clearfix">
                            <a class="check-item" href="javascript:void(0);" data-do="c1.all">全部</a>
                            <c:forEach items="${c1s}" var="c1">
                                <a class="check-item" href="javascript:void(0);" data-do="c1.${c1.id}">${fn:escapeXml(c1.name)}</a>
                            </c:forEach>
                        </div>
                    </li>
                    <c:if test="${!empty c2s}">
                        <li class="fold clearfix">
                            <div class="row-header">分类：</div>
                            <div class="row-content clearfix">
                                <a class="check-item" href="javascript:void(0);" data-do="c2.all">全部</a>
                                <c:forEach items="${c2s}" var="c2">
                                    <a class="check-item" href="javascript:void(0);" data-do="c2.${c2.id}">${fn:escapeXml(c2.name)}</a>
                                </c:forEach>
                            </div>
                        </li>
                    </c:if>
                </ul>
            </div>

            <div class="video-list margintop20 clearfix">
                <c:if test="${empty videos.content}">
                    <div class="doc-empty">
                        您查看的版块暂无内容
                    </div>
                </c:if>
                <c:forEach items="${videos.content}" var="v">
                    <li class="clearfix">
                            <%--src="../images/demo/ex_1.jpg"--%>
                        <div class="video-preview">
                            <a href="${ctx}/video/intro/${v.id}">
                                <img src="${bt:url(v.cover,'files/','assets/images/demo/ex_1.jpg')}" alt="${fn:escapeXml(v.name)}">
                            </a>
                        </div>
                        <div class="video-info">
                            <h4><a class="linkcolor linkline" href="${ctx}/video/intro/${v.id}">${fn:escapeXml(v.name)}</a></h4>
                            <div class="video-meta clearfix">
                                <div class="video-date">
                                    <i class="icon-clock"></i><fmt:formatDate value="${v.createTime}" pattern="yyyy-MM-dd"/>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </div>

            <!-- 分页 -->
            <div class="clearfix">
                <div class="pagination fl clearfix">
                    <bt:paginationSite data="${videos}" basePath="${ctx}/video"/>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
<script type="text/javascript">
    $(function () {
        $(document).on('click', '.check-toolbar .row-content .check-item', function () {
            $(this).addClass("checked").siblings().removeClass('checked');
        });

        var c1El = $('[data-do="c1.${param.c1}"]');
        if (c1El.length > 0) {
            c1El.addClass('checked');
        } else {
            $('[data-do="c1.all"]').addClass('checked');
        }
        var c2El = $('[data-do="c2.${param.c2}"]');
        if (c2El.length > 0) {
            c2El.addClass('checked');
        } else {
            $('[data-do="c2.all"]').addClass('checked');
        }

        $('[data-do]').click(function () {
            var tv = $(this).attr('data-do').split('.');
            if (tv && tv.length === 2) {
                $('#' + tv[0]).val(tv[1] === 'all' ? '' : tv[1]);
                if (tv[0] === 'c1') {
                    $('#c2').val('');
                }
                $('#searchForm').submit();
            }
        });

        $('.nav .menu li a[data-nav="vdo"]').addClass('current');
    })
</script>