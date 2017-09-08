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
                            <button type="button" class="btn-cancel" onclick="window.location.href='${ctx}/m/auth/register'">注册</button>
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
                        <c:if test="${sessionScope != null && sessionScope.user != null}">
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
                        <i class="fa fa-exclamation-circle"></i> 您需要绑定建站内账号，才能够下载资料
                    </a>
                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript" src="${assets}/js/mLoginBox.js"></script>
<script type="text/javascript" src="${assets}/js/mBindAccount.js"></script>

<script>

    $(function () {
        $('.dialog').click(function (event) {
            event.stopPropagation();
        });

        $('.dialog .close,.dialog .btn-cancel').click(function () {
            $('.dialog').hide();
            $('.page-mask').hide();
        });

        $('.captcha-img').click(function () {
            $(this).attr('src', '/assets/captcha.jpg?t=' + new Date().getTime());
        });

        $('.dialog-input').focus(function () {
            $(this).parent('li').addClass('current').siblings().removeClass('current');
        });

        $('#getCaptcha').attr('src', '/assets/captcha.jpg?t=' + new Date().getTime());
    });

    function loginEvents(){
        if (!$('#dialogLogin').is(':visible')) {
            $('#dialogLogin').show();
            $('.page-mask').show();
            $(document).one('click', function () {
                $('#dialogLogin').hide();
                $('.page-mask').hide();
            })
        } else {
            $('#dialogLogin').hide();
            $('.page-mask').hide();
        }
    }

    function bindEvents(){
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
</script>