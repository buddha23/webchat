<jsp:include page="../header.jsp"/>
<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main">
            <div class="box clearfix" style="min-height:440px;">
                <div class="box-header">
                    <h1><a href="${ctx}/user/center">个人中心</a> <i class="fa fa-angle-right breadcrumbs"></i> 消息</h1>
                </div>
                <div class="box-content">
                    <ul class="PM">
                        <c:forEach items="${messages.content}" var="m">
                            <li class="clearfix">
                                <div class="portrait"><img src="${assets}/images/sys_portrait.png" alt="大牛数控"></div>
                                <div class="PM-message">
                                    <div class="name">${m.title}</div>
                                    <div class="text">${fn:escapeXml(m.message)}</div>
                                    <div class="PM-meta clearfix">
                                        <div class="tips floatleft">
                                            <fmt:formatDate value="${m.createTime}" pattern="yyyy年MM月dd日 HH:mm"/>
                                        </div>
                                        <div class="operate floatright clearfix">
                                            <div class="operate-item">
                                                <a href="javascript:;" onclick="deleteM(${m.id})">删除</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>

                    <!-- 分页 -->
                    <div class="clearfix">
                        <div class="pagination fl margintop20 clearfix">
                            <bt:paginationSite data="${messages}" basePath="${ctx}/user/message"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<script type="text/javascript">
    function deleteM(id) {
        layer.confirm("确认删除?", "删除", function () {
            $.ajax({
                url: '${ctx}/user/deleteMessage',
                data: {id:id},
                type: 'post',
                success: function () {
                    layer.alert("删除成功", {icon: 1, time: 1000});
                    setTimeout(function () {
                        location.reload();
                    }, 1500);
                },
                error: function (r) {
                    console.log(r);
                    layer.alert("删除失败！", {icon: 2, time: 1000});
                }
            })
        })
    }
</script>

<jsp:include page="../footer.jsp"/>