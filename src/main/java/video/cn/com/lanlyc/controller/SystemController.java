package video.cn.com.lanlyc.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.com.lanlyc.core.util.Response;
import net.sf.json.JSONObject;
import video.cn.com.lanlyc.core.po.BaseSetting;
import video.cn.com.lanlyc.core.po.VideoSetting;
import video.cn.com.lanlyc.core.po.VidiconSetting;
import video.cn.com.lanlyc.core.service.SystemService;
import gate.cn.com.lanlyc.controller.BaseController;
/***
 * 
 * @author 胡志浩
 * @date 2017年9月7日
 * @version 1.0
 * @Title: 系统设置Controller
 */

@Controller
@RequestMapping(value = "/sysetem")
public class SystemController extends BaseController {
	
	@Autowired
	SystemService systemService;

	/***
	 * 设置预览视频显示参数。亮度、对比度、饱和度、色度
	 * 
	 * @param String
	 *            ip ip地址
	 * @param int
	 *            prot 端口号
	 * @param String
	 *            username 用户名
	 * @param String
	 *            password 用户密码
	 * @param int
	 *            dwBrightValue 亮度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
	 * @param int
	 *            dwContrastValue 对比度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
	 * @param int
	 *            dwSaturationValue 饱和度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
	 * @param int
	 *            dwHueValue 色度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为1
	 *
	 * @return True  json 200，成功
	 * 
	 * @return False json -1，操作错误
	 */
	@RequestMapping(value = "/SetVideoEffect", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response ClientSetVideoEffect(String paramJson) {
		JSONObject json = JSONObject.fromObject(paramJson);
		Boolean result = systemService.ClientSetVideoEffect(
				json.getString("ip"), 
				(short) json.getInt("port"),
				json.getString("username"), 
				json.getString("password"), 
				json.getInt("dwBrightValue"), 		// 亮度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
				json.getInt("dwContrastValue"), 	// 对比度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
				json.getInt("dwSaturationValue"),	// 饱和度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
				json.getInt("dwHueValue")); 		// 色度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
		if (!result){
			return Response.ERROR(-1, "操作错误！");
		}
		return Response.OK();
	}

	/***
	 * 设置设备的配置信息，初始预览时的数目、是否声音预览，预览切换时间
	 * 
	 * @param ip	IP地址
	 * @param port	端口号
	 * @param username	用户名
	 * @param password	密码
	 * @param byPreviewNumber	预览数目：0-1画面，1-4画面，2-9画面，3-16画面，0xff-最大画面
	 * @param byEnableAudio		是否声音预览：0-不预览，1-预览
	 * @param wSwitchTime		预览切换时间：0-不切换，1-5s，2-10s，3-20s，4-30s，5-60s，6-120s，7-300s 
	 * 
	 * @return Boolean True
	 * @return Boolean False
	 */
	@RequestMapping(value = "/SetDVRConfig", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response DVR_SetDVRConfig(String paramJson) {
		JSONObject json = JSONObject.fromObject(paramJson);
		Boolean result = systemService.DVR_SetDVRConfig(
				json.getString("ip"), 
				(short) json.getInt("port"),
				json.getString("username"), 
				json.getString("password"),
				(byte)json.getInt("byPreviewNumber"),
				(byte)json.getInt("byEnableAudio"),
				(byte)json.getInt("wSwitchTime")
				);
		if (!result){
			return Response.ERROR(-1, "操作错误！");
		}
		return Response.OK();
	}

	/***
	 * 恢复设备默认参数。
	 * 
	 * @param ip		ip地址
	 * @param port		端口号
	 * @param username	用户名
	 * @param password	密码
	 *
	 * @return Boolean True
	 * @return Boolean False
	 */
	@RequestMapping(value = "/RestoreConfig", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response DVR_RestoreConfig(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		Boolean result = systemService.DVR_RestoreConfig(
				json.getString("ip"), 
				(short) json.getInt("port"),
				json.getString("username"), 
				json.getString("password"));
		if (!result){
			return Response.ERROR(-1, "操作错误！");
		}
		return Response.OK();
	}




	//*****************摄像头设置*******************//

	/***
	 * 云台控制操作摄像头焦点(需先启动图象预览)。
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param int
	 *            dwPTZCommand 云台控制焦点动作：13、焦点前调。14、焦点后调
	 * @param int
	 *            dwStop 云台停止动作或开始动作：0－开始，1－停止
	 * @return boolean True/False
	 */
	@RequestMapping(value = "/PTZControl", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response DVR_PTZControl(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		Boolean result = systemService.DVR_PTZControl(
				json.getString("ip"), 
				(short) json.getInt("port"),
				json.getString("username"), 
				json.getString("password"),
				json.getInt("dwPTZCommand"));
		if (!result){
			return Response.ERROR(-1, "操作错误！");
		}
		return Response.OK();
	}



	/***
	 * 云台预置点操作（需先启动预览）。
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param int
	 *            dwPTZPresetCmd 操作云台预置点命令:8、设置预置点。9、清除预置点。39、转到预置点
	 * @param int
	 *            dwPresetIndex 预置点的序号（从1开始），最多支持300个预置点
	 * @return boolean True/False
	 */
	@RequestMapping(value = "/PTZPreset", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response DVR_PTZPreset(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		Boolean result = systemService.DVR_PTZPreset(
				json.getString("ip"), 
				(short) json.getInt("port"),
				json.getString("username"), 
				json.getString("password"),
				json.getInt("dwPTZPresetCmd"),
				json.getInt("dwPresetIndex"));
		if (!result){
			return Response.ERROR(-1, "操作错误！");
		}
		return Response.OK();
	}



	/***
	 * 云台巡航操作（需先启动预览）。
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param int
	 *            dwPTZCruiseCmd
	 *            操作云台巡航命令:30、将预置点加入巡航序列。31、设置巡航点停顿时间。32、设置巡航速度。33、将预置点从巡航序列中删除。
	 *            37、开始巡航。38、停止巡航
	 * @param byte
	 *            byCruiseRoute 巡航路径，最多支持32条路径（序号从1开始）
	 * @param byte
	 *            byCruisePoint 巡航点，最多支持32个点（序号从1开始）
	 * @param short
	 *            wInput 不同巡航命令时的值不同，预置点(最大300)、时间(最大255)、速度(最大40)
	 * @return boolean True/False
	 */
	@RequestMapping(value = "/PTZCruise", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response DVR_PTZCruise(String paramJson){
		JSONObject json = JSONObject.fromObject(paramJson);
		Boolean result = systemService.DVR_PTZCruise(
				json.getString("ip"), 
				(short) json.getInt("port"),
				json.getString("username"), 
				json.getString("password"),
				json.getInt("dwPTZPresetCmd"),
				(byte)json.getInt("byCruiseRoute"),
				(byte)json.getInt("byCruisePoint"),
				(short)json.getInt("wInput"));
		if (!result){
			return Response.ERROR(-1, "操作错误！");
		}
		return Response.OK();
	}



	/***
	 * 查询用户设置的系统状态
	 * 	function_mode  功能模式：1、监控直播 2、录像回放
	 *	screen_mode    分屏模式： 1、1x1  2、2x2  3、3x3  4、4x4
	 *	polling_mode   轮巡模式：1、一般  2、重点
	 * @return
	 */
	@RequestMapping(value = "/getBaseSetting", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response getBaseSetting(){
		try{
			List<BaseSetting> list = systemService.getBaseSetting();
			if(list.size()>0){
				Response response = Response.OK();
				response.setData(list);
				return response;
			}else{
				return Response.ERROR(3001, "暂无数据");
			}
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}


	}



	/**
	 * 用户自定义设置系统状态信息
	 * 
	 * @return boolean
	 * @author huzhihao
	 * 
	 */
	@RequestMapping(value = "/setBaseSetting", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response setBaseSetting(String paramJson) {
		try {
			JSONObject json = JSONObject.fromObject(paramJson);
			BaseSetting baseSetting = new BaseSetting();
			baseSetting.setFunction_mode(json.getInt("function")); // 功能模式：1、监控直播   2、录像回放
			baseSetting.setScreen_mode(json.getInt("screen")); // 分屏模式： 1、1x1  2、2x2  3、3x3  4、4x4
			baseSetting.setPolling_mode(json.getInt("polling")); // 轮巡模式：1、一般  2、重点
			List<BaseSetting> baseSettingList =systemService.getBaseSetting();	//取出数据库中的id
			baseSetting.setId(baseSettingList.get(0).getId());
			boolean flag = systemService.setBaseSetting(baseSetting);
			if (flag) {
				return Response.OK();
			}
			return Response.ERROR(-1, "操作错误！");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	
	/***
	 * 系统设置--设置出厂设置
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "/reduction", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response reduction(String paramJson) {
		JSONObject json = JSONObject.fromObject(paramJson);
		
		//-------------设置系统状态的初始化信息-------------//
		BaseSetting baseSetting = new BaseSetting();
		baseSetting.setFunction_mode(1); 
		baseSetting.setScreen_mode(2);
		baseSetting.setPolling_mode(1); 
		baseSetting.setPolling_time(10); 
		List<BaseSetting> baseSettingList =systemService.getBaseSetting();	//取出数据库中的id
		baseSetting.setId(baseSettingList.get(0).getId());
		boolean BaseSettingFlag = systemService.setBaseSetting(baseSetting);
		
		//-------------设置视频设置的初始化信息-------------//	
		VideoSetting videoSetting = new VideoSetting();
		videoSetting.setBrightness(5);	//视频亮度(1-10)		
		videoSetting.setContrast(5);		//视频对比度(1-10)		
		videoSetting.setSaturation(5);	//视频饱和度(1-10)		
		videoSetting.setChroma(5);			//视频色度(1-10)
//		List<VideoSetting> video =systemService.getAll();	//取出数据库中的id
//		videoSetting.setId(video.get(0).getId());
		boolean VideoSettingFlag = systemService.setVideoSetting(videoSetting);
		
		//-------------设置摄像头设置的初始化信息-------------//		
		VidiconSetting vidiconSetting = new VidiconSetting();
		vidiconSetting.setCruise(1); //巡航
		vidiconSetting.setFocus(1);	//焦点
		vidiconSetting.setPreset(1); //预置位置
		vidiconSetting.setCruise_time(10);//设置巡航时间间隔
		List<VidiconSetting> vidicon =systemService.getVidiconSetting();	//取出数据库中的id
		vidiconSetting.setId(vidicon.get(0).getId());
		boolean VidiconSettingFlag = systemService.setVidiconSetting(vidiconSetting);
		
		if(BaseSettingFlag && VideoSettingFlag && VidiconSettingFlag){
			return Response.OK();
		}
		else {
			return Response.ERROR(-2, "初始化失败");
		}
	}
	


	/***
	 * 系统设置--摄像头设置
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "/cameraSetting", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response cameraSetting(String paramJson) {
		JSONObject json = JSONObject.fromObject(paramJson);
		VidiconSetting vidiconSetting = new VidiconSetting();
		vidiconSetting.setCruise(json.getInt("cruise"));
		vidiconSetting.setFocus(json.getInt("focus"));
		vidiconSetting.setPreset(json.getInt("preset"));
		if (json.get("cruise_time") != "") {
			vidiconSetting.setCruise_time(json.getInt("cruise_time"));
		}
		List<VidiconSetting> vidicon =systemService.getVidiconSetting();	//取出数据库中的id
		vidiconSetting.setId(vidicon.get(0).getId());
		boolean flag = systemService.setVidiconSetting(vidiconSetting);
		boolean flag2 = systemService.DVR_PTZControl(	//调整摄像头焦点前后
				json.getString("ip"), 
				(short)8000,
				json.getString("username"), 
				json.getString("password"), 
				json.getInt("focus"));
//		
//		boolean flag3 = systemService.DVR_PTZCruise(	//开启或关闭巡航
//				"192.168.3.3", 
//				(short)8000,
//				"admin", 
//				"llyc123456",  
//				json.getInt("cruise"), 
//				(byte)1,
//				(byte)1,
//				(short)0); 			
		if(flag){
			System.out.println("焦点启动"+flag2);
//			System.out.println("巡航启动"+flag3);
			System.out.println("systemController-----------");
			
			return Response.OK(); 
		}
		return Response.ERROR(-1, "修改失败");
	}
	
	
	
	
	private Byte Byte(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/***
	 * 系统设置--视频设置
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "/videoSetting", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response videoSetting(String paramJson) {
		JSONObject json = JSONObject.fromObject(paramJson);
		VideoSetting videoSetting = new VideoSetting();
		videoSetting.setBrightness(json.getInt("brightness"));	//视频亮度(1-10)		
		videoSetting.setContrast(json.getInt("contrast"));		//视频对比度(1-10)		
		videoSetting.setSaturation(json.getInt("saturation"));	//视频饱和度(1-10)		
		videoSetting.setChroma(json.getInt("chroma"));			//视频色度(1-10)
		List<VideoSetting> video =systemService.getAll(json.getString("vidicon_id"));	//取出数据库中的id
		videoSetting.setId(video.get(0).getId());
		videoSetting.setVidicon_id(json.getString("vidicon_id"));
		boolean flag = systemService.setVideoSetting(videoSetting);
//		boolean flag2 = systemService.ClientSetVideoEffect(
//				json.getString("ip"), 
//				(short)8000,
//				json.getString("username"), 
//				json.getString("password"), 
//				json.getInt("brightness"), 
//				json.getInt("contrast"), 
//				json.getInt("saturation"), 
//				json.getInt("chroma"));
		
		boolean flag3 = systemService.DVR_Set_OSD_DVRConfig(
				json.getString("ip"), 
				(short)8000,
				json.getString("username"), 
				json.getString("password"), 
				json.getInt("DeviceName"),
				json.getInt("DeviceTime"),
				json.getString("Dname")
				);
		if(flag){
//			System.out.println("设置设备明亮度对比度："+flag2);
			System.out.println("设置设备日期显示和设备名称显示："+flag3);
			return Response.OK(); 
		}
		return Response.ERROR(-1, "修改失败");
	}
	
	
	
	
	/***
	 * 系统设置--系统状态
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "/systemState", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response systemState(String paramJson) {
		JSONObject json = JSONObject.fromObject(paramJson);
		BaseSetting baseSetting = new BaseSetting();
		baseSetting.setFunction_mode(json.getInt("function"));
		baseSetting.setScreen_mode(json.getInt("screen"));
		baseSetting.setPolling_mode(json.getInt("polling"));
		baseSetting.setPolling_time(json.getInt("polling_time"));
		List<BaseSetting> base =systemService.getBaseSetting();	//取出数据库中的id
		baseSetting.setId(base.get(0).getId());
		boolean flag =systemService.setBaseSetting(baseSetting);
		if(flag){
			return Response.OK(); 
		}
		return Response.ERROR(-1, "修改失败");
	}
	

	
	
	/****
	 * 系统设置--进入视频监控系统时查询设置的默认起始页
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "/getFunction", method = RequestMethod.POST) // 配置请求地址映射
	@ResponseBody // 配置注解直接返回数据
	public Response getFunction(String paramJson) {
		List<BaseSetting> base =systemService.getBaseSetting();	//取出数据库中的id
		Response response = Response.OK();
		response.setData(base.get(0).getFunction_mode());
		return response; 
	}
}
