<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>商品信息列表</title>
</head>

<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()">
                    <i class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">商品交易</div>
        </div>
    </header>

    <div class="content pdb60 clearfix">
        <div class="page market">
            <%--<c:if test="${imgFileList.size()>0}">--%>
            <%--<c:forEach items="${imgFileList}" var="img" varStatus="imgStatus">--%>
            <%--<div class="market-banner">--%>
            <%--<a href="${bt:out(img.link,'javascript:void(0);')}" target="_blank"><img src="${ctx}/files/user/${img.path}" alt=""/></a>--%>
            <%--</div>--%>
            <%--</c:forEach>--%>
            <%--</c:if>--%>
            <div class="market-banner">
                <img src="${assets}/images/banner/3633b0058d9466ba421cfbbdf4efd5be.jpg" alt=""/>
            </div>
            <div class="section tabs">
                <ul class="section-tab tabs-nav market-tab">
                    <li class="tab-menu tabs-nav-item current"><i class="fa fa-shopping-cart"></i> 今日出售</li>
                    <li class="tab-menu tabs-nav-item"><i class="fa fa-briefcase"></i> 今日急购</li>
                </ul>
                <div class="section-content tabs-panel">
                    <div class="tab">
                        <ul class="market-list" id="sellList">
                            <c:forEach items="${sells}" var="c">
                                <li>
                                    <a href="${ctx}/m/commodity/detail/${c.id}">
                                        <div class="market-preview"
                                             style="background-image:url(${bt:url(c.cover,"/files/" ,"/assets/images/preview_default.png" )})"
                                             alt=""></div>
                                        <div class="market-info">
                                            <div class="market-title">${fn:escapeXml(c.name)}</div>
                                            <div class="market-business clearfix">
                                                <em>
                                                    <i class="fa fa-cny"></i>
                                                    <span class="price">
                                                        <c:if test="${c.price!=0}">${c.price}</c:if>
                                                        <c:if test="${c.price==0}">面议</c:if>
                                                    </span>
                                                </em>
                                                <span class="name">${fn:escapeXml(c.userName)}</span>
                                            </div>
                                        </div>
                                    </a>
                                    <div class="market-meta clearfix">
                                        <div class="meta-item"><i class="icon-eye"></i> ${c.views}</div>
                                            <%--<div class="meta-item"><i class="icon-bubble"></i> 20</div>--%>
                                        <div class="meta-btns clearfix">
                                            <div class="meta-btn share-btn js-do-share" data-id="${c.id}"
                                                 data-title="${fn:escapeXml(c.name)}"
                                                 data-imgUrl="${bt:url(c.cover,"/files/" ,"/assets/images/preview_default.png" )}">
                                                <i class="icon-share"></i> 分享
                                            </div>
                                            <div class="meta-btn buy-btn" onclick="jumpDetail(${c.id})"><i
                                                    class="icon-basket-loaded"></i> 购买
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                        <c:if test="${sells.size()>=3}">
                            <a href="javascript:;" class="more-btn clearfix text-center js-moreSells"
                               onclick="getMoreSells()">加载更多</a>
                        </c:if>
                        <c:if test="${sells.size()<3}">
                            <a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>
                        </c:if>

                    </div>

                    <div class="tab">
                        <ul class="market-list" id="needList">
                            <c:forEach items="${needs}" var="c">
                                <li>
                                    <a href="${ctx}/m/commodity/detail/${c.id}">
                                        <div class="market-preview"
                                             style="background-image: url(${bt:url(c.cover,"/files/" ,"/assets/images/preview_default.png" )})"
                                             alt=""></div>
                                        <div class="market-info">
                                            <div class="market-title">
                                                    ${fn:escapeXml(c.name)}
                                            </div>
                                            <div class="market-business clearfix">
                                                <em>
                                                    <i class="fa fa-cny"></i>
                                                    <span class="price">
                                                        <c:if test="${c.price!=0}">${c.price}</c:if>
                                                        <c:if test="${c.price==0}">面议</c:if>
                                                    </span>
                                                </em>
                                                <span class="name">${fn:escapeXml(c.userName)}</span>
                                            </div>
                                        </div>
                                    </a>
                                    <div class="market-meta clearfix">
                                        <div class="meta-item"><i class="icon-eye"></i> ${c.views}</div>
                                            <%--<div class="meta-item"><i class="icon-bubble"></i> 20</div>--%>
                                        <div class="meta-btns clearfix">
                                            <div class="meta-btn share-btn js-do-share" data-id="${c.id}"
                                                 data-title="${fn:escapeXml(c.name)}"
                                                 data-imgUrl="${bt:url(c.cover,"/files/" ,"/assets/images/preview_default.png" )}">
                                                <i class="icon-share"></i> 分享
                                            </div>
                                            <div class="meta-btn order-btn" onclick="jumpDetail(${c.id})"><i
                                                    class="icon-cursor"></i> 约单
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                        <c:if test="${needs.size()>=3}">
                            <a href="javascript:;" class="more-btn clearfix text-center js-moreneeds"
                               onclick="getMoreNeeds()">加载更多</a>
                        </c:if>
                        <c:if test="${needs.size()<3}">
                            <a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <footer></footer>
</div>

<div class="float-bar">
    <div class="container">
        <div class="bar-btn-group clearfix">
            <a class="bar-btn" href="javascript:;" onclick="OBJ_mLoginBox.checkLogin(event,operate.jumpBuy)">
                <em>我要求购</em>
            </a>
            <a class="bar-btn" href="javascript:;" onclick="OBJ_mLoginBox.checkLogin(event,operate.jumpSell)">
                <em>我要出售</em>
            </a>
        </div>
    </div>
</div>

<div class="page-tips" id="share-tips">
    <div class="container">
        <div class="clearfix">
            <div class="tips-arrow"></div>
        </div>
        <div class="tips-txt">
            <p>分享到朋友圈，让好朋友们一起学习！</p>
            <p>分享自己的商品，进行愉快的交易吧！</p>
        </div>
    </div>
</div>

<jsp:include page="login-div.jsp"/>
<div class="page-mask" style="display: none;"></div>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.leoDialog.js"></script>
<script>
    var isLogin = false;
    var isBind = false;
    <c:if test="${!empty sessionScope.user}">
    isLogin = true;
    if (${sessionScope.user.mobile != null}) {
        isBind = true;
    } else if (${sessionScope.user.isBind == true}) {
        isBind = true;
    }
    </c:if>
    var ctx = '${ctx}';
    var needsPage = 1;
    var sellsPage = 1;
    function getMoreNeeds() {
        var i;
        needsPage += 1;
        $.ajax({
            url: ctx + '/m/commodity/getmore',
            data: {getType: 'needs', page: needsPage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var cd = getData[i];
                        var img = cd.cover === undefined ? '/assets/images/logo.jpg' : ('/files/' + cd.cover);
                        $("#needList").append(
                                '<li><a href="' + ctx + '/m/commodity/detail/' + cd.id + '">' +
                                '<div class="market-preview" style="background-image: url(' + ctx + img + ')"></div>' +
                                '<div class="market-info">' +
                                '<div class="market-title">' + cd.name + '</div>' +
                                '<div class="market-business clearfix">' +
                                '<em><i class="fa fa-cny"></i> <span class="price">' + cd.price + '</span></em>' +
                                '<span class="name">' + cd.userName + '</span>' +
                                '</div></div></a>' +
                                '<div class="market-meta clearfix">' +
                                '<div class="meta-item"><i class="icon-eye"></i> ' + cd.views + '</div>' +
                                '<div class="meta-btns clearfix">' +
                                '<div class="meta-btn share-btn js-do-share" data-id="' + cd.id + '" data-title="' + cd.name + '" data-imgUrl="' + img +'"><i class="icon-share"></i> 分享</div>' +
                                '<div class="meta-btn order-btn" onclick="jumpDetail(' + cd.id + ')"><i class="icon-cursor"></i> 约单</div>' +
                                '</div></div></li>'
                        );
                    }
                } else {
                    $(".js-moreneeds").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    function getMoreSells() {
        var i;
        sellsPage += 1;
        $.ajax({
            url: ctx + '/m/commodity/getmore',
            data: {getType: 'sells', page: sellsPage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var cd = getData[i];
                        var img = cd.cover === undefined ? '/assets/images/logo.jpg' : ('/files/' + cd.cover);
                        $("#sellList").append(
                                '<li><a href="' + ctx + '/m/commodity/detail/' + cd.id + '">' +
                                '<div class="market-preview" style="background-image: url(' + ctx + img + ')"></div>' +
                                '<div class="market-info">' +
                                '<div class="market-title">' + cd.name + '</div>' +
                                '<div class="market-business clearfix">' +
                                '<em><i class="fa fa-cny"></i> <span class="price">' + cd.price + '</span></em>' +
                                '<span class="name">' + cd.userName + '</span>' +
                                '</div></div></a>' +
                                '<div class="market-meta clearfix">' +
                                '<div class="meta-item"><i class="icon-eye"></i> ' + cd.views + '</div>' +
                                '<div class="meta-btns clearfix">' +
                                '<div class="meta-btn share-btn js-do-share" data-id="' + cd.id + '" data-title="' + cd.name + '" data-imgUrl="' + img +'"><i class="icon-share"></i> 分享</div>' +
                                '<div class="meta-btn buy-btn" onclick="jumpDetail(' + cd.id + ')"><i class="icon-basket-loaded"></i> 购买</div>' +
                                '</div></div></li>'
                        );
                    }
                } else {
                    $(".js-moreSells").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    function jumpDetail(commodityId) {
        location.href = '${ctx}/m/commodity/detail/' + commodityId;
    }
    var operate = {
        jumpBuy: function () {
            location.href = '${ctx}/m/commodity/publish?type=2';
        },
        jumpSell: function () {
            location.href = '${ctx}/m/commodity/publish?type=1';
        }
    };

    // 微信分享
    var title = '大牛数控文库';
    var imgUrl = 'http://www.d6sk.com/assets/images/logo(500).jpg';
    var desc = '大牛数控-国内最大的数控技术交流社区';
    var link = location.href;

    $('.js-do-share').click(function (e) {
        title = $(this).attr('data-title');
        link = '${ctx}/m/commodity/detail/' + $(this).attr('data-id');
        console.log(title);
        console.log(link);
        if ($('.micromessenger').length > 0) {
            e.stopPropagation();
            var ld3 = leoDialog.window({
                isWindow: false,
                modal: true,
                content: $('#share-tips')
            });
        } else {
            $('.float-bar .share-bar').show();
        }
    });

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
