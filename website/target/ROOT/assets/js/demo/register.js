function register()
{

	this.sendMsg = function(obj_name)
	{
		if(!OBJ_formcheck.checkFormat(obj_name,'phone','手机号码格式不正确'))
			return false;
		var phone = $('#'+obj_name).val();
		$.getJSON('json/register.json',{'phone': phone},function(data){
			if(data.status==1)
			{
				$('#msg_end,#sendmsg_time').show();
				$('#sendmsg_button').hide();
				send_interval = setInterval("OBJ_register.changeTimes($('#sendmsg_time_text'),'this.overCallback')",1000);
			}
			else
			{
				$('#'+obj_name+'_tips').html(data.data.msg).removeClass('right').addClass('error');
			}
		});	
		
	}
	this.password_find_send_msg = function(obj_name)
	{
		var phone = $('#'+obj_name).val();	
		$.getJSON('json/register.json',{'phone': phone},function(data){
			if(data.status==1)
			{
				$('#msg_end,#sendmsg_time').show();
				$('#sendmsg_button').hide();
				send_interval = setInterval("OBJ_register.changeTimes($('#sendmsg_time_text'),'this.overCallback')",1000);
				$('#sign').val(data.data.sign);
				$('#'+obj_name+'_tips').html('').addClass('right');
			}
			else
			{
				$('#'+obj_name+'_tips').html(data.data.msg).removeClass('right').addClass('warning');
			}
		});	
	}
	this.checkSms = function()
	{
		//username phone_code
		var phone = $('#username').val();
		var phone_code = $('#phone_code').val();
		if(!OBJ_formcheck.checkFormat('username','phone','手机号码格式不正确'))
			return false;
		$.getJSON('json/register.json',{'phone':phone,'phone_code':phone_code},function(data){
			if(data.status==1)
			{
				$('#phone_code_tips').html('').removeClass('error').addClass('right');
			}
			else
			{
				$('#phone_code_tips').html(data.data.msg).removeClass('right').addClass('error');
			}
		});
	}
	
	this.changeTimes = function(obj,fun)
	{
		var times = parseInt(obj.html());
		if(times>0)
		{
			obj.html(--times);
		}
		else
		{
			eval(fun+"()");
			obj.html(60);
			clearInterval(send_interval);
		}
	}
	this.overCallback = function()
	{
		$('#msg_end,#sendmsg_time').hide();
		$('#sendmsg_button').show();
	}
	this.checkPassword = function()
	{
		if($('#password').val()!=$('#repassword').val())
		{
			$('#repassword_tips').html("两次的密码不一致").removeClass("right").addClass("error");
			return false;
		}
		else
		{
			$('#repassword_tips').html("").removeClass("error").addClass("right");
			return true;
		}
	}
	this.submitForm = function()
	{
		
		if(!(OBJ_formcheck.checkFormat('username','phone','手机号码格式不对')&&OBJ_formcheck.checkFormat('phone_code','other','没有填写手机验证码')&&OBJ_formcheck.checkFormat('password','password','密码为空或不符合规则'))){
			return false;
		}
		else if($('#password').val()!=$('#repassword').val())
		{
			$('#repassword_tips').html("两次的密码不一致").removeClass("right").addClass("error");
			return false;
		}
		return true;
	}
	
	this.reg = function(url)
	{
		if(!this.submitForm()){
			return false;
		}else{
			$('#register-form').submit();
		}
	}
}

var OBJ_register = new register();
var send_interval;