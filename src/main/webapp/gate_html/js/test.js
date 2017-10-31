/**
 * Created by jiangyanyan on 2017/8/14.
 */
/**
 * Created by jiangyanyan on 2017/8/12.
 */
var chartData = [{ workers_type: "大木工", number: 52 ,color: "#FF0F00"},
    { workers_type: "木工", number: 82 ,color: "#FF6600"},
    { workers_type: "泥工", number: 9 ,color: "#FF9E01"},
    { workers_type: "瓦工", number: 22 ,color: "#FCD202"},
    { workers_type: "土工", number: 22 ,color: "#F8FF01"},
    { workers_type: "钳工", number: 14 ,color: "#B0DE09"},
    { workers_type: "焊工", number: 4 ,color: "#04D215"},
    { workers_type: "高脚架工", number: 11 ,color: "#0D8ECF"},
    { workers_type: "水工", number: 65 ,color: "#0D52D1"},
    { workers_type: "土木工", number: 80 ,color: "#2A0CD0"},
    { workers_type: "电工", number: 43 ,color: "#8A0CCF"},
    { workers_type: "雨棚工", number: 41 ,color: "#CD0D74"},
    { workers_type: "机电工", number: 95 ,color: "#754DEB"},
    { workers_type: "电焊工", number: 86 ,color: "#DDDDDD"},
    { workers_type: "砌墙工", number: 84 ,color: "#999999"},
    { workers_type: "泥瓦工", number: 38 ,color: "#333333"},
    { workers_type: "其他", number: 28 ,color: "#000000"}];


var chart = new AmCharts.AmSerialChart();
chart.dataProvider = chartData;
chart.categoryField = "workers_type";
chart.angle = 30;
chart.depth3D = 15;
chart.theme = "Dark";  // 主题颜色
chart.startDuration = 1;//动画

var valueAxis = new AmCharts.ValueAxis();
valueAxis.axisColor = "#DADADA";
valueAxis.title = " 工种统计图";
valueAxis.gridAlpha = 0.1;
chart.addValueAxis(valueAxis);

var graph = new AmCharts.AmGraph();
graph.valueField = "number"
graph.type = "column";  // 柱图

graph.balloonText = "[[workers_type]]: <b>[[value]]</b>";
graph.lineAlpha = 0;
graph.lineColor = "#f4fb16";  // 柱状颜色
//graph.lineColor = "#8d1cc6";  // 柱状颜色
graph.fillAlphas = 0.8;
chart.addGraph(graph);

var categoryAxis = chart.categoryAxis;
categoryAxis.autoGridCount  = false;
categoryAxis.gridCount = chartData.length;
categoryAxis.gridPosition = "start";
categoryAxis.labelRotation = 90;
////
////
//chart.marginTop = 15;
//chart.marginLeft = 55;
//chart.marginRight = 15;
//chart.marginBottom = 80;
//




//画连线图-----去除柱状
//        graph.fillAlphas = 0; // 或者删除这一行, 因为0是默认值
//        graph.bullet = "round";
//        graph.lineColor = "#8d1cc6";

chart.write('amChart-column2');



var chart1 = AmCharts.makeChart( "amChart-pie2", {
    "type": "pie",
    "theme": "black",
    "titles": [ {
        "text": "人员类别分布图",
        "size": 16
    } ],
    "legend":{
        "position":"right",
        "marginRight":10,
        "autoMargins":false
    },
    "dataProvider": [ {
        "country": "平台人员",
        "visits": 52
    }, {
        "country": "施工人员",
        "visits": 82
    }, {
        "country": "特殊工人",
        "visits": 9
    }, {
        "country": "临时工",
        "visits": 22
    }, {
        "country": "外包工人",
        "visits": 22
    }, {
        "country": "一般人员",
        "visits": 4
    }, {
        "country": "管理员",
        "visits": 4
    }, {
        "country": "其他",
        "visits": 11
    } ],
    "valueField": "visits",
    "titleField": "country",
    "startEffect": "elastic",
    "startDuration": 2,
    "labelRadius": 15,
    "innerRadius": "50%",
    "depth3D": 10,
    "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
    "angle": 15,
    "export": {
        "enabled": true
    }
});
