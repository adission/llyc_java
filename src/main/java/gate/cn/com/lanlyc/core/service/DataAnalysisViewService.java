package gate.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gate.cn.com.lanlyc.core.dao.DataAnalysisViewDao;

@Service
@Transactional
public class DataAnalysisViewService {
	@Autowired
	private DataAnalysisViewDao DataAnalysisViewDao;

	public DataAnalysisViewDao getDataAnalysisViewDao() {
		return DataAnalysisViewDao;
	}

	public void setDataAnalysisViewDao(DataAnalysisViewDao dataAnalysisViewDao) {
		DataAnalysisViewDao = dataAnalysisViewDao;
	}
}
