
var webHost = "";
var apiHost = "";
var frontWebHost = "";
var hostName = location.hostname;
if(hostName.indexOf("admin.plathome.com.vn") >= 0) {
    webHost = "https://admin.plathome.com.vn";
    apiHost = "https://admin.plathome.com.vn";
    frontWebHost = "https://m.plathome.com.vn";
} else if(hostName.indexOf("test.plathome.com.vn") >= 0) {
    webHost = "https://test.plathome.com.vn";
    apiHost = "https://test.plathome.com.vn";
    frontWebHost = "https://test.plathome.com.vn";
} else if(hostName.indexOf("local.plathome.com.vn") >= 0) {
    webHost = "http://local.plathome.com.vn";
    apiHost = "http://local.plathome.com.vn:5000";
    frontWebHost = "http://local.plathome.com.vn";
} else {
    webHost = "http://localhost:8080";
    apiHost = "http://local.plathome.com.vn:8090";
    frontWebHost = "http://local.plathome.com.vn:8010";
}

var pageExtension = ".html";

var urlInfo = (function ($, win, doc) {
    var loginUrl = "/page/admin/login";
    var findPwd  = "/admin/account/findPwd.html";
    var userManagerUrl = "/page/admin/userpartner/user";
    var myProfileUrl = "/admin/membership/profile.html";
    var editProfileUrl = "/admin/membership/editProfile.html";
    var adminManagerUrl = "/page/admin/userpartner/admin";
    var userProfileUrl = "/page/admin/userpartner/profile";
    var adminProfileUrl = "/admin/userpartner/adminProfile.html";
    var adminEditProfileUrl = "/admin/userpartner/adminEditProfile.html";
    var adminSignUpProfileProfileUrl = "/admin/userpartner/adminSignUpProfile.html";
    var centerInfoUrl = "/admin/center/info.html";
    var centerEditUrl = "/admin/center/edit.html";
    var estimateListUrl = "/admin/estimate/list.html";
    var estimateInfoUrl = "/admin/estimate/info.html";
    var estimateWrite = "/admin/estimate/write.html";
    var feedListUrl = "/admin/contents/feed.html";
    var logListUrl = "/admin/cms/log.html";
    var filterInfoUrl = "/admin/cms/filter.html";
    var magazine = "/admin/contents/magazine.html";
    var magazineWrite = "/admin/contents/magazineWrite.html";
    var magazineInfo = "/admin/contents/magazineInfo.html";
    var magazinePreview = "/mobile/admin/magazine.html";
    var partnersInfoUrl = "/admin/partners/list.html";
    var partnerDetailUrl = "/admin/partners/detail.html";
    var banner = "/admin/contents/bannerT.html";
    var bannerWrite = "/admin/contents/bannerWrite.html";
    var bannerInfo = "/admin/contents/bannerInfo.html";
    var mobileFeedDetailUrl = "/mobile/feed/detail.html";
    var portfolioListUrl = "/admin/portfolio/list.html";
    var portfolioDetailUrl = "/admin/portfolio/detail.html";
    var portfolioEditUrl = "/admin/portfolio/edit.html";
    var historyUrl = "/page/admin/history/list";
    var investInfoUrl = "/page/admin/investInfo/list";
    var notice = "/page/admin/applicationForm/list";
    var faq = "/page/admin/faq/list";
    var qna = "/page/admin/qna/list";

    return {
        loginUrl : loginUrl
        , findPwd : findPwd
        , userManagerUrl : userManagerUrl
        , userProfileUrl : userProfileUrl
        , editProfileUrl : editProfileUrl
        , myProfileUrl : myProfileUrl
        , adminManagerUrl : adminManagerUrl
        , adminProfileUrl : adminProfileUrl
        , adminEditProfileUrl : adminEditProfileUrl
        , adminSignUpProfileProfileUrl : adminSignUpProfileProfileUrl
        , centerInfoUrl : centerInfoUrl
        , centerEditUrl : centerEditUrl
        , estimateListUrl : estimateListUrl
        , estimateInfoUrl : estimateInfoUrl
        , estimateWrite : estimateWrite
        , feedListUrl : feedListUrl
        , logListUrl : logListUrl
        , filterInfoUrl : filterInfoUrl
        , magazine : magazine
        , magazineWrite : magazineWrite
        , magazineInfo : magazineInfo
        , magazinePreview : magazinePreview
        , mobileFeedDetailUrl : mobileFeedDetailUrl
        , banner : banner
        , bannerWrite : bannerWrite
        , bannerInfo : bannerInfo
        , partnersInfoUrl : partnersInfoUrl
        , partnerDetailUrl : partnerDetailUrl
        , portfolioListUrl : portfolioListUrl
        , portfolioDetailUrl : portfolioDetailUrl
        , portfolioEditUrl : portfolioEditUrl
        , historyUrl : historyUrl
        , investInfoUrl : investInfoUrl
        , notice : notice
        , faq : faq
        , qna : qna
    }

}(jQuery, window, document));

var userInfo = (function ($, win, doc) {
    var userNicName = "";
    var userName = "";


    return {
        userNicName : userNicName,
        userName : userName
    }

}(jQuery, window, document));

var errorUrl = (function ($, win, doc) {
    var com = "/error/page";
    var network = "/error/unconnect";
    var deny = "/error/deny";
    var notFound = "/error/notFound";
    var internal = "/error/internal";

    return {
        com : com,
        network : network,
        deny: deny,
        notFound: notFound,
        internal: internal
    }

}(jQuery, window, document));

//세션체크
var path = window.location.pathname;
var pathFlag = path.indexOf("/admin/login") == -1 &&  path.indexOf("/account/") == -1
if(pathFlag) {
    tms.ajaxPostHelper('/membership/check/live/session', null, null, function(result){

    }, function(e){

    });
}

$(function(){
    includeHTML();
    //moment.locale("ko");
    //moment.locale("kr");
});

function includeHTML() {

    var z, i, elmnt, file, xhttp;
    /*loop through a collection of all HTML elements:*/
    z = document.getElementsByTagName("*");
    for (i = 0; i < z.length; i++) {
        elmnt = z[i];
        /*search for elements with a certain atrribute:*/
        file = elmnt.getAttribute("w3-include-html");
        if (file) {
            /*make an HTTP request using the attribute value as the file name:*/
            xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4) {
                    if (this.status == 200) {elmnt.innerHTML = this.responseText;}
                    if (this.status == 404) {elmnt.innerHTML = "Page not found.";}
                    /*remove the attribute, and call this function once more:*/
                    elmnt.removeAttribute("w3-include-html");
                    includeHTML();
                }
            }
            xhttp.open("GET", file, true);
            xhttp.send();
            /*exit the function:*/
            return;
        }
    }
}

function moveMyProfile() {
    location.href = urlInfo.myProfileUrl;
}

function adminLogout() {
    tms.ajaxGetHelper("/api/logout", null, null, function(result){
        //location.href = "/page/admin/login";
    });

}

function moveMenuPage(obj) {
    var dataValue = $(obj).attr("data-value");

    window.sessionStorage["menu_data"] = dataValue;
    //$.cookie('the_cookie', 'the_value', { expires: 7, path: '/', domain: 'jquery.com' });

    if(dataValue == "user") {
        location.href = urlInfo.userManagerUrl;
    } else if(dataValue == "history") {
        location.href = urlInfo.historyUrl;
    } else if(dataValue == "investInfo") {
        location.href = urlInfo.investInfoUrl;
    } else if(dataValue == "notice") {
        location.href = urlInfo.notice;
    } else if(dataValue == "faq") {
        location.href = urlInfo.faq;
    } else if(dataValue == "qna") {
        location.href = urlInfo.qna;
    }

}

var path = window.location.pathname;

function myProfileInfo() {
    var options = {async: false};
    return ;
    tms.ajaxGetHelper('/mapi/membership/profile/my', null, null, function(result) {
        if(result.code == "0") {
            var data = result.data;
            var imgPath = "";
            if(_.isEmpty(data.filePath)) {
                imgPath = "../static/images/img_profile_default.png";
            } else {
                imgPath = data.filePath;
            }

            $(".header").find(".profile").find(".name").text(data.nickName);
            $(".header").find(".profile").find(".photo").children().attr("src", imgPath);

            userInfo.userNicName = data.nickName;
            userInfo.userName = data.userName;

            //메뉴별 권한 처리
            var authorityStr = data.authorityStr;



            $(".sidebar-menu .menu-items li.mt30").each(function(index, data) {
                var authValue = $(this).attr("data-value");
                if(!(_.isUndefined(authValue) || _.isEmpty(authValue))) {
                    var authArray = authValue.split(",");
                    if(authArray != null && authArray.length > 0) {
                        for(var i=0; i<authArray.length; i++) {
                            if(authorityStr == authArray[i]) {
                                $(this).css("display", "block");
                                return;
                            }
                        }
                    }
                }
            });

            if(path.indexOf("/userpartner/admin") >= 0){

                if(authorityStr != "SUPER_ADMIN") {
                    $(document).find("#adminSignUpBtn").hide();
                }
                else {
                    $(document).find("#adminSignUpBtn").show();
                }
            }

            if(path.indexOf("/userpartner/adminProfile") >= 0){

                if(authorityStr != "SUPER_ADMIN") {
                    $(document).find("#adminDelInfoBtn").hide();
                    $(document).find("#adminEditInfoBtn").hide();
                }
                else {
                    $(document).find("#adminDelInfoBtn").show();
                    $(document).find("#adminEditInfoBtn").show();
                }
            }

        } else {

        }
    }, function(e) {
        console.log("my profile error", e)
    }, function(result) {
        UI.fn_side_select();
        //UI.fn_depth2();
        //UI.fn_profile();
        var $profile = $('.header .profile');
        var $button = $profile.find('.photo');
        var $menu = $profile.find('.layer_menu');

        $button.on('click',function(){
            $menu.toggle();
        });

        fn_depth2();
    });
}

function fn_depth2() {
    var $depth2_wrap = $('.menu-items');
    var $depth2 = $depth2_wrap.find('.depth_02');
    var $depth2_btn = $depth2.find('> a');

    $depth2_btn.each(function(i){this.num = i});

    $depth2_btn.on('click',function(){
        if ($depth2.eq(this.num).hasClass('open')){
            $depth2.removeClass('open');
        }else{
            $depth2.removeClass('open');
            $depth2.eq(this.num).addClass('open');
        }
    });
}

