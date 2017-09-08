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

        <div class="title">申请提现</div>
    </header>

    <div class="content clearfix">
        <div class="page-finance-wrapper">
            <form class="page-form-wap" id="applyForm" method="post">
                <div class="row margintop0">
                    <label for="">类型：</label>
                    <div class="item">
                        <div>
                            <select class="ui-select-default input-full">
                                <option value="1">支付宝</option>
                                <%--<option value="2">银行卡</option>--%>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">支付宝账户：</label>
                    <div class="item">
                        <div>
                            <input name="accountId" id="accountId" value="${aliAccount.id}" hidden>
                            <input class="ui-input-default input-full" type="text" name="account" value="${aliAccount.account}" readonly>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">姓名：</label>
                    <div class="item">
                        <div><input class="ui-input-default input-full" type="text" name="accountName" value="${aliAccount.accountName}" readonly></div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">本次可提金额：</label>
                    <div class="item">
                        <div><strong class="color-vermeil">${userScore.totalScore}牛人币/￥${userScore.totalScore/10}</strong></div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">提现金额：</label>
                    <div class="item">
                        <div><input class="ui-input-default input-full" type="number" name="tradeAmount" id="tradeAmount" max="${userScore.totalScore/10}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" required></div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">手机号：(<a href="${ctx}/m/user/account"><font style="color: #0b97f0">修改</font></a>)</label>
                    <div class="item">
                        <div><input class="ui-input-default input-full" name="mobile" id="mobile" value="${user.mobile}" readonly></div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">验证码：</label>
                    <div class="item">
                        <div class="clearfix">
                            <input class="ui-input-default phonenumber floatleft" type="text" name="validCode" id="code" placeholder="输入验证码" onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="8">
                            <div class="button-tiny floatleft codeBtn" id="validCode">获取验证码</div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <label for="password">账户密码：</label>
                    <div class="item">
                        <div><input class="ui-input-default input-full" type="password" name="password" id="password" onkeyup="value=value.replace(/[\u4E00-\u9FA5]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[\u4E00-\u9FA5]/g,''))"/></div>
                    </div>
                </div>

                <div class="row">
                    <label></label>
                    <div class="item">
                        <button class="button btn-blue input-full btn-large" type="button" id="submitBtn">确定</button>
                    </div>
                </div>

                <div class="row">
                    <div class="dialog-tips">
                        备注 : 1、提现最低不低于50元；<br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2、牛人币提现收取20%平台佣金；<br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3、到账日期大约为2-3个工作日。
                    </div>
                </div>
            </form>
        </div>
    </div>
    <footer></footer>
</div>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<script type="text/javascript">
    $('.codeBtn').click(function () {
//        var validCode = $('#validCode').val().trim();
        var mobile = $('#mobile').val().trim();
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
        var mobile = $("#mobile").val();
        var validCode = $("#code").val();
        var password = $("#password").val();
        var tradeAmount = $("#tradeAmount").val();
        var core = ${userScore.totalScore};
        if (!tradeAmount || !mobile || !validCode || !password) {
            alert("请完善表单数据！");
            return false;
        }
        if (tradeAmount < 50 || tradeAmount * 10 > core) {
            alert("提现金额不能小于50元或大于可提金额！");
            return false;
        }
        $("#applyForm").ajaxSubmit({
            url: '${ctx}/withdrawals/apply',
            success: function () {
                alert('申请提现成功');
                location.href = '${ctx}/m/withdraw/board';
                <%--location.href='${ctx}/m/withdraw/history';--%>
            },
            error: function (r) {
                console.log(r);
                alert(JSON.parse(r.responseText).message || '保存失败！');
            }
        });
    });

</script>
</body>
</html>
