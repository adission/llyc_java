package pos.cn.com.lanlyc.core.dto;

import java.io.Serializable;

import video.cn.com.lanlyc.core.po.VidiconInfo;

public class PosCameraDto extends VidiconInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1326450926133794164L;
	private String layerName;
	public String getLayerName() {
		return layerName;
	}
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
	

}
