/**
 * modal 添加和修改信息操作
 */
$(function(){
	
	var id = $('#myModal').attr("data-id");
	if(id != '') {
		getBlock(id);
	}
	$('form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	groupname: {
                message: '分区名称验证失败',
                validators: {
                    notEmpty: {
                        message: '分区名称不能为空'
                    },
                }
            }
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        surBlock();
    });
	
});
// 通过id获取信息
function getBlock(id){
	$.post(urlpath+'Gategroup/getinfo',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		console.log(id);
		if(res.code==200){
			$('input[name="welcome"]').val(res.data.welcome);
			$('input[name="groupname"]').val(res.data.group_name);
		}
	});
}


//确认按钮用户
function surBlock(){
	var id = $('#myModal').attr("data-id");
	if(id != '') {// 编辑
		var paramJson = {
				"welcome": $('input[name="welcome"]').val(),
				"group_name": $('input[name="groupname"]').val(),
				 "id": id
		 };
		$.post(urlpath+'Gategroup/update',{
			"paramJson": JSON.stringify(paramJson),
			 "token": getCookie('token')
		},function(res){
			tool2(res.code==200,"myModal","闸机分区信息修改成功",message);
		});
	}else {// 新增用户
		var paramJson = {
				"welcome": $('input[name="welcome"]').val(),
				"group_name": $('input[name="groupname"]').val(),
		 };
		 $.post(urlpath+"Gategroup/add",{
			 "paramJson": JSON.stringify(paramJson),
			 "token": getCookie('token')
		 },function(res){
			 tool2(res.code==200,"myModal","闸机分区信息添加成功",message);
		 });	
	}	 
}
