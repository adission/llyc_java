/**
 * 闸机添加权限
 */
	var $wrapper2 = $('#div-table-container');
	var $table2 =$('#userList');
	var id =  window.location.href.split("=")[1];
    var paramJson = {
    		"user_name": '',  // 
    		"user_card": '',   // 
    		"mobile": '',   // 
    		"token": getCookie('token'),
    		"gate_id": id
    };
    var table2 = $table2.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
            $wrapper2.spinModal();
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: urlpath+"GateUser/queryUsersByIc",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    $wrapper2.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
//                	$('.alert-danger').html("闸机权限列表获取失败");
//        			$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper2.spinModal(false);
                }
            })
        },
        "columns": [
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "user_card" },
            { "data": "cid" },
            { "data": "name" },
            { "data": "mobile" },
        ],
        "createdRow": function ( row, data, index ) {

        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table2).eq(0).click();
        },

    })).api();
  //checkbox全选
    $("#checkAll1").on("click", function () {
        if ($(this).prop("checked") === true) {
            $("input[name='checkList']").prop("checked", $(this).prop("checked"));
            $('#gatePloe tbody tr').addClass('selected');
        } else {
            $("input[name='checkList']").prop("checked", false);
            $('#gatePloe tbody tr').removeClass('selected');
        }
    });
    $table2.on("change",":checkbox",function() {
        var checkbox = $("tbody :checkbox[name='checkList']",$table2);
        $("#checkAll1").prop('checked', checkbox.length == checkbox.filter(':checked').length);
    });
    // 自定义搜索
    $('.search-gate1').on('click',function(res){
    	var un,uc,mb;
    	uc = $("select[name='selectUser'] option:selected").val()=="1"?$('#selectUserValue').val():"";
    	un = $("select[name='selectUser'] option:selected").val()=="2"?$('#selectUserValue').val():"";
    	mb = $("select[name='selectUser'] option:selected").val()=="3"?$('#selectUserValue').val():"";
    	
		paramJson = {
			"user_name": un,  // 
    		"user_card": uc,   // 卡号
    		"mobile": mb,
    		"gate_id": id,
    		"token": getCookie('token')
		};
    	table2.ajax.reload(null,false);
    });
    
    // 闸机列表
    var $wrapper1 = $('#div-table-container1');
    var $table1 =$('#gateList');
    
    var paramJson1 = {
    		"gate_no": '',   //
    		"gate_name": '',
    		"token": getCookie('token')
    };
    var table1 = $table1.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	$wrapper1.spinModal();
        	data.paramJson = JSON.stringify(paramJson1);
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: urlpath+"GateList/list",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    callback(result);
                    $wrapper1.spinModal(false);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
//                	$('.alert-danger').html("闸机权限列表获取失败");
//        			$('.alert-danger').show(300).delay(1000).hide(300);
                	$wrapper1.spinModal(false);
                }
            })
        },
        "columns": [
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "gate_no" },
            { "data": "gate_name" },
        ],
        "createdRow": function ( row, data, index ) {

        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table1).eq(0).click();
        },

    })).api();
    //checkbox全选
    $("#checkAll").on("click", function () {
        if ($(this).prop("checked") === true) {
            $("input[name='checkList']").prop("checked", $(this).prop("checked"));
            $('#gatePloe tbody tr').addClass('selected');
        } else {
            $("input[name='checkList']").prop("checked", false);
            $('#gatePloe tbody tr').removeClass('selected');
        }
    });
    $table1.on("change",":checkbox",function() {
        var checkbox = $("tbody :checkbox[name='checkList']",$table1);
        $("#checkAll").prop('checked', checkbox.length == checkbox.filter(':checked').length);
    });
    // 自定义搜索
    $('.search-gate2').on('click',function(res){
    	var gna,gno;
    	gno = $("select[name='selectGate'] option:selected").val()=="1"?$('#selectGateValue').val():"";
    	gna = $("select[name='selectGate'] option:selected").val()=="2"?$('#selectGateValue').val():"";
    	
		paramJson = {
			"gate_name": gna,  // 
    		"gate_no": gno,   // 卡号
    		"token": getCookie('token')
		};
    	table1.ajax.reload(null,false);
    });
    
    var tag= 0;// tag=0 为添加人员，tag =1为添加闸机
    $('.add-gate').click(function(){
    	var tables = $.fn.dataTable.fnTables(true);
	    if ( tables.length > 0 ) {
	    	$(tables).dataTable().fnAdjustColumnSizing();
	    }
    	tag =0;
        $('.add-gate').removeClass("btn-default").addClass("btn-primary");
        $('.copy-gate').removeClass("btn-primary").addClass("btn-default");
        $('.add-gate-list').show();
        $('.copy-gate-list').hide();
    });
    $('.copy-gate').click(function(){
    	var tables = $.fn.dataTable.fnTables(true);
	    if ( tables.length > 0 ) {
	    	$(tables).dataTable().fnAdjustColumnSizing();
	    }
    	tag =1;
        $('.copy-gate').removeClass("btn-default").addClass("btn-primary");
        $('.add-gate').removeClass("btn-primary").addClass("btn-default");
        $('.copy-gate-list').show();
        $('.add-gate-list').hide();
        table1.ajax.reload(null,false);
    });
    
    
    // 添加闸机/  添加人员
    function addPloe(){
    	var arrItemId;
        var arrItemName;
        if(tag==0){
        	arrItemId=[];
        	arrItemName=[];
        	$("tbody :checkbox:checked",$table2).each(function(i) {
	            var item = table2.row($(this).closest('tr')).data();
	            arrItemId.push(item.id);
	            arrItemName.push(item.name);
	        });
        	$.post(urlpath+"GateUserAuth/setAuth",{
        		"gate_id": id,
        		"user_ids": arrItemId.join(','),
        		"token": getCookie('token')
        	},function(res){
        		tool2(res.code==200,"myModal","闸机权限添加成功",res.message);
        	});
        }else {
        	arrItemId=[];
        	arrItemName=[];
        	$("tbody :checkbox:checked",$table1).each(function(i) {
	            var item = table1.row($(this).closest('tr')).data();
	            arrItemId.push(item.id);
	            arrItemName.push(item.gate_name);
	        });
        	$.post(urlpath+"GateUserAuth/copyAuth",{
        		"new_gate_id": id,
        		"ids": arrItemId.join(','),
        		"token": getCookie('token')
        	},function(res){
        		tool2(res.code==200,"myModal","复制闸机权限成功",res.message);
        	});
        }
    }
    
    
    
    
    