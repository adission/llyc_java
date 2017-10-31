/**
 * 
 */
drag = 0
move = 0

var ie = document.all;
var nn6 = document.getElementById && !document.all;
var isdrag = false;
var y, x;
var oDragObj;
var svgObj;
var userObj;
var id=0;


function moveMouse(e) {

    if (isdrag) {
        oDragObj.style.top = (nn6 ? nTY + e.clientY - y : nTY + event.clientY - y) + "px";
        svgObj.style.top = (nn6 ? nTY + e.clientY - y : nTY + event.clientY - y) + "px";
        userObj.style.top = (nn6 ? nTY + e.clientY - y : nTY + event.clientY - y) + "px";

       
        oDragObj.style.left = (nn6 ? nTX + e.clientX - x : nTX + event.clientX - x) + "px";
        svgObj.style.left = (nn6 ? nTX + e.clientX - x : nTX + event.clientX - x) + "px";
        userObj.style.left = (nn6 ? nTX + e.clientX - x : nTX + event.clientX - x) + "px";

        return false;
    }
}
// 初始化
function initDrag(e) {
    var oDragHandle = nn6 ? e.target : event.srcElement;
    
    while (oDragHandle.className != "parent" && oDragHandle.tagName !="HTML" ) {
        oDragHandle = nn6 ? oDragHandle.parentNode : oDragHandle.parentElement;
    }

    if (oDragHandle.className == "parent") {
        isdrag = true;
        oDragObj = $(oDragHandle).children().eq(0).get(0);
        svgObj = $(oDragHandle).find("svg").get(0);
        userObj =  $(oDragHandle).children().eq(0).get(0);

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

//鼠标滚动
function onWheelZoom(obj) {  
    $("svg").children("circle").remove();
    // 图片放大
    var zoom = parseFloat($("#imgB").get(0).style.zoom);
    tZoom = zoom + (event.wheelDelta > 0 ? 0.05 : -0.05);
    if (tZoom < 1.4 || tZoom > 5) return true;
    $("#imgB").get(0).style.zoom = tZoom;

    $("svg").attr("width",949*tZoom);
    $("svg").attr("height",805*tZoom);
    
    if(drawag){
    	console.log();
    	draw(data);
    }
    newPainter(tZoom);
    return false;
}



function drawPerson(x,y,zoom,obj,i,col,id){
    var c=document.createElementNS("http://www.w3.org/2000/svg","circle");
    c.setAttribute("cx",x*zoom);
    c.setAttribute("cy",y*zoom);
    c.setAttribute("r",3*zoom);
    c.setAttribute("fill",col);
    c.setAttribute("class","child"+i);
    c.setAttribute("data-id","child"+i);
    c.setAttribute("people-id",id); //人员id 
    obj.appendChild(c);  
}


