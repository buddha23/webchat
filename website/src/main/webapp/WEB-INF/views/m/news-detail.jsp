<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0"/>
    <title>大牛数控资讯</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.ext.js"></script>
</head>

<body>
<div class="container">
    <div class="float-btns">
        <div class="float-menu">
            <a href="javascript:;" class="menu-btn menu-totop"></a>
            <a href="${ctx}/m/" class="menu-btn">首页</a>
        </div>
    </div>

    <div class="zoomable">
        <header>
            <div class="top">
                <div class="menu-left">
                    <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
                </div>

                <div class="menu-right">
                    <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
                </div>

                <div class="title">大牛数控资讯</div>
            </div>
        </header>

        <div class="content clearfix">
            <article>
                <div class="doc-header">
                    <h1>${fn:escapeXml(news.title)}</h1>
                </div>
                <div class="text">
                    ${news.content}
                </div>

                <div class="qr">
                    <img src="${assets}/images/qr.jpg" alt=""/>
                    <p>扫描二维码关注我们</p>
                </div>
            </article>
        </div>

        <footer>
        </footer>
    </div>
</div>
</body>

<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    $(function () {
        //置顶/返回首页
        $(window).bind('scrollstart', function () {
            $('.float-btns .float-menu .menu-btn').animate({opacity: .6}, 'fast');
        });

        $(window).bind('scrollstop', function () {
            $('.float-btns .float-menu .menu-btn').animate({opacity: .2}, 'fast');
        });

        $('.float-btns .float-menu .menu-btn.menu-totop').click(function () {
            $('html,body').animate({scrollTop: 0}, 'fast');
        });
    });

    // 微信分享
    var title = '大牛数控资讯';
    var imgUrl = 'http://www.d6sk.com/files/${news.imgUrl}';
    if (imgUrl == 'http://www.d6sk.com/files/') imgUrl = 'http://www.d6sk.com/assets/images/logo_posts.png';
    var desc = '${fn:escapeXml(news.title)}';
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
            title: '${news.title}',
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
</html>
