package cn.com.lanlyc.core.util;

public class GetLostMonitor implements Runnable{

	private String portname;
	private GateBoardController m = GateBoardController.getInstance();
	public GetLostMonitor(String portname) {
		this.portname=portname;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		m.getLostCheckLogPassive(portname);
		
	}

}
