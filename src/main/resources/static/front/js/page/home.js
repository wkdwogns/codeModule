$(function(){
    var ln = tms.getCookie('lang');
    if((ln=='eng')){
        $('#header .logo a').css({
            'background': 'url(/front/images/common/logo_eng.png) 0 center no-repeat'
            ,'background-size': '185px auto'
        })
        $('#footer .logo_box ul li .logo_01').css({
            'background-image': 'url(/front/images/common/footer_logo_01_eng.png)'
        })
        $('#footer .logo_box ul li .logo_02').css({
            'background-image': 'url(/front/images/common/footer_logo_02_eng.png)'
        })
    }
});

var setLang = function (lang) {
    tms.ajaxPostHelper('/api/lang',JSON.stringify({lang:lang}), null, function(rs) {
        setLangUI();

        var ln = tms.getCookie('lang');
        var pNm = location.pathname;
        var pLink = (ln == 'eng')?pNm.replace('kor','eng'):pNm.replace('eng','kor');

        location.href=pLink+location.search;
    });
}

var setLangUI = function () {
    var ln = tms.getCookie('lang');
    var mLi = $('#header .util_menu li');
    mLi.removeClass('on')
    if(ln == 'eng'){
        mLi.eq(1).addClass('on')
    }else{
        mLi.eq(0).addClass('on')
    }
    $.each($('#header .depth2 a'),function () {
        var attr = $(this).attr('href');
        var link = (ln == 'eng')?attr.replace('kor','eng'):attr.replace('eng','kor');
        $(this).attr('href',link);
    })

    $.each($('.mobild_sub_tabs a'),function () {
        var attr = $(this).attr('href');
        var link = (ln == 'eng')?attr.replace('kor','eng'):attr.replace('eng','kor');
        $(this).attr('href',link);
    })


}

var getList = function(no) {
    var params = {
        cntPerPage: 10
        , currentPage:no
    };

    tms.ajaxGetHelper('/api/notice', params, null, function(result) {
        var paging = {
            pageNo: no,
            totalCnt: 0,
            countPerPage: 4,
            fn: 'getList',
            activeClass: 'on',
            type: 'type2'
        };

        var info = {
            paging: paging
        };

        var contents = function (obj){
            return '<a href="/page/notice/detail?seq='+obj.seq+'"> <span class="article">'+obj.title+'</span> <span class="date">'+obj.createDt+'</span>  </a>';
        }

        if(result.code == "0") {
            var totalCnt = result.data.totalCnt;
            if(totalCnt == 0){
                tableType2(".notice_list", null, contents, info)
            } else {
                tableType2(".notice_list", result.data.list, contents, info)
            }
        }
    }, function(){

    });

};