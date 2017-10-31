package gate.cn.com.lanlyc.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.dao.ConnectLogDao;
import gate.cn.com.lanlyc.core.po.ConnectLog;
import gate.cn.com.lanlyc.core.po.GateGroup;

@Service
@Transactional
public class ConnectLogService {
	@Autowired
	private ConnectLogDao connectLogDao;

	public ConnectLogDao getConnectLogDao() {
		return connectLogDao;
	}

	public void setConnectLogDao(ConnectLogDao connectLogDao) {
		this.connectLogDao = connectLogDao;
	}
	
	/**
	 * 添加一条考勤设备通讯日志
	 * 
	 * @param connect_type 
	 * @param connect_fail_reson
	 * @param gate_id
	 * @return 成功或失败
	 */
	public boolean add(String connect_type,String connect_fail_reson,String gate_id){
		ConnectLog log=new ConnectLog();
		String id=DataUtils.getUUID();
		log.setId(id);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());//获取当前系统时间
		log.setConnect_time(date);
		log.setConnect_type(connect_type);
		log.setConnect_fail_reson(connect_fail_reson);
		log.setGate_id(gate_id);
		
		return connectLogDao.addOneConnectLog(log);
	}

}
