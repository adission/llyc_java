package video.cn.com.lanlyc.core.po;

import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:分屏与摄像头关系实体类
 * @date:2017年9月6日 下午5:01:50
 */
@TableMapper("video_t_screen_vidicon_relation")
public class ScreenVidicon implements Serializable {
	
    @PrimaryKeyMapper
    private String id;                     //主键id（uuid）		string
    private String split_screen_id;       // 屏id（uuid）		string
    private String vidicon_id;              //摄像头id（uuid）		string
    private int screen_position;		//在屏幕中所处的位置（1：代表位置为本屏幕中的第1行第1列）
	private String extend1;               //备用字段1			string
	private String extend2;               //备用字段2			string
	private String extend3;               //备用字段3			string
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVidicon_id() {
		return vidicon_id;
	}
	public void setVidicon_id(String vidicon_id) {
		this.vidicon_id = vidicon_id;
	}
	public String getSplit_screen_id() {
		return split_screen_id;
	}
	public void setSplit_screen_id(String split_screen_id) {
		this.split_screen_id = split_screen_id;
	}
	public int getScreen_position() {
		return screen_position;
	}
	public void setScreen_position(int screen_position) {
		this.screen_position = screen_position;
	}
	public String getExtend1() {
		return extend1;
	}
	public void setExtend1(String extend1) {
		this.extend1 = extend1;
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
