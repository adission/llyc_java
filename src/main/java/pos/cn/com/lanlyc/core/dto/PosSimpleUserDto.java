package pos.cn.com.lanlyc.core.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PosSimpleUserDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal x;
	
	private BigDecimal y;
	
	private String name;
	
	private String worker_type;
	
	private String perId;
	
	private String layer_id;
	
	private int cardType;
	
	private String typeName;
	
	private String color;

	public BigDecimal getX() {
		return x;
	}

	public void setX(BigDecimal x) {
		this.x = x;
	}

	public BigDecimal getY() {
		return y;
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}

	public String getLayer_id() {
		return layer_id;
	}

	public void setLayer_id(String layer_id) {
		this.layer_id = layer_id;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPerId() {
		return perId;
	}

	public void setPerId(String perId) {
		this.perId = perId;
	}

	public String getWorker_type() {
		return worker_type;
	}

	public void setWorker_type(String worker_type) {
		this.worker_type = worker_type;
	}
	
	

}
