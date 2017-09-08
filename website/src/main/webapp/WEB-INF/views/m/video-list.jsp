<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>大牛数控课堂</title>

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
                <a class="menu-item menu-user" href="${ctx}/m/user/center"></a>
            </div>

            <div class="title">大牛数控课堂</div>
        </div>

        <div class="tabs clearfix">
            <a href="${ctx}/m/">首页</a>
            <a href="${ctx}/m/doc/category">文库</a>
            <a href="${ctx}/m/video/" class="current">课堂</a>
            <a href="${ctx}/m/posts/category">社区</a>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <div class="page-course">

            <div class="quick-nav bg-white">
                <a href="javascript:;" onclick="choseTag(1)">
                    <div class="quick-icon icon-1"></div>
                    <div class="quick-name">编程设计</div>
                </a>
                <a href="javascript:;" onclick="choseTag(2)">
                    <div class="quick-icon icon-2"></div>
                    <div class="quick-name">数控维修</div>
                </a>
                <a href="javascript:;" onclick="choseTag(3)">
                    <div class="quick-icon icon-3"></div>
                    <div class="quick-name">独门绝技</div>
                </a>
                <a href="javascript:;" onclick="choseTag(4)">
                    <div class="quick-icon icon-4"></div>
                    <div class="quick-name">匠人笔记</div>
                </a>
            </div>

            <c:if test="${c1Obj == null}">
                <div class="category-toolbar">
                    <div class="toolbar-info">分类： 全部 - 全部</div>
                    <a class="toolbar-filter" href="javascript:;">筛选<i class="fa fa-angle-down"></i></a>
                </div>
                <div class="category-list" style="display: none">
                    <a class="clearfix" href="javascript:;">
                        <div class="category-icon"></div>
                        <div class="category-title">全部</div>
                    </a>
                    <c:forEach items="${c1s}" var="c1">
                        <a class="clearfix" href="${ctx}/m/video/?c1=${c1.id}&tagId=${tagId}">
                            <div class="category-icon"></div>
                            <div class="category-title">${fn:escapeXml(c1.name)}</div>
                        </a>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${c1Obj != null}">
                <div class="category-toolbar">
                    <div class="toolbar-info">分类： ${fn:escapeXml(c1Obj.name)} - ${bt:out(c2Obj.name,'全部')}</div>
                    <a class="toolbar-filter" href="javascript:;">筛选<i class="fa fa-angle-down"></i></a>
                </div>
                <div class="category-list" style="display: none">
                    <a class="clearfix" href="${ctx}/m/video/">
                        <div class="category-icon"></div>
                        <div class="category-title">全部</div>
                    </a>
                    <c:forEach items="${c2s}" var="c2">
                        <a class="clearfix" href="${ctx}/m/video/?c1=${c1Obj.id}&c2=${c2.id}&tagId=${tagId}">
                            <div class="category-icon"></div>
                            <div class="category-title">${fn:escapeXml(c2.name)}</div>
                        </a>
                    </c:forEach>
                </div>
            </c:if>
            <div class="course-list">
                <c:forEach items="${videos.content}" var="v">
                    <a class="course-item clearfix" href="${ctx}/m/video/intro/${v.id}">
                        <img class="course-img" src="${bt:url(v.cover,'/files/','/assets/images/demo/ex_1.jpg')}" alt="${fn:escapeXml(v.name)}">
                        <div class="course-info">
                            <div class="course-title">${fn:escapeXml(v.name)}</div>
                            <div class="course-meta"><span>课时：${v.totleNum}</span>
                                <span class="marginleft15">浏览：<c:if test="${v.views > 999}">999+</c:if><c:if test="${v.views <= 999}">${v.views}</c:if></span>
                            </div>
                            <c:if test="${v.costScore > 0}">
                                <div class="course-price cost">${v.costScore} 牛人币</div>
                            </c:if>
                            <c:if test="${v.costScore < 1}">
                                <div class="course-price free">免费</div>
                            </c:if>
                        </div>
                    </a>
                </c:forEach>
                <bt:pagination data="${videos}"/>
            </div>
        </div>
    </div>

    <footer></footer>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script>
        $(function () {
            var category = (function () {
                var _menuState = false;
                return {
                    openMenu: function () {
                        $('.category-list').show();
                        $('.course-list').hide();
                        _menuState = true;
                    },
                    closeMenu: function () {
                        $('.category-list').hide();
                        $('.course-list').show();
                        _menuState = false;
                    },
                    getMenuState: function () {
                        return _menuState;
                    }
                }
            })();

            //demo
            $('.toolbar-filter').click(function () {
                if (!category.getMenuState())
                    category.openMenu();
                else
                    category.closeMenu();
            });

            $('.category-list a').click(function () {
                category.closeMenu();
            })
        });

        function choseTag(tagId) {
            var c1 = '${c1Obj.id}';
            var c2 = '${c2Obj.id}';
            location.href = '${ctx}/m/video/?c1=' + c1 + '&c2=' + c2 + '&tagId=' + tagId;
        }

        // 微信分享
        var title = '大牛数控课堂';
        var imgUrl = 'http://www.d6sk.com/assets/images/logo_video.jpg';
        var desc = '大牛数控:国内最牛掰的数控技术文库,涵盖智能制造等各类数控学术参考资料';
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
</div>
</body>
</html>
