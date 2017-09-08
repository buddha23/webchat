function bindAccount() {

    this.sendMsg = function (obj_name) {
        if (!OBJ_formcheck.checkFormat(obj_name, 'phone', '手机号码格式不正确'))
            return false;
        var phone = $('#' + obj_name).val();
        $.ajax({
            url: ctx + '/auth/smsCode',
            data: {'mobile': phone, 'type': 4},
            success: function (data) {
                $('#bind_msg_end,#bind_sendmsg_time_text').show();
                $('#bind_sendmsg_button').hide();
                send_interval = setInterval("OBJ_bindAccount.changeTimes($('#bind_sendmsg_time_text'),'this.overCallback')", 1000);
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
        if (!OBJ_formcheck.checkFormat('bindAccountMgs', 'phone', '手机号码格式不对'))
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
        $('#bind_msg_end,#bind_sendmsg_time').hide();
        $('#bind_sendmsg_button').show();
    };
    this.checkPassword = function () {
        if ($('#password').val() != $('#repassword').val()) {
            $('#repassword_tips').html("两次的密码不一致").addClass('color-red');
            return false;
        }
        else {
            return true;
        }
    };
    this.bind = function () {
        var type = $('#type').val();
        var openid = $('#openid').val();
        var password = $('#password').val();
        var mobile = $('#mobileBind').val();
        var code = $('#code').val();
        if (!OBJ_formcheck.checkFormatBind('bindMgs','phone','手机号码格式不对')) {
            return false;
        }
        $.ajax({
            url: ctx + '/user/bindAccount',
            type: 'post',
            data: {
                mobile: mobile,
                bindType: type,
                password: password,
                smsType: 4,
                code: code,
                openid: openid
            },
            success: function (data) {
                console.log(data);
                $('#bindMgs').addClass('form-group success');
                $('#bindMgs_tips').html('<i class="fa fa-check"></i>' + data);
                setTimeout('window.location.href= bindUrl', 1500);
            },
            error: function (req) {
                if (req.responseText) {
                    var err = JSON.parse(req.responseText);
                    $('#bindMgs').addClass('form-group error');
                    $('#bindMgs_tips').html('<i class="fa fa-times"></i>' + err.message);
                } else {
                    console.log("失败。")
                }
            }
        });
    };
    this.checkBind = function (dom) {
        bindUrl = $(dom).attr('href');
        if (isBind) {
            return true;
        } else {
            var pagei = layer.open({
                type: 1,   //0-4的选择,
                title: "绑定账号",
                border: [0],
                shadeClose: true,
                area: ['auto', '360px'],
                content: $("#accountDialog")
            });
            return false;
        }
    }
}

var OBJ_bindAccount = new bindAccount();
var send_interval;