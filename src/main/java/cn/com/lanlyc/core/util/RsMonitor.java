package cn.com.lanlyc.core.util;
/**
 * 监听JavaRs收到的数据并处理
 * @author ying luo
 *
 */
public class RsMonitor implements Runnable{

	private JavaRs rs=null;
	
	public RsMonitor(JavaRs rs) {
		this.rs=rs;
	}
	
	@Override
	public void run() {
		while (true) {
			synchronized (rs) {
				rs.delBufs();
			}	
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

	}

}
