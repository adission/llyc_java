/**
 * Created by Administrator on 2017/9/18.
 * 标签颜色设置
 */
$(function(){    //背景颜色初始化
	var h = $("#workname").height();
	var inputdic = {"工作人员":"workname","外来人员":"outname"}
	var spandic = {"工作人员":"workcolor","外来人员":"outcolor"}
	var paramJson = {"token":"132"};
	$.post("../cardtype/list",{"paramJson":JSON.stringify(paramJson)},function(res){
		if(res.code == "200"){
			var data = res.data;
			for (var i = 0; i < data.length; i++) {
				var inputid = inputdic[data[i].typeName];
				var spanid = spandic[data[i].typeName];
				$("#"+inputid).val(data[i].color)
				$("#"+spanid).css("background-color",data[i].color);
			}
		}
		
	})

	
})

function changecolor(id){  //跟随输入框修改颜色
	alert($(this).attr("id"));
	$("#"+id).css('background-color', $(this).val());
	
}
function sureColor(){  //确定
	var paramJson = {
		"workcolor":$("#workname").val(),
		"outcolor" : $("#outname").val(),
		"token" : "123"
	}
	$.post("../cardtype/update",{"paramJson":JSON.stringify(paramJson)},function(res){
		getMapList();
		$("#labelModal").modal("hide");
	})
}