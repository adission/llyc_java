package gate.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.GateUserAuthView;

@Service
public class GateUserAuthDao extends MySQLMapper<GateUserAuth> {

	/**
	 * 获取人员在一个闸机的权限
	 * 
	 * @param user_id
	 * @param gate_id
	 * @return
	 */
	public GateUserAuth getUserAuth(String user_id, String gate_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		paramMap.put("gate_id", gate_id);
		String sql = new String("SELECT * from gate_t_user_cards_auth where user_id=:user_id and gate_id=:gate_id");
		GateUserAuth userauths = get(sql, paramMap, GateUserAuth.class);
		return userauths;

	}

	/**
	 * 获取人员在所有闸机的权限
	 * 
	 * @param user_id
	 * @param gate_id
	 * @return
	 */
	public List<GateUserAuthView> getUserGateAuth(String user_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);

		String sql = new String("SELECT * from gate_v_gate_auth where user_id=:user_id");
		List<GateUserAuthView> userauths = findList(sql, paramMap, GateUserAuthView.class);
		return userauths;

	}

	/**
	 * 查询某一个闸机下有权限的人员列表
	 * 
	 * @param gate_id
	 * @return
	 */
	public List<GateUserAuth> getGateUserAuth(String gate_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("gate_id", gate_id);
		String sql = new String("SELECT * from gate_v_gate_auth where  gate_id=:gate_id");
		List<GateUserAuth> userauths = findList(sql, paramMap);
		return userauths;

	}

	/**
	 * 根据相关条件查询相应管理的日志
	 * 
	 * @param
	 * 
	 * @return 列表
	 */
	public Page<GateUserAuthView> queryGateAllAuth(Page<GateUserAuthView> page, Map<String, Object> paramMap) {
		StringBuffer sql = new StringBuffer("SELECT * from gate_v_gate_auth");
		sql.append(" where  1=1");
		if (paramMap.containsKey("name")) {
			String name = (String) paramMap.get("name");
			if (StringUtils.isNotEmpty(name)) {
				sql.append(" and  name LIKE '%").append(name).append("%' ");
				paramMap.remove("name");
			}

		}
		if (paramMap.containsKey("gate_id")) {
			String gate_id = (String) paramMap.get("gate_id");
			if (StringUtils.isNotEmpty(gate_id)) {
				sql.append(" and  gate_id ='").append(gate_id).append("' ");
				paramMap.remove("gate_id");
			}

		}
		Page<GateUserAuthView> result = getPage(sql, null, page, GateUserAuthView.class);
		return result;
	}

	/**
	 * 判断一个人员在一个控制板和指定通道下是否有权限
	 * 
	 * @param user_id
	 * @param COM
	 * @param connect_id
	 * @return
	 */
	public boolean GateBoradHasUserAuth(String user_id, String COM, String connect_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("type_value", "lanlyc");
		paramMap.put("user_id", user_id);
		paramMap.put("connect_port", COM);

		paramMap.put("connect_id", connect_id);
		String sql = new String(
				"SELECT * from gate_v_gate_auth where type_value=:type_value and user_id=:user_id and connect_port=:connect_port and connect_id=:connect_id");
		GateUserAuth userauths = get(sql, paramMap, GateUserAuth.class);
		boolean result = false;
		if (userauths != null) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断一个人员在一个控制板和指定通道下是否有权限
	 * 
	 * @param current_page//当前页面
	 * @param page_num//页面的数据
	 * @param user_name//用户名称
	 * @param gate_name//闸机名称
	 * @param gate_type//闸机类型
	 * @param is_use//是否可以进出的状态
	 * @return
	 */
	public Page<GateUserAuth> queryList(int current_page, int page_num, String user_name, String gate_name,
			String gate_type, String is_use) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("gate_type", gate_type);
		paramMap.put("is_use", is_use);
		Page<GateUserAuth> page = new Page<GateUserAuth>(page_num, current_page, 0);
		String sql1 = new String("SELECT * from gate_t_user_cards_auth ");
		String sql2 = new String("where 1=1 ");
		String sql3 = user_name != ""
				? new String("and user_id in (select id from gate_t_user where name like '%" + user_name + "%') ")
				: "";
		String sql4 = gate_name != ""
				? new String("and gate_id in (select id from gate_t_list where gate_name like '%" + gate_name + "%' ")
				: "";
		String sql5 = gate_type != "" ? new String("and type=:gate_type ) ") : ") ";
		String sql6 = is_use != "" ? new String("and is_use=:is_use order_by user_id,id ") : " ";
		String sql = sql1 + sql2 + sql3 + sql4 + sql5 + sql6;
		Page<GateUserAuth> userauths = getPage(sql, paramMap, page, GateUserAuth.class);
		if (userauths != null) {
			return userauths;
		} else {
			return null;
		}
	}

	/**
	 * 通过gate_id查询gate_t_user_cards_auth记录
	 * 
	 * @param gate_id
	 * @return
	 */
	public List<GateUserAuth> getListByGateID(String gate_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("gate_id", gate_id);
		String sql = new String("SELECT * from gate_t_user_cards_auth where gate_id=:gate_id");

		return findList(sql, paramMap, GateUserAuth.class);
	}

	// 获取该人员，某种类型的所有可以使用（或不能使用的）权限的闸机权限
	public List<GateUserAuth> getGateUserAuthbytype(String user_id, String type, String is_use) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		paramMap.put("is_use", is_use);
		if (type.equals("4")) {
			paramMap.put("type", "lanlyc");
		} else if (type.equals("7")) {
			paramMap.put("type", "hanwang");
		}

		String sql = new String(
				"SELECT * from gate_t_user_cards_auth where user_id=:user_id and gate_id in (select id from gate_t_list where type=:type) ");

		if (is_use != null) {
			sql += "and is_use=:is_use";
		}
		return findList(sql, paramMap, GateUserAuth.class);
	}

	// 获取该人员下所有的设备权限对象
	public List<GateUserAuth> getUserAuthbyUser(String user_id, String is_use) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		paramMap.put("is_use", is_use);
		String sql = new String("SELECT * from gate_t_user_cards_auth where user_id=:user_id ");

		if (is_use != null) {
			sql += "and is_use=:is_use";
		}
		return findList(sql, paramMap, GateUserAuth.class);
	}

	// 根据控制板编号和通道号获取相应的设备
	public GateUserAuth getAuthBygateidandUserid(String gate_id, String user_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("gate_id", gate_id);
		paramMap.put("user_id", user_id);
		paramMap.put("is_use", "0");
		String sql = new String(
				"SELECT * from gate_t_user_cards_auth where user_id=:user_id and gate_id=:gate_id and is_use=:is_use");
		List<GateUserAuth> list = findList(sql, paramMap, GateUserAuth.class);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}
}
