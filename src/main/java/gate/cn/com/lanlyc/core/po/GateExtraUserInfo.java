package gate.cn.com.lanlyc.core.po;

import java.io.Serializable;
import java.util.Date;

import cn.com.lanlyc.base.persistence.TableMapper;

@TableMapper("gate_t_extra_user_info")
public class GateExtraUserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4642326936892033820L;
    private String id;
    private String user_id;
    private Date registration_date;
    private Date entry_date;
    private Date leave_date;
    private String car_type;
    private String car_number;
    private String pap_type;
    private String pap_number;
    private String visit_reason;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getRegistration_date() {
		return registration_date;
	}
	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}
	public Date getEntry_date() {
		return entry_date;
	}
	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}
	public Date getLeave_date() {
		return leave_date;
	}
	public void setLeave_date(Date leave_date) {
		this.leave_date = leave_date;
	}
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	public String getCar_number() {
		return car_number;
	}
	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}
	public String getPap_type() {
		return pap_type;
	}
	public void setPap_type(String pap_type) {
		this.pap_type = pap_type;
	}
	public String getPap_number() {
		return pap_number;
	}
	public void setPap_number(String pap_number) {
		this.pap_number = pap_number;
	}
	public String getVisit_reason() {
		return visit_reason;
	}
	public void setVisit_reason(String visit_reason) {
		this.visit_reason = visit_reason;
	}
    
    
}
