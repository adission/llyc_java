package video.cn.com.lanlyc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import cn.jiguang.common.utils.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import video.cn.com.lanlyc.core.dto.VidiconInfoDto;
import video.cn.com.lanlyc.core.po.VidiconInfo;
import video.cn.com.lanlyc.core.sdk.HCNetSDK;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_DEVICEINFO_V30;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_IPPARACFG;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_WORKSTATE_V30;
import video.cn.com.lanlyc.core.service.NetSdkLogin;
import gate.cn.com.lanlyc.controller.BaseController;



/**
 * @author:chupeng
 * @version:v1.0
 * @discription:摄像头控制层
 * @date:2017年9月11日 下午3:37:24
 */
@Controller
@RequestMapping("/vidicon")
public class VidiconController extends BaseController{
	VidiconInfo vidicon = new VidiconInfo();
	Response response = Response.OK();
	
	
	@Autowired
	NetSdkLogin sdk;
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:摄像头的新增接口controller层
	 * @param:paramJson：新增的摄像头信息json串{"vidicon_name":xxx,"vidicon_ip":xxx,"vidicon_port":xxx,"vidicon_username":xxx,
	 * 				"vidicon_password":xxx,"vidicon_desc":xxx,"vidicon_type":xxx,"whether_important":xxx}
	 * @return:状态码以及提示信息
	 * @url:../vidicon/addVidicon
	 * @date:2017年9月11日 下午3:26:03
	 */
	@RequestMapping(value = "/addVidicon", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Response addVidicon(String paramJson){
		String usereId = this.getCurrentuserid();
		
		
		JSONObject object=JSONObject.fromObject(paramJson);
		if(object.containsKey("vidicon_name")){
			if(StringUtils.isNotEmpty(object.getString("vidicon_name"))){
				vidicon.setVidicon_name(object.getString("vidicon_name"));
			}
		}
		if(object.containsKey("vidicon_ip")){
			if(StringUtils.isNotEmpty(object.getString("vidicon_ip"))){
				vidicon.setVidicon_ip(object.getString("vidicon_ip"));
			}
			
		}
		if(object.containsKey("vidicon_port")){
			if(StringUtils.isNotEmpty(object.getString("vidicon_port"))){
				vidicon.setVidicon_port(Integer.parseInt(object.getString("vidicon_port")));
			}
		}
		if(object.containsKey("vidicon_username")){
			if(StringUtils.isNotEmpty(object.getString("vidicon_username"))){
				vidicon.setVidicon_username(object.getString("vidicon_username"));
			}
		}
		
		if(object.containsKey("vidicon_password")){
			if(StringUtils.isNotEmpty(object.getString("vidicon_password"))){
				vidicon.setVidicon_password(object.getString("vidicon_password"));
			}
		}
		if(object.containsKey("vidicon_desc")){
			vidicon.setVidicon_desc(object.getString("vidicon_desc"));
		}
		if(object.containsKey("vidicon_type")){
			if(StringUtils.isNotEmpty(object.getString("vidicon_type"))){
				vidicon.setVidicon_type(Integer.parseInt(object.getString("vidicon_type")));
			}
		}
		if(object.containsKey("whether_important")){
			if(object.getString("whether_important").equals("1")){
				vidicon.setWhether_important(true);
			}else{
				vidicon.setWhether_important(false);
			}
		}
		vidicon.setId(DataUtils.getUUID());
		Map<String, Object> map = this.getVideoService().getVidiconService().addVidiconService(vidicon,usereId);
		response.setData(map);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:删除摄像头controller层接口
	 * @param:paramJson：摄像头id字符串数组["id1","id2",...]
	 * @return:成功或失败的状态码以及消息json串
	 * @url:../vidicon/deleteVidicon
	 * @date:2017年9月11日 下午4:42:48
	 */
	@RequestMapping("/deleteVidicon")
	@ResponseBody
	@Transactional
	public Response deleteVidicon(String paramJson){
		try{
			JSONObject object = JSONObject.fromObject(paramJson);
			JSONArray jsonArray = JSONArray.fromObject(object.get("id")); 
			Object [] obj = jsonArray.toArray();
			this.getVideoService().getVidiconService().deleteVidiconService(obj);
		}catch(Exception e){
			response.ERROR(500, "操作错误");
		}
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:修改摄像头相关信息controller层接口
	 * @param:paramJson：摄像头实体组成的json串{"id":xxx,"vidicon_name":xxx,"vidicon_desc":xxx,
	 * 									"vidicon_type":xxx,"whether_important":xxx}
	 * @return:状态码以及信息
	 * @url:../vidicon/updateVidicon
	 * @date:2017年9月11日 下午4:53:17
	 */
	@RequestMapping("/updateVidicon")
	@ResponseBody
	public Response updateVidicon(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", object.getString("id"));
		map.put("vidicon_name", object.getString("vidicon_name"));
		map.put("vidicon_desc", object.getString("vidicon_desc"));
		map.put("vidicon_type", object.getString("vidicon_type"));
		if(object.getString("whether_important").equals("1")){
			map.put("whether_important", true);
		}else{
			map.put("whether_important", false);
		}
		int data = this.getVideoService().getVidiconService().updateVidiconService(map);
		if(data <= 0){
			response.ERROR(500, "操作错误");
		}
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:全部查询（或模糊查询）摄像头的接口
	 * @param:paramJson:检索关键字组成的json串，draw、start、length：固定写法，无须传递{"keyword":xxx}
	 * @return:分页后的摄像头实体信息
	 * @url:../vidicon/selectVidicon
	 * @date:2017年9月11日 上午11:27:45
	 */
	@RequestMapping(value = "/selectVidicon", method=RequestMethod.POST)
	@ResponseBody
	public Response selectVidicon(String paramJson,int draw,int start,int length){
		JSONObject object = JSONObject.fromObject(paramJson);
        System.out.println("传入参数：" + object);
        String keyWord = object.getString("keyword");
        //length = Integer.parseInt(object.getString("length"));
        
        int currentPage = start / length + 1;
        Page<VidiconInfoDto> page = new Page<VidiconInfoDto>(currentPage);
        page.setPageSize(length);
        Page<VidiconInfoDto> data = this.getVideoService().getVidiconService().getVidiconServiceListByPage(page, keyWord);
        response.put("recordsTotal", data.getTotalRow());
        response.put("recordsFiltered", data.getTotalRow());
        response.setData(data.getResultRows());
        response.put("recordsFiltered", data.getTotalRow());
        response.put("draw", draw);
        return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:通过摄像头id获取摄像头单条数据controller层接口
	 * @param:paramJson：摄像头id组成的json串{"id":xxx}
	 * @return:摄像头单条数据实体
	 * @url:../vidicon/getOneVidicon
	 * @date:2017年9月11日 下午5:14:11
	 */
	@RequestMapping(value = "/getOneVidicon", method=RequestMethod.POST)
	@ResponseBody
	public Response getOneVidiconById(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		String id = object.getString("id");
		VidiconInfo vidiconInfo = this.getVideoService().getVidiconService().getVidiconById(id);
		response.setData(vidiconInfo);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:自动获取是否重点的所有的摄像头id和名称（不分页）controller层接口
	 * @param:无
	 * @return:所有的摄像头实体
	 * @url:../vidicon/getAllVidicon
	 * @date:2017年9月18日 下午5:51:01
	 */
	@RequestMapping(value = "/getAllVidicon", method=RequestMethod.POST)
	@ResponseBody
	public Response getAllVidiconController(String paramJson){
		int whether_important = 2;
		JSONObject object = JSONObject.fromObject(paramJson);
		if(object.containsKey("whether_important")){
			whether_important = object.getInt("whether_important");
		}
		List<VidiconInfo> vidicon = this.getVideoService().getVidiconService().getAllVidiconService(whether_important);
		response.setData(vidicon);
		return response;
	}
	
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:通过摄像头id组成的数组获取所有id的ip、端口号、用户名、密码controller层接口
	 * @param:paramJson：id组成的数组json串
	 * @return:包含返回状态码及其相关摄像头的信息
	 * @url:../vidicon/getVidiconPartField
	 * @date:2017年10月13日 下午2:22:38
	 */
	@RequestMapping(value = "/getVidiconPartField", method=RequestMethod.POST)
	@ResponseBody
	public Response getPartFieldFromId(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		JSONArray jsonArray = JSONArray.fromObject(object.get("id")); 
		Object [] obj = jsonArray.toArray();
		List<VidiconInfo> vidiconList = this.getVideoService().getVidiconService().getPartFieldFromIdService(obj);
		response.setData(vidiconList);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:通过摄像头ip获取摄像头单条数据controller层接口
	 * @param:paramJson：id组成的数组json串
	 * @return:包含返回状态码及其相关摄像头的信息
	 * @url:../vidicon/getVidiconByIp
	 * @date:2017年10月18日 下午2:22:38
	 */
	@RequestMapping(value = "/getVidiconByIp", method=RequestMethod.POST)
	@ResponseBody
	public Response getVidiconByIpController(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		String ip = object.getString("ip");
		VidiconInfo vidicon = this.getVideoService().getVidiconService().getVidiconByIp(ip);
		response.setData(vidicon);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:获取所有nvr信息的controller层接口
	 * @param:无
	 * @return:包含返回状态码及其相关nvr的信息
	 * @url:../vidicon/getAllNvr
	 * @date:2017年10月19日 下午9:48:38
	 */
	@RequestMapping(value = "/getAllNvr", method=RequestMethod.POST)
	@ResponseBody
	public Response getAllNvr(){
		List<Object> list = this.getVideoService().getVidiconService().getAllNvrService();
		response.setData(list);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:获取从指定位置开始、且指定数量的摄像头信息
	 * @param:paramJson：start：起始位置，size指定长度
	 * @return:相关摄像头的信息
	 * @url:../vidicon/getLimitVidicon
	 * @date:2017年10月27日 上午11:36:35
	 */
	@RequestMapping(value = "/getLimitVidicon", method=RequestMethod.POST)
	@ResponseBody
	public Response getLimitVidicon(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
        int start = Integer.parseInt(object.getString("start"));
        int size = Integer.parseInt(object.getString("size"));
        List<VidiconInfo> vis = this.getVideoService().getVidiconService().getLimitVidiconService(start, size);
        response.setData(vis);
        return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:判断摄像头名称是否重复
	 * @param:paramJson----vidicon_name：摄像头名称
	 * @return:rest----true:代表数据库已经存在该摄像头名称，false:代表数据库不存在该摄像头名称
	 * @url:../vidicon/judgeNameWhetherRepeat
	 * @date:2017年10月30日 上午9:47:19
	 */
	@RequestMapping(value = "/judgeNameWhetherRepeat", method=RequestMethod.POST)
	@ResponseBody
	public Response judgeNameWhetherRepeatCtrl(String vidicon_name){
		/*JSONObject object = JSONObject.fromObject(paramJson);
        String vidicon_name = object.getString("vidicon_name");*/
		boolean rest = this.getVideoService().getVidiconService().judgeNameWhetherRepeat(vidicon_name);
		if(rest){
			response.put("message", "该摄像头名称已存在！");
		}
		response.put("valid", !rest);
		return response;
	}
	
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:判断摄像头ip是否重复
	 * @param:paramJson----vidicon_ip：摄像头ip
	 * @return:rest----true:代表数据库已经存在该摄像头ip，false:代表数据库不存在该摄像头ip
	 * @url:../vidicon/judgeIpWhetherRepeat
	 * @date:2017年10月30日 上午9:53:19
	 */
	@RequestMapping(value = "/judgeIpWhetherRepeat", method=RequestMethod.POST)
	@ResponseBody
	public Response judgeIpWhetherRepeatCtrl(String vidicon_ip){
		/*JSONObject object = JSONObject.fromObject(paramJson);
        String vidicon_ip = object.getString("vidicon_ip");*/
		boolean rest = this.getVideoService().getVidiconService().judgeIpWhetherRepeat(vidicon_ip);
		if(rest){
			response.put("message", "该摄像头IP已存在！");
		}
		response.put("valid", !rest);
		return response;
	}

}
