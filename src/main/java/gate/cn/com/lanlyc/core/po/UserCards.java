package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * user_cards 实体类
    * Wed Aug 16 15:54:45 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_user_cards")

public class UserCards  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String user_auth_card;
	private String type;
	private String user_id;
	private String use_times;
	private String end_time;
	private String is_tmp;
	
	

	
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setUser_auth_card(String user_auth_card){
	this.user_auth_card=user_auth_card;
	}
	public String getUser_auth_card(){
		return user_auth_card;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setUser_id(String user_id){
	this.user_id=user_id;
	}
	public String getUser_id(){
		return user_id;
	}
	public void setUse_times(String use_times){
	this.use_times=use_times;
	}
	public String getUse_times(){
		return use_times;
	}
	public void setEnd_time(String end_time){
	this.end_time=end_time;
	}
	public String getEnd_time(){
		return end_time;
	}
	public void setIs_tmp(String is_tmp){
	this.is_tmp=is_tmp;
	}
	public String getIs_tmp(){
		return is_tmp;
	}
}

