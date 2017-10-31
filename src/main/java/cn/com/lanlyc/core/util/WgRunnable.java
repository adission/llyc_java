package cn.com.lanlyc.core.util;

import gate.cn.com.lanlyc.core.service.WgListener;

/*
 * 打卡监听类，监听每个控制板实时传递的打卡记录
 * 与控制板是一对一关系
 * @luoying 2017-08-29 10:50:14
 */
public class WgRunnable implements Runnable{
	private WgListener ws = WgListener.getInstance();
	 
	@Override
	public void run() {
		try {
			ws.init_listen();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
