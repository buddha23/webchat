<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>数控资料查询</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
</head>

<body>
<div class="container">
    <header>
        <div class="wrapper">
            <div class="logo">
                <img src="${assets}/images/logo.jpg"/>
            </div>
            <div class="header-info">
                <h1>数控资料查询</h1>

                <p>随身的技术手册--数控技术人员的好帮手</p>
            </div>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <dl class="record-count">
            <c:if test="${!empty param.keyword}">
                <dt>共 ${articles.size()} 篇与“<em>${param.keyword}</em>”相关的技术文章</dt>
            </c:if>
        </dl>
        <table class="table-default">
            <colgroup>
                <col class="col2"/>
                <col class="col8"/>
            </colgroup>
            <thead>
            <tr>
                <th>序号</th>
                <th>标题</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${articles}" var="article" varStatus="s">
                <tr>
                    <td>${s.index + 1}</td>
                    <td><a href="${ctx}/m/article/${article.id}">${article.title}</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- 分页 -->
        <%--<div class="pagination-wrapper clearfix">
            <div class="pagination clearfix">
                <a class="paging current" href="#">1</a>
                <a class="paging" href="#">2</a>
                <a class="paging" href="#">3</a>
                <a class="paging" href="#">4</a>
                <a class="paging" href="#">5</a>
                <!--
                <a class="paging ellipsis" href="javascript:void(0);">...</a>
                <a class="paging disable" href="#">下一页</a>
                -->
                <a class="paging" href="#">下一页</a>
            </div>
        </div>--%>
    </div>
    <footer>
    </footer>
</div>
<script type="text/javascript" src="${assets}/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
    var title = '关于${param.keyword}的资料';
    var link = location.href;
    var imgUrl = 'http://www.d6sk.com/assets/images/logo.jpg';
    var desc = '随身的技术手册--数控技术人员的好帮手';

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
