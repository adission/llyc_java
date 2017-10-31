package pos.cn.com.lanlyc.core.dto;

import java.io.Serializable;

import pos.cn.com.lanlyc.core.po.PosAssignmentsSection;

public class PosAssignmentsSectionDto extends PosAssignmentsSection implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6847517311894047090L;
	private String layerName;
	public String getLayerName() {
		return layerName;
	}
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
}
