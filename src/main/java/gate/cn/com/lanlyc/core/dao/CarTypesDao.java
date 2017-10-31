package gate.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.CarTypes;
import gate.cn.com.lanlyc.core.po.PapTypes;

@Service
public class CarTypesDao   extends MySQLMapper<CarTypes>{
	/**
	 * 查询所有的车辆类型 
	 * 
	 * @return response
	 */
	public List<CarTypes> getAllCarTypes() {
		String sql="select name,value from gate_t_car_types";
		Map<String, Object> paramMap = new HashMap<>();
		List<CarTypes> result = findList(sql, paramMap, CarTypes.class);	
		if(result!=null && result.size()>0){
			return result;
		}else{
			return null;
		}
	}
	
	/**
	 * 分页获取证件类型
	 * @param page
	 * @param keyword
	 * @author jiangyanyan
	 * @return Page<PapTypes>
	 */
	 public Page<CarTypes> queryCarTypes(Page<CarTypes>page,String keyword) {
		 String sql = "SELECT * FROM gate_t_car_types WHERE name LIKE :name";
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("name", "%"+keyword+"%");
		 Page<CarTypes> result = getPage(sql,paramMap,page,CarTypes.class);
    	 return result;
	 }
	 
	 
	 /**
	  * 根据数据的车辆名称，判断名称是否存在
	  * @param name 车辆类型名称
	  * @author jiangyanyan
	  * @return true 存在 false不存在
	  */
	 public boolean isCreateCarTypes(String name) {
		 String sql = "SELECT * FROM gate_t_car_types WHERE name=:name";
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("name", name);
		 List<CarTypes> ret = findList(sql,paramMap);
		 if(ret==null || ret.size()==0) {
    		return false; 
    	 }else {
    		return true;
    	 }	 
	 }
	 
	 
	 /**
	  * 根据车辆id列表删除车辆类型
	  * @param idsList 要删除的车辆id列表
	  * @author jiangyanyan
	  * @return true 删除成功   false删除失败
	  */
	 public boolean isDeleteCarTypes(List<String> idsList) {
		 String sql = "DELETE FROM gate_t_car_types WHERE gate_t_car_types.name IN (:idsList)";
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("idsList", idsList);
		 int result = this.execute(sql, paramMap);
		 if(result==1) {
			 return true;
		 }else {
			 return false;
		 } 
	 }
	 
	 
	 /**
	  * 根据车辆类型的旧名称修改成车辆类型的新名称
	  * @param newName 车辆类型的新名称
	  * @param oldName 车辆类型的旧名称
	  * @author jiangyanyan
	  * @return true 修改成功  false 修改失败
	  */
	 public boolean isAditCarTypes(String newName,String oldName) {
		 String sql = "UPDATE gate_t_car_types SET name=:newName WHERE name=:oldName";
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("newName", newName);
		 paramMap.put("oldName", oldName);
		 int result = this.execute(sql, paramMap);
		 if(result==1) {
			 return true;
		 }else {
			 return false;
		 } 
	 }
	 
	
	
}
