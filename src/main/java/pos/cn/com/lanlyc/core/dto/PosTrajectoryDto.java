package pos.cn.com.lanlyc.core.dto;
/**
 * @author cjt
 */
import java.io.Serializable;
import java.math.BigDecimal;

public class PosTrajectoryDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal x;
	
	private BigDecimal y;
	
	private String time;
	
	private int tindex;
	
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getTindex() {
		return tindex;
	}

	public void setTindex(int tindex) {
		this.tindex = tindex;
	}
	
	

}
