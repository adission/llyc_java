package gate.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.dto.GateExtraUserInfoDto;
import gate.cn.com.lanlyc.core.po.GateExtraUserInfo;
import gate.cn.com.lanlyc.core.po.GateUser;
import pos.cn.com.lanlyc.core.dto.PosCameraDto;
@Service
public class GateVisitDao  extends MySQLMapper<GateExtraUserInfo>{
	
	/*
	 * 分页查询所有访客列表
	 */
	public Page<GateExtraUserInfoDto> getGateVisitListByPage(Page<GateExtraUserInfoDto> page,String name,String mobile,String pap_number){
		/*StringBuffer sql=new StringBuffer("select c.*,f.layer_name layerName from"
				+ "(select * from video_t_vidicon_info)c,pos_t_floor f where 1=1 and c.layer_id=f.id ");
		if(layer_id != null && !"".equals(layer_id)) {
			sql.append(" and layer_id=:layer_id ");
		}*/
		StringBuffer sql=new StringBuffer("select a.id,a.name,a.avatar_img,a.mobile,a.card_sn,a.gonghao,b.registration_date,"
				+ "b.entry_date,b.leave_date,b.visit_reason,b.pap_number,b.car_number,c.name as cartype,d.name as paptype "
				+ "from gate_t_user a,gate_t_extra_user_info b,gate_t_car_types c,gate_t_pap_types d where b.user_id=a.id "
				+ "and b.car_type=c.value and b.pap_type=d.value ");
		
		if(StringUtils.isNotEmpty(name)||StringUtils.isNotEmpty(pap_number)||StringUtils.isNotEmpty(mobile)){
			sql.append(" and (  ");
		}
		
		if(StringUtils.isNotEmpty(name)){
			System.out.println("name    :"+name);
			name = StringEscapeUtils.escapeSql(name.toLowerCase());
			sql.append("  LOWER(a.name) LIKE '%").append(name).append("%'");
			if(StringUtils.isNotEmpty(pap_number)||StringUtils.isNotEmpty(mobile)){
				sql.append(" or");
			}
		}
		
		if(StringUtils.isNotEmpty(pap_number)){
			System.out.println("pap_number    :"+pap_number);
			sql.append("  b.pap_number LIKE '%").append(pap_number).append("%'");
			if(StringUtils.isNotEmpty(mobile)){
				sql.append(" or");
			}
		}
		
		if(StringUtils.isNotEmpty(mobile)){
			System.out.println("mobile    :"+mobile);
			sql.append("  a.mobile LIKE '%").append(mobile).append("%'");		
		}
		if(StringUtils.isNotEmpty(name)||StringUtils.isNotEmpty(pap_number)||StringUtils.isNotEmpty(mobile)){
			sql.append(" )");
		}
		
		/*if(StringUtils.isNotEmpty(name)||StringUtils.isNotEmpty(mobile)||StringUtils.isNotEmpty(pap_number)){
			name = StringEscapeUtils.escapeSql(name.toLowerCase());
			pap_number = StringEscapeUtils.escapeSql(pap_number.toLowerCase());
			sql.append(" and ( LOWER(a.name) LIKE '%").append(val)
			.append("%' OR LOWER(c.vidicon_number) LIKE '%").append(val)
			.append("%' )");

		}*/
		
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", name);
		paramMap.put("pap_number", pap_number);
		paramMap.put("mobile", mobile);
		Page<GateExtraUserInfoDto> result = getPage(sql, paramMap, page, GateExtraUserInfoDto.class);
		System.out.println(result);
		return result;
	}

	
	
	/**
	 * 根据访客id，查询访客信息
	 * @param id 访客的id
	 * @return        
	 */                
	public GateExtraUserInfoDto selectVisitorById(String id){
		/*StringBuffer sql=new StringBuffer("select a.name,a.mobile,a.avatar_img,a.card_sn,b.registration_date,"
				+ "b.entry_date,b.leave_date,b.car_type,b.car_number,b.pap_type,b.pap_number,b.visit_reason "
				+ "from gate_t_user a,gate_t_extra_user_info b where b.user_id=a.id and a.id=:id ");*/
		
		StringBuffer sql=new StringBuffer("select a.name,a.mobile,a.avatar_img,a.card_sn,b.registration_date,"
				+ "b.entry_date,b.leave_date,b.car_number,b.pap_number,c.name as cartype,d.name as paptype, b.visit_reason "
				+ "from gate_t_user a,gate_t_extra_user_info b,gate_t_car_types c,gate_t_pap_types d where b.user_id=a.id "
				+ " and a.id=:id and b.car_type=c.value and b.pap_type=d.value ");
		
		Map<String,Object>paramMap=new HashMap<>();
		paramMap.put("id",id);
		List<GateExtraUserInfoDto> result = findList(sql, paramMap, GateExtraUserInfoDto.class);	
		if(result!=null && result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据访客证件号码，查询访客信息
	 * @param pap_number 访客的证件号码
	 * @return
	 */
	public GateExtraUserInfoDto selectVisitorByUserId(String user_id){
		StringBuffer sql=new StringBuffer("select id,registration_date,"
				+ "entry_date,leave_date,car_type,car_number,pap_type,pap_number,visit_reason "
				+ "from gate_t_extra_user_info where  user_id=:user_id ");
		Map<String,Object>paramMap=new HashMap<>();
		paramMap.put("user_id",user_id);
		List<GateExtraUserInfoDto> result = findList(sql, paramMap, GateExtraUserInfoDto.class);	
		if(result!=null && result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 查找身份证
	 * @param 
	 * @return
	 */
	public Boolean selectPapNumber(String pap_number){
		String sql1="select pap_number from gate_t_extra_user_info where pap_number=:pap_number";
		String sql2="select cid from gate_t_user where cid=:pap_number";
		Map<String,Object>paramMap=new HashMap<>();
		paramMap.put("pap_number",pap_number);
		if(pap_number.length()!=18){
			List<GateExtraUserInfo> result = findList(sql1, paramMap, GateExtraUserInfo.class);
			System.out.println(result.size());
			if(result!=null && result.size()>0){
				return true;
			}else{
				return false;
			}
		}else{
			List<GateUser> result = findList(sql2, paramMap, GateUser.class);
			if(result!=null && result.size()>0){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 删除员工
	 * @param id
	 * @return
	 */
	public int deleteVisit(String id){
		String sql="DELETE FROM  gate_t_extra_user_info where user_id=:id";
		Map<String,Object>paramMap=new HashMap<>();
		paramMap.put("id",id);
		int num=execute(sql, paramMap);
		System.out.println("num      :"+num);
		return num;
	}
	
	/**
	 * 修改员工
	 * @param id
	 * @return
	 */
	public int updateVisit(GateExtraUserInfo visit){
		String sql="update gate_t_extra_user_info set entry_date=:entry_date,leave_date=:leave_date,"
				+ "car_type=:car_type,car_number=:car_number,pap_type=:pap_type,pap_number=:pap_number,"
				+ "visit_reason=:visit_reason where id=:id";
		//String sql="DELETE FROM  gate_t_extra_user_info where user_id=:id";
		Map<String,Object>paramMap=new HashMap<>();
		paramMap.put("entry_date",visit.getEntry_date());
		paramMap.put("leave_date",visit.getLeave_date());
		paramMap.put("car_type",visit.getCar_type());
		paramMap.put("car_number",visit.getCar_number());
		paramMap.put("pap_type",visit.getPap_type());
		paramMap.put("pap_number",visit.getPap_number());
		paramMap.put("visit_reason",visit.getVisit_reason());
		paramMap.put("id",visit.getId());
		int num=execute(sql, paramMap);
		System.out.println("num      :"+num);
		return num;
	}
}
