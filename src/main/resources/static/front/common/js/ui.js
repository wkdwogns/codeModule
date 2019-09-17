UI = {
	load: function(){
		$(document).ready(function(){
			UI.fn_screen();
			UI.fn_gnb();
			UI.fn_about();
			UI.fn_business();
		});//ready

		$(window).load(function(){

		});//load

		$(window).resize(function(){
			UI.fn_screen();
		});
	},

	SV : {
		screenFlag :1
	},

	fn_screen : function (){
		var screen_width = $(window).width();
		if (screen_width <= 750){
			UI.SV.screenFlag = 1;//MO
			console.log("모바일");
		}else {
			UI.SV.screenFlag = 2;//PC
			console.log("피시");
		}
	},

	fn_gnb : function(){
		var header = $('#header'),
			btn_gnb = header.find('.btn_menu'),
			gnb_close = header.find('.btn_close');

		btn_gnb.on('click',function(){
			if (UI.SV.screenFlag == 2 ){return;}
			header.addClass('open');
			$('html').addClass('fixed');
		});
		gnb_close.on('click',function(){
			if (UI.SV.screenFlag == 2 ){return;}
			header.removeClass('open');
			$('html').removeClass('fixed');
		});

	},

	fn_about : function (){
		if($('.sub_about').length == 0){return;}
		var navi = $('.navi'),
			btn_move_01 = navi.find('ul li:nth-child(1)'),
			btn_move_02 = navi.find('ul li:nth-child(2)'),
			btn_move_03 = navi.find('ul li:nth-child(3)');

		btn_move_01.on('click',function(){
			$('html,body').stop().animate({scrollTop: ($('.section_01').offset().top) },500, 'easeInOutExpo');
		});
		btn_move_02.on('click',function(){
			$('html,body').stop().animate({scrollTop: ($('.section_02').offset().top - 100) },500, 'easeInOutExpo');
		});
		btn_move_03.on('click',function(){
			$('html,body').stop().animate({scrollTop: ($('.section_03').offset().top - 100) },500, 'easeInOutExpo');
		});

		var navi_movement = function (){
			sTop = $(window).scrollTop();
			if (UI.SV.screenFlag == 1 ){
				if (sTop >= 51){
					navi.stop().animate({'top': (sTop) - 31},0,'easeOutExpo');
				}else{
					navi.stop().animate({'top': 20 },0,'easeOutExpo');
				}
			}
			if (UI.SV.screenFlag == 2 ){
				if (sTop >= 80){
					navi.stop().animate({'top': (sTop) },0,'easeOutExpo');
				}else{
					navi.stop().animate({'top': 80},0,'easeOutExpo');
				}
			}
			if ( sTop >= $('.section_01').offset().top -100 && sTop < $('.section_02').offset().top -100){
				btn_move_01.addClass('on');
			}else{
				btn_move_01.removeClass('on');
			}
			if ( sTop >= $('.section_02').offset().top -100 && sTop < $('.section_03').offset().top -100){
				btn_move_02.addClass('on');
			}else{
				btn_move_02.removeClass('on');
			}
			if ( sTop >= $('.section_03').offset().top -100){
				btn_move_03.addClass('on');
			}else{
				btn_move_03.removeClass('on');
			}
		};

		$(window).scroll(function(){
			navi_movement();
		});

	},

	fn_business : function (){
		if($('.sub_business').length == 0){return;}
		var	btn_select = $('.btn_select');
		var	select = $('.select');

		btn_select.on('click',function(){
			if (UI.SV.screenFlag == 2 ){return;}
			select.addClass('open');
		});

		select.on('click',function(){
			if (UI.SV.screenFlag == 2 ){return;}
			select.removeClass('open');
		});

	}


}

//레이어 오픈 
var layer_OPEN = function (obj_selector){
	var obj = $(obj_selector);
	$('html').addClass('fixed');
	obj.css({'display':'block','opacity':0});
	obj.stop().animate({'opacity':1},500);
};

//레이어 클로즈
var layer_CLOSE = function (obj_selector){
	var obj = $(obj_selector);
	obj.stop().animate({'opacity':0},500,function (){
		$(this).css({'display':'none'});
		$('html').removeClass('fixed');
	});
};


UI.load();
