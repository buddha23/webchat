<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>大牛数控文库搜索</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="${ctx}/m/">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu">
        </div>

        <div class="title">大牛数控</div>
    </header>

    <div class="content clearfix">
        <div class="doc-search">
            <div class="searchbar">
                <form action="${ctx}/m/doc/" method="get">
                    <div class="searchbox">
                        <button type="submit" class="searchbtn" id="searchbtn">
                            <i class="fa fa-search"></i>
                        </button>
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
            <c:if test="${docs.content.size()>0}">
                <dl class="record-count">
                    <c:if test="${!empty(param.content)}">
                        <dt>与“<em>${fn:escapeXml(param.content)}</em>”相关的文档</dt>
                    </c:if>
                    <dd>共${docs.totalPages}页， ${docs.totalElements} 篇文章</dd>
                </dl>
                <div class="record-list">
                    <c:forEach items="${docs.content}" var="video" varStatus="i">
                        <dl class="clearfix">
                            <dt>${i.count}</dt>
                            <dd><a href="${ctx}/m/doc/${video.id}">${fn:escapeXml(video.title)}</a></dd>
                        </dl>
                    </c:forEach>
                </div>
                <bt:pagination data="${docs}"/>
            </c:if>
        </div>
    </div>

    <footer>
    </footer>
</div>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    // 微信分享
    var title = '大牛数控文库';
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
