function Register() {

    this.sendMsg = function (obj_name) {
        if (!OBJ_formcheck.checkFormat(obj_name, 'phone', '请输入正确的手机号'))
            return false;
        var phone = $('#' + obj_name).val();
        $.ajax({
            url: ctx + '/auth/smsCode',
            data: {'mobile': phone, 'type': 1},
            success: function (data) {
                $('#msg_end,#sendmsg_time').show();
                $('#sendmsg_button').hide();
                send_interval = setInterval("OBJ_register.changeTimes($('#sendmsg_time_text'),'this.overCallback')", 1000);
            },
            error: function (req) {
                if (req.responseText) {
                    var data = JSON.parse(req.responseText);
                    $('#' + obj_name + '_tips').html(data.message).removeClass('right').addClass('error');
                } else {
                    $('#' + obj_name + '_tips').html('短信发送失败').removeClass('right').addClass('error');
                }
            }
        });

    };
    this.password_find_send_msg = function (obj_name) {
        var phone = $('#' + obj_name).val();
        $.getJSON('json/register.json', {'phone': phone}, function (data) {
            if (data.status == 1) {
                $('#msg_end,#sendmsg_time').show();
                $('#sendmsg_button').hide();
                send_interval = setInterval("OBJ_register.changeTimes($('#sendmsg_time_text'),'this.overCallback')", 1000);
                $('#sign').val(data.data.sign);
                $('#' + obj_name + '_tips').html('').addClass('right');
            }
            else {
                $('#' + obj_name + '_tips').html(data.data.msg).removeClass('right').addClass('warning');
            }
        });
    };
    this.checkSms = function () {
        //username phone_code
        var phone = $('#username').val();
        var validCode = $('#captcha').val();
        if (!OBJ_formcheck.checkFormat('username', 'phone', '请输入正确的手机号'))
            return false;
        if (validCode == '') {
            $('#captcha_tips').html('没有填写手机验证码').removeClass('right').addClass('error');
            return false;
        }
        $.ajax({
            url: ctx + '/auth/smsCode',
            data: {'mobile': phone, 'validCode': validCode, 'type': 1},
            success: function (data) {
                $('#captcha_tips').html('').removeClass('error').addClass('right');
            },
            error: function (r) {
                var data = JSON.parse(r.responseText);
                $('#captcha_tips').html(data.message).removeClass('right').addClass('error');
            }
        });
    };

    this.changeTimes = function (obj, fun) {
        var times = parseInt(obj.html());
        if (times > 0) {
            obj.html(--times);
        }
        else {
            eval(fun + "()");
            obj.html(60);
            clearInterval(send_interval);
        }
    };
    this.overCallback = function () {
        $('#msg_end,#sendmsg_time').hide();
        $('#sendmsg_button').show();
    };
    this.checkPassword = function () {
        if ($('#password').val() != $('#repassword').val()) {
            $('#repassword_tips').html("两次的密码不一致").removeClass("right").addClass("error");
            return false;
        }
        else {
            $('#repassword_tips').html("").removeClass("error").addClass("right");
            return true;
        }
    };
    this.submitForm = function () {
        if (!(OBJ_formcheck.checkFormat('username', 'phone', '请输入正确的手机号') && OBJ_formcheck.checkFormat('captcha', 'other', '没有填写手机验证码') && OBJ_formcheck.checkFormat('password', 'password', '密码为空或不符合规则'))) {
            return false;
        }
        else if ($('#password').val() != $('#repassword').val()) {
            $('#repassword_tips').html("两次的密码不一致").removeClass("right").addClass("error");
            return false;
        }
        return true;
    };

    this.reg = function (url) {
        if (!this.submitForm()) {
            return false;
        } else {
            $('#register-form').ajaxSubmit({
                success: function (user) {
                    location.href = ctx + '/auth/register2';
                },
                error: function (r) {
                    var o = JSON.parse(r.responseText);
                    if (o && o.message) {
                        layer.alert(o.message);
                    } else {
                        layer.alert('注册失败！');
                    }
                }
            });
        }
    }
}

var OBJ_register = new Register();
var send_interval;