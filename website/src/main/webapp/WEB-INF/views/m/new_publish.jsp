<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/animate.min.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/animate.delay.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
    <link rel="stylesheet" type="text/css" href="${assets}/js/mobile/layer.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/jquery.marquee.min.js"></script>
    <script type="text/javascript" src="${assets}/js/jquery.ripple.js"></script>
    <script type="text/javascript" src="${assets}/js/min/swiper.jquery.min.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title></title>
    <script>
        $(document).ready(function () {
            //home view
            //$("#marquee").marquee({yScroll: "bottom",showSpeed: 1000,noMarquee: true});

            var mySwiper = new Swiper('.swiper-banner', {
                pagination: '.swiper-banner-pagination',
                paginationClickable: '.swiper-pagination',
                nextButton: '.swiper-button-next',
                prevButton: '.swiper-button-prev',
                loop: true,
                autoplay: 4000
            });

            var swiper = new Swiper('.swiper-container', {
                pagination: '.swiper-pagination',
                slidesPerView: 4,
                paginationClickable: true
            });

            //usercenter view
            var signScore = 1;
            var $scoreContainer = $('.profile-score');
            var $scoreNum = $('.profile-score span');
            $(document).on('click', '.profile .sign-btn', function () {
                var left = parseInt($scoreNum.position().left) + 10,
                        top = parseInt($scoreNum.position().top) - 10;
                $('.profile-score').append('<div class="sign-score color-red"><b>+' + signScore + '<\/b></\div>');
                $('.sign-score').css({
                    'position': 'absolute',
                    'z-index': '1',
                    'left': left + 'px',
                    'top': top + 'px'
                }).animate({
                    top: top - 10,
                    left: left + 10
                }, 'slow', function () {
                    $(this).fadeIn('fast').remove();
                    var Num = parseInt($scoreNum.text());
                    Num += signScore;
                    $scoreNum.text(Num);
                });
                return false;
            });

            //public view
            $(".userbar .item").click(function (e) {
                $(this).ripple();

                //状态切换
                $(this).addClass('act').siblings().removeClass('act');
            });

            $('.userbar .item.home').click(function () {
                $('.container#home').show().siblings('.container').hide();
            });

            $('.userbar .item.publish').click(function () {
                $('.container#publish').show().siblings('.container').hide();
            });

            $('.userbar .item.usercenter').click(function () {
                $('.container#usercenter').show().siblings('.container').hide();
            });

            //初始化视图，移植时切记此处放最后
            $('.container#home').show().siblings('.container').hide();
            //定位视图
            var view = window.location.hash.substring(1);
            $('.container#' + view).show().siblings('.container').hide();
            $('.userbar .item.' + view).addClass('act').siblings().removeClass('act');

        });
    </script>
</head>

<body>

<div id="publish" class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item menu-sign"></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">CNC数控技术</div>
        </div>

    </header>

    <div class="content bg-white clearfix">
        <div class="publish-list clearfix">
            <a href="${ctx}/m/doc/upload">
                <div class="publish-icon"><i class="icon-docs"></i></div>
                <div class="publish-name">上传文档</div>
            </a>
            <a href="${ctx}/m/posts/list/${postCate.id}?action=publish">
                <div class="publish-icon"><i class="icon-bubbles"></i></div>
                <div class="publish-name">发布帖子</div>
            </a>
            <a href="${ctx}/m/commodity/publish?type=2">
                <div class="publish-icon"><i class="icon-wallet"></i></div>
                <div class="publish-name">求购商品</div>
            </a>
            <a href="${ctx}/m/commodity/publish?type=1">
                <div class="publish-icon"><i class="icon-present"></i></div>
                <div class="publish-name">出售商品</div>
            </a>
        </div>
    </div>

</div>

<div class="container-user">
    <div class="userbar">
        <div class="userbar-menu">
            <a class="item home" href="${ctx}/m/">
                <div class="item-icon"></div>
                <div class="item-name">首页</div>
            </a>
            <a class="item publish act" href="${ctx}/m/publish">
                <div class="item-icon"></div>
                <div class="item-name">发布</div>
            </a>
            <a class="item usercenter" href="${ctx}/m/#usercenter">
                <div class="item-icon"></div>
                <div class="item-name">我</div>
            </a>
        </div>
    </div>
</div>
</body>
</html>
