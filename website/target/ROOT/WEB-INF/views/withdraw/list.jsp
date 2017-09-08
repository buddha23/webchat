<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="finance">
            <div class="box clearfix">
                <div class="box-header">
                    <h1>充值 & 提现</h1>
                </div>
                <div class="box-content">
                    <div class="box-section">
                        <div class="finance-board clearfix">
                            <div class="account">
                                <div class="clearfix">
                                    <div class="fr">我的牛人币：<strong class="score color-vermeil"></strong></div>
                                    <div class="fl btn-group">
                                        <a class="button btn-orange" href="${ctx}/user/myscore/pay">购买充值</a>
                                        <a class="button btn-orange marginleft5 cashBtn">兑换提现</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="box-pane margintop20">
                            <ul class="subnav clearfix">
                                <li><a href="${ctx}/user/myscore"
                                       onclick="return OBJ_bindAccount.checkBind(this)">充值记录</a></li>
                                <li class="current"><a href="${ctx}/withdrawals/list">提现记录</a></li>
                            </ul>
                            <div class="box-pane-content">
                                <table id="dyntable" class="table-style2">
                                    <thead>
                                    <tr>
                                        <th>日期</th>
                                        <th>牛人币变动</th>
                                        <th>金额（元）</th>
                                        <th>账户类型</th>
                                        <th>账户</th>
                                        <th>状态</th>
                                        <th>备注</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${list.content}" var="wi">
                                        <tr>
                                            <td><fmt:formatDate value="${wi.createTime}"
                                                                pattern="yyyy-MM-dd HH:mm"/></td>
                                            <td>
                                                <span class="color-vermeil">-<fmt:formatNumber
                                                        value="${wi.tradeAmount*10}" type="number"/></span>
                                            </td>
                                            <td><span class="color-green">+<fmt:formatNumber value="${wi.tradeAmount}"
                                                                                             type="number"/></span></td>
                                            <td>
                                                <c:if test="${wi.userAccount.accountType == 1}">支付宝</c:if>
                                            </td>
                                            <td>${wi.userAccount.account}</td>
                                            <td>
                                                <c:if test="${wi.tradeStatus==1}">待处理</c:if>
                                                <c:if test="${wi.tradeStatus==2}">处理中</c:if>
                                                <c:if test="${wi.tradeStatus==3}">成功</c:if>
                                                <c:if test="${wi.tradeStatus==4}">失败</c:if>
                                                <c:if test="${wi.tradeStatus==5}">审核失败</c:if>
                                                <c:if test="${wi.tradeStatus==6}">服务不可用</c:if>
                                            </td>
                                            <td>${wi. failReason}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        var core;
        $('.cashBtn').on('click', function () {
            $.ajax({
                url: ctx + "/userAccount",
                type: 'get',
                success: function (data) {
                    if (data["user"]["totalScore"] && data["user"]["totalScore"] < 500) {
                        layer.alert("提现最低不低于50元,您的牛人币不够！");
                        return;
                    }
                    core = data["user"]["totalScore"] && data["user"]["totalScore"];
                    if (!data["user"]["mobile"]) {
                        layer.alert("您未绑定手机号，不能提现。请先绑定手机号！");
                    } else if (!data["user"]["idcard"] || data["user"]["idcard"].length < 1) {
                        layer.alert('您未绑定身份证号，不能提现。请先绑定身份证号！', function () {
                            location.href = '${ctx}/user/profile';
                        });
                        return;
                    }
                    if (data["account"] && data["account"].length > 0) {
                        generateData(data);
                    } else {
                        layer.alert('请先绑定账户再提现！<a href="' + ctx + '/finance/accountList' + '" class="score-btn cashBtn">前往绑定</a>');
                        return;
                    }
                },
                error: function (r) {
                    window.close();
                }
            });
        });

        function generateData(data) {

            var accountOption = "";
            data["account"].forEach(function (val, index, arr) {
                if (index == 0) {
                    accountOption += '<option value="' + val.id + '" selected="selected">' + val.account + '</option>';
                    $("#accountName").val(val.accountName);
                } else {
                    accountOption += '<option value="' + val.id + '">' + val.account + '</option>';
                }
            });
            $("#accountId").append(accountOption);
            $("#mobile").val(data["user"]["mobile"]);
            var money = parseInt(data['user'].totalScore / 10);
            var score = money * 10;
            $("#all_score").text(score + "牛人币 / ￥" + money);

            var pagei = layer.open({
                type: 1,   //0-4的选择,
                title: '提现申请',
                border: [0],
                shadeClose: true,
                area: ['auto', 'auto'],
                content: $('#cashDialog')
            });
        }

        $(".watch_remark").click(function () {
            layer.open({
                type: 1,
                title: '提现备注',
                shadeClose: true,
                area: ['auto', 'auto'],
                content: $(this).next(".remark").val()
            });
        });
        $("#apply_form").submit(function () {
            var mobile = $("#mobile").val();
            var validCode = $("#validCode").val();
            var password = $("#apassword").val();
            var tradeAmount = $("#tradeAmount").val();
            if (!tradeAmount || !mobile || !validCode || !password) {
                layer.alert("请完善表单数据！");
                return false;
            }
            if (tradeAmount < 50 || tradeAmount * 10 > core) {
                layer.alert("提现金额不能小于50元或大于可提金额！");
                return false;
            }
            $.ajax({
                url: ctx + '/withdrawals/apply',
                type: 'post',
                data: {
                    accountId: $("#accountId").val(),
                    mobile: mobile,
                    validCode: validCode,
                    password: password,
                    tradeAmount: tradeAmount
                },
                success: function () {
                    window.location.href = window.location.href;
                },
                error: function (err) {
                    layer.alert(JSON.parse(err.responseText).message);
                }
            });
            return false;
        });

        //动态表格
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
    })
</script>
<div id="cashDialog" style="padding: 20px; width: 420px; display: none;">
    <form class="dialog-form" method="post" id="apply_form">
        <div class="row margintop0">
            <label for="accountType">类型：</label>
            <div class="item">
                <div>
                    <select class="ui-select-default" name="accountType">
                        <option value="1" selected="selected">支付宝</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="row">
            <label for="accountId">支付宝账户：</label>
            <div class="item">
                <select class="ui-select-default" name="accountId" id="accountId"></select>
            </div>
        </div>


        <div class="row">
            <label for="accountName">户名：</label>
            <div class="item">
                <div><input class="ui-input-default" type="text" name="accountName" id="accountName"/></div>
            </div>
        </div>

        <div class="row">
            <label>本次可提金额：</label>
            <div class="item">
                <div><strong class="color-vermeil"><p id="all_score"></p></strong></div>
            </div>
        </div>

        <div class="row">
            <label for="tradeAmount">提现金额：</label>
            <div class="item">
                <div><input class="ui-input-default" type="text" name="tradeAmount" id="tradeAmount"
                            onkeyup="this.value=this.value.replace(/\D/g,'')"
                            onafterpaste="this.value=this.value.replace(/\D/g,'')"/></div>
            </div>
        </div>

        <div class="row">
            <label for="mobile">手机号：</label>
            <div class="item">
                <div><input id="mobile" name="mobile" class="ui-input-default phonenumber" type="text" readOnly="true"/>
                    <a class="marginleft10" href="">修改</a></div>
            </div>
        </div>

        <div class="row">
            <label for="validCode">验证码：</label>
            <div class="item">
                <div>
                    <input class="ui-input-default phonenumber" type="text" name="validCode" id="validCode"
                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                           onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
                    <div class="button-tiny" id="sendmsg_button" onclick="apply_money.send_sms();">获取验证码</div>
                    <div class="button-tiny" id="msg_end" style="display:none;">
                        <span id="sendmsg_time_text">60</span>秒后重发
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <label for="apassword">账户密码：</label>
            <div class="item">
                <div><input class="ui-input-default" type="password" name="apassword" id="apassword"
                            onkeyup="value=value.replace(/[\u4E00-\u9FA5]/g,'')"
                            onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[\u4E00-\u9FA5]/g,''))"/>
                </div>
            </div>
        </div>

        <div class="row">
            <label></label>
            <div class="item">
                <button class="button btn-primary" type="submit">确定</button>
            </div>
        </div>

        <div class="row">
            <div class="dialog-tips">
                备注 : 1、提现最低不低于50元；<br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2、牛人币提现收取20%平台佣金；<br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3、到账日期大约为2-3个工作日。
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="${assets}/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="${assets}/js/applyMoney.js"></script>
<%@include file="../footer.jsp" %>

