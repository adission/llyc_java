var vm = new Vue({
    el:".content",
    data:{
        cameraList:'',
        cameraId:''
    },
    methods:{
        inti:function(){

            v.addEventListener('timeupdate', function(){
                nowTime.textContent = parseTime(v.currentTime);
                getProgress();
            })
            //进度条
             var progressWrap = document.getElementById("progressWrap");
             var playProgress = document.getElementById("playProgress");

            function getProgress(){
                var percent = v.currentTime / v.duration;
                playProgress.style.width = percent * (progressWrap.offsetWidth) + "px";
            }

        },

        //获取摄像头列表
        getCameraList:function(){
            var data = {
            		"token": getCookie('token'),
            		"whether_important":2,
            };
            $.post('../vidicon/getAllVidicon',{"paramJson": JSON.stringify(data)},function(ret){
                console.log(ret)
                vm.$data.cameraList = ret.data;
            })
        },
        //选中一个摄像头
        selectCamera:function(id){
            vm.$data.cameraId = id;
            $(event.currentTarget).addClass("active").siblings().removeClass("active");
        },
        //确定条件,请求录像
        sure:function(type){
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            return;
            if(vm.$data.cameraId == ''){
                alert("请先选择摄像头");
                return;
            }
            if(startTime == ''){
                alert("请先选择开始时间");
                return;
            }
            if(endTime == ''){
                alert("请先选择结束时间");
                return;
            }
            var data ={
            		"token": getCookie('token'),
            		"data":'',
            		"start_time":'',
            		"end_time":'',
            		"vidicon_id":'',
            		"ip":'',
            		"port":'',
            		"username":'',
            		"password":'',
            		"lChannel":'',
            		"hWnd":'',
            		"dwControlCode":''
            }
            console.log(data);
            $.post("../video/playBack",{
                "paramJson":JSON.stringify(data)
            },function(ret){
                console.log(ret);
            });
        }




    },



});
vm.getCameraList();


//时间控件初始化
$(".form_datetime").datetimepicker({
    format:    'yyyy/mm/dd HH:ii',
    autoclose:1,
    minuteStep: 5,
    onChangeDateTime: function(dateText, inst) {
        console.log(dateText);
    }
});
