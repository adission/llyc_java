package gate.cn.com.lanlyc.core.po;

import java.io.Serializable;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

@TableMapper("gate_t_request_log")
public class RequestLog implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
    private String id;
    /**
     * 
     *请求编号
     * 
     */
    private String seq_no;
    /**
     * 
     *请求类型
     * 
     */
    private String request_type;
    /**
     * 
     *返回code
     * 
     */
    private String return_code;
    /**
     * 
     *请求时间
     * 
     */
    private String return_time;
    /**
     * 
     *请求参数
     * 
     */
    private String request_parm;
    /**
     * 
     *返回数据
     * 
     */
    private String return_body;
    /**
     * 
     *刷新频率
     * 
     */
    private String req_rate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSeq_no() {
		return seq_no;
	}
	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}
	public String getRequest_type() {
		return request_type;
	}
	public void setRequest_type(String request_type) {
		this.request_type = request_type;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_time() {
		return return_time;
	}
	public void setReturn_time(String return_time) {
		this.return_time = return_time;
	}
	public String getRequest_parm() {
		return request_parm;
	}
	public void setRequest_parm(String request_parm) {
		this.request_parm = request_parm;
	}
	public String getReturn_body() {
		return return_body;
	}
	public void setReturn_body(String return_body) {
		this.return_body = return_body;
	}
	public String getReq_rate() {
		return req_rate;
	}
	public void setReq_rate(String req_rate) {
		this.req_rate = req_rate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
