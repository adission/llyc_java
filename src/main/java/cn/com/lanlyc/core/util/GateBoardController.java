package cn.com.lanlyc.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.com.lanlyc.base.util.ConstantUtils;
import gate.cn.com.lanlyc.core.service.ConnectLogService;
import gate.cn.com.lanlyc.core.service.ServiceContainer;

/**
 * 关于闸机控制板操作的类
 * 
 * @author liyangfu
 *
 */
@Component
public class GateBoardController {
	protected final static String SessionContainer_Key = ConstantUtils.getConstant("Session_User_key");

	private GateBoardUtil util = new GateBoardUtil();
	private List<String> portList = util.getPortList();// 与当前计算机连接的所有端口号

	private ConcurrentHashMap<String, String> usefulPortList2 = new ConcurrentHashMap<>();// 正在使用的控制板的端口号和闸机号

	private List<String> usefulPortList = new ArrayList<>();// 正在使用的控制板的端口号
	private Map comOpreation = util.getComOpreation();// COM号和其操作类JavaRs一一对应
	private String gate_id;
	private List<String[]> checkLogs = new ArrayList<>();// 保存实时接收到的并且还未做处理的刷卡记录，每个String[]包含卡号（18位或9位），时间（标准时间格式），卡类型（身份证:0或者IC卡:1）,闸机通道编号
	private ConcurrentHashMap<String, Boolean> openFlag = new ConcurrentHashMap<>();// 每个COM口是否打开的标志
	private boolean allPortOpen = false;// 所有COM口是否都处于连接状态的标志

	private byte[] thelastframeI = new byte[100];
	private byte[] thelastframeJ = new byte[100];

	private ConcurrentHashMap<String, Boolean> idleFlag = new ConcurrentHashMap<>();// 控制板通讯是否空闲，空闲时获取未上传刷卡记录

	private ConcurrentHashMap<String, Boolean> setTimeFlag = new ConcurrentHashMap<>();// 是否已经设置过控制板的时间
	
	private String useBoardFlag="";//使用控制板的同步信号量
	private ConcurrentHashMap<String, String> useOneBoardFlag = new ConcurrentHashMap<>();//使用控制板的同步信号量，每个控制板一个

	private GateBoardController() {
		this.confirmIdleFlag();
	}

	private static final GateBoardController single = new GateBoardController();

	// 静态工厂方法
	public static GateBoardController getInstance() {
		return single;
	}

	@Autowired
	private ServiceContainer myservice;
	public static GateBoardController testUtils;

	/**
	 * 获取 业务类容器
	 * 
	 * @return
	 */
	protected ServiceContainer getService() {
		return testUtils.myservice;
	}

	@PostConstruct
	public void init() {
		testUtils = this;
	}

	public List<String> getPortList() {
		return portList;
	}

	/**
	 * 一次关闭所有串口
	 * 
	 * @param portname
	 * @return
	 */
	public void closeall() {
		Iterator it = comOpreation.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			this.closeAPort(key);
		}
	}

	/**
	 * 一次打开所有串口
	 * 
	 */
	public void openall() {
		Iterator it = comOpreation.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			this.openAPort(key);
		}
	}

	/*
	 * 连接控制板，并分配一个监听打卡类 参数：1、portname String 控制板对应的COM号
	 * 
	 * @luoying 2017-08-16 16:56:40
	 */
	public GateReturnMessage openAPort(String portname) {
		// this.idleFlag.put(portname, false);
		GateReturnMessage remegs = new GateReturnMessage();// 返回数据

		// 判断电脑是否扫描到COM号
		if (!this.portList.contains(portname)) {
			remegs.paramErrorMesg("该COM号不存在");
			ConnectLogService connservice = this.getService().getConnectLogService();
			connservice.add("4", remegs.getMessage(), this.gate_id);
			// this.idleFlag.put(portname, true);
			return remegs;
		}
		// 判断COM口是否已经打开
		if (this.openFlag.containsKey(portname) && this.openFlag.get(portname)) {
			remegs.rightMesg("已连接控制板", null);
			// this.idleFlag.put(portname, true);
			return remegs;
		}
		// 判断COM口是否属于正在使用的控制板、不属于则添加
		if (!this.usefulPortList.contains(portname)) {
			this.usefulPortList.add(portname);
			this.usefulPortList2.put(portname, gate_id);
			this.idleFlag.put(portname, true);
			this.setTimeFlag.put(portname, false);
			this.useOneBoardFlag.put(portname, "");

			this.openFlag.put(portname, false);
		}

		JavaRs rs = ((JavaRs) comOpreation.get(portname));
		new Thread(new ClockMonitor(portname), "ClockMonitor" + portname).start();// 运行监听portname端口刷卡记录的线程
		new Thread(new RsMonitor(rs), "RsMonitor" + portname).start();
		new Thread(new GetLostMonitor(portname), "GetLostMonitor" + portname).start();
		boolean flag = rs.openSerialPort();
		if (flag) {
			openFlag.put(portname, true);
			remegs.rightMesg("成功连接控制板", null);
		} else {
			remegs.errorMesg("连接控制板失败", null);
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
		}
		// this.idleFlag.put(portname, true);
		return remegs;

	}

	/*
	 * 关闭和控制板的连接 参数：1、portname String 控制板对应的COM号
	 * 
	 * @luoying 2017-08-16 17:03:40
	 */
	public GateReturnMessage closeAPort(String portname) {
		// this.idleFlag.put(portname, false);
		GateReturnMessage remegs = new GateReturnMessage();// 返回数据

		// 判断电脑是否扫描到COM号
		if (!this.portList.contains(portname)) {
			remegs.paramErrorMesg("该COM号不存在");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
			// this.idleFlag.put(portname, true);
			return remegs;
		}
		if (this.openFlag.containsKey(portname) && !this.openFlag.get(portname)) {
			remegs.rightMesg("已关闭控制板", null);
			// this.idleFlag.put(portname, true);
			return remegs;
		}
		if (this.usefulPortList.contains(portname)) {
			this.usefulPortList.remove(portname);
			this.usefulPortList2.remove(portname);
			this.idleFlag.remove(portname);
			this.setTimeFlag.remove(portname);
			this.useOneBoardFlag.remove(portname);

			this.openFlag.remove(portname);
		}

		JavaRs rs = ((JavaRs) comOpreation.get(portname));
		boolean flag = rs.closeSerialPort();
		if (flag) {
			openFlag.put(portname, false);
			remegs.rightMesg("成功关闭和控制板的连接", null);
		} else {
			remegs.errorMesg("连关闭和控制板的连接失败", null);
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
		}

		// this.idleFlag.put(portname, true);
		return remegs;
	}

	/*
	 * 1、跟指定COM号的控制板握手 参数：1、portname String 控制板对应的COM号
	 * 
	 * @luoying 2017-08-16 18:11:31
	 */
	public GateReturnMessage handshake(String portname) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] sendBs = { (byte) 0XA5, 0X01, 0X00, 0X00, 0X00, 0X0D, 0X0A };
			GateBoardUtil.addCRC(sendBs);

			JavaRs rs = ((JavaRs) comOpreation.get(portname));

			// 握手，如果3次都无反应，报通讯线路故障。
			int count = 0;
			while (count++ < 3) {
				// 初始化握手接收数据
				rs.setReciHandMes("");
				// rs.setBufs(new ArrayList<>());//串口监听接收数据的缓冲区
				// rs.setBufsLen(new ArrayList<>());

				Date a = new Date();
				rs.sendDataToSeriaPort(sendBs);// 发送握手请求

				// 判断在指定时间内是否握手成功,初步设定为5s
				while ((new Date().getTime() - a.getTime()) < 2 * 1000) {
					try {
						Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中

					if (!"".equals(rs.getReciHandMes())) {
						System.out.println("握手成功");
						remegs.rightMesg("握手成功", null);
//						this.idleFlag.put(portname, true);

						// 握手之后设置一下控制板的时间
						if (!this.setTimeFlag.containsKey(portname) || !this.setTimeFlag.get(portname)) {
							GateReturnMessage remegs2 = this.setControllerTime(portname);
							if (200 == remegs2.getCode()){
								idleFlag.get(portname).notify();
								return remegs;
							}
								
						} else{
							idleFlag.get(portname).notify();
							return remegs;
						}

					}
				}
			}

			System.out.println("超时,握手失败");
			remegs.commuErrorMesg();
			remegs.setMessage("超时,握手失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;

		}
	}

	/*
	 * 2、设置指定COM号的控制板的通道（常开、正常、常闭） 参数： 1、portname String 控制板对应的COM号 2、gateNo int
	 * 闸机编号（0、1-8；0表示该控制板的所有闸机、1-8表示该控制板对应的某个闸机） 3、state int 通道状态（0代表常开，1代表正常，2代表常闭）
	 * 
	 * @luoying 2017-08-17 14:40:58
	 */
	public GateReturnMessage setGateState(String portname, int gateNo, int state) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			/*
			 * if(state!=0 && state!=1 && state!=2 && gateNo>=0 && gateNo<=8){
			 * System.out.println("参数格式错误，设置通道状态失败");
			 * remegs.paramErrorMesg("参数格式错误，设置通道状态失败"); return remegs; }
			 */

			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] sendBs = { (byte) 0XA5, 0X02, 0X02, 0X02, 0X10, 0X00, 0X00, 0X0D, 0X0A };
			sendBs[3] = (byte) gateNo;
			sendBs[4] = (byte) (160 + state);
			GateBoardUtil.addCRC(sendBs);

			JavaRs rs = ((JavaRs) comOpreation.get(portname));

			// 初始化接受数据
			rs.setMesg2(null);
			// rs.setBufs(new ArrayList<>());//串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());

			Date a = new Date();
			rs.sendDataToSeriaPort(sendBs);// 发送请求

			// 判断在指定时间内是否握手成功,初步设定为5s
			while ((new Date().getTime() - a.getTime()) < 5 * 1000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中

				if (rs.getMesg2() != null) {
					if ((byte) 0xf0 == rs.getMesg2()[3]) {
						System.out.println("设置通道状态成功");
						remegs.rightMesg("设置通道状态成功", null);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if ((byte) 0xf1 == rs.getMesg2()[3]) {
						System.out.println("设置通道状态失败");
						remegs.errorMesg("设置通道状态失败", null);
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}

				}
			}
			System.out.println("超时,设置通道状态失败");
			remegs.commuErrorMesg();
			remegs.setMessage("超时,设置通道状态失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
		
	}

	/*
	 * 3、查询通道状态（常开，正常，常闭） 参数： 1、portname String 控制板对应的COM号 2、gateNo int 闸机编号（1-8）
	 * 返回值： 1、ReturnMessage.data=0表示常开 2、ReturnMessage.data=1表示正常
	 * 2、ReturnMessage.data=2表示常闭
	 * 
	 * @luoying 2017-08-17 16:43:26
	 */
	public GateReturnMessage checkGateState(String portname, int gateNo) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			/*
			 * if (gateNo <= 0 || gateNo > 8) { System.out.println("参数格式错误，设置通道状态失败");
			 * remegs.paramErrorMesg("参数格式错误，设置通道状态失败"); return remegs; }
			 */

			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] sendBs = { (byte) 0XA5, 0X03, 0X01, 0X05, 0X00, 0X00, 0X0D, 0X0A };
			sendBs[3] = (byte) gateNo;
			GateBoardUtil.addCRC(sendBs);

			JavaRs rs = ((JavaRs) comOpreation.get(portname));

			// 初始化接受数据
			rs.setMesg3(null);
			// rs.setBufs(new ArrayList<>());// 串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());

			Date a = new Date();
			rs.sendDataToSeriaPort(sendBs);// 发送请求

			// 判断在指定时间内是否查询成功,初步设定为5s
			while ((new Date().getTime() - a.getTime()) < 10 * 1000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中

				if (rs.getMesg3() != null) {
					byte b = rs.getMesg3()[4];
					byte[] bbs = rs.getMesg3();
					if ((byte) 0xa0 == rs.getMesg3()[4]) {
						System.out.println("常开");
						remegs.rightMesg("查询通道状态成功", 0);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if ((byte) 0xa1 == rs.getMesg3()[4]) {
						System.out.println("正常");
						remegs.rightMesg("查询通道状态成功", 1);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if ((byte) 0xa2 == rs.getMesg3()[4]) {
						System.out.println("常闭");
						remegs.rightMesg("查询通道状态成功", 2);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}

				}
			}

			System.out.println("超时,查询通道状态失败");
			remegs.commuErrorMesg();
			remegs.setMessage("超时,查询通道状态失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}		
	}

	/*
	 * 4、批量下传人员授权信息（单条信息下传超时继续发送） 参数： 1、portname String 控制板对应的COM号 2、infos
	 * List<String[]> 每个String[]按顺序保存单个人员的身份证号、IC卡号、人脸编号，权限位 规定：
	 * 1、身份证号、IC卡号、人脸编号如果没有则用""表示
	 * 
	 * @luoying 2017-08-21 16:04:07
	 */
	public GateReturnMessage addSomeCard(String portname, List<String[]> infos) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}
			if (null == infos) {
				remegs.paramErrorMesg("参数传递错误");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			int missCount = 0;// 未成功下传的编号个数
			int rightCount = 0;// 成功下传的编号个数
			List<String[]> missId = new ArrayList<>();// 未成功下传的人员信息
			List<GateReturnMessage> missReMesg = new ArrayList<>();// 保存未成功下传的编号对应的返回信息
			for (int i = 0; i < infos.size(); i++) {
				String[] info = infos.get(i);

				GateReturnMessage megs = new GateReturnMessage();// 单条数据的返回数据

				megs = this.addOneAuthInfo(portname, info[0], info[1], info[2], info[3]);

				if (200 == megs.getCode())
					rightCount++;
				else {
					missCount++;
					missId.add(info);
					missReMesg.add(megs);
				}
			}

			if (infos.size() == rightCount) {
				System.out.println("批量下传授权信息成功");
				remegs.rightMesg("批量下传授权信息成功", null);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			} else {
				// 将下传失败的人员信息和其失败原因对应
				HashMap<String[], String> missIdAndReason = new HashMap<String[], String>();
				for (int i = 0; i < missId.size(); i++) {
					missIdAndReason.put(missId.get(i), missReMesg.get(i).getMessage());
				}
				remegs.errorMesg("批量下传授权信息失败," + missCount + "条未成功下传", missIdAndReason);
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}
		}
	}

	/*
	 * 5、批量上传授权信息 参数：1、portname String 控制板对应的COM号
	 * 
	 * @luoying 2017-08-21 09:38:07
	 */
	public GateReturnMessage getAllAuthInfo(String portname) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] sendBs = { (byte) 0XA5, 0X05, 0X00, 0X00, 0X00, 0X0D, 0X0A };
			GateBoardUtil.addCRC(sendBs);

			JavaRs rs = ((JavaRs) comOpreation.get(portname));

			boolean flag = false;// 是否接收到结束信号,false表示没有

			// 初始化接受数据
			rs.setMesg5(null);// 初始化单个接受数据
			rs.setMesg5List(new ArrayList<>());
			// rs.setBufs(new ArrayList<>());//串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());

			Date a = new Date();// 等待接受数据的开始时间
			rs.sendDataToSeriaPort(sendBs);// 发送请求

			List<String[]> listInfo = new ArrayList<>();// 保存所有人员的卡号和权限位
			List<byte[]> bsList = new ArrayList<>();// 保存已收到的批量上传的数据

			while ((new Date().getTime() - a.getTime()) < 10 * 1000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中

				bsList = rs.getMesg5List();// 获取所有已收到的批量上传的数据

				if (0 != bsList.size()) {
					// 如果收到上传结束信号，则接收成功
					byte[] lastInts = bsList.get(bsList.size() - 1);// 收到的最后一条数据

					if ((byte) 0xb1 == lastInts[3]) {
						System.out.println("getMesg5List:" + bsList.size());
						flag = true;
						break;
					}
				}

			}

			// 提取数据帧中的卡号和权限位
			for (int i = 0; i < bsList.size(); i++) {
				if ((byte) 0xb1 != bsList.get(i)[3])
					listInfo.add(GateBoardUtil.getNoAndAuthByFrame(bsList.get(i)));
			}

			for (String[] strs : listInfo) {
				for (String string : strs) {
					System.out.print(string + ",");
				}
				System.out.println();
			}

			if (flag) {// 收到结束信号
				System.out.println("成功收到所有上传的授权信息");
				remegs.rightMesg("成功收到所有上传的授权信息", listInfo);
				System.out.println("人员数量：" + listInfo.size());
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			// 超时
			System.out.println("超时,批量上传授权信息失败");
			remegs.setData(listInfo);// 保存并返回接收到的部分数据
			remegs.commuErrorMesg();
			remegs.setMessage("超时,批量上传授权信息失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
	}

	/*
	 * 6、增加单个人员授权信息 参数： 1、id String 身份证号 长度=18或为空 2、IC_id String IC卡号 长度<=10或为空
	 * 3、face_id String 人脸识别编号 长度<=10或为空 4、authority String 权限位，八个01组成的字符串 规定：
	 * 1、参数按顺序传入，如果没有则用""表示
	 * 
	 * @luoying 2017-08-18 09:46:51
	 */
	public GateReturnMessage addOneAuthInfo(String portname, String id, String IC_id, String face_id,
			String authority) {

//		this.idleFlag.put(portname, false);
		GateReturnMessage remegs = new GateReturnMessage();// 返回数据

		// 判断COM号是否存在
		if (!this.usefulPortList.contains(portname)) {
			remegs.paramErrorMesg("该COM号不存在");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			return remegs;
		}

		// 判断该人员是否已经存在控制板，如果已存在，则先删除再增加
		boolean flag = false;// 该人员是否已经被删除，false没有
		GateReturnMessage isExist = new GateReturnMessage();
		if (!"".equals(id) && !flag) {
			isExist = this.queryOneAuthority(portname, id, "", "");
			if (null != isExist.getData() && 1 == (int) isExist.getData()) {
				this.delOneAuthInfo(portname, id, "", "");
				flag = true;
			}
		}
		if (!"".equals(IC_id) && !flag) {
			isExist = this.queryOneAuthority(portname, "", IC_id, "");
			if (null != isExist.getData() && 1 == (int) isExist.getData()) {
				this.delOneAuthInfo(portname, "", IC_id, "");
				flag = true;
			}
		}
		if (!"".equals(face_id) && !flag) {
			isExist = this.queryOneAuthority(portname, "", "", face_id);
			if (null != isExist.getData() && 1 == (int) isExist.getData()) {
				this.delOneAuthInfo(portname, "", "", face_id);
				flag = true;
			}
		}

		synchronized (idleFlag.get(portname)) {
			/* 确定发送数据 */
			byte sendBs[] = new byte[27];// 发送的数据
			byte[] bsHead = { (byte) 0XA5, 0X06, 0X14 };// 帧头 命令码 长度
			byte[] bsMid = GateBoardUtil.setCardsStyle(id, IC_id, face_id);// 卡号
			byte[] bsEnd = { 0X00, 0X00, 0X00, 0X0D, 0X0A };// 权限 校验码 帧尾

			int t = Integer.parseInt(authority, 2);// 将二进制字符串表示的权限位转化成int
			int r = t <= 127 ? t : t - 256;
			bsEnd[0] = (byte) r;

			/* 组合发送数据 */
			sendBs = GateBoardUtil.concatByte(bsHead, bsMid, bsEnd);
			GateBoardUtil.addCRC(sendBs);

			JavaRs rs = ((JavaRs) comOpreation.get(portname));

			// 初始化接受数据
			rs.setMesg6(null);
			// rs.setBufs(new ArrayList<>());// 串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());

			Date a = new Date();
			rs.sendDataToSeriaPort(sendBs);// 发送请求

			// 判断在指定时间内是否增加成功,初步设定为5s
			while ((new Date().getTime() - a.getTime()) < 1 * 1000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中

				if (rs.getMesg6() != null) {
					if ((byte) 0xf0 == rs.getMesg6()[3]) {
						System.out.println("增加单个授权信息成功");
						remegs.rightMesg("增加单个授权信息成功", null);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if ((byte) 0xf1 == rs.getMesg6()[3]) {
						System.out.println("增加单个授权信息失败");
						remegs.errorMesg("增加单个授权信息失败", null);
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if ((byte) 0xf2 == rs.getMesg6()[3]) {
						System.out.println("该授权信息已存在，增加失败");
						remegs.dateExsitMesg("该授权信息已存在，增加失败");
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}

				}
			}

			System.out.println("超时，增加单个授权信息失败");
			remegs.commuErrorMesg();
			remegs.setMessage("超时，增加单个授权信息失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}

	}

	/*
	 * 7、删除单个人员授权信息 参数：1、id String 身份证号 长度=18或为空 2、IC_id IC卡号 长度<=10或为空
	 * 3、face_id人脸识别编号 长度<=10或为空 规定： 1、参数按顺序传入，如果没有则用""表示 2、可以通过一个或多个卡号来删除某个人员的授权信息
	 * 
	 * @luoying 2017-08-18 09:47:58
	 */
	public GateReturnMessage delOneAuthInfo(String portname, String id, String IC_id, String face_id) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			/* 确定发送数据 */
			byte sendBs[] = new byte[27];// 发送的数据
			byte[] bsHead = { (byte) 0XA5, 0X07, 0X13 };// 帧头 命令码 长度
			byte[] bsMid = GateBoardUtil.setCardsStyle(id, IC_id, face_id);// 卡号
			byte[] bsEnd = { 0X00, 0X00, 0X0D, 0X0A };// 权限 校验码 帧尾

			/* 组合发送数据 */
			sendBs = GateBoardUtil.concatByte(bsHead, bsMid, bsEnd);
			GateBoardUtil.addCRC(sendBs);

			JavaRs rs = ((JavaRs) comOpreation.get(portname));

			Date a = new Date();
			rs.setMesg7(null);// 初始化接受数据
			// rs.setBufs(new ArrayList<>());
			// rs.setBufsLen(new ArrayList<>());
			rs.sendDataToSeriaPort(sendBs);// 发送请求

			// 判断在指定时间内是否删除成功,初步设定为5s
			while ((new Date().getTime() - a.getTime()) < 5000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中

				if (rs.getMesg7() != null) {
					if ((byte) 0xf0 == rs.getMesg7()[3]) {
						System.out.println("删除单个授权信息成功");
						remegs.rightMesg("删除单个授权信息成功", null);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if ((byte) 0xf1 == rs.getMesg7()[3]) {
						System.out.println("删除单个授权信息失败");
						remegs.errorMesg("删除单个授权信息失败", null);
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if ((byte) 0xf3 == rs.getMesg7()[3]) {
						System.out.println("该授权信息不存在，删除失败");
						remegs.dateNotExsitMesg("该授权信息不存在，删除失败");
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}

				}
			}

			System.out.println("删除单个授权信息失败，超时");
			remegs.commuErrorMesg();
			remegs.setMessage("删除单个授权信息失败，超时");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
		
	}

	/**
	 * 8、查询单个是否授权
	 * 
	 * @author huangju 2017-08-18 10:10:00
	 * @param portname
	 *            COM口号
	 * @param String
	 *            id 身份证号 长度<=18或为空
	 * @param String
	 *            IC_id 长度<=10或为空
	 * @param String
	 *            face_id 长度<=10或为空
	 * @return GateReturnMessage
	 */
	public GateReturnMessage queryOneAuthority(String portname, String id, String IC_id, String face_id) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] middle = GateBoardUtil.setCardsStyle(id, IC_id, face_id);
			byte[] head = { (byte) 0xa5, 0x08, 0x13 };//
			byte[] tail = { 0x00, 0x00, 0x0d, 0x0a };//
			byte[] sendBs = GateBoardUtil.concatByte(head, middle, tail);
			GateBoardUtil.addCRC(sendBs);

			// 转化为要发送的数据帧形式
			JavaRs rs = (JavaRs) comOpreation.get(portname);
			// 初始化接收数据
			rs.setMesg8(null);
			// rs.setBufs(new ArrayList<>());// 串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());
			rs.sendDataToSeriaPort(sendBs);
			Date a = new Date();

			// 判断在指定时间内是否查询成功,初步设定为5s
			while ((new Date().getTime() - a.getTime()) < 5 * 1000) {
				// 必须加时间间隔，用于区分接收数据和处理数据的先后顺序
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中
				if (rs.getMesg8() != null) {
					byte[] temp = rs.getMesg8();
					// 当接收的信息不为空且返回的参数码为数据存在（F2）,rs.getMesg8()[3]为标志位
					if (temp[3] == (byte) 0xf2) {
						System.out.println("已授权");
						// 1表示已授权
						remegs.rightMesg("已授权", 1);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}
					// 当传入的id不存在时返回标志（F3）
					else if (temp[3] == (byte) 0xf3) {
						System.out.println("未授权");
						remegs.dateNotExsitMesg("未授权");
						// 0表示未授权
						remegs.setData(0);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}
				}
			}
			System.out.println("超时,查询单个是否授权失败");
			remegs.commuErrorMesg();
			remegs.setMessage("超时,查询单个是否授权失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
		
	}

	/**
	 * 10、清除所有授权信息（清空控制器flash）
	 * 
	 * @author huangju 2017-08-17 18:57:00
	 * @param portname
	 *            COM口号
	 * @return GateReturnMessage
	 */
	public GateReturnMessage resetAllAuthority(String portname) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] sendBs = { (byte) 0xa5, 0x10, 0x00, 0x00, 0x00, 0x0d, 0x0a };

			GateBoardUtil.addCRC(sendBs);
			JavaRs rs = (JavaRs) comOpreation.get(portname);
			rs.setBufs(new ArrayList<>());// 串口监听接收数据的缓冲区
			// 初始化接收数据
			rs.setMesg10(null);
			// rs.setBufs(new ArrayList<>());
			// rs.setBufsLen(new ArrayList<>());
			rs.sendDataToSeriaPort(sendBs);

			Date a = new Date();
			// 判断在指定时间内是否查询成功,初步设定为5s，清除所有信息板子上会耗时30秒
			while ((new Date().getTime() - a.getTime()) < 1000 * 35) {
				// 必须加时间间隔，用于区分接收数据和处理数据的先后顺序
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中
				if (rs.getMesg10() != null) {
					// 当接收的信息不为空且返回的参数码为成功标志（F0），rs.getMesg10()[3]为标志位
					if (rs.getMesg10()[3] == (byte) 0xf0) {
						System.out.println("清除成功");
						// 1表示清除成功
						remegs.rightMesg("清除成功", 1);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}
					// 当不存在时数据时返回标志（F1）
					else if (rs.getMesg10()[3] == (byte) 0xf1) {
						System.out.println("清除失败");
						// 0表示清除失败
						remegs.errorMesg("清除失败", 0);
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}
				}
			}
			System.out.println("超时,清除所有授权信息失败");
			remegs.commuErrorMesg();
			remegs.setMessage("超时,清除所有授权信息失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
		

	}

	/**
	 * 11.接收一个刷卡信息
	 * 
	 * @author huangju 2017-08-18 15:16:00
	 * @param portname
	 *            COM口号
	 */
	public void reciOneCardInfo(String portname) {
//		synchronized (idleFlag.get(portname)) {
			JavaRs rs = ((JavaRs) comOpreation.get(portname));
			while (true) {
				/*try {
					idleFlag.get(portname).wait(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				
				try {
					Thread.sleep(1);// 必须加时间间隔，用于区分接收数据和处理数据的先后顺序
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (rs.getCheckBytes() != null && rs.getCheckBytes().size() != 0) {
					System.out.println("11");

					byte[] reciBs = rs.getCheckBytes().get(0);
					rs.getCheckBytes().remove(0);

					// 提取出卡号id
					byte[] temp = new byte[reciBs[2] - 7];
					for (int i = 0; i < temp.length; i++) {
						temp[i] = reciBs[i + 3];
					}
					// 处理后的id字段
					String tempid = GateBoardUtil.bytesToString(temp);
					// 确定刷卡类型，长度为18即为身份证，type用0表示，长度为10即为IC卡，type用1表示，
					int type = tempid.length() == 18 ? 0 : 1;
					// 处理时间字段
					int[] temp2 = new int[6];
					for (int i = 0; i < 6; i++) {
						temp2[i] = reciBs[i + reciBs[2] - 3 - 1];
					}
					// 临时保存处理后的时间字段
					String time = GateBoardUtil.timeTypeChange(temp2);
					// System.out.println("time"+time);
					// 获取系统当前时间
					Date date = new Date();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String nowtime = format.format(date);
					byte gateNo = reciBs[reciBs[2] + 7 - 5];
					int gateno = gateNo;
					String[] result = { tempid, GateBoardUtil.select_time(time, nowtime), String.valueOf(type), portname,
							String.valueOf(gateno) };
					this.checkLogs.add(result);

					for (int i = 0; i < result.length; i++) {
						System.out.println(result[i]);
					}
				}

			}
//		}
		
	}

	/**
	 * 12.查询一段时间内的刷卡记录
	 * 
	 * @author huangju 2017-08-21 15:42:00
	 * @param portname
	 *            COM口号
	 * @param start_time
	 *            开始时间"2017-08-19 14:54:00"格式
	 * @param end_time
	 *            结束时间"2017-08-19 14:54:00"格式
	 * @return GateReturnMessage
	 */
	public GateReturnMessage queryCardInfoByTime(String portname, String start_time, String end_time) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			byte[] head = { (byte) 0xa5, 0x12, 0x06 };
			byte[] tail = { 0x00, 0x00, 0x0d, 0x0a };
			byte[] start = GateBoardUtil.stringToIntsTime(start_time);
			byte[] end = GateBoardUtil.stringToIntsTime(end_time);
			byte[] sendBs = GateBoardUtil.concatByte(head, start, end, tail);
			JavaRs rs = ((JavaRs) comOpreation.get(portname));
			rs.setI(0);
			boolean flag = false;// 是否接收到结束信号,false表示没有
			// 初始化接受数据
			rs.setMesg12(null);// 初始化单个接受数据
			rs.setMesg12List(new ArrayList<>());
			// rs.setBufs(new ArrayList<>());//串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());

			GateBoardUtil.addCRC(sendBs);
			Date a = new Date();// 等待接受数据的开始时间
			rs.sendDataToSeriaPort(sendBs);// 发送请求

			List<String[]> listInfo = new ArrayList<>();// 保存所有人员的卡号和刷卡时间
			List<byte[]> intsList = new ArrayList<>();// 保存已收到的批量上传的数据

			while ((new Date().getTime() - a.getTime()) < 50 * 1000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 处理缓冲区中的数据，并保存在相应变量中
				// rs.delBufs();
				// 获取所有已收到的批量上传的数据
				intsList = rs.getMesg12List();
				int len = intsList.size();
				// 如果收到上传结束信号，则接收成功
				if (intsList.size() != 0) {
					// 收到的最后一条数据
					byte[] lastInts = intsList.get(intsList.size() - 1);
					if ((byte) 0xb1 == lastInts[3]) {
						System.out.println("getMesg12List:" + intsList.size());
						flag = true;
						break;
					}
				}
			}
			System.out.println("记录数：" + intsList.size());
			// 提取数据帧中的卡号和时间
			for (int i = 0; i < intsList.size(); i++) {
				if ((byte) 0xb1 != intsList.get(i)[3]) {
					// 得到处理后的卡号和时间、类型、闸机通道编号
					if (0 != intsList.size()) {
						if (GateBoardUtil.comArray(this.getThelastframeI(), intsList.get(0))) {
							intsList.remove(0);
						}
						String[] info = GateBoardUtil.getCardIDAndTime(intsList.get(i));
						listInfo.add(info);
					}
				}
			}
			if (0 != intsList.size()) {
				this.setThelastframeI(intsList.get(intsList.size() - 1));// 将本次的最后一条保存
			}

			for (String[] strs : listInfo) {
				for (String string : strs) {
					System.out.print(string + ",");
				}
				System.out.println();
			}
			if (flag) {// 收到结束信号
				System.out.println("成功收到所有刷卡记录");
				remegs.rightMesg("成功收到所有刷卡记录", listInfo);
				System.out.println("处理后的记录数：" + listInfo.size());
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}
			// 超时
			System.out.println("超时,接收刷卡记录失败");
			remegs.setData(listInfo);// 保存并返回接收到的部分数据
			remegs.commuErrorMesg();
			remegs.setMessage("超时,接收刷卡记录失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
		
	}

	/**
	 * 13、手动打开通道
	 * 
	 * @author huangju 2017-08-17 17:40:00
	 * @param portname
	 *            COM口号
	 * @param pathNo
	 *            要打开的通道号
	 * @return GateReturnMessage
	 */
	public GateReturnMessage openGateByHand(String portname, int pathNo) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数
			// 判断COM号是否存在

			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] sendBs = { (byte) 0XA5, 0X13, 0X01, 0x00, 0X00, 0X00, 0X0D, 0X0A };
			sendBs[3] = (byte) pathNo;
			JavaRs rs = ((JavaRs) comOpreation.get(portname));
			// 初始化接收数据
			rs.setMesg13(null);
			// rs.setBufs(new ArrayList<>());// 串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());
			GateBoardUtil.addCRC(sendBs);
			rs.sendDataToSeriaPort(sendBs);

			Date a = new Date();
			// 判断在指定时间内是否设置成功,初步设定为5s
			while ((new Date().getTime() - a.getTime()) < 5000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中
				if (rs.getMesg13() != null) {
					// 当接收的信息不为空且返回的参数码为成功标志（F0），rs.getMesg14()[3]为标志位
					if (rs.getMesg13()[3] == (byte) 0xf0) {
						System.out.println("打开成功:打开的通道号为" + pathNo);
						// 1表示打开成功
						remegs.rightMesg("打开成功", 1);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if (rs.getMesg13()[3] == (byte) 0xf1) {
						System.out.println("打开失败");
						// 0表示打开失败
						remegs.errorMesg("打开失败", 0);
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}
				}
			}
			System.out.println("超时,手动打开通道失败");
			remegs.commuErrorMesg();
			remegs.setMessage("超时,手动打开通道失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
		
	}

	/**
	 * 14、查询授权总数
	 * 
	 * @huangju 2017-08-17 17:13:00 参数： 1.portname COM口号
	 *          2.在JavaRS中定义一个全局变量totalauthority（int），保存查询到的授权总数
	 */
	public GateReturnMessage queryTotalAuthority(String portname) {
		synchronized (idleFlag.get(portname)) {
//			this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			int totalauthority = 0;// 保存查询到的授权总数
			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}
			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] sendBs = { (byte) 0XA5, 0X14, 0x00, 0X00, 0X00, 0X0D, 0X0A };
			JavaRs rs = (JavaRs) comOpreation.get(portname);
			// 初始化接收数据
			rs.setMesg14(null);
			// rs.setBufs(new ArrayList<>());// 串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());
			GateBoardUtil.addCRC(sendBs);
			rs.sendDataToSeriaPort(sendBs);
			Date a = new Date();
			// 判断在指定时间内是否查询成功,初步设定为5s
			while ((new Date().getTime() - a.getTime()) < 5000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中
				if (rs.getMesg14() != null) {
					// 当接收的信息不为空且返回的参数码为成功标志（F0），rs.getMesg14()[3]为标志位，rs.getMesg14()[4]+rs.getMesg14()[5]为授权数量
					if (rs.getMesg14()[3] == (byte) 0xf0) {
						byte[] tempbyte = { rs.getMesg14()[4], rs.getMesg14()[5] };
						totalauthority = Integer.parseInt(util.bytes2hex01(tempbyte), 16);
						System.out.println("Totalauthority:" + totalauthority);
						System.out.println("查询成功");
						remegs.rightMesg("查询成功", totalauthority);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if (rs.getMesg14()[3] == (byte) 0xf1) {
						System.out.println("查询失败");
						// 0表示查询失败
						remegs.errorMesg("查询失败", 0);
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}
				}
			}
			System.out.println("超时,查询授权总数失败");
			remegs.commuErrorMesg();
			remegs.setMessage("超时,查询授权总数失败");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
		

	}

	/**
	 * 15、设置控制器时间
	 * 
	 * @author huangju 2017-08-17 10:57:00
	 * @param portname
	 *            COM口号
	 * @return GateReturnMessage
	 */
	public GateReturnMessage setControllerTime(String portname) {
		synchronized (idleFlag.get(portname)) {
	//		this.idleFlag.put(portname, false);
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据

			// 判断COM号是否存在
			if (!this.usefulPortList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//				this.idleFlag.put(portname, true);
				idleFlag.get(portname).notify();
				return remegs;
			}

			Calendar now = Calendar.getInstance();
			// 发送数据（根据命令对应的十六进制并转换成字节）
			int year = now.get(Calendar.YEAR) % 2000;
			int month = now.get(Calendar.MONTH) + 1;
			int day = now.get(Calendar.DAY_OF_MONTH);
			int hour = now.get(Calendar.HOUR_OF_DAY);
			int minuter = now.get(Calendar.MINUTE);
			int second = now.get(Calendar.SECOND);

			byte[] sendBs = { (byte) 0xa5, 0x15, 0x06, (byte) year, (byte) month, (byte) day, (byte) hour, (byte) minuter,
					(byte) second, 0x00, 0x00, 0x0d, 0x0a };

			JavaRs rs = ((JavaRs) comOpreation.get(portname));
			// 初始化接收数据
			rs.setMesg15(null);
			// rs.setBufs(new ArrayList<>());// 串口监听接收数据的缓冲区
			// rs.setBufsLen(new ArrayList<>());
			GateBoardUtil.addCRC(sendBs);
			rs.sendDataToSeriaPort(sendBs);
			Date a = new Date();
			// 判断在指定时间内是否设置成功,初步设定为5s
			while ((new Date().getTime() - a.getTime()) < 5000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// rs.delBufs();//处理缓冲区中的数据，并保存在相应变量中
				if (rs.getMesg15() != null) {
					// 当接收的信息不为空且返回的参数码为成功标志（F0），rs.getMesg14()[3]为标志位
					if (rs.getMesg15()[3] == (byte) 0xf0) {
						System.out.println("设置成功");
						// 1表示设置成功
						remegs.rightMesg("设置成功", 1);
	//					this.idleFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					} else if (rs.getMesg15()[3] == (byte) 0xf1) {
						System.out.println("设置失败");
						// 0表示设置失败
						remegs.errorMesg("设置失败", 0);
						this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//						this.idleFlag.put(portname, true);
						this.setTimeFlag.put(portname, true);
						idleFlag.get(portname).notify();
						return remegs;
					}
				}
			}
			System.out.println("超时,设置控制器时间");
			remegs.commuErrorMesg();
			remegs.setMessage("超时,设置控制器时间");
			this.getService().getConnectLogService().add("4", remegs.getMessage(), this.gate_id);
//			this.idleFlag.put(portname, true);
			idleFlag.get(portname).notify();
			return remegs;
		}
		
	}

	
	/**
	 * 16、被动接收未上传的刷卡记录
	 * 
	 * @param portname
	 *            COM口号
	 * @return ReturnMessage
	 */
	public void getLostCheckLogPassive(String portname) {
		// 定义发送数据（根据命令对应的十六进制并转换成字节）
		JavaRs rs = ((JavaRs) comOpreation.get(portname));
		// 初始化接受数据
		rs.setMesg16(null);// 初始化单个接受数据
		while (true) {
			try {
				Thread.sleep(1);//线程睡眠，用于确定接收数据和处理顺序的先后顺序
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(rs.getLostcheckBytes()!=null&&rs.getLostcheckBytes().size()!=0) {
				byte[] tempcheck=rs.getLostcheckBytes().get(0);
				rs.getLostcheckBytes().remove(0);
				if(tempcheck[3]!=(byte)0xb1) {
					String cardID[]=GateBoardUtil.getCardIDAndTime(tempcheck);
					String[] cardIDreturn = { cardID[0], cardID[1], cardID[2], portname, cardID[3] };
					this.checkLogs.add(cardIDreturn);
					for(String mi:cardIDreturn) {
						System.out.println(mi);
					}
				}
			}
			rs.setMesg16(null);
		}	
		
	}

	
	
	
	/**
	 * 16、主动查询未上传的刷卡记录
	 * 
	 * @param portname
	 *            COM口号
	 * @return ReturnMessage
	 */
	public GateReturnMessage getLostCheckLog(String portname) {
		synchronized (idleFlag.get(portname)) {
			GateReturnMessage remegs = new GateReturnMessage();// 返回数据
			// 判断COM号是否存在
			if (!this.portList.contains(portname)) {
				remegs.paramErrorMesg("该COM号不存在");
				idleFlag.get(portname).notify();
				return remegs;
			}
			// 定义发送数据（根据命令对应的十六进制并转换成字节）
			byte[] sendBs = { (byte) 0XA5, 0X16, 0X00, 0X00, 0X00, 0X0D, 0X0A };
			GateBoardUtil.addCRC(sendBs);
			JavaRs rs = ((JavaRs) comOpreation.get(portname));
			rs.setJ(0);
			boolean flag = false;// 是否接收到结束信号,false表示没有
			// 初始化接受数据
			rs.setMesg16(null);// 初始化单个接受数据
			rs.setMesg16List(new ArrayList<>());
			Date a = new Date();// 等待接受数据的开始时间
			rs.sendDataToSeriaPort(sendBs);// 发送请求
			List<String[]> listInfo = new ArrayList<>();// 保存所有人员的卡号和权限位
			List<byte[]> bsList = new ArrayList<>();// 保存已收到的批量上传的数据
			int sizebefor = 0;
			while ((new Date().getTime() - a.getTime()) < 10 * 1000) {
				try {
					Thread.sleep(1);// 线程睡眠，用于确定接收数据和处理顺序的先后顺序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bsList = rs.getMesg16List();// 获取所有已收到的批量上传的数据
				if (0 != bsList.size()) {
					if (sizebefor != bsList.size())
						a = new Date();
					sizebefor = bsList.size();
					// 如果收到上传结束信号，则接收成功
					byte[] lastInts = bsList.get(bsList.size() - 1);// 收到的最后一条数据
					if (8 == lastInts.length && (byte) 0xb1 == lastInts[3]) {
						System.out.println("getMesg16List:" + bsList.size());
						flag = true;
						break;
					}
				}
			}
			// 提取数据帧中的卡号和权限位
			for (int i = 0; i < bsList.size(); i++) {

				if ((byte) 0xb1 != bsList.get(i)[3]) {
					if (0 != bsList.size()) {
						// 本次接收的第一次与上次的最后一条比较，重复则去掉
						if (GateBoardUtil.comArray(this.getThelastframeJ(), bsList.get(0))) {
							bsList.remove(0);
						}
						// 得到帧中的卡号，时间，类型，通道号
						String[] get = GateBoardUtil.getCardIDAndTime(bsList.get(i));
						listInfo.add(get);
						String[] result = { get[0], get[1], get[2], portname, get[3] };
						this.checkLogs.add(result);
					}
				}
			}
			if (0 != bsList.size()) {
				this.setThelastframeJ(bsList.get(bsList.size() - 1));// 将本次的最后一条保存
			}

			for (String[] strs : listInfo) {
				for (String string : strs) {
					System.out.print(string + ",");
				}
				System.out.println();
			}

			if (flag) {// 收到结束信号
				System.out.println("成功收到所有上传的未上传刷卡记录");
				remegs.rightMesg("成功收到所有上传的未上传刷卡记录", listInfo);
				System.out.println("人员数量：" + listInfo.size());
				idleFlag.get(portname).notify();
				return remegs;
			}

			// 超时
			System.out.println("超时,批量上传未上传刷卡记录失败");
			remegs.setData(listInfo);// 保存并返回接收到的部分数据
			remegs.commuErrorMesg();
			idleFlag.get(portname).notify();
			return remegs;

		}
		
	}

	/**
	 * 同步usefulPortList和idleFlag及setTimeFlag中的COM编号 usefulPortList的值是对的
	 */
	public void confirmIdleFlag() {
		// 将idleFlag缺少的portname加上
		for (String portname : this.usefulPortList) {
			if (!this.idleFlag.containsKey(portname))
				this.idleFlag.put(portname, true);
			if (!this.setTimeFlag.containsKey(portname))
				this.setTimeFlag.put(portname, false);
			if (!this.useOneBoardFlag.containsKey(portname))
				this.useOneBoardFlag.put(portname, "");
		}

		// 将idleFlag多余的portname去除
		for (Map.Entry<String, Boolean> entry : this.idleFlag.entrySet()) {
			if (!this.usefulPortList.contains(entry.getKey()))
				this.idleFlag.remove(entry.getKey());
		}

		// 将setTimeFlag多余的portname去除
		for (Map.Entry<String, Boolean> entry : this.setTimeFlag.entrySet()) {
			if (!this.usefulPortList.contains(entry.getKey()))
				this.setTimeFlag.remove(entry.getKey());
		}
		
		// 将useOneBoardFlag多余的portname去除
		for (Map.Entry<String, String> entry : this.useOneBoardFlag.entrySet()) {
			if (!this.usefulPortList.contains(entry.getKey()))
				this.useOneBoardFlag.remove(entry.getKey());
		}
	}

	public String getGate_id() {
		return gate_id;
	}

	public void setGate_id(String gate_id) {
		this.gate_id = gate_id;
	}

	public List<String[]> getCheckLogs() {
		return checkLogs;
	}

	public void setCheckLogs(List<String[]> checkLogs) {
		this.checkLogs = checkLogs;
	}

	public List<String> getUsefulPortList() {
		return usefulPortList;
	}

	public void setUsefulPortList(List<String> usefulPortList) {
		this.usefulPortList = usefulPortList;
	}

	public Map<String, Boolean> getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(ConcurrentHashMap<String, Boolean> openFlag) {
		this.openFlag = openFlag;
	}

	public boolean isAllPortOpen() {
		for (String port : usefulPortList) {
			if (!openFlag.get(port))
				return false;
		}
		return true;
	}

	public Map<String, String> getUsefulPortList2() {
		return usefulPortList2;
	}

	public void setUsefulPortList2(ConcurrentHashMap<String, String> usefulPortList2) {
		this.usefulPortList2 = usefulPortList2;
	}

	public Map<String, Boolean> getIdleFlag() {
		return idleFlag;
	}

	public void setIdleFlag(ConcurrentHashMap<String, Boolean> idleFlag) {
		this.idleFlag = idleFlag;
	}

	public ConcurrentHashMap<String, Boolean> getSetTimeFlag() {
		return setTimeFlag;
	}

	public void setSetTimeFlag(ConcurrentHashMap<String, Boolean> setTimeFlag) {
		this.setTimeFlag = setTimeFlag;
	}

	public byte[] getThelastframeJ() {
		return thelastframeJ;
	}

	public void setThelastframeJ(byte[] thelastframeJ) {
		this.thelastframeJ = thelastframeJ;
	}

	public byte[] getThelastframeI() {
		return thelastframeI;
	}

	public void setThelastframeI(byte[] thelastframeI) {
		this.thelastframeI = thelastframeI;
	}

}
