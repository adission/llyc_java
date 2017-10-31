package pos.cn.com.lanlyc.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pos.cn.com.lanlyc.core.dao.PosCardDao;
import pos.cn.com.lanlyc.core.dao.PosFloorDao;
import pos.cn.com.lanlyc.core.dao.PosTrajectoryDao;
import pos.cn.com.lanlyc.core.po.PosFloor;
import pos.cn.com.lanlyc.core.po.PosTrajectory;


/**
 * @author jiangyanyan
 * @date 2017年9月27日 17:29:23
 * @version 1.0
 */


@Service
public class PosFloorService {
	
	@Autowired
	private PosFloorDao posFloorDao;

	public PosFloorDao getPosFloorDao() {
		return posFloorDao;
	}
	
	@Autowired
	private PosCardDao posCardDao;
	
	public PosCardDao getPosCardDao() {
		return posCardDao;
	}
	
	@Autowired
	private PosTrajectoryDao posTrajectoryDao;
	
	public PosTrajectoryDao getPosTrajectoryDao() {
		return posTrajectoryDao;
	}



	/**
	 * 获取图层列表
	 * @return 返回一个工地的所有图层
	 * @author jiangyanyan
	 */
	public List<PosFloor> getFloorInfoService(){
		List<PosFloor> floorList = this.getPosFloorDao().getFloorInfo();
		List<PosFloor> floorListNew = new ArrayList<PosFloor>();
		List<String> floorIds = new ArrayList<String>(); //图层id列表
		for(int i=0;i<floorList.size();i++) {
			PosFloor pf = floorList.get(i);
			floorIds.add(pf.getId());//把图层id放入列表中
//			int user_count = this.getPosCardDao().getCardCountByLayerId(pf.getId());
//			pf.setUser_count(String.valueOf(user_count));
//			floorListNew.add(pf);
		}
		if(floorIds.size()==0) {
			return floorListNew;
		}
		
		List<PosTrajectory> posList = this.getPosTrajectoryDao().getLayerUserCount(floorIds);
		for(int j=0;j<floorList.size();j++) {
			PosFloor pf = floorList.get(j); //图层对象
			int user_count = 0;
			String perId = "12";
			for(int m=0;m<posList.size();m++) {
				PosTrajectory posT = posList.get(m);
				if(pf.getId().equals(posT.getStationId()) && !perId.equals(posT.getPerId())) {
					user_count++;
					perId = posT.getPerId();
				}
			}
			pf.setUser_count(String.valueOf(user_count));
			floorListNew.add(pf);	
		}
		return floorListNew;
	}
	
	
	
	/**
	 * 获取一个工地的所有图层(只有图层id、name)
	 * @author jiangyanyan
	 * @return 图层列表
	 */
	public List<Map<String,Object>> getFloorInfoLessService(){
		List<PosFloor> floorList = this.getPosFloorDao().getFloorInfo();
		List<Map<String,Object>> floorLM = new ArrayList<Map<String,Object>>();
		for(int i=0;i<floorList.size();i++){
			Map<String,Object> floorMap = new HashMap<String,Object>();
			PosFloor posF = floorList.get(i);
			floorMap.put("id", posF.getId());
			floorMap.put("name", posF.getLayer_name());
			floorMap.put("bjtx", posF.getBjtx());
			floorMap.put("min_x", posF.getMin_x());
			floorMap.put("min_y", posF.getMin_y());
			floorMap.put("max_x", posF.getMax_x());
			floorMap.put("max_y", posF.getMax_y());
			floorLM.add(floorMap);
		}
		return floorLM;
	}
	
	
	/**
	 * 根据图层id获取图层信息
	 * @param id 图层id
	 * @return
	 */
	public Map<String,Object> getPosFloorBJTPZB(String id){
		PosFloor posF = this.getPosFloorDao().getFloorObject(id);
		Map<String,Object> floorMap = new HashMap<String,Object>();
		floorMap.put("bjtx", posF.getBjtx());
		floorMap.put("min_x", posF.getMin_x());
		floorMap.put("min_y", posF.getMin_y());
		floorMap.put("max_x", posF.getMax_x());
		floorMap.put("max_y", posF.getMax_y());
		return floorMap;	
	}
	
	
	/**
	 * 查询数据表中第一个图层
	 * @author jiangyanyan
	 * @return 返回项目中的第一个图层列表
	 */
	public Map<String,Object> getOneFloorInfo(){
		List<PosFloor> posFL = this.getPosFloorDao().getOneFloorInfo();
		PosFloor posF = posFL.get(0);
		Map<String,Object> floorMap = new HashMap<String,Object>();
		floorMap.put("id", posF.getId());
		floorMap.put("bjtx", posF.getBjtx());
		floorMap.put("name", posF.getLayer_name());
		floorMap.put("min_x", posF.getMin_x());
		floorMap.put("min_y", posF.getMin_y());
		floorMap.put("max_x", posF.getMax_x());
		floorMap.put("max_y", posF.getMax_y());
		return floorMap;	
	}
	
	
	

}
