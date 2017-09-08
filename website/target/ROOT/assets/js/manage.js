// JavaScript Document

//获取当前日期时间
function timeprint() {
    var week;
    var date;
    var today = new Date();
    var year = today.getFullYear();
    var month = today.getMonth() + 1;
    var day = today.getDate();
    var ss = today.getDay();
    var hours = today.getHours();
    var minutes = today.getMinutes();
    var seconds = today.getSeconds();
    date = year + "/" + month + "/" + day;
    if (ss == 0) week = "星期日";
    if (ss == 1) week = "星期一";
    if (ss == 2) week = "星期二";
    if (ss == 3) week = "星期三";
    if (ss == 4) week = "星期四";
    if (ss == 5) week = "星期五";
    if (ss == 6) week = "星期六";
    if (minutes <= 9)
        minutes = "0" + minutes;
    if (seconds <= 9)
        seconds = "0" + seconds;

    $('.curtain .clock').html(hours + ':' + minutes);
    $('.curtain .date').html(date);
    setTimeout("timeprint()", 1000);
}

//高度设置
function initHeight() {
    var contentHeight = $(window).height() - $('.top').height() - $('.nav').height() - $('.footer').height();
    //var menuHeight = $('.leftside').height();
    //contentHeight = contentHeight < menuHeight ? menuHeight : contentHeight;
    $('.content').css('min-height', contentHeight);
}

$(window).load(function () {
    //初始化高度
    initHeight();
});

$(function () {
    //窗帘日期时间
    timeprint();

    //菜单绑定对应页
    var $menu = $('[data-menu]');
    if ($menu && $menu.length > 0) {
        var menu = $menu.attr('data-menu');
        if (menu) {
            var menus = menu.split('.');
            menus.forEach(function (m, i) {
                $('.menu-' + m).addClass('active');
            });
        }
    }

    //菜单
    $('.management .menu > li').each(function (index, element) {
        var $el = $(this);
        if ($el.children('.menu-sub').length != 0) {
            if ($el.hasClass('active')) {
                $el.find('ul').slideDown();
            }
            $el.children('a').click(function () {
                if (!$el.hasClass('active')) {
                    $el.children('.menu-sub').slideDown(250, function () {
                        $el.addClass('active');
                    });
                    $el.siblings('.active').children('.menu-sub').slideUp(250, function () {
                        $el.siblings('.active').removeClass('active');
                    });
                }
                else if ($el.hasClass('active')) {
                    $el.children('.menu-sub').slideUp(250, function () {
                        $el.removeClass('active');
                    });
                }
            });
        } else {
            $el.addClass('no-sub');
        }
    });

    //动态表格
    if ($('.dyntable').length > 0) {
        $('.dyntable').dataTable({
            "sPaginationType": "full_numbers",
            "ordering": false
            //"bFilter": false,
            //"bLengthChange": false
        });
    }
});