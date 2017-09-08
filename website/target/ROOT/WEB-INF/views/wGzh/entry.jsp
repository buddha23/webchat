<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <c:choose>
        <c:when test="${ !empty param.title }">
            <title>${param.title}_大牛数控</title>
            <c:if test="${ !empty param.keywords}">
                <meta name="keywords" content="${param.keywords },大牛数控"/>
            </c:if>
            <c:if test="${ !empty param.description}">
                <meta name="description" content="${param.description }"/>
            </c:if>
        </c:when>

        <c:otherwise>
            <title>大牛数控_你身边的数控专家</title>
            <meta name="keywords"
                  content="大牛数控,数控专家,数控,fanuc,发那科,西门子,三菱数控,马扎克,华中数控,哈斯数控,CAXA,发格数控,精雕数控,海德汉数控,数控操作手册,数控常用参数"/>
            <meta name="description"
                  content="大牛数控是国内最牛掰的数控技术交流学习中心，涵盖fanuc/发那科/西门子/三菱数控/马扎克/华中数控/哈斯数控/CAXA/发格数控/精雕数控/海德汉数控等各类数控机床资料，比如数控操作手册，数控常用参数，数控帮助文档等。大牛数控网是广大数控达人的家园。"/>
        </c:otherwise>
    </c:choose>
    <meta name="generator" content="www.d6sk.com 大牛数控"/>
    <meta name="author" content="www.d6sk.com 大牛数控"/>
    <link rel="shortcut icon" href="${assets}/images/favicon.ico"/>
    <link rel="stylesheet" href="${assets}/css/public.css" type="text/css">
    <link rel="stylesheet" href="${assets}/css/style.css" type="text/css">
    <link rel="stylesheet" href="${assets}/css/icheck-blue.css" type="text/css">
    <c:if test="${!empty param.css}">
        <c:forTokens items="${param.css}" delims="," var="c">
            <link rel="stylesheet" href="${assets}/${c}">
        </c:forTokens>
    </c:if>
    <script type="text/javascript" src="${assets}/js/min/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/icheck.min.js"></script>
    <script type="text/javascript" src="${assets}/js/website.js"></script>
    <script type="text/javascript" src="${assets}/js/layer.js"></script>
    <script type="text/javascript" src="${assets}/js/utils.js"></script>
    <script type="text/javascript" src="${assets}/js/bindAccount.js"></script>
    <script type="text/javascript" src="${assets}/js/formcheck.js"></script>
    <script type="text/javascript" src="${assets}/js/loginBox.js"></script>
    <base href="${ctx}/">
    <script type="text/javascript">
        var ctx = '${ctx}';
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
    </script>

    <script>
        var _hmt = _hmt || [];
        (function () {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?aeaf44e7e7e0e2ed12ebcdc99e35e2ab";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>

</head>

<body class="bg-white">
<div class="header">
    <div class="top">
        <div class="container">
            <a href="/" class="logo"></a>
            <div class="searchbar">
                <form action="${ctx}/doc/">
                    <div class="searchbox clearfix">
                        <input class="search-input" type="text" name="content" id="searchWord"
                               placeholder="请输入您要搜索的关键词" value="${fn:escapeXml(param.content)}">
                        <button class="search-btn" type="submit" id="searchDoc"></button>
                    </div>
                </form>
                <div class="search-hots">
                    <span>热门搜索：</span>
                    <c:if test="${!empty pageContext.servletContext.getAttribute('hotSearchWords')}">
                        <c:forEach items="${pageContext.servletContext.getAttribute('hotSearchWords')}" var="word">
                            <a href="${ctx}/doc/?content=${fn:escapeXml(word)}">${fn:escapeXml(word)}</a>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
    <div class="nav">
        <div class="container">
            <ul class="menu clearfix">
                <li><a data-nav="index" href="${ctx}/">首页</a></li>
                <li><a data-nav="doc" href="${ctx}/doc">数控文库</a></li>
                <li><a data-nav="soft" href="${ctx}/soft">数控软件</a></li>
                <li><a data-nav="vdo" href="${ctx}/video/">数控课堂</a></li>
                <li><a data-nav="course" href="${ctx}/course">精品教程</a></li>
                <li><a data-nav="qa" href="${ctx}/post/">数控社区</a></li>
                <li><a data-nav="cooperation" href="${ctx}/weixin/gzh/entry">战略合作</a></li>
                <li><a href="https://cncweixiu.taobao.com/index.htm" target="_blank">官方网店</a></li>
            </ul>
            <c:if test="${!empty sessionScope.user}">
                <div class="nav-toolbar clearfix">
                    <div class="userbar clearfix">
                        <a class="userdrop-toggle" href="javascript:void(0);">
                            <img class="portrait" src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}" alt="大牛数控">
                            <div class="new-message"></div>
                            <span class="name">
                            <i class=""></i>
                            ${bt:out(sessionScope.user.nickname, sessionScope.user.name)}</span>
                        </a>
                        <ul class="userbar-userdrop">
                            <div class="userbar-profile clearfix">
                                <img class="profile-avatar" src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}" alt="大牛数控"/>
                                <div class="profile-info">
                                    <div class="profile-name">
                                            ${bt:out(sessionScope.user.nickname, sessionScope.user.name)}
                                        <c:if test="${sessionScope.user.id != null}">
                                            <a id="userSignin" class="sign-btn" href="javascript:;">签到</a>
                                        </c:if>
                                    </div>
                                    <div class="profile-score">
                                        我的牛人币：<em class="score">0</em>
                                        &nbsp;&nbsp;&nbsp;&nbsp;积分：<em class="point">0</em>
                                        <%--<a href="${ctx}/user/myscore" class="marginleft10" onclick="return OBJ_bindAccount.checkBind(this)">积分历史</a>--%>
                                    </div>
                                </div>
                            </div>
                            <div class="userbar-box">
                                <div class="userbar-menu">
                                    <a class="menu-item orange" href="${ctx}/doc/upload"
                                       onclick="return OBJ_bindAccount.checkBind(this)">
                                        <i class="fa fa-upload"></i>
                                        <div>上传文档</div>
                                    </a>
                                    <a class="menu-item blue" href="${ctx}/user/center">
                                        <i class="fa fa-desktop"></i>
                                        <div>管理中心</div>
                                    </a>
                                    <a class="menu-item" href="${ctx}/user/message"
                                       onclick="return OBJ_bindAccount.checkBind(this)">
                                        <i class="fa fa-envelope"></i>
                                        <div class="new-message"></div>
                                        <div>我的消息</div>
                                    </a>
                                    <a class="menu-item dark" href="${ctx}/auth/logout">
                                        <i class="fa fa-sign-out"></i>
                                        <div>退出</div>
                                    </a>
                                </div>
                            </div>
                        </ul>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty sessionScope.user}">
                <div class="nav-toolbar clearfix">
                    <ul class="iconbar clearfix">
                        <a href="${ctx}/auth/login" title="登录">
                            <li><i class="icon-login"></i> <em>登录</em></li>
                        </a>
                        <a href="${ctx}/auth/register" title="注册">
                            <li><i class="icon-user-follow"></i> <em>注册</em></li>
                        </a>
                    </ul>
                </div>
            </c:if>
        </div>
    </div>

    <div class="banner">
        <div class="banner-pic">
            <div class="banner-p1"
                 style="background: url(${assets}/images/demo/banner_cooperate.jpg) center no-repeat; display: block; height: 300px; width: 100%; position: absolute;"></div>
            <div class="banner-content">
                <div class="banner-text">
                    <em>微信公众号授权</em>
                    <p>一键开启CNC海量文库</p>
                </div>
                <a href="${ctx}/weixin/open" class="banner-btn">确认授权</a>
            </div>
        </div>
        <ul class="banner-intro container clearfix">
            <li>
                <div class="item-icon"><i class="icon-compass"></i></div>
                <div class="item-txt">
                    <div class="item-txt-title">简单易用</div>
                    <p>一键授权多个公众号，同步资源</p>
                </div>
            </li>
            <li>
                <div class="item-icon"><i class="icon-folder"></i></div>
                <div class="item-txt">
                    <div class="item-txt-title">海量资料</div>
                    <p>海量数控资料，教程免费下载</p>
                </div>
            </li>
            <li>
                <div class="item-icon"><i class="icon-share"></i></div>
                <div class="item-txt">
                    <div class="item-txt-title">裂变传播</div>
                    <p>精准客户群，精准传播引流</p>
                </div>
            </li>
            <li>
                <div class="item-icon"><i class="icon-users"></i></div>
                <div class="item-txt">
                    <div class="item-txt-title">人才资源</div>
                    <p>人才招聘，人力资源共享</p>
                </div>
            </li>
        </ul>
    </div>
</div>

<%--<div class="content pd20 management">--%>
    <%--<div class="container clearfix">--%>
        <%--<c:if test="${empty content}">--%>
            <%--<a href="${ctx}/weixin/open">点击接入</a>--%>
        <%--</c:if>--%>
        <%--<c:if test="${!empty content}">--%>
            <%--${content}--%>
        <%--</c:if>--%>
        <%--&lt;%&ndash;<div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<c:forEach items="${gzhs}" var="gzh">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<img class="avatar" src="${gzh.icon}"/>:${gzh.name}&ndash;%&gt;--%>
        <%--&lt;%&ndash;</c:forEach>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
    <%--</div>--%>
<%--</div>--%>

<div class="content bg-white pd20">
    <div class="container">
        <div class="cooperate-user">
            <div class="cooperate-header">合作伙伴</div>
            <ul class="cooperate-list clearfix">
                <c:forEach items="${gzhs}" var="gzh">
                <li class="clearfix">
                    <div class="user-avatar">
                        <img src="${gzh.icon}" alt="${fn:escapeXml(gzh.name)}"/>
                    </div>
                    <div class="user-name">${fn:escapeXml(gzh.name)}</div>
                </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
<script>
    $(function () {
        $('.nav .menu li a[data-nav="cooperation"]').addClass('current');
    })
</script>