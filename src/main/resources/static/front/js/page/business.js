$(function () {

    $('.sub_business .section_02 .aside .select_box .select li a').click(function () {
        var ref = $(this).attr('ref');
        $('.sub_business .section_02 .aside .select_box .select li').removeClass('on');
        $(this).parent().addClass('on');
        var labelArr = $('.sub_business .section_02 .cont ul li');
        if(ref=='All'){
            labelArr.show();
            return ;
        }

        $.each(labelArr,function () {
            var txt = $(this).find('.img_box .label').text();
            if(ref==txt){
                $(this).show();
            }else{
                $(this).hide();
            }
        })
    });

    var obj = $('.sub_business .section_02 .aside .select_box .select li a');
    var info = tms.getParameterByName("info");
    if(info==''||info=='All'){obj.eq(0).click()}
    if(info=='Tech'){obj.eq(1).click()}
    if(info=='Health'){obj.eq(2).click()}
    if(info=='Culture'){obj.eq(3).click()}
    if(info=='Play'){obj.eq(4).click()}
})