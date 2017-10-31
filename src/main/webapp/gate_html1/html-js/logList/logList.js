$(function(){

    initDatable();

});

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


/**
 * 初始化datatable
 */
function initDatable() {
    var paramJson = {
    		"name": '',
    		"start_time": '',
    		"end_time": '',
    		"token": getCookie('token')
    };
    
    var $wrapper = $('#div-table-container');
    var $table =$('#logList');

    table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
            data.paramJson = JSON.stringify(paramJson);
            $wrapper.spinModal();
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: urlpath+"OperationLog/queryLog",
                dataType: "json",
                success: function (result){
                    console.log(result);
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
                vm.getDetail(data.id);
            });

            $('td', row).eq(1).bind("mouseenter",function(){
                $('td',row).eq(1).css({cursor:"pointer"});
            });
        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table).eq(0).click();
        }
    })).api();
    
    // 自定义搜索
    $('#searchBtn').on('click',function(res){
    		paramJson = {
			"name": Trim($('input[name="username"]').val()),
			"start_time": $('input[name="from"]').val(),
			"end_time": $('input[name="to"]').val(),
			"token": getCookie('token')
		};
    	table.ajax.reload(null,false);
    });
    
}
//获取人员详情
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

