package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * operation_log 实体类
    * Wed Aug 16 15:55:28 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_operation_log")

public class OperationLog  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String operation_time;
	private String operation_desc;
	private String operation_user;
	private String operation_action;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setOperation_time(String operation_time){
	this.operation_time=operation_time;
	}
	public String getOperation_time(){
		return operation_time;
	}
	public void setOperation_desc(String operation_desc){
	this.operation_desc=operation_desc;
	}
	public String getOperation_desc(){
		return operation_desc;
	}
	public void setOperation_user(String operation_user){
	this.operation_user=operation_user;
	}
	public String getOperation_user(){
		return operation_user;
	}
	public void setOperation_action(String operation_action){
	this.operation_action=operation_action;
	}
	public String getOperation_action(){
		return operation_action;
	}
}

