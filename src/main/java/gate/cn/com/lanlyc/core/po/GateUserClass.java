package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * gate_user_class 实体类
    * Wed Aug 16 15:56:05 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_user_class")

public class GateUserClass  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String name;
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
}

