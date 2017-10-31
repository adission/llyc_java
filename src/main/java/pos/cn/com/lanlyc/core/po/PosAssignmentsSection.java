package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posAssignmentsSection 实体类
    * Thu Sep 14 16:01:24 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_assignmentsSection")
public class PosAssignmentsSection  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String name;

	private String type;

	private int numOfPersonnel;

	private String startLongitude;

	private String startLatitude;

	private String endLongitude;

	private String endLatitude;

	private String attention;

	private String resPerId;

	private String instructions;

	private String secColor;

	private String layer_id;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setName(String name){
	this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setNumOfPersonnel(int numOfPersonnel){
	this.numOfPersonnel=numOfPersonnel;
	}
	public int getNumOfPersonnel(){
		return numOfPersonnel;
	}
	public void setStartLongitude(String startLongitude){
	this.startLongitude=startLongitude;
	}
	public String getStartLongitude(){
		return startLongitude;
	}
	public void setStartLatitude(String startLatitude){
	this.startLatitude=startLatitude;
	}
	public String getStartLatitude(){
		return startLatitude;
	}
	public void setEndLongitude(String endLongitude){
	this.endLongitude=endLongitude;
	}
	public String getEndLongitude(){
		return endLongitude;
	}
	
	public String getEndLatitude() {
		return endLatitude;
	}
	public void setEndLatitude(String endLatitude) {
		this.endLatitude = endLatitude;
	}
	public void setAttention(String attention){
	this.attention=attention;
	}
	public String getAttention(){
		return attention;
	}
	public void setResPerId(String resPerId){
	this.resPerId=resPerId;
	}
	public String getResPerId(){
		return resPerId;
	}
	public void setInstructions(String instructions){
	this.instructions=instructions;
	}
	public String getInstructions(){
		return instructions;
	}
	public void setSecColor(String secColor){
	this.secColor=secColor;
	}
	public String getSecColor(){
		return secColor;
	}
	public void setLayer_id(String layer_id){
	this.layer_id=layer_id;
	}
	public String getLayer_id(){
		return layer_id;
	}
}

