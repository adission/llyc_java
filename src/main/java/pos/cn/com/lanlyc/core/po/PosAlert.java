package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posAlert 实体类
    * Wed Sep 20 16:54:36 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_alert")
public class PosAlert  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private Date time;

	private String type;

	private String section;

	private String alertType;

	private String alertContext;

	private String perId;

	private String equipId;

	private String status;

	private String area_id;

	private String materialId;

	private long timeStamp;

	private String processUser;

	private String processMode;

	private Date processTime;
	
	private String sectionName;
	
	private String layerName;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setTime(Date time){
	this.time=time;
	}
	public Date getTime(){
		return time;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setSection(String section){
	this.section=section;
	}
	public String getSection(){
		return section;
	}
	public void setAlertType(String alertType){
	this.alertType=alertType;
	}
	public String getAlertType(){
		return alertType;
	}
	public void setAlertContext(String alertContext){
	this.alertContext=alertContext;
	}
	public String getAlertContext(){
		return alertContext;
	}
	public void setPerId(String perId){
	this.perId=perId;
	}
	public String getPerId(){
		return perId;
	}
	public void setEquipId(String equipId){
	this.equipId=equipId;
	}
	public String getEquipId(){
		return equipId;
	}
	public void setStatus(String status){
	this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setArea_id(String area_id){
	this.area_id=area_id;
	}
	public String getArea_id(){
		return area_id;
	}
	public void setMaterialId(String materialId){
	this.materialId=materialId;
	}
	public String getMaterialId(){
		return materialId;
	}
	public void setTimeStamp(long timeStamp){
	this.timeStamp=timeStamp;
	}
	public long getTimeStamp(){
		return timeStamp;
	}
	public void setProcessUser(String processUser){
	this.processUser=processUser;
	}
	public String getProcessUser(){
		return processUser;
	}
	public void setProcessMode(String processMode){
	this.processMode=processMode;
	}
	public String getProcessMode(){
		return processMode;
	}
	public void setProcessTime(Date processTime){
	this.processTime=processTime;
	}
	public Date getProcessTime(){
		return processTime;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getLayerName() {
		return layerName;
	}
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
}

