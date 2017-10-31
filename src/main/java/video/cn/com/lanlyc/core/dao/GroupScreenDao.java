package video.cn.com.lanlyc.core.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.dto.GroupScreenDto;
import video.cn.com.lanlyc.core.po.GroupScreen;

/**
 * 组和屏关联表Dao
 * @author chenyan
 * @date 2017年9月13日 10:35:56
 * @version v1.0
 */
@Service
public class GroupScreenDao extends MySQLMapper<GroupScreen>{
	
	/**
	 * 2、分组管理新增
	 * 			新增一条组与屏关联关系
	 * @param 	groupScreen
	 * @return 	int
	 * @author	chenyan
	 */
	public int addGroupScreen(GroupScreen groupScreen){
		return this.save(groupScreen);
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			获取某个组下的所有组与屏关系列表（Dao）
	 * @param 	groupId
	 * @return 	List
	 * @author 	chenyan
	 */
	public List<GroupScreen> getGroupScreenListByGroupIdDao(String groupId) {
		StringBuilder sql = new StringBuilder("select gs.* from video_t_group_screen_relation gs where 1=1 ");
		QMap map = new QMap();
		if (isValid(groupId)){
			sql.append(" and gs.vidicon_group_id = :vidicon_group_id ;");
			map.put("vidicon_group_id", groupId);
		}
        List<GroupScreen> list = findList(sql, map, GroupScreen.class);
		return list;
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			通过id删除一条组与屏关联关系
	 * @param 	id
	 * @return 	int
	 * @author 	chenyan
	 */
	public int deleteGroupScreenById(String id) {
		return this.delete(id);
	}
	
	/**
	 * 5、分组管理详情
	 * 			获取某个组下的所有组与屏关系列表（Dto）
	 * 三、3、获取某个组下的所有屏列表
	 * @param 	groupId
	 * @return 	某个组下的所有屏列表	List
	 * @author 	chenyan
	 */
	public List<GroupScreenDto> getScreenListByGroupIdDto(String groupId) {
		StringBuilder sql = new StringBuilder("select gs.*,s.* from video_t_group_screen_relation gs inner join video_t_split_screen s on gs.split_screen_id = s.id where 1=1 ");
		QMap map = new QMap();
		if (isValid(groupId)){
			sql.append(" and gs.vidicon_group_id = :vidicon_group_id ;");
			map.put("vidicon_group_id", groupId);
		}
        List<GroupScreenDto> list = findList(sql, map, GroupScreenDto.class);
		return list;
	}
	
	/**
	 * 三、4、通过组id和屏排序获取组与屏关联关系
	 * @param 	groupId
	 * @param 	sort
	 * @return
	 * @author 	chenyan
	 */
	public List<GroupScreen> getGroupScreenListByGroupIdAndScreenSort(String groupId, int screenSort) {
		StringBuilder sql = new StringBuilder("select t.* from video_t_group_screen_relation t where 1 = 1");
		QMap map = new QMap();
		if (isValid(groupId)){
			sql.append(" and t.vidicon_group_id = :group_id");
			map.put("group_id", groupId);
		}
		if (isValid(screenSort)){
			sql.append(" and t.sort = :sort ;");
			map.put("sort", screenSort);
		}
        List<GroupScreen> list = findList(sql, map, GroupScreen.class);
		return list;
	}

//	/**
//	 * 通过id获取
//	 * @param id
//	 * @return 
//	 * @author chenyan
//	 */
//	public GroupScreen getGroupScreenById(int id) {
//		return this.get(id);
//	}
//	
//	/**
//	 * 更新一条
//	 * @param groupScreen
//	 * @return int
//	 * @author chenyan
//	 */
//	public int updateGroupScreen(GroupScreen groupScreen) {
//		return this.update(groupScreen,false);
//	}

}
