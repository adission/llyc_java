var table;

$(function(){
    showLeft();
    showHeader();
    var $wrapper = $('#div-table-container');
    var $table =$('#videoList');
    var paramJson = {
    		"token": getCookie('token'),
    		"keyword":""
    };
    table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
        	console.log(data);	
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: "../vidicon/selectVidicon",
                dataType: "json",
                success: function (result){
                	console.log(result);
                	callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    alert("获取数据失败");
                }
            })
        },

        "columns": [
            { "data": "vidicon_number" },
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "vidicon_number" },
            { "data": "vidicon_name" },
            { "data": "vidicon_type" },
            { "data": "whether_important" },
            { "data": null },
        ],
        "createdRow": function ( row, data, index ) {
            $('td', row).eq(-1).html(' <div class="btn-group"> ' +
                    '<button type="button" class="btn btn-default" title="编辑" onclick="openModal(\''+data.id+'\')"><i class="glyphicon glyphicon-file text-primary"></i></button> ' +
                    '<button type="button" class="btn btn-default" title="删除" onclick="del(\''+data.id+'\')"><i class="glyphicon glyphicon-remove text-danger"></i></button>' +
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
            arrItemName.push(item.name);
        });
        if(arrItemName.length!=0){
        	delTool(arrItemName.join(','),delPerson,arrItemId.join(","));
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
		delTool(data.name,delBlock,data.id);
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


// 点击展开详情
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
})

//打开新增和编辑modal
function openModal(id){
	$('#myModal').attr("data-id",id);
	$('#myModal').empty();
	$.ajaxSetup ({ cache: false });
	$('#myModal').load('html-js/video_manage/video_manage_modal.html',function(result) {
		if(id == '') {
			$(".modal-title").html("添加摄像头");
		} else {
			$(".modal-title").html("编辑摄像头信息");
		}
		$('#myModal').modal({
			show:true
		});
	});
}


// 删除接口
function del(id){
	if(confirm("确定要删除吗？")){
		var paramJson = {
				"id":id
			};
		$.post("../vidicon/deleteVidicon",{
			"paramJson": JSON.stringify(paramJson),
			"token": getCookie('token')
		},function(res){
			if(res.code == "200"){
				alert("删除成功！");
				table.ajax.reload(null,false);
			}
		});
	}
}




























