package gate.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * gate_v_userclassdataanalysis 实体类
    * Fri Sep 22 10:05:34 CST 2017 黎洋富
    */ 


@TableMapper("gate_v_userclassdataanalysis")

public class GateUserClassDataAnalysisView  implements Serializable{
	private String id;
	private String name;
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
	public void setTotal_person(long total_person){
	this.total_person=total_person;
	}
	public long getTotal_person(){
		return total_person;
	}
}

