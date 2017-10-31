package gate.cn.com.lanlyc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Codes;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.CarTypes;
import gate.cn.com.lanlyc.core.po.PapTypes;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/CarTypes")
public class CarTypesController   extends BaseController {
	/**
	 * 查询所有的车辆类型 访问url CarTypes/selectAllCarType?token
	 * 
	 * @return response
	 */
	@RequestMapping("/selectAllCarTypes")
	@ResponseBody
	public Response selectAllCarTypes() {
		try {
			List<CarTypes> cartypes = this.getService().getCarTypesService().getAllCarTypes();
			Response response = Response.OK();
			if(cartypes!=null){
				response.put("data", cartypes);
			}else{
				response.put("message","没有查找到车辆类型");
				response.put("code",0);				
			}			
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}
	
	
	/**
	 * 分页查询车辆类型&根据关键字分页查询车辆类型
	 * @param start 每页以第几条开始
	 * @param length 每页的长度
	 * @param draw 第几页
	 * @author jiangyanyan
	 * @return response
	 */
	@RequestMapping(value="queryCar")
	@ResponseBody
	public Response queryCarTypesAndPage(String paramJson,Integer start,Integer length,Integer draw) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String keyword = "";
		if(jObject.has("keyword")) {
			keyword = jObject.getString("keyword");
		}
		int currentPage = start/length+1;
		Page<CarTypes> page = new Page<CarTypes>(currentPage);
		page.setPageSize(length);
		try {
			Page<CarTypes> result = this.getService().getCarTypesService().getCarTypesDao().queryCarTypes(page, keyword);
			Response response=Response.OK();
			response.put("recordsTotal", result.getTotalRow());
	        response.put("recordsFiltered", result.getTotalRow());
	        response.setData(result.getResultRows());
	        response.put("recordsFiltered", result.getTotalRow());
	        response.put("draw", draw);
	        response.put("total", result.getTotalRow());
			return response;
		}catch(Exception e){
			String message = new String("未知错误！");
			return Response.ERROR(1019, message);
		}
		
	}
	
	
	/**
	 * 根据车辆类型名称新建车辆类型
	 * @param name 新建证件类型的名称
	 * @author jiangyanyan
	 * @return response
	 */
	public Response createCarTypes(String paramJson){
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String name = jObject.getString("name");
		if(!jObject.has("name")|| name == null || "".equals(name)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		try {
			int createStatus = this.getService().getCarTypesService().isCreateCarTypes(name);
			if(createStatus==1) {
				return Response.ERROR(Codes.ROLE_EXISTS, "证件类型"+name+"已经存在！");
			}else if(createStatus==2) {
				return Response.set("message", "新增证件类型"+name+"成功！");
			}else {
				return Response.ERROR(Codes.ERROR, "新增证件类型"+name+"失败！");
			}
			
		}catch(Exception e) {
			String message = new String("未知错误！");
			return Response.ERROR(1019, message);
		}	
	}
	
	
	/**
	 * 根据车辆id列表删除车辆类型
	 * @param ids 要删除的车辆类型列表
	 * @author jiangyanyan
	 * @return response
	 */
	public Response deleteCarTypes(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		JSONArray ids = jObject.getJSONArray("ids");
		List<String> idsList = ids.toList(ids,new String(),null);
		if(idsList.size()==0) {
			return Response.ERROR(Codes.ERROR, "请选择要删除的车辆类型！");
		}else {
			try {
				boolean isDelete = this.getService().getCarTypesService().getCarTypesDao().isDeleteCarTypes(idsList);
				if(isDelete) {
					return Response.set("message", "删除车辆类型成功！");
				}else {
					return Response.ERROR(Codes.ERROR, "删除车辆类型失败！");
				}
			}catch(Exception e) {
				String message = new String("未知错误！");
				return Response.ERROR(1019, message);
			}
		}
	}
	
	
	 /**
	  * 根据车辆类型的旧名称修改车辆类型的新名称
	  * @param newName 车辆类型的新名称
	  * @param oldName 车辆类型的旧名称
	  * @author jiangyanyan
	  * @return response
	  */
	public Response aditPapTypes(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String newName = jObject.getString("newName");
		String oldName = jObject.getString("oldName");
		if(!jObject.has("newName")|| newName == null ||"".equals(newName)||!jObject.has("oldName")|| oldName == null ||"".equals(oldName)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		
		try {
			int aditStatus = this.getService().getCarTypesService().aditCarTypes(newName, oldName);
			if(aditStatus==1) {
				return Response.ERROR(Codes.ROLE_EXISTS, "车辆类型"+oldName+"已经存在！");
			}else if(aditStatus==2) {
				return Response.set("message", "编辑车辆类型"+oldName+"成功！");
			}else {
				return Response.ERROR(Codes.ERROR, "编辑车辆类型"+oldName+"失败！");
			}
		}catch(Exception e) {
			String message = new String("未知错误！");
			return Response.ERROR(1019, message);
		}
		
	}
	
	
	
	
}
