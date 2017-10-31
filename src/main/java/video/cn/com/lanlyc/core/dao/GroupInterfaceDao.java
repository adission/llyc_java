package video.cn.com.lanlyc.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.po.Group;

@Service
public class GroupInterfaceDao extends MySQLMapper<Group>{
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:判断是否有分组
	 * @param:无
	 * @return:true:有分组，false:无分组
	 * @date:2017年9月18日 下午5:13:37
	 */
	public boolean judgeGroupInfo(boolean whether_important) {
		String sql = "select id from video_t_vidicon_group where whether_important=:whether_important";
		QMap qMap = new QMap();
		if(whether_important){//摄像头为非重点摄像头时查询一般组
			qMap.add("whether_important", 1);
		}else{
			qMap.add("whether_important", 2);
		}
		
		List<String> screenVidicon = findListForColumn(sql, qMap,String.class);
		if(screenVidicon.isEmpty()){
			return false;
		}
		return true;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:获取所有分屏模式下的所有分组
	 * @param:无
	 * @return:分屏模式与分组对应的键值对
	 * @date:2017年9月18日 下午5:18:02
	 */
	public Map<String, Object> getAllGroup(boolean whether_important) {
		Map<String, Object> gMap = new HashMap<String, Object>();
		String sql = "";
		if(whether_important){//摄像头为重点摄像头的时的操作
			sql = "select id from video_t_vidicon_group where split_screen_type=:split_screen_type";
			List<String> gList1 = getListByScreenType(1, sql,null);
			gMap.put(1+"", gList1);
			List<String> gList2 = getListByScreenType(2, sql,null);
			gMap.put(2+"", gList2);
			List<String> gList3 = getListByScreenType(3, sql,null);
			gMap.put(3+"", gList3);
			List<String> gList4 = getListByScreenType(4, sql,null);
			gMap.put(4+"", gList4);
		}else{//摄像头为非重点摄像头的时的操作
			sql = "select id from video_t_vidicon_group where split_screen_type=:split_screen_type and whether_important=:whether_important";
			List<String> gList1 = getListByScreenType(1, sql,2+"");
			gMap.put(1+"", gList1);
			List<String> gList2 = getListByScreenType(2, sql,2+"");
			gMap.put(2+"", gList2);
			List<String> gList3 = getListByScreenType(3, sql,2+"");
			gMap.put(3+"", gList3);
			List<String> gList4 = getListByScreenType(4, sql,2+"");
			gMap.put(4+"", gList4);
		}
		
		return gMap;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据分组模式和slq语句获取分组id
	 * @param:screenType:分屏类型，sql：sql语句
	 * @return:分组id组成的list集合
	 * @date:2017年9月18日 下午5:42:24
	 */
	public List<String> getListByScreenType(int screenType,String sql,String whether_important){
		QMap qMap =  new QMap();
		qMap.put("split_screen_type", screenType);
		if(whether_important != null){
			qMap.put("whether_important", Boolean.getBoolean(whether_important));
		}
		List<Group> screenVidicon1 = findList(sql, qMap);
		List<String> gList = new ArrayList<String>();
		for (int i = 0; i < screenVidicon1.size(); i++) {
			gList.add(screenVidicon1.get(i).getId());
		}
		return gList;
	}

}
