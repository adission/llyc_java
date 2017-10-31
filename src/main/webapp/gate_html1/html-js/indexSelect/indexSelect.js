/**
 * 
 */
$('#user_name').html(getCookie("user_name"));

var uimg = getCookie("img");
if(uimg!=null){
	$('#userImg').attr('src',urlpath+""+uimg);
}else {
	$('#userImg').attr('src',"images/sidebar-toggle-light.png");
}

function gogo(url){
	location.href=url;
}