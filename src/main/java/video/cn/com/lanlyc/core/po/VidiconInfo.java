package video.cn.com.lanlyc.core.po;

import java.io.Serializable;
import java.util.Date;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:摄像头信息表实体类
 * @date:2017年9月6日 下午5:03:25
 */
@TableMapper("video_t_vidicon_info")
public class VidiconInfo implements Serializable{
	
    @PrimaryKeyMapper
	private String id;                   //主键id（uuid）       string
	private int vidicon_number;       //摄像头编号（从1开始自动递增）		int
	private String vidicon_name;         //摄像头名称		string
	private String vidicon_ip;			//摄像头ip	String
	private int vidicon_port;		//摄像头端口号	int
	private String vidicon_username;	//摄像头登录用户名	String
	private String vidicon_password;	//摄像头密码	String
	private String vidicon_desc;          //摄像头描述		string
	private int vidicon_type;			//摄像头类型		string
	private boolean whether_important;    //是否标记为重点（1：是，0：否） boolean
	private Date vidicon_add_time;	//添加时间			string
	private String geo_x;			//摄像头横坐标
	private String geo_y;			//摄像头总坐标
	private String layer_id;		//摄像头图层id
	private String nvr_ip;             // 备用字段1		string
	private String nvr_username;              //备用字段2		string
	private String nvr_password;              //备用字段3		string
	private int nvr_port;              //备用字段3		string
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getVidicon_number() {
		return vidicon_number;
	}
	public void setVidicon_number(int vidicon_number) {
		this.vidicon_number = vidicon_number;
	}
	public String getVidicon_name() {
		return vidicon_name;
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
	public int getVidicon_port() {
		return vidicon_port;
	}
	public void setVidicon_port(int vidicon_port) {
		this.vidicon_port = vidicon_port;
	}
	public String getVidicon_username() {
		return vidicon_username;
	}
	public void setVidicon_username(String vidicon_username) {
		this.vidicon_username = vidicon_username;
	}
	public String getVidicon_password() {
		return vidicon_password;
	}
	public void setVidicon_password(String vidicon_password) {
		this.vidicon_password = vidicon_password;
	}
	public String getVidicon_desc() {
		return vidicon_desc;
	}
	public void setVidicon_desc(String vidicon_desc) {
		this.vidicon_desc = vidicon_desc;
	}
	public int getVidicon_type() {
		return vidicon_type;
	}
	public void setVidicon_type(int vidicon_type) {
		this.vidicon_type = vidicon_type;
	}
	
	public boolean getWhether_important() {
		return whether_important;
	}
	public void setWhether_important(boolean whether_important) {
		this.whether_important = whether_important;
	}
	public String getGeo_x() {
		return geo_x;
	}
	public void setGeo_x(String geo_x) {
		this.geo_x = geo_x;
	}
	public String getGeo_y() {
		return geo_y;
	}
	public void setGeo_y(String geo_y) {
		this.geo_y = geo_y;
	}
	public String getLayer_id() {
		return layer_id;
	}
	public void setLayer_id(String layer_id) {
		this.layer_id = layer_id;
	}
	
	public Date getVidicon_add_time() {
		return vidicon_add_time;
	}
	public void setVidicon_add_time(Date vidicon_add_time) {
		this.vidicon_add_time = vidicon_add_time;
	}
	public String getNvr_ip() {
		return nvr_ip;
	}
	public void setNvr_ip(String nvr_ip) {
		this.nvr_ip = nvr_ip;
	}
	public String getNvr_username() {
		return nvr_username;
	}
	public void setNvr_username(String nvr_username) {
		this.nvr_username = nvr_username;
	}
	public String getNvr_password() {
		return nvr_password;
	}
	public void setNvr_password(String nvr_password) {
		this.nvr_password = nvr_password;
	}
	public int getNvr_port() {
		return nvr_port;
	}
	public void setNvr_port(int nvr_port) {
		this.nvr_port = nvr_port;
	}

	
	
}
