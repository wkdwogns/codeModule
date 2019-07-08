UI = {
	load: function(){
		$(document).ready(function(){
			UI.fn_gnb();
			UI.fn_main();
			UI.fn_top();
			UI.fn_request();
			UI.fn_count();
		});//ready


		$(window).load(function(){

		});//load
	},

	fn_gnb : function(){
		var header = $('#header'),
			gnb = $('#gnb > ul'),
			btnGnb = header.find('.btn_gnb'),
			btnGnbClose = header.find('.gnb_close'),
			depth1 = gnb.children();
		//서브페이지 고정
		var selected_flag = null;
		depth1.each(function(i){
			if ($(this).hasClass('current')){
				selected_flag = i;
				header.addClass('open');
			}
		});
		depth1.on('mouseover focusin', function(){				
			depth1.removeClass('current');
			depth1.removeClass('on');
			$(this).addClass('on');
			if ($(this).find('ul').length == 1){
				header.addClass('open');
			}else{
				header.removeClass('open');
			}
		});
		header.on('mouseover focusin', function(){				
			header.addClass('active');
		});
		header.on('mouseleave focusout', function(){
			if (selected_flag == null){
				header.removeClass('open');
				depth1.removeClass('on');
				header.removeClass('active');
			}else {
				header.addClass('open');
				depth1.removeClass('on');
				depth1.eq(selected_flag).addClass('current');
			}
		});

		btnGnb.on('click',function(){
			header.addClass('gnb_open');
			$('html').addClass('fixed');
		});

		btnGnbClose.on('click',function(){
			header.removeClass('gnb_open');
			$('html').removeClass('fixed');
		});
	},

	fn_top : function(){
		var btnTop = $('#btn_top');

		var btnTop_movement = function (){
			sTop = $(window).scrollTop();
			if ( sTop >= $('#footer').offset().top - 1000){
				btnTop.addClass('on');
			}else{
				btnTop.removeClass('on');
			}
		};

		btnTop.bind('click',function(){
			$('html,body').stop().animate({scrollTop: ($('#header').offset().top) },500, 'easeInOutExpo');
		});

		$(window).scroll(function(){
			btnTop_movement();
		});
	},

	fn_main : function (){
		if($('#main_contents').length == 0){return;}

		//메인 슬라이더
		$(".section_01 .slider").slick({
			dots: true,
			arrows: false,
			draggable: false,
			infinite: true,
			pauseOnHover: false,
			pauseOnFocus: false,
			autoplay: true,
			autoplaySpeed: 3000,
			responsive: [
				{
				  breakpoint: 750,
				  settings: {
					draggable: true
				  }
				}
			]
		});

		var slickPause = $('.section_01 .btn_pause');
		var slickPlay = $('.section_01 .btn_play');

		slickPause.on('click',function(){
			$(".section_01 .slider").slick('slickPause');
			$(this).fadeOut();
			slickPlay.fadeIn();
		});
		slickPlay.on('click',function(){
			$(".section_01 .slider").slick('slickPlay');
			$(this).fadeOut();
			slickPause.fadeIn();
		});


		//동영상 플레이
		var sec3 = $('.section_03'),
			sec3Play = sec3.find('.btn_play'),
			sec3video = sec3.find('.video'),
			sec3Dim = sec3.find('.dim'),
			sec3Close = sec3.find('.btn_close');

		function stopVideo() {
			var iframe = sec3video.find("iframe")[0].contentWindow;
			iframe.postMessage('{"event":"command","func":"pauseVideo","args":""}','*');
		}
		function playVideo() {
			var iframe = sec3video.find("iframe")[0].contentWindow;
			iframe.postMessage('{"event":"command","func":"playVideo","args":""}','*');
		}

		sec3Play.on('click',function(){
			playVideo();
			$(this).fadeOut();
			sec3video.fadeIn();
			sec3Dim.fadeIn();
		});

		sec3Close.on('click',function(){
			stopVideo();
			sec3Play.fadeIn();
			sec3video.fadeOut();
			sec3Dim.fadeOut();
		});


		//슬라이더
		$(".section_04 .slider").slick({
			dots: false,
			fade: true,
			arrows: false,
			draggable: false,
			pauseOnHover: false,
			pauseOnFocus: false,
			infinite: true,
			autoplay: true,
			autoplaySpeed: 3000,
			asNavFor: '.section_04 .thumnail',
			responsive: [
				{
				  breakpoint: 750,
				  settings: {
					draggable: true,
				  }
				}
			]

		});

		$(".section_04 .thumnail").slick({
			slidesToShow: 5,
			slidesToScroll: 5,
			asNavFor: '.section_04 .slider',
			dots: false,
			arrows: false,
			vertical: true,
			focusOnSelect: true,
			infinite: false,
			responsive: [
				{
				  breakpoint: 750,
				  settings: {
					vertical: false,
				  }
				}
			]
		});

		$(".section_04 .thumnail").on('mouseover',function(){
			$(".section_04 .slider").slick('slickPause');
		});
		$(".section_04 .thumnail").on('mouseleave',function(){
			$(".section_04 .slider").slick('slickPlay');
		});


		//날개nav
		var wing = $('.wing'),
			wing_btn1 = wing.find('.btn_move a:nth-child(1)'),
			wing_btn2 = wing.find('.btn_move a:nth-child(2)'),
			wing_btn3 = wing.find('.btn_move a:nth-child(3)'),
			wing_btn4 = wing.find('.btn_move a:nth-child(4)'),
			wing_top = wing.offset().top;

		var wing_movement = function (){
			sTop = $(window).scrollTop();
			if (sTop >= 0){
				wing.stop().animate({'top': (sTop + 360) },1000,'easeOutExpo');
			}else {
				wing.stop().animate({'top': wing_top },1000,'easeOutExpo');
			}
			if ( sTop >= $('.section_01').offset().top -1 && sTop < $('.section_02').offset().top - wing_top){
				wing_btn1.addClass('on');
			}else{
				wing_btn1.removeClass('on');
			}
			if ( sTop >= $('.section_02').offset().top - wing_top && sTop < $('.section_03').offset().top - wing_top){
				wing.addClass('blue');
				wing_btn2.addClass('on');
			}else{
				wing.removeClass('blue');
				wing_btn2.removeClass('on');
			}
			if ( sTop >= $('.section_03').offset().top - wing_top && sTop < $('.section_04').offset().top - wing_top){
				wing_btn3.addClass('on');
			}else{
				wing_btn3.removeClass('on');
			}
			if ( sTop >= $('.section_04').offset().top - wing_top){
				wing.addClass('blue');
				wing_btn4.addClass('on');
			}else{
				wing_btn4.removeClass('on');
			}
		};

		wing_btn1.bind('click',function(){
			$('html,body').stop().animate({scrollTop: ($('.section_01').offset().top) },500, 'easeInOutExpo');
		});
		wing_btn2.bind('click',function(){
			$('html,body').stop().animate({scrollTop: ($('.section_02').offset().top) },500, 'easeInOutExpo');
		});
		wing_btn3.bind('click',function(){
			$('html,body').stop().animate({scrollTop: ($('.section_03').offset().top) },500, 'easeInOutExpo');
		});
		wing_btn4.bind('click',function(){
			$('html,body').stop().animate({scrollTop: ($('.section_04').offset().top) },500, 'easeInOutExpo');
		});

		$(window).scroll(function(){
			wing_movement();
		});

	},

	fn_count : function (){
		if($('.sub_about_02').length == 0){return;}

		//대상 값의 타입이 String 형 일때
		String.prototype.getComma = function getComma()
		{
			 var reg = /(^[+-]?\d+)(\d{3})/;   // 정규식
			 var num = this;

			 while (reg.test(num)) {
				num = num.replace(reg, '$1' + ',' + '$2');
			 }
			return num;
		}

		//대상 값의 타입이 Number 형 일때
		Number.prototype.getComma = function getComma()
		{
			 var reg = /(^[+-]?\d+)(\d{3})/;   // 정규식
			 var num = this + '';                                // 숫자를 문자열로 변환

			 while (reg.test(num)) {
				num = num.replace(reg, '$1' + ',' + '$2');
			 }
			return num;
		}

		// 숫자카운터 (기존함수) 
		var numberCountingEffect = function (tagID){
			$(tagID).each(function () {
				$(this).prop('Counter',0).animate({ Counter: $(this).text()}, {
					duration: 5000,
					easing: 'swing',
					step: function (now) {
						$(this).text(Math.round(now).getComma());
					}
				});
			});
		}		
		
		var sec3 = $('.section_03'); 
		var counter_01 = 1000000; 
		var counter_02 = 1000000; 
		var counter_03 = 100; 
		var counter_flag = true;

		$(window).scroll(function(){
			sTop = $(window).scrollTop();
			if ( sTop >= (sec3.offset().top - 500) && counter_flag){
				$('#count_01').text(counter_01);
				$('#count_02').text(counter_02);
				$('#count_03').text(counter_03);
				numberCountingEffect('#count_01');
				numberCountingEffect('#count_02');
				numberCountingEffect('#count_03');
				counter_flag = false;
			}
		});
	
	},

	fn_request : function (){
		if($('.sub_request').length == 0){return;}

		var btnRequest = $('.section_03 .btn_box').children();
		var request = $('.section_03 .request_box').children();

		btnRequest.each(function(i){this.num = i});

		btnRequest.on('click',function(){
			if ($(this).hasClass('on')){
				btnRequest.removeClass('on');
				request.removeClass('open');
			}else{
				btnRequest.removeClass('on');
				request.removeClass('open');
				$(this).addClass('on');
				request.eq(this.num).addClass('open');
			}
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
