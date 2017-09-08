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
        <div class="title">牛人币历史</div>
    </header>
    <div class="content bg-white clearfix">
        <div class="page">
            <div class="section">
                <div class="section-content">
                    <ul class="doc-favlist">
                        <li class="clearfix">
                                <div class="finance-board">
                                    <div class="clearfix">
                                        <div class="finance-tag">我的牛人币</div>
                                        <div class="finance-num"><span class="icon-coin">牛</span> ${userScore.totalScore}</div>
                                    </div>
                                    <ul class="finance-count">
                                        <li style="border-bottom:0;"><span class="tag-income">总收入</span> ${userScore.totalIn} 牛人币</li>
                                        <li style="border-bottom:0;"><span class="tag-cost">总支出</span> ${userScore.totalOut} 牛人币</li>
                                    </ul>
                                </div>
                                <%--<div class="doc-info">--%>
                                    <%--<div class="doc-title">--%>
                                        <%--总牛人币：<span class="f16" style="color: #22a4ff !important;">${userScore.totalScore}</span>--%>
                                        <%--<span class="marginleft10">总收入：</span><span class="f16" style="color: #00a65a !important;">${userScore.totalIn}</span>--%>
                                        <%--<span class="marginleft10">总支出：</span><span class="f16" style="color: #f00 !important;">${userScore.totalOut}</span>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                        </li>
                        <c:forEach items="${scoreHistories}" var="s">
                            <li class="clearfix" data-docid= ${s.id}>
                                <a class="doc-content" href="###">
                                        <%--<div class="doc-icon"></div>--%>
                                    <div class="doc-info">
                                        <div class="doc-title">牛人币：
                                            <c:if test="${s.scoreChange > 0}">
                                                <label style="color: #00a65a !important;">+${s.scoreChange}</label>
                                            </c:if>
                                            <c:if test="${s.scoreChange < 0}">
                                                <label style="color: #f00 !important;">${s.scoreChange}</label>
                                            </c:if>
                                        </div>
                                        <div class="doc-meta">
                                            <span>
                                                <fmt:formatDate value="${s.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                                            </span>
                                            <span>| 途径：
                                                <c:if test="${!empty s.description}">${s.description}</c:if>
                                                <c:if test="${empty s.description}">
                                                    <c:choose>
                                                        <c:when test="${s.type eq 1}">上传文档</c:when>
                                                        <c:when test="${s.type eq 2}">下载文档</c:when>
                                                        <c:when test="${s.type eq 3}">文档被下载</c:when>
                                                        <c:when test="${s.type eq 4}">注册加分</c:when>
                                                        <c:when test="${s.type eq 5}">绑定第三方帐号系统赠送</c:when>
                                                        <c:when test="${s.type eq 6}">购买牛人币</c:when>
                                                        <c:when test="${s.type eq 7}">附赠牛人币</c:when>
                                                        <c:when test="${s.type eq 8}">文档被删除扣分</c:when>
                                                        <c:when test="${s.type eq 9}">每日签到</c:when>
                                                    </c:choose>
                                                </c:if>
                                            </span>
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