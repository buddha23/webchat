<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>密码修改</title>
    <base href="${ctx}/">
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="javascript:history.back()">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu">
        </div>

        <div class="title">修改密码</div>
    </header>

    <div class="content clearfix">
        <div class="login">
            <form id="rePwdForm" action="" method="post">
                <ul class="login-group">
                    <c:if test="${!empty success}">
                        <li class="text-center" style="background: #C2E0C2;">
                            修改成功,请<a href="${ctx}/m/auth/login">【重新登录】</a>
                        </li>
                    </c:if>
                    <c:if test="${!empty error}">
                        <li class="text-center" style="background: #e06557; color: white;">${error}</li>
                    </c:if>
                    <li id="err" class="text-center" style="background: #e06557; color: white;"></li>
                    <li class="clearfix">
                        <label>原密码</label>
                        <input id="p0" name="op" class="login-input" type="password" maxlength="32" required>
                    </li>
                    <li class="clearfix">
                        <label>密&nbsp;&nbsp;&nbsp;码</label>
                        <input id="p1" name="np" class="login-input" type="password" maxlength="20" pattern="\w{6,20}" title="由6-20个字母数字或下划线组成" required>
                    </li>
                    <li class="clearfix">
                        <label>重复密码</label>
                        <input id="p2" class="login-input" type="password" maxlength="20" pattern="\w{6,20}" title="由6-20个字母数字或下划线组成" required>
                    </li>
                </ul>

                <div class="login-footer">
                    <div class="login-btns clearfix">
                        <button type="submit" class="btn-primary">确定</button>
                        <a href="${ctx}/m/user/center" class="btn-default btn-back text-center">返回</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <footer>
    </footer>
</div>
<script type="text/javascript">
    $(function () {
        $('#rePwdForm').submit(function () {
            $('#err').hide();
            if(!/^\w{6,20}$/.test($('#p1').val())) {
                $('#err').html('密码只能由6至20个字母数字或下划线组成。').show();
                return false;
            }
            if($('#p1').val() !== $('#p2').val()) {
                $('#err').html('两次输入的新密码不一致！').show();
                return false;
            }
            return true;
        });
    });
</script>
</body>
</html>
