    $(".step").click( function() {
	$(this).addClass("active").prevAll().addClass("active");
	$(this).nextAll().removeClass("active");
});

$(".step01").click( function() {
	$(".cssi").addClass("active").siblings().removeClass("active");
});

$(".step02").click( function() {
	$(".dandyhacks").addClass("active").siblings().removeClass("active");
});

$(".step03").click( function() {
	$(".ep").addClass("active").siblings().removeClass("active");
});

$(".step04").click( function() {
	$(".intern").addClass("active").siblings().removeClass("active");
});

$(".step05").click( function() {
	$(".ghc").addClass("active").siblings().removeClass("active");
});
