package video.cn.com.lanlyc.core.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.po.ScreenVidicon;

@Service
public class ScreenVidiconInterfaceDao extends MySQLMapper<ScreenVidicon> {
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据分屏id获取摄像头的总数
	 * @param:split_screen_id：分屏id
	 * @return:摄像头的总数量
	 * @date:2017年9月18日 下午4:43:08
	 */
	public int getAllVidiconByScreenId(String split_screen_id) {
		QMap map = new QMap();
		String sql = new String("select count(vidicon_id) as num from video_t_screen_vidicon_relation where split_screen_id=:split_screen_id");
		map.add("split_screen_id", split_screen_id);
		Number num = this.getNumber(sql, map);
		return num.intValue();
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:查询所有的没有放置摄像头的空位置
	 * @param:split_screen_type：分屏模式，split_screen_id：分屏id
	 * @return:
	 * @date:2017年9月18日 下午4:45:40
	 */
	public List<Integer> getNullPosition(String split_screen_id) {
		String sql = new String("select screen_position from video_t_screen_vidicon_relation where split_screen_id=:split_screen_id");
		QMap qmap = new QMap();
		qmap.put("split_screen_id", split_screen_id);
		List<ScreenVidicon> screenVidicon = findList(sql, qmap);
		
		List<Integer> positionList = new ArrayList<Integer>();
		for (int i = 0; i < screenVidicon.size(); i++) {
			String str = String.valueOf(screenVidicon.get(i).getScreen_position());
			if(str != null && str != ""){
				positionList.add(screenVidicon.get(i).getScreen_position());
			}
		}
		
		return positionList;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据摄像头id查询对应的分屏id
	 * @param:vidicon_id：摄像头id
	 * @return:分屏id组成的list集合["id1","id2"...]
	 * @date:2017年9月26日 下午4:32:32
	 */
	public List<ScreenVidicon> getSplitScreenId(String vidicon_id) {
		String sql = new String("select id, split_screen_id from video_t_screen_vidicon_relation where vidicon_id=:vidicon_id");
		QMap qmap = new QMap();
		qmap.put("vidicon_id", vidicon_id);
		List<ScreenVidicon> ssids = findList(sql, qmap);
		
		/*List<String> ids = new ArrayList<String>();
		for (int i = 0; i < ssids.size(); i++) {
			String str = ssids.get(i).getSplit_screen_id();
			if(str != null && str != ""){
				ids.add(str);
			}
		}*/
		return ssids;
	}

}
