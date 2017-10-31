// 初始化插件

// 全局保存当前选中窗口
var g_iWndIndex = 0; //可以不用设置这个变量，有窗口参数的接口中，不用传值，开发包会默认使用当前选择窗口
function start(ip,port,username,password){
	
//	
//	var IP="192.168.3.2";
//	var Port = "80";
//	var Username = "admin";
//	var Password = "llyc123456";
	clickLogin(ip,port,username,password);
}
 

<!--通过摄像头的IP，端口，用户名，密码登录-->
function clickLogin(IP,Port,Username,Password) {
	var szIP = IP,
		szPort = Port,
		szUsername = Username,
		szPassword = Password;

	if ("" == szIP || "" == szPort) {
		return;
	}

	var iRet = WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
		success: function (xmlDoc) {  // 登录成功
			clickStartRealPlay(szIP);
		},
		error: function () {
		}
	});
	// 已登录
	if (-1 == iRet) {
		clickStartRealPlay(szIP);
	}
}

//通过摄像头的IP，码流，通道......开始预览
function clickStartRealPlay(IP) {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		szIP = IP,//IP
		iStreamType = 1, //主码流
		bZeroChannel = false,
		szInfo = "",
	    iChannelID =1;//通道1

	if ("" == szIP) {
		return;
	}

	if (oWndInfo != null) {// 已经在播放了，先停止
		WebVideoCtrl.I_Stop();
	}

	var iRet = WebVideoCtrl.I_StartRealPlay(szIP, {
		iWndIndex:0,
		iStreamType: iStreamType,
		iChannelID: iChannelID,
		bZeroChannel: bZeroChannel
	});
	

	if (0 == iRet) {
		szInfo = "开始预览成功！";
		$('.icon').show();
	} else {
		szInfo = "开始预览失败！";
	}

	showOPInfo(szIP + " " + szInfo);
}
//显示操作信息
function showOPInfo(szInfo) {
	szInfo = "<div>" + dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss") + " " + szInfo + "</div>";
	$("#opinfo").html(szInfo + $("#opinfo").html());
}
//显示回调信息
function showCBInfo(szInfo) {
	szInfo = "<div>" + dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss") + " " + szInfo + "</div>";
	$("#cbinfo").html(szInfo + $("#cbinfo").html());
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

$('#show').click(function(){
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
	WebVideoCtrl.I_InitPlugin("200", "200", {
        bWndFull: true,//是否支持单窗口双击全屏，默认支持 true:支持 false:不支持
        iWndowType: 1,
		cbSelWnd: function (xmlDoc) {
			g_iWndIndex = $(xmlDoc).find("SelectWnd").eq(0).text();
			var szInfo = "当前选择的窗口编号：" + g_iWndIndex;
			showCBInfo(szInfo);
		}
	});
	WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin");

	// 检查插件是否最新
	if (-1 == WebVideoCtrl.I_CheckPluginVersion()) {
		alert("检测到新的插件版本，双击开发包目录里的WebComponentsKit.exe升级！");
		return;
	}
	
	start();
});

$("#close").click(function(){
	$("#divPlugin").remove();
	$('.icon').hide();
	$("#videoParent").append('<div id="divPlugin" class="plugin" ></div>');
});

