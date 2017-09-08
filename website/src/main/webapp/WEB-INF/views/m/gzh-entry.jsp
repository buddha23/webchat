<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/swiper.jquery.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script>
        $(function () {
            var swiper = new Swiper('.swiper-container', {
                pagination: '.swiper-pagination',
                slidesPerView: 4,
                paginationClickable: true,
                autoplay: 3000,
                loop: true
            });
        })
    </script>
    <title>合作授权</title>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="javascript:history.back()">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu">
        </div>

        <div class="title">合作授权</div>
    </header>

    <div class="content bg-white clearfix">
        <div class="banner">
            <c:if test="${empty content}">
                <div class="banner-content">
                    <div class="banner-text">
                        <em>微信公众号授权</em>
                        <p>一键开启CNC海量文库</p>
                    </div>
                        <%--确认授权--%>
                    <a href="${ctx}/weixin/open" class="banner-btn">确认授权</a>
                </div>
            </c:if>

            <c:if test="${!empty content}">${content}</c:if>
        </div>

        <div class="cooperate-user">
            <div class="cooperate-header">合作伙伴</div>
            <div class="swiper-container">
                <ul class="cooperate-list swiper-wrapper clearfix">
                    <c:forEach items="${gzhs}" var="gzh">
                        <li class="swiper-slide clearfix">
                            <div class="user-avatar"><img src="${gzh.icon}" alt=""></div>
                            <div class="user-name">${fn:escapeXml(gzh.name)}</div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>

    <footer>

    </footer>
</div>
</body>
</html>
