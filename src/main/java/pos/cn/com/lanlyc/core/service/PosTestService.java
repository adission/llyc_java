package pos.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pos.cn.com.lanlyc.core.dao.PosTestDao;

@Service
public class PosTestService {

	@Autowired
	PosTestDao testDao;
	
	public PosTestDao getTsetDao() {
		return this.testDao;
	}
}
