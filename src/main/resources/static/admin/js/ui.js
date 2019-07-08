$(function(){
    var mainPath = '/page/admin';

    var h ={
        banner:{
            name:'메인관리'
            ,bigMenu:[{name:'노출 관리'
                    ,midMenu:[
                        {name:'최상단 배너',url:mainPath+'/bannerT/list'}
                        ,{name:'콘텐츠 배너',url:mainPath+'/bannerC/list'}
                    ]
                },
                {name:'팝업창관리',url:mainPath+'/popup/list'}
            ]
        },
        notice:{
            bigMenu:[
                {
                    name:'공지사항관리'
                    ,url:mainPath+'/notice/list'
                }
            ]
        },
        story:{
            bigMenu:[
                {
                    name:'수혜아동 스토리'
                    ,url:mainPath+'/story/list'
                }
            ]
        },
        appForm:{
            bigMenu:[
                {
                    name:'신청서 관리'
                    ,url:mainPath+'/appForm/list'
                }
            ]
        },
        qna:{
            name:'1:1문의관리',
            bigMenu:[
                {
                    name:'1:1문의관리'
                    ,url:mainPath+'/qna/list'
                }
            ]
        },
        mypage:{
            name:'마이페이지',
            bigMenu:[
                {
                    name:'마이페이지'
                    ,url:mainPath+'/myPage/add'
                }
            ]
        },
    }



    var pN = location.pathname;
    $('.header nav a').removeClass('on')
    if(pN.indexOf('banner')!=-1){
        $('.header nav a.banner').addClass('on');
        setMenu(h.banner);
    }

    if(pN.indexOf('popup')!=-1){
        $('.header nav a.banner').addClass('on');
        setMenu(h.banner);
    }

    if(pN.indexOf('notice')!=-1){
        $('.header nav a.notice').addClass('on');
        setMenu(h.notice);
    }
    if(pN.indexOf('story')!=-1){
        $('.header nav a.story').addClass('on');
        setMenu(h.story);
    }
    if(pN.indexOf('appForm')!=-1){
        $('.header nav a.appForm').addClass('on');
        setMenu(h.appForm);
    }
    if(pN.indexOf('qna')!=-1){
        $('.header nav a.qna').addClass('on');
        setMenu(h.qna);
    }
    if(pN.indexOf('mypage')!=-1){
        $('.header nav a.mypage').addClass('on');
        setMenu(h.mypage);
    }
});

function setMenu(obj){
    //$('.leftSide .snb').append('<h2>'+obj.bigMenu[0].name+'</h2>');

    var ul = $('<ul class="bigMenu"></ul>');
    var bArr = obj.bigMenu;
    for(var i in bArr){
        var bnm = bArr[i].name;
        var bUrl = (bArr[i].url!=null&&bArr[i].url!='')?bArr[i].url:'#self';
        var midUl ='';

        var midM = bArr[i].midMenu;
        if(midM!=null){
            midUl= $('<ul class="midMenu" style="display: block"></ul>');
            for(var i in midM){
                var nm = midM[i].name;
                var url = (midM[i].url!=null&&midM[i].url!='')?midM[i].url:'#self';

                var li ='<li><a href="'+url+'" class="">'+nm+'</a></li>';
                midUl.append(li);
            }
        }

        var li = $('<li></li>');
        li.append(  '<a href="'+bUrl+'">'+
                        '<span>'+bnm+'</span>'+
                        '<span class="icon"><i class="fas fa-sort-down"></i></span>'+
                    '</a>');
        li.append(midUl);
        ul.append(li);
    }

    $('.leftSide .snb').append(ul);
    var anc = $('.leftSide .snb a');
    $.each( anc,function(k,v){
        var href = $(v).attr('href');
        var add = href.replace('list','add');
        var detail = href.replace('list','detail');
        var pn = location.pathname;

        if(pn==href||pn==add||pn==detail){
            $(v).addClass('on');
            $(v).parent().parent().parent().addClass('on');
            $(v).parent().addClass('on');
            return false;
        }

    })
}

function fileCheck(obj,isImg) {
    var files= obj.files;

    var exp_file = false;
    var exp_fileNm = false;
    var exp_size = false;
    var black_file = false;
    for (var i in files) {

        var file = files[i];
        if (typeof file != 'object') { continue; }

        if(isImg=='onlyImg'){
            var ext = file.name.split('.').pop().toLowerCase();
            if($.inArray(ext, ['jpg','gif','png','jpeg','tif','tiff']) == -1) {
                exp_file = true;
                break;
            }
        }


        if($.inArray(ext, ['php','htm','html','inc','htm','shtm','ztx','dot','cgi','pl','phtm','ph','exe']) != -1) {
            black_file = true;
            break;
        }

        if(file.name.length>100) {
            exp_fileNm = true;
            break;
        }

        var size = file.size/1024/1024;
        if(file.size>parseInt(10485760)) {
            exp_size = true;
            break;
        }
    }

    if(isImg=='onlyImg'&&exp_file){
        alert('이미지 파일만 업로드 가능합니다.');
        $(obj).val('');
        return ;
    }

    if(exp_fileNm){
        alert('파일명을 줄여 주세요.');
        $(obj).val('');
        return ;
    }

    if(exp_size){
        alert('10mb 이내로 업로드 해 주세요.');
        $(obj).val('');
        return ;
    }

    if(black_file){
        alert('업로드할 수 없는 형식의 파일입니다.');
        $(obj).val('');
        return ;
    }


}