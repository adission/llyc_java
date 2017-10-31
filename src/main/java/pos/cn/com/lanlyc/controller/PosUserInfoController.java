package pos.cn.com.lanlyc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.core.util.Response;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.dto.UserDto;


@Controller
@RequestMapping("/Pos")
public class PosUserInfoController extends BaseController {
	
	/**
	 * 根据用户id，查询用户信息
	 * 
	 * @return
	 */
	@RequestMapping("/selectWorkerById")
	@ResponseBody
	public Response selectWorkerById(String paramJson) {	
		System.out.println(paramJson);
		JSONObject object=JSONObject.fromObject(paramJson);
		if(object==null||"".equals(object)) {
			Response response=Response.PARAM_ERROR();
			return response;
		}
		String id=object.getString("id");
		UserDto user=this.getService().getPosUserService().getUserById(id);
		Response response=Response.OK();
        if(user!=null){
    		response.put("data", user);
        }else{
            response.put("code", -1);
    		response.put("message", "获取人员失败");
        }
		return response;			
	}

}

