package gate.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gate.cn.com.lanlyc.core.dao.WorkersTypesDao;
@Service("WorkersTypesService")
@Transactional
public class WorkersTypesService {
	 @Autowired
	 private WorkersTypesDao WorkersTypes;

	public WorkersTypesDao getWorkersTypes() {
		return WorkersTypes;
	}

	public void setWorkersTypes(WorkersTypesDao workersTypes) {
		WorkersTypes = workersTypes;
	}
}
