package gate.cn.com.lanlyc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.ExcelUtiltest;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.CheckLog;
import gate.cn.com.lanlyc.core.po.GateCheckLogView;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;

@Controller
@RequestMapping("/GateCheckLog")
public class CheckLogController extends BaseController {
	/**
	 * 获取指定条件的的刷卡记录
	 * 访问url GateCheckLog/getCheckLog?paramJson={"name":"扬威1","id_card":"420621199408257725","card_no":"1234567890","start_time":"2017-09-04 10:00:00","end_time":"2017-09-04 18:00:00"}&length=10&start=0
	 * 
	 * @param paramJson
	 * 
	 * @param length
	 *            
	 * @param start
	 *            
	 */
	@RequestMapping("/getCheckLog")
	@ResponseBody
	public Response getCheckLog(String paramJson,int length,int start) {
		JSONObject object=JSONObject.parseObject(paramJson);
		Map<String, Object> paramMap=new HashMap<String, Object>();
		if(object.containsKey("name")) {
			String name=object.getString("name");
			if(StringUtils.isNotEmpty(name)) 
			{
				paramMap.put("name", name);
			} 
		}
		if(object.containsKey("user_id")) {
			String user_id=object.getString("user_id");
			if(StringUtils.isNotEmpty(user_id)) 
			{
				paramMap.put("user_id", user_id);
			} 
		}
		if(object.containsKey("id_card")) {
			String id_card=object.getString("id_card");
			if(StringUtils.isNotEmpty(id_card)) 
			{
				paramMap.put("id_card", id_card);
			} 
		}
		if(object.containsKey("mobile")) {
			String mobile=object.getString("mobile");
			if(StringUtils.isNotEmpty(mobile)) 
			{
				paramMap.put("mobile", mobile);
			} 
		}
		if(object.containsKey("card_no")) {
			String card_no=object.getString("card_no");
			if(StringUtils.isNotEmpty(card_no)) 
			{
				paramMap.put("card_no", card_no);
			} 
		}
		if(object.containsKey("start_time")) {
			String start_time=object.getString("start_time");
			if(StringUtils.isNotEmpty(start_time)) 
			{
				paramMap.put("start_time", start_time);
			} 
		}
		if(object.containsKey("end_time")) {
			String end_time=object.getString("end_time");
			if(StringUtils.isNotEmpty(end_time)) 
			{
				paramMap.put("end_time", end_time);
			} 
		}
		int currentPage=start/length+1;
		Page<GateCheckLogView> page=new Page<GateCheckLogView>(currentPage);
		page.setPageSize(length);
		Page<GateCheckLogView> result=this.getService().getChecklogservice().getCheckLogDao().queryCheckLog(page,paramMap);
		Response response=Response.OK();
		response.put("recordsTotal", result.getTotalRow());
        response.put("recordsFiltered", result.getTotalRow());
        response.setData(result.getResultRows());
        response.put("recordsFiltered", result.getTotalRow());
        response.put("total", result.getTotalRow());
		return response;
	}
	
	
	/**
	 * 获取指定条件的的刷卡记录
	 * 访问url GateCheckLog/getCheckLog?paramJson={"name":"扬威1","id_card":"420621199408257725","card_no":"1234567890","start_time":"2017-09-04 10:00:00","end_time":"2017-09-04 18:00:00"}&length=10&start=0
	 * 
	 * @param paramJson
	 * 
	 * @param length
	 *            
	 * @param start
	 *            
	 */
	@RequestMapping("/getCheckLogExcel")
	@ResponseBody
	public void getCheckLogExcel(HttpServletRequest request, HttpServletResponse response,String paramJson) {
		JSONObject object=JSONObject.parseObject(paramJson);
		Map<String, Object> paramMap=new HashMap<String, Object>();
		if(object.containsKey("name")) {
			String name=object.getString("name");
			if(StringUtils.isNotEmpty(name)) 
			{
				paramMap.put("name", name);
			} 
		}
		if(object.containsKey("user_id")) {
			String user_id=object.getString("user_id");
			if(StringUtils.isNotEmpty(user_id)) 
			{
				paramMap.put("user_id", user_id);
			} 
		}
		if(object.containsKey("id_card")) {
			String id_card=object.getString("id_card");
			if(StringUtils.isNotEmpty(id_card)) 
			{
				paramMap.put("id_card", id_card);
			} 
		}
		if(object.containsKey("mobile")) {
			String mobile=object.getString("mobile");
			if(StringUtils.isNotEmpty(mobile)) 
			{
				paramMap.put("mobile", mobile);
			} 
		}
		if(object.containsKey("card_no")) {
			String card_no=object.getString("card_no");
			if(StringUtils.isNotEmpty(card_no)) 
			{
				paramMap.put("card_no", card_no);
			} 
		}
		if(object.containsKey("start_time")) {
			String start_time=object.getString("start_time");
			if(StringUtils.isNotEmpty(start_time)) 
			{
				paramMap.put("start_time", start_time);
			} 
		}
		if(object.containsKey("end_time")) {
			String end_time=object.getString("end_time");
			if(StringUtils.isNotEmpty(end_time)) 
			{
				paramMap.put("end_time", end_time);
			} 
		}
		List<GateCheckLogView> page=this.getService().getChecklogservice().getCheckLogDao().queryCheckLogExcel(paramMap);
		
		String[] headers = { "姓名","性别","工种", "人员类别","考勤类型", "考勤时间","行为", "考勤卡号","设备号","身份证","电话","班组名称","惩罚次数","是否上报"};
		
		
		List<List> dataset = new ArrayList<List>();
		for (GateCheckLogView user_info:page) {
			dataset.add(new ArrayList() {
				{
					add(user_info.getName()!=null?user_info.getName():"");
					
					add(user_info.getGender()!=null?user_info.getGender():"");
					add(user_info.getWorker_type()!=null?user_info.getWorker_type():"");
					add(user_info.getClass_name()!=null?user_info.getClass_name():"");
					add(user_info.getType()!=null?user_info.getType():"");

					add(user_info.getCheck_date()!=null?user_info.getCheck_date():"");
					add(user_info.getCross_flag()!=null?user_info.getCross_flag():"");
					add(user_info.getCard_no()!=null?user_info.getCard_no():"");
					add(user_info.getGate_no()!=null?user_info.getGate_no():"");
					add(user_info.getId_card()!=null?user_info.getId_card():"");
					add(user_info.getMobile()!=null?user_info.getMobile():"");
					
					add(user_info.getTeam()!=null?user_info.getTeam():"");
					add(user_info.getPunish_record()!=null?user_info.getPunish_record():"");
					add(user_info.getIs_shangbao()!=null?user_info.getIs_shangbao():"");

					
					
				}
			});
		}
		ExcelUtiltest.Test(request, response, headers, dataset); 

	}

}
