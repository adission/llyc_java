//关于点击基站、工人、访客弹窗的方法
map.on('click', function (evt) {
//	  window.setTimeout(function() {
	      var features = [];
 		  urlLocation="http://localhost:8080/lanlyc";
	      
	      map.forEachFeatureAtPixel(evt.pixel, function(feature) {
	    	  if(feature.getGeometryName()=="user"){
	    		  var coordinate = evt.coordinate;
		          var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(
		                  coordinate, 'EPSG:3857', 'EPSG:4326'));
		
		          var datas;
		          alert(feature.get("color"));
		          feature.set("color","#ff00ff")
		          alert(feature.get("color"));
		          alert(feature.get("userId")+"   "+feature.get("type"));
		          console.log(feature.get("userId")+"   "+feature.get("type"))
		          //var types=feature.get("type");
		          var types=1;
			      //var param={"id":"ff1490066d004b19ada2eba8ffe993c3","token":getCookie("token")}
			      var param={"id":feature.get("userId"),"token":getCookie("token")}
			      //还需要判断人员是访客还是工人，确定ajax调用的方法
			      if(types==1){
			    	  //alert("工人")
			          $.ajax({
				          type:"post",
				          url:"/lanlyc/Pos/selectWorkerById",
				          data:{
				             "paramJson":JSON.stringify(param),
				          },
				          success:function(result){
				      	  if(result.code=="200"){
				      	      datas=result.data;

				      	  $("#username1").text("姓名:");
				      	  $("#username").text(datas.name);
				      	  $("#label_id1").text("标签ID:");
				      	  $("#label_id").text(datas.card_sn);
				      	  $("#workerstype1").text("工种:")
				      	  $("#workerstype").text(datas.workerstype)
				      	  datas.gender=(datas.gender=='1'?"男":"女");
				      	  $("#gender1").text("性别:")
		                  $("#gender").text(datas.gender)
		                  $("#nation1").text("民族:")
		                  $("#nation").text(datas.nation)
		                  var cid=datas.cid;
				      	  $("#cid1").text("身份证号:") 
		                  $("#cid").text(cid)  
			   		      var birthday=datas.birthday;
			   		      //如果数据库中没有birthday，那么就截取身份证上的
			   		      if(birthday==null || birthday=="" || birthday=== undefined){
			   		    	 if(cid != null && cid !="" && cid != undefined){
			   		    		 var year=datas.cid.substring(6,10);
			   		    		 var month=datas.cid.substring(10,12);
			   		    		 var day=datas.cid.substring(12,14);
			   		    		 birthday=year+"-"+month+"-"+day;
			   		    	 }
			   		      }
			   		      $("#gender").css("margin-left","4px");
			   		      $("#birthday").css("margin-left","4px");
			   		      $("#birthday1").css("margin-left","7px");
			   		      $("#birthday1").text("出生日期:"); 
				   		  $("#birthday").text(birthday); 
				   		  $("#mobile1").text("联系方式:");
				   		  $("#mobile").text(datas.mobile);
				   		  $("#native_place1").css("margin-left","-2px");
				   		  $("#native_place1").text("籍贯:");
				   		  $("#native_place").text("湖北");//这个需要表中添加字段
				   		  var user_type=datas.userclass;
				   		  var gonghao=datas.gonghao;
				   		  $("#department1").text("部门:");
				   		  $("#department").text(datas.team);//这个是班组 要改为部门,表中要添加字段			   		     
				   		  var image=(datas.avatar_img.indexOf("http") ==-1?urlLocation+datas.avatar_img:datas.avatar_img);
				   		  $("#abc").css("display","none");
				   		  $("#userimage").attr("src",image)
				   		  $("#abc").css("display","none");			      	  
				   		  $("#baseStation").hide();
		                  $("#user_worker").show();  
				          overlay.setPosition(coordinate);// 弹窗位置重置	 
				      	}else{
				      	       alert(result.message);   
				      	}
			    	  }         
		          });
			     }else{
			    	  //alert("访客")
			          $.ajax({
				          type:"post",
				          url:"/lanlyc/GateVisit/selectVisitorById",
				          data:{
				             "paramJson":JSON.stringify(param),
				          },
				          success:function(result){
				        	  //alert(JSON.stringify(result))
				      	     if(result.code=="200"){
				      	    	 datas=result.data;
				      	    $("#user_worker").css("min-width","428px");	 
				   			$("#username").text(datas.name); 
				   			$("#username1").text("访客:"); 
				   			$("#label_id").text(datas.card_sn); 
				   			$("#label_id1").text("卡号:");
				   			var registration_date=formats3(datas.registration_date).substr(0,formats3(datas.registration_date).length-9);
				   			$("#workerstype").text(registration_date); 
				   			$("#workerstype1").text("登记日期:");
				   			var entry_date=formats3(datas.entry_date).substr(0,formats3(datas.entry_date).length-3);
				   			$("#gender").text(entry_date); 
				   			$("#gender1").text("进入时间:");
				   			$("#gender").css("margin-left","0px");
				   			$("#birthday1").css("margin-left","-22px");
				   			$("#birthday").css("margin-left","0px");
				   			var leave_date=formats3(datas.leave_date).substr(0,formats3(datas.leave_date).length-3);
				   			$("#birthday").text(leave_date); 
				   			$("#birthday1").text("离开时间:");
				   			$("#nation").text(datas.car_type); 
				   			$("#nation1").text("车辆类型");
				   			$("#native_place1").css("margin-left","-5px");
				   			$("#native_place").text(datas.car_number); 
				   			$("#native_place1").css("margin-left","-25px");
				   			$("#native_place1").text("车牌号:");
				   			$("#mobile").text(datas.mobile); 
				   			$("#mobile1").text("联系方式:");
				   			$("#department").text("水电费斯");
				   			$("#department1").css("margin-left","-29px");
				   			$("#department1").text("证件类型:");
				   			$("#cid").text(datas.pap_number); 
				   			$("#cid1").text("证件号码:");
				   			$("#abc").css("display","block");
				   			$("#yy").text(datas.visit_reason); 
				   			$("#yy1").text("来访事由:");

				   			//alert(urlLocation+"    "+datas.avatar_img)
				   			var image=(datas.avatar_img.indexOf("http") ==-1?urlLocation+datas.avatar_img:datas.avatar_img);
					   		$("#userimage").attr("src",image)
					   		//alert(image);				   		   
			                $("#baseStation").hide();
                            $("#user_worker").show();  
		                    overlay.setPosition(coordinate);// 弹窗位置重置	 
				      	   }else{
				      	       alert(result.message);   
				      	     }
			    	  }         
		          });
			      }
		          
		           
	    	  }else if (feature.getGeometryName() == "base") {
		              var coordinate = evt.coordinate;
		              var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(
		                  coordinate, 'EPSG:3857', 'EPSG:4326'));
		              
		              var datas;
		              //alert(feature.get("type")+"    "+feature.get("baseId"))
				      var param={"id":feature.get("baseId"),"token":getCookie("token")}
		              //alert(JSON.stringify(param))
			          $.ajax({
			          type:"post",
			          url:"/lanlyc/Pos/selectBaseStationById",
			          data:{
			             "paramJson":JSON.stringify(param),
			          },
			          success:function(result){
			      	     if(result.code=="200"){
			      	    	 datas=result.data;
			      	    	 //alert(JSON.stringify(datas))
			      	    	 
			      	     datas.type=(datas.type=='1'?"无线":datas.type=='2'?"网关":'双监');	 
			      	     $("#type").text(datas.type);
			      	     $("#mc").text(datas.mc);
			      	     $("#number").text(datas.base_number);
			      	      $('#status').css("color","");
			      	     datas.status=(datas.status=='1'?'正常':'故障');
			      	     $("#status").text(datas.status);
			      	     if(datas.status=='故障'){
			      	    	 $('#status').css("color","red");
			      	     }
			      	     
                         $("#mapname").text(datas.layerName);//需要同过地图名称获取地图名
                         $("#x_coordinate").text(datas.geo_x);
                         $("#y_coordinate").text(datas.geo_y);
                         $("#ip").text(datas.ip);
                         $("#port").text(datas.port);
                         $("#yxjl").text(datas.yxjl);
                         $("#user_worker").hide();
                         $("#baseStation").show();
                         
			              overlay.setPosition(coordinate);// 弹窗位置重置			   		     
			      	   }else{
			      	       alert(result.message);   
			      	     }
		    	  }         
	          });

		          }else if (feature.getGeometryName() == "video") {
		              var coordinate = evt.coordinate;
		              var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(
		                  coordinate, 'EPSG:3857', 'EPSG:4326'));
		              //content.innerHTML = '<p class="text-danger">摄像头' + feature.get("videoId") + '被点击了</p> <button class="btn btn-primary">轨迹回放</button>';
		              //overlay.setPosition(coordinate);// 弹窗位置重置
		              
		              
		              var param={"id":"ff1490066d004b19ada2eba8ffe993c3","layer_id":"22695c4659f04fb781f316cc332ef176","token":getCookie("token")}
		              $.ajax({
				          type:"post",
				          url:"/lanlyc/Pos/selectCameraByUser",
				          data:{
				             "paramJson":JSON.stringify(param),
				          },
				          success:function(result){
				        	  alert(JSON.stringify(result))
				        	  alert("进来了");
				          }
				      	            
		          });
		              
		              
		              
		              
		          }
		      });
//		        }, 1);
		  });

function add0(m){
	return m<10?'0'+m:m 
}
function formats3(shijianchuo){
	//shijianchuo是整数，否则要parseInt转换
	var time = new Date(shijianchuo);
	var y = time.getFullYear();
	var m = time.getMonth()+1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}