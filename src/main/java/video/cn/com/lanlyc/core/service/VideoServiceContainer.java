package video.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:service容器
 * @date:2017年9月11日 上午10:17:12
 */
@Service
public class VideoServiceContainer {
	
	@Autowired
	private VidiconService vidiconService; 
	
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:获取摄像头的service
	 * @param:无
	 * @return:摄像头的service类
	 * @date:2017年9月11日 上午10:16:22
	 */
    public VidiconService getVidiconService() {
        return vidiconService;
    }

	public void setVidiconService(VidiconService vidiconService) {
		this.vidiconService = vidiconService;
	}

	
}
