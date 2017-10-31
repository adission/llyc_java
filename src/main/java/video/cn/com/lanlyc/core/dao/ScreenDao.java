package video.cn.com.lanlyc.core.dao;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import video.cn.com.lanlyc.core.po.Screen;

/**
 * 分屏Dao
 * @author chenyan
 * @date 2017年9月12日 11:45:16
 * @version v1.0
 */
@Service
public class ScreenDao extends MySQLMapper<Screen>{

	/**
	 * 2、分组管理新增
	 * 			新增一条分屏
	 * @param 	screen
	 * @return 	int
	 * @author 	chenyan
	 */
	public int addScreen(Screen screen){
		return this.save(screen);
	}
	
	/**
	 * 3、分组管理单条删除
	 * 			通过id删除一条屏
	 * @param 	id
	 * @return 	int
	 * @author 	chenyan
	 */
	public int deleteScreenById(String id) {
		return this.delete(id);
	}
	
//	/**
//	 * 通过id更新一条分屏
//	 * @param screen
//	 * @return int
//	 * @author chenyan
//	 * used
//	 */
//	public int updateScreenByScreenId(Screen screen) {
//		return this.update(screen,false);
//	}
	
//	/**
//	 * 通过id获取分屏
//	 * @param id
//	 * @return Split_Screen
//	 * @author chenyan
//	 * 
//	 */
//	public Screen getScreenById(int id) {
//		return this.get(id);
//	}

}