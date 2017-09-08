<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <title></title>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="javascript:history.back()">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu"></div>

        <div class="title">牛人币充值</div>
    </header>

    <div class="content clearfix">
        <div class="score-buy">
            <div class="buy-header">快速充值</div>
            <ul class="buy-list clearfix">
                <li data-amount="10">
                    <div class="buy-box">
                        <div class="buy-title">100分</div>
                        <div class="buy-meta">10.00元</div>
                    </div>
                </li>
                <li data-amount="20">
                    <div class="buy-box">
                        <div class="buy-title">200分</div>
                        <div class="buy-meta">20.00元</div>
                    </div>
                </li>
                <li data-amount="30">
                    <div class="buy-box">
                        <div class="buy-title">300分</div>
                        <div class="buy-meta">30.00元</div>
                    </div>
                </li>
                <li data-amount="50">
                    <div class="buy-box">
                        <div class="buy-title">500分</div>
                        <div class="buy-meta">50.00元</div>
                    </div>
                </li>
                <li data-amount="100">
                    <div class="buy-box">
                        <div class="buy-title">1000分</div>
                        <div class="buy-meta">100.00元</div>
                    </div>
                </li>
                <li data-amount="200">
                    <div class="buy-box">
                        <div class="buy-title">2000分</div>
                        <div class="buy-meta">200.00元</div>
                    </div>
                </li>
            </ul>

            <div class="buy-header">自定义充值（1元 = 10牛人币，最低充值 <strong>10</strong> 元）</div>
            <div class="buy-wrapper">
                <input type="number" id="inputAmount" class="input-full ui-input-wap" placeholder="充值金额为大于10的整数，单位/元"/>
                <input name="amount" id="amount" style="display: none;"/>
            </div>
            <div class="buy-header">选择支付方式</div>
            <div class="buy-wrapper">
                <ul class="buy-payment clearfix">
                    <li class="alipay">
                        <div class="mask"></div>
                    </li>
                    <li class="wechatpay">
                        <div class="mask"></div>
                    </li>
                </ul>
            </div>

            <div class="buy-wrapper" style="padding-bottom: 30px;">
                <center>
                    <lable style="color:red;" id="errMsg"></lable>
                </center>
                <button id="btnConfirm" class="button btn-green btn-large input-full" type="button">确认支付</button>
                <button class="button btn-light btn-large input-full margintop15" type="button"
                        onclick="javascript:history.back()">返回
                </button>
            </div>
        </div>
    </div>

    <footer>

    </footer>
</div>
<form id="orderForm" action="${ctx}/m/finance/alipay/createOrder" method="post" style="display: none;">
    <input type="hidden" name="amount" id="orderAmount"/>
    <input type="hidden" name ="tradeType" id="tradeType" value="1"/>
</form>
<script>
    $(function () {
        $('.buy-list li').click(function () {
            if ($(this).hasClass('chk')) {
                $(this).removeClass('chk');
            } else {
                $(this).addClass('chk').siblings().removeClass('chk');
                $('#amount').val($(this).data('amount'));
            }
        });

        $('#inputAmount').change(function () {
            $('#amount').val($(this).val());
        });


        $('.buy-payment li').click(function () {
            if ($(this).hasClass('chk')) {
                $(this).removeClass('chk');
            } else {
                $(this).addClass('chk').siblings().removeClass('chk');
            }
            if ($('.chk').hasClass('wechatpay')) {
                $.post('${ctx}/weixin/paySignature', {url: location.href}, function (data) {
                    wx.config({
                        debug: false,
                        appId: data.appId,
                        timestamp: data.timestamp,
                        nonceStr: data.noncestr,
                        signature: data.signature,
                        jsApiList: ['chooseWXPay']
                    });
                });
            }
        });

        $('#btnConfirm').click(function () {

            if ($('.chk').hasClass('alipay')) {
                $('#orderAmount').val($('#amount').val());
                $('#orderForm').submit();
            }
            else if ($('.chk').hasClass('wechatpay')) {
                $.post(
                        '${ctx}' + '/m/finance/weixin/recharge',
                        {amount: $('#amount').val(),tradeType :1},
                        function (result) {
                            wx.chooseWXPay({
                                timestamp: result.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                                nonceStr: result.nonceStr, // 支付签名随机串，不长于 32 位
                                package: result.wxPackage, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                                signType: result.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                                paySign: result.paySign, // 支付签名
                                success: function (res) {
                                }
                            });
                        });
            } else {
            }
        });
        <c:if test="${errMsg != null}">
        $("#errMsg").text('${errMsg}');
        </c:if>
    })


</script>
</body>
</html>
