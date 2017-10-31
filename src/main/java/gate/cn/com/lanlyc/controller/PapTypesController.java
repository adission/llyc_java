package gate.cn.com.lanlyc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Codes;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.PapTypes;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/PapTypes")
public class PapTypesController  extends BaseController {

	/**
	 * 查询所有的证件类型 访问url PapTypes/selectAllPapType?token
	 * 
	 * @return response
	 */
	@RequestMapping("/selectAllPapTypes")
	@ResponseBody
	public Response selectAllPapTypes() {
		try {
			List<PapTypes> paptypes = this.getService().getPapTypesService().getAllPapTypes();
			Response response = Response.OK();
			if(paptypes!=null){
				response.put("data", paptypes);
			}else{
				response.put("message","没有查找到证件类型");
				response.put("code",0);				
			}			
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}
	
	
	/**
	 * 分页查询证件类型&根据关键字分页查询证件类型
	 * @param start 每页以第几条开始
	 * @param length 每页的长度
	 * @param draw 第几页
	 * @author jiangyanyan
	 * @return response
	 */
	@RequestMapping(value="queryPap")
	@ResponseBody
	public Response queryPapTypesAndPage(String paramJson,Integer start,Integer length,Integer draw) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String keyword = "";
		if(jObject.has("keyword")) {
			keyword = jObject.getString("keyword");
		}
		int currentPage = start/length+1;
		Page<PapTypes> page = new Page<PapTypes>(currentPage);
		page.setPageSize(length);
		try {
			Page<PapTypes> result = this.getService().getPapTypesService().getPapTypesDao().queryUserAndCard(page, keyword);
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
	 * 根据证件类型名称新建证件类型
	 * @param name 新建证件类型的名称
	 * @author jiangyanyan
	 * @return response
	 */
	public Response createPapTypes(String paramJson){
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String name = jObject.getString("name");
		if(!jObject.has("name")|| name == null || "".equals(name)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		try {
			int createStatus = this.getService().getPapTypesService().isCreatePapTypes(name);
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
	 * 根据证件id列表删除证件类型
	 * @param ids 要删除的证件类型列表
	 * @author jiangyanyan
	 * @return response
	 */
	public Response deletePapTypes(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		JSONArray ids = jObject.getJSONArray("ids");
		List<String> idsList = ids.toList(ids,new String(),null);
		if(idsList.size()==0) {
			return Response.ERROR(Codes.ERROR, "请选择要删除的证件！");
		}else {
			try {
				boolean isDelete = this.getService().getPapTypesService().getPapTypesDao().isDeletePapTypes(idsList);
				if(isDelete) {
					return Response.set("message", "删除证件类型成功！");
				}else {
					return Response.ERROR(Codes.ERROR, "删除证件类型失败！");
				}
			}catch(Exception e) {
				String message = new String("未知错误！");
				return Response.ERROR(1019, message);
			}
		}
	}
	
	
	 /**
	  * 根据证件类型的旧名称修改证件类型的新名称
	  * @param newName 证件类型的新名称
	  * @param oldName 证件类型的旧名称
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
			int aditStatus = this.getService().getPapTypesService().aditPapTypes(newName, oldName);
			if(aditStatus==1) {
				return Response.ERROR(Codes.ROLE_EXISTS, "证件类型"+oldName+"已经存在！");
			}else if(aditStatus==2) {
				return Response.set("message", "编辑证件类型"+oldName+"成功！");
			}else {
				return Response.ERROR(Codes.ERROR, "编辑证件类型"+oldName+"失败！");
			}
		}catch(Exception e) {
			String message = new String("未知错误！");
			return Response.ERROR(1019, message);
		}
		
	}
	
	
}
