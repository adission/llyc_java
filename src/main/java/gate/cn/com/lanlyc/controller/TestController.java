package gate.cn.com.lanlyc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.core.util.Response;

@Controller
@RequestMapping("/test")
public class TestController {
	@RequestMapping("/test1")
	@ResponseBody
	  public Response printHello(ModelMap model) {
		 System.out.println("-----请求json数据--------");
//			Shop shop = new Shop();
//			shop.setName("hi");
//			shop.setStaffName(new String[]{"mkyong1", "mkyong2"});
		   Map < String , Object > jsonMap = new HashMap< String , Object>();
		   jsonMap.put("a",1);
		   jsonMap.put("b","");
		   jsonMap.put("c",null);
		   jsonMap.put("d","zhenghuasheng");

//		   String str = JSONObject.toJSONString(jsonMap);
		   Response response = Response.OK();
		   response.put("data", jsonMap);	
		   return response;
	   }
		@RequestMapping("/test2")

	   public String printHello2(ModelMap model) {
			System.out.println("hi");
//		      model.addAttribute("message", "Hello Spring MVC Framework!");
		   model.addAttribute("message", "Hello liyangfu!");

	      return "hello";
	   }
}
