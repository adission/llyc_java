package pos.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import pos.cn.com.lanlyc.core.po.PosBaseStation;
import pos.cn.com.lanlyc.core.po.PosConstructionSite;
@Service
public class PosConstructionSiteDao extends MySQLMapper<PosConstructionSite>{
	/*
	 * 查询项目信息
	 */
	public List<PosConstructionSite> getConstructionSite(String id) {
		StringBuffer sql=new StringBuffer("select * from pos_t_constructionSite ");		
		System.out.println(id);
		Map<String, Object> paramMap = new HashMap<>();
		if(id!=null&&!"".equals(id)) {
			sql.append(" where id=:id ");
			paramMap.put("id", id);
		}		
	    System.out.println(sql);
		List<PosConstructionSite> result = findList(sql, paramMap, PosConstructionSite.class);	
		return result;

	}
}
