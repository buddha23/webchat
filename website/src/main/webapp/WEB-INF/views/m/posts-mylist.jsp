<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>大牛数控社区</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" href="${assets}/plugins/froalaEditor/css/froala_editor.pkgd.min.css" type="text/css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/layer.js"></script>
    <!-- froala editor -->
    <script src="${assets}/plugins/froalaEditor/js/froala_editor.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/languages/zh_cn.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/align.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/colors.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/draggable.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/code_view.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/code_beautifier.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/font_size.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/font_family.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/fullscreen.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/image.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/image_manager.min.js"></script>
    <script src="${assets}/plugins/froalaEditor/js/plugins/table.min.js"></script>
</head>

<body>
<%--帖子主体--%>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()"><i
                        class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">大牛数控</div>
        </div>

        <div class="tabs clearfix">
            <a href="${ctx}/m/">首页</a>
            <a href="${ctx}/m/doc/category">文库</a>
            <a href="${ctx}/m/video/">课堂</a>
            <a class="current" href="${ctx}/m/posts/category">社区</a>
        </div>
    </header>

    <div class="content clearfix">
        <div class="page-qa">
            <div class="qa-toolbar">
                <a class="qa-toolbar-tab" href="${ctx}/m/posts/category">全部</a>
                <a class="qa-toolbar-tab act" href="${ctx}/m/posts/myposts">我的提问</a>
                <a class="qa-toolbar-tab" href="${ctx}/m/posts/manage">版块管理</a>
            </div>
            <div class="qa-list">
                <c:forEach items="${postsInfos.content}" var="postsInfo">
                    <a class="qa-list-item" href="${ctx}/m/posts/${postsInfo.id}">
                        <div class="qa-item-profile">
                            <img class="avatar" alt="" src="${bt:url(postsInfo.userAvatar, '/files/', '/assets/images/default_avatar.png')}"/>
                        </div>
                        <div class="qa-item-info">
                            <div class="qa-author">${fn:escapeXml(postsInfo.userNickname)}</div>
                            <div class="qa-meta">
                                <span><i class="icon-clock"></i>
                                    <fmt:formatDate value="${postsInfo.createTime}" pattern="yyyy-MM-dd"/>
                                </span>
                                <span><i class="icon-eye"></i> ${postsInfo.views}</span>
                                <span><i class="icon-bubble"></i> ${postsInfo.commentsNum}</span>
                            </div>
                        </div>
                        <div class="qa-title">
                            <c:if test="${postsInfo.postsReward.state == 1 && postsInfo.postsReward.type == 1}">
                                <div class="qa-reward qa-icon-offer">${postsInfo.postsReward.amount} 牛人币</div>
                            </c:if>
                            <c:if test="${postsInfo.postsReward.state == 1 && postsInfo.postsReward.type == 2}">
                                <div class="qa-reward qa-icon-offer">${postsInfo.postsReward.amount} 积分</div>
                            </c:if>
                            <c:if test="${postsInfo.postsReward.state%2 == 0}">
                                <div class="qa-reward qa-icon-end">已悬赏</div>
                            </c:if>
                                ${fn:escapeXml(postsInfo.title)}
                        </div>
                        <div class="qa-content">
                            <p>${bt:html2Text(postsInfo.content)}</p>
                        </div>
                    </a>
                </c:forEach>
            </div>
            <bt:pagination data="${postsInfos}"/>
        </div>
    </div>

    <footer>
    </footer>
    <div class="page-mask" style="display: none;"></div>
    <div class="float-btns">
        <div class="question-btn">
            <i class="icon-pencil"></i>
        </div>
    </div>
</div>

<jsp:include page="posts-editor.jsp"/>
</body>

<script>

    $('.question-btn').click(function () {
        $('.window').show(0, function () {
            disable_scroll();
            jQuery('body').css('overflow-y', 'hidden');
        });
    });

</script>

</html>
