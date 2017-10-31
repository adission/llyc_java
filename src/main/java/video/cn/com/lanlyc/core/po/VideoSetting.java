package video.cn.com.lanlyc.core.po;

import java.io.Serializable;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:视频设置实体类
 * @date:2017年9月18日 上午10:43:57
 */
@TableMapper("video_t_setting")
public class VideoSetting implements Serializable{
	
	@PrimaryKeyMapper
	private String id;                     //主键id（uuid）		string
	private int resolution;		  //视频分辨率 (eg：1:1024*768,...)             int
	private int format;             //视频格式（eg:1:avi,...）		int
	private int brightness;			//视频亮度	(1-10)	int
	private int contrast;			//视频对比度(1-10)		int
	private int saturation;			//视频饱和度(1-10)		int
	private int chroma;			//视频色度(1-10)		int
	private String vidicon_id;               //摄像头		string
	private String extend2;               //备用字段2		string
	private String extend3;               //备用字段3		string
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getResolution() {
		return resolution;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	public int getFormat() {
		return format;
	}
	public void setFormat(int format) {
		this.format = format;
	}
	
	
	public int getBrightness() {
		return brightness;
	}
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	public int getContrast() {
		return contrast;
	}
	public void setContrast(int contrast) {
		this.contrast = contrast;
	}
	public int getSaturation() {
		return saturation;
	}
	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}
	public int getChroma() {
		return chroma;
	}
	public void setChroma(int chroma) {
		this.chroma = chroma;
	}
	public String getVidicon_id() {
		return vidicon_id;
	}
	public void setVidicon_id(String vidicon_id) {
		this.vidicon_id = vidicon_id;
	}
	public String getExtend2() {
		return extend2;
	}
	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}
	public String getExtend3() {
		return extend3;
	}
	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}
	
	
}
