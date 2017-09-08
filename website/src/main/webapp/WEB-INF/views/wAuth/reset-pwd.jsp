<jsp:include page="../header.jsp">
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>

<div class="content bg-white">
    <div class="container">
        <div class="login">
            <div class="clearfix">
                <div class="login-left">
                    <div class="login-left-title">
                        <h2>重设密码</h2>
                    </div>
                    <div class="login-left-content">
                        <form id="resetPwdForm" class="login-form margintop15" action="${ctx}/auth/resetPwd"
                              method="post">
                            <input name="mobile" value="${mobile}" hidden>
                            <div class="row">
                                <label for="">密码</label>
                                <div class="field">
                                    <input id="password1" name="password" class="login-input" placeholder="输入6-20位英文字母加数字的组合密码" onchange="return OBJ_formcheck.checkFormatResetPwd('repassword','password','密码为空或不符合规则');" maxlength="20" type="password"/>
                                </div>
                            </div>
                            <div class="row">
                                <label for="">确认密码</label>
                                <div class="field">
                                    <input id="repassword" name="repassword" placeholder="请输入与上面一致的密码" maxlength="20" class="login-input" type="password"/>
                                </div>
                            </div>
                            <div>
                                <button class="button btn-login input-full" type="button" onclick="return OBJ_forgotPwd.checkPasswordAll();" >确定</button>
                            </div>
                            <div class="row">
                                <div class="field">
                                    <label for="" id="repassword_tips1" hidden style="color:red;"></label>
                                    <c:if test="${messageSuccess!=null}">
                                        <label for="" id="repassword_tips" style="color:green;">${messageSuccess}
                                        <a href="${ctx}/auth/login?mobile=${param.mobile}" style="color:dodgerblue;">${messageLogin}</a>
                                        </label></c:if>
                                    <c:if test="${error!=null}">
                                        <label for="" id="repassword_tips" style="color:red;">${error}
                                            <a href="${ctx}/auth/forgotPwd">前往</a></label></c:if>
                                </div>
                            </div>
                            <div class="login-msg">
                                如有账号异常问题，请咨询在线客服。
                                <a class="login-forget" href="${ctx}/auth/login?mobile=${param.mobile}">返回登录</a>
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
    $(function () {
        $('.login-form .row .login-input').focus(function () {
            $(this).parent('.field').parent('.row').addClass('current').siblings().removeClass('current');
        })
    });
</script>
<%@include file="../footer.jsp" %>
