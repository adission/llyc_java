package gate.cn.com.lanlyc.core.po;

import java.io.Serializable;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

@TableMapper("gate_t_setting")
public class Setting implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
    private String id;
    /**
     * 
     *工地编号
     * 
     */
    private String region_id;
    /**
     * 
     *请求密码
     * 
     */
    private String scrt_key;
    /**
     * 
     *请求token
     * 
     */
    private String token;
    /**
     * 
     *上一次登陆时间
     * 
     */
    private String last_login_time;
    /**
     * 
     *请求url
     * 
     */
    private String request_url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRegion_id() {
		return region_id;
	}
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	public String getScrt_key() {
		return scrt_key;
	}
	public void setScrt_key(String scrt_key) {
		this.scrt_key = scrt_key;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}
	public String getRequest_url() {
		return request_url;
	}
	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
