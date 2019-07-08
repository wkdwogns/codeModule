userInfo.lang = tms.getCookie("defaultLangSet");
setLanguageLoad(userInfo.lang, ['password', 'common'], 'password');

var message = {}
i18next.on('loaded', function(loaded) {
    message = {
        required: {
            userId: i18next.t('common:msg.required.email')
        },
        validation: {
            id: {
                1: i18next.t('common:msg.validation.id.chk1')
                , 2: i18next.t('common:msg.validation.id.chk2')
                , 3: i18next.t('common:msg.validation.id.chk3')
                , 4: i18next.t('common:msg.validation.id.chk4')
            }
        }
    }
});
var findFlag = false;
$(function(){
    /* 비밀번호 찾기 벨리데이션*/
    $("input[name=userId]").on("keyup", function(){
        var valid = findpasswordValid();
        if(!valid){
            return false;
        }

        $(".findPwdBtn").addClass("on");
    });

    
    /* 비밀번호 찾기 버튼 클릭*/
    $(".findPwdBtn").click(function(){
        if(!$(this).hasClass("on")){return;}
        findPassword();
    });

    /* 비밀번호 찾기 후 처리 */
    $(document).on("click", ".btn_confirm", function(){
        if(findFlag){
            location.href = urlInfo.login
        }
    });

});

$(window).load(function(){
    fn_resister();
});

var findpasswordValid = function(){
    $("#findError").removeClass("error");
    var valid = tmsValidation.validInputChk('#frm', message, function(res){
        console.log(res);
        var codeArray = res.split('.');
        var msg = message[codeArray[0]][codeArray[1]];
        if(codeArray[0] ==  "validation"){
            msg = message[codeArray[0]][codeArray[1]][codeArray[2]];
        }
        $("#findError").addClass("error").find("span").text(msg);
    });

    var data = {
        "emailUserOnly": true,
        "userId": $("input[name=userId]").val()
    }

    if(valid){
        if(!checkId(data)){
            $("#findError").addClass("error").find("span").text(message['validation']['id'][4]);
            valid = false;
        }
    }

    return valid;
}

var findPassword = function() {

    var valid = findpasswordValid();
    if(!valid) {
        return false;
    }

    var data = {
        userId: $("input[name=userId]").val()
    };

    var option = {
        showLoding: true
        , showLodingMsg: ""
    }

    tms.ajaxGetHelper(apiHost+"/api/membership/find/pwd", data, option, function(rs){
        if(rs.code == 0){
            $("#popupTxt").html(i18next.t('msg.send'));
            layer_OPEN(".popup_alert_confirm_type1");
            findFlag = true;
        }

    }, function(e){
        console.log("code:"+e.status+"message:"+e.responseText)
    });
}

var checkId = function(data){
    var flag = true;
    var option = {
        async:false
    }
    tms.ajaxPostHelper(apiHost+"/api/membership/check/existing/field", JSON.stringify(data), option, function(rs){
        if(rs.code == 0 ){
            flag = rs.data.existing;
        }
    }, function(e){
        console.log("code:"+e.status+"message:"+e.responseText)
    });
    return flag;
}

var fn_resister = function (){
    var mail_input_obj = $('.resi_mail input');

    mail_input_obj.bind('focusout',function(){
        if ($(this).val() != ""){
            $(this).parent().addClass('on');
        }else {
            $(this).parent().removeClass('on');
        }
    });

    mail_input_obj.parent().find('.btn_delete').bind('click',function(){
        $(this).parent().removeClass('on');
        $(this).parent().find('input').val("");
        $(this).parent().find('input').focus();
    });
};