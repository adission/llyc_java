package gate.cn.com.lanlyc.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import gate.cn.com.lanlyc.core.po.GateUserClass;
import gate.cn.com.lanlyc.core.po.ShowProjectPeopleCout;
import gate.cn.com.lanlyc.core.po.WorkersTypes;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_DECCHANSTATUS.objectInfo;

@Service
public class ShowProjectPeopleCoutDao extends MySQLMapper<ShowProjectPeopleCout> {

	@Autowired
	private GateUserClassDao gateUserClassDao;
	@Autowired
	private WorkersTypesDao workersTypesDao;

	/**
	 * 查询大屏显示数据表中所有的闸机分组id
	 * 
	 * @return
	 */
	public List<String> getAllGroupId() {
		String sql = "select area from gate_t_showproject_peoplecout";
		return findListForColumn(sql, null, String.class);
	}
	/**
	 * 查询大屏显示数据表中所有的分组数据
	 * 
	 * @return
	 */
	public List<ShowProjectPeopleCout> getAllShow() {
		String sql = "select * from gate_t_showproject_peoplecout";
		return findList(sql, null, ShowProjectPeopleCout.class);
	}	

	/**
	 * 根据闸机分组id获取大屏显示数据
	 * 
	 * @param area
	 * @return
	 */
	public ShowProjectPeopleCout getCoutByArea(String area) {
		Map paramMap = new HashMap();
		paramMap.put("area", area);
		String sql = new String("SELECT * from gate_t_showproject_peoplecout where area=:area");

		return get(sql, paramMap, ShowProjectPeopleCout.class);
	}

	public void delOneCout(ShowProjectPeopleCout cout) {
		String sql = "delete from gate_t_showproject_peoplecout where id=:id";
		Map paramMap = new HashMap();
		paramMap.put("id", cout.getId());

		CharSequence s = sql;
		this.execute(s, paramMap);
	}

	/**
	 * 根据大屏纪录id更新记录
	 * 
	 * @param oldId
	 * @param cout
	 * @return
	 */
	public boolean updateCout(String show_welcome, String area) {
		String sql = "update gate_t_showproject_peoplecout set show_welcome=:show_welcome where area=:area";
		Map paramMap = new HashMap();
		paramMap.put("area", area);
		paramMap.put("show_welcome", show_welcome);

		CharSequence s = sql;
		if (1 == this.execute(s, paramMap))
			return true;
		return false;
	}

	/**
	 * 根据大屏纪录id更新记录
	 * 
	 * @param oldId
	 * @param cout
	 * @return
	 */
	public boolean updateCout(String oldId, ShowProjectPeopleCout cout) {
		String sql = "update gate_t_showproject_peoplecout set show_welcome=:show_welcome,enter_person=:enter_person,admin_person=:admin_person,worker_person=:worker_person,worker_type_person=:worker_type_person,last_upload_time=:last_upload_time,area=:area where id=:id";
		Map paramMap = new HashMap();
		paramMap.put("id", oldId);
		paramMap.put("show_welcome", cout.getShow_welcome());
		paramMap.put("enter_person", cout.getEnter_person());
		paramMap.put("class_type_person", cout.getClass_type_person());
		paramMap.put("last_enter_person", cout.getLast_enter_person());
		paramMap.put("worker_type_person", cout.getWorker_type_person());
		paramMap.put("last_upload_time", cout.getLast_upload_time());
		paramMap.put("area", cout.getArea());

		CharSequence s = sql;
		if (1 == this.execute(s, paramMap))
			return true;
		return false;
	}

	/**
	 * 清空区域内的数据
	 * 
	 * @param worker_type_person
	 * @param area
	 * @return
	 *
	 */
	public Boolean clear(String worker_type_person, String class_type_person, String area) {
		String sql = "update gate_t_showproject_peoplecout set enter_person=:enter_person,worker_type_person=:worker_type_person,last_enter_person=:last_enter_person,class_type_person=:class_type_person where area=:area";
		Map paramMap = new HashMap();
		paramMap.put("worker_type_person", worker_type_person);
		paramMap.put("class_type_person", class_type_person);

		paramMap.put("enter_person", 0);
		paramMap.put("last_enter_person", "");
		paramMap.put("area", area);
		CharSequence s = sql;
		if (1 == this.execute(s, paramMap))
			return true;
		return false;
	}

	/**
	 * 修改大屏上的相应数据
	 * 
	 * @param worker_type_person
	 * @param area
	 * @return
	 *
	 */
	public Boolean change() {
		Boolean change = false;

		// 获取所有的工种name和value值
		String sql = "select * from gate_t_workers_types ORDER BY order_by";
		Map paramMap = new HashMap();
		List<WorkersTypes> allwork = findList(sql, null, WorkersTypes.class);

		// 新建一个列表将相应的value存起来
		List<String> list_value = new ArrayList<String>();
		// 获取所有的分区的大屏显示数据
		List<ShowProjectPeopleCout> list_show = this.getAll();
		// 遍历所有大屏显示数据
		for (ShowProjectPeopleCout sj : list_show) {
			// 新建一个map用于存放新建的工种json数据
			List<Map<String, Object>> wt = new ArrayList<Map<String, Object>>();
			// 获取当前大屏上的所有的工种的value值和work_name
			String worker_type_person = sj.getWorker_type_person();
			// 获取当前大屏的数据，并将其转换为相应的json数据格式
			JSONArray ja = JSONArray.parseArray(worker_type_person);
			// 遍历数据表中的工种的值
			for (WorkersTypes work : allwork) {
				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("type_value", work.getValue());
				jsonMap.put("type_name", work.getName());
				// 遍历相应的大屏的json数据
				Boolean exsit_count = false;
				for (int i = 0; i < ja.size(); i++) {
					Map map = (Map) ja.get(i);

					String type_value = (String) map.get("type_value");
					if (work.getValue().equals(type_value)) {
						jsonMap.put("type_count", map.get("type_count"));
						exsit_count = true;
					}
				}
				// 如果是新建的相应的工种，就添加相应的人员数量的字段
				if (!exsit_count) {
					jsonMap.put("type_count", 0);
				}
				// 将数据表中的json字段先存起来
				wt.add(jsonMap);
				// 将相应的工种值存到一个列表中
				list_value.add(work.getValue());
			}
			// 数据表中已经删除的工种，进行相应的保留，但是数据是放在最后的
			for (int i = 0; i < ja.size(); i++) {
				Map map = (Map) ja.get(i);

				String type_value = (String) map.get("type_value");
				if (!list_value.contains(type_value)) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					// 1.遍历相应的数据wt
					for (Map<String, Object> work_type_map : wt) {
						// 获取 默认工种-(其他工种)
						WorkersTypes workersTypes = workersTypesDao.getDefaultWork();

						if (work_type_map.get("type_value").equals(workersTypes.getValue())) {
							int work_index = wt.indexOf(work_type_map);
							jsonMap.put("type_value", work_type_map.get("type_value"));
							jsonMap.put("type_name", work_type_map.get("type_name"));
							int work_type_account = Integer.parseInt(String.valueOf(work_type_map.get("type_count")))
									+ Integer.parseInt(String.valueOf(map.get("type_count")));
							jsonMap.put("type_count", work_type_account);
							wt.remove(work_index);
							wt.add(work_index, jsonMap);
							break;
						}
					}

					// wt.add(jsonMap);
				}
			}
			// 修改相应的数据表
			String Worker_type_person = JSONObject.toJSONString(wt);

			ShowProjectPeopleCout sp_new = new ShowProjectPeopleCout();

			sp_new.setId(sj.getId());

			sp_new.setWorker_type_person(Worker_type_person);

			int change_line = this.update(sp_new, false);

			if (change_line == 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 修改大屏上的相应数据_有关人员类别的修改
	 * 
	 * @param worker_type_person
	 * @param area
	 * @return
	 *
	 */
	public Boolean ChangeClass() {
		Boolean change = false;

		// 获取所有的工种name和value值
		String sql = "select * from gate_t_user_class ORDER BY id";
		Map paramMap = new HashMap();
		List<GateUserClass> allwork = findList(sql, null, GateUserClass.class);

		// 新建一个列表将相应的value存起来
		List<String> list_value = new ArrayList<String>();
		// 获取所有的分区的大屏显示数据
		List<ShowProjectPeopleCout> list_show = this.getAll();
		// 遍历所有大屏显示数据
		for (ShowProjectPeopleCout sj : list_show) {
			// 新建一个map用于存放新建的工种json数据
			List<Map<String, Object>> wt = new ArrayList<Map<String, Object>>();
			// 获取当前大屏上的所有的工种的id值和class_name
			String class_type_person = sj.getClass_type_person();
			// 获取当前大屏的数据，并将其转换为相应的json数据格式
			JSONArray ja = JSONArray.parseArray(class_type_person);
			// 遍历数据表中的工种的值
			for (GateUserClass work : allwork) {
				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("type_value", work.getId());
				jsonMap.put("type_name", work.getName());
				// 遍历相应的大屏的json数据
				Boolean exsit_count = false;
				for (int i = 0; i < ja.size(); i++) {
					Map map = (Map) ja.get(i);

					String type_value = (String) map.get("type_value");
					if (work.getId().equals(type_value)) {
						jsonMap.put("type_count", map.get("type_count"));
						exsit_count = true;
					}
				}
				// 如果是新建的相应的工种，就添加相应的人员数量的字段
				if (!exsit_count) {
					jsonMap.put("type_count", 0);
				}
				// 将数据表中的json字段先存起来
				wt.add(jsonMap);
				// 将相应的工种值存到一个列表中
				list_value.add(work.getId());
			}
			// 数据表中已经删除的工种，进行相应的保留，但是数据是放在最后的
			for (int i = 0; i < ja.size(); i++) {
				Map map = (Map) ja.get(i);

				String type_value = (String) map.get("type_value");

				if (!list_value.contains(type_value)) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					// 1.遍历相应的数据wt
					for (Map<String, Object> class_type_map : wt) {
						// 获取 默认人员类别-(其他人员)
						GateUserClass gateUserClass = gateUserClassDao.getDefaultClass();
						if (class_type_map.get("type_value").equals(gateUserClass.getId())) {
							int work_index = wt.indexOf(class_type_map);
							jsonMap.put("type_value", class_type_map.get("type_value"));
							jsonMap.put("type_name", class_type_map.get("type_name"));
							int work_type_account = Integer.parseInt(String.valueOf(class_type_map.get("type_count")))
									+ Integer.parseInt(String.valueOf(map.get("type_count")));
							jsonMap.put("type_count", work_type_account);
							wt.remove(work_index);
							wt.add(work_index, jsonMap);
							break;
						}
					}

					// wt.add(jsonMap);
				}

			}
			// 修改相应的数据表
			String Class_type_person = JSONObject.toJSONString(wt);

			ShowProjectPeopleCout sp_new = new ShowProjectPeopleCout();

			sp_new.setId(sj.getId());

			sp_new.setClass_type_person(Class_type_person);

			int change_line = this.update(sp_new, false);

			if (change_line == 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 当相应人员的工种和人员类发生修改后的相应
	 * 
	 * @param value_old(这个是原来的需要减一的值),value_new(这个现在需要修改成的值),area,value_type(work代表的是工种，class代表的人员类别)
	 * @param
	 * @return
	 *
	 */
	public Boolean changeScreen(String value_old, String value_new, String area, String value_type) {
		// 1修改类型是工种
		if (value_type.equals("work")) {
			return changeScreenWork(value_old, value_new, area);
		}
		// 2修改类型是人员类别
		if (value_type.equals("class")) {
			return changeScreenClass(value_old, value_new, area);
		}

		return null;
	}

	// 1、修改类型是工种
	private Boolean changeScreenWork(String value_old, String value_new, String area) {
		// a)获取分区的大屏显示数据
		// b)将其转换为相应的可修改的json格式
		JSONArray json_work = getJsonScreen(area, "work");
		Boolean isZero = testIsZero(json_work, value_old);
		if (isZero) {
			return true;
		} else {
			JSONArray json_work_minus =  addOrMinusOne(json_work, value_old, "minus");
			JSONArray json_work_add =addOrMinusOne(json_work_minus, value_new, "add");
			ShowProjectPeopleCout sp_new = getCoutByArea(area);
			sp_new.setWorker_type_person(JSONObject.toJSONString(json_work_add));
			this.update(sp_new,false);
			return true;
		}
	}

	// 2、修改类型是人员类别
	private Boolean changeScreenClass(String value_old, String value_new, String area) {
		// a)获取分区的大屏显示数据
		// b)将其转换为相应的可修改的json格式
		JSONArray json_work = getJsonScreen(area, "class");
		Boolean isZero = testIsZero(json_work, value_old);
		if (isZero) {
			return false;
		} else {
			JSONArray json_work_minus =  addOrMinusOne(json_work, value_old, "minus");
			JSONArray json_work_add =addOrMinusOne(json_work_minus, value_new, "add");
			ShowProjectPeopleCout sp_new = getCoutByArea(area);
			sp_new.setClass_type_person(JSONObject.toJSONString(json_work_add));
			this.update(sp_new,false);
			return true;
		}
	}

	// 根据区域的id，获取该区域的相应的数据，并将相应的数据转变为相应的json格式
	private JSONArray getJsonScreen(String area, String value_type) {
		// 获取相应的大屏数据对象
		ShowProjectPeopleCout showProjectPeopleCout = this.getCoutByArea(area);
		JSONArray res_json = new JSONArray();
		if (value_type.equals("work")) {
			res_json = JSONArray.parseArray(showProjectPeopleCout.getWorker_type_person());
		}
		if (value_type.equals("class")) {
			res_json = JSONArray.parseArray(showProjectPeopleCout.getClass_type_person());
		}
		return res_json;
	}

	// 验证旧的是否是0
	private Boolean testIsZero(JSONArray json_work, String value_old) {
		for (Object json_single : json_work) {
			Map work_map = (Map) json_single;
			if (work_map.get("type_value").equals(value_old)) {
				int work_type_count = Integer.parseInt(String.valueOf(work_map.get("type_count")));
				if (work_type_count == 0) {// 如果原来的工种的人数是0的情况下，相应的数据是不变的
					return true;
				} else {
					return false;
				}
			}
		}
		return null;
	}

	// 对相应的人员的数据进行相应的加减
	private JSONArray addOrMinusOne(JSONArray json_work, String value, String addOrMinus) {
		for (Object json_single : json_work) {
			Map work_map = (Map) json_single;
			if (work_map.get("type_value").equals(value)) {
				int work_type_count = Integer.parseInt(String.valueOf(work_map.get("type_count")));
				int work_index = json_work.indexOf(json_single);
				Map<String, Object> jsonMap = new HashMap<String, Object>();

				jsonMap.put("type_value", work_map.get("type_value"));
				jsonMap.put("type_name", work_map.get("type_name"));
				int work_type_account = 0;
				if (addOrMinus.equals("add")) {
					work_type_account = Integer.parseInt(String.valueOf(work_map.get("type_count"))) + 1;
				} else if (addOrMinus.equals("minus")) {
					work_type_account = Integer.parseInt(String.valueOf(work_map.get("type_count"))) - 1;
				}
				jsonMap.put("type_count", work_type_account);
				json_work.remove(work_index);
				json_work.add(work_index, jsonMap);
				break;
			}
		}
		return json_work;
	}

}
