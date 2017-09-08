<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="file">
            <div class="box">
                <div class="box-header">
                    <h1>软件管理</h1>
                </div>
                <div class="box-content">
                    <div class="user-panel clearfix">
                        <div class="panel-col clearfix">
                            <div class="col-main">
                                <div class="item-zip"></div>
                                <div class="text">
                                    <div class="item-title">我的文件包（${fn:length(docs)}）</div>
                                    <a class="item-btn" href="${ctx}/soft/upload" onclick="return OBJ_bindAccount.checkBind(this)">上传</a>
                                </div>
                            </div>
                            <div class="col-intro">
                                <p>说明：上传压缩文件，如数控工具，常用软件等</p>
                                <p class="tips">文件格式：rar，zip，7-zip</p>
                            </div>
                        </div>
                    </div>
                    <div class="box-pane margintop20">
                        <ul class="subnav clearfix">
                            <li><a href="${ctx}/soft/myUpload">我的上传</a></li>
                            <li><a href="${ctx}/soft/myDownload">我的下载</a></li>
                            <li class="current"><a href="${ctx}/soft/myFav">我的收藏</a></li>
                        </ul>
                        <div class="box-pane-content">
                            <form id="uncollectform" action="${ctx}/soft/unConllect" method="post">
                                <table class="table-style2 dyntable">
                                    <thead>
                                    <tr>
                                        <th><input class="checkall" type="checkbox"/></th>
                                        <th>名称</th>
                                        <th>类型</th>
                                        <th>收藏日期</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${docs}" var="video">
                                        <tr>
                                            <td><input name="myconllects" type="checkbox" value="${video.uuId}"></td>
                                            <td>${video.title}</td>
                                            <td>数控软件</td>
                                            <td>${video.collectTime}</td>
                                            <td><a href="${ctx}/soft/${video.uuId}">查看</a>
                                                <a class="marginleft10" id="unconllect(${video.uuId})"
                                                   onclick="unconllect(${video.uuId})" href="javascript:void(0);">取消收藏</a></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <td colspan="5" rowspan="1">
                                            <a class="" href="javascript:void(0);" onclick="unconllects()">批量取消</a>
                                        </td>
                                    </tr>
                                    </tfoot>
                                </table>
                            </form>
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
<script>
    function unconllect(id) {
        $.ajax({
            url: ctx + "/soft/uncollect",
            data: {uuId: id},
            success: function () {
                location.href = ctx + "/soft/myFav";
            },
            error: function () {
                layer.alert("取消关注失败");
            }
        })
    }
    function unconllects() {
        var myFavs = $("div .checked").children("input");
        if (myFavs.length > 1) {
            $("#uncollectform").submit();
        }
    }
</script>
<%@include file="../footer.jsp" %>
