<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>用户注册</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="${ctx}/m/auth/login">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu"></div>
        <div class="title">注册</div>
    </header>
    <div class="content clearfix">
        <div class="login">
            <form>
                <div class="login-board">
                    注册就送50牛人币，受邀用户更享平台奖励
                </div>

                <ul class="login-group">
                    <li class="clearfix">
                        <label>手机号</label>
                        <input id="mobile" name="username" class="login-input" type="text" placeholder="11位手机号码" maxlength="11">
                    </li>
                    <li class="clearfix">
                        <label>验证码</label>
                        <button id="validCode" class="captcha-btn" type="button">获取验证码</button>
                        <input class="login-input captcha" onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="6" type="text" placeholder="请输入验证码">
                    </li>
                    <li class="clearfix">
                        <label>密&nbsp;&nbsp;&nbsp;码</label>
                        <input name="password" class="login-input" type="password" placeholder="6至20位字母或数字" maxlength="20">
                    </li>
                    <%--<li class="clearfix">--%>
                    <%--<label>邀请码</label>--%>
                    <%--<input name="inviteCode" class="login-input" type="text" placeholder="邀请人的邀请码" maxlength="6">--%>
                    <%--</li>--%>

                </ul>
                <div class="msg" style="text-align: center; font-size: 13px; height: 20px;color:red;"></div>

                <div class="login-footer">
                    <div class="agreement-group">
                        <i class="fa fa-check-square"></i> 阅读并同意<a href="${ctx}/m/auth/agreement">《用户协议》</a>
                    </div>
                    <div class="login-btns clearfix">
                        <a href="javascript:void(0);" class="btn-primary" style="text-align: center;">注册</a>
                    </div>
                    <a class="register-tips" href="${ctx}/m/auth/login">已有账号？<em>点击登录</em></a>
                </div>
            </form>
        </div>
    </div>
    <footer></footer>
</div>
<script type="text/javascript" src="${assets}/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript">
    var ctx = '${ctx}';
    var username, password;
    function mobileCheck() {
        username = $('[name=username]').val().trim();
        password = $('[name=password]').val().trim();
        if (!/^1[3-8]\d{9}$/.test(username)) {
            $('.msg').text('请输入有效的手机号码');
            return false;
        } else {
            $('.msg').text('');
            return true;
        }
    }

    function dataCheck() {
        var captcha = $('.login-input.captcha').val().trim();
        var password = $('[name=password]').val().trim();
        if (!/^\w{6,20}$/.test(password)) {
            $('.msg').text('请输入有效的密码');
            return false;
        }
        else if (!/^[0-9]{6}$/.test(captcha)) {
            $('.msg').text('请输入有效的验证码');
            return false;
        } else {
            $('.msg').text('');
            return true;
        }
    }

    //获取验证码
    $('.captcha-btn').click(function () {
        var validCode = $('#validCode').val().trim();
        var mobile = $('#mobile').val().trim();
        if (mobileCheck()) {
            $.ajax({
                url: ctx + "/auth/smsCode",
                data: {mobile: mobile, type: 1, validCode: validCode},
                type: 'get',
                success: function (data) {
                    $('.msg').text("验证码发送" + data.message);
                    $('#validCode').prop('disabled', true);
                    var t = 60;
                    var tt = setInterval(function () {
                        $('#validCode').text('再次发送（' + t-- + '）');
                        if (t < 1) {
                            clearInterval(tt);
                            $('#validCode').prop('disabled', false);
                            $('#validCode').text('获取验证码');
                        }
                    }, 1000);
                },
                error: function (error) {
                    $('.msg').text(error.responseJSON.message);
                }
            });
        }
    });

    //注册
    $('.btn-primary').click(function () {
        if (mobileCheck() && dataCheck()) {
            var inviteCode = "${param.inviteCode}";
            if (inviteCode == "") {
                doRegist();
            } else {
                codeCheck(inviteCode)
            }
        }
    });

    function doRegist() {
        var captcha = $('.login-input.captcha').val().trim();
        var password = $('[name=password]').val().trim();
        $.ajax({
            url: ctx + "/auth/register",
            data: {
                mobile: username,
                password: password,
                validCode: captcha
            },
            type: 'post',
            success: function () {
                location.href = ctx + '/m/auth/userprofile';
            },
            error: function (r) {
                $('.msg').text(r['responseJSON']['message']);
            }
        });
    }

    function codeCheck(code) {
        $.ajax({
            url: ctx + "/auth/inviteCodeCheck?inviteCode=" + code,
            type: 'get',
            success: function () {
                var captcha = $('.login-input.captcha').val().trim();
                var password = $('[name=password]').val().trim();
                $.ajax({
                    url: ctx + "/auth/register",
                    data: {
                        mobile: username,
                        password: password,
                        validCode: captcha,
                        inviteCode: code
                    },
                    type: 'post',
                    success: function () {
                        location.href = ctx + '/m/auth/userprofile';
                    },
                    error: function (r) {
                        $('.msg').text(r['responseJSON']['message']);
                    }
                });
            },
            error: function () {
                $('.msg').text("邀请码错误");
                return false;
            }
        });
    }

</script>
</body>
</html>
