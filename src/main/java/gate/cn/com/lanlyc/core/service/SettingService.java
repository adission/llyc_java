package gate.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gate.cn.com.lanlyc.core.dao.SettingDao;

@Service
@Transactional
public class SettingService {
	@Autowired
	 private SettingDao settingdao;

	public SettingDao getSettingdao() {
		return settingdao;
	}

	public void setSettingdao(SettingDao settingdao) {
		this.settingdao = settingdao;
	}
}
