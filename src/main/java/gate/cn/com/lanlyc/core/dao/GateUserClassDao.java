package gate.cn.com.lanlyc.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.GateGroup;
import gate.cn.com.lanlyc.core.po.GateUserClass;
import gate.cn.com.lanlyc.core.po.ShowProjectPeopleCout;
import gate.cn.com.lanlyc.core.po.WorkersTypes;

@Service
public class GateUserClassDao extends MySQLMapper<GateUserClass> {
	/**
	 * 根据用户名，模糊查询相关的人员数据
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public Page<GateUserClass> queryList(int current_page, int page_num, String class_name, String order,
			String order_by) {
		GateUserClass GUC = new GateUserClass();
		Map paramMap = new HashMap();
		Page<GateUserClass> page = new Page<GateUserClass>(page_num, current_page, 0);
		String sql1 = new String("SELECT * from gate_t_user_class ");
		String sql2 = new String("where 1=1 ");
		String sql3 = new String("");
		String sql4 = new String("");
		String sql5 = class_name != "" ? new String("and name like '%" + class_name + "%' ") : "";
		if (order != "") {// 数据按相应的字段进行排序
			sql3 = new String("ORDER BY " + order + " ");
		} else {// 默认按name进行排序
			sql3 = new String("ORDER BY name ");
		}
		if (order_by != "") {// 排序的是正序还是反序
			sql4 = new String(order_by);
		} else {// 默认是升序
			sql4 = new String("ASC");
		}
		String sql = sql1 + sql2 + sql5 + sql3 + sql4;
		Page<GateUserClass> u1 = getPage(sql, paramMap, page, GateUserClass.class);
		System.out.println(u1);
		if (u1 != null) {
			return u1;
		} else {
			return null;
		}
	}

	/**
	 * 获取所有的人员类别值，并将其拼接成相应的字符串
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public String AllclassStr() {
		// 获取相应的对象并排序
		String sql = "select * from gate_t_user_class ORDER BY id";
		Map paramMap = new HashMap();
		List<GateUserClass> allwork = findList(sql, paramMap);
		// 将相应的对象获取相应的值
		List<Map<String, Object>> wt = new ArrayList<Map<String, Object>>();
		for (GateUserClass work : allwork) {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("type_value", work.getId());
			jsonMap.put("type_name", work.getName());
			jsonMap.put("type_count", 0);
			wt.add(jsonMap);
		}
		String str = JSONObject.toJSONString(wt);
		return str;
	}

	/**
	 * 获传一个工种人员类别值，和进出（0出，1进，），area的id 返回将要存的worker_type_person的值
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public String UpdaclassStr(String value, String cross, String area) {
		// 获取相应的对象并排序
		String sql = "select * from gate_t_showproject_peoplecout where area=:area";
		Map paramMap = new HashMap();
		paramMap.put("area", area);
		List<ShowProjectPeopleCout> allwork = findList(sql, paramMap, ShowProjectPeopleCout.class);
		String wkp = allwork.get(0).getClass_type_person();
		JSONArray ja = JSONArray.parseArray(wkp);
		for (int i = 0; i < ja.size(); i++) {
			Map map = (Map) ja.get(i);
			String type_value = (String) map.get("type_value");
			if (type_value.equals(value)) {
				System.out.println(map.get("type_count"));
				int type_count = (Integer) map.get("type_count");
				if (cross.equals("1")) {
					type_count = type_count + 1;
				} else if (cross.equals("0")) {
					type_count = type_count - 1 >= 0 ? type_count - 1 : 0;
				}
				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("type_value", value);
				jsonMap.put("type_name", map.get("type_name"));
				jsonMap.put("type_count", type_count);
				ja.remove(i);
				ja.add(i, jsonMap);
			}
		}
		String str = JSONObject.toJSONString(ja);
		return str;
	}

	/**
	 */
	public List<GateUserClass> classnametest(String name, String id) {
		if (id == null) {
			WorkersTypes GUC = new WorkersTypes();
			String sql = new String("SELECT * from gate_t_user_class where name = \"" + name + "\"");
			List<GateUserClass> u1 = findList(sql, null, GateUserClass.class);
			if (u1.size() != 0)
				return u1;
			return null;
		} else {
			WorkersTypes GUC = new WorkersTypes();
			String sql = new String(
					"SELECT * from gate_t_user_class where name = \"" + name + "\" and id != \"" + id + "\"");
			List<GateUserClass> u1 = findList(sql, null, GateUserClass.class);
			if (u1.size() != 0)
				return u1;
			return null;
		}

	}

	/**
	 * 获取相应的默认的人员类别的id
	 */
	public GateUserClass getDefaultClass() {
		Map paramMap = new HashMap();
		paramMap.put("name", "其他人员");
		String sql = new String("SELECT * from gate_t_user_class where name =:name");
		List<GateUserClass> u1 = findList(sql, paramMap, GateUserClass.class);
		if (u1.size() != 0)
			return u1.get(0);
		return null;

	}

	// 进行相应人员类别的名称验证
	public GateUserClass testUserClass(String name, String id) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("select * from gate_t_user_class where name =:name ");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", name);
		paramMap.put("id", id);
		if (id == null) {
			List<GateUserClass> result = findList(sql, paramMap, GateUserClass.class);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		} else {
			sql.append(" and id != :id");
			List<GateUserClass> result = findList(sql, paramMap, GateUserClass.class);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		}
	}

}
