//ready the dom.
$(document).ready(function(){

  //when the search box is entered
  $(".search").focus(function(){
    //slideDown the results div
    $(".result").slideDown(200);

    //animate the form to the top
    $(".form").animate({
      top:"-190px",
    });

    $(".res a").fadeIn(1000);
    //when the search box is unfocused:
    return true;
  })
});
