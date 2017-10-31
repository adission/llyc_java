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
import gate.cn.com.lanlyc.core.dto.WorkersTypesDto;
import gate.cn.com.lanlyc.core.po.GateUserClass;
import gate.cn.com.lanlyc.core.po.ShowProjectPeopleCout;
import gate.cn.com.lanlyc.core.po.WorkersTypes;

@Service
public class WorkersTypesDao extends MySQLMapper<WorkersTypes> {

	/**
	 * 根据工种名称获取工种信息
	 * 
	 */
	public WorkersTypes getWorkersTypesinfoByname(String name) {

		Map paramMap = new HashMap();
		paramMap.put("name", name);
		String sql = new String("SELECT * from gate_t_workers_types where name=:name");

		WorkersTypes wtd = new WorkersTypes();
		wtd = get(sql, paramMap, WorkersTypes.class);
		return wtd;

	}

	/**
	 * 根据工种值获取工种信息
	 * 
	 */
	public WorkersTypes getWorkersTypesinfoByvalue(String value) {

		Map paramMap = new HashMap();
		paramMap.put("value", value);
		String sql = new String("SELECT * from gate_t_workers_types where value=:value");

		WorkersTypes wtd = new WorkersTypes();
		wtd = get(sql, paramMap, WorkersTypes.class);
		return wtd;

	}

	/**
	 * 根据工种，模糊查询相关的人员数据
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public Page<WorkersTypes> queryList(int current_page, int page_num, String name, String order, String order_by) {
		WorkersTypes GUC = new WorkersTypes();
		Map paramMap = new HashMap();
		Page<WorkersTypes> page = new Page<WorkersTypes>(page_num, current_page, 0);
		String sql1 = new String("SELECT * from gate_t_workers_types ");
		String sql2 = new String("where 1=1 ");
		String sql3 = new String("");
		String sql4 = new String("");
		String sql5 = new String("");

		if (name != "") {// 数据按相应的字段进行排序
			sql3 = new String("and name like '%" + name + "%' ");
		}

		if (order != "") {// 数据按相应的字段进行排序
			sql4 = new String("ORDER BY " + order + " ");
		} else {// 默认按name进行排序
			sql4 = new String("ORDER BY cast(order_by as SIGNED INTEGER),id  ");
		}
		if (order_by != "") {// 排序的是正序还是反序
			sql5 = new String(order_by);
		} else {// 默认是升序
			sql5 = new String("ASC");
		}
		String sql = sql1 + sql2 + sql3 + sql4 + sql5;
		Page<WorkersTypes> u1 = getPage(sql, paramMap, page, WorkersTypes.class);
		System.out.println(u1);
		if (u1 != null) {
			return u1;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据工种名称进行相应的查询，检测工种名称是否重复
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public List<WorkersTypes> worknametest(String name,String id) {
		if(id==null) {
			WorkersTypes GUC = new WorkersTypes();
			String sql = new String("SELECT * from gate_t_workers_types where name = \""+name+"\"");
			List<WorkersTypes> u1 = findList(sql, null,WorkersTypes.class);
			if(u1.size()!=0) 
				return u1;
			return null;
		}else {
			WorkersTypes GUC = new WorkersTypes();
			String sql = new String("SELECT * from gate_t_workers_types where name = \""+name+"\" and id !=\""+id+"\"" );
			List<WorkersTypes> u1 = findList(sql, null,WorkersTypes.class);
			if(u1.size()!=0) 
				return u1;
			return null;
		}
	}
	
	
	
	/**
	 * 获取所有的工种值，并将其拼接成相应的字符串
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public String AllworkStr() {
		//获取相应的对象并排序
		String sql = "select * from gate_t_workers_types ORDER BY order_by";
		Map paramMap = new HashMap();
		List<WorkersTypes> allwork = findList(sql,paramMap);
		//将相应的对象获取相应的值
		List<Map<String, Object>> wt=new ArrayList<Map<String, Object>>();
		for (WorkersTypes work : allwork) {
				Map<String ,Object > jsonMap = new HashMap< String , Object>();
				jsonMap.put("type_value",work.getValue());
				jsonMap.put("type_name",work.getName());
				jsonMap.put("type_count",0);
				wt.add(jsonMap);
			}
		String str = JSONObject.toJSONString(wt);
		return str;
	}
	
	
	/**
	 * 获传一个工种value值，和进出（0出，1进，），area的id
	 * 返回将要存的worker_type_person的值
	 * 
	 * @param user_name
	 *            检索关键字（闸机用户的名字）
	 * @return 场馆列表
	 */
	public String UpdaWorkStr(String value ,String cross,String area) {
		//获取相应的对象并排序
		String sql = "select * from gate_t_showproject_peoplecout where area=:area";
		Map paramMap = new HashMap();
		paramMap.put("area", area);
		
		List<ShowProjectPeopleCout> allwork = findList(sql,paramMap,ShowProjectPeopleCout.class);
		String  wkp = allwork.get(0).getWorker_type_person();
		JSONArray ja = JSONArray.parseArray(wkp);
		for (int i=0 ;i<ja.size();i++) {
			Map map = (Map)ja.get(i);
			String type_value = (String) map.get("type_value");
			if (type_value.equals(value)) {
				System.out.println(map.get("type_count"));
				int type_count = (Integer) map.get("type_count");
				if (cross.equals("1")) {
					type_count = type_count+1;
				}else if(cross.equals("0")) {
					type_count = type_count-1>=0?type_count-1:0;
				}
				Map<String ,Object > jsonMap = new HashMap< String , Object>();
				jsonMap.put("type_value",value);
				jsonMap.put("type_name",map.get("type_name"));
				jsonMap.put("type_count",type_count);
				ja.remove(i);
				ja.add(i, jsonMap);
			}
		}
		String str = JSONObject.toJSONString(ja);
		
		return str;
	}
	
	/**
	 * 获取相应的默认的人员工种的对象
	 */
	public WorkersTypes getDefaultWork() {
		Map paramMap = new HashMap();
		paramMap.put("name", "其他工种");
		String sql = new String("SELECT * from gate_t_workers_types where name = :name");
		List<WorkersTypes> u1 = findList(sql, paramMap,WorkersTypes.class);
		if(u1.size()!=0) 
			return u1.get(0);
		return null;
	
	}
	
}
