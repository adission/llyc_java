/**
 * 
 */
var ws;
var warningCount = 0
var warningArray = [];  //未处理告警数组
var warningIndex = 0;   //未处理告警浏览索引
function openwarningsocket(){
	localStorage.warningCount = warningCount;
    if(warningCount == 0){
    	$("#warnCount").html("");
    }else{
    	$("#warnCount").html(warningCount);
    }
    ws = new WebSocket("ws://localhost:8080/lanlyc/alertserver");
    ws.onopen = function(evn){
    	ws.send("open_123");
        console.log(evn);
    };
    ws.onmessage = function(evn){
        console.log(evn.data);
        var data = JSON.parse(evn.data);
        var resdata = data.data;
        if(localStorage.warningCount){
        	warningCount = parseInt(localStorage.warningCount)+resdata.length;
        }else{
        	warningCount = resdata.length;
        }
        localStorage.warningCount = warningCount;
        $('.warningInfo').animate({right: 0});
    	$("#warnCount").html(warningCount);
    	showwarndata(resdata[0]);
        $.post('../alert/all',{"token":"123"},function(res){
        	warningArray = res.data;
        })
    };
//    ws.onclose = function(){
//        console.log("关闭");
//    };
    
};
function closesocket(){
    ws.onclose = function(){
        console.log("关闭");
    };
}
function closewarningInfo(){
	$('.warningInfo').animate({right: -350});
}
function init(){
	
	if(localStorage.warningCount){
		warningCount = localStorage.warningCount;
	}else{
		warningCount = 0;
		localStorage.warningCount = 0;
	}
	if(ws==null || ws=="undifine" || ws==""){
		openwarningsocket();
	}
}
//将数据展示到页面上
function showwarndata(data){
	
    $("#name").html(data.userName)
    $("#alertType").html(data.alertType)
    $("#time").html(format(data.time, 'yyyy-MM-dd HH:mm'))
    $("#place").html(data.sectionName+"/"+data.layerName)
}
//上一页
function upwarn(){
	if(warningIndex>0){
		warningIndex--;
		showwarndata(warningArray[warningIndex]);
	}
	
}
//下一页
function downwarn(){
	if(warningIndex<warningArray.length-1){
		warningIndex++;
		showwarndata(warningArray[warningIndex]);
	}
	
}
init();

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

