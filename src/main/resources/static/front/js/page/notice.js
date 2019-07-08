var today = new Date();
$(function() {
    getList(1);
});

var getList = function(no) {
    var colsInfo = [
          {no: 1, col: 'no', name: '번호'}
        , {no: 2, col: 'title', name: '제목', width:"400px;", class: function(){return "article"}, custom:function(obj){
                var creDt = obj.creDt;
                var creDtArr = creDt.split(".");

                var date = new Date();
                date.setFullYear(creDtArr[0], (Number(creDtArr[1])-1), creDtArr[2]);
                date.setDate(date.getDate() + 30);

                var html = '<a href="/page/notice/detail?seq='+obj.noticeSeq+'">'+obj.title+'</a>';
                if(obj.importantYn == "Y"){
                    html += '<span class="important"><em class="blind">important</em></span>';
                }
                if(obj.noticeType == noticeTypeCode.type2) { //공지사항
                    html += '<span class="notice"><em class="blind">notice</em></span>';
                }

                if(today.getTime() < date.getTime()) { //30일전
                    html += '<span class="new"><em class="blind">new</em></span>';
                }

                return html;
            }
        }
        , {no: 3, col: 'fileGrpSeq', name: '첨부파일', custom: function(obj){
                if(obj.fileGrpSeq != null && obj.fileGrpSeq != 0) {
                    return '<img src="/front/images/common/ico_file.gif" alt="첨부파일" />';
                } else {
                    return "";
                }
            }
        }
        , {no: 4, col: 'creDt', name: '등록일'}

    ];

    var params = {
        cntPerPage: 10
        ,currentPage:no
    };

    tms.ajaxGetHelper('/api/notice', params, null, function(result) {
        var paging = {
            id: '#paging',
            pageNo: no,
            totalCnt: 0,
            countPerPage: 10,
            pageCount: 10,
            fn: 'getList',
            activeClass: 'on',
            type: 'type3'
        };

        var info = {
            colsInfo: colsInfo,
            paging: paging
        };

        var listObj = "#list";

        if(result.code == 0) {
            var totalCnt = result.data.totalCnt;
            paging.totalCnt = totalCnt;

            if(totalCnt == 0){
                tableType3(listObj, null, info, 4, "데이터가 없습니다.");
            } else {
                tableType3(listObj, result.data.list, info);
            }
        } else {
            tableType3(listObj, null, info );
        }

    }, function(){

    });

}