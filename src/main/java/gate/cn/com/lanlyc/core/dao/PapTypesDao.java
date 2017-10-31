package gate.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.PapTypes;
import pos.cn.com.lanlyc.core.dto.PosCardDto;
import cn.com.lanlyc.base.util.Page;

@Service
public class PapTypesDao  extends MySQLMapper<PapTypes>{
	/**
	 * 查询所有的证件类型 
	 * 
	 * @return response
	 */
	public List<PapTypes> getAllPapTypes() {
		String sql="select name,value from gate_t_pap_types";
		Map<String, Object> paramMap = new HashMap<>();
		List<PapTypes> result = findList(sql, paramMap, PapTypes.class);	
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
	 public Page<PapTypes> queryUserAndCard(Page<PapTypes>page,String keyword) {
		 String sql = "SELECT * FROM gate_t_pap_types WHERE name LIKE :name";
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("name", "%"+keyword+"%");
		 Page<PapTypes> result = getPage(sql,paramMap,page,PapTypes.class);
    	 return result;
	 }
	
	 /**
	  * 根据数据的证件名称，判断名称是否存在
	  * @param name 证件类型名称
	  * @author jiangyanyan
	  * @return true 存在 false不存在
	  */
	 public boolean isCreatePapTypes(String name) {
		 String sql = "SELECT * FROM gate_t_pap_types WHERE name=:name";
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("name", name);
		 List<PapTypes> ret = findList(sql,paramMap);
		 if(ret==null || ret.size()==0) {
    		return false; 
    	 }else {
    		return true;
    	 }	 
	 }
	
	 
	 /**
	  * 根据证件id列表删除证件类型
	  * @param idsList 要删除的证件id列表
	  * @author jiangyanyan
	  * @return true 删除成功   false删除失败
	  */
	 public boolean isDeletePapTypes(List<String> idsList) {
		 String sql = "DELETE FROM gate_t_pap_types WHERE gate_t_pap_types.name IN (:idsList)";
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
	  * 根据证件类型的旧名称修改证件类型的新名称
	  * @param newName 证件类型的新名称
	  * @param oldName 证件类型的旧名称
	  * @author jiangyanyan
	  * @return true 修改成功  false 修改失败
	  */
	 public boolean isAditPapTypes(String newName,String oldName) {
		 String sql = "UPDATE gate_t_pap_types SET name=:newName WHERE name=:oldName";
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
