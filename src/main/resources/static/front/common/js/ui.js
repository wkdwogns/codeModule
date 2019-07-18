UI = {
	load: function(){
		$(document).ready(function(){
			UI.fn_main();
			UI.fn_gnb();
			UI.fn_screen();
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
			//console.log("모바일");
		}else {
			UI.SV.screenFlag = 2;//PC
			//console.log("피시");
		}
	},

	fn_gnb : function(){
		var header = $('#header'),
			gnb = $('#gnb > ul'),
			depth1 = gnb.children();
			btn_gnb = header.find('.btn_menu');
			gnb_close = header.find('.btn_close');
			//서브페이지 고정
		var selected_flag = null;

		depth1.each(function(i){
			if (UI.SV.screenFlag == 1 ){return;}
			if ($(this).hasClass('current')){
				selected_flag = i;
				header.addClass('open');
			}
		});
		depth1.on('mouseover focusin', function(){		
			if (UI.SV.screenFlag == 1 ){return;}
			depth1.removeClass('current');
			depth1.removeClass('on');
			$(this).addClass('on');
			if ($(this).find('ul').length == 1){
				header.addClass('open');
			}else{
				header.removeClass('open');
			}
		});
		header.on('mouseleave focusout', function(){
			if (UI.SV.screenFlag == 1 ){return;}
			if (selected_flag == null){
				header.removeClass('open');
				depth1.removeClass('on');	
			}else {
				header.addClass('open');
				depth1.removeClass('on');
				depth1.eq(selected_flag).addClass('current');
			}				
		});
		depth1.on('click', function(){		
			if (UI.SV.screenFlag == 2 ){return;}
			depth1.removeClass('current');
			
			if($(this).hasClass('on'))
			{
				$(this).removeClass('on');
			}else {
				depth1.removeClass('on');
				$(this).addClass('on');
			}
			
		});
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

	fn_main : function (){
		if($('#main_contents').length == 0){return;}

		//메인 슬라이더
		$(".section_01 .slider").slick({
			fade: true,
			dots: false,
			arrows: false,
			draggable: false,
			asNavFor: '.section_01 .slider_thum',
			responsive: [
				{
				  breakpoint: 750,
				  settings: {
					draggable: true,
					dots: true,
				  }
				}
			]
		});

		$(".section_01 .slider_thum").slick({
			slidesToShow: 4,
			slidesToScroll: 4,
			asNavFor: '.section_01 .slider',
			dots: false,
			arrows: false,
			focusOnSelect: true,
			infinite: false,
			responsive: [
				{
				  breakpoint: 750,
				  settings: {
					draggable: true,
					slidesToShow: 2,
					slidesToScroll: 1,

				  }
				}
			]
		});



	}



}

//레이어 오픈 
var layer_OPEN = function (obj_selector){
	var obj = $(obj_selector);
	obj.css({'display':'block','opacity':0});
	obj.stop().animate({'opacity':1},500);
};

//레이어 클로즈
var layer_CLOSE = function (obj_selector){
	var obj = $(obj_selector);	
	obj.stop().animate({'opacity':0},500,function (){
		$(this).css({'display':'none'});	
	});
};


UI.load();
