package video.cn.com.lanlyc.core.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.po.BaseSetting;
import video.cn.com.lanlyc.core.po.Group;

/***
 * 
 * @author 	    胡志浩
 * @date     2017年9月19日 
 * @version  1.0 
 * @Title:	 基本模式设置Dao层
 */
@Service
public class BaseSettingDao extends MySQLMapper<BaseSetting>{

	
	
	/***
	 *  初始化初始化系统状态
	 *  功能模式为	1、监控直播
	 *  分屏模式为	2、2x2
	 *  轮巡模式为	1、一般
	 * @param base
	 * @return boolean
	 */
	public boolean initBaseSetting(BaseSetting base){
		int flag = this.save(base);
		if (flag >=1){
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 获取系统状态信息
	 * @return
	 * @author huzhihao
	 * used
	 */
	public List<BaseSetting> getBaseSetting() {
		QMap qMap = new QMap();
		String sql = "select t.* from video_t_base_setting t";
		return this.findList(sql, qMap);
	}
	
	
	
	/**
	 * 用户自定义设置系统状态信息
	 * @return
	 * @author huzhihao
	 * used
	 */
	public boolean setBaseSetting(BaseSetting baseSetting) {
		int flag = this.update(baseSetting);
		if (flag>=1) {
			return true;
		}
		return false;
	}
}
