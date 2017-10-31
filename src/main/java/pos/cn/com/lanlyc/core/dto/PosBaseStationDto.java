package pos.cn.com.lanlyc.core.dto;

import java.io.Serializable;

import pos.cn.com.lanlyc.core.po.PosBaseStation;

public class PosBaseStationDto extends PosBaseStation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7256161545078907674L;
    private String layerName;
	public String getLayerName() {
		return layerName;
	}
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
    
}
