package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posTrajectory 实体类
    * Thu Sep 14 16:05:55 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_trajectory")
public class PosTrajectory  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String perId;

	private BigDecimal perLongitude;

	private BigDecimal perLatitude;

	private Date timeStamp;

	private String stationId;
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
	public void setPerLongitude(BigDecimal perLongitude){
	this.perLongitude=perLongitude;
	}
	public BigDecimal getPerLongitude(){
		return perLongitude;
	}
	public void setPerLatitude(BigDecimal perLatitude){
	this.perLatitude=perLatitude;
	}
	public BigDecimal getPerLatitude(){
		return perLatitude;
	}
	public void setTimeStamp(Date timeStamp){
	this.timeStamp=timeStamp;
	}
	public Date getTimeStamp(){
		return timeStamp;
	}
	public void setStationId(String stationId){
	this.stationId=stationId;
	}
	public String getStationId(){
		return stationId;
	}
}

