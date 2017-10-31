/**
 * 人员列表中的model 操作js文件
 */
$(function(){
	initImg();
	getPapType();
	getCarType();
	var id = $('#myModal').attr("data-id");
	if(id != '') {
		getUserDetail(id);
	};
	
	$('form').bootstrapValidator({
        message: 'This value is not valid',
        excluded : [':disabled'],//[':disabled', ':hidden', ':not(:visible)']
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                message: '用户名验证失败',
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },
                }
            },
            mobile: {
                validators: {
                    notEmpty: {
                        message: '手机号不能为空'
                    },
                    stringlength:{
                        min:11,
                        max:11,
                        message:'请输入11位手机号码'
                    },
                    regexp:{
                    	regexp:/^1[3|5|8]{1}[0-9]{9}$/,
                    	message:'请输入正确的手机号码'
                    },
                    threshold :  11, //有11字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: 'http://localhost:8080/lanlyc/'+'GateUser/testMobile',//验证地址
                        message: '该手机号已存在',//提示消息
                        delay :  4000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        //**自定义提交数据，默认值提交当前input value**//*
                    	data: {
                    		"mobile": $('input[name="mobile"]').val(),
                    		"token": getCookie('token'),
                    		"user_id": id
                    	}
                    },
                }
            },
            visit_reason: {
            	validators: {
                    notEmpty: {
                        message: '来访事由不能为空'
                    },
                }
            },
            cid: {
            	validators: {
                    notEmpty: {
                        message: '身份证号码不能为空'
                    },
                    stringLength:{
                        min:18,
                        max:18,
                        message:'请输入18位身份证号码'
                    },
                   /* creditCard:{
                        message: '身份证格式有误'
                    },*/
                    
                    regexp:{
                    	regexp:/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
                    	message:'请输入正确格式的身份证'
                    },
                    
                    threshold :  18, //有11字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: urlpath+'GateVisit/selectCid',//验证地址
                        message: '身份证号码已存在',//提示消息
                        delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        /**自定义提交数据，默认值提交当前input value**/
                    	data: {
                    		"cid": $('input[name="cid"]').val(),
                    		"token": getCookie('token')
                    		/*"user_id": id*/
                    	}
                    },
                }
            },
            passport: {
            	validators: {
                    notEmpty: {
                        message: '护照号码不能为空'
                    },
                    stringLength:{
                        min:9,
                        max:9,
                        message:'请输入9位护照号码'
                    },
                    /*creditCard: {
                        message: '护照号码格式有误'
                    },*/
                    threshold :  9, //有11字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                   /* remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: urlpath+'GateVisit/selectVisitorById',//验证地址
                        message: '护照号码已存在',//提示消息
                        delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        *//**自定义提交数据，默认值提交当前input value**//*
                    	data: {
                    		"number": $('#pap_number').val(),
                    		"token": getCookie('token')
                    		"user_id": id
                    	}
                    },*/
                }
            },
          
/*            entry_date: {
                validators: {
                    integer: {},
                    callback: {
                        message: '开始日期不能大于结束日期',
                        callback:function(value, validator,$field,options){
                            var leave_date =$('input[name="leave_date"]').val();
                            return parseInt(value)<=parseInt(leave_date);
                        }
                    }
                }
            },
            leave_date: {
                validators: {
                    integer: {},
                    callback: {
                        message: '结束日期不能小于开始日期',
                        callback:function(value, validator,$field){
                            var entry_date = $('input[name="entry_date"]').val();
                            $('input[name="entry_date"]').keypress();
                            validator.updateStatus('entry_date', 'VALID');
                            return parseInt(value)>=parseInt(entry_date);
                        }
                    }
                }
            }*/
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureUser();
    });
})

/*function a(){
	$("form").data('bootstrapValidator').destroy();
	$('form').data('bootstrapValidator', null);
	$('form').bootstrapValidator({
        message: 'This value is not valid',
        excluded : [':disabled'],//[':disabled', ':hidden', ':not(:visible)']
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                message: '用户名验证失败',
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },
                }
            },
            mobile: {
                validators: {
                    notEmpty: {
                        message: '手机号不能为空'
                    },
                    stringlength:{
                        min:11,
                        max:11,
                        message:'请输入11位手机号码'
                    },
                    regexp:{
                    	regexp:/^1[3|5|8]{1}[0-9]{9}$/,
                    	message:'请输入正确的手机号码'
                    },
                    threshold :  11, //有11字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: 'http://localhost:8080/lanlyc/'+'GateUser/testMobile',//验证地址
                        message: '该手机号已存在',//提示消息
                        delay :  4000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        //**自定义提交数据，默认值提交当前input value**//*
                    	data: {
                    		"mobile": $('input[name="mobile"]').val(),
                    		"token": getCookie('token'),
                    		"user_id": id
                    	}
                    },
                }
            },
            visit_reason: {
            	validators: {
                    notEmpty: {
                        message: '来访事由不能为空'
                    },
                }
            },
            cid: {
            	validators: {
                    notEmpty: {
                        message: '身份证号码不能为空'
                    },
                    stringLength:{
                        min:18,
                        max:18,
                        message:'请输入18位身份证号码'
                    },
                    creditCard: {
                        message: '身份证格式有误'
                    },
                    threshold :  18, //有11字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: urlpath+'GateVisit/selectCid',//验证地址
                        message: '身份证号码已存在',//提示消息
                        delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        *//**自定义提交数据，默认值提交当前input value**//*
                    	data: {
                    		"cid": $('input[name="cid"]').val(),
                    		"token": getCookie('token')
                    		"user_id": id
                    	}
                    },
                }
            },
            passport: {
            	validators: {
                    notEmpty: {
                        message: '护照号码不能为空'
                    },
                    stringLength:{
                        min:9,
                        max:9,
                        message:'请输入9位护照号码'
                    },
                    creditCard: {
                        message: '护照号码格式有误'
                    },
                    threshold :  9, //有11字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: urlpath+'GateVisit/selectVisitorById',//验证地址
                        message: '护照号码已存在',//提示消息
                        delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        *//**自定义提交数据，默认值提交当前input value**//*
                    	data: {
                    		"number": $('#pap_number').val(),
                    		"token": getCookie('token')
                    		"user_id": id
                    	}
                    },
                }
            },
          
            entry_date: {
                validators: {
                    integer: {},
                    callback: {
                        message: '开始日期不能大于结束日期',
                        callback:function(value, validator,$field,options){
                            var leave_date =$('input[name="leave_date"]').val();
                            return parseInt(value)<=parseInt(leave_date);
                        }
                    }
                }
            },
            leave_date: {
                validators: {
                    integer: {},
                    callback: {
                        message: '结束日期不能小于开始日期',
                        callback:function(value, validator,$field){
                            var entry_date = $('input[name="entry_date"]').val();
                            $('input[name="entry_date"]').keypress();
                            validator.updateStatus('entry_date', 'VALID');
                            return parseInt(value)>=parseInt(entry_date);
                        }
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureUser();
    });

}*/


/*$('input[name="cid"]').blur(function(){
	console.log($('input[name="cid"]').val());
});*/

$("#pap_type").change(function(){ 
   if($('select[name="pap_type"] option:selected').val()=="1"){
	   $('#pap_number').attr("name","cid");
	   //a();
   }else if($('select[name="pap_type"] option:selected').val()!="1"){  
	   //$('#pap_number').attr("name","passport");
	   $('form').data('bootstrapValidator').enableFieldValidators('cid', false);
	   //$('form').data('bootstrapValidator').enableFieldValidators('passport', true);
	   //$('form').data('bootstrapValidator').enableFieldValidators('passport', true);
	   //a();
	   //$('form').data('bootstrapValidator').updateStatus('passport', 'NOT_VALIDATED').validateField('passport');
   }

       //$('form').data('bootstrapValidator').enableFieldValidators('cid', false);
       //$('form').data('bootstrapValidator').enableFieldValidators('passport', true);
       
   //$('form').data("bootstrapValidator").resetForm();
   //$('form').data('bootstrapValidator').updateStatus('passport', 'NOT_VALIDATED').validateField('passport');
}) 

$('#pap_number').blur(function(){
   if($('select[name="pap_type"] option:selected').val()=="2"){
	   $('#pap_number').attr("name","passport");
	   $('form').data('bootstrapValidator').enableFieldValidators('cid', false);
	   
	 //判断护照是否为null、""、一个空格或全部为空格的方法
   	if($("#pap_number").val().length==0||$("#pap_number").val()==""||
   	   $("#pap_number").val()==null||new RegExp("^[ ]+$").test($("#pap_number").val())
       || !new RegExp(/^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$/).test($("#pap_number").val())
       || !selectPapNumber($("#pap_number").val())){ 	
   		alert(!new RegExp(/^(1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+)$/).test($("#pap_number").val()))
	    	$("#username2").val("");
	    	$("#pap_number").val("");
	    	alert("请输入正确的护照号码")
   		    return false;
   	}
   }else if($('select[name="pap_type"] option:selected').val()=="3"){
	   if($("#pap_number").val().length==0||$("#pap_number").val()==""||
		 $("#pap_number").val()==null||new RegExp("^[ ]+$").test($("#pap_number").val())
	|| !new RegExp(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/).test($("#pap_number").val())
	|| !selectPapNumber($("#pap_number").val())){ 		
			    	$("#username2").val("");
			    	$("#pap_number").val("");
			    	alert("请输入正确格式的军官证号码")
		   		    return false;
		   	} 
   }else if($('select[name="pap_type"] option:selected').val()=="4"){
	   if($("#pap_number").val().length==0||$("#pap_number").val()==""||
				 $("#pap_number").val()==null||new RegExp("^[ ]+$").test($("#pap_number").val())
	|| !new RegExp(/^[a-zA-Z]([0-9]{9})$/).test($("#pap_number").val())
	|| !selectPapNumber($("#pap_number").val())){ 		
					$("#username2").val("");
					$("#pap_number").val("");
					 alert("请输入正确格式的台胞证号码")
				    return false;
				   	}  
   }else if($('select[name="pap_type"] option:selected').val()=="5"){
	   if($("#pap_number").val().length==0||$("#pap_number").val()==""||
				 $("#pap_number").val()==null||new RegExp("^[ ]+$").test($("#pap_number").val())
	|| !new RegExp("^[HMhm]{1}([0-9]{10}|[0-9]{8})$").test($("#pap_number").val())
	|| !selectPapNumber($("#pap_number").val())){		
					$("#username2").val("");
					$("#pap_number").val("");
					 alert("请输入正确格式的港澳居民来往内地通行证号码")
				    return false;
	     	}  
   }else if($('select[name="pap_type"] option:selected').val()=="1"){
	   $('#pap_number').attr("name","cid");
	   $('form').data('bootstrapValidator').enableFieldValidators('cid', true);
   }
})

//当证件号码不为身份证时判断号码是否已存在
function selectPapNumber(pap_number){
	var param={"cid":pap_number,"token":getCookie("token")}
	$.ajax({
        type:"post",
        url:urlpath+'GateVisit/selectCid',
        async: false,
        data:{
           "paramJson":JSON.stringify(param),
        },
        success:function(result){
    	  alert(JSON.stringify(result));
          if(result.valid==true){
        	  return true;
          }else{
        	  alert("证件号码重复");
        	  return false;
          }
	  }         
});
}

/**
 * 通过用户id 获取用户详情
 * @param id
 * @returns
 */
/*function  getUserDetail(id){
	if(id == '') {
		return;
	}
	var paramsData = {
		"id" : id,
		"token": getCookie("token")
	};
	$.ajax({
		type : 'post',
		url  : urlpath+'GateUser/querybyid',
		data : paramsData,
		success : function(res) {
			if(res.code==200){
				var user = res.data;
				$('input[name="gonghao"]').val(user.gonghao);
				$('input[name="username"]').val(user.name);
				$('input[name="cid"]').val(user.cid);
				$('input[name="mobile"]').val(user.mobile);
				$('input[name="team"]').val(user.team);
				$('select[name="gender"] option[value='+user.gender+']').attr("selected",true);
				$('select[name="worker_type"] option[value='+user.workers_type+']').attr("selected",true);
				$('select[name="person_type"] option[value='+user.user_class_id+']').attr("selected",true);
				$('input[name="punish_record"]').val(user.punish_record);
				img = user.avatar_img.indexOf('http')!=-1?user.avatar_img:(urlpath+user.avatar_img);
				var html = `<div id="uploadList_1" class="upload_append_list">
								<div class="file_bar">
									<div style="padding:5px;">
										<p class="file_name" title="btn_exit_select.png">btn_exit_select.png</p>
										<span class="file_del" data-index="1" title="删除"></span>
									</div>
								</div>
								<a style="height:85px;width:100px;" href="#" class="imgBox">
									<div class="uploadImg" style="width:85px;max-width:85px;max-height:75px;">
										<img id="uploadImage_1" class="upload_image" src="${img}" style="width:expression(this.width > 85 ? 85px : this.width);">
									</div>
								</a>
								<p id="uploadProgress_1" class="file_progress"></p>
								<p id="uploadFailure_1" class="file_failure">上传失败，请重试</p>
								<p id="uploadSuccess_1" class="file_success"></p>
							</div>`;
				$(".add_upload").before(html);
			}else {
				
			}
		}
	});
}*/




/**
 * 图片上传初始化
 * @returns
 */
function initImg(){
//  图片上传 初始化插件
    $("#zyupload").zyUpload({
        width            :   "450px",                 // 宽度
        height           :   "190px",                 // 宽度
        itemWidth        :   "100px",                 // 文件项的宽度
        itemHeight       :   "85px",                 // 文件项的高度
        url              :   "/lanlyc/FileUpload/save",  // 上传文件的路径
        fileType         :   ["jpg","png","js","exe"],// 上传文件的类型
        fileSize         :   51200000,                // 上传文件的大小
        multiple         :   false,                    // 是否可以多个文件上传
        dragDrop         :   false,                   // 是否可以拖动上传文件
        tailor           :   false,                   // 是否可以裁剪图片
        del              :   true,                    // 是否可以删除文件
        finishDel        :   false,  				  // 是否在上传文件完成后删除预览
        /* 外部获得的回调接口 */
        onSelect: function(selectFiles, allFiles){    // 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
        },
        onDelete: function(file, files){              // 删除一个文件的回调方法 file:当前删除的文件  files:删除之后的文件
        	
        },
        onSuccess: function(file, response){          // 文件上传成功的回调方法
//        	alertTool("图片上传的成功");
        	img = JSON.parse(response).data[0];
        },
        onFailure: function(file, response){          // 文件上传失败的回调方法
	        alertTool("图片上传的失败");
        },
    });
}


function getPapType(){
	//获取证件类型列表
	$.post(urlpath+"PapTypes/selectAllPapTypes",{
		"token": getCookie('token')
	},function(res){
		//alert(JSON.stringify(res));
		if(res.code==200){
			$('select[name="pop_type"]').html("");
			for(var i=0;i<res.data.length;i++){
				//alert(res.data[i].value+"    "+res.data[i].name)
				$('select[name="pap_type"]').append('<option value="'+res.data[i].value+'">'+res.data[i].name+'</option>');
			}
		}
	});
}

function getCarType(){
	//获取车辆类型列表
	$.post(urlpath+"CarTypes/selectAllCarTypes",{
		"token": getCookie('token')
	},function(res){
		//alert(JSON.stringify(res));
		if(res.code==200){
			$('select[name="car_type"]').html("");
			for(var i=0;i<res.data.length;i++){
				//alert(res.data[i].value+"    "+res.data[i].name)
				$('select[name="car_type"]').append('<option value="'+res.data[i].value+'">'+res.data[i].name+'</option>');
			}
		}
	});
}

//确认按钮用户
function sureUser(){
	var id = $('#myModal').attr("data-id");
	if(id != '') {// 编辑
		if(img=='null' || img==null){
			alertTool("请先上传图片");
		}else {
			var paramJson = {
					"gonghao": $('input[name="gonghao"]').val(),
					"name": $('input[name="username"]').val(),
					"gender": $('select[name="gender"] option:selected').val(),
					 "mobile": $('input[name="mobile"]').val(),
					 "workers_type": $('select[name="worker_type"] option:selected').val(),
					 "punish_record": $('input[name="punish_record"]').val(),
					 "cid": $('input[name="cid"]').val(),
					 "avatar_img": img,
					 "user_class_id": $('select[name="person_type"] option:selected').val(), // 人员类别id
					 "team": $('input[name="team"]').val(), // 班组
					 "id": id
			 };
			$.post(urlpath+'GateUser/updateGateuser',{
				"paramJson": JSON.stringify(paramJson),
				 "token": getCookie('token')
			},function(res){
				tool2(res.code==200,'myModal',"人员信息修改成功",res.message);
			});
		}
		
	}else {// 新增用户
		if(img=='null' || img==null){
			alertTool("请先上传图片");
		}else {
			if($("#username2").val()=="" ||$("#pap_number").val()==""){
				alert("请输入姓名和合理的证件号码");
				return;
			}
			var registration_dates=Date.parse(new Date());
			var registration_date=formats3(registration_dates).substr(0,formats3(registration_dates).length-9);
			//alert("registration_date             :"+registration_date)
			var paramJson = {
					 "name": $('input[name="username"]').val(),
					 "mobile": $('input[name="mobile"]').val(),
					 "car_type": $('select[name="car_type"] option:selected').val(), 
					 "car_number": $('input[name="car_number"]').val(),
					 "avatar_img": img,
					 "pap_type": $('select[name="pap_type"] option:selected').val(),
					 "pap_number": $('#pap_number').val(), 
					 "visit_reason": $('#visit_reason').val(),
					 "registration_date":registration_date,
					 "entry_date": $('input[name="entry_date"]').val(),
					 "leave_date": $('input[name="leave_date"]').val()
			 };
			alert($('#pap_number').val())
		     //alert(JSON.stringify(paramJson));
			 $.post(urlpath+"GateVisit/addVisit",{
				 "paramJson": JSON.stringify(paramJson),
				 "token": getCookie('token')
			 },function(res){
				 tool2(res.code==200,'myModal',"人员添加成功",res.message);
			 });	
		}
		
	}
}

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
