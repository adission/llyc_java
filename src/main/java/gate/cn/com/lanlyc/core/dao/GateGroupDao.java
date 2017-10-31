package gate.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.dto.GateGroupDto;
import gate.cn.com.lanlyc.core.po.GateGroup;

@Service
public class GateGroupDao extends MySQLMapper<GateGroup> {
	/**
	 * 根据关键字模糊检索版本记录列表
	 * 
	 * @param page
	 *            page对象
	 * @param keywords
	 *            搜索关键字
	 * @param orderColumn
	 *            排序列
	 * @param orderDir
	 *            desc 或asc 排序升序或降序
	 * @return
	 */
	public Page<GateGroup> getGateGroupList(Page<GateGroup> page, String keywords, String orderColumn, String orderDir,
			String middle_sql) {
		StringBuffer sql = new StringBuffer("select id,group_name from gate_t_group");
		if ((middle_sql != null && middle_sql != "") || StringUtils.isNotEmpty(keywords)) {
			sql.append(" where( ");
		}

		if (middle_sql != null && middle_sql != "") {
			sql.append(middle_sql).append(" )");
		}

		if (((middle_sql != null && middle_sql != "")) && StringUtils.isNotEmpty(keywords)) {
			sql.append(" and ( ");
		}

		if (StringUtils.isNotEmpty(keywords)) {
			String val = StringEscapeUtils.escapeSql(keywords.toLowerCase());
			sql.append("   group_name LIKE '%").append(val)

					.append("%' )");

		}
		if (orderColumn != null && orderColumn != "" && orderDir != null && orderDir != "") {
			sql.append(" order by ").append(orderColumn).append(" ").append(orderDir);
		}
		Page<GateGroup> result = getPage(sql, null, page, GateGroup.class);
		return result;
	}

	/**
	 * 
	 * 
	 */
	public boolean add(GateGroup ga) {
		this.save(ga);
		return false;

	}

	// 进行分区名称是否重复的验证
	public GateGroup testGroupName(String group_name, String id) {
		StringBuffer sql = new StringBuffer("select * from gate_t_group where group_name=:group_name ");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("group_name", group_name);
		paramMap.put("id", id);
		if (id == null) {
			List<GateGroup> result = findList(sql, paramMap, GateGroup.class);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		} else {
			sql.append(" and id != :id");
			List<GateGroup> result = findList(sql, paramMap, GateGroup.class);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		}
	}
}
