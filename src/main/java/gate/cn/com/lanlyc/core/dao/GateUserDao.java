package gate.cn.com.lanlyc.core.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.dto.GateUserDto;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;

@Service
public class GateUserDao extends MySQLMapper<GateUser> {

	/**
	 * 根据用户名，模糊查询相关的人员数据
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public Page<GateUserInfoView> getAllbyName(int current_page, // Integer.parseInt(str) String转为int的数据格式
			int page_num, // 页面数据
			String name, // 名称（进行模糊查询）
			String cid, // 身份证（进行模糊查询）
			String mobile, // 手机号（进行模糊查询）
			String type, // 人员类别（进行条件查询）
			String sate, // 开卡状态（进行条件查询）
			String age, // 年龄（进行条件查询）
			String gender, // 性别（进行条件查询）
			String card_type, // 卡类型（进行条件查询）
			String card_id, // 卡类型（进行条件查询）
			String workers_type, // 工种（进行条件查询）
			String effective_time, // 有效期（进行条件查询）
			String order, // 排序方式（进行相应的字段排序）
			String order_by, // 排序是正序还是反序（）
			String icUser, // 代表的是相应的 ,如果传参数，返回的的所有的
			String gate_id) {
		GateUserInfoView u = new GateUserInfoView();
		Map paramMap = new HashMap();
		paramMap.put("type", type);
		paramMap.put("sate", sate);
		paramMap.put("age", age);
		paramMap.put("gender", gender);
		paramMap.put("card_type", card_type);
		paramMap.put("user_card", card_id);
		paramMap.put("workers_type", workers_type);
		paramMap.put("effective_time", effective_time);
		paramMap.put("gate_id", gate_id);
		Page<GateUserInfoView> page = new Page<GateUserInfoView>(page_num, current_page, 0);
		StringBuffer sql1 = new StringBuffer("SELECT * from gate_v_user_info ");
		sql1.append("where 1=1 ");

		if (StringUtils.isNotEmpty(type)) {// 人员类别
			sql1.append("and type=:type ");
		}
		if (StringUtils.isNotEmpty(sate)) {// 开卡状态
			sql1.append("and sate=:sate ");
		}
		if (StringUtils.isNotEmpty(age)) {// 年龄
			sql1.append("and age=:age ");
		}
		if (StringUtils.isNotEmpty(gender)) {// 性别
			sql1.append("and gender=:gender ");
		}
		if (StringUtils.isNotEmpty(card_type)) {// 开卡类型
			sql1.append("and card_type=:card_type ");
		}
		if (StringUtils.isNotEmpty(card_id)) {// 卡号
			sql1.append("and user_card like '%" + card_id + "%' ");
		}

		if (icUser != null) {// 如果传相应的参数，就返回有ic卡权限的人员
			sql1.append("and user_card IS NOT NULL and user_card!=\"\"");
		}

		if (gate_id != null && (!gate_id.equals(""))) {// 这个传入的有权先的人员列表
			sql1.append("and id not in (select user_id from gate_t_user_cards_auth where gate_id =:gate_id)");
		}

		if (StringUtils.isNotEmpty(workers_type)) {// 工种
			sql1.append("and workers_type=:workers_type ");
		}
		if (StringUtils.isNotEmpty(effective_time)) {// 有效期
			sql1.append("and effective_time=:effective_time ");
		}
		// String name,//名称（进行模糊查询）
		// String cid,//身份证（进行模糊查询）
		// String mobile,//手机号（进行模糊查询）
		// String card_id,//卡号（进行模糊查询）
		if (StringUtils.isNotEmpty(name)) {// 姓名
			sql1.append("and name like '%" + name + "%' ");
		}
		if (StringUtils.isNotEmpty(cid)) {// 身份证
			sql1.append("and cid like '%" + cid + "%' ");
		}
		if (StringUtils.isNotEmpty(mobile)) {// 手机号
			sql1.append("and mobile like '%" + mobile + "%' ");
		}
		// 以下是进行相应的数据排序
		// String order,//排序方式（进行相应的字段排序）
		// String order_by//排序是正序还是反序（）
		if (order != null) {
			sql1.append("ORDER BY " + order + " ");
		} else {
			sql1.append("ORDER BY gonghao");
		}
		if (order_by != null) {
			sql1.append(order_by);
		}

		Page<GateUserInfoView> u1 = getPage(sql1, paramMap, page, GateUserInfoView.class);
		// System.out.println("Dao满足条件的记录数："+u1.getPageSize());
		if (u1 != null) {
			return u1;
		} else {
			return null;
		}
	}

	/**
	 * 根据用户名，模糊查询相关的人员数据
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public List<GateUserInfoView> getAllbyNameExcel(String name, // 名称（进行模糊查询）
			String cid, // 身份证（进行模糊查询）
			String mobile, // 手机号（进行模糊查询）
			String type, // 人员类别（进行条件查询）
			String sate, // 开卡状态（进行条件查询）
			String age, // 年龄（进行条件查询）
			String gender, // 性别（进行条件查询）
			String card_type, // 卡类型（进行条件查询）
			String workers_type, // 工种（进行条件查询）
			String effective_time, // 有效期（进行条件查询）
			String order, // 排序方式（进行相应的字段排序）
			String order_by// 排序是正序还是反序（）
	) {
		GateUserInfoView u = new GateUserInfoView();
		Map paramMap = new HashMap();
		paramMap.put("type", type);
		paramMap.put("sate", sate);
		paramMap.put("age", age);
		paramMap.put("gender", gender);
		paramMap.put("card_type", card_type);
		paramMap.put("workers_type", workers_type);
		paramMap.put("effective_time", effective_time);
		String sql1 = new String("SELECT * from gate_v_user_info ");
		String sql2 = new String("where 1=1 ");
		String sql3 = new String("");
		String sql4 = new String("");
		String sql5 = new String("");
		String sql6 = new String("");
		String sql7 = new String("");
		String sql8 = new String("");
		String sql9 = new String("");
		String sql10 = new String("");
		String sql11 = new String("");
		String sql12 = new String("");
		String sql13 = new String("");
		String sql14 = new String("");
		String sql15 = new String("");
		if (type != null) {// 人员类别
			sql3 = new String("and type=:type ");
		}
		if (sate != null) {// 开卡状态
			sql4 = new String("and sate=:sate ");
		}
		if (age != null) {// 年龄
			sql5 = new String("and age=:age ");
		}
		if (gender != null) {// 性别
			sql6 = new String("and gender=:gender ");
		}
		if (card_type != null) {// 开卡类型
			sql7 = new String("and card_type=:card_type ");
		}
		if (workers_type != null) {// 工种
			sql8 = new String("and workers_type=:workers_type ");
		}
		if (effective_time != null) {// 有效期
			sql9 = new String("and effective_time=:effective_time ");
		}
		// String name,//名称（进行模糊查询）
		// String cid,//身份证（进行模糊查询）
		// String mobile,//手机号（进行模糊查询）
		// String card_id,//卡号（进行模糊查询）
		if (name != null) {// 姓名
			sql10 = new String("and name like '%" + name + "%' ");
		}
		if (cid != null) {// 身份证
			sql11 = new String("and cid like '%" + cid + "%' ");
		}
		if (mobile != null) {// 手机号
			sql12 = new String("and mobile like '%" + mobile + "%' ");
		}
		// 以下是进行相应的数据排序
		// String order,//排序方式（进行相应的字段排序）
		// String order_by//排序是正序还是反序（）
		if (order != null) {
			sql14 = new String("ORDER BY " + order + " ");
		} else {
			sql14 = new String("ORDER BY gonghao");
		}
		if (order_by != null) {
			sql15 = new String(order_by);
		}
		String sql = sql1 + sql2 + sql3 + sql4 + sql5 + sql6 + sql7 + sql8 + sql9 + sql10 + sql11 + sql12 + sql13
				+ sql14 + sql15;
		List<GateUserInfoView> u1 = findList(sql, paramMap, GateUserInfoView.class);
		// System.out.println("Dao满足条件的记录数："+u1.getPageSize());
		if (u1 != null) {
			return u1;
		} else {
			return null;
		}
	}

	public List<GateUser> GetGateUser(String name) {
		List<GateUser> gateuser = new ArrayList<GateUser>();
		String sql = "select * from gate_t_user where name like '%:name%'";
		Map paramMap = new HashMap();
		paramMap.put("name", name);

		gateuser = this.getPage(sql, paramMap, 0, 1);
		return gateuser;
	}

	/**
	 * 根据手机号码，或者身份证 进行 验证手机号码是否重复
	 * 
	 * @param mobile
	 *            检索关键字（手机号码）
	 * @param user_id
	 * @return
	 */
	public String getGateuserInfoByMobileandcid(String mobile, String cid, String user_id) {
		GateUser u = new GateUser();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (mobile != null && user_id == null) {
			paramMap.put("mobile", mobile);
			String sql = new String("SELECT * from gate_t_user where mobile=:mobile");
			GateUserDto u1 = get(sql, paramMap, GateUserDto.class);
			if (u1 != null) {
				return u1.getId();
			} else {
				return null;
			}
		} else if (cid != null && user_id == null) {
			paramMap.put("cid", cid);
			String sql = new String("SELECT * from gate_t_user where cid=:cid");
			GateUserDto u1 = get(sql, paramMap, GateUserDto.class);
			System.out.println(u1);
			if (u1 != null) {
				return u1.getId();
			} else {
				return null;
			}
		} else if (mobile != null && user_id != null) {
			paramMap.put("mobile", mobile);
			String sql = new String("SELECT * from gate_t_user where mobile=:mobile and id!=\"" + user_id + "\"");
			GateUserDto u1 = get(sql, paramMap, GateUserDto.class);
			if (u1 != null) {
				return u1.getId();
			} else {
				return null;
			}
		} else if (cid != null && user_id != null) {
			paramMap.put("cid", cid);
			String sql = new String("SELECT * from gate_t_user where cid=:cid and id!=\"" + user_id + "\"");
			GateUserDto u1 = get(sql, paramMap, GateUserDto.class);
			System.out.println(u1);
			if (u1 != null) {
				return u1.getId();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 根据id查询人员实体
	 * 
	 * @param id
	 * @return
	 */
	public GateUser getUserById(String id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		String sql = new String("SELECT * from gate_t_user where id=:id");
		return get(sql, paramMap, GateUser.class);
	}

	/**
	 * 根据id查询人员实体
	 * 
	 * @param id
	 * @return
	 */
	public GateUserInfoView getUserByuserId(String id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		String sql = new String("SELECT * from gate_v_user_info where id=:id");
		return get(sql, paramMap, GateUserInfoView.class);
	}

	/**
	 * 根据人员的身份证 获取人员对象
	 * 
	 * @param card_id
	 * 
	 * @return
	 */
	public GateUser getGateuserbycid(String cid) {
		GateUser u = new GateUser();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cid", cid);
		String sql = new String("SELECT * from gate_t_user where cid=:cid");
		GateUserDto u1 = get(sql, paramMap, GateUserDto.class);
		System.out.println(u1);
		if (u1 != null) {
			return u1;
		} else {
			return null;
		}
	}

	/**
	 * 根据相应的card_id 获取人员对象
	 * 
	 * @param card_id
	 * 
	 * @return
	 */
	public GateUser getGateuserbycard_id(int card_id, String type) {
		GateUser u = new GateUser();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("card_id", card_id);
		String sql = "";
		if (type.equals("hanwang")) {
			sql = new String("SELECT * from gate_t_user where gonghao=:card_id");
		} else if (type.equals("weigeng")) {
			paramMap.put("type", "4");
			sql = new String(
					"SELECT * from gate_t_user WHERE id =(SELECT user_id from gate_t_user_cards where user_auth_card=:card_id and type=:type)");
		}
		GateUserDto u1 = get(sql, paramMap, GateUserDto.class);
		System.out.println(u1);
		if (u1 != null) {
			return u1;
		} else {
			return null;
		}
	}

	/**
	 * 根据相应的id 修改人员的开卡状态
	 * 
	 * @param id
	 * @param sate
	 * 
	 * @return 修改的数量
	 */
	public int updateSate(String id, String sate) {
		String sql = "UPDATE gate_t_user set sate=:sate where id=:id";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		paramMap.put("sate", sate);
		int num = execute(sql, paramMap);
		return num;
	}

	// 将相应的人员的工种对应的value进行相应的修改
	// 获取所有的相应的人员的
	// work_type_id 是相应的工种的id
	public Boolean changeUserWorkTypeToDefault(String work_type_id, String default_value) {
		// TODO Auto-generated method stub
		String sql = "UPDATE gate_t_user set workers_type=:workers_type where workers_type = (select value from gate_t_workers_types where id=:work_type_id)";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("work_type_id", work_type_id);
		paramMap.put("workers_type", default_value);
		int num = execute(sql, paramMap);
		return true;
	}

	// 将相应的人员类别修改为相应的默认值
	// 获取所有的相应的人员的
	// user_class_id 是相应需要被删除的的相应的人员类别id
	// default_class_id 默认的人员类别的
	public Boolean changeUserClassToDefault(String user_class_id, String default_class_id) {
		// TODO Auto-generated method stub
		String sql = "UPDATE gate_t_user set user_class_id =:default_class_id where user_class_id =:user_class_id";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_class_id", user_class_id);
		paramMap.put("default_class_id", default_class_id);
		int num = execute(sql, paramMap);
		return true;
	}
}
