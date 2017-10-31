package pos.cn.com.lanlyc.core.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import pos.cn.com.lanlyc.core.dto.PosCardDto;
import pos.cn.com.lanlyc.core.po.PosIndentificationCard;

/**
 * 批量制卡，人员与卡绑定，删除，修改，解绑Controller
 * @author jiangyanyan
 * @date 2017年9月15日 14:10:23
 * @version 1.0
 *
 */

@Repository
public class PosCardDao extends MySQLMapper<PosIndentificationCard> {
	
	/**
	 * 根据开始卡号与结束卡号批量添加定位卡
	 * @param startNum 开始卡号
	 * @param endNum 结束卡号
	 * @author jiangyanyan
	 * @return 返回添加定位卡的状态信息
	 */
     public Map<String,Integer> batchFabricationCard(int startNum,  int endNum) {
    	 Map<String,Integer> addCard = new HashMap<String,Integer>();
    	 for(int i=startNum;i<=endNum;i++) {
    		 String card_sn = String.valueOf(i);
    		 PosIndentificationCard p = new PosIndentificationCard(card_sn);
    		 p.setId(DataUtils.getUUID()); //不放uuid回报错
    		 int result = this.save(p);
    		 addCard.put(card_sn, result);
    	 }
    	 return addCard;
     }
     
     /**
      * 根据开始卡号与结束卡号判断定位卡是否存在
      * @param startNum 开始卡号
      * @param endNum 结束卡号
      * @author jiangyanyan 
      * @return 有定位卡时返回真，没有定位卡时返回假
      */
     public boolean judgementBatchFabricationCard(String startNum, String endNum) {
    	 String sql0 = "SELECT a.card_sn FROM pos_t_indentificationCard a WHERE a.card_sn BETWEEN (:scard_sn) AND (:ecard_sn)";
    	 Map<String,Object> queryCard = new HashMap<String,Object>();
    	 queryCard.put("scard_sn", startNum);
    	 queryCard.put("ecard_sn", endNum);
    	 List<PosIndentificationCard> ret = findList(sql0,queryCard,PosIndentificationCard.class);
    	 if(null==ret || ret.size()==0) {
    		 return false;
    	 }else {
    		 return true;
    	 } 
     }
     
     
     /**
      * 判断输入的卡号是否在数据表中存在
      * @param card_sn
      * @author jiangyanyan
      * @return 返回假时不存在，返回真时存在
      */
     public boolean judgementCardIsExist(String card_sn){
    	 String sql = new String("SELECT * FROM pos_t_indentificationCard WHERE card_sn=:card_sn");
    	 Map<String,Object> queryCard = new HashMap<String,Object>();
    	 queryCard.put("card_sn", card_sn);
    	 List<PosIndentificationCard> ret = findList(sql,queryCard);
    	 if(ret==null || ret.size()==0) {
    		return false; 
    	 }else {
    		return true;
    	 }	 
     }
     
     
     //此方法暂时没有用
     public boolean bindingUserAndCard(String card_sn,String user_id) {
    	 String sql = new String("UPDATE gate_t_user SET card_sn=:card_sn WHERE id=:user_id");
    	 Map<String,Object> setCard = new HashMap<String,Object>();
    	 setCard.put("card_sn", card_sn);
    	 setCard.put("user_id", user_id);
    	 return true;
     }
     
    /**
     * 分页查询定位卡
     * @param page 
     * @param keyword 搜索的关键字
     * @author jiangyanyan
     * @return 分页返回定位卡列表
     */
     public Page<PosCardDto> queryUserAndCard(Page<PosCardDto>page,String keyword) {
    	 String sqlHeader = "select * from (";
    	 String sql = new String(" select a.id,a.card_sn, b.id as id1,b.name,b.mobile,b.worker_type from pos_t_indentificationCard a left join gate_v_user_info b on a.card_sn=b.card_sn");
    	 String sqlFuzzy =") c WHERE c.name like :name or c.worker_type like :worker_type or c.card_sn like :card_sn";
    	 if(!DataUtils.isNullOrEmpty(keyword)) {
    		 sql = sqlHeader+sql+sqlFuzzy;
    	 }
    	 Map<String,Object> paramMap = new HashMap<String,Object>();
    	 paramMap.put("card_sn", "%"+keyword+"%");
    	 paramMap.put("name", "%"+keyword+"%");
    	 paramMap.put("worker_type", "%"+keyword+"%");
    	 Page<PosCardDto> result = getPage(sql,paramMap,page,PosCardDto.class);
    	 return result;
     }
     
     
     /**
      * 根据卡ids删除定位卡
      * @param cardIds 要删除的卡的ids
      * @author jiangyanyan
      * @return
      */
     public Map<String,Integer> batchDeleteCardsByCardIdArr(String cardIds){
    	 Map<String,Integer> deleteCard = new HashMap<String,Integer>();
    	 return deleteCard;
     }
     
     
     /**
      * 根据卡号id删除定位卡
      * @param cardId 卡号id
      * @return 如果删除成功返回真 删除失败返回假
      */
     public boolean deleteCardByCardId(String cardId) {
    	 int res = this.delete(cardId);
    	 if(res==1) {
    		 return true;
    	 }else {
    		 return false;
    	 }
     }
     
     
     /**
      * 根据图层id 获取该图层卡的个数
      * @param layerId 图层id
      * @author jiangyanyan
      * @return 返回图层卡的个数
      */
     public int getCardCountByLayerId(String layerId) {
    	 String sql = new String("SELECT * FROM pos_t_indentificationCard p WHERE p.layer_id =:layer_id");
    	 Map<String,Object> paramMap = new HashMap<String,Object>();
    	 paramMap.put("layer_id", layerId);
    	 List<PosIndentificationCard> ret = findList(sql,paramMap,PosIndentificationCard.class);
    	 return ret.size(); 
     }
     
     
     /**
      * 根据图层id 获取图层实体
      * @param cardId
      * @return 返回图层实体信息
      */
     public PosIndentificationCard getCardEntityByCardId(String cardId) {
    	 String sql = new String("SELECT * FROM pos_t_indentificationCard p WHERE p.id=:id");
    	 Map<String,Object> paramMap = new HashMap<String,Object>();
    	 paramMap.put("id", cardId);
    	 List<PosIndentificationCard> ret = findList(sql,paramMap,PosIndentificationCard.class);
    	 return ret.get(0);
     }
     
     /**
      * 根据定位卡id列表删除定位卡
      * @param cardIds
      * @return 删除成功返回 true 删除失败返回false
      */
     public boolean deleteCardByCardIds(List<String> cardIds) {
    	 String sql = "DELETE FROM pos_t_indentificationCard WHERE pos_t_indentificationCard.id IN (:cardIds)";
    	 Map<String,Object> paramMap = new HashMap<String,Object>();
    	 paramMap.put("cardIds", cardIds);
    	 int result = this.execute(sql, paramMap);
    	 if(result == 1) {
    		 return true;
    	 }else {
    		 return false; 
    	 }
     }
     
}
