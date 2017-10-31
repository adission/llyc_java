package gate.cn.com.lanlyc.core.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.lanlyc.core.util.GateBoardController;
import cn.com.lanlyc.core.util.GateReturnMessage;
import cn.com.lanlyc.core.util.UuidTransation;
import gate.cn.com.lanlyc.core.dao.GateListDao;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateUser;

@Service("GateBoardService")
@Transactional
public class GateBoardService {
	@Autowired
	private GateUserAuthService gateuserauthservice;
	@Autowired
	private GateListDao gatelistdao;
	@Autowired
	private WgService wgService;

	// 检测设置是否在线 参数:com 端口
	public boolean isOnline(String com, String gate_id) {
		GateBoardController m = GateBoardController.getInstance();
		if (!m.isAllPortOpen())
			m.openall();
		m.setGate_id(gate_id);
		GateReturnMessage rs = m.handshake(com);
		if (rs.getCode() != 200)
			return false;

		return true;

	}

	// 矫正时间 参数:com 端口
	public String resetTime(String com, String gate_id) {
		GateBoardController m = GateBoardController.getInstance();
		if (!m.isAllPortOpen())
			m.openall();
		m.setGate_id(gate_id);
		// m.openAPort(com);
		GateReturnMessage rs = m.setControllerTime(com);
		String answer = "";
		if (rs.getCode() == 200) {
			answer = "ok";
		} else {
			answer = "error";
		}
		return answer;
	}

	// 通过闸机控制板编号 人员id 获取一个用户在同一个闸机控制板上面各个通道的权限 并组装成一个八位的权限位
	public String[] getGateBoradUserAuth(String com, String user_id) {
		String[] authstr = new String[8];
		// StringBuffer authstr=new StringBuffer();
		for (int i = 7; i >= 0; i--) {
			String res = gateuserauthservice.GateBoradHasUserAuth(user_id, com, String.valueOf(i + 1));
			int xiabiao = Math.abs(i - 7);
			authstr[xiabiao] = res;
			// authstr.append(res);
		}
		return authstr;
		// return authstr.toString();
	}

	/**
	 * 写入闸机版授权人员
	 * 
	 * 
	 * @param com
	 *            连接闸机版的COM口
	 * @param connect_id
	 *            连接闸机版的通道
	 * @param gate_id
	 *            闸机id
	 * @param user_id
	 *            用户id
	 * @param id_card
	 *            用户身份证
	 * @param IC_id
	 *            用户ic卡
	 * @return
	 */

	public boolean WritePersonAuth(String com, String connect_id, String gate_id, String user_id, String id_card,
			String IC_id) {
		UuidTransation tra = new UuidTransation();
		String face_id = "";
		GateBoardController m = GateBoardController.getInstance();
		if (!m.isAllPortOpen())
			m.openall();
		// m.openAPort(com);
		m.setGate_id(gate_id);
		// 获取当前用户在该控制板的权限
		String[] auths = this.getGateBoradUserAuth(com, user_id);
		int tongdao = Integer.parseInt(connect_id);

		auths[8 - tongdao] = "1";
		StringBuffer auth_code = new StringBuffer();
		for (int i = 0; i < auths.length; i++) {
			// auth_code=auth_code+auths[i];
			auth_code.append(auths[i]);
		}
		// String auth_code=auths.toString();
		String s = auth_code.toString();
		// String s="11111111";

		if (null == id_card)
			id_card = "";
		if (null == IC_id)
			IC_id = "";
		// 往闸机控制板添加一个单独的人员信息
		GateReturnMessage remsg2 = m.addOneAuthInfo(com, id_card, IC_id, face_id, s);
		boolean res = false;
		if (remsg2.getCode() == 200) {
			res = true;
		}
		// System.out.println(remsg2.getMessage());
		// m.closeAPort(com);
		return res;
	}

	// 获取一个人员在闸机版权限
	public Object getUserAuth(String com, String gate_id, String user_id, String id_card, String IC_id) {

		GateBoardController m = GateBoardController.getInstance();// 默认打开了所有的COM口
		if (!m.isAllPortOpen())
			m.openall();
		// m.openAPort(com);//测试时使用，调用时不用执行。连接指定COM号的控制板（同时启动其实时打卡监听线程）
		m.setGate_id(gate_id);
		UuidTransation tra = new UuidTransation();
		String face_id = tra.UuidTransIntid(user_id);

		GateReturnMessage rm = m.queryOneAuthority(com, id_card, IC_id, face_id);// 查询单个是否授权
		// m.closeAPort(com);
		return rm.getData();

	}

	// 修改闸机版权限
	public boolean updatePersonAuth(String com, String connect_id, String gate_id, String user_id, String id_card,
			String IC_id) {
		boolean res = false;
		// 首先根据user_id 唯一性 删除掉授权信息
		UuidTransation tra = new UuidTransation();
		String face_id = tra.UuidTransIntid(user_id);
		this.deltePersonAuth(com, gate_id, user_id, "", "");
		// 然后新增新的授权信息
		res = this.WritePersonAuth(com, connect_id, gate_id, user_id, id_card, IC_id);
		return res;
	}

	// 删除一个人在闸机版上面的权限
	public boolean deltePersonAuth(String com, String gate_id, String user_id, String id_card, String IC_id) {
		GateBoardController m = GateBoardController.getInstance();// 默认打开了所有的COM口
		if (!m.isAllPortOpen())
			m.openall();
		// m.openAPort(com);//测试时使用，调用时不用执行。连接指定COM号的控制板（同时启动其实时打卡监听线程）
		m.setGate_id(gate_id);

		UuidTransation tra = new UuidTransation();
		String face_id = tra.UuidTransIntid(user_id);

		if (null == id_card)
			id_card = "";
		if (null == IC_id)
			IC_id = "";

		GateReturnMessage remsg = m.delOneAuthInfo(com, id_card, IC_id, "");// 删除单个人员授权信息
		boolean res = false;
		if (remsg.getCode() == 200 || remsg.getCode() == 203) {
			res = true;
		}
		// m.closeAPort(com);
		return res;
	}

	// 删除一个人员在某个闸机上的权限
	public boolean delUserInGate(String gate_id, String user_id, String id_card, String IC_id) {
		GateBoardController m = GateBoardController.getInstance();// 默认打开了所有的COM口
		if (!m.isAllPortOpen())
			m.openall();
		GateList gateList = gatelistdao.get(gate_id);
		// 获取当前用户在该控制板的权限
		String[] auths = this.getGateBoradUserAuth(gateList.getConnect_port(), user_id);
		auths[8 - Integer.parseInt(gateList.getConnect_id())] = "0";// 将该闸机对应的权限位置空
		StringBuffer authority = new StringBuffer();
		for (String string : auths) {
			authority.append(string);
		}

		m.setGate_id(gateList.getGate_no());
		// 重新下放该人员的授权（会删除控制板中原来的数据）
		GateReturnMessage remsg = m.addOneAuthInfo(gateList.getConnect_port(), id_card, IC_id, "",
				authority.toString());
		if (200 == remsg.getCode())
			return true;
		return false;

	}

	// 获取闸机版所有权限
	public GateReturnMessage getAuthperson(String com, String gate_id) {
		GateBoardController m = GateBoardController.getInstance();
		if (!m.isAllPortOpen())
			m.openall();

		m.setGate_id(gate_id);
		GateReturnMessage remsg = m.getAllAuthInfo(com);
		return remsg;
	}

	/**
	 * 实时获取指定com 刷卡记录 并处理
	 * 
	 * @param com
	 * @param gate_id
	 * @return
	 */
	public boolean GetAndoperatingcheck(String com, String gate_id) {
		GateBoardController m = GateBoardController.getInstance();// 默认打开了所有的COM口
		m.openAPort(com);// 测试时使用，调用时不用执行。连接指定COM号的控制板（同时启动其实时打卡监听线程）
		m.setGate_id(gate_id);
		m.reciOneCardInfo(com); // 接收打卡信息

		m.closeAPort(com);
		return false;

	}

	/**
	 * 控制设备状态
	 * 
	 * @param port
	 * @param Connect_id
	 * @param gate_id
	 * @param state
	 * @return
	 */
	public boolean setAllState(String port, String Connect_id, String gate_id, int state) {
		GateBoardController m = GateBoardController.getInstance();
		if (!m.isAllPortOpen())
			m.openall();

		m.setGate_id(gate_id);
		int connect_id = Integer.parseInt(Connect_id);
		GateReturnMessage remsg = m.setGateState(port, connect_id, state);// 设置控制板的通道

		if (200 == (int) remsg.getCode())
			return true;
		return false;
	}

	/**
	 * 控制设备常开
	 * 
	 * @param port
	 * @param Connect_id
	 * @param gate_id
	 * @return
	 */
	public boolean allopen(String port, String Connect_id, String gate_id) {
		return this.setAllState(port, Connect_id, gate_id, 0);
	}

	/**
	 * 控制设备正常
	 * 
	 * @param port
	 * @param Connect_id
	 * @param gate_id
	 * @return
	 */
	public boolean allnormal(String port, String Connect_id, String gate_id) {
		return this.setAllState(port, Connect_id, gate_id, 1);
	}

	/**
	 * 控制设备常闭
	 * 
	 * @param port
	 * @param Connect_id
	 * @param gate_id
	 * @return
	 */
	public boolean allclose(String port, String Connect_id, String gate_id) {
		return this.setAllState(port, Connect_id, gate_id, 2);
	}

	/**
	 * 清除所有授权信息
	 * 
	 * @param port
	 * @return
	 */
	public boolean cleanAllAuthority(String port, String gate_id) {
		GateBoardController m = GateBoardController.getInstance();
		if (!m.isAllPortOpen())
			m.openall();
		m.setGate_id(gate_id);
		GateReturnMessage remsg = m.resetAllAuthority(port);
		boolean is_success = false;
		if (200 == (int) remsg.getCode()) {
			// String datas=remsg.getData().toString();
			// if(StringUtils.isNotEmpty(datas))
			// if(200==Integer.parseInt(datas))
			// is_success=true;
			is_success = true;
		} else {
			is_success = false;
		}

		return is_success;
	}

	/**
	 * 设置闸机状态（常开、正常、常闭）
	 * 
	 * @param port
	 * @param gateNo
	 * @param state
	 * @param gate_id
	 * @return
	 */
	public boolean setGateState(String port, int gateNo, int state, String gate_id) {
		GateBoardController m = GateBoardController.getInstance();
		if (!m.isAllPortOpen())
			m.openall();
		m.setGate_id(gate_id);
		GateReturnMessage remsg = m.setGateState(port, gateNo, state);
		if (200 == (int) remsg.getCode())
			return true;

		return false;
	}

	/**
	 * 得到未上传的刷卡记录
	 * 
	 * @param portname
	 * @return
	 */
	public void getLostCheckLog(String portname) {
		GateBoardController m = GateBoardController.getInstance();
		if (!m.isAllPortOpen())
			m.openall();
		GateReturnMessage returnMessage = m.handshake(portname);
		if (returnMessage.getCode() == 200) {
			m.getLostCheckLog(portname);
		}
	}

	/**
	 * 就相应的微耕设备进行相应的操作—常开
	 * 
	 * @param portname
	 * @return
	 */
	public boolean openWeigeng(GateList gatelist) {
		// TODO Auto-generated method stub
		// 调用微耕的常开方法
		int open_out_come = wgService.open_door_sate(gatelist.getConnect_ip(), Long.parseLong(gatelist.getSN()),
				Integer.parseInt(gatelist.getConnect_id()));
		if (open_out_come == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 就相应的微耕设备进行相应的操作—常闭
	 * 
	 * @param portname
	 * @return
	 */
	public boolean closeWeigeng(GateList gatelist) {
		// TODO Auto-generated method stub
		// 调用微耕的常闭方法
		int open_out_come = wgService.close_door_sate(gatelist.getConnect_ip(), Long.parseLong(gatelist.getSN()),
				Integer.parseInt(gatelist.getConnect_id()));
		if (open_out_come == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 就相应的微耕设备进行相应的操作—恢复正常
	 * 
	 * @param portname
	 * @return
	 */
	public boolean normalWeigeng(GateList gatelist) {
		// TODO Auto-generated method stub
		// 调用微耕的恢复正常的方法
		int open_out_come = wgService.common_door_sate(gatelist.getConnect_ip(), Long.parseLong(gatelist.getSN()),
				Integer.parseInt(gatelist.getConnect_id()));
		if (open_out_come == 1) {
			return true;
		} else {
			return false;
		}
	}

}
