/**
 *  on 2017/9/12
 */
var blockId = window.location.href.split("=")[1];
$(function(){
	$(".panel .panel-header").height(window.screen.height *  0.2);
	$(".panel .panel-body").height(window.screen.height *  0.55);
	$(".panel .panel-footer").height(window.screen.height *  0.03);
	
	
	
	$('#time').leoweather({
	    format:'{时段}好！，{年}年{月}月{日}日 星期{周} {时}:{分}:{秒}'
	});
	$('#wen').leoweather({
	    format:'{城市} {昼夜}天气 {天气} {气温}℃  {风向} {风级}'
	});
	
	hello();
});


var ws;
var ws2;
function hello(){
	var socket_path="ws://"+server+"server";		 
    ws = new WebSocket(socket_path);
    ws.onopen = function(evn){
    	ws.send("open_"+blockId);
    };
    ws.onmessage = function(evn){
        console.log(evn.data);
//        $('#typeInfo tbody').html("");
//		 $('#workerInfo tbody').html("");
		 $('.personInfo').hide();
        if(JSON.parse(evn.data).hasOwnProperty("data")){
 
       	 	var tl=[],wl=[],last_person,welcome;
       	
       	 	//welcome = JSON.parse(evn.data).data.show_welcome;
			last_person =JSON.parse(evn.data).data;
			//tl = JSON.parse(JSON.parse(evn.data).data.class_type_person.split("\\").join(""));
			//wl = JSON.parse(JSON.parse(evn.data).data.worker_type_person.split("\\").join(""));
			//last_person =JSON.parse(JSON.parse(evn.data).data.last_enter_person.split("\\\"\"").join("\"").split("\"\\\"").join("\"").split("\\").join(""));
			//$("#welcome").html(welcome);
			
			if(JSON.parse(evn.data).data!=""||JSON.parse(evn.data).data!="null"){// 
				console.log(last_person.avatar_img.indexOf('http')!=-1?last_person.avatar_img:(urlpath+last_person.avatar_img));
				$('#personname').html(last_person.name);
				 $('#team').html(last_person.team);
				 $('#worker_type').html(last_person.worker_type);
				 $('#punish_record').html(last_person.punish_record);
				 $('#img').attr("src",last_person.avatar_img.indexOf('http')!=-1?last_person.avatar_img:(urlpath+last_person.avatar_img));
				 
				//$('#tableInfo').removeClass('col-xs-12').addClass("col-xs-7");
				 $('.personInfo').show();
			}else {
				 //$('#tableInfo').removeClass('col-xs-7').addClass("col-xs-12");
				 $('.personInfo').hide();
			}
			 
			 
			 
        }else {
        	 $('.personInfo').hide();
       	 $('.alert-danger').html("改分区暂时无人刷卡");
    		 $('.alert-danger').show(300).delay(1000).hide(300);
        }
    };
    ws.onclose = function(){
        console.log("关闭");
    };
    var socket_path2="ws://"+server+"PushDataStatistics";	
    ws2 = new WebSocket(socket_path2);
    ws2.onopen = function(evn){
    	ws2.send("open_"+blockId);
    };
    ws2.onmessage = function(evn){
       
        $('#typeInfo tbody').html("");
		 $('#workerInfo tbody').html("");
		
        if(JSON.parse(evn.data).hasOwnProperty("data")){
       	 
       	 var tl=[],wl=[],welcome;
//			 tl = JSON.parse(evn.data).data.class_type_person;
//			 wl=JSON.parse(evn.data).data.worker_type_person;
			if(JSON.parse(evn.data).data!=""||JSON.parse(evn.data).data!="null")
			{
				 console.log(JSON.parse(evn.data).data.class_type_person);
			 tl = JSON.parse(JSON.parse(evn.data).data.class_type_person);
			 wl = JSON.parse(JSON.parse(evn.data).data.worker_type_person);
			 
       	 welcome = JSON.parse(evn.data).data.show_welcome;
       	$("#welcome").html(welcome);
			$('#tableInfo').removeClass('col-xs-12').addClass("col-xs-8");
				 
			
			 
			 for(var i=0;i<tl.length;i++){
				 var html = '';
				 html = `<tr class="warning">
	                        <td>${tl[i].type_name}: <strong>${tl[i].type_count}人</strong></td>
	                    </tr>`;
				 $('#typeInfo tbody').append(html);
			 }
			 var ln = (wl.length%3==0)?wl.length:(wl.length-3);
			 for(var w=0;w<ln;){
				 if(w%3==0){
					 var html = '';
					 html = `<tr class="info">
						 		<td>${wl[w].type_name}: <strong>${wl[w].type_count}人</strong> </td>
						 		<td>${wl[w+1].type_name}: <strong>${wl[w+1].type_count}人</strong></td>
						 		<td>${wl[w+2].type_name}: <strong>${wl[w+2].type_count}人</strong></td>
						 	</tr>`;
					 $('#workerInfo tbody').append(html);
				 }else {
					 console.log(w-wl.lwngth);
				 }
				 w+=3;
			 }
			 if(wl.length%3==1){
				 var html = '';
				 html = `<tr class="info">
					 		<td>${wl[wl.length-1].type_name}: <strong>${wl[wl.length-1].type_count}人</strong> </td>
					 		<td></td>
					 		<td></td>
					 	</tr>`;
				 $('#workerInfo tbody').append(html);
			 }else if(wl.length%3==2){
				 var html = '';
				 html = `<tr class="info">
					 		<td>${wl[wl.length-2].type_name}: <strong>${wl[wl.length-2].type_count}人</strong> </td>
					 		<td>${wl[wl.length-1].type_name}: <strong>${wl[wl.length-1].type_count}人</strong></td>
					 		<td></td>
					 	</tr>`;
				 $('#workerInfo tbody').append(html);
			 }
				autoScroll();
			}
       	
        }else {
       	 $('.alert-danger').html("改分区暂时无人刷卡");
    		 $('.alert-danger').show(300).delay(1000).hide(300);
        }
        
    };
	ws2.onclose = function(){
        console.log("关闭");
    }
};
// 切换分区握手
function subsend(blockId){
   ws.send("switcharea_"+blockId);
   ws2.send("switcharea_"+blockId);
}


function autoScroll(){
	// 首先统计ul个数
    var ulCount = $('#scrollTable2').children('tr');
    console.log(ulCount.length);
    var i = 0;
//    $('#scrollTable2').children("tr:lt(6)").show();
    $('#scrollTable2').children("tr:gt(5)").hide();

    setInterval(function () {
        // 显示
        if (i == 0) {
            $('#scrollTable2').children("tr:lt(6)").show();
            $('#scrollTable2').children("tr:gt(5)").hide();
        } else {
            $('#scrollTable2').children("tr:lt(" + (i) + ")").hide();
            $('#scrollTable2').children("tr:gt(" + (i-1) + "):lt(" + (i + 6) + ")").show();
            $('#scrollTable2').children("tr:gt(" + (i + 6) + ")").hide();
        }
        i += 6;
        
        if (i >=ulCount.length) {
            i = 0;
        }
    }, 3000);
}


$('#back').click(function(){
	setCookie("bid",blockId);
	location.href="screenShow.html";
	ws2.close();
	ws.close();	
	
});
var ip=returnCitySN["cip"];
//document.write(returnCitySN["cip"]+','+returnCitySN["cname"])   //输出接口数据中的IP地址 
function getDate(){
	$.ajax({
		type: 'get',
        url: 'http://v.juhe.cn/weather/ip',
		data:{"ip":ip,"format":"1","dtype":"json","key":"bf665ff43a9fded8614a11e8cbd5e9c9"},
        dataType: 'json',
        success: function(data){
            console.log(data);
      
        },
        error:function(){
            
        }
	});
}

getDate();


