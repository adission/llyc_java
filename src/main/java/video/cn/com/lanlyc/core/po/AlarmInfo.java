package video.cn.com.lanlyc.core.po;

import java.io.Serializable;
import java.util.Date;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:报警信息表实体
 * @date:2017年9月7日 下午2:23:25
 */
@TableMapper("video_t_alarm_info")
public class AlarmInfo implements Serializable{
	@PrimaryKeyMapper
	private String id;                     //主键id（uuid）		string
	private String alarm_message;		  //报警消息             string
	private Date alarm_time;        //报警时间       Date
	private String alarm_detail;        //报警详情      String
	private String vidicon_id;        //摄像头id      String
	private String extend1;               //备用字段1		string
	private String extend2;               //备用字段2		string
	private String extend3;               //备用字段3		string
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlarm_message() {
		return alarm_message;
	}
	public void setAlarm_message(String alarm_message) {
		this.alarm_message = alarm_message;
	}
	public Date getAlarm_time() {
		return alarm_time;
	}
	public void setAlarm_time(Date alarm_time) {
		this.alarm_time = alarm_time;
	}
	public String getAlarm_detail() {
		return alarm_detail;
	}
	public void setAlarm_detail(String alarm_detail) {
		this.alarm_detail = alarm_detail;
	}
	public String getVidicon_id() {
		return vidicon_id;
	}
	public void setVidicon_id(String vidicon_id) {
		this.vidicon_id = vidicon_id;
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
