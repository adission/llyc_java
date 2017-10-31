package video.cn.com.lanlyc.core.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.po.VidiconSetting;;

/***
 * 
 * @author 	    胡志浩
 * @date     2017年9月28日 
 * @version  1.0 
 * @Title:	 摄像头设置相关DAO层
 */
@Service
public class VidiconSettingDao extends MySQLMapper<VidiconSetting>{
	/***
	 *  初始化初始化摄像头设置
	 * @param base
	 * @return boolean
	 */
	public boolean initVidiconSetting(VidiconSetting vidicon){
		int flag = this.save(vidicon);
		if (flag >=1){
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 获取摄像头设置信息
	 * @return
	 * @author huzhihao
	 * used
	 */
	public List<VidiconSetting> getVidiconSetting() {
		QMap qMap = new QMap();
		String sql = "select t.* from video_t_vidicon_setting t";
		return this.findList(sql, qMap);
	}
	
	
	
	/**
	 * 用户自定义设置摄像头设置信息
	 * @return
	 * @author huzhihao
	 * used
	 */
	public boolean setVidiconSetting(VidiconSetting vidicon) {
		int flag = this.update(vidicon);
		if (flag>=1) {
			return true;
		}
		return false;
	}
}
