package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * gate_group 实体类
    * Wed Aug 16 15:57:24 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_group")

public class GateGroup  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String group_name;
	private String welcome;
	public String getWelcome() {
		return welcome;
	}
	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setGroup_name(String group_name){
	this.group_name=group_name;
	}
	public String getGroup_name(){
		return group_name;
	}
}

