package pos.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.util.Page;
import pos.cn.com.lanlyc.core.dao.PosCameraDao;
import pos.cn.com.lanlyc.core.dto.PosCameraDto;
import video.cn.com.lanlyc.core.po.VidiconInfo;

@Service
public class PosCameraService {
	
	@Autowired
	private PosCameraDao posCameraDao;

	public PosCameraDao getPosCameraDao() {
		return posCameraDao;
	}
	
	/*
	 * 分页查询所有的/某一个图层的摄像头列表
	 */
	public Page<PosCameraDto> getPosCameraListByPage(Page<PosCameraDto> page,String layer_id,String keyWords) {
        return posCameraDao.getPosCameraListByPage(page,layer_id,keyWords);
	}
	
	/*
	 * 通过图层id 查找这个图层里面所有的摄像头
	 */
    public List<VidiconInfo> selectCameraByLayerId(String layer_id){
    	return posCameraDao.selectCameraByLayerId(layer_id);
	}
    
    
    /**
     * 保存一个摄像头到摄像头表中
     */
    public int savePosCamera(VidiconInfo pc){
    	try {
    		return posCameraDao.save(pc);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
    }
    
    /**
     * 修改摄像头
     */
    /*public int updateCamera(VidiconInfo pc){
    	try {
    		return posCameraDao.update(pc,false);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
    }*/
    
    public int updateCamera(String id,String geo_x,String geo_y,String layer_id){
    	try {
    		return posCameraDao.updateCamera(id,geo_x,geo_y,layer_id);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
    }
    
    /**
     * 删除摄像头
     */
    public int updateCamera(String id){
    	try {
    		return posCameraDao.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
    }

}
