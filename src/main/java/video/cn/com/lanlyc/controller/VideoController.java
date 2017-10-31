package video.cn.com.lanlyc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.jna.NativeLong;
import com.sun.jna.examples.win32.W32API.HWND;

import cn.com.lanlyc.core.util.Response;
import net.sf.json.JSONObject;
import video.cn.com.lanlyc.core.service.VideoService;

/**
 * 视频Controller
 * @author chenyan
 * @date
 * @version v1.0
 */
@Controller
@RequestMapping("/video")
public class VideoController extends BaseController{

	@Autowired
	private VideoService videoService;
	
	/**
	 * 视频回放
	 * @param	paramJson:{
	 * 					"date":...,				//日期		类型：暂未知	格式：暂未知
	 * 					"start_time":...,		//开始时间		类型：暂未知	格式：暂未知
	 * 					"end_time":...,			//结束时间		类型：暂未知	格式：暂未知
	 * 					"vidicon_id":...,		//摄像机id	string
	 * 					"ip":...,				//摄像机ip	string
	 * 					"port":...,				//摄像机端口号	int
	 * 					"username":...,			//用户名		string
	 * 					"password":...,			//密码		string
	 * 					"lChannel":...,			//通道号		NativeLong
	 * 					"hWnd":...,				//窗口句柄		HWND
	 * 					"dwControlCode":...		//控制录像回放状态命令		int	（1-开始播放，3-暂停播放，4-恢复播放（在暂停后调用将恢复暂停前的速度播放），5-快放，6-慢放，7-正常速度播放，9-打开声音，10-关闭声音，11-调节音量（取值范围[0,0xffff]），14-获取当前已经播放的时间(按文件回放的时候有效)）
	 * 				}
	 * @return	Response
	 * @url		../video/playBack
	 * @author	chenyan
	 * @dete
	 */
	@RequestMapping(value = "/playBack", method = RequestMethod.POST)
	@ResponseBody
	public Response playBack(String jsonStr){
		
		JSONObject json = JSONObject.fromObject(jsonStr);

		if(
//				json.getString("date") == null || json.getString("date") == "" ||
//				json.getString("start_time") == null || json.getString("start_time") == "" ||
//				json.getString("end_time") == null || json.getString("end_time") == "" ||
				json.getString("vidicon_id") == null || json.getString("vidicon_id") == ""
				){
			return Response.PARAM_ERROR();
		}

		String date = json.getString("date");
		String startTime = json.getString("start_time");
		String endTime = json.getString("end_time");
		String vidiconId = json.getString("vidicon_id");
		String ip = json.getString("ip");
		int port_1 = json.getInt("port");
		short port = (short)port_1;//摄像机端口号	
		String username = json.getString("username");//用户名
		String password = json.getString("password");//密码
		int lChannel_1 = json.getInt("lChannel");//通道号
		long lChannel_2 = (long)lChannel_1;
		NativeLong lChannel  = new NativeLong(lChannel_2);
		int hWnd_1 = json.getInt("hWnd");//窗口句柄		HWND
		HWND hWnd = null;
		int dwControlCode = json.getInt("dwControlCode");//控制录像回放状态命令	

		try{
			NativeLong result = videoService.playBack(ip, port, username, password, lChannel, hWnd, dwControlCode);
			if(result != null){
				Response response = Response.OK();
				response.setData(result);
				return response;
			}else{
				return Response.ERROR(300, "暂无数据");
			}
		}catch (Exception e){
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
//	/**
//	 * 登录后首页默认预览
//	 * 
//	 */
//	@RequestMapping("/indexPreview")
//	@ResponseBody
//	public void indexPreview(String jsonStr) {
//		JSONObject json = JSONObject.parseObject(jsonStr);
//
//		//1、初始化SDK
//		boolean init = HCNetSDK.INSTANCE.NET_DVR_Init();
//		if(init == false){
//			int e = HCNetSDK.INSTANCE.NET_DVR_GetLastError();
//			System.out.println("初始化SDK错误：" + e);
//		}
//
//		HCNetSDK.INSTANCE.NET_DVR_GetDVRIPByResolveSvr_EX(sServerIP, wServerPort, 
//				sDVRName, wDVRNameLen, 
//				sDVRSerialNumber, wDVRSerialLen, sGetIP, dwPort)
//
//		//2、用户注册设备
//		HCNetSDK.INSTANCE.NET_DVR_Login_V30(sDVRIP, wDVRPort, sUserName, sPassword, lpDeviceInfo);//用户注册设备启动预览
//
//		//3、启动预览
//		HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(lUserID, lpClientInfo, fRealDataCallBack_V30, pUser, bBlocked)
//
//	}
//
//	/**
//	 * 多路预览
//	 */
//	@RequestMapping(value = "/multiplePreview", method = RequestMethod.POST)
//	@ResponseBody
//	public void multiplePreview(String jsonStr) {
//		JSONObject cameraObjects = JSONObject.parseObject(jsonStr);
//
//		//1、初始化SDK
//		boolean init = HCNetSDK.INSTANCE.NET_DVR_Init();
//		if(init == false){
//			int e = HCNetSDK.INSTANCE.NET_DVR_GetLastError();
//			System.out.println("初始化SDK错误：" + e);
//		}
//
//		HCNetSDK.INSTANCE.NET_DVR_GetDVRIPByResolveSvr_EX(sServerIP, wServerPort, 
//				sDVRName, wDVRNameLen, 
//				sDVRSerialNumber, wDVRSerialLen, sGetIP, dwPort)
//
//		//2、用户注册设备
//		HCNetSDK.INSTANCE.NET_DVR_Login_V30(sDVRIP, wDVRPort, sUserName, sPassword, lpDeviceInfo);//用户注册设备启动预览
//
//		//3、启动预览
//		HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(lUserID, lpClientInfo, fRealDataCallBack_V30, pUser, bBlocked)
//
//	}
//
//	/**
//	 * 停止预览
//	 */
//	@RequestMapping(value = "/stopPreview", method = RequestMethod.POST)
//	@ResponseBody
//	public void stopPreview(String jsonStr) {
//
//		//1、停止预览
//		HCNetSDK.INSTANCE.NET_DVR_StopRealPlay(lRealHandle);
//
//		//2、注销设备
//		HCNetSDK.INSTANCE.NET_DVR_Logout_V30(lUserID)
//
//		//3、释放SDK资源
//		HCNetSDK.INSTANCE.NET_DVR_Cleanup();
//
//	}

}
