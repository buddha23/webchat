<jsp:include page="../header.jsp">
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>
<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="user">
            <div class="box clearfix">
                <div class="box-header">
                    <h1>资料编辑</h1>
                </div>
                <div class="box-content">
                    <form id="page-form" class="form page-form mgr-form margintop15" action="${ctx}/user/updateUser" method="post">
                        <div class="row">
                            <label>头像：</label>
                            <div class="field">
                                <div class="item-field clearfix">
                                    <div class="avatar-img">
                                        <img src="${bt:url(user.avatar, 'files/', 'assets/images/default_avatar.png')}" alt="我的头像">
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
                            <label for="nickname"><small style="color: red;">*</small> 昵称：</label>
                            <div class="field">
                                <input id="nickname" name="nickname" class="ui-input-default" type="text" onKeypress="if(event.keyCode == 32)event.returnValue = false;" maxlength="10" placeholder="起一个好听的昵称吧(必填)" value="${fn:escapeXml(user.nickname)}"/>
                                * 必填
                            </div>
                        </div>
                        <div class="row">
                            <label for="nickname"><small style="color: red;">*</small> 真实姓名：</label>
                            <div class="field">
                                <input id="name" name="name" class="ui-input-default" type="text" onKeypress="if(event.keyCode == 32)event.returnValue = false;" maxlength="10" placeholder="个人真实姓名" value="${fn:escapeXml(user.name)}"/>
                                * 必填
                            </div>
                        </div>
                        <div class="row">
                            <label for="mailAddress"><small style="color: red;">*</small> 邮箱：</label>
                            <div class="field">
                                <input id="mailAddress" name="mailAddress" class="ui-input-default" type="email" onKeypress="if(event.keyCode == 32)event.returnValue = false;" maxlength="80" placeholder="邮箱地址(必填,关系到您的账号找回)" value="${fn:escapeXml(user.mailAddress)}"/>
                                * 必填
                            </div>
                        </div>
                        <div class="row">
                            <label for="qq">QQ号：</label>
                            <div class="field">
                                <input id="qq" name="qq" class="ui-input-default" type="text" onKeypress="if(event.keyCode == 32)event.returnValue = false;" maxlength="12" placeholder="QQ号码" value="${fn:escapeXml(user.qq)}"/>
                                * 版主申请必填项
                            </div>
                        </div>
                        <div class="row">
                            <label for="idcard">身份证号：</label>
                            <div class="field">
                                <input id="idcard" name="idcard" class="ui-input-default" type="text" onKeypress="if(event.keyCode == 32)event.returnValue = false;" maxlength="18" placeholder="身份证号" value="${fn:escapeXml(user.idcard)}"/>
                                * 提现申请必填项
                            </div>
                        </div>
                        <div class="row">
                            <label for="address">地址：</label>
                            <div class="field">
                                <input id="address" name="address" class="ui-input-default" type="text" maxlength="80" placeholder="我的所在地" value="${fn:escapeXml(user.address)}"/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="address">简介：</label>
                            <div class="field">
                                <textarea id="description" name="description" class="ui-textarea-default input-description" type="text" maxlength="200" placeholder="我的个人简介">${fn:escapeXml(user.description)}</textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label></label>
                            <div class="field">
                                <button id="updateProfile" class="button btn-green btn-large" type="submit">保存资料</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="sd rightside">
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
                            <img src="${assets}/images/default_avatar.png" class="img-origin" alt="">
                        </div>
                    </div>
                    <div class="crop-side">
                        <div class="crop-preview">
                            <img src="${assets}/images/default_avatar.png" class="img-preview" alt="Preview">
                            <input type="file" name="file" class="upload-select" id="upload-select" accept="image/*"
                                   style="display: none;"/>
                        </div>
                        <div class="tips"><i class="bek-icon-quote-left"></i> 您可以拖动图片进行裁剪，达到满意效果后点击保存生成头像。
                            <i class="bek-icon-quote-right"></i></div>
                    </div>
                </div>
                <div class="crop-dialog-btns">
                    <a class="btn-yes">保存</a>
                </div>
            </div>
        </div>

    </div>
</div>

<script type="text/javascript" src="${assets}/js/jquery.ajaxfileupload.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="${assets}/js/imgUpload.js"></script>

<%@include file="../footer.jsp" %>
<script>
    <c:if test="${message != null}">
    layer.msg('${message}', {
        icon: 1,
        time: 1000
    });
    </c:if>
    <c:if test="${errMsg != null}">
    layer.alert('${errMsg}', {
        icon: 2
    });
    </c:if>
</script>
