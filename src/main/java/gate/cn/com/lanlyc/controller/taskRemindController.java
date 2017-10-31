package gate.cn.com.lanlyc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.velocity.runtime.directive.Break;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import Com.FirstSolver.Splash.FaceId_Item;
import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.DateUtils;
import cn.com.lanlyc.base.util.HttpUtils;
import cn.com.lanlyc.core.util.GateBoardController;
import gate.cn.com.lanlyc.core.po.CheckLog;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateListView;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.GateUserClass;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import gate.cn.com.lanlyc.core.po.Setting;
import gate.cn.com.lanlyc.core.po.ShowProjectPeopleCout;
import gate.cn.com.lanlyc.core.po.UserCards;
import gate.cn.com.lanlyc.core.po.WorkersTypes;
import gate.cn.com.lanlyc.core.service.FaceListenTest;
import gate.cn.com.lanlyc.core.service.FaceService;
import gate.cn.com.lanlyc.core.service.GateBoardService;
import gate.cn.com.lanlyc.core.service.GateListService;
import gate.cn.com.lanlyc.core.service.SettingService;
import gate.cn.com.lanlyc.core.service.WgListener;
import gate.cn.com.lanlyc.core.service.WorkersTypesService;

@Component("myTask")
@Lazy(false)
public class taskRemindController extends BaseController {

	private static String TOKEN = "";
	SettingService settingservice = null;

	Setting system_setting = null;
	private int request_time = 0;

	@Scheduled(cron = "0/1 * * * * ?")
	public void run() throws ParseException {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println(sdf.format(new Date()) + "定时任务执行");

		// 定时处理实时刷卡记录,并删除已处理的记录
		GateBoardController m = GateBoardController.getInstance();// 默认打开了所有的COM口
		List<String[]> checkLogs = m.getCheckLogs();// 保存实时接收到的并且还未做处理的刷卡记录
		if (checkLogs != null && checkLogs.size() != 0) {
			// ***************保存刷卡记录或者大屏显示数据
			this.dealCheckLog(checkLogs.get(0));
			// if (this.dealCheckLog(checkLogs.get(0)))
			// 有一个失误就会重复执行****************
			checkLogs.remove(0);
		}

		// 获取人脸监控系统的相应的数据处理
		FaceListenTest ft = FaceListenTest.getInstance();
		// 考勤数据
		List<Map<String, String>> check_list = ft.getcheck_list();
		// 用户数据
		List<Map<String, String>> user_list = ft.getuser_list();

		// 定时处理实时人脸的传来的考勤数据,并删除已处理的记录
		if (check_list != null && check_list.size() != 0) {

			if (this.dealfacecheck_list(check_list.get(0)))
				check_list.remove(0);
		}
		// 定时处理实时人脸的传来的人脸数据,并删除已处理的记录
		if (user_list != null && user_list.size() != 0) {

			if (this.dealfaceuser_list(user_list.get(0)))
				user_list.remove(0);
		}
		// 获取相应的监听器的单例
		WgListener ws = WgListener.getInstance();

		// 相应考勤数据的处理
		List<Map<String, String>> wg_check_list = ws.getwgcheck_list();

		// 定时处理实时人脸的传来的考勤数据,并删除已处理的记录
		if (wg_check_list != null && wg_check_list.size() != 0) {

			if (this.delwgcheck_list(wg_check_list.get(0)))
				wg_check_list.remove(0);
		}

	}

	// 处理一条相应的微耕中的数据
	private boolean delwgcheck_list(Map<String, String> infos1) {
		// TODO Auto-generated method stub
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

		boolean saveFlag = false;// 将刷卡信息保存进数据库中是否成功
		// 获取设备编号
		String sn = infos1.get("sn");
		// 获取对应的设备列表
		List<GateListView> list = this.getService().getGatelistservice().getGatelistdao().getAllllycGate(sn, "weigeng");

		if (list.size() != 0 && list != null) {// 如果在设备列表中，就将相应的人员存储起来
			// 1.先获取相应的索引号
			int index = Integer.parseInt(infos1.get("index"));
			// 2、获取数据表中的索引
			int begain_index = Integer.parseInt(list.get(0).getLast_queryrecord_time());

			if (begain_index <= index - 1) {
				if (begain_index < index - 1) {
					// 先将之前的数据处理了
					handle(begain_index, index, sn, list.get(0));
				}
				// 获取当天的时间
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				String cur_time = String.valueOf(df.format(new Date()));
				Boolean today = infos1.get("punch_time").indexOf(cur_time) != -1 ? true : false;

				if (infos1.get("effective").equals("1") && today) {
					Boolean out_come = handeldata(list.get(0), infos1);
					if (out_come) {// 考勤数据存储成功
						// 将设备的索引进行修改
						GateList ga = new GateList();
						ga.setId(list.get(0).getId());
						ga.setLast_queryrecord_time(String.valueOf(index));
						this.getService().getGatelistservice().getGatelistdao().update(ga, false);
						saveFlag = true;
					}
				} else if (infos1.get("effective").equals("1") && (!today)) {
					handle(index - 1, index + 1, sn, list.get(0));
					GateList ga = new GateList();
					ga.setId(list.get(0).getId());
					ga.setLast_queryrecord_time(String.valueOf(index));
					this.getService().getGatelistservice().getGatelistdao().update(ga, false);
					saveFlag = true;
				} else {// 即使这个打卡是无效的，也将索引处理了
					GateList ga = new GateList();
					ga.setId(list.get(0).getId());
					ga.setLast_queryrecord_time(String.valueOf(index));
					this.getService().getGatelistservice().getGatelistdao().update(ga, false);
					saveFlag = true;
				}

			} else {
				// 不对数据做处理，就是无视
				saveFlag = true;
			}
		}
		return saveFlag;
	}

	// 对考勤数据进行处理
	private boolean handeldata(GateListView gateListView, Map<String, String> infos1) {
		// TODO Auto-generated method stub
		GateUser gu = this.getService().getFaceservice().card_card_by_type(infos1.get("card"), "weigeng");
		if (gu == null) {
			return true;
		}
		boolean saveFlag = false;
		CheckLog log = new CheckLog();
		log.setCard_no(infos1.get("card"));
		log.setGate_no(gateListView.getGate_no());
		log.setCheck_date(infos1.get("punch_time"));
		// log.setCross_flag(cross_flag);
		log.setCheck_type("4");
		// 把考勤数据存入数据表中
		String cross_flag = infos1.get("cross").equals("1") ? "1" : "0";
		try {

			saveFlag = this.setShowProjectPeopleCout(infos1.get("card"), cross_flag, gateListView.getGroupid(),
					gateListView.getWelcome(), log);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return saveFlag;
	}

	// 处理微耕机器上两个索引之间记录
	// 一般用于服务器断开，有没有处理的数据
	// 传的下标是（0，187）（那么就处理1到186所有的数据）
	private void handle(int begain_index, int index, String sn, GateListView gate) {
		// TODO Auto-generated method stub
		for (int i = begain_index + 1; i < index; i++) {
			Map<String, String> record = this.getService().getWgService().getRecordByIndex(gate.getConnect_ip(),
					Long.parseLong(sn), i);
			if (record != null) {
				if (record.get("index").equals("161")) {
					System.out.println("111");
				}
			}
			// 对相应的数据进行处理的条件是
			// 1、数据必须是相应的考勤数据
			// 2、数据必须是有效的
			// 3、如果不是当天的数据，只把当天的数据进行相应存储
			// 4、如果是当天的数据，就涉及了相应的大屏数据的修改
			if (record != null && record.get("effective").equals("1")) {

				Boolean today = testToday(record.get("punch_time"));
				// 获取相应的人员对象
				if (!today) {// 如果数据不是今天的数据
					// 需要根据卡号，获取人员对象
					// 根据sn获取设备对象(参数中已经获取相应的数据类型)
					// 记录需要存入的数据
					// id（有）、身份证（没有）、卡号（有了）、打卡时间（有了）、进出方向（0是出，1是进）（已经获取）、打卡类型（4）、是否是考情（这个不用）
					GateUser gu = this.getService().getFaceservice().card_card_by_type(record.get("card"), "weigeng");
					if (gu == null) {
						continue;// 如果没有相应的人员信息，就不对数据进行处理——是否要删除权限-暂时先不处理
					}
					CheckLog log = new CheckLog();
					log.setId(DataUtils.getUUID());
					log.setId_card(gu.getCid());
					log.setCard_no(record.get("card"));
					log.setGate_no(gate.getGate_no());
					log.setCheck_date(record.get("punch_time"));
					log.setCross_flag(record.get("cross").equals("1") ? "1" : "0");
					log.setCheck_type("4");
					this.getService().getChecklogservice().getCheckLogDao().save(log);
				} else {// 如果是今天的数据，就涉及大屏数据的修改,因为不是最后一条数据，就不对相应的数据进行相应的处理
					Boolean out_come = handeldata(gate, record);
				}

			}
		}
	}

	// 本方法是用于测试相应的打卡时间，是否是今天的打卡数据
	// 此方法只是一个相应的小工具
	private Boolean testToday(String punch_time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		String cur_time = String.valueOf(df.format(new Date()));
		Boolean today = punch_time.indexOf(cur_time) != -1 ? true : false;
		return today;
	}

	/**
	 * 处理一条实时刷卡记录
	 * 
	 * @param infos
	 */
	public boolean dealCheckLog(String[] infos) {
		boolean saveFlag = false;// 将刷卡信息保存进数据库中是否成功

		String connect_id = infos[4];
		String connect_port = infos[3];
		GateListView gate = this.getService().getGatelistservice().getGatelistdao()
				.getGateListByPortAndConnectId(connect_port, connect_id);
		if (null != gate) {
			// 将刷卡记录保存到数据库
			String user_auth_card = "";
			CheckLog log = new CheckLog();
			log.setId(DataUtils.getUUID());
			if ("0".equals(infos[2])) {
				log.setId_card(infos[0]);
				user_auth_card = infos[0];
				log.setCheck_type("0");
			} else if ("1".equals(infos[2])) {
				log.setCard_no(infos[0].substring(1));
				user_auth_card = infos[0].substring(1);
				log.setCheck_type("4");
			}
			log.setGate_no(gate.getGate_no());
			log.setCheck_date(infos[1]);

			// if (1 == this.getService().getChecklogservice().getCheckLogDao().save(log))
			// saveFlag = true;

			saveFlag = this.setShowProjectPeopleCout(user_auth_card, gate.getCrossflag(), gate.getGroupid(),
					gate.getWelcome(), log);

		}

		return saveFlag;
	}

	/**
	 * 处理一条实时刷脸数据
	 * 
	 * @param infos1
	 * @throws ParseException
	 */
	public boolean dealfacecheck_list(Map<String, String> infos1) throws ParseException {
		boolean saveFlag = false;// 将刷卡信息保存进数据库中是否成功
		// 获取设备编号
		String sn = infos1.get("sn");

		FaceService fs = this.getService().getFaceservice();
		// 验证该设备是否在数据表中
		Boolean exist = fs.SnText(sn);

		if (exist) {// 如果在设备列表中，就将相应的人员存储起来
			// 1、先判断相应的人员是否具有相应的人脸的权限
			String card_id = infos1.get("id");
			Boolean auth = fs.card_idText(card_id, "7");
			Boolean checktoday = testToday(infos1.get("time"));

			if (auth) {// 只是代表有显相应的权限，但是并没有就相应数据
				String id = infos1.get("id");
				String name = infos1.get("name");
				String time = infos1.get("time");
				// 如果人员有相应人脸权限
				// 先获取相应的设备信息
				GateListView gl = fs.sn_face_info(sn);
				// 获取相关人员信息
				GateUser gu = fs.card_card(card_id);
				if (gu == null) {// 如果该权限，没有相应的人员对应，就对考勤不处理——但是权限不删除，（原因：防止ip设置不对应时，人员权限的删除）
					System.out.println("该考勤数据无对应的人员，对相应的考勤数据不处理");
					return true;
				}

				String cross_flag = gl.getCrossflag();
				// 根据相应的人员信息，判断这次考勤是否进行相应的计数
				// Boolean is_kaoqin =
				// this.getService().getChecklogservice().check_times(gu.getId(), "7");
				// Boolean is_kaoqin_1 =
				// this.getService().getGatelistservice().GateTimeHandle();
				if (checktoday) {// 如果是当天的数据
					/*
					 * if (cross_flag.equals("2")) { cross_flag =
					 * this.getService().getChecklogservice().getCheckLogDao().ChangCross(gu.getId()
					 * , gl.getGroup_id()); }
					 */
					CheckLog log = new CheckLog();

					log.setCard_no(id);
					log.setGate_no(gl.getGate_no());
					log.setCheck_date(time);
					// log.setCross_flag(cross_flag);
					log.setCheck_type("7");
					// 把考勤数据存入数据表中
					try {

						saveFlag = this.setShowProjectPeopleCout(id, cross_flag, gl.getGroupid(), gl.getWelcome(), log);

					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {// 如果不是当天的数据，就只把数据存入相应的数据表中，不对大屏进行相应的修改
					saveFlag = true;
					CheckLog log = new CheckLog();
					log.setId(DataUtils.getUUID());
					log.setId_card(gu.getCid());
					log.setCard_no(id);// 这个id只是工号，请不要误解
					log.setGate_no(gl.getGate_no());
					log.setCheck_date(time);
					// log.setCross_flag(cross_flag);
					log.setCheck_type("7");
					String cross = "";
					if (cross_flag.equals("2")) {
						cross = this.getService().getChecklogservice().getCheckLogDao().ChangCross(gu.getId(),
								gl.getGroupid(), time.substring(0, 10));
					} else {
						cross = cross_flag;
					}
					log.setCross_flag(cross);
					int i = this.getService().getChecklogservice().getCheckLogDao().save(log);
					saveFlag = true;
				}

			} else {
				// 如果没有相应的人脸权限
				// 先获取相应的设备信息（这个功能已经屏蔽，不会实现人员的删除）
				GateListView gl = fs.sn_face_info(sn);
				// 将数设备中相应的人员给删除了
				String connect_port = gl.getConnect_port();
				String connect_ip = gl.getConnect_ip();
				String password = gl.getPassword();
				String id = infos1.get("id");
				Boolean del = true;
				// fs.deleteFace(id, connect_ip, connect_port, password);
				// 根据相应sn获取设备的相应的设备属性
				// 根据相应的设备属性和id对相应的人员进行删除

				if (del) {// 如果人员删除成功
					saveFlag = true;

				}
			}
		} else {
			saveFlag = true;
		}
		return saveFlag;
	}

	/**
	 * 处理一条实时人员信息
	 * 
	 * @param infos1
	 */
	public boolean dealfaceuser_list(Map<String, String> infos1) {
		boolean saveFlag = false;// 将刷卡信息保存进数据库中是否成功
		// 获取设备编号
		String sn = infos1.get("sn");

		FaceService fs = this.getService().getFaceservice();
		// 验证该设备是否在数据表中
		Boolean exist = fs.SnText(sn);

		if (exist) {// 如果在设备列表中，就将相应的人员存储起来
			// 1、先判断相应的人员是否具有相应的人脸的权限
			String card_id = infos1.get("id");
			String name = infos1.get("name");
			Boolean auth = fs.card_idText(card_id, "7");
			if (auth) {

				// 如果人员有相应人脸权限

				// 把人员存入相应数据表中

				// 将图片存入相应的数据表中
				String id = infos1.get("id");
				// 如果人员有相应人脸权限
				// 先获取相应的设备信息
				GateListView gl = fs.sn_face_info(sn);
				// 获取相关人员信息
				GateUser gu = fs.card_card(card_id);

				String face_data = infos1.get("face_data") + "," + infos1.get("authority") + ","
						+ infos1.get("card_num") + "," + infos1.get("check_type") + "," + infos1.get("opendoor_type");

				// 如果数据表中已经存在相应的数据
				GateUserAuth gu_exist = this.getService().getGateuserauthservie().getGateuserauthdao()
						.getUserAuth(gu.getId(), gl.getId());

				if (gu_exist == null) {
					// 获取人员设备权限表
					GateUserAuth ga = new GateUserAuth();
					// 获取相应的id
					ga.setId(DataUtils.getUUID());
					// 获取相应的用户的user_id
					ga.setUser_id(gu.getId());
					// 获取相应的设备id
					ga.setGate_id(gl.getId());
					// 是否可用
					ga.setIs_use("0");
					// 将人脸数据存入数据表中，用于复卡的操作
					ga.setFace_data(face_data);
					// 将相应的人员权限存入数据表中
					this.getService().getGateuserauthservie().getGateuserauthdao().save(ga);

				} else {
					gu_exist.setFace_data(face_data);
					this.getService().getGateuserauthservie().getGateuserauthdao().update(gu_exist, false);
				}
				String[] face_list = face_data.split(",");

				if (name == null || !gu.getName().equals(name)) {
					Boolean update_result = fs.updatrFace(id, gl.getConnect_ip(), gl.getConnect_port(),
							gl.getPassword(), face_list, gu.getName());

					if (update_result) {
						System.out.println("修改名字成功");
						saveFlag = true;
					}

				} else {
					saveFlag = true;
				}

			} else {
				// 如果没有相应的人脸权限
				// 先获取相应的设备信息
				GateListView gl = fs.sn_face_info(sn);
				// 将数设备中相应的人员给删除了
				String connect_port = gl.getConnect_port();
				String connect_ip = gl.getConnect_ip();
				String password = gl.getPassword();
				String id = infos1.get("id");
				Boolean del = true;
				// fs.deleteFace(id, connect_ip, connect_port, password);
				// 根据相应sn获取设备的相应的设备属性
				// 根据相应的设备属性和id对相应的人员进行删除

				if (del) {// 如果人员删除成功
					saveFlag = true;
				}

			}
		}
		return saveFlag;
	}

	/**
	 * 统计人员进出
	 * 
	 * @param user_auth_card
	 *            刷卡授权
	 * @param Cross_flag
	 *            闸机进出类型
	 * @param Group_id
	 *            闸机所属分组
	 * @param welcome
	 *            闸机所属分组大屏欢迎语
	 * @return
	 */
	public boolean setShowProjectPeopleCout(String user_auth_card, String Cross_flag, String Group_id, String welcome,
			CheckLog log) {
		boolean coutFlag = false;// 根据刷卡信息统计进出人员是否成功

		UserCards ucard = this.getService().getUserCardsService().getUserCardsDao()
				.getUserCardByUserAuthCard(user_auth_card);
		if (ucard == null)
			return false;
		GateUserInfoView user = this.getService().getGateuserservice().getGateuser()
				.getUserByuserId(ucard.getUser_id());
		// GateUser user =
		// this.getService().getGateuserservice().getGateuser().getUserById(ucard.getUser_id());
		String cross = "";
		if (Cross_flag.equals("2")) {
			cross = this.getService().getChecklogservice().getCheckLogDao().ChangCross(user.getId(), Group_id, null);
		} else {
			cross = Cross_flag;
		}
		// 往大屏推送最新的刷卡人信息
		ChatServer push_lastperson = new ChatServer();
		push_lastperson.getlast_enter_person(Group_id, user);

		log.setCross_flag(cross);

		log.setId(DataUtils.getUUID());
		log.setId_card(user.getCid());
		// log.set
		this.getService().getChecklogservice().getCheckLogDao().save(log);

		this.Check(log);

		// 获取工种的对象
		WorkersTypes wt = this.getService().getWorkerstypesservice().getWorkersTypes()
				.getWorkersTypesinfoByvalue(user.getWorkers_type());
		// 获取人员类别的对象
		GateUserClass gc = this.getService().getGateuserclassservice().getGateuserclass().get(user.getUser_class_id());
		// 获取相应的数据
		ShowProjectPeopleCout cout = new ShowProjectPeopleCout();
		cout.setId(DataUtils.getUUID());
		cout.setShow_welcome(welcome);
		cout.setEnter_person("1");
		cout.setClass_type_person(user.getUser_class_id());
		cout.setLast_enter_person(user.getId());
		cout.setWorker_type_person(user.getWorkers_type());
		cout.setLast_upload_time("");
		cout.setArea(Group_id);
		coutFlag = this.addOrUpdateCout(cout, cross, user);
		return coutFlag;
	}

	/**
	 * 统计人员进出数据，保存在大屏统计数据中
	 * 
	 * @param cout
	 * @param cross_flag
	 * @param user
	 */
	public boolean addOrUpdateCout(ShowProjectPeopleCout cout, String cross_flag, GateUserInfoView user) {
		if (user == null)
			return false;
		// 获取所有已经有人员统计的闸机分区的id集合
		List<String> allGroupId = this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao()
				.getAllGroupId();
		// 如果已经存在的没有相应的分区，就新建一个
		if (!allGroupId.contains(cout.getArea())) {
			// 新建一个相应的大屏对象
			ShowProjectPeopleCout cout_new = new ShowProjectPeopleCout();
			cout_new.setId(DataUtils.getUUID());
			cout_new.setShow_welcome(cout.getShow_welcome());
			cout_new.setEnter_person("0");
			cout_new.setArea(cout.getArea());
			// 对相应的人员类别值进行相应的添加
			cout_new.setClass_type_person(this.getService().getGateuserclassservice().getGateuserclass().AllclassStr());
			// 对相应的工种值进行添加
			cout_new.setWorker_type_person(this.getService().getWorkerstypesservice().getWorkersTypes().AllworkStr());
			this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao().save(cout_new);

		}

		// 先获取user_id、区域id、进出方向（1进0出）
		Boolean testFirst = this.getService().getChecklogservice().getCheckLogDao().testFirst(user.getId(),
				cout.getArea());
		// 根据区域id获取大屏对象
		ShowProjectPeopleCout cout_obj = this.getService().getShowProjectPeopleCoutService()
				.getShowProjectPeopleCoutDao().getCoutByArea(cout.getArea());

		if (!testFirst && (!cout_obj.getEnter_person().equals("0"))) {// 如果最近两次在该区域的进出方向是一致的
			// 进：如果大屏数是0，仍旧加1

			// 对相应对象进行修改
			ShowProjectPeopleCout cout1 = new ShowProjectPeopleCout();
			cout1.setId(cout_obj.getId());
			cout1.setLast_enter_person(user.getId());
			this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao().update(cout1, false);
			PushDataStatistics pushdatastatistics = new PushDataStatistics();

			pushdatastatistics.getlast_enter_person(this.getService().getShowProjectPeopleCoutService()
					.getShowProjectPeopleCoutDao().get(cout_obj.getId()), cout.getArea(), user.getId());
			// 如果不是0，就不计数，计最后的人
			// 出：无论是不是0，都不减1
			// 只是要把最后出入的人变一下
			return true;
		} else {
			// 闸机属于大屏记录表中的某个闸机分组
			// 先获取相应的分区显示的对象
			ShowProjectPeopleCout cout2 = this.getService().getShowProjectPeopleCoutService()
					.getShowProjectPeopleCoutDao().getCoutByArea(cout.getArea());
			// 分区中已经进入的人员数
			String enter_person = cout2.getEnter_person();
			// 这个是考勤数据处理完后的数据
			String enter_person_new = "";
			if ("0".equals(cross_flag)) {// 出门
				if (null == enter_person)
					enter_person_new = "0";
				else {
					// 人数不能少于零
					enter_person_new = Integer.parseInt(enter_person) - 1 > 0
							? String.valueOf(Integer.parseInt(enter_person) - 1)
							: "0";
				}
			} else if ("1".equals(cross_flag)) {// 进门
				if (null == enter_person)
					enter_person = "1";
				else
					enter_person_new = String.valueOf(Integer.parseInt(enter_person) + 1);
			}
			// 新的相应的数据
			String worker_type_person_new = this.getService().getWorkerstypesservice().getWorkersTypes()
					.UpdaWorkStr(user.getWorkers_type(), cross_flag, cout.getArea());

			String class_type_person_new = this.getService().getGateuserclassservice().getGateuserclass()
					.UpdaclassStr(user.getUser_class_id(), cross_flag, cout.getArea());

			cout2.setLast_enter_person(user.getId());
			cout2.setEnter_person(enter_person_new);
			// 调用socket 给客户端发送区域有人进入的函数
			PushDataStatistics pushdatastatistics = new PushDataStatistics();
			System.out.println(cout.getArea());

			cout2.setClass_type_person(class_type_person_new);
			cout2.setWorker_type_person(worker_type_person_new);
			int issucc = this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao().update(cout2,
					false);
			pushdatastatistics.getlast_enter_person(cout2, cout.getArea(), user.getId());
			if (issucc == 1) {// 如果修改成功
				return true;
			}

			return false;
		}
	}

	/**
	 * 冒泡排序worker_type,根据gate_t_workers_types表中的order_by字段升序排序
	 * 
	 * @param list
	 * @return
	 */
	public List<String> sortList(List<String> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 1; j < list.size() - i; j++) {
				String t;
				WorkersTypes wt1 = this.getService().getWorkerstypesservice().getWorkersTypes()
						.getWorkersTypesinfoByvalue(list.get(j - 1));
				WorkersTypes wt2 = this.getService().getWorkerstypesservice().getWorkersTypes()
						.getWorkersTypesinfoByvalue(list.get(j));
				if (wt1 != null && wt2 != null
						&& Integer.parseInt(wt1.getOrder_by()) > Integer.parseInt(wt2.getOrder_by())) { // 通过工种order_by字段大小排序

					t = list.get(j - 1);
					list.set((j - 1), list.get(j));
					list.set(j, t);
				}
			}
		}

		return list;
	}

	/**
	 * 打卡
	 * 
	 */
	public int Check(CheckLog log) {

		String module = "check";
		System.out.println("上报打卡信息");
		settingservice = this.getService().getSettingservice();
		system_setting = settingservice.getSettingdao().getOneObject();
		String TOKEN = system_setting.getToken();
		String url = system_setting.getRequest_url() + "/gate/" + module;
		String region_id = system_setting.getRegion_id();
		String scrt_key = system_setting.getScrt_key();

		String seq_no = MakeSeq();
		String parm = "region_id=" + region_id + "&scrt_key=" + scrt_key + "&seq_no=" + seq_no + "&TOKEN=" + TOKEN;
		Date check_date = DateUtils.strToDate(log.getCheck_date());
		String date_str = DateUtils.dateToStr(check_date, 5);
		parm = parm + "&CID=" + log.getId_card() + "&card_no=" + log.getCard_no() + "&gate_no=" + log.getGate_no()
				+ "&check_date=" + date_str;
		// 将最晚一条刷卡记录并未上传的数据 提交到云端服务器
		parm = parm + "&cross_flag=" + log.getCross_flag() + "&check_type=" + log.getCheck_type();

		String data = HttpUtils.sendPost(url, parm);
		System.out.println("请求url" + parm);
		JSONObject json = JSONObject.parseObject(data);

		int error = json.getInteger("error");
		System.out.println("请求url" + parm);
		if (error == 0) {
			request_time = 0;
			TOKEN = json.getString("TOKEN");

			ChangeToken();
		} else {
			request_time = request_time + 1;
			this.login();
			if (request_time <= 3) {
				int error2 = this.Check(log);
			}

		}
		System.out.print("上报打卡记录结果:" + error);
		return error;

	}

	/**
	 * 登陆接口login
	 * 
	 */
	public String login() {

		System.out.println("登录");
		String module = "login";
		String url = system_setting.getRequest_url() + "/gate/" + module;
		String seq_no = MakeSeq();
		String parm = "region_id=" + system_setting.getRegion_id() + "&scrt_key=" + system_setting.getScrt_key()
				+ "&seq_no=" + seq_no;

		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);
		int error = json.getInteger("error");
		if (error == 0) {
			TOKEN = json.getString("TOKEN");
			this.ChangeLastLogintime();

		}

		return TOKEN;

	}

	/**
	 * 每次登陆成功后更新数据库的最后登录时间
	 * 
	 */
	private void ChangeLastLogintime() {
		if (system_setting != null) {
			// System.out.println("登陆后改变token");
			system_setting.setToken(TOKEN);
			String last_login_time = DateUtils.getCurrenTimeStamp();

			system_setting.setLast_login_time(last_login_time);
			settingservice.getSettingdao().update(system_setting);

		}
	}

	/**
	 * 
	 * 
	 * 
	 * @api {get} taskRemind/ChangeToken
	 * @apiGroup APIManager
	 * @apiName TODO(这里用一句话描述这个类的作用)
	 * @apiDescription更新DeviceToken
	 * @apiParam
	 * @apiSuccess
	 * @apiVersion 1.0.0
	 * @apiErrorExample {json} Error-Response: { "code" :"", "message": "请登录" }
	 */
	private void ChangeToken() {
		if (system_setting != null) {
			// System.out.println("请求后改变token");
			system_setting.setToken(TOKEN);
			settingservice.getSettingdao().update(system_setting);
		}
	}

	/**
	 * 生成带时间的请求编号
	 * 
	 * @return
	 */
	public String MakeSeq() {
		Date date = new Date();

		String time = DateUtils.dateToStr(date, 5);
		int radomInt = new Random().nextInt(999999);
		String seq = time + String.valueOf(radomInt);
		return seq;

	}
}
