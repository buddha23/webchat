<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(soft.title)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(soft.title)}"/>
    <jsp:param name="description" value="${fn:escapeXml(soft.title)} ${fn:escapeXml(soft.description)}"/>
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>

<div class="content bg-white pdb40">
    <div class="container">
        <div class="page-doc clearfix">
            <div class="doc-header">
                <h1 class="doc-title">${fn:escapeXml(soft.title)}</h1>
                <div class="doc-meta clearfix">
                    <div class="meta-pic">
                        <img src="${bt:url(soft.user.avatar,'files/','assets/images/demo/p1.jpg')}" alt=""/></div>
                    <div class="meta-info">
                        <div class="clearfix">
                            <div class="meta-info-name">
                                <c:if test="${!empty soft.user}">${bt:out(soft.user.nickname, soft.user.name)}</c:if>
                                <c:if test="${empty soft.user}">管理员</c:if>
                            </div>
                            <ul class="meta-info-nums clearfix">
                                <li><em>类型：数控软件</em></li>
                                <li><em>阅读：</em>${soft.views}</li>
                                <li><em>牛人币：</em>${soft.costScore}</li>
                                <c:if test="${!empty soft.description}">
                                    <li><em>简介：</em>${soft.description}</li>
                                </c:if>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="doc-content clearfix">
                <div class="main" oncopy="alert('对不起，禁止复制！');return false;">
                    <div class="doc-reader">
                        <div class="reader-content">
                            <div class="reader-toolbar clearfix">
                                <c:if test="${soft.state == 1}">
                                    <a id="download" class="reader-btn btn-download" href="javascript:;"
                                       onclick="OBJ_loginBox.checkLogin(operate.clickDownload)">
                                        <i class="fa fa-cloud-download"></i> 下载</a>
                                </c:if>
                                <c:if test="${isCollect=='unLogin' || isCollect == 'notCollect'}">
                                    <a id="collect" class="reader-btn btn-fav" href="javascript:;"
                                       onclick="OBJ_loginBox.checkLogin(operate.clickCollect)"><i
                                            class="fa fa-star"></i> 收藏</a></c:if>
                                <c:if test="${isCollect=='collect'}">
                                    <a id="collect" class="reader-btn btn-fav" href="javascript:;"
                                       onclick="OBJ_loginBox.checkLogin(operate.clickCollect)"><i
                                            class="fa fa-star-o"></i> 取消收藏</a></c:if>
                                <!-- JiaThis Button BEGIN -->
                                <a class="reader-btn btn-share jiathis" href="http://www.jiathis.com/share/"
                                   target="_blank"><i class="fa fa-share-square"></i> 分享</a>
                                <!-- JiaThis Button END -->
                            </div>
                            <div class="reader-box">
                                <div class="reader-box-content">
                                    <div class="file-detail">
                                        <div class="section navtab1">
                                            ${soft.specification}
                                        </div>
                                        <div class="section navtab2">
                                            <div class="section-content">
                                                <div class="file-finish">
                                                    <ul class="finish-list">
                                                        <li>下载文件需要使用 <span class="color-red"> ${soft.costScore}牛人币</span>
                                                        </li>
                                                        <li>
                                                            <a href="javascript:;"
                                                               class="button btn-blue btn-large btn-download margintop15"
                                                               onclick="OBJ_loginBox.checkLogin(operate.clickDownload)">下载文件</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="aside">
                    <div class="aside-wrapper">
                        <div class="widget">
                            <div class="widget-header">相关软件推荐</div>
                            <div class="widget-content">
                                <c:forEach items="${recommendList}" var="video">
                                    <div class="widget-item">
                                        <a class="linkcolor linkline"
                                           href="${ctx}/soft/${video.uuId}">${fn:escapeXml(video.title)}</a>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="widget margintop30">
                            <div class="widget-header">热门软件</div>
                            <div class="widget-content">
                                <div class="hots-list">
                                    <c:forEach items="${hotSofts}" var="soft" varStatus="docStatus">
                                        <dl class="clearfix">
                                            <dt>${docStatus.count}</dt>
                                            <dd>
                                                <a href="${ctx}/soft/${soft.uuId}" class="linkcolor linkover">
                                                    <div class="hots-title">${fn:escapeXml(soft.title)}</div>
                                                </a>
                                                <div class="count">${soft.views}人阅读</div>
                                            </dd>
                                        </dl>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <a href="#0" class="cd-top to-top">Top</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>
<div id="downloadDialog" style="padding: 20px; width: 540px; display: none;">
</div>
<%@include file="../footer.jsp" %>

<script type="text/javascript">
    //各项操作
    var id = ${soft.uuId};
    var score = ${soft.costScore};
    var memberScore = 0;
    if (score && score > 0) memberScore = Math.round(score * 0.5);
    var mobile = '';
    var isDownload = ${isDownload};
    <c:if test="${sessionScope.user.mobile != null}">mobile = ${sessionScope.user.mobile};</c:if>

    function doDownload() {
        $.ajax({
            type: 'get',
            url: '${ctx}/soft/dl/' + id,
            success: function (data) {
                isDownload = true;
                $("#downloadDialog").empty();
                var downloadhtml = '<ul class="file-list clearfix">';
                data['data'].forEach(function (element, index, array) {
                    downloadhtml += '<li class="clearfix"><div class="file-name">' + element.soft.fileName + '</div>' +
                            '<a class="file-download" href="' + element.url + '"><i class="fa fa-download"></i> 下载</a></li>';
                });
                downloadhtml += '</ul>';
                document.getElementById('downloadDialog').innerHTML += downloadhtml;
                var pagei = layer.open({
                    type: 1,   //0-4的选择,
                    title: "下载附件",
                    border: [0],
                    shadeClose: true,
                    area: ['auto', 'auto'],
                    content: $('#downloadDialog')
                });
            },
            error: function (req) {
                var errormsg = JSON.parse(req.responseText);
                if (errormsg.code == 20001) {
                    //                    layer.alert(errormsg.message, {icon: 2, time: 2000});
                    layer.alert("牛人币不足！前往 <a href='${ctx}/user/myscore/pay' target='_blank' style='color:red;text-decoration:underline;'>充值</a> (<a href='${ctx}/auth/scoreExplain' target='_blank'><small>牛人币说明</small></a>)", {icon: 2});
                } else {
                    layer.alert(errormsg || '下载失败');
                }
                console.warn(req);
                //                alert(req['responseJSON'].message || '下载失败');
            }
        });
    }
    var operate = {
        clickDownload: function () {
            if (score == 0 || isDownload) {
                doDownload();
            } else if (mobile == '') {
                layer.open({
                    content: '请先绑定手机号',
                    btn: ['确定', '取消'],
                    yes: function (index) {
                        layer.close(index);
                        location.href = "${ctx}/user/accountManage";
                    }
                });
            } else {
                var down = layer.open({
                    content: '一次购买，无限次下载，普通会员 ' + score + ' 牛人币，<a href="${ctx}/m/user/buyVip" target="blank" style="color:red;">VIP尊享价</a> ' + memberScore + ' 牛人币，立即支付？',
                    btn: ['确定', '取消'],
                    yes: function (index) {
                        layer.close(index);
                        doDownload();
                    }
                });
            }
        },
        clickCollect: function () {
            var type = $('#collect').html();
            if (type == '<i class="fa fa-star"></i> 收藏') {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/soft/collect?uuId=' + id,
                    success: function (data) {
                        $('#collect').html('<i class="fa fa-star-o"></i> 取消收藏');
                    },
                    error: function (req) {
                        console.log(req);
                        layer.alert("操作失败！");
                    }
                });
            } else {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/soft/uncollect?uuId=' + id,
                    success: function (data) {
                        $('#collect').html('<i class="fa fa-star"></i> 收藏');
                    },
                    error: function (req) {
                        console.log(req);
                        layer.alert("操作失败！");
                    }
                });
            }
        }
    };

    //导航容器宽度
    function readerToolbarWidth() {
        var wrapperWidth = $('.reader-content').width() - parseInt($('.reader-content .reader-toolbar').css('borderLeftWidth')) - parseInt($('.reader-content .reader-toolbar').css('borderRightWidth'));
        $('.reader-content .reader-toolbar').css('width', wrapperWidth);
    }
    ;

    //阅读器工具条定位
    function readerToolbarPosition() {
        if ($(document).scrollTop() > $('.reader-content').offset().top) {
            $('.reader-content .reader-toolbar').addClass('fixed').css('top', 0);
        } else {
            $('.reader-content .reader-toolbar').removeClass('fixed').css('top', 0);
        }
    }

    //侧栏定位
    function asidePosition() {
        if ($(document).scrollTop() > $('.aside').offset().top) {
            $('.aside .aside-wrapper').addClass('fixed').css('top', 0);
        } else {
            $('.aside .aside-wrapper').removeClass('fixed').css('top', 0);
        }

        if ($(document).scrollTop() > ($('.doc-content').offset().top + $('.doc-content').height() - $('.aside .aside-wrapper').height())) {
            $('.aside .aside-wrapper').removeClass('fixed').css('top', $('.doc-content').height() - $('.aside .aside-wrapper').height());
        }
    }

    $(document).scroll(function () {
        readerToolbarPosition();
        asidePosition();
    });

    $(function () {
        readerToolbarWidth();

        //计算当前浏览图片页码
        //动画速度
        var speed = 250, offsetFix = 0;
        $(document).scroll(function () {
            $('.reader-box .reader-page').each(function (index, element) {
                var $ctx = $(this);
                rangeTop = parseInt($(element).offset().top - offsetFix, 10),
                        rangeBottom = parseInt($(element).offset().top + $(element).height(), 10);
                if ($(document).scrollTop() >= rangeTop && $(document).scrollTop() <= rangeBottom) {
                    $('.reader-count .current').text(index + 1);
                }
            });
        });

        <%--//全屏/还原--%>
        <%--$(document).on('click', '.reader-tools .icon-size-fullscreen', function () {--%>
        <%--$('.doc-reader .reader-content').addClass('fullscreen');--%>
        <%--readerToolbarWidth();--%>
        <%--});--%>

        $(document).on('click', '.reader-tools .icon-size-actual', function () {
            $('.doc-reader .reader-content').removeClass('fullscreen');
            readerToolbarWidth();
            asidePosition();
        });

        <%--//图片懒加载--%>
        <%--$('.reader-box img').lazyload({effect: "fadeIn"});--%>

        $('.nav .menu li a[data-nav="soft"]').addClass('current');
    })
</script>

<!-- JiaThis -->
<script type="text/javascript" src="http://v1.jiathis.com/code/jia.js" charset="utf-8"></script>