package pos.cn.com.lanlyc.controller;
import java.util.List;

/**
 * @author hucong
 * 项目相关
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.core.util.Response;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.po.PosConstructionSite;

@Controller
@RequestMapping("/Pos")
public class PosConstructionSiteController extends BaseController{

	/**
	 * 查询项目信息
	 * @param paramJson 
	 * @return 
	 */
   @RequestMapping("/selectConstructionSite")
   @ResponseBody
   public Response selectConstructionSite(String paramJson){
	   JSONObject object=JSONObject.fromObject(paramJson);
	   Response response = Response.newResponse();
	   String id="";
		if(object.has("id")) {
			id=object.getString("id");
		}

		List<PosConstructionSite>pcs=null;
		try {
			
			pcs=this.getService().getPosConstructionSiteService().selectConstructionSite(id);
			if(pcs!=null){
				response.ok(pcs);
			}else{
				response.put("code","0");
				 response.put("message","没有查找到id为"+id+"的项目");
			}
			return response;
			
		} catch (Exception e) {
          e.getStackTrace();
          response.put("code","-1");
          response.put("message","数据库操作异常");
          return response;
		}
   }
   
}
