package video.cn.com.lanlyc.controller;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.examples.win32.W32API.HWND;
import com.sun.jna.ptr.IntByReference;
import video.cn.com.lanlyc.core.sdk.HCNetSDK;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_DEVICEINFO_V30;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_FILECOND;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_FINDDATA_V30;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_IPPARACFG;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_TIME;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_WORKSTATE_V30;

public class PlayBackTest {

	public static void main(String[] args) {
		HCNetSDK sdk = HCNetSDK.INSTANCE;
		
		System.out.println(sdk);
		//1、初始化
        if(!sdk.NET_DVR_Init()){//SDK初始化
        	int e = sdk.NET_DVR_GetLastError();
            System.out.println("SDK初始化失败：" + e);
            return;
        }
        
        //2、注册设备
        NativeLong uid = new NativeLong(-1);
        NET_DVR_DEVICEINFO_V30 devinfo = new NET_DVR_DEVICEINFO_V30();//设备信息
        String ip="192.168.3.2";
        short port=8000;
        uid=sdk.NET_DVR_Login_V30(ip,port,"admin","llyc123456",devinfo);//返回一个用户编号，同时将设备信息写入devinfo
        System.out.println(uid);
        int Iuid=uid.intValue();
        if(Iuid<0){
        	int e = sdk.NET_DVR_GetLastError();
            System.out.println("设备注册失败：" + e);
            return;
        }

        //3、查找文件
        System.out.println("3、查找文件");
        
//        public byte[] sCardNumber = new byte[32];//����
//        public NET_DVR_TIME struStartTime;//��ʼʱ��
//        public NET_DVR_TIME struStopTime;//����ʱ��
        
        //欲查找的文件信息结构 
        NET_DVR_FILECOND filecond = new NET_DVR_FILECOND();
        
        filecond.lChannel = new NativeLong(1);
        filecond.dwFileType = 0xff;
        filecond.dwIsLocked = 0xff;
        filecond.dwUseCardNo = 0;
//        filecond.sCardNumber = new byte[32];
        
        NET_DVR_TIME startTime = new NET_DVR_TIME();
        startTime.dwYear = 2017;
        startTime.dwMonth = 10;
        startTime.dwDay = 11;
        startTime.dwHour = 14;
        startTime.dwMinute = 0;
        startTime.dwSecond = 0;
        filecond.struStartTime = startTime;
        
        NET_DVR_TIME endTime = new NET_DVR_TIME();
        endTime.dwYear = 2017;
        endTime.dwMonth = 10;
        endTime.dwDay = 11;
        endTime.dwHour = 18;
        endTime.dwMinute = 0;
        endTime.dwSecond = 0;
        filecond.struStopTime = endTime;
        
        NativeLong lFindHandle = sdk.NET_DVR_FindFile_V30(uid, filecond);
        
        System.out.println("filecond:" + filecond);
        
        System.out.println("lFindHandle:" + lFindHandle);
        if(lFindHandle.equals(-1)){
        	int e = sdk.NET_DVR_GetLastError();
        	System.out.println("文件查找失败：" + e);
            return;
        }
        
        //4、获取文件信息
        System.out.println("4、获取文件信息");
        NET_DVR_FINDDATA_V30 fileData = new NET_DVR_FINDDATA_V30();
        NativeLong info = sdk.NET_DVR_FindNextFile_V30(lFindHandle, fileData);
        
        System.out.println(info);
        
        if(info.equals(-1)){
			int e = sdk.NET_DVR_GetLastError();
			System.out.println("获取文件信息：" + e);
			return;
        }
////        else{
////	    	while(!info.equals(1000)){
////	    		if(!info.equals(1000)){
////	            	System.out.println("1111");
////	            	NativeLong info_2 = sdk.NET_DVR_FindNextFile_V30(lFindHandle, fileData);
////	            }
////	    	}
////        }
//        System.out.println(fileData);
//        
//        //5、关闭查找
//        boolean close = sdk.NET_DVR_FindClose_V30(lFindHandle);
//		if(!close){
//			int e = sdk.NET_DVR_GetLastError();
//			System.out.println("关闭文件查找：" + e);
//			return;
//		}
//		
//		//4、按时间回放
//		System.out.println("4、按时间回放");
//		HWND hWnd = null;
//		NativeLong lChannel = new NativeLong(1);
//		NativeLong lPlayHandle = sdk.NET_DVR_PlayBackByTime(uid, lChannel, fileData.struStartTime, fileData.struStopTime, hWnd);
//		System.out.println(lPlayHandle);
//		int e = sdk.NET_DVR_GetLastError();
//		System.out.println("按时间回放：" + e);
//		return;
//		if(lPlayHandle.equals(-1)){
//			
//		}
        
        
        
        
//        NET_DVR_WORKSTATE_V30 devwork=new NET_DVR_WORKSTATE_V30();//设备工作状态信息结构体
//        if(!sdk.NET_DVR_GetDVRWorkState_V30(uid, devwork)){//获取设备的工作状态
//            //返回Boolean值，判断是否获取设备能力
//            System.out.println("返回设备状态失败");
//        }
//        IntByReference ibrBytesReturned = new IntByReference(0);//获取IP接入配置参数
//        NET_DVR_IPPARACFG ipcfg=new NET_DVR_IPPARACFG();//IP设备资源及IP通道资源配置结构体
//        ipcfg.write();
//        Pointer lpIpParaConfig =ipcfg.getPointer();
//        //获取设备的配置信息。
//        sdk.NET_DVR_GetDVRConfig(uid,sdk.NET_DVR_GET_IPPARACFG,new NativeLong(0),lpIpParaConfig,ipcfg.size(),ibrBytesReturned);
//        ipcfg.read();
//        System.out.println("IP地址:"+ip);
//        System.out.println("设备状态："+devwork.dwDeviceStatic);//0正常，1CPU占用率过高，2硬件错误，3未知
//        //显示模拟通道
//        for(int i=0;i< devinfo.byChanNum;i++){
//            System.out.println("Camera"+i+1);//模拟通道号名称
//            System.out.println("是否录像:"+devwork.struChanStatic[i].byRecordStatic);//0不录像，1录像
//            System.out.println("信号状态:"+devwork.struChanStatic[i].bySignalStatic);//0正常，1信号丢失
//            System.out.println("硬件状态:"+devwork.struChanStatic[i].byHardwareStatic);//0正常，1异常
//        }
//        
//        HCNetSDK.NET_DVR_CLIENTINFO clientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();//用户参数
//        NativeLong result_num = HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(uid,clientInfo,null,null,true);
//        System.out.println("-------预览-------");
//        System.out.println(result_num);
//        int a = HCNetSDK.INSTANCE.NET_DVR_GetLastError();HCNetSDK.INSTANCE.NET_DVR_GetLastError();
//        System.out.println(a);
//        boolean start = sdk.NET_DVR_PTZControl(result_num,21,0);//启动
//        if(!start){
//        	int aa = HCNetSDK.INSTANCE.NET_DVR_GetLastError();
//        	System.out.println(aa);
//        }
//        System.out.println(start);
//        //boolean stop = sdk.NET_DVR_PTZControl(result_num,21,1);//停止
//        
//        sdk.NET_DVR_Logout(uid);//用户注销
//        sdk.NET_DVR_Cleanup();//释放SDK资源，在程序结束之前调用
	}
	

}
