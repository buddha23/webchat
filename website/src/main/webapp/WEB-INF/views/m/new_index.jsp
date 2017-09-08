<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/animate.min.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/animate.delay.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
    <link rel="stylesheet" type="text/css" href="${assets}/js/mobile/layer.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/jquery.marquee.min.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.ripple.js"></script>
    <script type="text/javascript" src="${assets}/js/min/swiper.jquery.min.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title><spring:message code="site.name"/></title>
</head>

<body>
<div id="home" class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a class="menu-item menu-sign js-do-sign"></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter" onclick="location.reload()"></a>
            </div>

            <div class="title">CNC数控技术</div>
        </div>

    </header>

    <div class="content bg-white pdb60 clearfix">
        <div class="page">
            <c:if test="${imgFileList.size()>0}">
            <div class="swiper-banner">
                <div class="swiper-wrapper">
                    <c:forEach items="${imgFileList}" var="img" varStatus="imgStatus">
                        <div class="swiper-slide"><a href="${bt:out(img.link,'javascript:void(0);')}" target="_blank"><img src="${ctx}/files/user/${img.path}" alt=""/></a></div>
                    </c:forEach>
                </div>
                <!-- 如果需要分页器 -->
                <div class="swiper-banner-pagination"></div>
            </div>
            </c:if>
            <div class="section">
                <div class="section-app">
                    <div class="app-list clearfix">
                        <a href="${ctx}/m/doc/category">
                            <div class="app-icon"><i class="icon-docs"></i></div>
                            <div class="app-name">数控文库</div>
                        </a>
                        <a href="${ctx}/m/video/">
                            <div class="app-icon"><i class="icon-book-open"></i></div>
                            <div class="app-name">视频课堂</div>
                        </a>
                        <a href="${ctx}/m/posts/category">
                            <div class="app-icon"><i class="icon-bubbles"></i></div>
                            <div class="app-name">大牛社区</div>
                        </a>
                        <a href="${ctx}/m/commodity/list">
                            <div class="app-icon"><i class="icon-handbag"></i></div>
                            <div class="app-name">商品交易</div>
                        </a>
                    </div>
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
                    <li class="tab-menu tabs-nav-item current">最新提问</li>
                    <li class="tab-menu tabs-nav-item">最新课程</li>
                    <li class="tab-menu tabs-nav-item">热门下载</li>
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

    <footer></footer>
</div>

<div id="usercenter" class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a class="menu-item menu-sign js-do-sign"></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter" onclick="location.reload()"></a>
            </div>

            <div class="title">大牛数控</div>
        </div>
    </header>
    <div class="content bg-white clearfix">
        <div class="page">
            <c:if test="${empty sessionScope.user}">
                <div class="profile clearfix fadeIn animate0">
                    <div class="profile-background"></div>
                    <div class="profile-content">
                        <div class="profile-photo fadeInUp animate1">
                            <img src="${assets}/images/default_avatar.png"/>
                        </div>
                        <div class="profile-info fadeInUp animate1">
                            <div class="profile-title">游客</div>
                            <a class="btn-login" href="${ctx}/m/auth/login?backurl=${ctx}/m/user/center">登录</a>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${!empty sessionScope.user}">
                <div class="profile clearfix fadeIn animate0">
                    <div class="profile-background"></div>
                    <div class="profile-content">
                        <a class="profile-photo fadeInUp animate1" href="${ctx}/m/user/message">
                            <img src="${bt:url(user.avatar, '/files/', '/assets/images/default_avatar.png')}"/>
                            <div class="new-message" style="display: none;"></div>
                        </a>
                        <ul class="profile-info fadeInUp animate1">
                            <li class="profile-title">
                                <i class=""></i>
                                <c:out value="${user.nickname}">${user.name}</c:out>
                            </li>
                            <li class="profile-score">积分：
                                <span id="totalScore" class="color-red">
                                    <c:if test="${userPoint.totalPoint == null}">0</c:if>${userPoint.totalPoint}
                                </span>
                            </li>
                            <c:if test="${sessionScope.user.id != null}">
                                <li>
                                    <button id="userSignin" class="sign-btn js-do-sign" type="button">点击签到</button>
                                </li>
                            </c:if>
                        </ul>
                        <a class="profile-goto fadeInRight animate1" href="${ctx}/m/user/list"></a>
                    </div>
                </div>
                <div class="section">
                    <c:if test="${empty sessionScope.user.id}">
                        <div class="section tabs">
                            <ul class="section-tab tabs-nav clearfix">
                                <li class="current" style="width: 100%; height: 50px;">
                                    <center>
                                        <i class="fa fa-star"></i>尚未绑定网站账号,前往
                                        <a style="color: #17a5ff;" href="${ctx}/m/auth/register">注册</a> 或
                                        <a style="color: #17a5ff;" onclick="OBJ_mLoginBox.checkLogin(event,operate.myDownload)">绑定账号</a>
                                    </center>
                                </li>
                            </ul>
                        </div>
                    </c:if>
                    <c:if test="${!empty sessionScope.user.id}">
                        <div class="section tabs">
                            <ul class="section-tab tabs-nav clearfix">
                                <li class="tab-menu tabs-nav-item current"><i class="fa fa-star"></i> 我的收藏</li>
                                <li class="tab-menu tabs-nav-item"><i class="fa fa-download"></i> 我的下载</li>
                                    <%--<li class="tab-menu tabs-nav-item"><i class="fa fa-comment"></i> 我的提问</li>--%>
                                <li class="tab-menu tabs-nav-item"><i class="fa fa-upload"></i> 我的上传</li>
                            </ul>
                            <div class="section-content tabs-panel">
                                <div class="tab">
                                    <ul class="doc-favlist">
                                        <div id="favlist">
                                            <c:forEach items="${mycollects}" var="c">
                                                <li class="${c.fileType} clearfix" data-docid= ${c.id}>
                                                    <a class="doc-btn" href="javascript:;">
                                                        <div class="btn-unfav"><i class="fa fa-star-o"></i></div>
                                                        <div class="btn-txt">取消</div>
                                                    </a>
                                                    <a class="doc-content" href="${ctx}/m/doc/${c.id}">
                                                        <div class="doc-icon"></div>
                                                        <div class="doc-info">
                                                            <div class="doc-title">${c.title}</div>
                                                            <div class="doc-meta">
                                                                <span><bt:fileSize size="${c.fileSize}"/></span>
                                                                <span>上传：${fn:escapeXml(c.uploaderName)}</span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </li>
                                            </c:forEach>
                                        </div>
                                        <c:if test="${mycollects.size()>0}">
                                            <a href="javascript:;" id="favA" onclick="getMoreFav()" class="more-btn clearfix text-center">加载更多</a>
                                        </c:if>
                                        <c:if test="${mycollects.size()== 0}">
                                            <a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>
                                        </c:if>
                                    </ul>
                                </div>

                                <div class="tab">
                                    <ul class="doc-favlist">
                                        <div id="downlist">
                                            <c:forEach items="${mydownloads}" var="c">
                                                <li class="${c.fileType} clearfix">
                                                    <a class="doc-content" href="${ctx}/m/doc/${c.id}">
                                                        <div class="doc-icon"></div>
                                                        <div class="doc-info">
                                                            <div class="doc-title">${c.title}</div>
                                                            <div class="doc-meta">
                                                                <span><bt:fileSize size="${c.fileSize}"/></span>
                                                                <span>上传：${fn:escapeXml(c.uploaderName)}</span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </li>
                                            </c:forEach>
                                        </div>
                                        <c:if test="${mydownloads.size()>0}">
                                            <a href="javascript:;" id="downA" onclick="getMoreDowns()" class="more-btn clearfix text-center">加载更多</a>
                                        </c:if>
                                        <c:if test="${mydownloads.size()==0}">
                                            <a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>
                                        </c:if>
                                    </ul>
                                </div>

                                <div class="tab">
                                    <ul class="doc-list">
                                        <div class="doc-toolbar">
                                            <a class="upload-btn" href="${ctx}/m/doc/upload"><i class="fa fa-plus"></i>
                                                上传文档</a>
                                        </div>
                                        <div id="uplist">
                                            <c:forEach items="${myuploads}" var="c">
                                                <a class="${c.fileType} clearfix" href="${ctx}/m/doc/${c.id}">
                                                    <div class="doc-icon ${c.fileType}"></div>
                                                    <div class="doc-content">
                                                        <div class="doc-title">${fn:escapeXml(c.title)}</div>
                                                        <div class="doc-meta">
                                                            <span><bt:fileSize size="${c.fileSize}"/></span>
                                                            <span>阅读：${c.views}</span>
                                                        </div>
                                                    </div>
                                                </a>
                                            </c:forEach>
                                        </div>
                                        <c:if test="${myuploads.size()>0}">
                                            <a href="javascript:;" id="upA" onclick="getMoreUploads()" class="more-btn clearfix text-center">加载更多</a>
                                        </c:if>
                                        <c:if test="${myuploads.size()==0}">
                                            <a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>
                                        </c:if>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
    <footer></footer>
    <div class="page-mask" style="display: none;"></div>
</div>

<div id="publish" class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a class="menu-item menu-sign js-do-sign"></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter" onclick="location.reload()"></a>
            </div>

            <div class="title">CNC数控技术</div>
        </div>

    </header>
    <div class="content bg-white clearfix">
        <div class="publish-list clearfix">
            <a href="javascript:;" onclick="OBJ_mLoginBox.checkLogin(event,operate.jumpUpload)">
                <div class="publish-icon"><i class="icon-docs"></i></div>
                <div class="publish-name">上传文档</div>
            </a>
            <a href="${ctx}/m/posts/list/${postCate.id}?action=publish">
                <div class="publish-icon"><i class="icon-bubbles"></i></div>
                <div class="publish-name">发布帖子</div>
            </a>
            <a href="javascript:;" onclick="OBJ_mLoginBox.checkLogin(event,operate.jumpBuy)">
                <div class="publish-icon"><i class="icon-wallet"></i></div>
                <div class="publish-name">求购商品</div>
            </a>
            <a href="javascript:;" onclick="OBJ_mLoginBox.checkLogin(event,operate.jumpSell)">
                <div class="publish-icon"><i class="icon-present"></i></div>
                <div class="publish-name">出售商品</div>
            </a>
        </div>
    </div>
</div>

<div class="container-user">
    <div class="userbar">
        <div class="userbar-menu">
            <a class="item home act" href="#home">
                <div class="item-icon"></div>
                <div class="item-name">首页</div>
            </a>
            <a class="item publish" href="#publish">
                <div class="item-icon"></div>
                <div class="item-name">发布</div>
            </a>
            <a class="item usercenter" href="#usercenter">
                <div class="item-icon"></div>
                <div class="item-name">我</div>
            </a>
        </div>
    </div>
</div>

<jsp:include page="login-div.jsp"/>
<div class="page-mask" style="display: none;"></div>
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

    var ctx = '${ctx}';
    function fileSize(size) {
        if (size < 1024) {
            return size + "KB";
        } else if (size < 1024 * 1024) {
            return (size / 1024).toFixed(2) + 'MB';
        } else {
            return (size / (1024 * 1024)).toFixed(2) + 'GB';
        }
    }

    var operate = {
        myDownload: function () {
            location.reload();
        },
        jumpBuy: function () {
            location.href = '${ctx}/m/commodity/publish?type=2';
        },
        jumpSell: function () {
            location.href = '${ctx}/m/commodity/publish?type=1';
        },
        jumpUpload: function () {
            location.href = '${ctx}/m/doc/upload';
        }
    };
    //    取消收藏
    $('body').on('click', '.doc-btn', function () {
        var $this = $(this);
        var docid = $this.parent().attr('data-docid');
        $.ajax({
            url: ctx + '/doc/uncollect?id=' + docid,
            type: 'get',
            success: function () {
                $this.parent().remove();
            },
            error: function (r) {
                console.log(r);
            }
        });
    });
    //用户积分,是否签到
    $(function () {
        if (isBind) {
            $.get(ctx + '/user/infos', function (data) {
                if (data) {
                    $('#totalScore').html(data['point']['totalPoint']);
                    if (data['isSigns'])
                        $('.profile .sign-btn').text('已签到').attr("disabled", 'disable');
                    if (data['messages']) {
                        $('.new-message').html(data['messages']).show();
                    }
                    if (data['isMember']) {
                        $('.profile-title').children('i').attr('class', 'vip-icon-hd');
                    }
                } else {
                    $('#totalScore').html(0);
                }
            });
        }

    });
    //签到
    $(".js-do-sign").click(function () {
        OBJ_mLoginBox.checkLogin(event, doSign);
    });
    function doSign() {
        $.ajax({
            url: '${ctx}/user/signin',
            type: 'get',
            success: function (data) {
                if (data == 'OK') {
                    $('.profile .sign-btn').text('已签到').attr("disabled", 'disable');
                    var $scoreNum = $('#totalScore');
                    var left = parseInt($scoreNum.position().left) + 10,
                            top = parseInt($scoreNum.position().top) - 10;
                    $('.profile-score').append('<div class="sign-score color-red"><b>+' + 10 + '<\/b></\div>');
                    $('.sign-score').css({
                        'position': 'absolute',
                        'z-index': '1',
                        'left': left + 'px',
                        'top': top + 'px'
                    }).animate({
                        top: top - 10,
                        left: left + 10
                    }, 'slow', function () {
                        $(this).fadeIn('fast').remove();
                        var Num = parseInt($scoreNum.text());
                        Num += 10;
                        $scoreNum.text(Num);
                    });
                    layer.open({
                        content: '签到成功！',
                        btn: '我知道了'
                    });
                    return false;
                }
                else {
                    layer.open({
                        content: data.message,
                        btn: '我知道了'
                    });
//                    alert(data.message);
                }
            },
            error: function (r) {
                layer.alert(JSON.parse(r.responseText).message || '签到失败');
            }
        })
    }
    var favpage = 1, downpage = 1, postspage = 1, uppage = 1;
    //更多收藏
    function getMoreFav() {
        var i;
        favpage += 1;
        var page;
        $.ajax({
            url: ctx + '/m/user/getmore',
            data: {getType: 'collects', page: favpage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var docData = getData[i];
                        $("#favlist").append(
                                "<li class='" + docData.fileType + " clearfix' data-docid= ''>" +
                                "<a class='doc-btn' href='javascript:;'>" +
                                "<div class='btn-unfav'><i class='fa fa-star-o'></i></div>" +
                                "<div class='btn-txt'>取消</div>" +
                                "</a>" +
                                "<a class='doc-content' href='" + ctx + "/m/doc/" + docData.id + "'>" +
                                "<div class='doc-icon'></div>" +
                                "<div class='doc-info'>" +
                                "<div class='doc-title'>" + docData.title + "</div>" +
                                "<div class='doc-meta'>" +
                                "<span>" + fileSize(docData.fileSize) + "</span>" +
                                "<span>上传：" + docData.uploaderName + "</span>" +
                                "</div></div></a></li>"
                        )
                    }
                } else {
                    $("#favA").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    //更多下载
    function getMoreDowns() {
        var i;
        downpage += 1;
        var page;
        $.ajax({
            url: ctx + '/m/user/getmore',
            data: {getType: 'downloads', page: downpage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var docData = getData[i];
                        $("#downlist").append(
                                "<li class='" + docData.fileType + " clearfix' data-docid= ''>" +
                                "<a class='doc-content' href='" + ctx + "/m/doc/" + docData.id + "'>" +
                                "<div class='doc-icon'></div>" +
                                "<div class='doc-info'>" +
                                "<div class='doc-title'>" + docData.title + "</div>" +
                                "<div class='doc-meta'>" +
                                "<span>" + fileSize(docData.fileSize) + "</span>" +
                                "<span>上传：" + docData.uploaderName + "</span>" +
                                "</div></div></a></li>"
                        )
                    }
                } else {
                    $("#downA").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    //更多上传
    function getMoreUploads() {
        var i;
        uppage += 1;
        var page;
        $.ajax({
            url: ctx + '/m/user/getmore',
            data: {getType: 'uploads', page: uppage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var docData = getData[i];
                        $("#uplist").append(
                                "<li class='" + docData.fileType + " clearfix' data-docid= ''>" +
                                "<a class='doc-content' href='" + ctx + "/m/doc/" + docData.id + "'>" +
                                "<div class='doc-icon'></div>" +
                                "<div class='doc-info'>" +
                                "<div class='doc-title'>" + docData.title + "</div>" +
                                "<div class='doc-meta'>" +
                                "<span>" + fileSize(docData.fileSize) + "</span>" +
                                "<span>阅读：" + docData.views + "</span>" +
                                "</div></div></a></li>"
                        )
                    }
                } else {
                    $("#upA").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    //更多提问
    function getMorePosts() {
        var i;
        postspage += 1;
        var page;
        $.ajax({
            url: ctx + '/m/user/getmore',
            data: {getType: 'posts', page: postspage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var docData = getData[i];
                        var postsAvatar = '/files/' + docData.userAvatar;
                        if (docData.userAvatar == null)
                            postsAvatar = '/assets/images/default_avatar.png';
                        $("#qalist").append(
                                "<a class='qa-list-item' href='" + ctx + "/m/posts/" + docData.id + "'>" +
                                "<div class='qa-item-profile'> " +
                                "<img class='avatar' src='" + postsAvatar + "' alt=''/></div>" +
                                "<div class='qa-item-info'> " +
                                "<div class='qa-title'> " + docData.title + "</div>" +
                                "<div class='qa-meta'><span><i class='icon-calendar'></i> " + longToDate(new Date(docData.createTime)) + "</span>" +
                                " <span><i class='icon-eye'></i> " + docData.views + "</span>" +
                                " <span><i class='icon-bubble'></i> " + docData.commentsNum +
                                "</span></div></div></a>"
                        )
                    }
                } else {
                    $("#postsA").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    //时间格式转换
    function longToDate(time) {
        var year = time.getFullYear();	   //年</span>
        var month = time.getMonth() + 1;  //月
        var day = time.getDate();		 //日
        var hh = time.getHours();	   //时
        var mm = time.getMinutes();	//分

        var str = year + "-";
        if (month < 10)
            str += "0";
        str = str + month + "-";
        if (day < 10)
            str += "0";
        str = str + day + " ";
        if (hh < 10)
            str += "0";
        str = str + hh + ":";
        if (mm < 10)
            str += "0";
        str = str + mm;
        return (str);
    }

    $(document).ready(function () {

        $("#marquee").marquee({yScroll: "bottom", showSpeed: 1000});

        var mySwiper = new Swiper('.swiper-banner', {
            pagination: '.swiper-banner-pagination',
            paginationClickable: '.swiper-pagination',
            nextButton: '.swiper-button-next',
            prevButton: '.swiper-button-prev',
            loop: true,
            autoplay: 4000
        });

        var swiper = new Swiper('.swiper-container', {
            pagination: '.swiper-pagination',
            slidesPerView: 4,
            paginationClickable: true
        });

        //usercenter view
        var signScore = 1;
        var $scoreContainer = $('.profile-score');
        var $scoreNum = $('.profile-score span');
        $(document).on('click', '.profile .sign-btn', function () {
            var left = parseInt($scoreNum.position().left) + 10,
                    top = parseInt($scoreNum.position().top) - 10;
            $('.profile-score').append('<div class="sign-score color-red"><b>+' + signScore + '<\/b></\div>');
            $('.sign-score').css({
                'position': 'absolute',
                'z-index': '1',
                'left': left + 'px',
                'top': top + 'px'
            }).animate({
                top: top - 10,
                left: left + 10
            }, 'slow', function () {
                $(this).fadeIn('fast').remove();
                var Num = parseInt($scoreNum.text());
                Num += signScore;
                $scoreNum.text(Num);
            });
            return false;
        });

        //public view
        $(".userbar .item").click(function (e) {
            $(this).ripple();

            //状态切换
            $(this).addClass('act').siblings().removeClass('act');
        });

        $('.userbar .item.home').click(function () {
            $('.container#home').show().siblings('.container').hide();
        });

        $('.userbar .item.publish').click(function () {
            $('.container#publish').show().siblings('.container').hide();
        });

        $('.userbar .item.usercenter').click(function () {
            $('.container#usercenter').show().siblings('.container').hide();
        });

        //初始化视图，移植时切记此处放最后
        $('.container#home').show().siblings('.container').hide();

        //定位视图
        var view = window.location.hash.substring(1);
        if (view == null || view == '') {
            view = 'home';
        }
        $('.container#' + view).show().siblings('.container').hide();
        $('.userbar .item.' + view).addClass('act').siblings().removeClass('act');

    });
</script>
</body>
</html>
