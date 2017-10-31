package video.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import video.cn.com.lanlyc.core.dao.ScreenDao;
import video.cn.com.lanlyc.core.po.Screen;

/**
 * 分屏Service
 * @author chenyan
 * @date 2017年9月12日 12:00:11
 * @version v1.0
 */
@Service
public class ScreenService {
	
	@Autowired
	ScreenDao screenDao;
	
	/**
	 * 2、分组管理新增
	 * 			新增一条分屏
	 * 二、初始化默认组
	 * 			新增一条分屏
	 * @param 	screen
	 * @return 	新增记录数	int
	 * @author 	chenyan
	 */
	public int addScreen(Screen screen){
		return screenDao.addScreen(screen);
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			通过id删除一条屏
	 * @param 	id
	 * @return 	删除记录数	int
	 * @author 	chenyan
	 */
	public int deleteScreenById(String id) {
		return screenDao.deleteScreenById(id);
	}

//	/**
//	 * 通过id更新一条分屏
//	 * @param screen
//	 * @return 更新记录数	int
//	 * @author chenyan
//	 * used
//	 */
//	public int updateScreenByScreenId(Screen screen) {
//		return screenDao.updateScreenByScreenId(screen);
//	}

//	/**
//	 * 通过id获取一条分屏
//	 * @param id
//	 * @return Screen
//	 * @author chenyan
//	 */
//	public Screen getScreenById(int id) {
//		return screenDao.getScreenById(id);
//	}
	
}