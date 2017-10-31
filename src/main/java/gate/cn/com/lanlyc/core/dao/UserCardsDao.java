package gate.cn.com.lanlyc.core.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.UserCards;

@Service
public class UserCardsDao extends MySQLMapper<UserCards> {

	public Page<UserCards> queryList(int current_page, int page_num, String user_name, String gate_type, String is_tmp,
			String start_time, String end_time) throws ParseException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("gate_type", gate_type);
		paramMap.put("is_tmp", is_tmp);
		Page<UserCards> page = new Page<UserCards>(page_num, current_page, 0);
		int time_start = 0;
		int time_end = 0;
		if (!start_time.equals("")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = format.parse(start_time);
			time_start = (int) date.getTime();
		}
		if (!end_time.equals("")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = format.parse(end_time);
			time_end = (int) date.getTime();
		}
		paramMap.put("time_start", time_start);
		paramMap.put("time_end", time_end);
		String sql1 = new String("SELECT * from gate_t_user_cards ");
		String sql2 = new String("where 1=1 ");
		String sql3 = !user_name.equals("")
				? new String("and user_id in (select id from gate_t_user where name like '%" + user_name + "%') ")
				: "";
		String sql4 = !gate_type.equals("") ? new String("and type=:gate_type  ") : "";
		String sql5 = time_start != 0 ? new String("and end_time >= :time_start  ") : "";
		String sql6 = time_end != 0 ? new String("and end_time <= :time_end  ") : "";
		String sql7 = is_tmp != "" ? new String("and is_tmp=:is_tmp order_by user_id,id ") : "";
		String sql = sql1 + sql2 + sql3 + sql4 + sql5 + sql6 + sql7;
		Page<UserCards> userauths = getPage(sql, paramMap, page, UserCards.class);
		if (userauths != null) {
			return userauths;
		} else {
			return null;
		}
	}

	public List<GateUserAuth> query_del(String id_split) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id_split", id_split);
		String sql = new String(
				"SELECT * from gate_t_user_cards_auth where user_id in (select user_id in gate_t_user_cards where id=:id_split) and gate_id in (select id from gate_t_list where type in (select type from gate_t_user_cards where id =: id_split))");
		List<GateUserAuth> userauths = findList(sql, paramMap, GateUserAuth.class);
		if (userauths != null) {
			return userauths;
		} else {
			return null;
		}
	}

	public List<GateList> getGateListInfo(String user_id, String type) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		paramMap.put("type", type);
		String sql = new String(
				"SELECT * from gate_t_list where id in (select gate_id in gate_t_user_cards_auth where user_id = :user_id and gate_id in (select id from gate_t_list where type=:type))");
		List<GateList> userauths = findList(sql, paramMap, GateList.class);
		if (userauths != null) {
			return userauths;
		} else {
			return null;
		}
	}

	// 根据相应的card_id来对相应的数据表中的数据进行相应的查询
	public List<GateUserAuth> card_test(String id, String type) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		paramMap.put("type", type);
		String sql = new String("SELECT * from gate_t_user_cards where type=:type and user_auth_card =:id");
		List<GateUserAuth> userauths = findList(sql, paramMap, GateUserAuth.class);
		if (userauths != null && userauths.size() > 0) {
			return userauths;
		} else {
			return null;
		}
	}

	// 根据相应的card_id来对相应的数据表中的数据进行相应的查询
	public List<UserCards> getByType(String type) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("type", type);
		String sql = new String("SELECT * from gate_t_user_cards where type=:type");
		List<UserCards> userauths = findList(sql, paramMap, UserCards.class);
		if (userauths != null && userauths.size() > 0) {
			return userauths;
		} else {
			return null;
		}
	}

	/**
	 * 通过用户考勤编号获取UserCards记录
	 * 
	 * @param user_auth_card
	 * @return
	 */
	public UserCards getUserCardByUserAuthCard(String user_auth_card) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_auth_card", user_auth_card);
		String sql = "SELECT * from gate_t_user_cards where user_auth_card=:user_auth_card";

		return get(sql, paramMap, UserCards.class);
	}

	/**
	 * 通过user_id查询符合条件的记录列表
	 * 
	 * @param user_id
	 * @return
	 */
	public List<UserCards> getUserCardsByUserid(String user_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		String sql = "SELECT * from gate_t_user_cards where user_id=:user_id";

		return findList(sql, paramMap, UserCards.class);
	}

	/**
	 * 通过user_id查询符合条件的记录列表
	 * 
	 * @param user_id
	 * @return
	 */
	public List<UserCards> getUserCardsByUserids(String user_ids) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_ids);
		String sql = "SELECT * from gate_t_user_cards where user_id in (:user_id)";

		return findList(sql, paramMap, UserCards.class);
	}

	/**
	 * 通过用户考勤编号获取UserCards记录
	 * 
	 * @param user_auth_card
	 * @return
	 */
	public UserCards getUserCarduser_id_type(String user_id, String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		paramMap.put("type", type);
		String sql = "SELECT * from gate_t_user_cards where user_id=:user_id and type=:type";

		return get(sql, paramMap, UserCards.class);
	}

	/**
	 * 通过用户id找到相应权限列表
	 * 
	 * @param user_auth_card
	 * @return
	 */
	public List<UserCards> getUserCarduser_id(String user_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		String sql = "SELECT * from gate_t_user_cards where user_id=:user_id";

		List<UserCards> uclist = findList(sql, paramMap, UserCards.class);
		System.out.println(uclist);
		if (uclist != null && uclist.size() > 0) {
			return uclist;
		} else {
			return null;
		}
	}

	/**
	 * 通过user_id删除gate_t_user_cards记录
	 * 
	 * @param user_id
	 * @return
	 */
	public int delUserCardByUserID(String user_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		String sql = "delete from gate_t_user_cards where user_id=:user_id";

		CharSequence s = sql;
		return this.execute(s, paramMap);
	}

	public List<GateUserAuth> query_del_user_id(String user_id) {
		String sql = new String("SELECT * from gate_t_user_cards_auth where user_id = user_id");
		List<GateUserAuth> userauths = findList(sql, null, GateUserAuth.class);
		return userauths;
	}

	/**
	 * 根据user_id、user_auth_card、type判断gate_t_user_cards记录是否存在
	 * 
	 * @param user_id
	 * @param user_auth_card
	 * @param type
	 * @return
	 */
	public boolean isExistQueryByUSerIDAndCardAndType(String user_id, String user_auth_card, String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		paramMap.put("user_auth_card", user_auth_card);
		paramMap.put("type", type);
		String sql = "SELECT * from gate_t_user_cards where user_id =:user_id and user_auth_card=:user_auth_card and type=:type";

		List<UserCards> list = findList(sql, paramMap, UserCards.class);

		if (null == list || 0 == list.size())
			return false;
		return true;
	}

	// 根据时间获取相应的权限
	public List<UserCards> getUserCardsByTime(long currentTime) {
		// TODO Auto-generated method stub
		String sql = "SELECT * from gate_t_user_cards where is_tmp = \"1\" and end_time is not NULL and end_time !='' and cast(end_time as unsigned int)<"
				+ currentTime + "";

		List<UserCards> list = findList(sql, null, UserCards.class);

		if (null != list && 0 != list.size())
			return list;
		return null;
	}

}