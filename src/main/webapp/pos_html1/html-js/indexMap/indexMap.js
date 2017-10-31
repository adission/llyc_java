 /**
 * 
 */

var workerColor,outColor;

gertColor();

var mapInfo;
function gertColor(){
	mapInfo = JSON.parse(getCookie("map"));
	$.post("../cardtype/list",{"paramJson":JSON.stringify({"token":getCookie("token")})},function(res){
		if(res.code == "200"){
			var data = res.data;
			for (var i = 0; i < data.length; i++) {
				if(data[i].id == 0 ){
					outColor = data[i].color
				}
				if(data[i].id == 1 ){
					workerColor = data[i].color
				}
			}
		}
	})
}
var videoFlag=true,baseFlag=true,userFlag=true;
// 人员详情显示
var container = document.getElementById('popup');
var content = document.getElementById('popup-content');
var closer = document.getElementById('popup-closer');
// 显示弹窗
var overlay = new ol.Overlay(({
    element: container,
    autoPan: true,
    autoPanAnimation: {
        duration: 250
    }
}));
// 关闭弹窗
closer.onclick = function () {
    overlay.setPosition(undefined);
    closer.blur();
    return false;
};
var userSource  = new ol.source.Vector({wrapX: false});
var baseSource  = new ol.source.Vector({wrapX: false}); // 基站
var videoSource  = new ol.source.Vector({wrapX: false}); // 摄像头
var areaSource  = new ol.source.Vector({wrapX: false}); // 区域

var userNameFlag=false;

function userStyle(feature, color,id,name) {
    var usStyle,op=1,op1 = 0;
    if(userLocationCash.indexOf(id)!=-1){
        op=0;
    }
    usStyle = new ol.style.Style({
        image: new ol.style.Icon({
            offset: [0, 0],
            opacity: op,
            rotateWithView: true,
            rotation: 0.0,
            scale: 0.3,
            color: color,
            anchor: [0.5, 1],
            src: 'images/user4.png'
        }),
       text: new ol.style.Text({
		    textBaseline: top,
		    text: name,
		    fill: new ol.style.Fill({color: "#ff0000"}),
		    stroke: new ol.style.Stroke({color: "#ffffff", width: 1}),
		    offsetY: 10,
		    offsetX: 0,
		    opacity: op
		})
    });
    return usStyle;
}


// 人员位置图标显示图层
var userLayer = new ol.layer.Vector({
    source: userSource
});

function baseStyle(feature, type) {
    var baStyle,color;
    if (type=="1") {
        color = "#ff0000";
    }else if(type=="2"){
        color = "#ffff00";
    }else if(type=="3"){
        color = "#00ff00";
    }
    baStyle = new ol.style.Style({
        image: new ol.style.Icon({
            offset: [0, 0],
            opacity: 1.0,
            rotateWithView: true,
            rotation: 0.0,
            scale: 0.1,
            color: color,
            src: 'images/base.png'
        })
    });
    return baStyle;
}
var basestyle1 = new ol.style.Style({
    image: new ol.style.Icon({
        offset: [0, 0],
        opacity: 0,
        rotateWithView: true,
        rotation: 0.0,
        scale: 1.5,
        color: "#ffffff",
        src: 'images/ch.png'
    })
});
// 基站位置图标显示图层
var baseLayer = new ol.layer.Vector({
    source: baseSource
});

// 摄像头位置图标显示图层
var videoLayer = new ol.layer.Vector({
    source: videoSource,
    style: new ol.style.Style({
        image: new ol.style.Icon({
            offset: [0, 0],
            opacity: 1.0,
            rotateWithView: true,
            rotation: 0.0,
            scale: 0.8,
            size: [44, 44],
            src: 'images/video.png'
        })
    })
});
// 获取某个图层的区域信息
function getAreaList(id){
	$.post("../Pos/selectassignmentsSection",{
		"paramJson":JSON.stringify({
			"layer_id": id,
			"token":getCookie("token")
		})
	},function(res){
		if(res.code==200){
			if($('#layerControl2>ul>li:nth-child(3)>a').attr('onclick')==""){
				$('#layerControl2>ul>li:nth-child(3)>a').attr('onclick', openArea(this))
			}
			
			var areas=[] ;
  		    var datas=res.data;
	        for(var i=0;i<datas.length;i++){   
	        	var data2={"data":[],"name":""};
	        	var str="("+datas[i].endLongitude+" "+datas[i].endLatitude+", "+datas[i].endLongitude+" "+datas[i].startLatitude+", "+
        	    datas[i].startLongitude+" "+datas[i].startLatitude+", "+datas[i].startLongitude+" "+datas[i].endLatitude+", "+
        	    datas[i].endLongitude+" "+datas[i].endLatitude+")";
        	  	data2.data=str;
        	  	data2.name=datas[i].name;
        	  	areas.push(data2);
        	  	
            }
	        areaList(areas);
		}else {
			$('#layerControl2>ul>li:nth-child(3)>a').attr('onclick', '').click(function(){
				alertTool(res.message);
			});
		}
	});
}
getAreaList(getCookie("mapId"));


//区域位置图标显示图层
var areaLayer  = new ol.layer.Vector({
    source: areaSource
});
function areaList(al){
	var wkt;
	for(var i=0;i<al.length;i++){
		wkt='POLYGON('+al[i]["data"]+")";
		var format = new ol.format.WKT();
		
		var name=al[i].name;
		var polygonFeature = format.readFeature(wkt);
		
		polygonFeature.setStyle(
			new ol.style.Style({
				stroke: new ol.style.Stroke({
		            width: 1,
		            color: [255, 153, 153, 1]
		        }),
		        fill: new ol.style.Fill({
		            color: [255, 153, 0, 0.6]
		        }),
				text: new ol.style.Text({
				    textBaseline: top,
				    text: name,
				    fill: new ol.style.Fill({color: "#ff0000"})
				})
			})
		);
		areaSource.addFeature(polygonFeature);
		areaLayer.setVisible(false);
	}
}	

var ccc = [parseFloat((parseFloat(mapInfo.max_x)-parseFloat(mapInfo.min_x))/2),parseFloat((parseFloat(mapInfo.max_y)-parseFloat(mapInfo.min_y))/2)];
var imgLayer = new ol.layer.Image();
//地图显示
var map = new ol.Map({
    target: 'map',
    controls: ol.control.defaults({
        attribution: false,
        rotate: false
    }).extend([
    	new ol.control.Zoom({
            zoomInTipLabel: "放大",
            zoomOutTipLabel: "缩小"
        }),
        new ol.control.FullScreen({
        	tipLabel: "全屏"
        }),
        new ol.control.ZoomSlider()
    ]),
    layers: [
    	imgLayer,
        userLayer,baseLayer,videoLayer,areaLayer
    ],
    view: new ol.View({
    	center: ccc,
        zoom: 17,
        maxZoom: 24,
//        minZoom: 14
    })
});


function getJson(url,extent){
	var source  = new ol.source.ImageStatic({
        url: 'images/'+url, // 熊猫基地地图
        imageExtent: extent    // 映射到地图的范围
    });
	imgLayer.setSource(source);
	
}
getJson(mapInfo.bjtx,[parseFloat(mapInfo.min_x),parseFloat(mapInfo.min_y),parseFloat(mapInfo.max_x),parseFloat(mapInfo.max_y)]);

// 人员详情的弹窗显示 方式1
map.addOverlay(overlay);

// 将人员实时位置信息加载到地图中
function addUserFea1(data,tag,source){
	var featus = source.getFeatures();
	featus.forEach(function(fea){
		if(fea.getGeometryName()=="user"){
			source.removeFeature(fea);
		}
	});
	
    var transform = ol.proj.getTransform('EPSG:4326', 'EPSG:3857');
    data.forEach(function (item) {
        var feature = new ol.Feature(item);
        feature.setGeometryName(tag);
        var coordinate = [parseFloat(item.x), parseFloat(item.y)];

        var geometry = new ol.geom.Point(coordinate);
        if(isNaN(coordinate[1])){
        	return;
        }
        feature.setGeometry(geometry);
        feature.set(tag+"Id", item.perId);
        feature.set("color", item.color);
        feature.set("typeName", item.typeName);
        feature.set(tag+"Name", item.name);
        
        var ff = false;  //  人员已经隐藏
		
		if(userLocationCash.indexOf(item.perId)!=-1 ){
			ff=true; 
		}
        if(userNameFlag && !ff){
        	feature.setStyle([userStyle(feature,item.color,item.perId,item.name)]);
        }else {
        	feature.setStyle([userStyle(feature,item.color,item.perId,"")]);
        }
        
        for(var i=0;i<userLineCashe.length;i++){
        	if(item.perId == userLineCashe[i].id){
        		userLineCashe[i].pos.push(coordinate);
        		if(userNameFlag ){
                	feature.setStyle([userStyle(feature,item.color,item.perId,item.name)]);
                }else {
                	feature.setStyle([userStyle(feature,item.color,item.perId,"")]);
                }
        		
        		// 画线
                var oldLine = new ol.Feature({//路线
                    geometry: new ol.geom.LineString(userLineCashe[i].pos, 'XY')
                });
                
                oldLine.setStyle(new ol.style.Style({
                    fill: new ol.style.Fill({
                        color: [255,0,0,1]
                    }),
                    stroke: new ol.style.Stroke({
                        color: [0,0,255,1],
                        lineDash:[1,2,3],
                        width: 1
                    })
                }));
                oldLine.set("id",item.perId);
                source.addFeature(oldLine);
        	}
        }
        source.addFeature(feature);
    });
}
// 基站信息
function baseList(id){
	var param={"layer_id": id,"token": getCookie("token")}
    $.ajax({
        url: '/lanlyc/Pos/selectBaseStation',
        type : 'post',
        data:{"paramJson":JSON.stringify(param)},
        dataType: 'json',
        success: function(result){
        	//alert(JSON.stringify(result));
        	if(result.code==200){
        		$('#layerControl2>ul>li:nth-child(2)>ul>li').each(function(){
        			if($(this).attr('onclick')==""){
        				$(this).attr('onclick', openBase(this))
        			}
        		});
        		
        		var data={"code":"","message":"","userlist":""};
                data.code=200;
                data.message="ok";
                var datas=result.data;
                var arr=[];
                var a1=0,a2=0,a3=0;
                for(var i=0;i<datas.length;i++){
            	    var arr2={"id":"","type":"","latitude":"","longitude":""};
            		arr2.id=datas[i].id;
            		arr2.type=datas[i].type;
            		arr2.latitude=datas[i].geo_y;
            		arr2.longitude=datas[i].geo_x;
            		arr.push(arr2);
            		if(datas[i].type==1){
            			a1++;
            		}
            		if(datas[i].type==2){
            			a2++;
            		}
            		if(datas[i].type==3){
            			a3++;
            		}
                }
                if(a1==0){
                	console.log(111)
                	$('#layerControl2>ul>li:nth-child(2)>ul>li:nth-child(1)>a').attr('onclick', '').click(function(){
        				alertTool("该图层没有找到无线基站。");
        			});
                }
                if(a2==0){
                	console.log(222)
                	$('#layerControl2>ul>li:nth-child(2)>ul>li:nth-child(2)>a').attr('onclick', '').click(function(){
        				alertTool("该图层没有找到网关基站。");
        			});
                }
                if(a3==0){
                	console.log(333)
                	$('#layerControl2>ul>li:nth-child(2)>ul>li:nth-child(3)>a').attr('onclick', '').click(function(){
        				alertTool("该图层没有找到双监基站。");
        			});
                }
                
                
                data.userlist=arr;
                addBaseFea(data,"base",baseSource);
        	}else{
        		$('#layerControl2>ul>li:nth-child(2)>ul>li').attr('onclick', '').click(function(){
    				alertTool(res.message);
    			});
        	}
        	
        }
    });
}
// 添加图标的位置
function addBaseFea(data,tag,source){
    var transform = ol.proj.getTransform('EPSG:4326', 'EPSG:3857');
    data.userlist.forEach(function (item) {
        var feature = new ol.Feature(item);
        feature.setGeometryName(tag);
        var coordinate = [parseFloat(item.longitude), parseFloat(item.latitude)];
        var geometry = new ol.geom.Point(coordinate);
        feature.setGeometry(geometry);
        feature.set(tag+"Id", item.id);

        feature.set("type",item.type);

        feature.setStyle(basestyle1);

        source.addFeature(feature);
    });
}
baseList(getCookie('mapId'));


// 获取摄像头位置列表
function videoList(id){
	var param={"layer_id": id,"token":getCookie("token")}
    $.ajax({
        url: '/lanlyc/Pos/selectCamera',
        type : 'post',
        data:{"paramJson":JSON.stringify(param)},
        dataType: 'json',
        success: function(result){
        	//alert(JSON.stringify(result));
        	if(result.code==200){
        		if($('#layerControl2>ul>li:nth-child(4)>a').attr('onclick')==""){
        			$('#layerControl2>ul>li:nth-child(4)>a').attr('onclick', openVideo(this))
        		}
        		
        		var data={"code":"","message":"","userlist":""};
                data.code=200;
                data.message="ok";
                var datas=result.data;
                var arr=new Array();
                for(var i=0;i<datas.length;i++){
                	    var arr2={"id":"","latitude":"","longitude":""};
                		arr2.id=datas[i].id;
                		arr2.latitude=datas[i].geo_y;
                		arr2.longitude=datas[i].geo_x;
                		arr.push(arr2);
                }
                data.userlist=arr;
                addVideoFea(data,"video",videoSource);
        	}else{
        		$('#layerControl2>ul>li:nth-child(4)>a').attr('onclick', '').click(function(){
    				alertTool(res.message);
    			});
        	}
        	
        }
    });
}

// 添加摄像头图标的位置
function addVideoFea(data,tag,source){
    var transform = ol.proj.getTransform('EPSG:4326', 'EPSG:3857');
    data.userlist.forEach(function (item) {
        var feature = new ol.Feature(item);
        feature.setGeometryName(tag);
        var coordinate = [parseFloat(item.longitude), parseFloat(item.latitude)];
        var geometry = new ol.geom.Point(coordinate);
        feature.setGeometry(geometry);
        feature.set(tag+"Id", item.id);
        source.addFeature(feature);
        
        videoLayer.setVisible(false);
    });
}
videoList(getCookie("mapId"));

  
//摄像头位置的显示
function openVideo(tag){
	$(tag).toggleClass('text-blur');
    videoLayer.setVisible(videoFlag);
    videoFlag = !videoFlag;
    $(tag).children("i").toggleClass('fa-eye-slash').toggleClass('fa-eye');
}


var baseCashe = [];
//基站位置的显示
function openBase(element,type){
	$(element).toggleClass('text-blur');
    $(element).children("i").toggleClass('fa-eye text-danger').toggleClass('fa-eye-slash');
    var features = baseLayer.getSource().getFeatures();
    
    if(baseCashe.indexOf(type)==-1){
        baseCashe.push(type);
    }else {
        baseCashe.splice($.inArray(type,baseCashe),1);
    }
    
    for(var i=0;i<features.length;i++){
        if(features[i].get("type") == type && baseCashe.indexOf(type)!=-1){
        	features[i].setStyle([baseStyle(features[i],type)]);
        }else if(features[i].get("type") == type){
        	features[i].setStyle(basestyle1);
        }
    }
}

var userLocationCash=[];
// 人员位置的显示隐藏
function openLocation(element,id){
	
    $(element).children("i").toggleClass('fa-eye').toggleClass('fa-eye-slash text-danger');
    if($(element).children("i").hasClass("text-danger")){
		$(element).next().attr("disabled","disabled");
	    $(element).next().next().attr("disabled","disabled");
	}else {
		$(element).next().removeAttr("disabled");
		$(element).next().next().removeAttr("disabled");
	}
    

    var features = userLayer.getSource().getFeatures();
    if(userLocationCash.indexOf(id)==-1){  // 如果没有
        $(element).attr("title","不可见");
        userLocationCash.push(id);
    }else {
        $(element).attr("title","可见");
        userLocationCash.splice($.inArray(id,userLocationCash),1);
    }
    for(var i=0;i<features.length;i++){
        if(features[i].get("userId") == id){ 
        	if(userNameFlag ){
        		if(userLocationCash.indexOf(id)!=-1){
        			features[i].setStyle([userStyle(features[i],features[i].get("color"),id,'')]);
        		}else {
        			features[i].setStyle([userStyle(features[i],features[i].get("color"),id,features[i].get("userName"))]);
        		}
        	}else {
        		features[i].setStyle([userStyle(features[i],features[i].get("color"),id,'')]);
        	}
        }
    }
}

//  人员姓名的显示隐藏
function openUserName(tag){ 
	$(tag).toggleClass('text-blur');
	$(tag).children("i").toggleClass('fa-eye-slash').toggleClass('fa-eye');
    var features = userLayer.getSource().getFeatures();
    
    userNameFlag = !userNameFlag;
    features.forEach(function(fea){
		if(fea.getGeometryName()=="user"){
			var ff = false;  //  人员已经隐藏
			
			if(userLocationCash.indexOf(fea.get("userId"))!=-1 ){
				ff=true; 
			}
			
			if(userNameFlag && !ff ){  //  人名 显示
				fea.setStyle([userStyle(fea,fea.get("color"),fea.get("userId"),fea.get("userName"))]);
			}else {
				fea.setStyle([userStyle(fea,fea.get("color"),fea.get("userId"),'')]);
			}
		}
	});
}

// 人员实时跟踪路线
var userLineCashe = [];
function openUserLine(element,id){
	$(element).children("i").toggleClass('text-danger');
	if($(element).children("i").hasClass("text-danger")){
		$(element).prev().attr("disabled","disabled");
	}else {
		$(element).prev().removeAttr("disabled");
	}
	
	
	var p={};
    p.id = id;
    p.pos = [];
	if(JSON.stringify(userLineCashe).indexOf(JSON.stringify(p.id))==-1){  // 如果没有
        userLineCashe.push(p);
    }else {
    	for(var i=0;i<userLineCashe.length;i++){
    		if(userLineCashe[i].id == id){
    			userLineCashe.splice(i,1);
    			var lines = userSource.getFeatures()
    			for(var i=0;i<lines.length;i++){
    				if(lines[i].get("id") == id){
    					userSource.removeFeature(lines[i]);
    				}
    			}
    		}
    	}
    }
}




// 区域位置的显示隐藏
var areaFlag = true;
function openArea(tag){
	$(tag).toggleClass('text-blur');
    areaLayer.setVisible(areaFlag);
    areaFlag = !areaFlag;
    $(tag).children("i").toggleClass('fa-eye-slash').toggleClass('fa-eye');
}

// 搜索列表中  点击默认后 高亮
function locationMap(name,x,y){
	
	$("#searchParam").val(name);
	var cent = [parseFloat(x),parseFloat(y)];
	
//    map.getView().setCenter(ol.proj.transform(cent, 'EPSG:4326', 'EPSG:3857'));
    map.getView().setCenter(cent);
    
    var feas = userLayer.getSource().getFeatures();
    
    feas.forEach(function(fea){
    	if(fea.get("userName") == name){
    		fea.setStyle(function(resolution){
    		      return [new ol.style.Style({
    		          image: new ol.style.Icon({
    		        	  src: 'images/user.png',
    		        	  scale: map.getView().getZoom() / 28
    		          }),
    		          text: new ol.style.Text({
    		        	  textBaseline: top,
    		  		      text: name,
    		  		      offsetY: 22,
    		  		      fill: new ol.style.Fill({color: "#ff0000"}),
    		  		      stroke: new ol.style.Stroke({color: "#ffffff", width: 1})
    		          })
    		        })];
    		  });
    	}
    })
}

function backStart(){
	areaLayer.setVisible(false); //区域隐藏
	areaFlag = true
	
	videoLayer.setVisible(false);
    videoFlag = true;
    
    baseCashe=[];
    var features = baseLayer.getSource().getFeatures();
    for(var i=0;i<features.length;i++){
    	features[i].setStyle(basestyle1);
    }
	
	userNameFlag = false;  //人名
	var features = userLayer.getSource().getFeatures();
	features.forEach(function(fea){
		fea.setStyle([userStyle(fea,fea.get("color"),fea.get("userId"),'')]);
	});
	
	$("#layerControl2").find("i").addClass('fa-eye-slash').removeClass('fa-eye');
	
	for(var i=0;i<lines.length;i++){
		userSource.removeFeature(lines[i]);
	}
	
	userLineCashe= [];
	userLocationCash=[];
	$(".btn-group.btnIcon").children().removeAttr("disabled");
	$(".btn-group.btnIcon").find("i.eye").removeClass("fa-eye-slash text-danger").addClass('fa-eye');
	$(".btn-group.btnIcon").find("i.user").removeClass("text-danger");
	
}




