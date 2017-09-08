<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0"/>
    <title>${fn:escapeXml(doc.title)}</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.ext.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.leoDialog.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
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
            <a id="prevPage" class="prev" href="javascript:history.back();">
                <i class="fa fa-chevron-left"></i>
            </a>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/user/center"></a>
            </div>
            <div class="title">
                <c:if test="${category == null}"> 大牛数控</c:if>
                <c:if test="${category != null}"> ${fn:escapeXml(category.name)}</c:if>
            </div>
        </header>
        <div class="content pdb60 clearfix reader-box">
            <article>
                <c:if test="${sessionScope.user == null}">
                    <a class="tips-bar" href="javascript:;">
                        - 登陆可继续阅读，下载可全部查阅 -
                    </a>
                </c:if>
                <div class="doc-header">
                    <h1>${fn:escapeXml(doc.title)}</h1>

                    <div class="btns-bar clearfix">
                        <span class="filetype">${doc.fileType}文件</span>
                    </div>
                </div>

                <div class="meta">

                    <ul class="doc-meta clearfix">
                        <li><strong>阅读：</strong>${doc.views}</li>
                        <li><strong>上传者：</strong><c:if test="${doc.user.nickname==null}">系统管理员</c:if>
                            ${fn:escapeXml(doc.user.nickname)}</li>
                        <li><strong>牛人币：</strong><span class="color-red">${doc.costScore}</span></li>
                    </ul>
                    <ul class="meta-list">
                        <!--<p><strong>关键字：</strong>90,DS1449,</p>-->
                        <li><strong>简介 : </strong>${fn:escapeXml(doc.summary)}</li>
                    </ul>
                </div>
                <c:if test="${images != null}">
                    <div class="pics">
                        <c:if test="${sessionScope.user == null && images.size() > 0}">
                            <img class="image-page" src="${files}/${images.get(0).path}" alt=""/>
                        </c:if>
                        <c:if test="${sessionScope.user != null}">
                            <c:forEach items="${images}" var="image">
                                <%--<img class="image-page" src="${files}/${image.path}" alt=""/>--%>
                                <img class="image-page" src="${assets}/images/grey.gif" data-original="${files}/${image.path}" alt=""/>
                            </c:forEach>
                        </c:if>
                    </div>
                </c:if>
                <c:if test="${!empty content}">
                    <div class="pics" style="text-indent: 2em;">${content}</div>
                </c:if>
                <c:if test="${doc.fileType.matches('jpg|png|gif|bmp|ico')}">
                    <div class="pics">
                        <img class="image-page" src="${files}/${doc.filePath}" alt=""/>
                    </div>
                </c:if>
                <!-- 分页 -->
                <%--<bt:pagination data="${docs}"/>--%>

                <div class="doc-footer">
                    ${footerContent}
                </div>
            </article>
        </div>
        <footer>
        </footer>
        <div class="page-mask" style="display: none;"></div>
    </div>
</div>

<div class="float-bar">
    <div class="container">
        <ul class="bar-list clearfix">
            <li>
                <a id="download" class="bar-btn download-btn btn-download" href="javascript:;" onclick="">
                    <i class="fa fa-download"></i> 下载</a>
            </li>
            <li>
                <c:if test="${isCollect == 'unLogin' || isCollect == 'notCollect'}">
                    <a class="bar-btn" id="collect" href="javascript:;" onclick="OBJ_mLoginBox.checkLogin(event,operate.clickCollect)"><i class="fa fa-star"></i> 收藏</a>
                </c:if>
                <c:if test="${isCollect == 'collect'}">
                    <a class="bar-btn" id="collect" href="javascript:;" onclick="OBJ_mLoginBox.checkLogin(event,operate.clickCollect)"><i class="fa fa-star"></i>取消收藏</a>
                </c:if>
            </li>
            <li>
                <a class="bar-btn share-btn" href="javascript:;"><i class="fa fa-share"></i> 分享</a>
            </li>
        </ul>
        <div class="share-bar">
            <div class="share-list">
                <!-- JiaThis Button BEGIN -->
                <div class="jiathis_style_m"></div>
                <!-- JiaThis Button END -->
            </div>
            <div class="close">
                <i class="fa fa-close"></i>
            </div>
        </div>
    </div>
</div>
<jsp:include page="login-div.jsp"/>

<div class="page-tips" id="down-tips">
    <div class="container">
        <div class="clearfix">
            <div class="tips-arrow"></div>
        </div>
        <div class="tips-txt">
            <p>需要切换浏览器打开才能够下载哟</p>
            <p>点击右上角菜单，选择【在浏览器中打开】</p>
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
            <p>分享自己的文档，还可以赚钱哦！</p>
        </div>
    </div>
</div>

</body>
<script type="text/javascript" src="http://v3.jiathis.com/code/jiathis_m.js" charset="utf-8"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>
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

    var id = ${doc.id};
    var score = ${doc.costScore};
    var memberScore = 0;
    if (score && score > 0) memberScore = Math.round(score * 0.5);
    var mobile = '';
    var isDownload = ${isDownload};
    <c:if test="${sessionScope.user.mobile != null}">mobile = ${sessionScope.user.mobile};
    </c:if>

    function doDownload() {
        $.ajax({
            type: 'get',
            url: '${ctx}/doc/dl/' + id,
            success: function (data) {
                isDownload = true;
                window.location.href = "${ctx}/doc/dl?c=" + data['data'];
            },
            error: function (req) {
                var errormsg = JSON.parse(req.responseText);
                if (errormsg) {
                    layer.open({
                        content: errormsg.message + "前往 <a href='${ctx}/m/user/scoreRecharge' target='_blank' style='color:red;text-decoration:underline;'>购买牛人币</a> (<a href='${ctx}/m/auth/scoreExplain' target='_blank' class=''><small>牛人币说明</small></a>)",
                        btn: '我知道了'
                    });
                } else {
                    layer.open({
                        content: '下载失败',
                        btn: '我知道了'
                    });
                }
                console.warn(req);
            }
        });
    }

    var operate = {
        clickDownload: function () {
            if (score == 0 || isDownload) {
                doDownload();
            } else if (mobile == '') {
                layer.open({
                    content: '请先绑定手机号',
                    btn: ['确定', '取消'],
                    yes: function (index) {
                        layer.close(index);
                        location.href = "${ctx}/m/user/account";
                    }
                });
            } else {
                var down = layer.open({
                    content: '一次购买，无限次下载，普通会员 ' + score + ' 牛人币，<a href="${ctx}/m/user/buyVip" target="blank" style="color:red;">VIP尊享价</a> ' + memberScore + ' 牛人币，立即支付？',
                    btn: ['确定', '取消'],
                    yes: function (index) {
                        layer.close(index);
                        doDownload();
                    }
                });
            }
        },
        clickCollect: function () {
            var type = $('#collect').html();
            if (type == '<i class="fa fa-star"></i> 收藏') {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/doc/collect?id=' + id,
                    success: function (data) {
                        $('#collect').html('<i class="fa fa-star"></i> 取消收藏');
                    },
                    error: function (req) {
                        console.log(req);
                        layer.open({
                            content: '操作失败！',
                            btn: '我知道了'
                        });
                    }
                });
            } else {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/doc/uncollect?id=' + id,
                    success: function (data) {
                        $('#collect').html('<i class="fa fa-star"></i> 收藏');
                    },
                    error: function (req) {
                        console.log(req);
                        layer.open({
                            content: '操作失败！',
                            btn: '我知道了'
                        });
                    }
                });
            }
        },
        clickReload: function () {
            location.reload();
        }
    };

    //    $(document).scroll(function(){
    //        if($(document).scrollTop() > $('.btns-bar').offset().top){
    //            $('.btns-bar').addClass('fixed').css('top',0);
    //        }else{
    //            $('.btns-bar').removeClass('fixed').css('top',0);
    //        }
    //    });
    $('.float-bar .share-btn').click(function (e) {
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

    $('.float-bar .close').click(function () {
        $('.float-bar .share-bar').hide();
    });

    $('#download').click(function (e) {
        if ($('.micromessenger').length > 0) {
            e.stopPropagation();
            var ld2 = leoDialog.window({
                isWindow: false,
                modal: true,
                content: $('#down-tips')
            });
        } else {
            OBJ_mLoginBox.checkLogin(event, operate.clickDownload);
        }
    });

    $('.tips-bar').click(function () {
        OBJ_mLoginBox.checkLogin(event, operate.clickReload);
    });

    //QQ第三方登录
    $('#qqLogin').click(function () {
        var url = location.href;
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=" + url,
                "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    });

    $(function () {
        //图片懒加载
        $('.reader-box img').lazyload();

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

    // 微信分享
    var title = '${doc.title}';
    if (title == '') title = '大牛数控文库';
    var imgUrl = 'http://www.d6sk.com/files/${fcategory.icon}';
    if (imgUrl == 'http://www.d6sk.com/files/') imgUrl = 'http://www.d6sk.com/assets/images/logo(500).jpg';
    var desc = '${fn:escapeXml(doc.summary)}';
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

    //置顶/返回首页
    $(window).bind('scrollstart', function () {
        $('.float-btns .float-menu .menu-btn').animate({opacity: .6}, 'fast');
    });

    $(window).bind('scrollstop', function () {
        $('.float-btns .float-menu .menu-btn').animate({opacity: .2}, 'fast');
    });

    $('.float-btns .float-menu .menu-btn.menu-totop').click(function () {
        $('html,body').animate({scrollTop: 0}, 'fast');
    })
</script>

</html>
