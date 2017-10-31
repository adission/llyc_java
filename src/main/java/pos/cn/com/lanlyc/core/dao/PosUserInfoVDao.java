package pos.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;


@Repository
public class PosUserInfoVDao extends MySQLMapper<GateUserInfoView> {
	
	
	/**
	 * 根据定位卡号，把人员的定位卡号置为空
	 * @param card_sn 人员所对应的卡号
	 * @return 返回0此卡号没有绑定，返回1时更新成功，其它更新失败
	 */
	public int updateUserInfoViewByCardSn(String card_sn) {
		 String sql = new String("SELECT *FROM gate_v_user_info WHERE card_sn=:card_sn");
    	 Map<String,Object> paramMap = new HashMap<String,Object>();
    	 paramMap.put("card_sn", card_sn);
    	 //查询人员视图表
    	 List<GateUserInfoView> userL = this.findList(sql, paramMap, GateUserInfoView.class);
    	 //返回0此卡号没有绑定，返回1时人卡解绑成功，其它更新失败
    	 if(userL.size()==0) {
    		 return 0;
    	 }else {
    		 GateUserInfoView userView = userL.get(0);
    		 PosPerConstructDao perCDao = new  PosPerConstructDao();
    		 boolean pd =perCDao.updateUserByUserId(userView.getId(),"");
    		 if(pd==true) {
    			 return 1; 
    		 }else {
    			 return 3;
    		 }
    	 }	 
	}
	
	/**
	 * 根据人员id列表 使人员的定位卡号置为空
	 * @param user_ids
	 * @return 成功返回true 失败返回false
	 */
	public boolean updateUserInfoViewByUserIds(List<String> user_ids){
		String sql = "UPDATE gate_v_user_info SET card_sn = '' WHERE gate_v_user_info.id IN (:userIds)";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("userIds", user_ids);
		int result = this.execute(sql, paramMap);
		if(result==1) {
			return true;
		}else {
			return false;
		}
	}
	

}
