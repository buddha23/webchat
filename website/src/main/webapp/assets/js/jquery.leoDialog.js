// JavaScript Document
// Author Belenk
// Lasted 2017/3/9

!function(w) {
	var idx = 0;
	
	self.leoDialog = {
		version: '1.0',
		
		window: function(options){
			var obj = new ld(options);
			obj.window();
			return idx;
		},
		
		alert: function(options){
			var obj = new ld(options);
			obj.alert();
			return idx;
		},
		
		close: function(index){
			$('.leo-dialog').after($('.leo-dialog .dialog-content').children().hide());
			$('.leo-dialog.dialog-index-' + index).remove();
			$('.dialog-mask').remove();
		},
		
		closeAll: function(){
			$('.leo-dialog').after($('.leo-dialog .dialog-content').children().hide());
			$('.leo-dialog').remove();
			$('.dialog-mask').remove();
		}
	};
	
	var ld = function(options){
		this.ctx = this;
		
		var cfg = {
			default: {
				title : '提示',
				modal: true,
				isWindow: true,
				content: ''
			}
		};
		
		this.settings = $.extend({},cfg.default,options);
	};
	
	ld.prototype.initDialog = function(){
		var ctx = this;
		var $dialog = $('.leo-dialog');
		
		if($dialog.length > 0){
			leoDialog.closeAll();
		};
		
		var html = 
			'<div class="leo-dialog">' +
				'<div class="dialog-box">' +
					'<div class="dialog-content">' + 
					'</div>' +
				'</div>' +
			'</div>';
		
		idx++;
		
		if(ctx.settings.isWindow == true){
			if(typeof ctx.settings.content == 'object'){
				ctx.settings.content.wrap(html);
				$('.leo-dialog .dialog-box').prepend('<div class="dialog-header">' + this.settings.title + '<i class="fa fa-close close"></i></div>');
				$('.leo-dialog').after('<div class="dialog-mask"></div>');
				ctx.settings.content.css('display','block');
			}
			else if(typeof ctx.settings.content == 'string'){
				$('body').append(html);
				$('.leo-dialog .dialog-content').append(this.settings.content);
				$('.leo-dialog .dialog-box').prepend('<div class="dialog-header">' + this.settings.title + '<i class="fa fa-close close"></i></div>');
				$('.leo-dialog').after('<div class="dialog-mask"></div>');
			};
		}else{
			html = 
			'<div class="leo-dialog">' +
				'<div class="dialog-content">' + 
				'</div>' +
			'</div>';
			
			if(typeof ctx.settings.content == 'object'){
				ctx.settings.content.wrap(html);
				$('.leo-dialog').after('<div class="dialog-mask"></div>');
				ctx.settings.content.css('display','block');
			}
			else if(typeof ctx.settings.content == 'string'){
				$('body').append(html);
				$('.leo-dialog .dialog-content').append(this.settings.content)
				$('.leo-dialog').after('<div class="dialog-mask"></div>');
			};
		}
	};
	
	ld.prototype.open = function(){
		var ctx = this;
		var $dialog = $('.leo-dialog');
		var $mask = $('.dialog-mask');
		
		$dialog.addClass('dialog-index-' + idx);
		
		if(ctx.settings.isWindow == true){
			var screenHeight = $(window).height();
			$dialog.css('top', screenHeight/2 - $dialog.height()/2).show();
		}else{
			$dialog.show();
		}
		$mask.show();
		
		$(document).on('click', function(){
			leoDialog.close(idx);
		});
		
		$dialog.children('.dialog-box').click(function(event){
			event.stopPropagation();
		});
		
		$dialog.find('.close').click(function(){
			leoDialog.close(idx);
		});
	};
	
	ld.prototype.window = function(){
		var ctx = this;
		ctx.initDialog();
		
		if(typeof ctx.settings.buttons != "undefined"){
			$('.leo-dialog .dialog-box').append(
				'<div class="dialog-btns-wrapper">' +
					'<div class="dialog-btns clearfix">' +
						'<div class="dialog-btns-col">' +
							'<button type="submit" class="dialog-btn-confirm">' + ctx.settings.buttons.confirm.text + '</button>' +
						'</div>' +
						'<div class="dialog-btns-col">' +
							'<button type="button" class="dialog-btn-cancel">' + ctx.settings.buttons.cancel.text + '</button>' +
						'</div>' +
					'</div>' +
				'</div>'
			);
		};
		
		$('.dialog-btn-confirm').click(function(){
			if(typeof ctx.settings.buttons != "undefined"){
				if(typeof ctx.settings.buttons.confirm.action === 'function'){
					ctx.settings.buttons.confirm.action();
				}
			}
		});
		
		$('.dialog-btn-cancel').click(function(){
			if(typeof ctx.settings.buttons.cancel.action === "function"){
				ctx.settings.buttons.cancel.action();
			}
			else{
				leoDialog.close(idx);
			}
		});
		
		ctx.open();
	};
	
	ld.prototype.alert = function(){
		var ctx = this;
		ctx.initDialog();
		
		$('.leo-dialog .dialog-box').append(
			'<div class="dialog-btns-wrapper">' +
				'<div class="dialog-btns clearfix">' +
					'<div class="dialog-btns-full">' +
						'<button type="submit" class="dialog-btn-confirm">' + ctx.settings.buttons.confirm.text + '</button>' +
					'</div>' +
				'</div>' +
			'</div>'
		);
		
		$('.dialog-btn-confirm').click(function(){
			if(typeof ctx.settings.buttons != "undefined"){
				if(typeof ctx.settings.buttons.confirm.action === 'function'){
					ctx.settings.buttons.confirm.action();
				}
			}
		});
		
		ctx.open();
	}
}(window);