<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/swiper.jquery.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script>
        $(function () {
            var swiper = new Swiper('.swiper-container', {
                nextButton: '.swiper-button-next',
                prevButton: '.swiper-button-prev',
                pagination: '.swiper-pagination',
                paginationClickable: true,
                // Disable preloading of all images
                preloadImages: false
            });
        })
    </script>
    <style>
        .swiper-container {
            width: 100%;
            height: 100%;
        }

        .swiper-slide {
            text-align: center;
            font-size: 18px;
            height: 180px;
            background: #fff;
        }

        .swiper-slide img {
            width: auto;
            height: auto;
            max-width: 100%;
            max-height: 100%;
            -ms-transform: translate(-50%, -50%);
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
            position: absolute;
            left: 50%;
            top: 50%;
        }
    </style>
    <title>商品信息详情</title>
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

            <div class="title">商品交易</div>
        </div>
    </header>

    <div class="content clearfix">
        <div class="page market-detail">
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <c:forEach items="${commodity.commodityImgs}" var="img">
                        <div class="swiper-slide">
                            <img class="market-preview" src="${bt:url(img.path,"/files/" ,"/assets/images/logo.jpg" )}" alt="${fn:escapeXml(commodity.name)}"/>
                        </div>
                    </c:forEach>
                </div>
                <!-- Add Pagination -->
                <div class="swiper-pagination"></div>
                <!-- Navigation -->
                <div class="swiper-button-next swiper-button-white"></div>
                <div class="swiper-button-prev swiper-button-white"></div>
            </div>

            <div class="market-header">
                <div class="market-title">${fn:escapeXml(commodity.name)}</div>
                <div class="market-meta">
                    <div class="market-price"><i class="fa fa-cny"></i> <em>
                        <c:if test="${commodity.price!=0}">${commodity.price}</c:if>
                        <c:if test="${commodity.price==0}">面议</c:if> </em>
                    </div>
                    <div class="market-location">
                        <i class="icon-pointer"></i> ${fn:escapeXml(commodity.area)}
                    </div>
                </div>
            </div>

            <div class="market-intro">
                <div class="market-intro-title">商品介绍</div>
                <pre class="market-intro-content">
${fn:escapeXml(commodity.information)}
                </pre>
            </div>

            <div class="market-info">
                <dl>
                    <dt>商品详情：</dt>
                    <dd>
                        <c:if test="${commodity.newDegree == 0}">不限</c:if>
                        <c:if test="${commodity.newDegree == 1}">全新</c:if>
                        <c:if test="${commodity.newDegree == 2}">二手</c:if>
                    </dd>
                </dl>
                <dl>
                    <dt>商家电话：</dt>
                    <dd>${fn:escapeXml(commodity.phone)}</dd>
                </dl>
                <dl>
                    <dt>商家微信：</dt>
                    <dd>${fn:escapeXml(commodity.weixin)}</dd>
                </dl>
            </div>
        </div>
    </div>

    <footer></footer>
</div>

<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    // 微信分享
    var title = '${commodity.name}';
    if (title == '') title = '大牛数控文库';
    var imgUrl = 'http://www.d6sk.com/files/${commodity.cover}';
    if (imgUrl == 'http://www.d6sk.com/files/') imgUrl = 'http://www.d6sk.com/assets/images/logo(500).jpg';
    var desc = '${fn:escapeXml(commodity.information)}';
    if (desc == '') desc = '大牛数控-国内最大的数控技术交流社区';
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
