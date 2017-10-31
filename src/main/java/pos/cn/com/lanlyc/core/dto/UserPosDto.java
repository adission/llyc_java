package pos.cn.com.lanlyc.core.dto;

import java.io.Serializable;
import java.util.List;

import gate.cn.com.lanlyc.core.po.GateUser;

public class UserPosDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<PosTrajectoryDto> posList;
	
	private String name;
	
	private String workers_type;

	public List<PosTrajectoryDto> getPosList() {
		return posList;
	}

	public void setPosList(List<PosTrajectoryDto> posList) {
		this.posList = posList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkers_type() {
		return workers_type;
	}

	public void setWorkers_type(String workers_type) {
		this.workers_type = workers_type;
	}
	

}
