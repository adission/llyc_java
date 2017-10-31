package pos.cn.com.lanlyc.core.service;
/**
 * @author hucong
 * 获得人员详情service
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.util.DataUtils;
import pos.cn.com.lanlyc.core.dao.PosUserDao;
import pos.cn.com.lanlyc.core.dto.UserDto;
@Service
public class PosUserService {
	@Autowired
	private PosUserDao posUserDao;

	public PosUserDao getPosUserDao() {
		return posUserDao;
	}
	public UserDto getUserById(String id){
		System.out.println("haha");
		if(DataUtils.isNullOrEmpty(id)) {
			return null;
		}else {
			return posUserDao.getUserById(id);
		}
	}
}
