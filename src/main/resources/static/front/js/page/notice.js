var today = new Date();
$(function() {
    getList(1);
});

var getList = function(no) {
    var colsInfo = [
          {no: 1, col: 'no', name: '번호'}
        , {no: 2, col: 'title', name: '제목', width:"400px;"
            , class: function(obj){
                var creDt = obj.creDt;
                var creDtArr = creDt.split(".");
                var date = new Date();
                date.setFullYear(creDtArr[0], (Number(creDtArr[1])-1), creDtArr[2]);
                date.setDate(date.getDate() + 30);

                var cl = "article";
                if(obj.fileGrpSeq != null && obj.fileGrpSeq != 0) {
                    cl += ' file';
                }
                if(today.getTime() < date.getTime()) { //30일전
                    cl += ' new';
                }
                return cl
            },
            custom:function(obj){
                var creDt = obj.creDt;
                var creDtArr = creDt.split(".");

                var date = new Date();
                date.setFullYear(creDtArr[0], (Number(creDtArr[1])-1), creDtArr[2]);
                date.setDate(date.getDate() + 30);

                var nw = '';
                var f = '';
                if(obj.fileGrpSeq != null && obj.fileGrpSeq != 0) {
                    f='<span class="ico_file"><em class="blind">file</em></span>';
                }

                if(today.getTime() < date.getTime()) { //30일전
                    nw='<span class="ico_new"><em class="blind">new</em></span>';
                }

                var url = location.pathname.replace('list','view')+'?seq='+obj.noticeSeq;
                var html = '<a href="#self" onclick="viewCnt('+obj.noticeSeq+')">'+obj.title+nw+f+'</a>';
                return html;
            }
        }
        , {no: 3, col: 'creDt', name: '등록일', class: function(){return "date"}}
        , {no: 4, col: 'viewCnt', name: '조회수', class: function(){return "count"}}
    ];

    var params = {
        cntPerPage: 10
        ,currentPage:no
        ,keyField:$('#keyField').val()
        ,keyWord:$('#keyWord').val()
    };

    tms.ajaxGetHelper('/api/notice', params, null, function(result) {

        var paging = {
            id: '#paging',
            pageNo: no,
            totalCnt: result.data.totalCnt,
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

        var listObj = ".notice_list_box";

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

var viewCnt = function(no){

    tms.ajaxPutHelper('/api/notice/viewCnt', JSON.stringify({no:no}), null, function(rs) {

        if(rs.code==0){
            var url = location.pathname.replace('list','view')+'?seq='+no;
            location.href=url;
        }
    });
}