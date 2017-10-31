package pos.cn.com.lanlyc.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import pos.cn.com.lanlyc.core.dao.PosCardDao;
import pos.cn.com.lanlyc.core.dao.PosUserInfoVDao;
import pos.cn.com.lanlyc.core.po.PosIndentificationCard;


/**
 * 
 * @author jiangyanyan
 * @date 2017年9月15日下午3:31:18
 * @version 1.0
 */

@Service
public class PosCardService {
	
	@Autowired
	private PosCardDao posCardDao;
	
	@Autowired
	private PosUserInfoVDao posUserInfoVDao;

	public PosCardDao getPosCardDao() {
		return posCardDao;
	}

	public PosUserInfoVDao getPosUserInfoVDao() {
		return posUserInfoVDao;
	}

	
	
	/**
	 * 根据定位卡列表删除定位卡，并且解绑与定位卡绑定的人
	 * @param cardIds 定位卡列表
	 * @author jiangyanyan
	 * @return
	 */
	public List<Object> batchDeleteCardsByCardIdArr(JSONArray cardIds){
		//将jsonArray字符串转化为JSONArray  
         List<String> cardId = JSONArray.toList(cardIds, new String(), null);
		 List<Object> mapL = new ArrayList<Object>();
    	 for(String card_id:cardId) {
    		 Map<String,Integer> deleteCard = new HashMap<String,Integer>();
    		//根据定位卡id获取定位卡实体 
    		PosIndentificationCard cardM = this.getPosCardDao().getCardEntityByCardId(card_id);
    		String card_sn = cardM.getCard_sn();
    		//根据定位卡号，把人员的定位卡号置为空
    		int numU = this.getPosUserInfoVDao().updateUserInfoViewByCardSn(card_sn);
    		deleteCard.put("numU", numU); //返回0此卡号没有绑定，返回1时人卡解绑成功，其它更新失败 
    		//根据定位卡id删除定位卡 numC等于0时人员与卡解绑失败，numC等于1时解绑成功
    		if(numU==1) {
    			int numC = this.getPosUserInfoVDao().delete(card_id);
        		deleteCard.put("numC", numC); //等于1时是成功
    		}else {
    			deleteCard.put("numC", 0);//等于0时是删除失败
    		}
    		
//    		boolean card_de = this.getPosCardDao().deleteCardByCardId(card_id);
//			if(card_de) {
//				deleteCard.put("cardC", 1); //等于1时是成功
//			}else {
//				deleteCard.put("cardC", 0); //等于0时是成功
//			}
    		mapL.add(deleteCard);
        }
    	 return mapL;
	}
	
	
	/**
	 * 根据定位卡列表删除定位卡，并且解绑与定位卡绑定的人
	 * @param cardIds 定位卡列表
	 * @author jiangyanyan
	 * @return
	 */
	public boolean batchDeleteCardsByCardIdArr2(JSONArray cardIds,JSONArray userIds){
		List<String> cardL = JSONArray.toList(cardIds, new String(),null);
		List<String> userL = JSONArray.toList(userIds, new String(), null);
		boolean user_b = true;
		if(userL.size()>0) {
			 user_b = this.getPosUserInfoVDao().updateUserInfoViewByUserIds(userL);
		}
     	if(user_b) {
     		boolean card_b = this.getPosCardDao().deleteCardByCardIds(cardL);
     		return card_b;
     	}else {
     		return user_b;
     	}	 
	}

}
