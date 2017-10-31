/*常量*/
function showLeft(){
    //$(".content .content-left").width(window.screen.width * 0.15);                //屏幕分辨率的高
    //$(".content .content-right").width(window.screen.width * 0.8);                //屏幕分辨率的高
    //$(".content").width(window.screen.width * 0.97);
    //$('.header').width(window.screen.width);

    $('.content-left').load("tel/nav.html",function(){
        var currentPage = window.location.href.split("/userLocation/")[1];
        $(".content-left a").each(function(index, element) {
            var href = $(this).attr('href');
            if (href.indexOf(currentPage) >= 0) {
                $(this).parent("li").addClass("open").addClass("opens");
                $(this).addClass("active");
            }
        });
        var Accordion = function(el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;

            // Variables privadas
            var links = this.el.find('.link');
            // Evento
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
        };
        Accordion.prototype.dropdown = function(e) {
            var $el = e.data.el;
            var $this = $(this),
                $next = $this.next();

            $next.slideToggle();
            $this.parent().toggleClass('open');

            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp().parent().removeClass('open');
            };
        };

        var accordion = new Accordion($('#accordion'), false);
    });
}




function showHeader(){
    $('.header').load("tel/header.html",function(){
        $('.header-img').click(function(){

            // 如果是小屏点击不响应
            if($('body').width()>=991) {
                //如果是大导航变成小导航
                if($('.content-right').hasClass("col-md-10")){
                    $(".header-logo").hide();
                    $(".header-img").css("margin-left",50);
                    $('.content-right').removeClass("col-md-10").addClass("col-md-11");
                    $('.content-left').removeClass("col-md-2").addClass("col-md-1");

                    $('.accordion>li>i').css("left","35%");
                    $('div.link span').hide();
                    $('div.link i').hide();
                    $('ul.submenu').addClass("small");
                    //$('ul.submenu').hide();
                    //
                    //$('ul.submenu.small').toggle('left',"108%");
                }else {
                    $(".header-logo").show();
                    $(".header-img").css("margin-left",0);
                    $('.content-right').removeClass("col-md-11").addClass("col-md-10");
                    $('.content-left').removeClass("col-md-1").addClass("col-md-2");

                    $('.accordion>li>i').css("left",15);
                    $('div.link span').show();
                    $('div.link i').show();
                    $('ul.submenu').removeClass("small");
                }
            }else if($('body').width()<=768){
                //$(".header-logo").show();
                $('#accordion').toggle();

                if($('body').width()<=550){
                    $('.content-left').css('width',200);
                    $(".content-right").css("width",550);
                    $(".content-right").removeClass("col-xs-12")
                }else {
                    $('.content-left').addClass("pull-left");
                    $('.content-left').toggleClass("col-xs-3");
                    $(".content-right").toggleClass("col-xs-12").toggleClass("col-xs-9");
                }
            }

            $('.dataTables_scrollHeadInner').width("100%");;
            $('.dataTables_scrollHeadInner table').width("100%");
        });



        // 个人资料
        $('.dropdown img').click(function(){
            if($('li.dropdown').hasClass("open")){
                $("li.dropdown").removeClass('click');
            }else {
                $("li.dropdown").addClass('click');
            }
        });
        //任意地点的单击事件 取消其click class属性
        $(document).click(function(){
            if(!$('li.dropdown').hasClass("open")){
                $("li.dropdown").removeClass('click');
            }
        });

    });
}

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

//通过点相对屏幕上的xy坐标、模态框的宽和高、地图框的宽和高、使用模态框的元素来确定模态框在地图中的位置
function getLocation(point_x,point_y,modal_height,modal_width,map_x,map_y,element){
	var scrollTop=document.body.scrollTop //浏览器右边滚轮的移动距离
	if(point_x-200<map_x/2&&point_y>map_y/2){
        if(map_y-point_y>modal_height/2){
            var awq=$(window).width()/2-point_x-modal_width/2-30;
            var yy=point_y-130-modal_height/2-20;
            element.css("left",-awq).css("top",yy);
        }else{
            var awq=$(window).width()/2-point_x-modal_width/2-30;
            var yy=map_y-130-modal_height+5;
            element.css("left",-awq).css("top",yy);
        }
    }

    if(point_x-200>=map_x/2&&point_y>map_y/2){
        if(map_y-point_y>modal_height/2){
            var awq=point_x-$(window).width()/2-modal_width/2-10;
            var yy=point_y-130-modal_height/2-20;
            element.css("left",awq).css("top",yy);
        }else{
            var awq=point_x-$(window).width()/2-modal_width/2-10;
            var yy=map_y-130-modal_height+5;
            element.css("left",awq).css("top",yy);
        }
    }

    if(point_x-200<map_x/2&&point_y<=map_y/2){
        if(point_y>modal_height/2){
            var awq=$(window).width()/2-point_x-modal_width/2-30;
            var yy=point_y-130-modal_height/2+20;
            element.css("left",-awq).css("top",yy);
        }else{
            var awq=$(window).width()/2-point_x-modal_width/2-30; 
            if(scrollTop<=80){
            	var yy=-scrollTop
            }else{
            	var yy=-110
            }
            
            element.css("left",-awq).css("top",yy);
        }
    }

    if(point_x-200>=map_x/2&&point_y<=map_y/2){
        if(point_y>modal_height/2){
            var awq=point_x-$(window).width()/2-modal_width/2-10;
            var yy=point_y-130-modal_height/2+20;
            element.css("left",awq).css("top",yy);
        }else{
            var awq=point_x-$(window).width()/2-modal_width/2-10;
            if(scrollTop<=80){
            	var yy=-scrollTop
            }else{
            	var yy=-110
            }
            element.css("left",awq).css("top",yy);
        }
    }
}



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
            "bFilter": true, //列筛序功能
            "searching": false,//本地搜索
            "ordering": false, //排序功能
            //"Info": true,//页脚信息
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