package gate.cn.com.lanlyc.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.lanlyc.core.util.GateBoardController;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.service.GateBoardService;
import gate.cn.com.lanlyc.core.service.GateListService;

@Component("GateCheckTask")
@Lazy(false)
public class GateCheckTaskController extends BaseController {
	GateListService gate_listservice=null;
	GateBoardService gatebordservice=null;
//	@Scheduled(cron="0 0/5 * * * ?")
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " 获取刷卡 定时任务执行");
		
		GateBoardController m=GateBoardController.getInstance();
//		m.confirmIdleFlag();
		
		//保证需要的COM口被打开
		for (String port : m.getUsefulPortList2().keySet()) {
			m.setGate_id(m.getUsefulPortList2().get(port));
			m.openAPort(port);
		}
		
		//获取未上传的刷卡记录
		for(Map.Entry<String, Boolean> entry:m.getIdleFlag().entrySet()){
//			if(entry.getValue())
				this.getService().getGateboradservie().getLostCheckLog(entry.getKey());
		}
	}
}
