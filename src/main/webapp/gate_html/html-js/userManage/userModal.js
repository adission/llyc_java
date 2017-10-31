/**
 * 用户管理中的model 操作js文件
 */
$(function(){
	
	initImg();
	
	var id = $('#myModal').attr("data-id");
	if(id != '') {
		getUserDetail(id);
	}
	
	$('form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	user_name: {
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
                    }
                }
            },
            pass_word: {
                message:'密码无效',
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    },
                    identical: {//相同
                        field: 'repassword', //需要进行比较的input name值
                        message: '两次密码不一致'
                    }
                }
            },
            repassword: {
                message: '密码无效',
                validators: {
                    notEmpty: {
                        message: '两次密码不一致'
                    },
                    identical: {//相同
                        field: 'pass_word',
                        message: '两次密码不一致'
                    }
                }
            },
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureUser();
    });
	
});
var img;
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
		url  : urlpath+'User/getuserinfo',
		data : paramsData,
		success : function(res) {
			if(res.code==200){
				var user = res.data;
				$('input[name="user_name"]').val(user.user_name);
				$('input[name="pass_word"]').val(user.pass_word);
				$('input[name="repassword"]').val(user.pass_word);
				$('input[name="mobile"]').val(user.mobile);
				
				img = user.image.indexOf('http')!=-1?user.image:(urlpath+user.image);
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
				alertTool("人员详情获取失败，请刷新后重新打开");
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
        width            :   "350px",                 // 宽度
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
        	img = JSON.parse(response).data[0];
        },
        onFailure: function(file, response){          // 文件上传失败的回调方法
        },
        onComplete: function(response){           	  // 上传完成的回调方法
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
					"user_name": $('input[name="user_name"]').val(),
					"pass_word": $('input[name="pass_word"]').val(),
					 "mobile": $('input[name="mobile"]').val(),
					 "image": img,
					 "id": id
			 };
			$.post(urlpath+'User/updateuser',{
				"paramJson": JSON.stringify(paramJson),
				 "token": getCookie('token')
			},function(res){
				tool2(res.code==200,"myModal","用户信息修改成功","用户信息修改失败");
				table.ajax.reload(null, false);
			});
		}
		
	}else {// 新增用户
		if(img=='null' || img==null){
			alertTool("请先上传图片");
		}else {
			var paramJson = {
					"user_name": $('input[name="user_name"]').val(),
					"pass_word": $('input[name="pass_word"]').val(),
					 "mobile": $('input[name="mobile"]').val(),
					 "image": img
			 };
			 $.post(urlpath+"User/add",{
				 "paramJson": JSON.stringify(paramJson),
				 "token": getCookie('token')
			 },function(res){
				 tool2(res.code==200,"myModal","用户信息添加成功","用户信息添加失败");
				 table.ajax.reload(null, false);
			 });	
		}
		
	}
}






