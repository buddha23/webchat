<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>个人信息</title>
</head>
<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="${ctx}/m/user/center">
            <i class="fa fa-chevron-left"></i>
        </a>
        <div class="menu"></div>
        <div class="title">充值记录</div>
    </header>
    <div class="content bg-white clearfix">
        <div class="page">
            <div class="section">
                <div class="section-content">
                    <ul class="doc-favlist">
                        <c:forEach items="${userStatements}" var="s">
                            <li class="clearfix" data-docid= ${s.id}>
                                <a class="doc-content" href="###">
                                    <div class="doc-info">
                                        <div class="doc-title">${s.tradeRemark} : ${s.tradeAmount}元</div>
                                        <div class="doc-meta">
                                            <span><fmt:formatDate value="${s.tradeTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                                            <span> |途径: ${s.payType}</span>
                                            <span> |状态: ${s.tradeState}</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <footer></footer>
</div>
</body>
</html>