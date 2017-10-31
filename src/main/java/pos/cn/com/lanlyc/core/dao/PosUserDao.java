package pos.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * @author hucong
 * 获得人员详情dao
 */
import cn.com.lanlyc.base.persistence.MySQLMapper;
import gate.cn.com.lanlyc.core.po.GateUser;
import pos.cn.com.lanlyc.core.dto.PosSimpleUserDto;
import pos.cn.com.lanlyc.core.dto.UserDto;
@Service
public class PosUserDao extends MySQLMapper<GateUser>{
	public UserDto getUserById(String id){
	    String sql="select t.name workerstype,c.name userclass,u.* from gate_t_workers_types t,"
	    		+ "(select * from gate_t_user)u,gate_t_user_class c "
	    		+ "where t.value=u.workers_type  and u.user_class_id=c.id and u.id=:id";
	    Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		List<UserDto> d = findList(sql, paramMap, UserDto.class);
		System.out.println(d+"    sss");
		if(d!=null&&d.size()>0){
			return d.get(0);
		}else{
			return null;
		}
	}
	
//	/**
//	 * 根据userId查询人员信息（包含工作类型、标签颜色）
//	 * 暂时不要
//	 */
//	public UserDto getMessById(String userId) {
//		String sql = "select two.*,one.typeName as typeName,one.color as color from "
//				+ "(select g.*,t.cardType from gate_t_user g left join pos_t_personCard t on g.id=t.perId) two "
//				+ "left join pos_t_cardType one on two.cardType=one.id where two.id=:userId";
//		
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("userId", userId);
//		List<UserDto> res = findList(sql, paramMap,UserDto.class);
//		if(res!=null && res.size()>0) {
//			return res.get(0);
//		}
//		return null;
//	}
	
}
