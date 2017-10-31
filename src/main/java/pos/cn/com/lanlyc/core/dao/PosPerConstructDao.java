package pos.cn.com.lanlyc.core.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import pos.cn.com.lanlyc.core.dto.PosSimpleUserDto;


@Service
public class PosPerConstructDao extends MySQLMapper<GateUser> {
	
	
	/**
	 * 查询人员列表分页
	 * @param layer_id 图层id
	 * @param searchContent 搜索的内容
	 * @author jiangyanyan
	 * @return 返回分页的人员列表
	 */
	public List<GateUserInfoView> getConstructPersonList(String worker_type,String layer_id){
		StringBuffer sql = new StringBuffer("SELECT a.*,b.geo_x,b.geo_y ,b.layer_id,b.cardType from gate_v_user_info a LEFT JOIN pos_t_indentificationCard b on "
				+ "a.card_sn = b.card_sn WHERE a.worker_type =:worker_type  AND b.layer_id =:layer_id");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("worker_type", worker_type);
		paramMap.put("layer_id", layer_id);
		List<GateUserInfoView> result = findList(sql,paramMap,GateUserInfoView.class);
		return result;
	}
	
	/**
	 * 查询图层内人员列表(以距离当前时间30s内为准)
	 * @param layer_id 图层id
	 * @author jiangyanyan
	 * @return 返回查询的人员列表
	 */
	public List<GateUserInfoView> getConstructPersonList2(String layer_id){
		StringBuffer sql = new StringBuffer("SELECT b.name,b.id,b.card_sn,b.worker_type,b.mobile,b.gender,b.staff_visitor from (SELECT perId as aperId, max(TIMESTAMP) as maxtime,stationId from pos_t_trajectory GROUP BY perId) a," + 
				"gate_v_user_info b " + 
				"WHERE a.aperId=b.id and (b.card_sn is not null AND b.card_sn !='') AND a.stationId =:layer_id and UNIX_TIMESTAMP(a.maxtime)>:time");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("layer_id", layer_id);
		paramMap.put("time", (new Date().getTime()-60*1000)/1000);
//		paramMap.put("time", new Date().getTime()-30*1000);
		List<GateUserInfoView> result = findList(sql,paramMap,GateUserInfoView.class);
		return result;
	}
	
	
	/**
	 * 根据搜索的内容获取所有绑定定位卡的人员
	 * @param layer_id 图层id
	 * @param searchCon 搜索内容
	 * @author jiangyanyan
	 * @return 返回定位卡人员列表
	 */
	public List<GateUserInfoView> getSearchUserInfoList(String searchCon){
		StringBuffer sql = new StringBuffer("SELECT b.*,a.* from (SELECT perId as aperId, max(TIMESTAMP) as maxtime,perLongitude as geo_x,perLatitude as geo_y,stationId as layer_id from pos_t_trajectory GROUP BY perId) a," + 
				"gate_v_user_info b " + 
				"WHERE a.aperId=b.id and (b.card_sn is not null AND b.card_sn !='') AND (b.name LIKE :name OR b.card_sn LIKE :card_sn) AND UNIX_TIMESTAMP(a.maxtime)>:time");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("name", "%"+searchCon+"%");
		paramMap.put("card_sn", "%"+searchCon+"%");
		paramMap.put("time", (new Date().getTime()-60*1000)/1000);
		List<GateUserInfoView> result = findList(sql, paramMap, GateUserInfoView.class);
		return result;
	}
	
	/**
	 * 实时监控根据人员名称定位卡图层id查询
	 * @param layer_id 图层id
	 * @param searchCon 搜索内容
	 * @author jiangyanyan
	 * @return CAST(t.perLatitude AS DECIMAL(18,6))
	 */
	public List<PosSimpleUserDto> getSearchUserInfoList2(String layer_id){
		String sql = "select g.*,c.typeName,c.color from (SELECT CAST(b.geo_x AS DECIMAL(18,6)) as x,CAST(b.geo_y AS DECIMAL(18,6)) as y,b.layer_id,b.cardType from gate_v_user_info a LEFT JOIN pos_t_indentificationCard b on "
				+ "a.card_sn = b.card_sn) g left join pos_t_cardType c on g.cardType= c.id WHERE g.layer_id =:layer_id";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("layer_id", layer_id);
		List<PosSimpleUserDto> result = findList(sql, paramMap, PosSimpleUserDto.class);
		return result;
	}
	
	/**
	 * 查询人员列表
	 * @param constructId 工地id
	 * @param name  人员名称
	 * @param mobile 人员手机号
	 * @author jiangyanyan
	 * @return 返回人员列表
	 */
	public List<GateUserInfoView> getConstructPersonListNoPage(){
//		StringBuffer sql = new StringBuffer("select u.* from gate_v_user_info u  inner join pos_t_perConstruct pc on u.id=pc.perId where pc.constructId=:constructId");
		StringBuffer sql = new StringBuffer("select * from gate_v_user_info");
		List<GateUserInfoView> result = findList(sql, null, GateUserInfoView.class);
		return result;
	}
	
	
	/**
	 * 根据人员id获取人员实体
	 * @param userId 人员id
	 * @author jiangyanyan
	 * @return 虚拟表人员列表
	 */
	public List<GateUserInfoView>getUserByUserId(String userId){
		String sql = new String("select gg.* from gate_v_user_info gg where gg.id=:id");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", userId);
		List<GateUserInfoView> result = findList(sql,paramMap,GateUserInfoView.class);
		return result;
	}
	
	
	/**
	 * 新增的卡与人员绑定
	 * @param userId 人员id
	 * @param card_sn 新增的卡号
	 * @return 返回true绑定成功，返回false绑定失败
	 */
	public boolean updateUserByUserId(String userId,String card_sn) {
		String sql = new String("UPDATE gate_v_user_info SET card_sn=:card_sn WHERE id=:id");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("card_sn", card_sn);
		paramMap.put("id",userId);
		int result = this.execute(sql, paramMap);
		if(result==1) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 根据定位卡号查询人员列表
	 * @param card_sn 人员定位卡
	 * @author jiangyanyan
	 * @return 返回人员列表
	 */
	public List<GateUserInfoView> getConstructPersonListByCard_sn(Page<GateUserInfoView>page,String card_sn ){
		StringBuffer sql = new StringBuffer("SELECT card_sn FROM gate_v_user_info WHERE card_sn:=card_sn");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("card_sn", card_sn);
		List<GateUserInfoView> result = findList(sql, paramMap, GateUserInfoView.class);
		return result;
	}
	
}
