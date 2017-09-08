<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>用户登录</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="${ctx}/m/">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu"></div>
        <div class="title">登录</div>
    </header>

    <div class="content clearfix">
        <div class="login">
            <form action="${ctx}/m/auth/login" method="post">
                <ul class="login-group">
                    <li class="clearfix">
                        <label>手机号</label>
                        <input name="username" class="login-input" type="text" placeholder="请输入手机号码" maxlength="11">
                        <input name="loginTimes" id="loginTimes" type="number" hidden="hidden" value="1">
                    </li>
                    <li class="clearfix">
                        <label>密&nbsp;&nbsp;&nbsp;码</label>
                        <input name="password" class="login-input" type="password" placeholder="请输入6-20位密码" maxlength="20">
                    </li>
                    <li class="clearfix" id="captchaLi" hidden="hidden">
                        <label>验证码</label>
                        <img id="captcha" class="img-captcha" src="${ctx}/assets/captcha.jpg" alt="点击图片更新">
                        <input name="captcha" class="login-input captcha" type="text" placeholder="点击图片更新" maxlength="10">
                    </li>
                </ul>
                <div class="err-msg" style="text-align: center; font-size: 13px; color: #f98f8f; height: 20px;">${message}</div>

                <div class="login-footer">
                    <div class="login-btns clearfix">
                        <button type="submit" class="btn-primary">登录</button>
                    </div>
                    <div class="clearfix">
                        <center>
                            如有账号异常问题，请咨询在线客服。
                            <a class="login-forget" href="${ctx}/m/auth/forgetPwd" target="_blank" style="color: #3a89eb;">忘记密码?</a>
                        </center>
                    </div>
                    <div class="third-group">
                        <div class="third-group-title">使用以下账号登录</div>
                        <div class="third-icon">
                            <dl class="weixin" id="weiXinLogin">
                                <dd></dd>
                                <dt>微信</dt>
                            </dl>
                            <dl class="qq" id="qqLogin">
                                <dd></dd>
                                <dt>QQ</dt>
                            </dl>
                            <span></span>
                        </div>
                    </div>
                    <a class="register-tips" href="${ctx}/m/auth/register">
                        还没有账号？<em>点击注册</em>
                    </a>
                </div>
            </form>
        </div>
    </div>
    <footer></footer>
</div>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<script type="text/javascript">
    $(function () {
        var backUrl = '${param.backurl}';

        $('#captcha').click(function () {
            $(this).attr('src', '${ctx}/assets/captcha.jpg?t=' + new Date().getTime());
        });

        var validate = function () {
            var username = $('[name=username]').val().trim();
            var password = $('[name=password]').val().trim();
            if (!/^1[3-8]\d{9}$/.test(username)) {
                $('.err-msg').text('请输入正确的手机号码');
                return false;
            } else if (username == '' || password == '') {
                $('.err-msg').text('账号和密码不能为空哦');
                return false;
            } else if (!/^\w{6,20}$/.test(password)) {
                $('.err-msg').text('密码为空或不符合规则');
                return false;
            }
            return true;
        };

        $('form').ajaxForm({
            beforeSubmit: validate,
            success: function (user) {
                location.href = backUrl ? backUrl : '${ctx}/m/';
            },
            error: function (r) {
                var $loginTimes = $('#loginTimes');
                var times = parseInt($loginTimes.val());
                $loginTimes.attr('value', times + 1);
                if (times + 1 > 3) {
                    $('#captchaLi').show();
                }
                if (r['responseJSON'] && r['responseJSON'].message) {
                    $('.err-msg').text(r['responseJSON'].message);
                } else {
                    $('.err-msg').text('登录失败！');
                }
            }
        });
    });

    <c:if test="${messageSuccess != null}">
    $('.err-msg').text('${messageSuccess}' + '   ${messageLogin}');
    </c:if>

    //QQ第三方登录
    $('#qqLogin').click(function () {
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=${configer.appUrl}m", "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    });

    $(function () {
        var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) != null && ua.match(/MicroMessenger/i) == 'micromessenger') {
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
    //微信第三方登录
    <%--$('#weiXinLogin').click(function () {--%>
    <%--window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize' +--%>
    <%--'?appid=${configer.wxAccountAppid}' +--%>
    <%--'&redirect_uri=${configer.appUrl}m/&response_type=code' +--%>
    <%--'&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';--%>
    <%--});--%>
</script>
</body>
</html>
