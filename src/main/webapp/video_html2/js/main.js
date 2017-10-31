$(function(){
    $("#header").load("template/header.html",function(){
    	var currentPage = window.location.href.split("/video_html2/")[1].split("_")[0];
    	$("#"+currentPage).addClass("active").siblings().removeClass("active");
    });
    $("#footer").load("template/footer.html");
    var windowHeight=$(document).height()-131;
    $(".content-left").css("height",windowHeight);
    $(".content-center").css("height",windowHeight);
    $(".content-right").css("height",windowHeight);
    
});

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
		}else if(res.code==2) {
			location.href="login.html";
		}
	});
} 

//datatables 默认参数
var CONSTANT = {
    DATA_TABLES : {
        DEFAULT_OPTION : { //DataTables初始化选项
            "scrollY": false,//dt高度
            //"lengthMenu": [
            //    [10, 2, 10, -1],
            //    [1, 2, 10, "All"]
            //],//每页显示条数设置
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
                data = data||"";
                return '<span title="' + data + '">' + data + '</span>';
            },
            // 常用编辑按钮
            BUTTONDS: function(data, type, row, meta){
                data = data||"";
                return '<span title="' + data + '">' + data + '</span>';
            }


        }
    }
};


var ScrollTime;
function ScrollAutoPlay(contID,scrolldir,showwidth,textwidth,steper){
    var PosInit,currPos;
    with($('#'+contID)){
        currPos = parseInt(css('margin-left'));
        if(scrolldir=='left'){
            if(currPos<0 && Math.abs(currPos)>textwidth){
                css('margin-left',showwidth);
            }
            else{
                css('margin-left',currPos-steper);
            }
        }
        else{
            if(currPos>showwidth){
                css('margin-left',(0-textwidth));
            }
            else{
                css('margin-left',currPos-steper);
            }
        }
    }
}
//--------------------------------------------左右滚动效果----------------------------------------------
/*
AppendToObj：        显示位置（目标对象）
ShowHeight：        显示高度
ShowWidth：        显示宽度
ShowText：        显示信息
ScrollDirection：    滚动方向（值：left、right）
Steper：        每次移动的间距（单位：px；数值越小，滚动越流畅，建议设置为1px）
Interval:        每次执行运动的时间间隔（单位：毫秒；数值越小，运动越快）
*/
function ScrollText(AppendToObj,ShowHeight,ShowWidth,ShowText,ScrollDirection,Steper,Interval){
    var TextWidth,PosInit,PosSteper;
    with(AppendToObj){
        html('');
        css('overflow','hidden');
        css('height',ShowHeight+'px');
        css('line-height',ShowHeight+'px');
        css('width',ShowWidth);
    }
    if (ScrollDirection=='left'){
        PosInit = ShowWidth;
        PosSteper = Steper;
    }
    else{
        PosSteper = 0 - Steper;
    }
    if(Steper<1 || Steper>ShowWidth){Steper = 1}//每次移动间距超出限制(单位:px)
    if(Interval<1){Interval = 10}//每次移动的时间间隔（单位：毫秒）
    var Container = $('<div></div>');
    var ContainerID = 'ContainerTemp';
    var i = 0;
    while($('#'+ContainerID).length>0){
        ContainerID = ContainerID + '_' + i;
        i++;
    }
    with(Container){
        attr('id',ContainerID);
        css('float','left');
        css('cursor','default');
        appendTo(AppendToObj);
        html(ShowText);
        TextWidth = width();
        if(isNaN(PosInit)){PosInit = 0 - TextWidth;}
        css('margin-left',PosInit);
        mouseover(function(){
            clearInterval(ScrollTime);
        });
        mouseout(function(){
            ScrollTime = setInterval("ScrollAutoPlay('"+ContainerID+"','"+ScrollDirection+"',"+ShowWidth+','+TextWidth+","+PosSteper+")",Interval);
        });
    }
    ScrollTime = setInterval("ScrollAutoPlay('"+ContainerID+"','"+ScrollDirection+"',"+ShowWidth+','+TextWidth+","+PosSteper+")",Interval);
}