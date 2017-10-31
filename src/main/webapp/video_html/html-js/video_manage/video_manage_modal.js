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
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
			vidicon_name:{
				message: 'The username is not valid',
				validators: {
					notEmpty: {
						message: '摄像头名称不能为空'
					}
				}
			},
			vidicon_ip:{
				message: 'The username is not valid',
				validators: {
					notEmpty: {
						message: '摄像头IP不能为空'
					}
				}
			},
			vidicon_port:{
				message: 'The username is not valid',
				validators: {
					notEmpty: {
						message: '摄像头端口不能为空'
					}
				}
			},
			vidicon_username:{
				message: 'The username is not valid',
				validators: {
					notEmpty: {
						message: '摄像头用户名不能为空'
					}
				}
			},
			vidicon_password:{
				message: 'The username is not valid',
				validators: {
					notEmpty: {
						message: '摄像头密码不能为空'
					}
				}
			},
			vidicon_desc:{
				message: 'The username is not valid',
				validators: {
					notEmpty: {
						message: '摄像头描述不能为空'
					}
				}
			},
			vidicon_type:{
				message: 'The username is not valid',
				validators: {
					notEmpty: {
						message: '摄像头类型不能为空'
					}
				}
			},
			whether_important: {
                message: 'The username is not valid',
                validators: {
                    notEmpty: {
                        message: '是否设为重点不能为空'
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureUser();
    });
})

//确认按钮用户
function sureUser(){
	var id = $('#myModal').attr("data-id");
	var paramJson = {
		"vidicon_name": $('input[name="vidicon_name"]').val(),
		"vidicon_ip": $('input[name="vidicon_ip"]').val(),
		"vidicon_port": $('input[name="vidicon_port"]').val(),
		"vidicon_username": $('input[name="vidicon_username"]').val(),
		"vidicon_password": $('input[name="vidicon_password"]').val(),
		"vidicon_desc": $('input[name="vidicon_desc"]').val(),
		"vidicon_type": $('input[name="vidicon_type"]').val(),
		"whether_important": $('input[name="whether_important"]').val(),
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



