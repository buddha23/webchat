function loginBox() {

    $('#getCaptcha').attr('src', '/assets/captcha.jpg?t=' + new Date().getTime());
    this.checkLogin = function (doMethod) {
        if (!isLogin) {
            var pagei = layer.open({
                type: 1,   //0-4的选择,
                title: "登录",
                border: [0],
                shadeClose: true,
                area: ['auto', '360px'],
                content: $('#loginDialog')
            });
        } else {
            if (isBind) {
                doMethod();
            } else {
                bindUrl = window.location.href;
                var pagei = layer.open({
                    type: 1,   //0-4的选择,
                    title: "绑定账号",
                    border: [0],
                    shadeClose: true,
                    area: ['auto', '360px'],
                    content: $("#accountDialog")
                });
            }
        }
    };

    this.login = function () {
        var mobile = $('#mobileLogin').val();
        var password = $('#passwordLogin').val();
        var captchaLogin = $('#captchaLogin').val();
        $.ajax({
            type: 'post',
            url: ctx + '/auth/login',
            data: {username: mobile, password: password, captcha: captchaLogin},
            success: function (s) {
                window.location.reload();
            }, error: function (req) {
                var err = JSON.parse(req.responseText);
                $('#loginMgs').addClass('form-group error');
                $('#loginMgs_tips').html('<i class="fa fa-times"></i>' + err.message);
            }
        })
    }




}
var OBJ_loginBox = new loginBox();