<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="doc">
            <div class="box">
                <div class="box-header">
                    <h1>文档管理</h1>
                </div>

                <div class="box-content">
                    <div class="user-panel clearfix">
                        <div class="panel-col clearfix">
                            <div class="col-main">
                                <div class="item-doc"></div>
                                <div class="text">
                                    <div class="item-title">我的文档（${fn:length(docs)}）</div>
                                    <a class="item-btn" href="${ctx}/doc/upload" onclick="return OBJ_bindAccount.checkBind(this)">上传</a>
                                </div>
                            </div>
                            <div class="col-intro">
                                <p>说明：上传数控相关操作手册，说明指南文档等</p>
                                <p class="tips">文档格式：pdf，doc，xls，ppt，txt</p>
                            </div>
                        </div>
                    </div>

                    <div class="box-pane margintop20">
                        <ul class="subnav clearfix">
                            <li class="current"><a href="${ctx}/doc/myUpload">我的上传</a></li>
                            <li><a href="${ctx}/doc/myDownload">我的下载</a></li>
                            <li><a href="${ctx}/doc/myFav">我的收藏</a></li>
                        </ul>
                        <div class="box-pane-content">
                            <table class="table-style2 dyntable">
                                <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>文档状态</th>
                                    <th>类型</th>
                                    <th>创建日期</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${docs}" var="video">
                                    <tr>
                                        <td>${fn:escapeXml(video.title)}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${video.state == 1}"><span
                                                        class="label label-green">已公开</span></c:when>
                                                <c:when test="${video.state == 2}"><span
                                                        class="label label-primary">解析中</span></c:when>
                                                <c:when test="${video.state == 3}"><span
                                                        class="label label-red">解析失败</span></c:when>
                                                <c:when test="${video.state == 4}"><span
                                                        class="label label-primary">待审核</span></c:when>
                                                <c:when test="${video.state == 5}"><span
                                                        class="label label-orange">审核不通过</span></c:when>
                                                <c:otherwise><span class="label lable-red">无效</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${video.fileType}</td>
                                        <td><fmt:formatDate value="${video.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td>
                                            <c:if test="${video.state == 1 || video.state == 4 || video.state == 5}">
                                                <a href="${ctx}/doc/${video.id}">查看</a>
                                            </c:if>
                                            <c:if test="${video.state != 1 && video.state != 2}">
                                                <a class="doDel" data-id="${video.id}"
                                                   href="javascript:void(0);">删除</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="sd rightside">

                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.dataTables.js"></script>
<%@include file="../footer.jsp" %>
<script>
    $(function () {
        //上传选择栏
        $('.fileGroup .fileSelect').click(function (e) {
            var $group = $(this).parent('.fileGroup');
            $group.find('.fileInput').click().change(function () {
                $group.find('.filePath').val($(this).val());
            })
        });

        function doDelete(ids) {
            layer.confirm("确定删除记录？", {btn: ['确定', '取消'], icon: 3}, function () {
                $.ajax({
                    url: ctx + "/doc/docDelete",
                    data: {ids: ids},
                    type: 'post',
                    success: function () {
                        location.reload();
                    },
                    error: function (r) {
                        try {
                            var rt = JSON.parse(r.responseText);
                            if (rt.data && rt.data.length > 0) {
                                layer.alert(rt.data.join('<br>'), '删除结果', function () {
                                    location.reload();
                                });
                            } else {
                                layer.alert(rt.message || '删除失败', function () {
                                    location.reload();
                                });
                            }
                        } catch (e) {
                            layer.alert("删除失败", function () {
                                location.reload();
                            });
                        }
                    }
                });
            });
        }

        $('.dataTable').on('click', '.doDel', function () {
            var id = $(this).attr('data-id');
            if (id) {
                doDelete([id]);
            }
        });

        $('.doBatchDel').click(function () {
            var cks = $('tbody :checkbox:checked');
            if (cks.length > 0) {
                var ids = [];
                cks.each(function () {
                    ids.push($(this).val());
                });
                if (ids.length > 0) {
                    doDelete(ids);
                }
            }
        });
    });
</script>