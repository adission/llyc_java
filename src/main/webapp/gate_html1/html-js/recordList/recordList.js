/**
 * Created by wangyanling on 2017/10/18.
 */
var table;
$(function(){
	
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

	
	
    var $wrapper = $('#div-table-container');
    var $table =$('#recordList');

    var paramJson = {
    		"name": '',
    		"mobile": '',
    		"card_id": '',
    		"card_no": '',
    		"start_time": '',
    		"end_time": "",
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
                url: urlpath+"GateCheckLog/getCheckLog",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    $wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    $('.alert-danger').html("刷卡记录列表获取失败");
        			$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper.spinModal(false);
                }
            })
        },
        columns: [
            {
                orderable: false,
                data: null,
                targets:0,
                "defaultContent": "<span  class='row-details row-details-close'></span>"
            },
            { "data": "check_date"},
            { "data": "name" },
            { "data": "gate_no" },
            { "data": "cross_flag" },
            { "data": "class_name" },
            { "data": "team" },
        ],
        "createdRow": function ( row, data, index ) {
        	$('td', row).eq(1).css("width","15%");
        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table).eq(0).click();
        },

    })).api();

    function format ( d ) {
    	var img = d.avatar_img.indexOf('http')!=-1?d.avatar_img:(urlpath+d.avatar_img);
        return `<div>
        			<div class="col-xs-2 text-center">
		                 <img src="${img}" style="width: 100px; margin-top: 10px;">
		            </div>
		            <form class="form-inline col-xs-10 formDetail">
		                <div class="form-group col-xs-3 text-center">
        					时间: ${d.check_date}
		                </div>
		                <div class="form-group col-xs-3 text-center">
        					姓名: ${d.name}
		                </div>
		                <div class="form-group col-xs-3 text-center">
        					性别: ${d.gender}
		                </div>
		                <div class="form-group  col-xs-3 text-center">
	                    	年龄: 27
		                </div>
		
		                <div class="form-group col-xs-3 text-center">
	                    	认证类型: ${d.type}
		                </div>
		                <div class="form-group  col-xs-3 text-center">
	                    	闸机号: ${d.gate_no}
		                </div>
		                <div class="form-group col-xs-3 text-center">
	                    	行为: ${d.cross_flag}
		                </div>
		                <div class="form-group col-xs-3 text-center">
	                    	人员类别: ${d.class_name}
		                </div>
		                <div class="form-group col-xs-3 text-center">
        					班组: ${d.team}
		                </div>
		                <div class="form-group col-xs-3 text-center">
                    		工种: ${d.worker_type}
		                </div>
		                <div class="form-group col-xs-5 text-center">
	                    	身份证号码: ${d.cid}
		                </div>
	            </form></div>` ;
    }
    // 点击展开详情
    $('.table').on('click', 'tbody tr td:first-child',function(){
        var tr = $(this).parent().closest('tr');
        var row = table.row( tr );
        if ( row.child.isShown() ) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
            $(this).children().addClass("row-details-close").removeClass("row-details-open");
        }
        else {
            // Open this row
            row.child( format(row.data()) ).show();
            tr.addClass('shown');
            $(this).children().addClass("row-details-open").removeClass("row-details-close");
        }
    });
    
    // 自定义搜索
    $('#searchBtn').on('click',function(res){
		paramJson = {
				"name": Trim($('input[name="username"]').val()),
	    		"mobile": Trim($('input[name="mobile"]').val()),
	    		"card_id": Trim($('input[name="id_card"]').val()),
	    		"card_no": Trim($('input[name="card_no"]').val()),
	    		"start_time": $('input[name="start_time"]').val(),
	    		"end_time": $('input[name="end_time"]').val(),
	    		"token": getCookie('token')
	    };
    	table.ajax.reload(null,false);
    });

});

//导出数据
$('#exportData').click(function(){
	sureTool("确定导出人员打卡信息列表吗","",exData);
	
});
function exData(){
	window.open(urlpath+"GateCheckLog/getCheckLogExcel?token="+getCookie("token")+"&paramJson=%7B%7D");
} 

