package video.cn.com.lanlyc.core.po;

import java.io.Serializable;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:摄像头设置相关实体类
 * @date:2017年9月18日 上午10:51:20
 */
@TableMapper("video_t_vidicon_setting")
public class VidiconSetting implements Serializable{
	
	@PrimaryKeyMapper
	private String id;                     //主键id（uuid）		string
	private int focus;		  //焦点（暂略）              string
	private int preset;             //预置位置	（暂略）	int
	private int cruise;					//巡航（暂略）    int
	private int cruise_time;			//巡航时间间隔
	private String vidicon_id;               //摄像头id		string
	private String extend2;               //备用字段2		string
	private String extend3;               //备用字段3		string
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFocus() {
		return focus;
	}
	public void setFocus(int focus) {
		this.focus = focus;
	}
	public int getPreset() {
		return preset;
	}
	public void setPreset(int preset) {
		this.preset = preset;
	}
	public int getCruise() {
		return cruise;
	}
	public void setCruise(int cruise) {
		this.cruise = cruise;
	}
	
	public int getCruise_time() {
		return cruise_time;
	}
	public void setCruise_time(int cruise_time) {
		this.cruise_time = cruise_time;
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
