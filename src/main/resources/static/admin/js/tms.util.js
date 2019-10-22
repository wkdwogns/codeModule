/*
 * 해당 js는 javascript util 모음 입니다.
 * 목록
 * 1. isEmpty: 공백 체크
 * 2. isNotEmpty: 비어있지 않음 체크
 * 3. LPad: 자리수 채워지지 않았을 경우 특정문자로 자리수 채우기(왼쪽)
 * 3. RPad: 자리수 채워지지 않았을 경우 특정문자로 자리수 채우기(오른쪽)
 * 4. setComma: 세자리수마다 콤마(,)표시
 * 5. removeComma: 문자열 콤마(,)삭제
 * 6 currentDate: 현재 날짜 가져오기
 *
 *
 * ****jquery 필수 *****
 * initInputTxt: input box초기화
 * checkboxControl
 * checkboxAllControl
 * 8. ajaxPostHelper: ajax post방식 호출
 * 9. ajaxGetHelper: ajax get방식 호출
 *
 * */
var tms = (function ($, win, doc) {

    var isEmpty = function(str){
        if (typeof str === 'undefined' || str == null || !str || str.length === 0 || str === "" || !/[^\s]/.test(str) || /^\s*$/.test(str) || ( str != null && typeof str == "object" && !Object.keys(str).length )) {
            return true;
        } else {
            return false;
        }
    }
    var isEmptyStr = function(str){
        if (typeof str === 'undefined' || str == null || !str || str.length === 0 || str === "" || !/[^\s]/.test(str) || /^\s*$/.test(str) || ( str != null && typeof str == "object" && !Object.keys(str).length )) {
            return "";
        } else {
            return str;
        }
    }
    var isNotEmpty = function(str){
        return !isEmpty(str);
    }
    var LPad = function(obj, charator, num){
        var str = obj+"";
        if (! str || ! charator || str.length >= num) {
            return str;
        }

        var max = (num - str.length)/charator.length;
        for (var i = 0; i < max; i++) {
            str = charator + str;
        }

        return str;
    }
    var RPad = function(obj, charator, num){
        var str = obj+"";
        if (! str || ! charator || str.length >= num) {
            return str;
        }

        var max = (num - str.length)/charator.length;
        for (var i = 0; i < max; i++) {
            str += charator;
        }

        return str;
    }
    var setComma = function(obj){
        return obj.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    }
    var removeComma = function(obj){
        return obj.replace(/,\s?/g, "");
    }
    var dateFormat = function(dt, format){
        var d = dt;
        var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
        return format.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
            switch ($1) {
                case "yyyy": return d.getFullYear();
                case "yy": return (LPad(d.getFullYear() % 1000), '0', 2);
                case "MM": return LPad(d.getMonth() + 1, '0', 2);
                case "dd": return LPad(d.getDate(), '0', 2);
                case "E": return weekName[d.getDay()];
                case "HH": return LPad(d.getHours(), '0', 2);
                case "hh": return (h = d.getHours() % 12) ? LPad(h, '0', 2) : 12;
                case "mm": return LPad(d.getMinutes(), '0', 2);
                case "ss": return LPad(d.getSeconds(), '0', 2);
                case "a/p": return d.getHours() < 12 ? "오전" : "오후";
                default: return $1;
            }
        });
    }
    var currentDate = function (format) {
        if(this.isEmpty(format)){
            format = 'yyyy-MM-dd';
        }
        return this.dateFormat(new Date(), format);
    }

    var isIe = function(){
        var agent = navigator.userAgent.toLowerCase();
        if (agent.indexOf("msie") != -1) {
            return true;
        }
        return false;
    }

    var initInputTxt = function(parentNode){
        var obj = $("input[type=text], input[type=number], input[type=tel], input[type=email], textarea, select, input[type=file], input[type=password]");
        var fileObj = $("input[type=file]");
        var checkObj = $("input[type=checkbox], input[type=radio]");
        if(typeof parentNode !== 'undefined'){
            obj = $(parentNode).find("input[type=text], input[type=number], input[type=tel], input[type=email], textarea, select, input[type=file], input[type=password]");
            fileObj = $(parentNode).find("input[type=file]"); //ie대응
            checkObj = $(parentNode).find("input[type=checkbox], input[type=radio]");
        }
        obj.val("");
        checkObj.prop('checked', false);

        if(isIe()){
            fileObj.replaceWith(fileObj.clone(true));
        }
    }
    var checkboxControl = function(allObj, subObj){
        var size = $(subObj).size();
        var checkSize = $(subObj+":checked").length;
        if ($(allObj).is(":checked")) {
            if(size != checkSize){
                $(allObj).prop("checked", false);
            }
        } else {
            if(size == checkSize){
                $(allObj).prop("checked", true);
            }
        }
    }
    var checkboxAllControl = function(allObj, subObj){
        if ($(allObj).is(":checked")) {
            $(subObj).prop("checked", true);
        } else {
            $(subObj).prop("checked", false);
        }
    }

    var confirmMsg = function(msg, okFunc, noFunc){
        if (confirm(msg)) {
            if (typeof okFunc == 'function') {
                okFunc();
            }
        } else {
            if (typeof noFunc == 'function') {
                noFunc();
            }
        }
    }

    var isCookie = function(nm){
        var gookieNm = getCookie(nm);
        if (gookieNm != "") {
            return true;
        } else {
            return false;
        }
    }

    var getCookie = function(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

    var setCookie = function(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires="+d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }

    var ajaxDefaults = {
        async:true
        , dataType: 'json'
        , ContentType: 'application/json;charset=UTF-8'
        , showLoding: false
        , showLodingMsg: ''
    }

    var getArray = new Array();
    var postArray = new Array();
    var putArray = new Array();
    var deleteArray = new Array();

    var urlCheck = function(url, array){
        var idx = $.inArray(url, array);
        if(idx != -1) {
            return false;
        }
        array.push(url);
        return idx;
    }

    var ajaxHelper = function(url, type, data, options, beforeFunc, sucessFunc, errorFunc){
        var checkArry = new Array();
        /* 이중 클릭을 막기위한 처리 */
        switch (type.toLocaleString()) {
            case "POST" : checkArry = postArray
                break;
            case "GET": checkArry = getArray;
                break;
            case "PUT": checkArry = putArray;
                break;
            case "DELETE" : checkArry = deleteArray;
                break;
        }

        var idx = urlCheck(url, checkArry);
        if(!idx){
            return;
        }

        options = options || {};
        for (var prop in ajaxDefaults)  {
            options[prop] = typeof options[prop] !== 'undefined' ? options[prop] : ajaxDefaults[prop];
        }

        $.ajax({
            url: url,
            type: type,
            dataType : options.dataType,
            async : options.async,
            contentType: options.ContentType,
            xhrFields: {withCredentials: true},
            data: data,
            cache: true,
            crossdomain:true,
            headers: {
                'content-type' : options.ContentType
            },
            beforeSend: function(){
                if(options.showLoding){
                    showLodingPopup();
                }
                if (typeof beforeFunc == 'function') {
                    beforeFunc();
                }
            },
            success: function(result){
                if (typeof sucessFunc == 'function') {
                    sucessFunc(result);
                }
                checkArry.splice(idx, 1);
                if(options.showLoding) {
                    hideLodingPopup();
                }
            },
            error: function (jqXHR, textStauts, errorThrown) {
                checkArry.splice(idx, 1);
                hideLodingPopup();

                if (typeof errorFunc == 'function') {
                    errorFunc(jqXHR, textStauts, errorThrown);
                } else {
                    // if (jqXHR.status === 0) {
                    //     location.href = errorUrl.network
                    // }
                    // else if (jqXHR.status == 400) {
                    //     location.href = errorUrl.com+"?code=400&msg=Server understood the request, but request content was invalid"
                    // }
                    // else if (jqXHR.status == 401) {
                    //     location.href = errorUrl.com+"?code=401&msg=Unauthorized access."
                    // }
                    // else if (jqXHR.status == 403) {
                    //     location.href = errorUrl.com+"?code=403&msg=Forbidden resource can not be accessed."
                    // }
                    // else if (jqXHR.status == 404) {
                    //     location.href = errorUrl.notFound
                    // }
                    // else if (jqXHR.status == 500) {
                    //     //location.href = errorUrl.internal
                    // }
                    // else if (jqXHR.status == 503) {
                    //     //location.href = errorUrl.internal
                    // }
                    // else if (errorThrown === 'parsererror') {
                    //     location.href = errorUrl.com+"?code=Failed&msg=Requested JSON parse failed"
                    // }
                    // else if (errorThrown === 'timeout') {
                    //     location.href = errorUrl.com+"?code=Timeout&msg=Time out error."
                    // }
                    // else if (errorThrown === 'abort') {
                    //     location.href = errorUrl.com+"?code=Aborted&msg=Ajax request aborted."
                    // }
                    // else {
                    //     //location.href = errorUrl.internal
                    // }
                }
            }
        });
    }

    var ajaxMulitpartHelper = function(url, data, options, sucessFunc, errorFunc){

        var idx = urlCheck(url, postArray);
        if(!idx){
            return false;
        }



        // var idx = $.inArray(url, aFlagArray);
        // if(idx != -1) {
        //     return false;
        // }

        //postArray.push(url);

        options = options || {};
        for (var prop in ajaxDefaults)  {
            options[prop] = typeof options[prop] !== 'undefined' ? options[prop] : ajaxDefaults[prop];
        }

        if(options.showLoding){
            showLodingPopup();
        }

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            url: url,
            data: data,
            xhrFields: {withCredentials: true},
            processData: false,
            contentType: false,
            cache: false,
            timeout: 900000,
            success: function (result) {
                if (typeof sucessFunc == 'function') {
                    sucessFunc(result);
                }
                postArray.splice(idx, 1);
                hideLodingPopup();
            },
            error: function(jqXHR, textStauts, errorThrown) {
                postArray.splice(idx, 1);
                if (typeof errorFunc == 'function') {
                    errorFunc(jqXHR, textStauts, errorThrown);
                }
                hideLodingPopup();
            }
        });
    }
    var ajaxPostHelper = function(url, data, option, sucessFunc, errorFunc) {
        ajaxHelper(url, 'POST', data, option, null, sucessFunc, errorFunc);
    }
    var ajaxGetHelper = function(url, data, option, sucessFunc, errorFunc){
        ajaxHelper(url, 'GET', data, option, null, sucessFunc, errorFunc);
    }
    var ajaxPutHelper = function(url, data, option, sucessFunc, errorFunc) {
        ajaxHelper(url, 'PUT', data, option, null, sucessFunc, errorFunc);
    }
    var ajaxDeleteHelper = function(url, data, option, sucessFunc, errorFunc) {
        ajaxHelper(url, 'DELETE', data, option, null, sucessFunc, errorFunc);
    }
    var ajaxPostBeforeHelper = function(url, data, option, beforeSend, sucessFunc, errorFunc){
        ajaxHelper(url, 'POST', data, option, beforeSend, sucessFunc, errorFunc);
    }

    var serializeData = function(formObj) {
        var dataObj = {};
        var $this = formObj;
        try {
            if($this[0].tagName && $this[0].tagName.toUpperCase() == "FORM" ) {
                var valArray = $this.serializeArray();
                if(valArray){
                    $.each(valArray, function(key, obj) {
                        dataObj[obj.name] = obj.value;
                    });
                }
            }
        }catch(e) {
            alert(e.message);
        }finally  {}
        return dataObj;
    };

    var showLodingPopup = function(){
        var hg = window.innerHeight/2-100;
        var html = '<div class="popup loading tmsDimPop" style="display: block"> <div class="bg white"></div> <div class="loader"></div></div>';
        $(document).find("body").append(html);

        //$(document).find(".tmsDimPop").show();
    }
    var hideLodingPopup = function(msg){
        $(document).find(".tmsDimPop").hide();
    }

    var getbrowser = function(){
        var agent = navigator.userAgent.toLowerCase(),
            name = navigator.appName,
            browser;

        // MS 계열 브라우저를 구분하기 위함.
        if(name === 'Microsoft Internet Explorer' || agent.indexOf('trident') > -1 || agent.indexOf('edge/') > -1) {
            browser = 'ie';
            if(name === 'Microsoft Internet Explorer') { // IE old version (IE 10 or Lower)
                agent = /msie ([0-9]{1,}[\.0-9]{0,})/.exec(agent);
                browser += parseInt(agent[1]);
            } else { // IE 11+
                if(agent.indexOf('trident') > -1) { // IE 11
                    browser += 11;
                } else if(agent.indexOf('edge/') > -1) { // Edge
                    browser = 'edge';
                }
            }
        } else if(agent.indexOf('safari') > -1) { // Chrome or Safari
            if(agent.indexOf('opr') > -1) { // Opera
                browser = 'opera';
            } else if(agent.indexOf('chrome') > -1 || agent.indexOf('crios') > -1) { // Chrome
                browser = 'chrome';
            } else { // Safari
                browser = 'safari';
            }
        } else if(agent.indexOf('firefox') > -1) { // Firefox
            browser = 'firefox';
        }

        return  browser;
    }

    var isMobile = {
        Android: function () {
            return navigator.userAgent.match(/Android/i) == null ? false : true;
        },
        BlackBerry: function () {
            return navigator.userAgent.match(/BlackBerry/i) == null ? false : true;
        },
        IOS: function () {
            return navigator.userAgent.match(/iPhone|iPad|iPod/i) == null ? false : true;
        },
        Opera: function () {
            return navigator.userAgent.match(/Opera Mini/i) == null ? false : true;
        },
        Windows: function () {
            return navigator.userAgent.match(/IEMobile/i) == null ? false : true;
        },
        any: function () {
            return (isMobile.Android() || isMobile.BlackBerry() || isMobile.IOS() || isMobile.Opera() || isMobile.Windows());
        }
    };

    var convertHtml = function(str){
        if(str == null)
            return null;

        //특수문자
        var returnStr = str;
        returnStr = returnStr.replace(/<br>/g,"\n");
        returnStr = returnStr.replace(/&gt;/g,">");
        returnStr = returnStr.replace(/&lt;/g,"<");
        returnStr = returnStr.replace(/&quot;/g,"\"");
        returnStr = returnStr.replace(/&apos;/g,"\'");
        returnStr = returnStr.replace(/&nbsp;/g," ");
        returnStr = returnStr.replace(/&amp;/g, "&");

        return returnStr;

    }

    /*
     * html 로딩
     * obj: 붙여넣을 위치 param
     * path: include 경로
     * callBack: callback 함수
     * */
    var includeHtml = function(obj, path, callBack){
        $(obj).load(path, null, function(){
            if(typeof callBack == "function"){
                callBack();
            }
        });

    }

    var getParameterByName = function(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }

    var toFixed = function(no, fixed){
        no = Number(no);
        return no.toFixed(fixed)
    }

    var checkFileExtensionImage = function(fileName) {
        if(tms.isNotEmpty(fileName)) {
            var fileLen = fileName.length;
            var lastIdx = fileName.lastIndexOf(".");
            var fileExt = fileName.substring(lastIdx +1, fileLen).toLowerCase();

            if(fileExt != "jpg" && fileExt != "png" && fileExt != "gif" && fileExt != "jpeg") {
                return false;
            } else {
                return true;
            }
        }
    };

    var checkFileSize = function(size, fileObj) {
        var limitSize = 1024 * 1024 * size; //size MB체크
        if(fileObj.size > limitSize) {
            return false;
        } else {
            return true;
        }
    };

    var checkFileListSize = function(size, fileList) {
        var limitSize = 1024 * 1024 * size; //size MB체크
        var totalSize = 0;
        for(var i in fileList) {
            var file = fileList[i];
            totalSize += file.size;
        }

        if(totalSize > limitSize) {
            return false;
        } else {
            return true;
        }
    };

    return {
        isEmpty: isEmpty,
        isEmptyStr: isEmptyStr,
        isNotEmpty: isNotEmpty,
        LPad: LPad,
        RPad: RPad,
        setComma: setComma,
        removeComma: removeComma,
        dateFormat: dateFormat,
        ajaxPostHelper : ajaxPostHelper,
        ajaxGetHelper : ajaxGetHelper,
        ajaxPutHelper: ajaxPutHelper,
        ajaxDeleteHelper: ajaxDeleteHelper,
        ajaxPostBeforeHelper: ajaxPostBeforeHelper,
        ajaxMulitpartHelper: ajaxMulitpartHelper,
        currentDate: currentDate,
        initInputTxt: initInputTxt,
        confirmMsg: confirmMsg,
        checkboxControl: checkboxControl,
        checkboxAllControl: checkboxAllControl,
        serializeData: serializeData,
        isCookie: isCookie,
        getCookie: getCookie,
        setCookie: setCookie,
        getbrowser: getbrowser,
        isMobile: isMobile,
        convertHtml: convertHtml,
        includeHtml:includeHtml,
        getParameterByName:getParameterByName,
        toFixed:toFixed,
        showLodingPopup:showLodingPopup,
        checkFileExtensionImage:checkFileExtensionImage,
        checkFileSize:checkFileSize,
        checkFileListSize:checkFileListSize
    }
}(jQuery, window, document));
