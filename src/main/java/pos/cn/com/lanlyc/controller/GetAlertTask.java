package pos.cn.com.lanlyc.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.service.ServiceContainer;
import pos.cn.com.lanlyc.core.po.PosAlert;


/**
 * 
 * @author cjt
 * 定时轮询最新告警信息，并添加进告警信息表中
 *
 */
@Component("alertTask")
@Lazy(false)
public class GetAlertTask extends BaseController{
	static long alertCount = 1l;
	@Autowired
	private ServiceContainer gateService;
	
	private List<GateUser> userArray=null;
//	@Scheduled(cron="0/30 * * * * ?")
//	public void addAlert() {
//		
//		//调用紫峰接口得到告警记录
//		
//		//遍历结果，添加筛选条件：距离当前时间30s内即为最新告警，将符合条件的告警信息添加到数据库中
//		
//		//调用websorket发送消息方法
//	}
	/**
	 * 测试用，模拟生成告警数据
	 */
	//@Scheduled(cron="0/120 * * * * ?")
	public void addAlert() {
		
		//生成告警记录
//		int max = (int)(3*Math.random());
//		Set<String> layerSet = new HashSet<>();
//		if(max>0) {
//			for (int i = 0; i < max; i++) {
//				PosAlert pa = createPosAlert("测试报警"+alertCount);
//				this.getService().getPosAlertService().getDao().save(pa);
//				layerSet.add(pa.getArea_id());
//				alertCount++;
//			}
//		}
		
		//给前台发送消息
		AlertServer as = new AlertServer();
//		for (String layerId : layerSet) {
			as.sendnew_alarm("123");
//		}
		
	}	
	
	public PosAlert createPosAlert(String context) {
		PosAlert pa = new PosAlert();
		pa.setId(DataUtils.getUUID());
		pa.setAlertContext(context);
		String[] types = {"事故报警","区域报警"};
 		pa.setAlertType(types[(int)(2*Math.random())]);
		pa.setEquipId("123");
		pa.setArea_id("123");
		pa.setMaterialId("");
		userArray = getUserList();
		pa.setPerId(userArray.get((int)(userArray.size()*Math.random())).getId());
		pa.setSection("123");
		pa.setStatus("未处理");
		pa.setTime(new Date());
		pa.setTimeStamp(new Date().getTime());
		return pa;
	}

	public ServiceContainer getGateService() {
		return gateService;
	}
	
	public List<GateUser> getUserList() {
		if(userArray==null) {
			userArray = gateService.getGateuserservice().getGateuser().getAll();
		}
		return userArray;
	}

	
}
