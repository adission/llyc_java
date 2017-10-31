package video.cn.com.lanlyc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.controller.BaseController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import video.cn.com.lanlyc.core.dto.GroupScreenDto;
import video.cn.com.lanlyc.core.dto.ScreenVidiconDto;
import video.cn.com.lanlyc.core.po.VidiconInfo;
import video.cn.com.lanlyc.core.service.GroupScreenService;
import video.cn.com.lanlyc.core.service.ScreenVidiconService;
import video.cn.com.lanlyc.core.service.GroupScreenVidiconService;

/**
 * 组、屏、摄像机关联Controller
 * @author chenyan
 * @date 2017年9月12日 14:58:23
 * @version v1.0
 */
@Controller
@RequestMapping("/groupScreenVidicon")
public class GroupScreenVidiconController extends BaseController{

	@Autowired
	private GroupScreenService groupScreenService;
	
	@Autowired
	private ScreenVidiconService screenVidiconService;
	
	@Autowired
	private GroupScreenVidiconService groupScreenVidiconService;

	/*
	 * 一、分组管理
	 */

	/**
	 * 2、分组管理新增
	 * 			包含：一个组、组下多个屏、每个屏下多个摄像机
	 * @param	paramJson：以共8个摄像机，2*2屏为例
	 * 				{
	 * 					"group_name":..., 		//组名称										string
	 * 					"screen_type":2,		//分屏模式（1代表1*1 2代表2*2 3代表3*3 4代表4*4）	int
	 * 					"set_default":...,		//是否设置为默认组（1代表是，2代表否）					int
	 * 					"is_important":...,		//是否设置为重要组（1代表是，2代表否）					int
	 * 					"all_screen":{			//健名：代表第几屏								int
	 * 								"1":{		
	 * 									"screen_name":...,			//屏名称					string
	 * 									"all_vidicon":{				//键名：代表摄像机在屏中位置		int	
	 * 													"1":...,	//键值：摄像机id			string
	 * 													"2":...,
	 * 													"3":...,	
	 * 													"4":...,
	 * 												  }
	 * 								  },
	 * 								"2":{
	 * 									"screen_name":...,
	 * 									"all_vidicon":{
	 * 													"1":...,
	 *													"2":...,
	 *													"3":...,
	 *													"4":...,
	 * 												  }
	 * 								  }
	 * 								}
	 * 				}
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							操作成功：	code:200,	message:"success"
	 * 							操作失败：	code:300,	message:"操作失败，请重新操作"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../groupScreenVidicon/addGroupScreenVidicon
	 * @author	chenyan
	 */
	@RequestMapping(value = "/addGroupScreenVidicon", method = RequestMethod.POST)
	@ResponseBody
	public Response addGroupScreenVidicon(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("group_name") == null || json.getString("group_name") == "" || 
				json.getString("screen_type") == null || json.getString("screen_type") == ""||
				json.getString("all_screen") == null ||
				json.getInt("set_default")== 0 || 
				json.getInt("is_important") == 0
				){
			return Response.PARAM_ERROR();
		}
		String userId = this.getCurrentuserid();
		try{
			Boolean result = groupScreenVidiconService.addGroupScreenVidicon(json, userId);
			if(result == true){
				return Response.OK();
			}else{
				return Response.ERROR(300, "操作失败，请重新操作");
			}	
		}catch (Exception e){
			e.printStackTrace();
			System.out.println(e);
			return Response.SERVER_ERROR();
		}
	}

	/** 
	 * 3、分组管理单条删除
	 * 			包含：一个组、组下多个屏、每个屏下多个摄像机
	 * @param	paramJson:{
	 * 				"group_id":...	//组id	string
	 * 			}
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							操作成功：	code:200,	message:"success"
	 * 							操作失败：	code:300,	message:"操作失败，请重新操作"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../groupScreenVidicon/deleteGroupScreenVidiconByGroupId
	 * @author	chenyan
	 * @date	2017年9月13日 17:32:21
	 */
	@RequestMapping(value = "/deleteGroupScreenVidiconByGroupId", method = RequestMethod.POST)
	@ResponseBody
	public Response deleteGroupScreenVidiconByGroupId(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("group_id") == null || json.getString("group_id") == ""){
			return Response.PARAM_ERROR();
		}
		try{
			Boolean result = groupScreenVidiconService.deleteGroupScreenVidiconByGroupId(json.getString("group_id"));
			if(result == true){
				return Response.OK();
			}else{
				return Response.ERROR(300, "操作失败，请重新操作");
			}	
		}catch (Exception e){
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}

	/** 
	 * 4、分组管理批量删除
	 * 			包含：多个组
	 * @param	paramJson:{
	 * 				"group_ids":...	//组id列表	string
	 * 			}
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							操作成功：	code:200,	message:"success"
	 * 							操作失败：	code:300,	message:"操作失败，请重新操作"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../groupScreenVidicon/deleteGroupScreenVidicon
	 * @author	chenyan
	 * @date	2017年9月27日10:39:05
	 */
	@RequestMapping(value = "/deleteGroupScreenVidicon", method = RequestMethod.POST)
	@ResponseBody
	public Response deleteGroupScreenVidicon(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("group_ids") == null || json.getString("group_ids") == ""){
			return Response.PARAM_ERROR();
		}
		JSONArray groupIdList = JSONArray.fromObject(json.get("group_ids"));
		try{
			Boolean result = groupScreenVidiconService.deleteGroupScreenVidicon(groupIdList);
			if(result == true){
				return Response.OK();
			}else{
				return Response.ERROR(300, "操作失败，请重新操作");
			}	
		}catch (Exception e){
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}

	/** 
	 * 5、分组管理详情
	 * 			通过组id获取分组管理详情
	 * 				包含：一个组、组下的多个屏、组与屏关联、屏与摄像机关联、摄像机
	 * @param	paramJson:{
	 * 				"group_id":...	//组id	string
	 * 			}

	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",	data:某个组的分组管理详情
	 * 							无数据：	code:3001,	message:"暂无数据"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @author	chenyan
	 * @url		../groupScreenVidicon/getGroupScreenVidiconByGroupId
	 * @date	2017年9月14日 10:55:18
	 */
	@RequestMapping(value = "/getGroupScreenVidiconByGroupId", method = RequestMethod.POST)
	@ResponseBody
	public Response getGroupScreenVidiconByGroupId(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("group_id") == null || json.getString("group_id") == ""){
			return Response.PARAM_ERROR();
		}
		try{
			Map<String, Object> result = groupScreenVidiconService.getGroupScreenVidiconByGroupId(json.getString("group_id"));
			if(result != null){
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

	/** 
	 * 6、分组管理修改
	 * 			通过组id修改分组管理（包括：一个组、组下的多个屏、组与屏关联、屏与摄像机关联、摄像机）
	 * @param	paramJson:{
	 * 					{
	 * 					"group_id":...,		//组id										string
	 * 					"group_name":..., 	//组名称										string
	 * 					"screen_type":2,	//分屏模式（1代表1*1 2代表2*2 3代表3*3 4代表4*4）		int
	 * 					"set_default":...,	//是否设置为默认组（1代表是，2代表否）					int
	 * 					"is_important":...,	//是否设置为重要组（1代表是，2代表否）					int
	 * 					"all_screen":{
	 * 						"1":{
	 * 									"screen_id":...,	//屏id							string
	 * 									"screen_name":...,	//屏名称							string
	 * 									"set_default":...,	//是否设置为默认屏（1代表是，0代表否）		int
	 * 									"all_vidicon":{
	 * 													"1":{
	 * 																"screen_vidicon_id":...,	//屏与摄像机对应关系id	string
	 * 																"vidicon_id":...,			//摄像机id			string
	 * 													},
	 * 													"2":{
	 * 																"screen_vidicon_id":...,
	 * 																"vidicon_id":...,
	 * 													},
	 * 													"3":{
	 * 																"screen_vidicon_id":...,
	 * 																"vidicon_id":...
	 * 													},
	 * 													"4":{
	 * 																"screen_vidicon_id":...,
	 * 																"vidicon_id":...
	 * 													}
	 * 									}
	 * 						},
	 * 						"2":{
	 * 									"screen_id":...,
	 * 									"screen_name":...,
	 * 									"set_default":...,
	 * 									"all_vidicon":{
	 * 													"1":{
	 * 																"screen_vidicon_id":...,
	 * 																"vidicon_id":...
	 * 													},
	 * 													"2":{
	 *  															"screen_vidicon_id":...,
	 * 																"vidicon_id":...
	 * 													},
	 * 													"3":{
	 * 																"screen_vidicon_id":...,
	 * 																"vidicon_id":...
	 * 													},
	 * 													"4":{
	 * 																"screen_vidicon_id":...,
	 * 																"vidicon_id":...
	 * 													}
	 * 									}
	 * 						}
	 * 					}
	 * }
	 * @url		../groupScreenVidicon/updateGroupScreenVidiconByGroupId
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							操作成功：	code:200,	message:"success"
	 * 							操作失败：	code:300,	message:"操作失败，请重新操作"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @author	chenyan
	 * @date	2017年9月15日 09:16:32
	 */
	@RequestMapping(value = "/updateGroupScreenVidiconByGroupId", method = RequestMethod.POST)
	@ResponseBody
	public Response updateGroupScreenVidiconByGroupId(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("group_id") == null || json.getString("group_id") == "" || 
				json.getString("group_name") == null || json.getString("group_name") == "" || 
				json.getString("screen_type") == null || json.getString("screen_type") == ""||
				json.getString("all_screen")== null ||
				json.getInt("set_default")== 0 || json.getInt("is_important") == 0
				){
			return Response.PARAM_ERROR();
		}
		String userId = this.getCurrentuserid();
		try{
			Boolean result = groupScreenVidiconService.updateGroupScreenVidiconByGroupId(json,userId);
			if(result == true){
				return Response.OK();
			}else{
				return Response.ERROR(300, "操作失败，请重新操作");
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
	 * 3、获取某个组下的所有屏列表
	 * @param	paramJson：{
	 * 					"group_id":...	//组id	 string
	 * 				}
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",	data:某个组下的所有屏列表  List
	 * 							无数据：	code:3001,	message:"暂无数据"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../groupScreenVidicon/getScreenListByGroupId
	 * @author	chenyan
	 */
	@RequestMapping(value = "/getScreenListByGroupId", method = RequestMethod.POST)
	@ResponseBody
	public Response getScreenListByGroupId(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("group_id") == null || json.getString("group_id") == ""){
			return Response.PARAM_ERROR();
		}
		try{
			List<GroupScreenDto> result = groupScreenService.getScreenListByGroupIdDto(json.getString("group_id"));
			if(result.size()>0){
				Response response = Response.OK();
				response.put("screen_length", result.size());
				response.setData(result);
				return response;
			}else{
				return Response.ERROR(3001, "暂无数据");
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}

	/** 
	 * 4、获取某个组下的第几屏
	 * @param	paramJson：{
	 * 					"group_id":...,		//组id	 string
	 * 					"screen_sort":...	//第几屏	int
	 * 				}
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",	data:某个组下的所有屏列表  List
	 * 							无数据：	code:3001,	message:"暂无数据"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../groupScreenVidicon/getScreenVidiconListByGroupIdAndSort
	 * @author	chenyan
	 */
	@RequestMapping(value = "/getScreenVidiconListByGroupIdAndSort", method = RequestMethod.POST)
	@ResponseBody
	public Response getScreenVidiconListByGroupIdAndSort(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("group_id") == null || json.getString("group_id") == "" ||
				json.getInt("screen_sort") == 0
				){
			return Response.PARAM_ERROR();
		}
		try{
			List<ScreenVidiconDto> result = screenVidiconService.getScreenVidiconListByGroupIdAndSort(json.getString("group_id"),json.getInt("screen_sort"));
			if(result.size()>0){
				Response response = Response.OK();
				response.setData(result);
				return response;
			}else{
				return Response.ERROR(3001, "暂无数据");
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}

	/** 
	 * 5、获取某个屏
	 * 			通过屏id获取某个屏下的所有屏与摄像机关联关系列表
	 * @param	paramJson：{
	 * 					"screen_id":...	//组id	 string
	 * 				}
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",	data:某个组下的所有屏列表  List
	 * 							无数据：	code:3001,	message:"暂无数据"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../groupScreenVidicon/getScreenVidiconListByScreenId
	 * @author	chenyan
	 */
	@RequestMapping(value = "/getScreenVidiconListByScreenId", method = RequestMethod.POST)
	@ResponseBody
	public Response getScreenVidiconListByScreenId(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("screen_id") == null || json.getString("screen_id") == ""){
			return Response.PARAM_ERROR();
		}
		try{
			List<ScreenVidiconDto> result = screenVidiconService.getScreenVidiconListByScreenId(json.getString("screen_id"));
			if(result.size()>0){
				Response response = Response.OK();
				response.setData(result);
				return response;
			}else{
				return Response.ERROR(3001, "暂无数据");
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}	
	}

	/**
	 * 6、获取某个屏以外的其他屏的所有摄像头列表
	 * @param	paramJson：{
	 * 					"screen_id":...,	//屏id	 string
	 * 				}
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",	data:某个屏以外的其他屏的所有摄像头列表	List
	 * 							无数据：	code:3001,	message:"暂无数据"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../groupScreenVidicon/getOtherVidiconListByScreenId
	 * @author	chenyan
	 */
	@RequestMapping(value = "/getOtherVidiconListByScreenId", method = RequestMethod.POST)
	@ResponseBody
	public Response getOtherVidiconListByScreenId(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getString("screen_id") == null || json.getString("screen_id") == "" ){
			return Response.PARAM_ERROR();
		}
		try{
			List<VidiconInfo> result = screenVidiconService.getOtherVidiconListByScreenId(json.getString("screen_id"));
			if(result.size()>0){
				Response response = Response.OK();
				response.setData(result);
				return response;
			}else{
				return Response.ERROR(3001, "暂无数据");
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}	
	}

	/**
	 * 7、获取某种分屏模式下的所有屏（除1*1屏）
	 * @param	paramJson：{
	 * 					"screen_type":...	//分屏模式
	 * 				}
	 * @return	Response：说明：	参数错误：	code:1001,	message:"参数错误"
	 * 							有数据：	code:200,	message:"success",	data:某个屏以外的其他屏的所有摄像头列表	List
	 * 							无数据：	code:3001,	message:"暂无数据"
	 * 						  	异常：    	code:500,	message:"服务器异常"
	 * @url		../groupScreenVidicon/getScreenListByScreenType
	 * @author	chenyan
	 */
	@RequestMapping(value = "/getScreenListByScreenType", method = RequestMethod.POST)
	@ResponseBody
	public Response getScreenListByScreenType(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		if(json.getInt("screen_type") == 0 || json.getInt("screen_type") == 1 ){
			return Response.PARAM_ERROR();
		}
		try{
			List<List<Map<String, Object>>> result = groupScreenVidiconService.getScreenListByScreenType(json.getInt("screen_type"));
			if(result == null || result.size()<1){
				return Response.ERROR(3001, "暂无数据");
			}else{
				Response response = Response.OK();
				response.setData(result);
				return response;
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}	
	}

	//	//获取某个组第几个屏以外的其他屏的所有摄像头列表
	//	@RequestMapping(value = "/getOtherVidiconListByGroupIdAndScreenSort", method = RequestMethod.POST)
	//	@ResponseBody
	//	public Response getOtherVidiconListByGroupIdAndScreenSort(String paramJson){
	//		JSONObject json = JSONObject.fromObject(paramJson);
	//		if(json.getString("group_id") == null || json.getString("group_id") == "" ||
	//				json.getInt("screen_sort") == 0
	//			){
	//			return Response.PARAM_ERROR();
	//		}
	//		try{
	//			List<VidiconInfo> result = screenVidiconService.getOtherScreenVidiconListByGroupIdAndSort(json.getString("group_id"),json.getInt("screen_sort"));
	//			if(result.size()>0){
	//				Response response = Response.OK();
	//				response.setData(result);
	//				return response;
	//			}else{
	//				return Response.ERROR(3001, "暂无数据");
	//			}
	//		}catch (Exception e){
	//			System.out.println(e);
	//			e.printStackTrace();
	//			return Response.SERVER_ERROR();
	//		}	
	//	}

}