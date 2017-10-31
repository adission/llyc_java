package pos.cn.com.lanlyc.controller;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import pos.cn.com.lanlyc.core.dto.PosSimpleUserDto;



@Controller
@RequestMapping("/PosUser")
public class PosUserController extends BaseController {
	
	
	/**
	 * 根据工地id获取工地人员列表
	 * @param layer_id 图层id
	 * @author jiangyanyan
	 * @return
	 */
	@RequestMapping(value="/getUserInfo")
	@ResponseBody
	public Response  getPosUserInfor(String paramJson) {
		JSONObject jObject = JSONObject.parseObject(paramJson);
		//图层id
		String layer_id = jObject.getString("layer_id");
		//假如工地id参数为空，返回参数错误信息
		List<Map<String,Object>> result=this.getService().getPosPerConstructService().getConstructPersonList(layer_id);
		Response response=Response.OK();
        response.setData(result);
		return response;
	}
	
	
	/**
	 * 实时监控根据人员名称定位卡图层id查询(获取一个图层的所有人员) (简化版)
	 * 	@param layer_id 图层id
	 *	@author jiangyanyan
	 * 	@return
	 */
	@RequestMapping(value="/getSearchUserInfo")
	@ResponseBody
	public Response getSearchUserInfor(String paramJson) {
		JSONObject jObject = JSONObject.parseObject(paramJson);
		if(!jObject.containsKey("searchCon") || !jObject.containsKey("searchCon")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		//人员名称,卡号
		String searchCon = jObject.getString("searchCon"); 
		//图层id
		try {
			//假如工地id参数为空，返回参数错误信息
//			List<GateUserInfoView> result=this.getService().getPosPerConstructService().getPosPerConstructDao().getSearchUserInfoList(searchCon);
			List<GateUserInfoView> result=this.getService().getPosPerConstructService().getSearchUserInfoListService(searchCon);
			Response response = Response.OK();
			response.setData(result);
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	/**
	 * 返回人员列表不分页
	 * @param constructId  工地id
	 * @param searchContent  搜索关键字
	 * @author jiangyanyan
	 * @return
	 */
	@RequestMapping(value="/getUserNoPage")
	@ResponseBody
	public Response getPosUserInfoNoPage(String paramJson) {
		JSONObject jObject = JSONObject.parseObject(paramJson);
		try {
			List<GateUserInfoView> userList = this.getService().getPosPerConstructService().getPosPerConstructDao().
					getConstructPersonListNoPage();
			Response response = Response.OK();
			response.setData(userList);
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
			
}
