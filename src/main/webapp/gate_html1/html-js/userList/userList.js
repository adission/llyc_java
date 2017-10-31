/**
 * 
 */
$(function(){

    initDatable();

});

/**
 * 初始化datatable
 */
function initDatable() {
	var paramJson = {
	   "user_name": "",
	   "mobile": '',
	   "token": getCookie('token')
   };
    
    var $wrapper = $('#div-table-container');
    var $table =$('#userList');

    table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
    	ajax: function(data,callback,settings){
        	
        	data.paramJson = JSON.stringify(paramJson);
    		$wrapper.spinModal();   
	    		
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: urlpath+"User/list",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    $wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
//                    $('.alert-danger').html("人员列表获取失败");
//        			$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper.spinModal(false);
                }
            })
        },
        "columns": [
            {
                orderable: false,
                data: null,
                targets:0,
                "defaultContent": "<span  class='row-details row-details-close'></span>"
            },
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "user_name" },
            { "data": "mobile" },
            { "data": "create_time" },
            { "data": "last_login_time" },
            { "data": null },
        ], 
        "createdRow": function ( row, data, index ) {
             //行渲染回调,在这里可以对该行dom元素进行任何操作
             //给当前行加样式$(row) $('td', row).eq(2) 
         		$('td', row).eq(6).html(' <div class="btn-group"> ' +
                         '<button type="button" class="btn btn-default" title="编辑" onclick="openModal(\''+data.id+'\')"><i class="fa fa-file text-primary"></i></button>' +
                         '<button type="button" class="btn btn-default delRow" title="删除" data-id=\''+data.id+'\'><i class="fa fa-times text-danger"></i></button>' +
                         ' </div>	');
         },
         // 表格重绘完成后
         "drawCallback": function( settings ) {
         		$("tbody tr",$table).eq(0).click();
         }
    })).api();
    
    //checkbox全选
    $("#checkAll").on("click", function () {
        if ($(this).prop("checked") === true) {
            $("input[name='checkList']").prop("checked", $(this).prop("checked"));
            $('#userList tbody tr').addClass('selected');
        } else {
            $("input[name='checkList']").prop("checked", false);
            $('#userList tbody tr').removeClass('selected');
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
            arrItemName.push(item.user_name);
        });
        if(arrItemName.length!=0){
        	delTool(arrItemName.join(','),delUser,arrItemId.join(","));
        }else {
        	alertTool("请选择要删除行数");
        }
    });
    
//  展开详情内容
    function format ( d ) {
        // `d` 每行元素的详情 
    	var img = d.image.indexOf('http')!=-1?d.image:(urlpath+d.image);
        return `<div>
    				<div class="col-xs-2 text-center">
                     	<img src="${img}" style="width: 100px; margin-top: 10px">
	                </div>
        			<form class="form-inline col-xs-9 formDetail">
		                <div class="form-group col-xs-4 text-center">
		                    	用户名: ${d.user_name}
		                </div>
		                <div class="form-group col-xs-4 text-center">
		                   	手机号: ${d.mobile}
		                </div>
		                <div class="form-group  col-xs-4 text-center">
		                    	创建时间: ${d.create_time}
		                </div>
		                <div class="form-group col-xs-4 text-center">
		                    	最近登录: ${d.last_login_time}
		                </div>
		            </form>
        			</div>`;
    }
    // 点击展开详情
    $('.table').on('click', 'tbody tr td:first-child',function(){
        var tr = $(this).parent().closest('tr');
        var row = table.row( tr );
        if ( row.child.isShown() ) {  // 如果已经展开，关闭
            row.child.hide();
            tr.removeClass('shown');
            $(this).children().addClass("row-details-close").removeClass("row-details-open");
        }
        else {// 否则 打开详情
            row.child( format(row.data()) ).show();
            tr.addClass('shown');
            $(this).children().addClass("row-details-open").removeClass("row-details-close");
        }
    });
    
    $('#userList tbody').on('click', '.delRow', function () {
		var data = table.row($(this).parents('tr')).data();
		var id  = $(this).attr('data-id');
		delTool(data.user_name,delUser,id);
    });

	// 自定义搜索
	$('#searchBtn').on('click',function(res){
		paramJson = {
				"user_name": Trim($('#username').val()),
				"mobile": Trim($('#mobile').val()),
				"token": getCookie('token')
		};
    	table.ajax.reload(null,false);
	});   
}

//打开弹框
function openModal(id) {
	$('#myModal').attr("data-id",id);
	$('#myModal').empty();
	$('#myModal').load('html-js/userList/userModal.html',function(result) {
		if(id == '') {
			$(".modal-title").html("添加用户"); 
		} else {
			$(".modal-title").html("编辑用户信息");
		}
		$('#myModal').modal({
			show:true
		});
	});
}

// 删除用户
function delUser(id){
	$.post(urlpath+'User/deleteuser',{
		"id": id,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"用户删除成功",res.message);
	});
}

//导出数据
$('#exportData').click(function(){
	sureTool("确定导出用户信息列表吗","",exData);
	
});
function exData(){
	window.open(urlpath+"User/listExcel?token="+getCookie("token")+"&paramJson=%7B%7D");
}
