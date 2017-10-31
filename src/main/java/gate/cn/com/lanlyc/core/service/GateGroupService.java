package gate.cn.com.lanlyc.core.service;

import java.nio.channels.GatheringByteChannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gate.cn.com.lanlyc.core.dao.GateGroupDao;
import gate.cn.com.lanlyc.core.po.GateGroup;

@Transactional
@Service("GateGroupService")
public class GateGroupService {
	@Autowired
	private GateGroupDao gategroupdao;

	public GateGroupDao getGategroupdao() {
		return gategroupdao;
	}

	public void setGategroupdao(GateGroupDao gategroupdao) {
		this.gategroupdao = gategroupdao;
	}

	// 进行分区名称是否重复的验证
	public GateGroup testGroup(String group_name, String id) {
		// TODO Auto-generated method stub
		if (id == null) {// 添加时进行的名称验证
			GateGroup outCome = gategroupdao.testGroupName(group_name, null);
			return outCome;
		} else {
			GateGroup outCome = gategroupdao.testGroupName(group_name, id);
			return outCome;
		}
	}
}
