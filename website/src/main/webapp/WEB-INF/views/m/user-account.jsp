<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.leoDialog.js"></script>
    <title>账号绑定</title>
</head>

<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()"><i
                        class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">账号绑定</div>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <div class="page">
            <div class="section tabs">
                <div class="section-content">
                    <div class="category-manage tabs">
                        <ul class="section-tab tabs-nav clearfix">
                            <li class="tab-menu tabs-nav-item current">绑定手机号</li>
                            <li class="tab-menu tabs-nav-item">第三方账号关联</li>
                        </ul>
                        <div class="tabs-panel">
                            <div class="tab">
                                <div class="page-form-style">
                                    <form action="${ctx}/user/accountManage" id="page-form" method="post">
                                        <ul class="form-group">
                                            <li class="clearfix">
                                                <label>手机号</label>
                                                <button class="captcha-btn" type="button" id="validCode">获取验证码</button>
                                                <input class="form-input captcha" type="text" maxlength="11" id="bindMobile" name="bindMobile">
                                            </li>
                                            <li class="clearfix">
                                                <label>验证码</label>
                                                <input class="form-input" type="text" maxlength="6" id="bindCode" name="bindCode">
                                            </li>
                                            <li class="clearfix">
                                                <label>密&nbsp;&nbsp;&nbsp;码</label>
                                                <input class="form-input" type="password" maxlength="20" id="password" name="password">
                                            </li>
                                            <li class="clearfix">
                                                <label>确认密码</label>
                                                <input class="form-input" type="password" maxlength="20" id="repassword">
                                                <input id="leaveId" name="leaveId" hidden>
                                            </li>
                                        </ul>
                                        <div class="msg"
                                             style="text-align: center; font-size: 13px; height: 20px;color:red;"></div>
                                        <div class="form-btns clearfix">
                                            <div class="form-btns-full">
                                                <button type="button" class="btn-confirm">确定</button>
                                            </div>
                                        </div>

                                        <a class="form-tips" href="">
                                            <i class="fa fa-exclamation-circle"></i>
                                            完成绑定手机号码，可通过点击“忘记密码”找回密码，并掌握文库最新资料信息。
                                        </a>
                                    </form>
                                </div>
                            </div>

                            <div class="tab">
                                <div class="third-account-list">
                                    <c:if test="${empty qqThird}">
                                        <a class="third-account-item" href="javascript:;" onclick="bindqq()">QQ：未绑定(点击绑定)</a></c:if>
                                    <c:if test="${!empty qqThird}">
                                        <a class="third-account-item" href="javascript:;"
                                           onclick="relieveqq()">QQ：${qqThird.nickname}（点击取消绑定）</a></c:if>
                                    <c:if test="${empty weixinThird}">
                                        <a class="third-account-item" href="javascript:;" onclick="bindwx()">微信：未绑定(点击绑定)</a></c:if>
                                    <c:if test="${!empty weixinThird}">
                                        <a class="third-account-item" href="javascript:;"
                                           onclick="relievewx()">微信：${weixinThird.nickname}（点击取消绑定）</a></c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <footer>
        </footer>
    </div>
</div>

<div id="dialog" class="account-dialog" action="#" style="display: none;">
    <div class="tips">检测到该手机号已注册有站内账号，请选择保留一项。若选择当前账号，则原手机注册账号停用；若选择原手机账号，则当前账号停用。此操作不可逆，请谨慎选择：</div>
    <div class="account-dialog-content">
        <div class="chosen-list choseleave1">
            <li data-check="1">保留当前账号</li>
            <li data-check="2">保留原手机账号</li>
        </div>
    </div>
</div>
<div id="dialog2" class="account-dialog" action="#" style="display: none;">
    <div class="tips">${checkleave}</div>
    <div class="account-dialog-content">
        <div class="chosen-list choseleave2">
            <li data-check="1">保留当前账号</li>
            <li data-check="2">保留原关联账号</li>
        </div>
    </div>
</div>

<script type="text/javascript" src="${assets}/js/mBindAccount.js"></script>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<script type="text/javascript">
    $(function () {
        $('.page-form-style .form-input').focus(function () {
            $(this).parent('li').addClass('current').siblings().removeClass('current');
        });

        $('.chosen-list li').click(function () {
            if ($(this).hasClass('act')) {
                $(this).removeClass('act');
            } else {
                $(this).addClass('act').siblings().removeClass('act');
            }
        });

        $('.captcha-btn').click(function () {
            var bindMobile = $('#bindMobile').val();
            if (OBJ_formcheck.check('phone', bindMobile)) {
                $.ajax({
                    url: "${ctx}/auth/smsCode",
                    data: {mobile: bindMobile, type: 4},
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
                        $('.msg').html(JSON.parse(req.responseText).message);
                    }
                });
            } else {
                $('.msg').html('无效的手机号码');
            }
        });

        var isExist = false;
        $('.btn-confirm').click(function () {
            var bindMobile = $('#bindMobile').val();
            var password = $('#password').val();
            var repassword = $('#repassword').val();
            if (checkFormate()) {
                $.ajax({
                    url: '${ctx}/user/checkBind',
                    data: {'bindMobile': bindMobile},
                    success: function (req) {
                        if (req == true) {
                            isExist = true;
                            var ld = leoDialog.window({
                                title: '选择账号保留',
                                modal: true,
                                //内容可以指定dom或者字符串
                                content: $('#dialog'),
                                buttons: {
                                    //确认按钮
                                    confirm: {
                                        text: '确定',
                                        action: function () {
                                            accountRelevance();
                                        }
                                    },
                                    //取消按钮
                                    cancel: {
                                        text: '取消',
                                        action: function () {
                                            leoDialog.closeAll();
                                        }
                                    }
                                }
                            });
                        } else {
                            isExist = false;
                            accountRelevance();
                        }
                    },
                    error: function (r) {
                        $('.msg').html(JSON.parse(req.responseText).message || "绑定错误");
                        console.log(r);
                    }
                });
            } else {
                $('.msg').html('信息填写错误');
                return false;
            }
        });

        function checkFormate() {
            var bindMobile = $('#bindMobile').val();
            var password = $('#password').val();
            var repassword = $('#repassword').val();
            return (OBJ_formcheck.check('phone', bindMobile) && OBJ_formcheck.check('password', password)) && password == repassword;
        }

        function accountRelevance() {
            var chosetype = $('.choseleave1 li.act').data('check');
            if (chosetype) {
                $('#leaveId').attr("value", chosetype);
                console.log(chosetype);
            } else {
                $('#leaveId').attr("value", 0);
            }
            $('#page-form').ajaxSubmit({
                success: function (req) {
                    alert('绑定成功！');
                    location.href = '${ctx}/m/user/center';
                    console.log(req);
                },
                error: function (err) {
                    $('.msg').html(JSON.parse(err.responseText).message || "绑定错误");
                    console.log(err);
                }
            })
        }

    });

    function bindqq() {
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=${configer.appUrl}m/user/account", "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    }
    function bindwx() {
        window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize' +
                '?appid=${configer.wxAccountAppid}' +
                '&redirect_uri=${configer.appUrl}m/user/account&response_type=code' +
                '&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';
    }
    function relieveqq() {
        var cfm = confirm('解绑后QQ再次登录将不会关联到此账号,确认解除？');
        if (cfm == true) {
            doRelieve(1);
        } else {
            return false;
        }
    }
    function relievewx() {
        var cfm = confirm('解绑后微信再次登录将不会关联到此账号,确认解除？');
        if (cfm == true) {
            doRelieve(2);
        } else {
            return false;
        }
    }
    function doRelieve(type) {
        $.ajax({
            type: 'get',
            url: '${ctx}/user/relieve?type=' + type,
            success: function () {
                console.log("OK");
                alert("解绑成功");
                location.href = '${ctx}/m/user/center';
            },
            error: function (req) {
                console.log(req);
                alert(JSON.parse(req.responseText).message);
            }
        });
    }

    <c:if test="${!empty msg}">alert("${msg}");</c:if>
    <c:if test="${!empty checkleave}">
    var ld = leoDialog.window({
        title: '选择账号保留',
        modal: true,
        //内容可以指定dom或者字符串
        content: $('#dialog2'),
        buttons: {
            //确认按钮
            confirm: {
                text: '确定',
                action: function () {
                    accountLeave();
                }
            },
            //取消按钮
            cancel: {
                text: '取消',
                action: function () {
                    leoDialog.closeAll();
                }
            }
        }
    });

    function accountLeave() {
        var oldBindId = ${oldBindId};
        var choseleave = $('.choseleave2 li.act').data('check');
        console.log(choseleave);
        if (choseleave) {
            $.ajax({
                url: '${ctx}/user/thirdLeave',
                type: 'post',
                data: {leaveId: choseleave, oldBindId: oldBindId},
                success: function () {
                    alert("操作成功!");
                    location.href = '${ctx}/m/user/center';
                },
                error: function (r) {
                    alert("操作失败!");
                    console.log(r);
                }
            });
        } else {
            return false;
        }
    }
    </c:if>
</script>
</body>
</html>
