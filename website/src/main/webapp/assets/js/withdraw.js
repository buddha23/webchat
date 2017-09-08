function withdraw() {

    this.sendMsg = function (obj_name) {
        var phone = $('#' + obj_name).val();
        $.ajax({
            url: ctx + '/auth/smsCode',
            data: {'mobile': phone, 'type': 5},
            success: function (data) {
                $('#msg_end,#sendmsg_time').show();
                $('#sendmsg_button').hide();
                send_interval = setInterval("OBJ_withdraw.changeTimes($('#sendmsg_time'),'this.overCallback')", 1000);
            },
            error: function (req) {
                if (req.responseText) {
                    var data = JSON.parse(req.responseText);
                    $('#' + obj_name + '_tips').html(data.message).addClass('color-red');
                } else {
                    $('#' + obj_name + '_tips').html('短信发送失败').addClass('color-red');
                }
            }
        });

    };

    this.checkSms = function () {
        //username phone_code
        var phone = $('#username').val();
        var validCode = $('#captcha').val();
        if (!OBJ_formcheck.checkFormat('username', 'phone', '手机号码格式不对'))
            return false;
        if(!OBJ_formcheck.checkFormatW('captcha','username','captcha','请输入有效的6位验证码'))
            return false;
        $.ajax({
            url: ctx + '/auth/smsCode',
            data: {'mobile': phone, 'validCode': validCode, 'type': 3},
            success: function (data) {
                window.location.href = ctx + "/auth/resetPwd?mobile=" + phone;
            },
            error: function (r) {
                var data = JSON.parse(r.responseText);
                $('#username_tips').html(data.message).addClass('color-red');
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
    this.checkPasswordAll = function () {
        if ((OBJ_formcheck.checkFormatResetPwd('repassword', 'password', '密码为空或不符合规则'))&&(this.checkPassword())) {
            $('#repassword_tips1').hide().html("").removeClass('color-red');
            $('#resetPwdForm').submit();
        } else {
            return false;
        }
    };
    this.checkPassword = function () {
        if ($('#password1').val() != $('#repassword').val()) {
            $('#repassword_tips1').show().html("两次的密码不一致").addClass('color-red');
            return false;
        }else {
            return true;
        }
    };
}

var OBJ_withdraw = new withdraw();
var send_interval;