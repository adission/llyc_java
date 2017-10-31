/**
 * Created by Administrator on 2017/9/13.
 */
var publicLoad = "public";

if (!Array.prototype.indexOf){
   Array.prototype.indexOf = function(value, from){
     var len = this.length >>> 0;
     // 索引必须为整数，忽略小数尾数
     var from = parseInt(from) || 0;
     // 当索引小于0时，加上一次数组长度
     from = from < 0 ? from + len : from;
     for (; from < len; from++){
       if (from in this && this[from] === value)
       return from;
     }
     return -1;
   };
 }


function showHeader(){
    $('.header').load("tel/header.html",function(){
        var currentPage = window.location.href.split("/pos_html1/")[1];
        $(".mainnav li a").each(function(index, element) {
            var href = $(this).attr('href');
            if (href.indexOf(currentPage) >= 0) {
                $(this).parent("li").addClass("active");
                $(this).parent("li").siblings().removeClass("active");
            }
        });
        
        $('#labelSetting').click(function(){
            $("#labelModal").modal("show");
            $('.modal-backdrop').css("z-index",99);

        });
        $('.colorpicker-default').colorpicker({
            format: 'hex'
        });
    });
}


var CONSTANT = {
    DATA_TABLES: {
        DEFAULT_OPTION: { //DataTables初始化选项
            "scrollY": false,//dt高度
            //"lengthMenu": [
            //    [10, 2, 10, -1],
            //    [1, 2, 10, "All"]
            //],//每页显示条数设置
            "dom": 'frtip',
            "lengthChange": true,//是否允许用户自定义显示数量
            "bPaginate": true, //翻页功能
            "bFilter": false, //列筛序功能
            "searching": false,//本地搜索
            "ordering": false, //排序功能
            //"Info": true,//页脚信息
//            "bProcessing":true, 
            "paging": true,
            "serverSide": true,   //启用服务器端分页

            "autoWidth": true,//自动宽度
            "bDestroy": true,
            "oLanguage": {//国际语言转化
                "oAria": {
                    "sSortAscending": " 以升序排列此列",
                    "sSortDescending": " 以降序排列此列"
                },
                "sLengthMenu": "显示 _MENU_ 记录",
                "sZeroRecords": "对不起，查询不到任何相关数据",
                "sEmptyTable": "未有相关数据",
                "sLoadingRecords": "正在加载数据-请等待...",
                "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录。",
                "sInfoEmpty": "当前显示0到0条，共0条记录",
                "sInfoFiltered": "（数据库中共为 _MAX_ 条记录）",
                "sProcessing": "<img src='../resources/user_share/row_details/select2-spinner.gif'/> 正在加载数据...",
                "sSearch": "模糊查询：",
                "sUrl": "",
                //多语言配置文件，可将oLanguage的设置放在一个txt文件中，例：Javascript/datatable/dtCH.txt
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": " 上一页 ",
                    "sNext": " 下一页 ",
                    "sLast": " 尾页 ",
                    "sJump": "跳转"
                }
            },
            "order": [
                [0, null]
            ],//第一列排序图标改为默认
        },
        COLUMN: {
            CHECKBOX: {	//复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck" name="checkList">';
                }
            },
            // 全局按钮列样式
            BUTTONS: {
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return ' <button type="button" class="btn btn-small btn-danger btn-del" onclick="del()">删除</button>';
                }
            }
        },
        RENDER: {	//常用render可以抽取出来，如日期时间、头像等
            ELLIPSIS: function (data, type, row, meta) {
                data = data || "";
                return '<span title="' + data + '">' + data + '</span>';
            },
            // 常用编辑按钮
            BUTTONDS: function (data, type, row, meta) {
                data = data || "";
                return '<span title="' + data + '">' + data + '</span>';
            }


        }
    }
};


//cookie操作
function setCookie(name,value){
	var Days = 100000;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString()+ ";path=/";
}
function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}


var urlLocation = window.location.host+"/lanlyc/";


//提示信息
//操作提示
function tool(flag,sume,errme){
	if(flag){
		$('.alert-success').html(sume);
		$('.alert-success').show(300).delay(1000).hide(300);
		table.ajax.reload(null,false);
	}else {
		$('.alert-danger').html(errme);
		$('.alert-danger').show(300).delay(1000).hide(300);
	}
}

function tool2(flag,id,sume,errme){
	if(flag){
		$('#'+id).modal("hide");
		$('.alert-success').html(sume);
		$('.alert-success').show(300).delay(1000).hide(300);
		table.ajax.reload(null,false);
	}else {
		alertTool(errme);
	}
}



//  提示
function alertTool(mess){
	$.alert({
        title: false,
        content: mess,
        opacity: 0.5,
        confirmButton: '确定',
    });
}

//确认提示
function sureTool(mes,name,fun,id,status){
	$.confirm({
        title: false,
        content: mes+': '+name+'  ?',
        confirmButton: '确定',
        confirmButtonClass: 'btn-primary',
        //autoClose: 'cancel|5000',
        cancelButton: '取消',
        icon: 'fa fa-question-circle',
        animation: 'scale',
        animationClose: 'top',
        opacity: 0.5,
        confirm: function () {
        	fun(id,status);
        }
    });
}
//删除确认提示
function delTool(name,fun,id,status){
	$.confirm({
        title: false,
        content: '确定删除: '+name+'  ?',
        confirmButton: '确定',
        confirmButtonClass: 'btn-primary',
        //autoClose: 'cancel|5000',
        cancelButton: '取消',
        icon: 'fa fa-question-circle',
        animation: 'scale',
        animationClose: 'top',
        opacity: 0.5,
        confirm: function () {
        	fun(id,status);
        }
    });
}
