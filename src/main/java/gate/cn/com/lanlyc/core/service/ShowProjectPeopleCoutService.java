package gate.cn.com.lanlyc.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.dao.ShowProjectPeopleCoutDao;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.ShowProjectPeopleCout;

@Service
@Transactional
public class ShowProjectPeopleCoutService {

	@Autowired
	private ShowProjectPeopleCoutDao showProjectPeopleCoutDao;

	public ShowProjectPeopleCoutDao getShowProjectPeopleCoutDao() {
		return showProjectPeopleCoutDao;
	}

	public void setShowProjectPeopleCoutDao(ShowProjectPeopleCoutDao showProjectPeopleCoutDao) {
		this.showProjectPeopleCoutDao = showProjectPeopleCoutDao;
	}


}

















