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
    <title>版主申请</title>
</head>

<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()">
                    <i class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">版主管理</div>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <div class="page">
            <div class="section tabs">
                <div class="section-content">
                    <div class="category-manage tabs">
                        <ul class="section-tab tabs-nav clearfix">
                            <li class="tab-menu tabs-nav-item current">我管理的版块</li>
                            <li class="tab-menu tabs-nav-item">版主申请</li>
                        </ul>
                        <div class="tabs-panel">
                            <div class="tab">
                                <ul class="category-manage-list">
                                    <c:if test="${empty moderatorCategories}">
                                        <div class="doc-empty">
                                            您查看的版块暂无内容
                                        </div>
                                    </c:if>
                                    <c:forEach items="${moderatorCategories}" var="c">
                                        <li class="clearfix">
                                            <img class="category-icon" width="166" height="166" alt="${fn:escapeXml(c.categoryName)}" src="${bt:url(c.categoryIcon,"/files/" ,"/assets/images/logo.jpg" )}"/>
                                            <div class="category-info">
                                                <div>
                                                    <div class="category-name">${fn:escapeXml(c.categoryName)}</div>
                                                </div>
                                                <div class="category-btn">
                                                    <c:if test="${c.state == 1}">
                                                        <a class="category-label blue" href="${ctx}/m/moderator/manage/${c.categoryId}/0">管理</a>
                                                    </c:if>
                                                    <c:if test="${c.state == 2}">
                                                        <a class="category-label orange" href="javascript:;">申请中</a>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>

                            <div class="tab">
                                <ul class="category-manage-list">
                                    <c:forEach items="${notModeratorCategories}" var="c">
                                        <li class="clearfix">
                                            <img class="category-icon" width="166" height="166" alt="${fn:escapeXml(c.name)}" src="${bt:url(c.icon,"/files/" ,"/assets/images/logo.jpg" )}"/>
                                            <div class="category-info">
                                                <div>
                                                    <div class="category-name">${fn:escapeXml(c.name)}</div>
                                                </div>
                                                <div class="category-btn">
                                                    <a class="category-label green" href="javascript:;" onclick="applyModerator(${c.id})">申请</a>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <footer>
        </footer>
    </div>
    <div class="page-mask"></div>
</div>

<div class="dialog">
    <div class="container">
        <div class="dialog-box">
            <div class="dialog-header">版主申请 <i class="fa fa-close close"></i></div>
            <div class="dialog-content">
                <form class="login-form" action="${ctx}/m/moderator/moderatorApply" method="post">
                    <input type="hidden" name="categoryId" id="categoryId">
                    <ul class="dialog-group">
                        <c:if test="${needUserInfo}">
                            <li class="clearfix">
                                <label for="userName">姓名</label>
                                <input class="dialog-input" id="userName" name="userName" type="text" value="${user.name}"/>
                            </li>
                            <li class="clearfix">
                                <label for="userQq">QQ号</label>
                                <input class="dialog-input" id="userQq" name="userQq" type="number" value="${user.qq}">
                            </li>
                            <li class="clearfix">
                                <label for="userEmail">Email</label>
                                <input class="dialog-input" id="userEmail" name="userEmail" type="email" value="${user.mailAddress}">
                            </li>
                        </c:if>
                        <li class="clearfix">
                            <label for="remarks">申请理由</label>
                            <textarea id="remarks" name="remarks" class="dialog-textarea-full" placeholder="请务必填上姓名、手机、QQ、邮箱等信息以增加审核通过几率，工作人员会适时与您联系"></textarea>
                        </li>
                    </ul>

                    <div class="dialog-btns clearfix">
                        <center><label style="color:red;" id="errMsg"></label></center>
                    </div>
                    <div class="dialog-btns clearfix">
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-confirm" onclick="checkUserinfo()">确定</button>
                        </div>
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-cancel">取消</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $.fn.extend({
        "dialogMiddle": function () {
            var screenHeight = $(window).height();
            var $el = $(this);
            $el.css('top', screenHeight / 2 - $el.height() / 2);
            return $el;
        }
    });

    var maxSize = ${maxNum};
    var mySize = ${moderatorCategories.size()};
    $(function () {
        $('.category-manage-list li .category-label.green').click(function (event) {
            event.stopPropagation();
            if (mySize >= maxSize) {
                alert("申请版块已达上限");
            } else {
                if (!$('.dialog').is(':visible')) {
                    $('.dialog').dialogMiddle().show();
                    $('.page-mask').show();
                    $(document).one('click', function () {
                        $('.dialog').hide();
                        $('.page-mask').hide();
                    })
                } else {
                    $('.dialog').hide();
                    $('.page-mask').hide();
                }
            }
        });

        $('.dialog').click(function (event) {
            event.stopPropagation();
        });

        $('.dialog .close,.dialog .btn-cancel').click(function () {
            $('.dialog').hide();
            $('.page-mask').hide();
        });
    });
    function applyModerator(categoryId) {
        $('#categoryId').attr("value", categoryId);
    }
    function checkUserinfo() {
        var userRemarks = $('#remarks').val();
        if (${needUserInfo}) {
            var userName = $('#userName').val();
            var userQq = $('#userQq').val();
            var userEmail = $('#userEmail').val();
            if (userName.length == 0) {
                $('#errMsg').html("名字不能为空");
                return false;
            } else if (userQq.length < 5 || userQq.length > 15) {
                $('#errMsg').html("QQ号信息错误");
                return false;
            } else if (userEmail.length == 0 || !/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(userEmail)) {
                $('#errMsg').html("Email信息错误");
                return false;
            } else if (userRemarks.length == 0 || userRemarks.length > 400) {
                $('#errMsg').html("申请理由不能为空或是过长");
                return false;
            } else {
                $('.login-form').submit();
            }
        } else if (userRemarks.length == 0 || userRemarks.length > 400) {
            $('#errMsg').html("申请理由不能为空或是过长");
            return false;
        } else {
            $('.login-form').submit();
        }
    }

</script>
</body>
</html>
