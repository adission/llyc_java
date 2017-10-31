/**
 * 登录页面，通过用户名，密码登录，登录成功后保存token，用户名和用户id
 */
 
 $(function () {
     $('form').bootstrapValidator({
         message: 'This value is not valid',
         feedbackIcons: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
             username: {
                 message: '用户名验证失败',
                 validators: {
                     notEmpty: {
                         message: '用户名不能为空'
                     }
                 }
             },
             password: {
                 message:'密码无效',
                 validators: {
                     notEmpty: {
                         message: '密码不能为空'
                     }
                 }
             },
         }

     }).on('success.form.bv', function(e) {//点击提交之后
         e.preventDefault();
         
         
         var name = $('input[name="username"]').val();
         var pass = $('input[name="password"]').val();

         $.post(urlpath+'User/login',{"user_name": name,"pass_word": pass},function(res){
     		if(res.code==200){
     			setCookie("token",res.data.token);
     			setCookie("id",res.data.id);
     			setCookie("user_name",res.data.user_name);
     			setCookie("img",res.data.image);
     			location.href="recordList.html";
     		}else {
     			$('p.text-danger').show();
     		}
         });
     });
 });
 
 $('input').focus(function(){
	 $('p.text-danger').hide();
 });
 