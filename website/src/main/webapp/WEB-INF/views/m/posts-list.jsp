<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>大牛数控社区</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" href="${assets}/plugins/froalaEditor/css/froala_editor.pkgd.min.css" type="text/css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/layer.js"></script>
    <!-- froala editor -->
    <script src="${assets}/plugins/froalaEditor/js/froala_editor.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/languages/zh_cn.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/align.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/colors.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/draggable.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/code_view.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/code_beautifier.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/font_size.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/font_family.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/fullscreen.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/image.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/image_manager.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/table.min.js"></script>
</head>

<body>
<%--帖子主体--%>
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

            <div class="title">大牛数控社区</div>
        </div>

        <div class="tabs clearfix">
            <a href="${ctx}/m/">首页</a>
            <a href="${ctx}/m/doc/category">文库</a>
            <a href="${ctx}/m/video/">课堂</a>
            <a class="current" href="${ctx}/m/posts/category">社区</a>
        </div>
    </header>

    <div class="content clearfix">
        <div class="page-qa">
            <div class="category-toolbar">
                <div class="toolbar-info">版块： ${fn:escapeXml(category.name)} </div>
                <a class="toolbar-filter" href="javascript:;">筛选<i class="fa fa-angle-down"></i></a>
            </div>

            <div class="qa-category" style="display: none;">
                <ul class="qa-category-list bg-white clearfix">
                    <c:forEach items="${categories}" var="category">
                        <a href="${ctx}/m/posts/list/${category.id}">
                            <div class="qa-category-pic">
                                <img src="${bt:url(category.icon,"/files/" ,"/assets/images/logo.jpg" )}" alt=""/></div>
                            <div class="qa-category-info">
                                <div class="qa-category-name">${fn:escapeXml(category.name)}</div>
                                <div class="qa-category-meta"><em class="meta-label">帖子：</em>${category.postsNum}
                                    <em class="meta-label marginleft15">活跃：</em>${category.commentNum}</div>
                                <div class="qa-category-meta">
                                    <em class="meta-label">最新：</em>${fn:escapeXml(category.lastPostsTitle)}</div>
                            </div>
                        </a>
                    </c:forEach>
                </ul>
            </div>

            <div class="qa-list">
                <c:forEach items="${postsInfos.content}" var="postsInfo">
                    <a class="qa-list-item" href="${ctx}/m/posts/${postsInfo.id}">
                        <div class="qa-item-profile">
                            <img class="avatar" alt="" src="${bt:url(postsInfo.userAvatar, '/files/', '/assets/images/default_avatar.png')}"/>
                        </div>
                        <div class="qa-item-info">
                            <div class="qa-author">${fn:escapeXml(postsInfo.userNickname)}</div>
                            <div class="qa-meta">
                                <span><i class="icon-clock"></i>
                                    <fmt:formatDate value="${postsInfo.createTime}" pattern="yyyy-MM-dd"/>
                                </span>
                                <span><i class="icon-eye"></i> ${postsInfo.views}</span>
                                <span><i class="icon-bubble"></i> ${postsInfo.commentsNum}</span>
                            </div>
                        </div>
                        <div class="qa-title">
                            <c:if test="${postsInfo.postsReward.state == 1 && postsInfo.postsReward.type == 1}">
                                <div class="qa-reward qa-icon-offer">${postsInfo.postsReward.amount} 牛人币</div>
                            </c:if>
                            <c:if test="${postsInfo.postsReward.state == 1 && postsInfo.postsReward.type == 2}">
                                <div class="qa-reward qa-icon-offer">${postsInfo.postsReward.amount} 积分</div>
                            </c:if>
                            <c:if test="${postsInfo.postsReward.state%2 == 0}">
                                <div class="qa-reward qa-icon-end">已悬赏</div>
                            </c:if>
                                ${fn:escapeXml(postsInfo.title)}
                        </div>

                        <div class="qa-content">
                            <p>${bt:html2Text(postsInfo.content)}</p>
                        </div>
                    </a>
                </c:forEach>
            </div>
            <bt:pagination data="${postsInfos}"/>
        </div>
    </div>

    <footer></footer>
    <div class="page-mask" style="display: none;"></div>
    <div class="float-btns">
        <div class="question-btn js-do-publish" onclick="OBJ_mLoginBox.checkLogin(event,operate.publishPosts)">
            <i class="icon-pencil"></i>
        </div>
    </div>
</div>

<jsp:include page="posts-editor.jsp"/>
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
                            <input class="dialog-input" id="mobileLogin" name="mobile" type="text" placeholder="请输入11位手机号码" onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="11" oninput="OBJ_formcheck.checkoninput(11,OBJ_formcheck.checkFormatM,'mobileLogin','loginMgs','phone','手机号码格式不对');">
                            <input name="loginTimes" id="loginTimes" type="number" hidden="hidden" value="1">
                        </li>
                        <li class="clearfix">
                            <label>密&nbsp;&nbsp;&nbsp;码</label>
                            <input class="dialog-input" type="password" id="passwordLogin" onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" placeholder="请输入6-20位有效密码" maxlength="20">
                        </li>
                        <li class="clearfix" id="captchaLi" hidden="hidden">
                            <label>验证码</label>
                            <img class="captcha-img" id="getCaptcha" src="" alt="点击图片更新"/>
                            <input class="dialog-input captcha" type="text" id="captchaLogin" placeholder="看不清？请点击图片" onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" maxlength="6">
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
                            <button type="button" class="btn-cancel" onclick="window.location.href='${ctx}/m/auth/register'">注册
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
                        <i class="fa fa-exclamation-circle"></i> 您需要绑定建站内账号，才能够进行操作
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

    var operate = {
        publishPosts: function () {
            if (isLogin) {
                $('.window').show(0, function () {
                    disable_scroll();
                    jQuery('body').css('overflow-y', 'hidden');
                });
            }
        }
    };

    $(function () {

        $('.window .window-close').click(function () {
            $('.window').hide(0, function () {
                enable_scroll();
                jQuery('body').css('overflow-y', 'visible');
            });
        });

        $('#edit').froalaEditor({
            height: 200,
            language: "zh_cn",
            toolbarButtonsXS: ['bold', 'italic', 'underline', 'insertImage']
            //imageUploadURL: "${ctx}/upload/uploadImage"
        });

        var category = (function () {
            var _menuState = false;
            return {
                openMenu: function () {
                    $('.qa-category').show();
                    $('.qa-list,.float-btns').hide();
                    _menuState = true;
                },
                closeMenu: function () {
                    $('.qa-category').hide();
                    $('.qa-list,.float-btns').show();
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

        $('.qa-category').click(function () {
            category.closeMenu();
        });

        //QQ第三方登录
        $('#qqLogin').click(function () {
            window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=${configer.appUrl}", "TencentLogin",
                    "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
        });

        var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == 'micromessenger') {
            $('.weixin').show();
            $('.weixin').click(function () {
                window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize' +
                        '?appid=${configer.wxAccountAppid}' +
                        '&redirect_uri=${configer.appUrl}m/&response_type=code' +
                        '&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';
            });
        } else {
            $('.weixin').hide();
            return false;
        }

    });

    // 微信分享
    var title = '${category.name}';
    if (title == '') title = '大牛数控社区';
    var imgUrl = 'http://www.d6sk.com/files/${category.icon}';
    if (imgUrl == 'http://www.d6sk.com/files/') imgUrl = 'http://www.d6sk.com/assets/images/logo_posts.png';
    var desc = '${fn:escapeXml(category.description)}';
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

    /*发布跳转过来*/
    var action = '${param.action}';
    if(action == 'publish'){
        $('.js-do-publish').click();
    }

</script>
</html>
