<jsp:include page="../header.jsp"/>

<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>
        <div class="main" data-menu="finance">
            <div class="box clearfix">
                <div class="box-header">
                    <h1><a href="user-vip.html">购买会员</a> <i class="breadcrumbs fa fa-angle-right"></i> 支付
                    </h1>
                </div>
                <div class="box-content">
                    <div class="box-section">
                        <div class="section-header">订单名称</div>
                        <div class="section-content">
                            <div class="buy-detail">
                                <div class="">VIP会员</div>
                                <div class="buy-num color-vermeil"><em>100</em> 元</div>
                            </div>
                        </div>
                        <div class="section-header margintop20">选择支付方式</div>
                        <div class="section-content">
                            <ul class="buy-payment clearfix">
                                <li class="alipay">
                                    <div class="mask"></div>
                                </li>
                                <li class="wechatpay">
                                    <div class="mask"></div>
                                </li>
                            </ul>
                        </div>
                        <div class="margintop20">
                            <button class="button btn-large btn-green" id="btnConfirm">确认支付</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
<script>
    $(function () {

        $('.buy-payment li').click(function () {
            if ($(this).hasClass('chk')) {
                $(this).removeClass('chk');
            } else {
                $(this).addClass('chk').siblings().removeClass('chk');
            }
        });


        $('#btnConfirm').click(function () {
            if (${sessionScope.user.mobile!=null}) {
                if ($('.chk').hasClass('alipay')) {
                    $.post('${ctx}' + '/finance/alipay/recharge',
                            {amount: 100, tradeType: 2},
                            function (result) {
                                if (result.code == 10200) {
                                    location.href = result.data;
                                } else {
                                    alert(result.message);
                                }
                            }
                    );
                } else if ($('.chk').hasClass('wechatpay')) {
                    window.open("${ctx}/finance/weixin/payByQr?amount=100&tradeType=2");
                }
            } else {
                layer.alert("请先绑定手机号", {icon: 5}, function () {
                    window.open("${ctx}/user/accountManage");
                });
            }
        });

    });
</script>
