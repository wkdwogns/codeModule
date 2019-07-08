var seq = tms.getParameterByName("seq");
var preview = tms.getParameterByName("preview");
var detailUrl = '/api/story/detail';
if(preview == "Y"){
    detailUrl = '/api/story/preview';
}
$(function() {
    getDetail();
});

var getDetail = function() {
    var params = {
        seq: seq
    };

    tms.ajaxGetHelper(detailUrl, params, null, function(res) {
        if(res.code == 0){
            var obj =res.data;
            tmsInput.detailMapping(obj);

            var prevStory = res.data.prevStory;
            var nextStory = res.data.nextStory;

            if(prevStory != null) {
                var prevHtml = '<strong>이전 이야기 보기</strong>';
                prevHtml += '<span>'+prevStory.storyNm+'</span>';
                $("#prevStory").attr("href", "/page/story/detail?seq=" + prevStory.storySeq);

                $("#prevStory").html(prevHtml);
            } else {
                $("#prevStory").hide();
            }

            if(nextStory != null) {
                var nextHtml = '<strong>다음 이야기 보기</strong>';
                nextHtml += '<span>'+nextStory.storyNm+'</span>';
                $("#nextStory").attr("href", "/page/story/detail?seq=" + nextStory.storySeq);

                $("#nextStory").html(nextHtml);
            } else {
                $("#nextStory").hide();
            }
        }  else if(res.code == "218"){
            alert("옳바르지 않은 접근입니다.");
            location.href = "/";
            return false;
        }
    });

};