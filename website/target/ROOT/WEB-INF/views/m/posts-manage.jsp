<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
    <script type="text/javascript">
        $(function(){
            $('.category-manage-list li .category-label.green').click(function(event){
                event.stopPropagation();
                if(!$('.dialog').is(':visible')){
                    $('.dialog').show();
                    $('.page-mask').show();
                    $(document).one('click', function(){
                        $('.dialog').hide();
                        $('.page-mask').hide();
                    })
                }else{
                    $('.dialog').hide();
                    $('.page-mask').hide();
                }
            });

            $('.dialog').click(function(event){
                event.stopPropagation();
            });

            $('.dialog .close,.dialog .btn-cancel').click(function(){
                $('.dialog').hide();
                $('.page-mask').hide();
            });
        })
    </script>
    <title>论坛管理</title>
</head>

<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">论坛管理</div>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <div class="page">
            <div class="section tabs">
                <div class="section-content">
                    <div class="category-manage tabs">
                        <ul class="section-tab tabs-nav clearfix">
                            <li class="tab-menu tabs-nav-item current">我管理的版块</li>
                            <%--<li class="tab-menu tabs-nav-item">管理员申请</li>--%>
                        </ul>
                        <div class="tabs-panel">
                            <div class="tab">
                                <ul class="category-manage-list">
                                    <c:forEach items="${categories}" var="category">
                                        <li class="clearfix">
                                            <img class="category-icon" src="${bt:url(category.icon,"/files/" ,"/assets/images/logo.jpg" )}" width="166" height="166" alt=""/>
                                            <div class="category-info">
                                                <div>
                                                    <div class="category-name">${fn:escapeXml(category.name)}</div>
                                                </div>
                                                <div class="category-btn"><a class="category-label blue" href="${ctx}/m/posts/doManage/${category.id}">管理</a></div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>

                            <%--<div class="tab">--%>
                                <%--<ul class="category-manage-list">--%>
                                    <%--<li class="clearfix">--%>
                                        <%--<img class="category-icon" src="../../images/demo/logo3.png" width="166" height="166" alt=""/>--%>
                                        <%--<div class="category-info">--%>
                                            <%--<div>--%>
                                                <%--<div class="category-name">山崎马扎克</div>--%>
                                            <%--</div>--%>
                                            <%--<div class="category-btn"><a class="category-label orange" href="javascript:;">申请中</a></div>--%>
                                        <%--</div>--%>
                                    <%--</li>--%>
                                <%--</ul>--%>
                            <%--</div>--%>

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

<%--<div class="dialog">--%>
    <%--<div class="container">--%>
        <%--<div class="dialog-box">--%>
            <%--<div class="dialog-header">管理员申请 <i class="fa fa-close close"></i></div>--%>
            <%--<div class="dialog-content">--%>
                <%--<form class="login-form" action="#" method="post">--%>
                    <%--<ul class="dialog-group">--%>
                        <%--<li class="clearfix">--%>
                            <%--<textarea  class="dialog-textarea-full" placeholder="请输入申请理由"></textarea>--%>
                        <%--</li>--%>
                    <%--</ul>--%>

                    <%--<div class="dialog-btns clearfix">--%>
                        <%--<div class="dialog-btns-col">--%>
                            <%--<button type="submit" class="btn-confirm">确定</button>--%>
                        <%--</div>--%>
                        <%--<div class="dialog-btns-col">--%>
                            <%--<button type="button" class="btn-cancel">取消</button>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</form>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
</body>
</html>
