package gate.cn.com.lanlyc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service容器
 * 
 * @author Jerry Zhou
 */

@Service
public class ServiceContainer {


    
    /**
     * 系统管理员Service
     */
   @Autowired
    private AdminUserService adminuserService;
    
    /**
     * 人员Service
     */
   @Autowired
    private GateUserService gateuserservice;
    /**
     * 请求日志Service
     */
   @Autowired
    private RequestLogService requestlogservice;
    /**
     * 所有工种Service
     */
   @Autowired
    private WorkersTypesService workerstypesservice;
    /**
     * 系统设置Service
     */
   @Autowired
    private SettingService settingservice;
    /**
     * 闸机分组Service
     */
   @Autowired
    private GateGroupService gategroupservice;
    
    /**
     * 考勤用户分类表Service
     */
   @Autowired
    private GateUserClassService gateuserclassservice;
    
   
   /**
    * 用户刷卡考勤记录表Service
    */
   	@Autowired
   	private CheckLogService checklogservice;

    /**
     * 管理员日志表的Service
     */
   @Autowired
    private OperationLogService operationlogService;
    /**
     * 
     * 闸机Service
     * @return
     */
   /**
    * 考勤设备通讯日志表的Service
    */
  @Autowired
   private ConnectLogService connectLogService;
   
   @Autowired
    private GateListService gatelistservice;
    
    /**
     * 闸机版相关操作service
     *
     */
   @Autowired
   private  GateBoardService gateboradservie;
   /**
    * 闸机权限操作service
    *
    */
   @Autowired
  	private  GateUserAuthService gateuserauthservie;
   
   /**
    * 闸机权限操作service
    *
    */
   
  @Autowired
  private FaceService faceservice;
  
  /**
   * 微耕操作service
   *
   */
  
 @Autowired
 private WgService wgService;
  
  
  /**
   * 人员统计分析的service
   * 
   */
  @Autowired
 private DataAnalysisViewService datanalysisiewservice;
  /**
   * 有关人员权限的相应的service
   */
  
  @Autowired
  private UserCardsService userCardsService;
  
  @Autowired
  private ShowProjectPeopleCoutService showProjectPeopleCoutService;
  
  /**
   * 访客
   */
  @Autowired
  private GateVisitService gateVisitService;
  
  /**
   * 证件类型
   */
  @Autowired
  private PapTypesService papTypesService;
  
  /**
   * 车辆类型
   */
  @Autowired
  private CarTypesService carTypesService;
  
    public CarTypesService getCarTypesService() {
	return carTypesService;
}

	
	public void setCarTypesService(CarTypesService carTypesService) {
		this.carTypesService = carTypesService;
	}
	

	public PapTypesService getPapTypesService() {
	return papTypesService;
}
	
	public void setPapTypesService(PapTypesService papTypesService) {
		this.papTypesService = papTypesService;
	}


	public GateVisitService getGateVisitService() {
	return gateVisitService;
}

public void setGateVisitService(GateVisitService gateVisitService) {
	this.gateVisitService = gateVisitService;
}

	public WgService getWgService() {
	return wgService;
}

public void setWgService(WgService wgService) {
	this.wgService = wgService;
}

	public UserCardsService getUserCardsService() {
	return userCardsService;
}

public void setUserCardsService(UserCardsService userCardsService) {
	this.userCardsService = userCardsService;
}

	public GateBoardService getGateboradservie() {
	return gateboradservie;
	}
	
	public void setGateboradservie(GateBoardService gateboradservie) {
		this.gateboradservie = gateboradservie;
	}

	public GateUserAuthService getGateuserauthservie() {
		return gateuserauthservie;
	}
	
	public void setGateuserauthservie(GateUserAuthService gateuserauthservie) {
		this.gateuserauthservie = gateuserauthservie;
	}

	public GateListService getGatelistservice() {
		return gatelistservice;
	}

	public void setGatelistservice(GateListService gatelistservice) {
		this.gatelistservice = gatelistservice;
	}

	public OperationLogService getOperationlogService() {
		return operationlogService;
	}

	public void setOperationlogService(OperationLogService operationlogService) {
		this.operationlogService = operationlogService;
	}

	public GateUserClassService getGateuserclassservice() {
		return gateuserclassservice;
	}

	public void setGateuserclassservice(GateUserClassService gateuserclassservice) {
		this.gateuserclassservice = gateuserclassservice;
	}

	public GateGroupService getGategroupservice() {
		return gategroupservice;
	}

	public void setGategroupservice(GateGroupService gategroupservice) {
		this.gategroupservice = gategroupservice;
	}

	public SettingService getSettingservice() {
		return settingservice;
	}

	public void setSettingservice(SettingService settingservice) {
		this.settingservice = settingservice;
	}

	public AdminUserService getAdminuserService() {
		return adminuserService;
	}

	public void setAdminuserService(AdminUserService adminuserService) {
		this.adminuserService = adminuserService;
	}

	public GateUserService getGateuserservice() {
		return gateuserservice;
	}

	public void setGateuserservice(GateUserService gateuserservice) {
		this.gateuserservice = gateuserservice;
	}

	public RequestLogService getRequestlogservice() {
		return requestlogservice;
	}

	public void setRequestlogservice(RequestLogService requestlogservice) {
		this.requestlogservice = requestlogservice;
	}

	public WorkersTypesService getWorkerstypesservice() {
		return workerstypesservice;
	}

	public void setWorkerstypesservice(WorkersTypesService workerstypesservice) {
		this.workerstypesservice = workerstypesservice;
	}


    
    public AdminUserService getAdminUserService() {
        return adminuserService;
    }

	public ConnectLogService getConnectLogService() {
		return connectLogService;
	}

	public void setConnectLogService(ConnectLogService connectLogService) {
		this.connectLogService = connectLogService;
	}

	public FaceService getFaceservice() {
		return faceservice;
	}

	public void setFaceservice(FaceService faceservice) {
		this.faceservice = faceservice;
	}

	public CheckLogService getChecklogservice() {
		return checklogservice;
	}

	public void setChecklogservice(CheckLogService checklogservice) {
		this.checklogservice = checklogservice;
	}

	public ShowProjectPeopleCoutService getShowProjectPeopleCoutService() {
		return showProjectPeopleCoutService;
	}

	public void setShowProjectPeopleCoutService(ShowProjectPeopleCoutService showProjectPeopleCoutService) {
		this.showProjectPeopleCoutService = showProjectPeopleCoutService;
	}

	public DataAnalysisViewService getDatanalysisiewservice() {
		return datanalysisiewservice;
	}

	public void setDatanalysisiewservice(DataAnalysisViewService datanalysisiewservice) {
		this.datanalysisiewservice = datanalysisiewservice;
	}

}
