package gate.cn.com.lanlyc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateUserClassDataAnalysisView;
import gate.cn.com.lanlyc.core.po.GateWorktypsDataAnalysisView;

@Controller
@RequestMapping("/DataAnalysisView")
public class DataAnalysisViewController extends BaseController {
	
	/**
	 * 获取人员总数
	 * 
	 * 
	 */
	@RequestMapping("/getotalperson")
	@ResponseBody
	public Response getTotalperson() 
	{
		Response response=Response.OK();
		Map<String,Object> totalperson=new HashMap<String,Object>();
		List<GateUserClassDataAnalysisView> UserClass_totalperson=this.getService().getDatanalysisiewservice().getDataAnalysisViewDao().getUserClass_totalperson();
		List<GateWorktypsDataAnalysisView> Worktyps_totalperson=this.getService().getDatanalysisiewservice().getDataAnalysisViewDao().getWorktyps_totalperson();
		totalperson.put("UserClass_totalperson", UserClass_totalperson);
		totalperson.put("Worktyps_totalperson", Worktyps_totalperson);
		response.setData(totalperson);
		return response;
	}

}
