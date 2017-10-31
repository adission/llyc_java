package video.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import video.cn.com.lanlyc.core.dao.GroupScreenDao;
import video.cn.com.lanlyc.core.dto.GroupScreenDto;
import video.cn.com.lanlyc.core.po.GroupScreen;

/**
 * 组和屏关联表Service
 * @author chenyan
 * @date 2017年9月13日 10:30:13
 * @version v1.0
 */
@Service
public class GroupScreenService {
	
	@Autowired
	GroupScreenDao groupScreenDao;
	
	/**
	 * 一、2、分组管理新增
	 * 			新增一条组与屏关联关系
	 * 二、初始化默认组
	 * 			新增一条组与屏关联关系
	 * @param 	groupScreen
	 * @return 	int
	 * @author	chenyan
	 */
	public int addGroupScreen(GroupScreen groupScreen){
		return groupScreenDao.addGroupScreen(groupScreen);
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			获取某个组下的所有组与屏关系列表（Dao）
	 * @param 	groupId
	 * @return 	某个组下的所有屏列表	List
	 * @author 	chenyan
	 */
	public List<GroupScreen> getGroupScreenListByGroupIdDao(String groupId) {
		return groupScreenDao.getGroupScreenListByGroupIdDao(groupId);
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			通过id删除一条组与屏关联关系
	 * @param 	id
	 * @return 	删除记录数	int
	 * @author 	chenyan
	 */
	public int deleteGroupScreenById(String id) {
		return groupScreenDao.deleteGroupScreenById(id);
	}
	
	/**
	 * 5、分组管理详情
	 * 			获取某个组下的所有组与屏关系列表（Dto）
	 * 三、3、获取某个组下的所有屏列表
	 * 			获取某个组下的所有组与屏关系列表（Dto）
	 * 三、7、获取某种分屏模式下的所有屏
	 * 			获取某个组下的所有组与屏关系列表（Dto）
	 * @param 	groupId
	 * @return 	某个组下的所有屏列表	List
	 * @author 	chenyan
	 */
	public List<GroupScreenDto> getScreenListByGroupIdDto(String groupId) {
		System.out.println(groupId);
		System.out.println(groupScreenDao.getScreenListByGroupIdDto(groupId));
		return groupScreenDao.getScreenListByGroupIdDto(groupId);
	}
	
	/**
	 * 三、4、通过组id和屏排序获取屏id
	 * @param 	string
	 * @param 	sort
	 * @return
	 * @author	chenyan
	 */
	public String getScreenIdByGroupIdAndScreenSort(String groupId, int screenSort) {
		List<GroupScreen> result = groupScreenDao.getGroupScreenListByGroupIdAndScreenSort(groupId, screenSort);
		if(result.size()<1){
			return null;
		}else{
			return result.get(0).getSplit_screen_id();
		}
	}

//	/**
//	 * 获取默认组的所有屏
//	 * @return
//	 */
//	public List<GroupScreenDto> getScreenListByDefaultGroupId(int screenType) {
//		GroupService groupService = new GroupService(); 
//		String groupId = groupService.getDefaultGroupIdByscreenType(screenType);
//		return groupScreenDao.getScreenListByGroupId(groupId);
//	}

//	/**
//	 * 通过id获取
//	 * @param id
//	 * @return Group
//	 * @author chenyan
//	 */
//	public GroupScreen getGroupScreenById(int id) {
//		return groupScreenDao.getGroupScreenById(id);
//	}
//
//	/**
//	 * 更新
//	 * @param Group
//	 * @return int
//	 * @author chenyan
//	 */
//	public int updateGroupScreen(GroupScreen groupScreen) {
//		return groupScreenDao.updateGroupScreen(groupScreen);
//	}

}