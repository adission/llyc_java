package pos.cn.com.lanlyc.core.dao;
/**
 * @author cjt
 * 人员轨迹dao
 */
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import pos.cn.com.lanlyc.core.dto.PosSimpleUserDto;
import pos.cn.com.lanlyc.core.dto.PosTrajectoryDto;
import pos.cn.com.lanlyc.core.po.PosTrajectory;

@Service
public class PosTrajectoryDao extends MySQLMapper<PosTrajectory>{
	
	/** 
	 * @author cjt
	 * 查询两小时前的人员轨迹记录，及两小时前到中间某个时间段的人员轨迹记录
	 * param  userId  人员id 必传
	 * 		  endtime 结束时间(UNIX_TIMESTAMP转换的时间戳是已s为单位，所以一般时间戳参数除以1000)
	 * 返回值[{x:..,y:..}]
	 */
	public List<PosTrajectoryDto> getHistoryPos(String userId, long endtime, long starttime){
		String sql = "select CAST(t.perLatitude AS DECIMAL(18,7)) as x,CAST(t.perLongitude AS DECIMAL(18,7)) as y,DATE_FORMAT(t.timeStamp, '%H:%i:%s') as time "
				+ " from pos_t_trajectory t where t.perId=:perId and UNIX_TIMESTAMP(t.timeStamp)>:startTime and UNIX_TIMESTAMP(t.timeStamp)<:endTime order by timeStamp asc";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("perId", userId);
		paramMap.put("startTime", starttime/1000);
		paramMap.put("endTime", endtime/1000);
		List<PosTrajectoryDto> ret = findList(sql, paramMap, PosTrajectoryDto.class);
		
		return ret;
	}
//	public List<PosTrajectoryDto> getHistoryPos2(String userId, long endtime, long starttime){
//		String sql = "select t.perLatitude as x,t.perLongitude as y,t.timeStamp as time t.perId as userId g.name as userName from pos_t_trajectory t "
//				+ "left join gate_t_user g on t.perId = g.id where perId=:perId and timeStamp>:startTime and timeStamp<:endTime order by timeStamp asc";
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("perId", userId);
//		paramMap.put("startTime", starttime);
//		paramMap.put("endTime", endtime);
//		List<PosTrajectoryDto> ret = findList(sql, paramMap, PosTrajectoryDto.class);
//		
//		return ret;
//	}
	
	/**
	 * @author cjt
	 * 查询人员当前坐标
	 * param  userId  人员id 必传
	 * 返回值{x:..,y:..}
	 */
	public List<PosTrajectoryDto> getCurrentPos(List<String> userId){
		String sql = "SELECT b.perLatitude as x, b.perLongitude as y, a.time as time from (select perId,max(timestamp) as time from pos_t_trajectory GROUP BY perid) a,pos_t_trajectory b WHERE a.perId=b.perId and a.time=b.timeStamp and a.perId in (:perId)";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("perId", userId);
		
		List<PosTrajectoryDto> ret = this.findList(sql, paramMap, PosTrajectoryDto.class);
		
		return ret;
	}
	
	/**
	 * 根据图层id查询图层内最新人员位置(已距离当前时间60s内的时间为准)
	 * 
	 */
	public List<PosSimpleUserDto> getAllUser(String layerId){
		String sql = "SELECT CAST(c.perLatitude AS DECIMAL(19,7)) as x, CAST(c.perLongitude AS DECIMAL(19,7)) as y, b.name,c.perId,f.typeName,f.color,f.id as cardType,c.stationId from (SELECT perId as aperId, max(TIMESTAMP) as maxtime from pos_t_trajectory GROUP BY perId) a,gate_t_user b," + 
				"pos_t_trajectory c,pos_t_indentificationCard d,pos_t_cardType f WHERE a.aperId=c.perId and a.maxtime=c.timeStamp and c.perId=b.id AND b.card_sn=d.card_sn AND b.staff_visitor=f.id and d.layer_id=c.stationId and c.stationId =:layerId and UNIX_TIMESTAMP(a.maxtime)>:time";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("layerId", layerId);
		paramMap.put("time", (new Date().getTime()-60*1000)/1000);
		List<PosSimpleUserDto> res = this.findList(sql, paramMap, PosSimpleUserDto.class);
		return res;
	}
	
	/**
	 * 根据人员id获取人员坐标点
	 * @param userId
	 * @author jiangyanyan 
	 * @return
	 */
	public PosTrajectory getPosTrajectoryEntity(String userId){
		String sql = "select *from pos_t_trajectory where perId=:perId order by timeStamp desc limit 0,1 ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("perId", userId);
		PosTrajectory posT = this.findList(sql, paramMap, PosTrajectory.class).get(0);
		return posT;
	}
	
	/**
	 * 根据图层列表获取图层中对应的人
	 * @param userId
	 * @author jiangyanyan 
	 * @return 返回人员轨迹列表
	 */
	public List<PosTrajectory> getLayerUserCount(List<String> layerIds){
		
		String sql = "SELECT a.* from (SELECT perId, max(TIMESTAMP) as maxtime, stationId from pos_t_trajectory GROUP BY perId) a WHERE UNIX_TIMESTAMP(a.maxtime)>:time AND a.stationId IN (:layerIds)";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("layerIds", layerIds);
		paramMap.put("time", (new Date().getTime()-60*1000)/1000);
		List<PosTrajectory> posList = this.findList(sql, paramMap, PosTrajectory.class);
		return posList;
	}
	
	public void deleteAll() {
		String sql = "delete FROM pos_t_trajectory ";
		this.execute(sql, null);
		
	}
	
}
