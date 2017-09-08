<jsp:include page="../header.jsp">
    <jsp:param name="css" value="css/jquery.Jcrop.css,css/validationEngine.jquery.css"/>
</jsp:include>
<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="enterprise">
            <div class="box clearfix">
             <div class="box-header">
                    <h1>企业入驻信息</h1>
                </div>
                <div class="box-content">
                    <form id="page-form" class="page-form business-form margintop15"  action="${ctx}/enterprise/settled" method="post">
                        <div class="row">
                            <label for=""><span class="color-red">*</span> 公司名称：</label>
                            <div class="field">
                                <input class="ui-input-default validate[required,minSize[2]] pass_info"  maxlength="24"  name="name" type="text" value="${certification.name}"/>
                            </div>
                        </div>
                        <div class="row">
                            <label><span class="color-red">*</span> 公司所在地：</label>
                            <div class="field">
                                <input id="password" type="text" name="address" class="ui-input-default validate[required] pass_info" value="${certification.address}"/>
                            </div>
                        </div>
                        <div class="row">
                            <label><span class="color-red">*</span> 公司营业执照代码：</label>
                            <div class="field">
                                <input class="ui-input-default validate[required,custom[onlyLetterNumber],minSize[2]] pass_info" maxlength="24" name="registrationNo" type="text" value="${certification.registrationNo}"/>
                            </div>
                        </div>
                        <div class="row">
                            <label><span class="color-red">*</span> 联系人姓名：</label>
                            <div class="field">
                                <input class="ui-input-default validate[required,,minSize[2]]" name="linkman" maxlength="7" type="text" value="${certification.linkman}"/>
                            </div>
                        </div>
                        <div class="row">
                            <label><span class="color-red">*</span> 联系人电话：</label>
                            <div class="field">
                                <input class="ui-input-default validate[required,custom[phone],minSize[11]]" maxlength="11" name="mobile" type="text" value="${certification.mobile}"/>
                            </div>
                        </div>
                        
                        <div class="row">
                            <label><span class="color-red">*</span> 验证码：</label>
                            <div class="field">
                                <input class="ui-input-default validate[required,custom[onlyLetterNumber]]" maxlength="6" name="captcha" type="text"/>
                                <img class="captcha" id="captcha" src="${ctx}/assets/captcha.jpg" />
                            </div>
                        </div>
                        <div class="row">
                            <label><span class="color-red">*</span> 企业营业执照图：</label>
                            <div class="field">
                                <div style="display:none">
                                    <input type="file" class="pass_info" id="choose" accept="image/*" multiple >
                                    <input type="text" id="imgPath" name="registrationImg" value="${certification.registrationImg}" />
                                </div>
                                <ul class="img-list clearfix">
                                    <li id="upload">
                                        <div class="upload-icon">
                                        	<div class="icon-wrapper">
                                                <div class="icon-plus-x"></div>
                                                <div class="icon-plus-y"></div>
                                            </div>
                                        </div>
                                        <em>点击添加图片</em>
                                        <p>支持jpg，gif，png格式</p>
                                    </li>
                                    <c:forEach items="${certification.imgPaths}" var="imgPath">
                                    <li>
                                    <img alt="" src="${ctx}/files/${imgPath}" style="width:160px;height:120px"/>
                                    </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <div class="row">
                            <label></label>
                            <div class="field">
                                <button class="button btn-green btn-large" type="submit"><i class="fa fa-check"></i> 确定</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.validationEngine-zh_CN.js"></script>
<script>
$(function () {
    $('#page-form').validationEngine({
        promptPosition: "topLeft", // 有5种模式 topLeft, topRight, bottomLeft, centerRight, bottomRight
        scroll: false,
        showOneMessage: true,
        maxErrorsPerField: true
    });
	
	/** 图片上传
	* Author: 贝大湿
	*/
	var $filechooser = $("#choose");
	
	$("#upload").on("click", function () {
		$filechooser.click();
	});
	  $('#captcha').click(function () {
          $(this).attr('src', '${ctx}/assets/captcha.jpg?t=' + new Date().getTime());
      });
	$filechooser.click(function() {
        if (!this.files.length) return;
        var files = Array.prototype.slice.call(this.files);
        if (files.length > 3) {
            alert("最多同时只可上传3张图片");
            return;
        }
        files.forEach(function (file, i) {
            if (!/\/(?:jpeg|png|gif)/i.test(file.type)) return;
            var reader = new FileReader();
            var li = document.createElement("li");
            li.innerHTML = '<div class="progress"><span></span></div>';
            $(".img-list").append($(li));
            reader.onload = function () {
                var result = this.result;
                var img = new Image();
                img.src = result;
                $(li).css("background-image", "url(" + result + ")");
                upload(result, file.type, $(li));
                return;
            };
            reader.readAsDataURL(file);
        })
    });
	
	//图片上传，将base64的图片转成二进制对象，塞进formdata上传
	function upload(basestr, type, $li) {
		var text = window.atob(basestr.split(",")[1]);
		var buffer = new Uint8Array(text.length);
		var pecent = 0, loop = null;
		for (var i = 0; i < text.length; i++) {
			buffer[i] = text.charCodeAt(i);
		}
		var blob = getBlob([buffer], type);
		var xhr = new XMLHttpRequest();
		var formdata = getFormData();
		formdata.append('file', blob);
		formdata.append('type', 3);
		//上传url
		$.ajax({
			type: "POST",
			url:  ctx + "/enterprise/uploadPic",
			data: formdata,
			cache: false,
			processData: false,
			contentType: false,
			success: function(data){
				console.log(data);
				var jsonData = data;
				var imagedata = jsonData || {};
				var text = '上传成功';
				//将上传成功后的路径保存在input中
				var $pathInput = $("#imgPath");
				if ($pathInput.val() == "") {
					$pathInput.val($pathInput.val() + data);
				} else {
					$pathInput.val($pathInput.val() + "," + data);
				}
				clearInterval(loop);
				//当收到该消息时上传完毕
				$li.find(".progress span").animate({'width': "100%"}, pecent < 95 ? 200 : 0, function () {
					$(this).html(text);
				});
				//$(".pic-list").append('<a href="' + imagedata.path + '">' + imagedata.name + '（' + imagedata.size + '）<img src="' + imagedata.path + '" /></a>');
			},
			xhrFields: {
				onsendstart: function() {
					//this是指向XHR
					//数据发送进度，前50%展示该进度
					this.upload.addEventListener('progress', function (e) {
						if (loop) return;
						pecent = ~~(100 * e.loaded / e.total) / 2;
						$li.find(".progress span").css('width', pecent + "%");
						if (pecent == 50) {
							mockProgress();
						}
					}, false);
					//数据后50%用模拟进度
					function mockProgress() {
						if (loop) return;
						loop = setInterval(function () {
							pecent++;
							$li.find(".progress span").css('width', pecent + "%");
							if (pecent == 99) {
								clearInterval(loop);
							}
						}, 100)
					}
				}
			}
		});
	}
	
	/**
	 * 获取blob对象的兼容性写法
	 * @param buffer
	 * @param format
	 * @returns {*}
	 */
	function getBlob(buffer, format) {
		try {
			return new Blob(buffer, {type: format});
		} catch (e) {
			var bb = new (window.BlobBuilder || window.WebKitBlobBuilder || window.MSBlobBuilder);
			buffer.forEach(function (buf) {
				bb.append(buf);
			});
			return bb.getBlob(format);
		}
	}

	/**
	 * 获取formdata
	 */
	function getFormData() {
		var isNeedShim = ~navigator.userAgent.indexOf('Android')
				&& ~navigator.vendor.indexOf('Google')
				&& !~navigator.userAgent.indexOf('Chrome')
				&& navigator.userAgent.match(/AppleWebKit\/(\d+)/).pop() <= 534;
		return isNeedShim ? new FormDataShim() : new FormData()
	}

	/**
	 * formdata 补丁, 给不支持formdata上传blob的android机打补丁
	 * @constructor
	 */
	function FormDataShim() {
		console.warn('using formdata shim');
		var o = this,
				parts = [],
				boundary = Array(21).join('-') + (+new Date() * (1e16 * Math.random())).toString(36),
				oldSend = XMLHttpRequest.prototype.send;
		this.append = function (name, value, filename) {
			parts.push('--' + boundary + '\r\nContent-Disposition: form-data; name="' + name + '"');
			if (value instanceof Blob) {
				parts.push('; filename="' + (filename || 'blob') + '"\r\nContent-Type: ' + value.type + '\r\n\r\n');
				parts.push(value);
			}
			else {
				parts.push('\r\n\r\n' + value);
			}
			parts.push('\r\n');
		};
		// Override XHR send()
		XMLHttpRequest.prototype.send = function (val) {
			var fr,
					data,
					oXHR = this;
			if (val === o) {
				// Append the final boundary string
				parts.push('--' + boundary + '--\r\n');
				// Create the blob
				data = getBlob(parts);
				// Set up and read the blob into an array to be sent
				fr = new FileReader();
				fr.onload = function () {
					oldSend.call(oXHR, fr.result);
				};
				fr.onerror = function (err) {
					throw err;
				};
				fr.readAsArrayBuffer(data);
				// Set the multipart content type and boudary
				this.setRequestHeader('Content-Type', 'multipart/form-data; boundary=' + boundary);
				XMLHttpRequest.prototype.send = oldSend;
			}
			else {
				oldSend.call(this, val);
			}
		};
	}
})
</script>
<%@include file="../footer.jsp" %>
<script>
    <c:if test="${errMsg!=null}">
    layer.alert('${fn:escapeXml(errMsg)}', {icon: 2, time: 1500});
    </c:if>

    <c:if test="${certification.state ==2}">
    $(".pass_info").attr("readonly", "readonly"); 
    </c:if>
</script>