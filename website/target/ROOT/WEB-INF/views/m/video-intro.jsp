<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <!-- player skin -->
    <link rel="stylesheet" href="${assets}/plugins/flowplayer/skin/functional.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
    <script type="text/javascript" src="${assets}/plugins/flowplayer/flowplayer.min.js"></script>
    <title>${fn:escapeXml(section.name)}</title>
</head>

<body>
<div class="container" style="padding-bottom: 50px;">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()">
                    <i class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/user/center"></a>
            </div>

            <div class="title">${fn:escapeXml(section.name)}</div>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <div class="video-intro">
            <!--<div class="preview-img" style="background-image: url(../images/demo/510.jpg)"></div>-->
            <div class="player is-splash" data-embed="false" style="height: 200px;background-image:url('/files/${vodVolumes.cover}');"></div>
            <div class="section tabs">
                <ul class="section-tab tabs-nav clearfix">
                    <li class="tab-menu tabs-nav-item current">详情</li>
                    <li class="tab-menu tabs-nav-item">目录</li>
                    <%--<li class="tab-menu tabs-nav-item">评论 (4)</li>--%>
                </ul>
                <div class="section-content tabs-panel">
                    <div class="tab">
                        <div class="tutorial-intro">
                            <div class="intro-info">
                                <%--<div class="intro-score">--%>
                                <%--<div class="score-txt">牛人币</div>--%>
                                <%--<div class="score-num">${vodVolumes.costScore}</div>--%>
                                <%--&lt;%&ndash;<button class="buyBtn">购买</button>&ndash;%&gt;--%>
                                <%--</div>--%>
                                <div class="intro-title">${fn:escapeXml(vodVolumes.name)}</div>
                                <ul class="intro-meta clearfix">
                                    <li>课时：${vodVolumes.totleNum}</li>
                                    <li>讲师：${fn:escapeXml(vodVolumes.lecturer)}</li>
                                    <li>牛人币：${vodVolumes.costScore}</li>
                                </ul>
                            </div>
                            <div>
                                <%--<p><strong>摘要</strong></p>--%>
                                <%--<p>${fn:escapeXml(vodVolumes.summary)}</p>--%>
                                <%--<p><strong>介绍</strong></p>--%>
                                <p>${vodVolumes.introduction}</p>
                                <%--<p><strong>内容</strong></p>--%>
                                <%--<p>${fn:escapeXml(vodVolumes.content)}</p>--%>
                                <%--<p><button onclick="goBuy()">购买</button></p>--%>
                            </div>
                        </div>
                    </div>

                    <div class="tab">
                        <div class="tutorial-chapter">
                            <c:forEach items="${chapters}" var="c">
                                <div class="chapter">
                                    <div class="chapter-header clearfix">
                                        <div class="chapter-num">第 ${c.sorting} 章</div>
                                        <div class="chapter-title">${fn:escapeXml(c.name)}</div>
                                    </div>
                                    <ul class="chapter-list">
                                        <c:forEach items="${c.vodSections}" var="s">
                                            <a class="list-item linkcolor" href="${ctx}/m/video/intro/${vodVolumes.id}?sectionId=${s.id}">
                                                    ${fn:replace(s.name,".mp4" ,"" )}
                                                    <%--${fn:substringBefore(s.name, ".mp4")}--%>
                                                    <%--<span class="tips">--%>
                                                    <%--（<bt:durationToString duration="${s.duration}"/>）--%>
                                                    <%--</span>--%>
                                                <c:if test="${s.free == true}">
                                                    <span class="label label-red marginleft5">免费试听</span>
                                                </c:if>
                                            </a>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <footer></footer>
    <div class="page-mask" style="display: none;"></div>
</div>

<div class="toolbar">
	<div class="container">
        <div class="toolbar-btn js-buy-btn" onclick="goBuy()">订阅课程</div>
    </div>
</div>

<%--登录--%>
<div class="dialog" id="dialogLogin">
    <div class="container">
        <div class="dialog-box">
            <div class="dialog-header">登录 <i class="fa fa-close close"></i></div>
            <div class="dialog-content">
                <form action="#" method="post">
                    <ul class="dialog-group">
                        <li class="clearfix">
                            <label>手机号</label>
                            <input class="dialog-input" id="mobileLogin" name="mobile" type="text"
                                   placeholder="请输入11位手机号码" onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="11"
                                   oninput="OBJ_formcheck.checkoninput(11,OBJ_formcheck.checkFormatM,'mobileLogin','loginMgs','phone','手机号码格式不对');">
                            <input name="loginTimes" id="loginTimes" type="number" hidden="hidden" value="1">
                        </li>
                        <li class="clearfix">
                            <label>密&nbsp;&nbsp;&nbsp;码</label>
                            <input class="dialog-input" type="password" id="passwordLogin"
                                   onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" placeholder="请输入6-20位有效密码"
                                   maxlength="20">
                        </li>
                        <li class="clearfix" id="captchaLi" hidden="hidden">
                            <label>验证码</label>
                            <img class="captcha-img" id="getCaptcha" src="" alt="点击图片更新"/>
                            <input class="dialog-input captcha" type="text" id="captchaLogin" placeholder="看不清？请点击图片"
                                   onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" maxlength="6">
                        </li>
                        <li class="clearfix" id="loginMgs" hidden>
                            <%--<label style="color: red" hidden>提示</label>--%>
                            <input class="dialog-input" id="loginMgs_tips" type="text" value="">
                        </li>
                    </ul>

                    <div class="dialog-btns clearfix">
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-confirm" onclick="OBJ_mLoginBox.login()">登录</button>
                        </div>
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-cancel"
                                    onclick="window.location.href='${ctx}/m/auth/register'">注册
                            </button>
                        </div>
                    </div>

                    <div class="third-group">
                        <div class="third-group-title">使用以下账号登录</div>
                        <div class="third-icon">
                            <dl class="weixin" id="weiXinLogin" hidden="hidden">
                                <dd></dd>
                                <dt>微信</dt>
                            </dl>
                            <dl class="qq" id="qqLogin">
                                <dd></dd>
                                <dt>QQ</dt>
                            </dl>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>
<%--账号绑定--%>
<div class="dialog" id="dialogBind">
    <div class="container">
        <div class="dialog-box">
            <div class="dialog-header">账号绑定 <i class="fa fa-close close"></i></div>
            <div class="dialog-content">
                <form action="#" method="post">
                    <ul class="dialog-group">
                        <c:if test="${sessionScope!=null&&sessionScope.user!=null}">
                            <input id="type" value="${sessionScope.user.loginType}" hidden/>
                            <input id="openid" value="${sessionScope.user.openid}" hidden/>
                        </c:if>
                        <li class="clearfix">
                            <label>手机号</label>
                            <input id="mobileBind" class="dialog-input" type="text" name="mobile"
                                   onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="11"
                                   onchange="OBJ_formcheck.checkFormatM('mobileBind','bindMgs','phone','手机号码格式不对');">
                        </li>
                        <li class="clearfix">
                            <label>验证码</label>
                            <button class="captcha-btn" type="button" id="bind_sendmsg_button"
                                    onclick="OBJ_mBindAccount.sendMsg('mobileBind');">获取验证码
                            </button>
                            <button class="captcha-btn" id="bind_msg_end" type="button" style="display:none;"><span
                                    id="bind_sendmsg_time_text">60</span>秒后重发
                            </button>
                            <input class="dialog-input captcha" id="code" type="text"
                                   onchange="return OBJ_formcheck.checkFormatM('code','bindMgs','code','请输入有效的验证码')"
                                   onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="6">
                        </li>
                        <li class="clearfix">
                            <label>密&nbsp;&nbsp;&nbsp;码</label>
                            <input class="dialog-input" type="password" id="password"
                                   onchange="return OBJ_formcheck.checkFormatM('password','bindMgs','password','密码为空或不符合规则');"
                                   onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" placeholder="请输入6-20位有效密码">
                        </li>
                        <li class="clearfix" id="bindMgs" hidden>
                            <label>提示</label>
                            <input class="dialog-input" id="bindMgs_tips" type="text" value="">
                        </li>
                    </ul>

                    <div class="dialog-btns clearfix">
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-confirm" onclick="OBJ_mBindAccount.bind()">绑定</button>
                        </div>
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-cancel" id="bindcancel">取消</button>
                        </div>
                    </div>

                    <a class="dialog-tips" href="javascript:;">
                        <i class="fa fa-exclamation-circle"></i> 您需要绑定站内账号，才能够继续观看
                    </a>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript" src="${assets}/js/mLoginBox.js"></script>
<script type="text/javascript" src="${assets}/js/mBindAccount.js"></script>
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
    var costScore = ${vodVolumes.costScore};
    var memberScore = 0;
    if (costScore && costScore > 0) memberScore = Math.round(costScore * 0.5);
    var isFree = false;
    <c:if test="${section.free}">
    isFree = ${section.free};
    </c:if>
    var isBuy = false;
    <c:if test="${sessionScope.user.id != null}">
    checkIsBuy();
    </c:if>

    function checkIsBuy() {
    	$.get('${ctx}/video/checkIsBuyVol', {volumeId: "${vodVolumes.id}"}, function (data) {
    		isBuy = data;
            if (data == true) {
                $('.js-buy-btn').addClass("active").html("已订阅").unbind("click");
            }
        });
    }

    function goBuy(){
        if(isBuy == false)
            location.href='${ctx}/m/user/videoPay?sectionId=${section.id}';
    }

    $(function () {
        var $player = $(".player").flowplayer({
            clip: {
                sources: [{
                    type: "video/mp4",
                    src: "${section.dealURL}"
                }]
            }
        });
        var api = $player.eq(0).data("flowplayer");
        api.play(0);
        api.on("ready", function (e, api) {
            if (api.playing == true) {
                if (costScore == 0 || isFree || isBuy) {
                    return true;
                } else {
                    var ongoing = setInterval(function () {
                        if (api.playing == true) {
                            if (isBuy) {
                                api.resume();
                                clearInterval(ongoing);
                            } else {
                                api.pause();
                                checkLogin();
                            }
                        }
                    }, 1000);
                }
            }
        });

        // 登录&绑定
        function checkLogin() {
            if (!isLogin) {
                clickLogin();
            } else {
                if (isBind) {
                    if (!isBuy) {
                        location.href = '${ctx}/m/user/videoPay?sectionId=${section.id}';
                        <%--var down = layer.open({--%>
                        <%--content: '一次购买，无限次学习，普通会员 ' + costScore + ' 牛人币，<a href="${ctx}/m/user/buyVip" target="blank" style="color:red;">VIP尊享价</a> ' + memberScore + ' 牛人币，立即支付？',--%>
                        <%--btn: ['确定', '取消'],--%>
                        <%--yes: function (index) {--%>
                        <%--layer.close(index);--%>
                        <%--videoBuy(1);--%>
                        <%--}--%>
                        <%--});--%>
                    }
                    return true;
                } else {
                    clickBind();
                }
                return true;
            }
        }

        function clickLogin() {
            if (!$('#dialogLogin').is(':visible')) {
                $('#dialogLogin').show();
                $('.page-mask').show();
                $(document).one('click', function () {
                    $('#dialogLogin').hide();
                    $('.page-mask').hide();
                });
            } else {
                $('#dialogLogin').hide();
                $('.page-mask').hide();
            }
        }

        function clickBind() {
            if (!$('#dialogBind').is(':visible')) {
                $('#dialogBind').show();
                $('.page-mask').show();
                $(document).one('click', function () {
                    $('#dialogBind').hide();
                    $('.page-mask').hide();
                })
            } else {
                $('#dialogBind').hide();
                $('.page-mask').hide();
            }
        }

    });

    //QQ第三方登录
    $('#qqLogin').click(function () {
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=" + location.href, "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    });
    //微信第三方登录
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

    // 微信分享
    var title = '${fn:escapeXml(vodVolumes.name)}';
    if (title == '') title = '大牛数控课堂';
    var imgUrl = 'http://www.d6sk.com/assets/images/logo_video.jpg';
    var desc = '${fn:escapeXml(vodVolumes.content)}';
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
</html>
