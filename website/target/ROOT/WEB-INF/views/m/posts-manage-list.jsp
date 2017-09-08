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
    <title>版块管理</title>
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
                <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
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
                <%--<div class="category-toolbar">--%>
                <%--<div class="toolbar-info">分类： 三菱数控 </div>--%>
                <%--<a class="toolbar-filter" href="javascript:;">筛选<i class="fa fa-angle-down"></i></a>--%>
                <%--</div>--%>

                <div class="category-doc-list">
                    <c:forEach items="${postsDtos.content}" var="post">
                        <div class="list-item swiper-container">
                            <div class="swiper-wrapper">
                                <div class="swiper-slide list-content">
                                    <div class="list-wrapper">
                                        <div class="list-info marginleft0">
                                            <div class="list-title">${fn:escapeXml(post.title)}</div>
                                            <div class="list-meta">
                                                <span><i class="icon-clock"></i> <fmt:formatDate value="${post.createTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="list-tips">
                                        <i class="icon-arrow-left"></i>
                                        <div class="tips-txt">管理</div>
                                    </div>
                                </div>
                                <div class="swiper-slide list-btns">
                                    <div class="list-btn-group clearfix">
                                        <a class="list-btn grey" href="${ctx}/m/posts/${post.id}">
                                            <span class="btn-txt">查看</span>
                                        </a>
                                        <a class="list-btn purple" href="javascript:;" onclick="doStick(${post.id})">
                                            <span class="btn-txt">置顶</span>
                                        </a>
                                        <a class="list-btn orange" href="javascript:;">
                                            <span class="btn-txt move-btn" data-postsId="${post.id}">移动</span>
                                        </a>
                                        <a class="list-btn red" href="javascript:;" onclick="deletePosts(${post.id})">
                                            <span class="btn-txt">删除</span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <footer></footer>
</div>
<div class="page-mask"></div>

<div class="dialog">
    <div class="container">
        <div class="dialog-box">
            <div class="dialog-header">移动文档至 <i class="fa fa-close close"></i></div>
            <ul class="category-list">
                <c:forEach items="${categories}" var="c">
                    <li onclick="changeCategory(${c.id},'${c.name}')" data-categoryId="${c.id}">${fn:escapeXml(c.name)}</li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>

<script type="text/javascript">
    var doPostsId = 0;
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
            doPostsId = $(this).attr('data-postsId');
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
                    $('.category-manage .category-list').show();
                    $('.category-manage .category-doc-list').hide();
                    _menuState = true;
                },
                closeMenu: function () {
                    $('.category-manage .category-list').hide();
                    $('.category-manage .category-doc-list').show();
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
        })
    })
</script>

<script>
    function doStick(id) {
        console.log(id);
        $.ajax({
            url: '${ctx}/m/posts/stick/' + id,
            type: 'get',
            success: function () {
                var pag = layer.open({
                    content: '操作成功！',
                    btn: '我知道了',
                    yes: function () {
                        layer.close(pag);
                        location.reload();
                    }
                });
            },
            error: function (r) {
                alert('操作失败');
            }
        });
    }

    function changeCategory(categoryId, cateName) {
        console.log(doPostsId);
        $('.dialog .close,.dialog .btn-cancel').click();
        if (confirm('将该提问移动至板块' + cateName + '?') && doPostsId != 0) {
            $.ajax({
                url: '${ctx}/m/posts/changeCategory',
                type: 'post',
                data: {categoryId: categoryId, postsId: doPostsId},
                success: function () {
                    var pag = layer.open({
                        content: '操作成功！',
                        btn: '我知道了',
                        yes: function () {
                            layer.close(pag);
                            location.reload();
                        }
                    });
                },
                error: function (r) {
                    console.log(r);
                    alert('操作失败');
                }
            });
        }
    }

    function deletePosts(postId) {
        if (confirm('确认删除该问题?')) {
            $.ajax({
                url: '${ctx}/m/posts/delete/' + postId,
                type: 'delete',
                success: function () {
                    var pag = layer.open({
                        content: '操作成功！',
                        btn: '我知道了',
                        yes: function () {
                            layer.close(pag);
                            location.reload();
                        }
                    });
                },
                error: function (r) {
                    console.log(r);
                    alert('操作失败');
                }
            });
        }
    }
</script>
</body>
</html>