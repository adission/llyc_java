package pos.cn.com.lanlyc.core.dto;

import java.io.Serializable;

public class PosCardDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String id1;
	
	private String card_sn;
	
	private String name;
	
	private String mobile;
	
	private String worker_type;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
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

	public String getWorker_type() {
		return worker_type;
	}

	public void setWorker_type(String worker_type) {
		this.worker_type = worker_type;
	}

	
}
