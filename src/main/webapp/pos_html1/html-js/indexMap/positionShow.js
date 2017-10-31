/**
 * 接收人员最新位置
 */
 var posws;
function openpossocket(){
	
    posws = new WebSocket("ws://localhost:8080/lanlyc/userposserver");
    posws.onopen = function(evn){
    	posws.send("open_22561d26b50542d38bc8c68ff5a761a5");
        console.log(evn);
    };
    posws.onmessage = function(evn){
        var data = JSON.parse(evn.data);
        var resdata = data.data;
//        console.log(resdata);
        addUserFea1(resdata,"user",userSource);
        
    };
//    ws.onclose = function(){
//        console.log("关闭");
//    };
    
};
function subsend(msg){
    
    posws.send("switcharea_"+msg);
    document.getElementById("msg").value = "switcharea_"+msg;
    
}
openpossocket();