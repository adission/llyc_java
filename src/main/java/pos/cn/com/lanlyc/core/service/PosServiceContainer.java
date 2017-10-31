package pos.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PosServiceContainer {

	@Autowired
	private PosTestService testService;
	
	@Autowired
	private PosTrajectoryService posTrajectoryService;
	
	@Autowired
	private PosPerConstructService posPerConstructService;
	
	@Autowired
	private PosUserService posUserService;
	
	@Autowired
	private PosCardService posCardService;
	
	@Autowired
	private PosAlertService posAlertService;
	
	@Autowired
	private PosBaseStationService posBaseStationService;
	
	@Autowired
	private PosAssignmentsSectionService posAssignmentsSectionService;
	
	@Autowired
	private PosUserInfoVService posUserInfoVService;
	
	//摄像头
	@Autowired
	private PosCameraService posCameraService;
	//人员类型
	@Autowired
	private PosCardTypeService posCardTypeService;
	
	//项目
	@Autowired
	private PosConstructionSiteService posConstructionSiteService;
	
	//图层
	@Autowired
	private PosFloorService posFloorService;
	
	public PosFloorService getPosFloorService() {
		return posFloorService;
	}

	public PosTestService getTestService() {
		return this.testService;
	}

	public PosBaseStationService getPosBaseStationService() {
		return posBaseStationService;
	}
	
	//定位卡
	public PosCardService getPosCardService() {
		return posCardService;
	}
	
	public PosAssignmentsSectionService getPosAssignmentsSectionService() {
		return posAssignmentsSectionService;
	}

	public PosTrajectoryService getPosTrajectoryService() {
		return this.posTrajectoryService;
	}
	
	//人员工地关系
	public PosPerConstructService getPosPerConstructService() {
		return posPerConstructService;
	}
				
	public PosUserService getPosUserService() {
		return this.posUserService;
	}
			
	//人员视图
	public PosUserInfoVService getPosUserInfoVService() {
		return posUserInfoVService;
	}
	
	public PosAlertService getPosAlertService() {
		return this.posAlertService;
	}

	public PosCameraService getPosCameraService() {
		return posCameraService;
	}

	public PosCardTypeService getPosCardTypeService() {
		return posCardTypeService;
	}

	public PosConstructionSiteService getPosConstructionSiteService() {
		return posConstructionSiteService;
	}
}
