package video.cn.com.lanlyc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.controller.BaseController;
import net.sf.json.JSONObject;
import video.cn.com.lanlyc.core.service.HolderControlService;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:关于云台控制相关的各个接口所在的Controller类(云台控制的各种操作必须建立在打开预览的基础上)
 * @date:2017年9月7日 下午2:32:50
 */
@Controller
@RequestMapping("/holderControl")
public class HolderControl extends BaseController {
	Response response=Response.OK();
	
	@Autowired
	HolderControlService holderService;
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:实时预览（1、初始化，2、注册，3、调用预览）
	 * @param:paramJson：预览参数json串	eg:{"userId":xxx,"ip":xxx,"port":xxx,"username":xxx,"password":xxx}
	 * 参数说明：	userId：摄像头唯一标识
	 * 			ip：局域网ip
	 * 			port：端口号
	 * 			username：注册用户名
	 * 			password：密码
	 * @return:状态码以及提示信息
	 * @url:../holderControl/preview
	 * @date:2017年9月7日 下午3:07:25
	 */
	@RequestMapping("/preview")
	@ResponseBody
	public Response realTimePreview(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		int userId = object.getInt("userId");
		String ip = object.getString("ip");
		int port = object.getInt("port");
		String username = object.getString("username");
		String password = object.getString("password");
		Map<String,Object> map = holderService.realTimePreview(userId, ip, (short)port, username, password);
		response.setData(map);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:云台转动控制接口
	 * @param:paramJson:转动控制参数json串	eg:{"userId":xxx,"command":xxx}
	 * 参数说明：    userId:摄像头注册用户唯一id,
	 * 		   command:云台控制命令21：上仰、22：下俯，23：左转，24：右转，25：上仰和左转，26：上仰和右转，27：下俯和左转，28：下俯和右转，29：左右自动扫描
	 * @return:成功：成功的状态码及信息的json串，失败：失败的状态码及信息json串
	 * @url:../holderControl/holderCtrl
	 * @date:2017年9月8日 上午10:59:19
	 */
	@RequestMapping("/holderCtrl")
	@ResponseBody
	public Response holderCtrl(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		int userId = Integer.parseInt(object.getString("userId"));
		int command = Integer.parseInt(object.getString("command"));
		Map<String, Object> map = holderService.holderCtrlService(userId, command);
		response.setData(map);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:云台预置点控制（设置、清除、转到预置点）controller层接口
	 * @param:paramJson:预置点参数json串{"userId":xxx,"command":xxx,"index":xxx}
	 * 参数说明：userId：摄像头唯一id，
	 * 		  command：命令（8：设置预置点，9：清除预置点，39：转到预置点），
	 * 		  index：预置点的序号（从1开始），最多支持300个预置点
	 * @return:返回状态码及其相关消息
	 * @url：../holderControl/presetCtrl
	 * @date:2017年9月12日 下午2:52:12
	 */
	@RequestMapping(value = "/presetCtrl",method = RequestMethod.POST)
	@ResponseBody
	public Response presetController(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		int userId = Integer.parseInt(object.getString("userId"));
		int command = Integer.parseInt(object.getString("command"));
		int index = Integer.parseInt(object.getString("index"));
		Map<String, Object> map = holderService.presetService(userId, command,index);
		response.setData(map);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:巡航设置controller层接口
	 * @param:  paramJson:巡航参数json串{"userId":xxx,"command":xxx,"byCruiseRoute":xxx,"byCruiseRoute":xxx,"wInput":xxx}
	 * 参数说明：       userId:摄像头注册返回值
	 * 			command：巡航命令（30：将预置点加入巡航序列，31：设置巡航点停顿时间，32：设置巡航速度，33：将预置点从巡航序列中删除，37：开始巡航，38：停止巡航）
	 * 			byCruiseRoute：巡航路径，最多支持32条路径（序号从1开始）
	 * 			byCruisePoint： 巡航点，最多支持32个点（序号从1开始）
	 * 			wInput：不同巡航命令时的值不同，预置点(最大300)、时间(最大255)、速度(最大40) 
	 * @return:返回状态码及其相关消息
	 * @url：../holderControl/cruiseCtrl
	 * @date:2017年9月12日 下午3:05:05
	 */
	@RequestMapping(value = "/cruiseCtrl",method = RequestMethod.POST)
	@ResponseBody
	public Response cruiseController(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		int userId = Integer.parseInt(object.getString("userId"));
		int command = Integer.parseInt(object.getString("command"));
		int byCruiseRoute = Integer.parseInt(object.getString("byCruiseRoute"));
		int byCruisePoint = Integer.parseInt(object.getString("byCruisePoint"));
		int wInput = object.getInt("wInput");
		Map<String, Object> map = holderService.cruiseService(userId, command, byCruiseRoute, byCruisePoint, wInput);
		response.setData(map);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:鹰视聚焦功能controller层接口
	 * @param:	paramJson:巡航参数json串{"userId":xxx,"dwAbilityType":xxx}
	 * 参数说明：       userId:摄像头注册返回值
	 * 			dwAbilityType：能力类型（3646：鹰视聚焦标定配置能力集，3649：鹰视聚焦配置能力集）
	 * @return:返回状态码及其相关消息
	 * @url:../holderControl/focusCtrl
	 * @date:2017年9月13日 上午11:11:19
	 */
	@RequestMapping(value = "/focusCtrl",method = RequestMethod.POST)
	@ResponseBody
	public Response focusController(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		int userId = object.getInt("userId");
		int dwAbilityType = object.getInt("dwAbilityType");
		Map<String, Object> map = holderService.focusService(userId, dwAbilityType);
		response.setData(map);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:摄像头抓图controller层接口
	 * @param:	paramJson：抓图所需参数json串
	 * 参数说明：	userId:摄像头注册返回值
	 * 			channel：通道号
	 * 			picFileName：保存JPEG图的文件路径（包括文件名）
	 * @return:包含返回状态码及其相关消息
	 * @url:../holderControl/captureCtrl
	 * @date:2017年9月13日 下午1:54:12
	 */
	@RequestMapping(value = "/captureCtrl",method = RequestMethod.POST)
	@ResponseBody
	public Response captureController(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		String path = object.getString("path");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dt = sdf.format(date);
		try {
			holderService.captureScreen(path,dt+".jpg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//response.setData(map);
		return response;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:镜像控制controller层接口
	 * @param:paramJson：镜像参数json串
	 * 参数说明：	userId:摄像头注册返回值
	 * 			channel：通道号
	 * 			mirrorType:镜像方式   镜像方式：0- 关闭，1- 左右，2- 上下，3- 中间 
	 * @return:包含返回状态码及其相关消息
	 * @date:2017年9月19日 下午2:31:45
	 */
	public Response mirrorController(String paramJson){
		JSONObject object = JSONObject.fromObject(paramJson);
		int userId = object.getInt("userId");
		int command = 3355;
		int channel = object.getInt("channel");
		int mirrorType = object.getInt("mirrorType");
		Map<String, Object> map = holderService.mirrorService(userId,command,channel,mirrorType);
		response.setData(map);
		return response;
	}
	
	
}
