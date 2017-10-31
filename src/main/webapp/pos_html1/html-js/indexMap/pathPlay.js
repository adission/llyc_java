$(function() {
    $('.default-date-picker').datepicker({
        format: 'yyyy-mm-dd'
    });
    
});
$('.timepicker-24').timepicker({
    autoclose: true,
    minuteStep: 1,
    secondStep: 10,
    showSeconds: true,
    showMeridian: false
});
function sureTime(){
	$("#range_5").ionRangeSlider({
        min: 0,
        max: 1000,
        type: 'single',
        step: 1,
        postfix: " ",
        prettify: false,
//        grid: true,
//        hide_min_max: true
    });
	$("#myModal1").css("zIndex","1050");
	$(".dropdown-menu.bootstrap-timepicker").css("zIndex","100000");
	$(".dropdown-menu.datepicker").css("zIndex","1000000");
    $('.pathMap').css("visibility","visible");
    
    $('.sure').attr('disabled',true);
    $("#animateBtn").attr('disabled',true);
    $("#zanStop").attr("disabled",true);
    $("#fast").attr("disabled",true);
    $("#low").attr("disabled",true);
    
    
}

function cancelTime(){
	$("#myModal1").css("zIndex","1050");
	$(".dropdown-menu.bootstrap-timepicker").css("zIndex","100000");
	$(".dropdown-menu.datepicker").css("zIndex","1000000");
	if($('.pathMap').css("visibility")=="hidden"){
		$('#myModal1').modal("hide");
	}
	
}

