<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>大牛数控推荐邀请</title>
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

        <div class="title">推荐邀请</div>
    </header>

    <div class="content clearfix">
        <div class="content-wrapper">
            <div>专属链接：</div>
            <div class="margintop10">
                <input class="ui-input-default input-full" type="text" readonly value="${configer.appUrl}m/auth/register?inviteCode=${userInvite.inviteCode}">
                <button id="copyBtn" class="button btn-green input-full margintop10 copyText">复制链接</button>
            </div>
            <div class="margintop10">
                专属二维码：
            </div>
            <div class="clearfix">
                <div class="margintop10">
                    <img style="margin: auto; width: 150px; height: 150px; display: block;" src="${ctx}/user/ewm" alt=""/>
                </div>
                <div class="margintop10">
                    <p>1、	推广链接与二维码具有同等效果；</p>
                    <p>2、	好友扫码或点击链接后，完成步骤，推广关系自动绑定；</p>
                    <p>3、	被推广人在平台消费金额的20%，奖励给推广人；</p>
                    <p>4、	奖励实时到账，推广人可在牛人币历史中查看来源；</p>
                    <p>5、	出现不足1牛人币的零头，平台采用四舍五入法计算入账；</p>
                </div>
            </div>
        </div>
    </div>

    <footer>

    </footer>
</div>
<script>
    $(function(){
        $('.copyText').click(function(){
            var input = $(this).prev('input');
            var text = input.val();
            var spreadText = text + "\r\n快来加入最大的数控交流社区——大牛数控！点击链接领取会员专属资格和50牛人币"
            input.val(spreadText);
            input.select(); // 选择对象
            document.execCommand("Copy"); // 执行浏览器复制命

            input.val(text);
            input.select(); // 选择对象
        });
        /*
         $('.copyText').prev('input').on('copy',this,function(){
         setTimeout( function () {
         var text = clipboardData.getData("text");
         if (text) {text = text + "\r\n快来加入最大的数控交流社区——大牛数控！点击链接领取会员专属资格和50牛人币;";
         clipboardData.setData("text", text); } }, 100 )
         })
         */
    });
</script>
</body>
</html>
