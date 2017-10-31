package pos.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pos.cn.com.lanlyc.core.dao.PosConstructionSiteDao;
import pos.cn.com.lanlyc.core.po.PosConstructionSite;
    @Service
   public class PosConstructionSiteService {

	@Autowired
	private PosConstructionSiteDao posConstructionSiteDao;
	
	public List<PosConstructionSite>selectConstructionSite(String id){
		return posConstructionSiteDao.getConstructionSite(id);
	}
}
