package video.cn.com.lanlyc.core.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.dto.ScreenVidiconDto;
import video.cn.com.lanlyc.core.po.ScreenVidicon;

/**
 * 屏和摄像机关联表Dao
 * @author chenyan
 * @date 2017年9月13日 14:07:00
 * @version v1.0
 */
@Service
public class ScreenVidiconDao extends MySQLMapper<ScreenVidicon>{

	/**
	 * 2、分组管理新增
	 * 			新增一条屏与摄像机关联关系
	 * @param screenVidicon
	 * @return int
	 * @author chenyan
	 */
	public int addScreenVidicon(ScreenVidicon screenVidicon){
		return this.save(screenVidicon);
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			获取某个屏下的所有屏和摄像机关联关系（Dao）
	 * @param 	screenId
	 * @return 	某个屏下的所有屏与摄像机关联列表	List
	 * @author 	chenyan
	 */
	public List<ScreenVidicon> getScreenVidiconListByScreenIdDao(String screenId) {
		StringBuilder sql = new StringBuilder("select * from video_t_screen_vidicon_relation sv where 1=1 ");
		QMap map = new QMap();
		if (isValid(screenId)){
			sql.append(" and sv.split_screen_id = :split_screen_id ;");
			map.put("split_screen_id", screenId);
		}
        List<ScreenVidicon> list = findList(sql, map, ScreenVidicon.class);
		return list;
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			通过id删除一条屏与摄像机关联关系
	 * @param 	id
	 * @return 	int
	 * @author 	chenyan
	 */
	public int deleteScreenVidiconById(String id) {
		return this.delete(id);
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
		StringBuilder sql = new StringBuilder("select sv.*,v.vidicon_ip,v.vidicon_name,v.vidicon_number from video_t_screen_vidicon_relation sv inner join video_t_vidicon_info v ");
		QMap map = new QMap();
		sql.append(" on sv.vidicon_id = v.id ");
		if (isValid(screenId)){
			sql.append(" and sv.split_screen_id = :split_screen_id ");
			map.put("split_screen_id", screenId);
		}
        List<ScreenVidiconDto> list = findList(sql, map, ScreenVidiconDto.class);
		return list;
	}

//	/**
//	 * 通过id更新一条屏与摄像机关联
//	 * @param ScreenVidicon
//	 * @return int
//	 * @author chenyan
//	 * used
//	 */
//	public int updateScreenVidiconById(ScreenVidicon screenVidicon) {
//		return this.update(screenVidicon,false);
//	}

//	/**
//	 * 通过id获取
//	 * @param id
//	 * @return 
//	 * @author chenyan
//	 * 
//	 */
//	public ScreenVidicon getScreenVidiconById(int id) {
//		return this.get(id);
//	}

}