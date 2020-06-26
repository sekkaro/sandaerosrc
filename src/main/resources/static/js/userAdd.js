$(document).ready(function(){
    $(".button").click(function(){
        console.log($(this).children("img"));
        if($(this).children("img").attr("src") == "/img/deactive.png"){
            $(this).children("img").attr("src", "/img/active.png");
            $(this).toggleClass("clickButton");
            $('input:checkbox[id="interest'+this.id+'"]').prop("checked", true);
        }else{
            $(this).children("img").attr("src", "/img/deactive.png");
            $(this).toggleClass("clickButton");
            $('input:checkbox[id="interest'+this.id+'"]').prop("checked", false);
        }
    })
});
