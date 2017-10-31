/**
 * Created by wangyanling on 2017/8/24.
 */
$(function(){
    showHeader();

    var Accordion = function(el, multiple) {
        this.el = el || {};
        this.multiple = multiple || false;

        var links = this.el.find('.link');
        links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
    };
    Accordion.prototype.dropdown = function(e) {
        var $el = e.data.el;
        $this = $(this);

        if($this.parent().children().length==1){
            $this.after('<ul class="submenu smAndxs">' +
                '<li><div class="btn-group">' +
                '<button type="button" class="btn btn-default">轨迹回放</button>' +
                '<button type="button" class="btn btn-default">定位跟踪</button>' +
                '</div></li></ul>');
        }
        var $next = $this.next();

        $next.slideToggle();
        $this.parent().toggleClass('open');

        if (!e.data.multiple) {
            $el.find('.submenu').not($next).slideUp().parent().removeClass('open');
        };
    };
    var accordion = new Accordion($('#accordion'), false);
    
    newPainter(tZoom);
    painting(tZoom);
    draw();
});


$("#start").click(function(){
    var obj = $("#svg").get(0);
    var tZoom = $('.dragAble img').get(0).style.zoom;
    console.log(tZoom);
    $('#svg').children().remove();
    moveToPoint(tZoom,obj);
});

$("#stop").click(function(){
    clearInterval(id)
});
var tZoom = $("imgB").attr("zoom");

var graph = new joint.dia.Graph();
var paper = new joint.dia.Paper({
    el: $('#papar'),
    width: 949*tZoom,
    height: 805*tZoom,
    model: graph,
    gridSize: 1
});

// 画人所在位置
function draw(){
    var obj = $("svg").get(0);
    var tZoom = $('.dragAble img').get(0).style.zoom;
    for(var i=0;i<array.length;i++){
        drawPerson(array[i].x,array[i].y,tZoom,obj,i);
    }
}

// 点击人员详情显示
$('svg').on("click","circle",function(){
    $(this).get(0).style.fill="#ffff00";
    $(this).siblings().css("fill","ff0000");
    $('#userDetail .panel-heading').html($(this).attr("data-id"));

    var t,l;
    if(parseInt($(this).get(0).style.cy)<=210){
        t= 210;
    }else {
        t= parseInt($(this).get(0).style.cy)
    }
    if(parseInt($(this).parent().parent().width())-parseInt($(this).get(0).style.cx)<=380){
        l= parseInt($(this).get(0).style.cx)-420;
    }else {
        l= parseInt($(this).get(0).style.cx)
    }

    $('#userDetail').css({"top": t-210+"px","left": l+20+"px"});
    $('.userModal').fadeIn();
});

$(".userModal").click(function(e) {
    if($("#userDetail").is("show")) {
        e ? e.stopPropagation() : event.cancelBubble = true;
    }
});
$("#userDetail").click(function(e) {
    e ? e.stopPropagation() : event.cancelBubble = true;
});
$(".userModal").click(function() {
    $(".userModal").fadeOut();
    $("svg").children().css("fill","ff0000");
});













