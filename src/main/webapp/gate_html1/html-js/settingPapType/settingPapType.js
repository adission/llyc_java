
/**
 * Created by wangyanling on 2017/8/30.
 */
var table;
$(function(){
    var $wrapper = $('#div-table-container');
    var $table =$('#personType');

    var paramJson = {
    		"class_name": '',
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
                url: urlpath+"PapTypes/queryPap",
                dataType: "json",
                success: function (result){
	                //关闭遮罩
	                $wrapper.spinModal(false);
	                callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                		$('.alert-danger').html("人员类别列表获取失败");
                		$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper.spinModal(false);
                }
            });
        },
        columns: [
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "name" },
            { "data": null },
        ],
        "createdRow": function ( row, data, index ) {
            //行渲染回调,在这里可以对该行dom元素进行任何操作
            //给当前行加样式
            if (data.role) {
                $(row).addClass("info");
            }
            $('td', row).eq(2).html(' <div class="btn-group"> ' +
                    '<button type="button" class="btn btn-default" title="编辑" onclick="openModal(\''+data.id+'\')"><i class="fa fa-file text-primary"></i></button>' +
                    '<button type="button" class="btn btn-default delRow" title="删除"><i class="fa fa-times text-danger"></i></button>' +
                    ' </div>	');
        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table).eq(0).click();
            console.log(1111);
        },
    })).api();

    //checkbox全选
    $("#checkAll").on("click", function () {
        if ($(this).prop("checked") === true) {
            $("input[name='checkList']").prop("checked", $(this).prop("checked"));
            $('#personType tbody tr').addClass('selected');
        } else {
            $("input[name='checkList']").prop("checked", false);
            $('#personType tbody tr').removeClass('selected');
        }
    });
    $table.on("change",":checkbox",function() {
        var checkbox = $("tbody :checkbox[name='checkList']",$table);
        $("#checkAll").prop('checked', checkbox.length == checkbox.filter(':checked').length);
    });
    //批量删除选中行
    $("#delRow").click(function(){
        var arrItemId = [];
        var arrItemName = [];
        $("tbody :checkbox:checked",$table).each(function(i) {
            var item = table.row($(this).closest('tr')).data();
            arrItemId.push(item.id);
            arrItemName.push(item.name);
        });
        if(arrItemName.length!=0){
        	delTool(arrItemName.join(','),delPerson,arrItemId.join(","));
        }else {
        	alertTool("请选择要删除行数");
        }
    });
    // 删除一个
    $('#personType tbody').on('click', '.delRow', function () {
		var data = table.row($(this).parents('tr')).data();
		var id  = $(this).attr('data-id');
		delTool(data.name,delPerson,data.id);
    });
	// 自定义搜索
    $('#searchBtn').on('click',function(res){
    		paramJson = {
			"class_name": Trim($('#classname').val()),
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
	$('#myModal').load('html-js/settingPersonType/personModal.html',function(result) {
		if(id == '') {
			$(".modal-title").html("添加人员类别"); 
		} else {
			$(".modal-title").html("编辑人员类别信息");
		}
		$('#myModal').modal({
			show:true
		});
	});
}

// 删除接口
function delPerson(id){
	$.post(urlpath+'GateUserClass/deleteUserClass',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"人员类别删除成功",res.message);
	});
}