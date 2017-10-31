package gate.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gate.cn.com.lanlyc.core.dao.GateUserClassDao;
import gate.cn.com.lanlyc.core.po.GateGroup;
import gate.cn.com.lanlyc.core.po.GateUserClass;

@Service("GateUserClassService")
@Transactional
public class GateUserClassService {
	@Autowired
	private GateUserClassDao gateuserclass;

	public GateUserClassDao getGateuserclass() {
		return gateuserclass;
	}

	public void setGateuserclass(GateUserClassDao gateuserclass) {
		this.gateuserclass = gateuserclass;
	}

	// 进行相应人员类别的名称验证
	public GateUserClass testUserClass(String name, String id) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (id == null) {// 添加时进行的名称验证
			GateUserClass outCome = gateuserclass.testUserClass(name, null);
			return outCome;
		} else {
			GateUserClass outCome = gateuserclass.testUserClass(name, id);
			return outCome;
		}
	}

}
