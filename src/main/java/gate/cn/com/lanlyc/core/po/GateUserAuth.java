package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * gate_t_user_cards_auth 实体类
    * Thu Aug 31 10:00:40 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_user_cards_auth")

public class GateUserAuth  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String user_id;
	private String gate_id;
	private String is_use;
	private String face_data;
	
	public String getFace_data() {
		return face_data;
	}
	public void setFace_data(String face_data) {
		this.face_data = face_data;
	}
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setUser_id(String user_id){
	this.user_id=user_id;
	}
	public String getUser_id(){
		return user_id;
	}
	public void setGate_id(String gate_id){
	this.gate_id=gate_id;
	}
	public String getGate_id(){
		return gate_id;
	}
	public void setIs_use(String is_use){
	this.is_use=is_use;
	}
	public String getIs_use(){
		return is_use;
	}
}

