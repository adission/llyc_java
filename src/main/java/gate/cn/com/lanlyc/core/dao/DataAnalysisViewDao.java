package gate.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import gate.cn.com.lanlyc.core.po.GateUserClassDataAnalysisView;
import gate.cn.com.lanlyc.core.po.GateWorktypsDataAnalysisView;
@Service
public class DataAnalysisViewDao extends MySQLMapper<GateUserClassDataAnalysisView>{

	/**
	 * 获取所有人员分类的人员总数
	 * 
	 * 
	 */
	public List<GateUserClassDataAnalysisView> getUserClass_totalperson()
	{
		
		String sql = new String("SELECT * from gate_v_userclassdataanalysis");
		List<GateUserClassDataAnalysisView> resuult=findList(sql, null);
		return resuult;
	}
	
	/**
	 * 获取所有工种的的人员总数
	 * 
	 * 
	 */
	public List<GateWorktypsDataAnalysisView> getWorktyps_totalperson()
	{
		
		String sql = new String("SELECT * from gate_v_worktypsdataanalysis");
		List<GateWorktypsDataAnalysisView> result=findList(sql, null,GateWorktypsDataAnalysisView.class);
		return result;
	}
}
