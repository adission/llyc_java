package video.cn.com.lanlyc.core.dto;

import java.io.Serializable;

import video.cn.com.lanlyc.core.po.GroupScreen;

/**
 * 组和屏Dto
 * @author chenyan
 * @date 2017年9月12日 15:03:13
 */
public class GroupScreenDto extends GroupScreen implements Serializable {

	private static final long serialVersionUID = 1L;

	private String split_screen_name;

	public String getSplit_screen_name() {
		return split_screen_name;
	}

	public void setSplit_screen_name(String split_screen_name) {
		this.split_screen_name = split_screen_name;
	}

}
