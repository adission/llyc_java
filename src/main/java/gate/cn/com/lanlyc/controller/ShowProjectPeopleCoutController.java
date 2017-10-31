package gate.cn.com.lanlyc.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.ShowProjectPeopleCout;


@Controller
@RequestMapping("/GateShowProject")
public class ShowProjectPeopleCoutController extends BaseController {
	

	/**
     * 通过区域id得到人员信息
     */
	@RequestMapping("/getcout")
	@ResponseBody
	public Response getShowProjectPeopleCount(String area) {
		if(StringUtils.isEmpty(area)) {
			Response response=Response.PARAM_ERROR();
			return response;
		}
		ShowProjectPeopleCout res= this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao().getCoutByArea(area);
		Response response=Response.OK();
		response.setData(res);
		return response;	
	}
	
	/**
     * 
     */
	@RequestMapping("/getlastenterperson")
	@ResponseBody
	public Response getlast_enter_person(String area) {
		if(StringUtils.isEmpty(area)) {
			Response response=Response.PARAM_ERROR();
			return response;
		}
		ShowProjectPeopleCout count= this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao().getCoutByArea(area);
		if(count!=null) 
		{
			String last_person=count.getLast_enter_person();
			if(!StringUtils.isEmpty(last_person)) {
				GateUser gUser=this.getService().getGateuserservice().getGateuser().get(last_person);
				Response response=Response.OK();
				response.setData(gUser);
				return response;
			}
		}else 
		{
			Response response=Response.OK();
			response.setData(null);
			return response;
		}
	
		Response response=Response.PARAM_ERROR();
		return response;
	}
	
	
	/**
     * 清除区域数据
     */
	@RequestMapping("/cleardata")
	@ResponseBody
	public Response clearData(String area) {
		if(StringUtils.isEmpty(area)) {
			Response response=Response.PARAM_ERROR();
			return response;
		}
		ShowProjectPeopleCout count= this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao().getCoutByArea(area);
		String worktypebefore=count.getWorker_type_person();
		List<Map<String, Object>> wt=new ArrayList<Map<String, Object>>();
		if(!StringUtils.isEmpty(worktypebefore)) 
		{
			JSONArray object=JSONArray.parseArray(worktypebefore);
			for(int i=0;i<object.size();i++) {
				JSONObject temp=object.getJSONObject(i);
				Map<String ,Object > jsonMap = new HashMap< String , Object>();
				jsonMap.put("type_value",temp.get("type_value"));
				jsonMap.put("type_name",temp.get("type_name"));
				jsonMap.put("type_count",0);
				wt.add(jsonMap);
			}
		}
		String worktype_person = JSONObject.toJSONString(wt);
		String class_type_person=count.getClass_type_person();
		List<Map<String, Object>> wt2=new ArrayList<Map<String, Object>>();
		if(!StringUtils.isEmpty(class_type_person)) 
		{
			JSONArray object=JSONArray.parseArray(class_type_person);
			for(int i=0;i<object.size();i++) {
				JSONObject temp=object.getJSONObject(i);
				Map<String ,Object > jsonMap = new HashMap< String , Object>();
				jsonMap.put("type_value",temp.get("type_value"));
				jsonMap.put("type_name",temp.get("type_name"));
				jsonMap.put("type_count",0);
				wt2.add(jsonMap);
			}
		}
		
		
		class_type_person = JSONObject.toJSONString(wt2);
		this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao().clear(worktype_person,class_type_person, area);
		Response response=Response.OK();
		return response;
	}


}
