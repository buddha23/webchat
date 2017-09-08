<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(doc.title)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(doc.title)}"/>
    <jsp:param name="description" value="${fn:escapeXml(doc.title)} ${fn:escapeXml(doc.summary)}"/>
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>

<div class="content bg-white pdb40">
    <div class="container">
        <div class="page-doc clearfix">
            <div class="doc-header">
                <h1 class="doc-title">${fn:escapeXml(doc.title)}</h1>
                <div class="doc-meta clearfix">
                    <div class="meta-pic">
                        <img src="${bt:url(doc.user.avatar,'files/','assets/images/demo/p1.jpg')}" alt=""/></div>
                    <div class="meta-info">
                        <div class="clearfix">
                            <div class="meta-info-name">
                                <c:if test="${isModeratorUp.equals(true)}">
                                    <span class="ico-moderator" title="版主"></span>
                                </c:if>
                                <c:if test="${!empty doc.user}">${bt:out(doc.user.nickname, doc.user.name)}</c:if>
                                <c:if test="${empty doc.user}">管理员</c:if>
                            </div>
                            <ul class="meta-info-nums clearfix">
                                <li><em>类型：</em>${fn:toUpperCase(doc.fileType)}</li>
                                <li><em>阅读：</em>${doc.views}</li>
                                <li><em>牛人币：</em>${doc.costScore}</li>
                                <c:if test="${!empty doc.summary}">
                                    <li><em>简介：</em>${doc.summary}</li>
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
                                <c:if test="${doc.state == 1}">
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
                                <div class="reader-tools clearfix">
                                    <div class="reader-count">总页数：${doc.filePages} &nbsp;&nbsp;预览页数：<span
                                            class="current">1</span>/<c:if
                                            test="${images.size()!=0}">${images.size()}</c:if><c:if
                                            test="${images.size()==0}">1</c:if></div>
                                    <div class="tools-panel clearfix">
                                        <a class="tools-btn icon-size-fullscreen" href="javascript:;" title="全屏显示"></a>
                                        <a class="tools-btn icon-size-actual" href="javascript:;" title="恢复窗口"></a>
                                    </div>
                                </div>
                            </div>
                            <div class="reader-box">
                                <div class="float-bar fixed">
                                    <div class="float-btns">
                                        <a class="float-btn to-top" title="返回顶部">
                                            <i class="fa fa-arrow-up"></i>
                                        </a>
                                        <a class="float-btn download-btn" title="下载" href="javascript:;" onclick="OBJ_loginBox.checkLogin(operate.clickDownload)">
                                            <i class="fa fa-download"></i>
                                        </a>
                                    </div>
                                </div>
                                <div class="reader-box-content">
                                    <div class="reader-box-wrapper">
                                        <c:if test="${images.size()!=0}">
                                            <c:forEach items="${images}" var="image">
                                                <img class="reader-page" src="${assets}/images/grey.gif" data-original="${files}/${image.path}" alt="大牛数控"/>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${doc.fileType.matches('jpg|jpeg|png|gif|bmp|ico')}">
                                            <div class="pics">
                                                <img class="reader-page" src="${files}/${doc.filePath}" alt="大牛数控"/>
                                            </div>
                                        </c:if>
                                        <c:if test="${!empty content}">
                                            <div class="reader-page">
                                                <div style="padding: 20px; background: #fff">${content}</div>
                                            </div>
                                        </c:if>
                                        <div class="reader-finish">
                                            <ul class="finish-list">
                                                <li>阅读已结束，如果下载本文需要使用 <span class="color-red">${doc.costScore}牛人币</span>
                                                </li>
                                                <li>
                                                    <a href="javascript:;" onclick="OBJ_loginBox.checkLogin(operate.clickDownload)" class="button btn-blue btn-large btn-download margintop15">下载文档</a>
                                                </li>
                                            </ul>
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
                            <div class="widget-header">相关文档推荐</div>
                            <div class="widget-content">
                                <c:forEach items="${recommendList}" var="video">
                                    <div class="widget-item ${video.fileType}">
                                        <a class="linkcolor linkline"
                                           href="${ctx}/doc/${video.id}">${fn:escapeXml(video.title)}</a>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="widget margintop30">
                            <div class="widget-header">热门文档</div>
                            <div class="widget-content">
                                <div class="hots-list">
                                    <c:forEach items="${hotDocs}" var="video" varStatus="docStatus">
                                        <dl class="clearfix">
                                            <dt>${docStatus.count}</dt>
                                            <dd>
                                                <a href="${ctx}/doc/${video.id}" class="linkcolor linkover">
                                                    <div class="hots-title">${fn:escapeXml(video.title)}</div>
                                                </a>
                                                <div class="count">${video.views}人阅读</div>
                                            </dd>
                                        </dl>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <%--<a href="#0" class="cd-top to-top">Top</a>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>
<%@include file="../footer.jsp" %>

<script type="text/javascript">
    //各项操作
    var id = ${doc.id};
    var score = ${doc.costScore};
    var memberScore = 0;
    if (score && score > 0) {
        memberScore = Math.round(score * 0.5);
    }
    var mobile = '';
    var isDownload = ${isDownload};
    <c:if test="${sessionScope.user.mobile != null}">mobile = ${sessionScope.user.mobile};
    </c:if>
    function doDownload() {
        $.ajax({
            type: 'get',
            url: '${ctx}/doc/dl/' + id,
            success: function (data) {
                isDownload = true;
                window.location.href = "${ctx}/doc/dl?c=" + data['data'];
            },
            error: function (req) {
                var errormsg = JSON.parse(req.responseText);
                if (errormsg.code == 20001) {
                    layer.alert("牛人币不足！前往 <a href='${ctx}/user/myscore/pay' target='_blank' style='color:red;text-decoration:underline;'>充值</a> (<a href='${ctx}/auth/scoreExplain' target='_blank'><small>牛人币说明</small></a>)", {icon: 2});
                } else {
                    layer.alert(errormsg || '下载失败');
                }
                console.warn(req);
            }
        });
    }
    var operate = {
        clickDownload: function () {
            if (score == 0 || isDownload) {
                doDownload();
            } else if (mobile == '') {
                layer.alert("请先绑定手机号", {icon: 5}, function () {
                    window.open("${ctx}/user/accountManage");
                });
            } else {
                var down = layer.confirm('一次购买，无限次下载，普通会员 ' + score + ' 牛人币，<br><a href="${ctx}/userAccount/vip" target="blank" style="color:red;">VIP尊享价</a> ' + memberScore + ' 牛人币，立即支付？',
                        {btn: ['确定', '取消'], icon: 3}, function () {
                            layer.close(down);
                            doDownload();
                        });
            }
        },
        clickCollect: function () {
            var type = $('#collect').html();
            if (type == '<i class="fa fa-star"></i> 收藏') {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/doc/collect?id=' + id,
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
                    url: '${ctx}/doc/uncollect?id=' + id,
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

    //浮动按钮定位
    function floatBarPosition() {
        if ($(document).scrollTop() + $(window).height() > $('.reader-box').offset().top + $('.reader-box').height()) {
            $('.reader-content .float-bar').removeClass('fixed');
        } else {
            $('.reader-content .float-bar').addClass('fixed');
        }
    }

    $(document).scroll(function () {
        readerToolbarPosition();
        asidePosition();
        floatBarPosition();
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

        //全屏/还原
        $(document).on('click', '.reader-tools .icon-size-fullscreen', function () {
            $('.doc-reader .reader-content').addClass('fullscreen');
            readerToolbarWidth();
        });

        $(document).on('click', '.reader-tools .icon-size-actual', function () {
            $('.doc-reader .reader-content').removeClass('fullscreen');
            readerToolbarWidth();
            asidePosition();
        });

        //图片懒加载
        $('.reader-box img').lazyload();

        $('.nav .menu li a[data-nav="doc"]').addClass('current');
    })
</script>

<!-- JiaThis -->
<script type="text/javascript" src="http://v1.jiathis.com/code/jia.js" charset="utf-8"></script>