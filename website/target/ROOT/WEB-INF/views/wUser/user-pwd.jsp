<jsp:include page="../header.jsp">
    <jsp:param name="css" value="css/jquery.Jcrop.css,css/validationEngine.jquery.css"/>
</jsp:include>
<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="user">
            <div class="box clearfix">
                <div class="box-header">
                    <h1>修改密码</h1>
                </div>
                <div class="box-content">
                    <form id="page-form" class="page-form margintop15" action="${ctx}/user/updatePwd" method="post">
                        <div class="row">
                            <label for=""><span class="color-red">*</span> 原密码：</label>
                            <div class="field">
                                <input id="password" name="password" maxlength="20"
                                       class="ui-input-default validate[required,minSize[6]]"
                                       type="password" placeholder="请输入原密码"/>
                            </div>
                        </div>
                        <div class="row">
                            <label><span class="color-red">*</span> 新密码：</label>
                            <div class="field">
                                <input id="newpassword" name="newpassword" type="password" maxlength="20"
                                       class="ui-input-default validate[required,minSize[6],custom[onlyLetterNumber]]"
                                       placeholder="请输入6-20位新密码"/>
                            </div>
                        </div>
                        <div class="row">
                            <label><span class="color-red">*</span> 确认密码：</label>
                            <div class="field">
                                <input id="renewpassword"
                                       class="ui-input-default validate[required,equals[newpassword]]" type="password"
                                       placeholder="再次输入"/>
                            </div>
                        </div>
                        <div class="row">
                            <label></label>
                            <div class="field">
                                <button class="button btn-green btn-large" type="submit">确定</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.validationEngine-zh_CN.js"></script>
<script>
    $(function () {
        $('#page-form').validationEngine({
            promptPosition: "centerRight", // 有5种模式 topLeft, topRight, bottomLeft, centerRight, bottomRight
            scroll: false,
            showOneMessage: true,
            maxErrorsPerField: true
        });
    })
</script>
<%@include file="../footer.jsp" %>
<script>
    <c:if test="${errMsg!=null}">
    layer.alert('${fn:escapeXml(errMsg)}', {icon: 2, time: 1500});
    </c:if>
</script>