<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>提现账户管理</title>
</head>

<body>
<div class="container">
    <header>
        <div class="menu-left">
            <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
        </div>

        <div class="menu-right">
            <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
        </div>

        <div class="title">提现账户管理</div>
    </header>

    <div class="content clearfix">
        <ul class="finance-account-list">
            <c:forEach items="${accounts}" var="account">
                <li>
                    <div class="">类型：
                        <c:if test="${account.accountType==1}">支付宝</c:if>
                        <c:if test="${account.accountType==2}">${account.bankName}</c:if>
                    </div>
                    <div class="">户名：${account.account}</div>
                    <div class="">
                        账号：${fn:replace(account.accountName,fn:substring(account.accountName,0,1) ," * "  )}</div>
                    <div class="finance-account-operate">
                        <div class="operate-btn" onclick="editAccount(${account.id})">编辑</div>
                        <div class="operate-btn margintop5" onclick="deleteAccount(${account.id})">删除</div>
                    </div>
                </li>
            </c:forEach>
        </ul>
        <div class="page-finance-wrapper">
            <a class="button btn-blue input-full btn-large" href="javascript:;" id="addAccount">
                <i class="fa fa-plus"></i> 新增账号</a>
        </div>
    </div>

    <footer>

    </footer>
</div>
<script type="text/javascript">
    $(function () {
        $("#addAccount").click(function () {
            $.ajax({
                type: 'get',
                url: '${ctx}/finance/accountIsExist',
                success: function (result) {
                    if (!result.isBind) {
                        alert("用户你好,请先进行<a href='${ctx}/m/user/account'>手机号绑定</a>")
                    } else {
                        location.href = '${ctx}/m/withdraw/accountAdd';
                    }
                }
            });
        });

    });

    function deleteAccount(id) {
        if (confirm("确定删除该账户吗？")) {
            $.ajax({
                url: "${ctx}/finance/deleteAccount",
                data: {id: id},
                type: 'post',
                success: function () {
                    location.reload();
                },
                error: function (r) {
                    layer.alert("删除失败", {icon: 2});
                    console.log(r);
                }
            });
        }
    }


    function editAccount(id) {
        location.href = '${ctx}/m/withdraw/accountAdd?accountId=' + id;
    }
</script>
</body>
</html>
