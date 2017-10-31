var vm = new Vue({
    el:"#vue",
    data:{
    	vidicon_list:{},	//摄像头列表
    },
    methods:{
    	
    	getFunction:function(){
			var data = {
					"token": getCookie('token')
				};
			$.ajax({
				type: "POST",
				cache: false,
				data: {"paramJson":JSON.stringify(data)},
				url: "../sysetem/getFunction",
				dataType: "json",
				success: function (result){
					console.log(result);
					if(result.data==1){
						window.location.replace("../video_html2/live_control.html");
					}
					if(result.data==2){
						window.location.replace("../video_html2/video_playback.html");
					}
					if(result.data==3){
						window.location.replace("../video_html2/camera_manage.html");
					}
					if(result.data==4){
						window.location.replace("../video_html2/system_manage.html");
					}
//					else{
//						window.location.replace("");
//					}
						
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						console.log("获取数据失败");
						}
					})
			},
    	
    	
    	//恢复系统默认参数
    	reduction:function(){
    		if(confirm('确认恢复出厂设置?'))
    		{
    			var data = {
    					"token": getCookie('token')
    					};
    			$.ajax({
    				type: "POST",
    				cache: false,
    				data: {"paramJson":JSON.stringify(data)},
    				url: "../sysetem/reduction",
    				dataType: "json",
    				success: function (result){
    					console.log(result);
    					if(result.code == 200){
    						alert("恢复出厂设置成功");
    						}else{
    							alert("系统繁忙，请稍后再试");
    							}
    					},
    					error: function(XMLHttpRequest, textStatus, errorThrown) {
    						alert("获取数据失败");
    						}
    					})
    					}
    		},
    		
    		
    	//摄像头设置
    	cameraSetting:function(){
    		var vidicon_id = $("#vidicon_type").val();
			var ip = "";
			var username = "";
			var password = "";
			
			 $.each((vm.$data.vidicon_list), function(){ 
				 if(this.id == vidicon_id){
					 ip = this.vidicon_ip;
					 username = this.vidicon_username;
					 password = this.vidicon_password;
				 }   
				 }); 
			 console.log(vidicon_id);
			 console.log(ip);
    		var data = {
    				"token": getCookie('token'),
    				"focus": $("#focus").val(),
    				"preset":$("#preset").val(),
    				"cruise":$("#cruise").val(),
    				"cruise_time":$("#cruise_time").val(),
    				"ip":ip,
    				"username":username,
    				"password":password
    				};
    		if (ip =="192.168.3.2"){
    			alert("设备不支持此功能");
    			return
    		}
			$.ajax({
				type: "POST",
				cache: false,
				data: {"paramJson":JSON.stringify(data)},
				url: "../sysetem/cameraSetting",
				dataType: "json",
				success: function (result){
					console.log(result);
					if(result.code == 200){
						alert("设置成功");
						}else{
							alert("系统繁忙，请稍后再试");
							}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert("获取数据失败");
						}
					})
			},
    					
    					
    					
		//视频设置
		videoSetting:function(){
			var vidicon_id = $("#video_type").val();
			var brightness = $("#brightness").val();
			var contrast = $("#contrast").val();
			var saturation = $("#saturation").val();
			var chroma = $("#chroma").val();
			var DeviceTime = $("#DeviceTime").val();
			var DeviceName = $("#DeviceName").val();
			var Dname = $("#Dname").val();
			var ip = "";
			var username = "";
			var password = "";
			if ((brightness =="") || (contrast =="")|| (saturation =="")|| (saturation =="")|| (chroma =="")){
				alert("输入不能为空");
				return;
			}
			if ((DeviceName =="1")&&(Dname == "")){
				alert("输入不能为空");
				return;
			}
			 $.each((vm.$data.vidicon_list), function(){ 
				 if(this.id ==vidicon_id){
					 ip = this.vidicon_ip;
					 username = this.vidicon_username;
					 password = this.vidicon_password;
				 }   
				 }); 
			 
			var data = {
					"token": getCookie('token'),
    				"brightness":brightness,
    				"contrast": contrast,
					"saturation":saturation,
					"chroma": chroma,
					"DeviceTime":DeviceTime,
					"DeviceName":DeviceName,
					"Dname":Dname,
					"vidicon_id":vidicon_id,
					"ip":ip,
					"username":username,
					"password":password
    				};
			$.ajax({
				type: "POST",
				cache: false,
				data: {"paramJson":JSON.stringify(data)},
				url: "../sysetem/videoSetting",
				dataType: "json",
				success: function (result){
					console.log(result);
					if(result.code == 200){
						alert("设置成功");
						}else{
							alert("系统繁忙，请稍后再试");
							}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert("获取数据失败");
						}
					})
			},
        			
        		
			
        	
		//系统状态
		systemState:function(){
			var data = {
					"token": getCookie('token'),
    				"function":$("#function").val(),
    				"screen":$("#screen").val(),
    				"polling":$("#polling").val(),
    				"polling_time":$("#polling_time").val()
    				};
			$.ajax({
				type: "POST",
				cache: false,
				data: {"paramJson":JSON.stringify(data)},
				url: "../sysetem/systemState",
				dataType: "json",
				success: function (result){
					console.log(result);
					if(result.code == 200){
						alert("设置成功");
						}else{
							alert("系统繁忙，请稍后再试");
							}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert("获取数据失败");
						}
					})
		},		
        			
     
		//是否巡航改变时间
		isCruise:function(){
			if($("#cruise").val() == "38"){
				$("#cruise_time").val("0");
				$("#cruise_time").attr("disabled",true);   // 禁用文本框
			}
			else{
				$("#cruise_time").attr("disabled",false);   // 禁用文本框
			}
		},
		
		//是否显示视频设备的名称
		isShow:function(){
			if($("#DeviceName").val() == "0"){
				$("#Dname").hide();   // 隐藏文本框
			}
			else{
				$("#Dname").show();  // 显示文本框
			}
		},
		init:function(){
			var data = {
					"token": getCookie('token'),
    				"whether_important":2,
    				};
			$.ajax({
				type: "POST",
				cache: false,
				data: {"paramJson":JSON.stringify(data)},
				url: "../vidicon/getAllVidicon",
				dataType: "json",
				success: function (result){
					vm.$data.vidicon_list = result.data;
					console.log(vm.$data.vidicon_list);
					
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert("获取数据失败");
						}
					})
		},
    }
});
//vm.getFunction();
vm.init();
