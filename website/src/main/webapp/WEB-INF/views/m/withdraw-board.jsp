<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title></title>
</head>

<body>
<div class="container">
    <header>
        <div class="menu-left">
            <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
        </div>

        <div class="menu-right">
            <a class="menu-item menu-user" href="${ctx}/m/user/center"></a>
        </div>

        <div class="title">账户绑定</div>
    </header>

    <div class="content clearfix">
        <div class="finance-board">
            <div class="clearfix">
                <div class="finance-num"><span class="icon-coin">牛</span> ${userScore.totalScore}</div>
                <div class="finance-tag">我的牛人币</div>
            </div>
            <ul class="finance-count clearfix">
                <li class="fl">
                    <div>${userScore.totalIn} 牛人币</div>
                    <div class="tag-income">总收入</div>
                </li>
                <li class="fr">
                    <div>${userScore.totalOut} 牛人币</div>
                    <div class="tag-income">总支出</div>
                </li>
            </ul>
        </div>
        <div class="finance-cash-list">
            <a class="finance-cash-item" href="${ctx}/m/user/scoreRecharge">账户充值</a>
            <a class="finance-cash-item" href="${ctx}/m/withdraw/apply">申请提现</a>
            <a class="finance-cash-item" href="${ctx}/m/user/scoreHistory">牛人币历史</a>
            <a class="finance-cash-item" href="${ctx}/m/user/rechargeHistory">充值记录</a>
            <%--<a class="finance-cash-item" href="${ctx}/m/withdraw/apply">提现记录</a>--%>
        </div>
    </div>

    <footer>

    </footer>
</div>
</body>
</html>
