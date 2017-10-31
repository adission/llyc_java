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
var polling_time = "";  //间隔时间
var turnOrStatic = 1;   //当前处于状态（1：正在轮循，2：静态显示）

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
					polling_time = res.data[0]["polling_time"]*1000;
					console.log("系统设置的默认巡航模式:");
					console.log(is_important);
					console.log("系统设置的默认分屏模式:");
					console.log(screen_type);
					console.log("系统设置的时间:");
					console.log(polling_time);
					//调用插件的分屏接口
					WebVideoCtrl.I_ChangeWndNum(screen_type);
					//1、通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的默认组id
					getDefaultGroupIdByscreenTypeAndIsImportant();
					//2、通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的所有组列表
					getGroupListByScreenTypeAndIsImportant();
					//3、获取某种分屏模式下的所有屏
					getScreenListByScreenType();					
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
									
					if(turnOrStatic = 1){
						//轮播状态下，开始轮循
						goRound(group_id);
					}else{
						//静态显示下，查询默认组第一屏
						getScreenVidiconListByGroupIdAndSort(1);
					}
					
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
				console.log("该巡航模式该分屏模式下的所有组列表：");
				console.log(res);
				if(res.code == 200){
					group_list = res.data;
					var $turnUl=$(".turn ul"),
						htm = '';
					for(i=0; i<group_list.length; i++){
						htm += '<li  gid="'+group_list[i].id+'" style="top:'+i*40+'px;"><a>'+group_list[i].group_name;
						if(group_list[i].whether_important == 1){
							htm +='<span style="color: #ff0000;margin-left: 5px;">*&nbsp;</span>';
						}
					}
					htm += '</a></li><iframe frameborder=0 scrolling=no style="background-color:transparent; position: absolute; z-index: 99; width: 100%; height: 100%; top: 0;left:0;"></iframe>';
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
						htm += '<li  sid="'+cameraList[i].id+'" style="top:'+i*40+'px;"><a>'+cameraList[i].vidicon_name;
						if(cameraList[i].whether_important){
							htm +='<span style="color: #ff0000;margin-left: 5px;">*&nbsp;</span>';
						}
					}
					htm += '</a></li><iframe frameborder=0 scrolling=no style="background-color:transparent; position: absolute; z-index: 99; width: 100%; height: 100%; top: 0;left:0;"></iframe>';
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
							htm += '<li  sid="'+screen_list[i].screen_id+'" style="top:'+i*40+'px;"><a>'+screen_list[i].screen_name;
							if(screen_list[i].is_important == 1){
								htm +='<span style="color: #ff0000;margin-left: 5px;">*&nbsp;</span>';
							}
						}
						htm += '</a></li><iframe frameborder=0 scrolling=no style="background-color:transparent; position: absolute; z-index: 99; width: 100%; height: 100%; top: 0;left:0;"></iframe>';
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
					vidicon_list = res.data;
					var ids = [];
					for(var i=0;i<res.data.length;i++){
						ids.push(res.data[i].vidicon_id);
					}
					console.log("某组第"+screen_sort+"屏下的摄像头ids：");
					console.log(ids);
					//通过摄像头id获取摄像头的IP，端口，用户名，密码
					getIPAndPortById(ids);
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






//-----------------------------chenyan end-------------------------------------------------------------

//通过摄像头id获取摄像头的IP，端口，用户名，密码
function getIPAndPortById(ids){
	var paramJson = {
			"id":ids
	};
	$.post(
			"../vidicon/getVidiconPartField",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				console.log(res);
				if(res.code == 200){
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
	var szIP = IP,
		szPort = Port,
		szUsername = Username,
		szPassword = Password;

	if ("" == szIP || "" == szPort) {
		return;
	}
	var iRet = WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
		success: function (xmlDoc) {
			showOPInfo(szIP + " 登录成功！");
			clickStartRealPlay(szIP,i);
		},
		error: function () {
			showOPInfo(szIP + " 登录失败！");
		}
	});

	if (-1 == iRet) {
		console.log(" 登录shibai！"+IP);
		showOPInfo(szIP + " 已登录过！");
		clickStartRealPlay(szIP,i);
	}
}

//通过摄像头的IP，码流，通道......开始预览
function clickStartRealPlay(IP,i) {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
	iWndIndex = i+1,//播放窗口
	szIP = IP,//IP
	iStreamType = 2, //主码流
	bZeroChannel = true,
	szInfo = "",
    iChannelID =1;//通道1

	if ("" == szIP) {
		return;
	}
	
	if (oWndInfo != null) {// 已经在播放了，先停止
		WebVideoCtrl.I_Stop();
	}
	console.log("第"+i+"次-----"+iChannelID+"-----"+iWndIndex+"------"+iStreamType+"------"+bZeroChannel);
	var iRet = WebVideoCtrl.I_StartRealPlay(szIP, {
		iWndIndex:iWndIndex,
		iStreamType: iStreamType,
		iChannelID: iChannelID,
		bZeroChannel: bZeroChannel
	});
	
	if (0 == iRet) {
		console.log("开始预览成功！---------"+IP);
		szInfo = "开始预览成功！";
	} else {
		szInfo = "开始预览失败！";
		var errorCode = WebVideoCtrl.I_GetLastError()
		console.log("预览失败错误码-----"+IP+"-----"+errorCode);
		clickStartRealPlay("192.168.3.2",0);
	}
	
	showOPInfo(szIP + " " + szInfo);
}




//窗口分割数
function changeWndNum(iType) {
	screen_type = iType;
	iType = parseInt(iType, 10);
	//调用插件的分屏接口
	WebVideoCtrl.I_ChangeWndNum(iType);
		
	//通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的所有组列表
	getGroupListByScreenTypeAndIsImportant();
	//获取某种分屏模式下的所有屏
	getScreenListByScreenType();
	//通过分屏模式和巡航模式，获取某种分屏模式下、某种巡航模式下的默认组id
	getDefaultGroupIdByscreenTypeAndIsImportant();

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
	//开始轮循
	goRound(group_id);
});

//轮循
function goRound(id){
	turnOrStatic = 1;
	//清除定时器
	window.clearInterval(timer);
	//获取该组下的第一屏
	screen_sort = 1;
	getScreenVidiconListByGroupIdAndSort(screen_sort);
	//获取该组下的屏个数
	getScreenListByGroupId();
	
	console.log("屏个数"+ screen_length);
	
	//定时器  每隔多少时间请求下一屏
	/*timer = window.setInterval(function(){
		//获取该组下的第一屏
		screen_sort = screen_sort+1;
		//当到最后一屏时，再次返回第一屏
		if(screen_sort > screen_length){
			screen_sort = 1;
		}
		getScreenVidiconListByGroupIdAndSort(screen_sort);
	},polling_time); */
	
	
	$(".turn button").addClass("btn-info");
	$(".static button").removeClass("btn-info");
}

//选择静态显示屏
$(".static").on('click','li',function(){
	turnOrStatic = 2;
	//清除定时器
	window.clearInterval(timer);
	 
	var id = $(this).attr("sid")
	if(screen_type == 1){
		id = [id];
		//通过摄像头id查询IP，端口
		getIPAndPortById(id);
	}else{
		//通过屏id查询所有的摄像头
		changeScreenId(id);
	}
	
	$(".static button").addClass("btn-info");
	$(".turn button").removeClass("btn-info");
});

//通过屏id查询所有的摄像头
function changeScreenId(screen_id){
	//获取某个屏下的所有摄像头
	var paramJson = {
			"screen_id":screen_id
	};
	$.post(
			"../groupScreenVidicon/getScreenVidiconListByScreenId",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token')
			},
			function(res){
				if(res.code == 200){
					vidicon_list = res.data;
					var ids = [];
					for(var i=0;i<res.data.length;i++){
						ids.push(res.data[i].vidicon_id);
					}
					console.log("该屏下的摄像头ids：");
					console.log(ids);
					//通过摄像头id获取摄像头的IP，端口，用户名，密码
					getIPAndPortById(ids);
				}else{
					alert(res.message);
				}
			}
	);

}

//获取某个组下的所有屏列表的长度
function getScreenListByGroupId(){
	var paramJson = {
			"group_id":group_id,//分组id
	};
	$.post(
			"../groupScreenVidicon/getScreenListByGroupId",
			{
				"paramJson": JSON.stringify(paramJson),
				"token": getCookie('token'),
			},
			function(res){
				if(res.code == 200){
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
$('.direction i').click(function(){
switch($(this).index()){
  case 0://21:上仰
      command = 21;
      break;
  case 1://26:上仰和右转
      command = 26;
      break;
  case 2://24:右转
      command = 24;
      break;
  case 3://28:下附和右转
      command = 28;
      break;
  case 4://22下俯
      command = 22;
      break;
  case 5://27:下附和左转
      command = 27;
      break;
  case 6://23:左转
      command = 23;
      break;
  case 7://25:上仰和左转
      command = 25;
      break;
  default:
      break;
}

var data = {'userId':1,'command':command,'token':getCookie('token')};
$.ajax({
	  type:'POST',
	  url:'../holderControl/holderCtrl',
	  data: {"paramJson":JSON.stringify(data)},
	  success:function(data){
		  console.log(data);
	  }
});
});

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
$("#preset_btn1").click(function (){
	var index = $("#preset_insex").val();
	setPresetPoint(8,index);
});

//关于云台预置点点击事件   转到
$("#preset_btn2").click(function (){
	var index = $("#preset_insex").val();
	setPresetPoint(39,index);
});

//关于云台预置点点击事件   清除
$("#preset_btn3").click(function (){
	var index = $("#preset_insex").val();
	setPresetPoint(9,index);
});

//关于云台预置点接口
function setPresetPoint(command,index){
	//command：命令（8：设置预置点，9：清除预置点，39：转到预置点）
	//index：预置点的序号（从1开始），最多支持300个预置点
	var paramJson = {
			"userId":1,
			"command":command,
			"index":1
	}
	$.post("../holderControl/presetCtrl",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
	});
}

//关于云台巡航点击事件     添加到巡航组
$("#cruise_btn1").click(function (){
	var index = $("#cruise_index1").val();
	setCruise(30,1,1,0);
});

//关于云台巡航点击事件     从巡航组删除
$("#cruise_btn2").click(function (){
	var index = $("#cruise_index1").val();
	setCruise(33,1,1,0);
});

//关于云台巡航点击事件     设置巡航速度
$("#cruise_btn3").click(function (){
	var index = $("#cruise_index2").val();
	setCruise(32,1,1,0);
});

//关于云台巡航点击事件     设置巡航点停顿时间
$("#cruise_btn4").click(function (){
	var index = $("#cruise_index3").val();
	setCruise(31,1,1,0);
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
	captureCtrl(1,1,"");
})

//关于抓图接口 
function captureCtrl(userId,channel,picFileName){
	//userId:摄像头注册返回值
	//channel：通道号
	//picFileName：文件保存路径
	var paramJson = {
			"userId":userId,
			"channel":channel,
			"picFileName":picFileName,
	}
	$.post("../holderControl/captureCtrl",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
	});
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

