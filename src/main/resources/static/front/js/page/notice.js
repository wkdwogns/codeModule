$(function() {
    getList(1);

    //상세이동
    $(document).on("click", ".notice_list li", function(e){
        var seq = $(this).find('a').data("seq");

        var p = $(this).prev().find('a').data("seq");
        var n = $(this).next().find('a').data("seq");

        getDetail(seq,p,n);
    });

});

var getList = function(no) {

    var params = {
        cntPerPage: 12
        ,currentPage:no
    };

    tms.ajaxGetHelper('/api/notice', params, null, function(result) {

        var contents = function (obj){

            var fPath = (obj.filePath==null)?'/front/images/sub/exp1.jpg':obj.filePath;

            // var html = "";
            // html += '<a href="#self" data-seq="'+obj.noticeSeq+'">';
            // html += '   <span class="img"><img src="'+fPath+'" alt=""/></span>';
            // html += '   <span class="tit">'+obj.title+'</span>';
            // html += '   <span class="date">'+obj.viewStDt+'</span>';
            // html += '</a>';

            var html = "";
            html += '<a href="#self" data-seq="'+obj.noticeSeq+'">';
            html += '   <span class="name">'+obj.title+'</span>';
            html += '</a>';
            html += '   <span class="date">'+obj.viewStDt+'</span>';

            return html;
        };

        if(result.code == "0") {
            var totalCnt = result.data.totalCnt;

            if(totalCnt == 0){
                tableType2("#notice_list", null, contents, {})
            } else {
                tableType2("#notice_list", result.data.list, contents, {})
                var len = $('.sub_news .story_list ul li').length;
                if( (len % 4) == 1 ){
                    $('.sub_news .story_list ul li:last-child').css({'float':'none'});
                }
            }

            // 페이지 카운트
            var pageCnt = totalCnt % params.cntPerPage;
            if (pageCnt == 0) {
                pageCnt = Math.floor(totalCnt / params.cntPerPage);
            } else {
                pageCnt = Math.floor(totalCnt / params.cntPerPage) + 1;
            }

            if(params.currentPage>=pageCnt){
                $('.sub_news .btn_more button').remove();
            }else{
                $('.sub_news .btn_more button').show();
                $('.sub_news .btn_more button').click(function(){
                    var idx=params.currentPage+1;
                    getList(idx);
                })
            }

            $('.sub_news .tab_box a:nth-child(1) .count').text(result.data.storyCnt);
            $('.sub_news .tab_box a:nth-child(2) .count').text(totalCnt);

        } else {
            tableType2("#story_list", null, contents, info)
        }

    }, function(){

    });

}

var getDetail = function(no,p,n) {
    var params = {
        seq: no
        ,prev:p
        ,next:n
    };

    tms.ajaxGetHelper('/api/notice/detail', params, null, function(rs) {

        if(rs.code==0){
            var title = rs.data.title;
            var viewStDt =rs.data.viewStDt;
            var contents = rs.data.contents;
            var fileSeq = rs.data.fileSeq;
            var orgFileNm = rs.data.orgFileNm;
            $('#popup_all .popup_view .cont').html('');
            $('#popup_all .popup_view .cont').append('<h2 class="name">'+title+'</h2>');
            $('#popup_all .popup_view .cont').append('<p class="date">'+viewStDt+'</p>');
            if(fileSeq!=null&&fileSeq!=''){
                $('#popup_all .popup_view .cont').append('<div class="file_list"><ul><li>'+
                    '<a href="/api/file/download?fileSeq='+fileSeq+'&category=3"><strong>'+orgFileNm+'</strong></a>'+
                    '</li></ul></div>');
            }
            $('#popup_all .popup_view .cont').append( tms.convertHtml(contents) );

            var prevS = rs.data.prevNotice;
            var nextS = rs.data.nextNotice;

            var BtnPrev = $('#popup_all .popup_view .control .btn_prev');
            var BtnNext = $('#popup_all .popup_view .control .btn_next');
            if(prevS==null){
                BtnPrev.hide()
            }else{
                //$('.prevS .img img').attr('src',prevS.filePath);
                $('.prevS .name').text( prevS.title );
                $('.prevS .date').text(prevS.viewStDt);
                BtnPrev.attr('onclick','getDetail('+prevS.noticeSeq+')').show();
            }
            if(nextS==null){
                BtnNext.hide()
            }else{
                //$('.nextS .img img').attr('src',nextS.filePath);
                $('.nextS .name').text( nextS.title );
                $('.nextS .date').text(nextS.viewStDt);
                BtnNext.attr('onclick','getDetail('+nextS.noticeSeq+')').show();
            }
        }

        layer_OPEN('.popup_view');
    });

}