tms.ajaxGetHelper('/api/story/topStory',{},null,function (rs) {
    if(rs.code==0){
        var list = rs.data;

        for(var i in list){
            var storySeq = list[i].STORY_SEQ;
            var title = list[i].TITLE.replace(/(\n|\r\n)/g,'<br>');;
            var creDt = list[i].CRE_DT;
            var updDt = list[i].UPD_DT;
            var fList = list[i].fList[0].filePath;
            var dt = (updDt!=null||updDt!='')?updDt:creDt;

            var html=''+
                '<li onclick="detail('+storySeq+')"><a href="#self">'+
                '<span class="img"><img src="'+fList+'"></span>'+
                '<span class="tit txt_line">'+title+'</span>'+
                '<span class="date">'+dt+'</span>'+
                '</a></li>';
            $('#main_contents .section_02 ul').append(html);
        }
    }
});

function detail(no){
    tms.ajaxGetHelper('/api/story/detail',{seq:no},null,function (rs) {

        if(rs.code==0){
            var title = rs.data.title;
            var contents = rs.data.contents;
            var fileSeq = rs.data.fileSeq;
            var orgFileNm = rs.data.orgFileNm;
            $('#popup_all .popup_view .cont').html('');
            $('#popup_all .popup_view .cont').append('<h2 class="name">'+title+'</h2>');
            if(fileSeq!=null&&fileSeq!=''){
                $('#popup_all .popup_view .cont').append('<div class="file_list"><ul><li>'+
                    '<a href="/api/file/download?fileSeq='+fileSeq+'&category=4"><strong>'+orgFileNm+'</strong></a>'+
                    '</li></ul></div>');
            }
            $('#popup_all .popup_view .cont').append(contents);
        }

        layer_OPEN('.popup_view');
    })
}