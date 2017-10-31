/**
 * 
 *//**
 * Created by wangyanling on 2017/9/7.
 */

var tZoom = $("#imgB").get(0).style.zoom;
var scale = 1; //快进/退倍率
var timer=0;
var drawag =true;
var array=[]; //人员轨迹点数组
var data=[]; //人员分布点数据
var posUserId; //人员定位、轨迹回放的人员id
$(function(){
	
	 /*获取人员列表*/
	   getUserList(); 
	
    var cityUrl = 'http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js';
    $.getScript(cityUrl, function (script, textStatus, jqXHR) { 	  
      var citytq = remote_ip_info.city; // 获取城市
      var url = "http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&city=" + citytq + "&day=0&dfc=3";
     //alert(citytq);
      $.ajax({
        url: url,
        dataType: "script",
        scriptCharset: "gbk",
        success: function (data) {
      	  //alert(JSON.stringify(window.SWther.w[citytq]));  //显示地点
          var _w = window.SWther.w[citytq][0];
          var weather= _w.s1 + " " +_w.t2 + "℃～" + _w.t1 + "℃ "
          $('#weather').html(weather);
          /*alert(weather)
          var _f = _w.f1 + "_0.png";
          if (new Date().getHours() > 17) {
            _f = _w.f2 + "_1.png";
          }
          var img = "<img width='16px' height='16px' src='http://i2.sinaimg.cn/dy/main/weather/weatherplugin/wthIco/20_20/" + _f
      + "' />";
          var tq = "今日天气 :　" + citytq + " " + img + " " + _w.s1 + " " + _w.t1 + "℃～" + _w.t2 + "℃ " + _w.d1 + _w.p1 + "级";
          $('#weather').html(tq);*/
        }
      });
    });
	
    showHeader();
//    rnnu(100);
    add5();

    var Accordion = function(el, multiple) {
        this.el = el || {};
        this.multiple = multiple || false;

        var links = this.el.find('.link');
        links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
    };
    Accordion.prototype.dropdown = function(e) {
        var $el = e.data.el;
        $this = $(this);

        if($this.parent().children().length==1){
            $this.after('<ul class="submenu smAndxs">' +
                '<li><div class="btn-group">' +
                '<button type="button" class="btn btn-default" onclick="startLink(\'id\')">轨迹回放</button>' +
                '<button type="button" class="btn btn-default" onclick="startAfter(\'id\')">定位跟踪</button>' +
                '</div></li></ul>');
        }
        var $next = $this.next();

        $next.slideToggle();
        $this.parent().toggleClass('open');

        if (!e.data.multiple) {
            $el.find('.submenu').not($next).slideUp().parent().removeClass('open');
        };
    };
    var accordion = new Accordion($('#accordion'), false);
});

//显示当前时间日期
function showLocale(objD) {
    var str, colorhead, colorfoot;
    var yy = objD.getYear();
    if (yy < 1900) yy = yy + 1900;
    var MM = objD.getMonth() + 1;
    if (MM < 10) MM = '0' + MM;
    var dd = objD.getDate();
    if (dd < 10) dd = '0' + dd;
    var hh = objD.getHours();
    if (hh < 10) hh = '0' + hh;
    var mm = objD.getMinutes();
    if (mm < 10) mm = '0' + mm;
    var ss = objD.getSeconds();
    if (ss < 10) ss = '0' + ss;
    var ww = objD.getDay();
    if (ww == 0) colorhead = "";
    if (ww > 0 && ww < 6) colorhead = "";
    if (ww == 6) colorhead = "";
    if (ww == 0) ww = "星期日";
    if (ww == 1) ww = "星期一";
    if (ww == 2) ww = "星期二";
    if (ww == 3) ww = "星期三";
    if (ww == 4) ww = "星期四";
    if (ww == 5) ww = "星期五";
    if (ww == 6) ww = "星期六";
    colorfoot = ""
    str = colorhead + yy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss + " " + ww + colorfoot;
    return (str);
  }
  function tick() {
    var today;
    today = new Date();
    document.getElementById("today_time").innerHTML = showLocale(today);
    window.setTimeout("tick()", 1000);
  }
  tick();



//获取随机的100个位置
function rn(min,max){
    return Math.floor(Math.random()*(max-min)+1);
}
function rc(){
    var r=rn(0,256);
    var g=rn(0,256);
    var b=rn(0,256);
    return "rgb("+r+","+g+","+b+")";
}
var rr="rgb(47,26,237)";
var rg="rgb(234,74,203)";
var rb="rgb(31,39,27)";

function add5(){
	/*获取人员坐标的方法*/
    getUserLocation();
	/*
	 *var x=rn(100,300),y=rn(100,300);
	 *for(var i=0;i<50;i++){
	 *var p = {};
	 *p.x=x+10*rn(0,10);
	 *p.y=y+10*rn(0,10);
	 *x+=5;y+=5;
	 *data.push(p);
	 *}
	 */
}



// 打开轨迹回放控制板
function startLink(id){
	
	drawag = false;
	initLink();
	posUserId = id;
	// 打开控制面板
	$('.search-content').show();
	//获取当前日期
	var today = new Date();
	var month = today.getMonth() + 1;
	if(month<10){
		month = "0"+month;
	}
	var cdate = (today.getFullYear()) + "-" + month + "-" + today.getDate();
	$("#startdate").val(cdate);
	// 删除图上的原来得人
	$("svg").children("circle").remove();
	
	array=[];
	initLink();
	rnnu(id);
	// 如果有弹框，隐藏
}
// 开始定位追踪
function startAfter(id){
	drawag = false;
	posUserId = id;
	initLink();
	id = "114de358f74c433ab62b9cfa702afae9";
	// 关闭控制面板
	$('.search-content').hide();
	// 删除图上的原来得人
	$("svg").children("circle").remove();
	
	paintingCur(tZoom,id);
}
function closeLink(e){
	e.preventDefault();
	$('.search-content').hide();
	draw(data);
	
	drawag = true;
}

$("#start").click(function(){
	// 先画点后画线
	
	$("#scale").val("");
	$("#offscale").val("");
	painting(tZoom);
	
	runbar();
});

$("#stop").click(function(){
	$("#scale").val("");
	$("#offscale").val("");
    clearInterval(timer);
    window.clearInterval(bartimer);
});

var graph = new joint.dia.Graph();
var paper = new joint.dia.Paper({
    el: $('#papar'),
    width: 949*tZoom,
    height: 805*tZoom,
    model: graph,
    gridSize: 1
});

// 画人所在位置
function draw(arr){
    var obj = $("svg").get(0);
    var tZoom = $('.dragAble img').get(0).style.zoom;
    for(var i=0;i<arr.length;i++){
    	if(i%3==0){
    		drawPerson(arr[i].x,arr[i].y,tZoom,obj,i,rr,arr[i].id);
    	}else if(i%3==1){
    		drawPerson(arr[i].x,arr[i].y,tZoom,obj,i,rg,arr[i].id);
    	}else {
    		drawPerson(arr[i].x,arr[i].y,tZoom,obj,i,rb,arr[i].id);
    	}
    }
}

function moveToPoint(tZoom,obj,arr){
	if(id!=0){
		draw(array.slice((id+1)));
	}
	
	var arr1=array.slice(id,array.length); 
    var i=0;
    id = setInterval(frame, 2000/scale);
    function frame() {
    	
        if (i >= arr1.length) {
            clearInterval(id);
        } else {
            drawPerson(arr1[i].x,arr1[i].y,tZoom,obj,i);
            i++;
        }
    }
}
//点击人员弹出人员详情模态框相关操作
$('svg').on("click","circle",function(){
	/*aaaa();
	
	var param={"type":"2","layer_id":"2"}
	$.ajax({
	type:"post",
	url:"/lanlyc/Pos/selectassignmentsSection",
	data:{
	   "paramJson":JSON.stringify(param),
	},
	success:function(re){
	     if(re.code=="200"){
	    	 var pos=re.data;
		     //console.log(pos);
		     console.log("区域     ："+JSON.stringify(pos));
	     }else{
	          alert(re.message);	       
	     }
	 }
	});  */
	
	
    var a=$(this);
    var user_id=$(this).attr("people-id");
    $("#gj").attr("value",user_id);
    $("#dw").attr("value",user_id);
    var color=$(this).get(0).style.fill

    $(this).get(0).style.fill="#ff0000";//选中的颜色变红
    //$(this).siblings().css("fill","#ff0000");//没选中的颜色还原
    //$(this).siblings().removeClass("fill");
   
    //人员点相对屏幕上的坐标
    var point_x=event.clientX;
    var point_y=event.clientY;

    var param={"id":user_id,"token":getCookie("token")}
    $.ajax({
    type:"post",
    url:"/lanlyc/Pos/selectWorkerById",
    data:{
       "paramJson":JSON.stringify(param),
    },
    success:function(result){
	     if(result.code=="200"){
	    	 var datas=result.data;
	    	 //alert(data);
		     //console.log(result.data);
		     		     
		     var objDiv = $("#workerInfo");
		     $(objDiv).modal("show");

		     $(".modal-backdrop").css("display","none");//删除class值为modal-backdrop的标签，可去除阴影
		     

		     //模态框的宽和高
		     var modal_height=$(".modal-dialog").height();
		     var modal_width=$(".modal-dialog").width();

		     
		     //地图框的宽和高
		     var map_x=$(window).width()-200;
		     var map_y=$(window).height();//浏览器当前窗口可视区域高度		     
		     
		     //通过点相对屏幕上的xy坐标、模态框的宽和高、地图框的宽和高、使用模态框的元素来确定模态框在地图中的位置
		     getLocation(point_x,point_y,modal_height,modal_width,map_x,map_y,$(".modal-content"));
  		     
		     var name=$("#name").text(datas.name);		     
		     if(datas.gender=='1'){
		    	 datas.gender="男"
		     }else{
		    	 datas.gender="女"
		     }
		     var gender=$("#gender").text(datas.gender);
		     var nation=$("#nation").text(datas.nation);
		     var mobile=$("#mobile").text(datas.mobile);
		     var cid=$("#cid").text(datas.cid);
		     var birthday=datas.birthday;
		     //如果数据库中没有birthday，那么就截取身份证上的
		     if(birthday==null || birthday=="" || birthday=== undefined){
		    	 if(cid != null && cid !="" && cid != undefined){
		    		 var year=datas.cid.substring(6,10);
		    		 var month=datas.cid.substring(10,12);
		    		 var day=datas.cid.substring(12,14);
		    		 birthday=year+"年"+month+"月"+day+"日";
		    	 }
		     }
		     birthday=$("#birthday").text(birthday);
		     var post=$("#post").text(datas.workerstype);
		     var user_type=$("#user_type").text(datas.userclass);
		     var gonghao=$("#gonghao").text(datas.gonghao);
		     var team=$("#team").text(datas.team);
		     var urlpath = "http://localhost:8080/lanlyc/";

		     if(datas.avatar_img.indexOf("http") >= 0){
		    	 image=datas.avatar_img  //使用云上的图片地址
		     }else{
		    	 image=urlpath+datas.avatar_img; //使用本地服务器上的图片地址
		     }

		     $('#workerInfo img').attr("src",image);	    	     		     
		     
		     //如果弹窗消失，则选中的颜色还原
		     $('#workerInfo').on("hide.bs.modal",function(){
		         a.get(0).style.fill=color;
		         $(".modal-content").removeClass("left").removeClass("top");

		     });

        }else{
	       alert(result.message);
	       
	     }
   
     }
    });   
});


$("#searchUser").click(function(){
	 /*获取人员列表*/
   getUserList(); 
});

/*根据参数获取地址栏中指定的参数值*/
function getDataFromAddressBar(name){
	 var reg = new RegExp('(^|&)'+ name +'=([^&]*)(&|$)','i');
    var r = window.location.search.substr(1).match(reg);
    if(r!=null){
    return  unescape(r[2]);
    }
    return null;
}

/*获取人员列表*/
function getUserList(){
//	var constructId = getDataFromAddressBar("constructId");
	var userName = $("#username").val();
	var constructId = "0585795194ae4ffaace277d592500f00";
	var name = "";
	var mobile = "";
	if(!parseInt(userName)){
		name=userName;	
	}else{
		mobile=userName;
	}
	var param = {"constructId":constructId,"name":name,"mobile":mobile,"token":getCookie("token")};
	$.ajax({
		 type:"post",
		 url:"/lanlyc/PosUser/getUserInfo",
		 data:{
		    "paramJson":JSON.stringify(param),
		 },
		 success:function(result){
			 if(result.code=="200"){
				 /*清空用户*/
				 $("#accordion").empty();
				 console.log(result);
				 /*人员个数*/
				 var dataLen = result.recordsTotal;
				 for(var i=0;i<dataLen.length;i++){
					var childenLi = "<li id='"+dataLen[i].id+"'><div class='link'><span class='text-warning' style='margin-right: 20px'>"+dataLen[i].name+"</span><span>"+dataLen[i].mobile+"</span></div></li>"; 
					$("#accordion").append(childenLi); 
				 }  
			 }else{
				 alert("人员信息查询失败!");
			 }	
		 }
		 

	});	
}


$("#searchUser").click(function(){
	 /*获取人员列表*/
   getUserList(); 
});

/*根据参数获取地址栏中指定的参数值*/
function getDataFromAddressBar(name){
	 var reg = new RegExp('(^|&)'+ name +'=([^&]*)(&|$)','i');
    var r = window.location.search.substr(1).match(reg);
    if(r!=null){
    return  unescape(r[2]);
    }
    return null;
}

/*获取人员列表*/
function getUserList(){
//	var constructId = getDataFromAddressBar("constructId");
	var userName = $("#username").val();
	var constructId = "0585795194ae4ffaace277d592500f00";
	var name = "";
	var mobile = "";
	if(!parseInt(userName)){
		name=userName;	
	}else{
		mobile=userName;
	}
	var param = {"constructId":constructId,"name":name,"mobile":mobile,"token":getCookie("token")};
	$.ajax({
		 type:"post",
		 url:"/lanlyc/PosUser/getUserInfo",
		 data:{
		    "paramJson":JSON.stringify(param),
		 },
		 success:function(result){
			 if(result.code=="200"){
				 /*清空用户*/
				 $("#accordion").empty();
				 console.log(result);
				 /*人员个数*/
				 var dataLen = result.recordsTotal;
				 for(var i=0;i<dataLen.length;i++){
					var childenLi = "<li id='"+dataLen[i].id+"'><div class='link'><span class='text-warning' style='margin-right: 20px'>"+dataLen[i].name+"</span><span>"+dataLen[i].mobile+"</span></div></li>"; 
					$("#accordion").append(childenLi); 
				 }  
			 }else{
				 alert("人员信息查询失败!");
			 }	
		 }
		 

	});	
}


/*人员坐标调用*/
function getUserLocation(){
	var constructId = "0585795194ae4ffaace277d592500f00";
	var param = {"constructId":constructId,"token":getCookie("token")};
	$.ajax({
		 type:"post",
		 url:"/lanlyc/postrajectory/getUserXY",
		 data:{
		    "paramJson":JSON.stringify(param),
		 },
		 success:function(result){
			 if(result.code=="200"){
				 /*获取人员坐标*/
				 //获取的人员轨迹点  ,真实的人员轨迹点
				 for(var i=0;i<result.data.length;i++){ 
						var p={};
						p.x=result.data[i].perLatitude;
						p.y=result.data[i].perLongitude;
						p.id=result.data[i].perId;
						data.push(p);
					}
					 /*画人员初始坐标*/
					 draw(data);
			 }else{
				 alert("人员信息坐标查询失败!");
			 }	
		 } 
	});	
}









