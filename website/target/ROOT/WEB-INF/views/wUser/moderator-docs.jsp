<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="moderator">
            <div class="box clearfix">
                <div class="box-header">
                    <h1><a href="${ctx}/user/moderators">版块管理</a>
                        <i class="fa fa-angle-right breadcrumbs"></i> ${category.name}</h1>
                </div>
                <div class="box-content">
                    <div class="box-section" style="margin-bottom: 20px;">
                        <div class="section-content">
                            <table class="table-style2">
                                <thead>
                                <tr>
                                    <th>文档</th>
                                    <th>类型</th>
                                    <th>上传者</th>
                                    <th>上传日期</th>
                                    <th>积分</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${docs.content}" var="video">
                                    <tr<c:if test="${video.state == 4}"> style="background:#ECE2E2;"</c:if>>
                                        <td>
                                            <a href="${ctx}/doc/${video.id}" target="_blank">${fn:escapeXml(video.title)}</a>
                                        </td>
                                        <td>${fn:toUpperCase(video.fileType)}</td>
                                        <td>${video.user.name}</td>
                                        <td><fmt:formatDate value="${video.createTime}" pattern="yyyy-MM-dd"/></td>
                                        <td>${video.costScore}</td>
                                        <td>
                                            <a href="${ctx}/doc/${video.id}" target="_blank">查看</a>
                                            <c:if test="${video.state == 4}">
                                                <a class="marginleft10 doReview" data-id="${video.id}"
                                                   href="javascript:void(0);">审核</a>
                                            </c:if>
                                            <a class="moveTo marginleft10" data-id="${video.id}"
                                               href="javascript:void(0);">移动</a>
                                            <c:if test="${video.downloads == 0}">
                                                <a class="marginleft10 doDel" data-id="${video.id}"
                                                   href="javascript:void(0);">删除</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <bt:paginationSite data="${docs}" basePath="${ctx}/user/moderators/${c1}"/>
                    <c:if test="${docs.totalElements == 0}">
                        <p>★ 当前没有需要审核的文档。</p>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>

<div id="categoryDialog" class="category-dialog clearfix" style="padding: 20px; width: 520px; display: none;">
    <div class="dialog-left">
        <h6>主版块</h6>
        <ul class="category-list margintop10" style="display: block;"></ul>
    </div>

    <div class="dialog-right">
        <h6>子版块</h6>
        <ul class="category-sub margintop10" style="display: block;"></ul>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.dataTables.js"></script>
<script type="text/javascript">
    $(function () {
        $('.doDel').click(function () {
            var id = $(this).attr('data-id');
            if (!id) {
                return;
            }

            layer.confirm("确定删除该文档吗？", {btn: ['确定', '取消'], icon: 3}, function () {
                $.ajax({
                    url: ctx + "/doc/docDelete",
                    data: {ids: [id]},
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
        });

        var categories;

        function setSubCategories(fid) {
            $('.category-sub').empty();
            categories.forEach(function (f) {
                if (f.id == fid) {
                    f.children.forEach(function (c) {
                        $('.category-sub').append('<li data-cid="' + c.id + '">'
                            + '<div class="wrapper clearfix">'
                            + '<div class="category-info">'
                            + '<div class="name">' + c.name + '</div></div></div></li>');
                    });
                    return false;
                }
            });
        }

        $.get(ctx + '/i/doc_categories', function (data) {
            categories = data;
            var first = true;
            categories.forEach(function (f) {
                $('.category-list').append('<li data-fid="' + f.id + '">'
                    + '<div class="wrapper clearfix">'
                    + '<img src="' + (f.icon ? '/files/' + f.icon : '/assets/images/logo_footer.png') + '">'
                    + '<div class="category-info">'
                    + '<div class="name">' + f.name + '</div></div></div></li>');

                if (first) {
                    setSubCategories(f.id);
                    first = false;
                }
            });
        });

        $('#categoryDialog').on('click', 'li', function () {
            $(this).addClass('current').siblings().removeClass('current');
            var fid = $(this).attr('data-fid');
            if (fid) {
                setSubCategories(fid);
            }
        });

        $('.moveTo').on('click', function () {
            var docId = $(this).attr('data-id');
            if (docId && isNaN(docId)) {
                return false;
            }
            var pagei = layer.open({
                type: 1,   //0-4的选择,
                title: '该文档移动至版块',
                border: [0],
                shadeClose: true,
                area: ['auto', 'auto'],
                btn: ['确定', '取消'],
                yes: function () {
                    var $item = $('[data-cid].current');
                    if ($item && $item.length === 1) {
                        var cid = $item.attr('data-cid');
                        if (cid) {
                            $.ajax({
                                url: ctx + '/doc/' + docId + '/category',
                                data: {'cid': cid},
                                type: 'POST',
                                success: function () {
                                    location.reload();
                                },
                                error: function (r) {
                                    try {
                                        var rt = JSON.parse(r.responseText);
                                        layer.alert(rt.message || '移动失败!');
                                    } catch (e) {
                                        layer.alert("移动失败");
                                    }
                                }
                            });
                        }
                    } else {
                        layer.msg('目标类型必须为第二级类型', {
                            icon: 2
                        });
                    }
                },
                btn2: function () {
                    layer.close(pagei);
                },
                content: $('#categoryDialog')
            });
        });

        // 执行审核操作
        function doReview(id, reviewOk, layid) {
            $.ajax({
                url: ctx + '/doc/' + id + '/review',
                data: {'reviewOk': reviewOk},
                type: 'POST',
                success: function () {
                    location.reload();
                },
                error: function (r) {
                    try {
                        var rt = JSON.parse(r.responseText);
                        layer.alert(rt.message || '审核操作失败!');
                    } catch (e) {
                        layer.alert("审核操作失败");
                    }
                }
            });
        }

        $('.doReview').on('click', function () {
            var docId = $(this).attr('data-id');
            if (docId && isNaN(docId)) {
                return false;
            }
            layer.open({
                type: 1,
                title: '确认审核操作',
                shadeClose: true,
                area: ['auto', 'auto'],
                content: '<p style="margin: 10px 20px;">请选择要执行的审核操作:</p>',
                btn: ['通过', '不通过', '取消'],
                yes: function (index) {
                    doReview(docId, true, index);
                },
                btn2: function (index) {
                    doReview(docId, false, index);
                    return false;
                },
                btn3: function (index) {
                    layer.close(index);
                }
            });
        });
    });
</script>
<jsp:include page="../footer.jsp"/>