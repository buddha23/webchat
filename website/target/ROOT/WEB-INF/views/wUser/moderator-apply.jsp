<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="moderator">
            <div class="box clearfix">
                <div class="box-header">
                    <h1>版块管理</h1>
                </div>
                <div class="box-content">
                    <c:if test="${needUserInfo}">
                        申请版主和版块管理需要先完善用户基本信息，点击前往<a href="${ctx}/user/profile">完善个人信息</a>。
                    </c:if>
                    <c:if test="${!needUserInfo}">
                    <div class="box-section">
                        <div class="section-header">我管理的版块</div>
                        <div class="section-content">
                            <ul class="category-list clearfix">
                                <c:forEach items="${moderatorCategories}" var="c">
                                    <li class="clearfix">
                                        <img class="category-icon" width="166" height="166"
                                             src="${bt:url(c.categoryIcon,'/files/' ,"assets/images/logo.jpg" )}"
                                             alt="${fn:escapeXml(c.categoryName)}"/>
                                        <div class="category-info">
                                            <div>
                                                <div class="category-name">${fn:escapeXml(c.categoryName)}</div>
                                            </div>
                                            <div class="margintop10">
                                                <c:if test="${c.state==1}">
                                                    <a class="category-label blue"
                                                       href="${ctx}/user/moderators/${c.categoryId}">管理</a>
                                                </c:if>
                                                <c:if test="${c.state==2}">
                                                    <a class="category-label orange" href="javascript:;">申请中</a>
                                                </c:if>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>

                        <div class="section-header margintop20">版主申请版块 (
                            <small>剩余可申请版块数量:
                                <lable id="restNum" style="color:red;"></lable>
                            </small>
                            )
                        </div>
                        <ul class="category-list clearfix">

                            <%--<li class="clearfix">--%>
                            <%--<img class="category-icon" width="166" height="166" alt=""/>--%>
                            <%--<div class="category-info">--%>
                            <%--<div>--%>
                            <%--<div class="category-name">山崎马扎克</div>--%>
                            <%--</div>--%>
                            <%--<div class="margintop10"><a class="category-label orange" href="#">申请中</a></div>--%>
                            <%--</div>--%>
                            <%--</li>--%>

                            <c:forEach items="${notModeratorCategories}" var="c">
                                <li class="clearfix">
                                    <img class="category-icon" width="166" height="166"
                                         src="${bt:url(c.icon,'/files/' ,"assets/images/logo.jpg" )}" alt="${fn:escapeXml(c.name)}"/>
                                    <div class="category-info">
                                        <div>
                                            <div class="category-name">${fn:escapeXml(c.name)}</div>
                                        </div>
                                        <div class="margintop10">
                                            <a class="category-label green" href="javascript:;"
                                               onclick="applyModerator(${c.id})">申请</a>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="sd rightside">

        </div>
    </div>
</div>

<div id="applyDialog" style="padding: 20px; width: 420px; display: none;">
    <form class="dialog-form" action="${ctx}/user/moderatorApply" method="post">
        <div class="">
            <input type="hidden" name="categoryId" id="categoryId">
            <textarea name="remarks" class="ui-textarea-default input-full" placeholder="输入申请信息，请务必填上姓名、手机、QQ、邮箱等信息以增加审核通过几率，工作人员会适时与您联系" maxlength="800"></textarea>
        </div>

        <div class="margintop15">
            <label>
                <input type="checkbox" name="agreeService">
                <span class="marginleft5">我已阅读
                    <a href="${ctx}/auth/aboutModerators" target="_blank">《网站版主职责规范》</a>
                </span>
            </label>
        </div>

        <div class="margintop15">
            <button id="submit_agree" class="button  btn-green" type="button" style="display:none;">确定</button>
            <button id="submit_noagree" disabled="disabled" class="button btn-disable btn-grey" type="button">确定
            </button>
        </div>
    </form>
</div>

<script>
    $(function () {
        $('.bind-qq').on('click', function () {
            var pagei = layer.open({
                type: 1,   //0-4的选择,
                title: '绑定QQ号',
                border: [0],
                shadeClose: true,
                area: ['auto', 'auto'],
                content: $('#bindDialog')
            });
        });

        var num = ${maxNum}-${moderatorCategories.size()};
        if (num < 0) num = 0;
        $('#restNum').html(num);
        if (num <= 0) {
            $('.category-list li .category-label.green').attr("class", 'category-label grey').removeAttr("onclick");
        }
        $('#submit_agree').on("click", function () {
            $(".dialog-form").submit();
        })
    });

    function applyModerator(id) {
        console.log(id);
        $('#categoryId').attr("value", id);
        var pagei = layer.open({
            type: 1,   //0-4的选择,
            title: '申请版主',
            border: [0],
            shadeClose: true,
            area: ['auto', 'auto'],
            content: $('#applyDialog')
        });
    }

    $(function () {
        $('input[name="agreeService"]').on('ifClicked', function (event) {
            $('#submit_noagree').hide();
            $('#submit_agree').show();
        });

        $('input[name="agreeService"]').on('ifUnchecked', function (event) {
            $('#submit_noagree').show();
            $('#submit_agree').hide();
        });
    })
</script>

<jsp:include page="../footer.jsp"/>