/**
 * 人员列表中的model 操作js文件
 */
$(function(){
	initImg();
	
	getWorkerType();
	var id = $('#myModal').attr("data-id");
	if(id != '') {
		getUserDetail(id);
	}
	
	$('form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'fa fa-check',
            invalid: 'fa fa-times',
            validating: 'fa fa-refresh'
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
                        url: '../GateUser/testMobile',//验证地址
                        message: '该手机号已存在',//提示消息
                        delay :  4000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        /**自定义提交数据，默认值提交当前input value**/
                    	data: {
                    		"mobile": $('input[name="mobile"]').val(),
                    		"token": getCookie('token'),
                    		"user_id": id
                    	}
                    },
                }
            },
            cid: {
            	validators: {
                    notEmpty: {
                        message: '身份证号码不能为空'
                    },
                    stringlength:{
                        min:18,
                        max:18,
                        message:'请输入18位身份证号码'
                    },
                    threshold :  18, //有11字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: '../GateUser/testcid',//验证地址
                        message: '身份证号码已存在',//提示消息
                        delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        /**自定义提交数据，默认值提交当前input value**/
                    	data: {
                    		"cid": $('input[name="id_card"]').val(),
                    		"token": getCookie('token'),
                    		"user_id": id
                    	}
                    },
                }
            },
            team: {
            	validators: {
                    notEmpty: {
                        message: '班组不能为空'
                    },
                }
            }
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureUser();
    });
});

var pimg="";
/**
 * 通过用户id 获取用户详情
 * @param id
 * @returns
 */
function  getUserDetail(id){
	
	if(id == '') {
		return;
	}
	var paramsData = {
		"id" : id,
		"token": getCookie("token")
	};
	$.ajax({
		type : 'post',
		url  : '../GateUser/querybyid',
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
				pimg = user.avatar_img.indexOf('http')!=-1?user.avatar_img:(urlpath+user.avatar_img);
				var html = `<div id="uploadList_1" class="upload_append_list">
								<div class="file_bar">
									<div style="padding:5px;">
										<p class="file_name" title="btn_exit_select.png">btn_exit_select.png</p>
										<span class="file_del" data-index="1" title="删除"></span>
									</div>
								</div>
								<a style="height:85px;width:100px;" href="#" class="imgBox">
									<div class="uploadImg" style="width:85px;max-width:85px;max-height:75px;">
										<img id="uploadImage_1" class="upload_image" src="${pimg}" style="width:expression(this.width > 85 ? 85px : this.width);">
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
}




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
        	alertTool2("上传成功");
        	pimg = JSON.parse(response).data[0];
        },
        onFailure: function(file, response){          // 文件上传失败的回调方法
	        alertTool("图片上传的失败");
        },
    });
}


function getWorkerType(){
	//获取工种列表
	$.post("../WorkersTypes/queryAll",{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			$('select[name="worker_type"]').html("");
			for(var i=0;i<res.data.length;i++){
				$('select[name="worker_type"]').append('<option value="'+res.data[i].value+'">'+res.data[i].name+'</option>');
			}
		}
	});
	//获取人员类别
	$.post("../GateUserClass/queryUserClassAll",{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			$('select[name="person_type"]').html("");
			for(var i=0;i<res.data.length;i++){
				$('select[name="person_type"]').append('<option value="'+res.data[i].id+'">'+res.data[i].name+'</option>');
			}
		}
	});
}

//确认按钮用户
function sureUser(){
	
	var id = $('#myModal').attr("data-id");
	if(id != '') {// 编辑
		if(pimg=='null' || pimg==null){
			alertTool("请先上传图片");
		}else {
			var paramJson = {
					"gonghao": Trim($('input[name="gonghao"]').val()),
					"name": Trim($('input[name="username"]').val()),
					"gender": $('select[name="gender"] option:selected').val(),
					 "mobile": Trim($('input[name="mobile"]').val()),
					 "workers_type": $('select[name="worker_type"] option:selected').val(),
					 "punish_record": Trim($('input[name="punish_record"]').val()),
					 "cid": Trim($('input[name="cid"]').val()),
					 "avatar_img": pimg,
					 "user_class_id": $('select[name="person_type"] option:selected').val(), // 人员类别id
					 "team": Trim($('input[name="team"]').val()), // 班组
					 "id": id
			 };
			$.post('../GateUser/updateGateuser',{
				"paramJson": JSON.stringify(paramJson),
				 "token": getCookie('token')
			},function(res){
				tool2(res.code==200,'myModal',"人员信息修改成功",res.message);
				pimg="";
			});
		}
		
	}else {// 新增用户
		if(pimg=='null' || pimg==null){
			alertTool("请先上传图片");
		}else {
			var paramJson = {
					"name": Trim($('input[name="username"]').val()),
					"gender": $('select[name="gender"] option:selected').val(),
					 "mobile": Trim($('input[name="mobile"]').val()),
					 "workers_type": $('select[name="worker_type"] option:selected').val(),
					 "punish_record": Trim($('input[name="punish_record"]').val()),
					 "cid": Trim($('input[name="cid"]').val()),
					 "avatar_img": pimg,
					 "user_class_id": $('select[name="person_type"] option:selected').val(), // 人员类别id
					 "team": Trim($('input[name="team"]').val()) // 班组
			 };
		
			 $.post("../GateUser/addGateUser",{
				 "paramJson": JSON.stringify(paramJson),
				 "token": getCookie('token')
			 },function(res){
				 tool2(res.code==200,'myModal',"人员添加成功",res.message);
				 pimg="";
			 });	
		}
	}
	table.ajax.reload(null, false);
}

