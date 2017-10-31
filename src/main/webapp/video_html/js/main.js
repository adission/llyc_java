//加载左侧
function showLeft(){
    //$(".content .content-left").width($(window).width()  * 0.15);                //屏幕分辨率的宽
    //$(".content .content-right").width($(window).width()  * 0.8 );                //屏幕分辨率的宽
    //$(".content .content-right").width($(window).width() - 350 );                //屏幕分辨率的宽
    //$(".content").width(window.screen.width * 0.97);
    //$('.header').width(window.screen.width);

    $('.content-left').load("tel/nav.html",function(){
    	//云台控制
    	cloudControl();
    	
        $( ".datepicker" ).datetimepicker({
            format: 'yyyy-mm-dd',
            inline: true,
            language: 'zh-CN',
            todayHighlight: true,
            minView : 2 ,  //	从月视图开始，选天
        }).on('changeDate',gotoDate);

        $('#datetimepicker3').datetimepicker({
            pickDate: false,
        });


        $subHeight=$(window).height() - $('.header').height() - 265;
        if($subHeight>460){
            $('.content-left .submenu').height($(window).height() - $('.header').height() - 265); //动态加载盒子高度
            //$('.content-right').height($(window).height() - $('.header').height() - 110); //动态加载盒子高度
            //console.log($('.content-right'));
        }else{
            $('.content-left .submenu').height(460);
            //$('.content-right').height(695);
        }



        var currentPage = window.location.href.split("/video_html/")[1];
        $(".content-left a").each(function(index, element) {
            var href = $(this).attr('href');
            if (href.indexOf(currentPage) >= 0) {
            	if(href=="video_grouping.html"){
            		$(this).parents(".submenu").parent("li").addClass("open");
                    $(this).parents(".submenu").show();
                    $(this).addClass("active");
            	}
                $(this).parent(".link").parent("li").addClass("open");
                $(this).parent(".link").next().show();
                $(this).addClass("active");
            }else{
                $(this).parent(".link").parent("li").removeClass("open");
                $(this).parent(".link").next().hide();
            }
          
        });
        var Accordion = function(el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;

            // Variables privadas
            var links = this.el.find('.link');
            // Evento
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
        };
        Accordion.prototype.dropdown = function(e) {
            var $el = e.data.el;
            $this = $(this),
            $next = $this.next();

            $next.slideToggle();
            $this.parent().toggleClass('open');

            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp().parent().removeClass('open');
            };
        };

        var accordion = new Accordion($('#accordion'), false);
    });
}
//加载头部
function showHeader(){
    $('.header').load("tel/header.html",function(){
        $('.header-img').click(function(){

            // 如果是小屏点击不响应
            if($('body').width()>=991) {
                //如果是大导航变成小导航
                if($('.content-right').hasClass("col-md-10")){
                    $(".header-logo").hide();
                    $(".header-img").css("margin-left",50);
                    $('.content-right').removeClass("col-md-10").addClass("col-md-11");
                    $('.content-left').removeClass("col-md-2").addClass("col-md-1");

                    $('.accordion>li>i').css("left","35%");
                    $('div.link span').hide();
                    $('div.link i').hide();
                    $('ul.submenu').addClass("small");
                    //$('ul.submenu').hide();
                    //
                    //$('ul.submenu.small').toggle('left',"108%");
                }else {
                    $(".header-logo").show();
                    $(".header-img").css("margin-left",0);
                    $('.content-right').removeClass("col-md-11").addClass("col-md-10");
                    $('.content-left').removeClass("col-md-1").addClass("col-md-2");

                    $('.accordion>li>i').css("left",15);
                    $('div.link span').show();
                    $('div.link i').show();
                    $('ul.submenu').removeClass("small");
                }
            }else if($('body').width()<=768){
                //$(".header-logo").show();
                $('#accordion').toggle();

                if($('body').width()<=550){
                    $('.content-left').css('width',200);
                    $(".content-right").css("width",550);
                    $(".content-right").removeClass("col-xs-12")
                }else {
                    $('.content-left').addClass("pull-left");
                    $('.content-left').toggleClass("col-xs-3");
                    $(".content-right").toggleClass("col-xs-12").toggleClass("col-xs-9");
                }
            }

            $('.dataTables_scrollHeadInner').width("100%");;
            $('.dataTables_scrollHeadInner table').width("100%");
        });



        // 个人资料
        $('.dropdown img').click(function(){
            if($('li.dropdown').hasClass("open")){
                $("li.dropdown").removeClass('click');
            }else {
                $("li.dropdown").addClass('click');
            }
        });
        //任意地点的单击事件 取消其click class属性
        $(document).click(function(){
            if(!$('li.dropdown').hasClass("open")){
                $("li.dropdown").removeClass('click');
            }
        });

    });
}

function gotoDate(ev){

	
var Date = ev.date.getFullYear().toString() + "-"+ (ev.date.getMonth()+1).toString()+ "-"+ ev.date.getDate().toString();
    alert(Date);
    //window.location.href = "XXXX.html" + "?Date=" + ev.date.getFullYear().toString() + "-"+ (ev.date.getMonth()+1).toString()+ "-"+ ev.date.getDate().toString();
}


var urlpath = "http://localhost:8080/lanlyc/";

//cookie操作
function setCookie(name,value){
	var Days = 100000;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString()+ ";path=/";
}
function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}
function delCookie(name){
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval=getCookie(name);
	if(cval!=null){
		document.cookie= name + "="+''+";expires="+exp.toGMTString();
	}
}
//退出登录
function loginOut(){
	$.post(urlpath+'User/logout',{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			location.href="login.html";
		}else if(res.code==2) {
			location.href="login.html";
		}
	});
} 

//datatables 默认参数
var CONSTANT = {
    DATA_TABLES : {
        DEFAULT_OPTION : { //DataTables初始化选项
            "scrollY": false,//dt高度
            //"lengthMenu": [
            //    [10, 2, 10, -1],
            //    [1, 2, 10, "All"]
            //],//每页显示条数设置
            "lengthChange": false,//是否允许用户自定义显示数量
            "bPaginate": true, //翻页功能
            "bFilter": false, //列筛序功能
            "searching": false,//本地搜索
            "ordering": false, //排序功能
//            "Info": true,//页脚信息
            "paging": true,
            "serverSide": true,   //启用服务器端分页

            "autoWidth": true,//自动宽度
            "bDestroy": true,
            "oLanguage": {//国际语言转化
                "oAria": {
                    "sSortAscending": " 以升序排列此列",
                    "sSortDescending": " 以降序排列此列"
                },
                "sLengthMenu": "显示 _MENU_ 记录",
                "sZeroRecords": "对不起，查询不到任何相关数据",
                "sEmptyTable": "未有相关数据",
                "sLoadingRecords": "正在加载数据-请等待...",
                "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录。",
                "sInfoEmpty": "当前显示0到0条，共0条记录",
                "sInfoFiltered": "（数据库中共为 _MAX_ 条记录）",
                "sProcessing": "<img src='../resources/user_share/row_details/select2-spinner.gif'/> 正在加载数据...",
                "sSearch": "模糊查询：",
                "sUrl": "",
                //多语言配置文件，可将oLanguage的设置放在一个txt文件中，例：Javascript/datatable/dtCH.txt
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": " 上一页 ",
                    "sNext": " 下一页 ",
                    "sLast": " 尾页 ",
                    "sJump": "跳转"
                }
            },
            "order": [
                [0, null]
            ],//第一列排序图标改为默认
        },
        COLUMN: {
            CHECKBOX: {	//复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck" name="checkList">';
                }
            },
            // 全局按钮列样式
            BUTTONS: {
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return ' <button type="button" class="btn btn-small btn-danger btn-del" onclick="del()">删除</button>';
                }
            }
        },
        RENDER: {	//常用render可以抽取出来，如日期时间、头像等
            ELLIPSIS: function (data, type, row, meta) {
                data = data||"";
                return '<span title="' + data + '">' + data + '</span>';
            },
            // 常用编辑按钮
            BUTTONDS: function(data, type, row, meta){
                data = data||"";
                return '<span title="' + data + '">' + data + '</span>';
            }


        }
    }
};
//操作提示
function tool(flag,sume,errme){
	if(flag){
		$('.alert-success').html(sume);
		$('.alert-success').show(300).delay(1000).hide(300);
		table.ajax.reload(null,false);
	}else {
		$('.alert-danger').html(errme);
		$('.alert-danger').show(300).delay(1000).hide(300);
	}
}
//function tool2(flag,id,sume,errme){
//	if(flag){
//		$('#'+id).modal("hide");
//		$('.alert-success').html(sume);
//		$('.alert-success').show(300).delay(1000).hide(300);
//		table.ajax.reload(null,false);
//	}else {
//		$('#'+id).modal("hide");
//		$('.alert-danger').html(errme);
//		$('.alert-danger').show(300).delay(1000).hide(300);
//	}
//}

//alert 提示
function alertTool(mess){
	$.alert({
        title: false,
        content: mess,
        opacity: 0.5,
        confirmButton: '好',
    });
}

//确认提示
function sureTool(mes,name,fun,id){
	$.confirm({
        title: false,
        content: mes+': '+name+'  ?',
        confirmButton: '确定',
        confirmButtonClass: 'btn-primary',
        //autoClose: 'cancel|5000',
        cancelButton: '取消',
        icon: 'fa fa-question-circle',
        animation: 'scale',
        animationClose: 'top',
        opacity: 0.5,
        confirm: function () {
        	fun(id);
        }
    });
}
//删除确认提示
function delTool(name,fun,id){
	$.confirm({
        title: false,
        content: '确定删除: '+name+'  ?',
        confirmButton: '确定',
        confirmButtonClass: 'btn-primary',
        //autoClose: 'cancel|5000',
        cancelButton: '取消',
        icon: 'fa fa-question-circle',
        animation: 'scale',
        animationClose: 'top',
        opacity: 0.5,
        confirm: function () {
        	fun(id);
        }
    });
}


//云台控制
//param: int userId:摄像头注册用户唯一id
// int command:云台控制命令21:上仰,22下俯,23:左转,24:右转,25:上仰和左转,26:上仰和右转,27:下附和左转,28:下附和右转,29:左右自动扫描
function cloudControl(){
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
 
  var paramJson = {'userId':'','command':command,'token':getCookie('token')};
  $.ajax({
	  type:'POST',
	  url:'../holderControl/holderCtrl',
	  data: JSON.stringify(paramJson),
	  success:function(data){
		  console.log(data);
	  }
  });
});
}



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

