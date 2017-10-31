/**
 * Created by wangyanling on 2017/8/30.
 */
var table;
$(function(){
    showLeft();
    showHeader();
    
    getPersonType();
	getWorkerType();
	
    var $wrapper = $('#div-table-container');
    var $table =$('#table-detail');
    
    var paramJson = {
    		"name": '',
    		"mobile": '',
    		"card_id": '',
    		"cid": '',
    		"token": getCookie('token')
    };
    
    table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	
        	data.paramJson = JSON.stringify(paramJson);
    		$wrapper.spinModal();   
	    		
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: urlpath+"GateUser/queryUserslist",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    $wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    $('.alert-danger').html("人员列表获取失败");
            			$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper.spinModal(false);
                }
            })
        },
        "columnDefs": [
            {
                orderable: false,
                data: null,
                targets:0,
                "defaultContent": "<span  class='row-details row-details-close'></span>"
            },
        ],//第一列与第二列禁止排序
        columns: [
            { data: null },
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "gonghao" },
            { "data": "name" },
            { "data": "class_name" },
            { "data": "worker_type" },
            { "data": "team" },
            { "data": "sate" },
            { "data": null },
            { "data": null },
        ],
        "createdRow": function ( row, data, index ) {
            $('td', row).eq(-2).html("<button class='btn btn-primary' onclick='attendanceList(\""+data.id+"\")'>查看</button>");
            $('td', row).eq(-1).html(' <div class="btn-group"> ' +
                    '<button type="button" class="btn btn-default" title="编辑" onclick="openModal(\''+data.id+'\')"><i class="glyphicon glyphicon-file text-primary"></i></button> ' +
                    '<button type="button" class="btn btn-default openCard" onclick="openCard(\''+data.id+'\',\''+data.cid+'\')" title="权限管理"><i class="glyphicon glyphicon-floppy-save text-primary"></i></button> ' +
                    '<button type="button" class="btn btn-default stopCard" title="停卡"><i class="glyphicon glyphicon-pause text-info"></i></button> ' +
                    '<button type="button" class="btn btn-default resetCard" title="复卡"><i class="glyphicon glyphicon-play text-success"></i></button>' +
                    '<button type="button" class="btn btn-default delCard" title="销卡"><i class="glyphicon glyphicon-floppy-remove text-danger"></i></button>' +
                    '<button type="button" class="btn btn-default delRow" title="删除"><i class="glyphicon glyphicon-remove text-danger"></i></button>' +
                    ' </div>	');
            
            // 复卡按钮
            if(data.sate_value==2){
            	$('td', row).find("button.resetCard").attr("disabled",false);  // 可用
            }else {
            	$('td', row).find("button.resetCard").attr("disabled",true);//不可用
            }
            
            // 停卡卡按钮
            if(data.sate_value==1){
            	$('td', row).find("button.stopCard").attr("disabled",false);  // 可用
            }else {
            	$('td', row).find("button.stopCard").attr("disabled",true);//不可用
            }
            
            // 销卡按钮
            if(data.sate_value==1 || data.sate_value==2){
            	$('td', row).find("button.delCard").attr("disabled",false);  // 可用
            }else {
            	$('td', row).find("button.delCard").attr("disabled",true);//不可用
            }
        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table).eq(0).click();
        },

    })).api();

    //  弹出框 隐藏后重绘表格
    $('#myModal').on('hide.bs.modal', function (e) {
    		table.ajax.reload(null, false);
	});
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
    var arrItemId = [];
    var arrItemName = [];
    var arrItemSate=[];
    function getId(){
    	arrItemId = [],arrItemName = [];
    	$("tbody :checkbox:checked",$table).each(function(i) {
            var item = table.row($(this).closest('tr')).data();
            arrItemId.push(item.id);
            arrItemName.push(item.name);
            arrItemSate.push(item.sate_value);
        });
    }

    //批量删除选中行
    $("#delRow").click(function(){
    	getId();
        if(arrItemName.length!=0){
        	delTool(arrItemName.join(','),delPerson,arrItemId.join(","));
        }else {
        	alertTool("请选择要删除人员");
        }
    });
    
    //批量停卡
    $("#stopCard").click(function(){
    	getId();
    	if(arrItemName.length!=0){
        	var sateCount=0;
        	for(var i=0;i<arrItemSate.length;i++){
        		if(arrItemSate[i].sate_value!=1 ){
        			sateCount++;
        		}
        	}
        	if(sateCount>0){
        		alertTool("选择人员中有人未开卡、已销卡或已停卡，请重新选择");
        	}else {
        		delTool(arrItemName.join(','),doCard,arrItemId.join(","),2);
        	}
        }else {
        	alertTool("请选择要停卡的人员");
        }
    });
    //批量复卡
    $("#resetCard").click(function(){
    	getId();
    	if(arrItemName.length!=0){
        	var sateCount=0;
        	for(var i=0;i<arrItemSate.length;i++){
        		if(arrItemSate[i].sate_value!=2){
        			sateCount++;
        		}
        	}
        	if(sateCount>0){
        		alertTool("选择人员中有人未停卡，请重新选择");
        	}else {
        		delTool(arrItemName.join(','),doCard,arrItemId.join(","),1);
        	}
        }else {
        	alertTool("请选择要复卡的人员");
        }
    });
    //批量销卡
    $("#delCard").click(function(){
    	getId();
        if(arrItemName.length!=0){
        	var sateCount=0;
        	for(var i=0;i<arrItemSate.length;i++){
        		if(arrItemSate[i].sate_value!=1 || arrItemSate[i].sate_value!=2){
        			sateCount++;
        		}
        	}
        	if(sateCount>0){
        		alertTool("选择人员中有人已经销卡，或未开卡，请重新选择");
        	}else {
        		delTool(arrItemName.join(','),doCard,arrItemId.join(","),3);
        	}
        }else {
        	alertTool("请选择要销卡的人员");
        }
    });
    
    
    
    
    
    //  弹出框 隐藏后重绘表格
    $('#myModal').on('hide.bs.modal', function (e) {
		table.ajax.reload(null, false);
	});
    
    // 删除一个
    $('#table-detail tbody').on('click', '.delRow', function () {
		var data = table.row($(this).parents('tr')).data();
		delTool(data.name,delPerson,data.id);
    }).on('click', '.stopCard', function () {  // 停卡
		var data = table.row($(this).parents('tr')).data();
		sureTool("确定停卡",data.name,doCard,data.id,2);
    }).on('click', '.resetCard', function () {  // 复卡
		var data = table.row($(this).parents('tr')).data();
		sureTool("确定复卡",data.name,doCard,data.id,1);
    }).on('click', '.delCard', function () {  // 销卡 3
		var data = table.row($(this).parents('tr')).data();
		sureTool("确定销卡",data.name,doCard,data.id,3);
    });
    
	// 自定义搜索
    $('#searchBtn').on('click',function(res){
    		paramJson = {
			"name": $('#username').val(),
			"mobile": $('#mobile').val(),
			"cid": $('#id_card').val(),
			"card_id": $('#cid').val(),
			"token": getCookie('token')
		};
    	table.ajax.reload(null,false);
    });
});

//获取工种列表
function getWorkerType(){
	$.post(urlpath+"WorkersTypes/queryAll",{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			$('select[name="worker_type"]').html("");
			for(var i=0;i<res.data.length;i++){
				$('select[name="worker_type"]').append('<option value="'+res.data[i].value+'">'+res.data[i].name+'</option>');
			}
		}
	});
}
// 获取人员类别
function getPersonType(){
	$.post(urlpath+"GateUserClass/queryUserClassAll",{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			$('select[name="worker_type"]').html("");
			for(var i=0;i<res.data.length;i++){
				$('select[name="person_type"]').append('<option value="'+res.data[i].id+'">'+res.data[i].name+'</option>');
			}
		}
	});
}



//人员暂停卡接口
function doCard(id,status){
	$.post(urlpath+'GateUser/Setusercardsstatus',{
		"user_ids": id,
		"status": status, //0 停卡 1恢复卡 2销卡 
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,res.message,res.message);
	});
}

// 编辑人员权限
function openCard(id,cid){
	getGate();
	$("#myModal3").modal("show");
	$("#myModal3").attr("data-id",id);
	$("#myModal3").attr("data-cid",cid);
	
	//人员已开卡信息 
	$.post(urlpath+"UserCards/querybyuser_id",{
		"token": getCookie('token'),
		"user_id": id
	},function(res){
		if(res.code==200){
			$('#type1').prop("checked",res.ic=="0"?false:true);
			$('#type2').prop("checked",res.face=="0"?false:true);
			$('#type3').prop("checked",res.id=="0"?false:true);
			$("select[name='is_tmp'] option[value='"+res.is_tmp+"']").prop('selected',true);
			$("input[name='end_time']").val(res.end_time);
			$("input[name='ic_card']").val(res.ic_card);
			$("input[name='use_times']").val(res.use_times);
			for(var i=0;i<res.gate_list.length;i++){
				$("select[name='gate_id'] option[value='"+res.gate_list[i].id+"']").prop('selected',true);
			}
		}
	});
	
	
	
}

// 点击查看按钮
function attendanceList(id){
	
    var $table1 =$('#attendanceLog');
    var table1 = $table1.dataTable($.extend(true,{}, CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	var paramJson1 = {
        		"token": getCookie('token'),
        		"start_time": '',
        		"end_time": "",
        		"user_id": id
        	};
        	data.paramJson = JSON.stringify(paramJson1);
            $.ajax({
                type: "GET",
                cache: false,
                data: data,
                url: urlpath+"GateCheckLog/getCheckLog",
                dataType: "json",
                success: function (result){
                    callback(result);
                    $('#myModal2').modal("show");
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                		$('.alert-danger').html("打卡记录列表获取失败");
                		$('.alert-danger').show(300).delay(1000).hide(300);
                }
            })
        },
        columns: [
            {
                "data": "check_date",
                width: 150
            },
            {data: "gate_no"},
            {data: "cross_flag"},
            {"data": "team"},
            {"data": "worker_type"}
        ],
        "createdRow": function ( row, data, index ) {
        },
        "drawCallback": function( settings ) {
        },
    })).api();
    
    // 自定义搜索
    $('.search-gate1').on('click',function(res){
		paramJson1 = {
    		"token": getCookie('token'),
    		"start_time": $('input[name="start_time"]').val(),
    		"end_time": $('input[name="start_time"]').val(),
    		"user_id": item.id
    	};
		table1.ajax.reload(null,false);
    });
    
}

//点击展开详情
function format ( d ) {
	var img = d.avatar_img.indexOf('http')!=-1?d.avatar_img:(urlpath+d.avatar_img);
    return `<div>
    			<div class="col-xs-2 text-center">
	                <img src="${img}" style="width: 100px;margin-top: 25px">
	            </div>
	            <form class="form-inline col-xs-10">
	                <div class="form-group col-xs-3 text-center">
	                    	姓名: ${d.name}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	年龄: ${d.age}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                   	性别: ${d.gender}
	                </div>
	                <div class="form-group  col-xs-3 text-center">
	                    	手机号: ${d.mobile}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	类别: ${d.class_name}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	状态: 生活区
	                </div>
	                <div class="form-group  col-xs-3 text-center">
	                    	身份证号码: ${d.cid}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	工种: ${d.worker_type}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	班组: ${d.team}
	                </div>
	                <div class="form-group col-xs-3 text-center">
    					惩罚次数: ${d.punish_record}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	卡类别: IC卡
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	备注: 无
	                </div>
	            </form>
	        </div>`;
}
$('.table').on('click', 'tbody tr td:first-child',function(){
    var tr = $(this).parent().closest('tr');
    var row = table.row( tr );
    if ( row.child.isShown() ) {
        row.child.hide();
        tr.removeClass('shown');
        $(this).children().addClass("row-details-close").removeClass("row-details-open");
    }
    else {
        row.child( format(row.data()) ).show();
        tr.addClass('shown');
        $(this).children().addClass("row-details-open").removeClass("row-details-close");
    }
});

//打开新增和编辑modal
function openModal(id){
	$('#myModal').attr("data-id",id);
	$('#myModal').empty();
	$.ajaxSetup ({ cache: false });
	$('#myModal').load('html-js/visitList/visitModal.html',function(result) {
		if(id == '') {
			$("#myModal .modal-title").html("添加访客"); 
		} else {
			$("#myModal .modal-title").html("编辑访客信息");
		}
		$('#myModal').modal({
			show:true
		});
	});
}

// 删除接口
function delPerson(id){
	$.post(urlpath+'GateUser/deleteGateuser',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"人员删除成功","人员删除失败");
	});
}

// 获取所有闸机列表
function getGate(){
	$.post(urlpath+'GateList/getAllGate',{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			$('select[name="gate_id"]').html("");
			for(var i=0;i<res.data.length;i++){
				$('select[name="gate_id"]').append('<option value="'+res.data[i].id+'">'+res.data[i].gate_name+'</option>');
			}
		}
	});
}

// 确定开卡
function addPloe(){
	// 判断是否有选择任意一个类型
	if(!$('input:checkbox').is(":checked")){
		// 提示选择卡类型
		alertTool("请先选择要开卡的类型");
	}else if($('#type1').is(":checked")  && $('input[name="ic_card"]').val().length==0 ){//如果选择了ＩＣ卡　　卡号必填
		alertTool("请输入IC卡卡号");
	}else {
		var gateList = [];
		$("select[name='gate_id'] :selected").each(function(){
			gateList.push($(this).val()); 
		 });
		$.post(urlpath+"UserCards/add",{
			"ic_card": $('#type1').is(":checked")?$("input[name='ic_card']").val():0,
			"use_times": $("input[name='use_times']").val(),
			"end_time": $("input[name='end_time']").val(),
			"face_auth": $('#type2').is(":checked")?1:0,
			"user_id": $('#myModal3').attr("data-id"),
			"gate_id": gateList.join(','),
			"id_card": $('#type3').is(":checked")?$('#myModal3').attr("data-cid"):0,
			"is_tmp": $('select[name="is_tmp"] option:selected').val(),
			"token": getCookie('token')
		},function(res){
			tool2(res.code==200,"myModal3","人员卡片权限设置成功","人员卡片权限设置失败");
		});
	}

}

// 导出数据
$('#exportData').click(function(){
	sureTool("确定导出人员信息列表吗","",exData);
	
});
function exData(){
	window.open(urlpath+"GateUser/queryUserexcel?token="+getCookie("token")+"&paramJson=%7B%7D");
} 



























