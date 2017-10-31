package video.cn.com.lanlyc.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.util.Page;
import video.cn.com.lanlyc.core.dao.GroupDao;
import video.cn.com.lanlyc.core.po.Group;

/**
 * 分组Service
 * @author chenyan
 * @date 2017年9月11日 16:56:35
 * @version v1.0
 */
@Service
public class GroupService {

	@Autowired
	private GroupDao groupDao;

	/*
	 * 一、分组管理
	 */

	/** 
	 * 1、分组管理列表查询：
	 * 			按条件（是否重点、分屏模式）筛选组列表带分页（不包含初始化数据）
	 * @param	page
	 * @param	queryMap
	 * @return
	 * @author	chenyan
	 */
	public Page<Group> getGroupListByCondition(Page<Group> page, Map<String, Integer> queryMap) {
		return groupDao.getGroupListByCondition(page, queryMap);	
	}

	/**
	 * 2、分组管理新增
	 * 			将某种分屏模式下的默认组修改为非默认
	 * 6、分组管理修改
	 * 			将非默认组修改为默认时，将某种分屏模式的默认组修改为非默认
	 * @param 	screenType
	 * @return 	更新结果	Boolean
	 * @author 	chenyan
	 */
	public Boolean updateDefaultGroupToNull(int screenType, int isImportant) {
		//1、通过分屏模式、是否重点获取某种分屏模式下的默认组id
		String defaultGroupId = getDefaultGroupIdByscreenTypeAndIsImportant(screenType,isImportant);
		if(defaultGroupId==null){//说明没有默认分组
			return true;
		}else{
			//2、通过id更新一条分组
			Group group = new Group();
			group.setId(defaultGroupId);
			group.setSet_default(2);
			group.setSplit_screen_type(screenType);
			group.setWhether_important(isImportant);
			int update_result = this.updateGroupByGroupId(group);
			if(update_result>1){
				return true;
			}else{
				return false;
			}
		}
	}

	/**
	 * 一、2、分组管理新增
	 * 			将某种分屏模式下的默认组修改为非默认
	 * 				通过分屏模式和是否重点获取某种分屏模式下的默认组id
	 * 一、6、分组管理修改
	 * 			将非默认组修改为默认时，将某种分屏模式的默认组修改为非默认
	 * 				通过分屏模式和是否重点获取某种分屏模式下的默认组id
	 * 三、1、通过分屏模式和是否重点获取某种分屏模式下的默认组id
	 * @param 	screenType
	 * @param 	isImportant
	 * @return 	查询成功：返回默认组id
	 * 		           查询失败：返回null
	 */
	public String getDefaultGroupIdByscreenTypeAndIsImportant(int screenType, int isImportant) {
		List<Group> result = groupDao.getDefaultGroupListByscreenTypeAndIsImportant(screenType, isImportant);
		if(result.size()<1){
			return null;
		}else{
			return result.get(0).getId();
		}
	}

	/**
	 * 2、分组管理新增
	 * 			将某种分屏模式下的默认组修改为非默认
	 * 				通过id更新一条分组
	 * 6、分组管理修改
	 * 			将非默认组修改为默认时，将某种分屏模式的默认组修改为非默认
	 * 				通过id更新一条分组
	 * @param 	group
	 * @return 	更新记录数	int
	 * @author 	chenyan
	 */
	public int updateGroupByGroupId(Group group) {
		return groupDao.updateGroupByGroupId(group);
	}

	/**
	 * 一、2、分组管理新增
	 * 			新增一个分组
	 * 二、初始化默认组
	 * 			新增一个分组
	 * @param 	group
	 * @return 	新增记录数	int
	 * @author 	chenyan
	 */
	public int addGroup(Group group){
		return groupDao.addGroup(group);
	}

	/**
	 * 3、分组管理单条删除
	 * 			通过id删除一个分组
	 * @param 	id
	 * @return 	删除记录数	int
	 * @author 	chenyan
	 */
	public int deleteGroupById(String id) {
		return groupDao.deleteGroupById(id);
	}

	/** 
	 * 5、分组管理详情
	 * 			通过id获取分组信息
	 * @param 	id
	 * @return 	Group 一个分组对象
	 * @author 	chenyan
	 */
	public Group getGroupById(String id) {
		return groupDao.getGroupById(id);
	}


	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------

	/*
	 * 三、视频直播
	 */

	/**
	 * 1、通过分屏模式和是否重点获取某种分屏模式下的默认组id（不存在默认组id时获取初始化组id）
	 * @param 	screenType
	 * @param 	isImportant
	 * @return
	 * @author 	chenyan
	 */
	public String getIndexGroupIdByscreenTypeAndIsImportant(int screenType, int isImportant) {
		//1、通过分屏模式和是否重点获取某种分屏模式下的默认组id
		String groupId = this.getDefaultGroupIdByscreenTypeAndIsImportant(screenType,isImportant);
		if(groupId == null){
			//2、通过分屏模式和是否重点获取某种分屏模式下的初始化组id
			groupId = this.getInitGroupIdByscreenTypeAndIsImportant(screenType,isImportant);
		}
		return groupId;
	}

	/**
	 * 2、通过分屏模式和是否重点获取某种分屏模式下的初始化组id
	 * @param 	screenType
	 * @param 	isImportant
	 * @return
	 * @author 	chenyan
	 */
	private String getInitGroupIdByscreenTypeAndIsImportant(int screenType, int isImportant) {
		List<Group> result = groupDao.getInitGroupListByscreenTypeAndIsImportant(screenType, isImportant);
		if(result.size()<1){
			return null;
		}else{
			return result.get(0).getId();
		}
	}

	/**
	 * 3、通过分屏模式和是否重点获取某种分屏模式下的所有组列表（不包含初始化数据）
	 * @param screenType
	 * @param isImportant
	 * @return 某种分屏模式下的所有组列表	List
	 * @author chenyan
	 */
	public List<Group> getGroupListByScreenTypeAndIsImportantNoInit(int screenType, int isImportant) {
		return groupDao.getGroupListByScreenTypeAndIsImportantNoInit(screenType,isImportant);
	}

	/**
	 * 7、获取某种分屏模式下的所有屏
	 * 			获取某种分屏模式下的所有组（不包含初始化数据）
	 * @param 	screenType
	 * @return
	 * @author chenyan
	 */
	public List<Group> getGroupListByScreenTypeNoInit(int screenType) {
		return groupDao.getGroupListByScreenTypeNoInit(screenType);
	}

//	/**
//	 * 通过分屏模式和巡航模式（重点或非重点）获取某种分屏模式下重点或所有摄像头的所有组列表（包含初始化数据）
//	 * @param screenType
//	 * @param isImportant
//	 * @return 某种分屏模式下的所有组列表	List
//	 * @author chenyan
//	 * used
//	 */
//	public List<Group> getGroupListByScreenTypeAndIsImportant2(int screenType, int isImportant) {
//		return groupDao.getGroupListByScreenTypeAndIsImportant2(screenType,isImportant);
//	}

}