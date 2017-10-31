package video.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jna.NativeLong;
import com.sun.jna.examples.win32.W32API.HWND;

import video.cn.com.lanlyc.core.sdk.HCNetSDK;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_FINDDATA_V30;

/**
 * 视频Service
 * @author	chenyan
 * @date	2017年9月8日 11:11:32
 * @version	v1.0
 */
@Service
public class VideoService {

	@Autowired
	private NetSdkLogin netSDK;
	
	HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
	NativeLong lFindHandle;

	NET_DVR_FINDDATA_V30 fileData = new HCNetSDK.NET_DVR_FINDDATA_V30();
		
	/**
	 * 回放
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param lChannel
	 * @param hWnd
	 * @param dwControlCode
	 * @return NativeLong
	 * @author chenyan
	 * @date 2017年9月8日 11:21:11
	 */
	public NativeLong playBack(String ip,short port,String username,String password,NativeLong lChannel,HWND hWnd, int dwControlCode){
		//1、初始化
		int init = netSDK.initSDK();
		if(init == -1){
			int errorCode = hcNetSDK.NET_DVR_GetLastError();
			System.out.println("DVR初始化失败：" + errorCode);
			hcNetSDK.NET_DVR_Cleanup();
			return null;
		}

		//2、设备注册
		int lUserID_1 = netSDK.loginSDK(ip,port,username,password);
		if(lUserID_1 == -1){
			int errorCode = hcNetSDK.NET_DVR_GetLastError();
			System.out.println("DVR登录失败：" + errorCode);
			hcNetSDK.NET_DVR_Cleanup();
			return null;
		}
		NativeLong lUserID  = new NativeLong(lUserID_1);

		//3、查找文件
		NativeLong info = findFile(lUserID);

		//4、回放
		
		//4.1 按时间回放
		/* 接口：	NET_DVR_PlayBackByTime(lUserID, lChannel, lpStartTime, lpStopTime, hWnd)按时间回放录像文件
		 * 参数：	参数名			参数类型				参数值描述
		 * 		lUserID			LONG				NET_DVR_Login_V40等登录接口的返回值
		 * 		lChannel		LONG				通道号
		 * 		lpStartTime		LPNET_DVR_TIME		文件的开始时间
		 * 		lpStopTime		LPNET_DVR_TIME		文件结束时间 
		 * 		hWnd			HWND				回放的窗口句柄，若置为空，SDK仍能收到码流数据，但不解码显示		
		 * 返回值：-1表示失败，其他值作为NET_DVR_StopPlayBack等函数的参数
		 */
		NativeLong lPlayHandle = hcNetSDK.NET_DVR_PlayBackByTime(lUserID, lChannel, fileData.struStartTime, fileData.struStopTime, hWnd);
		if(lPlayHandle.equals(-1)){
			int e = hcNetSDK.NET_DVR_GetLastError();
			System.out.println("按时间回放：" + e);
			return null;
		}
		
		//4.2 开始回放
		/* 接口：	NET_DVR_PlayBackControl(lPlayHandle, dwControlCode, dwInValue, LPOutValue)
		 * 参数：	参数名			参数类型	参数值描述
		 * 		lPlayHandle		LONG	播放句柄，NET_DVR_PlayBackByName或NET_DVR_PlayBackByTime的返回值
		 * 		dwControlCode	DWORD	控制录像回放状态命令：	1-开始播放，3-暂停播放，4-恢复播放（在暂停后调用将恢复暂停前的速度播放），5-快放，6-慢放，7-正常速度播放，
		 * 												9-打开声音，10-关闭声音，11-调节音量（取值范围[0,0xffff]），14-获取当前已经播放的时间(按文件回放的时候有效) 
		 * 		dwInValue		DWORD	设置的参数，如设置文件回放的进度(命令值NET_DVR_PLAYSETPOS)时，此参数表示进度值；如开始播放(命令值NET_DVR_PLAYSTART)时，此参数表示断点续传的文件位置（Byte）
		 * 		lpOutValue		DWORD	获取的参数，如获取当前播放文件总的时间（命令值NET_DVR_GETTOTALTIME ），此参数就是得到的总时间 
		 * 返回值：-1表示失败，其他值作为NET_DVR_StopPlayBack等函数的参数
		 */
//		int dwControlCode = 1;
		boolean play = hcNetSDK.NET_DVR_PlayBackControl(lPlayHandle, dwControlCode, 0, null);
		if(!play){
			int e = hcNetSDK.NET_DVR_GetLastError();
			System.out.println("开始回放：" + e);
			return null;
		}
			
		return null;

	}

	/**
	 * 停止回放
	 * @return Boolean
	 */
	public Boolean stopPlayBack(){
		/* 接口：	NET_DVR_StopPlayBack(lPlayHandle)停止回放录像文件
		 * 参数：	参数名			参数类型	参数值描述
		 * 		lPlayHandle		LONG	回放句柄，NET_DVR_PlayBackByName、NET_DVR_PlayBackByTime_V40或者NET_DVR_PlayBackReverseByName、NET_DVR_PlayBackReverseByTime_V40的返回值
		 * 返回值：TRUE表示成功，FALSE表示失败
		 */
		boolean flag = hcNetSDK.NET_DVR_StopPlayBack(lFindHandle);
		if(!flag){
			int e = hcNetSDK.NET_DVR_GetLastError();
			System.out.println("开始回放：" + e);
		}
		return flag;
	}

	/**
	 * 查找录像文件
	 * @param lUserID
	 * @return NativeLong			
	 * @author chenyan
	 * @date 2017年9月8日 11:21:11
	 * @version:v1.0
	 */
	public NativeLong findFile(NativeLong lUserID){

		//1、查找录像文件
		/* 接口：	NET_DVR_FindFile_V30(lUserID, pFindCond)
		 * 参数：	参数名		参数类型				参数值描述
		 * 		lUserID		LONG				NET_DVR_Login_V40的返回值
		 * 		pFindCond	LPNET_DVR_FILECOND	文件查找条件，包括设备通道号、文件类型、查找起止时间等
		 * 返回值：-1表示失败，其他值作为NET_DVR_FindClose等函数的参数
		 */
		this.lFindHandle = hcNetSDK.NET_DVR_FindFile_V30(lUserID, new HCNetSDK.NET_DVR_FILECOND());

		System.out.println(lFindHandle);
		if(lFindHandle.equals(-1)){
			int e = hcNetSDK.NET_DVR_GetLastError();
			System.out.println("文件查找失败：" + e);
			return null;
		}else{

			//2、查找成功或者查找等待（获取文件信息）
			/* 接口：	NET_DVR_FindNextFile_V30(lUserID, pFindCond)逐个获取查找到的文件信息
			 * 参数：	参数名			参数类型						参数值描述
			 * 		lFindHandle		LONG						文件查找句柄，NET_DVR_FindFile_V40或者NET_DVR_FindFile_V30的返回值
			 * 		lpFindData		LPNET_DVR_FINDDATA_V40		保存文件信息的指针
			 * 返回值：-1表示失败，其他值作为NET_DVR_FindClose等函数的参数
			 */

			NativeLong info = hcNetSDK.NET_DVR_FindNextFile_V30(lFindHandle, fileData);

			if(info.equals(-1)){
				int e = hcNetSDK.NET_DVR_GetLastError();
				System.out.println("获取文件信息：" + e);
				return null;
			}else{

				//3、查找结束或者失败（关闭文件查找，释放资源）
				/* 接口：	NET_DVR_FindClose_V30(lFindHandle)逐个获取查找到的文件信息
				 * 参数：	参数名			参数类型						参数值描述
				 * 		lFindHandle		LONG						文件查找句柄，NET_DVR_FindFile_V40、NET_DVR_FindFileByEvent或者NET_DVR_FindFile_V30的返回值
				 * 返回值：TRUE表示成功，FALSE表示失败
				 */
				boolean close = hcNetSDK.NET_DVR_FindClose_V30(lFindHandle);

				if(!close){
					int e = hcNetSDK.NET_DVR_GetLastError();
					System.out.println("关闭文件查找：" + e);
					return null;
				}
				return info;
			}

		}

	}

}
