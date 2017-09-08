// JavaScript Document
// @Author Belenk
// Lasted 2015-03-09

$(document).ready(function(e) {
	//配置参数
	var interval = 8000;	//翻页间隔
	var current_photo = 1;	//当前图片
	var speed = 750;		//切换速度
	
	var viewpic = $(".mini-slide-container li").outerWidth(true);	//预览图宽
	var photo_num = $(".mini-slide-container li").length;			//图片总数
	if($(".slide-viewport").length > 0){
		var viewport_offset = $(".slide-viewport").position().left;		//预览图滑块y偏移量
	}
	
	if($(".bgchange").length > 0)
		$(".bgchange").eq(0).show().siblings(".bgchange").hide();
	
	$(".slides li").click(function(e){
		current_photo = $(this).index(".slides li") + 1;
		bgp_move();
	})
	.eq(current_photo -1).addClass("current");
	
	$(".banner-prev,.mini-left-arrow").click(function(e){
		bgp_prev();
	});
	$(".banner-next,.mini-right-arrow").click(function(e){
		bgp_next();
	});

	timecontrol = setInterval(bgp_next,interval);
	
	function bgp_next(){
		current_photo += 1;
		
		//当前播放图片大于总数则循环至第一张
		if(current_photo > photo_num){
			current_photo = 1;
		}
		
		bgp_move();
	}
	
	function bgp_prev(){
		current_photo -= 1;
		
		//当前播放图片小于第一张则循环至最后一张
		if(current_photo < 1){
			current_photo = photo_num;
		}
		
		bgp_move();
	}
	
	function bgp_move(){
		clearInterval(timecontrol);
		timecontrol = setInterval(bgp_next,interval);
		
		if($(".slide-viewport").length > 0){
			$(".slide-viewport").animate({
				//预览图滑块滑至点击的预览图位置
				left: (viewpic * (current_photo - 1)) + viewport_offset + "px"
			},"normal")
		}
		
		$(".slides li").eq(current_photo - 1).addClass("current").siblings().removeClass("current");
		
		var bgp_c = ".banner_" + "p" + current_photo;
		$(bgp_c).stop(true, true);
		$(".bgchange").not(bgp_c).stop(true, true);//清除动画队列
		$(bgp_c).fadeIn(speed);
		$(".bgchange").not(bgp_c).fadeOut(speed);
	}
	
});
