// JavaScript Document
// by @Belenk
// 为元素附加水波纹点击效果
 
;(function($){
	$.fn.extend({
		"ripple" : function(options){
			//配置
			$.fn.ripple.defaults = {
				//类名
				className: 'ripple'
			};
			
			var settings=$.extend({},$.fn.ripple.defaults,options);
			
			//容器
			var $element = this;
			
			$(".ripple").remove();

			var posX = $element.offset().left,
				posY = $element.offset().top,
				buttonWidth = $element.width(),
				buttonHeight = $element.height();
			$element.append("<span class='" + settings.className +"'></span>");
		
			if (buttonWidth >= buttonHeight) {
				buttonHeight = buttonWidth;
			} else {
				buttonWidth = buttonHeight;
			}
		
			var x = event.pageX - posX - buttonWidth / 2;
			var y = event.pageY - posY - buttonHeight / 2;
		
			$("." + settings.className).css({
				width: buttonWidth,
				height: buttonHeight,
				top: y + 'px',
				left: x + 'px'
			}).addClass("rippleEffect");
		}
	})
})(jQuery);