<jsp:include page="../header.jsp">
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>
<div class="content bg-white">
    <div class="container">
        <div class="register">
            <ul class="stepbar clearfix">
                <a>
                    <li>
                        <div class="step-num">1</div>
                        <div class="step-txt">账户注册</div>
                    </li>
                </a>
                <a>
                    <li class="current">
                        <div class="step-num">2</div>
                        <div class="step-txt">完善资料</div>
                    </li>
                </a>
                <a>
                    <li>
                        <div class="step-num">3</div>
                        <div class="step-txt">注册成功</div>
                    </li>
                </a>
            </ul>
            <div class="register-content clearfix">
                <form action="${ctx}/auth/register2" id="register-form" class="form register-form" method="post">
                    <div class="row">
                        <label for="avatar">头像：</label>
                        <div class="field">
                            <div class="item-field clearfix">
                                <div class="avatar-img">
                                    <img src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}" alt="我的头像">
                                </div>
                                <div class="avatar-upload">
                                    <div class="upload-tips">
                                        <p>您可以从预设图片中选取一张作为头像</p>
                                        <p>您也可自行上传头像，支持jpg、gif、png、bmp格式</p>
                                        <p></p>
                                    </div>
                                    <div class="upload-btn-wrapper">
                                        <div class="upload-btn">
                                            <span class="upload-icon"><i class="fa fa-upload"></i> 上传头像</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <label for="nickname">
                            <small style="color: red;">*</small>
                            昵称：</label>
                        <div class="field">
                            <input id="nickname" name="nickname" class="ui-input-default" type="text" maxlength="40"
                                   value="${user.nickname}" placeholder="起一个好听的昵称吧(必填)"/>
                        </div>
                    </div>
                    <div class="row">
                        <label for="mailAddress">
                            <small style="color: red;">*</small>
                            邮箱：</label>
                        <div class="field">
                            <%--value="${user.mailAddress}"--%>
                            <input id="mailAddress" name="mailAddress" class="ui-input-default" type="text"
                                   maxlength="100" placeholder="邮箱关系到您的账号找回(必填)"/>
                        </div>
                    </div>
                    <div class="row">
                        <label for="address">地址：</label>
                        <div class="field">
                            <input id="address" name="address" class="ui-input-default" type="text" maxlength="100"
                                   placeholder="我的所在地"/>
                        </div>
                    </div>
                    <div class="row">
                        <label for="description">简介：</label>
                        <div class="field">
                            <textarea id="description" name="description" class="ui-textarea-default input-description"
                                      maxlength="180" type="text" placeholder="我的个人简介"></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <label></label>
                        <div class="item">
                            <button class="button btn-green btn-large" type="button" onclick="submitForm()">完成注册
                            </button>
                            <%--<a class="marginleft10" href="${ctx}/auth/register3">跳过，暂不完善</a>--%>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="crop-dialog clearfix">
    <div class="avatar-select">
        <div class="crop-add">
        </div>

        <div class="crop-dialog-btns">
            <a class="btn-yes">保存</a>
        </div>
    </div>
    <div class="crop-box">
        <div class="clearfix">
            <div class="crop-main">
                <div class="crop-wrapper">
                    <img src="${assets}/images/blank.png" class="img-origin" alt="avatar">
                </div>
            </div>
            <div class="crop-side">
                <div class="crop-preview">
                    <img src="${assets}/images/blank.png" class="img-preview" alt="Preview">
                    <input type="file" name="file" class="upload-select" id="upload-select" style="display: none;"/>
                </div>
                <div class="tips"><i class="bek-icon-quote-left"></i> 您可以拖动图片进行裁剪，达到满意效果后点击保存生成头像。 <i
                        class="bek-icon-quote-right"></i></div>
            </div>
        </div>
        <div class="crop-dialog-btns">
            <a class="btn-yes">保存</a>
        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.ajaxfileupload.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="${assets}/js/imgUpload.js"></script>

<jsp:include page="../footer.jsp"/>
<script type="text/javascript">
    <c:if test="${err != null}">
    layer.alert('${err}', {
        icon: 2,
        time: 1000
    });
    </c:if>
    function submitForm() {
        var nickname = $("#nickname").val();
        var mailaddress = $("#mailAddress").val();
        if (nickname == null || nickname == '' || mailaddress == null || mailaddress == '') {
            layer.alert("昵称和邮箱不能为空 !", {icon: 2, time: 1500});
            return false;
        } else {
            $("#register-form").submit();
        }
    }
</script>