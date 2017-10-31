
/**
 * Created by wangyanling on 2017/8/30.
 */
var table;
$(function(){
    showLeft();
    showHeader();
    var $wrapper = $('#div-table-container');
    var $table =$('#table-detail');

    var paramJson = {
    		"group_name": '',
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
                url: urlpath+"Gategroup/list",
                dataType: "json",
                success: function (result){
	                //关闭遮罩
	                $wrapper.spinModal(false);
	                callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                		$('.alert-danger').html("闸机分区列表获取失败");
                		$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper.spinModal(false);
                }
            })
        },
        "columns": [
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "welcome" },
            { "data": "group_name" },
            { "data": null },
        ],
        "createdRow": function ( row, data, index ) {
            //行渲染回调,在这里可以对该行dom元素进行任何操作
            //给当前行加样式
            if (data.role) {
                $(row).addClass("info");
            }
            $('td', row).eq(3).html(' <div class="btn-group"> ' +
                    '<button type="button" class="btn btn-default" title="编辑" onclick="openModal(\''+data.id+'\')"><i class="glyphicon glyphicon-file text-primary"></i></button>' +
                    '<button type="button" class="btn btn-default delRow" title="删除"><i class="glyphicon glyphicon-remove text-danger"></i></button>' +
                    ' </div>	');
        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table).eq(0).click();
        },
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

    //批量删除选中行
    $("#delRow").click(function(){
        var arrItemId = [];
        var arrItemName = [];
        $("tbody :checkbox:checked",$table).each(function(i) {
            var item = table.row($(this).closest('tr')).data();
            arrItemId.push(item.id);
            arrItemName.push(item.group_name);
        });
        if(arrItemName.length!=0){
        	delTool(arrItemName.join(','),delBlock,arrItemId.join(","));
        }else {
        	alertTool("请选择要删除行数");
        }
    });
    //  弹出框 隐藏后重绘表格
    $('#myModal').on('hide.bs.modal', function (e) {
    		table.ajax.reload(null, false);
	});
    // 删除一个
    $('#table-detail tbody').on('click', '.delRow', function () {
		var data = table.row($(this).parents('tr')).data();
		var id  = $(this).attr('data-id');
		delTool(data.group_name,delBlock,data.id);
    });
	// 自定义搜索
    $('#searchBtn').on('click',function(res){
    		paramJson = {
			"group_name": $('#groupname').val(),
			"token": getCookie('token')
		};
	    	table.ajax.reload(null,false);
    });   
		
});

// 打开新增和编辑modal
function openModal(id){
	$('#myModal').attr("data-id",id);
	$('#myModal').empty();
	$.ajaxSetup ({ cache: false });
	$('#myModal').load('html-js/settingBlock/blockModal.html',function(result) {
		if(id == '') {
			$(".modal-title").html("添加闸机分区"); 
		} else {
			$(".modal-title").html("编辑闸机分区信息");
		}
		$('#myModal').modal({
			show:true
		});
	});
}

// 删除接口
function delBlock(id){
	$.post(urlpath+'Gategroup/delete',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"闸机分区删除成功","闸机分区删除失败");
	});
}
