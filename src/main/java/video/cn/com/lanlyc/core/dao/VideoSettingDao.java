package video.cn.com.lanlyc.core.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.po.VideoSetting;

/***
 * 
 * @author 	    胡志浩
 * @date     2017年9月27日 
 * @version  1.0 
 * @Title:	 视频监控--视频设置Dao层
 */
@Service
public class VideoSettingDao extends MySQLMapper<VideoSetting>{

	
	/***
	 * 初始化视频设置
	 * @param v
	 * @return boolean
	 */
	public boolean InitVideoSetting(VideoSetting v) {
		int flag = this.save(v);
		if (flag >=1){
			return true;
		}
		return false;
	}
	

	
	/***
	 * 获取视频设置表中的ID
	 * @param 
	 * @return List<VideoSetting>
	 */
	public List<VideoSetting> getAll(String vidicon_id) {
		QMap qMap = new QMap();
		String sql = "select t.id from video_t_setting t where t.vidicon_id=:vidicon_id";
		qMap.put("vidicon_id", vidicon_id);
		return this.findList(sql, qMap);
	}
	
	
	
	/**
	 * 用户自定义设视频设置信息
	 * @return
	 * @author huzhihao
	 * used
	 */
	public boolean setVideoSetting(VideoSetting v) {
		int flag = this.update(v);
		if (flag>=1) {
			return true;
		}
		return false;
	}
}
