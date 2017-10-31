package gate.cn.com.lanlyc.core.po;

import java.io.Serializable;

import cn.com.lanlyc.base.persistence.TableMapper;

@TableMapper("gate_t_pap_types")
public class PapTypes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4218017543347374670L;
    private String id;
    private String name;
    private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
    
}
