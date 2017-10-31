package video.cn.com.lanlyc.controller;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import video.cn.com.lanlyc.core.sdk.HCNetSDK;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_DEVICEINFO_V30;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_IPPARACFG;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_WORKSTATE_V30;

public class Test {

	public static void main(String[] args) {
		HCNetSDK sdk = HCNetSDK.INSTANCE;
		
		
        if(!sdk.NET_DVR_Init()){//SDK初始化
            System.out.println("SDK初始化失败");
            return ;
        }
        NativeLong uid=new  NativeLong(-1);
        NET_DVR_DEVICEINFO_V30 devinfo=new NET_DVR_DEVICEINFO_V30();//设备信息
        String ip="192.168.3.3";
        short port=8000;
        uid=sdk.NET_DVR_Login_V30(ip,port,"admin","llyc123456",devinfo);//返回一个用户编号，同时将设备信息写入devinfo
        System.out.println("----唯一标识-----");
        System.out.println(uid);
        int Iuid=uid.intValue();
        if(Iuid<0){
            System.out.println("设备注册失败");
            return ;
        }
        /*NET_DVR_WORKSTATE_V30 devwork=new NET_DVR_WORKSTATE_V30();//设备工作状态信息结构体
        if(!sdk.NET_DVR_GetDVRWorkState_V30(uid, devwork)){//获取设备的工作状态
            //返回Boolean值，判断是否获取设备能力
            System.out.println("返回设备状态失败");
        }
        IntByReference ibrBytesReturned = new IntByReference(0);//获取IP接入配置参数
        NET_DVR_IPPARACFG ipcfg=new NET_DVR_IPPARACFG();//IP设备资源及IP通道资源配置结构体
        ipcfg.write();
        Pointer lpIpParaConfig =ipcfg.getPointer();
        //获取设备的配置信息。
        sdk.NET_DVR_GetDVRConfig(uid,sdk.NET_DVR_GET_IPPARACFG,new NativeLong(0),lpIpParaConfig,ipcfg.size(),ibrBytesReturned);
        ipcfg.read();
        System.out.println("IP地址:"+ip);
        System.out.println("设备状态："+devwork.dwDeviceStatic);//0正常，1CPU占用率过高，2硬件错误，3未知
        //显示模拟通道
        for(int i=0;i< devinfo.byChanNum;i++){
            System.out.println("Camera"+i+1);//模拟通道号名称
            System.out.println("通道号------"+devinfo.byDiskNum );//模拟通道号名称
            System.out.println("是否录像:"+devwork.struChanStatic[i].byRecordStatic);//0不录像，1录像
            System.out.println("信号状态:"+devwork.struChanStatic[i].bySignalStatic);//0正常，1信号丢失
            System.out.println("硬件状态:"+devwork.struChanStatic[i].byHardwareStatic);//0正常，1异常
        }*/
        
        HCNetSDK.NET_DVR_CLIENTINFO clientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();//预览参数
        clientInfo.lChannel = new NativeLong(1);
        
        NativeLong result_num = HCNetSDK.INSTANCE.NET_DVR_RealPlay_V30(uid,clientInfo,null,null,true);//开启预览
        System.out.println("-------预览-------");
        System.out.println(result_num);
        if(result_num.intValue() == -1){
        	int a = HCNetSDK.INSTANCE.NET_DVR_GetLastError();HCNetSDK.INSTANCE.NET_DVR_GetLastError();
            System.out.println(a);
        }
        
       //boolean start = sdk.NET_DVR_PTZControl(result_num,27,0);//启动
        /*if(!start){
        	int aa = HCNetSDK.INSTANCE.NET_DVR_GetLastError();
        	System.out.println(aa);
        }
        System.out.println(start);*/
        //boolean stop = sdk.NET_DVR_PTZControl(result_num,27,1);//停止
        /*boolean presetCode = sdk.NET_DVR_PTZPreset(result_num, 39, 1);//设置预置点、删除预置点、转到预置点
        System.out.println("-------设置预置点-------");
        System.out.println(presetCode);*/
        
        //boolean cruiseCode = sdk.NET_DVR_PTZCruise(result_num, 30, (byte)5, (byte)1, (short)1);//将预置点1添加到巡航点1
        //boolean cruiseCode = sdk.NET_DVR_PTZCruise(result_num, 30, (byte)1, (byte)1, (short)3);//将预置点3添加到巡航点1
        //boolean cruiseCode = sdk.NET_DVR_PTZCruise(result_num, 32, (byte)1, (byte)1, (short)7);//设置巡航速度
        boolean cruiseCode = sdk.NET_DVR_PTZCruise(result_num, 37, (byte)1, (byte)1, (short)10);//开启巡航
        //boolean cruiseCode = sdk.NET_DVR_PTZCruise(result_num, 3, (byte)1, (byte)1, (short)3);//设置巡航速度
        System.out.println("-------巡航-------");
        System.out.println(cruiseCode);
        
        sdk.NET_DVR_Logout(uid);//用户注销
        sdk.NET_DVR_Cleanup();//释放SDK资源，在程序结束之前调用
	}
	

}
