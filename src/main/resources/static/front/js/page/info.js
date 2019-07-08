$(function(){
    console.log(tms.isMobile.any())
    if(tms.isMobile.any()){
        $(".txt_box_03 .btn_down span").text("안내문 미리보기");
    }
})

var downFile = function(seq){

    if(tms.isMobile.any()){
        if(seq == "1"){ //안내문
            url = '/docs/3323961f80da4f83af124dfca05f3848.pdf';
        } else if(seq == "4"){//임직원
            url = '/docs/15b6e9f72b4e4e059a41e7c90670d720.pdf';
        } else if(seq == "5"){//컨설턴트
            url = '/docs/e8242b237e9f4d79b4e211360648b09a.pdf';
        }
        window.open(url, "미리보기");
    } else {
        var url = '/api/file/download?category=6&fileSeq='+seq;
        location.href = encodeURI(url);
    }
}