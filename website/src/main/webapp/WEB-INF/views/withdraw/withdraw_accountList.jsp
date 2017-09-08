<jsp:include page="../header.jsp">
    <jsp:param name="css" value="/css/validationEngine.jquery.css"/>
    <jsp:param name="css" value="/css/jquery.Jcrop.css"/>
</jsp:include>
<div class="content management pd20">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="finance">
            <div class="box clearfix">
                <div class="box-header"><h1>财务管理 <i class="breadcrumbs fa fa-angle-right"></i> 账户管理</h1></div>
                <div class="box-content">
                    <div>
                        <div id="addAccount" class="button btn-primary">新增账户</div>
                    </div>
                    <table id="dyntable" class="table-style2 margintop15">
                        <thead>
                        <tr>
                            <th>类型</th>
                            <th>账户</th>
                            <th>户名</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${list}" var="account">
                            <tr>
                                <td>
                                    <c:if test="${account.accountType==1}">支付宝</c:if>
                                    <c:if test="${account.accountType==2}">${account.bankName}</c:if>
                                </td>
                                <td>${account.account}</td>
                                <td>${fn:replace(account.accountName,fn:substring(account.accountName,0,1) ," * "  )}</td>
                                <td>
                                    <a id="edit" class="label label-primary editAccount"
                                       onclick="editAccount(${account.id});">编辑</a>
                                    <a class="label label-red" onclick="deleteAccount(${account.id})">删除</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="sd rightside"></div>
    </div>

    <div class="box-pane-content" style="padding: 20px; width: 420px; display: none;" id="addAcc">
        <form id="addform" class="page-form" action="${ctx}/finance/addAccount" method="post">
            <input type="hidden" id="accountId" value="">
            <div class="row"><label>账户类型：</label>
                <div class="field">
                    <select class="ui-input-default " id="accountType" style="width: 180px;"
                            onchange="changeAccount();">
                        <option value="1">支付宝</option>
                        <%-- <option  value="2">银行卡</option>--%>
                    </select>
                </div>
            </div>
            <div id="ali">
                <div class="row"><label>账户:</label>
                    <div class="field">
                        <input type="text" id="account" name="account" placeholder="输入账户信息" style="width: 180px;"
                               onkeyup="alicky(this);" class="ui-input-default ">
                    </div>
                </div>
            </div>
            <div style="display: none" id="bank">
                <div class="row"><label>卡号:</label>
                    <div class="field">
                        <input type="text" id="bankNo" name="bankNo" style="width: 180px;" placeholder="输入账户信息"
                               onchange="checkNum();" onkeyup="cky(this);"
                               onafterpaste="this.value=this.value.replace(/[^\d]/g,'') " class="ui-input-default ">
                    </div>
                </div>
                <div class="row"><label>开户行:</label>
                    <div class="field">
                        <input type="text" id="bankName" name="bankName" placeholder="输入账户信息" style="width: 180px;"
                               class="ui-input-default "><br><font
                            style="font-size: 10px;color: grey">输入卡号后会智能识别开户行信息</font>
                    </div>
                </div>
            </div>
            <div class="row"><label>户名:</label>
                <div class="field">
                    <input type="text" id="accountName" name="accountName" placeholder="输入户名" style="width: 180px;"
                           class="ui-input-default ">
                </div>
            </div>

            <div class="row"><label>手机号:</label> <font id="mobile">${user.mobile}</font><a
                    href="/user/accountManage">修改</a>
                <input type="hidden" value="${user.mobile}" id="bindMobile">
            </div>
            <div class="row"><label for="Code">验证码:</label>
                <div class="field clearfix" style="width: 300px;">
                    <div class="fl">
                        <input class="ui-input-default " onkeyup="cky(this);" maxlength="6" style="width: 180px;"
                               name="Code" id="Code" type="text" placeholder="输入验证码"/>
                    </div>
                    <div class="button-tiny" id="sendmsg_button" onclick="OBJ_withdraw.sendMsg('bindMobile');">获取验证码
                    </div>
                    <div class="button-tiny" id="msg_end" style="display:none;"><span id="sendmsg_time">60</span>秒后重发
                    </div>
                </div>
            </div>
            <div class="row">
                <label></label>
                <div class="field">
                    <input id="leaveId" name="leaveId" hidden>
                    <button class="button btn-green btn-large" type="button" id="check">确定</button>
                </div>
            </div>
        </form>
    </div>
    <script type="text/javascript" src="${assets}/js/jquery.validationEngine.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${assets}/js/withdraw.js"></script>
    <script type="text/javascript" src="${assets}/js/formcheck.js"></script>
    <script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.dataTables.js"></script>
    <%@include file="../footer.jsp" %>
    <script>
        function cky(obj) {
            var t = obj.value.replace(/[^\d]/g, '');
            if (obj.value != t)
                obj.value = t;
        }
        function alicky(obj) {
            var t = obj.value.replace(/[\u4e00-\u9fa5]/g, '');
            if (obj.value != t)
                obj.value = t;
        }
        $(function () {
            if ($('#dyntable').length > 0) {
                $('#dyntable').dataTable({
                    "sPaginationType": "full_numbers",
                    "ordering": false,
                    "bFilter": false,
                    "bLengthChange": false
                    //"bFilter": false,
                    //"bLengthChange": false
                });
            }
            $("#addAccount").click(function () {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/userAccount',
                    success: function (data) {
                        if (!data["user"]["mobile"]) {
                            layer.alert("用户你好请先进行<a href='/user/accountManage'>手机号绑定</a>")
                        } else if (!data["user"]["idcard"] || data["user"]["idcard"].length < 1) {
                            layer.alert('您未绑定身份证号，不能提现。请先进行<a href="/user/profile">身份证绑定</a>！');
                        } else {
                            $("#accountName").val("");
                            $("#account").val("");
                            $("#bankNo").val("");
                            $("#bankName").val("");
                            $("#Code").val("");
                            layer.open({
                                type: 1,
                                title: '新增账户',
                                shadeClose: true,
                                shade: 0.8,
                                area: ['540px', '450px'],
                                content: $('#addAcc')
                            });
                        }
                    }
                });
            });
            $("#check").click(function () {
                var account, accountName, accountType, code, accountId;
                var bankName = "";
                accountName = $("#accountName").val();
                accountId = $("#accountId").val();
                accountType = $("#accountType").val();
                code = $("#Code").val();
                if ($("#accountType").val() == 1) {
                    account = $("#account").val();
                } else {
                    account = $("#bankNo").val();
                    bankName = $("#bankName").val();
                }
                if (!checkFormate(accountName, code)) {
                    return;
                }
                var msg;
                $.ajax({
                    type: "post",
                    url: ctx + '/finance/addAccount',
                    data: {
                        account: account,
                        accountName: accountName,
                        accountType: accountType,
                        bankName: bankName,
                        code: code,
                        id: accountId
                    },
                    success: function (req) {
                        layer.alert(req.msg, {icon: 1, time: 1000});
                        location.reload();
                    },
                    error: function (r) {
                        msg = JSON.parse(r.responseText).msg;
                        layer.alert(JSON.parse(r.responseText).msg || msg);
                        console.log(r);
                    }
                });

            });
            function checkFormate(accountName, code) {
                if ($("#accountType").val() == 1) {
                    var account = $("#account").val();
                    if (account == "") {
                        layer.alert("账户不能为空！", {icon: 5});
                        return false;
                    }
                    if (!OBJ_formcheck.check('email', account) && !OBJ_formcheck.check('phone', account)) {
                        layer.alert("账户格式不正确，请重新输入！", {icon: 5});
                        return false;
                    }
                } else {
                    var account = $("#bankNo").val();
                    bankName = $("#bankName").val();
                    if (account == "" || account == null) {
                        layer.alert("账户不能为空！", {icon: 5});
                        return false;
                    }
                    if (bankName == "" || bankName == null) {
                        layer.alert("开户行不能为空！", {icon: 5});
                        return false;
                    }
                    if (!OBJ_formcheck.check('bankCard', account)) {
                        layer.alert("银行卡号应为16-19位数字！", {icon: 5});
                        return false;
                    }
                }
                if (accountName == "" || accountName == null) {
                    layer.alert("户名不能为空！", {icon: 5});
                    return false;
                } else if (code.length < 6) {
                    layer.alert("验证码输入错误！", {icon: 5});
                    return false;
                } else if (code == "" || code == null) {
                    layer.alert("验证码不能为空！", {icon: 5});
                    return false;
                } else {
                    return true;
                }
            }

            $('#dialogClose').click(function () {
                layer.closeAll();
            });
        });
        function changeAccount() {
            if ($("#accountType").val() == 1) {
                $("#ali").removeAttr("style");
                $("#bank").attr("style", "display:none");
            } else {
                $("#bank").removeAttr("style");
                $("#ali").attr("style", "display:none");
            }
        }
        function editAccount(id) {
            $.ajax({
                type: 'get',
                url: '${ctx}/finance/editAccount/' + id,
                success: function (result) {
                    var user = result.userAccount;
                    $("#mobile").text(result.mobile);
                    $("#accountName").val(user.accountName);
                    $("#accountType").val(user.accountType);
                    $("#accountId").val(user.id);
                    $("#Code").val("");
                    changeAccount();
                    if (user.accountType == 1) {
                        $("#account").val(user.account);

                    } else {
                        $("#bankNo").val(user.account);
                        $("#bankName").val(user.bankName);

                    }
                    layer.open({
                        type: 1,
                        title: '新增账户',
                        shadeClose: true,
                        shade: 0.8,
                        area: ['600px', '520px'],
                        content: $('#addAcc')
                    });
                }
            });
        }
        function deleteAccount(id) {
            layer.confirm("确定删除提现记录吗？", {btn: ['确定', '取消'], icon: 3}, function () {
                $.ajax({
                    url: ctx + "/finance/deleteAccount",
                    data: {id: id},
                    type: 'post',
                    success: function () {
                        location.reload();
                    },
                    error: function (r) {

                    }
                });
            });
        }
        function checkNum() {
            var bankNo = $("#bankNo").val();
            $.ajax({
                type: "post",
                url: ctx + '/finance/checkBankNo',
                data: {'bankNo': bankNo},
                success: function (data) {
                    $("#bankName").val(data.bankName);
                }, error: function (req) {
                    layer.alert(JSON.parse(req.responseText).msg);
                }
            });
        }
    </script>


