package gate.cn.com.lanlyc.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.ExcelUtiltest;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.dao.GateUserClassDao;
import gate.cn.com.lanlyc.core.dao.GateUserDao;
import gate.cn.com.lanlyc.core.dao.WorkersTypesDao;
import gate.cn.com.lanlyc.core.po.GateGroup;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserClass;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import gate.cn.com.lanlyc.core.po.WorkersTypes;
import net.sf.json.JSONObject;

@Service("GateUserService")
@Transactional
public class GateUserService {
	@Autowired
	private GateUserDao gateuser;
	@Autowired
	private GateUserClassDao gateUserClassDao;
	@Autowired
	private WorkersTypesDao workersTypesDao;
	@Autowired
	private UserCardsService userCardsService;
	@Autowired
	private GateGroupService gateGroupService;
	@Autowired
	private CheckLogService checkLogService;
	@Autowired
	private ShowProjectPeopleCoutService showProjectPeopleCoutService;

	public GateUserDao getGateuser() {
		return gateuser;
	}

	public void setGateuser(GateUserDao gateuser) {
		this.gateuser = gateuser;
	}

	// 该方法是进行相应的手机号的验证
	public Map<String, Object> test_mobile(String mobile, String user_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String ref = gateuser.getGateuserInfoByMobileandcid(mobile, null, user_id);
		if (ref != null) {
			paramMap.put("id", ref);
		} else {
			paramMap.put("no_reat", "no_reat");
		}
		return paramMap;
	}

	// 该方法是进行相应的身份证的验证
	public Map<String, Object> test_cid(String cid, String user_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String ref = gateuser.getGateuserInfoByMobileandcid(null, cid, user_id);
		if (ref != null) {
			paramMap.put("id", ref);
		} else {
			paramMap.put("no_reat", "no_reat");
		}
		return paramMap;
	}

	// 将相应的人员的工种对应的value进行相应的修改
	// 获取所有的相应的人员的
	// work_type_id 是相应的工种的id
	public Boolean changeUserWorkTypeToDefault(String work_type_id) {
		WorkersTypes workersTypes = workersTypesDao.getDefaultWork();
		Boolean change_outcome = gateuser.changeUserWorkTypeToDefault(work_type_id, workersTypes.getValue());
		return change_outcome;
	}

	// 将相应的人员类别修改为相应的默认值
	// 获取所有的相应的人员的
	// user_class_id 是相应的工种的id
	public Boolean changeUserClassToDefault(String user_class_id) {
		GateUserClass gateUserClass = gateUserClassDao.getDefaultClass();
		Boolean change_outcome = gateuser.changeUserClassToDefault(user_class_id, gateUserClass.getId());
		return change_outcome;
	}

	// 1、增加人员（四个基本方法）
	public String add_gate_user(JSONObject json) {
		String name = json.getString("name");
		String mobile = json.getString("mobile");
		String gender = json.getString("gender");
		String sate = "0";
		String avatar_img = json.getString("avatar_img");
		String cid = json.getString("cid");
		String workers_type = json.getString("workers_type");
		String team = "";
		if (json.containsKey("team")) {
			team = json.getString("team");
		}
		String punish_record = "";
		if (json.containsKey("punish_record")) {
			punish_record = json.getString("punish_record");
		}
		String user_class_id = json.getString("user_class_id");
		GateUser GU = new GateUser();
		String id = DataUtils.getUUID();
		GU.setId(id);
		GU.setName(name);
		GU.setCid(cid);
		GU.setMobile(mobile);
		GU.setGender(gender);
		GU.setSate(sate);
		GU.setAvatar_img(avatar_img);
		GU.setWorkers_type(workers_type);
		if (team != "") {
			GU.setTeam(team);
		}
		if (punish_record != "") {
			GU.setPunish_record(punish_record);
		}
		GU.setUser_class_id(user_class_id);
		int save_changr_line = this.getGateuser().save(GU);
		if (save_changr_line != 0) {
			String log = "新增用户：" + name;
			return log;
		} else {
			return null;
		}
	}

	// 2、删除人员（四个基本方法）
	public String del_gate_user(String id) {
		String ids = id;
		String[] ids1 = ids.split(",");

		// 删除gate_t_user_cards和gate_t_user_cards_auth里的记录，同时删除控制板里面该人员的授权（相当于销卡）
		boolean is_success = (Boolean) userCardsService.ClearUserCards(ids);

		StringBuffer strb = new StringBuffer("删除用户：");
		for (String id_split : ids1) {
			GateUser gu = this.getGateuser().get(id_split);
			strb.append(gu.getName() + " ");
			this.getGateuser().delete(id_split);
		}
		return strb.toString();
	}

	// 3、修改人员（四个基本方法）
	public String update_gate_user(JSONObject json) {
		String id = json.getString("id");
		GateUser gate_user_old = this.getGateuser().get(id);
		GateUser GU = new GateUser();
		GU.setId(id);
		if (json.containsKey("name")) {
			if (json.getString("name") != "") {
				String name = json.getString("name");
				GU.setName(name);
			}
		}
		if (json.containsKey("mobile")) {
			if (json.getString("mobile") != "") {
				String mobile = json.getString("mobile");
				GU.setMobile(mobile);
			}
		}
		if (json.containsKey("gender")) {
			if (json.getString("gender") != "") {
				String gender = json.getString("gender");
				GU.setGender(gender);
			}
		}
		if (json.containsKey("sate")) {
			if (json.getString("sate") != "") {
				String sate = json.getString("sate");
				GU.setSate(sate);
			}
		}
		if (json.containsKey("avatar_img")) {
			if (json.getString("avatar_img") != "") {
				String avatar_img = json.getString("avatar_img");
				GU.setAvatar_img(avatar_img);
			}
		}
		if (json.containsKey("workers_type")) {
			if (json.getString("workers_type") != "") {
				String workers_type = json.getString("workers_type");
				GU.setWorkers_type(workers_type);
				if (!workers_type.equals(gate_user_old.getWorkers_type())) {
					// 调用一个相应的工种修改方法
					List<GateGroup> all_group = gateGroupService.getGategroupdao().getAll();
					for (GateGroup group : all_group) {
						if (checkLogService.getCheckLogDao().queryCross(id, group.getId()).equals("1")) {
							showProjectPeopleCoutService.getShowProjectPeopleCoutDao()
									.changeScreen(gate_user_old.getWorkers_type(), workers_type, group.getId(), "work");
						}
					}
				}
			}
		}
		if (json.containsKey("team")) {
			if (json.getString("team") != "") {
				String team = json.getString("team");
				GU.setTeam(team);
			}
		}
		if (json.containsKey("punish_record")) {
			if (json.getString("punish_record") != "") {
				String punish_record = json.getString("punish_record");
				GU.setPunish_record(punish_record);
			}
		}

		if (json.containsKey("user_class_id")) {
			if (json.getString("user_class_id") != "") {
				String user_class_id = json.getString("user_class_id");
				GU.setUser_class_id(user_class_id);
				if (!user_class_id.equals(gate_user_old.getUser_class_id())) {
					// 调用一个相应的人员类别修改方法
					// 调用一个相应的工种修改方法
					List<GateGroup> all_group = gateGroupService.getGategroupdao().getAll();
					for (GateGroup group : all_group) {
						if (checkLogService.getCheckLogDao().queryCross(id, group.getId()).equals("1")) {
							showProjectPeopleCoutService.getShowProjectPeopleCoutDao().changeScreen(
									gate_user_old.getUser_class_id(), user_class_id, group.getId(), "class");
						}
					}
				}
			}
		}
		if (json.containsKey("cid")) {
			if (json.getString("cid") != "") {
				String cid = json.getString("cid");
				GU.setCid(cid);
			}
		}
		GU.setGonghao(Integer.parseInt(json.getString("gonghao")));
		int up_outcome = this.getGateuser().update(GU, false);
		String outcome = null;
		if (up_outcome != 0) {// 表示修改成功
			GateUser gu = this.getGateuser().get(id);
			outcome = "修改用户：" + gu.getName();
		}
		return outcome;
	}

	// 4、查询人员（四个基本方法）
	public Page<GateUserInfoView> list_gate_user(JSONObject json, int current_page, int page_num) {
		String name = json.containsKey("name") ? json.getString("name") : null;
		String cid = json.containsKey("cid") ? json.getString("cid") : null;
		String mobile = json.containsKey("mobile") ? json.getString("mobile") : null;
		String card_id = json.containsKey("card_id") ? json.getString("card_id") : null;
		String type = json.containsKey("type") ? json.getString("type") : null;
		String sate = json.containsKey("sate") ? json.getString("sate") : null;
		String age = json.containsKey("age") ? json.getString("age") : null;
		String gender = json.containsKey("gender") ? json.getString("gender") : null;
		String card_type = json.containsKey("card_type") ? json.getString("card_type") : null;
		String workers_type = json.containsKey("workers_type") ? json.getString("workers_type") : null;
		String effective_time = json.containsKey("effective_time") ? json.getString("effective_time") : null;
		String order = json.containsKey("order") ? json.getString("order") : null;
		String order_by = json.containsKey("order_by") ? json.getString("order_by") : null;
		List<GateUser> gus;

		Page<GateUserInfoView> page;
		if (1 == 1) {
			page = this.getGateuser().getAllbyName(current_page, // Integer.parseInt(str)
					page_num, name, cid, mobile, type, sate, age, gender, card_type, card_id, workers_type,
					effective_time, order, order_by, null, null);

			for (GateUserInfoView gi : page.getResultRows()) {
				String c_id = gi.getCid();
				// 获取当前的年份
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				int now_year = Integer.parseInt(df.format(new Date()));
				if (c_id.length() == 18) {
					int borth = Integer.parseInt(c_id.substring(6, 10));
					if (now_year - borth >= 0) {
						gi.setAge(String.valueOf(now_year - borth));
					} else {
						gi.setAge("身份证格式错误");
					}
				} else if (c_id.length() == 15) {
					int borth = Integer.parseInt("19" + c_id.substring(6, 8));
					if (now_year - borth >= 0) {
						gi.setAge(String.valueOf(now_year - borth));
					} else {
						gi.setAge("身份证格式错误");
					}
				} else {
					gi.setAge("身份证格式错误");
				}
			}
		}
		return page;

	}

	// 4、查询人员（四个基本方法）(导出Excel)
	public void Excel_gate_user(JSONObject json, HttpServletRequest request, HttpServletResponse response1) {
		String name = json.containsKey("name") ? json.getString("name") : null;
		String cid = json.containsKey("cid") ? json.getString("cid") : null;
		String mobile = json.containsKey("mobile") ? json.getString("mobile") : null;
		String card_id = json.containsKey("card_id") ? json.getString("card_id") : null;
		String type = json.containsKey("type") ? json.getString("type") : null;
		String sate = json.containsKey("sate") ? json.getString("sate") : null;
		String age = json.containsKey("age") ? json.getString("age") : null;
		String gender = json.containsKey("gender") ? json.getString("gender") : null;
		String card_type = json.containsKey("card_type") ? json.getString("card_type") : null;
		String workers_type = json.containsKey("workers_type") ? json.getString("workers_type") : null;
		String effective_time = json.containsKey("effective_time") ? json.getString("effective_time") : null;
		String order = json.containsKey("order") ? json.getString("order") : null;
		String order_by = json.containsKey("order_by") ? json.getString("order_by") : null;
		Response response = Response.OK();
		List<GateUser> gus;

		List<GateUserInfoView> page;

		page = this.getGateuser().getAllbyNameExcel(// Integer.parseInt(str)
				name, cid, mobile, type, sate, age, gender, card_type, workers_type, effective_time, order, order_by);

		//
		String[] headers = { "工号", "姓名", "身份证", "性别", "手机号码", "类别", "工种", "班组", "开卡状态" };

		List<List> dataset = new ArrayList<List>();
		for (GateUserInfoView user_info : page) {
			dataset.add(new ArrayList() {
				{
					add(user_info.getGonghao() != null ? user_info.getGonghao() : "");
					add(user_info.getName() != null ? user_info.getName() : "");
					add(user_info.getCid() != null ? user_info.getCid() : "");
					add(user_info.getGender() != null ? user_info.getGender() : "");
					add(user_info.getMobile() != null ? user_info.getMobile() : "");

					add(user_info.getClass_name() != null ? user_info.getClass_name() : "");
					add(user_info.getWorker_type() != null ? user_info.getWorker_type() : "");
					add(user_info.getTeam() != null ? user_info.getTeam() : "");
					add(user_info.getSate() != null ? user_info.getSate() : "");

				}
			});
		}
		ExcelUtiltest.Test(request, response1, headers, dataset);
	}

	// 4、查询人员（四个基本方法）
	public Page<GateUserInfoView> list_gate_user_byIC(JSONObject json, int current_page, int page_num) {
		String name = json.containsKey("user_name") ? json.getString("user_name") : null;
		String cid = json.containsKey("cid") ? json.getString("cid") : null;
		String mobile = json.containsKey("mobile") ? json.getString("mobile") : null;
		String card_id = json.containsKey("user_card") ? json.getString("user_card") : null;
		String type = json.containsKey("type") ? json.getString("type") : null;
		String sate = json.containsKey("sate") ? json.getString("sate") : null;
		String age = json.containsKey("age") ? json.getString("age") : null;
		String gender = json.containsKey("gender") ? json.getString("gender") : null;
		String card_type = json.containsKey("card_type") ? json.getString("card_type") : null;
		String workers_type = json.containsKey("workers_type") ? json.getString("workers_type") : null;
		String effective_time = json.containsKey("effective_time") ? json.getString("effective_time") : null;
		String order = json.containsKey("order") ? json.getString("order") : null;
		String order_by = json.containsKey("order_by") ? json.getString("order_by") : null;
		String gate_id = json.containsKey("gate_id") ? json.getString("gate_id") : null;
		List<GateUser> gus;

		Page<GateUserInfoView> page;
		if (1 == 1) {
			page = this.getGateuser().getAllbyName(current_page, // Integer.parseInt(str)
					page_num, name, cid, mobile, type, sate, age, gender, card_type, card_id, workers_type,
					effective_time, order, order_by, "4", gate_id);

			for (GateUserInfoView gi : page.getResultRows()) {
				String c_id = gi.getCid();
				// 获取当前的年份
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				int now_year = Integer.parseInt(df.format(new Date()));
				if (c_id.length() == 18) {
					int borth = Integer.parseInt(c_id.substring(6, 10));
					if (now_year - borth >= 0) {
						gi.setAge(String.valueOf(now_year - borth));
					} else {
						gi.setAge("身份证格式错误");
					}
				} else if (c_id.length() == 15) {
					int borth = Integer.parseInt("19" + c_id.substring(6, 8));
					if (now_year - borth >= 0) {
						gi.setAge(String.valueOf(now_year - borth));
					} else {
						gi.setAge("身份证格式错误");
					}
				} else {
					gi.setAge("身份证格式错误");
				}
			}
		}
		return page;
	}

}
