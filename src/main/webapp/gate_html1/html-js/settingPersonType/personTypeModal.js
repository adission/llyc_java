/**
 * modal 添加和修改信息操作
 */
$(function(){
	
	var id = $('#myModal').attr("data-id");
	if(id != '') {
		getPsersonType(id);
	}
	
	$('form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'fa fa-check',
            invalid: 'fa fa-times',
            validating: 'fa fa-refresh'
        },
        fields: {
        	classname: {
                message: '人员类别名称验证失败',
                validators: {
                    notEmpty: {
                        message: '人员类别名称不能为空'
                    },
                }
            }
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureType();
    });
});
// 通过id获取信息
function getPsersonType(id){
	$.post(urlpath+'GateUserClass/queryUserClassbyid',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			$('input[name="classname"]').val(res.data.name);
		}
	});
}

//确认按钮用户
function sureType(){
	var id = $('#myModal').attr("data-id");
	if(id != '') {// 编辑
		var paramJson = {
				"class_name": Trim($('input[name="classname"]').val()),
				 "id": id
		 };
		$.post(urlpath+'GateUserClass/updateUserClass',{
			"paramJson": JSON.stringify(paramJson),
			 "token": getCookie('token')
		},function(res){
			tool2(res.code==200,"myModal","人员类别名称修改成功",res.message);
		});
	}else {// 新增用户
		var paramJson = {
				"class_name": Trim($('input[name="classname"]').val()),
		 };
		 $.post(urlpath+"GateUserClass/addUserClass",{
			 "paramJson": JSON.stringify(paramJson),
			 "token": getCookie('token')
		 },function(res){
			 tool2(res.code==200,"myModal","人员类别名称添加成功",res.message);
		 });	
	}	
	table.ajax.reload(null, false);
}
