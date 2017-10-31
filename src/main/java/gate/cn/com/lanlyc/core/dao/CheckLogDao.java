package gate.cn.com.lanlyc.core.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.CheckLog;
import gate.cn.com.lanlyc.core.po.GateCheckLogView;
import gate.cn.com.lanlyc.core.po.GateUser;

@Service
public class CheckLogDao extends MySQLMapper<CheckLog> {
	/**
	 * 根据相关条件查询相应管理的日志
	 * 
	 * @param
	 * 
	 * @return 列表
	 */
	public Page<GateCheckLogView> queryCheckLog(Page<GateCheckLogView> page, Map<String, Object> paramMap) {

		StringBuffer sql = new StringBuffer("SELECT * from gate_v_check_log");
		sql.append(" where  1=1 ");
		if (paramMap.containsKey("name")) {
			String name = (String) paramMap.get("name");
			if (StringUtils.isNotEmpty(name)) {
				sql.append(" and  name LIKE '%").append(name).append("%' ");
				paramMap.remove("name");
			}

		}
		if (paramMap.containsKey("id_card")) {
			String id_card = (String) paramMap.get("id_card");
			if (StringUtils.isNotEmpty(id_card)) {
				sql.append(" and  id_card LIKE '%").append(id_card).append("%' ");
				paramMap.remove("id_card");
			}

		}
		if (paramMap.containsKey("mobile")) {
			String mobile = (String) paramMap.get("mobile");
			if (StringUtils.isNotEmpty(mobile)) {
				sql.append(" and  mobile LIKE '%").append(mobile).append("%' ");
				paramMap.remove("mobile");
			}

		}
		if (paramMap.containsKey("user_id")) {
			String user_id = (String) paramMap.get("user_id");
			if (StringUtils.isNotEmpty(user_id)) {
				sql.append(" and  user_id = '").append(user_id).append("' ");
				paramMap.remove("user_id");
			}

		}

		if (paramMap.containsKey("card_no")) {
			String card_no = (String) paramMap.get("card_no");
			if (StringUtils.isNotEmpty(card_no)) {
				sql.append(" and  card_no LIKE '%").append(card_no).append("%' ");
				paramMap.remove("card_no");
			}

		}
		if (paramMap.containsKey("start_time")) {
			String start_time = (String) paramMap.get("start_time");
			if (StringUtils.isNotEmpty(start_time)) {
				long start = this.getTimeStamp(start_time);
				sql.append(" and " + start + " <= unix_timestamp(check_date)");
				paramMap.remove("start_time");
			}

		}

		if (paramMap.containsKey("end_time")) {
			String end_time = (String) paramMap.get("end_time");
			if (StringUtils.isNotEmpty(end_time)) {
				long end = this.getTimeStamp(end_time);
				sql.append(" and " + end + " >= unix_timestamp(check_date)");
				paramMap.remove("end_time");
			}

		}
		System.out.println("sql:" + sql);
		Page<GateCheckLogView> result = getPage(sql, null, page, GateCheckLogView.class);
		return result;

	}

	/**
	 * 根据相关条件查询相应管理的日志Excel
	 * 
	 * @param
	 * 
	 * @return 列表
	 */
	public List<GateCheckLogView> queryCheckLogExcel(Map<String, Object> paramMap) {

		StringBuffer sql = new StringBuffer("SELECT * from gate_v_check_log");
		sql.append(" where  1=1 ");
		if (paramMap.containsKey("name")) {
			String name = (String) paramMap.get("name");
			if (StringUtils.isNotEmpty(name)) {
				sql.append(" and  name LIKE '%").append(name).append("%' ");
				paramMap.remove("name");
			}

		}
		if (paramMap.containsKey("id_card")) {
			String id_card = (String) paramMap.get("id_card");
			if (StringUtils.isNotEmpty(id_card)) {
				sql.append(" and  id_card LIKE '%").append(id_card).append("%' ");
				paramMap.remove("id_card");
			}

		}
		if (paramMap.containsKey("mobile")) {
			String mobile = (String) paramMap.get("mobile");
			if (StringUtils.isNotEmpty(mobile)) {
				sql.append(" and  mobile LIKE '%").append(mobile).append("%' ");
				paramMap.remove("mobile");
			}

		}
		if (paramMap.containsKey("user_id")) {
			String user_id = (String) paramMap.get("user_id");
			if (StringUtils.isNotEmpty(user_id)) {
				sql.append(" and  user_id = '").append(user_id).append("' ");
				paramMap.remove("user_id");
			}

		}

		if (paramMap.containsKey("card_no")) {
			String card_no = (String) paramMap.get("card_no");
			if (StringUtils.isNotEmpty(card_no)) {
				sql.append(" and  card_no LIKE '%").append(card_no).append("%' ");
				paramMap.remove("card_no");
			}

		}
		if (paramMap.containsKey("start_time")) {
			String start_time = (String) paramMap.get("start_time");
			if (StringUtils.isNotEmpty(start_time)) {
				long start = this.getTimeStamp(start_time);
				sql.append(" and " + start + " <= unix_timestamp(check_date)");
				paramMap.remove("start_time");
			}

		}

		if (paramMap.containsKey("end_time")) {
			String end_time = (String) paramMap.get("end_time");
			if (StringUtils.isNotEmpty(end_time)) {
				long end = this.getTimeStamp(end_time);
				sql.append(" and " + end + " >= unix_timestamp(check_date)");
				paramMap.remove("end_time");
			}

		}
		System.out.println("sql:" + sql + "order by check_date DESC");
		List<GateCheckLogView> result = findList(sql, null, GateCheckLogView.class);
		return result;

	}

	/**
	 * 将时间字符串转化成时间戳
	 * 
	 * @param date
	 *            时间字符串
	 * @return 时间戳
	 */
	public long getTimeStamp(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		long l = 0;
		try {
			l = df.parse(date).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l;
	}

	/**
	 * 传用户id、闸机id、进出（1进、0出）
	 * 
	 * 返回Boolean常量(true代表的是是第一次，可以对大屏人数进行操作)（false代表的是不是第一次，大屏人数不会发生变化，但是最后一个人，要存相应的人员id）
	 * 
	 * @param date
	 *            时间字符串
	 * @return 时间戳
	 */
	public Boolean testFirst(String user_id, String group_id) {
		// 获取该人当天（该区域）的所有的打卡记录（最后一条是记录）（按时间进行排序）
		// 如果只有一条记录，就进行操作
		// 如果大于两条记录，就取最后两条记录
		// 如果两条记录的方向相同，返回false
		// 如果两条记录的方向相反，返回true
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		String time = df.format(new Date());

		StringBuffer sql = new StringBuffer("SELECT * from gate_t_check_log where check_date like '%" + time
				+ "%' and gate_no in (select gate_no from gate_t_list where group_id = \"" + group_id
				+ "\") and ( card_no in (select user_auth_card from gate_t_user_cards where user_id = \"" + user_id
				+ "\") or id_card in (select cid from gate_t_user where id = \"" + user_id
				+ "\")) order by check_date DESC");

		List<CheckLog> cl = findList(sql, null, CheckLog.class);

		if (cl.size() == 1) {
			return true;
		} else {
			String cross1 = cl.get(0).getCross_flag();
			String cross2 = cl.get(1).getCross_flag();
			if (cross1.equals(cross2)) {
				return false;
			} else {
				return true;
			}

		}

	}

	/**
	 * 传用户id、区域id
	 * 
	 * 返回String 1，代表的是进，0代表的出
	 * 
	 * @param date
	 *            时间字符串
	 * @return 时间戳
	 */
	public String ChangCross(String user_id, String group_id, String date) {
		// 获取该人当天（该区域）的所有的打卡记录（最后一条是记录）（按时间进行排序）
		// 如果只有一条记录，就进行操作
		// 如果大于两条记录，就取最后两条记录
		// 如果两条记录的方向相同，返回false
		// 如果两条记录的方向相反，返回true
		String sql = "";
		if (date == null || date.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			String time = df.format(new Date());

			sql = "SELECT * from gate_t_check_log where check_date like '%" + time
					+ "%' and gate_no in (select gate_no from gate_t_list where group_id = \"" + group_id
					+ "\") and ( card_no in (select user_auth_card from gate_t_user_cards where user_id = \"" + user_id
					+ "\") or id_card in (select cid from gate_t_user where id = \"" + user_id
					+ "\")) order by check_date DESC LIMIT 0,1";
		} else {
			sql = "SELECT * from gate_t_check_log where check_date like '%" + date
					+ "%' and gate_no in (select gate_no from gate_t_list where group_id = \"" + group_id
					+ "\") and ( card_no in (select user_auth_card from gate_t_user_cards where user_id = \"" + user_id
					+ "\") or id_card in (select cid from gate_t_user where id = \"" + user_id
					+ "\")) order by check_date DESC LIMIT 0,1";
		}

		List<CheckLog> cl = findList(sql, null, CheckLog.class);

		if (cl.size() == 0) {
			return "1";
		} else {
			String cross = cl.get(0).getCross_flag();
			if (cross.equals("1")) {
				return "0";
			} else {
				return "1";
			}
		}

	}

	/**
	 * 传用户id、区域id
	 * 
	 * 返回String 1，代表的是进，0代表的出
	 * 
	 * @param date
	 *            时间字符串
	 * @return 时间戳
	 */
	public String queryCross(String user_id, String group_id) {
		// 获取该人当天（该区域）的所有的打卡记录（最后一条是记录）（按时间进行排序）
		// 如果只有一条记录，就进行操作
		// 如果大于两条记录，就取最后两条记录
		// 如果两条记录的方向相同，返回false
		// 如果两条记录的方向相反，返回true
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		String time = df.format(new Date());

		StringBuffer sql = new StringBuffer("SELECT * from gate_t_check_log where check_date like '%" + time
				+ "%' and gate_no in (select gate_no from gate_t_list where group_id = \"" + group_id
				+ "\") and ( card_no in (select user_auth_card from gate_t_user_cards where user_id = \"" + user_id
				+ "\") or id_card in (select cid from gate_t_user where id = \"" + user_id
				+ "\")) order by check_date DESC LIMIT 0,1");

		List<CheckLog> cl = findList(sql, null, CheckLog.class);

		if (cl.size() == 0) {
			return "nodata";// 没有数据
		} else {
			String cross = cl.get(0).getCross_flag();
			if (cross.equals("1")) {
				return "1";
			} else {
				return "0";
			}
		}

	}

}
