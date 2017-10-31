package gate.cn.com.lanlyc.core.po;

import java.io.Serializable;
import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * gate_v_worktypsdataanalysis 实体类
    * Fri Sep 08 09:24:24 CST 2017 黎洋富
    */ 


@TableMapper("gate_v_worktypsdataanalysis")

public class GateWorktypsDataAnalysisView  implements Serializable{
	private String id;
	private String name;
	private String order_by;
	private String value;
	private long total_person;
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
	public void setOrder_by(String order_by){
	this.order_by=order_by;
	}
	public String getOrder_by(){
		return order_by;
	}
	public void setValue(String value){
	this.value=value;
	}
	public String getValue(){
		return value;
	}
	public void setTotal_person(long total_person){
	this.total_person=total_person;
	}
	public long getTotal_person(){
		return total_person;
	}
}

