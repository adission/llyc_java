package gate.cn.com.lanlyc.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Codes;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.dto.GateExtraUserInfoDto;
import net.sf.json.JSONObject;

/**
 * 
 * @author hucong time 2017-10-19 09:01:07 Function 实现访客方面的逻辑操作
 * 
 */

@Controller
@RequestMapping("/GateVisit")
public class GateVisitController extends BaseController {
	@RequestMapping("/addVisit")
	@ResponseBody
	/**
	 * 新增访客
	 * 
	 * 访问url GateVisit/addVisit
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response addVisit(String paramJson) throws ParseException {
		System.out.println("新增访客的参数                      ："+paramJson);
		JSONObject object=JSONObject.fromObject(paramJson);
		int num=this.getService().getGateVisitService().addVisit(object);
		if(num<=0){
	    	   Response response=Response.PARAM_ERROR();
			   response.put("code",300);
			   response.put("message","数据库操作错误,新增未完成");
			   return response;
	       } 
	       Response response = Response.newResponse();
	       response.put("code",200);
	       response.put("message", "新增成功");
		   return  response;		
	}
	

	/**
	 * 根据访客id，查询访客信息
	 * 
	 * 访问url GateVisit/selectVisitorById
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	@RequestMapping("/selectVisitorById")
	@ResponseBody
	public Response selectVisitorById(String paramJson) {	
		System.out.println("根据访客id来查找访客          ："+paramJson);
		JSONObject object=JSONObject.fromObject(paramJson);
		if(object==null||"".equals(object)) {
			Response response=Response.PARAM_ERROR();
			return response;
		}
		String id=object.getString("id");
		GateExtraUserInfoDto visitor=this.getService().getGateVisitService().selectVisitorById(id);
		Response response=Response.OK();
        if(visitor!=null){
    		response.put("data", visitor);
        }else{
            response.put("code", -1);
    		response.put("message", "获取人员失败");
        }
		return response;			
	}
	
	/**
	 * 新增用户时证件号码是否已存在
	 * 
	 * 访问url GateVisit/selectCid
	 * @return 
	 */
	@RequestMapping("/selectCid")
	@ResponseBody
	public Response selectPapNumber(String paramJson) {	
		System.out.println("查找证件号码          ："+paramJson);
		JSONObject object=JSONObject.fromObject(paramJson);
		if(object==null||"".equals(object)) {
			Response response=Response.PARAM_ERROR();
			response.put("message", "参数错误");
    		response.put("valid", false);
			return response;
		}
		String cid=object.getString("cid");
		Boolean  pap_number=this.getService().getGateVisitService().selectPapNumber(cid);	
		System.out.println(pap_number+"  是否存在");
        if(pap_number==true){
        	Response response = Response.set("code", Codes.PHONE_EXISTS);
    		response.put("message", "证件号码已存在");
    		response.put("valid", false);
    		return response;
        }else{
        	Response response = Response.OK();
    		response.put("valid", true);
    		return response;
        }		
	}
	
	
	@RequestMapping("/deleteVisit")
	@ResponseBody
	/**
	 * 删除访客  可以批量删除
	 * 
	 * 访问url GateVisit/deleteVisit
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response deleteVisit(String id){
		System.out.println("删除访客的参数                      ："+id); 
		// 删除可以批量删除
		try {
			Boolean deleteVisit = this.getService().getGateVisitService().del_visit_user(id);
			Response response = Response.newResponse();
			if(deleteVisit){
				response.put("code", 200);
	    		response.put("message", "删除人员成功");
			}else{
				response.put("code", -1);
	    		response.put("message", "删除人员失败");
			}	
			return response;
		} catch (Exception e) {
			Response response=Response.PARAM_ERROR();
			response.put("code",300);
			response.put("message","数据库操作错误,新增未完成");
			return response;
		}
	}
	
	@RequestMapping("/updateVisit")
	@ResponseBody
	/**
	 * 修改访客 
	 * 
	 * 访问url GateVisit/updateVisit
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response updateVisit(String paramJson){
		System.out.println("修改访客的参数                      ："+paramJson); 
		JSONObject object=JSONObject.fromObject(paramJson);
		try {
			Boolean updateVisit = this.getService().getGateVisitService().update_visit_user(object);
			Response response = Response.newResponse();
			if(updateVisit){
				response.put("code", 200);
	    		response.put("message", "修改访客成功");
			}else{
				response.put("code", -1);
	    		response.put("message", "修改访客失败");
			}	
			return response;
		} catch (Exception e) {
			Response response=Response.PARAM_ERROR();
			response.put("code",300);
			response.put("message","数据库操作错误,修改未完成");
			return response;
		}
	}
	
	
	/**
	 * 分页查询所有访客列表
	 * @param draw
	 * @param length 每页长度
	 * @param start 开始记录数
	 * @return 
	 */
	@RequestMapping("/selectVisitList")
	@ResponseBody
	public Response selectVisitList(String paramJson, int length, int start, int draw) {
		System.out.println(paramJson);
		JSONObject object = JSONObject.fromObject(paramJson);
		int currentPage=start/length+1;
		String name="";
		if(object.has("name")) {
			name=object.getString("name");
		}
		
		String mobile="";
		if(object.has("mobile")) {
			mobile=object.getString("mobile");
		}
		
		String pap_number="";
		if(object.has("pap_number")) {
			pap_number=object.getString("pap_number");
		}
		Page<GateExtraUserInfoDto>page=new Page<GateExtraUserInfoDto>(currentPage);
		page.setPageSize(length);

		Page<GateExtraUserInfoDto>result=this.getService().getGateVisitService().getGateVisitListByPage(page,name,mobile,pap_number);
		System.out.println(result);
		SimpleDateFormat sdf; 
		for(GateExtraUserInfoDto visit:result.getResultRows()){
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(visit.getEntry_date() !=null && !"".equals(visit.getEntry_date())){
				visit.setEnter(sdf.format(visit.getEntry_date()));
			}else{
				visit.setEnter("");
			}
			if(visit.getLeave_date() !=null && !"".equals(visit.getLeave_date())){
				visit.setLeave(sdf.format(visit.getLeave_date()));
			}else{
				visit.setLeave("");
			}
			
			if(visit.getRegistration_date() !=null && !"".equals(visit.getRegistration_date())){
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				visit.setRegistration(sdf.format(visit.getRegistration_date()));
			}else{
				visit.setRegistration("");
			}
		}
		Response response=Response.OK();
		
		response.put("recordsTotal", result.getTotalRow());
        response.put("recordsFiltered", result.getTotalRow());
        response.setData(result.getResultRows());
        response.put("recordsFiltered", result.getTotalRow());
        response.put("draw", draw);
        response.put("total", result.getTotalRow());
		return response;
		
	}
}	