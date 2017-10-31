drag = 0
move = 0



// ��ק����
// �μ���http://blog.sina.com.cn/u/4702ecbe010007pe
var ie = document.all;
var nn6 = document.getElementById && !document.all;
var isdrag = false;
var y, x;
var oDragObj;
var svgObj;

function setView(x,y,w,h){
    //var svg = $('.avg');
    //svg.setAttribute("viewBox",x+" "+y+" "+w+" "+h);
    //svg.setAttribute("viewBox", "0 0 20 20");
    document.getElementById('svg').setAttributeNS('http://www.w3.org/2000/svg','viewBox',x+" "+y+" "+w+" "+h);
}

function moveMouse(e) {

    if (isdrag) {
        oDragObj.style.top = (nn6 ? nTY + e.clientY - y : nTY + event.clientY - y) + "px";
        svgObj.style.top = (nn6 ? nTY + e.clientY - y : nTY + event.clientY - y) + "px";

        //if(parseInt(oDragObj.style.top)>600){
        //    oDragObj.style.top=600+"px";
        //    svgObj.style.top=600+"px";
        //}
        //if(parseInt(oDragObj.style.top)<-200){
        //    oDragObj.style.top=-200+"px";
        //    svgObj.style.top=-200+"px";
        //}
        oDragObj.style.left = (nn6 ? nTX + e.clientX - x : nTX + event.clientX - x) + "px";
        svgObj.style.left = (nn6 ? nTX + e.clientX - x : nTX + event.clientX - x) + "px";

        //if(parseInt(oDragObj.style.left)>800){
        //    oDragObj.style.left=800+"px";
        //    svgObj.style.left=800+"px";
        //}
        //if(parseInt(oDragObj.style.left)<-200){
        //    oDragObj.style.left=-200+"px";
        //    svgObj.style.left=-200+"px";
        //}
        return false;
    }
}

function initDrag(e) {

    var oDragHandle = nn6 ? e.target : event.srcElement;
    var topElement = "HTML";
    while (oDragHandle.tagName != topElement && oDragHandle.className != "parent") {
        oDragHandle = nn6 ? oDragHandle.parentNode : oDragHandle.parentElement;
    }

    if (oDragHandle.className == "parent") {
        isdrag = true;
        oDragObj = oDragHandle.childNodes[1];
        svgObj = oDragHandle.childNodes[3];
        nTY = parseInt(oDragObj.style.top + 0);
        y = nn6 ? e.clientY : event.clientY;
        nTX = parseInt(oDragObj.style.left + 0);
        x = nn6 ? e.clientX : event.clientX;
        document.onmousemove = moveMouse;
        return false;
    }


}
document.onmousedown = initDrag;
document.onmouseup = new Function("isdrag=false");

function clickMove(s) {
    if (s == "up") {
        console.log(oDragObj.style.top)
        oDragObj.style.top = parseInt(oDragObj.style.top) + 100+"px";
        if(parseInt(oDragObj.style.top)>600){
            oDragObj.style.top=600+"px";
        }

    } else if (s == "down") {
        console.log(s)
        dragObj.style.top = parseInt(dragObj.style.top) - 100+"px";
    } else if (s == "left") {
        console.log(s)
        dragObj.style.left = parseInt(dragObj.style.left) + 100+"px";
    } else if (s == "right") {
        console.log(s)
        dragObj.style.left = parseInt(dragObj.style.left) - 100+"px";
    }

}

function smallit() {
    var height1 = images1.height;
    var width1 = images1.width;
    images1.height = height1 / 1.2;
    images1.width = width1 / 1.2;
}

function bigit() {
    var height1 = images1.height;
    var width1 = images1.width;
    images1.height = height1 * 1.2;
    images1.width = width1 * 1.2;
}
function realsize() {
    images1.height = images2.height;
    images1.width = images2.width;
    block1.style.left = 0;
    block1.style.top = 0;

}
function featsize() {
    var width1 = images2.width;
    var height1 = images2.height;
    var width2 = 701;
    var height2 = 576;
    var h = height1 / height2;
    var w = width1 / width2;
    if (height1 < height2 && width1 < width2) {
        images1.height = height1;
        images1.width = width1;
    }
    else {
        if (h > w) {
            images1.height = height2;
            images1.width = width1 * height2 / height1;
        }
        else {
            images1.width = width2;
            images1.height = height1 * width2 / width1;
        }
    }
    block1.style.left = 0;
    block1.style.top = 0;
}
$()

var id;

function onWheelZoom(obj) {  //鼠标滚动

    var img = obj.parentNode.childNodes[1].childNodes[1];
    $(obj).children().remove();
    // 图片放大
    zoom = parseFloat(img.style.zoom);
    tZoom = zoom + (event.wheelDelta > 0 ? 0.05 : -0.05);
    if (tZoom < 1.4 || tZoom > 5) return true;
    img.style.zoom = tZoom;

    $(obj).attr("width",949*tZoom);
    $(obj).attr("height",805*tZoom);

    clearInterval(id);

    //moveToPoint(tZoom,obj);
    draw()


    return false;
}
var data = [
    {
        x: 110,
        y: 120,
    },
    {
        x: 320,
        y: 140,
    },
    {
        x: 160,
        y: 710,
    },
    {
        x: 180,
        y: 290,
    },
    {
        x: 170,
        y: 130,
    },
    {
        x: 110,
        y: 140,
    },
    {
        x: 280,
        y: 160,
    },
    {
        x: 220,
        y: 190,
    },
];

function moveToPoint(tZoom,obj){
        var i=0;
        id = setInterval(frame, 1000);
        function frame() {
            if (i >= data.length) {
                clearInterval(id);
            } else {
                drawPerson(data[i].x,data[i].y,tZoom,obj,i);
                if(i<data.length-1){
                    drawLink(data[i],data[i+1],tZoom,obj);
                }
                i++;
            }
        }

}


function drawPerson(x,y,zoom,obj,i){
    var c=document.createElementNS("http://www.w3.org/2000/svg","circle");
    c.style.cx=x*zoom;
    c.style.cy=y*zoom;
    c.style.r = 10*zoom;
    c.style.fill="#ff0000";
    c.setAttribute("className","child"+i);
    c.setAttribute("data-id","child"+i);
    obj.appendChild(c);

}


function drawLink(t1,t2,zoom,obj){
    var l=document.createElementNS("http://www.w3.org/2000/svg","line");

    l.setAttribute("x1",t1.x*zoom);
    l.setAttribute("y1",t1.y*zoom);
    l.setAttribute("x2",t2.x*zoom);
    l.setAttribute("y2",t2.y*zoom);
    //l.setAttribute("transform","scale(0.5)");
    l.style.strokeWidth = 2;
    l.style.stroke="#ff0000";
    obj.appendChild(l);
}

$('#svg').on("click","circle",function(){
    var a=$(this);
    $(this).get(0).style.fill="#ffff00";//选中的颜色变黄
    $(this).siblings().css("fill","#ff0000");//没选中的颜色还原

    //alert($(this).attr("data-id"))

    /*alert(123);
     $("#myModal").modal("show");*/
    /*var param={"id":"ccc"}
     $.ajax({
     type:"post",
     url:"/position/worker/select_worker",
     data:{
     "paramJson":JSON.stringify(param),
     },
     success:function(result){
     if(result.code=="200"){
     console.log(result.data);
     var objDiv = $("#workerInfo");
     $(objDiv).modal("show");
     var name=$("#name").val(data.name);


     }else{
     alert(data.message);
     //$('#myModal2').modal('hide');
     }
     }
     });*/


    var data={"name":"李梦","sex":"女","nation":"回族","phone":"13110010001","id_card":"420123198510106962","native_place":"湖北黄冈红安",
        "post":"施工员","institute":"中铁一局","project_name":"土建施工","project_post":"班组长","identification_id":"J6206962",
        "technology":"施工","certificate":"12345","working_range":"放线","remarks":"","img":"images/l.jpg"};
    var objDiv = $("#workerInfo");
    $(objDiv).modal("show");

    $(".modal-backdrop").css("display","none");//删除class值为modal-backdrop的标签，可去除阴影


    //模态框的宽和高
    var modal_height=$(".modal-dialog").height();
    var modal_width=$(".modal-dialog").width();

    //人员点相对屏幕坐上的坐标
    var point_x=event.clientX;
    var point_y=event.clientY;

    //地图框的宽和高
    var map_x=$(window).width()-200;
    var map_y=$(window).height();//浏览器当前窗口可视区域高度


    if(point_x-200<map_x/2&&point_y>map_y/2){
        if(map_y-point_y>modal_height/2){
            var awq=$(window).width()/2-point_x-modal_width/2-40;
            var yy=point_y-30-modal_height/2-20;
            //alert(awq)
            $(".modal-content").css("left",-awq).css("top",yy);
        }else{
            var awq=$(window).width()/2-point_x-modal_width/2-40;
            var yy=map_y-30-modal_height-20;
            $(".modal-content").css("left",-awq).css("top",yy);
        }
    }

    if(point_x-200>=map_x/2&&point_y>map_y/2){
        if(map_y-point_y>modal_height/2){
            var awq=point_x-$(window).width()/2-modal_width/2-10;
            var yy=point_y-30-modal_height/2-20;
            //alert(awq)
            $(".modal-content").css("left",awq).css("top",yy);
        }else{
            var awq=point_x-$(window).width()/2-modal_width/2-10;
            var yy=map_y-30-modal_height-20;
            $(".modal-content").css("left",awq).css("top",yy);
        }
    }

    if(point_x-200<map_x/2&&point_y<=map_y/2){
        if(point_y>modal_height/2){
            var awq=$(window).width()/2-point_x-modal_width/2-40;
            var yy=point_y-30-modal_height/2+20;
            $(".modal-content").css("left",-awq).css("top",yy);
        }else{
            var awq=$(window).width()/2-point_x-modal_width/2-40;
            var yy=map_y-30-modal_height-20;
            $(".modal-content").css("left",-awq);
        }
    }

    if(point_x-200>=map_x/2&&point_y<=map_y/2){
        if(point_y>modal_height/2){
            var awq=point_x-$(window).width()/2-modal_width/2-10;
            var yy=point_y-30-modal_height/2+20;
            $(".modal-content").css("left",awq).css("top",yy);
        }else{
            var awq=point_x-$(window).width()/2-modal_width/2-10;
            var yy=map_y-30-modal_height-20;
            $(".modal-content").css("left",awq);
        }
    }


    var name=$("#name").text(data.name);
    var sex=$("#sex").text(data.sex);
    var nation=$("#nation").text(data.nation);
    var phone=$("#phone").text(data.phone);
    var id_card=$("#id_card").text(data.id_card);
    var native_place=$("#native_place").text(data.native_place);
    var post=$("#post").text(data.post);
    var institute=$("#institute").text(data.institute);
    var project_name=$("#project_name").text(data.project_name);
    var project_post=$("#project_post").text(data.project_post);
    var identification_id=$("#identification_id").text(data.identification_id);
    var technology=$("#technology").text(data.technology);
    var certificate=$("#certificate").text(data.certificate);
    var working_range=$("#working_range").text(data.working_range);
    var remarks=$("#remarks").text(data.remarks);
    $('#workerInfo img').attr("src",data.img);



    //如果弹窗消失，则选中的颜色还原
    $('#workerInfo').on("hide.bs.modal",function(){
        a.get(0).style.fill="#ff0000";
        $(".modal-content").removeClass("left").removeClass("top");
    })
})