<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>密码找回</title>
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
        <div class="title">忘记密码</div>
    </header>

    <div class="content clearfix">
        <div class="login">
            <form id="resetForm" action="${ctx}/m/auth/resetForgetPwd" method="post">
                <ul class="login-group">
                    <li class="clearfix">
                        <label>手机号</label>
                        <input id="mobile" name="mobile" class="login-input" type="text" placeholder="11位手机号码" maxlength="11">
                    </li>
                    <li class="clearfix">
                        <label>验证码</label>
                        <button class="captcha-btn" type="button">获取验证码</button>
                        <input id="validCode" class="login-input captcha" onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="10" name="validCode" type="text" placeholder="请输入验证码">
                    </li>
                    <li class="clearfix">
                        <label>新&nbsp;&nbsp;密&nbsp;&nbsp;码</label>
                        <input name="password" class="login-input" type="password" placeholder="6至20位字母或数字"
                               maxlength="20">
                    </li>

                </ul>
                <div class="msg" style="text-align: center; font-size: 13px; height: 20px;"></div>

                <div class="login-footer">
                    <div class="login-btns clearfix">
                        <a href="javascript:void(0);" class="btn-primary"
                           style="text-align: center;color: #f98f8f;">确定</a>
                    </div>
                    <a class="register-tips" href="${ctx}/m/auth/login">记起密码？<em>点击登录</em>
                    </a>
                </div>
            </form>
        </div>
    </div>
    <footer></footer>
</div>
<script type="text/javascript" src="${assets}/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript">
    var ctx = '${ctx}';

    function dataCheck() {
        var mobile = $('#mobile').val().trim();
        if (!/^1[3-8]\d{9}$/.test(mobile)) {
            $('.msg').text('请输入有效的手机号码');
            return false;
        } else {
            $('.err-msg').text('');
            return true;
        }
    }
    //获取验证码
    $('.captcha-btn').click(function () {
        var mobile = $('#mobile').val().trim();
        if (dataCheck()) {
            $.ajax({
                url: ctx + "/auth/smsCode",
                data: {mobile: mobile, type: 3},
                type: 'get',
                success: function (data) {
                    $('.msg').text("验证码发送" + data.message);
                    $('.captcha-btn').prop('disabled', true);
                    var t = 60;
                    var tt = setInterval(function () {
                        $('.captcha-btn').text('重发(' + t-- + '秒)');
                        if (t < 1) {
                            clearInterval(tt);
                            $('.captcha-btn').prop('disabled', false);
                            $('.captcha-btn').text('获取验证码');
                        }
                    }, 1000);
                },
                error: function (error) {
                    $('.msg').text(error.responseJSON.message).addClass('color-red');
                }
            });
        }
    });

    //submit
    $('.btn-primary').click(function () {
        var mobile = $('#mobile').val().trim();
        var validCode = $("input[name='validCode']").val().trim();
        var password = $('[name=password]').val().trim();
        if (dataCheck()) {
            if (!/^\w{6,20}$/.test(password)) {
                $('.msg').text('请输入有效的密码');
                return false;
            }
            if (!/^[0-9]{6}$/.test(validCode)) {
                $('.msg').text('请输入有效的验证码');
                return false;
            } else {
                $.ajax({
                    url: ctx + '/auth/smsCode',
                    data: {'mobile': mobile, 'validCode': validCode, 'type': 3},
                    success: function (data) {
                        $("#resetForm").submit();
                    },
                    error: function (r) {
                        var data = JSON.parse(r.responseText);
                        $('.msg').html(data.message).addClass('color-red');
                    }
                });
            }
        }
    });

    <c:if test="${error != null}">
    $('.msg').text('请先进行手机短信验证');
    </c:if>

</script>
</body>
</html>