/**
 * Created by Administrator on 2017/9/6.
 */
var status = "1" ; //暂停、播放状态
var painter,current,timer;
var paintarray = []; // 已画的点线
var linkarray = [];
var rmarray = [];
var k = 0;
function initLink(){
	k = 0;
	paintarray = [];
	linkarray = [];
	if(painter){
		clearInterval(painter);
	}
	if(current){
		clearInterval(current);
	}
	if(timer){
		clearInterval(timer);
	}
//	if(bartimer){
//		clearInterval(bartimer);
//	}
	graph.resetCells([]);
	$("#scale").val("");
	$("#offscale").val("");
}
function newPainter(tZoom) {
    if(linkarray.length>0){
        for (var i=0;i<linkarray.length;i++){
            var link = new joint.dia.Link({
                source: { x: paintarray[i].x*tZoom, y: paintarray[i].y*tZoom },
                target: { x: paintarray[i+1].x*tZoom, y: paintarray[i+1].y*tZoom },
                attrs: {}
            });
            link.attr({
                '.marker-target': { stroke: '#31d0c6', fill: '#31d0c6', d: 'M10.139,20.862c-2.958-2.968-2.959-7.758-0.001-10.725c2.966-2.957,7.756-2.957,10.725,0c2.954,2.965,2.955,7.757-0.001,10.724C17.896,23.819,13.104,23.817,10.139,20.862z' }
            });
            if(i==0) {
                link.attr({
                    '.marker-source': { stroke: '#31d0c6', fill: '#31d0c6', d: 'M10.139,20.862c-2.958-2.968-2.959-7.758-0.001-10.725c2.966-2.957,7.756-2.957,10.725,0c2.954,2.965,2.955,7.757-0.001,10.724C17.896,23.819,13.104,23.817,10.139,20.862z' }
                });
            }
            linkarray[i] = link;
        }
    }

}
//画历史轨迹线
function painting(tZoom){
    timer = setInterval("addlink(tZoom)",2000/scale);
    painter = setInterval(function(){graph.resetCells(linkarray);},10);
}
//给历史轨迹线添加或删除线对象
function addlink(tZoom) {
    // for (var i=0;i<array.length-1;i++){
    if ($("#offscale").val()){    //快退
        clearInterval(timer);
        scale = parseFloat($("#offscale").val());
        linkarray.pop(linkarray.length-1);
        paintarray.pop(paintarray.length-1);
        $("#scale").val("");
        k--;
        if(k>0){
            timer = setInterval("addlink(tZoom)", 2000/scale);
        }else {
            $("#offscale").val("");
        }
    }else {                   // 快进、正常播放
        if(k>=array.length-1){
            clearInterval(timer);
            timer = setInterval("addlink(tZoom)",2000/scale);
            return;
        }
        pushlink(array,k);
        
        if($("#scale").val()) {   // 快进
            clearInterval(timer);
            $("#offscale").val("");
            scale = parseInt($("#scale").val());
            timer = setInterval("addlink(tZoom)", 2000 / scale);
        }
        k++;
    }
//    console.log(k);
//    console.log(linkarray.length);
    // }
}


function changestatus() {   //暂停、播放

    if(status == "0"){ //播放
        status = "1";
        $("#contro").html("暂停");
        timer = setInterval("addlink()",2000/scale);
    }else{            //暂停
        status = "0";
        $("#contro").html("播放");
        clearInterval(timer);
    }
}

function speed() {   // 快进按钮
    var old = 1;
    $("#offscale").val("")
    if($("#scale").val()){
        old = parseInt($("#scale").val());
    }
    if(old>=4){
        $("#scale").val("")
    }else{
        $("#scale").val(old+2);
    }
}
function low() {   // 快退按钮
    var old = 0;
    $("#scale").val("")
    if($("#offscale").val()){
        old = parseInt($("#offscale").val());
    }
    if(old>=4){
        $("#offscale").val("")
    }else{
        old++
        $("#offscale").val(old+2);
    }
}

//画实时轨迹线
function paintingCur(tZoom,id){
    current = setInterval(function(){
    	var paramJson = {"userId":id,"token":getCookie("token")};
    	$.post("../postrajectory/currentPos",{"paramJson":paramJson},function(ret){
    		var pos = ret.data;
    		paintarray.push({"x":pos.x,"y":pos.y});
    		if (k>=1){
	    		var link = new joint.dia.Link({
		            source: { x: paintarray[k-1].x*tZoom, y: paintarray[k-1].y*tZoom },
		            target: { x: paintarray[k].x*tZoom, y: paintarray[k].y*tZoom },
		            attrs: {}
		        });
		        link.attr({
		            '.marker-target': { stroke: '#31d0c6', fill: '#31d0c6', d: 'M10.139,20.862c-2.958-2.968-2.959-7.758-0.001-10.725c2.966-2.957,7.756-2.957,10.725,0c2.954,2.965,2.955,7.757-0.001,10.724C17.896,23.819,13.104,23.817,10.139,20.862z' }
		        });
	
		        if(k==1){
		            link.attr({
		                '.marker-source': { stroke: '#31d0c6', fill: '#31d0c6', d: 'M10.139,20.862c-2.958-2.968-2.959-7.758-0.001-10.725c2.966-2.957,7.756-2.957,10.725,0c2.954,2.965,2.955,7.757-0.001,10.724C17.896,23.819,13.104,23.817,10.139,20.862z' }
		            });
		        }
		        linkarray.push(link);
				
    		}
    		k++
    	})
    	
    	
    },2000);
    
    painter = setInterval(function(){graph.resetCells(linkarray);},10);
}

//进度条
function setProcess(){
    var processbar = document.getElementById("processbar");
    var container = document.getElementById("processcontainer");
    var maxwidth = container.clientWidth;
    var singlewidth = parseFloat(maxwidth)/(60*30);
    console.log(parseFloat(processbar.style.width));
    processbar.style.width = parseFloat(processbar.style.width) + singlewidth + "px";
//            processbar.innerHTML = processbar.style.width;
    console.log(processbar.style.width);
    if(processbar.style.width >= maxwidth){
        window.clearInterval(bartimer);
    }
}
//        var bartimer = window.setInterval(function(){setProcess();},1000);
//        window.onload = function(){
//            bartimer;
//        }

function runbar(){
	bartimer = window.setInterval(function(){setProcess();},1000);
}

function jump(event) {
        //鼠标点击的绝对位置
    var e=event||window.event;
//        var mousePos = mouseCoords(event);
    var x = e.clientX;
    //alert("鼠标点击的绝对位置坐标："+x+","+y);

    //获取div在body中的绝对位置
    var x1 = $("#processcontainer").offset().left;
    //alert("div在body中的绝对位置坐标："+x1+","+y1);

    //鼠标点击位置相对于div的坐标
    var x2 = x - x1;
    var maxwidth = parseFloat($("#processcontainer").width());
    alert(x2);
    $("#processbar").width(x2);
    var endtime = alltime*x2/maxwidth;
    var starttime = testend-alltime*1000
    //点击读条未到达的地方
    console.log(endtime);
    console.log(starttime);
    console.log(parseInt(1000*(endtime+1))+starttime);
    console.log(parseInt(array[paintarray.length-1].time));
    console.log(parseInt(array[0].time));
    if(parseInt(array[paintarray.length-1].time)<parseInt(1000*(endtime+1))+starttime){
    	alert(array[linkarray.length].time+"----"+parseInt(1000*(endtime+1)+starttime))
	    for(var i=linkarray.length;i<array.length-1;i++){
	    	if(parseInt(array[i].time)<parseInt(1000*(endtime+1))+starttime){
	    		pushlink(array,i);
	    	}
	    }
	    alert(linkarray.length);
	//点击读条已经经过的地方
    }else if(parseInt(array[paintarray.length-1].time)>parseInt(1000*(endtime+1))+starttime){
    	alert(linkarray.length+"-------3");
    	for(var i=paintarray.length-1;i>=0;i--){
    		if(parseInt(array[i].time)>parseInt(1000*(endtime+1))+starttime){
    			linkarray.pop(linkarray.length-1);
        		paintarray.pop(paintarray.length-1);
        		if(i==0){
        			paintarray.pop(paintarray.length-1);
        		}
    		}
    	}
    }

}

//根据参数数据源填充 linkarray、painterarray
function pushlink(paramarray,m){
	var link = new joint.dia.Link({
        source: { x: paramarray[m].x*tZoom, y: paramarray[m].y*tZoom },
        target: { x: paramarray[m+1].x*tZoom, y: paramarray[m+1].y*tZoom },
        attrs: {}
    });
    link.attr({
        '.marker-target': { stroke: '#31d0c6', fill: '#31d0c6', d: 'M10.139,20.862c-2.958-2.968-2.959-7.758-0.001-10.725c2.966-2.957,7.756-2.957,10.725,0c2.954,2.965,2.955,7.757-0.001,10.724C17.896,23.819,13.104,23.817,10.139,20.862z' }
    });

    if(m==0){
        link.attr({
            '.marker-source': { stroke: '#31d0c6', fill: '#31d0c6', d: 'M10.139,20.862c-2.958-2.968-2.959-7.758-0.001-10.725c2.966-2.957,7.756-2.957,10.725,0c2.954,2.965,2.955,7.757-0.001,10.724C17.896,23.819,13.104,23.817,10.139,20.862z' }
        });
        paintarray.push({x: paramarray[0].x, y: paramarray[0].y});
        paintarray.push({x: paramarray[m+1].x, y: paramarray[m+1].y});
    }else{
        paintarray.push({x: paramarray[m+1].x, y: paramarray[m+1].y});
    }
    linkarray.push(link);
    
}

