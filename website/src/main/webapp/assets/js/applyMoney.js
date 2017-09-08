function Apply_money(){

this.send_sms = function(){
    var phone = $('#mobile').val();
    $.ajax({
        url: ctx + '/auth/smsCode',
        data: {'mobile': phone, 'type': 5},
        success: function (data) {
            $('#msg_end,#sendmsg_time').show();
            $('#sendmsg_button').hide();
            send_interval = setInterval("apply_money.changeTimes($('#sendmsg_time_text'),'this.overCallback')", 1000);
        },
        error: function (req) {
            if (req.responseText) {
                var data = JSON.parse(req.responseText);
                layer.alert("data");
        }}
    });
}

     this.changeTimes=function (obj, fun) {
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

}
var apply_money = new Apply_money();

var send_interval;
