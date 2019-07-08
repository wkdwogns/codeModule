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
            tmsInput.detailMapping(obj);

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

            if(prevNotice != null) {
                var prevHtml = '<span class="up">이전 글</span>';
                prevHtml += '<a href="/page/notice/detail?seq='+prevNotice.noticeSeq+'">'+prevNotice.title+'</a>';

                $("#prevNotice").html(prevHtml);
            } else {
                $("#prevNotice").hide();
            }

            if(nextNotice != null) {
                var nextHtml = '<span class="down">다음 글</span>';
                nextHtml += '<a href="/page/notice/detail?seq='+nextNotice.noticeSeq+'">'+nextNotice.title+'</a>';

                $("#nextNotice").html(nextHtml);
            } else {
                $("#nextNotice").hide();
            }

        } else {
            alert("삭제 혹은 비공개 개시글 입니다.")
        }
    });

}
