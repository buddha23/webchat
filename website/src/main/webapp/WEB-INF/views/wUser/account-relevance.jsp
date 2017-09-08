<jsp:include page="../header.jsp">
    <jsp:param name="css" value="/css/validationEngine.jquery.css"/>
    <jsp:param name="css" value="/css/jquery.Jcrop.css"/>
</jsp:include>

<div class="content management pd20">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="user">
            <div class="box clearfix">
                <div class="box-header">
                    <h1>账号管理</h1>
                </div>
                <div class="box-content">
                    <div class="box-pane">
                        <ul class="subnav clearfix">
                            <ul class="subnav clearfix">
                                <li><a href="${ctx}/user/accountManage">绑定/更改手机号</a></li>
                                <li class="current"><a href="javascript:;">第三方账号关联</a></li>
                                <%--<c:if test="${sessionScope.user.loginType != null}">--%>
                                <%--<li class="current"><a href="javascript:;">绑定/更改手机号</a></li>--%>
                                <%--</c:if>--%>
                                <%--<c:if test="${sessionScope.user.loginType == null}">--%>
                                <%--<li  class="current"><a href="${ctx}/user/accountRelevance">第三方账号关联</a></li>--%>
                                <%--</c:if>--%>
                            </ul>
                        </ul>

                        <div class="box-pane-content">
                            <div class="box-section">
                                <div class="section-content">
                                    <ul class="account-item">
                                        <li class="qq clearfix">
                                            <div class="account-icon"></div>
                                            <div class="account-info">
                                                <c:if test="${qqThird != null}">
                                                    <div>
                                                        <span class="acount-name">QQ：${qqThird.nickname}</span>
                                                    </div>
                                                    <div class=""><a class="relieveQQ" href="javascript:;">解除绑定</a>
                                                    </div>
                                                </c:if>
                                                <c:if test="${qqThird == null}">
                                                    <div>
                                                        <span class="acount-name">QQ：未绑定</span>
                                                    </div>
                                                    <div class=""><a class="bind-qq" href="javascript:;">绑定账号</a></div>
                                                </c:if>
                                            </div>
                                        </li>
                                        <li class="wechat clearfix">
                                            <div class="account-icon"></div>
                                            <div class="account-info">
                                                <c:if test="${weixinThird != null}">
                                                    <div>
                                                        <span class="acount-name">微信：${weixinThird.nickname}</span>
                                                    </div>
                                                    <div class=""><a class="relieveWeixin" href="javascript:;">解除绑定</a>
                                                    </div>
                                                </c:if>
                                                <c:if test="${weixinThird == null}">
                                                    <div>
                                                        <span class="acount-name">微信：未绑定</span>
                                                    </div>
                                                    <div class=""><a class="bind-wechat" href="javascript:;" href="#">绑定微信</a>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>

<div id="accountDialog2" style="padding: 20px; width: 420px; display: none;">
    <form class="dialog-form">
        <div class="margintop0">
            <div class="item" style="background: #fff4e0; color: #bc841d; padding: 10px;">
                ${checkleave}
            </div>
        </div>

        <div class="margintop15">
            <div class="">
                <label>
                    <input type="radio" name="RadioGroup1" value="单选" id="RadioGroup1_0">
                    <span class="marginleft5">保留当前账号</span></label>
            </div>
            <div class="margintop10">
                <label>
                    <input type="radio" name="RadioGroup1" value="单选" id="RadioGroup1_1">
                    <span class="marginleft5">保留原关联账号</span></label>
            </div>
        </div>

        <div class="margintop15">
            <div class="item">
                <button class="button btn-primary" type="button" onclick="accountRelevance()">确定</button>
                &nbsp;
                <button id="dialogClose" class="button btn-grey" type="button">取消</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript" src="${assets}/js/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.validationEngine-zh_CN.js"></script>
<script>
    $(function () {
        $('#page-form').validationEngine({
            promptPosition: "centerRight", // 有5种模式 topLeft, topRight, bottomLeft, centerRight, bottomRight
            scroll: false,
            showOneMessage: true,
            maxErrorsPerField: true
        });
    });
    <c:if test="${msg != null}">layer.alert("${msg}", {icon: 1, time: 1500});
    <%--console.log("${msg}");--%>
    </c:if>

    <c:if test="${checkleave != null}">
    var pagei = layer.open({
        type: 1,   //0-4的选择,
        title: "选择账号保留",
        border: [0],
        shadeClose: true,
        area: ['auto', 'auto'],
        content: $('#accountDialog2')
    });

    function accountRelevance() {
        var oldBindId = ${oldBindId};
        var type1 = false;
        var type2 = false;
        var leaveId = 0;
        if ($('#RadioGroup1_0').parent().hasClass('checked')) type1 = true;
        if ($('#RadioGroup1_1').parent().hasClass('checked')) type2 = true;

        if (type1) {
            leaveId = 1;
        } else if (type2) {
            leaveId = 2;
        } else {
            return false;
        }
        layer.closeAll();

        $.ajax({
            url: '${ctx}/user/thirdLeave',
            type: 'post',
            data: {leaveId: leaveId, oldBindId: oldBindId},
            success: function () {
                layer.alert("操作成功!", function () {
                    location.reload();
                });
            },
            error: function (r) {
                layer.alert("操作失败!");
                console.log(r);
            }
        });
    }
    </c:if>
    //QQ第三方登录
    $('.bind-qq').click(function () {
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=${configer.appUrl}user/accountRelevance", "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    });
    //微信第三方登录
    $('.bind-wechat').click(function () {
        window.location.href = 'https://open.weixin.qq.com/connect/qrconnect' +
                '?appid=${configer.wxAccountAppid}' +
                '&redirect_uri=${configer.appUrl}user/accountRelevance/&response_type=code' +
                '&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';
    });
    $('.relieveQQ').click(function () {
        var ques = layer.confirm('解绑后QQ再次登录将不会关联到此账号,确认解除？', {btn: ['确定', '取消'], icon: 3}, function () {
            layer.close(ques);
            doRelieve(1);
        })
    });
    $('.relieveWeixin').click(function () {
        var ques = layer.confirm('解绑后微信再次登录将不会关联到此账号,确认解除？', {btn: ['确定', '取消'], icon: 3}, function () {
            layer.close(ques);
            doRelieve(2);
        })
    });
    function doRelieve(type) {
        $.ajax({
            type: 'get',
            url: '${ctx}/user/relieve?type=' + type,
            success: function () {
                console.log("OK");
                layer.alert("解绑成功", {icon: 1}, function () {
                    location.reload();
                });
            },
            error: function (req) {
                console.log(req);
                layer.alert(req.responseText, {icon: 2, time: 2000});
            }
        });
    }

    $('#dialogClose').click(function () {
        layer.closeAll();
    })

</script>

<%@include file="../footer.jsp" %>
