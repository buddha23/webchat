<jsp:include page="../header.jsp">
    <jsp:param name="css" value="/css/validationEngine.jquery.css"/>
    <jsp:param name="css" value="/css/jquery.Jcrop.css"/>
</jsp:include>
<script>
    $('#accountDialog').remove();
</script>
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
                            <li class="current"><a href="javascript:;">绑定/更改手机号</a></li>
                            <li><a href="${ctx}/user/accountRelevance">第三方账号关联</a></li>
                        </ul>

                        <div class="box-pane-content">
                            <form id="page-form" class="page-form" action="${ctx}/user/accountManage" method="post">
                                <div class="row">
                                    <p class="color-grey marginleft20">绑定手机号码可以使用手机号码登录并找回密码，可第一时间掌握文库最新资源信息。</p>
                                    <p class="color-grey marginleft20">手机号若为首次绑定还有牛人币赠送。请您放心，大牛数控不会泄露您的手机号码。</p>
                                </div>

                                <div class="row">
                                    <label for="bindMobile">手机号码 <c:if
                                            test="${sessionScope.user.mobile == null}">(<font style="color: red;">未绑定</font>)</c:if><c:if
                                            test="${sessionScope.user.mobile != null}">(<font style="color: limegreen;">已绑定</font>)</c:if>：</label>
                                    <div class="field clearfix">
                                        <div class="fl">
                                            <input class="ui-input-default validate[required,custom[phone],minSize[11]]"
                                                   maxlength="11" name="bindMobile" id="bindMobile" type="text"
                                                   placeholder="输入绑定手机号"/></div>
                                        <div class="button-tiny" id="bind_sendmsg_button"
                                             onclick="OBJ_bindAccount.sendMsg('bindMobile');">获取验证码
                                        </div>
                                        <div class="button-tiny" id="bind_msg_end" style="display:none;"><span
                                                id="bind_sendmsg_time_text">60</span>秒后重发
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label>验证码：</label>
                                        <div class="field">
                                            <input type="text" id="bindCode" name="bindCode" placeholder="输入验证码"
                                                   class="ui-input-default validate[required,custom[number],maxSize[6]]">
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label>账户密码：</label>
                                        <div class="field">
                                            <input class="ui-input-default validate[required,minSize[6],maxSize[20],custom[onlyLetterNumber]]"
                                                   id="password" name="password" type="password"
                                                   placeholder="输入6-20位英文字母加数字的组合密码"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label>确认密码：</label>
                                        <div class="field">
                                            <input class="ui-input-default validate[required,equals[password],minSize[6],maxSize[20],custom[onlyLetterNumber]]"
                                                   id="repassword" name="repassword" placeholder="请输入与上面一致的密码"
                                                   maxlength="20" type="password"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label></label>
                                        <div class="field">
                                            <input id="leaveId" name="leaveId" hidden>
                                            <button class="button btn-green btn-large" type="button"
                                                    onclick="checkIsBind()">确定
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>

<div id="accountDialog" style="padding: 20px; width: 420px; display: none;">
    <form class="dialog-form">
        <div class="margintop0">
            <div class="item" style="background: #fff4e0; color: #bc841d; padding: 10px;">
                检测到该手机号已注册有站内账号，请选择保留一项。<br>若选择当前账号，则原手机注册账号停用；<br>若选择原手机账号，则当前账号停用。<br>此操作不可逆，请谨慎选择：
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
                    <span class="marginleft5">保留原手机账号</span></label>
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
<script type="text/javascript" src="${assets}/js/forgotPwd.js"></script>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>

<script>
    $(function () {
        $('#page-form').validationEngine({
            promptPosition: "centerRight", // 有5种模式 topLeft, topRight, bottomLeft, centerRight, bottomRight
            scroll: false,
            showOneMessage: true,
            maxErrorsPerField: true
        });

    });
    var isExist = false;
    function checkIsBind() {
        var bindMobile = $('#bindMobile').val();
        var password = $('#password').val();
        var repassword = $('#repassword').val();
        if (checkFormate()) {
            $.ajax({
                url: ctx + '/user/checkBind',
                data: {'bindMobile': bindMobile},
                success: function (req) {
                    if (req == true) {
                        isExist = true;
                        var pagei = layer.open({
                            type: 1,   //0-4的选择,
                            title: "选择账号保留",
                            border: [0],
                            shadeClose: true,
                            area: ['auto', 'auto'],
                            content: $('#accountDialog')
                        });
                    } else {
                        isExist = false;
                        accountRelevance();
                    }
                },
                error: function (r) {
                    layer.alert(JSON.parse(r.responseText).message || "绑定错误");
                    console.log(r);
                }
            });
        } else {
            layer.alert("信息填写不完整", {icon: 5});
            return false;
        }
    }
    function checkFormate() {
        var bindMobile = $('#bindMobile').val();
        var password = $('#password').val();
        var repassword = $('#repassword').val();
        return (OBJ_formcheck.check('phone', bindMobile) && OBJ_formcheck.check('password', password)) && password == repassword;
    }
    function accountRelevance() {
        var type1 = false;
        var type2 = false;
        if ($('#RadioGroup1_0').parent().hasClass('checked')) type1 = true;
        if ($('#RadioGroup1_1').parent().hasClass('checked')) type2 = true;

        if (isExist && type1) {
            $('#leaveId').attr("value", 1);
        } else if (isExist && type2) {
            $('#leaveId').attr("value", 2);
        } else if (isExist && !type1 && !type2) {
            return false;
        } else {
            $('#leaveId').attr("value", 0);
        }
        layer.closeAll();

        $('#page-form').ajaxSubmit({
            success: function (req) {
                layer.alert(req, {icon: 1}, function () {
                    location.reload();
                });
                console.log(req);
            },
            error: function (err) {
                layer.alert(JSON.parse(err.responseText).message || "绑定错误", {icon: 2});
                console.log(err);
            }
        })
    }

    $('#dialogClose').click(function () {
        layer.closeAll();
    })
</script>

<%@include file="../footer.jsp" %>
