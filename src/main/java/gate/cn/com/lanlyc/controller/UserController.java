package gate.cn.com.lanlyc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.DateUtils;
import cn.com.lanlyc.base.util.ExcelUtiltest;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.dto.AdminUserDto;
import gate.cn.com.lanlyc.core.po.AdminUser;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import gate.cn.com.lanlyc.core.service.AdminUserService;
import net.sf.json.JSONArray;

@Controller
@RequestMapping("/User")
public class UserController extends BaseController{


	@RequestMapping("/add")
	@ResponseBody
	/**
	 * @api {post} /user/:paramJson 新增管理员
	 * @apiName add
	 * @apiGroup User
	 *
	 * @apiParam {String} paramJson Users unique ID.
	 *
	 * @apiSuccess {String} firstname Firstname of the User.
	 * @apiSuccess {String} lastname  Lastname of the User.
	 *
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 200 OK
	 *     {
	 *       "firstname": "John",
	 *       "lastname": "Doe"
	 *     }
	 *
	 * @apiError UserNotFound The id of the User was not found.
	 *
	 * @apiErrorExample Error-Response:
 	 *	  {
	 *       "code"	:"",
	 *       "message": "请登录"
	 *     }
	 */
	public Response add(String paramJson) {
		JSONObject object = JSONObject.parseObject(paramJson);
		if (!object.containsKey("user_name")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String user_name=object.getString("user_name");
		String pass_word=object.getString("pass_word");
		String mobile=object.getString("mobile");
		String image=object.getString("image");
		
		AdminUser flag=this.getService().getAdminuserService().getAdminuserdao().checkUsernameAndMobile("",user_name, mobile);
		if(flag!=null) 
		{
			if(flag.getUser_name().equals(user_name)) {
				Response response = Response.ERROR(-1,"用户名已存在");
				//System.out.println("用户名已存在");
				return response;
			}
			if(flag.getMobile().equals(mobile)) {
				Response response = Response.ERROR(-1,"手机号已存在");
				//System.out.println("手机号已存在");
				return response;
			}
		}
		
		Response response = Response.OK();
		AdminUser dt=new AdminUser();
		String id=DataUtils.getUUID();
		String create_time=DateUtils.getCurrenTimeStamp();

		dt.setId(id);
		dt.setMobile(mobile);
		dt.setImage(image);
		dt.setUser_name(user_name);
		dt.setCreate_time(create_time);
		dt.setPass_word(pass_word);
		dt.setLast_login_time("");
		this.getService().getAdminUserService().getAdminuserdao().save(dt);
		this.addUserOperationlog("新增管理员："+user_name);
		return response;

	}
	
	@RequestMapping("/getuserinfo")
	@ResponseBody
	/**
	 * 通过用户名查询管理员
	 * 访问url User/getuserinfo?user_name=liyangfu1
	 * @param username 用户名
	 * @return JSON
	 */
	public Response getuserinfo(String id) 
	{
		if(id==null||"".equals(id))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		AdminUser dt=this.getService().getAdminUserService().getAdminuserdao().get(id);
		
		Response response = Response.OK();
	   response.put("data", dt);	
	   return response;
		
	}
	/**
	 * 获取所有管理员列表
	 *  访问url User/list?paramJson={"length":"10","start":"0","draw":"0"}
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Response getuserlist(String paramJson,int length,int start,int draw) 
	{
		JSONObject object=JSONObject.parseObject(paramJson);
		Map<String, Object> paramMap=new HashMap<String, Object>();
		
		if(object.containsKey("user_name")) {
			String username=object.getString("user_name");
			if(StringUtils.isNotEmpty(username)) 
			{
				paramMap.put("user_name", username);
			} 
			
			
		}
		System.out.println("当前登录用户id "+this.getCurrentuserid());
		if(object.containsKey("mobile")) {
			String mobile=object.getString("mobile");
			if(StringUtils.isNotEmpty(mobile)) 
			{
				paramMap.put("mobile", mobile);
			} 
			
			
		}

		
		String orderColumn="";
		String orderDir="";
		JSONArray filterColumn=null;
		JSONArray filter=null;
		if(object.containsKey("orderColumn")) {
			orderColumn=object.getString("orderColumn");
		}
		if(object.containsKey("orderDir")) {
			orderDir=object.getString("orderDir");
		}
		int currentPage=start/length+1;
		Page<AdminUser> page=new Page<AdminUser>(currentPage);
		page.setPageSize(length);
		Page<AdminUser> result=this.getService().getAdminuserService().getAdminuserdao().getAdminList(page, paramMap, orderColumn, orderDir);
		Response response=Response.OK();
		response.put("recordsTotal", result.getTotalRow());
        response.put("recordsFiltered", result.getTotalRow());
        response.setData(result.getResultRows());
        response.put("recordsFiltered", result.getTotalRow());
        response.put("draw", draw);
        response.put("total", result.getTotalRow());
		return response;
		

		
	}
	
	
	/**
	 * 获取所有管理员列表EXCEL
	 *  访问url User/list?paramJson={"length":"10","start":"0","draw":"0"}
	 * 
	 * @return
	 */
	@RequestMapping("/listExcel")
	@ResponseBody
	public void getuserlist(HttpServletRequest request, HttpServletResponse response,String paramJson) 
	{
		JSONObject object=JSONObject.parseObject(paramJson);
		Map<String, Object> paramMap=new HashMap<String, Object>();
		
		if(object.containsKey("user_name")) {
			String username=object.getString("user_name");
			if(StringUtils.isNotEmpty(username)) 
			{
				paramMap.put("user_name", username);
			} 
			
			
		}
		System.out.println("当前登录用户id "+this.getCurrentuserid());
		if(object.containsKey("mobile")) {
			String mobile=object.getString("mobile");
			if(StringUtils.isNotEmpty(mobile)) 
			{
				paramMap.put("mobile", mobile);
			} 
			
			
		}

		
		String orderColumn="";
		String orderDir="";
		JSONArray filterColumn=null;
		JSONArray filter=null;
		if(object.containsKey("orderColumn")) {
			orderColumn=object.getString("orderColumn");
		}
		if(object.containsKey("orderDir")) {
			orderDir=object.getString("orderDir");
		}
		List<AdminUser> page=this.getService().getAdminuserService().getAdminuserdao().getAdminListExcel(paramMap, orderColumn, orderDir);
		String[] headers = { "用户名", "手机号", "创建时间", "最近登录"};

		List<List> dataset = new ArrayList<List>();
		for (AdminUser ad:page) {
			dataset.add(new ArrayList() {
				{
					add(ad.getUser_name()!=null?ad.getUser_name():"");
					add(ad.getMobile()!=null?ad.getMobile():"");
					add(ad.getCreate_time()!=null?ad.getCreate_time():"");
					add(ad.getLast_login_time()!=null?ad.getLast_login_time():"");
					
				}
			});
		}
		ExcelUtiltest.Test(request, response, headers, dataset); 

		
	}
	
	
	
	/**
	 * 通过id 修改某一个管理员
	 *  访问url User/updateuser?id=f5db32a8ef8642e688407ff68de4373c&user_name=li&pass_word=123456
	 * 
	 * @param id
	 * @param user_name
	 * @param pass_word
	 * @return
	 */
	
	@RequestMapping("/updateuser")
	@ResponseBody
	public Response updateuser(String paramJson) 
	{
		JSONObject object=JSONObject.parseObject(paramJson);
		Map<String, Object> paramMap=new HashMap<String, Object>();
		List<AdminUser> au=this.getService().getAdminuserService().getAdminuserdao().getAll();
		String user_name="";
		if(object.containsKey("user_name")) {
			user_name=object.getString("user_name");
		}
		String id="";
		if(object.containsKey("id")) {
			id=object.getString("id");
		}
		String pass_word="";
		
		if(object.containsKey("pass_word")) {
			pass_word=object.getString("pass_word");
		}
		String mobile="";
		if(object.containsKey("mobile")) {
			mobile=object.getString("mobile");
		}
		String imgae="";
		if(object.containsKey("image")) {
			imgae=object.getString("image");
		}
		AdminUser dt=new AdminUser();
		
		dt.setId(id);
		dt.setUser_name(user_name);
		dt.setPass_word(pass_word);
		dt.setMobile(mobile);
		dt.setImage(imgae);
		
		AdminUser flag1=this.getService().getAdminUserService().getAdminuserdao().checkUsernameAndMobile(id, "", mobile);
		AdminUser flag2=this.getService().getAdminUserService().getAdminuserdao().checkUsernameAndMobile(id, user_name, "");
		if(flag1!=null) {
			Response response = Response.ERROR(-1,"手机号已存在");
			//System.out.println("手机号已存在");
			return response;
		}
		if(flag2!=null) {
			Response response = Response.ERROR(-1,"用户名已存在");
			//System.out.println("用户名已存在");
			return response;
		}
		
		StringBuffer res=new StringBuffer("修改管理员：");
		int dtres=this.getService().getAdminUserService().getAdminuserdao().update(dt,false);
		try {
			AdminUser adminUser=this.getService().getAdminUserService().getAdminuserdao().get(id);
			res.append(adminUser.getUser_name());
		} catch (Exception e) {
			
		}
		if(dtres==0){
			   Response response=Response.PARAM_ERROR();
			   response.put("code",300);
			   response.put("message","数据库操作错误,删除未完成");
			   return response;
			}
		Response response = Response.OK();
		this.addUserOperationlog(res.toString());
		return response;
		
	}
	/**
	 * 删除一个管理员
	 *访问url User/deleteuser?id=f5db32a8ef8642e688407ff68de4373c
	 * 
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteuser")
	@ResponseBody
	public Response deleteuser(String id) 
	{
		if(id==null||"".equals(id))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		String[] ids=id.split(",");
		int dt=0;
		StringBuffer del=new StringBuffer("删除管理员：");
		for(String i:ids) 
		{
			try {
				if(!i.equals(this.getCurrentuserid())) {
					AdminUser adminUser=this.getService().getAdminuserService().getAdminuserdao().get(i);
					del.append(adminUser.getUser_name()+" ");
					dt=this.getService().getAdminUserService().getAdminuserdao().delete(i);
				}		
			} catch (Exception e) {
			}		
		}
		if(dt==0){
			   Response response=Response.PARAM_ERROR();
			   response.put("code",300);
			   System.out.println("删除失败");
			   response.put("message","数据库操作错误,删除未完成");
			   return response;
			}
		else {
			Response response = Response.OK();
			this.addUserOperationlog(del.toString());
			return response;
		}
	}
	
	@RequestMapping("/login")
	@ResponseBody
	/**
	 * 通过用户名和密码
	 * 访问url User/testadmin?user_name=yangwei&pass_word=123
	 * @param username 用户名
	 * @param pass_word	 密码
	 * @return JSON
	 */
	public Response login(String user_name,String pass_word) 
	{
		
		
		
		//后面下面这个代码报错 空指针异常 
		AdminUserService auserive=this.getService().getAdminuserService();
		Map ref=auserive.testadmin(user_name,pass_word);
		Response response;
		if (ref.containsKey("id")) {
			String operation_action = "/User/login";//操作行为
			String operation_desc = "登陆";//操作描述
//			String operation_user = this.getCurrentuserid();//操作人id、
			String operation_user=(String) ref.get("id");
			this.getService().getOperationlogService().addlog(operation_action, operation_desc, operation_user);
			
			response = Response.OK();
			response.put("data", ref);
		}else {
			response = Response.ACCOUNT_PASS_ERROR();
		}
	   return response;
		
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	/**
	 * 退出登录
	 * 
	 * @param token
	 * @return
	 */
	public Response logout(String token) 
	{
		String user_id=this.getService().getAdminUserService().getAdminuserdao().getUserInfoByToken(token);
		AdminUser dt=new AdminUser();
		
		dt.setId(user_id);
		dt.setToken(user_id+"logout");
		this.getService().getAdminUserService().getAdminuserdao().update(dt,false);
		
		Response response=Response.OK();
		return response;
	}
	
}
