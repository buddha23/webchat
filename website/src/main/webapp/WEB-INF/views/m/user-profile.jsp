<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>完善资料</title>
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
        <div class="title">完善资料</div>
    </header>
    <div class="content clearfix">
        <div class="login">
            <form id="form" action="${ctx}/m/user/updateUser" method="post">
                <ul class="login-group">
                    <li class="clearfix">
                        <label><small style="color: red;">*</small> 昵称</label>
                        <input id="nickname" name="nickname" class="login-input" type="text" placeholder="取个好听的昵称吧(必填)"
                               maxlength="40">
                    </li>
                    <li class="clearfix">
                        <label><small style="color: red;">*</small> 邮箱</label>
                        <input id="mailAddress" name="mailAddress" class="login-input" type="text"
                               placeholder="邮箱(必填,关系到您的账号找回)" maxlength="100">
                    </li>
                    <li class="clearfix">
                        <label>地址</label>
                        <input id="address" name="address" class="login-input" type="text" placeholder="我的所在地"
                               maxlength="100">
                    </li>
                    <li class="clearfix">
                        <label>简介</label>
                        <input id="description" name="description" class="login-input" type="text" placeholder="我的个人简介"
                               maxlength="100">
                    </li>
                </ul>
                <div class="msg" style="text-align: center; font-size: 13px; height: 20px;color:red;"></div>

                <div class="login-footer">
                    <div class="login-btns clearfix">
                        <a href="javascript:void(0);" class="btn-primary"
                           style="text-align: center;color: #f98f8f;">完成</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <footer></footer>
</div>
<script type="text/javascript" src="${assets}/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript">
    <c:if test="${err!=null}">
    $('.msg').text('昵称和邮箱不能为空');
    </c:if>

    var ctx = '${ctx}';
    var nickname, mailaddress;

    function dataCheck() {
        nickname = $("#nickname").val().trim();
        mailaddress = $("#mailAddress").val().trim();
        if (nickname == null || nickname == '' || mailaddress == null || mailaddress == '') {
            $('.msg').text('昵称和邮箱不能为空');
            return false;
        } else {
            $('.err-msg').text('');
            return true;
        }
    }

    $('.btn-primary').click(function () {
        nickname = $("#nickname").val().trim();
        mailaddress = $("#mailAddress").val().trim();
        if (nickname == null || nickname == '' || mailaddress == null || mailaddress == '') {
            $('.msg').text('昵称和邮箱不能为空');
            return false;
        } else {
            $('.err-msg').text('');
            $("#form").submit();
//            return true;
        }

    })

</script>
</body>
</html>
