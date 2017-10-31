/**
 * Created by Administrator on 2017/9/23.
 */

//$("#userList").niceScroll({
//    styler:"fb",
//    cursorcolor:"#65cea7",
//    cursorwidth: '6',
//    cursorborderradius: '0px',
//    background: '#424f63',
//    spacebarenabled:false,
//    cursorborder: '0',
//    zindex: '1000'
//});

$('#openUser').click(function(){
	$(this).next().toggle();
});

// 获取图层人员列表
function getUserList(id){
	
	$("#userList").append("<li><a>数据加载中<i class='fa fa-spinner' style='margin-left: 10px'></i></a></li>");
	var param = {
		"layer_id": id,
		"token": getCookie("token"),
		"searchCon": ''
	}
	$.post("../PosUser/getSearchUserInfo",{
		"paramJson": JSON.stringify(param)
	},function(res){
		
		$('#userList').html("");
		if(res.code==200 && res.data.length>0){
			for(var i=0;i<res.data.length;i++){
				$("#userList").append("<li><a onclick='pathStart(this,\""+res.data[i].id+"\")'><i class='fa fa-map-marker'></i><span>"+res.data[i].name+"</span></a></li>");
			}
		}else if(res.code==200 && res.data.length==0){
			$("#userList").append("<li><a>查无数据</a></li>");
		}
	})
}
getUserList(getCookie("mapId"));

function selectMaps(){
	$("#currentMap>span").html("---请选择---");
	$.ajax({
		type:"post",
		url:"/lanlyc/posfloor/getFloorInfoL",
		data:{
			"paramJson":JSON.stringify({"token":getCookie("token")}),
		},
		success:function(res){
			if(res.code=="200"){
				
				$(".dropdown-menu.maps").empty();
				for(var i=0;i<res.data.length;i++){
					var layer_obj=res.data[i];
					var op = "<li value='"+layer_obj.id+"'><a onclick='sureMap(this,\""+layer_obj.id+"\")'>"+layer_obj.name+"</a></li>";
					$(".dropdown-menu.maps").append(op);
					
					if(layer_obj.id == getCookie("mapId")){
						$("#currentMap>span").html(layer_obj.name);
					}
				}
			}else{
				alert(result.message);   
			}
		}         
	});
}
selectMaps();

var floors = new ol.layer.Vector({
    source: new ol.source.Vector(),
	style: new ol.style.Style({
	    stroke: new ol.style.Stroke({
	        color: 'red',
	        size: 1
	    })
	})
});
var center = [parseFloat((parseFloat(mapInfo.max_x)-parseFloat(mapInfo.min_x))/2),parseFloat((parseFloat(mapInfo.max_y)-parseFloat(mapInfo.min_y))/2)];
var imgLayer1 = new ol.layer.Image();
var map1 = new ol.Map({
    layers: [
        imgLayer1
    ],
    controls: ol.control.defaults({
        attribution: false,
        rotate: false
    }).extend([
        new ol.control.Zoom({
            zoomInTipLabel: "放大",
            zoomOutTipLabel: "缩小"
        }),
        new ol.control.ZoomSlider(),
        new ol.control.FullScreen({
        	tipLabel: "全屏"
        })
    ]),
    target: 'map1',
    view: new ol.View({
    	center: center,
    	zoom: 17,
        maxZoom: 24,
    })
});


function getJson2(url,extent){
	var source  = new ol.source.ImageStatic({
        url: 'images/'+url, // 熊猫基地地图
        imageExtent: extent    // 映射到地图的范围
    });
	imgLayer1.setSource(source);
}
getJson2(mapInfo.bjtx,[parseFloat(mapInfo.min_x),parseFloat(mapInfo.min_y),parseFloat(mapInfo.max_x),parseFloat(mapInfo.max_y)]);


// 轨迹的图层资源
var source = new ol.source.Vector({
    wrapX: false
});
var vector = new ol.layer.Vector({
    type: 'LineString',
    source: source
});
map1.addLayer(vector);
// 线颜色 样式
var style={
    "line1": new ol.style.Style({
        fill: new ol.style.Fill({
            color: [255,0,0,0]
        }),
        stroke: new ol.style.Stroke({
            color: [255,0,0,0],
            width: 1
        })
    }),
    "line2": new ol.style.Style({
        fill: new ol.style.Fill({
            color: [255,0,0,1]
        }),
        stroke: new ol.style.Stroke({
            color: [0,0,255,1],
            width: 1
        })
    }),
    "icon": new ol.style.Style({
        image: new ol.style.Icon(({
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            opacity: 1,
            src: 'images/vn.png'
        }))
    }),
    "start": new ol.style.Style({
    	image: new ol.style.Icon({
            src: 'images/vn.png',
            anchor: [0.5, 1],
            opacity: 1
        })
    }),
    "end": new ol.style.Style({
        image: new ol.style.Icon({
            src: 'images/vn.png',
            anchor: [0.5, 1],
            opacity: 1
        })
    }),
    "person": new ol.style.Style({
        image: new ol.style.Icon({
            src: 'images/user4.png',
            anchor: [0.5, 1],
            color: "#ff0000",
            scale: 0.3,
            opacity: 1
        })
    })
};

var points = [],times = [];
var lineFeature,
	carFeature = new ol.Feature(),
	oldLineFeature = new ol.Feature();
source.addFeature(oldLineFeature);


var slider = $("#range_5").data("ionRangeSlider");
var $w = $('#mapLoad');
//获取坐标
function getList(id,name){
	
	$w.spinModal();
	var param = {
			"starttime": $('#startTime').val(),
			"endtime": $('#endTime').val(),
			"date": $('#pathDate').val(), 
			"userId": id,
			"token": getCookie("token")
		};
		$.post("../postrajectory/history",{
			"paramJson": JSON.stringify(param)
		},function(res){
			
			if(res.code==200){
				if(res.data.length==0){
					$w.spinModal(false);
					alert("此人在该时段内未进入该图层");
				}else {
					// 消除请求等待 的效果
					$w.spinModal(false);
					
					points = [];
					times=[];
					var point = [];
					for(var i=0;i<res.data.length;i++){
						var p = [];
						p.push(parseFloat(res.data[i].x));
						p.push(parseFloat(res.data[i].y));
//						point.push(ol.proj.transform(p, 'EPSG:4326', 'EPSG:3857'));
						point.push(p);
						
						times.push(res.data[i].time);
					}
					points = point;
		
					$("#range_5").data("ionRangeSlider").update({
			            max: times.length,
			            values: times,
			            step: times.length/speed
			        });
					
					$('.timepicker-241').timepicker({
					    autoclose: true,
					    minuteStep: 1,
					    secondStep: 2,
					    defaultTime: times[0],
					    showSeconds: true,
					    showMeridian: false
					});
					
					// 设置地图的中心点位此人定位路线的中间
			        center = points[0];
			        map1.getView().setCenter(center);
			        
			        AddLine(points,name);
			        
			        $("#animateBtn").removeAttr('disabled');
			        $(".sure").removeAttr('disabled');
				}
				
			}
		});
}
function pathStart(tag,id){
	$(tag).addClass("openEye");
	
	$(tag).parent("li").siblings().children("a").removeClass("openEye");
	
	getList(id,$(tag).children("span").html());
}

function userName(name){
	return new ol.style.Style({
	    image: new ol.style.Icon({
	        src: 'images/user4.png',
	        anchor: [0.5, 1],
	        color: "#ff0000",
	        scale: 0.3,
	        opacity: 1
	    }),
	    text: new ol.style.Text({
	        textBaseline: top,
	        text:name,
	        fill: new ol.style.Fill({color: "#ff0000"}),
	        stroke: new ol.style.Stroke({color: "#ffffff", width: 1}),
	        offsetY: 10,
		    offsetX: 0
	    })
	});
}
//轨迹描绘
function AddLine(points,name) {
    lineFeature = new ol.Feature({//路线
        geometry: new ol.geom.LineString(points,'XY')
    });
    lineFeature.setStyle(style.line1);
    source.addFeature(lineFeature);
    
    carFeature.setGeometry(new ol.geom.Point(points[1]));
    carFeature.setStyle(userName(name));
    source.addFeature(carFeature);
}

//轨迹回放start  参考官网
var speed=10, now;
var index = 0;
var animating = false;  //播放停止标志
var animatingPause = false;  // 暂停播放 标志
var speedFlag = false;  // 快退标志



$('#animateBtn').click(function () {
    startAnimation();
});
$('#zanStop').click(function () {
    pause();
});
var time = 0;
// 前进动作
var moveFeature = function (event) {
    var vectorContext = event.vectorContext;
    if (animating) {
    	console.log(time)
        time += 1;
        index = Math.round(speed * time / 300);

        if (index >= points.length) {
            stopAnimation(true);
            return;
        }
        // 人在走
        var currentPoint = new ol.geom.Point(points[index]);
        var feature = new ol.Feature(currentPoint);
        vectorContext.drawFeature(feature, style.person);

        // 画线
        var oldLine = new ol.Feature({//路线
            geometry: new ol.geom.LineString(points.slice(0, index + 1), 'XY')
        });
        vectorContext.drawFeature(oldLine, style.line2);
    }
    map1.render();
    // 设置地图的中心点位此人定位路线的中间
//    center = points[index];
//    map1.getView().setCenter(center);
    
    $("#range_5").data("ionRangeSlider").update({
    	from: index
    });
};
// 快退动作
var backFeature = function (event) {
    var vectorContext = event.vectorContext;
    time -= 1;
    index = Math.round(speed * time / 300);
    if (index <= 0) {
        // 解绑后退动作
        stopLow();
        // 初始位置给人员添加标签
        carFeature.setGeometry(new ol.geom.Point(points[0]));
        carFeature.setStyle(style.person);
        // index 变量值归0
        time = 0;
        animating = false;
        animatingPause  =false;
        return;
    }
    
    // 人倒着走
    var currentPoint = new ol.geom.Point(points[index]);
    var feature = new ol.Feature(currentPoint);
    vectorContext.drawFeature(feature, style.person);

    // 画线
    oldLineFeature.setGeometry(new ol.geom.LineString(goLine.slice(0, index + 1), 'XY'));
    oldLineFeature.setStyle(style.line2);
 	// 设置地图的中心点位此人定位路线的中间
//    center = points[index];
//    map1.getView().setCenter(center);
    map1.render();
    
    $("#range_5").data("ionRangeSlider").update({
    	from: index
    });
};

function startAnimation() {

    if (animating) {
        stopAnimation(false);
    } else {
        animating = true;
        lineFeature.setGeometry(new ol.geom.LineString(points, 'XY'));
        lineFeature.setStyle(style.line1);

        $("#animateBtn").html('停止');
        $("#zanStop").removeAttr("disabled");
        $("#fast").removeAttr("disabled");
        $("#low").removeAttr("disabled");

        carFeature.setStyle(null);
        map1.on('postcompose', moveFeature);
        map1.render();
    }
}
var goLine = [];
function pause() {
	
    if (animatingPause) {  //继续播放
        animatingPause = false;

        $("#zanStop").html('暂停');
        carFeature.setStyle(null);
        map1.on('postcompose', moveFeature);
    } else {// 暂停
        animatingPause = true;
        $("#zanStop").html('继续播放');
        goLine = [].concat(points.slice(0, index + 1));

        oldLineFeature.setGeometry(new ol.geom.LineString(goLine, 'XY'));
        oldLineFeature.setStyle(style.line2);

        carFeature.setGeometry(new ol.geom.Point(points[index]));
        carFeature.setStyle(style.person);

        (carFeature.getGeometry()).setCoordinates(points[index]);
        map1.un('postcompose', moveFeature);
    }
    map1.render();
}
function stopAnimation(ended) {

    if (index < points.length) {
        oldLineFeature.setGeometry(new ol.geom.LineString(goLine, 'XY'));
        oldLineFeature.setStyle(style.line1);
        $("#zanStop").attr("disabled", true);
        $("#fast").attr("disabled", true);
        $("#low").attr("disabled", true);
        goLine = [];
        time = 0;
    } else {
        $("#zanStop").attr("disabled", true);
        $("#fast").attr("disabled", true);
        lineFeature.setGeometry(new ol.geom.LineString(points, 'XY'));
        lineFeature.setStyle(style.line2);
    }

    animating = false;
    animatingPause = false;
    $("#animateBtn").html('播放');
    $("#zanStop").html('暂停');


    var coord = ended ? points[points.length - 1] : points[0];
    carFeature.setGeometry(new ol.geom.Point(coord));
    carFeature.setStyle(style.person);

    (carFeature.getGeometry()).setCoordinates(coord);
    map1.un('postcompose', moveFeature);
    $("#range_5").data("ionRangeSlider").update({
    	from: 0
    });
}
//轨迹回放end

$('#fast').click(function(){
    fastPlay();
});
function fastPlay() {
	map.un('postcompose', moveFeature);
	time = time*speed/(parseInt(speed)+2)
    speed =parseInt(speed)+2;
    if(speed>=50){
        speed=50;
    }
    
    map.on('postcompose', moveFeature);
}

$('#low').click(function () {
    lowPlay();
});
function stopLow() {
    map1.un('postcompose', backFeature);
    $("#animateBtn").html('播放');
    $("#zanStop").attr("disabled", true);
    $("#fast").attr("disabled", true);
    $("#low").attr("disabled", true);
    speedFlag = false;
    $('#low').html("快退");
}

function lowPlay() {
    if(speedFlag){  // 点击暂停快退
        $('#low').html("快退");
        $('#zanStop').html("继续播放");

        animatingPause = true;
        goLine = [].concat(points.slice(0, index + 1));
        oldLineFeature.setGeometry(new ol.geom.LineString(goLine, 'XY'));
        oldLineFeature.setStyle(style.line2);

        var coord = points[index];
        carFeature.setGeometry(new ol.geom.Point(coord));
        carFeature.setStyle(style.person);

        (carFeature.getGeometry()).setCoordinates(coord);
        map1.un('postcompose', backFeature);
        speedFlag =false;
    }else{ // 点击快退
        speedFlag = true;
        $('#low').html("暂停快退");

        lineFeature.setGeometry(new ol.geom.LineString(points, 'XY'));
        lineFeature.setStyle(style.line1);
        map1.un('postcompose', moveFeature);
        goLine = [].concat(points.slice(0, index + 1));

        carFeature.setStyle(null);
        map1.on('postcompose', backFeature);
        map1.render();
    }
}

function goToLine(i){
	map1.un('postcompose', moveFeature);
    $("#zanStop").html('继续播放');
	time = i*300/speed;
	index = i;
    
    $("#range_5").data("ionRangeSlider").update({
    	from: i
    });
    center = points[i];
    map1.getView().setCenter(center);
    
    pause();
    pause();
}


function goTo(){
	var ti = $("#goToTime").val();
	
	var ind = 0;
	for(var i=0;i<times.length;i++){
		if(times[i]==ti){
			ind = i;
		}
	}
	goToLine(ind);
}



function selectTime(){
	$("#myModal1").css("zIndex","105000000");
	$(".dropdown-menu.bootstrap-timepicker").css("zIndex","105000001");
	$(".dropdown-menu.datepicker").css("zIndex","105000005");
	
	//停止播放
	stopAnimation(false);
	$("#range_5").data("ionRangeSlider").update({
    	from: 0
    });
	// 调用接口
//	getList();
	
}

// 关闭地图
function closePathMap(){
    $('#myModal1').modal("hide");
    $('.pathMap').css("visibility","hidden");
    
    stopAnimation(false);
    $('#userList').hide();
    $("#userList li a").removeClass("openEye");
    points=[];
    times=[];
    
    carFeature.setStyle(
		new ol.style.Style({
	        image: new ol.style.Icon({
	            src: 'images/user4.png',
	            anchor: [0.5, 1],
	            color: "#ff0000",
	            scale: 0.3,
	            opacity: 0
	        })
    }));
    $("#range_5").data("ionRangeSlider").update({
    	values: []
    });
}

//获取所有图层接口
function selectMap(tag){
	$(tag).children("i").toggleClass("fa-angle-double-up fa-angle-double-down");
	$('.maps.dropdown-menu').toggle();
}

// 切换图层
function sureMap(tag,id){
	$("#currentMap>span").html($(tag).html());
	$('.maps.dropdown-menu').hide();
	$w.spinModal();
	var paramJson = {
		"token": getCookie("token"),
		"id": id
	};
	
	//切换 第二层  json文件， 中心点 
	//根据图层id， 获取图层文件路径，中心点坐标，
	$.ajax({
	    url: '../posfloor/getFloorBJTPZB',
	    data: {
	    	"paramJson":JSON.stringify(paramJson)
	    },
	    success: function(res) {
	    	
	    	var coor = [parseFloat((parseFloat(res.data.max_x)+parseFloat(res.data.min_x))/2),
	    		parseFloat((parseFloat(res.data.max_y)+parseFloat(res.data.min_y))/2)];

//	    	map1.getView().setCenter(ol.proj.transform(coor, 'EPSG:4326', 'EPSG:3857'));
	    	map1.getView().setCenter(coor);
	    	
	    	getJson2(res.data.bjtx,[parseFloat(res.data.min_x),parseFloat(res.data.min_y),parseFloat(res.data.max_x),parseFloat(res.data.max_y)]);
	    	getUserList(id);
	        $w.spinModal(false);
	    }
	});
}









