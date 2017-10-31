//-----------------------------------------------------------------------------
//-----视频控制------------------------------------------------------------------
//-----------------------------------------------------------------------------

//-----------------------------chenyan start-----------------------------------

var is_important = 0;	//巡航模式（1：代表重点，2：代表所有）
var screen_type = 0;	//分屏模式（1：代表1*1 2：代表2*2 3：代表3*3 4：代表4*4）
var group_id = "";		//当前分组id
var group_list = {};	//当前巡航模式和分屏模式下的所有组列表
var screen_list = {};	//当前模式下的所有屏列表
var screen_sort = 1;	//当前显示在第几屏
var vidicon_list = {};	//屏幕当前要显示的摄像头列表
var screen_id = "";		//静态显示时，某个屏
var cameraList = "";    //所有摄像头列表
var targetName = "";    //正在巡航的对象名称
var timer = "";			//定时器

var screen_length = ""; //屏最大长度

$(function(){
	//获取系统设置的默认巡航模式和分屏模式
	getDefaultScreenTypeAndIsImportant();
	//ajax设置同步
	$.ajaxSetup({async : false }); 
});
	
//获取系统设置的默认巡航模式和分屏模式
function getDefaultScreenTypeAndIsImportant(){
	$.post(
			"../sysetem/getBaseSetting",
			{
				"token": getCookie('token')
			},
			function(res){
				if(res.code == 200){
					is_important = res.data[0]["polling_mode"];	//巡航模式（是否重要组）
					screen_type = res.data[0]["screen_mode"];	//分屏模式
					console.log("系统设置的默认巡航模式:");
					console.log(is_important);
					console.log("系统设置的默认分屏模式:");
					console.log(screen_type);
					//1、通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的默认组id
					getDefaultGroupIdByscreenTypeAndIsImportant();
					//2、通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的所有组列表
					getGroupListByScreenTypeAndIsImportant();
					//3、获取某种分屏模式下的所有屏
					getScreenListByScreenType();
					//执行默认分屏模式
					changeWndNum(screen_type)
					//执行默认巡航模式
					setImportant(is_important);
				}else{
					alert(res.message);
				}
			}
	);
};

//获取某种分屏模式下、某种巡航模式下的默认组id
function getDefaultGroupIdByscreenTypeAndIsImportant(){
	var paramJson = {
			"screen_type":screen_type,
			"is_important":is_important
	};
	$.post(
			"../videoGroup/getDefaultGroupIdByscreenTypeAndIsImportant",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				if(res.code == 200){
					group_id = res.data;
					console.log("该巡航模式该分屏模式下的默认组id：");
					console.log(group_id);
					targetName = group_id;
					//获取默认组的第一屏
					screen_sort = 1;
					getScreenVidiconListByGroupIdAndSort(screen_sort);
					//获取默认组的所有屏
					//getScreenListByGroupId();
				}else{
					alert(res.message);
				}
			}
	);
}

//通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的所有组列表
function getGroupListByScreenTypeAndIsImportant(){
	var paramJson = {
			"screen_type":screen_type,
			"is_important":is_important
	};
	$.post(
			"../videoGroup/getGroupListByScreenTypeAndIsImportant",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				console.log(res);
				if(res.code == 200){
					console.log("该巡航模式该分屏模式下的所有组列表：");
					console.log(res.data);
					group_list = res.data;
					var $turnUl=$(".turn ul"),
						htm = '';
					for(i=0; i<group_list.length; i++){
						htm += '<li  gid="'+group_list[i].id+'"><a>'+group_list[i].group_name;
						if(group_list[i].whether_important == 1){
							htm +='<span style="color: #ff0000;margin-left: 5px;">*&nbsp;</span>';
						}
					}
					htm += '</a></li>';
					$turnUl.html(htm);
				}else{
					$(".turn ul").html("<li><a>"+res.message+"</a></li>");
				}
			}
	);
}

//获取某种分屏模式下的所有屏
function getScreenListByScreenType(){
	if(screen_type == 1){
		//分屏模式为1则获取所有的摄像头列表
		var paramJson = { "whether_important":2};
        $.post("../vidicon/getAllVidicon",
        		{
        	     	"paramJson": JSON.stringify(paramJson),
        	     	"token": getCookie('token'),
        		},
        		function(ret){
		        	console.log("所有的摄像头列表：")
		            console.log(ret.data)
		            cameraList = ret.data;
					var $staticUl=$(".static ul"),
					htm = '';
					for(i=0; i<cameraList.length; i++){
						htm += '<li  sid="'+cameraList[i].id+'"><a>'+cameraList[i].vidicon_name;
						if(cameraList[i].whether_important){
							htm +='<span style="color: #ff0000;margin-left: 5px;">*&nbsp;</span>';
						}
					}
					htm += '</a></li>';
					$staticUl.html(htm);
        });
	}else{
		var paramJson = {"screen_type":screen_type};
		$.post(
				"../groupScreenVidicon/getScreenListByScreenType",
				{
					"paramJson": JSON.stringify(paramJson),
					"token": getCookie('token')
				},
				function(res){
					if(res.code == 200){
						console.log("该分屏模式下的所有屏列表：");
						console.log(res.data);
						screen_list = res.data[0];
						var $staticUl=$(".static ul"),
						htm = '';
						for(i=0; i<screen_list.length; i++){
							htm += '<li  sid="'+screen_list[i].screen_id+'"><a>'+screen_list[i].screen_name;
							if(screen_list[i].is_important == 1){
								htm +='<span style="color: #ff0000;margin-left: 5px;">*&nbsp;</span>';
							}
						}
						htm += '</a></li>';
						$staticUl.html(htm);
					}else{
						$(".static ul").html("<li><a>"+res.message+"</a></li>");
					}
				}
		);
	}
}

//获取某个组下的第几屏
function getScreenVidiconListByGroupIdAndSort(screen_sort){
	var paramJson = {
			"group_id":group_id,
			"screen_sort":screen_sort
	};
	$.post(
			"../groupScreenVidicon/getScreenVidiconListByGroupIdAndSort",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				if(res.code == 200){
					console.log("该组下的第" + screen_sort + "屏的摄像头列表：");
					console.log(res.data);
					vidicon_list = res.data;
					var ids = [];
					for(var i=0;i<res.data.length;i++){
						ids.push(res.data[i].vidicon_id);
					}
					//通过摄像头id获取摄像头的IP，端口，用户名，密码
					getIPAndPortById(ids);
				}else{
					alert(res.message);
				}
			}
	);
}

//获取某个组下的所有屏列表
function getScreenListByGroupId(){
	var paramJson = {
			"group_id":group_id,//分组id
	};
	$.post(
			"../groupScreenVidicon/getScreenListByGroupId",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				if(res.code == 200){
					console.log("该组下的所有屏列表：");
					console.log(res.data);
					screen_list = res.data;
				}else{
					alert(res.message);
				}
			}
	);
}


//改变分屏模式时
function changeScreenType(screen_type){
	screen_type = screen_type;
	//通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的默认组id
	getDefaultGroupIdByscreenTypeAndIsImportant();
	//通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的所有组列表
	getGroupListByScreenTypeAndIsImportant();
}

//改变巡航模式时
function changeIsImportant(is_important){
	is_important = is_important;
	//通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的默认组id
	getDefaultGroupIdByscreenTypeAndIsImportant();
	//通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的所有组列表
	getGroupListByScreenTypeAndIsImportant();
}

//点击某个分组
function changeGroupId(group_id){
	group_id = group_id;
	//获取该组下的所有屏列表
	//getScreenListByGroupId();
	//获取该组下的第一屏
	screen_sort = 1;
	getScreenVidiconListByGroupIdAndSort(screen_sort);
}

//静态显示，点击某个屏
function changeScreenId(screen_id){
	//1、获取某个屏下的所有摄像头
	var paramJson = {
			"screen_id":screen_id
	};
	console.log(paramJson);
	$.post(
			"../groupScreenVidicon/getScreenVidiconListByScreenId",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				console.log("啊啊啊啊啊啊：");
				console.log(res);
				if(res.code == 200){
					console.log("某个屏的摄像头列表：");
					console.log(res.data);
					vidicon_list = res.data;
					//2、获取该屏外其他的所有摄像头列表
					getOtherVidiconListByScreenId();
				}else{
					alert(res.message);
				}
			}
	);

}

//获取某个屏外其他的所有摄像头列表
function getOtherVidiconListByScreenId(){
	var paramJson = {
			"screen_id":screen_id
	};
	console.log(paramJson);
	$.post(
			"../groupScreenVidicon/getOtherVidiconListByScreenId",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				if(res.code == 200){
					console.log("该屏外其他的所有摄像头列表：");
					console.log(res.data);
					vidicon_list = res.data;
				}else{
					alert(res.message);
				}
			}
	);
}


//-----------------------------chenyan end-------------------------------------------------------------

//通过摄像头id获取摄像头的IP，端口，用户名，密码
function getIPAndPortById(ids){
	var paramJson = {
			"id":ids
	};
	console.log("===========");
	console.log(paramJson);
	$.post(
			"../vidicon/getVidiconPartField",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				if(res.code == 200){
					console.log("摄像头的IP，端口，用户名，密码：");
					console.log(res.data);
					for(var i=0;i<res.data.length;i++){
						var ip = res.data[i].vidicon_ip,
						    port = res.data[i].vidicon_port,
							username = res.data[i].vidicon_username,
							password = res.data[i].vidicon_password;
						clickLogin(ip,port,username,password,i);
					}
				}else{
					alert(res.message);
				}
			}
	);
}

//通过摄像头的IP，端口，用户名，密码登录
function clickLogin(IP,Port,Username,Password,i) {
	console.log(99999999);
	console.log(IP);
	console.log(Port);
	console.log(Username);
	console.log(Password);
	
	var szIP = IP,
		szPort = Port,
		szUsername = Username,
		szPassword = Password;

	if ("" == szIP || "" == szPort) {
		return;
	}

	var iRet = WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
		success: function (xmlDoc) {
			console.log(" 登录成功！");
			showOPInfo(szIP + " 登录成功！");

			clickStartRealPlay(szIP,i);
		},
		error: function () {
			console.log(" 登录失败！");
			showOPInfo(szIP + " 登录失败！");
		}
	});

	if (-1 == iRet) {
		showOPInfo(szIP + " 已登录过！");
	}
}

//通过摄像头的IP，码流，通道......开始预览
function clickStartRealPlay(IP,i) {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		iWndIndex = i,//播放窗口
		szIP = IP,//IP
		iStreamType = 1, //主码流
		bZeroChannel = true,
		szInfo = "";

	if(IP="192.168.3.2"){
		var iChannelID ="1";//通道0
	}else{
		var iChannelID = "2";//通道0
	}
	if ("" == szIP) {
		return;
	}

	if (oWndInfo != null) {// 已经在播放了，先停止
		WebVideoCtrl.I_Stop();
	}

	var iRet = WebVideoCtrl.I_StartRealPlay(szIP, {
		iWndIndex:iWndIndex,
		iStreamType: iStreamType,
		iChannelID: iChannelID,
		bZeroChannel: bZeroChannel
	});

	if (0 == iRet) {
		console.log("开始预览成功！");
		szInfo = "开始预览成功！";
	} else {
		szInfo = "开始预览失败！";
	}

	showOPInfo(szIP + " " + szInfo);
}

//窗口分割数
function changeWndNum(iType) {
	screen_type = iType;
	iType = parseInt(iType, 10);
	//调用插件的分屏接口
	WebVideoCtrl.I_ChangeWndNum(iType);
	
	//1、通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的默认组id
	getDefaultGroupIdByscreenTypeAndIsImportant();
	//2、通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的所有组列表
	getGroupListByScreenTypeAndIsImportant();
	//3、获取某种分屏模式下的所有屏
	getScreenListByScreenType();
}

//设置重点轮循或者一般轮循
function setImportant(type){
	is_important = type;
	//1、通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的默认组id
	getDefaultGroupIdByscreenTypeAndIsImportant();
	//2、通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的所有组列表
	getGroupListByScreenTypeAndIsImportant();
	
	var text=((is_important==1)?"一般":"重点");
	//滚动字幕
	ScrollText($('#scrollText'),35,400,'正在对某某分组进行'+text+'轮巡','left',1,20);//滚动字幕
}

//全屏
function clickFullScreen() {
	WebVideoCtrl.I_FullScreen(true);
}

//选择轮循小组
$(".turn").on('click','li',function(){
	group_id = $(this).attr("gid")
	//获取该组下的第一屏
	screen_sort = 1;
	getScreenVidiconListByGroupIdAndSort(screen_sort);
	//获取该组下的屏个数
	getScreenListByGroupId();
	
	console.log("屏个数"+ screen_length);
	
	//定时器  每隔多少时间请求下一屏
	timer = window.setInterval(function(){
		screen_sort = screen_sort+1;
		//当到最后一屏时，再次返回第一屏
		if(screen_sort > screen_length){
			screen_sort = 1;
		}
		getScreenVidiconListByGroupIdAndSort(screen_sort);
	},5000) 
	
	
	$(".turn button").addClass("btn-info");
	$(".static button").removeClass("btn-info");
});

//选择静态显示屏
$(".static").on('click','li',function(){
	//清除定时器
	window.clearInterval(timer);
	 
	//通过屏id返回所有的摄像头
	var screen_id = $(this).attr("sid")
	changeScreenId(screen_id)
	
	$(".static button").addClass("btn-info");
	$(".turn button").removeClass("btn-info");
});



//获取某个组下的所有屏列表的长度
function getScreenListByGroupId(){
	var paramJson = {
			"group_id":group_id,//分组id
	};
	console.log("hehehhe");
	console.log(paramJson);
	$.post(
			"../groupScreenVidicon/getScreenListByGroupId",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token'),
			},
			function(res){
				console.log("hahahhaha");
				console.log(res);
				if(res.code == 200){
					console.log("该组下的所有屏列表：");
					console.log(res);
					screen_length = res.screen_length;									
				}else{
					alert(res.message);
				}
			}
	);
}




//-----------------------------------------------------------------------------
//-----云台控制------------------------------------------------------------------
//-----------------------------------------------------------------------------
//param: int userId:摄像头注册用户唯一id
//int command:云台控制命令21:上仰,22下俯,23:左转,24:右转,25:上仰和左转,26:上仰和右转,27:下附和左转,28:下附和右转,29:左右自动扫描
$('.direction i').mousedown(function(){
	switch($(this).index()){
	  case 0://21:上仰
	      command = 1;
	      mouseDownPTZControl(1);
	      break;
	  case 1://26:上仰和右转
	      command = 7;
	      mouseDownPTZControl(7);
	      break;
	  case 2://24:右转
	      command = 4;
	      mouseDownPTZControl(4);
	      break;
	  case 3://28:下附和右转
	      command = 8;
	      mouseDownPTZControl(8);
	      break;
	  case 4://22下俯
	      command = 2;
	      mouseDownPTZControl(2);
	      break;
	  case 5://27:下附和左转
	      command = 6;
	      mouseDownPTZControl(6);
	      break;
	  case 6://23:左转
	      command = 3;
	      mouseDownPTZControl(3);
	      break;
	  case 7://25:上仰和左转
	      command = 5;
	      mouseDownPTZControl(5);
	      break;
	  default:
	      break;
	}
});



$('.direction i').mouseup(function(){
	switch($(this).index()){
	  case 0://21:上仰
		  mouseUpPTZControl();
	      break;
	  case 1://26:上仰和右转
		  mouseUpPTZControl();
	      break;
	  case 2://24:右转
		  mouseUpPTZControl();
	      break;
	  case 3://28:下附和右转
		  mouseUpPTZControl();
	      break;
	  case 4://22下俯
		  mouseUpPTZControl();
	      break;
	  case 5://27:下附和左转
		  mouseUpPTZControl();
	      break;
	  case 6://23:左转
		  mouseUpPTZControl();
	      break;
	  case 7://25:上仰和左转
		  mouseUpPTZControl();
	      break;
	  default:
	      break;
	}
});

$('#btn_auto').mousedown(function(){
	mouseDownPTZControl(9);
});




/*var data = {'userId':1,'command':command,'token':getCookie('token')};
$.ajax({
	  type:'POST',
	  url:'../holderControl/holderCtrl',
	  data: {"paramJson":JSON.stringify(data)},
	  success:function(data){
		  console.log(data);
	  }
});*/


//PTZ控制 9为自动，1,2,3,4,5,6,7,8为方向PTZ
var g_bPTZAuto = false;
function mouseDownPTZControl(iPTZIndex) {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(0),
		//bZeroChannel = $("#channels option").eq($("#channels").get(0).selectedIndex).attr("bZero") == "true" ? true : false,
		bZeroChannel = false,
		iPTZSpeed = $("#ptzspeed").val();

	if (bZeroChannel) {// 零通道不支持云台
		return;
	}
	if (oWndInfo != null) {
		if (9 == iPTZIndex && g_bPTZAuto) {
			iPTZSpeed = 0;// 自动开启后，速度置为0可以关闭自动
		} else {
			g_bPTZAuto = false;// 点击其他方向，自动肯定会被关闭
		}
		console.log(iPTZIndex);
		var ss = WebVideoCtrl.I_PTZControl(iPTZIndex, false, {
			
			iPTZSpeed: iPTZSpeed,
			success: function (xmlDoc) {
				if (9 == iPTZIndex) {
					g_bPTZAuto = !g_bPTZAuto;
				}
				showOPInfo(oWndInfo.szIP + " 开启云台成功！");
			},
			error: function () {
				showOPInfo(oWndInfo.szIP + " 开启云台失败！");
				var errorCode = WebVideoCtrl.I_GetLastError();
				alert("该设备暂无此功能");
			}
		});
	}
}

//方向PTZ停止
function mouseUpPTZControl() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

	if (oWndInfo != null) {
		WebVideoCtrl.I_PTZControl(1, true, {
			success: function (xmlDoc) {
				showOPInfo(oWndInfo.szIP + " 停止云台成功！");
			},
			error: function () {
				showOPInfo(oWndInfo.szIP + " 停止云台失败！");
			}
		});
	}
}



//关于监控直播接口
function previewCtrl(ip,port,username,password){
	var paramJson = {
			"userId":"",
			"ip":ip,
			"port":port,
			"username":username,
			"password":password
	}
	$.post("../holderControl/preview",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
	});
}

//关于云台预置点点击事件   设置
/*$("#preset_btn1").click(function (){
	var index = $("#preset_insex").val();
	setPresetPoint(8,index);
});

//关于云台预置点点击事件   转到
$("#preset_btn2").click(function (){
	var index = $("#preset_insex").val();
	setPresetPoint(39,index);
});*/

//关于云台预置点点击事件   清除
$("#preset_btn3").click(function (){
	var index = $("#preset_index").val();
	setPresetPoint(9,1);
});

//关于云台预置点接口
function setPresetPoint(command,index){
	//command：命令（8：设置预置点，9：清除预置点，39：转到预置点）
	//index：预置点的序号（从1开始），最多支持300个预置点
	var paramJson = {
			"userId":1,
			"command":command,
			"index":index
	}
	$.post("../holderControl/presetCtrl",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
	});
}

//设置预置点
function clickSetPreset() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		iPresetID = parseInt($("#preset_index").val(), 10);

	if (oWndInfo != null) {
		WebVideoCtrl.I_SetPreset(iPresetID, {
			success: function (xmlDoc) {
				showOPInfo(oWndInfo.szIP + " 设置预置点成功！");
				console.log("设置预置点成功");
			},
			error: function () {
				showOPInfo(oWndInfo.szIP + " 设置预置点失败！");
				console.log("设置预置点失败");
			}
		});
	}
}

//调用预置点
function clickGoPreset() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		iPresetID = parseInt($("#preset_index").val(), 10);

	if (oWndInfo != null) {
		WebVideoCtrl.I_GoPreset(iPresetID, {
			success: function (xmlDoc) {
				showOPInfo(oWndInfo.szIP + " 调用预置点成功！");
				console.log("调用预置点成功");
			},
			error: function () {
				showOPInfo(oWndInfo.szIP + " 调用预置点失败！");
				console.log("调用预置点失败");
			}
		});
	}
}

//删除预置点
function deletePreset(){
	var oWndInfo = WebVideoCtrl.I_SendHTTPRequest("192.168.3.3","/ISAPI/PTZCtrl/channels/1/absolute", {

	    async: false,

	    type: "PUT",

	    data: "<PTZData version='2.0' xmlns='http://www.isapi.org/ver20/XMLSchema'><AbsoluteHigh><elevation>270</elevation><azimuth>360</azimuth><absoluteZoom>10</absoluteZoom></AbsoluteHigh></PTZData>",    

	    success: function (text) {

	        showOPInfo("success - " + text);
	        console.log("成功！");
	    },

	    error: function (key, text) {

	        showOPInfo("error - " + key + " / " + text);
	        console.log("失败！");
	    }

	});
}








//关于云台巡航点击事件     添加到巡航组
$("#cruise_btn1").click(function (){
	var index = $("#cruise_index1").val();
	setCruise(30,1,1,1);
});

//关于云台巡航点击事件     从巡航组删除
$("#cruise_btn2").click(function (){
	var index = $("#cruise_index1").val();
	setCruise(33,1,1,1);
});

//关于云台巡航点击事件     设置巡航速度
$("#cruise_btn3").click(function (){
	var index = $("#cruise_index2").val();
	setCruise(32,1,1,1);
});

//关于云台巡航点击事件     设置巡航点停顿时间
$("#cruise_btn4").click(function (){
	var index = $("#cruise_index3").val();
	setCruise(31,1,1,1);
});

//关于云台巡航点击事件     开始巡航
$("#cruise_btn5").click(function (){
	setCruise(37,1,1,0);
});

//关于云台巡航点击事件     停止巡航
$("#cruise_btn6").click(function (){
	setCruise(38,1,1,1);
});

//关于云台巡航接口
function setCruise(command,byCruiseRoute,byCruisePoint,wInput){
	//userId:摄像头注册返回值
	//command：巡航命令（30：将预置点加入巡航序列，31：设置巡航点停顿时间，32：设置巡航速度，33：将预置点从巡航序列中删除，37：开始巡航，38：停止巡航）
	//byCruiseRoute：巡航路径，最多支持32条路径（序号从1开始）
	//byCruisePoint： 巡航点，最多支持32个点（序号从1开始）
	//wInput：不同巡航命令时的值不同，预置点(最大300)、时间(最大255)、速度(最大40)
	var paramJson = {
			"userId":1,
			"command":command,
			"byCruiseRoute":byCruiseRoute,
			"byCruisePoint":byCruisePoint,
			"wInput":wInput
	}
	$.post("../holderControl/cruiseCtrl",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
	});
}

//关于抓图点击事件    抓图
$("#capture_btn").click(function (){
	var picName = new Date().getTime();
	captureCtrl("C:\\Users\\user\\Desktop");
})

//关于抓图接口 
function captureCtrl(path){
	var paramJson = {
			"path":path,
	}
	$.post("../holderControl/captureCtrl",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
	});
}

// 抓图
function clickCapturePic() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(0),
		szInfo = "";
	if (oWndInfo != null) {
		var szChannelID = 1,
			/*szPicName = oWndInfo.szIP + "_" + szChannelID + "_" + new Date().getTime()+".jpg",*/
			szPicName = "C:\\Picture\\" + new Date().getTime()+".jpg";
			
		localInfo = WebVideoCtrl.I_GetLocalCfg();
		console.log(localInfo);	
		
		iRet = WebVideoCtrl.I_CapturePic(szPicName);
		console.log(iRet);
		if (0 == iRet) {
			szInfo = "抓图成功！";
		} else {
			szInfo = "抓图失败！";
			var errorCode = WebVideoCtrl.I_GetLastError()
			console.log(errorCode);
		}
		console.log(szInfo);
		showOPInfo(oWndInfo.szIP + " " + szInfo);
	}
}


//关于镜像点击事件    上下成像
$("#mirror_btn1").click(function(){
	mirrorCtrl(1,1,2);
})

//关于镜像点击事件    左右成像
$("#mirror_btn1").click(function(){
	mirrorCtrl(1,1,1);
})

//关于镜像接口
function mirrorCtrl(userId,channel,mirrorType){
	//userId:摄像头注册返回值
	//channel：通道号
	//mirrorType：镜像成像类型   镜像方式：0- 关闭，1- 左右，2- 上下，3- 中间 
	var paramJson = {
			"userId":userId,
			"channel":channel,
			"mirrorType":mirrorType,
	}
	$.post("../holderControl/captureCtrl",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
	});
}

$("#focus_btn1").click(function (){
	focusCtrl(1,1);
})

//关于聚焦接口
function focusCtrl(userId,channel){
	//userId:摄像头注册返回值
	//channel：通道号
	var paramJson = {
			"userId":userId,
			"channel":channel,
	}
	$.post("../holderControl/focusCtrl",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
	});
}










