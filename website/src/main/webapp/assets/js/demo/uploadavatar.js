$(function(){
    var jcrop_api,
        boundx,
        boundy,
		coords,
		
		//元素及容器
		$cropEl = $('.crop-dialog'),
		$cropContainer = $cropEl.find('.crop-main'),
		$cropImg = $cropEl.find('.crop-main .crop-wrapper img'),

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
			// 使用api得到图片真实大小
			var bounds = this.getBounds();
			boundx = bounds[0];
			boundy = bounds[1];
			// api变量定义
			jcrop_api = this;
			
			// 不绑定到容器，所以注释
			//$preview.appendTo(jcrop_api.ui.holder);
			
			$cropEl.find('.avatar-select').hide();
			$cropEl.find('.crop-box').show();
			
			$('.crop-dialog .crop-main .crop-wrapper').css({
				'top': $('.crop-dialog .crop-main').height()/2 - $('.crop-dialog .crop-main .crop-wrapper').height()/2,
				'left': $('.crop-dialog .crop-main').width()/2 - $('.crop-dialog .crop-main .crop-wrapper').width()/2
			});
		});
	}

    function updatePreview(c){
		coords = c;
		console.log('左上坐标是:x' + c.x + " y:" + c.y);
		console.log('右下坐标是:x' + c.x2 + " y:" + c.y2);
		
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
    $('.avatar-upload .upload-btn').click(function(e){
		var $el = $(this);
		var id =  layer.open({
			type: 1,
			area: ['auto', 'auto'],
			title: ['头像选择', 'font-size:16px;'],
			closeBtn: 1,
			shadeClose: true,
			content: $cropEl,
			success: function (el, index) {
				el.find('.avatar-select .crop-dialog-btns .btn-yes').unbind('click').click(function(){
					/* 仅演示 */
					layer.close(id);
				});
				
				el.find('.crop-box .crop-dialog-btns .btn-yes').unbind('click').click(function(){
					/* 关闭，仅演示 */
					layer.close(id);
					
					/* 示例：头像裁剪确定按钮事件
					if (coords) {
	                    $.ajaxFileUpload({
	                        url: '${ctx}' + '/user/uploadavatar',
	                        type: 'POST',
	                        fileElementId: 'upload-select',
	                        dataType: 'json',
	                        data: {'x': coords.x, 'y': coords.y, 'w': coords.w, 'h': coords.h},
	                        success: function (data, status) {
	                            if(data){
									$('.avatar-img img').attr('src', '${ctx}' + '/' + data);
									layer.msg('头像更换成功！', {
										shift: 5,
										icon: 1
									});
								}else{
									layer.msg('出现错误，头像更换失败。', {
										shift: 5,
										icon: 2
									});
								}
	                        },
	                        error: function () {
	                            layer.msg('出现错误，头像更换失败。', {
									shift: 5,
									icon: 2
								});
	                        }
	                    });
	                    layer.close(id);
	                } else {
	                	layer.msg('您未裁剪出头像区域。', {
							shift: 5,
							icon: 2
						});
	                }
					*/
				});
			},
			yes: function () {
				
			},
			end: function () {
				$cropEl.find('.avatar-select').show();
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
			//这里判断下类型如果不是图片就返回 去掉就可以上传任意文件
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