var seq = tms.getParameterByName("info");
var message = {
    required: {
        bannerNm: '배너 명을 입력해주세요',
        bannerType: '배너타입을 선택해주세요',
        viewDt: '노출기간을 입력해주세요',
        img: '이미지를 등록해주세요'
    }
}

var bannerUrl = {
      insert: "/mapi/banner"
    , update: "/mapi/banner/edit"
    , delete: "/mapi/banner"
    , detail: "/mapi/banner/detail"
    , pageList: "/page/admin/bannerT/list"
}

$(document).ready(function () {
    //영역 노출처리
    $(".control").hide();
    $(".type1").show();

    //공통코드 매핑
    getComCode(function(res){
        var obj = res.data.comCodeList;
        var html = '';
        obj.forEach(function(obj, idx){
            html += "<option value='"+obj.dtlCd+"'>"+obj.dtlNm+"</option>";
        })
        $("select[name=bannerType]").append(html);


        //상세조회
        if(tms.isNotEmpty(seq)){
            detail(seq);
        }else{
            $('.devCssBtnDel').remove();
        }
    });


    /* 달력세팅 */
    $('.dt').datetimepicker({
        dateFormat: "yy-mm-dd"
        ,onClose: function() {
            $('.note-toolbar').css({'zIndex':500});
        }
        ,beforeShow:function () {
            $('.note-toolbar').css({'zIndex':0});
        }
    });

    /* 배너타입별 컨트롤 */
    $("select[name=bannerType]").on("change", function(){
        var value = $(this).val();
        inputControl(value);
    });

    /* 무제한 버튼 세팅 */
    $("#chk").on("change", function(){
        var isChk = $(this).is(":checked");
        inputPeriodControl(isChk);
    });

    /* 취소버튼 */
    $('.devCssBtnCancel').click(function(){
        location.href= bannerUrl.pageList;
    });

    /* 이미지 등록 */
    $('#devIdTxtImg').change(function (e) {
        fileCheck(e.target,'onlyImg');
    });

    /* 삭제하기 */
    $('.devCssBtnDel').click(function () {
        if( confirm('삭제하시겠습니까?') ){

            tms.ajaxDeleteHelper(bannerUrl.delete, JSON.stringify({bannerSeq:seq}), null, function(rs) {
                if(rs.code==0){
                    alert('삭제되었습니다.');
                    location.href= bannerUrl.pageList;
                    return ;
                }else{
                    alert('오류가 있습니다.');
                }
            });
        }
    });


    /* 저장하기 */
    $("#btnSave").click(function(){
        /* 벨리데이션 필수체크, 형식체크 */
        var valid = tmsValidation.validInputChk('#frm', message);
        if(!valid){
            return false;
        }

        var stDt = $(".dt").eq(0).val();
        var endDt = $(".dt").eq(1).val();
        if(tms.isNotEmpty(stDt) && tms.isNotEmpty(endDt)){
            stDt = stDt.replace(/\-|\s|\:/g,'');
            endDt = endDt.replace(/\-|\s|\:/g,'');

            if(stDt > endDt){
                alert("시작일은 작성일보다 이전일 수 없습니다.");
                return;
            }
        }

        if(!confirm("저장하시겠습니까?")) {
            return;
        }

        var form = $('#frm')[0];
        var data = new FormData(form);
        var f = $('#devIdTxtImg')[0].files[0];

        if(f != null && f.filename != ""){
            data.append("image", f );
        }

        var url = bannerUrl.insert;
        if(tms.isNotEmpty(seq)){
            url = bannerUrl.update
            data.append("bannerSeq", seq );
        }

        tms.ajaxMulitpartHelper(url, data, null, function(rs){
            if(rs.code == 0){
                alert("저장되었습니다")
                location.href = bannerUrl.pageList;
            } else if(rs.code == 450){
                alert("파일은 이미지 형식으로 등록해주세요.");
            } else {
                alert("저장중 에러가 발생하였습니다");
            }
        }, function(){
            alert("저장중 에러 발생");
        });
    });

});

var detail = function(info){
    var params = {
        bannerSeq:info
    }
    tms.ajaxGetHelper(bannerUrl.detail, params, null, function(rs) {
        if(rs.code === 0){
            var d = rs.data;
            tmsInput.detailMapping(d, "#frm");
            inputControl(d.bannerType);
            inputPeriodControl((d.viewUnlimitYn == "Y")?true:false);

            if(d.bannerType != bannerTypeCode.type3){
                $("#devIdTxtImg").removeAttr("data-required");
            }

            var fList = d.flist;

            for(var i in fList){
                var sq = fList[i].fileGrpSeq;
                var fNm = fList[i].orgFileNm;
                var imgPath = fList[i].filePath;
                //var img = $('<input type="hidden" id="attachSeq" name="fileGrpSeq" value="'+sq+'" /><a href="/api/file/download?fileSeq='+sq+'&category=0">'+fNm+'</a>');
                var img = $('<input type="hidden" id="attachSeq" name="fileGrpSeq" value="'+sq+'" /> <div><img src="'+imgPath+'" alt="배너 이미지" width="500" /></div>');
                $('#pictureList').append(img);
            }
        }
    }, function(){});
}


var getComCode = function(callback){
    tms.ajaxGetHelper("/api/com/code", {mstCdArr: "002"}, null, function(res){
        if(typeof callback == "function"){
            callback(res);
        }
    });
}


var inputControl = function(value){
    var $img = $("#devIdTxtImg");
    $(".control").hide();
    $img.attr("data-required", "img");
    if(value == bannerTypeCode.type3){
        $(".type3").show();
        tms.initInputTxt(".control:not(.type3)");
        $img.removeAttr("data-required");
    } else if(value == bannerTypeCode.type2){
        $(".type2").show();
        tms.initInputTxt(".control:not(.type2)");
    } else {
        $(".type1").show();
        tms.initInputTxt(".control:not(.type1)");
    }
}

var inputPeriodControl = function(isChk){
    var $dtObj = $(".dt");
    if(isChk){
        $dtObj.val("")
        $dtObj.prop("readonly", false)
        $dtObj.prop("disabled", true);
        $dtObj.removeAttr("data-required");
    } else {
        $dtObj.prop("disabled", false);
        $dtObj.prop("readonly", true);
        $dtObj.attr("data-required", "viewDt");
    }
}
