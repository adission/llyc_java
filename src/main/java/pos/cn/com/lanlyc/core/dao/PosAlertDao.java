package pos.cn.com.lanlyc.core.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import pos.cn.com.lanlyc.core.dto.PosAlertDto;
import pos.cn.com.lanlyc.core.po.PosAlert;
/**
 * 
 * @author cjt
 * 告警dao
 *
 */
@Service
public class PosAlertDao extends MySQLMapper<PosAlert>{
	
	/**
	 * 查询最新告警（30s以内）
	 * param  layer_id 图层id
	 */
	public List<PosAlert> newAlert(String layer_id){
		String sql = "select * from pos_t_alert where timeStamp>:timeStamp and area_id=:area_id";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("timeStamp", new Date().getTime()-30*1000);
		paramMap.put("area_id", layer_id);
		List<PosAlert> res = findList(sql, paramMap, PosAlert.class);
		return res;
	}
	
	/**
	 * 查询历史告警
	 * param 
	 * keyword 模糊搜索关键字
	 * type 告警类型
	 */
	public Page<PosAlertDto> history(Page<PosAlertDto> page,String keyword,String type,String status){
		StringBuilder sql = new StringBuilder("SELECT g1.*,g2.name as processName,g2.card_sn as card");
		sql.append(" from (SELECT p.*,g.name userName from pos_t_alert p LEFT JOIN gate_t_user g ON p.perId=g.id)g1 LEFT JOIN gate_t_user g2");
		sql.append(" ON g1.processUser = g2.id where 1=1");
		Map<String, Object> paramMap = new HashMap<>();
		if(!DataUtils.isNullOrEmpty(keyword)) {
			sql.append(" and ( g1.alertContext like :alertContext ");
			sql.append(" or g1.userName like :name ");
			
			sql.append(") ") ;
			
			paramMap.put("alertContext", "%"+keyword+"%");
			paramMap.put("name", "%"+keyword+"%");
		} 
		if(!DataUtils.isNullOrEmpty(type)) {
			sql.append(" and g1.alertType=:type");
			paramMap.put("type", type);
		}
		if(!DataUtils.isNullOrEmpty(status)) {
			sql.append(" and g1.status=:status");
			paramMap.put("status", status);
		}
		sql.append(" order by timeStamp desc");
		Page<PosAlertDto> res = getPage(sql, paramMap, page, PosAlertDto.class);
		return res;
		
	}
	/**
	 * 查询所有未处理的报警
	 */
	public List<PosAlert> getAllWarning(){
		String sql = "select * from pos_t_alert where status =:status order by timeStamp desc";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("status", "未处理");
		List<PosAlert> res = this.findList(sql, paramMap);
		return res;
	}
}
