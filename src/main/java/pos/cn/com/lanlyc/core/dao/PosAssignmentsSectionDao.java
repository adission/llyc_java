package pos.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import pos.cn.com.lanlyc.core.dto.PosAssignmentsSectionDto;
import pos.cn.com.lanlyc.core.po.PosAssignmentsSection;

@Service
public class PosAssignmentsSectionDao extends MySQLMapper<PosAssignmentsSection>{

	/*
	 * 查询指定图层上指定类型的区域列表
	 */
	public List<PosAssignmentsSectionDto> selectAssignmentsSectionList(String type,String layer_id) {
		StringBuffer sql=new StringBuffer("select a.id,a.name,a.type,a.endLongitude,a.endLatitude,"
				+ "a.startLatitude,a.startLongitude,a.secColor,f.layer_name layerName from pos_t_assignmentsSection a,"
				+ "pos_t_floor f where a.layer_id=:layer_id and a.layer_id=f.id");
		Map<String, Object> paramMap = new HashMap<>();
		if((type != null && !"".equals(type))) {
			sql.append(" and type=:type ");
			paramMap.put("type", type);
		}		
		paramMap.put("layer_id", layer_id);
		List<PosAssignmentsSectionDto> result = findList(sql, paramMap, PosAssignmentsSectionDto.class);	
		return result;

	}
	
	/*
	 * 通过区域id获取区域
	 */
	public PosAssignmentsSectionDto getPosAssignmentsSectionById(String id) {
		StringBuffer sql=new StringBuffer("select  a.id,a.name,a.type,a.endLongitude,a.endLatitude,"
				+ "a.startLatitude,a.startLongitude,a.secColor,f.layer_name layerName from pos_t_assignmentsSection a,"
				+ "pos_t_floor f  where a.id=:id and a.layer_id=f.id");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		List<PosAssignmentsSectionDto> result = findList(sql, paramMap, PosAssignmentsSectionDto.class);	
		if(result!=null && result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
}
