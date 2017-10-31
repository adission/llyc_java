package pos.cn.com.lanlyc.controller;
import java.util.Date;
import java.util.List;

/**
 * @author cjt
 * 人员轨迹控制器
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.dto.PosTrajectoryDto;
import pos.cn.com.lanlyc.core.dto.UserPosDto;
import pos.cn.com.lanlyc.core.po.PosTrajectory;

@Controller
@RequestMapping("/postrajectory")
public class PosTrajectoryController extends BaseController{
	
	/**
	 * 查询前两小时的人员轨迹历史记录
	 * param
	 * userId 人员id
	 * @return 
	 */
	@RequestMapping("/history")
	@ResponseBody
	public Response findHistory(String paramJson) {
		JSONObject jb = JSONObject.fromObject(paramJson);
		Response re = Response.newResponse();
		List<PosTrajectoryDto> list = null;
		try {
			long endtime,starttime;
			
			if(jb.has("date") && !DataUtils.isNullOrEmpty(jb.getString("date"))) {
				String date = jb.getString("date");
				if(jb.has("endtime") && !DataUtils.isNullOrEmpty(jb.getString("endtime"))) {
					String endtime2 = jb.getString("endtime");
					endtime=DataUtils.getTimeStamp(date+" "+endtime2);
				}else {
					endtime = new Date().getTime();
				}
				if(jb.has("starttime") && !DataUtils.isNullOrEmpty(jb.getString("starttime"))) {
					String starttime2 = jb.getString("starttime");
					starttime=DataUtils.getTimeStamp(date+" "+starttime2);
				}else {
					starttime = (new Date().getTime()-2*60*60*1000);
				}
			}else {
				endtime = new Date().getTime();
				starttime = new Date().getTime()-2*60*60*1000;
			}
			list = this.getService().getPosTrajectoryService().getHistoryPos(jb.getString("userId"),endtime,starttime);
//			GateUser gu = this.getService().getPosUserService().getPosUserDao().get(jb.getString("userId"));
			UserPosDto ud= new UserPosDto();
			ud.setPosList(list);
//			ud.setName(gu.getName());
//			ud.setWorkers_type(gu.getWorkers_type());
			re.ok(list);
		} catch (Exception e) {
			re = Response.PARAM_ERROR();
			// TODO: handle exception
		}
		return re;
		
	}
	
	/*
	 * 根据人员id实时查询该人员坐标
	 * param:  userIds  人员id列表
	 */
	@RequestMapping("/currentPos")
	@ResponseBody
	public Response getPosByUserId(String paramJson) {
		JSONObject jb = JSONObject.fromObject(paramJson);
		Response re = Response.newResponse();
		try {
			JSONArray userArray = jb.getJSONArray("userIds");
			List<String> userIds = JSONArray.toList(userArray,new String(), null);
			List<PosTrajectoryDto> pos = this.getService().getPosTrajectoryService().getCurrentPos(userIds);
			re.ok(pos);
			
		} catch (Exception e) {
			// TODO: handle exception
			re = Response.PARAM_ERROR();
			
		}
		return re;
	}
	
	
	/**
	 * 根据工地id获取人员最新的坐标点
	 * @param paramJson
	 * @author jiangyanyan
	 * @return
	 */
//	@RequestMapping("/getUserXY")
//	@ResponseBody
//	public Response getUserTrajectory(String paramJson) {
//		JSONObject jObject = JSONObject.fromObject(paramJson);
//		//假如没有工地id这个字段，返回参数错误信息
//		if(!jObject.containsKey("constructId")) {
//			Response response = Response.PARAM_ERROR();
//			return response;	
//		}
//		//工地id
//		String constructId = jObject.getString("constructId");
//		//假如工地id参数为空，返回参数错误信息
//		if( constructId==null || "".equals(constructId)) {
//			Response response = Response.PARAM_ERROR();
//			return response;
//		}
//		List<PosTrajectory> trajectory_list = this.getService().getPosTrajectoryService().getPosTrajectoryList(constructId);
//		Response response = Response.OK();
//		response.put("data", trajectory_list);
//		return response;	
//	}
	
	
}
