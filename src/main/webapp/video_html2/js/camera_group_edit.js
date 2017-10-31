var vm = new Vue({
    el: ".content",
    data: {
        all_screen: '',
        group_name: '',
        screen_type: '',
        is_important: '',
        old_set_default: '',
        
        group_id:''
    },
    methods: {
        index: function () {
        	var id = window.location.href.split("/camera_group_edit.html?")[1];
        	vm.$data.group_id = id;
            var data = {
            		"token": getCookie('token'),
            		"group_id":id,
            };
            console.log(data);
            $.post('../groupScreenVidicon/getGroupScreenVidiconByGroupId',{
            	"paramJson": JSON.stringify(data)
            }, function (ret) {
                console.log(ret);
                console.log(ret.data);
                $('#group_name').val(ret.data.group_name);
                vm.$data.group_name = ret.data.group_name;
                vm.$data.screen_type = ret.data.screen_type;
                vm.$data.is_important = ret.data.is_important;
                vm.$data.old_set_default = ret.data.set_default;
                vm.$data.all_screen = ret.data.all_screen;

                vm.mounted();
            });
        },
        
        mounted: function(){
            this.$nextTick(function () {
                $('div[id^="nestable_list_"]').nestable({
                    group: 1,
                    maxDepth: 1
                })
            })
        },
        
        //添加分组
        editGroup:function(){        
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
          
        	//判断是否选择了分屏模式
            var screen_type = vm.$data.screen_type;
            var $all = $('.all_screen'); 
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
            		"group_id":vm.$data.group_id, 	//组id
                    "group_name":group_name, 	//组名称									string
                    "screen_type":vm.$data.screen_type,	//分屏模式（1代表1*1 2代表2*2 3代表3*3 4代表4*4）int
                    "is_important":vm.$data.is_important, //是否设置为重要组（1代表是，2代表否）				int
                    "set_default":set_default,   //是否设置为默认组（1代表是，2代表否）	
                    "all_screen":all_screen,
                    "old_set_default":vm.$data.old_set_default
            }
            console.log(data);
            $.post('../groupScreenVidicon/updateGroupScreenVidiconByGroupId',{
                "paramJson":JSON.stringify(data)
            },function(ret){
                console.log(ret);
                if(ret.code == "200"){
                	alert("编辑成功");
                }
            });
        },
        
    }
});
vm.index();




