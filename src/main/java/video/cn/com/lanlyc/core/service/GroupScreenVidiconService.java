package video.cn.com.lanlyc.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;

import cn.com.lanlyc.base.util.DataUtils;
import net.sf.json.JSONObject;
import video.cn.com.lanlyc.core.dto.GroupScreenDto;
import video.cn.com.lanlyc.core.dto.ScreenVidiconDto;
import video.cn.com.lanlyc.core.po.Group;
import video.cn.com.lanlyc.core.po.Screen;
import video.cn.com.lanlyc.core.po.GroupScreen;
import video.cn.com.lanlyc.core.po.ScreenVidicon;

/**
 * 组、屏、摄像机关联Service
 * @author chenyan
 * @date 2017年9月12日 16:34:27
 * @version v1.0
 */
@Service
@Transactional
public class GroupScreenVidiconService {

	@Autowired
	private GroupService groupService;

	@Autowired
	private ScreenService screenService;

	@Autowired
	private GroupScreenService groupScreenService;

	@Autowired
	private ScreenVidiconService screenVidiconService;
	
	/*
	 * 一、分组管理
	 */

	/**
	 * 2、分组管理新增
	 * 			包括：一个组、组下多个屏、每个屏下多个摄像机
	 * 3、分组管理修改
	 * 			新增一条分组管理
	 * @param	json：以共8个摄像机，2*2屏为例
	 * 				{
	 * 					"group_name":..., 		//组名称										string
	 * 					"screen_type":2,		//分屏模式（1代表1*1 2代表2*2 3代表3*3 4代表4*4）	int
	 * 					"set_default":...,		//是否设置为默认组（1代表是，2代表否）					int
	 * 					"is_important":...,		//是否设置为重要组（1代表是，2代表否）					int
	 * 					"all_screen":{			//健名：代表第几屏								int
	 * 								"1":{		
	 * 									"screen_name":...,			//屏名称					string
	 * 									"all_vidicon":{				//键名：代表摄像机在屏中位置		int	
	 * 													"1":...,	//键值：摄像机id			string
	 * 													"2":...,
	 * 													"3":...,	
	 * 													"4":...,
	 * 												  }
	 * 								  },
	 * 								"2":{
	 * 									"screen_name":...,
	 * 									"all_vidicon":{
	 * 													"1":...,
	 *													"2":...,
	 *													"3":...,
	 *													"4":...,
	 * 												  }
	 * 								  }
	 * 								}
	 * 				}
	 * @param 	userId
	 * @return	
	 * @author	chenyan
	 */
	public Boolean addGroupScreenVidicon(JSONObject json,String userId) {
		//根据是否设置为默认，是否设置为重点，
		//1、若为默认，先将之前的这种分屏模式下的重点或非重点的默认组修改为非默认，再新增组
		if(json.getInt("set_default")==1){
			Boolean updateResult = groupService.updateDefaultGroupToNull(json.getInt("screen_type"),json.getInt("is_important"));
			if(!updateResult){
				return false;
			}
		}

		//2、新增一条分组
		Group group = new Group();
		String groupId = DataUtils.getUUID();
		group.setId(groupId);
		group.setGroup_name(json.getString("group_name"));
		group.setSplit_screen_type(json.getInt("screen_type"));
		group.setSet_default(json.getInt("set_default"));
		group.setWhether_important(json.getInt("is_important"));
		Date date = new Date();
		group.setGrouping_time(new java.sql.Date(date.getTime()));
		group.setGrouping_person(userId);
		int addGroupResult = groupService.addGroup(group);
		if(addGroupResult < 1){
			return false;
		}

		JSONObject allScreen = JSONObject.fromObject(json.getString("all_screen"));
		int allScreenCount = allScreen.size();//大屏数目

		//3、新增分屏、分组分屏关系、分屏摄像机关系
		for(int i=1; i<=allScreenCount; i++){
			JSONObject bigScreen = JSONObject.fromObject(allScreen.getString(String.valueOf(i)));

			//1、新增分屏
			Screen screen = new Screen();
			String screenId = DataUtils.getUUID();
			screen.setId(screenId);
			if(bigScreen.getString("screen_name")!=null && bigScreen.getString("screen_name")!=""){
				screen.setSplit_screen_name(bigScreen.getString("screen_name"));
			}else{
				return false;
			}
			//screen.setSet_default(allScreen.getInt("set_default"));

			int addScreenResult = screenService.addScreen(screen);
			if(addScreenResult < 1){
				return false;
			}

			//2、新增分组分屏关系
			GroupScreen groupScreen = new GroupScreen();
			groupScreen.setId(DataUtils.getUUID());
			groupScreen.setVidicon_group_id(groupId);
			groupScreen.setSplit_screen_id(screenId);
			groupScreen.setSort(i);

			int addGroupScreenResult = groupScreenService.addGroupScreen(groupScreen);
			if(addGroupScreenResult < 1){
				return false;
			}

			JSONObject allVidicon = JSONObject.fromObject(bigScreen.getString("all_vidicon"));
			int allVidiconCount = allVidicon.size();//一屏中摄像机数目

			//3、新增分屏摄像机关系
			for(int j=1; j<=allVidiconCount; j++){

				//3、新增分屏摄像机关系
				ScreenVidicon screenVidicon = new ScreenVidicon();
				screenVidicon.setId(DataUtils.getUUID());
				screenVidicon.setVidicon_id(allVidicon.getString(String.valueOf(j)));
				screenVidicon.setSplit_screen_id(screenId);
				screenVidicon.setScreen_position(j);

				int addScreenVidiconResult = screenVidiconService.addScreenVidicon(screenVidicon);
				if(addScreenVidiconResult<1){
					return false;
				}
			}
		}
		return true;
	}

	/** 
	 * 3、分组管理单条删除
	 * 			包括：一个组、组下多个屏、每个屏下多个摄像机
	 * 4、分组管理批量删除
	 * 			逐条删除
	 * 6、分组管理修改
	 * 			删除原来组
	 * @param 	groupId
	 * @return 	Boolean
	 * @author 	chenyan
	 */
	public Boolean deleteGroupScreenVidiconByGroupId(String groupId) {
		//1、获取组下的所有组与屏关联关系
		List<GroupScreen> screenList = groupScreenService.getGroupScreenListByGroupIdDao(groupId);

		for(int i=0; i< screenList.size(); i++){
			GroupScreen gs = screenList.get(i);
			String screenId = gs.getSplit_screen_id();

			//2、获取屏下的所有屏和摄像机关联关系
			List<ScreenVidicon> screenVidiconList = screenVidiconService.getScreenVidiconListByScreenIdDao(screenId);

			for(int j=0; j<screenVidiconList.size(); j++){
				//3、删除屏和摄像机关联
				ScreenVidicon sv = screenVidiconList.get(j);
				int deleteScreenVidiconResult = screenVidiconService.deleteScreenVidiconById(sv.getId());
				if(deleteScreenVidiconResult<1){
					return false;
				}
			}

			//4、删除屏
			int deletScreenResult = screenService.deleteScreenById(screenId);
			if(deletScreenResult<1){
				return false;
			}

			//5、删除组与屏关联关系
			int deleteGroupScreenResult = groupScreenService.deleteGroupScreenById(gs.getId());
			if(deleteGroupScreenResult<1){
				return false;
			}
		}

		//6、删除组
		int deletGroupResult = groupService.deleteGroupById(groupId);
		if(deletGroupResult<1){
			return false;
		}

		return true;
	}

	/** 
	 * 4、分组管理批量删除
	 * 			包含：多个组
	 * @param	groupIdList
	 * @return	Boolean
	 * @author	chenyan
	 */
	public Boolean deleteGroupScreenVidicon(JSONArray groupIdList) {
		int groupIdCount = groupIdList.size();
		for(int i=0; i<groupIdCount; i++){
			String groupId = (String) groupIdList.get(i);
			//逐条删除
			Boolean delete_one = this.deleteGroupScreenVidiconByGroupId(groupId);
			if(!delete_one){
				return false;
			}
		}
		return true;
	}

	/** 
	 * 5、分组管理详情
	 * 			通过组id获取分组管理详情
	 * 				包含：一个组、组下的多个屏、组与屏关联、屏与摄像机关联、摄像机
	 * @param groupId
	 * @return
	 * @logic:	1.通过组id，获取组信息（组表）
	 * 			2.通过组信息中创建人id，获取创建人信息（接口） （暂不需要）
	 * 			3.通过组id，获取所有组与屏关联关系列表（组与屏关联表）
	 * 				循环组与屏关联关系列表：
	 * 				4.通过屏id获取屏信息（屏表）
	 * 				5.通过屏id获取所有屏与摄像头关联关系列表（屏与摄像头关联表）
	 * 					循环屏与摄像头关联关系列表：
	 * 					6.通过摄像头id，获取摄像头信息
	 * @author chenyan
	 */
	public Map<String, Object> getGroupScreenVidiconByGroupId(String groupId) {
		Map<String, Object> data = new HashMap<String, Object>();
		//1.通过组id，获取组信息（组表）
		Group group = groupService.getGroupById(groupId);

		Map<Integer, Object> allScreen = new HashMap<Integer, Object>();

		//2.通过组信息中创建人id，获取创建人信息（接口）
		//或者：groupDto

		//3.通过组id，获取所有组与屏关联关系列表（组与屏关联表）
		List<GroupScreenDto> screenList = groupScreenService.getScreenListByGroupIdDto(groupId);

		for(int i=0; i<screenList.size(); i++){

			Map<String, Object> screenIndex = new HashMap<String, Object>();

			GroupScreenDto gs = screenList.get(i);
			String screenId = gs.getSplit_screen_id();

			screenIndex.put("group_screen_id", gs.getId());
			screenIndex.put("screen_id", screenId);
			screenIndex.put("screen_name", gs.getSplit_screen_name());
			//screenIndex.put("screen_sort", gs.getSort());

			Map<Integer, Object> allVidicon = new HashMap<Integer, Object>();
			screenIndex.put("all_vidicon", allVidicon);

			//4、通过屏id获取所有屏与摄像头关联关系列表（屏与摄像头关联表）
			List<ScreenVidiconDto> vidiconList = screenVidiconService.getScreenVidiconListByScreenId(screenId);

			for(int j=0; j<vidiconList.size(); j++){
				Map<String, Object> vidiconIndex = new HashMap<String, Object>();
				ScreenVidiconDto sv = vidiconList.get(j);

				vidiconIndex.put("screen_vidicon_id", sv.getId());
				vidiconIndex.put("vidicon_id", sv.getVidicon_id());
				vidiconIndex.put("vidicon_name", sv.getVidicon_name());
				vidiconIndex.put("vidicon_number", sv.getVidicon_number());
				//vidiconIndex.put("position", sv.getScreen_position());

				allVidicon.put(sv.getScreen_position(), vidiconIndex);
			}
			allScreen.put(gs.getSort(), screenIndex);
		}

		data.put("group_name", group.getGroup_name());
		data.put("screen_type", group.getSplit_screen_type());
		data.put("is_important", group.getWhether_important());
		data.put("set_default", group.getSet_default());
		data.put("all_screen", allScreen);
		return data;
	}

	/**
	 * 6、分组管理修改
	 * 			修改一个分组管理（包括：一个组、组下的多个屏、屏与摄像机关联）
	 * @param 	json
	 * @param 	userId
	 * @return
	 * @author 	chenyan
	 */
	public Boolean updateGroupScreenVidiconByGroupId(JSONObject json, String userId) {
		//1、判断组的默认状态是否发生变化
		int old_set_default = json.getInt("old_set_default");
		int set_default = json.getInt("set_default");

		if(old_set_default==2 && set_default==1){
			//1、修改默认组为非默认
			Boolean updateResult = groupService.updateDefaultGroupToNull(json.getInt("screen_type"),json.getInt("is_important"));
			if(!updateResult){
				return false;
			}
		}

		//2、删除原来组
		Boolean delete_one = this.deleteGroupScreenVidiconByGroupId(json.getString("group_id"));
		if(!delete_one){
			return false;
		}

		//3、新增一条组
		Boolean add_one = this.addGroupScreenVidicon(json,userId);
		if(!add_one){
			return false;
		}

		return true;
	}

	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------

	/*
	 * 二、分组初始化
	 */

	/**
	 * 添加第一个摄像机时，初始化默认组
	 * @param 	userId
	 * @param 	vidiconId
	 * @param 	vidiconType
	 * @return	新增成功：true
	 * 		           新增失败：false
	 * @author	chenyan
	 */
	public Boolean addInitGroupScreenVidicon(String userId, String vidiconId, int vidiconType) {

		if( vidiconType == 1){//重点摄像头
			Boolean add_1 = addInitGroupByIsImportant(userId, vidiconId, 1);//重点摄像头
			if(!add_1){
				return false;
			}
		}

		Boolean add_2 = addInitGroupByIsImportant(userId, vidiconId, 2);//所有摄像头
		if(add_2){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 添加第一个摄像机时，初始化重点摄像头/所有摄像头、四种分屏模式的组
	 * @param 	userId
	 * @param 	vidiconId
	 * @param 	isImportant
	 * @return 	新增成功：true
	 * 		           新增失败：false
	 * @author 	chenyan
	 */
	public Boolean addInitGroupByIsImportant(String userId, String vidiconId, int isImportant) {
		Boolean add_1 = addInitGroupByScreenType(userId, vidiconId, isImportant, 1);
		Boolean add_2 = addInitGroupByScreenType(userId, vidiconId, isImportant, 2);
		Boolean add_3 = addInitGroupByScreenType(userId, vidiconId, isImportant, 3);
		Boolean add_4 = addInitGroupByScreenType(userId, vidiconId, isImportant, 4);

		if(add_1 && add_2 && add_3 && add_4){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 初始化四种分屏模式的重点摄像头/所有摄像头组（包括：一个组、组下的一个屏、一个组与屏关联、一个屏与摄像机关联、摄像机）
	 * @param 	userId
	 * @param 	vidiconId
	 * @param 	isImportant
	 * @param 	ScreenType
	 * @return	新增成功：true
	 * 		           新增失败：false
	 * @author 	chenyan
	 */
	public Boolean addInitGroupByScreenType(String userId, String vidiconId, int isImportant, int ScreenType) {
		//1、新增一条分组
		Group group = new Group();
		String groupId = DataUtils.getUUID();
		group.setId(groupId);					//主键id
		String groupName = "";
		if(isImportant == 1){
			groupName += "初始化--重点--";
		}else{
			groupName += "初始化--所有--";
		}
		if(ScreenType == 1){
			groupName += "1*1模式--组";
		}else if(ScreenType == 2){
			groupName += "2*2模式--组";
		}else if(ScreenType == 3){
			groupName += "3*3模式--组";
		}else if(ScreenType == 4){
			groupName += "4*4模式--组";
		}

		group.setGroup_name(groupName);			//分组名称
		group.setSplit_screen_type(ScreenType);	//分屏方式	（1:1*1,2:2*2,3:3*3,4:4*4）
		group.setSet_default(-1);				//是否设置为默认（1：是，2：否）
		group.setWhether_important(isImportant);//是否标记为重点（1：是，2否）
		//Date date = new Date();
		//Date createTime = new Date(date.getTime());
		//group.setGrouping_time(createTime);	//分组时间
		group.setGrouping_person(userId);		//分组人

		int addGroupResult = groupService.addGroup(group);
		if(addGroupResult < 1){
			return false;
		}

		//2、新增一个屏、一个组与屏关联关系、一个屏与摄像机关联关系
		Boolean add_result = addInitScreenVidicon(groupId,vidiconId);
		if(add_result != true){
			return false;
		}
		return true;
	}

	/**
	 * 新增一个组之后：新增一个屏、一个组与屏关联关系、一个屏与摄像机关联关系
	 * @param 	groupId
	 * @param 	vidiconId
	 * @return	新增成功：true
	 * 		           新增失败：false
	 * @author	chenyan
	 */
	public Boolean addInitScreenVidicon(String groupId, String vidiconId){

		//1、新增分屏
		Screen screen = new Screen();
		String screenId = DataUtils.getUUID();
		screen.setId(screenId);//主键id
		screen.setSplit_screen_name("初始化屏一");//分屏名称
		screen.setSet_default(1);//是否设置为默认（1：默认，0：非默认）

		int addScreenResult = screenService.addScreen(screen);
		if(addScreenResult < 1){
			return false;
		}

		//2、新增分组分屏关系
		GroupScreen groupScreen = new GroupScreen();
		groupScreen.setId(DataUtils.getUUID());
		groupScreen.setVidicon_group_id(groupId);	//分组id
		groupScreen.setSplit_screen_id(screenId);	//分屏id
		groupScreen.setSort(1);						//分屏排序

		int addGroupScreenResult = groupScreenService.addGroupScreen(groupScreen);
		if(addGroupScreenResult < 1){
			return false;
		}

		//3、新增分屏摄像机关系
		ScreenVidicon screenVidicon = new ScreenVidicon();
		screenVidicon.setId(DataUtils.getUUID());
		screenVidicon.setVidicon_id(vidiconId);
		screenVidicon.setSplit_screen_id(screenId);
		screenVidicon.setScreen_position(1);

		int addScreenVidiconResult = screenVidiconService.addScreenVidicon(screenVidicon);
		if(addScreenVidiconResult<1){
			return false;
		}

		return true;
	}
	
	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------

	/*
	 * 三、视频直播
	 */

	/**
	 * 7、获取某种分屏模式下的所有屏
	 * @param 	screenType
	 * @return
	 * @author 	chenyan
	 */
	public List<List<Map<String, Object>>> getScreenListByScreenType(int screenType) {
		//1、获取某种分屏模式下的所有组（不包含初始化组）
		List<Group> groupList = groupService.getGroupListByScreenTypeNoInit(screenType);
		if(groupList.size()<1){
			return null;
		}
		List<List<Map<String, Object>>> result = new ArrayList<List<Map<String, Object>>>() ;
		for(int i=0; i<groupList.size();i++){
			List<Map<String, Object>> screenList = new ArrayList<Map<String, Object>>();
			//2、获取某个组下的所有组与屏关系列表（Dto）
			List<GroupScreenDto> groupScreenList = groupScreenService.getScreenListByGroupIdDto(groupList.get(i).getId());
			for(int j=0; j<groupScreenList.size();j++){
				Map<String, Object> screen = new HashMap<String, Object>();
				screen.put("screen_id", groupScreenList.get(j).getSplit_screen_id());
				screen.put("screen_name", groupScreenList.get(j).getSplit_screen_name());
				screen.put("is_important", groupList.get(i).getWhether_important());
				screenList.add(screen);	
			}
			result.add(screenList);
		}
		return result;
	}

}