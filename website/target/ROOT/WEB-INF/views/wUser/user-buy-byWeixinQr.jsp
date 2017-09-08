<jsp:include page="../header.jsp"/>

<div class="content cashier pd20" style="padding-left:100px">
    <div class="container clearfix">
        <div>
            <div class="box margintop10">
                <div class="cashier-content">
                    <div class="cashier-info clearfix">
                        <div>
                            <div id="trade_no">订单号：${trodeNo}</div>
                            <div id="trade_type">订单类型：
                                <strong>
                                    <c:if test="${tradeType==1}">购买牛人币</c:if>
                                    <c:if test="${tradeType==2}">购买会员</c:if>
                                </strong></div>
                        </div>
                        <div>
                            <div class="cashier-price">金额：<strong class="color-vermeil">${amount}</strong> 元</div>
                        </div>
                    </div>
                    <div class="cashier-section" id="pay_div">
                        <div class="cashier-qrcode">
                            <div class="cashier-txt">扫码付款（元）</div>
                            <div class="cashier-price"><strong class="color-vermeil">${amount}</strong></div>
                            <div id="qrcode"></div>
                            <img id="weixin-tips" class="margintop10" src="${ctx}/assets/images/weixin-tips.png"
                                 width="180" alt=""/>
                        </div>
                    </div>
                    <div class="cashier-status" id="pay_success">
                        <div class="cashier-success">购买成功！</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="sd rightside">
        </div>
    </div>
</div>

<script type="text/javascript" src="${assets}/js/jquery.qrcode.min.js"></script>
<script>

    $('#qrcode').qrcode({
        render: "canvas",//也可以替换为table
        width: 200,
        height: 200,
        text: "${codeUrl}"
    });

    var c = setInterval(checkPayStatue, 10000);

    function checkPayStatue() {
        $.ajax({
            type: "get",
            url: "${ctx}/finance/getPayStatus/${trodeNo}",
            success: function (result) {
                if (result == "success") {
                    $("#pay_div").hide();
                    $("#pay_success").show();
                    window.clearInterval(c);
                }
            }

        });


    }


</script>

<jsp:include page="../footer.jsp"/>
