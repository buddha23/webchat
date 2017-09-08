<jsp:include page="../header.jsp">
    <jsp:param name="css" value="css/jquery.Jcrop.css,css/validationEngine.jquery.css"/>
</jsp:include>
<div class="content pd20 management">
    <div class="container clearfix">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="enterprise">
             <div class="box clearfix">
                <div class="box-header">
                    <h1>企业信息</h1>
                </div>
                <div class="box-content">
                    <form id="page-form" class="page-form business-form margintop15" action="${ctx}/enterprise/info" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="logoImg" id="logoImg" value="${info.logoImg}"/>
                        <div class="row">
                            <label for="">公司LOGO：</label>
                            <div class="field">
                            	<div class="upload-group">
                                    <div class="pic-upload">
                                        <div class="pic-upload-default">
                                        	<div class="upload-icon">
                                                <div class="icon-wrapper">
                                                    <div class="icon-plus-x"></div>
                                                    <div class="icon-plus-y"></div>
                                                </div>
                                            </div>
                                            <em>点击上传logo</em>
                                        </div>
                                    </div>
                                    <img alt="" src="">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <label>咨询电话：</label>
                            <div class="field">
                                <input class="ui-input-default vvalidate[required,custom[phone],minSize[11]]" maxlength="11" type="text" name="telephone" placeholder="请输入手机号" value="${info.telephone}"/>
                            </div>
                        </div>
                        <div class="row">
                            <label>企业口号：</label>
                            <div class="field">
                                <input class="ui-input-default validate[required]"  maxlength="28" type="text" name="slogan" placeholder="请输入企业口号，文字不得超过28字" value="${info.slogan}"/>
                            </div>
                        </div>
                        <div class="row">
                            <label>公司主营产品 (服务)：</label>
                            <div class="field">
                                <textarea type="text" name="service"  maxlength="100" class="ui-textarea-default input-full validate[required]" placeholder="请输入主营产品（服务），文字不得超过100字">${info.service}</textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label>公司介绍：</label>
                            <div class="field">
                                <textarea type="text" name="introduce" class="ui-textarea-default input-full validate[required]"  maxlength="200" placeholder="请输入公司介绍，文字不得超过200字">${info.introduce}</textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label>微信二维码：</label>
                            <div class="field fileGroup">
                                <input class="ui-input-default filePath validate[required]"  type="text" placeholder="上传微信二维码图片" readonly/><a class="button btn-normal fileSelect"><i class="fa fa-search"></i></a><input class="fileInput" style="display: none" type="file" name="qrCodeFile">
                                <input type="hidden" name="qrCode" value="${info.qrCode}"/>
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
<div class="crop-dialog clearfix">
	<div class="crop-add">
    </div>
	<div class="crop-box">
        <div class="crop-main">
            <div class="crop-wrapper">
                <img src="images/blank.png" class="img-origin" alt="[Jcrop Example]">
            </div>
        </div>
        <div class="crop-side">
            <div class="crop-preview">
                <img src="images/blank.png" class="img-preview" alt="Preview">
                <input type="file" name="file" class="upload-select" id="upload-select" style="display: none;"/>
            </div>
            <div class="tips"><i class="bek-icon-quote-left"></i> 您可以拖动图片进行裁剪，达到满意效果后保存生成等比logo。 <i class="bek-icon-quote-right"></i></div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.ajaxfileupload.js"></script>
<script>
$(function () {
    $(function () {
        $('#page-form').validationEngine({
            promptPosition: "topLeft", // 有5种模式 topLeft, topRight, bottomLeft, centerRight, bottomRight
            scroll: false,
            showOneMessage: true,
            maxErrorsPerField: true
        });
		
		//微信上传选择栏
		$('.fileGroup .fileSelect').click(function(e){
			var $group = $(this).parent('.fileGroup');
			$group.find('.fileInput').click().change(function(){
				$group.find('.filePath').val(this.files[0].name);
			})
		});
		
		// logo上传
		// Create variables (in this scope) to hold the API and image size
		var jcrop_api,
			boundx,
			boundy,
			coords,
			
			//元素及容器
			$cropEl = $('.crop-dialog'),
			$cropContainer = $cropEl.find('.crop-main'),
			$cropImg = $cropEl.find('.crop-main .crop-wrapper img'),
	
		// Grab some information about the preview pane
			$preview = $cropEl.find('.crop-preview'),
			$pimg = $cropEl.find('.crop-preview img'),
	
			xsize = $preview.width(),
			ysize = $preview.height();
	
		console.log('init',[xsize,ysize]);
		
		function initJcrop(){
			$cropImg.Jcrop({
				onChange: updatePreview,
				onSelect: updatePreview,
				onRelease: cleanCoords,
				boxWidth: $cropContainer.width(),
				boxHeight: $cropContainer.height(),
				//锁定比例 1:1
				aspectRatio: 1/1,
				bgFade: true,
				bgOpacity: .5
			},function(){
				// Use the API to get the real image size
				var bounds = this.getBounds();
				boundx = bounds[0];
				boundy = bounds[1];
				// Store the API in the jcrop_api variable
				jcrop_api = this;
		
				// Move the preview into the jcrop container for css positioning
				// 不绑定到容器，所以注释
				//$preview.appendTo(jcrop_api.ui.holder);
				
				$cropEl.find('.crop-add').hide();
				$cropEl.find('.crop-box').show();
				
				$('.crop-dialog .crop-main .crop-wrapper').css({
					'top': $('.crop-dialog .crop-main').height()/2 - $('.crop-dialog .crop-main .crop-wrapper').height()/2,
					'left': $('.crop-dialog .crop-main').width()/2 - $('.crop-dialog .crop-main .crop-wrapper').width()/2
				});
			});
		}
	
		function updatePreview(c){
			coords = c;
			//console.log('左上坐标是:x' + c.x + " y:" + c.y);
			//console.log('右下坐标是:x' + c.x2 + " y:" + c.y2);
			
			if (parseInt(c.w) > 0) {
				var rx = xsize / c.w;
				var ry = ysize / c.h;
	
				$pimg.css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			}
		}
		
		function cleanCoords(){
			coords = undefined;
		}
		
		//上传选择栏
		$('.upload-group .pic-upload').click(function(e){
			var $el = $(this);
			var id =  layer.open({
				type: 1,
				area: ['auto', 'auto'],
				title: ['logo图片裁剪', 'font-size:16px;'],
				btn: '保存',
				closeBtn: 1,
				shadeClose: true,
				content: $cropEl,
				success: function () {
				},
				yes: function () {
					
					if (coords) {
						$.ajaxFileUpload({
							url: ctx + '/enterprise/uploadLogo',
							type: 'POST',
							dataType: 'json',
							fileElementId: 'upload-select',
							data: {'x': coords.x, 'y': coords.y, 'w': coords.w, 'h': coords.h},
							success: function (result) {
								 if (result) {
	                                    $('.upload-group img').attr('src', ctx + '/files/' + result);
	                                    $('#logoImg').val(data);
	                               }
							},
							error: function (r) {
								 if (r) {
	                                    $('.upload-group img').attr('src', ctx + '/files/' + r.responseText);
	                                    $('#logoImg').val(r.responseText);
	                               }
							}
						});
					} else {
						layer.alert("请先裁剪出图片区域");
					}
					
					layer.close(id);
				},
				end: function () {
					$cropEl.find('.crop-add').show();
					$cropEl.find('.crop-box').hide();
					if (jcrop_api) {
						jcrop_api.destroy();
					}
					cleanCoords();
					
					var file = $('#upload-select');
					file.after(file.clone().val('')); 
					file.remove();
				}
			});
		});
		
		$cropEl.find('.crop-add').click(function(){
			$('.upload-select').click();
		});
		
		$(document).on('change', '#upload-select', function () {
			var $el = $(this);
			if (this.files && this.files[0]) {
				var file = this.files[0];
				//判断下类型如果不是图片就返回 去掉就可以上传任意文件
				if(!/image\/\w+/.test(file.type)){
					layer.msg('请确保文件为图像类型！', {
						shift: 2,
						icon: 0
					});
					return false;
				}
				if(file.size>3145728){//字节数
					layer.msg('图片大小不能超过3M！', {
						shift: 2,
						icon: 0
					});
					return false;
				}
				if(window.FileReader) {  
					console.log('浏览器支持FileReader，采用base64预览图片');
					var reader = new FileReader();
					reader.readAsDataURL(file);
					reader.onload = function(e){
						$cropImg.attr('src', this.result);
						$pimg.attr('src', this.result);
						if(jcrop_api){
							jcrop_api.destroy();
						}
						initJcrop();
					}
				}
				else {  
					console.log('浏览器不支持FileReader');
				}
			}
		});
    })
   
})
</script>
<%@include file="../footer.jsp" %>
<script>
    <c:if test="${errMsg!=null}">
    layer.alert('${fn:escapeXml(errMsg)}', {icon: 2});
    $("#page-form input").attr("readonly", "readonly"); 
    $("#page-form textarea").attr("readonly", "readonly"); 
    </c:if>
</script>