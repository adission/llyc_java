package pos.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import pos.cn.com.lanlyc.core.dto.PosBaseStationDto;
import pos.cn.com.lanlyc.core.po.PosBaseStation;

@Service
public class PosBaseStationDao extends MySQLMapper<PosBaseStation>{
	/*
	 * 查询指定图层上指定类型的基站列表
	 */
	public List<PosBaseStationDto> selectBaseStationList(String type,String layer_id) {
		StringBuffer sql=new StringBuffer("select b.id,b.type,b.geo_y,b.geo_x,f.layer_name layerName from pos_t_baseStation b,"
				+ "pos_t_floor f where b.layer_id=:layer_id and b.layer_id=f.id");		
		Map<String, Object> paramMap = new HashMap<>();
		if(type!=null&&!"".equals(type)) {
			sql.append(" and type=:type ");
			paramMap.put("type", type);
		}
		
		paramMap.put("layer_id", layer_id);
		List<PosBaseStationDto> result = findList(sql, paramMap, PosBaseStationDto.class);	
		if(result!=null && result.size()>0){
			return result;
		}else{
			return null;
		}

	}
	
	/*
	 * 通过基站id获取基站
	 */
	public PosBaseStationDto getPosBaseStationById(String id) {
		String sql="select b.id,b.type,b.geo_y,b.geo_x,b.mc,b.base_number,b.status,b.ip,b.port,b.yxjl,f.layer_name layerName from pos_t_baseStation b,"
				+ "pos_t_floor f  where b.id=:id and b.layer_id=f.id";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		List<PosBaseStationDto> result = findList(sql, paramMap, PosBaseStationDto.class);	
		if(result!=null && result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
}
