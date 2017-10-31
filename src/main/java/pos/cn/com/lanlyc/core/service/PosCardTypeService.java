package pos.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pos.cn.com.lanlyc.core.dao.PosCardTypeDao;

/**
 * 
 * @author cjt
 * 人员类型service
 */
@Service
public class PosCardTypeService {

	@Autowired
	private PosCardTypeDao dao;

	public PosCardTypeDao getDao() {
		return dao;
	}

	
}
