<jsp:include page="../header.jsp"/>

<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main" data-menu="user">
            <div class="box clearfix" style="min-height:440px;">
                <div class="box-header">
                    <h1>个人中心</h1>
                </div>
                <div class="box-content margintop15">
                    <div class="center-info clearfix">
                        <div class="welcome">
                            <div><span class="f16">Hi</span>，${bt:out(user.nickname, user.name)}，欢迎回来！</div>
                            <div><a href="${ctx}/user/profile" onclick="return OBJ_bindAccount.checkBind(this)">资料编辑</a></div>
                        </div>
                        <div class="account">
                            <div class="">我的牛人币：<span class="score f16">0</span></div>
                            <div><a href="${ctx}/user/myscore" onclick="return OBJ_bindAccount.checkBind(this)">牛人币历史</a></div>
                        </div>
                    </div>

                    <div class="center-section margintop15">
                        <div class="section-title">快捷管理</div>
                        <ul class="center-grid clearfix">
                            <li class="grid-upload">
                                <div class="service-name">我上传的文档</div>
                                <div class="service-info"><a href="${ctx}/doc/myUpload" onclick="return OBJ_bindAccount.checkBind(this)">查看</a></div>
                                <div class="service-info"><a class="num" href="${ctx}/doc/myUpload" onclick="return OBJ_bindAccount.checkBind(this)">${myUploadCount}</a>
                                    个上传
                                </div>
                            </li>
                            <li class="grid-download">
                                <div class="service-name">我下载的文档</div>
                                <div class="service-info">
                                    <a href="${ctx}/doc/myDownload" onclick="return OBJ_bindAccount.checkBind(this)">查看</a></div>
                                <div class="service-info">
                                    <a class="num" href="${ctx}/doc/myDownload" onclick="return OBJ_bindAccount.checkBind(this)">${myDownloadCount}</a> 个下载
                                </div>
                            </li>
                            <li class="grid-fav">
                                <div class="service-name">我收藏的文档</div>
                                <div class="service-info"><a href="${ctx}/doc/myFav" onclick="return OBJ_bindAccount.checkBind(this)">查看</a></div>
                                <div class="service-info"><a class="num" href="${ctx}/doc/myFav" onclick="return OBJ_bindAccount.checkBind(this)">${myConllectCount}</a>
                                    个收藏
                                </div>
                            </li>
                        </ul>
                    </div>

                    <c:if test="${user.userInvite != null && user.userInvite.state == 1}">
                    <div class="margintop15 f16 color-009e0f">推广给更多人</div>
                    <div class="box-panel margintop10">
                        <div>专属链接：</div>
                        <div class="margintop10">
                            <input class="ui-input-default" type="text" readonly="" value="${configer.appUrl}m/auth/register?inviteCode=${user.userInvite.inviteCode}"> <button id="copyBtn" class="button btn-green copyText">复制链接</button>
                        </div>
                        <div class="margintop10">
                            专属二维码：
                        </div>
                        <div class="clearfix margintop10">
                            <div class="fl">
                                <img width="120" height="120" src="${ctx}/user/ewm" alt=""/>
                            </div>
                            <div class="fl marginleft20">
                                <p>1、	推广链接与二维码具有同等效果；</p>
                                <p>2、	好友扫码或点击链接后，完成步骤，推广关系自动绑定；</p>
                                <p>3、	被推广人在平台消费金额的20%，奖励给推广人；</p>
                                <p>4、	奖励实时到账，推广人可在牛人币历史中查看来源；</p>
                                <p>5、	出现不足1牛人币的零头，平台采用四舍五入法计算入账；</p>
                            </div>
                        </div>
                    </div>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
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
<jsp:include page="../footer.jsp"/>
