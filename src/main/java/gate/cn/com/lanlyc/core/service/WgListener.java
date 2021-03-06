/* ----------------------------------------------------------
 * 文件名称：FXMLDocumentController.java
 * 
 * 作者：杨威
 * 
 * 微信：yangwei728118
 * 
 * 
 * 开发环境：
 *      NetBeans 8.1
 *      JDK 8u92
 *      
 * 版本历史：
 *      V1.0    2014年09月04日
 *              接收卡点数据
------------------------------------------------------------ */

package gate.cn.com.lanlyc.core.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.annotation.PostConstruct;

import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Com.FirstSolver.Splash.FaceId_Item;
import cn.com.lanlyc.handler.WatchingShortHandler;
import cn.com.lanlyc.handler.WgUdpCommShort;

/**
 *
 * @author yangwei
 */
public class WgListener {

	long controllerSN = 253116377;
	String controllerIP = "192.168.199.228";
	String watchServerIP = "192.168.199.187";
	int watchServerPort = 61005;

	private static List<Map<String, String>> wg_check_list = new ArrayList<Map<String, String>>();

	private static List<Map<String, String>> wg_user_list = new ArrayList<Map<String, String>>();

	private WgListener() {

	}

	private static WgListener wgListener = null;

	public static WgListener getInstance() {
		if (wgListener == null) {
			wgListener = new WgListener();
		}
		return wgListener;
	}

	/*
	 * 这个是存所有的人脸的考勤数据
	 * 
	 */
	public static List<Map<String, String>> getwgcheck_list() {
		return wg_check_list;
	}

	/*
	 * 这个是存所有用户的相关信息
	 * 
	 */
	public static List<Map<String, String>> getwguser_list() {
		return wg_user_list;
	}

	// 监听时的初始化方法
	public void init_listen() throws IOException, Exception {
		WgListener ft = new WgListener();

		String ip = InetAddress.getLocalHost().toString();
		int i = ip.indexOf("/");
		ft.WatchingServerRuning(ip.substring(i + 1), 61005);
	}

	private int WatchingServerRuning(String watchServerIP, int watchServerPort) // 进入服务器监控状态
	{
		Queue<byte[]> queue = new LinkedList<byte[]>();

		// 创建UDP数据包NIO
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		// NIO设置底层IOHandler
		acceptor.setHandler(new WatchingShortHandler(queue));

		// 设置是否重用地址？ 也就是每个发过来的udp信息都是一个地址？
		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReuseAddress(true);

		// 绑定端口地址
		try {
			acceptor.bind(new InetSocketAddress(watchServerIP, watchServerPort));
		} catch (IOException e) {
			log("绑定接收服务器失败....");
			e.printStackTrace();
			return 0;
		}
		log("进入接收服务器监控状态....[如果在win7下使用 一定要注意防火墙设置]");

		long recordIndex = 0;
		while (true) {
			try {
				Thread.sleep(10);// 必须加时间间隔，用于区分接收数据和处理数据的先后顺序
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!queue.isEmpty()) {
				byte[] recvBuff;
				synchronized (queue) {
					recvBuff = queue.poll();
				}
				if (recvBuff[1] == 0x20) {
					long sn = WgUdpCommShort.getLongByByte(recvBuff, 4, 4);
					long recordIndexGet = WgUdpCommShort.getLongByByte(recvBuff, 8, 4);
					log(String.format("接收到来自控制器SN = %d 的数据包..", sn));

					if (recordIndex < recordIndexGet) {
						recordIndex = recordIndexGet;
						displayRecordInformation(recvBuff, sn);
					}
				}
			}

		}
		// 如果 不是while(true), 则打开下面的注释...
		// acceptor.unbind();
		// acceptor.dispose();
		// return 0;
	}

	// // 以下基本是相应的日志
	public static void log(String info) // 日志信息——相应的——
	{
		System.out.println(info);
	}

	public static byte GetHex(int val) // 获取Hex值, 主要用于日期时间格式
	{
		return (byte) ((val % 10) + (((val - (val % 10)) / 10) % 10) * 16);
	}

	/// <summary>
	/// 显示记录信息
	/// </summary>
	/// <param name="recvBuff"></param>
	public static void displayRecordInformation(byte[] recvBuff, long sn) {

		// 8-11 最后一条记录的索引号
		// (=0表示没有记录) 4 0x00000000
		long recordIndex = WgUdpCommShort.getLongByByte(recvBuff, 8, 4);

		// 12 记录类型
		// 0=无记录
		// 1=刷卡记录
		// 2=门磁,按钮, 设备启动, 远程开门记录
		// 3=报警记录 1
		int recordType = WgUdpCommShort.getIntByByte(recvBuff[12]);

		// 13 有效性(0 表示不通过, 1表示通过) 1
		int recordValid = WgUdpCommShort.getIntByByte(recvBuff[13]);

		// 14 门号(1,2,3,4) 1
		int recordDoorNO = WgUdpCommShort.getIntByByte(recvBuff[14]);

		// 15 进门/出门(1表示进门, 2表示出门) 1 0x01
		int recordInOrOut = WgUdpCommShort.getIntByByte(recvBuff[15]);

		// 16-19 卡号(类型是刷卡记录时)
		// 或编号(其他类型记录) 4
		long recordCardNO = WgUdpCommShort.getLongByByte(recvBuff, 16, 4);

		// 20-26 刷卡时间:
		// 年月日时分秒 (采用BCD码)见设置时间部分的说明
		String recordTime = String.format("%02X%02X-%02X-%02X %02X:%02X:%02X",
				WgUdpCommShort.getIntByByte(recvBuff[20]), WgUdpCommShort.getIntByByte(recvBuff[21]),
				WgUdpCommShort.getIntByByte(recvBuff[22]), WgUdpCommShort.getIntByByte(recvBuff[23]),
				WgUdpCommShort.getIntByByte(recvBuff[24]), WgUdpCommShort.getIntByByte(recvBuff[25]),
				WgUdpCommShort.getIntByByte(recvBuff[26]));

		// 2012.12.11 10:49:59 7
		// 27 记录原因代码(可以查 "刷卡记录说明.xls"文件的ReasonNO)
		// 处理复杂信息才用 1
		int reason = WgUdpCommShort.getIntByByte(recvBuff[27]);

		// 0=无记录
		// 1=刷卡记录
		// 2=门磁,按钮, 设备启动, 远程开门记录
		// 3=报警记录 1
		// 0xFF=表示指定索引位的记录已被覆盖掉了. 请使用索引0, 取回最早一条记录的索引值
		if (recordType == 0) {
			log(String.format("索引位=%u  无记录", recordIndex));
		} else if (recordType == 0xff) {
			log(" 指定索引位的记录已被覆盖掉了,请使用索引0, 取回最早一条记录的索引值");
		} else if (recordType == 1) // 2015-06-10 08:49:31 显示记录类型为卡号的数据
		{
			// 卡号
			log(String.format("索引位=%d  ", recordIndex));
			log(String.format("  卡号 = %d", recordCardNO));
			log(String.format("  门号 = %d", recordDoorNO));
			log(String.format("  进出 = %s", recordInOrOut == 1 ? "进门" : "出门"));
			log(String.format("  有效 = %s", recordValid == 1 ? "通过" : "禁止"));
			log(String.format("  时间 = %s", recordTime));
			log(String.format("  描述 = %s", getReasonDetailChinese(reason)));

			// 将上传的数据进行存储
			Map<String, String> log_map = new HashMap<String, String>();
			log_map.put("sn", String.valueOf(sn));// 设备控制器编号
			log_map.put("index", String.valueOf(recordIndex));// 相关的记录的索引
			log_map.put("card", String.valueOf(recordCardNO));// 相关的卡号
			log_map.put("cross", String.valueOf(recordInOrOut));// 进出属性（1表示进门, 2表示出门）
			log_map.put("effective", String.valueOf(recordValid));//是否有效
			log_map.put("punch_time", recordTime);//打卡时间
			wg_check_list.add(log_map);
			// 这里就是用相应的数据存入，相应的列表中
			// 就在这里进行相应的数据处理
			// 1.根据相应数据表中是否有相应的设备
			// 2.如果有相应的设备 ，获取相应的索引的位置
			// 3.索引首先是0，
			// 比较索引（如果本条的记录《数据表中的最后一条索引）——不做处理
			// 如果=不处理
			// 如果=索引+1-直接处理
			// 如果>索引+1（将区间的所有索引，进行查询，并做处理）
			// 处理完后，更新索引

		} else if (recordType == 2) {
			// 其他处理
			// 门磁,按钮, 设备启动, 远程开门记录
			log(String.format("索引位=%d  非刷卡记录", recordIndex));
			log(String.format("  编号 = %d", recordCardNO));
			log(String.format("  门号 = %d", recordDoorNO));
			log(String.format("  时间 = %s", recordTime));
			log(String.format("  描述 = %s", getReasonDetailChinese(reason)));
		} else if (recordType == 3) {
			// 其他处理
			// 报警记录
			log(String.format("索引位=%d  报警记录", recordIndex));
			log(String.format("  编号 = %d", recordCardNO));
			log(String.format("  门号 = %d", recordDoorNO));
			log(String.format("  时间 = %s", recordTime));
			log(String.format("  描述 = %s", getReasonDetailChinese(reason)));
		}
	}

	public static String RecordDetails[] = {
			// 记录原因 (类型中 SwipePass 表示通过; SwipeNOPass表示禁止通过; ValidEvent 有效事件(如按钮 门磁 超级密码开门);
			// Warn 报警事件)
			// 代码 类型 英文描述 中文描述
			"1", "SwipePass", "Swipe", "刷卡开门", "2", "SwipePass", "Swipe Close", "刷卡关", "3", "SwipePass", "Swipe Open",
			"刷卡开", "4", "SwipePass", "Swipe Limited Times", "刷卡开门(带限次)", "5", "SwipeNOPass",
			"Denied Access: PC Control", "刷卡禁止通过: 电脑控制", "6", "SwipeNOPass", "Denied Access: No PRIVILEGE",
			"刷卡禁止通过: 没有权限", "7", "SwipeNOPass", "Denied Access: Wrong PASSWORD", "刷卡禁止通过: 密码不对", "8", "SwipeNOPass",
			"Denied Access: AntiBack", "刷卡禁止通过: 反潜回", "9", "SwipeNOPass", "Denied Access: More Cards", "刷卡禁止通过: 多卡",
			"10", "SwipeNOPass", "Denied Access: First Card Open", "刷卡禁止通过: 首卡", "11", "SwipeNOPass",
			"Denied Access: Door Set NC", "刷卡禁止通过: 门为常闭", "12", "SwipeNOPass", "Denied Access: InterLock", "刷卡禁止通过: 互锁",
			"13", "SwipeNOPass", "Denied Access: Limited Times", "刷卡禁止通过: 受刷卡次数限制", "14", "SwipeNOPass",
			"Denied Access: Limited Person Indoor", "刷卡禁止通过: 门内人数限制", "15", "SwipeNOPass",
			"Denied Access: Invalid Timezone", "刷卡禁止通过: 卡过期或不在有效时段", "16", "SwipeNOPass", "Denied Access: In Order",
			"刷卡禁止通过: 按顺序进出限制", "17", "SwipeNOPass", "Denied Access: SWIPE GAP LIMIT", "刷卡禁止通过: 刷卡间隔约束", "18",
			"SwipeNOPass", "Denied Access", "刷卡禁止通过: 原因不明", "19", "SwipeNOPass", "Denied Access: Limited Times",
			"刷卡禁止通过: 刷卡次数限制", "20", "ValidEvent", "Push Button", "按钮开门", "21", "ValidEvent", "Push Button Open", "按钮开",
			"22", "ValidEvent", "Push Button Close", "按钮关", "23", "ValidEvent", "Door Open", "门打开[门磁信号]", "24",
			"ValidEvent", "Door Closed", "门关闭[门磁信号]", "25", "ValidEvent", "Super Password Open Door", "超级密码开门", "26",
			"ValidEvent", "Super Password Open", "超级密码开", "27", "ValidEvent", "Super Password Close", "超级密码关", "28",
			"Warn", "Controller Power On", "控制器上电", "29", "Warn", "Controller Reset", "控制器复位", "30", "Warn",
			"Push Button Invalid: Disable", "按钮不开门: 按钮禁用", "31", "Warn", "Push Button Invalid: Forced Lock",
			"按钮不开门: 强制关门", "32", "Warn", "Push Button Invalid: Not On Line", "按钮不开门: 门不在线", "33", "Warn",
			"Push Button Invalid: InterLock", "按钮不开门: 互锁", "34", "Warn", "Threat", "胁迫报警", "35", "Warn", "Threat Open",
			"胁迫报警开", "36", "Warn", "Threat Close", "胁迫报警关", "37", "Warn", "Open too long", "门长时间未关报警[合法开门后]", "38",
			"Warn", "Forced Open", "强行闯入报警", "39", "Warn", "Fire", "火警", "40", "Warn", "Forced Close", "强制关门", "41",
			"Warn", "Guard Against Theft", "防盗报警", "42", "Warn", "7*24Hour Zone", "烟雾煤气温度报警", "43", "Warn",
			"Emergency Call", "紧急呼救报警", "44", "RemoteOpen", "Remote Open Door", "操作员远程开门", "45", "RemoteOpen",
			"Remote Open Door By USB Reader", "发卡器确定发出的远程开门" };

	public static String getReasonDetailChinese(int Reason) // 中文
	{
		if (Reason > 45) {
			return "";
		}
		if (Reason <= 0) {
			return "";
		}
		return RecordDetails[(Reason - 1) * 4 + 3]; // 中文信息
	}

	public static String getReasonDetailEnglish(int Reason) // 英文描述
	{
		if (Reason > 45) {
			return "";
		}
		if (Reason <= 0) {
			return "";
		}
		return RecordDetails[(Reason - 1) * 4 + 2]; // 英文信息
	}
}
