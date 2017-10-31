package pos.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.util.Page;
import pos.cn.com.lanlyc.core.dao.PosAlertDao;
import pos.cn.com.lanlyc.core.dto.PosAlertDto;
import pos.cn.com.lanlyc.core.po.PosAlert;

/**
 * 
 * @author cjt
 * 告警service
 *
 */
@Service
public class PosAlertService {

	@Autowired
	private PosAlertDao dao;
	
	public PosAlertDao getDao() {
		return this.dao;
	}
	
	public List<PosAlert> getNewAlert(String layer_id){
		return this.dao.newAlert(layer_id);
	}
	
	public Page<PosAlertDto> getHistory(Page<PosAlertDto> page,String keyword,String type,String status){
		return this.dao.history(page, keyword,type,status);
	}
	
	public List<PosAlert> getAllWarning(){
		
		return this.dao.getAllWarning();
	}
}
