package gate.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gate.cn.com.lanlyc.core.dao.RequestLogDao;
@Service
@Transactional
public class RequestLogService {
	
	@Autowired
	 private RequestLogDao requestlogdao;

	public RequestLogDao getRequestlogdao() {
		return requestlogdao;
	}

	public void setRequestlogdao(RequestLogDao requestlogdao) {
		this.requestlogdao = requestlogdao;
	}
}
