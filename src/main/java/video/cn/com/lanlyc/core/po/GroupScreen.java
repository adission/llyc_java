package video.cn.com.lanlyc.core.po;

import java.io.Serializable;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:分组和分屏关系表实体类
 * @date:2017年9月12日 上午11:41:17
 */
@TableMapper("video_t_group_screen_relation")
public class GroupScreen implements Serializable {
	
	@PrimaryKeyMapper
    private String id;                     //主键id（uuid）		string
    private String vidicon_group_id;       // 分组id（uuid）		string
    private String split_screen_id;			//分屏id
    private int sort;					//分屏排序		int
	private String extend1;               //备用字段1			string
	private String extend2;               //备用字段2			string
	private String extend3;               //备用字段3			string
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVidicon_group_id() {
		return vidicon_group_id;
	}
	public void setVidicon_group_id(String vidicon_group_id) {
		this.vidicon_group_id = vidicon_group_id;
	}
	public String getSplit_screen_id() {
		return split_screen_id;
	}
	public void setSplit_screen_id(String split_screen_id) {
		this.split_screen_id = split_screen_id;
	}
	
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
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
