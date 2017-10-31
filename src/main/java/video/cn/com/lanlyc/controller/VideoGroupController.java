package video.cn.com.lanlyc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import video.cn.com.lanlyc.controller.BaseController;
import net.sf.json.JSONObject;
import video.cn.com.lanlyc.core.po.Group;
import video.cn.com.lanlyc.core.service.GroupService;

/**
 * 分组Controller
 * @author chenyan
 * @date 2017年9月12日 10:15:23
 * @version v1.0
 */
@Controller
@RequestMapping("/videoGroup")
public class VideoGroupController extends BaseController{

	@Autowired
	private GroupService groupService;

	/* 
	 * 分组管理页面
	 */

	/** 
	 * 1、分组管理列表查询：
	 * 			按条件（是否重点、分屏模式）筛选组列表带分页（不包含初始化数据）
	 * @param	paramJson：{
	 * 						"screen_type":...,	//分屏模式（0代表所有  1代表1*1 2代表2*2 3代表3*3 4代表4*4），注意：js中初始值设置为0	int
	 * 						"is_important":...	//是否是重点分组	（0代表所有  1代表是  2 代表否）注意：js中初始值设置为0			int
	 * 				   	   }
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",		data:组列表 List	
	 * 									draw:draw,	recordsTotal:总记录数,		recordsFiltered:总记录数,		
	 * 							无数据：	code:3001,	message:"暂无数据"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../videoGroup/getGroupListByCondition
	 * @author 	chenyan
	 * @date	2017年9月14日 10:21:48
	 */
	@RequestMapping(value = "/getGroupListByCondition", method = RequestMethod.POST)
	@ResponseBody
	public Response getGroupListByCondition(String paramJson,int draw,int start,int length){
		JSONObject json = JSONObject.fromObject(paramJson);
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		if(json.getInt("screen_type") != 0){
			queryMap.put("screenType", json.getInt("screen_type"));
		}
		if(json.getInt("is_important") != 0){
			queryMap.put("isImportant", json.getInt("is_important"));
		}
		queryMap.put("containInit", 2);//代表不包含初始化组
		int currentPage = start / length + 1;
		try{
			Page<Group> page = new Page<Group>(currentPage);
			page.setPageSize(length);
			Page<Group> data = groupService.getGroupListByCondition(page,queryMap);
			if(data.getTotalRow()>0){
				Response response = Response.OK();
				response.setData(data.getResultRows());
				response.put("recordsTotal", data.getTotalRow());
				response.put("recordsFiltered", data.getTotalRow());
				response.put("draw", draw);
				return response;
			}else{
				Response response = Response.ERROR(3001, "暂无数据");
				response.setData(null);
				response.put("recordsTotal", 0);
				response.put("recordsFiltered", 0);
				response.put("draw", draw);
				return response;
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------
	
	/*
	 * 三、视频直播
	 */

	/** 
	 * 1、通过分屏模式和是否重点获取某种分屏模式下的默认组id（不存在默认组id时获取初始化组id）
	 * @param paramJson：{
	 * 					"screen_type":...,	//分屏模式（1代表1*1 2代表2*2 3代表3*3 4代表4*4）	int
	 * 					"is_important":...	//巡航模式（1重点   2非重点）						int
	 * 				   }
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",		data:默认组ID：group_id
	 * 							无数据：	code:3001,	message:"查询错误，无默认组"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../videoGroup/getDefaultGroupIdByscreenTypeAndIsImportant
	 * @author	chenyan
	 */
	@RequestMapping(value = "/getDefaultGroupIdByscreenTypeAndIsImportant", method = RequestMethod.POST)
	@ResponseBody
	public Response getDefaultGroupIdByscreenTypeAndIsImportant(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getInt("screen_type") == 0){
			return Response.PARAM_ERROR();
		}
		if(json.getInt("is_important") == 0){
			return Response.PARAM_ERROR();
		}
		try{
			String result = groupService.getIndexGroupIdByscreenTypeAndIsImportant(json.getInt("screen_type"),json.getInt("is_important"));
			if(result != null){
				Response response = Response.OK();
				response.setData(result);
				return response;
			}else{
				return Response.ERROR(3001, "查询错误，无默认组");
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}

	/** 
	 * 2、通过分屏模式和是否重点获取某种分屏模式下的所有组列表（不包含初始化数据）
	 * @param	paramJson：{
	 * 					"screen_type":...,	//分屏模式（1代表1*1 2代表2*2 3代表3*3 4代表4*4），注意：js中初始值设置为0	int
	 * 					"is_important":...	//巡航模式（1重点   2非重点）										int
	 * 				   }
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",	data:某种分屏模式下的所有组列表 List
	 * 							无数据：	code:3001,	message:"暂无数据"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../videoGroup/getGroupListByScreenTypeAndIsImportant
	 * @author	chenyan
	 */
	@RequestMapping(value = "/getGroupListByScreenTypeAndIsImportant", method = RequestMethod.POST)
	@ResponseBody
	public Response getGroupListByScreenTypeAndIsImportant(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getInt("screen_type") == 0){
			return Response.PARAM_ERROR();
		}
		if(json.getInt("is_important") == 0){
			return Response.PARAM_ERROR();
		}
		try{
			List<Group> result = groupService.getGroupListByScreenTypeAndIsImportantNoInit(json.getInt("screen_type"), json.getInt("is_important"));
			if(result.size()>0){
				Response response = Response.OK();
				response.setData(result);
				return response;
			}else{
				return Response.ERROR(3001, "暂无数据");
			}
		}catch (Exception e){
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}

}