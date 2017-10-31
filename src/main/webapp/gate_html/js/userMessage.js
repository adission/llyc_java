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
	    categoryAxis.gridCount = chartData.length;
	    categoryAxis.gridPosition = "start";
	    categoryAxis.labelRotation = 90;

	    chart.write('amChart-column');
    }
    
    var chart1;
    var chartType = "pie";//柱状图
    var chartData1 = [{
        year: "平台人员",
        income: 50
    }, {
        "year": "管理人员",
        income: 3
    }, {
        year: "施工人员",
        income: 30
    }, {
        year: "临时工",
        income: 29
    }, {
        year: "其他",
        income: 5
    }];

    $(document).ready(function() {

        createPieChart();
    });
    //创建柱状图
    function createChart(){
        // SERIAL CHART
        chart1 = new AmCharts.AmSerialChart();
        chart1.dataProvider = chartData1;
        chart1.categoryField = "year";
        // this single line makes the chart a bar chart,
        // try to set it to false - your bars will turn to columns
        chart1.rotate = true;
        // the following two lines makes chart 3D
        chart1.depth3D = 20;
        chart1.angle = 30;
        chart1.startDuration=1;

        // AXES
        // Category
        var categoryAxis = chart.categoryAxis;
        categoryAxis.gridPosition = "start";
        categoryAxis.axisColor = "#DADADA";
        categoryAxis.fillAlpha = 1;
        categoryAxis.gridAlpha = 0;
        categoryAxis.fillColor = "#FAFAFA";

        // value
        var valueAxis = new AmCharts.ValueAxis();
        valueAxis.axisColor = "#DADADA";
        valueAxis.title = " xx统计图";
        valueAxis.gridAlpha = 0.1;
        chart.addValueAxis(valueAxis);

        // GRAPH
        var graph = new AmCharts.AmGraph();
        graph.title = "Income";
        graph.valueField = "income";
        graph.type = "column";
        //graph.balloonText = "Income in [[category]]:[[value]]";
        graph.balloonText = "Income in [[category]]:[[value]]";
        graph.lineAlpha = 0;
        graph.fillColors = "#bf1c25";
        graph.fillAlphas = 1;
        chart1.addGraph(graph);

        // WRITE
        chart1.write("amChart-pie");
    }
    //创建饼图
    function createPieChart(){
        chart1 = new AmCharts.AmPieChart();
        chart1.dataProvider = chartData1;
        chart1.titleField = "year";

        chart1.valueField = "income";
        chart1.outlineColor = "#FFFFFF";
        chart1.labelsEnabled = true;
        chart1.outlineAlpha = 0.8;
        chart1.outlineThickness = 2;
        chart1.depth3D = 10;
        chart1.creditsPosition = "right";

        legend = new AmCharts.AmLegend();
        legend.align = "center";
        chart1.addLegend(legend);

        chart1.write("amChart-pie1");
    }
    //图形转换
    function switchChartType(){
        $("#amChart-pie1").html("");
        if(chartType=="column"){
            createPieChart();
            chartType = "pie";
        }else{
            createChart();
            chartType = "column";
        }

    }
