<jsp:include page="../header.jsp">
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>
<script>
    $("#accountDialog").remove();
</script>

<div class="content bg-white">
	<div class="container">
        <div class="login">
            <div class="clearfix">
            	<div class="login-left">
                    <div class="login-left-title">
                        <h2>密码找回</h2>
                    </div>
                    <div class="login-left-content">
                    	<form class="login-form margintop15">
                        	<div class="row">
                                <label for="">手机号</label>
                                <div class="field">
                                    <input id="username" name="mobile" class="login-input phonenumber" type="text" onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="11" onchange="OBJ_formcheck.checkFormat('username','phone','手机号码格式不正确');"/>
                                    <button class="captcha-btn" id="sendmsg_button" type="button" onclick="OBJ_forgotPwd.sendMsg('username');">获取验证码</button>
                                    <button class="captcha-btn" id="msg_end" style="display:none;"><span id="sendmsg_time_text">60</span>秒后重发</button>
                                </div>
                            </div>
                            <div class="row">
                                <label for="">验证码</label>
                                <div class="field">
                                    <input id="captcha" name="code" class="login-input" type="text" onchange="return OBJ_formcheck.checkFormatW('captcha','username','captcha','请输入有效的6位验证码')" onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" maxlength="6"/>
                                </div>
                            </div>
                            <div>
                            	<button type="button" class="button btn-login input-full" onclick="return OBJ_forgotPwd.checkSms()">下一步</button>
                            </div>
                            <div class="row">
                                <div class="field">
                                    <label for="" id="username_tips" style="color:red;" ></label>
                                </div>
                            </div>
                            <div class="login-msg">
                            	如有账号异常问题，请咨询在线客服。
                                <a class="login-forget" href="${ctx}/auth/login">返回登录</a>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="login-right">
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/forgotPwd.js"></script>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script>
    $(function() {
        $('.login-form .row .login-input').focus(function(){
            $(this).parent('.field').parent('.row').addClass('current').siblings().removeClass('current');
        })
    });
</script>
<%@include file="../footer.jsp" %>

