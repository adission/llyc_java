function fnFormatDetails ( aData, nTr )
{
    var sOut = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
    for(i in aData){
    	sOut += '<tr><td>屏名称:</td><td>'+aData[i]["screen_name"]+'</td></tr>';
    	sTd = '';
    	for( j in aData[i]["all_vidicon"]){
    		sTd += '<td>'+aData[i]["all_vidicon"][j]["vidicon_name"]+'</td>'
    	}
    	sOut += '<tr><td>摄像头名称:</td>'+sTd+'</tr>';
    }
    sOut += '</table>';

    return sOut;
}
    
var table;
$(function(){
	
    var $table =$('#hidden-table-info');
    var paramJson = {
    		"token": getCookie('token'),
    		"screen_type":0,//分屏模式（0所有，1,2,3,4）
    		"is_important":0//是否重要（0所有，1是，2否）
    };
    table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
        	console.log(data);	
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: "../videoGroup/getGroupListByCondition",
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
        	{ "data": null },
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,           
            { "data": "group_name" },
            { "data": "split_screen_type" },
            { "data": "whether_important" },
            { "data": "set_default" },
            { "data": "grouping_person" },
            { "data": "grouping_person" },
            { "data": null }
            
        ],
        "createdRow": function ( row, data, index ) {
        	$('td', row).eq(0).html('<img src="../public/images/details_open.png">');
            $('td', row).eq(-1).html(' <div class="btn-group"> ' +
                    '<a href="camera_group_edit.html?'+data.id+'"><button type="button" class="btn btn-default" title="编辑"><i class="fa fa-file"></i></button></a>' +
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
//            arrItemName.push(item.name);
            
        });
        if(arrItemId.length!=0){
        	var paramJson = {
    				"group_ids":arrItemId
    			};
        	console.log(paramJson);
    		$.post("../groupScreenVidicon/deleteGroupScreenVidicon",{
    			"paramJson": JSON.stringify(paramJson),
    			"token": getCookie('token')
    		},function(res){
    			console.log(res);
    			if(res.code == "200"){
    				alert("删除成功！");
    				table.ajax.reload(null,false);
    			}
    		});
        	
        }else {
        	alert("请勾选要删除的分组");
        }
    });
    //  弹出框 隐藏后重绘表格
    $('#myModal').on('hide.bs.modal', function (e) {
		table.ajax.reload(null, false);
	});
	// 自定义搜索
    $('#searchBtn').on('click',function(res){
    	var is_important = $("#isImportant option:selected").val();
    	var screen_type	 = $("#screenType option:selected").val();
        paramJson = {
        		"token": getCookie('token'),
        		"screen_type":screen_type,//分屏模式（0所有，1,2,3,4）
        		"is_important":is_important//是否重要（0所有，1是，2否）
        };
    	table.ajax.reload(null,false);
    });
    
    
   
    //点击展开详情
    $(document).on('click','#hidden-table-info tbody td img',function () {
        var nTr = $(this).parents('tr')[0];
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
            var id = $table.fnGetData( nTr )["id"];
            var data = {
            		"token": getCookie('token'),
            		"group_id":id,
            };
            $.post('../groupScreenVidicon/getGroupScreenVidiconByGroupId',{
            	"paramJson": JSON.stringify(data)
            }, function (ret) {
            		console.log(ret);
            		aData = ret.data.all_screen;
            		$table.fnOpen( nTr, fnFormatDetails(aData, nTr), 'details' );
            })            
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
	if(confirm("确定要删除吗？")){
		var paramJson = {
				"group_id":id
			};
		$.post("../groupScreenVidicon/deleteGroupScreenVidiconByGroupId",{
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
