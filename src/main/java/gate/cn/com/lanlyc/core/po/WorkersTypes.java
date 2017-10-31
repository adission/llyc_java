package gate.cn.com.lanlyc.core.po;

import java.io.Serializable;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

@TableMapper("gate_t_workers_types")
public class WorkersTypes implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
    private String id;
    /**
     * 
     *工种名称 
     * 
     */
    private String name;
    /**
     * 
     *工种值 
     * 
     */
    private String value;
    /**
     * 
     *排序序号
     * 
     */
    private String order_by;
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
	public String getOrder_by() {
		return order_by;
	}
	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}
}
