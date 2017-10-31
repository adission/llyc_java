package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posPersonCard 实体类
    * Thu Sep 14 16:05:32 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_personCard")
public class PosPersonCard  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String cardId;

	private String perId;

	private Date bindingTime;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setCardId(String cardId){
	this.cardId=cardId;
	}
	public String getCardId(){
		return cardId;
	}
	public void setPerId(String perId){
	this.perId=perId;
	}
	public String getPerId(){
		return perId;
	}
	public void setBindingTime(Date bindingTime){
	this.bindingTime=bindingTime;
	}
	public Date getBindingTime(){
		return bindingTime;
	}
}

