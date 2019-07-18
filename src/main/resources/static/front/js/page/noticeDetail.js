var seq = tms.getParameterByName("seq");
$(function() {
    getDetail();
});

var getDetail = function() {
    var params = {
        seq: seq
    };

    tms.ajaxGetHelper('/api/notice/detail', params, null, function(res) {

        if(res.code == 0){
            var obj = res.data;

            $('.story_view_wrap .story_top .title').text(obj.title);
            $('.story_view_wrap .story_top .date').html(obj.creDt+'<em class="count">'+obj.viewCnt+'</em>');
            $('.story_view_wrap .story_article').html( obj.contents );

            var files = obj.flist;
            if(tms.isNotEmpty(files)){
                for(var i in files){
                    $('#file_list').append('<li><a href="/api/file/download?fileSeq='+files[i].fileSeq+'&category=3">'+files[i].orgFileNm+'</a></li>');
                }
            } else {
                $(".file_list").hide();
            }

            var prevNotice = res.data.prevNotice;
            var nextNotice = res.data.nextNotice;

            var ln = tms.getCookie('lang');
            var next = (ln=='eng')?'next':'다음글';
            var prev = (ln=='eng')?'prev':'이전글';

            if(prevNotice != null) {
                var prevHtml = prev+'<span>'+prevNotice.title+'</span>';
                $("#prevNotice").attr("href", location.pathname +'?seq='+ prevNotice.noticeSeq);
                $("#prevNotice").html(prevHtml);
            } else {
                $("#prevNotice").hide();
            }

            if(nextNotice != null) {
                var nextHtml = next+'<span>'+nextNotice.title+'</span>';
                $("#nextNotice").attr("href", location.pathname +'?seq='+ nextNotice.noticeSeq);
                $("#nextNotice").html(nextHtml);
            } else {
                $("#nextNotice").hide();
            }

        } else {
            alert("삭제 혹은 비공개 개시글 입니다.")
        }
    });

}
