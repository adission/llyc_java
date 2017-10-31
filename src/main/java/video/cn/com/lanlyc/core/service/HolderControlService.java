package video.cn.com.lanlyc.core.service;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import video.cn.com.lanlyc.core.sdk.HCNetSDK;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_CLIENTINFO;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_CORRIDOR_MODE;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_DEVICEINFO_V30;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:云台控制service层
 * @date:2017年9月8日 上午11:58:17
 */
@Service
public class HolderControlService {
	NativeLong loginCode;
	NET_DVR_DEVICEINFO_V30 devinfo=new NET_DVR_DEVICEINFO_V30();//设备信息
	
	NET_DVR_CLIENTINFO clientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();//预览参数
    
	
	HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
	int errorCode;
	Map<String,Object> map = new HashMap<String,Object>();
	
	@Autowired
	NetSdkLogin netSDK;
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:云台转动控制接口
	 * @param:int userId：用户唯一标识,int command：所要发出的云台控制命令
	 * @return:Map：状态码和提示消息
	 * @date:2017年9月8日 上午11:58:42
	 */
	public Map<String, Object> holderCtrlService(int userId,int command){
		System.out.println("----初始化----");
		if(!hcNetSDK.NET_DVR_Init()){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "初始化失败，请根据code码查找错误信息！");
			return map;
		}
		String ip="192.168.3.3";
        short port=8000;
		
		
        loginCode=hcNetSDK.NET_DVR_Login_V30(ip,port,"admin","llyc123456",devinfo);//返回一个用户编号，同时将设备信息写入devinfo
		if(loginCode.intValue()==-1){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "注册失败，请根据code码查找错误信息！");
			return map;
		}
		clientInfo.lChannel = new NativeLong(1);
		NativeLong previewCode = HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(loginCode,clientInfo,null,null,true);
		
		String startMmg = "";
		String stopMmg = "";
		switch(command){
			case 21:
				startMmg = "云台上仰启动失败";
				stopMmg = "云台上仰停止失败";
				break;
			case 22:
				startMmg = "云台下俯启动失败";
				stopMmg = "云台下俯停止失败";
				break;
			case 23:
				startMmg = "云台左转启动失败";
				stopMmg = "云台左转停止失败";
				break;
			case 24:
				startMmg = "云台右转启动失败";
				stopMmg = "云台右转停止失败";
				break;
			case 25:
				startMmg = "云台上仰和左转启动失败";
				stopMmg = "云台上仰和左转停止失败";
				break;
			case 26:
				startMmg = "云台上仰和右转启动失败";
				stopMmg = "云台上仰和右转停止失败";
				break;
			case 27:
				startMmg = "云台下俯和左转启动失败";
				stopMmg = "云台下俯和左转停止失败";
				break;
			case 28:
				startMmg = "云台下俯和右转启动失败";
				stopMmg = "云台下俯和右转停止失败";
				break;
			case 29:
				startMmg = "云台左右自动扫描启动失败";
				stopMmg = "云台左右自动扫描停止失败";
				break;
		}
		
		if(previewCode.intValue() != -1){
			
			boolean start = hcNetSDK.NET_DVR_PTZControl(previewCode,command,0);//启动
			if(!start){
				errorCode = hcNetSDK.NET_DVR_GetLastError();
				map.put("code", errorCode);
				map.put("message", startMmg);
			}else{
				boolean stop = hcNetSDK.NET_DVR_PTZControl(previewCode,command,1);//停止
				if(!stop){
					errorCode = hcNetSDK.NET_DVR_GetLastError();
					map.put("code", errorCode);
					map.put("message", stopMmg);
				}else{
					map.put("code", "200");
					map.put("message", "OK");
				}
			}
		}else{
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "预览失败，请根据code码查找错误信息！");
		}
		hcNetSDK.NET_DVR_Logout(loginCode);//用户注销
		hcNetSDK.NET_DVR_Cleanup();//释放SDK资源，在程序结束之前调用
		return map;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:实时预览service层接口
	 * @param:userId：用户唯一标识,
	 * 			ip：摄像头ip,
	 * 			port：摄像头端口号,
	 * 			username：摄像头登录用户名,
	 * 			password：摄像头登录密码
	 * @return:Map：状态码和提示消息
	 * @date:2017年9月8日 下午1:37:13
	 */
	public Map<String,Object> realTimePreview(int userId,String ip,short port,String username,String password){
		int initStatusCode = netSDK.initSDK();
		if(initStatusCode == -1){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			hcNetSDK.NET_DVR_Cleanup();
			map.put("code", errorCode);
			map.put("message", "DVR初始化失败");
		}else{
			
			int loginStatusCode = netSDK.loginSDK(ip, port, username, password);
			if(loginStatusCode == -1){
				errorCode = hcNetSDK.NET_DVR_GetLastError();
				hcNetSDK.NET_DVR_Cleanup();
				map.put("code", errorCode);
				map.put("message", "DVR登录失败");
			}else{
				
				int previewStatusCode = netSDK.previewSDK(userId);
				if(previewStatusCode == -1){
					errorCode = hcNetSDK.NET_DVR_GetLastError();
					hcNetSDK.NET_DVR_Cleanup();
					map.put("code", errorCode);
					map.put("message", "DVR视频预览失败");
				}else{
					map.put("code", previewStatusCode);
					map.put("message", "OK");
				}
			}
		}
		return map;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:云台预置点控制（设置、清除、转到预置点）service层接口
	 * @param:userId：摄像头唯一id，command：命令（8：设置预置点，9：清除预置点，39：转到预置点），index：预置点的序号（从1开始），最多支持300个预置点
	 * @return:返回状态码及其相关消息
	 * @date:2017年9月12日 下午2:58:12
	 */
	public Map<String,Object> presetService(int userId,int command,int index){
		System.out.println("----初始化----");
		if(!hcNetSDK.NET_DVR_Init()){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "初始化失败，请根据code码查找错误信息！");
			return map;
		}
		String ip="192.168.3.3";
        short port=8000;
		
		
        loginCode=hcNetSDK.NET_DVR_Login_V30(ip,port,"admin","llyc123456",devinfo);//返回一个用户编号，同时将设备信息写入devinfo
		if(loginCode.intValue()==-1){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "注册失败，请根据code码查找错误信息！");
			return map;
		}
		clientInfo.lChannel = new NativeLong(1);
		NativeLong previewCode = HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(loginCode,clientInfo,null,null,true);
		//int previewCode = netSDK.previewSDK(userId);//打开预览
		//NativeLong viewCode = new NativeLong(previewCode);
		String msg = "";
		switch(command){
			case 8://设置预置点
				msg = "设置云台预置点失败";
				break;
			case 9://清除预置点
				msg = "清除云台预置点失败";
				break;
			case 39://转到预置点
				msg = "转到云台预置点失败";
				break;
		}
		
		if(previewCode.intValue() != -1){
			boolean start = hcNetSDK.NET_DVR_PTZPreset(previewCode,command,index);//云台预置点
			if(!start){
				errorCode = hcNetSDK.NET_DVR_GetLastError();
				hcNetSDK.NET_DVR_Cleanup();
				map.put("code", errorCode);
				map.put("message", msg);
			}else{
				map.put("code", 200);
				map.put("message", "OK");
			}
		}else{
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			hcNetSDK.NET_DVR_Cleanup();
			map.put("code", errorCode);
			map.put("message", msg);
		}
		return map;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:巡航设置service层接口
	 * @param: userId:摄像头注册返回值
	 * 			command：巡航命令（30：将预置点加入巡航序列，31：设置巡航点停顿时间，32：设置巡航速度，33：将预置点从巡航序列中删除，37：开始巡航，38：停止巡航）
	 * 			byCruiseRoute：巡航路径，最多支持32条路径（序号从1开始）
	 * 			byCruisePoint： 巡航点，最多支持32个点（序号从1开始）
	 * 			wInput：不同巡航命令时的值不同，预置点(最大300)、时间(最大255)、速度(最大40) 
	 * @return:返回状态码及其相关消息
	 * @date:2017年9月12日 下午3:05:05
	 */
	public Map<String,Object> cruiseService(int userId,int command,int byCruiseRoute,int byCruisePoint,int wInput){
		System.out.println("----巡航接口开始----");
		if(!hcNetSDK.NET_DVR_Init()){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "初始化失败，请根据code码查找错误信息！");
			return map;
		}
		String ip="192.168.3.3";
        short port=8000;
		
		
        loginCode=hcNetSDK.NET_DVR_Login_V30(ip,port,"admin","llyc123456",devinfo);//返回一个用户编号，同时将设备信息写入devinfo
		if(loginCode.intValue()==-1){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "注册失败，请根据code码查找错误信息！");
			return map;
		}
		clientInfo.lChannel = new NativeLong(1);
		NativeLong previewCode = HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(loginCode,clientInfo,null,null,true);

		
		
		String msg = "";
		switch(command){
			case 30://将预置点加入巡航序列
				msg = "将预置点加入巡航序列失败";
				break;
			case 31://设置巡航点停顿时间
				msg = "设置巡航点停顿时间失败";
				break;
			case 32://设置巡航速度
				msg = "设置巡航速度失败";
				break;
			case 33://将预置点从巡航序列中删除
				msg = "将预置点从巡航序列中删除失败";
				break;
			case 37://开始巡航
				msg = "开始巡航失败";
				break;
			case 38://停止巡航
				msg = "停止巡航失败";
				break;
		}
		
		if(previewCode.intValue() != -1){
			boolean start = hcNetSDK.NET_DVR_PTZCruise(previewCode, command, (byte)byCruiseRoute, (byte)byCruisePoint, (short)wInput);//云台巡航
			if(!start){
				errorCode = hcNetSDK.NET_DVR_GetLastError();
				hcNetSDK.NET_DVR_Cleanup();
				map.put("code", errorCode);
				map.put("message", msg);
			}else{
				map.put("code", 200);
				map.put("message", "OK");
			}
		}
		return map;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:鹰视聚焦功能service层接口
	 * @param:	userId:摄像头注册返回值
	 * 			dwAbilityType：能力类型（3646：鹰视聚焦标定配置能力集，3649：鹰视聚焦配置能力集）
	 * @return:map:包含返回状态码及其相关消息键值对
	 * @date:2017年9月13日 上午11:40:19
	 */
	public Map<String,Object> focusService(int userId,int dwAbilityType){
		NativeLong lUserID = new NativeLong(userId);
		boolean status = hcNetSDK.NET_DVR_GetSTDAbility(lUserID, dwAbilityType, new HCNetSDK.LPNET_DVR_STD_ABILITY());
		if(!status){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			hcNetSDK.NET_DVR_Cleanup();
			map.put("code", errorCode);
			map.put("message", "鹰视聚焦失败");
		}else{
			map.put("code", 200);
			map.put("message", "OK");
		}
		return map;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:摄像头抓图service层接口
	 * @param:	userId:摄像头注册返回值
	 * 			channel：通道号
	 * 			sPicFileName：保存JPEG图的文件路径（包括文件名）
	 * @return:包含返回状态码及其相关消息键值对
	 * @date:2017年9月13日 下午1:54:12
	 */
	public Map<String,Object> captureService(int userId, int channel, String sPicFileName){
		System.out.println("----抓图接口开始----");
		if(!hcNetSDK.NET_DVR_Init()){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "初始化失败，请根据code码查找错误信息！");
			return map;
		}
		String ip="192.168.3.3";
        short port=8000;
		
		
        loginCode=hcNetSDK.NET_DVR_Login_V30(ip,port,"admin","llyc123456",devinfo);//返回一个用户编号，同时将设备信息写入devinfo
		if(loginCode.intValue()==-1){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			map.put("code", errorCode);
			map.put("message", "注册失败，请根据code码查找错误信息！");
			return map;
		}
		clientInfo.lChannel = new NativeLong(1);
		NativeLong previewCode = HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(loginCode,clientInfo,null,null,true);
		System.out.println(sPicFileName);
		if(previewCode.intValue() != -1){
			boolean status = hcNetSDK.NET_DVR_CapturePicture(previewCode, sPicFileName);
			if(!status){
				errorCode = hcNetSDK.NET_DVR_GetLastError();
				hcNetSDK.NET_DVR_Cleanup();
				map.put("code", errorCode);
				map.put("message", "摄像头抓图失败");
			}else{
				map.put("code", 200);
				map.put("message", "OK");
			}
		}else{
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			hcNetSDK.NET_DVR_Cleanup();
			map.put("code", errorCode);
			map.put("message", "预览失败，请根据code码查找错误信息！");
		}
		
		//NativeLong lUserID = new NativeLong(userId);
		//NativeLong lChannel = new NativeLong(channel);
		//boolean status = hcNetSDK.NET_DVR_CaptureJPEGPicture(lUserID, lChannel, new HCNetSDK.NET_DVR_JPEGPARA(), sPicFileName);
		
		return map;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:镜像控制controller层接口
	 * @param:userId:摄像头注册返回值，command：控制命令，channel：通道号,mirrorType:镜像方式
	 * @return:包含返回状态码及其相关消息键值对
	 * @date:2017年9月19日 下午2:41:45
	 */
	public Map<String, Object> mirrorService(int userId, int command, int channel,int mirrorType) {
		NativeLong lUserID = new NativeLong(userId);
		NativeLong lChannel = new NativeLong(channel);
		NET_DVR_CORRIDOR_MODE mode = new HCNetSDK.NET_DVR_CORRIDOR_MODE();
		mode.write();
		mode.byMirrorMode = (byte)mirrorType;
		Pointer pointer = mode.getPointer();
		boolean status = hcNetSDK.NET_DVR_SetDVRConfig(lUserID,command, lChannel, pointer, mode.size());
		mode.read();
		if(!status){
			errorCode = hcNetSDK.NET_DVR_GetLastError();
			hcNetSDK.NET_DVR_Cleanup();
			map.put("code", errorCode);
			map.put("message", "获取镜像失败");
		}else{
			map.put("code", 200);
			map.put("message", "OK");
		}
		return map;
	}
	
	 public static void captureScreen(String fileName, String folder) throws Exception {
		  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		  Rectangle screenRectangle = new Rectangle(screenSize);
		  Robot robot = new Robot();
		  BufferedImage image = robot.createScreenCapture(screenRectangle);
		  //保存路径
		  File screenFile = new File(fileName);
		  if (!screenFile.exists()) {
		   screenFile.mkdir();
		  }
		  File f = new File(screenFile, folder);
		  ImageIO.write(image, "png", f);
		  //自动打开
		  if (Desktop.isDesktopSupported()
		     && Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
		     Desktop.getDesktop().open(f);
		 }
	
	
}

