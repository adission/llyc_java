package gate.cn.com.lanlyc.core.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.GateOperationlogView;
import gate.cn.com.lanlyc.core.po.OperationLog;

@Service
public class OperationLogDao extends MySQLMapper<OperationLog> {

	/**
	 * 根据相关条件查询相应管理的日志
	 * 
	 * @param
	 * 
	 * @return
	 */
	public Page<GateOperationlogView> queryList(Page<GateOperationlogView> page, Map paramMap, String order,
			String order_by) {
		GateOperationlogView u = new GateOperationlogView();

		StringBuffer sql = new StringBuffer("SELECT * from gate_v_operation_user ");
		sql.append(" where 1=1 ");
		if (paramMap.containsKey("start_time")) {
			String start_time = (String) paramMap.get("start_time");
			if (StringUtils.isNotEmpty(start_time)) {
				long start = this.getTimeStamp(start_time);
				sql.append(" and " + start + " <= unix_timestamp(operation_time)");
				paramMap.remove("start_time");
			}

		}

		if (paramMap.containsKey("end_time")) {
			String end_time = (String) paramMap.get("end_time");
			if (StringUtils.isNotEmpty(end_time)) {
				long end = this.getTimeStamp(end_time);
				sql.append(" and " + end + " >= unix_timestamp(operation_time)");
				paramMap.remove("end_time");
			}

		}
		
		if (paramMap.containsKey("operation_time")) {
			String operation_time = (String) paramMap.get("operation_time");
			if (StringUtils.isNotEmpty(operation_time)) {
				sql.append(" and operation_time=:operation_time ");
			}
			paramMap.remove("operation_time");
		}
		if (paramMap.containsKey("operation_desc")) {
			String operation_desc = (String) paramMap.get("operation_desc");
			if (StringUtils.isNotEmpty(operation_desc)) {
				sql.append(" and operation_desc='").append(operation_desc).append("'");
			}
			paramMap.remove("operation_desc");
		}
		if (paramMap.containsKey("operation_user")) {
			String operation_user = (String) paramMap.get("operation_user");
			if (StringUtils.isNotEmpty(operation_user)) {
				sql.append(" and user_name='").append(operation_user).append("'");
			}
			paramMap.remove("operation_user");
		}
		if (paramMap.containsKey("operation_action")) {
			String operation_action = (String) paramMap.get("operation_action");
			if (StringUtils.isNotEmpty(operation_action)) {
				sql.append(" and operation_action='").append(operation_action).append("'");
			}
			paramMap.remove("operation_action");
		}

		// 以下是进行相应的数据排序
		// String order,//排序方式（进行相应的字段排序）
		// String order_by//排序是正序还是反序（）
		if (order != null) {
			sql.append(" ORDER BY " + order + " ");

		} else {
			sql.append(" ORDER BY operation_time DESC ");
		}
		if (order_by != null) {
			sql.append(order_by);

		}

		Page<GateOperationlogView> u1 = getPage(sql, paramMap, page, GateOperationlogView.class);
		System.out.println(u1);
		if (u1 != null) {
			return u1;
		} else {
			return null;
		}
	}

	/**
	 * 根据相关条件查询相应管理的日志Excel
	 * 
	 * 
	 * @param
	 * 
	 * @return
	 */
	public List<GateOperationlogView> queryListExcel(Map paramMap, String order, String order_by) {
		GateOperationlogView u = new GateOperationlogView();

		StringBuffer sql = new StringBuffer("SELECT * from gate_v_operation_user ");
		sql.append(" where 1=1 ");
		
		
		
		if (paramMap.containsKey("operation_time")) {
			String operation_time = (String) paramMap.get("operation_time");
			if (StringUtils.isNotEmpty(operation_time)) {
				sql.append(" and operation_time=:operation_time ");
			}
			paramMap.remove("operation_time");
		}
		if (paramMap.containsKey("operation_desc")) {
			String operation_desc = (String) paramMap.get("operation_desc");
			if (StringUtils.isNotEmpty(operation_desc)) {
				sql.append(" and operation_desc=:operation_desc ");
			}
			paramMap.remove("operation_desc");
		}
		if (paramMap.containsKey("operation_time")) {
			String operation_user = (String) paramMap.get("operation_user");
			if (StringUtils.isNotEmpty(operation_user)) {
				sql.append(" and operation_user=:operation_user ");
			}
			paramMap.remove("operation_user");
		}
		if (paramMap.containsKey("operation_action")) {
			String operation_action = (String) paramMap.get("operation_action");
			if (StringUtils.isNotEmpty(operation_action)) {
				sql.append(" and operation_action=:operation_action ");
			}
			paramMap.remove("operation_action");
		}

		// 以下是进行相应的数据排序
		// String order,//排序方式（进行相应的字段排序）
		// String order_by//排序是正序还是反序（）
		if (order != null) {
			sql.append(" ORDER BY " + order + " ");
			if (order_by != null) {
				sql.append(order_by);
			}
		} else {
			sql.append(" ORDER BY operation_time DESC ");
		}

		List<GateOperationlogView> u1 = findList(sql, paramMap, GateOperationlogView.class);

		return u1;
	}
	
	/**
	 * 将时间字符串转化成时间戳
	 * 
	 * @param date
	 *            时间字符串
	 * @return 时间戳
	 */
	public long getTimeStamp(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		long l = 0;
		try {
			l = df.parse(date).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l;
	}

}
