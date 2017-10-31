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
import pos.cn.com.lanlyc.core.dto.PosBaseStationDto;
import pos.cn.com.lanlyc.core.po.PosBaseStation;
@Controller
@RequestMapping("/Pos")
public class PosBaseStationController  extends BaseController{
	
	
	/**
	 * 查询基站列表
	 * @param paramJson 查询指定图层上指定类型的基站列表
	 * @return 
	 */
   @RequestMapping("/selectBaseStation")
   @ResponseBody
   public Response selectBaseStationList(String paramJson){
	   JSONObject object=JSONObject.fromObject(paramJson);
	   Response response = Response.newResponse();
	   String type="";
		if(object.has("types")) {
			type=object.getString("types");
		}

	   String layer_id=object.getString("layer_id");
		List<PosBaseStationDto>bs=null;
		try {
			
			bs=this.getService().getPosBaseStationService().selectBaseStationList(type,layer_id);
			if(bs!=null){
				System.out.println(bs.get(0).getLayerName());
				response.ok(bs);
			}else{
				response.put("code","0");
				 response.put("message","本图层没有查找到基站");
			}
			return response;
			
		} catch (Exception e) {
          e.getStackTrace();
          response.put("code","-1");
          response.put("message","数据库操作异常");
          return response;
		}
   }
   
   /**
	 * 保存一个基站到基站列表中
	 * @param paramJson 一个基站的信息
	 * @return 
	 */
  @RequestMapping("/saveBaseStation")
  @ResponseBody
  public Response saveBaseStation(String paramJson){
	  System.out.println("开始存吧");
	   JSONObject object=JSONObject.fromObject(paramJson);
	   //JSONObject object=JSONObject.parseObject(paramJson);
		//还需进行非空 不重复等的判断
		if(!object.containsKey("id")) 
		{
			return Response.PARAM_ERROR();
		}
		
		/*if(!object.containsKey("mc")) 
		{
			return Response.PARAM_ERROR();
		}*/
	   
		if(!object.containsKey("type")) 
		{
			return Response.PARAM_ERROR();
		}
		
		/*if(!object.containsKey("station_id")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		if(!object.containsKey("geo_x")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("geo_y")) 
		{
			return Response.PARAM_ERROR();
		}
		
		/*if(!object.containsKey("ip")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		if(!object.containsKey("layer_id")) 
		{
			return Response.PARAM_ERROR();
		}
		
		/*if(!object.containsKey("RepairRSSI")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("one_metre_RSSI")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("decay_rate")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("port")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("yxjl")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("xgjz")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("xzjl")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		if(!object.containsKey("status")) 
		{
			return Response.PARAM_ERROR();
		}
		
		String id=object.getString("id");
		//String mc=object.getString("mc");
		String type=object.getString("type");
		//String station_id=object.getString("station_id");
		String geo_x=object.getString("geo_x");
		String geo_y=object.getString("geo_y");
		//String ip=object.getString("ip");
		String layer_id=object.getString("layer_id");
		/*String RepairRSSI=object.getString("RepairRSSI");
		String one_metre_RSSI=object.getString("one_metre_RSSI");
		String decay_rate=object.getString("decay_rate");
		String port=object.getString("port");
		String yxjl=object.getString("yxjl");
		String xgjz=object.getString("xgjz");
		String xzjl=object.getString("xzjl");*/
		String status=object.getString("status");
		
		//通过基站id来判断基站是否已存在
		PosBaseStation posBaseStation=this.getService().getPosBaseStationService().getPosBaseStationDao().getPosBaseStationById(id);
		if(posBaseStation != null) 
		{
			Response response=Response.ERROR(-1, "已存在相同id的基站");
			return response;
		}
	   
       PosBaseStation ps=new PosBaseStation();
       ps.setId(id);
       //ps.setMc(mc);
       ps.setType(type);
       //ps.setStation_id(station_id);
       ps.setGeo_x(geo_x);
       ps.setGeo_y(geo_y);
       //ps.setIp(ip);
       ps.setLayer_id(layer_id);
       /*ps.setRepairRSSI(RepairRSSI);
       ps.setOne_metre_RSSI(one_metre_RSSI);
       ps.setDecay_rate(decay_rate);
       ps.setPort(port);
       ps.setYxjl(yxjl);
       ps.setXgjz(xgjz);
       ps.setXzjl(xzjl);*/
       ps.setStatus(status);
       int num=this.getService().getPosBaseStationService().saveBaseStation(ps);
       if(num<=0){
    	   Response response=Response.PARAM_ERROR();
		   response.put("code","300");
		   response.put("message","数据库操作错误,新增未完成");
		   return response;
       } 
       Response response = Response.newResponse();
       response.put("code",200);
       response.put("message", "新增成功");
	   return  response;
  }
  
  /**
 	 * 根据基站id，查询基站信息
 	 * 
 	 * @return
 	 */
 	@RequestMapping("/selectBaseStationById")
 	@ResponseBody
 	public Response selectWorkerById(String paramJson) {	
 		System.out.println(paramJson);
 		JSONObject object=JSONObject.fromObject(paramJson);
 		if(object==null||"".equals(object)) {
 			Response response=Response.PARAM_ERROR();
 			return response;
 		}
 		String id=object.getString("id");
 		PosBaseStationDto posBaseStation=this.getService().getPosBaseStationService().getBaseStationById(id);
 		System.out.println(posBaseStation);
 		
 		Response response=Response.OK();
 		response.put("data", posBaseStation);
 		return response;
 				
 	}
}
