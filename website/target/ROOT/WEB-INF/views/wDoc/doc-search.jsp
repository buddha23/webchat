<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(c1Obj.name)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(c1Obj.name)}"/>
</jsp:include>
<div class="content bg-white pd20">
    <div class="container">
        <form id="searchForm" action="${ctx}/doc" method="get" style="display: none;">
            <input type="hidden" id="content" name="content" value="${fn:escapeXml(param.content)}">
            <input type="hidden" id="docType" name="docType" value="${fn:escapeXml(param.docType)}">
            <input type="hidden" id="c1" name="c1" value="${fn:escapeXml(param.c1)}">
            <input type="hidden" id="c2" name="c2" value="${fn:escapeXml(param.c2)}">
        </form>
        <ul class="check-toolbar">
            <li class="clearfix">
                <div class="row-header">文档类型：</div>
                <div class="row-content clearfix">
                    <a class="check-item" href="javascript:void(0);" data-do="docType.all">全部</a>
                    <a class="check-item" href="javascript:void(0);" data-do="docType.pdf">pdf</a>
                    <a class="check-item" href="javascript:void(0);" data-do="docType.doc">doc</a>
                    <a class="check-item" href="javascript:void(0);" data-do="docType.xls">xls</a>
                    <a class="check-item" href="javascript:void(0);" data-do="docType.ppt">ppt</a>
                    <a class="check-item" href="javascript:void(0);" data-do="docType.txt">txt</a>
                </div>
            </li>
            <li class="clearfix">
                <div class="row-header">版块：</div>
                <div class="row-content clearfix">
                    <a class="check-item" href="javascript:void(0);" data-do="c1.all">全部</a>
                    <c:forEach items="${c1s}" var="c1">
                        <a class="check-item" href="javascript:void(0);"
                           data-do="c1.${c1.id}">${fn:escapeXml(c1.name)}</a>
                    </c:forEach>
                </div>
            </li>
            <c:if test="${!empty c2s}">
                <li class="fold clearfix">
                    <div class="row-header">分类：</div>
                    <div class="row-content clearfix">
                        <a class="check-item" href="javascript:void(0);" data-do="c2.all">全部</a>
                        <c:forEach items="${c2s}" var="c2">
                            <a class="check-item" href="javascript:void(0);"
                               data-do="c2.${c2.id}">${fn:escapeXml(c2.name)}</a>
                        </c:forEach>
                    </div>
                </li>
            </c:if>
        </ul>

        <div class="page-doc clearfix">
            <div class="doc-content clearfix">
                <div class="main">
                    <c:if test="${!empty param.content}">
                        <div class="search-word">搜索“<em>${fn:escapeXml(param.content)}</em>”相关的文档</div>
                    </c:if>

                    <ul class="doc-list">
                        <c:forEach items="${docs.content}" var="video">
                            <li>
                                <div class="doc-title ${video.fileType}">
                                    <a class="linkcolor linkline"
                                       href="${ctx}/doc/${video.id}">${fn:escapeXml(video.title)}</a>
                                </div>
                                <c:if test="${!empty video.summary}">
                                    <div class="doc-content">${fn:escapeXml(video.summary)}</div>
                                </c:if>
                                <div class="doc-meta clearfix">
                                    <div class="meta-item">${fn:toUpperCase(video.fileType)}文档</div>
                                    <div class="meta-item">阅读：${video.views}</div>
                                    <div class="meta-item">上传者：${video.user.name}</div>
                                    <div class="meta-item">牛人币：${video.costScore}</div>
                                </div>
                            </li>
                        </c:forEach>
                        <c:if test="${empty docs.content}">
                            <div class="doc-empty">
                                您查看的版块暂无内容
                            </div>
                        </c:if>
                    </ul>

                    <!-- 分页 -->
                    <div class="margintop20 clearfix">
                        <bt:paginationSite data="${docs}" basePath="${ctx}/doc"/>
                    </div>
                </div>

                <div class="aside">
                    <div class="aside-wrapper">
                        <div class="widget">
                            <div class="widget-header">热门文档</div>
                            <div class="widget-content">
                                <div class="hots-list">
                                    <c:forEach items="${hotDocs}" var="video" varStatus="docStatus">
                                        <dl class="clearfix">
                                            <dt>${docStatus.count}</dt>
                                            <dd>
                                                <a href="${ctx}/doc/${video.id}"
                                                   class="linkcolor linkover">
                                                    <div class="hots-title">${fn:escapeXml(video.title)}</div>
                                                </a>
                                                <div class="count">${video.views}人阅读</div>
                                            </dd>
                                        </dl>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <a href="#0" class="cd-top to-top">Top</a>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $(document).on('click', '.check-toolbar .row-content .check-item', function () {
            $(this).addClass("checked").siblings().removeClass('checked');
        });

        $(document).scroll(function(){
            if($(document).scrollTop() > $('.aside').offset().top){
                $('.aside .aside-wrapper').addClass('fixed').css('top',0);
            }else{
                $('.aside .aside-wrapper').removeClass('fixed').css('top',0);
            }

            if($(document).scrollTop() > ($('.doc-content').offset().top + $('.doc-content').height() - $('.aside .aside-wrapper').height())){
                $('.aside .aside-wrapper').removeClass('fixed').css('top', $('.doc-content').height() - $('.aside .aside-wrapper').height());
            }
        });

        var dtEl = $('[data-do="docType.${param.docType}"]');
        if (dtEl.length > 0) {
            dtEl.addClass('checked');
        } else {
            $('[data-do="docType.all"]').addClass('checked');
        }
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

        $('.nav .menu li a[data-nav="doc"]').addClass('current');
    });
</script>
<jsp:include page="../footer.jsp"/>