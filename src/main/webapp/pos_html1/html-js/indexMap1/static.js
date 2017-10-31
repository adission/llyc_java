/**
 * Created by Administrator on 2017/9/13.
 */

var personTypeList = [];
var mapList = [];
$(function () {
	setMapId();
	
    showHeader();
    showProjectMess();
    $("#searchParam").val("");
    
    $('#left-panel-link').panelslider({clickClose: false});
    $('#map').width(document.body.clientWidth);
    $('#map').height(document.body.clientHeight);
    
    getMapList();
    
});

function setMapId(){
	$.post('../posfloor/getOneFloorInfo',{
		"paramJson":JSON.stringify({"token":getCookie("token")})
	},function(res){
		console.log(res)
//		if(res.code=="200"){
//			setCookie("mapId",res.data[0].id);
//			
//		}
	});
}

function getPersonList(id){
	var param = {
		"token": getCookie("token"),
		"layer_id": id
	};
	$.post("../PosUser/getUserInfo",{
		"paramJson": JSON.stringify(param)
	},function(res){
		if(res.code==200){
			personTypeList = res.data;
		}else {
			alertTool("数据加载错误，请刷新页面");
		}
	});

}
function getMapList(){
	$.post("../posfloor/getFloorInfo",{
		"paramJson": JSON.stringify({"token": getCookie("token")})
	},function(res){
		if(res.code==200){
			mapList = res.data;
			getPersonList(mapList[0].id);
			
		}else {
			alertTool("数据加载错误，请刷新页面");
		}
	});
}

// 弹出左侧导航
$('#left-panel-link').click(function () {
    $("#left-panel").load("tel/left.html", function () {
        $('#left-panel-link').click(function () {
        	clearInterval(times);
        	clearInterval(times1);
            $.panelslider.close();
        });
        
        var $wrapper = $('#div-table-container');
        var $wrapper1 = $('#accordion2');
        $wrapper.spinModal();
        $wrapper1.spinModal();
        
        times1 = setInterval(function(){
    		if( mapList.length>0 ){
    			$(".mapName").html(mapList[0].layer_name);
        		clearInterval(times1);
                for(var i=0;i<mapList.length;i++){
                	if(i==0){
                		var html = `<li class="activeMap">
        					<i class="fa fa-times delMap" title="删除图层" style="cursor: pointer;float: right; wiwdth: 20px; height: 20px; " onclick="deleteMap('${ mapList[i].id }','${mapList[i].layer_name}')"></i>
	                        <div class="directory-info-row" onclick="changeMap(this,'${ mapList[i].id }')">
	                            <div class="media">
	                                <a class="pull-left" >
	                                    <img class="thumb media-object" src="images/map_006.jpg" alt="">
	                                </a>
	                                <div class="media-body">
	                                    <address>
	                                        <strong>${ mapList[i].layer_name }</strong><br>
	                                        	在线人数： <strong>${ mapList[i].user_count }人</strong>
	                                    </address>
	                                    <button class="btn btn-info"  onclick="openModal('${ mapList[i].id }')">轨迹回放</button>
	                                </div>
	                            </div>
	                        </div>
	                    </li>`;
        	
                		$('.mapList').append(html);
                	}else {
                		var html = `<li>
        					<i class="fa fa-times delMap" title="删除图层" style="cursor: pointer;float: right; wiwdth: 20px; height: 20px; " onclick="deleteMap('${ mapList[i].id }','${mapList[i].layer_name}')"></i>
	                        <div class="directory-info-row" onclick="changeMap(this,'${ mapList[i].id }','${mapList[i].bjtx}')">
	                            <div class="media">
	                                <a class="pull-left" >
	                                    <img class="thumb media-object" src="images/map_006.jpg" alt="">
	                                </a>
	                                <div class="media-body">
	                                    <address>
	                                        <strong>${ mapList[i].layer_name }</strong><br>
	                                        	在线人数： <strong>${ mapList[i].user_count }人</strong>
	                                    </address>
	                                    <button class="btn btn-info"  onclick="openModal('${ mapList[i].id }')">轨迹回放</button>
	                                </div>
	                            </div>
	                        </div>
	                    </li>`;
        	
                		$('.mapList').append(html);
                	}
                }
            	$wrapper.spinModal(false);
        	}
        },10);
        
        personLoad($wrapper1);

    });
});
// 加载人员列表
function personLoad($wrapper1){
	times = setInterval(function(){
    	// 判断mapList的 长度，大于1，
    	if( personTypeList.length>0 ){
    		clearInterval(times);
			for(var i=0;i<personTypeList.length;i++){
				var html1= '';
            	html1 += `<div class="panel">
                                <div class="panel-heading dark">
                                    <h4 class="panel-title">
                                        <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne${ i }">
                                            <i class="fa fa-minus-square fa-lg"></i>${ personTypeList[i].work_name } (${ personTypeList[i].type_name.length })
                                        </a>
                                    </h4>
                                </div>
                                <div id="collapseOne${ i }" class="panel-collapse collapse" style="height: auto;">
                                    <div class="panel-body" style="padding: 0 15px">
                                        <ul class="list-group">
                                        </ul>
                                    </div>
                                </div>
                            </div>`;
            	
            	$('#accordion2').append(html1);
            	
            	for(var j=0;j<personTypeList[i].type_name.length;j++){
            		var color;
            		color = personTypeList[i].type_name[j].staff_visitor==0?outColor:workerColor;
            		
	    			var user = personTypeList[i].type_name[j];
	    			var html2 = `<li class="list-group-item" style="padding: 10px 5px;max-height: 32px">
	                            	<span style="width: 46px;display: inline-block;">${ user.name }</span>
	                                <span class="labelgroup">
	                                	<div class="btn-group btnIcon">
	                                        <button class="btn btn-default" type="button"><i class="fa  fa-map-marker" style="color: ${ color }"  title="标签颜色"></i></button>
	                                        <button class="btn btn-default" type="button"  title="可见"  onclick="openLocation(this,'${ user.id }')"><i class="fa  fa-eye eye" ></i></button>
	                                        <button class="btn btn-default" type="button"  title="追踪"  onclick="openUserLine(this,'${ user.id }')"><i class="fa  fa-user user"></i></button>
	                                        <button class="btn btn-default" type="button" title="监控摄像" id="${user.id}"  onclick="openVideoLocation(this,'${ user.id }')"><i class="fa  fa-video-camera"></i></button>
	                                    </div>
	                                </span>
	                            </li>`;
	        		$('#collapseOne'+i+' ul.list-group').append(html2);
	        	}
            }
        	$wrapper1.spinModal(false);
    	}
    },10);
}



function openVideoLocation(element,id){
	$(element).children("i").addClass('text-danger');
	$(element).prev().attr("disabled","disabled");
	$(element).prev().prev().attr("disabled","disabled");
	var $w1  = $('#videoParent');
	$w1.spinModal();
	$('#videoParent').show();
	$('.icon').show();
	$('#videoParent').attr("data-id",id);
	var g_iWndIndex = 0; //可以不用设置这个变量，有窗口参数的接口中，不用传值，开发包会默认使用当前选择窗口
	//通过人员id 查找此时离此人最近的摄像头的编号
	$.post("../Pos/selectCameraByUser",{
		"paramJson": JSON.stringify({
			"token": getCookie("token"),
			"layer_id": mapList[0].id,
			"id": id
		})
	},function(res){
		if(res.code == 200){
			$('#videoName').html(res.data.vidicon_name);
			
			// 检查插件是否已经安装过
			$w1.spinModal(false);
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
			WebVideoCtrl.I_InitPlugin("650", "600", {
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
			
			start(res.data.vidicon_ip,res.data.vidicon_port,res.data.vidicon_username,res.data.vidicon_password);
		}else {
			alertTool(res.message+",请刷新页面。");
		}
	});
	
	
	
}

$('#close').click(function(){
	var id = $(this).parent().attr("data-id");
	$("#"+id).children("i").removeClass('text-danger');
	$("#"+id).prev().removeAttr("disabled");
	$("#"+id).prev().prev().removeAttr("disabled");
	
	$('#videoParent').hide();
	$("#divPlugin").remove();
	$("#videoParent").append('<div id="divPlugin" class="plugin" ></div>');
});


function changeMap(tag,id,url){
	$(tag).parents('li').addClass("activeMap");
	$(tag).parents('li').siblings().removeClass("activeMap");
	
	var $w1  = $('#accordion2');
	$(".mapName").html($(tag).find('strong').html());
	
	getJson(url);
	
	personTypeList=[];
	$('#accordion2').html("");
	$w1.spinModal();
	getPersonList(id);
	
	personLoad($w1);
	
}
function initMap(){
	$("#locationMap>ul>li:first-child").addClass("activeMap");
}
initMap();



//弹出项目信息框
$("#projectInfo").click(function(){
	$(this).toggleClass('text-blur');
    if($('.projectInfo').css("right")=="0px"){
        $('.projectInfo').animate({right: -350});
    }else {
        $('.projectInfo').animate({right: 0});
    }
});


//打开轨迹回放modal
function openModal(){
    $('#myModal1').empty();
    $.ajaxSetup ({ cache: false });
    $('#myModal1').load('html-js/indexMap/pathPlay.html',function(result) {
        $('#myModal1').modal({
            show:true
        });
    });
}

//基站
$('#base').click(function(){
	$(this).next().toggle();
});
function personList(){
	var param = {
		"searchCon": $("#searchParam").val(),
		"layer_id": "22561d26b50542d38bc8c68ff5a761a5",
		"token": getCookie("token")
	}
	$.post("../PosUser/getSearchUserInfo",{
		"paramJson": JSON.stringify(param)
	},function(res){
		$("#searchBtn").next().html("");
		if(res.code==200 && res.data.length>0){
			
			for(var i=0;i<res.data.length;i++){
				var card = res.data[i].card_sn==null?"暂时未绑定":res.data[i].card_sn;
				$("#searchBtn").next().append("<li><a onclick='locationMap(\""+res.data[i].name+"\",\""+res.data[i].geo_x+"\",\""+res.data[i].geo_y+"\")'>"+res.data[i].name+"("+card+")</a></li>");
			}
		}else if(res.code==200 && res.data.length==0){
			$("#searchBtn").next().append("<li><a>查无数据</a></li>");
		}
	});
}

//人员模糊搜索
$('#searchBtn').click(function(){
	$("#searchBtn").next().html("<li><a>数据加载中<i class='fa fa-spinner' style='margin-left: 10px'></i></a></li>");
	personList();
});
//$('#searchParam').keydown(function(e){
//	//e.preventDefault();
//	if(e.keyCode == 13){
//		$('#userList').show();
//		personList();
//	}
//});

var users=[];

//获取项目信息
function showProjectMess(){
	var projectParam = {"token": getCookie("token")};
	$.post("../Pos/selectConstructionSite",{"paramJson":JSON.stringify(projectParam)},function(res){
		$("#name").html(res.data[0].name);
		$("#conLongth").html(res.data[0].conLongth);
		$("#investAmount").html(res.data[0].investAmount);
		$("#startDate").html(format(res.data[0].startDate, 'yyyy-MM-dd'));
		$("#planComDate").html(format(res.data[0].planComDate, 'yyyy-MM-dd'));
	})
}

// 还原接口
function back(){
	areaLayer.setVisible(false);
	baseLayer.setVisible(false);
	videoLayer.setVisible(false);
	userCash=[];
	userNameFlag=false;
	areaFlag = true;
	baseCashe = [];
	videoFlag=true,baseFlag=true,userFlag=true;
	$('#layerControl2 ul.dropdown-menu>li').each(function(i,w){
		$(w).children().children('i').removeClass("fa-eye-slash").addClass("fa-eye");
	});
	
}




var format = function(time, format){
    var t = new Date(time);
    var tf = function(i){return (i < 10 ? '0' : '') + i};
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
        switch(a){
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'mm':
                return tf(t.getMinutes());
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case 'HH':
                return tf(t.getHours());
                break;
            case 'ss':
                return tf(t.getSeconds());
                break;
        }
    })
}


//添加图层
//aditor:jiangyanyan
function addMap(id){
	$("#myModal").modal("show");
}

//添加图层的确定按钮
//aditor:jiangyanyan
$("#addCardModal").click(function(){
	console.log("确定！！");
});

var layer_id = "";
//删除图层按钮
//aditor:jiangyanyan
function deleteMap(id,name){
	layer_id = id;
	$("#myModalDelete .modal-title-dele").html("你确定要删除"+name+"吗？");
	$("#myModalDelete").modal("show");
}

//删除图层的确定按钮
$("document").on("click","#delLayerInfo",function(){
	console.log(1223124235);
});

//删除图层的确定方法
//aditor:jiangyanyan
$('#delLayerInfo').click(function(){
	console.log("fangfa diaohyong lemeiyou")
	alert(layer_id);
	var data = {"token":getCookie("token"),"layer_id":layer_id};
	$.ajax({
		type:'post',
      cache: false,
      data: {"paramJson":JSON.stringify(data)},
      url:"../PosUser/deleteFloorInfo",
      dataType: "json",
      success: function (jsondata){
      	alert(jsondata.message); 
	       	 if(jsondata.code==200){
	       		 	 
	       	 }
      },       
	});
});








