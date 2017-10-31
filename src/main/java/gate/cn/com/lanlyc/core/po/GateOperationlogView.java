package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * gate_v_operation_user 实体类
    * Wed Sep 06 09:34:54 CST 2017 黎洋富
    */ 


@TableMapper("gate_v_operation_user")

public class GateOperationlogView  implements Serializable{
	private String id;
	private String operation_action;
	private String operation_desc;
	private String operation_time;
	private String user_name;
	private String image;
	private String mobile;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setOperation_action(String operation_action){
	this.operation_action=operation_action;
	}
	public String getOperation_action(){
		return operation_action;
	}
	public void setOperation_desc(String operation_desc){
	this.operation_desc=operation_desc;
	}
	public String getOperation_desc(){
		return operation_desc;
	}
	public void setOperation_time(String operation_time){
	this.operation_time=operation_time;
	}
	public String getOperation_time(){
		return operation_time;
	}
	public void setUser_name(String user_name){
	this.user_name=user_name;
	}
	public String getUser_name(){
		return user_name;
	}
	public void setImage(String image){
	this.image=image;
	}
	public String getImage(){
		return image;
	}
	public void setMobile(String mobile){
	this.mobile=mobile;
	}
	public String getMobile(){
		return mobile;
	}
}

