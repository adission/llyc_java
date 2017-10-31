/**
 * create by wangyanling in 2017/10/18
 */
 $(function(){
    gateBlock();
});
 
 var ws;
 var ws2;
 function hello(){
	 $("#blockName").html($('select[name="area"] option:selected').val()!=0?$('select[name="area"] option:selected').html():$('select[name="area"] option:nth-child(1)').html());
	 var blockId = $('select[name="area"] option:selected').val()!=0
	 		 ?$('select[name="area"] option:selected').val()
			 :$('select[name="area"] option:nth-child(2)').val();
	 var socket_path="ws://"+server+"server";	
	 var socket_path2="ws://"+server+"PushDataStatistics";	
     ws = new WebSocket(socket_path);
     ws.onopen = function(evn){
     	ws.send("open_"+blockId);
     };
     ws.onmessage = function(evn){
         
       
		 $('.personInfo').hide();
         if(JSON.parse(evn.data).hasOwnProperty("data")){
        	 
        	 
			if(JSON.parse(evn.data).data!=""||JSON.parse(evn.data).data!="null"){// 
				var last_person=JSON.parse(evn.data).data;
				$('#personname').html(last_person.name);
				 $('#team').html(last_person.team);
				 $('#worker_type').html(last_person.worker_type);
				 $('#punish_record').html(last_person.punish_record);
				 $('#img').attr("src",last_person.avatar_img.indexOf('http')!=-1?last_person.avatar_img:(urlpath+last_person.avatar_img));
				 $('.personInfo').show();
			
			 
			}
         }else {
        	 $('.alert-danger').html("改分区暂时无人刷卡");
     		 $('.alert-danger').show(300).delay(1000).hide(300);
         }
         
     };
     ws.onclose = function(){
         console.log("关闭");
     };
     ws2 = new WebSocket(socket_path2);
     ws2.onopen = function(evn){
     	ws2.send("open_"+blockId);
     };
     ws2.onmessage = function(evn){
        
         $('#typeInfo tbody').html("");
		 $('#workerInfo tbody').html("");
		
         if(JSON.parse(evn.data).hasOwnProperty("data")){
        	 
        	 var tl=[],wl=[];
        	 if(JSON.parse(evn.data).data!="" || JSON.parse(evn.data).data!="null"){
        		 console.log(JSON.parse(evn.data).data.worker_type_person);
            	 tl = JSON.parse(JSON.parse(evn.data).data.class_type_person);
    			 wl = JSON.parse(JSON.parse(evn.data).data.worker_type_person);
    			 
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
	 setCookie("bid",blockId);
    ws.send("switcharea_"+blockId);
	ws2.send("switcharea_"+blockId);
}
 
 
//获取闸机分区列表
 function gateBlock(){
	 var $wrapper = $('#div-table-container');
	 $wrapper.spinModal();
 	$.post(urlpath+"Gategroup/getAll",{
 		"token": getCookie('token')
 	},function(res){
 		if(res.code==200){
 			$wrapper.spinModal(false);
 			for(var i=0;i<res.data.length;i++){
 				$('select[name="area"]').append('<option value="'+res.data[i].id+'">'+res.data[i].group_name+'</option>');
 			}
 			console.log(getCookie("bid"));
 			if(getCookie("bid")!=null || getCookie("bid")!="" || getCookie("bid")!=undefinde){
 				$('select[name="area"] option[value="'+getCookie("bid")+'"]').attr("selected",true);
 			}
 			hello();
 		}
 	});
 }
 //切换分区
 $("#sureSearch").click(function(e){
	 e.preventDefault();
	 var blockId = $('select[name="area"] option:selected').val()!=0
		 ?$('select[name="area"] option:selected').val()
		 :$('select[name="area"] option:nth-child(2)').val();
	 subsend(blockId);
 });
 $('#cleardata').click(function(){
	 var name = $('#blockName').html();
	 sureTool("确定清除",name+"的数据吗",clearTotal);
//	clearTotal();
 });
 
 function clearTotal(){
	 var blockId = $('select[name="area"] option:selected').val()!=0
		 ?$('select[name="area"] option:selected').val()
		 :$('select[name="area"] option:nth-child(2)').val();
		  var socket_path="ws://"+server+"server";	
		var socket_path2="ws://"+server+"PushDataStatistics";	
		ws = new WebSocket(socket_path);
		ws2 = new WebSocket(socket_path2);
		
	 $.post(urlpath+"GateShowProject/cleardata",{
		 'token': getCookie('token'),
		 "area": blockId
	 },function(res){
		 console.log(res.code)
		 tool3(res.code==200,"数据清零成功",res.message);
		 hello();
//		 subsend(blockId);
	 });
 }
 
 $('#bigData').click(function(){
	 var blockId = $('select[name="area"] option:selected').val()!=0?$('select[name="area"] option:selected').val():$('select[name="area"] option:nth-child(2)').val();
	 location.href="bigScreen.html?blockId="+blockId;
 });