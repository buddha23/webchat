<jsp:include page="../header.jsp"/>

<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main" data-menu="finance">
            <div class="box clearfix">
                <div class="box-header">
                    <h1><a href="${ctx}/user/myscore">我的牛人币</a> <i class="fa fa-angle-right breadcrumbs"></i> 购买牛人币</h1>
                </div>
                <div class="box-content">
                    <div class="box-section">
                        <div class="section-header">
                            充值RMB购买牛人币
                            <small>（<strong>1</strong>元 = <strong>10</strong> 牛人币，最低充值 <strong>10</strong> 元）</small>
                            <a href="${ctx}/auth/scoreExplain" class="" target="_blank">
                                <small>牛人币说明</small>
                            </a>
                        </div>
                        <div class="section-content">
                            <ul class="buy-list clearfix">
                                <li data-amount="10">充值 <strong class="color-vermeil">10</strong> 元</li>
                                <li data-amount="20">充值 <strong class="color-vermeil">20</strong> 元</li>
                                <li data-amount="30">充值 <strong class="color-vermeil">30</strong> 元</li>
                                <li data-amount="50">充值 <strong class="color-vermeil">50</strong> 元</li>
                                <li data-amount="100">充值 <strong class="color-vermeil">100</strong> 元</li>
                            </ul>
                            <div class="buy-custom margintop15">
                                其它金额：<input id="inputAmount" class="ui-input-default" style="width: 150px;"/> 元
                                <input name="amount" id="amount" style="display: none;"/>
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
                            <button id="btnConfirm" class="button btn-large btn-green">确认支付</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>


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
        });

        $('#btnConfirm').click(function () {
            if (${sessionScope.user.mobile!=null}) {
                if (!$('#amount').val()){
                  alert("充值金额不能为空！");
                   return;
                  }
                  if ($('#amount').val()<10){
                    alert("充值金额不能小于10元！");
                    return;
                  }
                if ($('.chk').hasClass('alipay')) {
                    $.post('${ctx}/finance/alipay/recharge',
                        {amount: $('#amount').val(),tradeType:1},
                        function (result) {
                            if (result.code == 10200) {
                            location.href = result.data;
                            } else {
                              alert(result.message);
                            }
                       }
                      );
                 }else if ($('.chk').hasClass('wechatpay')){
                     window.open("${ctx}/finance/weixin/payByQr?amount="+$('#amount').val()+"&tradeType=1");
                 }
            } else {
                layer.alert("请先绑定手机号", {icon: 5}, function () {
                    window.open("${ctx}/user/accountManage");
                });
            }
        });

    });
</script>
    <jsp:include page="../footer.jsp"/>
