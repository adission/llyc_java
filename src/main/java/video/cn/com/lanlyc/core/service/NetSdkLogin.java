package video.cn.com.lanlyc.core.service;

import org.springframework.stereotype.Service;
import com.sun.jna.NativeLong;
import video.cn.com.lanlyc.core.sdk.HCNetSDK;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:连接SDK的必要步骤：初始化SDK、注册设备、注销设备、释放SDK资源
 * @date:2017年9月7日 上午9:12:19
 */
@Service
public class NetSdkLogin {
	HCNetSDK.NET_DVR_CLIENTINFO clientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();//用户参数
	HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();//设备信息
	boolean bRealPlay;//是否在预览
	
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:SDK接口调用主要流程之一：初始换设备SDK，调用NET_DVR_Init接口
	 * @param:无
	 * @return:statusCode:返回初始化成功与否的状态码，成功：200，失败：-1
	 * @date:2017年9月7日 上午9:16:23
	 */
	public int initSDK(){
		int statusCode = 200; 
		//SDK初始化函数,返回TRUE表示成功，FALSE表示失败
		boolean if_success = HCNetSDK.INSTANCE.NET_DVR_Init();
		if(!if_success){
			statusCode = -1;
		}
		return statusCode;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:SDK接口调用主要流程之一：注册设备，调用NET_DVR_Login_V30接口
	 * @param:1、String ip:ip或域名,2、short port:端口号,3、String username:用户名,4、String password:密码
	 * @return:statusCode:接口调用成功：返回用户唯一id，失败：-1
	 * @date:2017年9月7日 上午10:57:51
	 */
	public int loginSDK(String ip,short port,String username,String password){
		//用户注册设备(ip,端口号,用户名,密码,设备信息)
		NativeLong result_num = HCNetSDK.INSTANCE.NET_DVR_Login_V30(ip, port, username, password, deviceInfo);
		int userId = result_num.intValue();
		return userId;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:SDK接口调用主要流程之一：注销设备，调用NET_DVR_Logout接口
	 * @param:int userId:用户唯一id
	 * @return:statusCode:返回注销成功与否的状态码，成功：200，失败：-1
	 * @date:2017年9月7日 上午11:52:37
	 */
	public int logoutSDK(int userId){
		int statusCode = 200;
		NativeLong lUserID= new NativeLong(userId);
		boolean if_success = HCNetSDK.INSTANCE.NET_DVR_Logout(lUserID);
		if(!if_success){
			statusCode = -1;
		}
		return statusCode;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:SDK接口调用主要流程之一：释放SDK调用NET_DVR_Cleanup接口
	 * @param:无
	 * @return:statusCode:返回释放成功与否的状态码，成功：200，失败：-1
	 * @date:2017年9月7日 下午1:47:53
	 */
	public int cleanSDK(){
		int statusCode = 200;
		boolean if_success = HCNetSDK.INSTANCE.NET_DVR_Cleanup();
		if(!if_success){
			statusCode = -1;
		}
		return statusCode;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:SDK回调接口之一：获取错误码
	 * @param:int status_num：所调用接口的返回状态码
	 * @return:statusCode：错误码，通过错误码判断出错原因。
	 * @date:2017年9月7日 下午1:41:39
	 */
	public int errorCode(){
		//接口返回失败调用NET_DVR_GetLastError接口获取错误码，通过错误码判断出错原因
		int statusCode = HCNetSDK.INSTANCE.NET_DVR_GetLastError();
		return statusCode;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:视频预览
	 * @param:摄像头唯一标识
	 * @return:所调用接口的返回值
	 * @date:2017年10月11日 上午10:10:41
	 */
	public int previewSDK(int userId){
		if(userId != -1){
			//获取通道号
//			int iChannelNum = getChannelNumber();//通道号
//			if (iChannelNum == -1) {
//				JOptionPane.showMessageDialog(this, "请选择要预览的通道");
//				return;
//			}
//
//			m_strClientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();
//			m_strClientInfo.lChannel = new NativeLong(iChannelNum);
		}else{
			
		}
		NativeLong lUserID= new NativeLong(userId);
		clientInfo.lChannel = new NativeLong(1);
		NativeLong result_num = HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(lUserID,clientInfo,null,null,true);
		int playCode = result_num.intValue();
		return playCode;
	}
	
	
}
