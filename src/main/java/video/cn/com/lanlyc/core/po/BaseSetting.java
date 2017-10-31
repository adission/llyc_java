package video.cn.com.lanlyc.core.po;

import java.io.Serializable;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:基本模式设置实体类
 * @date:2017年9月19日 上午11:26:28
 */
@TableMapper("video_t_base_setting")
public class BaseSetting implements Serializable {
	
	@PrimaryKeyMapper
    private String id;                     //主键id（uuid）		string
    private int function_mode;       // 功能模式
    private int screen_mode;              //分屏模式		int
    private int polling_mode;		//轮询模式		int
    private int polling_time;		//轮询时间间隔		int
	private String vidicon_id;               //摄像头id			string
	private String extend2;               //备用字段2			string
	private String extend3;               //备用字段3			string
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFunction_mode() {
		return function_mode;
	}
	public void setFunction_mode(int function_mode) {
		this.function_mode = function_mode;
	}
	public int getScreen_mode() {
		return screen_mode;
	}
	public void setScreen_mode(int screen_mode) {
		this.screen_mode = screen_mode;
	}
	public int getPolling_mode() {
		return polling_mode;
	}
	public void setPolling_mode(int polling_mode) {
		this.polling_mode = polling_mode;
	}
	
	public int getPolling_time() {
		return polling_time;
	}
	public void setPolling_time(int polling_time) {
		this.polling_time = polling_time;
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
