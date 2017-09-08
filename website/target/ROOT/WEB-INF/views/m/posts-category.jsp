<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
    <title>大牛数控社区</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" href="${assets}/plugins/froalaEditor/css/froala_editor.pkgd.min.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/min/swiper.jquery.min.js"></script>
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
    <script>
        $(function(){
            $('.float-btns .question-btn').click(function(){
                $('.window').show(0, function(){
                    disable_scroll();
                    jQuery('body').css('overflow-y','hidden');
                });
            });

            $('.window .window-close').click(function(){
                $('.window').hide(0, function(){
                    enable_scroll();
                    jQuery('body').css('overflow-y','visible');
                });
            });

            $('#edit').froalaEditor({
                height: 200,
                language: "zh_cn",
                toolbarButtonsXS: ['bold', 'italic', 'underline', 'insertImage']
                //imageUploadURL: "${ctx}/upload/uploadImage"
            });
        })
    </script>
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

            <div class="title">大牛数控社区</div>
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

            <c:if test="${imgs.size()>0}">
                <div class="swiper-container">
                    <div class="swiper-wrapper">
                        <c:forEach items="${imgs}" var="img">
                            <div class="swiper-slide">
                                <a href="${bt:out(img.link,'javascript:void(0);')}" target="_blank">
                                    <img src="${ctx}/files/user/${img.path}"  alt=""/>
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- 如果需要分页器 -->
                    <div class="swiper-pagination"></div>
                </div>
            </c:if>

            <div class="qa-toolbar">
                <a class="qa-toolbar-tab act" href="${ctx}/m/posts/category">全部</a>
                <a class="qa-toolbar-tab" href="${ctx}/m/posts/myposts">我的提问</a>
                <a class="qa-toolbar-tab" href="${ctx}/m/posts/manage">版块管理</a>
            </div>

            <div class="qa-category">
                <ul class="qa-category-list bg-white clearfix">
                    <c:forEach items="${categories}" var="category">
                    <a href="${ctx}/m/posts/list/${category.id}">
                        <div class="qa-category-pic"><img src="${bt:url(category.icon,"/files/" ,"/assets/images/logo.jpg" )}" alt=""/></div>
                        <div class="qa-category-info">
                            <div class="qa-category-name">${fn:escapeXml(category.name)}</div>
                            <div class="qa-category-meta"><em class="meta-label">帖子：</em>${category.postsNum} <em class="meta-label marginleft15">活跃：</em>${category.commentNum}</div>
                            <div class="qa-category-meta"><em class="meta-label">最新：</em>${fn:escapeXml(category.lastPostsTitle)}</div>
                        </div>
                    </a>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>

    <footer>
    </footer>
</div>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    $(function(){
        $('.float-btns .question-btn').click(function(){
            $('.window').show(0, function(){
                disable_scroll();
                jQuery('body').css('overflow-y','hidden');
            });
        });

        $('.window .window-close').click(function(){
            $('.window').hide(0, function(){
                enable_scroll();
                jQuery('body').css('overflow-y','visible');
            });
        });

        $('#edit').froalaEditor({
            height: 200,
            language: "zh_cn",
            toolbarButtonsXS: ['bold', 'italic', 'underline', 'insertImage']
            //imageUploadURL: "${ctx}/upload/uploadImage"
        });

        var mySwiper = new Swiper ('.swiper-container', {
            pagination: '.swiper-pagination',
            paginationClickable: '.swiper-pagination',
            nextButton: '.swiper-button-next',
            prevButton: '.swiper-button-prev',
            loop: true,
            autoplay: 4000
        })
    })
</script>
<script>
    // 微信分享
    var title = '大牛数控社区';
    var imgUrl = 'http://www.d6sk.com/assets/images/logo_posts.png';
    var desc = '大牛数控-国内最大的数控技术交流社区';
    var link = location.href;

    $.post('${ctx}/weixin/signature', {url: location.href}, function (data) {
        wx.config({
            debug: false,
            appId: data.appId,
            timestamp: data.timestamp,
            nonceStr: data.noncestr,
            signature: data.signature,
            jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone']
        });
    });
    wx.ready(function () {
        wx.onMenuShareTimeline({    // 朋友圈
            title: title,
            link: link,
            imgUrl: imgUrl,
            success: function () {
                console.log('朋友圈分享成功！');
            },
            cancel: function () {
                console.log('朋友圈分享取消！');
            }
        });
        wx.onMenuShareAppMessage({ // 朋友
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('朋友分享成功！');
            },
            cancel: function () {
                console.log('朋友分享取消！');
            }
        });
        wx.onMenuShareQQ({ // QQ
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('QQ分享成功！');
            },
            cancel: function () {
                console.log('QQ分享取消！');
            }
        });
        wx.onMenuShareWeibo({ // 微博
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('微博分享成功！');
            },
            cancel: function () {
                console.log('微博分享取消！');
            }
        });
        wx.onMenuShareQZone({ // QZone
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('QZone分享成功！');
            },
            cancel: function () {
                console.log('QZone分享取消！');
            }
        });
    });
    wx.error(function (res) {
        console.log(res);
    });

</script>
<style>
    .swiper-container {
        width: 100%;
    }
    .swiper-slide {
        text-align: center;
        font-size: 18px;
        background: #fff;

        /* Center slide text vertically */
        display: -webkit-box;
        display: -ms-flexbox;
        display: -webkit-flex;
        display: flex;
        -webkit-box-pack: center;
        -ms-flex-pack: center;
        -webkit-justify-content: center;
        justify-content: center;
        -webkit-box-align: center;
        -ms-flex-align: center;
        -webkit-align-items: center;
        align-items: center;
    }
    .swiper-container img{
        width: 100%;
    }
</style>
</body>
</html>
