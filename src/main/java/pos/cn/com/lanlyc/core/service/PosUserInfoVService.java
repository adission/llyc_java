package pos.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pos.cn.com.lanlyc.core.dao.PosUserInfoVDao;


/**
 * 人员视图service
 * @author jiangyanyan
 * @date 2017年9月18日下午4:33:06
 * @version 1.0
 */
@Service
public class PosUserInfoVService {

	@Autowired
	private PosUserInfoVDao posUserInfoVDao;
	
	public PosUserInfoVDao getPosUserInfoVDao() {
		return posUserInfoVDao;
	}

	
	
}
