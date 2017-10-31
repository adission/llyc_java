//-----------------------------------------------------------------------------
//-----视频控制------------------------------------------------------------------
//-----------------------------------------------------------------------------

var screen_type = 0;	  //分屏模式（1：代表1*1 2：代表2*2 3：代表3*3 4：代表4*4）
var cameraList = "";      //摄像头列表
var cameraLength = 0;     //摄像头总个数
var polling_time = 0;     //轮巡间隔时间
var one_group_id = "";    //1*1默认组id
var two_group_id = "";    //2*2默认组id
var three_group_id = "";  //3*3默认组id
var four_group_id = "";   //4*4默认组id
var group_id = "";        //当前默认组
var screen_sort = 0;      //当前屏
var screen_length = "";   //当前默认组屏个数
var timer = "";           //定时器




/*var draw = 0;           //当前页
var g_iWndIndex = 0;
var start = 0;          //当前页的起始index
var sign = 0;
var start = 0;
*/
$(function(){
	//ajax设置同步
	$.ajaxSetup({async : false }); 
	
	// 检查插件是否已经安装过
    var iRet = WebVideoCtrl.I_CheckPluginInstall();
	if (-2 == iRet) {
		alert("您的Chrome浏览器版本过高，不支持NPAPI插件！");
		return;
	} else if (-1 == iRet) {
        alert("您还未安装过插件，双击开发包目录里的WebComponentsKit.exe安装！");
		return;
    }


    var windowHeight=($(document).height()-131)*0.9;
	// 初始化插件参数及插入插件
	WebVideoCtrl.I_InitPlugin("100%", windowHeight, {
        bWndFull: true,//是否支持单窗口双击全屏，默认支持 true:支持 false:不支持
        iWndowType: 2,
		cbSelWnd: function (xmlDoc) {
			g_iWndIndex = $(xmlDoc).find("SelectWnd").eq(0).text();
			var szInfo = "当前选择的窗口编号：" + g_iWndIndex;
			console.log(szInfo);
		}
	});
	WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin");

	// 检查插件是否最新
	if (-1 == WebVideoCtrl.I_CheckPluginVersion()) {
		alert("检测到新的插件版本，双击开发包目录里的WebComponentsKit.exe升级！");
		return;
	}
	
	//获取摄像头列表
	getCameraList();
	//获取系统设置的默认分屏模式及间隔时间
	getDefaultScreenType();
});

//获取摄像头列表
function getCameraList(){
    var data = {
    		"token": getCookie('token'),
    		"whether_important":2,
    };
    $.post('../vidicon/getAllVidicon',{"paramJson": JSON.stringify(data)},function(ret){
        cameraList = ret.data;
        cameraLength = ret.data.length;
        var Li = '';
        var ids = [];
        for(var i=0; i<cameraLength; i++){
        	//获取摄像头id数组
        	ids.push(ret.data[i].id);
        	//拼接摄像头列表
        	Li += '<li id="'+ret.data[i].id+'"><i class="fa fa-video-camera"></i>'+ret.data[i].vidicon_name;
        	if(ret.data[i].whether_important){
        		Li +='<span style="color: #ff0000;margin-left: 5px;">*&nbsp;</span>';
			}
        	Li += '<i class="fa fa-play" id="'+ret.data[i].vidicon_ip+'" title="开始预览"></i><i class="fa fa-stop" title="停止预览"></i></li>';
        }
        $(".camera_list").html(Li);
        //批量登录摄像头
        getIPAndPortById(ids);    	
    })
}

//获取系统设置的默认分屏模式及间隔时间 及各个默认组id
function getDefaultScreenType(){
	$.post(
			"../sysetem/getBaseSetting",
			{
				"token": getCookie('token')
			},
			function(res){
				if(res.code == 200){
					one_group_id = "8723d53df318422d99b2dea11927d784";    //1*1默认组id
					two_group_id = "6d932fe78fba4543a31b5d574e9a29d5";    //2*2默认组id
					three_group_id = "4a48a66b43b74170a368190bffe22a31";  //3*3默认组id
					four_group_id = "da582f593b604360bfaffe0511f3f664";   //4*4默认组id
					
					screen_type = res.data[0]["screen_mode"];	     //分屏模式
					polling_time = res.data[0]["polling_time"]*1000; //巡航间隔时间
					console.log("系统设置的默认分屏模式:");
					console.log(screen_type);
					console.log("系统设置的时间:");
					console.log(polling_time);
					//切换分屏模式
					changeWndNum(screen_type);
					
				}else{
					alert(res.message);
				}
			}
	);
};




//点击摄像头
$(".camera_list").on("click",".fa",function(){
	var ip = $(this)[0].id;
	//开始预览
	if($(this).hasClass("fa-play")){
		g_iWndIndex = parseInt(g_iWndIndex, 10);
		clickStartRealPlay(ip,g_iWndIndex);
	}
	//停止预览
	if($(this).hasClass("fa-stop")){
		clickStopRealPlay();
	}

});


//静态显示
$(".static").click(function(){
	//清除定时器
	window.clearInterval(timer);
	
	var $span = $(this).children();
	if($span.hasClass("fa-caret-square-o-right")){
		//全部开始预览
		getScreenVidiconListByGroupIdAndSort(group_id,1);
		$span.removeClass("fa-caret-square-o-right").addClass("fa-stop");
	}else{
		//全部停止预览
		$span.removeClass("fa-stop").addClass("fa-caret-square-o-right");
		for(var i=0; i<screen_type*screen_type; i++){
			var oWndInfo = WebVideoCtrl.I_GetWindowStatus(i),
				szInfo = "";
	
			if (oWndInfo != null) {
				var iRet = WebVideoCtrl.I_Stop(i);
				if (0 == iRet) {
					szInfo = "停止预览成功！";
				} else {
					szInfo = "停止预览失败！";
				}
				console.log(oWndInfo.szIP + " " + szInfo);
			}
		}
	}
	
});


//开始轮循
function Turn(){
	//清除定时器
	window.clearInterval(timer);
	
	var screen_sort = 1;
	getScreenVidiconListByGroupIdAndSort(group_id,1);
	//定时器  每隔多少时间请求下一屏
	timer = window.setInterval(function(){
		//获取该组下的第一屏
		screen_sort = screen_sort+1;
		//当到最后一屏时，再次返回第一屏
		if(screen_sort > screen_length){
			screen_sort = 1;
		}
		getScreenVidiconListByGroupIdAndSort(group_id,screen_sort);
	},polling_time);
}

//上一页
function pre(){
	//清除定时器
	window.clearInterval(timer);
	
	screen_sort -= 1;
	if(screen_sort <= 0){
		screen_sort = screen_length;
	}
	getScreenVidiconListByGroupIdAndSort(group_id,screen_sort);
}
//下一页
function next(){
	//清除定时器
	window.clearInterval(timer);
	
	screen_sort += 1;
	if(screen_sort > screen_length){
		screen_sort = 1;
	}
	getScreenVidiconListByGroupIdAndSort(group_id,screen_sort);
}


//获取某个组下的第几屏
function getScreenVidiconListByGroupIdAndSort(group_id,screen_sort){
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
					for(var i=0;i<res.data.length;i++){
						var ip = res.data[i].vidicon_ip;
						clickStartRealPlay(ip,i);
					}
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


//换一屏
/*function changeScreen(){
	
	var size = screen_type;
	var length = screen_type*screen_type;
	var pingshu = Math.ceil(cameraLength / length);
	if(pingshu == sign){
    	start = 0;
    	sign = 0;
    }
    var param = {
    		"start":start,
			"size":size,
    };
    console.log("===========pingshu============="+pingshu+"--------"+length);
    getVidicon(param);
    sign += 1;
    start += length;
}

function getVidicon(param){
	$.post("../vidicon/getLimitVidicon",
			{
				"paramJson": JSON.stringify(param),
				"token": getCookie('token'),
			},
			function(res){
				console.log(res);
				for(var i=0;i<res.data.length;i++){
					var ip = res.data[i].vidicon_ip;
					console.log("ip:"+ip+"和i:"+i);
					clickStartRealPlay(ip,i);
				}
			})
}*/


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
				if(res.code == 200){
					for(var i=0;i<res.data.length;i++){
						var ip = res.data[i].vidicon_ip,
						    port = res.data[i].vidicon_port,
							username = res.data[i].vidicon_username,
							password = res.data[i].vidicon_password;
						clickLogin(ip,port,username,password);
					}
				}else{
					alert(res.message);
				}
			}
	);
}

//登录
function clickLogin(IP,Port,Username,Password) {
	var szIP = IP,
		szPort = Port,
		szUsername = Username,
		szPassword = Password;

	if ("" == szIP || "" == szPort) {
		return;
	}

	var iRet = WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
		success: function (xmlDoc) {
			console.log(szIP + " 登录成功！");
			
			setTimeout(function () {
				getChannelInfo(szIP);
			}, 10);
		},
		error: function () {
			console.log(szIP + " 登录失败！");
		}
	});

	if (-1 == iRet) {
		console.log(szIP + " 已登录过！");
	}
}


//获取通道
function getChannelInfo(szIP) {
	var szIP = szIP;

	if ("" == szIP) {
		return;
	}

	// 模拟通道
	WebVideoCtrl.I_GetAnalogChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("VideoInputChannel");

			$.each(oChannels, function (i) {
				var id = $(this).find("id").eq(0).text(),
					name = $(this).find("name").eq(0).text();
				if ("" == name) {
					name = "Camera " + (i < 9 ? "0" + (i + 1) : (i + 1));
				}
			});
			console.log(szIP + " 获取模拟通道成功！");
		},
		error: function () {
			console.log(szIP + " 获取模拟通道失败！");
		}
	});
	// 数字通道
	WebVideoCtrl.I_GetDigitalChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("InputProxyChannelStatus");

			$.each(oChannels, function (i) {
				var id = $(this).find("id").eq(0).text(),
					name = $(this).find("name").eq(0).text(),
					online = $(this).find("online").eq(0).text();
				if ("false" == online) {// 过滤禁用的数字通道
					return true;
				}
				if ("" == name) {
					name = "IPCamera " + (i < 9 ? "0" + (i + 1) : (i + 1));
				}
			});
			console.log(szIP + " 获取数字通道成功！");
		},
		error: function () {
			console.log(szIP + " 获取数字通道失败！");
		}
	});
	// 零通道
	WebVideoCtrl.I_GetZeroChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("ZeroVideoChannel");
			
			$.each(oChannels, function (i) {
				var id = $(this).find("id").eq(0).text(),
					name = $(this).find("name").eq(0).text();
				if ("" == name) {
					name = "Zero Channel " + (i < 9 ? "0" + (i + 1) : (i + 1));
				}
				if ("true" == $(this).find("enabled").eq(0).text()) {// 过滤禁用的零通道
				}
			});
			console.log(szIP + " 获取零通道成功！");
		},
		error: function () {
			console.log(szIP + " 获取零通道失败！");
		}
	});
}


//通过摄像头的IP，码流，通道......开始预览
function clickStartRealPlay(IP,i) {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(i),
		iWndIndex = i,
		szIP = IP,//IP
		iStreamType = 1, //主码流
		bZeroChannel = false,
		szInfo = "",
	    iChannelID =1;//通道1

	if ("" == szIP) {
		return;
	}
	console.log("----------当前窗口状态----------");
	console.log(oWndInfo);
	
	if (oWndInfo != null) {// 已经在播放了，先停止
		console.log(i+"窗口已经在播放");
		WebVideoCtrl.I_Stop(i);
	}

	var iRet = WebVideoCtrl.I_StartRealPlay(szIP, {
		iWndIndex : iWndIndex,
		iStreamType: iStreamType,
		iChannelID: iChannelID,
		bZeroChannel: bZeroChannel
	});

	if (0 == iRet) {
		console.log("开始预览成功！");
		szInfo = "开始预览成功！";
	} else {
		szInfo = "开始预览失败！";
		/*var errorCode = WebVideoCtrl.I_GetLastError()
		console.log("预览失败错误码----------"+errorCode);*/
	}

	console.log(szIP + " " + szInfo);
}


//停止预览
function clickStopRealPlay() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		szInfo = "";

	if (oWndInfo != null) {
		var iRet = WebVideoCtrl.I_Stop();
		if (0 == iRet) {
			szInfo = "停止预览成功！";
		} else {
			szInfo = "停止预览失败！";
		}
		console.log(oWndInfo.szIP + " " + szInfo);
	}
}


//窗口分割数
function changeWndNum(Type){
	//清除定时器
	window.clearInterval(timer);
	
	//获取默认组
	switch(Type){
		case 1:
			group_id = one_group_id;
			break;
		case 2:	
			group_id = two_group_id;
			break;
		case 3:	
			group_id = three_group_id;
			break;
		case 4:	
			group_id = four_group_id;
			break;
		default:
			break;
	};
	//获取组长度
	getScreenListByGroupId(group_id);
	//分屏
	screen_type = parseInt(Type, 10);
	WebVideoCtrl.I_ChangeWndNum(screen_type);
}


//全屏
function clickFullScreen() {
	WebVideoCtrl.I_FullScreen(true);
}

//右侧收起
$(".content-right-tip span").click(function(){
	if($(".content-right").is(":visible") ){
		$(".content-right").css("display","none");
		$(".content-right-tip").css("right","0px");
		$(".content-center").css("right","30px");
		
	}else{
		$(".content-right").css("display","block");
		$(".content-right-tip").css("right","270px");
		$(".content-center").css("right","300px");
	}
});




//-----------------------------------------------------------------------------
//-----云台控制------------------------------------------------------------------
//-----------------------------------------------------------------------------

function PTZZoomIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(10, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                console.log(" 调焦+成功！");
            },
            error: function () {
            	console.log("  调焦+失败！");
            }
        });
    }
}

function PTZZoomout() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(11, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
            	console.log(" 调焦-成功！");
            },
            error: function () {
            	console.log("  调焦-失败！");
            }
        });
    }
}

function PTZZoomStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(11, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
            	console.log(" 调焦停止成功！");
            },
            error: function () {
            	console.log("  调焦停止失败！");
            }
        });
    }
}

function PTZFocusIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(12, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
            	console.log(" 聚焦+成功！");
            },
            error: function () {
            	console.log("  聚焦+失败！");
            }
        });
    }
}

function PTZFoucusOut() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(13, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
            	console.log(" 聚焦-成功！");
            },
            error: function () {
            	console.log("  聚焦-失败！");
            }
        });
    }
}

function PTZFoucusStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(12, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
            	console.log(" 聚焦停止成功！");
            },
            error: function () {
            	console.log("  聚焦停止失败！");
            }
        });
    }
}

function PTZIrisIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(14, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
            	console.log(" 光圈+成功！");
            },
            error: function () {
            	console.log("  光圈+失败！");
            }
        });
    }
}

function PTZIrisOut() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(15, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
            	console.log(" 光圈-成功！");
            },
            error: function () {
            	console.log("  光圈-失败！");
            }
        });
    }
}

function PTZIrisStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(14, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
            	console.log(" 光圈停止成功！");
            },
            error: function () {
            	console.log("  光圈停止失败！");
            }
        });
    }
}


$(function(){
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
	
	
	//关于抓图点击事件    抓图
	$("#capture_btn").click(function (){
		captureCtrl("C:\\Users\\user\\Desktop");
	})
});


//PTZ控制 9为自动，1,2,3,4,5,6,7,8为方向PTZ
var g_bPTZAuto = false;
function mouseDownPTZControl(iPTZIndex) {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
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
				console.log(" 开启云台成功！");
			},
			error: function () {
				console.log(" 开启云台失败！");
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
				console.log(" 停止云台成功！");
			},
			error: function () {
				console.log(" 停止云台失败！");
			}
		});
	}
}



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

//抓图
function clickCapturePic() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		szInfo = "";

	if (oWndInfo != null) {
		var name = dateFormat(new Date(), "yyyyMMddHHmmss");
			szPicName = "C:\\Picture\\"+name,
			iRet = WebVideoCtrl.I_CapturePic(szPicName);
		if (0 == iRet) {
			szInfo = "抓图成功！";
		} else {
			szInfo = "抓图失败！";
			var errorCode = WebVideoCtrl.I_GetLastError();
			console.log("-----iRet00------"+iRet+"-----------"+errorCode);
		}
		console.log(szInfo);
	}
}


//设置预置点
function clickSetPreset() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		iPresetID = parseInt($("#preset_index").val(), 10);

	if (oWndInfo != null) {
		WebVideoCtrl.I_SetPreset(iPresetID, {
			success: function (xmlDoc) {
				console.log(" 设置预置点成功！");
			},
			error: function () {
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
				console.log("调用预置点成功");
			},
			error: function () {
				console.log("调用预置点失败");
			}
		});
	}
}



//格式化时间
function dateFormat(oDate, fmt) {
	var o = {
		"M+": oDate.getMonth() + 1, //月份
		"d+": oDate.getDate(), //日
		"h+": oDate.getHours(), //小时
		"m+": oDate.getMinutes(), //分
		"s+": oDate.getSeconds(), //秒
		"q+": Math.floor((oDate.getMonth() + 3) / 3), //季度
		"S": oDate.getMilliseconds()//毫秒
	};
	if (/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (oDate.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
}