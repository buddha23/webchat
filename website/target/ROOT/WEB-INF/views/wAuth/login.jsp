<jsp:include page="../header.jsp"/>
<div class="content bg-white">
    <div class="container">
        <div class="login">
            <div class="clearfix">
                <div class="login-left">
                    <div class="login-left-title">
                        <h2>欢迎回来</h2>
                        <span>还没有账号？<a href="${ctx}/auth/register">立即注册</a></span>
                    </div>
                    <div class="login-left-content">
                        <form class="login-form margintop15" action="${ctx}/auth/login" method="post">
                            <div class="row">
                                <label for="">账号</label>
                                <div class="field">
                                    <input value="${mobile}" class="login-input" type="text" name="username" placeholder="请输入账号" maxlength="11" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                                </div>
                            </div>
                            <div class="row">
                                <label for="">密码</label>
                                <div class="field">
                                    <input class="login-input" type="password" name="password" placeholder="请输入6-20位有效密码" maxlength="20"/>
                                </div>
                            </div>
                            <div class="row">
                                <label for="">验证码</label>
                                <div class="field">
                                    <input class="login-input" type="text" name="captcha" maxlength="6" style="width: 50%; display: inline-block;" placeholder="看不清？请点击图片"/>
                                     <img id="captcha" src="${ctx}/assets/captcha.jpg" alt="图片验证码" style="width: 45%; vertical-align: top;">
                                </div>
                            </div>
                            <div class="err-msg" style="text-align: center; color: red;"></div>
                            <div>
                                <button class="button btn-login input-full" type="submit">登录</button>
                            </div>
                            <div class="login-msg">
                                如有账号异常问题，请咨询在线客服。
                                <a class="login-forget" href="${ctx}/auth/forgotPwd" target="_blank">忘记密码?</a>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="login-right">
                    <div class="login-right-title">
                        <h2>第三方账号登录</h2>
                    </div>
                    <div class="login-party">
                        <a href="javascript:;" class="login-qq" id="qqLogin">
                            <span><i class="fa fa-qq"></i><p>QQ帐号登录</p></span>
                        </a>
                        <a href="javascript:;" class="login-wechat" id="weiXinLogin">
                            <span><i class="fa fa-wechat"></i><p>微信帐号登录</p></span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<script type="text/javascript">
    $(function () {

        <c:if test="${!empty message}">
        layer.msg('${message}', {
            icon: 6,
            time: 1500
        });
        </c:if>

        var backUrl = '${param.backurl}';

        $('#captcha').click(function () {
            $(this).attr('src', '${ctx}/assets/captcha.jpg?t=' + new Date().getTime());
        });

        var validate = function () {
            var username = $.trim($('[name=username]').val());
            var password = $.trim($('[name=password]').val());
            if (!/^1[3-8]\d{9}$/.test(username)) {
                $('.err-msg').text('请输入正确的手机号码');
                return false;
            } else if (username == '' || password == '') {
                $('.err-msg').text('账号和密码不能为空哦');
                return false;
            } else if (!/^\w{6,20}$/.test(password)) {
                $('.err-msg').text('无效的密码');
                return false;
            }
            return true;
        };

        $('.login-form').ajaxForm({
            beforeSubmit: validate,
            success: function (user) {
                location.href = ctx + '/user/center';
            },
            error: function (req) {
                var data = JSON.parse(req.responseText);
                if (data.message) {
                    $('.err-msg').text(data.message);
                } else {
                    $('.err-msg').text('登录失败！');
                }
            }
        });
    });
</script>
<script type="text/javascript">
    //QQ第三方登录
    $('#qqLogin').click(function () {
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=${configer.appUrl}", "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    });
    //微信第三方登录
    $('#weiXinLogin').click(function () {
        window.location.href = 'https://open.weixin.qq.com/connect/qrconnect' +
                '?appid=${configer.wxAccountAppid}' +
                '&redirect_uri=${configer.appUrl}/&response_type=code' +
                '&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';
    })
</script>

<%@include file="../footer.jsp" %>