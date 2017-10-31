package gate.cn.com.lanlyc.core.dto;

import java.io.Serializable;

import gate.cn.com.lanlyc.core.po.GateExtraUserInfo;

public class GateExtraUserInfoDto extends GateExtraUserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7739681186646594397L;
    private String name;
    private String mobile;
    private String avatar_img;
    private String card_sn;
    private String gonghao;
    private String enter;
    private String leave;
    private String registration;
    private String cartype;
    private String paptype;
    
    
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public String getCartype() {
		return cartype;
	}
	public void setCartype(String cartype) {
		this.cartype = cartype;
	}
	public String getPaptype() {
		return paptype;
	}
	public void setPaptype(String paptype) {
		this.paptype = paptype;
	}
	public String getEnter() {
		return enter;
	}
	public void setEnter(String enter) {
		this.enter = enter;
	}
	public String getLeave() {
		return leave;
	}
	public void setLeave(String leave) {
		this.leave = leave;
	}
	public String getGonghao() {
		return gonghao;
	}
	public void setGonghao(String gonghao) {
		this.gonghao = gonghao;
	}
	public String getCard_sn() {
		return card_sn;
	}
	public void setCard_sn(String card_sn) {
		this.card_sn = card_sn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAvatar_img() {
		return avatar_img;
	}
	public void setAvatar_img(String avatar_img) {
		this.avatar_img = avatar_img;
	}
    
}
