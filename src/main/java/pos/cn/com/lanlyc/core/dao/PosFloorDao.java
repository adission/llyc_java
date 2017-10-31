package pos.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import pos.cn.com.lanlyc.core.po.PosFloor;


/**
 * @author jiangyanyan
 * @date 2017年9月27日 17:29:23
 * @version 1.0
 */

@Service
public class PosFloorDao extends MySQLMapper<PosFloor>{
	
	
	/**
	 * 查询图层
	 * @author jiangyanyan
	 * @return 返回项目中的所有图层列表
	 */
	public List<PosFloor> getFloorInfo(){
		String sql = new String("SELECT * FROM pos_t_floor");
		List<PosFloor> result = findList(sql,null,PosFloor.class);
		return result;
	}
	
	
	/**
	 * 查询数据表中第一个图层
	 * @author jiangyanyan
	 * @return 返回项目中的第一个图层列表
	 */
	public List<PosFloor> getOneFloorInfo(){
		String sql = "SELECT * FROM pos_t_floor LIMIT 0,1";
		List<PosFloor> result = findList(sql,null,PosFloor.class);
		return result;
	}
	
	
	/**
	 *根据图层id查询
	 * @author jiangyanyan
	 * @return 返回图层对象
	 */
	public PosFloor getFloorObject(String id) {
		String sql = "SELECT * FROM pos_t_floor p WHERE p.id =:id";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", id);
		PosFloor posF = findList(sql,paramMap,PosFloor.class).get(0);
		return posF;
		
	}

	
	/**
	 * 根据图层id删除图层
	 * @param layer_id
	 * @return 返回true时删除成功，返回false时删除失败
	 */
	public boolean deletePosFloorInfo(String layer_id) {
		int res = this.delete(layer_id);
		if(res==1) {
			return true;
		}else {
			return false;
		}
	}
	
	
}
