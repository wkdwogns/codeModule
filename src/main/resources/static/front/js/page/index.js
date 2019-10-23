$(function () {
    var ln = tms.getCookie('lang');
    if(ln!='eng'){
        topStory()
    }
})

function topStory() {
    tms.ajaxGetHelper('/api/story/topStory',{},null,function (rs) {
        if(rs.code==0){
            var list = rs.data;

            for(var i in list){
                var storySeq = list[i].STORY_SEQ;
                var title = list[i].TITLE.replace(/(\n|\r\n)/g,'<br>');;
                var viewStDt = list[i].VIEW_ST_DT;
                var fList = list[i].fList[0].filePath;

                var html=''+
                    '<li onclick="detail('+storySeq+')"><a href="#self">'+
                    '<span class="img"><img src="'+fList+'"></span>'+
                    '<span class="tit txt_line two-line">'+title+'</span>'+
                    '<span class="date">'+viewStDt+'</span>'+
                    '</a></li>';
                $('#main_contents .section_02 ul').append(html);

            }
        }
    });
}



function detail(no){
    tms.ajaxGetHelper('/api/story/detail',{seq:no},null,function (rs) {
        if(rs.code==0){

            var title = rs.data.title;
            var contents = rs.data.contents;
            var fileSeq = rs.data.fileSeq;
            var orgFileNm = rs.data.orgFileNm;
            var viewStDt = rs.data.viewStDt;
            var flist = rs.data.flist;

            $('#popup_all .popup_view .cont').html('');
            $('#popup_all .popup_view .cont').append('<h2 class="name">'+title+'</h2>');
            $('#popup_all .popup_view .cont').append('<p class="date">'+viewStDt+'</p>');
            $('#popup_all .popup_view .cont').append('<div class="file_list"><ul></ul></div>');
            for(var ii in flist){
                $('#popup_all .popup_view .cont .file_list ul').append('<li>'+
                    '<a href="/api/file/download?fileSeq='+flist[ii].fileSeq+'&category=4">' +
                    '<strong>'+flist[ii].orgFileNm+'</strong>' +
                    '</a>'+
                    '</li>');
            }

            $('#popup_all .popup_view .cont').append(contents);
        }

        layer_OPEN('.popup_view');
    })
}