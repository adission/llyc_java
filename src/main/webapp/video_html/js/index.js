$(function(){
    $("#header").load("template/header.html");
    $("#footer").load("template/footer.html");
    changeModel(4); //默认分4屏    //tips：这里默认分几屏，从getDefaultScreenTypeAndIsImportant()中获取
    
    //获取系统设置的默认巡航模式和分屏模式
	getDefaultScreenTypeAndIsImportant();
});

//-----------------------------------------------------------------------------
//-----云台控制------------------------------------------------------------------
//-----------------------------------------------------------------------------
//param: int userId:摄像头注册用户唯一id
// int command:云台控制命令21:上仰,22下俯,23:左转,24:右转,25:上仰和左转,26:上仰和右转,27:下附和左转,28:下附和右转,29:左右自动扫描
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
 
  var data = {'userId':'','command':command,'token':getCookie('token')};
  $.ajax({
	  type:'POST',
	  url:'../holderControl/holderCtrl',
	  data: {"paramJson":JSON.stringify(data)},
	  success:function(data){
		  console.log(data);
	  }
  });
});

// 关于监控直播接口
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

// 关于云台预置点点击事件   设置
$("#preset_btn1").click(function (){
	var index = $("#preset_insex").val();
	setPresetPoint(8,index);
});

// 关于云台预置点点击事件   转到
$("#preset_btn2").click(function (){
	var index = $("#preset_insex").val();
	setPresetPoint(39,index);
});

// 关于云台预置点点击事件   清除
$("#preset_btn3").click(function (){
	var index = $("#preset_insex").val();
	setPresetPoint(9,index);
});

// 关于云台预置点接口
function setPresetPoint(command,index){
	//command：命令（8：设置预置点，9：清除预置点，39：转到预置点）
	//index：预置点的序号（从1开始），最多支持300个预置点
	var paramJson = {
			"userId":"",
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

// 关于云台巡航点击事件     添加到巡航组
$("#cruise_btn1").click(function (){
	var index = $("#cruise_index1").val();
	setCruise(30,1,1,index);
});

// 关于云台巡航点击事件     从巡航组删除
$("#cruise_btn2").click(function (){
	var index = $("#cruise_index1").val();
	setCruise(33,1,1,index);
});

// 关于云台巡航点击事件     设置巡航速度
$("#cruise_btn3").click(function (){
	var index = $("#cruise_index2").val();
	setCruise(32,1,1,index);
});

// 关于云台巡航点击事件     设置巡航点停顿时间
$("#cruise_btn4").click(function (){
	var index = $("#cruise_index3").val();
	setCruise(31,1,1,index);
});

// 关于云台巡航点击事件     开始巡航
$("#cruise_btn2").click(function (){
	setCruise(37,1,1,0);
});

// 关于云台巡航点击事件     停止巡航
$("#cruise_btn2").click(function (){
	setCruise(38,1,1,1);
});

// 关于云台巡航接口
function setCruise(command,byCruiseRoute,byCruisePoint,wInput){
	//userId:摄像头注册返回值
	//command：巡航命令（30：将预置点加入巡航序列，31：设置巡航点停顿时间，32：设置巡航速度，33：将预置点从巡航序列中删除，37：开始巡航，38：停止巡航）
	//byCruiseRoute：巡航路径，最多支持32条路径（序号从1开始）
	//byCruisePoint： 巡航点，最多支持32个点（序号从1开始）
	//wInput：不同巡航命令时的值不同，预置点(最大300)、时间(最大255)、速度(最大40)
	var paramJson = {
			"userId":"",
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

// 关于抓图点击事件    抓图
$("#capture_btn").click(function (){
	captureCtrl(1,1,"");
})

// 关于抓图接口 
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

// 关于镜像点击事件    上下成像
$("#mirror_btn1").click(function(){
	mirrorCtrl(1,1,2);
})

// 关于镜像点击事件    左右成像
$("#mirror_btn1").click(function(){
	mirrorCtrl(1,1,1);
})

// 关于镜像接口
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

// 关于聚焦接口
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



//-----------------------------------------------------------------------------
//-----视频控制------------------------------------------------------------------
//-----------------------------------------------------------------------------

//-----------------------------chenyan end-------------------------------------------------------------


var is_important = 0;	//巡航模式（1：代表重点，2：代表所有）
var screen_type = 0;	//分屏模式（1：代表1*1 2：代表2*2 3：代表3*3 4：代表4*4）
var group_id = "";		//当前分组id
var group_list = {};	//当前巡航模式和分屏模式下的所有组列表
var screen_list = {};	//当前模式下的所有屏列表
var screen_sort = 1;	//当前显示在第几屏
var vidicon_list = {};	//屏幕当前要显示的摄像头列表
var screen_id = "";		//静态显示时，某个屏
	
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
				if(res.code == 200){
					console.log("该巡航模式该分屏模式下的所有组列表：");
					console.log(res.data);
					group_list = res.data;
				}else{
					alert(res.message);
				}
			}
	);
}

//获取某种分屏模式下的所有屏
function getScreenListByScreenType(){
	if(screen_type == 1){
		alert("111");
	}else{
		alert("222");
		var paramJson = {
				"screen_type":screen_type
		};
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
						screen_list = res.data;
					}else{
						alert(res.message);
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

////点击某个摄像头，获取该屏外其他的所有摄像头列表
//function getOtherVidiconListByGroupIdAndScreenSort(){
//	var paramJson = {
//			"group_id":group_id,		//分组id
//			"screen_sort":screen_sort	//第几屏
//	};
//	$.post(
//			"../groupScreenVidicon/getOtherVidiconListByGroupIdAndScreenSort",
//			{
//				"paramJson": JSON.stringify(paramJson),
//				"token": getCookie('token')
//			},
//			function(res){
//				if(res.code == 200){
//					console.log("该屏外其他的所有摄像头列表：" );
//					console.log(res.data);
//					screen_list = res.data;
//				}else{
//					alert(res.message);
//				}
//			}
//	);
//}



//-----------------------------chenyan end-------------------------------------------------------------


//渲染分屏结构
function changeModel(model){
    var videoDiv=$('.video-content');
    videoDiv.empty();
    var modelDiv = $('<div class="modelDiv"></div>')
    switch(Number(model)){

        case 1:
            var fpDiv = $('<div class="fp-box fp-1-1"><img src="images/2.png" /></div>');
            modelDiv.append(fpDiv)
            break;
        case 4:
            for (var i=1;i<5;i++){
                var fpDiv = $('<div class="fp-box fp-4-'+i+' "><img src="images/2.png" /></div>');
                modelDiv.append(fpDiv)
            }
            break;
        case 9:
            var modelDiv = $('<div class="modelDiv"></div>')
            for (var i=1;i<10;i++){
                var fpDiv = $('<div class="fp-box fp-9-'+i+' "><img src="images/2.png" /></div>');
                modelDiv.append(fpDiv)
            }
            break;
        case 13:
            var modelDiv = $('<div class="modelDiv"></div>')
            for (var i=1;i<14;i++){
                var fpDiv = $('<div class="fp-box fp-13-'+i+' "><img src="images/2.png" /></div>');
                modelDiv.append(fpDiv)
            }
            break;
        case 16:
            for (var i=1;i<17;i++){
                var fpDiv = $('<div class="fp-box fp-16-'+i+' "><img src="images/2.png" /></div>');
                modelDiv.append(fpDiv)
            }
            break;
    }
    videoDiv.append(modelDiv);

};

//选中某个摄像头
$('.video-content').on('click', '.fp-box', function(){
    var $fpDiv =$(this) ; //jq对象
    $(this).css("border","2px solid red").siblings().css("border","none"); 
    //自定义右击菜单
    $(this).bind("contextmenu",function(e){
        windowwidth = $(window).width();
        windowheight = $(window).height();
        checkmenu = 1;
        $('#mask').css({
            'height': windowheight-131,
            'width':  windowwidth
        });
        $('#myMenu').show();
        $('#myMenu').css({
            'top':(e.pageY -67) +'px',
            'left':(e.pageX -285) +'px'
        });
        return false;
    });
    $('#mask').click(function(){
            $(this).height(0);
            $(this).width(0);
            $('#myMenu').hide();
            checkmenu = 0;
            return false;
        });
    $('#mask').bind("contextmenu",function(){
        $(this).height(0);
        $(this).width(0);
        $('#myMenu').hide();
        checkmenu = 0;
        return false;
    });
    $(window).resize(function(){
        if(checkmenu == 1) {
            windowwidth = $(window).width();
            windowheight = $(window).height();
            $('#mask').css({
                'height':document.body.clientHeight,
                'width': document.body.clientWidth
            });
        }
    });
});

//双击放大
$('.video-content').on('dblclick', '.fp-box', function(){
    var $fpDiv =$(this) ; //jq对象
    if ($fpDiv.hasClass("full-screen")){
        $fpDiv.removeClass("full-screen");
    }else{
        $fpDiv.addClass("full-screen");
    }
});

//点击全屏
$('.btn-fullscreen').click(function(){
    if($("#video-content").is(":visible")){
        elem = document.getElementById('video-content');
    }
    fullscreen(elem);
});

//全屏方法
var fullscreen=function(elem){

    elem.classList.add('full-screen');

    if(elem.webkitRequestFullScreen){
        elem.webkitRequestFullScreen();
    }else if(elem.mozRequestFullScreen){
        elem.mozRequestFullScreen();
    }else if(elem.requestFullScreen){
        elem.requestFullscreen();
    }else{
        //浏览器不支持全屏API或已被禁用
    }
}

//监听窗口大小变化
window.onresize = function(){
    if(!checkFull()){
        //要执行的动作
        $('#video-content').removeClass('full-screen');
//            $('.fp-box').removeClass('full-screen');


    }
}

//检查是否全屏
function checkFull(){
    var isFull =  document.fullscreenEnabled || window.fullScreen || document.webkitIsFullScreen || document.msFullscreenEnabled;

    //to fix : false || undefined == undefined
    if(isFull === undefined) isFull = false;
    return isFull;
}
