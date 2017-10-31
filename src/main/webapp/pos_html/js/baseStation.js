//关于基站
var allStations={"wireless":"","gateway":"","doubleMonitor":""}; //全局变量 一个图层中所有基站信息
var Stations={"wireless":"","gateway":"","doubleMonitor":""}; //将要在图层中显示的基站信息
//初始化的时候获得一个图层中所有基站的位置
function init(){
	//getBaseStation("wireless","5");
	getBaseStation("gateway","2");
	//getBaseStation("doubleMonitor","2");

}

//通过类型和图层id，获得一张图中基站的位置
function getBaseStation(type,layer_id){
	/*if(type!=="wireless" && type!=="gateway" && type!=="doubleMonitor" && (layer_id==null || layer_id ==="")){
		return;
	}*/
	if(type==null || type==="" || type.replace(/(^\s*)|(\s*$)/g, "").length ==0 || type == undefined || 
			layer_id==null || layer_id ==="" || layer_id.replace(/(^\s*)|(\s*$)/g, "").length ==0 || layer_id == undefined){
		console.log("参数为空或空串,方法未调用");
		return;
	}
	var param={"type":type,"layer_id":layer_id}
	$.ajax({
	type:"post",
	url:"/lanlyc/Pos/selectBaseStation",
	data:{
	   "paramJson":JSON.stringify(param),
	},
	success:function(re){
	     if(re.code=="200"){
	    	 var pos=re.data;
		     //console.log(pos);
		     allStations[pos[0].type]=re.data;		     
		     //console.log(JSON.stringify(allStations));
	     }else{
	       alert(re.message);	       
	     }
	 }
	});   
	
}

$(function(){
	init();
})

function aaaa(){
	console.log("来了");
	var wireless="wireless";
	var wireless2=allStations[wireless]
	console.log(JSON.stringify(wireless2))
	console.log(JSON.stringify(allStations["gateway"]))
	console.log(JSON.stringify(allStations))
}

//显示指定类型的基站
function showAllBaseStation(id){
	alert("进来了")
	//$("#bs").toggleClass("kk")
	$("#"+id).toggleClass("show")
	var ty=$("#"+id).attr("value");//将基站的类型存储在value中
	alert(ty)
	alert(JSON.stringify(allStations[ty]))
	alert(JSON.stringify(Stations))
	if($("#"+id).attr("class")!=null && $("#"+id).attr("class")!=""){
		alert("置换")
		Stations[ty]=allStations[ty];
	}
	alert(JSON.stringify(Stations[ty]))
	alert(JSON.stringify(allStations))
	for(var i in Stations){
		for(var j=0;j<Stations[i].length;j++){
			//showBaseStation(Stations[i][j]);
			alert(Stations[i][j].ip);
		}
	}
	
}

/*//根据提供的基站信息显示基站
function showBaseStation(station){
	
}*/

