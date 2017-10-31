package gate.cn.com.lanlyc.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import gate.cn.com.lanlyc.core.po.Setting;

@Service
public class SettingDao extends MySQLMapper<Setting>{

	public Setting getOneObject() 
	{
		List<Setting> results=new ArrayList<Setting>();
		String sql = new String("SELECT * from gate_t_setting");
		
		results=getPage(sql,null,0,1,Setting.class);
		return results.get(0);
	}
}
