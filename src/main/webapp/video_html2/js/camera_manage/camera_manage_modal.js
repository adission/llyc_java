/**
 * 摄像头列表中的model 操作js文件
 */
$(function(){
	var id = $('#myModal').attr("data-id");
	//编辑
	if(id != '') {
		getDetail(id);
	}
	
	$('form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'fa fa-check',
            invalid: 'fa fa-times',
            validating: 'fa fa-refresh'
        },
        fields: {
			vidicon_name:{				
				validators: {
					notEmpty: {
						message: '摄像头名称不能为空'
					},
					threshold :  1, //有1字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: '../vidicon/judgeNameWhetherRepeat',//验证地址
                        message: '该摄像头名称已存在',//提示消息
                        delay :  1000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置1秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        //**自定义提交数据，默认值提交当前input value**//*
                    	data: {
                    		"vidicon_name": $('input[name="vidicon_name"]').val(),
                    		"token": getCookie('token')
                    	}
                    }
				}
			},
			vidicon_ip:{			
				validators: {
					notEmpty: {
						message: '摄像头IP不能为空'
					},
					regexp:{
                    	regexp:/^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/,
                    	message:'请输入正确的IP'
                    },
					threshold :  1, //有1字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: '../vidicon/judgeIpWhetherRepeat',//验证地址
                        message: '该摄像头IP已存在',//提示消息
                        delay :  1000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置1秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        //**自定义提交数据，默认值提交当前input value**//*
                    	data: {
                    		"vidicon_ip": $('input[name="vidicon_ip"]').val(),
                    		"token": getCookie('token')
                    	}
                    }
				}
			},
			vidicon_port:{
				validators: {
					notEmpty: {
						message: '摄像头端口不能为空'
					},
					regexp:{
                    	regexp:/^[1-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)/,
                    	message:'请输入正确的端口号'
                    }
				}
			},
			vidicon_username:{
				validators: {
					notEmpty: {
						message: '摄像头用户名不能为空'
					}
				}
			},
			vidicon_password:{
				validators: {
					notEmpty: {
						message: '摄像头密码不能为空'
					}
				}
			},
			vidicon_type:{
				validators: {
					notEmpty: {
						message: '摄像头类型不能为空'
					}
				}
			},
			whether_important: {
                validators: {
                    notEmpty: {
                        message: '是否设为重点不能为空'
                    }
                }
            },
            nvr_ip:{
				validators: {
					notEmpty: {
						message: 'NVR的IP不能为空'
					},
					regexp:{
                    	regexp:/^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/,
                    	message:'请输入正确的IP'
                    }
				}
			},
			nvr_port:{
				validators: {
					notEmpty: {
						message: 'NVR的端口号不能为空'
					},
					regexp:{
                    	regexp:/^[1-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)/,
                    	message:'请输入正确的端口号'
                    }
				}
			},
			nvr_name:{
				validators: {
					notEmpty: {
						message: 'NVR的用户名不能为空'
					}
				}
			},
			nvr_password:{
				validators: {
					notEmpty: {
						message: 'NVR的密码不能为空'
					}
				}
			},
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureUser();
    });
})

//确认按钮
function sureUser(){
	var id = $('#myModal').attr("data-id");
	var paramJson = {
		"vidicon_name": $('input[name="vidicon_name"]').val(),
		"vidicon_ip": $('input[name="vidicon_ip"]').val(),
		"vidicon_port": $('input[name="vidicon_port"]').val(),
		"vidicon_username": $('input[name="vidicon_username"]').val(),
		"vidicon_password": $('input[name="vidicon_password"]').val(),
		"vidicon_desc": $('input[name="vidicon_desc"]').val(),
		"vidicon_type": $('input[name="vidicon_type"]:checked').val(),
		"whether_important": $('input[name="whether_important"]:checked').val(),
		"id":id
	};
	if(id != '') {// 编辑
		$.post("../vidicon/updateVidicon",{
			"paramJson": JSON.stringify(paramJson),
			"token": getCookie('token')
		},function(res){
			console.log(res);
			if(res.code=="200"){
				alert("修改成功");
				$('#myModal').modal('hide');
			}
		});
	}else {// 新增
		$.post("../vidicon/addVidicon",{
			"paramJson": JSON.stringify(paramJson),
			"token": getCookie('token')
		},function(res){
			console.log(res);
			if(res.code=="200"){
				alert("新增成功");
				$('#myModal').modal('hide');
//				table.draw( false );
			}
		});
	}

}

//获取详情
function getDetail(id){
	var paramJson = {
    		"id":id
    };
	$.post("../vidicon/getOneVidicon",{
		"paramJson": JSON.stringify(paramJson),
		"token": getCookie('token')
	},function(res){
		console.log(res);
		$('input[name="vidicon_name"]').val(res.data.vidicon_name);
		$('input[name="vidicon_ip"]').val(res.data.vidicon_ip);
		$('input[name="vidicon_port"]').val(res.data.vidicon_port);
		$('input[name="vidicon_username"]').val(res.data.vidicon_username);
		$('input[name="vidicon_password"]').val(res.data.vidicon_password);
		$('input[name="vidicon_desc"]').val(res.data.vidicon_desc);
		$('input[name="vidicon_type"]').val(res.data.vidicon_type);
		$('input[name="whether_important"]').val(res.data.whether_important);
	});
}



