/**
 * create by wangyanling in 2017/9/8
 */
 $(function(){
    showLeft();
    showHeader();
    gateBlock();
    
    setTimeout(getData,500);

});
 
//获取闸机分区列表
 function gateBlock(){
 	$.post(urlpath+"Gategroup/getAll",{
 		"token": getCookie('token')
 	},function(res){
 		if(res.code==200){
 			for(var i=0;i<res.data.length;i++){
 				$('select[name="area"]').append('<option value="'+res.data[i].id+'">'+res.data[i].group_name+'</option>');
 			}
 		}
 	});
 } 

 
 function getData(){
	 $.post(urlpath+"GateShowProject/getcout",{
		 "token": getCookie('token'),
		 "area": $('select[name="area"] option:selected').val()!=0?$('select[name="area"] option:selected').val():$('select[name="area"] option:nth-child(2)').val()
	 },function(res){
		 if(res.code==200){
//			 $('.alert-success').html("数据统计成功");
//			 $('.alert-success').show(300).delay(1000).hide(300);
			 var tl=[],wl=[];
			 tl = JSON.parse(res.data.class_type_person);
			 wl = JSON.parse(res.data.worker_type_person);
			 
			 $('#typeInfo tbody').html("");
			 $('#workerInfo tbody').html("");
			 
			 for(var i=0;i<tl.length;i++){
				 var html = '';
				 html = `<tr class="warning">
                            <td>${tl[i].type_name}: <strong>${tl[i].type_count}人</strong></td>
                        </tr>`;
				 $('#typeInfo tbody').append(html);
			 }
			 for(var w=0;w<wl.length-3;){
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
		 }else {
//			 $('.alert-danger').html('数据统计失败');
//			 $('.alert-danger').show(300).delay(1000).hide(300);
		 }
	 });
 }
 
 $("#sureSearch").click(function(e){
	 e.preventDefault();
	 getData();
 });
 
 // 大屏数据清零
 $('#cleardata').click(function(){
	 sureTool("确定清零区域数据","",clearTotal);
 });
 
 function clearTotal(){
	 $.post(urlpath+"GateShowProject/cleardata",{
		 'token': getCookie('token'),
		 "area": $('select[name="area"] option:selected').val()!=0?$('select[name="area"] option:selected').val():$('select[name="area"] option:nth-child(2)').val()
	 },function(res){
		 tool(res.code==200,"数据清零成功","数据清零失败");
	 });
 }
 
 
 // 获取刷卡人员信息
 function personGate(){
	 $.post(urlpath+'GateShowProject/getlastenterperson',{
		 'token': getCookie('token'),
		 "area": $('select[name="area"] option:selected').val()!=0?$('select[name="area"] option:selected').val():$('select[name="area"] option:nth-child(2)').val()
	 },function(res){
		 if(res.code==200){
			 console.log(res.data.avatar_img.indexOf('http')!=-1?res.data.avatar_img:(urlpath+res.data.avatar_img));
			 $('#personname').html(res.data.name);
			 $('#team').html(res.data.team);
			 $('#worker_type').html(res.data.worker_type);
			 $('#punish_record').html(res.data.punish_record);
//			 $('#img').attr("src",res.data.avatar_img.indexOf('http')!=-1?res.data.avatar_img:(urlpath+res.data.avatar_img));
			 
			 $('#tableInfo').removeClass('col-xs-12').addClass("col-xs-8");
			 $('.personInfo').show();
		 }
	 });
 }
 
var timer = setInterval(function(){
	 personGate(); 
	 getData();
	 clearInterval(timer);
 },2000);

 
 
 