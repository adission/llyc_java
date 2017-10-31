/**
 * Created by wangyanling on 2017/10/17.
 */
var table;
$(function(){
	
	var nowTemp = new Date();
	var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
	
	var checkin = $('.dpd1').datepicker({
			format:    'yyyy-mm-dd',
		    onRender: function(date) {
		        return date.valueOf() < now.valueOf() ? 'disabled' : '';
		    }
		}).on('changeDate', function(ev) {
	        if (ev.date.valueOf() > checkout.date.valueOf()) {
	            var newDate = new Date(ev.date)
	            newDate.setDate(newDate.getDate() + 1);
	            checkout.setValue(newDate);
	        }
	        checkin.hide();
	        $('.dpd2')[0].focus();
	    }).data('datepicker');
	var checkout = $('.dpd2').datepicker({
			format:    'yyyy-mm-dd',
		    onRender: function(date) {
		        return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
		    }
		}).on('changeModa', function(ev) {
	        checkout.hide();
	    }).data('datepicker');
	
    getPersonType();
	getWorkerType();
	
    var $wrapper = $('#div-table-container');
    var $table =$('#personList');
    
    var paramJson = {
    		"name": '',
    		"mobile": '',
    		"pap_number":'',
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
                url: urlpath+"GateVisit/selectVisitList",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    $wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    $('.alert-danger').html("访客列表获取失败");
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
            { "data": "mobile" },
            { "data": "enter" },
            { "data": "leave" },
            { "data": "pap_number" },
            { "data": "visit_reason" },
            { "data": null },
        ],
        "createdRow": function ( row, data, index ) {
            //$('td', row).eq(-2).html("<button class='btn btn-primary' onclick='attendanceList(\""+data.id+"\")'>查看</button>");
            $('td', row).eq(-1).html(' <div class="btn-group"> ' +
                    '<button type="button" class="btn btn-default" title="编辑" onclick="openModal(\''+data.id+'\')"><i class="fa fa-file text-primary"></i></button> ' +
                    '<button type="button" class="btn btn-default openCard" onclick="openCard(\''+data.id+'\',\''+data.cid+'\')" title="权限管理"><i class="fa fa-upload text-primary"></i></button> ' +
                    '<button type="button" class="btn btn-default stopCard" title="停卡"><i class="fa fa-pause text-info"></i></button> ' +
                    '<button type="button" class="btn btn-default resetCard" title="复卡"><i class="fa fa-play text-success"></i></button>' +
                    '<button type="button" class="btn btn-default delCard" title="销卡"><i class="fa fa-scissors text-danger"></i></button>' +
                    '<button type="button" class="btn btn-default delRow" title="删除"><i class="fa fa-times text-danger"></i></button>' +
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
  //checkbox全选
    $("#checkAll").on("click", function () {
        if ($(this).prop("checked") === true) {
            $("input[name='checkList']").prop("checked", $(this).prop("checked"));
            $('#personList tbody tr').addClass('selected');
        } else {
            $("input[name='checkList']").prop("checked", false);
            $('#personList tbody tr').removeClass('selected');
        }
    });
    $table.on("change",":checkbox",function() {
        var checkbox = $("tbody :checkbox[name='checkList']",$table);
        $("#checkAll").prop('checked', checkbox.length == checkbox.filter(':checked').length);
    });
    var arrItemId = [];
    var arrItemName = [];
    var arrItemSate=[];
    function getId(){
    	arrItemId = [],arrItemName = [],arrItemSate=[];
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
        		if(arrItemSate[i]==1 || arrItemSate[i]==2){
        			sateCount++;
        		}
        	}
        	if(sateCount < arrItemSate.length){
        		alertTool("选择人员中有人未开卡、已销卡或已停卡，请重新选择");
        	}else {
        		sureTool("确定停卡",arrItemName.join(','),doCard,arrItemId.join(","),2);
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
        		console.log(arrItemSate[i]);
        		if(arrItemSate[i]==2){
        			sateCount++;
        		}
        	}
        	console.log(sateCount);
        	if(sateCount < arrItemSate.length){
        		alertTool("选择人员中有人未停卡，请重新选择");
        	}else {
        		sureTool("确定复卡",arrItemName.join(','),doCard,arrItemId.join(","),1);
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
        		if(arrItemSate[i]==1 || arrItemSate[i]==2){
        			sateCount++;
        		}
        	}
        	if(sateCount < arrItemSate.length){
        		alertTool("选择人员中有人已经销卡，或未开卡，请重新选择");
        	}else {
        		sureTool("确定销卡",arrItemName.join(','),doCard,arrItemId.join(","),3);
        	}
        }else {
        	alertTool("请选择要销卡的人员");
        }
    });
    
    // 删除一个
    $('#personList tbody').on('click', '.delRow', function () {
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
			"name": Trim($('#username').val()),
			"mobile": Trim($('#mobile').val()),
			"pap_number": Trim($('#pap_number').val()),
			/*"card_id": Trim($('#card_id').val()),*/
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
var $table1 =$('#attendanceLog');
// 点击查看按钮
function attendanceList(id){
	$('input[name="from"]').val("");
	$('input[name="to"]').val("");
	$('#myModal2').modal("show");
	
	$table1.dataTable().fnDestroy();
    var $wrapper2 = $('#div-table-container2');
	$("#myModal2").on("shown.bs.modal",function(){
		var paramJson1 = {
    		"token": getCookie('token'),
    		"start_time": '',
    		"end_time": "",
    		"user_id": id
    	};
	    var table1 = $table1.dataTable($.extend(true,{}, CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
	        ajax: function(data,callback,settings){
	        	$wrapper2.spinModal();   
	        	data.paramJson = JSON.stringify(paramJson1);
	            $.ajax({
	                type: "GET",
	                cache: false,
	                data: data,
	                url: urlpath+"GateCheckLog/getCheckLog",
	                dataType: "json",
	                success: function (result){
	                	$wrapper2.spinModal(false);   
	                    callback(result);
	                },
	                error: function(XMLHttpRequest, textStatus, errorThrown) {
                		$('.alert-danger').html("打卡记录列表获取失败");
                		$('.alert-danger').show(300).delay(1000).hide(300);
                		$wrapper2.spinModal(false);   
	                }
	            })
	        },
	        columns: [
	            {"data": "check_date"},
	            {"data": "gate_no"},
	            {"data": "cross_flag"},
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
	    		"start_time": $('input[name="from"]').val(),
	    		"end_time": $('input[name="to"]').val(),
	    		"user_id": id
	    	};
			table1.ajax.reload(null,false);
	    });
	});
    
    
    
}

//点击展开详情
function format ( d ) {
	var img1 = d.avatar_img.indexOf('http')!=-1?d.avatar_img:(urlpath+d.avatar_img);
    return `<div>
    			<div class="col-xs-2 text-center">
	                <img src="${img1}" style="width: 100px;margin-top: 10px">
	            </div>
	            <form class="form-inline col-xs-10 formDetail">
	                <div class="form-group col-xs-3 text-center">
	                    	姓名: ${d.name}
	                </div>
	                <div class="form-group  col-xs-3 text-center">
	                    	手机号: ${d.mobile}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	车辆类型: ${d.cartype}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                   	         车牌号: ${d.car_number}
	                </div>  
	                <div class="form-group col-xs-3 text-center">
	                    	证件类型: ${d.paptype}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	证件号码: ${d.pap_number}
	                </div>
	                <div class="form-group  col-xs-3 text-center">
	                    	登记时间: ${d.registration}
	                </div>
	                <div class="form-group  col-xs-3 text-center">
	                    	进入时间: ${d.enter}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	离开时间: ${d.leave}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	访问理由: ${d.visit_reason}
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
			$("#myModal .modal-title").html("添加人员"); 
		} else {
			$("#myModal .modal-title").html("编辑人员信息");
		}
		$('#myModal').modal({
			show:true
		});
	});
}

// 删除接口
function delPerson(id){
	$.post(urlpath+'GateVisit/deleteVisit',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		alert(JSON.stringify(res)+"删除完成了");
		tool(res.code==200,"人员删除成功",res.message);
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
			"use_times": Trim($("input[name='use_times']").val()),
			"end_time": Trim($("input[name='end_time']").val()),
			"face_auth": $('#type2').is(":checked")?1:0,
			"user_id": $('#myModal3').attr("data-id"),
			"gate_id": gateList.join(','),
			"id_card": $('#type3').is(":checked")?$('#myModal3').attr("data-cid"):0,
			"is_tmp": $('select[name="is_tmp"] option:selected').val(),
			"token": getCookie('token')
		},function(res){
			tool2(res.code==200,"myModal3","人员卡片权限设置成功",res.message);
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
