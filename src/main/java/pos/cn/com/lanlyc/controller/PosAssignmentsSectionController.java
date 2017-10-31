package pos.cn.com.lanlyc.controller;
import java.util.List;

/**
 * @author hucong
 * 查询基站列表
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.core.util.Response;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.dto.PosAssignmentsSectionDto;
import pos.cn.com.lanlyc.core.po.PosAssignmentsSection;

@Controller
@RequestMapping("/Pos")
public class PosAssignmentsSectionController extends BaseController{
	/**
	 * 根据类型查询区域列表
	 * @param paramJson 查询指定图层上指定类型的区域列表
	 * @return 
	 */
	
	@RequestMapping("/selectassignmentsSection")
	   @ResponseBody
	   public Response selectAssignmentsSectionList(String paramJson){
		   JSONObject object=JSONObject.fromObject(paramJson);
		   Response response = Response.newResponse();
		   String type="";
		   if(object.containsKey("type")) 
			{
			   type=object.getString("type");
			}
		   String layer_id=object.getString("layer_id");
			List<PosAssignmentsSectionDto>bs=null;
			try {
				  if(layer_id!=null && !"".equals(layer_id)){		
					 bs=this.getService().getPosAssignmentsSectionService().selectAssignmentsSectionList(type,layer_id);
					 if(bs.isEmpty()||bs.size()<=0){
						 response.put("code","0");
						 response.put("message","本图层没有查找到区域");
					 }else{
						 response.ok(bs);
					 } 
				  }else{
					  System.out.println("有问题啦");
					 return Response.PARAM_ERROR();
				  }
			} catch (Exception e) {
	          e.getStackTrace();
	          response.put("message","数据库操作异常");
			}
		   return response;
	   }
	
	
	/**
	 * 保存一个区域到区域列表中
	 * @param paramJson 一个区域的信息
	 * @return 
	 */
  @RequestMapping("/saveAssignmentsSection")
  @ResponseBody
  public Response saveAssignmentsSection(String paramJson){
	   JSONObject object=JSONObject.fromObject(paramJson);
	   //JSONObject object=JSONObject.parseObject(paramJson);
		
		if(!object.containsKey("id")) 
		{
			return Response.PARAM_ERROR();
		}
		
		/*if(!object.containsKey("name")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		if(!object.containsKey("layer_id")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("type")) 
		{
			return Response.PARAM_ERROR();
		}
		
		/*if(!object.containsKey("numOfPersonnel")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		if(!object.containsKey("startLongitude")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("startLatitude")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("endLongitude")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("endLatitude")) 
		{
			return Response.PARAM_ERROR();
		}
		/*
		if(!object.containsKey("attention")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("resPerId")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("instructions")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("secColor")) 
		{
			return Response.PARAM_ERROR();
		}
		
			   */
		
		
		
		String id=object.getString("id");
		//String name=object.getString("name");
		String layer_id=object.getString("layer_id");
		String type=object.getString("type");
		/*String numOfPersonnels=object.getString("numOfPersonnel");*/
		String startLongitude=object.getString("startLongitude");
		String startLatitude=object.getString("startLatitude");
		String endLongitude=object.getString("endLongitude");
		String endLatitude=object.getString("endLatitude");
		/*String attention=object.getString("attention");
		String resPerId=object.getString("resPerId");
		String instructions=object.getString("instructions");
		String secColor=object.getString("secColor");
		int numOfPersonnel=Integer.valueOf(numOfPersonnels);*/
		
		//通过基站id来判断基站是否已存在
		PosAssignmentsSection posAssignmentsSection=this.getService().getPosAssignmentsSectionService().getPosAssignmentsSectionDao().getPosAssignmentsSectionById(id);
		if(posAssignmentsSection != null) 
		{
			Response response=Response.ERROR(-1, "已存在相同id的区域");
			return response;
		}
		
		
       PosAssignmentsSection ps=new PosAssignmentsSection();
       ps.setId(id);
       //ps.setName(name);
       ps.setLayer_id(layer_id);
       ps.setType(type);
       /*ps.setNumOfPersonnel(numOfPersonnel);*/
       ps.setStartLongitude(startLongitude);
       ps.setStartLatitude(startLatitude);
       ps.setEndLongitude(endLongitude);
       ps.setEndLatitude(endLatitude);
     /*  ps.setAttention(attention);
       ps.setResPerId(resPerId);
       ps.setInstructions(instructions);
       ps.setSecColor(secColor);
       */
       
       int num=this.getService().getPosAssignmentsSectionService().saveAssignmentsSection(ps);
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
}
