// JavaScript Document
// 顶部菜单切换 by @Belenk
// 用rel属性替代锚点，动画滚动
 
;(function($){
	$.fn.extend({
		"aniAnchor" : function(options){
			//配置
			$.fn.aniAnchor.defaults = {
				//动画速度
				speed: 250,
				//定位偏移量
				offsetFix: 0
			};
			
			var settings=$.extend({},$.fn.aniAnchor.defaults,options);
			
			//容器
			var $element = this,
			//动画执行开关
			aniTrigger = true;
			
			
			//初始化
			(function init(){
				$(document).scroll(function(){
					if($(document).scrollTop() > $element.offset().top){
						if(aniTrigger){
							$element.find('a').each(function(index, element) {
								var $ctx = $(this),
								linkClass = $ctx.attr('rel'),
								//针对不同浏览器兼容问题，将范围上限值与下限值以10进制取整
								rangeTop = parseInt($('.' + linkClass).offset().top - settings.offsetFix,10),
								rangeBottom = parseInt($('.' + linkClass).offset().top + $('.' + linkClass).height(),10);
								if($(document).scrollTop() >= rangeTop && $(document).scrollTop() <= rangeBottom){
									//console.log(rangeTop);
									$element.find('a').eq(index).addClass('current');
									$element.find('a').eq(index).siblings().removeClass('current');
								}
							});
						}
					}
				})
				
				$element.find('a').click(function(){
					aniTrigger = false;
					var $ctx = $(this),
					linkClass = $ctx.attr('rel');
					$ctx.addClass('current');
					$ctx.siblings().removeClass('current');
					if(linkClass.length > 0){
						$('html,body').animate({scrollTop: $('.' + linkClass).offset().top - settings.offsetFix}, settings.speed, function(){aniTrigger = true;});
					}else{
						aniTrigger = true;
					}
				})
			})();
		}
	})
})(jQuery);