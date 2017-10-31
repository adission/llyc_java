package pos.cn.com.lanlyc.core.service;
import java.util.ArrayList;
import java.util.Date;
/**
 * @author cjt
 * 人员轨迹service
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.po.GateUser;
import pos.cn.com.lanlyc.core.dao.PosPerConstructDao;
import pos.cn.com.lanlyc.core.dao.PosTrajectoryDao;
import pos.cn.com.lanlyc.core.dto.PosSimpleUserDto;
import pos.cn.com.lanlyc.core.dto.PosTrajectoryDto;
import pos.cn.com.lanlyc.core.po.PosTrajectory;

@Service
public class PosTrajectoryService {
	
	@Autowired
	private PosTrajectoryDao dao;
	
	public PosTrajectoryDao getDao() {
		return this.dao;
	}
	public List<PosTrajectoryDto> getHistoryPos(String userId,long endtime,long starttime){
		if(DataUtils.isNullOrEmpty(userId)) {
			return null;
		}else {
			return dao.getHistoryPos(userId,endtime,starttime);
		}
	}
	public List<PosTrajectoryDto> getCurrentPos(List<String> userId) {
		return this.dao.getCurrentPos(userId);
	}
	public List<PosSimpleUserDto> getAllUser(String layerId){
		return this.dao.getAllUser(layerId);
	}
	public void deleteAll() {
		this.dao.deleteAll();
	}
	
	@Autowired
	private PosPerConstructDao posPerConstructDao;

	public PosPerConstructDao getPosPerConstructDao() {
		return this.posPerConstructDao;
	}
	
	/**
	 * 根据工地id获取人员列表，再根据人员列表获取人员坐标列表
	 * @author jiangyanyan
	 * @param constructId 工地id
	 * @return 返回人员坐标列表
	 */
//	public List<PosTrajectory> getPosTrajectoryList(String constructId){
//		String name = "";
//		String mobile = "";
//		List<GateUser> user_list = this.posPerConstructDao.getConstructPersonList(constructId,name,mobile);
//		List<PosTrajectory> posTrajectoryList = new ArrayList<PosTrajectory>();
//		for(GateUser gateUser:user_list) {
//			PosTrajectory posT = this.dao.getPosTrajectoryEntity(gateUser.getId());
//			posTrajectoryList.add(posT);
//		}
//		return posTrajectoryList;
//	}	

}
