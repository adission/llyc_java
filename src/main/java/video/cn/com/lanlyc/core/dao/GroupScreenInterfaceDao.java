package video.cn.com.lanlyc.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.po.GroupScreen;

@Service
public class GroupScreenInterfaceDao extends MySQLMapper<GroupScreen> {
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据分组id集合获取所有的分屏
	 * @param:list：分组id集合
	 * @return:
	 * @date:2017年9月18日 下午4:59:01
	 */
	public Map<String, Object> getAllScreenByGroupId(List<String> list) {
		Map<String, Object> gsMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select gs.split_screen_id from video_t_group_screen_relation gs where gs.vidicon_group_id=:vidicon_group_id");
		for (int i = 0; i < list.size(); i++) {
			QMap qMap = new QMap();
			 qMap.put("vidicon_group_id", list.get(i));
			 List<GroupScreen> gsList = findList(sql, qMap);
			 List<String> lis = new ArrayList<String>();
			 for (int j = 0; j < gsList.size(); j++) {
				 lis.add(gsList.get(j).getSplit_screen_id());
			}
			 gsMap.put(list.get(i), lis);
		}
		return gsMap;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据摄像头id查询对应的分屏id
	 * @param:vidicon_id：摄像头id
	 * @return:分屏id组成的list集合["id1","id2"...]
	 * @date:2017年9月26日 下午4:32:32
	 */
	public List<GroupScreen> getSplitScreenId(String split_screen_id) {
		String sql = new String("select id vidicon_group_id from video_t_group_screen_relation where split_screen_id=:split_screen_id");
		QMap qmap = new QMap();
		qmap.put("split_screen_id", split_screen_id);
		List<GroupScreen> ssids = findList(sql, qmap);
		
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
