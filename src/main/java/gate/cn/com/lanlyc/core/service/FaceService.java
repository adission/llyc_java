package gate.cn.com.lanlyc.core.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Com.FirstSolver.Security.Utils;
import Com.FirstSolver.Splash.FaceId_Item;
import cn.com.lanlyc.core.util.KaoqinUtuil;
import cn.com.lanlyc.core.util.UuidTransation;
import gate.cn.com.lanlyc.core.dao.GateListDao;
import gate.cn.com.lanlyc.core.dao.GateUserDao;
import gate.cn.com.lanlyc.core.dao.UserCardsDao;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateListView;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserAuth;

@Service("FaceService")
@Transactional
public class FaceService {
	KaoqinUtuil kqutil = new KaoqinUtuil();
	UuidTransation tra = new UuidTransation();

	@Autowired
	private ServiceContainer myservice;
	public static FaceService myfaceservice;

	/**
	 * 获取 业务类容器
	 * 
	 * @return
	 */
	protected ServiceContainer getService() {
		return myfaceservice.myservice;
	}

	@PostConstruct
	public void init() {
		myfaceservice = this;
	}

	// 检测设备是否存在
	public String existFace(String ip, String port, String password) {
		String TestCommand = "DetectDevice()";
		String answer = kqutil.connect(ip, port, password, TestCommand);
		return answer != "" ? "ok" : "error";
	}

	// 重启设备
	public String reSetFace(String ip, String port, String password) {
		String TestCommand = "RestartDevice()";
		String answer = kqutil.connect(ip, port, password, TestCommand);
		return answer != "" ? "ok" : "error";
	}

	// 时间矫正
	public String reSetFaceTime(String ip, String port, String password) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		String TestCommand = "SetDeviceInfo(time=\"" + time + "\")";
		String answer = kqutil.connect(ip, port, password, TestCommand);
		return answer != "" ? "ok" : "error";
	}

	// 增加一个人员（即是给一个人分配在某个人脸识别机上的相应权限）
	//
	public String addFace(String gong_hao, String name, String ip, String port, String password, List<String> face) {
		Boolean saveFlag = false;
		// 1、根据工号，在相应的工号中，查询相应的人员信息
		if (TestAuth(ip, port, password, gong_hao)) {// 如果设备中有相应的人员权限
			String TestCommand = "GetEmployee(id=\"" + gong_hao + "\")";
			String Answer = kqutil.connect(ip, port, password, TestCommand);

			// 创建一个map用于存储相应的人员的基本信息
			Map<String, String> user_map = new HashMap<String, String>();
			System.out.println(FaceId_Item.GetKeyValue(Answer, "face_data"));
			String[] face_datelist = Answer.split("face_data=\"");
			System.out.println(face_datelist.length);
			String face_date_all = "";
			if (!(face_datelist.length == 1)) {// 代表没有相应的人脸数据
				for (int i = 0; i <= face_datelist.length - 1; i++) {
					if (i == 0) {
						continue;
					} else if (i == face_datelist.length - 1) {
						face_date_all += face_datelist[i].substring(0, face_datelist[i].length() - 2);
					} else {
						face_date_all += face_datelist[i].substring(0, face_datelist[i].length() - 3) + ",";
					}
				}

			}
			user_map.put("sn", FaceId_Item.GetKeyValue(Answer, "sn"));
			user_map.put("id", FaceId_Item.GetKeyValue(Answer, "id"));
			user_map.put("name", FaceId_Item.GetKeyValue(Answer, "name"));
			user_map.put("authority", FaceId_Item.GetKeyValue(Answer, "authority"));
			user_map.put("card_num", FaceId_Item.GetKeyValue(Answer, "card_num"));
			user_map.put("check_type", FaceId_Item.GetKeyValue(Answer, "check_type"));
			user_map.put("opendoor_type", FaceId_Item.GetKeyValue(Answer, "opendoor_type"));
			user_map.put("face_data", face_date_all);

			String face_data = user_map.get("face_data") + "," + user_map.get("authority") + ","
					+ user_map.get("card_num") + "," + user_map.get("check_type") + "," + user_map.get("opendoor_type");

			GateUser gu = card_card(gong_hao);
			GateListView gl = sn_face_info(user_map.get("sn"));

			if (!user_map.get("name").equals(gu.getName())) {
				String[] face_list = face_data.split(",");
				Boolean update_result = updatrFace(gong_hao, gl.getConnect_ip(), gl.getConnect_port(), gl.getPassword(),
						face_list, gu.getName());
				if (update_result) {
					System.out.println("修改名字成功");
				}
			}

			return face_data;//非0是有权限
			
		} else {// 如果没有相应的设备权限
			return "0";//返回0，代表的是，设备中没有权限
		}
	}

	// 给一个人复卡
	public boolean revertFaceAuth(String user_id, String name, String ip, String port, String password, String face) {

		String[] face_list = face.split(",");

		String answer = "";

		if (face_list.length > 18) {// 代表有相应的人脸数据
			// 将相应的信息存入相应的设备
			String TestCommand1 = "SetEmployee(id=\"" + user_id + "\" \r\n" + "name=\"" + name + "\" \r\n"
					+ "calid=\"0\" \r\n" + "card_num=\"" + face_list[19] + "\" \r\n" + "authority=\"" + face_list[18]
					+ "\" \r\n" + "check_type=\"" + face_list[20] + "\"\r\n" + "opendoor_type=\"" + face_list[21]
					+ "\" \r\n" + "face_data=\"" + face_list[0] + "\" \r\n" + "face_data=\"" + face_list[1] + "\" \r\n"
					+ "face_data=\"" + face_list[2] + "\" \r\n" + "face_data=\"" + face_list[3] + "\" \r\n"
					+ "face_data=\"" + face_list[4] + "\" \r\n" + "face_data=\"" + face_list[5] + "\" \r\n"
					+ "face_data=\"" + face_list[6] + "\" \r\n" + "face_data=\"" + face_list[7] + "\" \r\n"
					+ "face_data=\"" + face_list[8] + "\" \r\n" + "face_data=\"" + face_list[9] + "\" \r\n"
					+ "face_data=\"" + face_list[10] + "\" \r\n" + "face_data=\"" + face_list[11] + "\" \r\n"
					+ "face_data=\"" + face_list[12] + "\" \r\n" + "face_data=\"" + face_list[13] + "\" \r\n"
					+ "face_data=\"" + face_list[14] + "\" \r\n" + "face_data=\"" + face_list[15] + "\" \r\n"
					+ "face_data=\"" + face_list[16] + "\" \r\n" + "face_data=\"" + face_list[17] + "\") ";
			answer = kqutil.connect(ip, port, password, TestCommand1);
		} else {// 代表没有相应的人脸数据,代表是只用ic开门的人
				// infos1.get("face_data") + "," + infos1.get("authority") + ","
				// + infos1.get("card_num") + "," + infos1.get("check_type") + "," +
				// infos1.get("opendoor_type");（0、1、2、3、4）
				// 将相应的信息存入相应的设备
			String TestCommand1 = "SetEmployee(id=\"" + user_id + "\" \r\n" + "name=\"" + name + "\" \r\n"
					+ "calid=\"0\" \r\n" + "card_num=\"" + face_list[2] + "\" \r\n" + "authority=\"" + face_list[1]
					+ "\" \r\n" + "check_type=\"" + face_list[3] + "\"\r\n" + "opendoor_type=\"" + face_list[4]
					+ "\") ";
			answer = kqutil.connect(ip, port, password, TestCommand1);
		}

		return answer != "" ? true : false;

	}

	// 删除一个人员(不传user_id删除所有的人员)
	public boolean deleteFace(String user_id, String ip, String port, String password) {
		// 先根据user_id找到相应的用户信息
		String TestCommand = "DeleteEmployee(id=\"" + user_id + "\")";
		String answer = kqutil.connect(ip, port, password, TestCommand);
		return answer != "" ? true : false;
	}

	// 修改相关人员信息(姓名人脸)
	// infos1.get("face_data") + "," + infos1.get("authority") + ","
	// + infos1.get("card_num") + "," + infos1.get("check_type") + "," +
	// infos1.get("opendoor_type");
	public boolean updatrFace(String user_id, String ip, String port, String password, String[] face_list,
			String name) {
		// 先根据user_id找到相应的用户信息
		String Intid = user_id;
		String TestCommand = "DeleteEmployee(id=\"" + Intid + "\")";
		String answer1 = kqutil.connect(ip, port, password, TestCommand);
		String answer = "";
		if (face_list.length > 18) {// 代表有相应的人脸数据
			if (answer1 != "") {
				// 将相应的信息存入相应的设备
				String TestCommand1 = "SetEmployee(id=\"" + user_id + "\" \r\n" + "name=\"" + name + "\" \r\n"
						+ "calid=\"0\" \r\n" + "card_num=\"" + face_list[19] + "\" \r\n" + "authority=\""
						+ face_list[18] + "\" \r\n" + "check_type=\"" + face_list[20] + "\"\r\n" + "opendoor_type=\""
						+ face_list[21] + "\" \r\n" + "face_data=\"" + face_list[0] + "\" \r\n" + "face_data=\""
						+ face_list[1] + "\" \r\n" + "face_data=\"" + face_list[2] + "\" \r\n" + "face_data=\""
						+ face_list[3] + "\" \r\n" + "face_data=\"" + face_list[4] + "\" \r\n" + "face_data=\""
						+ face_list[5] + "\" \r\n" + "face_data=\"" + face_list[6] + "\" \r\n" + "face_data=\""
						+ face_list[7] + "\" \r\n" + "face_data=\"" + face_list[8] + "\" \r\n" + "face_data=\""
						+ face_list[9] + "\" \r\n" + "face_data=\"" + face_list[10] + "\" \r\n" + "face_data=\""
						+ face_list[11] + "\" \r\n" + "face_data=\"" + face_list[12] + "\" \r\n" + "face_data=\""
						+ face_list[13] + "\" \r\n" + "face_data=\"" + face_list[14] + "\" \r\n" + "face_data=\""
						+ face_list[15] + "\" \r\n" + "face_data=\"" + face_list[16] + "\" \r\n" + "face_data=\""
						+ face_list[17] + "\") ";
				answer = kqutil.connect(ip, port, password, TestCommand1);
			}
		} else {// 代表没有相应的人脸数据
			if (answer1 != "") {
				// infos1.get("face_data") + "," + infos1.get("authority") + ","
				// + infos1.get("card_num") + "," + infos1.get("check_type") + "," +
				// infos1.get("opendoor_type");（0、1、2、3、4）
				// 将相应的信息存入相应的设备
				String TestCommand1 = "SetEmployee(id=\"" + user_id + "\" \r\n" + "name=\"" + name + "\" \r\n"
						+ "calid=\"0\" \r\n" + "card_num=\"" + face_list[2] + "\" \r\n" + "authority=\"" + face_list[1]
						+ "\" \r\n" + "check_type=\"" + face_list[3] + "\"\r\n" + "opendoor_type=\"" + face_list[4]
						+ "\") ";
				answer = kqutil.connect(ip, port, password, TestCommand1);
			}
		}

		return answer != "" ? true : false;
	}

	// 获取考勤记录(有相应的时间范围)
	public Object GetRecord(String ip, String port, String password, String start_time, String end_time) {
		String TestCommand = "";
		if (start_time != "" && start_time != null && end_time != "" && end_time != null) {
			TestCommand = "GetRecord(start_time=\"" + start_time + "\" \r\n" + "end_time=\"" + end_time + "\")";
		} else if ((start_time == "" || start_time == null) && (end_time != "" && end_time != null)) {
			TestCommand = "GetRecord(end_time=\"" + end_time + "\")";
		} else if ((start_time != "" && start_time != null) && (end_time == "" || end_time == null)) {
			TestCommand = "GetRecord(start_time=\"" + start_time + "\")";
		} else {
			TestCommand = "GetRecord()";
		}
		String answer = kqutil.connect(ip, port, password, TestCommand);
		String[] list = answer.split("\r\n");
		List<HashMap<String, String>> answer_list = new ArrayList<HashMap<String, String>>();
		for (int j = 1; j < list.length - 1; j++) {
			Map<String, String> answer_map = new HashMap<String, String>();
			String i = list[j];
			String time = i.substring(6, 25);
			answer_map.put("time", time);
			String left = i.substring(27);
			String[] list_left = left.split(" ");
			String id = list_left[0].substring(4, list_left[0].length() - 1);
			answer_map.put("user_id", tra.IntidTransUuid(id));
			answer_list.add((HashMap<String, String>) answer_map);
		}
		return answer != "" ? answer_list : "error";
	}

	// 删除考勤记录(有相应的时间范围之前的所有考勤记录)(如果没有时间范围就全部删除)
	public String delRecord(String ip, String port, String password, String start_time, String end_time) {
		String TestCommand = "";
		if (start_time != "" && start_time != null && end_time != "" && end_time != null) {
			TestCommand = "DeleteRecord(start_time=\"" + start_time + "\" \r\n" + "end_time=\"" + end_time + "\")";
		} else if (start_time == "" || start_time == null && end_time != "" && end_time != null) {
			TestCommand = "DeleteRecord(end_time=\"" + end_time + "\")";
		} else if (start_time != "" && start_time != null && end_time == "" || end_time == null) {
			TestCommand = "DeleteRecord(start_time=\"" + start_time + "\")";
		} else {
			TestCommand = "DeleteAllRecord()";
		}
		String answer = kqutil.connect(ip, port, password, TestCommand);
		return answer != "" ? "ok" : "error";
	}

	// 删除已经自动上报成功的考勤记录
	public boolean DeleteUploadedRecord(String ip, String port, String password) {
		String TestCommand = "DeleteUploadedRecord()";
		String answer = kqutil.connect(ip, port, password, TestCommand);
		return answer != "" ? true : false;
	}

	// 验证一个id在该设备中是否有相应的权限_这个id对应是工号
	public boolean TestAuth(String ip, String port, String password, String id) {
		String TestCommand = "GetEmployee(id=\"" + id + "\")";
		String answer = kqutil.connect(ip, port, password, TestCommand);
		String result = FaceId_Item.GetKeyValue(answer, "result");// 获取相应的结果
		if (result == null)
			return false;
		return result.equals("success") ? true : false;
	}

	// 获取answer中相应的字段
	/*    */ public static String GetKeyValue(String answer, String keyName)
	/*    */ {
		/* 22 */ if (Utils.IsNullOrEmpty(answer)) {
			return null;
			/*    */ }
		/*    */
		/* 25 */ Pattern p = Pattern.compile("\\b" + keyName + "\\s*=\\s*\"([^\"]+)\"");
		/* 26 */ Matcher m = p.matcher(answer);
		/* 27 */ if (m.find()) {
			/* 28 */ return m.group(1);
			/*    */ }
		/* 30 */ return null;
		/*    */ }

	// 根据一个sn，判断一个人脸识别机设备是否已经存入了相关设备数据表中
	public boolean SnText(String sn) {
		// 1、获取符合条件的sn的相应的设备的对象
		System.out.println("11");
		List<GateListView> gl = this.getService().getGatelistservice().getAllhangwangGate(sn);
		return gl != null ? true : false;
	}

	// 根据一个card_id，判断一个人员id是否具有相应的人脸权限
	public boolean card_idText(String card_id, String type) {
		// 1、获取符合条件的card_id的相应的人员权限

		List<GateUserAuth> um = this.getService().getUserCardsService().getUserCardsDao().card_test(card_id, type);
		return um != null ? true : false;
	}

	// 根据sn获取设备对象
	public GateListView sn_face_info(String sn) {
		// 1、获取符合条件的sn的相应的设备的对象
		List<GateListView> gl = this.getService().getGatelistservice().getGatelistdao().getAllllycGate(sn);
		return gl.get(0);
	}

	// 根据card_id获取人员id_card
	public GateUser card_card(String card_id) {
		// 1、获取符合条件的sn的相应的设备的对象
		GateUserDao gd = new GateUserDao();
		int gonghao = Integer.parseInt(card_id);
		// 获取用户的相关信息
		GateUser gl = this.getService().getGateuserservice().getGateuser().getGateuserbycard_id(gonghao, "hanwang");
		return gl;
	}

	// 根据card_id获取人员id_card
	public GateUser card_card_by_type(String card_id, String type) {
		// 1、获取符合条件的sn的相应的设备的对象
		GateUserDao gd = new GateUserDao();
		int gonghao = Integer.parseInt(card_id);
		// 获取用户的相关信息
		GateUser gl = new GateUser();
		if (type.equals("hanwang")) {
			gl = this.getService().getGateuserservice().getGateuser().getGateuserbycard_id(gonghao, type);
		}
		if (type.equals("weigeng")) {
			gl = this.getService().getGateuserservice().getGateuser().getGateuserbycard_id(gonghao, type);
		}
		return gl;
	}

}