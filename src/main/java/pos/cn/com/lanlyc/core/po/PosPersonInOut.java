package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posPersonInOut 实体类
    * Thu Sep 14 16:05:44 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_personInOut")
public class PosPersonInOut  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String perId;

	private String secId;

	private Date time;

	private String typeWay;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setPerId(String perId){
	this.perId=perId;
	}
	public String getPerId(){
		return perId;
	}
	public void setSecId(String secId){
	this.secId=secId;
	}
	public String getSecId(){
		return secId;
	}
	public void setTime(Date time){
	this.time=time;
	}
	public Date getTime(){
		return time;
	}
	public void setTypeWay(String typeWay){
	this.typeWay=typeWay;
	}
	public String getTypeWay(){
		return typeWay;
	}
}

