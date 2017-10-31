package pos.cn.com.lanlyc.core.dto;

import java.io.Serializable;
import java.util.List;

import gate.cn.com.lanlyc.core.po.GateUser;

public class UserDto extends GateUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7303445023793330440L;
	private String workerstype;
	private String userclass;
    private String nation;	
    private List<PosTrajectoryDto> posList;
    private String typeName;
    private String color;

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getWorkerstype() {
		return workerstype;
	}

	public void setWorkerstype(String workerstype) {
		this.workerstype = workerstype;
	}

	public String getUserclass() {
		return userclass;
	}

	public void setUserclass(String userclass) {
		this.userclass = userclass;
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

}
