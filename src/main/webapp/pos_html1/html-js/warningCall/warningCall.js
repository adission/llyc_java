/**
 * Created by Administrator on 2017/9/13.
 */
var tableCall;
var paramJson;
var warningId;
$(function(){
    showHeader();
	setTimeout('clearSocket()',500);
    var $table = $('#dynamic-table');
    var total=0;
    paramJson = {"token":"123"};
    tableCall = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
        	$('#dynamic-table').spinModal();
            $.ajax({
                type: "GET",
                cache: false,
                data: data,
                url: "../alert/list",
                dataType: "json",
                success: function (result){
                    console.log(result);
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
//                        关闭遮罩
                        $('#dynamic-table').spinModal(false);
                        //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                        //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                        callback(returnData);
                    },200);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    $.dialog.alert("查询失败");
                    $('#dynamic-table').spinModal(false); 
                }
            })

        },
        columns: [
            { "data": null },
            { "data": "card" },
            { "data": "userName" },
            { "data": "alertType" },
            { "data": null },
            { "data": "layerName" },
            { "data": "status" },
            { "data": "processName" },
            { "data": "processMode","width": 150 },
            { "data": null },
            {"data": null }
        ],
        "createdRow": function ( row, data, index ) {
            //行渲染回调,在这里可以对该行dom元素进行任何操作
            //给当前行加样式

            if (data.status=="未处理") {
                $('td', row).eq(6).css("color","#ff0000");
            }
			var datestr = format(data.time, 'yyyy-MM-dd HH:mm');
			$('td', row).eq(4).html(datestr);
			var section_layer = data.sectionName || "";
			if(data.layerName){
				section_layer += "/"+data.layerName;
			}
			$('td', row).eq(5).html(section_layer);
            $('td', row).eq(0).html(index+1);
            $('td', row).eq(-1).html('<button data-toggle="modal" class="btn btn-info" onclick="openModal(\''+data.id+'\')">处理</button>');
            $('td', row).eq(0).addClass("text-center");
            var processMode=data.processMode?data.processMode:"";
            $('td', row).eq(8).html('<div class="contentOver" title="'+processMode+'">'+processMode || +'</div>');
            var processTime=data.processTime?format(data.processTime, 'yyyy-MM-dd HH:mm'):"";
			$('td', row).eq(9).html(processTime);
            
        },
        "drawCallback": function( settings ) {
			
        	// 高亮显示当前行  
            $(settings.nTable).find("tbody tr").hover(function(e) {  
                $(e.target).parents('table').find('tr').removeClass('warning');  
                $(e.target).parents('tr').addClass('warning');  
            });  
        },
        "columnDefs": [
            {orderable: false,
                targets: 0 }
        ]//第一列与第二列禁止排序


    })).api();
//    $(".edit-button").on("click",function(){
//    	alert(123);
//    		$('#myModal').empty();
//    		$.ajaxSetup ({ cache: false });
//    		$('#myModal').load('html-js/warningCall/warningModal.html',function(result) {
//    			
//    			$('#myModal').modal({
//    				show:true
//    			});
//    		});
//
//    })
});
	//打开处理模态框
	function openModal(id){
		$("#myModal").modal("show");
		warningId = id;
	}
	//socket推送清空
	function clearSocket(){
		localStorage.removeItem('warningCount');
		 $("#warnCount").text("");
	}
	//模态框确定
	function sureMethod(){
		var editJson = {"id":warningId,"processMode":$("#processMode").val(),"token":"123"};
		$.post("../alert/update",{"paramJson":JSON.stringify(editJson)},function(res){
			$("#myModal").modal("hide");
			tableCall.ajax.reload(null,false);
		})
	}
	// 自定义查询（模糊查询、类型选择、状态选择）
    function selector(type){
    	if(!type){
    		if($('#typeselector .active').val()){
    			type = $('#typeselector .active').val();
    		}else{
    			type = "";
    		} 
    	}
		paramJson = {
			"keyword": $("#keyword").val(),
			"status": $('#statusselector').val(),
			"type": type,
			"token": "123"//getCookie('token')
		};
    	tableCall.ajax.reload(null,false);
    };
	
	function format(time, format){
	    var t = new Date(time);
	    var tf = function(i){return (i < 10 ? '0' : '') + i};
	    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
	        switch(a){
	            case 'yyyy':
	                return tf(t.getFullYear());
	                break;
	            case 'MM':
	                return tf(t.getMonth() + 1);
	                break;
	            case 'mm':
	                return tf(t.getMinutes());
	                break;
	            case 'dd':
	                return tf(t.getDate());
	                break;
	            case 'HH':
	                return tf(t.getHours());
	                break;
	            case 'ss':
	                return tf(t.getSeconds());
	                break;
	        }
	    })
	}
