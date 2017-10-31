/**
 * Created by wangyanling on 2017/8/12.
 */
    $(function(){
        showLeft();
        showHeader();
        getData();
    });
    var workertype,userClass;
    // 去随机色
    function rc(){
        var r=Math.floor(Math.random()*256);
        var g=Math.floor(Math.random()*160);
        var b=Math.floor(Math.random()*160);
        return "rgb("+r+","+g+","+b+")";
    }
    
    
    
    function getData(){
    	workertype=[],userClass=[];
    	$.post(urlpath+"DataAnalysisView/getotalperson",{
    		"token": getCookie('token')
    	},function(res){
    		if(res.code==200){
    			workertype = res.data.Worktyps_totalperson;
    			userClass = res.data.UserClass_totalperson;
    		}
    		for(var i=0;i<workertype.length;i++){
    			workertype[i].color=rc();
    		}
    		
    		createLink(workertype);
    		
    		var chart1 = AmCharts.makeChart( "amChart-pie1", {
    		    "type": "pie",
    		    "theme": "black",
    		    "titles": [ {
    		        "text": "人员类别分布图",
    		        "size": 16
    		    } ],
    		    "legend":{
    		        "position":"right",
    		        "marginRight":30,
    		        "autoMargins":false
    		    },
    		    "dataProvider": userClass,
    		    "valueField": "total_person",
    		    "titleField": "name",
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
    	});
    }

    // 画柱状图
    function createLink(workertype){
    	
	    var chart = new AmCharts.AmSerialChart();
	    chart.dataProvider = workertype;
	    chart.categoryField = "name";
	    chart.angle = 30;
	    chart.depth3D = 15;

	    chart.startDuration = 1;//动画

	    var valueAxis = new AmCharts.ValueAxis();
	    valueAxis.axisColor = "#DADADA";
	    valueAxis.title = " 工种统计图";
	    valueAxis.gridAlpha = 0.1;
	    chart.addValueAxis(valueAxis);

	    var graph = new AmCharts.AmGraph();
	    graph.valueField = "total_person"
	    graph.type = "column";  // 柱图
	    graph.balloonText = "[[name]]: <b>[[value]]</b>";
	    graph.lineAlpha = 0;
	    graph.fillColorsField="color"; // 柱体颜色取值
//	    graph.lineColor = "#8d1cc6";  // 柱状颜色
	    graph.fillAlphas = 0.8;
	    chart.addGraph(graph);

	    var categoryAxis = chart.categoryAxis;
	    categoryAxis.autoGridCount  = false;
	    categoryAxis.gridCount = workertype.length;
	    categoryAxis.gridPosition = "start";
	    categoryAxis.labelRotation = 90;

	    chart.write('amChart-column');
    }
   
    
  

