package video.cn.com.lanlyc.core.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.po.Group;

/**
 * 分组Dao
 * @author chenyan
 * @date 2017年9月11日 16:32:06
 * @version v1.0
 */
@Service
public class GroupDao extends MySQLMapper<Group>{
	
	/** 
	 * 1、分组管理列表查询：
	 * 			按条件（是否重点、分屏模式）筛选组列表带分页（不包含初始化数据）
	 * @param	page
	 * @param	queryMap
	 * @return
	 * @author	chenyan
	 */
	public Page<Group> getGroupListByCondition(Page<Group> page, Map<String, Integer> queryMap) {
		StringBuilder sql = new StringBuilder("select t.* from video_t_vidicon_group t where 1=1 ");
		QMap map = new QMap();
		if (isValid(queryMap.get("screenType"))){
			sql.append(" and t.split_screen_type = :split_screen_type ");
			map.put("split_screen_type", queryMap.get("screenType"));
		}
		if (isValid(queryMap.get("isImportant"))){
			sql.append(" and t.whether_important = :whether_important ");
			map.put("whether_important", queryMap.get("isImportant"));
		}
		if(queryMap.get("containInit")==2){
			sql.append(" and t.set_default <> -1 ");
		}
		sql.append(" order by t.split_screen_type asc ");
		Page<Group> list = getPage(sql, map, page, Group.class);
		return list;
	}
	
	/**
	 * 2、分组管理新增
	 * 			将某种分屏模式下的默认组修改为非默认
	 * 				通过分屏模式和是否重点获取某种分屏模式下的默认组信息
	 * 6、分组管理修改
	 * 			将非默认组修改为默认时，将某种分屏模式的默认组修改为非默认
	 * 				通过分屏模式和是否重点获取某种分屏模式下的默认组信息
	 * @param 	screenType
	 * @param 	isImportant
	 * @return	返回分组信息列表
	 */
	public List<Group> getDefaultGroupListByscreenTypeAndIsImportant(int screenType,int isImportant) {
		StringBuilder sql = new StringBuilder("select t.* from video_t_vidicon_group t where t.set_default = 1");
		QMap map = new QMap();
		if (isValid(screenType)){
			sql.append(" and t.split_screen_type = :split_screen_type ");
			map.put("split_screen_type", screenType);
		}
		if (isValid(isImportant)){
			sql.append(" and t.whether_important = :whether_important ;");
			map.put("whether_important", isImportant);
		}
        List<Group> list = findList(sql, map, Group.class);
		return list;
	}
	
	/**
	 * 2、分组管理新增
	 * 			将某种分屏模式下的默认组修改为非默认
	 * 				通过id更新一条分组
	 * @param 	group
	 * @return 	int
	 * @author 	chenyan
	 */
	public int updateGroupByGroupId(Group group) {
		return this.update(group,false);
	}
	
	/**
	 * 2、分组管理新增
	 * 			新增一个分组
	 * @param 	group
	 * @return 	int
	 * @author 	chenyan
	 */
	public int addGroup(Group group){
		int result = this.save(group);
		return result;
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			通过id删除一个分组
	 * @param 	id
	 * @return 	int
	 * @author 	chenyan
	 */
	public int deleteGroupById(String id) {
		return this.delete(id);
	}

	/** 
	 * 5、分组管理详情
	 * 			通过id获取分组信息
	 * @param 	id
	 * @return 	Group
	 * @author 	chenyan
	 */
	public Group getGroupById(String id) {
		return this.get(id);
	}
	
	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------
	
	/*
	 * 三、视频直播
	 */
	
	/**
	 * 2、通过分屏模式和是否重点获取某种分屏模式下的初始化组信息
	 * @param 	screenType
	 * @param 	isImportant
	 * @return
	 * @author 	chenyan
	 */
	public List<Group> getInitGroupListByscreenTypeAndIsImportant(int screenType, int isImportant) {
		StringBuilder sql = new StringBuilder("select t.* from video_t_vidicon_group t where t.set_default = -1");
		QMap map = new QMap();
		if (isValid(screenType)){
			sql.append(" and t.split_screen_type = :split_screen_type ");
			map.put("split_screen_type", screenType);
		}
		if (isValid(isImportant)){
			sql.append(" and t.whether_important = :whether_important ;");
			map.put("whether_important", isImportant);
		}
        List<Group> list = findList(sql, map, Group.class);
		return list;
	}
	
	/**
	 * 3、通过分屏模式和是否重点获取某种分屏模式下的所有组列表（不包含初始化数据）
	 * @param screen_type
	 * @param isImportant
	 * @return List
	 * @author chenyan
	 */
	public List<Group> getGroupListByScreenTypeAndIsImportantNoInit(int screenType, int isImportant) {
		StringBuilder sql = new StringBuilder("select t.* from video_t_vidicon_group t where 1=1 ");
		QMap map = new QMap();
		if (isValid(screenType)){
			sql.append(" and t.split_screen_type = :split_screen_type ");
			map.put("split_screen_type", screenType);
		}
		if (isValid(isImportant)){
			sql.append(" and t.whether_important = :whether_important ");
			map.put("whether_important", isImportant);
		}
		sql.append(" and t.set_default <> -1");
        List<Group> list = findList(sql, map, Group.class);
		return list;
	}
	
	/**
	 * 7、获取某种分屏模式下的所有屏
	 * 			获取某种分屏模式下的所有组（不包含初始化数据）
	 * @param screen_type
	 * @param isImportant
	 * @return List
	 * @author chenyan
	 * used
	 */
	public List<Group> getGroupListByScreenTypeNoInit(int screenType) {
		StringBuilder sql = new StringBuilder("select t.* from video_t_vidicon_group t where 1=1 ");
		QMap map = new QMap();
		if (isValid(screenType)){
			sql.append(" and t.split_screen_type = :split_screen_type ");
			map.put("split_screen_type", screenType);
		}
		sql.append(" and t.set_default <> -1");
        List<Group> list = findList(sql, map, Group.class);
		return list;
	}
		
//	/**
//	 * 通过分屏模式和巡航模式（重点或非重点）获取某种分屏模式下重点或所有摄像头的所有组列表（包含初始化数据）
//	 * @param screen_type
//	 * @param isImportant
//	 * @return List
//	 * @author chenyan
//	 * used
//	 */
//	public List<Group> getGroupListByScreenTypeAndIsImportant2(int screenType, int isImportant) {
//		StringBuilder sql = new StringBuilder("select t.* from video_t_vidicon_group t where 1=1 ");
//		QMap map = new QMap();
//		if (isValid(screenType)){
//			sql.append(" and t.split_screen_type = :split_screen_type ");
//			map.put("split_screen_type", screenType);
//		}
//		if (isValid(isImportant)){
//			sql.append(" and t.whether_important = :whether_important ");
//			map.put("whether_important", isImportant);
//		}
//        List<Group> list = findList(sql, map, Group.class);
//		return list;
//	}

}