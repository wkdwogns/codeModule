$(function() {

    /* 로그인 버튼 클릭 */
    $("#btnLogin").on("click", function(){
        /* 벨리데이션 필수체크 */
        var message = {
            required: {
                userId: "아이디를 입력해주세요",
                userPwd: "비밀번호를 입력해주세요"
            }
        };

        var valid = tmsValidation.validInputChk("#frm", message, function(errorMsg){
            $(".error_box span").text(errorMsg).css({'visibility': 'visible'});
        });

        if(!valid){
            return false;
        }


        /* 로그인 액션 */
        var option = {
            ContentType: 'application/x-www-form-urlencoded; charset=UTF-8'
        }

        var checked = $("input:checkbox[id='login_save']").is(":checked");
        var forever = "";
        if(checked) {
            forever = "Y";
        } else {
            forever = "N";
        }

        var params = {
            userId: $("#userId").val()
            , userPwd: $("#userPwd").val()
            , forever: forever
        };

        tms.ajaxPostHelper("/mapi/login", JSON.stringify(params), null, function(result){

            if(result.code == 0) {
                location.href='/page/admin/notice/list';
            } else if(result.code == 226){
                alert("이메일 미인증");
            } else {
                alert("아이디 또는 비밀번호가 일치하지 않습니다.\n다시한번 확인해주세요");
            }
        }, function(e){
            alert("아이디 또는 비밀번호가 일치하지 않습니다.\n다시한번 확인해주세요");
        });
    });

});