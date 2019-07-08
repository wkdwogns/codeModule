$(function() {
    $(".selectBox").on("click", function(e){
        e.preventDefault();
        var $select = $(this);
        var $select_btn = $select.find('> a');
        var $select_option = $select.find('.option');

        $select.toggleClass('open');
        $select_option.stop().slideToggle("fast");

    });



    $(".selectBox").on("click", "ul a", function(e){
        e.preventDefault();
        var dataValue = $(this).attr("data-value");
        var dataText = $(this).text();


        var parentTag = $(this).parent().parent();
        var prevObj = parentTag.prev();
        prevObj.attr("data-value", dataValue);
        prevObj.text(dataText);
        //parentTag.css("display", "none");
        //parentTag.parent().removeClass("open");

        //$this.addClass("select").siblings().removeClass("select");
        //$("#sizevalue").val($this.data("value"));
    })

    $("body, html").on("click", function(e){
        if(!$(e.target).is(".selectBox") && $(e.target).parents(".selectBox").length < 1){
            $(".selectBox").removeClass("open");
            $(".selectBox .option").css("display", "none");
        }
    });
});

function setMomentTimeFormat(time, format){
    return moment.utc(time).tz("Asia/Ho_Chi_Minh").format(format);
}

function checkEmpty(value) {
    if(value == null || value == "null" || value == "undefined" || typeof value == "undefined" || $.trim(value) == "") {
        return "";
    } else {
        return value;
    }
}

function btnDisabled(obj) {
    $(obj).attr("disabled", true);
    $(obj).css("background", "#dfbee8");
}

function btnEnabled(obj) {
    $(obj).attr("disabled", false);
    $(obj).css("background", "#6d5cae");
}
