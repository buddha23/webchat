<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/jquery.marquee.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/swiper.jquery.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title><spring:message code="site.name"/></title>
</head>

<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">大牛数控</div>
        </div>

        <div class="tabs clearfix">
            <a class="current" href="${ctx}/m/">首页</a>
            <a href="${ctx}/m/doc/category">文库</a>
            <a href="${ctx}/m/video/">课堂</a>
            <a href="${ctx}/m/posts/category">社区</a>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <div class="page">
            <c:if test="${uploads.size()>0}">
                <ul id="marquee" class="marquee">
                    <c:forEach items="${uploads}" var="upload">
                        <li><span class="username">${bt:out(upload.user.nickname, '系统管理员')}
                        </span> 上传了文档 《${fn:escapeXml(upload.title)}》
                        </li>
                    </c:forEach>
                </ul>
            </c:if>

            <div class="searchbar">
                <form action="${ctx}/m/doc/" method="get">
                    <div class="searchbox">
                        <button type="submit" class="searchbtn" id="searchbtn" href="javascript:;">
                            <i class="fa fa-search"></i></button>
                        <div class="searchinput-wrapper">
                            <input class="searchinput" name="content" type="text" placeholder="输入搜索内容" value="${fn:escapeXml(param.content)}">
                        </div>
                    </div>
                </form>
                <div class="search-hots">
                    <span>热门搜索：</span>
                    <c:if test="${!empty pageContext.servletContext.getAttribute('hotSearchWords')}">
                        <c:forEach items="${pageContext.servletContext.getAttribute('hotSearchWords')}" var="word">
                            <a href="${ctx}/m/doc?content=${fn:escapeXml(word)}">${fn:escapeXml(word)}</a>
                        </c:forEach>
                    </c:if>
                </div>
            </div>

            <div class="section">
                <div class="section-content">
                    <div class="section-news">
                        <div class="news-pic"></div>
                        <ul class="news-list">
                            <c:forEach items="${news}" var="n">
                                <li>
                                    <a class="linkover" href="${ctx}/m/newsDetail/${n.id}">${fn:escapeXml(n.title)}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="section">
                <h4>推荐板块 <span class="color-grey">(左右滑动)</span></h4>
                <div class="section-content">
                    <div class="recommend-category swiper-container">
                        <ul class="category-wrapper swiper-wrapper clearfix">
                            <c:forEach items="${categorys}" var="category">
                                <li class="swiper-slide">
                                    <a href="${ctx}/m/doc/categoryC2/${category.id}">
                                        <div class="category-ico">
                                            <img src="${bt:url(category.icon,"/files/" ,"/assets/images/logo.jpg" )}" alt=""/>
                                        </div>
                                        <div class="category-txt">${fn:escapeXml(category.name)}</div>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="section tabs">
                <ul class="section-tab tabs-nav">
                    <li class="tab-menu tabs-nav-item current">最新问答</li>
                    <li class="tab-menu tabs-nav-item">最新课程</li>
                    <li class="tab-menu tabs-nav-item">最热下载</li>
                </ul>
                <div class="section-content tabs-panel">
                    <div class="tab">
                        <div class="qa-list">
                            <c:forEach items="${newPosts}" var="posts">
                                <a class="qa-list-item" href="${ctx}/m/posts/${posts.id}">
                                    <div class="qa-item-profile">
                                        <img class="avatar" src="${bt:url(posts.userAvatar,'/files/', '/assets/images/default_avatar.png')}" alt=""/>
                                    </div>
                                    <div class="qa-item-info">
                                        <div class="qa-title">${fn:escapeXml(posts.title)}</div>
                                        <div class="qa-meta">
                                            <span>
                                                <i class="icon-star">
                                                    <c:choose>
                                                        <c:when test="${posts.postsReward.state == 1 && posts.postsReward.type == 1}">
                                                            ${posts.postsReward.amount} 牛人币
                                                        </c:when>
                                                        <c:when test="${posts.postsReward.state == 1 && posts.postsReward.type == 2}">
                                                            ${posts.postsReward.amount} 积分
                                                        </c:when>
                                                        <c:when test="${posts.postsReward.state%2 == 0}">
                                                            已悬赏
                                                        </c:when>
                                                        <c:otherwise>已悬赏</c:otherwise>
                                                    </c:choose>
                                                </i>
                                            </span>
                                            <span><i class="icon-eye"></i> ${posts.views}</span>
                                            <span><i class="icon-bubble"></i> ${posts.commentsNum}</span>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                            <a class="more-btn clearfix text-center" href="${ctx}/m/posts/category">查看更多</a>
                        </div>
                    </div>

                    <div class="tab">
                        <div class="course-list">
                            <c:forEach items="${videos}" var="video">
                                <a class="clearfix" href="${ctx}/m/video/intro/${video.id}">
                                    <div class="course-icon"><img src="${bt:url(video.cover,'/files/','/assets/images/demo/ex_1.jpg')}" alt="${fn:escapeXml(v.name)}"/></div>
                                    <div class="course-content">
                                        <div class="course-title">${fn:escapeXml(video.name)}</div>
                                        <div class="course-meta">
                                            <span>期数：${video.sectionCount}</span>
                                            <span>花费：${video.costScore} 牛人币</span>
                                            <span>购买：${video.buysCount} 人</span>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                            <a href="${ctx}/m/video/" class="more-btn clearfix text-center">查看更多</a>
                        </div>
                    </div>

                    <div class="tab">
                        <div class="doc-list">
                            <c:forEach items="${docDownLoads}" var="doc">
                                <a class="${doc.fileType} clearfix" href="${ctx}/m/doc/${doc.id}">
                                    <div class="doc-icon"></div>
                                    <div class="doc-content">
                                        <div class="doc-title">${fn:escapeXml(doc.title)}</div>
                                        <div class="doc-meta">
                                            <span>大小：<bt:fileSize size="${doc.fileSize}"/></span>
                                            <span>阅读：${doc.views}</span>
                                            <span>下载数：${doc.downloads}</span>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                            <a href="${ctx}/m/doc/category" class="more-btn clearfix text-center">查看更多</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div>
        <span id="qqLoginBtn" style="margin:20px 0;"></span>
    </div>

    <footer>
    </footer>

    <div class="page-mask" style="display: none;"></div>
</div>

<jsp:include page="login-div.jsp"/>

<div id="toLogin"></div>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
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

    $(document).ready(function () {
        $("#marquee").marquee({yScroll: "bottom", showSpeed: 1000});

        var swiper = new Swiper('.swiper-container', {
            pagination: '.swiper-pagination',
            slidesPerView: 4,
            paginationClickable: true
        });
    });

    $(function () {
        var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == 'micromessenger') {
            $('.weixin').show();
            $('.weixin').click(function () {
                window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize' +
                        '?appid=${configer.wxAccountAppid}' +
                        '&redirect_uri=' + location.href + '&response_type=code' +
                        '&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';
            });
        } else {
            $('.weixin').hide();
            return false;
        }
    });

    <%--$('.weixin').click(function () {--%>
        <%--window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize' +--%>
                <%--'?appid=${configer.wxAccountAppid}' +--%>
                <%--'&redirect_uri=${configer.appUrl}m/&response_type=code' +--%>
                <%--'&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';--%>
    <%--});--%>

    //QQ第三方登录
    $('#qqLogin').click(function () {
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=${configer.appUrl}", "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    });

    $('#toLogin').click(function (event) {
        event.stopPropagation();
        loginEvents();
    });

    var dlg = "${param.dlg}";
    if (dlg == "login" && isLogin == false) {
        $('#toLogin').click();
        $.ajax({
            url: '${ctx}/m/auth/addCookies',
            type: 'post',
            data: {cookyName: "qrCodeVisit", workTime: 30},
            success: function () {
                console.log("成功添加二维码访问cookies");
                $.ajax({
                    url: '${ctx}/m/auth/getCookies',
                    type: 'post',
                    success: function (c) {
                        console.log(c);
                    }
                })
            }
        });
    }

    // 微信分享
    var title = '大牛数控';
    var imgUrl = 'http://www.d6sk.com/assets/images/logo(500).jpg';
    var desc = '大牛数控-国内最大的数控技术交流社区';
    var link = location.href;

    $.post('${ctx}/weixin/signature', {url: location.href}, function (data) {
        wx.config({
            debug: false,
            appId: data.appId,
            timestamp: data.timestamp,
            nonceStr: data.noncestr,
            signature: data.signature,
            jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone']
        });
    });
    wx.ready(function () {
        wx.onMenuShareTimeline({    // 朋友圈
            title: title,
            link: link,
            imgUrl: imgUrl,
            success: function () {
                console.log('朋友圈分享成功！');
            },
            cancel: function () {
                console.log('朋友圈分享取消！');
            }
        });
        wx.onMenuShareAppMessage({ // 朋友
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('朋友分享成功！');
            },
            cancel: function () {
                console.log('朋友分享取消！');
            }
        });
        wx.onMenuShareQQ({ // QQ
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('QQ分享成功！');
            },
            cancel: function () {
                console.log('QQ分享取消！');
            }
        });
        wx.onMenuShareWeibo({ // 微博
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('微博分享成功！');
            },
            cancel: function () {
                console.log('微博分享取消！');
            }
        });
        wx.onMenuShareQZone({ // QZone
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('QZone分享成功！');
            },
            cancel: function () {
                console.log('QZone分享取消！');
            }
        });
    });
    wx.error(function (res) {
        console.log(res);
    });

</script>
</body>
</html>
