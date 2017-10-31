package gate.cn.com.lanlyc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.DataUtils;
import org.apache.commons.lang.StringUtils;
import cn.com.lanlyc.base.util.ImageSlimUtil;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.KaoqinUtuil;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.GateUserAuthView;
import gate.cn.com.lanlyc.core.po.GateUserClass;
import gate.cn.com.lanlyc.core.po.WorkersTypes;
import gate.cn.com.lanlyc.core.service.FaceService;
import net.sf.json.JSONArray;



@Controller
@RequestMapping("/GateUserAuth")
public class GateUserAuthController extends BaseController {
	/**
	 * 设置闸机权限
	 * 
	 * @param id
	 * @param user_ids 批量人员id "1","2"
	 * @return
	 */
	@RequestMapping("/setAuth")
	@ResponseBody
	public Response setAuth(String gate_id,String user_ids) 
	{
		if(gate_id==null||"".equals(gate_id))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		if(user_ids==null||"".equals(user_ids))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		
		HttpServletRequest request=this.getRequest();
		boolean res=this.getService().getGateuserauthservie().addsomepeopleAuth(gate_id, user_ids, request);

		Response response = Response.OK();
		return response;
	}
	
	/**
	 * 复制闸机权限
	 * 
	 */
	@RequestMapping("/copyAuth")
	@ResponseBody
	public Response copyAuth(String new_gate_id,String ids) 
	{
		if(new_gate_id==null||"".equals(new_gate_id))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		String[] gate_ids=ids.split(",");
		int dt=0;
		
		//获取多个人闸机中的所有不重复的人员信息
		List<String> user_ids=new ArrayList<String>();
		for(String i:gate_ids) 
		{
			List<GateUserAuth> users=this.getService().getGateuserauthservie().getGateUserAuth(i);
			for(GateUserAuth index:users) 
			{
				user_ids.add(index.getUser_id());
			}
		} 
		HashSet all_users  =   new  HashSet(user_ids);
		user_ids.clear();
		user_ids.addAll(all_users);
		String users=StringUtils.join(user_ids.toArray(),",");
		
		//for 循环每个人 然后根据每个人有不同的开卡权限 
		HttpServletRequest request=this.getRequest();
		boolean res=this.getService().getGateuserauthservie().addsomepeopleAuth(new_gate_id, users, request);

		return null;
		
	}
	
	/**
	 * 获取闸机所有人员
	 * 
	 * @return
	 */
	@RequestMapping("/getAllAuth")
	@ResponseBody
	public Response getAllAuth(String paramJson,int length,int start,int draw) 
	{
		if(paramJson==null||"".equals(paramJson))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		JSONObject object = JSONObject.parseObject(paramJson);
		Map<String, Object> paramMap=new HashMap<String, Object>();
		
		if(object.containsKey("mobile")) {
			String mobile=object.getString("mobile");
			if(StringUtils.isNotEmpty(mobile)) 
			{
				paramMap.put("mobile", mobile);
			} 
			
			
		}
		if(object.containsKey("name")) {
			String name=object.getString("name");
			if(StringUtils.isNotEmpty(name)) 
			{
				paramMap.put("name", name);
			} 
			
			
		}
		if(object.containsKey("gate_id")) {
			String gate_id=object.getString("gate_id");
			if(StringUtils.isNotEmpty(gate_id)) 
			{
				paramMap.put("gate_id", gate_id);
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
		Page<GateUserAuthView> page=new Page<GateUserAuthView>(currentPage);
		page.setPageSize(length);
		Response response = Response.OK();
		Page<GateUserAuthView> result=this.getService().getGateuserauthservie().getGateuserauthdao().queryGateAllAuth(page, paramMap);
		
		response.put("recordsTotal", result.getTotalRow());
        response.put("recordsFiltered", result.getTotalRow());
        response.setData(result.getResultRows());
        response.put("recordsFiltered", result.getTotalRow());
        response.put("draw", draw);
        response.put("total", result.getTotalRow());
		
		return response;
		
		
		
	}
	
	/**
	 * 删除某些人员在指定闸机的刷卡权限
	 * @param gate_id  闸机id
	 * @param ids 人员id "1","2"
	 * @return
	 */
	@RequestMapping("/delsomeAuth")
	@ResponseBody
	public Response delsomeAuth(String user_ids,String gate_id) 
	{
		if(user_ids==null||"".equals(user_ids))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		
		boolean res=this.getService().getGateuserauthservie().delSomeUserInGate(user_ids,gate_id);

		System.out.println(res);
		Response response = Response.OK();
		return response;
	}
	
	

	
}
