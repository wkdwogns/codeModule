$(function(){
    setLangUI();
});

var setLang = function (lang) {
    tms.ajaxPostHelper('/api/lang',JSON.stringify({lang:lang}), null, function(rs) {
        setLangUI();

        var ln = tms.getCookie('lang');
        var pNm = location.pathname;
        var pLink = (ln == 'eng')?pNm.replace('kor','eng'):pNm.replace('eng','kor');
        location.href=pLink;
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

var getPopup = function() {

    var cookieVal = tms.getCookie("popup");
    var cookieArray = cookieVal.split(",");

    tms.ajaxGetHelper('/api/popup', null, null, function(result) {
        if(result.code == 0) {
            var list = result.data.list;
            var html = "";
            if(list.length > 0) {
                for(var i=1; i<list.length+1; i++) {
                    var cookieFalg = false;
                    if(cookieArray != null && cookieArray.length > 0) {
                        for(var j=0; j<cookieArray.length; j++) {
                            if(cookieArray[j] == ("popup_main_0"+i)) {
                                cookieFalg = true;
                                break;
                            }
                        }
                    }

                    if(!cookieFalg) {
                        html += '<div class="popup_main_0'+i+'">';
                        html += '    <div class="out_layer_box">';
                        html += '        <div class="in_layer_box">';
                        if(tms.isNotEmpty(list[i-1].targetUrl)){
                            if(list[i-1].targetYn == "N"){
                                html += ' <div class="img"> <a href="'+list[i-1].targetUrl+'" target="_blank"><img src="'+list[i-1].filePath+'" /></a></div>';
                            } else {
                                html += ' <div class="img"> <a href="'+list[i-1].targetUrl+'"><img src="'+list[i-1].filePath+'" /></a></div>';
                            }
                        } else {
                            html += '           <div class="img"><img src="'+list[i-1].filePath+'" /></div>';
                        }
                        html += '            <div class="close_box">';
                        html += '                <label for="CHK_0'+i+'" class="chk"><input type="checkbox" id="CHK_0'+i+'" name="CHK_0'+i+'" /> 오늘 하루 다시 보지 않기</label>';
                        html += "                <a href=\"#self\" class=\"btn_close_02\" onclick=\"popupClose('popup_main_0', "+i+");\"><span class=\"blind\">닫기</span></a>";
                        html += '            </div>';
                        html += '        </div>';
                        html += '    </div>';
                        html += '</div>';
                    }
                }

                $(".popup_main .in_box").html(html);
                $(".popup_all").show();
            }

        }

    });
};

var popupClose = function(classVal, idx) {
    var checked = $("input:checkbox[id='CHK_0"+idx+"']").is(":checked");
    if(checked) {
        var cookieVal = tms.getCookie("popup");
        cookieVal = cookieVal + "," + classVal + idx;
        tms.setCookie("popup", cookieVal, 1);
    }

    layer_CLOSE("." + classVal + idx);
};


/* 메인배너 조회 */
var getBanner = function(){
    tms.ajaxGetHelper("/api/banner", null, null, function(res){
        if(res.code == 0){
            var list = res.data.list;
            var html = '';
            list.forEach(function(obj, idx){
                $('.section_01 .slider').slick('slickAdd', bannerTemplate(obj))
            });
        }
    });
}

/* 컨텐츠 배너 조회 */
var getContentsBanner = function(){
    tms.ajaxGetHelper("/api/banner/contents", null, null, function(res){
        if(res.code == 0){
            var list = res.data.list;
            var html = '';
            list.forEach(function(obj, idx){
                $(".section_04 .thumnail").slick('slickAdd', bannerContentsThumTemplate(obj))
                $('.section_04 .slider').slick('slickAdd', bannerContentsTemplate(obj))
            });
        }
    });
}

/* 메인 배너 템플릿 */
var bannerTemplate = function(obj){
    var html;
    if(obj.bannerType == bannerTypeCode.type3){
        html = '<div>'
        html +=    '<div class="bg_picture">';
        html +=    '    <iframe src="https://www.youtube.com/embed/'+obj.videoUrl+'" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen  style="width: 100%;height: 100%;"></iframe>';
        html +=   '</div>'
        html +=   '   <div class="comment_box">'
         html +=  '    <p class="tit">'+obj.topText+'</p>'
         html +=  '<p class="txt" style="word-break: keep-all;white-space:pre;">'+obj.title+'</p>'
         html +=  '</div>'
         html +=  '</div>';
    } else {
        html = '<div>';
        if(tms.isNotEmpty(obj.targetUrl)){
            html +='<a href="'+obj.targetUrl+'">';
        }
         html +=   '<div class="bg_picture" style="background:url('+obj.filePath+') center top no-repeat;"></div>';
        if(tms.isNotEmpty(obj.targetUrl)){
            html +='</a>';
        }
         html +=   '   <div class="comment_box">';
         html +=   '    <p class="tit">'+obj.topText+'</p>';
         html +=   '<p class="txt" style="word-break: keep-all;white-space:pre;">'+obj.title+'</p>';
         html +=   '</div>';
         html +=   '</div>';
    }
    return html;
}

/* 컨텐츠 배너 썸네일 템플릿 */
var bannerContentsThumTemplate = function(obj){
    var html = '<div>'+
      '  <div class="img"><img src="'+obj.filePath+'" alt="" /></div>'+
     '   <p class="name" style="word-break: keep-all;">'+obj.thumbnailNm+'</p>'+
    '</div>';

    return html;
}

/* 컨텐츠 배너 템플릿 */
var bannerContentsTemplate = function(obj){
    var html = '<div>'
    html += '<div class="picture"><img src="'+obj.filePath+'" alt="" /></div>';
    html +=                '<div class="txt_box">';
    html +=                    '<p class="txt_01" style="word-break: keep-all;white-space:pre;">'+obj.topText+'</p>';
    html +=                    '<p class="txt_02" style="word-break: keep-all;white-space:pre;">'+obj.title+'</p>';
    html +=                    '<p class="txt_03" style="word-break: keep-all;white-space:pre;">'+obj.contents+'</p>';
                                if(tms.isNotEmpty(obj.targetUrl)){
                                    html += '<a href="'+obj.targetUrl+'" class="btn_more"><span class="blind">자세히보기</span></a>';
                                }
    html +=                '</div>';
    html +=           '</div>';

    return html;
}