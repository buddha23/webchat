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
    <script type="text/javascript" src="${assets}/js/jquery.leoDialog.js"></script>
    <title>大牛数控邀请卡</title>
    <style>
        .invite {
        }

        .invite .invite-header {
            position: relative;
            border-bottom: 1px solid #eee;
            overflow: hidden;
        }

        .invite .invite-header img {
            position: absolute;
            top: 15px;
            left: 15px;
        }

        .invite .invite-header nav {
            margin: 15px 15px 15px 75px;
            line-height: 1.2;
        }

        .invite .invite-header nav h1 {
            font-size: 18px;
        }

        .invite .invite-header nav p {
            margin-top: 10px;
            color: #999;
        }

        .invite .invite-content .invite-title {
            text-align: center;
            font-size: 16px;
        }

        .invite .invite-content .invite-subtitle {
            margin-top: 5px;
            text-align: center;
            color: #ff8400;
            font-size: 24px;
            font-weight: bold;
        }

        .invite .invite-content .invite-code {
            background: #f8f8f8;
            margin: 30px 30px 0;
            padding: 10px;
            text-align: center;
            font-size: 14px;
            font-size: 16px;
        }

        .invite .invite-content .invite-meta {
            text-align: center;
            margin-top: 5px;
            color: #999;
        }

        .invite .invite-content .invite-tips {
            color: #999;
            margin-top: 20px;
        }

        .invite .invite-content .invite-qr {
            width: 150px;
            height: 150px;
            margin: 20px auto;
            display: block;
        }

        .invite .invite-wrapper {
            padding: 20px;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="content clearfix">
        <div class="invite">
            <div class="invite-header bg-white">
                <img src="${assets}/images/logo(500).jpg" width="48" height="48" alt=""/>
                <nav>
                    <h1>大牛数控邀请卡</h1>
                    <p>邀请码</p>
                </nav>
            </div>

            <c:if test="${inviteCard != null}">
                <div class="invite-content content-wrapper">
                    <div class="invite-title">(邀请卡) ${fn:escapeXml(inviteCard.objectName)}</div>
                    <div class="invite-subtitle"><i class="fa fa-cny"></i> ${inviteCard.costScore.doubleValue() / 10}</div>
                    <div class="invite-meta">有效日期：2017-12-31</div>
                    <div class="invite-code">邀请码: ${inviteCard.inviteCode}</div>
                    <c:if test="${inviteCard.state!=1}">
                        <div class="invite-meta"><font style="color: red">邀请码无效或已被使用</font></div>
                    </c:if>
                    <div class="invite-wrapper">
                        <c:if test="${inviteCard.state!=1}">
                            <div class="button btn-grey input-full btn-large">
                                <i class="fa fa-check"></i> 立即使用
                            </div>
                        </c:if>
                        <c:if test="${inviteCard.state == 1}">
                            <div class="button btn-blue input-full btn-large" onclick="OBJ_mLoginBox.checkLogin(event,activeCard)">
                                <i class="fa fa-check"></i> 立即使用
                            </div>
                        </c:if>
                    </div>
                    <div class="invite-tips">
                        <p>使用须知：</p>
                        <p>一经使用不能退换，有效期至2017年12月31日</p>
                    </div>
                </div>
            </c:if>

            <c:if test="${inviteCard == null}">
                <div class="invite-content content-wrapper">
                    <img class="invite-qr" src="${assets}/images/qr_video.png" width="120" height="120" alt=""/>
                    <div class="invite-meta">无效的邀请码</div>
                    <div class="invite-tips">
                        <p>使用须知：</p>
                        <p>一经使用不能退换，有效期至2017年12月31日</p>
                    </div>
                </div>
            </c:if>

        </div>
    </div>
</div>
<div class="page-mask" style="display: none;"></div>
<jsp:include page="login-div.jsp"/>

<script>
    var isLogin = false;
    var isBind = false;
    <c:if test="${!empty sessionScope.user}">
    isLogin = true;
    if (${sessionScope.user.mobile != null}) {
        isBind = true;
    } else if (${sessionScope.user.isBind == true}) {
        isBind = true;
    }
    </c:if>

    var code = '${fn:escapeXml(inviteCode)}';
    function activeCard() {
        location.href = '${ctx}/m/video/activate?inviteCode=' + code;
    }

    //QQ第三方登录
    $('#qqLogin').click(function () {
        var url = location.href;
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=" + url,
                "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    });

    $(function () {
        var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == 'micromessenger') {
            $('.weixin').show();
            $('.weixin').click(function () {
                window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize' +
                        '?appid=${configer.wxAccountAppid}' +
                        '&redirect_uri=' + location.href + '&response_type=code' +
                        '&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';
            });
        } else {
            $('.weixin').hide();
            return false;
        }
    });

</script>
</body>
</html>
