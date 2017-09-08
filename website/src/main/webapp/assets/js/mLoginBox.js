function mLoginBox() {
    $('#getCaptcha').attr('src', '/assets/captcha.jpg?t=' + new Date().getTime());
    //var bindUrl = window.location.href;
    this.checkLogin = function (event, doMethod) {
        if (!isLogin) {
            this.clickLogin(event);
        } else {
            if (isBind) {
                doMethod();
            } else {
                this.clickBind(event);
            }
        }
    };

    this.login = function () {
        var mobile = $('#mobileLogin').val();
        var password = $('#passwordLogin').val();
        var captchaLogin = $('#captchaLogin').val();
        var $loginTimes = $('#loginTimes');
        var times = parseInt($loginTimes.val());
        if (!OBJ_formcheck.checkFormatM('mobileLogin', 'loginMgs', 'phone', '手机号码格式不对')) return false;
        if (!OBJ_formcheck.checkFormatMPwd('loginMgs', 'password', '密码为空或不符合规则')) return false;
        if (times == null || times > 3) {
            if (!OBJ_formcheck.checkFormatM('captchaLogin', 'loginMgs', 'captcha', '请输入有效的验证码'))
                return false;
        }
        $.ajax({
            type: 'post',
            url: '/m/auth/login',
            data: {username: mobile, password: password, captcha: captchaLogin, loginTimes: times},
            success: function (s) {
                window.location.reload();
            }, error: function (req) {
                $loginTimes.attr('value', times + 1);
                if (times + 1 > 3) {
                    $('#captchaLi').show();
                }
                var err = JSON.parse(req.responseText);
                $('#loginMgs').show();
                $('#loginMgs_tips').val(err.message).addClass('color-red');
            }
        })
    };
    this.clickLogin = function (event) {
        event.stopPropagation();
        if (!$('#dialogLogin').is(':visible')) {
            $('#dialogLogin').show();
            $('.page-mask').show();
            $(document).one('click', function () {
                $('#dialogLogin').hide();
                $('.page-mask').hide();
            })
        } else {
            $('#dialogLogin').hide();
            $('.page-mask').hide();
        }
    };
    this.clickBind = function (event) {
        event.stopPropagation();
        if (!$('#dialogBind').is(':visible')) {
            $('#dialogBind').show();
            $('.page-mask').show();
            $(document).one('click', function () {
                $('#dialogBind').hide();
                $('.page-mask').hide();
            })
        } else {
            $('#dialogBind').hide();
            $('.page-mask').hide();
        }
    };
    $('.captcha-img').click(function () {
        $(this).attr('src', '/assets/captcha.jpg?t=' + new Date().getTime());
    });

    $('.dialog').click(function (event) {
        event.stopPropagation();
    });

    $('.dialog .close,#bindcancel').click(function () {
        $('.dialog').hide();
        $('.page-mask').hide();
    });

    $('.dialog-input').focus(function () {
        $(this).parent('li').addClass('current').siblings().removeClass('current');
    })


}
var OBJ_mLoginBox = new mLoginBox();