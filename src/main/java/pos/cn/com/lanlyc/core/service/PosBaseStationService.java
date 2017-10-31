package pos.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.util.DataUtils;
import pos.cn.com.lanlyc.core.dao.PosBaseStationDao;
import pos.cn.com.lanlyc.core.dto.PosBaseStationDto;
import pos.cn.com.lanlyc.core.po.PosBaseStation;

@Service
public class PosBaseStationService {
	@Autowired
	private PosBaseStationDao posBaseStationDao;

	public PosBaseStationDao getPosBaseStationDao() {
		return posBaseStationDao;
	}
	
	/*
	 * 查询指定图层上指定类型的基站列表
	 */
    public List<PosBaseStationDto> selectBaseStationList(String type,String layer_id){
    	return posBaseStationDao.selectBaseStationList(type,layer_id);
	}
    
    /**
     * 保存一个基站到基站列表中
     */
    public int saveBaseStation(PosBaseStation ps){
    	try {
    		return posBaseStationDao.save(ps);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
    	
    }
    
    /**
   	 * 根据基站id，查询基站信息
   	 * 
   	 * @return
   	 */
       public PosBaseStationDto getBaseStationById(String id){
   		if(DataUtils.isNullOrEmpty(id)) {
   			return null;
   		}else {
   			return posBaseStationDao.getPosBaseStationById(id);
   		}
   	}
}
