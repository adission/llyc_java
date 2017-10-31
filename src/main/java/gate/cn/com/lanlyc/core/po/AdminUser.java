package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
import cn.com.lanlyc.base.util.EncryptionUtils;

import java.io.Serializable;
   /**
    * admin_user 实体类
    * Wed Aug 16 16:18:09 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_admin_user")

public class AdminUser  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String user_name;
	private String pass_word;
	private String last_login_time;
	private String mobile;

	private String image;
	private String create_time;
	private String token;

	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setUser_name(String user_name){
	this.user_name=user_name;
	}
	public String getUser_name(){
		return user_name;
	}
	public void setPass_word(String pass_word){
		this.pass_word=EncryptionUtils.encryptV5(pass_word);
	}
	public String getPass_word(){
		
		return pass_word;
	}
	public void setLast_login_time(String last_login_time){
	this.last_login_time=last_login_time;
	}
	public String getLast_login_time(){
		return last_login_time;
	}
	public void setMobile(String mobile){
	this.mobile=mobile;
	}
	public String getMobile(){
		return mobile;
	}
	public void setCreate_time(String create_time){
	this.create_time=create_time;
	}
	public String getCreate_time(){
		return create_time;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}

