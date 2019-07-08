/*
* 해당 js는 validation을 위한 모음 입니다.
* */
var tmsValidation = (function ($, win, doc) {
    var validRegex = {
          id: ''
        , pwd: ''
        , email: ''
        , tel: ''
        , nickName: ''
    }

    /* validation 문구를 지정함 */
    var message = {
        required: {
              id: '아이디를 입력해주세요.'
            , password: i18next.t('common:msg.required.pwd')
            , tel: i18next.t('common:msg.required.tel')
            , selbox: ' 선택해주세요.'
            , textarea: '내용을 입력해주세요.'
            , check: '체크해주세요.'
        },
        validation: {
            id: {
                1: i18next.t('common:msg.validation.id.chk1')
                , 2: i18next.t('common:msg.validation.id.chk2')
                , 3: i18next.t('common:msg.validation.id.chk3')
            },
            password: {
                1: i18next.t('common:msg.validation.password.chk1')
                , 2:i18next.t('common:msg.validation.password.chk2')
                , 3:i18next.t('common:msg.validation.password.chk2')
            },
            email: {
                  1: i18next.t('common:msg.validation.id.chk3')
                , 2: i18next.t('common:msg.validation.id.chk3')
            },
            tel: {
                  1: '-를 포함하여 번호를 입력해주세요.'
                , 2: i18next.t('common:msg.validation.id.chk1')
            },
            nick: {
                1: i18next.t('common:msg.validation.nick.chk1')
                ,2: i18next.t('common:msg.validation.nick.chk2')
            }
        }
    }

    /* validation 결과 메시지 노출 */
    var messageAlert = function(code, msgObj){
        msgObj = msgObj || {};
        var codeArray = code.split('.');
        if((typeof codeArray[2] === 'undefined')){
            message[codeArray[0]][codeArray[1]] = typeof msgObj[codeArray[0]][codeArray[1]] !== 'undefined' ?
                 msgObj[codeArray[0]][codeArray[1]] : message[codeArray[0]][codeArray[1]];
            alert(message[codeArray[0]][codeArray[1]]);
        } else {
            message[codeArray[0]][codeArray[1]][codeArray[2]] = typeof msgObj[codeArray[0]][codeArray[1]][codeArray[2]] !== 'undefined' ?
                msgObj[codeArray[0]][codeArray[1]][codeArray[2]] : message[codeArray[0]][codeArray[1]][codeArray[2]];
            alert(message[codeArray[0]][codeArray[1]][codeArray[2]]);
        }
        layer_OPEN(".popup_alert_confirm_type1");
    }

    /* 필수입력항목 체크 */
    var requiredChk = function(parentNode, msgObj, customFn){
        var returnFlag = true;
        var obj;
        if(typeof parentNode !== 'undefined'){
            obj = $(parentNode).find('[data-required]');
        } else {
            obj = $('[data-required]');
        }

        obj.each(function(){
            var $this = $(this);
            if($this.attr('type') == 'checkbox'){
                //checkbox일때
                if(!$this.is(":checked")) returnFlag = false;
            }

            if(jQuery.trim($this.val()) == "") returnFlag = false;

            if(!returnFlag){
                if (typeof customFn == 'function') {
                    customFn('required.'+$this.data('required'));
                } else {
                    messageAlert('required.'+$this.data('required'), msgObj);
                    $this.focus();
                }
                returnFlag = false;
                return returnFlag;
            }
        });

        return returnFlag;
    }

    /* 정규식에 따른 입력 체크(config설정 정규식) */
    var validInputChk = function(parentNode, msgObj, customFn){
        var returnFlag = requiredChk(parentNode, msgObj, customFn);

        if(!returnFlag){
            return returnFlag;
        }

        var obj;
        if(typeof parentNode !== 'undefined'){
            obj = $(parentNode).find('input[type=email], input[type=tel], [data-valid]');
        } else {
            obj = $('input[type=email], input[type=tel], [data-valid]');
        }

        obj.each(function(){
            var code = 0;
            var $this = $(this);
            var $type = $this.attr('type');

            if($this.attr('id') == "beforePassword") {
                return true;
            }
            $type = (typeof $this.attr('data-valid') !== 'undefined')? $this.data('valid') : $type;

            if(jQuery.trim($this.val()) != ""){
                switch ($type) {
                    case 'password':
                        code = checkPwd($this.val());
                        break;
                    case "email":
                        code = checkEmail($this.val());
                        break;
                    case "tel":
                        code = checkPhone($this.val());
                        break;
                    case "id":
                        code = checkId($this.val());
                        break;

                    case "nick":
                        code = checkNickName($this.val());
                        break;
                    default: '';
                }

                if(code > 0){
                    returnFlag = false;

                    if (typeof customFn == 'function') {
                        customFn('validation.'+$type+'.'+code);
                    } else {
                        messageAlert('validation.'+$type+'.'+code, msgObj);
                        $this.focus();
                    }
                    return returnFlag;
                }
            }
        });

        return returnFlag;
    }

    /* id형식 체크 */
    var checkId = function(id) {
        var idSet = validRegex.id.set;
        if((idSet & 0x01) == 0x01) {
            if(id.match(validRegex.id.length) == null) return 1;
        }
        if((idSet & 0x02) == 0x02) {
            if(id.match(validRegex.id.normal) == null) return 2;
        }
        if((idSet & 0x04) == 0x04) {
            if (id.match(validRegex.id.email) == null) return 3;
        }
        return 0;
    }

    /* 비밀번호 형식 체크 */
    var checkPwd = function(pwd) {
        var pwdSet = validRegex.pwd.set;
        if((pwdSet & 0x01) == 0x01) {
            if(pwd.match(validRegex.pwd.length) == null) return 1;
        }
        if((pwdSet & 0x02) == 0x02) {
            if(pwd.match(validRegex.pwd.capChar) == null) return 2;
        }
        if((pwdSet & 0x04) == 0x04) {
            if(pwd.match(validRegex.pwd.specChar) == null) return 3;
        }
        if((pwdSet & 0x08) == 0x08) {
            if(pwd.match(validRegex.pwd.allChar) == null) return 2;
        }
        if((pwdSet & 0x10) == 0x10) {
            if(pwd.match(validRegex.pwd.digitChar) == null) return 2;
        }
        return 0;
    }

    /* 핸드폰 형식 체크 */
    var checkPhone = function(phone) {
        var phoneSet = validRegex.phone.set;
        if((phoneSet & 0x01) == 0x01) {
            if(phone.match(validRegex.phone.dash) == null) return 1;
        }
        if((phoneSet & 0x02) == 0x02) {
            if(phone.match(validRegex.phone.noDash) == null) return 2;
        }
        return 0;
    }

    var checkEmail = function(email) {
        var emailSet = validRegex.email.set;
        if((emailSet & 0x01) == 0x01) {
            if(email.match(validRegex.email.length) == null) return 1;
        }
        if((emailSet & 0x02) == 0x02) {
            if (email.match(validRegex.email.form) == null) return 2;
        }
        return 0;
    }

    var checkNickName = function(nickname) {
        var nickNameSet = validRegex.nickName.set;
        if((nickNameSet & 0x01) == 0x01) {
            if(nickname.match(validRegex.nickName.length) == null) return 1;
        }
        if((nickNameSet & 0x02) == 0x02) {
            if(nickname.match(validRegex.nickName.specChar) != null) return 2;
        }
        return 0;
    }

    return {
        messageAlert : messageAlert,
        requiredChk: requiredChk,
        validInputChk: validInputChk,
        validRegex: validRegex
    }
}(jQuery, window, document));



$(function(){
    //정규식 형식 가져와서 초기 세팅
    tms.ajaxGetHelper('/validationCheck/getAllRegex', null, null, function(result){
        tmsValidation.validRegex.id = result.idValidInfo;
        tmsValidation.validRegex.pwd = result.pwdValidInfo;
        tmsValidation.validRegex.phone = result.phoneInfo;
        tmsValidation.validRegex.email = result.emailInfo;
        tmsValidation.validRegex.nickName = result.nickNameInfo;
    }, function(e){
        console.log(e+"정규식 error")
    });
});