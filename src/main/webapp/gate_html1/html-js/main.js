
$(function(){
    "use strict";
    $.ajaxSetup ({ cache: false });
    showHead();
    $("input").val("");
})

function showHead(){
    $('.header').load('tel/header.html',function(result){
    	var currentPage = window.location.href.split("/gate_html1/")[1];
    	if(currentPage.indexOf("gate") >= 0){
        	$(".mainnav>li:nth-child(3)").addClass("active");
    		$(".mainnav>li:nth-child(3)").siblings().removeClass("active");
        }else if(currentPage.indexOf("person") >= 0){
        	$(".mainnav>li:nth-child(4)").addClass("active");
    		$(".mainnav>li:nth-child(4)").siblings().removeClass("active");
        }else if(currentPage.indexOf("setting") >= 0){
        	$(".mainnav>li:nth-child(6)").addClass("active");
    		$(".mainnav>li:nth-child(6)").siblings().removeClass("active");
        }
    	
        $(".mainnav li a").each(function(index, element) {
            var href = $(this).attr('href');
            if (href.indexOf(currentPage) >= 0 ) {
        		$(this).parent("li").addClass("active");
        		$(this).parent("li").siblings().removeClass("active");
            } 
        });
    });
}

var server=window.location.host+"/lanlyc/";
var urlpath = "http://"+server;
// datatables 默认参数
var CONSTANT = {
    DATA_TABLES : {
        DEFAULT_OPTION : { //DataTables初始化选项
            "scrollY": false,//dt高度
            "lengthChange": false,//是否允许用户自定义显示数量
            "bPaginate": true, //翻页功能
            "bFilter": false, //列筛序功能
            "searching": false,//本地搜索
            "ordering": false, //排序功能
//            "Info": true,//页脚信息
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
            ]//第一列排序图标改为默认
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
            }
        }
    }
};

//cookie操作
function setCookie(name,value){
	var Days = 100000;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString()+ ";path=/lanlyc/";
}
function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}
function delCookie(name){
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval=getCookie(name);
	if(cval!=null){
		document.cookie= name + "="+''+";expires="+exp.toGMTString();
	}
}

//退出登录
function loginOut(){
	$.post(urlpath+'User/logout',{
		"token": getCookie('token')
	},function(res){
		if(res.code==200){
			location.href="login.html";
			
			delCookie("token");
 			delCookie("id");
 			delCookie("user_name");
 			delCookie("img");
			
		}else if(res.code==2) {
			location.href="login.html";
			delCookie("token");
 			delCookie("id");
 			delCookie("user_name");
 			delCookie("img");
		}
	});
}

var server=window.location.host+"/lanlyc/";
var urlpath = "http://"+server;

// 提示信息
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
//操作提示
function tool3(flag,sume,errme){
	if(flag){
		$('.alert-success').html(sume);
		$('.alert-success').show(300).delay(1000).hide(300);
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

//alert 提示
function alertTool(mess){
	$.alert({
        title: false,
        content: mess,
        opacity: 0.5,
        confirmButton: '好',
    });
}
//alert 提示
function alertTool(mess){
	$.alert({
        title: false,
        content: mess,
        autoClose: 'cancel|5000',
        opacity: 0.5,
        confirmButton: '好',
    });
}
//自动取消确认提示
function alertTool2(mess){
	$.confirm({
        title: false,
        content: mess,
        confirmButton: false,
        confirmButtonClass: 'btn-primary',
        autoClose: 'cancel|2000',
        cancelButton: '好',
        icon: 'fa fa-question-circle',
        animation: 'scale',
        animationClose: 'top',
        opacity: 0.5
    });
}

//　确认提示
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

function Trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, ""); 
}












