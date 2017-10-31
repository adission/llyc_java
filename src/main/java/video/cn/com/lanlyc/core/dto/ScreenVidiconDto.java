package video.cn.com.lanlyc.core.dto;

import java.io.Serializable;

import video.cn.com.lanlyc.core.po.ScreenVidicon;

/**
 * 屏和摄像机Dto
 * @author chenyan
 * @date 2017年9月12日 15:03:13
 */
public class ScreenVidiconDto extends ScreenVidicon implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String vidicon_ip;

	private String vidicon_name;
	
	private int vidicon_number;
	
	public String getVidicon_name() {
		return vidicon_name;
	}

	public int getVidicon_number() {
		return vidicon_number;
	}

	public void setVidicon_number(int vidicon_number) {
		this.vidicon_number = vidicon_number;
	}

	public void setVidicon_name(String vidicon_name) {
		this.vidicon_name = vidicon_name;
	}

	public String getVidicon_ip() {
		return vidicon_ip;
	}

	public void setVidicon_ip(String vidicon_ip) {
		this.vidicon_ip = vidicon_ip;
	}
	
}
