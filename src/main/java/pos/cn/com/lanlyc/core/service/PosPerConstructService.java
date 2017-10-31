package pos.cn.com.lanlyc.core.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import gate.cn.com.lanlyc.core.dao.WorkersTypesDao;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import pos.cn.com.lanlyc.core.dao.PosPerConstructDao;
import pos.cn.com.lanlyc.core.dao.PosTrajectoryDao;
import pos.cn.com.lanlyc.core.dto.PosTrajectoryDto;


@Service
public class PosPerConstructService {
	
	@Autowired
	private PosPerConstructDao posPerConstructDao;
	
	//获取工种类型
	@Autowired
	private WorkersTypesDao workersTypesDao;
	
	@Autowired
	private PosTrajectoryDao posTrajectoryDao;
	
	
	public PosTrajectoryDao getPosTrajectoryDao() {
		return posTrajectoryDao;
	}

	public PosPerConstructDao getPosPerConstructDao() {
		return this.posPerConstructDao;
	}
	
	public WorkersTypesDao getWorkersTypesDao() {
		return workersTypesDao;
	}


	/**
	 * 获取工地人员信息
	 * @param layer_id 图层id
	 * @author jiangyanyan
	 * @return
	 */
	public List<Map<String,Object>> getConstructPersonList(String layer_id){
		String work_type = this.getWorkersTypesDao().AllworkStr();
		JSONArray json_work = JSONArray.parseArray(work_type); //获取所有工种
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		//取出图层中的卡号不为空的所有工种人员
		List<GateUserInfoView> userArray = this.posPerConstructDao.getConstructPersonList2(layer_id); 
		List<Map<String ,Object >> jsonFKList = new ArrayList<Map<String ,Object >>(); //访客
		Map<String ,Object > jsonFKMap = new HashMap< String , Object>();
		jsonFKMap.put("type_value", "");
		jsonFKMap.put("work_name", "访客");
		for(int i=0;i<json_work.size();i++) {
			List<Map<String ,Object >> wtArray = new ArrayList<Map<String ,Object >>();
			Map<String ,Object > jsonMap = new HashMap< String , Object>();
			JSONObject  mapOb = json_work.getJSONObject(i); //取出工种
			String workType = mapOb.getString("type_name"); //工种名称
			String type_value = mapOb.getString("type_value");//工种值
			jsonMap.put("type_value",type_value);
			jsonMap.put("work_name", workType);
			for(int j=0;j<userArray.size();j++) {
				GateUserInfoView userInfo = userArray.get(j); //取出人员对象
				Map<String,Object> userMap = new HashMap<String,Object>();//人员map
				if(workType.equals(userInfo.getWorker_type())){
					userMap.put("id", userInfo.getId());
					userMap.put("name", userInfo.getName());
					userMap.put("card_sn", userInfo.getCard_sn());
					userMap.put("staff_visitor", userInfo.getStaff_visitor());
					wtArray.add(userMap);
				}
				if(i==0&&userInfo.getWorker_type()==null) {
					userMap.put("id", userInfo.getId());
					userMap.put("name", userInfo.getName());
					userMap.put("card_sn", userInfo.getCard_sn());
					userMap.put("staff_visitor", userInfo.getStaff_visitor());
					jsonFKList.add(userMap);
				}
				
			}
			jsonMap.put("type_name", wtArray);
			res.add(jsonMap);
		}
		jsonFKMap.put("type_name", jsonFKList);
		res.add(jsonFKMap);
		return res;
	}
	
	
	/**
	 * 根据搜索的内容获取所有绑定定位卡的人员
	 * @param searchCon 搜索的内容
	 * @author jiangyanyan
	 * @return
	 */
	public List<GateUserInfoView> getSearchUserInfoListService(String searchCon){
		//根据搜索关键字，取出所有人员
		List<GateUserInfoView> result = this.getPosPerConstructDao().getSearchUserInfoList(searchCon);
		//放所有人员id的列表
		List<String> userList = new ArrayList<String>(); 
		//循环取出所有人员id,放到人员id的列表中
		for(int i=0;i<result.size();i++) {
			GateUserInfoView userInfo = result.get(i);
			userList.add(userInfo.getId());
		}
		if(userList.size()!=0) {
			List<PosTrajectoryDto> traList = this.getPosTrajectoryDao().getCurrentPos(userList);
			for(int j=0;j<traList.size();j++) {
				//取出人员
				GateUserInfoView userInfo = result.get(j);
				//取出人员对应的坐标点
				PosTrajectoryDto tra = traList.get(j);
				userInfo.setGeo_x(String.valueOf(tra.getX()));
				userInfo.setGeo_y(String.valueOf(tra.getY()));
				result.set(j, userInfo);
			}
		}
		return result;	
	}
	
	
	
}
