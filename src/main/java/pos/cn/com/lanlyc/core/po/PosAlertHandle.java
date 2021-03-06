package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posAlertHandle 实体类
    * Thu Sep 14 16:00:59 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_alertHandle")
public class PosAlertHandle  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String perId;

	private Date handleTime;

	private String alertId;

	private String processMode;
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
	public void setHandleTime(Date handleTime){
	this.handleTime=handleTime;
	}
	public Date getHandleTime(){
		return handleTime;
	}
	public void setAlertId(String alertId){
	this.alertId=alertId;
	}
	public String getAlertId(){
		return alertId;
	}
	public void setProcessMode(String processMode){
	this.processMode=processMode;
	}
	public String getProcessMode(){
		return processMode;
	}
}

