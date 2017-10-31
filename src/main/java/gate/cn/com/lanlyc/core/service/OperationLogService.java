package gate.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.DateUtils;
import gate.cn.com.lanlyc.core.dao.OperationLogDao;
import gate.cn.com.lanlyc.core.po.OperationLog;
@Service
@Transactional
public class OperationLogService {
	
	@Autowired
	 private OperationLogDao operationlogdao;

	public OperationLogDao getOperationlogdao() {
		return operationlogdao;
	}

	public void setOperationlogdao(OperationLogDao operationlogdao) {
		this.operationlogdao = operationlogdao;
	}

	public boolean addlog(String operation_action,String operation_desc,String operation_user) 
	{
		String id = DataUtils.getUUID();
		String operation_time = DateUtils.getCurrenTimeStamp();
		OperationLog OL = new OperationLog();
		OL.setId(id);
		
		OL.setOperation_action(operation_action);
		
		OL.setOperation_desc(operation_desc);
		OL.setOperation_time(operation_time);
		
		OL.setOperation_user(operation_user);
		
		int num=this.getOperationlogdao().save(OL);
		boolean is_success=false;
		if(num>0) 
		{
			is_success=true;
		}
		return is_success;
	}
	
}
