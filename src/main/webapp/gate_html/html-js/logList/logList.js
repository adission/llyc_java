/**
 * Created by wangyanling on 2017/9/5.
 */
var table;
$(function(){
    showLeft();
    showHeader();
    
    var $wrapper = $('#div-table-container');
    var $table =$('#table-detail');
    var paramJson = {
    		"name": '',
    		"start_time": '',
    		"end_time": '',
    		"token": getCookie('token')
    };

    table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
    		$wrapper.spinModal();   
            $.ajax({
                type: "GET",
                cache: false,
                data: data,
                url: urlpath+"OperationLog/queryLog",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    $wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    $('.alert-danger').html("日志列表获取失败");
        			$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper.spinModal(false);
                }
            });
        },
        "columns": [
            { "data": "operation_time",width: 200 },
            { "data": "user_name" },
            { "data": "operation_action" },
            { "data": "operation_desc" },

        ],
        "createdRow": function ( row, data, index ) {
        	 $('td', row).eq(1).bind("click",function(){
                 $('#myModal').modal('show');
                 vm.getDetail(data.user_id);
             });

             $('td', row).eq(1).bind("mouseenter",function(){
                 $('td',row).eq(1).css({cursor:"pointer"});
             });
        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table).eq(0).click();
        },
    })).api();

    // 自定义搜索
    $('#searchBtn').on('click',function(res){
    		paramJson = {
			"name": $('input[name="username"]').val(),
			"start_time": $('input[name="start_time"]').val(),
			"end_time": $('input[name="end_time"]').val(),
			"token": getCookie('token')
		};
    	table.ajax.reload(null,false);
    });
});

//获取闸机详情
var vm = new Vue({
	el: '#myModal',
	data: {
		userDetail: {},
	},
	methods: {
		getDetail: function(id){
			$.post(urlpath+"User/getuserinfo",{
				"id": id,
				"token": getCookie("token")
			},function(res){
				console.log(res);
				if(res.code==200){
					vm.$data.userDetail = res.data;
				}
			});
		}
	}
});

//导出数据
$('#exportData').click(function(){
	sureTool("确定导出日志信息列表吗","",exData);
	
});
function exData(){
	window.open(urlpath+"OperationLog/queryLogExcel?token="+getCookie("token")+"&paramJson=%7B%7D");
} 

