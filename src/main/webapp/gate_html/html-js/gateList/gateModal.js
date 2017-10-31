/**
 * wangyanling by 2017/8/31
 */
$(function(){
	gateTypeList();
	gateBlock();
	var id = $('#myModal').attr("data-id");
	if(id != '') {
		getGate(id);
	}
	
	$('#form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	gatename: {
                message: '闸机名称验证失败',
                validators: {
                    notEmpty: {
                        message: '闸机名称不能为空'
                    }
                }
            },
            sn: { // 控制器编号
                validators: {
                    notEmpty: {
                        message: '闸机编号不能为空'
                    }
                }
            },
            gateno: { //　闸机编号
            	validators: {
                    notEmpty: {
                        message: '闸机编号不能为空'
                    }
                }
            },
            connect_port: {
            	validators: {
                    notEmpty: {
                        message: '端口不能为空'
                    },
                }
            },
            porttype3: {
            	enabled: false,
            	validators: {
                    notEmpty: {
                        message: 'TCP/IP不能为空'
                    },
                }
            }
        }
    }).on('success.form.bv', function(e) {//点击提交之后
        e.preventDefault();
        sureGate();
    });
});

function getGate(id){
	if(id == '') {
		return;
	}
	var paramsData = {
		"id" : id,
		"token": getCookie("token")
	};
	$.post(urlpath+"GateList/getinfo",{
		"id": id,
		"token": getCookie("token")
	},function(res){
		if(res.code==200){
			var user = res.data;
			$('select[name="type"] option[value='+user.type+']').attr("selected",true);
			$('select[name="type"]').attr("disabled",true);
		    $('input[name="gatename"]').val(user.gate_name);
		    $('input[name="gateno"]').val(user.gate_no);
		    $('input[name="sn"]').val(user.sn);
		    $('input[name="common"]').val(user.common);
		    $('input[name="location"]').val(user.location);
		    $('input[name="password"]').val(user.password);
		    
		    $('select[name="is_kaoqin"] option[value='+user.is_kaoqin+']').attr("selected",true);
		    $('select[name="cross_flag"] option[value='+user.cross_flag+']').attr("selected",true);
		    $('select[name="is_use"] option[value='+user.is_use+']').attr("selected",true);
		    $('select[name="connect_id"] option[value='+user.connect_id+']').attr("selected",true);
		    $('select[name="group_id"] option[value='+user.group_id+']').attr("selected",true);
		}
	});
}



$('input[name="optionsRadios"]').on('change', function() {
    var bootstrapValidator = $('#form').data('bootstrapValidator'),
        ip     = ($(this).val() == 'option3');

    ip ? $('#form').find('input[name="porttype3"]').removeAttr('disabled')
       : $('#form').find('input[name="porttype3"]').attr('disabled', 'disabled');
    bootstrapValidator.enableFieldValidators('porttype3', ip);
});

/**
 * 获取闸机型号列表
*/
function gateTypeList(){
	$.post(urlpath+'GateList/getAllAddInformation',{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			for(var i=0;i<res.data.alltype.length;i++){
				$('select[name="type"]').append('<option value="'+res.data.alltype[i].type_value+'">'+res.data.alltype[i].type_name+'</option>');
			}
			
			for(var i=0;i<res.data.allcanport.length;i++){
				$('select[name="porttype1"]').append('<option value="'+res.data.allcanport[i]+'">'+res.data.allcanport[i]+'</option>');
				$('select[name="porttype2"]').append('<option value="'+res.data.allcanport[i]+'">'+res.data.allcanport[i]+'</option>');
			}
		}
	});
}

// 获取闸机分区列表
function gateBlock(){
	$.post(urlpath+"Gategroup/getAll",{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			for(var i=0;i<res.data.length;i++){
				$('select[name="group_id"]').append('<option value="'+res.data[i].id+'">'+res.data[i].group_name+'</option>');
			}
		}
	});
}



//确认按钮闸机
function sureGate(){
	var id = $('#myModal').attr("data-id");
	
	var ipS = $('input:radio[name="optionsRadios"]:checked').val();
	var ip;
	if(ipS =="option1"){
		ip = $('select[name="porttype1"] option:selected').val()
	}else if(ipS =="option2"){
		ip = $('select[name="porttype2"] option:selected').val()
	}else if(ipS =="option3"){
		ip = $('input[name="porttype3"]').val()
	}
	if(id != '') {// 编辑
		var paramJson = {
				"id": id,
				 "is_kaoqin": $('select[name="is_kaoqin"] option:selected').val(),
				 "gate_name": $('input[name="gatename"]').val(),
				 "cross_flag": $('select[name="cross_flag"] option:selected').val(),
				 "sn": $('input[name="sn"]').val(),
				 "connect_id": $('select[name="connect_id"] option:selected').val(),
				 "gate_no": $('input[name="gateno"]').val(),
				 "location": $('input[name="location"]').val(),
				 "group_id": $('select[name="group_id"] option:selected').val(),
				 "is_use": $('select[name="is_use"] option:selected').val(),
				 "reader_bytes": $('select[name="reader_bytes"] option:selected').val(),
				 "common": $('input[name="common"]').val(),
				 "connect_port": $('input[name="connect_port"]').val(),
				 "connect_ip": ip ,
				 "password": $('input[name="password"]').val(),
				 "is_master": $('select[name="is_master"] option:selected').val()
		};
		$.post(urlpath+"GateList/update",{
			"paramJson": JSON.stringify(paramJson),
			 "token": getCookie('token')
		},function(res){
			tool2(res.code==200,"myModal","闸机信息编辑成功","闸机信息编辑失败");
		});
	}else {//添加
		var paramJson = {
				"type": $('select[name="type"] option:selected').val(),
				 "is_kaoqin": $('select[name="is_kaoqin"] option:selected').val(),
				 "gate_name": $('input[name="gatename"]').val(),
				 "cross_flag": $('select[name="cross_flag"] option:selected').val(),
				 "sn": $('input[name="sn"]').val(),
				 "connect_id": $('select[name="connect_id"] option:selected').val(),
				 "gate_no": $('input[name="gateno"]').val(),
				 "location": $('input[name="location"]').val(),
				 "group_id": $('select[name="group_id"] option:selected').val(),
				 "is_use": $('select[name="is_use"] option:selected').val(),
				 "reader_bytes": $('select[name="reader_bytes"] option:selected').val(),
				 "common": $('input[name="common"]').val(),
				 "connect_port": $('input[name="connect_port"]').val(),
				 "connect_ip": ip ,
				 "password": $('input[name="password"]').val(),
				 "is_master": $('select[name="is_master"] option:selected').val()
				 
		 };
		 $.post(urlpath+"GateList/add",{
			 "paramJson": JSON.stringify(paramJson),
			 "token": getCookie('token')
		 },function(res){
			 tool2(res.code==200,"myModal","闸机添加成功","闸机添加失败");
		 });
	}
	
			
}