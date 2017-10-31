package gate.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.DateUtils;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateListView;

@Service
public class GateListDao extends MySQLMapper<GateList> {

	public Page<GateList> getAllGate(Page<GateList> page, Map<String, Object> paramMap, String orderColumn,
			String orderDir) {
		StringBuffer sql = new StringBuffer("SELECT * from gate_v_list");
		if (paramMap.containsKey("gate_name")) {
			String gate_name = (String) paramMap.get("gate_name");
			if (StringUtils.isNotEmpty(gate_name)) {
				sql.append(" where  gate_name LIKE '%").append(gate_name).append("%' ");
			} else {
				sql.append(" where  1=1");
			}
			paramMap.remove("gate_name");
		}

		if (paramMap.containsKey("gate_no")) {
			String gate_no = (String) paramMap.get("gate_no");
			if (StringUtils.isNotEmpty(gate_no)) {
				sql.append(" and gate_no LIKE '%").append(gate_no).append("%' ");
			}
			paramMap.remove("gate_no" + "");
		}

		if (orderColumn != null && orderColumn != "" && orderDir != null && orderDir != "") {
			sql.append(" order by ").append(orderColumn).append(" ").append(orderDir);
		} else {
			sql.append(" order by ").append("cast(gate_no as SIGNED INTEGER)").append(" ").append("ASC");
		}
		Page<GateList> result = getPage(sql, null, page, GateList.class);
		return result;
	}

	public List<GateList> getAllGateExcel(Map<String, Object> paramMap, String orderColumn, String orderDir) {

		StringBuffer sql = new StringBuffer("SELECT * from gate_v_list");
		if (paramMap.containsKey("gate_name")) {
			String gate_name = (String) paramMap.get("gate_name");
			if (StringUtils.isNotEmpty(gate_name)) {
				sql.append(" where  gate_name LIKE '%").append(gate_name).append("%' ");
			} else {
				sql.append(" where  1=1");
			}
			paramMap.remove("gate_name");
		}

		if (paramMap.containsKey("gate_no")) {
			String gate_no = (String) paramMap.get("gate_no");
			if (StringUtils.isNotEmpty(gate_no)) {
				sql.append(" and gate_no LIKE '%").append(gate_no).append("%' ");
			}
			paramMap.remove("gate_no" + "");
		}

		if (orderColumn != null && orderColumn != "" && orderDir != null && orderDir != "") {
			sql.append(" order by ").append(orderColumn).append(" ").append(orderDir);
		} else {
			sql.append(" order by ").append("cast(gate_no as SIGNED INTEGER)").append(" ").append("ASC");
		}
		List<GateList> result = findList(sql, null, GateList.class);
		return result;
	}

	/**
	 * 获取单个闸机详情
	 * 
	 * @param id
	 * @return
	 */
	public GateList getGateListInfo(String id) {
		Map paramMap = new HashMap();
		paramMap.put("id", id);

		String sql = new String("SELECT * from gate_v_list where id=:id");
		GateList gate = get(sql, paramMap, GateList.class);
		return gate;

	}

	public List<GateList> getAllGate() {
		List<GateList> allgate = this.getAll("gate_t_list");
		return allgate;

	}

	/**
	 * 获取所有蓝领英才的考勤设置
	 * 
	 */
	public List<GateList> getAllllycGate() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("type", "lanlyc");

		String sql = new String("SELECT * from gate_t_list where type=:type");

		List<GateList> gate_list = findList(sql, paramMap);
		return gate_list;
	}

	/**
	 * 获取所有人脸识别的的考勤设置
	 * 
	 */
	public List<GateListView> getAllllycGate(String sn) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer("SELECT * from gate_v_list where type_value='hanwang' ");
		sql.append(" and sn='").append(sn).append("'");

		List<GateListView> gate_list = findList(sql, paramMap, GateListView.class);

		return gate_list;

	}

	/**
	 * 获取所有人脸识别的的考勤设置
	 * 
	 */
	public List<GateListView> getAllllycGate(String sn, String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sn", sn);
		paramMap.put("type", type);
		StringBuffer sql = new StringBuffer("SELECT * from gate_v_list where type_value= :type and sn = :sn");

		List<GateListView> gate_list = findList(sql, paramMap, GateListView.class);

		return gate_list;

	}

	/**
	 * 根据闸机编号获取单个闸机详情
	 * 
	 * @param id
	 * @return
	 */
	public GateList getGateListInfoBygateno(String gate_no) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("gate_no", gate_no);

		String sql = new String("SELECT * from gate_v_list where gate_no=:gate_no");
		GateList gate = get(sql, paramMap, GateList.class);
		return gate;

	}

	/**
	 * 根据闸机ip 和闸机端口 获取单个闸机详情
	 * 
	 * @param id
	 * @return
	 */
	public GateList getGateListInfoBygateip(String connect_ip, String connect_port) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("connect_ip", connect_ip);
		paramMap.put("connect_port", connect_port);

		String sql = new String(
				"SELECT * from gate_v_list where connect_ip=:connect_ip and connect_port=:connect_port");
		GateList gate = get(sql, paramMap, GateList.class);
		return gate;

	}

	/**
	 * 根据闸机com端口 和闸机通道号 获取单个闸机详情
	 * 
	 * @param id
	 * @return
	 */
	public GateList getGateListInfoBygateconnectid(String connect_port, String connect_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("connect_id", connect_id);
		paramMap.put("connect_port", connect_port);

		String sql = new String(
				"SELECT * from gate_v_list where connect_id=:connect_id and connect_port=:connect_port");
		GateList gate = get(sql, paramMap, GateList.class);
		return gate;

	}

	/**
	 * 通过闸机的COM号和对应的连接编号查询获取单个闸机详情
	 * 
	 * @param connect_port
	 * @param connect_id
	 * @return
	 */
	public GateListView getGateListByPortAndConnectId(String connect_port, String connect_id) {
		Map paramMap = new HashMap();
		paramMap.put("connect_port", connect_port);
		paramMap.put("connect_id", connect_id);

		String sql = new String(
				"SELECT * from gate_v_list where connect_port=:connect_port and connect_id=:connect_id");
		GateListView gate = get(sql, paramMap, GateListView.class);
		return gate;
	}

	/**
	 * 修改最后一次上报刷卡记录时间
	 * 
	 */
	public int updateLast_uploadrecord_time(String id, String last_uploadrecord_time) {
		StringBuffer sql = new StringBuffer("UPDATE gate_t_list");
		Map paramMap = new HashMap();
		paramMap.put("id", id);
		String temp_time = DateUtils.dateToTimestamp(last_uploadrecord_time);
		paramMap.put("last_queryrecord_time", temp_time);
		if (StringUtils.isNotEmpty(last_uploadrecord_time)) {
			sql.append(" set last_queryrecord_time='").append(temp_time).append("'");
		} else {
			sql.append("");
		}

		if (StringUtils.isNotEmpty(id)) {
			sql.append(" where id='").append(id).append("'");
		} else {
			sql.append(" where  1=1");
		}
		return execute(sql, paramMap);
	}

	/**
	 * 修改最后一次查询刷卡时间段时间
	 * 
	 */
	public int updateLast_queryrecord_time(String id, String last_queryrecord_time) {
		StringBuffer sql = new StringBuffer("UPDATE gate_t_list");
		Map paramMap = new HashMap();
		paramMap.put("id", id);
		String temp_time = DateUtils.dateToTimestamp(last_queryrecord_time);
		paramMap.put("last_queryrecord_time", temp_time);
		if (StringUtils.isNotEmpty(last_queryrecord_time)) {
			sql.append(" set last_queryrecord_time='").append(temp_time).append("'");
		} else {
			sql.append("");
		}

		if (StringUtils.isNotEmpty(id)) {
			sql.append(" where id='").append(id).append("'");
		} else {
			sql.append(" where  1=1");
		}
		return execute(sql, paramMap);
	}

	/**
	 * 查询闸机最后一次上报刷卡记录时间
	 * 
	 * @param id
	 * @return "2017-09-05 14:53:22"格式的时间
	 */
	public String getLast_uploadrecord_time(String id) {
		Map paramMap = new HashMap();
		paramMap.put("id", id);
		String sql = new String("SELECT * from gate_t_list where id=:id");
		GateList gate = get(sql, paramMap, GateList.class);
		return DateUtils.timestampToDate(gate.getLast_uploadrecord_time());
	}

	/**
	 * 查询闸机最后一次查询刷卡时间段时间
	 * 
	 * @param id
	 * @return "2017-09-05 14:53:22"格式的时间
	 */
	public String getLast_queryrecord_time(String id) {
		Map paramMap = new HashMap();
		paramMap.put("id", id);
		String sql = new String("SELECT * from gate_t_list where id=:id");
		GateList gate = get(sql, paramMap, GateList.class);
		return DateUtils.timestampToDate(gate.getLast_queryrecord_time());
	}

	/**
	 * 根据用户id，获取她有使用权限的闸机信息
	 * 
	 * @param id
	 * @return
	 */
	public List<GateList> getGateListbyuser_id(String user_id) {
		Map paramMap = new HashMap();
		paramMap.put("user_id", user_id);

		String sql = new String(
				"SELECT * from gate_v_list where id in(select gate_id from gate_t_user_cards_auth where user_id=:user_id)");
		List<GateList> gate = findList(sql, paramMap, GateList.class);
		return gate;

	}

	/**
	 * 获取所有正在使用的不重复的COM号
	 * 
	 * @return
	 */
	public List<String> getAllDiffLlycCOM() {
		String sql = "select distinct connect_port from gate_t_list where type='lanlyc' and connect_port like 'COM%'";
		List<String> comList = findListForColumn(sql, null, String.class);

		return comList;
	}

	/**
	 * 通过COM号获取闸机id列表
	 * 
	 * @param com
	 * @return
	 */
	public List<String> getAllIdByCOM(String com) {
		String sql = "select id from gate_t_list where connect_port=:connect_port";
		Map paramMap = new HashMap();
		paramMap.put("connect_port", com);
		List<String> idList = findListForColumn(sql, paramMap, String.class);

		return idList;
	}

	/**
	 * 通过COM号获取闸机编号列表
	 * 
	 * @param com
	 * @return
	 */
	public List<String> getAllNoByCOM(String com) {
		String sql = "select gate_no from gate_t_list where connect_port=:connect_port";
		Map paramMap = new HashMap();
		paramMap.put("connect_port", com);
		List<String> idList = findListForColumn(sql, paramMap, String.class);

		return idList;
	}

	// 根据控制板编号和通道号获取相应的设备
	public GateList getGalistBySnAndTondaoDao(String sn, String connect_id, String type) {
		String sql = "select * from gate_t_list where sn=:sn and connect_id=:connect_id and type = :type";
		Map paramMap = new HashMap();
		paramMap.put("sn", sn);
		paramMap.put("connect_id", connect_id);
		paramMap.put("type", type);
		List<GateList> gate = findList(sql, paramMap, GateList.class);
		if (gate.size() > 0) {
			return gate.get(0);
		} else {
			return null;
		}
	}

}
