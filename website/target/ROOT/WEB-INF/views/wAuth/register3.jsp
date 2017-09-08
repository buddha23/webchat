<jsp:include page="../header.jsp"/>
<div class="content bg-white">
    <div class="container">
        <div class="register">
            <ul class="stepbar clearfix">
                <a>
                    <li>
                        <div class="step-num">1</div>
                        <div class="step-txt">账户注册</div>
                    </li>
                </a>
                <a>
                    <li>
                        <div class="step-num">2</div>
                        <div class="step-txt">完善资料</div>
                    </li>
                </a>
                <a>
                    <li class="current">
                        <div class="step-num">3</div>
                        <div class="step-txt">注册成功</div>
                    </li>
                </a>
            </ul>
            <div class="register-content clearfix">
                <div class="register-success">恭喜您！已经完成注册，现在可以为您转到您的<a href="${ctx}/user/center">个人中心</a>，或返回<a
                        href="###">网站首页</a>继续浏览。
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../footer.jsp" %>