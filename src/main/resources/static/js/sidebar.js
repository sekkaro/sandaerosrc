
function showSubMenu() {
  $(document).ready(function() {
    $(".menu").click(function(){
      $(this).next().toggle();
      console.log($(this).hasClass('clickedMenu'));
      $(this).toggleClass('clickedMenu');
      // $(this).children("img").attr("src", "/img/below_arrow.png");
      //$(this).img.src = "./img/bar.png";
    });
  });
}

