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
            var c = obj.category;
            if(c=='1'){c='IT'}
            if(c=='2'){c='CULTURE'}
            if(c=='3'){c='PLAY'}
            if(c=='4'){c='HEALTH'}

            $('.story_view_wrap .story_top .cate').text(c);
            $('.story_view_wrap .story_top .part').text(obj.storyNm);
            $('.story_view_wrap .story_top .title').text(obj.title);
            $('.story_view_wrap .story_top .date').text(obj.creDt);
            $('.story_view_wrap .story_article').html( obj.contents );

            var prevStory = res.data.prevStory;
            var nextStory = res.data.nextStory;

            var ln = tms.getCookie('lang');
            var next = (ln=='eng')?'next':'다음글';
            var prev = (ln=='eng')?'prev':'이전글';

            if(prevStory != null) {
                var prevHtml = prev+'<span>'+prevStory.storyNm+'</span>';
                $("#prevStory").attr("href", location.pathname +'?seq='+ prevStory.storySeq);
                $("#prevStory").html(prevHtml);
            } else {
                $("#prevStory").hide();
            }

            if(nextStory != null) {
                var nextHtml = next+'<span>'+nextStory.storyNm+'</span>';
                $("#nextStory").attr("href", location.pathname +'?seq='+ nextStory.storySeq);
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