package cn.com.lanlyc.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
 * 闸机控制板测试运行类
 */
public class Run {
	
	public static void main(String[] args) {
		
		GateBoardController m = GateBoardController.getInstance();
		m.openAPort("COM8");//测试时使用，调用时不用执行。连接指定COM号的控制板（同时启动其实时打卡监听线程）
//		m.openAPort("COM1");
//		m.openAPort("COM9");

//		 m.handshake("COM8");//握手
//		 m.setGateState("COM8",3,1);//设置控制板的通道
//		 m.checkGateState("COM8",3);//查询通道状态
		

//		 String id="421222199112216017";
//		 String IC_id="1234567898";
//		 String face_id="6789012346";
		//往闸机控制板添加一个单独的人员信息
//		 GateReturnMessage remsg2=m.addOneAuthInfo("COM8", id, IC_id, face_id,"00000000");//增加单个人员授权信息
//		 System.out.println(remsg2.getMessage());

		//获取闸机控制板中所有人员信息
		 GateReturnMessage remsg=m.getAllAuthInfo("COM8");//批量上传人员授权信息
			System.out.println(remsg.getData());
			
		// String[] ids={"42102219961213062X","421022199612130631","421022199612130622"};
		// String[] ics={"4234567890","4234567891","4234567892"};
		// String[] faces={"5234567890","5234567891","5234567892"};
		// String[] auths={"10011100","00001111","11001100","00110011"};
//		 m.delOneAuthInfo("COM8", "421222199112216017", "4234567892", "5234567892");//删除单个人员授权信息

//		 List<String[]> list=new ArrayList<>();
//		 String[]
//		 p1={"42102219961213062X","4234567890","5234567890","10011100"};
//		 String[]
//		 p2={"421022199612130631","4234567891","5234567891","00001111"};
//		 String[]
//				 p3={"421222199112216017","4234567892","5234567892","00001111"};
//		 list.add(p1);
//		 list.add(p2);
//		 list.add(p3);
//		 m.addSomeCard("COM8", list);//批量上传人员授权信息

		// String IC_id="1234567890";
		// String id="420902199510073316";
		// String face_id="0987654321";
		// ReturnMessage rm=new ReturnMessage();
//		 rm=m.queryOneAuthority("COM4", "", "1234567890", "");//查询单个是否授权
//		 rm=m.queryTotalAuthority("COM1");//查询授权总数
//		 rm=m.openGateByHand("COM1", 1);//手动打开通道
//		 rm=m.setControllerTime("COM1");//设置控制器时间
		// System.out.println("code:"+rm.getCode());
		// System.out.println("message:"+rm.getMessage());
		// System.out.println("data:"+rm.getData());

//		 m.resetAllAuthority("COM1");//清除所有授权信息
//		m.reciOneCardInfo("COM1");	//接收打卡信息
//		GateReturnMessage check_data=m.queryCardInfoByTime("COM1", "2017-08-01 11:12:00", "2017-08-25 11:12:00");//查询一段时间内的刷卡记录
//		System.out.println(check_data.getData()); 
		m.closeAPort("COM8");
//		 m.closeall();
	}
}
