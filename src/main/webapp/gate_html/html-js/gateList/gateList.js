/**
 * wangyanling by 2017/8/31
 * @returns
 */
var table;
$(function(){
    showLeft();
    showHeader();
    var $wrapper = $('#div-table-container');
    var $table =$('#table-detail');

    var paramJson = {
    		"gate_name": '',
    		"gate_no": '',
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
                url: urlpath+"GateList/list",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    $wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                	$('.alert-danger').html("闸机列表获取失败");
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
        "columns": [
            {"data": null },
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "type"},
            { "data": "gate_name"},
            { "data": "cross_flag" },
            { "data": "sn" },
            { "data": "gate_no" },
            { "data": "location" },
//            { "data": "type" },
            { "data": "group_id" },
            { "data": "is_use" },
            { "data": "is_kaoqin" },
            { "data": null },
        ],
        "createdRow": function ( row, data, index ) {
            //给当前行某列加样式
//            $('td', row).eq(2).html("<a href='gatePloe.html?id="+data.id+"' class='text-primary'>"+data.gate_name+"</a>");
            $('td', row).eq(-1).html(' <div class="btn-group"> ' +
            		'<button type="button" class="btn btn-default" title="编辑" onclick="openModal(\''+data.id+'\')"><i class="glyphicon glyphicon-file text-primary"></i></button> ' +
            		'<button type="button" class="btn btn-default openCard" onclick="location.href=\'gatePloe.html?id='+data.id+'\'" title="权限管理"><i class="glyphicon glyphicon-floppy-save text-primary"></i></button> ' +
                    '<button type="button" class="btn btn-default start" title="启动"><i class="glyphicon glyphicon-off text-primary"></i></button> ' +
                    '<button type="button" class="btn btn-default alOpen" title="常开"><i class="glyphicon glyphicon-ok-circle text-info"></i></button> ' +
                    '<button type="button" class="btn btn-default alClose" title="常闭"><i class="glyphicon glyphicon-ban-circle text-danger"></i></button>' +
                    '<button type="button" class="btn btn-default resetOpen" title="重启"><i class="glyphicon glyphicon-repeat text-success"></i></button>' +
                    '<button type="button" class="btn btn-default delRow" title="删除"><i class="glyphicon glyphicon-remove text-danger"></i></button>' +
                ' </div>	');
        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table).eq(0).click();
        },
    })).api();

    //  弹出框 隐藏后重绘表格
    $('#myModal').on('hide.bs.modal', function (e) {
    		table.ajax.reload(null, false);
	});
   
	// 自定义搜索
    $('#searchBtn').on('click',function(res){
    		paramJson = {
			"gate_name": $('#gatename').val(),
			"gate_no": $('#gateno').val(),
			"token": getCookie('token')
		};
	    	table.ajax.reload(null,false);
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
    
  //点击展开详情
    function format ( d ) {
        return `<form class="form-inline col-xs-10">
	                <div class="form-group col-xs-3 text-center">
	                    	设备型号: ${d.type}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	是否记录考勤: ${d.is_kaoqin?"否":"是"}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                   	方向: ${d.cross_falg?"进":"出"}
	                </div>
	                <div class="form-group  col-xs-3 text-center">
	                    	名称: ${d.gate_name}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	闸机编号: ${d.gate_no}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	控制器编号: ${d.sn}
	                </div>
	                <div class="form-group  col-xs-3 text-center">
	                    	闸机在控制器内编号: ${d.connect_id}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	安装位置: ${d.location}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	闸机分区: ${d.group_id}
	                </div>
	                <div class="form-group col-xs-3 text-center">
    					闸机状态: ${d.is_use=="0"?"常开":d.is_use=="1"?"正常":"常闭"}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	闸机Ip: ${d.connect_ip}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	闸机端口: ${d.connect_port}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	是否为主机: ${d.is_master?"是":"否"}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	密码: ${d.password}
	                </div>
	                <div class="form-group col-xs-3 text-center">
	                    	备注: ${d.common}
	                </div>
	            </form>`;
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
    var arrItemId = [];
    var arrItemName = [];
    function getId(){
    	arrItemId = [],arrItemName = [];
    	$("tbody :checkbox:checked",$table).each(function(i) {
            var item = table.row($(this).closest('tr')).data();
            arrItemId.push(item.id);
            arrItemName.push(item.gate_name);
        });
    }
    
    //批量删除选中行
    $("#delRow").click(function(){
    	getId();
        if(arrItemName.length!=0){
        	delTool(arrItemName.join(','),delGate,arrItemId.join(","));
        }else {
    		alertTool("请先选中删除行数");
        }
    });
    //常开
    $("#alOpen").click(function(){
    	getId();
        if(arrItemName.length!=0){
        	sureTool("确定常开闸机",arrItemName.join(','),alwaysOpen,arrItemId.join(","));
        }else {
    		alertTool("请先选中需要常开的闸机");
        }
    });
    //常闭
    $("#alClose").click(function(){
    	getId();
        if(arrItemName.length!=0){
        	sureTool("确定常闭闸机",arrItemName.join(','),alwaysClose,arrItemId.join(","));
        }else {
    		alertTool("请先选中需要常闭的闸机");
        }
    });
    //启动
    $("#start").click(function(){
    	getId();
        if(arrItemName.length!=0){
        	sureTool("确定启动闸机",arrItemName.join(','),startGate,arrItemId.join(","));
        }else {
    		alertTool("请先选中需要启动的闸机");
        }
    });
    //重启
    $("#resetOpen").click(function(){
    	getId();
        if(arrItemName.length!=0){
        	sureTool("确定重启闸机",arrItemName.join(','),resetGate,arrItemId.join(","));
        }else {
    		alertTool("请先选中需要重启的闸机");
        }
    });
    // 删除一个
    $('#table-detail tbody').on('click', '.delRow', function () {
		var data = table.row($(this).parents('tr')).data();
		var id  = $(this).attr('data-id');
		delTool(data.gate_name,delGate,data.id);
    }).on('click', '.alOpen', function () {  // 常开
		var data = table.row($(this).parents('tr')).data();
		sureTool("确定常开闸机",data.gate_name,alwaysOpen,data.id);
    }).on('click', '.alClose', function () {  // 常闭
		var data = table.row($(this).parents('tr')).data();
		sureTool("确定常闭闸机",data.gate_name,alwaysClose,data.id);
    }).on('click', '.resetOpen', function () {  // 重启
		var data = table.row($(this).parents('tr')).data();
		sureTool("确定重启闸机",data.gate_name,resetGate,data.id);
    }).on('click', '.start', function () {  // 启动
		var data = table.row($(this).parents('tr')).data();
		sureTool("确定启动闸机",data.gate_name,startGate,data.id);
    });
});





//打开新增和编辑modal
function openModal(id){
	$('#myModal').attr("data-id",id);
	$('#myModal').empty();
	$.ajaxSetup ({ cache: false });
	$('#myModal').load('html-js/gateList/gateModal.html',function(result) {
		if(id == '') {
			$("#myModal .modal-title").html("添加闸机"); 
		} else {
			$("#myModal .modal-title").html("编辑闸机信息");
		}
		$('#myModal').modal({
			show:true
		});
	});
}
//删除接口
function delGate(id){
	$.post(urlpath+'GateList/delete',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"闸机删除成功","闸机删除失败");
	});
}

// 重启闸机接口
function resetGate(id){
	$.post(urlpath+'GateList/restart',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"重启闸机成功","重启闸机失败");
	});
}
//启动闸机接口
function startGate(id){
	$.post(urlpath+'GateList/allnormal',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"闸机启动成功","闸机启动失败");
	});
}
// 常开接口
function alwaysOpen(id){
	$.post(urlpath+'GateList/allopen',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"设置常开闸机成功","设置常开闸机失败");
	});
}

// 常闭接口
function alwaysClose(id){
	$.post(urlpath+'GateList/allclose',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"设置常闭闸机成功","设置常闭闸机失败");
	});	
}

//导出数据
$('#exportData').click(function(){
	sureTool("确定导出闸机信息列表吗","",exData);
	
});
function exData(){
	window.open(urlpath+"GateList/listexcel?token="+getCookie("token")+"&paramJson=%7B%7D");
} 





