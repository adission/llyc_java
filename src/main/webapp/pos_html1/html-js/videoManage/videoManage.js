/**
 * Created by Administrator on 2017/9/14.
 */
var tableCall;
var paramJson;
var video_id;//点击定位的那个摄像头id
var lg = [0,0];// 坐标
$(function(){
	
    showHeader();
    selectMaps();
    var $wrapper = $('#div-table-container');
    var $table = $('#dynamic-table');
    var total=0;
    paramJson={"token":getCookie("token")}
    tableCall = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
        	$wrapper.spinModal();
            $.ajax({
                type: "GET",
                cache: false,
                data: data,
                url: "../Pos/selectCameraList",
                dataType: "json",
                success: function (result){
                	$wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                	$wrapper.spinModal(false);
                }
            })

        },
        columns: [
            { "data": null },
            { "data": "vidicon_name" },
            { data: "vidicon_number" },
            { data: "layerName" },
            { "data": "geo_x" },
            { "data": "geo_y" },
            {"data": null }
        ],
        "createdRow": function ( row, data, index ) {
            //行渲染回调,在这里可以对该行dom元素进行任何操作
            //给当前行加样式


            $('td', row).eq(0).html(index+1);
            $('td', row).eq(-1).html('<a onclick="openVideo(\''+data.id+'\',\''+data.layer_id+'\',\''+data.layerName+'\')" class="btn btn-info"><i class="fa fa-map-marker"></i>定位</a>');
            $('td', row).eq(0).addClass("text-center");
            $('td', row).eq(8).html('<div class="contentOver" title="'+data.dealContent+'">'+data.dealContent+'</div>');

        },
        "drawCallback": function( settings ) {

        },
        "columnDefs": [
            {orderable: false,
                targets: 0 }
        ]//第一列与第二列禁止排序
    })).api();

    //模糊查询
    $("#searchs").on("blur",function (res) {
    	var $table2=$('#searchs'); 
    	var layer_id = $('#map_name option:selected').val();
    	var keyWords=$('#searchs').val();
    	if(keyWords==null || keyWords=="" || keyWords.length==0|| new RegExp("^[ ]+$").test(keyWords)){
    		paramJson={"layer_id":layer_id,"token":getCookie("token")}
    		return;
    	}else{
    		paramJson={"layer_id":layer_id,"keyWords":keyWords,"token":getCookie("token")}
    	}
    	tableCall.ajax.reload(null,false);
    });
});

//地图显示
var app = {};

app.Drag = function() {

  ol.interaction.Pointer.call(this, {
    handleDownEvent: app.Drag.prototype.handleDownEvent,
    handleDragEvent: app.Drag.prototype.handleDragEvent,
    handleMoveEvent: app.Drag.prototype.handleMoveEvent,
    handleUpEvent: app.Drag.prototype.handleUpEvent
  });
  this.coordinate_ = null;

  this.cursor_ = 'pointer';

  this.feature_ = null;
  this.previousCursor_ = undefined;

};
ol.inherits(app.Drag, ol.interaction.Pointer);

app.Drag.prototype.handleDownEvent = function(evt) {
  var map = evt.map;

  var feature = map.forEachFeatureAtPixel(evt.pixel,
      function(feature) {
		  if(feature.get("type")=="icon"){
	          return feature;
	      }
  });

  if (feature) {
    this.coordinate_ = evt.coordinate;
    this.feature_ = feature;
  }
  return !!feature;
};

app.Drag.prototype.handleDragEvent = function(evt) {
  var deltaX = evt.coordinate[0] - this.coordinate_[0];
  var deltaY = evt.coordinate[1] - this.coordinate_[1];

  var geometry = (this.feature_.getGeometry());
  geometry.translate(deltaX, deltaY);

  this.coordinate_[0] = evt.coordinate[0];
  this.coordinate_[1] = evt.coordinate[1];
};

app.Drag.prototype.handleMoveEvent = function(evt) {
  if (this.cursor_) {
    var map = evt.map;
    var feature = map.forEachFeatureAtPixel(evt.pixel,
        function(feature) {
	    	if(feature.get("type")=="icon"){
		        return feature;
	    	}
        });
    var element = evt.map.getTargetElement();
    if (feature) {
      if (element.style.cursor != this.cursor_) {
        this.previousCursor_ = element.style.cursor;
        element.style.cursor = this.cursor_;
      }
    } else if (this.previousCursor_ !== undefined) {
      element.style.cursor = this.previousCursor_;
      this.previousCursor_ = undefined;
    }
  }
};

app.Drag.prototype.handleUpEvent = function() {
	lg = this.coordinate_;
    this.coordinate_ = null;
	this.feature_ = null;
	return false;
	
};

function sureLg(){
	var datas;
	var layer_id = $("#currentMap>span").attr("value");
	if(typeof(layer_id)=="undefined"){
		 alertTool("请先选择图层");
		 return;
	 }
    var param={
    		"token": getCookie("token"),
    		"xy": lg,
    		"id": video_id,
    		"layer_id": layer_id
	};
    $.ajax({
	    type:"post",
	    url:"../Pos/updateCamera",
	    data:{
	       "paramJson":JSON.stringify(param),
	    },
	    success:function(result){
		     if(result.code=="200"){
		    	alertTool("修改成功");
		    	
		    	closeVideoMap();
		    	tableCall.ajax.reload(null,false);
		     }else{
		    	alertTool(result.message); 	 	          
		     }
	    }         
   });
}
var center = [500,500];
var pointFeature = new ol.Feature(new ol.geom.Point(center));
pointFeature.set("type","icon");
var videoLayer = new ol.layer.Image();
var map3 = new ol.Map({
	interactions: ol.interaction.defaults().extend([new app.Drag()]),
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
    	videoLayer,
    	new ol.layer.Vector({
            source: new ol.source.Vector({
            	features: [pointFeature]
            }),
            style: new ol.style.Style({
            	image: new ol.style.Icon(({
	                anchor: [0.5, 1],
	                anchorXUnits: 'fraction',
	                anchorYUnits: 'pixels',
	                opacity: 0.95,
	                scale: 0.5,
	                src: 'images/video.png'
              }))
          })
    	})
          
    ],
    view: new ol.View({
//        center: ol.proj.transform(center, 'EPSG:4326', 'EPSG:3857'),
        zoom: 17,
        maxZoom: 24
    })
});

function getJson3(url,extent){
	var source  = new ol.source.ImageStatic({
        url: 'images/'+url, // 熊猫基地地图
        imageExtent: extent    // 映射到地图的范围
    });
	videoLayer.setSource(source);
}


// 点击定位按钮，初始化图层
function openVideo(vid,id,name){
	video_id=vid;
	$('#myModal').modal("show");
	$("#videoMap").css("visibility","visible");
	
	$("#currentMap>span").html(name);
	$("#currentMap>span").attr("value",id);
	
	mapDefault(id);
	
	
}
function closeVideoMap(){
	$('#myModal').modal("hide");
	$("#videoMap").css("visibility","hidden");
	
}

function changeForm(){
	$('#searchs').val("")
	var layer_id = $('#map_name option:selected').val();
	paramJson={"layer_id":layer_id,"token":getCookie("token")}
	tableCall.ajax.reload(null,false);
}
function selectMaps(){
	var datas;
	var param={"token":getCookie("token")}

	$.ajax({
		type:"post",
		url:"/lanlyc/posfloor/getFloorInfoL",
		data:{
			"paramJson":JSON.stringify(param),
		},
		success:function(result){
			if(result.code=="200"){
				datas=result.data;
				$("#layer_name").empty();
				$("#layer_name").append("<option value=''>全部</option>");
				$("#currentMap .map1").html(datas[0].name);
				$("#currentMap .map1").attr("value",datas[0].id);
				for(var i=0;i<datas.length;i++){
					var layer_obj=datas[i];
					var op = "<li value='"+layer_obj.id+"'><a onclick='sureMaps(this,\""+layer_obj.id+"\")'>"+layer_obj.name+"</a></li>";
					$(".dropdown-menu.maps").append(op);
					op = "<option value='"+layer_obj.id+"'>"+layer_obj.name+"</option>";
					$("#layer_name").append(op);
				}
			}else{
				alert(result.message);   
			}
		}         
	});
}

function selectMap(tag){
	$(tag).children("i").toggleClass("fa-angle-double-up fa-angle-double-down");
	$('.maps.dropdown-menu').toggle();
	
}

//切换图层
function sureMaps(tag,id){
	$("#currentMap>span").html($(tag).html());
	$("#currentMap>span").attr("value",id);
	$('.maps.dropdown-menu').hide();
	
	mapDefault(id);
}

function mapDefault(id){
	var paramJson = {
		"token": "123",
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
	    	getJson3(res.data.bjtx,[parseFloat(res.data.min_x),parseFloat(res.data.min_y),parseFloat(res.data.max_x),parseFloat(res.data.max_y)]);
	    	center = [parseFloat((parseFloat(res.data.max_x)+parseFloat(res.data.min_x))/2),
	    		parseFloat((parseFloat(res.data.max_y)+parseFloat(res.data.min_y))/2)];

	    	map3.getView().setCenter(center);
	    }
	});
}
