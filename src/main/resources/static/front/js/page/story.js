$(function() {
    getImportList();
    getList(1);

    //상세이동
    $(document).on("click", ".story_list li a, .slide_wrap .slick-list a", function(e){
        location.href = "/page/story/detail?seq="+$(this).data("seq");
    });

});

var getList = function(no) {
    var params = {
        cntPerPage: 8
        , currentPage:no
    };

    tms.ajaxGetHelper('/api/story', params, null, function(result) {
        var paging = {
            id: '#paging',
            pageNo: no,
            totalCnt: 0,
            countPerPage: 8,
            fn: 'getList',
            activeClass: 'on',
            type: 'pagingMore'
        };

        var info = {
            paging: paging
        };

        var contents = function (obj){
            //return '<a href="#self" data-seq="'+obj.seq+'">  <img src="'+obj.thumbnail+'" alt="" width="370px;" /> <span class="tit">'+obj.cate+'</span>  <span class="comn">'+obj.title+'</span>  </a>';
            var html = "";
            html += '<a href="#self" data-seq="'+obj.storySeq+'">';
            html += '   <span class="img_box"><img src="'+obj.filePath+'" alt="" /></span>';
            html += '   <span class="infor_box">';
			html += '       <strong>'+obj.title+'</strong>';
			html += '       '+obj.storyNm+'';
			html += '   </span>';
            html += '   <span class="frame"><em></em></span>';
            html += '</a>';
            return html;
        };

        if(result.code == "0") {
            var totalCnt = result.data.totalCnt;

            $("#totalCnt").text(totalCnt);
            paging.totalCnt = totalCnt;
            if(totalCnt == 0){
                tableType2More("#story_list_ul", null, contents, info)
            } else {
                tableType2More("#story_list_ul", result.data.list, contents, info)
            }
        } else {
            tableType2More("#story_list_ul", null, contents, info)
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

                loadSwiper();
            }
        } else {

        }


    }, function(){

    });

}

