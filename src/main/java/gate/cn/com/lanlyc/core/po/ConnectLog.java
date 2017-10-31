package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * connect_log 实体类
    * Wed Aug 16 15:57:50 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_connect_log")

public class ConnectLog  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String connect_time;
	private String connect_type;
	private String connect_fail_reson;
	private String gate_id;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setConnect_time(String connect_time){
	this.connect_time=connect_time;
	}
	public String getConnect_time(){
		return connect_time;
	}
	public void setConnect_type(String connect_type){
	this.connect_type=connect_type;
	}
	public String getConnect_type(){
		return connect_type;
	}
	public void setConnect_fail_reson(String connect_fail_reson){
	this.connect_fail_reson=connect_fail_reson;
	}
	public String getConnect_fail_reson(){
		return connect_fail_reson;
	}
	public String getGate_id() {
		return gate_id;
	}
	public void setGate_id(String gate_id) {
		this.gate_id = gate_id;
	}
	
}

