package gate.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * gate_t_showproject_peoplecout 实体类
    * Wed Sep 06 15:53:13 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_showproject_peoplecout")

public class ShowProjectPeopleCout  implements Serializable{
	@PrimaryKeyMapper
	private String id;
	private String show_welcome;
	private String enter_person;
	private String class_type_person;
	private String last_enter_person;
	private String worker_type_person;
	private String last_upload_time;
	private String area;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setShow_welcome(String show_welcome){
	this.show_welcome=show_welcome;
	}
	public String getShow_welcome(){
		return show_welcome;
	}
	public void setEnter_person(String enter_person){
	this.enter_person=enter_person;
	}
	public String getEnter_person(){
		return enter_person;
	}
	
	
	public void setWorker_type_person(String worker_type_person){
	this.worker_type_person=worker_type_person;
	}
	public String getWorker_type_person(){
		return worker_type_person;
	}
	public void setLast_upload_time(String last_upload_time){
	this.last_upload_time=last_upload_time;
	}
	public String getLast_upload_time(){
		return last_upload_time;
	}
	public void setArea(String area){
	this.area=area;
	}
	public String getArea(){
		return area;
	}
	public String getClass_type_person() {
		return class_type_person;
	}
	public void setClass_type_person(String class_type_person) {
		this.class_type_person = class_type_person;
	}
	public String getLast_enter_person() {
		return last_enter_person;
	}
	public void setLast_enter_person(String last_enter_person) {
		this.last_enter_person = last_enter_person;
	}
}

