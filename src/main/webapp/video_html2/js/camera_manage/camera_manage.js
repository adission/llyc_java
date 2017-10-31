function fnFormatDetails ( oTable, nTr )
{
    var aData = oTable.fnGetData( nTr );
    console.log(aData);
    var sOut = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
    sOut += '<tr><td>摄像头id:</td><td>'+aData["id"]+' '+aData["vidicon_number"]+'</td></tr>';
    sOut += '<tr><td>摄像头名称:</td><td>'+aData["vidicon_name"]+'</td></tr>';
    sOut += '<tr><td>摄像头描述:</td><td>'+aData["vidicon_desc"]+'</td></tr>';
    sOut += '</table>';

    return sOut;
}
    

var table;
$(function(){
	
    var $table =$('#hidden-table-info');
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
        	$('td', row).eq(0).html('<img src="../public/images/details_open.png">');
            $('td', row).eq(-1).html(' <div class="btn-group"> ' +
                    '<button type="button" class="btn btn-default" title="编辑" onclick="openModal(\''+data.id+'\')"><i class="fa fa-file"></i></button> ' +
                    '<button type="button" class="btn btn-default" title="删除" onclick="del(\''+data.id+'\')"><i class="fa fa-times"></i></button>' +
                    ' </div>	');
        },
        "drawCallback": function( settings ) {
//            $("tbody tr",$table).eq(0).click();
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
            arrItemName.push(item.vidicon_number);
        });
        if(arrItemName.length!=0){
        	//alert(arrItemId.join(","));
        	alert("ssssss");
        	var paramJson = {
    				"id":arrItemId,
    				"token": getCookie('token')
    			};
        	$.post("../vidicon/deleteVidicon",{
    			"paramJson": JSON.stringify(paramJson),
    			
    		},function(res){
    			if(res.code == "200"){
    				alert("删除成功！");
    				table.ajax.reload(null,false);
    			}
    		});
        }else {
        	alert("请选择要删除行数");
        }
    });

	// 自定义搜索
    $('#searchBtn').on('click',function(res){
    		paramJson = {
    		"keyword":$("#number").val(),
			"token": getCookie('token')
		};
    	table.ajax.reload(null,false);
    });
    
    
   
    //点击展开详情
    $(document).on('click','#hidden-table-info tbody td img',function () {
        var nTr = $(this).parents('tr')[0];
        console.log(1111111);
        console.log(nTr);
        if ( $table.fnIsOpen(nTr) )
        {
            /* This row is already open - close it */
            this.src = "../public/images/details_open.png";
            $table.fnClose( nTr );
        }
        else
        {
            /* Open this row */
            this.src = "../public/images/details_close.png";
            $table.fnOpen( nTr, fnFormatDetails($table, nTr), 'details' );
        }
    } );
    
});



//打开新增和编辑modal
function openModal(id){
	$('#myModal').attr("data-id",id);
	$('#myModal').empty();
	$('#myModal').load('js/camera_manage/camera_manage_modal.html',function(result) {
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
	var ids = [];
	ids.push(id);
	if(confirm("确定要删除吗？")){
		var paramJson = {
				"id":ids
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










