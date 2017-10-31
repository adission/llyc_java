package cn.com.lanlyc.core.util;

/*
 * 打卡监听类，监听每个控制板实时传递的打卡记录
 * 与控制板是一对一关系
 * @luoying 2017-08-29 10:50:14
 */
public class ClockMonitor implements Runnable{
	private String portname;//监听控制板对应的COM号
	private GateBoardController m = GateBoardController.getInstance();
	
	public ClockMonitor(String portname) {
		this.portname=portname;
	}
	 
	@Override
	public void run() {
		m.reciOneCardInfo(portname);
		
	}

}
