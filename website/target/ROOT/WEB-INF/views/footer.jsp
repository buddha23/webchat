<div class="footer pd">
    <div class="container clearfix">
        <div class="logo"></div>
        <div class="footer-info">
            <div class="footer-header">CNC数控文库</div>
            <ul class="footer-nav clearfix">
                <li><a class="linkcolor linkline" href="#">商务合作</a></li>
                <li><a class="linkcolor linkline" href="${ctx}/suggest" target="_blank">意见反馈</a></li>
                <li><a class="linkcolor linkline" href="#">关于我们</a></li>
                <li><a class="linkcolor linkline" href="${ctx}/auth/copyrightStatement" target="_blank">版权声明</a></li>
                <li><a class="linkcolor linkline" href="${ctx}/auth/contactUs" target="_blank">联系我们</a></li>
            </ul>
            <div class="footer-text">
                © Copyright <a style="color:#99a2aa;" href="http://www.miitbeian.gov.cn/" target="_blank">粤ICP备16119083号</a> 深圳市大牛数控有限责任公司
            </div>
        </div>
        <div class="footer-info">
            <div class="footer-header">快速导航</div>
            <ul class="footer-nav clearfix">
                <li><a class="linkcolor linkline" href="${ctx}/doc">数控文库</a></li>
                <li><a class="linkcolor linkline" href="${ctx}/soft">数控软件</a></li>
                <li><a class="linkcolor linkline" href="${ctx}/video">数控课堂</a></li>
                <li><a class="linkcolor linkline" href="${ctx}/course">精品教程</a></li>
                <li><a class="linkcolor linkline" href="${ctx}/post">数控社区</a></li>
            </ul>
        </div>
        <div class="qr">
            <img class="qr-img" src="${assets}/images/qr.jpg" alt=""/>
            <div class="qr-text">微信公众号二维码</div>
        </div>
    </div>
</div>

<script>
    (function () {
        function getUserInfos() {
            $.get(ctx + '/user/infos', function (data) {
                if (data) {
                    $('.score').html(data['score']['totalScore']);
                    $('.point').html(data['point']['totalPoint']);
                    if (data['isSigns'])
                        $('.userbar-profile .sign-btn').text('已签到').removeAttr('id').unbind();
                    if (data['messages']) {
                        $('.new-message').html(data['messages']).show();
                    }
                    if(data['isModerator']) {
                        $('.moderator').show();
                    }
                    if(data['isMember']) {
                        $('.profile-name').children('i').attr('class','vip-icon');
                    }
                }
            });
        }

        <c:if test="${!empty sessionScope.user.id}">getUserInfos();</c:if>

        $('.new-message').hide();

        var bp = document.createElement('script');
        var curProtocol = window.location.protocol.split(':')[0];
        if (curProtocol === 'https') {
            bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
        }
        else {
            bp.src = 'http://push.zhanzhang.baidu.com/push.js';
        }
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(bp, s);

        //签到
        $("#userSignin").click(function () {
            $.ajax({
                url: '${ctx}/user/signin',
                type: 'get',
                success: function (data) {
                    if (data == 'OK') {
                        getUserInfos();
                        layer.alert('签到成功!积分+10', {icon: 6, time: 1500});
                    }
                    else
                        layer.alert(data.message, {icon: 7, time: 2000});
                },
                error: function (r) {
                    layer.alert(JSON.parse(r.responseText).message || '签到失败');
                }
            })
        });
    })();
</script>

</body>
</html>