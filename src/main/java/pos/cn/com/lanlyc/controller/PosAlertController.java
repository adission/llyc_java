package pos.cn.com.lanlyc.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateUser;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.dto.PosAlertDto;
import pos.cn.com.lanlyc.core.po.PosAlert;

/**
 * 
 * @author cjt
 * 告警控制器
 *
 */
@Controller
@RequestMapping("/alert")
public class PosAlertController extends BaseController{

	/**
	 * 查询历史告警（datadable分页）
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Response historyAlert(String paramJson,Integer start,Integer length,Integer draw) {
		JSONObject jb = JSONObject.fromObject(paramJson);
		
		int currentPage = start/length+1;
		Page<PosAlertDto> page = new Page<>(currentPage);
		page.setPageSize(length);
		String keyword = "";
		String type = "";
		String status = "";
		if(jb.has("keyword")) {    //关键字
			keyword = jb.getString("keyword");
		}
		if(jb.has("type")) {       //告警类型
			type = jb.getString("type");
		}
		if(jb.has("status")) {     //告警处理状态
			status = jb.getString("status");
		}
		Page<PosAlertDto> data = this.getService().getPosAlertService().getHistory(page, keyword,type,status);
		Response response = Response.OK();
		response.put("recordsTotal", data.getTotalRow());
        response.put("total", data.getTotalRow());
        response.put("recordsFiltered", data.getTotalRow());
        response.setData(data.getResultRows());
        response.put("recordsFiltered", data.getTotalRow());
        response.put("draw", draw);
		return response;
		
	}
	
	/**
	 * 添加处理方法
	 * param
	 * context 处理方法
	 * userId 处理人
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Response update(String paramJson) {
		JSONObject jb = JSONObject.fromObject(paramJson);
		try {
			PosAlert pa = this.getService().getPosAlertService().getDao().get(jb.getString("id"));
			pa.setProcessMode(jb.getString("processMode"));
			GateUser user = (GateUser)this.getSession().getAttribute("loginUser");
			if(user!=null) {
				pa.setProcessUser(user.getId());
			}
			pa.setStatus("已处理");
			pa.setProcessTime(new Date());
			this.getService().getPosAlertService().getDao().update(pa);
			return Response.OK();
		} catch (Exception e) {
			// TODO: handle exception
			return Response.PARAM_ERROR();
		}
				
		
		
	}
	/**
	 * 查询所有未处理的报警(不分页)
	 */
	@RequestMapping("/all")
	@ResponseBody
	public Response getAllWarning() {
		Response re = Response.newResponse();
		try {
			List<PosAlert> list = this.getService().getPosAlertService().getAllWarning();
			re.ok(list);
		} catch (Exception e) {
			// TODO: handle exception
			re = Response.SERVER_ERROR();
		}
		return re;
	}
	
}
