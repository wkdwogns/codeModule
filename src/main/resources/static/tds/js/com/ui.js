UI = {
	load : function(){
        UI.fn_side();
        UI.fn_depth2();
        UI.fn_scroll('.sidebar-menu > ul');
        UI.fn_scroll('.project_list > ul');
        UI.fn_profile();
        UI.fn_datepicker('#DATE');
        UI.fn_datepicker('#DEADLINE');
        UI.fn_datepicker('#Date1');
        UI.fn_datepicker('#Date2');
        UI.fn_datepicker('.startDt');
        UI.fn_datepicker('.endDt');
        //UI.fn_select('#select_01');
        //UI.fn_select('#select_02');
        //UI.fn_select('.selectBox_01');
        //UI.fn_select('.selectBox_02');
        UI.fn_btnRemove('.tag_list');
        UI.fn_btnRemove('.project_items');
        UI.fn_tabAction('#tab_btn','#tab_cont');
        UI.fn_side_select();

		/*$(window).load(function(){
			
		});

		$(document).ready(function(){
			UI.fn_side();
			UI.fn_depth2();
			UI.fn_scroll('.sidebar-menu > ul');
			UI.fn_scroll('.project_list > ul');
			UI.fn_profile();
			UI.fn_datepicker('#DATE');
			UI.fn_datepicker('#DEADLINE');
			UI.fn_datepicker('#Date1');
			UI.fn_datepicker('#Date2');
			UI.fn_select('#select_01');
			UI.fn_select('#select_02');
			UI.fn_btnRemove('.tag_list');
			UI.fn_btnRemove('.project_items');
			UI.fn_tabAction('#tab_btn','#tab_cont');
		});*/
	},

	fn_side : function(){
		var $side = $('#sidebar');
		var $body = $('body');

		$(document).on('mouseover', '#sidebar',function(){
			$body.addClass('sidebar-visible');
		});

        $(document).on('mouseleave','#sidebar',function(){
			$body.removeClass('sidebar-visible');
		});
	},

    fn_side_select : function() {
        //메뉴 정보 설정
        $(document).find(".menu-items li").each(function(e) {
            $(this).removeClass("open");
            $(this).find("span").removeClass("bg-success");
        });

        var menuData = window.sessionStorage["menu_data"];
        $(document).find(".menu-items a").each(function(e) {
            var dataValue = $(this).attr("data-value");

            if(tms.isEmpty(menuData)) {
                if(menuData == dataValue) {
                    $(this).parents().addClass("open");
                    $(this).next().addClass("bg-success");
                }
            }

        });
	},

	fn_depth2 : function(){

	},

	fn_scroll : function (obj_scroll){
		var $sidebar = $(obj_scroll);
		$sidebar.scrollbar();
	},

	fn_datepicker : function (obj_date){
		var $date = $(obj_date);
		$date.datepicker();
	},

	fn_profile : function (){
        /*var $profile = $('.header .profile');
        var $button = $profile.find('.photo');
        var $menu = $profile.find('.layer_menu');

        $button.on('click',function(){
            $menu.toggle();
        });*/
		/*var $profile = $(document).find('.header .profile');
		//var $button = $profile.find('.photo');
		var $menu = $profile.find('.layer_menu');

		$(document).on('click', '.header .profile .photo', function(){
			$menu.toggle();
		});*/
	},

	fn_select : function (obj_select){
		var $select = $(obj_select);
		var $select_btn = $select.find('> a');
		var $select_option = $select.find('.option');

		$select_btn.bind('click',function(){
			$select.toggleClass('open');
			$select_option.stop().slideToggle("fast");
		});

	},

	fn_btnRemove : function (obj_remove){
		var $RemoveWrap = $(obj_remove);
		var $Removes = $RemoveWrap.find ('> li');
		var $btnRemove = $Removes.find('.btn_remove');

		$btnRemove.each(function(i){this.num = i});

		$btnRemove.bind('click',function(){
			$Removes.eq(this.num).remove();
		});
	},

	fn_tabAction : function(button, target){
		var $button = $(button).children(),
			$target = $(target).children();

		$button.each(function(i){this.num = i});

		$button.bind('click', function(){
			$(this).addClass('on');
			$(this).siblings().removeClass('on');
			$target.eq(this.num).addClass('on');
			$target.eq(this.num).siblings().removeClass('on');
		});
	}



}

//UI.load();


//레이어 오픈 
var layer_OPEN = function (obj_selector){
	var obj = $(obj_selector);
	obj.css({'display':'table','opacity':0});
	obj.stop().animate({'opacity':1},300);
};

//레이어 클로즈
var layer_CLOSE = function (obj_selector){
	var obj = $(obj_selector);	
	obj.stop().animate({'opacity':0},300,function (){
		$(this).css({'display':'none'});	
	});
};


$(window).load(function(){
    UI.load();
});