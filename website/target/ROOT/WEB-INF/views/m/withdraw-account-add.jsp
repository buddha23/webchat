<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title></title>
</head>

<body>
<div class="container">
    <header>
        <div class="menu-left">
            <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
        </div>

        <div class="menu-right">
            <a class="menu-item menu-user" href="${ctx}/m/user/center"></a>
        </div>

        <div class="title">提现账户绑定</div>
    </header>

    <div class="content clearfix">
        <div class="page-finance-wrapper">
            <form class="page-form-wap" action="${ctx}/finance/addAccount" method="post">
                <input name="id" id="accountId" value="${account.id}" hidden>
                <div class="row margintop0">
                    <label for="type">类型：</label>
                    <div class="item">
                        <div>
                            <select class="ui-select-default input-full" id="accountType" name="accountType">
                                <option value="1" <c:if test="${account.accountType.equals(1)}">selected</c:if>>支付宝</option>
                                <%--<option value="2">银行卡</option>--%>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">账户：</label>
                    <div class="item">
                        <div><input class="ui-input-default input-full" type="text" id="account" name="account" placeholder="输入账户信息" maxlength="200" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'')" value="${fn:escapeXml(account.account)}"></div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">户名：</label>
                    <div class="item">
                        <div><input class="ui-input-default input-full" type="text" id="accountName" name="accountName" placeholder="输入户名" maxlength="100" value="${fn:escapeXml(account.accountName)}"></div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">手机号： (<a href="${ctx}/m/user/account"><font style="color: #0b97f0">修改</font></a>)</label>
                    <div class="item">
                        <div><input class="ui-input-default input-full" id="userMobile" type="text" value="${user.mobile}" readonly></div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">验证码：</label>
                    <div class="item">
                        <div class="clearfix">
                            <input class="ui-input-default phonenumber floatleft" type="text" name="code" id="code" placeholder="输入验证码" onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="8">
                            <div class="button-tiny floatleft codeBtn" id="validCode">获取验证码</div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <label></label>
                    <div class="item">
                        <button class="button btn-blue input-full btn-large" type="button" id="submitBtn">确定</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <footer></footer>
</div>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript">
    $('.codeBtn').click(function () {
        var mobile = $('#userMobile').val().trim();
        $.ajax({
            url: "${ctx}/auth/smsCode",
            data: {mobile: mobile, type: 5},
            type: 'get',
            success: function (data) {
                $('.msg').text("验证码发送" + data.message);
                $('#validCode').prop('disabled', true);
                var t = 60;
                var tt = setInterval(function () {
                    $('#validCode').text('再次发送(' + t-- + ')');
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
    });

    $('#submitBtn').click(function () {
        if (checkFormate())
            $(this).parents('form').ajaxSubmit({
                url: '${ctx}/finance/addAccount',
                success: function () {
                    alert('保存成功！');
                    location.href = "${ctx}/m/withdraw/accounts";
                },
                error: function (r) {
                    console.log(r);
                    alert(JSON.parse(r.responseText).msg || '保存失败！');
                }
            });
        return false;
    });

    function checkFormate() {
        var account, accountName, accountType, code, accountId;
        var bankName = "";
        accountName = $("#accountName").val();
        accountId = $("#accountId").val();
        accountType = $("#accountType").val();
        code = $("#code").val();
        if (accountType == 1) {
            account = $("#account").val();
        } else {
            account = $("#bankNo").val();
            bankName = $("#bankName").val();
        }

        if (accountType == 1) {
            if (account == "") {
                alert("账户不能为空！");
                return false;
            }
            if (!OBJ_formcheck.check('email', account) && !OBJ_formcheck.check('phone', account)) {
               alert("账户格式不正确，请重新输入！");
                return false;
            }
        } else {
            if (account == "" || account == null) {
                alert("账户不能为空！");
                return false;
            }
            if (bankName == "" || bankName == null) {
                alert("开户行不能为空！");
                return false;
            }
            if (!OBJ_formcheck.check('bankCard', account)) {
                alert("银行卡号应为16-19位数字！");
                return false;
            }
        }
        if (accountName == "" || accountName == null) {
            alert("户名不能为空！");
            return false;
        } else if (code.length < 6) {
           alert("验证码输入错误！");
            return false;
        } else if (code == "" || code == null) {
            alert("验证码不能为空！");
            return false;
        } else {
            return true;
        }
    }
</script>
</body>
</html>
