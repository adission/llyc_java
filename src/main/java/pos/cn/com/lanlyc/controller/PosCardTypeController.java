package pos.cn.com.lanlyc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.core.util.Response;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.po.PosCardType;

/**
 * 
 * @author cjt
 * 人员类型controller
 */
@Controller
@RequestMapping("/cardtype")
public class PosCardTypeController extends BaseController{

	/**
	 * 查询人员类型列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Response getAllType() {
		
		List<PosCardType> res =this.getService().getPosCardTypeService().getDao().getAll();
		Response re = Response.newResponse();
		re.ok(res);
		return re;
	}
	
	/**
	 * 修改颜色
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Response updateColor(String paramJson) {
		JSONObject jb = JSONObject.fromObject(paramJson);
//		List<PosCardType> modelList = new ArrayList<>();
		if(jb.has("workcolor")) {
			PosCardType p = new PosCardType();
			p.setId(1);
//			p.setTypeName("工作人员");
			p.setColor(jb.getString("workcolor"));
			this.getService().getPosCardTypeService().getDao().update(p,false);
		}
		if(jb.has("outcolor")) {
			PosCardType p = new PosCardType();
//			p.setTypeName("访客");
			p.setId(0);
			p.setColor(jb.getString("outcolor"));
			this.getService().getPosCardTypeService().getDao().update(p,false);
		}
		
		return Response.OK();
	}
}
