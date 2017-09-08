<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
    <script type="text/javascript" src="${assets}/js/min/swiper.jquery.min.js"></script>
    <title>文档管理</title>
    <style>
        .swiper-container {
            width: 100%;
            height: 100%;
        }

        .swiper-slide {
            /* Center slide text vertically */
            display: -webkit-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
        }

        .list-btns {
            min-width: 100px;
            width: 100%;
            max-width: 80%;
        }

        .list-content {
            width: 100%;
        }
    </style>
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

            <div class="title">版块管理</div>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <div class="page">
            <div class="category-manage">
                <div class="category-toolbar">
                    <div class="toolbar-info">
                        分类： ${fn:escapeXml(category.name)} - <c:if test="${category2 != null}">${category2.name}</c:if>
                        <c:if test="${category2 == null}">全部</c:if></div>
                    <a class="toolbar-filter" href="javascript:;">筛选<i class="fa fa-angle-down"></i></a>
                </div>
                <div class="category-doc-list">
                    <c:forEach items="${docs.content}" var="video">
                        <div class="list-item swiper-container">
                            <div class="swiper-wrapper">
                                <div class="swiper-slide list-content">
                                    <div class="list-wrapper">
                                        <div class="list-icon ${video.fileType}"></div>
                                        <div class="list-info">
                                            <div class="list-title">${fn:escapeXml(video.title)}</div>
                                            <div class="list-meta">
                                                <span>${fn:toUpperCase(video.fileType)}文档</span>
                                                <span>${fn:escapeXml(video.user.name)}</span>
                                                <span>${video.costScore}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="list-tips">
                                        <i class="icon-arrow-left"></i>
                                        <div class="tips-txt">管理</div>
                                    </div>
                                </div>
                                <div class="swiper-slide list-btns clearfix">
                                    <div class="list-btn-group clearfix">
                                        <a class="list-btn green" href="${ctx}/m/doc/${video.id}">
                                            <span class="btn-txt">查看</span>
                                        </a>
                                        <a class="list-btn purple" href="javascript:;" onclick="checkDoc(${video.id})">
                                            <span class="btn-txt">审核</span>
                                        </a>
                                        <a class="list-btn orange" href="javascript:;">
                                            <span class="btn-txt move-btn" data-docId="${video.id}">移动</span>
                                        </a>
                                        <a class="list-btn red" href="javascript:;" onclick="deleteDoc(${video.id})">
                                            <span class="btn-txt">删除</span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="category-list" style="display: none;">
                    <a class="clearfix" href="${ctx}/m/moderator/manage/${category.id}/0">
                        <div class="category-icon"></div>
                        <div class="category-title">全部</div>
                    </a>
                    <c:forEach items="${category2s}" var="c">
                        <a class="clearfix" href="${ctx}/m/moderator/manage/${category.id}/${c.id}">
                            <div class="category-icon"></div>
                            <div class="category-title">${fn:escapeXml(c.name)}</div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <footer>
    </footer>
</div>
<div class="page-mask"></div>

<div class="dialog">
    <div class="container">
        <div class="dialog-box">
            <div class="dialog-header">移动文档至 <i class="fa fa-close close"></i></div>
            <ul class="category-list" id="categories"></ul>
            <ul class="category-list" id="subcategories"></ul>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $.fn.extend({
            "dialogMiddle": function () {
                var screenHeight = $(window).height();
                var $el = $(this);
                $el.css('top', screenHeight / 2 - $el.height() / 2);
                return $el;
            }
        });

        $('.category-doc-list .list-btns .move-btn').click(function (event) {
            event.stopPropagation();
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
        });

        $('.dialog').click(function (event) {
            event.stopPropagation();
        });

        $('.dialog .close,.dialog .btn-cancel').click(function () {
            $('.dialog').hide();
            $('.page-mask').hide();
        });

        var category = (function () {
            var _menuState = false;
            return {
                openMenu: function () {
                    $('.category-list').show();
                    $('.category-doc-list').hide();
                    _menuState = true;
                },
                closeMenu: function () {
                    $('.category-list').hide();
                    $('.category-doc-list').show();
                    _menuState = false;
                },
                getMenuState: function () {
                    return _menuState;
                }
            }
        })();

        //demo
        $('.toolbar-filter').click(function () {
            if (!category.getMenuState())
                category.openMenu();
            else
                category.closeMenu();
        });

        $('.category-list a').click(function () {
            category.closeMenu();
        });

        //版主管理列表
        var toggleMenu = function () {
            if (swiper.previousIndex == 0)
                swiper.slidePrev()
        }
                , menuButton = document.getElementsByClassName('list-tips')
                , swiper = new Swiper('.swiper-container', {
            slidesPerView: 'auto'
            , initialSlide: 0
            , resistanceRatio: .00000000000001
            , onSlideChangeStart: function (slider) {
                if (slider.activeIndex == 0) {
                    menuButton.classList.add('cross');
                    menuButton.removeEventListener('click', toggleMenu, false)
                } else
                    menuButton.classList.remove('cross')
            }
            , onSlideChangeEnd: function (slider) {
                if (slider.activeIndex == 0)
                    menuButton.removeEventListener('click', toggleMenu, false);
                else
                    menuButton.addEventListener('click', toggleMenu, false)
            }
            , slideToClickedSlide: true
        });

        // 获取版块列表
        var categories, docId;
        $.get('${ctx}/i/doc_categories', function (data) {
            categories = data;
            categories.forEach(function (f) {
                $('#categories').append('<li data-fid="' + f.id + '" class="fid">' + f.name + '</li>');
            });
        });
        //移动文档
        $('.move-btn').click(function () {
            docId = $(this).attr('data-docId');
            $('#categories').empty();
            $('#subcategories').empty();
            categories.forEach(function (f) {
                $('#categories').append('<li data-fid="' + f.id + '" class="fid">' + f.name + '</li>');
            });
        });
        $('#categories').on('click', 'li', function () {
            var fid = $(this).attr('data-fid');
            if (fid) {
                categories.forEach(function (f) {
                    $('#categories').empty();
                    if (f.id == fid) {
                        f.children.forEach(function (c) {
                            $('#subcategories').append('<li data-cid="' + c.id + '" class="cid">' + c.name + '</li>');
                        });
                    }
                });
            } else {
                console.log("获取列表出错");
            }
        });
        $('#subcategories').on('click', 'li', function () {
            var cid = $(this).attr('data-cid');
            if (cid && docId) {
                $.ajax({
                    url: '${ctx}/doc/' + docId + '/category',
                    data: {'cid': cid},
                    type: 'POST',
                    success: function () {
                        console.log("操作成功");
                        location.reload();
//                        layer.open({
//                            content: "操作成功",
//                            btn: ["我知道了"],
//                            yes: function () {
//                                location.reload();
//                            }
//                        });
                    },
                    error: function (r) {
                        try {
                            var rt = JSON.parse(r.responseText);
                            alert(rt.message || '移动失败!');
                        } catch (e) {
                            alert("移动失败");
                        }
                    }
                });
            } else {
                console.log("获取列表出错");
            }
        });

    });

    //删除文档
    function deleteDoc(id) {
        var dele = layer.open({
            content: "确认删除该文档么？",
            btn: ['确认', '取消'],
            yes: function () {
                layer.close(dele);
                $.ajax({
                    url: "${ctx}/doc/docDelete",
                    data: {ids: [id]},
                    type: 'post',
                    success: function () {
                        location.reload();
                    },
                    error: function (r) {
                        try {
                            var rt = JSON.parse(r.responseText);
                            if (rt.data && rt.data.length > 0) {
                                alert(rt.data.concat("<br>删除结果:失败"));
                            } else {
                                alert(rt.message || '删除失败');
                            }
                        } catch (e) {
                            layer.alert("删除失败");
                        }
                    }
                });
            }
        });
    }
    //审核文档
    function checkDoc(id) {
        var down = layer.open({
            content: '请选择要执行的操作:<br>' +
            '<div id="checkdoc">' +
            '<label><input type="radio" name="checkdoc" value="1"><span class="marginleft5">通过</span></label>' +
            '<label class="marginleft15"><input type="radio" name="checkdoc" value="0"><span class="marginleft5">不通过</span></label>' +
            '</div>',
            btn: ['确定', '取消'],
            yes: function () {
                var chose = $('input[name="checkdoc"]:checked ').val();
                layer.close(down);
                doReview(id, chose);
            }
        })
    }
    // 执行审核操作
    function doReview(id, reviewOk) {
        $.ajax({
            url: '${ctx}/doc/' + id + '/review',
            data: {'reviewOk': reviewOk},
            type: 'POST',
            success: function () {
                location.reload();
            },
            error: function (r) {
                try {
                    var rt = JSON.parse(r.responseText);
                    alert(rt.message || '审核操作失败!');
                } catch (e) {
                    alert("审核操作失败");
                }
            }
        });
    }
</script>
</body>
</html>
