<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
    <script>
        $(function () {
            $('.buy-payment li,.buy-list li').click(function () {
                if ($(this).hasClass('chk')) {
                    $(this).removeClass('chk');
                } else {
                    $(this).addClass('chk').siblings().removeClass('chk');
                }
            })
        });
    </script>
    <title>视频购买</title>
</head>

<body>
<div class="container">
    <header>
        <div class="menu-left">
            <a id="prevPage" class="menu-item" href="javascript:history.back()">
                <i class="fa fa-chevron-left"></i></a>
        </div>

        <div class="menu-right">
            <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
        </div>

        <div class="title">视频购买</div>
    </header>

    <div class="content clearfix">
        <div class="score-buy">
            <div class="buy-header">购买详情</div>

            <div class="buy-wrapper">
                <ol>
                    <li>一次购买，无限次学习</li>
                    <li>普通会员价: ${vodVolumes.costScore} 牛人币</li>
                    <li><a href="${ctx}/m/user/buyVip" target="blank" style="color:red;">VIP尊享价</a>: ${vodVolumes.costScore * discount} 牛人币
                    </li>
                    <li>可现金支付, 支付比例: 10 牛人币 = 1 元</li>
                </ol>
            </div>

            <%--<div class="buy-header">会员价格</div>--%>

            <%--<div class="buy-wrapper paddingtb0">--%>
            <%--<div class="buy-num color-vermeil"><em>100</em> 元/年</div>--%>
            <%--</div>--%>

            <div class="buy-header">选择支付方式</div>
            <div class="buy-wrapper">
                <ul class="buy-payment clearfix">
                    <%--<li class="alipay">--%>
                    <%--<div class="mask"></div>--%>
                    <%--</li>--%>
                    <li class="wechatpay">
                        <div class="mask"></div>
                    </li>
                    <li class="coin">
                        <div class="mask"></div>
                    </li>
                </ul>
            </div>

            <div class="buy-wrapper" style="padding-bottom: 30px;">
                <button class="button btn-green btn-large input-full" type="button" onclick="doPay()">确认支付</button>
                <a class="button btn-light btn-large input-full margintop15" type="button" href="javascript:history.back()">返回</a>
            </div>
        </div>
    </div>

    <footer>

    </footer>
</div>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>

    function doPay() {
        var $wechat = $('.wechatpay');
        var $coin = $('.coin');
        if ($wechat.hasClass('chk')) {
            wxPay();
        } else if ($coin.hasClass('chk')) {
            dnbPay();
        }
    }
    $(function () {
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
    });

    function dnbPay() {
        $.ajax({
            url: '${ctx}/video/buy',
            type: 'post',
            data: {volumeId:${vodVolumes.id}},
            success: function () {
                isBuy = true;
                var pagei = layer.open({
                    content: '购买成功!',
                    btn: '我知道了',
                    yes: function () {
                        layer.close(pagei);
                        location.href = '${ctx}/m/video/intro/${vodVolumes.id}?sectionId=${vodSection.id}';
                    }
                });
            },
            error: function (r) {
                layer.open({
                    content: JSON.parse(r.responseText).message || '购买失败! ',
                    btn: '我知道了'
                });
                console.log(JSON.parse(r.responseText).message, {icon: 2, time: 1500});
            }
        });
    }

    function wxPay() {
        $.post('${ctx}/video/weixinBuy', {volumeId: ${vodVolumes.id}},
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
                }
        );
    }
</script>
</body>
</html>
