package video.cn.com.lanlyc.core.po;

import java.io.Serializable;
import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:摄像头分组实体类
 * @date:2017年9月6日 下午4:59:46
 */
@TableMapper("video_t_vidicon_group")
public class Group implements Serializable {
	
    @PrimaryKeyMapper
	private String id;                     //主键id		string
	private String group_name;            //分组名称		string
	private int set_default;             //是否设置为默认（1：是，2：否，-1：初始化）		int
	private int split_screen_type;		//分屏方式	（1:1*1,2:2*2,3:3*3,4:4*4）
	private int whether_important;		//是否标记为重点（1：是，2否）	int
	private Date grouping_time;			//分组时间
	private String grouping_person;		//分组人
//	private String extend1;              //备用字段1		string
//	private String extend2;             // 备用字段2		string
//	private String extend3;            //备用字段3			string
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public int getSet_default() {
		return set_default;
	}
	public void setSet_default(int set_default) {
		this.set_default = set_default;
	}
	
	public int getSplit_screen_type() {
		return split_screen_type;
	}
	public void setSplit_screen_type(int split_screen_type) {
		this.split_screen_type = split_screen_type;
	}
	public int getWhether_important() {
		return whether_important;
	}
	public void setWhether_important(int whether_important) {
		this.whether_important = whether_important;
	}
	public Date getGrouping_time() {
		return grouping_time;
	}
	public void setGrouping_time(Date grouping_time) {
		this.grouping_time = grouping_time;
	}
	public String getGrouping_person() {
		return grouping_person;
	}
	public void setGrouping_person(String grouping_person) {
		this.grouping_person = grouping_person;
	}
//	public String getExtend1() {
//		return extend1;
//	}
//	public void setExtend1(String extend1) {
//		this.extend1 = extend1;
//	}
//	public String getExtend2() {
//		return extend2;
//	}
//	public void setExtend2(String extend2) {
//		this.extend2 = extend2;
//	}
//	public String getExtend3() {
//		return extend3;
//	}
//	public void setExtend3(String extend3) {
//		this.extend3 = extend3;
//	}
	
	
}
