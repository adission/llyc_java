/**
 * Created by Administrator on 2017/9/13.
 */
var tableCall;
var paramJson; //定位卡
var cre_or_update = ""; //判断是新增定位卡还是修改定位卡
var card_id_g="";
var card_sn_g="";
var user_id_g="";
var card_id_d="";
var card_sn_d="";
var user_id_d="";
var user_id_j="";
var del_var = 0; //等于0时是单个删除，等于1时是批量删除
$(function(){
	$.fn.modal.Constructor.prototype.enforceFocus = function(){};
	var data = [{id:0,text:'enhancement'}];
	$("#user-sel").select2({
		data:data,
//		id:sss,
		placeholder:'请选择',
		width:"100%",
	});
	
    showHeader();
    var $wrapper = $('#div-table-container');
    var $table = $('#dynamic-table');
    var total=0;
    paramJson = {"token": "1213"};
    paramJson.fuzzy =$(".dsearch").val();
    tableCall = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
        	$wrapper.spinModal();
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: "../poscard/queryCard",
                dataType: "json",
                success: function (result){
                    setTimeout(function(){
                        //异常判断与处理
                        if (result.errorCode) {
                            $.dialog.alert("查询失败。错误码："+result.errorCode);
                            return;
                        }
                        //封装返回数据，这里仅演示了修改属性名
                        var returnData = {};
                        returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = result.total;
                        returnData.recordsFiltered = result.total;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.data;
                        //关闭遮罩
                        $wrapper.spinModal(false);
                        //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                        //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                        callback(returnData);
	                    },200);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
//                    $.dialog.alert("查询失败");
                    $wrapper.spinModal(false);
                }
            })
        },
        
        columns: [
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            {"data": null },
            {"data": "card_sn" },
            {"data": "name" },
            {"data": "worker_type" },
            {"data": null }
        ],
        
        "createdRow": function ( row, data, index ) {
            //行渲染回调,在这里可以对该行dom元素进行任何操作
            //给当前行加样式
            //if (data.status=="未处理") {
            //    $('td', row).eq(6).css("color","#ff0000");
            //}
			$('td', row).eq(0).children().eq(0).attr("data-id",data.id);
			$('td', row).eq(0).children().eq(0).attr("user-id","");
			$('td', row).eq(0).children().eq(0).attr("user-id",data.id1);
            $('td', row).eq(1).html(index+1);
            $('td', row).eq(-1).html('<a href="#myModal" data-toggle="modal" class="btn btn-info" card_id='+data.id+' user_id='+data.id1+' card_sn='+data.card_sn+'  onclick=xiugai(\"'+data.id+'\",\"'+data.card_sn+'\",\"'+data.id1+'\")>修改</a>' +
                                     '<a class="btn btn-danger" style="margin: 0 10px" card_id='+data.id+' user_id='+data.id1+' card_sn='+data.card_sn+' onclick=shanchuka(\"'+data.id+'\",\"'+data.card_sn+'\",\"'+data.id1+'\")>删除</a>' +
                                     '<a class="btn btn-warning" user_id='+data.id1+' onclick=jiebang(\"'+data.id1+'\")>解绑</a>');
            $('td', row).eq(1).addClass("text-center");
            //$('td', row).eq(8).html('<div class="contentOver" title="'+data.dealContent+'">'+data.dealContent+'</div>');

        },
        
        "drawCallback": function( settings ) {
			// 高亮显示当前行  
            $(settings.nTable).find("tbody tr").click(function(e) {  
                $(e.target).parents('table').find('tr').removeClass('warning');  
                $(e.target).parents('tr').addClass('warning');  
            });  
            
            
            //非全选按钮
            $(".iCheck").on("click", function () {
            	btn = true;
            	$(".iCheck").each(function(){
            		if(!$(this).prop('checked')){
            			$("#checkAll").prop('checked',false);
            			btn = false;
            		}
            	});
            	if(btn){
            		$("#checkAll").prop('checked',!$("#checkAll").prop('checked'));
            	}
            });
            
        },
        
        "columnDefs": [
            {orderable: false,
                targets: 0 }
        ]//第一列与第二列禁止排序
    })).api();
    
    
    //checkbox全选
    $("#checkAll").on("click", function () {
        if ($(this).prop("checked") === true) {
            $("input[name='checkList']").prop("checked", $(this).prop("checked"));
            $('#example tbody tr').addClass('selected');
        } else {
            $("input[name='checkList']").prop("checked", false);
            $('#example tbody tr').removeClass('selected');
        }
    });
});


//批量删除定位卡
$('#delCards').click(function(){
	del_var = 1; 
	sureTool("确定要删除定位卡吗","",queDing);
});


//新增定位与修改定位卡弹框确认按钮
//update-aditor:jiangyanyan
$('#addCard').click(function(){
    $("#myModal .modal-title").html("新增定位卡");
    $("#myModal").modal("show");
    cre_or_update = 1; //1新增定位卡 ,0修改定位卡
    $("#card-num").val("");
    getUserInfoNoPage(); //新增定位卡时获取人员
});


//新增定位卡时获取人员
//aditor:jiangyanyan
function getUserInfoNoPage(){
	var data = {"token":"123456","searchContent":""};
	$.ajax({
		 type:'post',
         cache: false,
         data: {"paramJson":JSON.stringify(data)},
         url:"../PosUser/getUserNoPage",
         dataType: "json",
         success: function (jsondata){
        	 if(jsondata.code==200){
        		 $("#user-sel").empty();
            	 for(var i=0;i<jsondata.data.length;i++){
            		 var obj = jsondata.data[i];
            		 $("#user-sel").append("<option name='"+obj.name+"' id='"+obj.id+"'>"+obj.name+obj.mobile+"</option>");
            	 }
        	 }
         },       
	});
}


//自定义查询（模糊查询、类型选择、状态选择）
//aditor:jiangyanyan
function selector(){
	paramJson = {
		"keyword": $("#dsearch").val(),
		"token": "123"//getCookie('token')
	};
	tableCall.ajax.reload(null,false);
};


//新增定位卡弹框的确定方法
//aditor:jiangyanyan
$('#addCardModal').click(function(){
	//等于1时是新增 ;等于0时是修改
	if(cre_or_update==1){
		//新增人员与卡的绑定
		var card_sn = $('#card-num').val(); //卡号
		var user_id = $("#user-sel option:selected").prop("id");
		var data = {"token":"123456","card_sn":card_sn,"user_id":user_id};
		$.ajax({
			 type:'post',
	         cache: false,
	         data: {"paramJson":JSON.stringify(data)},
	         url:"../poscard/cardUserBinding",
	         dataType: "json",
	         success: function (jsondata){
	        	 if(jsondata.code==200){
	        		 alertTool("成功");
	        		 location.reload();
	        	 }else{
	        		 alertTool(jsondata.message);
	        	 }
	         },    
		});
		
	}else{
		if(user_id_g =="null"){
			user_id_g="";
		}
		//选择的新人id
		var user_id_sele = $("#user-sel option:selected").prop("id");
		//修改人员与卡的绑定
		var data = {"token":"123456","card_id":card_id_g,"card_sn":card_sn_g,"user_id":user_id_sele,"user_id_g":user_id_g};
		$.ajax({
			 type:'post',
	         cache: false,
	         data: {"paramJson":JSON.stringify(data)},
	         url:"../poscard/userCardUpdate",
	         dataType: "json",
	         success: function (jsondata){
	        	 if(jsondata.code==200){
	        		 alertTool(jsondata.message);
	        		 location.reload();
	        	 }else{
	        		 alertTool(jsondata.message);
	        	 }
	         },    
		});
	}	
});


//批量制卡
//aditor:jiangyanyan
$("#makeCard").click(function(){
	var min_numb = $("#min-numb").val();
	var max_numb = $("#max-numb").val();
	var data = {"token":"123456","startNum":min_numb,"endNum":max_numb};
	if(min_numb>max_numb){
		alertTool("最小值不能大于最大值!");
	}else{
		$.ajax({
			 type:'post',
	         cache: false,
	         data: {"paramJson":JSON.stringify(data)},
	         url:"../poscard/batchFabrication",
	         dataType: "json",
	         success: function (jsondata){
	        	 if(jsondata.code==200){
	        		 location.reload();
	        	 }
	        	 alertTool(jsondata.message);
	         },       
		});
	}
});


//批量制卡时最大值的输入方法
//aditor:jiangyanyan
function maxInput(){
	var min_numb = $("#min-numb").val();
	var max_numb = $("#max-numb").val();
	$("#min-max-numb").val(max_numb-min_numb);
}


//修改按钮的点击事件
//aditor:jiangyanyan
function xiugai(card_id,card_sn,user_id){
	$("#myModal .modal-title").html("修改定位卡");
	card_id_g = card_id; //卡号id
	card_sn_g = card_sn; //卡号
	user_id_g = user_id; //人员id
	cre_or_update = 0;//判断是新增定位卡还是修改定位卡
	$("#card-num").val(card_sn);
	getUserInfoNoPage(); //新增定位卡时获取人员
}


//单个删除定位卡与人员绑定
//aditor:jiangyanyan
function shanchuka(card_id,card_sn,user_id){
	card_id_d = card_id; //卡号id
	card_sn_d = card_sn; //卡号
	user_id_d = user_id; //人员id
	del_var = 0; //批量删除
	console.log("单个删除"+del_var);
	sureTool("确定要删除定位卡吗","",queDing);
}


//删除定位卡的确定按钮
//aditor:jiangyanyan
function queDing(){
	console.log("确定按钮！！！");
	if(del_var==0){
		if(user_id_d =="null"){
			user_id_d="";
		}
		var data = {"token":"123456","user_id":user_id_d,"card_id":card_id_d};
		$.ajax({
			type:'post',
	        cache: false,
	        data: {"paramJson":JSON.stringify(data)},
	        url:"../poscard/cardUserUnbindDel",
	        dataType: "json",
	        success: function (jsondata){
	        	if(jsondata.code==200){
	        		location.reload();
	        	}
	        	alertTool(jsondata.message);
	        },       
		});
		
	}else{
		//批量删除定位卡
		/*获取定位卡id以字符串分隔*/
		var cards_ids = new Array();//定位卡ids
		var users_ids = new Array();//人员ids
		$(".iCheck:checked").each(function(i){
			cards_ids.push($(this).attr("data-id"));
			if($(this).hasClass("user-id")){
				users_ids.push($(this).attr("user-id"));
			}
		});
		if(cards_ids.length==0){
			alert("没有选择定位卡!");
			return;
		}
		var data = {"token":"123456","cards_ids":cards_ids,"users_ids":users_ids};
		$.ajax({
			 type:'post',
	        cache: false,
	        data: {"paramJson":JSON.stringify(data)},
	        url:"../poscard/delCardAndUnbind",
	        dataType: "json",
	        success: function (jsondata){
	        	if(jsondata.code==200){
	        		location.reload();
	        	}
	        	alertTool(jsondata.message);
	        },       
		});
	}
}


//人员与卡解绑
//aditor:jiangyanyan
function jiebang(user_id){
	user_id_j = user_id; //人员id	
	sureTool("你确定要解绑选中的定位卡吗","",queDingJieBang);
}


//人员与卡的解绑
//aditor:jiangyanyan
function queDingJieBang(){
	if(user_id_j=="null"){
		alertTool("此卡与人员没有绑定！");
	}else{
		var data = {"token":"123456","user_id":user_id_j};
		$.ajax({
			type:'post',
	        cache: false,
	        data: {"paramJson":JSON.stringify(data)},
	        url:"../poscard/cardUserUnbinding",
	        dataType: "json",
	        success: function (jsondata){
	        	if(jsondata.code==200){
	        		location.reload();
	        	}
	        	alertTool(jsondata.message);
	        },       
		});
	}
}





