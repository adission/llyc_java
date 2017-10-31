package gate.cn.com.lanlyc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.Setting;
import gate.cn.com.lanlyc.core.service.SettingService;

@Controller
@RequestMapping("/GateSetting")
public class GateSettingController extends BaseController {
	@RequestMapping("/getinfo")
	@ResponseBody
	/**
	 * 获取系统与云端交互设置
	 * 
	 * 访问url GateSetting/getinfo
	 * @param user_name 用户名
	 * @param pass_word 密码
	 * @return
	 */
	public Response getinfo() 
	{
		SettingService  settingservice=this.getService().getSettingservice();
		Setting system_setting=settingservice.getSettingdao().getOneObject();
		Response response = Response.OK();
		response.setData(system_setting);
		return response;
		
	} 
	@RequestMapping("/setting")
	@ResponseBody
	/**
	 * 设置系统与云端交互设置
	 * 
	 * 访问url GateSetting/setting?
	 * @param user_name 用户名
	 * @param pass_word 密码
	 * @return
	 */
	public Response setting(String paramJson) 
	{
		if(paramJson==null||"".equals(paramJson))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		JSONObject object=JSONObject.parseObject(paramJson);
		Setting commit_gate_setting=JSON.parseObject(paramJson, new TypeReference<Setting>() {});
		
		SettingService  settingservice=this.getService().getSettingservice();
		Setting old_system_setting=settingservice.getSettingdao().getOneObject();
		
		
		int res=0;
		if(old_system_setting==null) 
		{
			String id=DataUtils.getUUID();
			commit_gate_setting.setId(id);
			res=settingservice.getSettingdao().save(commit_gate_setting);
		}else 
		{
			commit_gate_setting.setId(old_system_setting.getId());
			res=settingservice.getSettingdao().update(commit_gate_setting);
		}
		if(res==0) 
		{
			 Response response=Response.PARAM_ERROR();
			 response.put("code",300);
			 response.put("message","数据库操作错误,删除未完成");
			 return response;
		}
		Response response = Response.OK();
		return response;
	}
	
	
	
	
}
