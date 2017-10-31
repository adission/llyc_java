package video.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import video.cn.com.lanlyc.core.dao.ScreenVidiconDao;
import video.cn.com.lanlyc.core.dto.ScreenVidiconDto;
import video.cn.com.lanlyc.core.po.ScreenVidicon;
import video.cn.com.lanlyc.core.po.VidiconInfo;

/**
 * 屏与摄像机关联表Service
 * @author chenyan
 * @date 2017年9月13日 13:59:56
 * @version v1.0
 */
@Service
public class ScreenVidiconService {

	@Autowired
	ScreenVidiconDao screenVidiconDao;

	@Autowired
	GroupScreenService groupScreenService;

	@Autowired
	VidiconService vidiconService;

	/**
	 * 2、分组管理新增
	 * 			新增一条屏与摄像机关联关系
	 * 二、初始化默认组
	 * 			新增一条屏与摄像机关联关系
	 * @param 	screenVidicon
	 * @return 	新增记录数		int
	 * @author 	chenyan
	 */
	public int addScreenVidicon(ScreenVidicon screenVidicon){
		return screenVidiconDao.addScreenVidicon(screenVidicon);
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			获取某个屏下的所有屏和摄像机关联关系（Dao）
	 * @param 	screenId
	 * @return 	某个屏下的所有屏与摄像机关联列表	List
	 * @author 	chenyan
	 */
	public List<ScreenVidicon> getScreenVidiconListByScreenIdDao(String screenId) {
		return screenVidiconDao.getScreenVidiconListByScreenIdDao(screenId);
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			通过id删除一条屏与摄像机关联关系
	 * @param 	id
	 * @return 	删除记录数	int
	 * @author 	chenyan
	 */
	public int deleteScreenVidiconById(String id) {
		return screenVidiconDao.deleteScreenVidiconById(id);
	}
	
	/**
	 * 5、分组管理详情
	 * 			通过屏id获取某个屏下的所有屏与摄像机关联关系列表
	 * 三、4、获取某个组下的第几屏
	 * 			通过屏id获取某个屏下的所有屏与摄像机关联关系列表
	 * 三、5、获取某个屏
	 * 			通过屏id获取某个屏下的所有屏与摄像机关联关系列表
	 * 三、6、获取某个屏以外的其他屏的所有摄像头列表
	 * 			通过屏id获取某个屏下的所有屏与摄像机关联关系列表
	 * @param 	screenId
	 * @return 	某个屏下的所有屏与摄像机关联关系列表	List
	 * @author 	chenyan
	 */
	public List<ScreenVidiconDto> getScreenVidiconListByScreenId(String screenId) {
		return screenVidiconDao.getScreenVidiconListByScreenId(screenId);
	}
	
	/**
	 * 三、4、获取某个组下的第几屏
	 * @param string
	 * @param int1
	 * @return
	 * used
	 */
	public List<ScreenVidiconDto> getScreenVidiconListByGroupIdAndSort(String groupId, int screenSort) {
		//1、通过组id和屏排序获取屏id
		String screenId = groupScreenService.getScreenIdByGroupIdAndScreenSort(groupId, screenSort);
		//2、通过屏id获取某个屏下的所有屏与摄像机关联关系列表
		List<ScreenVidiconDto> result = getScreenVidiconListByScreenId(screenId);
		return result;
	}
	
	/**
	 * 三、6、获取某个屏以外的其他屏的所有摄像头列表
	 * @param screenId
	 * @return
	 */
	public List<VidiconInfo> getOtherVidiconListByScreenId(String screenId) {
		//1、通过屏id获取某个屏下的所有屏与摄像机关联关系列表
		List<ScreenVidiconDto> screenVidiconList = this.getScreenVidiconListByScreenId(screenId);
		//2、获取所有摄像头列表（调用接口）
		List<VidiconInfo> vidiconList = vidiconService.getAllVidiconService(2);
		//3、剔除所有摄像头中该屏中的摄像头
		for(int i=0; i<vidiconList.size();i++){
			for(int j=0; j<screenVidiconList.size(); j++){
				String screenVidiconId = screenVidiconList.get(j).getVidicon_id();
				if(screenVidiconId.equals(vidiconList.get(i).getId())){
					screenVidiconList.remove(j);
					vidiconList.remove(i);
					continue;
				}
			}	
		}
		return vidiconList;
	}

//	/*
//	 * 获取除了第几个屏之外的其他摄像头列表
//	 */
//	public List<VidiconInfo> getOtherScreenVidiconListByGroupIdAndSort(String groupId, int screenSort) {
//		List<ScreenVidiconDto> screenVidiconList = this.getScreenVidiconListByGroupIdAndSort(groupId, screenSort);
//		List<VidiconInfo> vidiconList = vidiconService.getAllVidiconService(2);
//		for(int i=0; i<vidiconList.size();i++){
//			String vidiconId = vidiconList.get(i).getId();
//			for(int j=0; j<screenVidiconList.size(); j++){
//				String screenVidiconId = screenVidiconList.get(j).getVidicon_id();
//				if(screenVidiconId.equals(vidiconId)){
//					vidiconList.remove(i);
//				}
//			}	
//		}
//		return vidiconList;
//	}
	

//	/**
//	 * 通过id更新一条屏与摄像机关联
//	 * @param screenVidicon
//	 * @return 更新记录数	int
//	 * @author chenyan
//	 * used
//	 */
//	public int updateScreenVidiconById(ScreenVidicon screenVidicon) {
//		return screenVidiconDao.updateScreenVidiconById(screenVidicon);
//	}

	//	/**
	//	 * 通过id获取一条屏与摄像机关联
	//	 * @param id
	//	 * @return ScreenVidicon
	//	 * @author chenyan
	//	 */
	//	public ScreenVidicon getScreenVidiconById(int id) {
	//		return screenVidiconDao.getScreenVidiconById(id);
	//	}

}