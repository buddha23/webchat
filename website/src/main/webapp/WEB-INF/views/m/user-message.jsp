<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>用户消息</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" href="${assets}/plugins/froalaEditor/css/froala_editor.pkgd.min.css" type="text/css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="${ctx}/m/#usercenter">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu">
        </div>

        <div class="title">用户消息</div>
    </header>

    <div class="content bg-white clearfix">
        <ul class="PM">
            <c:forEach items="${messages.content}" var="m">
                <li class="clearfix">
                    <div class="portrait"><img src="${assets}/images/sys_portrait.png"></div>
                    <div class="PM-message">
                        <div class="name">${m.title}</div>
                        <div class="text">${fn:escapeXml(m.message)}</div>
                        <div class="PM-meta clearfix">
                            <div class="tips floatleft"><fmt:formatDate value="${m.createTime}" pattern="yyyy年MM月dd日 HH:mm"/></div>
                            <div class="operate floatright clearfix">
                                <%--<div class="operate-item"><a href="#">删除</a></div>--%>
                            </div>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>
        <bt:pagination data="${messages}"/>
    </div>

    <footer>
    </footer>

    <div class="page-mask"></div>
</div>

</body>
</html>
