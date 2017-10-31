var vm = new Vue({
    el:".content",
    data:{
        cameraList:'',
        cameraLength:'',
        screen_type:0,
        is_important:0,
        
        max_num:'', //分屏模式下最多摄像头个数
    },
    methods:{

        //获取摄像头列表
        getCameraList:function(type){       
            vm.$data.is_important = type;
            var data = {
            		"token": getCookie('token'),
            		"whether_important":type,
            };
            $.post('../vidicon/getAllVidicon',{"paramJson": JSON.stringify(data)},function(ret){
                console.log(ret)
                vm.$data.cameraList = ret.data;
                vm.$data.cameraLength = ret.data.length;
            })
        },

        selectFp:function(type){
            //判断是否有摄像头列表
            var len = $("#nestable_list_1 ol").children().length;
            if(len==0){
                alert("请先选择摄像头类型");
                return;
            }
            //换分屏模式时重新请求一次摄像头列表
            vm.getCameraList(vm.$data.is_important);
            //设置分屏模式
            vm.$data.screen_type = type;
            $('.fPright').empty();
            var fp = '<div class="col-md-3">'+
                      '<section class="panel all_screen">'+
                      '<header class="panel-heading">'+
                      '<input type="text" class="form-control input-sm" placeholder="屏名称" />'+
                      '</header>'+
                      '<div class="panel-body" style="height: 180px;overflow: auto">'+
                      '<div class="dd nestable_list" id="nestable_list_100">'+
                      '<div class="dd-empty"></div>'+
                      '</div>'+
                      '</div>'+
                      '</section>'+
                      '</div>';
            switch(type){
                case 1:
                	vm.$data.max_num = 1;
                	
                    for( var i=1; i<= vm.$data.cameraLength;i++ ){
                        $('.fPright').append(fp);
                    }
                    $('div[id^="nestable_list_"]').nestable({
                        group: 1,
                        maxDepth:1
                    })
                    break;
                case 2:
                	vm.$data.max_num = 4;
                	
                	b = vm.$data.cameraLength%4;
                	if(b == 0){
                		num = vm.$data.cameraLength/4; 
                	}else{
                		num = Math.floor(vm.$data.cameraLength/4) +1;
                	}
                    for( var i=1; i<= num; i++ ){
                        $('.fPright').append(fp);
                    }
                    $('div[id^="nestable_list_"]').nestable({
                        group: 1,
                        maxDepth:1
                    });
                    break;
                case 3:
                	vm.$data.max_num = 9;
                	
                	b = vm.$data.cameraLength%9;
                	if(b == 0){
                		num = vm.$data.cameraLength/9; 
                	}else{
                		num = Math.floor(vm.$data.cameraLength/9) +1;
                	}
                    for( var i=1; i<= num; i++ ){
                        $('.fPright').append(fp);
                    }
                    $('div[id^="nestable_list_"]').nestable({
                        group: 1,
                        maxDepth:1
                    });
                    break;
                case 4:
                	vm.$data.max_num = 16;
                	
                	b = vm.$data.cameraLength%16;
                	if(b == 0){
                		num = vm.$data.cameraLength/16; 
                	}else{
                		num = Math.floor(vm.$data.cameraLength/16) +1;
                	}
                    for( var i=1; i<= num; i++ ){
                        $('.fPright').append(fp);
                    }
                    $('div[id^="nestable_list_"]').nestable({
                        group: 1,
                        maxDepth:1
                    })
                    break;
            };
        },

        //添加分组
        addGroup:function(){
        	//判断是否选择了分屏模式
            var $all = $('.all_screen');
            if($all.length == 0){
                alert("请先选择分屏模式");
                return;
            }           
            //判断摄像头是否分组完全
            var len = $("#nestable_list_1 ol").children().length;
            if(len>0){
                alert("请将所有的摄像头完成分组");
                return;
            }
            ////判断分组名称是否为空
            var group_name = $("#group_name").val();
            if(group_name == ''){
                alert("请填写分组名称");
                return;
            }
            ////获取分屏类型,分组类型
            var screen_type = vm.$data.screen_type;
            var is_important = vm.$data.is_important;
            
            var all_screen = {};
            for(var i = 0;i<$all.length;i++){
                all_screen[i+1]={}
                all_screen[i+1]['screen_name'] = $($all[i]).children().find('input').val();
                all_screen[i+1]['all_vidicon'] = {}
                var $ddItem = $($all[i]).children().find('.dd-item');
                for(var j=0; j<$ddItem.length; j++){
                    all_screen[i+1]['all_vidicon'][j+1]=$($ddItem[j]).attr("cid");
                };
            }
            console.log(all_screen);
            for( m in all_screen){
            	console.log(all_screen[m]);
            	all_screen[m]
            	if(all_screen[m]["screen_name"] == ''){
            		alert("请填写第"+m+"屏的屏名称");
            		return;
            	}
            	var vidiconLength = 0;
            	for( n in all_screen[m]["all_vidicon"]){
            		vidiconLength++;
            	}
            
            	if(vidiconLength > screen_type*screen_type){
            		console.log(vidiconLength);
            		console.log( screen_type*screen_type);
            		alert("第"+m+"屏摄像头个数超过"+screen_type*screen_type+"个");
            		return;
            	}
            }

            if(confirm('是否同时设置为默认分组?')){
                var set_default = 1;
            }else{
                var set_default = 2;
            }

            var data = {
            		"token": getCookie('token'),
                    "group_name":group_name, 	//组名称									string
                    "screen_type":screen_type,	//分屏模式（1代表1*1 2代表2*2 3代表3*3 4代表4*4）int
                    "is_important":is_important, //是否设置为重要组（1代表是，2代表否）				int
                    "set_default":set_default, //是否设置为默认组（1代表是，2代表否）				int
                    "all_screen":all_screen
            }
            console.log(data);
            $.post('../groupScreenVidicon/addGroupScreenVidicon',{
                "paramJson":JSON.stringify(data)
            },function(ret){
                console.log(ret);
                if(ret.code == "200"){
                	alert("新增成功");
                }
            });
        },

        //限制拖拽个数
        limitCount: function(type){
            alert(type);
        }
    },

});




//var Nestable = function () {
//
//    $('#nestable_list_100').nestable({
//
//        group: 1
//
//    }).on('change', function(e){
//
//        console.log(123);
//        console.log('dataChange');
//
//    });
//
//    var updateOutput = function (e) {
//        var list = e.length ? e : $(e.target),
//            output = list.data('output');
//        if (window.JSON) {
//            output.val(window.JSON.stringify(list.nestable('serialize'))); //, null, 2));
//        } else {
//            output.val('JSON browser support required for this demo.');
//        }
//    };
//
//
//    // activate Nestable for list 1
//    $('#nestable_list_1').nestable({
//            group: 1,
//            maxDepth:1
//        })
//        .on('change', updateOutput);
//
//    // activate Nestable for list 2
//    $('#nestable_list_2').nestable({
//            group: 1,
//            maxDepth:1
//        })
//        .on('change', updateOutput);
//
//    // activate Nestable for list 3
//    $('#nestable_list_3').nestable({
//            group: 1,
//            maxDepth:1
//        })
//        .on('change', updateOutput);
//
//    $('#nestable_list_4').nestable({
//            group: 1,
//            maxDepth:1
//        })
//        .on('change', updateOutput);
//
//    $('#nestable_list_5').nestable({
//            group: 1,
//            maxDepth:1
//        })
//        .on('change', updateOutput);
//
//    // output initial serialised data
//    updateOutput($('#nestable_list_1').data('output', $('#nestable_list_1_output')));
//    updateOutput($('#nestable_list_2').data('output', $('#nestable_list_2_output')));
//    updateOutput($('#nestable_list_3').data('output', $('#nestable_list_3_output')));
//    updateOutput($('#nestable_list_4').data('output', $('#nestable_list_4_output')));
//    updateOutput($('#nestable_list_5').data('output', $('#nestable_list_5_output')));
//
//
//
//}();