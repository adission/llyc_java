package pos.cn.com.lanlyc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.core.util.Codes;
import cn.com.lanlyc.core.util.Response;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.po.PosFloor;

/**
 * 关于图层Controller
 * @author jiangyanyan
 * @date 2017年9月27日 17:29:23
 * @version 1.0
 *
 */

@Controller
@RequestMapping("/posfloor")
public class PosFloorController extends BaseController {
	
	/**
	 * 获取所有图层
	 * @param 暂时没有参数,以后会传项目id
	 * @author jiangyanyan
	 * @return 返回图层列表
	 */
	@RequestMapping(value="/getFloorInfo")
	@ResponseBody
	public Response getPosFloorInfo(String paramJson) {
		try {
			List<PosFloor> posFloor = this.getService().getPosFloorService().getFloorInfoService();
			Response response=Response.OK();
	        response.setData(posFloor);
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	/**
	 * 获取项目中的所有图层(只有图层id、name)
	 * @param paramJson
	 * @return 获取图层列表数据
	 */
	@RequestMapping(value = "/getFloorInfoL")
	@ResponseBody
	public Response getPosFloorInfoLess(String paramJson){
		try {
			List<Map<String,Object>> posFloor = this.getService().getPosFloorService().getFloorInfoLessService();
			Response response=Response.OK();
	        response.setData(posFloor);
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	/**
	 * 查询数据表中第一个图层
	 * @param paramJson
	 * @author jiangyanyan
	 * @return 返回项目中的第一个图层列表
	 */
	@RequestMapping(value = "/getOneFloorInfo")
	@ResponseBody
	public Response getOnePosFloorInfo(String paramJson){
		try {
			Map<String,Object> posFloor = this.getService().getPosFloorService().getOneFloorInfo();
			Response response=Response.OK();
	        response.setData(posFloor);
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	/**
	 * 根据图层id，获取图层的背景图片与坐标点
	 * @param id 图层id
	 * @return map对象，map对象有图层背景图片与坐标点
	 */
	@RequestMapping(value = "/getFloorBJTPZB")
	@ResponseBody
	public Response getPosFloorBJTPZB(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		if(!jObject.has("id")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String  layer_id = jObject.getString("id"); //图层id
		if(layer_id==null || "".equals(layer_id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		try {
			Map<String,Object> posFloor = this.getService().getPosFloorService().getPosFloorBJTPZB(layer_id);
			Response response=Response.OK();
	        response.setData(posFloor);
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	
	/**
	 * 根据图层id删除图层
	 * @param paramJson
	 * @param layer_id 图层id
	 * @return 返回删除成功与失败的json数据
	 */
	@RequestMapping(value="/deleteFloorInfo")
	@ResponseBody
	public Response deletePosFloorInfo(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String layer_id = jObject.getString("layer_id");
		if(!jObject.containsKey("layer_id")||layer_id==null||"".equals(layer_id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		try{
			boolean res = this.getService().getPosFloorService().getPosFloorDao().deletePosFloorInfo(layer_id);
			if(res) {
				Response response=Response.OK();
				return response;
			}else {
				 return Response.ERROR(Codes.ROLE_EXISTS, "删除图层失败!");	
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	/**
	 * 新建图层接口
	 * @param paramJson
	 * @param map_name 图层名称
	 * @param space_name 空间名称
	 * @param datas_name 数据集名称
	 * @param url 图层头像
	 * @param MIN_X 图层最小x坐标
	 * @param MIN_Y 图层最小y坐标
	 * @param MAN_X 图层最大x坐标
	 * @param MAN_Y 图层最大y坐标
	 * @param scale 背景图的缩放比例
	 * @param back_url 图层的背景图url
	 * @return 返回图层添加成功与失败
	 */
	@RequestMapping(value="/initMap")
	@ResponseBody
	public Response addPosFloorInfo(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String map_name = jObject.getString("map_name");
		String space_name = jObject.getString("space_name");
		String datas_name = jObject.getString("datas_name");
		String url = jObject.getString("url");
		String MIN_X = jObject.getString("MIN_X");
		String MIN_Y = jObject.getString("MIN_Y");
		String MAN_X = jObject.getString("MAN_X");
		String MAN_Y = jObject.getString("MAN_Y");
		String scale = jObject.getString("scale");
		String back_url = jObject.getString("back_url");
		if(!jObject.containsKey("map_name")||!jObject.containsKey("MAN_X")||!jObject.containsKey("MAN_Y")||!jObject.containsKey("scale")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		if(map_name==null||"".equals(map_name)||MAN_X==null||"".equals(MAN_X)||MAN_Y==null||"".equals(MAN_Y)||scale==null||"".equals(scale)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		PosFloor floor = new PosFloor();
		floor.setId(DataUtils.getUUID());
		floor.setMin_x(MIN_X);
		floor.setMin_y(MIN_Y);
		floor.setMax_x(MAN_X);
		floor.setMax_y(MAN_Y);
		floor.setLayer_name(map_name);
		floor.setSpace_name(space_name);
		floor.setScale(scale);
		floor.setUrl(url);
		floor.setBjtx(back_url);
		floor.setDatas_name(datas_name);
		try {
			int saveF = this.getService().getPosFloorService().getPosFloorDao().save(floor);
			if(saveF==1) {
				Response response=Response.OK();
				return response;
			}else {
				return Response.ERROR(Codes.ROLE_EXISTS, "保存图层失败!");	
			}
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
		
}
