var bannerUrl = {
    list: "/mapi/banner"
    , viewList: "/mapi/banner/view"
    , sort: "/mapi/banner/sort"
    , pageAdd : "/page/admin/bannerT/add"
}

$(function(){
    /* 노출관리 목록 조회 및 배너 목록 조회 */
    getViewList(function(){ //노출목록
        list(1, function(){ //배너목록
            //노출관리 목록에 추가 컨트롤
            $(document).on("change", "input[name=chk]", function(){
                viewYnControl(this);
            });
        });
    });

    /* 노출 및 정렬 저장 */
    $("#btnViewSave").on("click", function(){
        viewSave();
    });

    /* 등록페이지 이동 */
    $('.devCssBtnAdd').click(function () {
        location.href= bannerUrl.pageAdd;
    });

    /* 엔터검색 */
    $("#sKeyword").keypress(function (e) {
        if (e.which == 13) {
            list(1);
        }
    });
});

function list(no, callback){
    $('.main').addClass('list');

    var colsInfo = [
        {no: 1, col: '', name: '선택',width:'40px', custom:function(obj){
            return "<input type='checkbox' name='chk' value='"+obj.bannerSeq+"' />";
        }}
        , {no: 2, col: 'no', name: '번호',width:'40px'}
        //,{no: 3, col: 'typeNm', name: '배너타입',width:'80px'}
        , {no: 4, col: 'subject', name: '배너명',custom:function (obj) {
            return '<a href="'+bannerUrl.pageAdd+'?info='+obj.bannerSeq+'">'+obj.bannerNm+'</a>';
        }}
        , {no: 5, col: '', name: '노출기간',width:'200px',custom:function (obj) {
            if(obj.viewUnlimitYn == "Y"){
                return "무제한"
            } else {
                return obj.viewStDt+' ~ '+obj.viewEndDt;
            }
        }}
        , {no: 6, col: 'thumbnail', name: '관리', width:'120px', custom:function(obj){
            var img = obj.filePath;
            if(obj.bannerType != bannerTypeCode.type3){
                return '<a href="#self" class="btn grayBtn" onclick="goT(\''+img+'\')">'+'미리보기'+'</a>';
            }
        }}
    ];

    if(bannerUrl.list == "/mapi/banner/contents") {
        colsInfo = [
            {no: 1, col: '', name: '선택',width:'40px', custom:function(obj){
                return "<input type='checkbox' name='chk' value='"+obj.bannerSeq+"' />";
            }}
            , {no: 2, col: 'no', name: '번호',width:'40px'}
            , {no: 3, col: 'subject', name: '배너명',custom:function (obj) {
                return '<a href="'+bannerUrl.pageAdd+'?info='+obj.bannerSeq+'">'+obj.bannerNm+'</a>';
            }}
            , {no: 4, col: '', name: '노출기간',width:'200px',custom:function (obj) {
                if(obj.viewUnlimitYn == "Y"){
                    return "무제한"
                } else {
                    return obj.viewStDt+' ~ '+obj.viewEndDt;
                }
            }}
            , {no: 5, col: 'thumbnail', name: '관리', width:'120px', custom:function(obj){
                var img = obj.filePath;
                return '<a href="#self" class="btn grayBtn" onclick="goT(\''+img+'\')">'+'미리보기'+'</a>';
            }}
        ];
    }

    var params = {
        cntPerPage: 10
        ,currentPage:no
        ,keyWord : $('#sKeyword').val()
    };

    tms.ajaxGetHelper(bannerUrl.list, params, null, function(result) {
        var paging = {
            id: '#paging',
            pageNo: no,
            totalCnt: 0,
            countPerPage: 10,
            fn: 'list',
            activeClass: 'link on',
            type: 'type1'
        };

        var info = {
            colsInfo: colsInfo,
            paging: paging
        };

        if(result.code == "0") {
            var totalCnt = result.data.totalCnt;
            paging.totalCnt = totalCnt;

            if(totalCnt == 0){
                tableType1("#list", null, info );
            } else {
                tableType1("#list", result.data.list, info);
            }
        } else {
            tableType1("#list", null, info );
        }

        $('.searchBox span').text('총 '+ totalCnt +'건의 데이터가 검색되었습니다');

        $("#viewList input[name=sortNo]").each(function(idx, obj){
            var $obj = $(obj);
            $("#list input[name=chk][value="+$obj.data("seq")+"]").prop("checked", true);
        });

        if(typeof callback == "function"){
            callback();
        }

    }, function(){

    });
}

function goT(imgPath){
    var imageWin = window.open("", "", "top=" + 0 + ",left=" + 0 + ",width=" + 800 + ",height=" + 380 +",scrollbars=yes,resizable=yes,status=no");
    imageWin.document.write("<html><title>메인팝업</title><body>");
    imageWin.document.write("<img src='" + imgPath + "' width='100%'>");
    imageWin.document.write("</body></html>");
}


var viewYnControl = function(obj){
    var $this = $(obj);
    var isChk = $this.is(":checked");
    var $tableObj = $("#viewList tbody");
    if(isChk){
        if($tableObj.find("tr").length == 5){
            $this.prop("checked", false);
            alert("메인 노출은 5개까지 가능합니다.")
            return;
        }

        var $htmlObj = $(template());
        var $bannerListTd = $this.parents("tr").find("td");

        $htmlObj.find("td").eq(0).find("input").attr("data-seq", $this.val());
        //$htmlObj.find("td").eq(1).text($bannerListTd.eq(2).text()); //배너타입
        $htmlObj.find("td").eq(1).text($bannerListTd.eq(2).text()); //배너명
        $htmlObj.find("td").eq(2).text($bannerListTd.eq(3).text()); //노출기간

        $(".nodata").remove();
        $tableObj.append($htmlObj[0]);
    } else {
        $tableObj.find("[data-seq="+$this.val()+"]").parents("tr").remove();
    }
}

var template = function(){
    var html = "<<tr>" +
        "<td><input type='text' name='sortNo' style='width: 50px;' maxlength='1' /></td> " +
        "<td></td>" +
        "<td></td>" +
        //"<td></td>" +
        "</tr>";
    return $.parseHTML( html);
}

var getViewList = function(callback){
    var colsInfo = [
        {no: 1, col: '', name: '노출순서',width:'80px', custom:function(obj){
            return "<input type='text' name='sortNo' style='width: 50px;' maxlength='1' value='"+obj.sortNo+"' data-seq='"+obj.bannerSeq+"' />";
        }}
        /*, {no: 2, col: 'typeNm', name: '배너타입'}*/
        , {no: 3, col: 'bannerNm', name: '배너명'}
        , {no: 4, col: '', name: '노출기간',width:'200px',custom:function (obj) {
            if(obj.viewUnlimitYn == "Y"){
                return "무제한"
            } else {
                return obj.viewStDt+' ~ '+obj.viewEndDt;
            }
        }}
    ];

    tms.ajaxGetHelper(bannerUrl.viewList, null, null, function(result) {
        var info = {
            colsInfo: colsInfo
        };

        if(result.code == "0") {
            if(result.data.list.length == 0){
                tableType1("#viewList", null, info);
                $("#viewList tbody tr").addClass("nodata");
            } else {
                tableType1("#viewList", result.data.list, info);
            }
        } else {
            tableType1("#viewList", null, info );
        }

        if(typeof callback == "function"){
            callback();
        }
    }, function(){ });
}

//노출 정렬 저장
var viewSave = function(){
    var sortArry = new Array();
    var $inputObj = $("#viewList input[name=sortNo]");
    var flag = 0;
    $inputObj.each(function(idx, obj){
        var keyObj = {viewYn: 'Y'}
        var $this = $(obj);
        keyObj.bannerSeq = $this.data("seq");
        keyObj.sortNo = $this.val();
        sortArry.push(keyObj);

        if(tms.isEmpty(keyObj.sortNo) || keyObj.sortNo > 5){
            flag = 1;
            return;
        }

        if(flag < 1){
            $inputObj.each(function(idx2, obj2){
                if(keyObj.bannerSeq != $(obj2).data("seq") && $this.val() == $(obj2).val()){
                    flag = 2;
                    return;
                }
            });
        }
    });

    if(flag == 1){
        alert("노출순서를 확인해주세요. \n노출순서는 5번까지 입력가능합니다.")
        return;
    } else if(flag == 2) {
        alert("중복된 번호 입니다.");
        return;
    }

    tms.ajaxPutHelper(bannerUrl.sort, JSON.stringify({sortArry: sortArry}), null, function(res){
        alert("노출순서가 저장되었습니다.")
    });
}