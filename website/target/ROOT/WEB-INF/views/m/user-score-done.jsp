<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title></title>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="javascript:history.back()">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu"></div>

        <div class="title">订单生成中</div>
    </header>

    <div class="content clearfix">
        <div class="wechat" style="display: none;">
            <div style="margin: 40px 0; text-align: center;">
                请选择菜单在 “浏览器中打开” 完成支付。
            </div>
        </div>
        <div class="mobile" style="display: none;">
            <div style="margin: 40px 0; text-align: center;">
                支付宝支付跳转中...
            </div>
        </div>
    </div>

    <footer>
    </footer>
</div>
<script>
    $(function () {
        if ($('.no-micromessenger').length != 0) {
            $('.mobile').show();
            $('.wechat').hide();
            $('form').remove();

            $.ajax({
                type: "get",
                url: '${ctx}' + ' /m/finance/alipay/payOrder',
                data: {'innerTradeNo': '${fn:escapeXml(param.innerTradeNo)}'},
                success: function (result) {
                    $('body').append(result);
                },
                error: function (result) {
                    if (null != result.responseJSON) {
                        alert(result.responseJSON.message);
                    }
                }
            })
        } else {
            $('.mobile').hide();
            $('.wechat').show();
        }
    })
</script>
</body>
</html>
