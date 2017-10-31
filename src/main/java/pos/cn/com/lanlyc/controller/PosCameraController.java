package pos.cn.com.lanlyc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author hucong
 * 摄像头信息
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.dto.PosCameraDto;
import pos.cn.com.lanlyc.core.dto.PosTrajectoryDto;
import pos.cn.com.lanlyc.core.service.PosCameraService;
import pos.cn.com.lanlyc.core.service.PosTrajectoryService;
import video.cn.com.lanlyc.core.po.VidiconInfo;
import video.cn.com.lanlyc.core.service.VidiconService;

@Controller
@RequestMapping("/Pos")
public class PosCameraController extends BaseController {
	@Autowired
	private PosCameraService posCameraService;
	@Autowired
	private PosTrajectoryService posTrajectoryService;
	@Autowired
	private VidiconService vidiconService;
	
	/**
	 * 分页查询所有的/某一个图层的摄像头列表
	 * @param draw
	 * @param length 每页长度
	 * @param start 开始记录数
	 * @return 
	 */
	@RequestMapping("/selectCameraList")
	@ResponseBody
	public Response selectCameraList(String paramJson,Integer start,Integer length,Integer draw) {
		System.out.println(paramJson);
		JSONObject object = JSONObject.fromObject(paramJson);
		int currentPage=start/length+1;
		String keyWords="";
		if(object.has("keyWords")) {
			keyWords=object.getString("keyWords");
		}
		Page<PosCameraDto>page=new Page<PosCameraDto>(currentPage);
		page.setPageSize(length);
		String layer_id="";
		if(object.containsKey("layer_id")){
			layer_id=object.getString("layer_id");
			if("全部".equals(layer_id)){ //注意此处不能用==
				layer_id="";
			}/*else{
				//调用根据图层名layer_name获得图层id layer_id的方法
				layer_id="2";//此处为防止报错   给一个默认值
			}*/
		}
		Page<PosCameraDto>result=this.getService().getPosCameraService().getPosCameraListByPage(page,layer_id,keyWords);
		Response response=Response.OK();
		/*List<PosCameraDto> posCamera=result.getResultRows();
		for(int i=0;i<posCamera.size();i++){
			if((object.containsKey("layer_id") && "全部".equals(object.getString("layer_id")))||!object.containsKey("layer_id")){
				//调用通过图层id获取图层名的方法
				posCamera.get(i).setLayer_id("工作区地图-3F");
			}else if(object.containsKey("layer_id") && !"全部".equals(object.getString("layer_id"))){
				posCamera.get(i).setLayer_id(object.getString("layer_id"));
			}
		}*/
		
		response.put("recordsTotal", result.getTotalRow());
        response.put("recordsFiltered", result.getTotalRow());
        response.setData(result.getResultRows());
        response.put("recordsFiltered", result.getTotalRow());
        response.put("draw", draw);
        response.put("total", result.getTotalRow());
		return response;
		
	}
	
	
	/**
	 * 通过图层id 查找这个图层里面所有的摄像头
	 * 
	 * @param paramJson
	 * @return
	 */
	@RequestMapping("/selectCamera")
	@ResponseBody
	public Response selectCameraByLayerId(String paramJson) {
		System.out.println(paramJson);
		JSONObject object = JSONObject.fromObject(paramJson);
		Response response = Response.newResponse();
		String layer_id = object.getString("layer_id");
		List<VidiconInfo> camera = null;
		try {
			if (layer_id != null && !"".equals(layer_id)) {
				camera = this.getService().getPosCameraService().selectCameraByLayerId(layer_id);
				System.out.println(camera);
				if (camera != null) {
					response.ok(camera);
				} else {
					response.put("code", "0");
					response.put("message", "本图层没有查找到摄像头");
				}
			} else {
				System.out.println("从前端传来的参数有问题");
				return Response.PARAM_ERROR();
			}
		} catch (Exception e) {
			e.getStackTrace();
			response.put("message", "数据库操作异常");
		}
		return response;

	}
	
	
	/**
	 * 保存一个摄像头到到摄像头表中 (保存视频那边传过来的摄像头)
	 * @param paramJson 一个摄像头的信息
	 * @return 
	 */
/*  @RequestMapping("/saveCamera")
  @ResponseBody*/
  public Response saveCamera(String paramJson){
	    JSONObject object=JSONObject.fromObject(paramJson);
		
		if(!object.containsKey("id")) 
		{
			return Response.PARAM_ERROR();
		}
		
		/*if(!object.containsKey("name")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		/*if(!object.containsKey("numbering")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		/*if(!object.containsKey("geo_x")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("geo_y")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		if(!object.containsKey("layer_id")) 
		{
			return Response.PARAM_ERROR();
		}
			
		
		String id=object.getString("id");
		//String name=object.getString("name");
		//String numbering=object.getString("numbering");
		//String geo_x=object.getString("geo_x");
		//String geo_y=object.getString("geo_y");
		String layer_id=object.getString("layer_id");

		
		//通过摄像头id来判断摄像头是否已存在
		VidiconInfo camera=this.getService().getPosCameraService().getPosCameraDao().getPosCameraById(id);
		if(camera != null) 
		{
			Response response=Response.ERROR(-1, "已存在相同id的摄像头");
			return response;
		}
		
		
		VidiconInfo pc=new VidiconInfo();
		pc.setId(id);
		//pc.setName(name);
		//pc.setNumbering(numbering);
		//pc.setGeo_x(geo_x);
		//pc.setGeo_y(geo_y);
		pc.setLayer_id(layer_id);

       
       int num=this.getService().getPosCameraService().savePosCamera(pc);
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
	 * 修改摄像头的信息
	 * @param paramJson 一个摄像头的信息
	 * @return 
	 */
	@RequestMapping("/updateCamera")
	@ResponseBody
	public Response updateCamera(String paramJson){
        JSONObject object=JSONObject.fromObject(paramJson);
		if(!object.containsKey("id")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("xy")) 
		{
			return Response.PARAM_ERROR();
		}
		
		/*if(!object.containsKey("geo_x")) 
		{
			return Response.PARAM_ERROR();
		}
		
		if(!object.containsKey("geo_y")) 
		{
			return Response.PARAM_ERROR();
		}*/
		
		if(!object.containsKey("layer_id")) 
		{   System.out.println("layer_id没有");
			return Response.PARAM_ERROR();
		}
		
		String id=object.getString("id");
		//String geo_x=object.getString("geo_x");
		//String geo_y=object.getString("geo_y");
		String xy=object.getString("xy");
		System.out.println("xy的坐标是              :"+xy);
		String[] strs=xy.split(",");
		String geo_x=strs[0].substring(1);
		String geo_y=strs[1].substring(0, strs[1].length()-1);
		String layer_id=object.getString("layer_id");
		
		//通过摄像头id来判断摄像头是否已存在
		VidiconInfo camera=this.getService().getPosCameraService().getPosCameraDao().getPosCameraById(id);
  		if(camera == null) 
  		{
  			Response response=Response.ERROR(-1, "不存在指定id的摄像头");
  			return response;
  		}
		
		//如果摄像头坐标未发生改变，就不修改
		VidiconInfo camera2=this.getService().getPosCameraService().getPosCameraDao().getPosCamera(id,geo_x,geo_y,layer_id);
		if(camera2 != null) 
		{
			Response response=Response.ERROR(-1, "摄像头位置未改变");
			return response;
		}		
		
		VidiconInfo pc=new VidiconInfo();
		/*pc.setId(id);
		pc.setGeo_x(geo_x);
		pc.setGeo_y(geo_y);
		pc.setLayer_id(layer_id);
		int num=this.getService().getPosCameraService().updateCamera(pc);*/
		int num=this.getService().getPosCameraService().updateCamera(id,geo_x,geo_y,layer_id);
		if(num<=0){
	    	   Response response=Response.PARAM_ERROR();
			   response.put("code",300);
			   response.put("message","数据库操作错误,修改未完成");
			   return response;
	       } 
		Response response = Response.newResponse();
        response.put("code",200);
        response.put("message", "修改成功");
	    return  response;
	}
	
	/**
	 * 通过id删除摄像头的信息
	 * @param paramJson 一个摄像头的信息
	 * @return 
	 */
	@RequestMapping("/deleteCamera")
	@ResponseBody
	public Response deleteCamera(String paramJson){
        JSONObject object=JSONObject.fromObject(paramJson);
        if(!object.containsKey("id") || "".equals(object.getString("id"))) {
			return Response.PARAM_ERROR();
		}
        
        String id = object.getString("id");
        
        //通过摄像头id来判断摄像头是否已存在
        VidiconInfo camera=this.getService().getPosCameraService().getPosCameraDao().getPosCameraById(id);
  		if(camera == null) 
  		{
  			Response response=Response.ERROR(-1, "不存在指定id的摄像头");
  			return response;
  		}
        
		int num=this.getService().getPosCameraService().updateCamera(id);
		if(num<=0){
	    	   Response response=Response.PARAM_ERROR();
			   response.put("code",300);
			   response.put("message","数据库操作错误,删除未完成");
			   return response;
	       } 
		Response response = Response.newResponse();
	       response.put("code",200);
	       response.put("message", "删除成功");
		   return  response;
	}
	
	
	/**
	 * 通过人员信息(xy坐标、图层id)来查找最近的摄像头的信息
	 * @param paramJson 人员的信息
	 * @return 
	 */
	@RequestMapping("/selectCameraByUser")
	@ResponseBody
	public Response selectCameraByUser(String paramJson){
		Response response = Response.newResponse();
        JSONObject object=JSONObject.fromObject(paramJson);
        System.out.println(paramJson+"    sss");
        /*if((object.containsKey("geo_x") && !"".equals(object.getString("geo_x")) && object.containsKey("geo_x") 
        		&& !"".equals(object.getString("geo_x")) && object.containsKey("layer_id") && !"".equals(object.getString("layer_id")))){*/
         if(object.containsKey("layer_id") && !"".equals(object.getString("layer_id"))||object.containsKey("id") && !"".equals(object.getString("id"))){
        	 String id = object.getString("id");
        	 //通过人员id获取人员最后的位置
        	 List<String> userId =new ArrayList<String>(); 
        	 userId.add(id);
        	 List<PosTrajectoryDto> pt= posTrajectoryService.getCurrentPos(userId);
        	 double geo_x=pt.get(0).getX().doubleValue();
        	 double geo_y=pt.get(0).getY().doubleValue();
        	 System.out.println("geo_x   :"+geo_x+" geo_y    :"+geo_y);
        	/*double geo_x=Double.parseDouble(object.getString("geo_x"));
        	double geo_y=Double.parseDouble(object.getString("geo_y"));*/
        	//通过图层id获取图层中所有的摄像头
        	String layer_id = object.getString("layer_id");
        	List<VidiconInfo> cameras = posCameraService.selectCameraByLayerId(layer_id);       	
        	double c_geo_x1;
        	double c_geo_y1;
        	double c_geo_x2;
        	double c_geo_y2;
        	double d1=0.0;
        	double d2=0.0;
        	VidiconInfo camera;
        	String camera_id;
        	int j=0;
    		Object [] ids=new Object[1];
        	if(cameras!=null && cameras.size()>=2){
        		camera=cameras.get(0);
        		c_geo_x1=Double.parseDouble(cameras.get(0).getGeo_x());
   			    c_geo_y1=Double.parseDouble(cameras.get(0).getGeo_y());
   			    d1=Math.sqrt(Math.pow((geo_x-c_geo_x1), 2)+Math.pow((geo_y-c_geo_y1), 2));
        		for(int i=1;i<cameras.size();i++){
        			 c_geo_x2=Double.parseDouble(cameras.get(i).getGeo_x());
        			 c_geo_y2=Double.parseDouble(cameras.get(i).getGeo_y());
        			 d2=Math.sqrt(Math.pow((geo_x-c_geo_x2), 2)+Math.pow((geo_y-c_geo_y2), 2));
        			 //System.out.println(d1+"         "+d2);
        			if(d1>d2){
        				d1=d2;
        				j=i;
        				camera=cameras.get(i);
        			}else{
        				camera=cameras.get(j);
        			}	
        		}        		
        		camera_id=camera.getId();
        		ids[0]=camera_id;
        		VidiconInfo vidiconInfo=vidiconService.getPartFieldFromIdService(ids).get(0);
        		response.ok(vidiconInfo);
        	}else if(cameras!=null && cameras.size()==1){
        		camera=cameras.get(0);
        		camera_id=camera.getId();
        		ids[0]=camera_id;
        		VidiconInfo vidiconInfo=vidiconService.getPartFieldFromIdService(ids).get(0);
        		response.ok(vidiconInfo);
        	}else{
        		response.put("code",-1);
        		response.put("message","没找到摄像头");
        	}
        }else{
        	return Response.PARAM_ERROR();
        }
		return  response;
	}
	
}

















