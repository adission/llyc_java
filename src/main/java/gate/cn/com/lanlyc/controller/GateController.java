package gate.cn.com.lanlyc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.ExcelUtiltest;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.GateBoardController;
import cn.com.lanlyc.core.util.GateReturnMessage;
import cn.com.lanlyc.core.util.KaoqinUtuil;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.UserCards;
import net.sf.json.JSONArray;

@Controller
@RequestMapping("/GateList")
public class GateController extends BaseController {

	/**
	 * 返回所有添加闸机时的下拉数据
	 * 
	 * @return
	 */
	@RequestMapping("/getAllAddInformation")
	@ResponseBody
	public Response getAllAddInformation() {
		GateBoardController m = GateBoardController.getInstance();
		List<String> allcanport = m.getPortList();
		// m.closeall();
		List<Map> alltype = new ArrayList<Map>();
		Map<String, String> typemap1 = new HashMap<String, String>();
		typemap1.put("type_value", "hanwang");
		typemap1.put("type_name", "汉王人脸识别考勤机");

		// typemap.put("hanwang", "汉王人脸识别考勤机");
		// typemap.put("lanlyc", "蓝领英才闸机");
		alltype.add(typemap1);
		Map<String, String> typemap2 = new HashMap<String, String>();
		typemap2.put("type_value", "lanlyc");
		typemap2.put("type_name", "蓝领英才闸机");
		// typemap.put("hanwang", "汉王人脸识别考勤机");
		// typemap.put("lanlyc", "蓝领英才闸机");
		alltype.add(typemap2);
		Map<String, String> typemap3 = new HashMap<String, String>();
		typemap3.put("type_value", "weigeng");
		typemap3.put("type_name", "微耕闸机");
		alltype.add(typemap3);

		Map datas = new HashMap();
		datas.put("allcanport", allcanport);
		datas.put("alltype", alltype);
		Response response = Response.OK();
		response.setData(datas);
		return response;

	}

	/**
	 * 添加闸机 请求URT
	 * add?paramJson={"user_name":"test","SN":"123","gate_no":"1","location":"侧面","connect_port":"9300","connect_id":"1","type":"7","connect_ip":"192.168.1.138","gate_name":"汉王考勤闸机1","is_kaoqin":"0","is_use":"0","cross_flag":"0","group_id":"dfacb21fe4b34604b72381163d88699a","common":"测试","read_bytes":"8","is_master":"0"}
	 * 
	 * @param paramJson
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Response add(String paramJson) {
		JSONObject object = JSONObject.parseObject(paramJson);

		if (!object.containsKey("gate_no")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		if (!object.containsKey("connect_ip")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		if (!object.containsKey("connect_port")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		if (!object.containsKey("type")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}

		String gate_no = object.getString("gate_no");
		String connect_ip = object.getString("connect_ip");
		String connect_port = object.getString("connect_port");
		String type = object.getString("type");
		String connect_id = object.getString("connect_id");

		// 判断闸机编号是否重复
		GateList gate = this.getService().getGatelistservice().getGatelistdao().getGateListInfoBygateno(gate_no);
		if (gate != null) {
			Response response = Response.ERROR(-1, "系统中已存在相同的闸机编号");
			return response;
		}
		// 判断同一COM口的通道号是否重复
		GateList gateList = this.getService().getGatelistservice().getGatelistdao()
				.getGateListInfoBygateconnectid(connect_port, connect_id);
		if (gateList != null) {
			Response response = Response.ERROR(-1, "该通道号已占用");
			return response;
		}

		// 对微耕设备的添加
		if (type.equals("weigeng")) {
			Boolean outcome = this.getService().getGatelistservice().addweigeng(object, paramJson);
			if (outcome) {
				Response response = Response.OK();
				this.addUserOperationlog("新增设备：" + gate_no);
				return response;
			} else {
				Response response = Response.ERROR(-1, "添加闸机失败");
				return response;
			}
		}
		// 判断闸机编号是否重复
		if (type.equals("hanwang")) {
			// 判断汉王的设置是否有ip 和端口的设备已经添加过了
			GateList gate2 = this.getService().getGatelistservice().getGatelistdao().getGateListInfoBygateip(connect_ip,
					connect_port);
			if (gate2 != null) {
				Response response = Response.ERROR(-1, "系统中已存在相同的闸机编号");
				return response;
			}

			// 判断设备是否在线
			if (this.getService().getFaceservice().existFace(connect_ip, connect_port, "") != "ok") {
				Response response = Response.ERROR(-1, "添加闸机失败");
				return response;
			}
		} else {
			// 如果是闸机版设置 则判断相同的板子和端口 已经添加过
			GateList gate2 = this.getService().getGatelistservice().getGatelistdao()
					.getGateListInfoBygateconnectid(connect_port, connect_id);
			if (gate2 != null) {
				Response response = Response.ERROR(-1, "系统中已存在相同的闸机编号");
				return response;
			}

			// 判断控制板是否打开
			GateBoardController m = GateBoardController.getInstance();
			List<String> comList = this.getService().getGatelistservice().getGatelistdao().getAllDiffLlycCOM();// 所有正在使用的不重复的COM号
			if (!comList.contains(connect_port)) {// 打开控制板
				m.setGate_id(gate_no);
				m.openAPort(connect_port);
				// 判断控制板闸机是否在线
				if (this.getService().getGateboradservie().isOnline(connect_port, gate_no) == false) {
					// 关闭控制板
					m.closeAPort(connect_port);
					Response response = Response.ERROR(-1, "添加闸机失败");
					return response;
				}
			} else {
				// 判断控制板闸机是否在线
				if (!this.getService().getGateboradservie().isOnline(connect_port, gate_no)) {
					Response response = Response.ERROR(-1, "添加闸机失败");
					return response;
				}
			}

		}

		GateList gatelist = JSON.parseObject(paramJson, new TypeReference<GateList>() {
		});
		String id = DataUtils.getUUID();
		gatelist.setId(id);
		/*
		 * if(!gatelist.getCross_flag().equals("0") &&
		 * !gatelist.getCross_flag().equals("1")){ Response response =
		 * Response.ERROR(-1, "添加闸机失败"); return response; }
		 */
		if (type.equals("lanlyc")) {// 设置闸机的进出状态
			if (!this.getService().getGateboradservie().setGateState(connect_port,
					Integer.parseInt(gatelist.getConnect_id()), 1, gatelist.getGate_no())) {
				Response response = Response.ERROR(-1, "设置闸机的进出状态失败");
				return response;
			}
		}
		if (0 == this.getService().getGatelistservice().getGatelistdao().save(gatelist)) {
			Response response = Response.ERROR(-1, "数据保存失败");
			return response;
		}
		Response response = Response.OK();
		this.addUserOperationlog("新增设备：" + gate_no);
		return response;
	}

	/**
	 * 获取所有闸机列表
	 * 
	 * @param paramJson
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Response list(String paramJson, int length, int start, int draw) {
		JSONObject object = JSONObject.parseObject(paramJson);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (object.containsKey("gate_name")) {
			String gate_name = object.getString("gate_name");
			if (StringUtils.isNotEmpty(gate_name)) {
				paramMap.put("gate_name", gate_name);
			}

		}
		if (object.containsKey("gate_no")) {
			String gate_no = object.getString("gate_no");
			if (StringUtils.isNotEmpty(gate_no)) {
				paramMap.put("gate_no", gate_no);
			}

		}
		String orderColumn = "";
		String orderDir = "";
		JSONArray filterColumn = null;
		JSONArray filter = null;
		if (object.containsKey("orderColumn")) {
			orderColumn = object.getString("orderColumn");
		}
		if (object.containsKey("orderDir")) {
			orderDir = object.getString("orderDir");
		}

		int currentPage = start / length + 1;
		Page<GateList> page = new Page<GateList>(currentPage);
		page.setPageSize(length);
		Page<GateList> result = this.getService().getGatelistservice().getGatelistdao().getAllGate(page, paramMap,
				orderColumn, orderDir);
		Response response = Response.OK();
		response.put("recordsTotal", result.getTotalRow());
		response.put("recordsFiltered", result.getTotalRow());
		response.setData(result.getResultRows());
		response.put("recordsFiltered", result.getTotalRow());
		response.put("draw", draw);
		response.put("total", result.getTotalRow());
		return response;

	}

	/**
	 * 获取所有闸机列表
	 * 
	 * @param paramJson
	 * @return
	 */
	@RequestMapping("/listexcel")
	@ResponseBody
	public void listExcel(HttpServletRequest request, HttpServletResponse response, String paramJson) {
		JSONObject object = JSONObject.parseObject(paramJson);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (object.containsKey("gate_name")) {
			String gate_name = object.getString("gate_name");
			if (StringUtils.isNotEmpty(gate_name)) {
				paramMap.put("gate_name", gate_name);
			}

		}
		if (object.containsKey("gate_no")) {
			String gate_no = object.getString("gate_no");
			if (StringUtils.isNotEmpty(gate_no)) {
				paramMap.put("gate_no", gate_no);
			}

		}
		String orderColumn = "";
		String orderDir = "";
		JSONArray filterColumn = null;
		JSONArray filter = null;
		if (object.containsKey("orderColumn")) {
			orderColumn = object.getString("orderColumn");
		}
		if (object.containsKey("orderDir")) {
			orderDir = object.getString("orderDir");
		}
		List<GateList> page = this.getService().getGatelistservice().getGatelistdao().getAllGateExcel(paramMap,
				orderColumn, orderDir);

		String[] headers = { "闸机型号", "名称", "方向", "控制器编号", "闸机编号", "安装位置", "闸机分区", "闸机状态", "考勤机", "设备通讯端口号",
				"设备ip COM端口号", "设备板通道编号", "最后一次上报时间", "最后一次查询时间" };

		List<List> dataset = new ArrayList<List>();
		for (GateList user_info : page) {
			dataset.add(new ArrayList() {
				{
					add(user_info.getType() != null ? user_info.getType() : "");
					add(user_info.getGate_name() != null ? user_info.getGate_name() : "");
					add(user_info.getCross_flag() != null ? user_info.getCross_flag() : "");
					add(user_info.getSN() != null ? user_info.getSN() : "");
					add(user_info.getGate_no() != null ? user_info.getGate_no() : "");
					add(user_info.getLocation() != null ? user_info.getLocation() : "");
					add(user_info.getGroup_id() != null ? user_info.getGroup_id() : "");
					add(user_info.getIs_use() != null ? user_info.getIs_use() : "");
					add(user_info.getIs_kaoqin() != null ? user_info.getIs_kaoqin() : "");

					add(user_info.getConnect_port() != null ? user_info.getConnect_port() : "");
					add(user_info.getConnect_ip() != null ? user_info.getConnect_ip() : "");
					add(user_info.getConnect_id() != null ? user_info.getConnect_id() : "");
					add(user_info.getLast_uploadrecord_time() != null ? user_info.getLast_uploadrecord_time() : "");
					add(user_info.getLast_queryrecord_time() != null ? user_info.getLast_queryrecord_time() : "");

				}
			});
		}
		ExcelUtiltest.Test(request, response, headers, dataset);

	}

	/**
	 * 获取闸机详情
	 * 
	 * 
	 * @param paramJson
	 * @return
	 */
	@RequestMapping("/getinfo")
	@ResponseBody
	public Response getinfo(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		GateList gatelist = this.getService().getGatelistservice().getGatelistdao().getGateListInfo(id);
		Response response = Response.OK();
		response.put("data", gatelist);
		return response;
	}

	/**
	 * 修改闸机详情
	 * 
	 * @param paramJson
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Response update(String paramJson) {
		if (paramJson == null || "".equals(paramJson)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		JSONObject object = JSONObject.parseObject(paramJson);

		GateList gatelist = JSON.parseObject(paramJson, new TypeReference<GateList>() {
		});

		GateList gatelist_old = this.getService().getGatelistservice().getGatelistdao().get(gatelist.getId());

		int num = this.getService().getGatelistservice().getGatelistdao().update(gatelist, false);

		if (num == 0) {
			Response response = Response.PARAM_ERROR();
			response.put("code", 300);
			response.put("message", "数据库操作失败");
			return response;
		}
		Response response = Response.OK();

		// 判断COM号有没有被修改,同时更改GateBoardController中的usefulPortList和控制板的打开状态
		GateBoardController m = GateBoardController.getInstance();
		if (!m.getUsefulPortList().contains(gatelist.getId()))
			// 打开新COM口
			m.setGate_id(gatelist.getGate_no());
		m.openAPort(gatelist.getConnect_port());
		List<String> comList = this.getService().getGatelistservice().getGatelistdao().getAllDiffLlycCOM();// 所有正在使用的不重复的COM号
		// 如果该闸机之前的COM号没有被其他闸机使用，则关闭连接
		if (!comList.contains(gatelist_old.getConnect_port())
				&& m.getUsefulPortList().contains(gatelist_old.getConnect_port()))
			m.closeAPort(gatelist_old.getConnect_port());

		this.addUserOperationlog("修改设备：" + gatelist.getGate_no());
		// response.put("data", dt);
		return response;
	}

	/**
	 * 删除闸机
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Response delete(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String[] ids = id.split(",");
		int dt = 0;
		for (String i : ids) {
			GateList gatelist = this.getService().getGatelistservice().getGatelistdao().get(i);
			// 删除设置则删除该设备中所有保存的权限
			if (gatelist.getType().equals("hanwang")) {
				KaoqinUtuil kqutil = new KaoqinUtuil();
				String DeviceIP = gatelist.getConnect_ip();
				String DevicePort = gatelist.getConnect_port();
				String SecretKey = gatelist.getPassword();
				String TestCommand = "DeleteAllEmployee()";
				String answer = kqutil.connect(DeviceIP, DevicePort, SecretKey, TestCommand);
				String TestCommand2 = "DeleteAllRecord()";
				String answer2 = kqutil.connect(DeviceIP, DevicePort, SecretKey, TestCommand2);

			} else if (gatelist.getType().equals("lanlyc")) {
				// 判断控制板是否只对应本闸机，如果是，则直接清楚控制板的授权信息并且关闭该控制板的连接；如果不是，则改变控制板人员信息的授权
				List<String> gateIdList = this.getService().getGatelistservice().getGatelistdao()
						.getAllIdByCOM(gatelist.getConnect_port());

				if (1 == gateIdList.size() && gateIdList.contains(gatelist.getId())) {// 直接清除控制板的授权信息并且关闭该控制板的连接
					boolean flag = this.getService().getGateboradservie().cleanAllAuthority(gatelist.getConnect_port(),
							gatelist.getGate_no());
					if (!flag) {
						Response response = Response.ERROR(-1, "清除闸机所有授权信息出错");
						return response;
					}

					GateBoardController m = GateBoardController.getInstance();
					m.setGate_id(gatelist.getGate_no());
					GateReturnMessage remsg = m.closeAPort(gatelist.getConnect_port());
					if (remsg.getCode() != 200) {
						Response response = Response.ERROR(-1, "关闭控制板连接出错");
						return response;
					}
				} else if (gateIdList.size() > 1 && gateIdList.contains(gatelist.getId())) {// 改变控制板人员信息的授权
					// 从数据库中获取该有该闸机授权的人员，并重新设置该人员在对应控制板上的权限
					List<GateUserAuth> gateUserAuths = this.getService().getGateuserauthservie().getGateuserauthdao()
							.getGateUserAuth(gatelist.getId());
					for (GateUserAuth gateUserAuth : gateUserAuths) {
						String user_id = gateUserAuth.getUser_id();

						String[] auths = this.getService().getGateboradservie()
								.getGateBoradUserAuth(gatelist.getConnect_port(), user_id);
						auths[8 - Integer.parseInt(gatelist.getConnect_id())] = "0";// 取消该人员在此闸机上的授权
						String authority = null;
						StringBuffer authority2 = new StringBuffer();
						for (String string : auths) {
							authority2.append(string);
						}
						authority = authority2.toString();

						List<UserCards> ucardsList = this.getService().getUserCardsService().getUserCardsDao()
								.getUserCardsByUserid(user_id);
						String id_card = "";
						String IC_card = "";
						String face_id = "";
						if (ucardsList != null) {
							for (UserCards userCards : ucardsList) {
								if ("0" == userCards.getType() || "0".equals(userCards.getType()))
									id_card = userCards.getUser_auth_card();
								else if ("4" == userCards.getType() || "4".equals(userCards.getType()))
									IC_card = userCards.getUser_auth_card();
								else if ("7" == userCards.getType() || "7".equals(userCards.getType()))
									face_id = userCards.getUser_auth_card();
							}
						}

						// 重新下放新的人员授权到控制板
						GateBoardController m = GateBoardController.getInstance();
						m.setGate_id(gatelist.getGate_no());
						m.addOneAuthInfo(gatelist.getConnect_port(), id_card, IC_card, face_id, authority);

					}
				}

			} else if (gatelist.getType().equals("weigeng")) {
				// 将相应的设备中的权限进行删除
				Boolean delOutCome = this.getService().getGatelistservice().delAuthFromWeigeng(gatelist);
				if (delOutCome == false) {
					Response response = Response.ERROR(-1, "清除闸机所有授权信息出错");
					return response;
				}
			}

			// 删除gate_t_user_cards_auth里对应的记录
			List<GateUserAuth> userAuths = this.getService().getGateuserauthservie().getGateuserauthdao()
					.getListByGateID(gatelist.getId());
			for (GateUserAuth gateUserAuth : userAuths) {
				this.getService().getGateuserauthservie().getGateuserauthdao().delete(gateUserAuth.getId());
			}

			try {
				GateList gt = this.getService().getGatelistservice().getGatelistdao().get(i);
				this.addUserOperationlog("删除设备：" + gt.getGate_name());
			} catch (Exception e) {

			}
			int num = this.getService().getGatelistservice().getGatelistdao().delete(i);
			if (num == 0) {
				Response response = Response.PARAM_ERROR();
				response.put("code", 300);
				response.put("message", "数据库操作失败");
				return response;
			}
		}
		Response response = Response.OK();
		return response;
	}

	/**
	 * 启动闸机设备
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/start")
	@ResponseBody
	public Response start(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		Response response = Response.ERROR(-1, "请手动插好设备线路");
		return response;
	}

	/**
	 * 关闭闸机
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/close")
	@ResponseBody
	public Response close(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		Response response = Response.ERROR(-1, "请手动拔掉设备电源线");
		return response;
	}

	/**
	 * 重启闸机
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/restart")
	@ResponseBody
	public Response restart(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String[] ids = id.split(",");

		for (String i : ids) {
			KaoqinUtuil kqutil = new KaoqinUtuil();
			GateList gatelist = this.getService().getGatelistservice().getGatelistdao().get(i);

			if (gatelist.getType().equals("hanwang")) {
				String DeviceIP = gatelist.getConnect_ip();
				String DevicePort = gatelist.getConnect_port();
				String SecretKey = gatelist.getPassword();
				String TestCommand = "RestartDevice()";
				String answer = kqutil.connect(DeviceIP, DevicePort, SecretKey, TestCommand);
				List<Map> result = kqutil.result(answer);

			} else {
				Response response = Response.ERROR(-1, "请手动拔掉闸机电源线");
				return response;
			}
		}
		Response response = Response.OK();

		return response;

	}

	/**
	 * 常开，该常开可以进行批量操作
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/allopen")
	@ResponseBody
	public Response allopen(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String[] ids = id.split(",");

		for (String i : ids) {
			GateList gatelist = this.getService().getGatelistservice().getGatelistdao().get(i);

			if (gatelist.getType().equals("hanwang")) {
				Response response = Response.ERROR(-1, "设备不支持此操作");
				return response;
			} else if (gatelist.getType().equals("lanlyc")) {
				if (this.getService().getGateboradservie().allopen(gatelist.getConnect_port(), gatelist.getConnect_id(),
						gatelist.getGate_no())) {
					Response response = Response.OK();
					return response;
				} else {
					Response response = Response.ERROR(-1, "设置失败");
					return response;
				}
			} else if (gatelist.getType().equals("weigeng")) {
				if (this.getService().getGateboradservie().openWeigeng(gatelist)) {
					Response response = Response.OK();
					return response;
				} else {
					Response response = Response.ERROR(-1, "设置失败");
					return response;
				}
			}
		}
		Response response = Response.OK();
		return response;

	}

	/**
	 * 正常
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/allnormal")
	@ResponseBody
	public Response allnormal(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String[] ids = id.split(",");
		for (String i : ids) {
			GateList gatelist = this.getService().getGatelistservice().getGatelistdao().get(i);

			if (gatelist.getType().equals("hanwang")) {
				Response response = Response.ERROR(-1, "设备不支持此操作");
				return response;
			} else if (gatelist.getType().equals("lanlyc")) {
				if (this.getService().getGateboradservie().allnormal(gatelist.getConnect_port(),
						gatelist.getConnect_id(), gatelist.getGate_no())) {

				} else {
					Response response = Response.ERROR(-1, "设置失败");
					return response;
				}
			} else if (gatelist.getType().equals("weigeng")) {
				if (this.getService().getGateboradservie().normalWeigeng(gatelist)) {
					Response response = Response.OK();
					return response;
				} else {
					Response response = Response.ERROR(-1, "设置失败");
					return response;
				}
			}
		}
		Response response = Response.OK();
		return response;

	}

	/**
	 * 常闭
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/allclose")
	@ResponseBody
	public Response allclose(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String[] ids = id.split(",");
		for (String i : ids) {
			GateList gatelist = this.getService().getGatelistservice().getGatelistdao().get(i);

			if (gatelist.getType().equals("hanwang")) {
				Response response = Response.ERROR(-1, "设备不支持此操作");
				return response;
			} else if (gatelist.getType().equals("lanlyc")) {
				if (this.getService().getGateboradservie().allclose(gatelist.getConnect_port(),
						gatelist.getConnect_id(), gatelist.getGate_no())) {

				} else {
					Response response = Response.ERROR(-1, "设置失败");
					return response;
				}
			} else if (gatelist.getType().equals("weigeng")) {
				if (this.getService().getGateboradservie().closeWeigeng(gatelist)) {
					Response response = Response.OK();
					return response;
				} else {
					Response response = Response.ERROR(-1, "设置失败");
					return response;
				}
			}
		}
		Response response = Response.OK();
		return response;

	}

	/**
	 * 得到闸机的授权信息
	 * 
	 */
	@RequestMapping("/getGateAuth")
	@ResponseBody
	public Response getGateAuth(String gate_id) {
		Response response = Response.OK();
		GateList gatelist = this.getService().getGatelistservice().getGatelistdao().get(gate_id);
		if (gatelist != null) {
			if (gatelist.getType().equals("hanwang")) {

			} else {
				String com = gatelist.getConnect_port();
				GateReturnMessage datas = this.getService().getGateboradservie().getAuthperson(com,
						gatelist.getGate_no());
				response.setData(datas);
			}
		}

		return response;
	}

	/**
	 * 清除闸机的授权信息
	 * 
	 */
	@RequestMapping("/clearGateAuth")
	@ResponseBody
	public Response clearGateAuth(String gate_id) {
		Response response = Response.OK();
		GateList gatelist = this.getService().getGatelistservice().getGatelistdao().get(gate_id);
		if (gatelist.getType().equals("hanwang")) {

		} else {
			String com = gatelist.getConnect_port();
			boolean datas = this.getService().getGateboradservie().cleanAllAuthority(com, gatelist.getGate_no());
			response.setData(datas);
		}

		return response;
	}

	/**
	 * 获取所有闸机不分页的列表
	 * 
	 * @param paramJson
	 * @return
	 */
	@RequestMapping("/getAllGate")
	@ResponseBody
	public Response getAllGate() {
		List<GateList> gatelist = this.getService().getGatelistservice().getGatelistdao().getAllGate();
		Response response = Response.OK();
		response.setData(gatelist);
		return response;
	}

}
