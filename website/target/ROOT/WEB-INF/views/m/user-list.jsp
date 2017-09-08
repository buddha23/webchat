<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.Accordion.js"></script>
    <title>个人信息</title>
</head>
<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="${ctx}/m/#usercenter"><i class="fa fa-chevron-left"></i></a>
            </div>

            <div class="title">个人</div>
        </div>
    </header>
    <div class="content clearfix">
        <ul id="accordion" class="accordion">
            <li>
                <div class="link">
                    <i class="icon-users"></i>账号管理
                    <i class="fa fa-angle-down"></i></div>
                <ul class="submenu">
                    <li><a href="${ctx}/m/user/account">账号管理</a></li>
                    <c:if test="${sessionScope.user.mobile != null}">
                        <li><a onclick="OBJ_mLoginBox.checkLogin(event,operate.repwd)">修改密码</a></li>
                    </c:if>
                    <c:if test="${userInvite!=null}">
                        <li><a href="${ctx}/m/user/invite">推荐邀请</a></li>
                    </c:if>
                </ul>
            </li>
            <li>
                <div class="link">
                    <i class="icon-credit-card"></i>我的账户
                    <i class="fa fa-angle-down"></i></div>
                <ul class="submenu">
                    <li><a href="${ctx}/m/withdraw/board">我的牛人币</a></li>
                    <li><a href="${ctx}/m/withdraw/accounts">提现账户管理</a></li>
                    <li><a href="${ctx}/m/user/buyVip">购买会员</a></li>
                    <%--<li><a onclick="OBJ_mLoginBox.checkLogin(event,operate.scoreHistory)">我的牛人币</a></li>--%>
                    <%--<li><a onclick="OBJ_mLoginBox.checkLogin(event,operate.scoreRecharge)">牛人币充值</a></li>--%>
                    <%--<li><a onclick="OBJ_mLoginBox.checkLogin(event,operate.rechargeHistory)">充值记录</a><li>--%>
                </ul>
            </li>
            <c:if test="${sessionScope.user.mobile != null}">
                <li>
                    <div class="link">
                        <i class="icon-graduation"></i>版主管理
                        <i class="fa fa-angle-down"></i></div>
                    <ul class="submenu">
                        <li><a href="${ctx}/m/moderator/apply">版主管理</a></li>
                    </ul>
                </li>
            </c:if>
            <li>
                <div class="link">
                    <i class="icon-question"></i>关于文库
                    <i class="fa fa-angle-down"></i></div>
                <ul class="submenu">
                    <li><a href="${ctx}/m/auth/scoreExplain">牛人币说明</a></li>
                    <li><a href="${ctx}/m/auth/copyrightStatement">版权声明</a></li>
                    <li><a href="${ctx}/m/gzh">战略合作</a></li>
                    <li><a href="${ctx}/m/auth/contactUs">联系我们</a></li>
                </ul>
            </li>
            <li>
                <a class="link" href="${ctx}/m/auth/logout" methods="get">
                    <i class="icon-logout"></i>
                    退出登录
                </a>
            </li>
        </ul>
    </div>
    <footer></footer>
    <div class="page-mask" style="display: none;"></div>
</div>

<%--绑定账号--%>
<div class="dialog" id="dialogBind">
    <div class="container">
        <div class="dialog-box">
            <div class="dialog-header">账号绑定 <i class="fa fa-close close"></i></div>
            <div class="dialog-content">
                <form action="#" method="post">
                    <ul class="dialog-group">
                        <c:if test="${sessionScope!=null&&sessionScope.user!=null}">
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
                        <i class="fa fa-exclamation-circle"></i> 您需要绑定建站内账号，才能够执行该操作
                    </a>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript" src="${assets}/js/mLoginBox.js"></script>
<script type="text/javascript" src="${assets}/js/mBindAccount.js"></script>
<script type="text/javascript">

    var isLogin = false;
    var isBind = false;
    <c:if test="${!empty sessionScope.user}">
    isLogin = true;
    if (${sessionScope.user.loginType == null}) {
        isBind = true;
    } else if (${sessionScope.user.isBind == true}) {
        isBind = true;
    }
    </c:if>
    var operate = {
        scoreRecharge: function () {
            location.href = "${ctx}/m/user/scoreRecharge";
        },
        scoreHistory: function () {
            location.href = "${ctx}/m/user/scoreHistory";
        },
        repwd: function () {
            location.href = "${ctx}/m/user/repwd";
        },
        rechargeHistory: function () {
            location.href = "${ctx}/m/user/rechargeHistory";
        }
    }


</script>
</html>
