<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/animate.min.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/animate.delay.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>用户注册</title>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="${ctx}/m/auth/login">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu"></div>
        <div class="title">出错啦</div>
    </header>
    <div class="content bg-white clearfix">
        <div class="login">
            <ul class="login-group">
                <label>o(╯□╰)o抱歉,您所请求的内容被拒绝 , </label>
                <label>请先<a href="${ctx}/m/auth/register" style="color: #17a5ff;"> 注册 </a>或
                    <a href="javascript:;" onclick="OBJ_mLoginBox.checkLogin(event)" style="color: #17a5ff;">绑定 </a>网站账号
                </label>
            </ul>
        </div>
    </div>
    <footer></footer>
    <div class="page-mask" style="display: none;"></div>
</div>

<jsp:include page="login-div.jsp"/>

<script type="text/javascript" src="${assets}/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="${assets}/js/layer.js"></script>
<script type="text/javascript">

    //绑定组
    var isLogin = false;
    var isBind = false;
    <c:if test="${!empty sessionScope.user}">
    isLogin = true;
    if (${sessionScope.user.loginType == null}) {
        isBind = true;
    } else if (${sessionScope.user.isBind == true}) {
        isBind = true;
    }
    </c:if>
    <%--var operate = {--%>
    <%--myDownload: function () {--%>
    <%--window.location.href = "${ctx}/m/";--%>
    <%--}--%>
    <%--};--%>
</script>
</body>
</html>
