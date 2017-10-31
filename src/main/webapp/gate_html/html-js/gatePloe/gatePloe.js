/**
 * Created by wangyanling on 2017/9/1.
 */
var table;
//获取闸机详情
var vm = new Vue({
	el: '#gateD',
	data: {
		gateDetail: {},
		da: '0'
	},
	methods: {
		getDetail: function(id){
			$.post(urlpath+"GateList/getinfo",{
				"id": id,
				"token": getCookie("token")
			},function(res){
				//console.log(res);
				if(res.code==200){
					vm.$data.gateDetail = res.data;
				}
			});
		}
	}
});

var id =  window.location.href.split("=")[1];
$(function(){
    showLeft();
    showHeader();
    
    gateTypeList();
	gateBlock();
    
    
    vm.getDetail(id);
   
    var $wrapper = $('#div-table-container');
    var $table =$('#table-detail');
    
    var paramJson = {
    		"user_name": '',  // 
    		"gate_no": '',   // 卡号
    		"gate_id": id,
    		"token": getCookie('token')
    };
    var table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        ajax: function(data,callback,settings){
        	data.paramJson = JSON.stringify(paramJson);
            $wrapper.spinModal();
            $.ajax({
                type: "POST",
                cache: false,
                data: data,
                url: urlpath+"GateUserAuth/getAllAuth",
                dataType: "json",
                success: function (result){
                    //关闭遮罩
                    $wrapper.spinModal(false);
                    callback(result);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                	$('.alert-danger').html("闸机权限列表获取失败");
        			$('.alert-danger').show(300).delay(1000).hide(300);
                    $wrapper.spinModal(false);
                }
            })
        },
        "columnDefs": [
            {
                orderable: false,
                data: null,
                targets: -1,
                width:200,
                "defaultContent": ' <div class="btn-group"> ' +
                '<button type="button" class="btn btn-default delRow" title="删除"><i class="glyphicon glyphicon-remove text-danger"></i></button>' +
                ' </div>	'
            },
            {
                orderable: false,
                data: null,
                targets: 1,
                "defaultContent": "<input class='' type='checkbox' name='checkList'>"
            },{
                orderable: false,
                data: null,
                targets: 0,
                "defaultContent": "<span  class='row-details row-details-close'></span>"
            }

        ],//第一列与第二列禁止排序
        "columns": [
            {"data": null },
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "gate_no" },
            { "data": "name" },
            { "data": "class_name" },
            { "data": "worker_type" },
            { "data": "team" },
            { "data": null },
        ],
        "createdRow": function ( row, data, index ) {

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
            arrItemId.push(item.user_id);
            arrItemName.push(item.name);
        });
        if(arrItemName.length!=0){
        	delTool(arrItemName.join(','),delPloe,arrItemId.join(","));
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
		
		var id  = $(this).attr('data-user_id');
		delTool(data.name,delPloe,data.user_id);
    });
	// 自定义搜索
    $('#searchBtn').on('click',function(res){
    		paramJson = {
    				"user_name": $('#username').val(),  // 
    	    		"gate_no": $('#cno').val(),   // 卡号
    	    		"gate_id": id,
    	    		"token": getCookie('token')
		};
    	table.ajax.reload(null,false);
    });
    function format ( d ) {
        // `d` is the original data object for the row
    	var img = d.avatar_img.indexOf('http')!=-1?d.avatar_img:(urlpath+d.avatar_img);
        return  `<div>
			        <img src="${img}" style="width: 70px">
			    </div>
			    <form class="form-inline col-xs-10">
			        <div class="form-group col-xs-3 text-center">
        				姓名: ${d.name}
			        </div>
			        <div class="form-group col-xs-3 text-center">
			            	年龄: ${d.age}
			        </div>
			        <div class="form-group col-xs-3 text-center">
		           		性别: ${d.gender}
			        </div>
			        <div class="form-group  col-xs-3 text-center">
			            	手机号: ${d.mobile}
			        </div>
			        <div class="form-group col-xs-3 text-center">
			            	类别: ${d.class_name}
			        </div>
			        <div class="form-group col-xs-3 text-center">
			            	状态: ${d.sate}
			        </div>
			        <div class="form-group  col-xs-3 text-center">
			            	身份证号码: ${d.id_card}
			        </div>
			        <div class="form-group col-xs-3 text-center">
			            	工种: ${d.worker_type}
			        </div>
			        <div class="form-group col-xs-3 text-center">
			            	班组: ${d.team}
			        </div>
			        <div class="form-group col-xs-3 text-center">
			            	惩罚次数: ${d.punish_record}
			        </div>
			        <div class="form-group col-xs-3 text-center">
			            	卡类别: ${d.type}
			        </div>
			        <div class="form-group col-xs-3 text-center">
			            	有效期: 2017-08-09
			        </div>
			        <div class="form-group col-xs-3 text-center">
        				备注: ${d.common}
			        </div>
			    </form>
			</div>`;						
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
        saveGate();
    });
    
});
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
			for(var i=0;i<res.data.allcanport.length;i++){
				$('select[name="porttype1"]').append('<option value="'+res.data.allcanport[i]+'">'+res.data.allcanport[i]+'</option>');
				$('select[name="porttype2"]').append('<option value="'+res.data.allcanport[i]+'">'+res.data.allcanport[i]+'</option>');
			}
		}
	});
}

//获取闸机分区列表
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

// 展开详情
$('.panel-title').click(function(){
    $('.panel-body').toggle();
});

//打开新增和编辑modal
function openModal(){
	$('#myModal').empty();
	$.ajaxSetup ({ cache: false });
	$('#myModal').load('html-js/gatePloe/ploeModal.html',function(result) {
		
		$(".modal-title").html("添加权限"); 
		$('#myModal').modal({
			show:true
		});
	});
}

//删除接口
function delPloe(ids){
	console.log(id);
	console.log(ids);
	$.post(urlpath+'GateUserAuth/delsomeAuth',{
		"gate_id": id,
		"user_ids": ids,
		"token": getCookie('token')
	},function(res){
		tool(res.code==200,"人员权限删除成功","人员权限删除失败");
	});
}





