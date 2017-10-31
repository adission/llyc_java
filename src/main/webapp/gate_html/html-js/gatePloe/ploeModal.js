/**
 * 闸机添加权限
 */

	var $table =$('#userList');
    
    var paramJson = {
    		"user_name": '',  // 
    		"gate_no": '',   // 卡号
    		"mobile": '',   // 卡号
    		"token": getCookie('token')
    };
    var table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
//            $wrapper.spinModal();
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: urlpath+"GateUser/queryUserslist",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
//                    $wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
//                	$('.alert-danger').html("闸机权限列表获取失败");
//        			$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper.spinModal(false);
                }
            })
        },
        "columns": [
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "cid" },
            { "data": "name" },
            { "data": "mobile" },
        ],
        "createdRow": function ( row, data, index ) {

        },
        "drawCallback": function( settings ) {
            $("tbody tr",$table).eq(0).click();
        },

    })).api();
    
    
    
    
    
    var $table1 =$('#gateList');
    
    var paramJson1 = {
    		"gate_no": '',   // 卡号
    		"token": getCookie('token')
    };
    var table1 = $table1.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
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
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
//                	$('.alert-danger').html("闸机权限列表获取失败");
//        			$('.alert-danger').show(300).delay(1000).hide(300);
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
            $("tbody tr",$table).eq(0).click();
        },

    })).api();
    var id =  window.location.href.split("=")[1];
    var tag= 0;// tag=0 为添加人员，tag =1为添加闸机
    
    $('.add-gate').click(function(){
    	tag =0;
        $('.add-gate').removeClass("btn-default").addClass("btn-primary");
        $('.copy-gate').removeClass("btn-primary").addClass("btn-default");
        $('.add-gate-list').show();
        $('.copy-gate-list').hide();
    });
    $('.copy-gate').click(function(){
    	tag =1;
        $('.copy-gate').removeClass("btn-default").addClass("btn-primary");
        $('.add-gate').removeClass("btn-primary").addClass("btn-default");
        $('.copy-gate-list').show();
        $('.add-gate-list').hide();
    });
    
    
    // 添加闸机/  添加人员
    function addPloe(){
    	var arrItemId;
        var arrItemName;
        if(tag==0){
        	arrItemId=[];
        	arrItemName=[];
        	$("tbody :checkbox:checked",$table).each(function(i) {
	            var item = table.row($(this).closest('tr')).data();
	            arrItemId.push(item.id);
	            arrItemName.push(item.name);
	        });
        	$.post(urlpath+"GateUserAuth/setAuth",{
        		"gate_id": id,
        		"user_ids": arrItemId.join(','),
        		"token": getCookie('token')
        	},function(res){
        		tool2(res.code==200,"myModal","闸机权限添加成功","闸机权限添加失败");
//        		if(res.code==200){
//        			$('#myModal').modal('hide');
//    				$('.alert-success').html("闸机权限添加成功");
//    				$('.alert-success').show(300).delay(1000).hide(300);
//        		}else {
//        			$('#myModal').modal('hide');
//					$('.alert-danger').html("闸机权限添加失败");
//					$('.alert-danger').show(300).delay(1000).hide(300);
//        		}
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
        		tool2(res.code==200,"myModal","复制闸机权限成功","复制闸机权限失败");
//        		if(res.code==200){
//        			$('#myModal').modal('hide');
//    				$('.alert-success').html("复制闸机权限成功");
//    				$('.alert-success').show(300).delay(1000).hide(300);
//        		}else {
//        			$('#myModal').modal('hide');
//					$('.alert-danger').html("复制闸机权限失败");
//					$('.alert-danger').show(300).delay(1000).hide(300);
//        		}
        	});
        }
        
    }
    
    
    
    
    