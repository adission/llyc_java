package video.cn.com.lanlyc.core.po;

import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:视频分屏实体类
 * @date:2017年9月6日 下午5:03:15
 */
@TableMapper("video_t_split_screen")
public class Screen implements Serializable{
	
	@PrimaryKeyMapper
	private String id;                     //主键id（uuid）		string
	private String split_screen_name;		  //分屏名称              string
	private int set_default;             //是否设置为默认（1：默认，0：非默认）		int
	private String extend1;               //备用字段1		string
	private String extend2;               //备用字段2		string
	private String extend3;               //备用字段3		string
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSplit_screen_name() {
		return split_screen_name;
	}
	public void setSplit_screen_name(String split_screen_name) {
		this.split_screen_name = split_screen_name;
	}
	
	public int getSet_default() {
		return set_default;
	}
	public void setSet_default(int set_default) {
		this.set_default = set_default;
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
