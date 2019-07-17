$(function() {
    basicList(1);

    //상세이동
    $(document).on("click", ".story_list li a", function(e){
        var lang = tms.getCookie('lang');
        var na = 'kor';
        if(lang=='eng'){na = 'eng';}
        location.href = "/page/"+na+"/story_view?seq="+$(this).data("seq");
    });

});

var params = {
    cntPerPage: 12
};

var basicList = function (no) {
    params.currentPage=no;
    params.category=$('#category').val();
    params.keyField=$('#keyField').val();
    params.keyWord=$('#keyWord').val();

    getList(params);
    setCate(params.category);
}

var barList = function (bar) {

    params.currentPage=1;
    params.category=bar;
    params.keyField=$('#keyField').val();
    params.keyWord=$('#keyWord').val();

    getList(params);
    setCate(params.category);
}

var setCate = function (bar) {
    $('#category').val(bar);

    if(bar==''){bar=0;}
    $('.story_list_wrap .story_tab li').removeClass('on');
    $('.story_list_wrap .story_tab li').eq(bar).addClass('on');
}



var getList = function(params) {

    tms.ajaxGetHelper('/api/story', params, null, function(result) {
        console.log(result.data.totalCnt);
        $('.story_list').html('');
        var paging = {
            id: '#paging',
            pageNo: params.currentPage,
            totalCnt: result.data.totalCnt,
            countPerPage: 12,
            fn: 'basicList',
            activeClass: 'on',
            type: 'type2'
        };

        var info = {
            paging: paging
        };

        var contents = function (obj){
            var cate = obj.category;
            if(cate==1){cate='IT'}
            if(cate==2){cate='CULTURE'}
            if(cate==3){cate='PLAY'}
            if(cate==4){cate='HEALTH'}


            var html = "";
            html += '<a href="#self" data-seq="'+obj.storySeq+'">';
            html += '   <span class="img_box"><img src="'+obj.filePath+'" alt=""/></span>';
            html += '   <span class="infor_box">';
            html += '       <span class="cate">'+cate+'</span>';
            html += '       <span class="tit">'+obj.storyNm+'</span>';
            html += '       <span class="comn">'+obj.title+'</span>';
			html += '   </span>';
            html += '</a>';

            return html;
        };

        if(result.code == "0") {
            var totalCnt = result.data.totalCnt;
            paging.totalCnt = totalCnt;
            if(totalCnt == 0){
                tableType2(".story_list", null, contents, info)
            } else {
                tableType2(".story_list", result.data.list, contents, info)
            }
        } else {
            tableType2(".story_list", null, contents, info)
        }

    }, function(){

    });

}

var getImportList = function(no) {

    tms.ajaxGetHelper('/api/story/important', null, null, function(result) {
        if(result.code == "0") {
            var storyImpList = result.data.storyImpList;
            if(storyImpList != null && storyImpList.length > 0) {
                var html = "";
                for(var i=0; i<storyImpList.length; i++) {
                    html += '<div class="slider-item">';
                    html += '<a href="#self" data-seq="'+storyImpList[i].storySeq+'">';
                    html += '   <span class="img_box"><img src="'+storyImpList[i].filePath+'" alt="" /></span>';
                    html += '   <span class="infor_box">';
                    html += '       <strong>'+storyImpList[i].title+'</strong>';
                    html += '       '+storyImpList[i].storyNm+'';
                    html += '   </span>';
                    html += '   <span class="frame"><em></em></span>';
                    html += '</a>';
                    html += '</div>';
                }

                $(".slide_wrap .slider").html(html);

                //loadSwiper();
            }
        } else {

        }


    }, function(){

    });

}

