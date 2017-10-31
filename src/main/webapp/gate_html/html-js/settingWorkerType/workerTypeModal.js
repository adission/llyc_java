/**
 * modal 添加和修改信息操作
 */
$(function(){
	
	var id = $('#myModal').attr("data-id");
	if(id != '') {
		getWork(id);
	}
	$('form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	workname: {
                message: '工种名称验证失败',
                validators: {
                    notEmpty: {
                        message: '工种名称不能为空'
                    },
                }
            }
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureWork();
    });
	
});
// 通过id获取信息
function getWork(id){
	$.post(urlpath+'WorkersTypes/querybyid',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		console.log(id);
		if(res.code==200){
			$('input[name="workname"]').val(res.data.name);
			$('input[name="order_by"]').val(res.data.order_by);
		}
	});
}


//确认按钮用户
function sureWork(){
	var id = $('#myModal').attr("data-id");
	if(id != '') {// 编辑
		var paramJson = {
				"work_name": $('input[name="workname"]').val(),
				"order_by": $('input[name="order_by"]').val(),
				 "id": id
		 };
		$.post(urlpath+'WorkersTypes/update',{
			"paramJson": JSON.stringify(paramJson),
			 "token": getCookie('token')
		},function(res){
			tool2(res.code==200,"myModal","工种修改成功","工种修改失败");
		});
	}else {// 新增工种
		var paramJson = {
				"work_name": $('input[name="workname"]').val(),
				"order_by": $('input[name="order_by"]').val(),
		 };
		 $.post(urlpath+"WorkersTypes/add",{
			 "paramJson": JSON.stringify(paramJson),
			 "token": getCookie('token')
		 },function(res){
			 tool2(res.code==200,"myModal","工种添加成功","工种添加失败");
		 });	
	}	 
}
