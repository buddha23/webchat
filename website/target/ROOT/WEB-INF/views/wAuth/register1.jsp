<jsp:include page="../header.jsp"/>
<script>
    $("#accountDialog").remove();
</script>
<div class="content bg-white">
    <div class="container">
        <div class="register">
            <ul class="stepbar clearfix">
                <a>
                    <li class="current">
                        <div class="step-num">1</div>
                        <div class="step-txt">账户注册</div>
                    </li>
                </a>
                <a>
                    <li>
                        <div class="step-num">2</div>
                        <div class="step-txt">完善资料</div>
                    </li>
                </a>
                <a>
                    <li>
                        <div class="step-num">3</div>
                        <div class="step-txt">注册成功</div>
                    </li>
                </a>
            </ul>
            <div class="register-content clearfix">
                <div class="bulletin">
                    <p>最全的数控文库，最新的数控软件</p>
                    <p>最实用的数控课堂，最大的交流社区</p>
                    <br/>
                    <p>就等您来！</p>
                    <p>1、	免费加入会员</p>
                    <p>2、	注册就送50牛人币</p>
                    <br/>
                    <p>受邀注册更享受：</p>
                    <p>1、	直接拥有邀请资格，可以邀请好友加入，获得平台奖励；</p>
                    <p>2、	直接升级为VIP会员。</p>
                </div>

                <form action="${ctx}/auth/register" id="register-form" class="form register-form" method="post">
                    <div class="row">
                            <label for=""><span class="color-red">*</span>验证码：</label>
                                <div class="field">
                                    <input id="captcha_text"  type="text" name="captcha" class="ui-input-default" type="text" maxlength="6" style="width: 50%; display: inline-block;" placeholder="看不清？请点击图片"/>
                                     <img id="pic_captcha" src="${ctx}/assets/captcha.jpg" alt="图片验证码" style="width: 45%; vertical-align: top;">
                                </div>
                                  <div class="tips-txt" id="pic_captcha_tips">请输入图片验证码</div>
                    </div>
                    <div class="row">
                        <label for="username"><span class="color-red">*</span> 手机号码：</label>
                        <div class="field">
                            <input id="username" name="mobile" class="ui-input-default phonenumber" type="text"
                                   onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="11"
                                   onchange="OBJ_formcheck.checkFormat('username','phone','请输入正确的手机号 ');"/>
                        </div>
                        <div class="button-tiny" id="sendmsg_button">获取验证码
                        </div>
                        <div class="button-tiny" id="msg_end" style="display:none;">
                            <span id="sendmsg_time_text">60</span>秒后重发
                        </div>
                        <div class="tips-txt" id="username_tips">请填写您真实的手机号码，作为您的登录账号</div>
                    </div>
                    <div class="row">
                        <label for="captcha"><span class="color-red">*</span> 验证码：</label>
                        <div class="field">
                            <input id="captcha" name="validCode" class="ui-input-default" type="text" maxlength="6"
                                   onkeyup="value=value.replace(/[^0-9_]/g,'')" onchange="OBJ_register.checkSms();"/>
                        </div>
                        <div class="tips-txt" id="captcha_tips">请输入手机收到的验证码</div>
                    </div>
                    <div class="row">
                        <label for="password"><span class="color-red">*</span> 密码：</label>
                        <div class="field">
                            <input id="password" name="password" class="ui-input-default" type="password" maxlength="20"
                                   onchange="OBJ_formcheck.checkFormat('password','password','密码为空或不符合规则');"/>
                        </div>
                        <div class="tips-txt" id="password_tips">6-20个字符，推荐使用英文字母加数字的组合密码</div>
                        <!--
                        <div class="validate-tips right">正确</div>
                        <div class="validate-tips error">密码不符合规则</div>
                        -->
                    </div>
                    <div class="row">
                        <label for="repassword"><span class="color-red">*</span> 确认密码：</label>
                        <div class="field">
                            <input id="repassword" class="ui-input-default" type="password"
                                   onchange="OBJ_register.checkPassword();"/>
                        </div>
                        <div class="tips-txt" id="repassword_tips">请再次输入密码</div>
                    </div>
                    <div class="row">
                        <label></label>
                        <div class="field"><label><input type="checkbox" name="agreeService"> 我已阅读并同意<a
                                class="useragreement-trigger" href="${ctx}/auth/agreement" target="_blank">《网站服务协议》</a></label>
                        </div>
                    </div>
                    <div class="row">
                        <label></label>
                        <div class="field">
                            <button class="button btn-disable btn-large" disabled="disabled" type="button" id="submit_noagree"><i class="icon-arrow-right"></i> 立即注册</button>
                            <button class="button btn-green btn-large" type="button" onclick="OBJ_register.reg();" id="submit_agree" style="display:none;"><i class="icon-arrow-right"></i> 立即注册</button>
                        </div>
                    </div>
                    <div class="row useragreement-wrapper">
                        <label></label>
                        <div class="field">
                            <pre class="useragreement">

                            </pre>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript" src="${assets}/js/register.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<%@include file="../footer.jsp" %>

<script>
    $(function () {
    	$("#sendmsg_button").click(function(){
    		  if (!OBJ_formcheck.checkFormat('username', 'phone', '请输入正确的手机号'))
  	            return false;
  	        var phone = $('#username').val();
  	        var picCaptcha = $('#captcha_text').val();
  	        $.ajax({
  	            url: ctx + '/auth/smsCodeForRegist',
  	            data: {'mobile': phone,'picCaptcha':picCaptcha},
  	            success: function (data) {
  	                $('#msg_end,#sendmsg_time').show();
  	                $('#sendmsg_button').hide();
  	                send_interval = setInterval("OBJ_register.changeTimes($('#sendmsg_time_text'),'this.overCallback')", 1000);
  	            },
  	            error: function (req) {
  	                if (req.responseText) {
  	                    var data = JSON.parse(req.responseText);
  	                    $('#pic_captcha_tips').html(data.message).removeClass('right').addClass('error');
  	                } else {
  	                    $('#pic_captcha_tips').html('短信发送失败').removeClass('right').addClass('error');
  	                }
  	            }
  	        });
    	});
    	
    
        $('input[name="agreeService"]').on('ifClicked', function (event) {
            $('#submit_noagree').hide();
            $('#submit_agree').show();
        });

        $('input[name="agreeService"]').on('ifUnchecked', function (event) {
            $('#submit_noagree').show();
            $('#submit_agree').hide();
        });
    })
</script>