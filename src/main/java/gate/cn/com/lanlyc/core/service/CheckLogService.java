package gate.cn.com.lanlyc.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.dao.CheckLogDao;
import gate.cn.com.lanlyc.core.dao.GateListDao;
import gate.cn.com.lanlyc.core.po.CheckLog;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.UserCards;

@Service
@Transactional
public class CheckLogService {
	@Autowired
	private CheckLogDao checkLogDao;

	@Autowired
	private GateUserAuthService gateUserAuthService;

	@Autowired
	private UserCardsService userCardsService;

	public CheckLogDao getCheckLogDao() {
		return checkLogDao;
	}

	public void CheckLogDao(CheckLogDao checkLogDao) {
		this.checkLogDao = checkLogDao;
	}

	/**
	 * 处理实时刷卡记录
	 * 
	 * @param logInfo
	 * @throws ParseException
	 */
	/*
	 * public void delConstantCheckLog(String[] logInfo){ String[] infos = logInfo;
	 * String connect_id = infos[4]; String connect_port = infos[3]; GateList gate =
	 * this.checkLogDao.getGateListByPortAndConnectId(connect_port, connect_id); if
	 * (null != gate) { CheckLog log = new CheckLog();
	 * log.setId(DataUtils.getUUID()); if ("0".equals(infos[2]))
	 * log.setId_card(infos[0]); else if ("1".equals(infos[2]))
	 * log.setCard_no(infos[0]); log.setGate_no(gate.getGate_no());
	 * log.setCheck_date(infos[1]); log.setCross_flag(gate.getCross_flag());
	 * log.setCheck_type("4");
	 * this.getService().getChecklogservice().getCheckLogDao().save(log); } }
	 */
	/*
	 * 验证能否正常打卡，这个必须时打卡时，才能验证，因为验证一下，如果时临时卡，就减一次数据 Boolean true 代表的是考勤要统计 false
	 * 代表的是考勤不统计
	 */
	public Boolean check_times(String user_id, String type) throws ParseException {
		// 1、如果是相应的人员，不是临时卡，就直接返回true
		UserCards uc = new UserCards();
		uc = userCardsService.getUserCardsDao().getUserCarduser_id_type(user_id, type);
		if (uc.getIs_tmp().equals("0")) {// 0不是临时卡，1是临时卡
			return true;
		} else {
			if (uc.getUse_times() != null) {// 如果有次数限制
				int user_time = Integer.parseInt(uc.getUse_times());
				if (user_time <= 1) {
					// 1、1，赋值为0
					UserCards uc1 = new UserCards();
					uc1.setId(uc.getId());
					uc1.setUse_times("0");
					userCardsService.getUserCardsDao().update(uc1, false);// 将相应的次数进行
					// 2、然后停卡
					List<GateUserAuth> ga_list = gateUserAuthService.getUserAuthbytype(user_id, type, "0");
					for (GateUserAuth ga : ga_list) {
						gateUserAuthService.delSomeUserInGate(ga.getUser_id(), ga.getGate_id());
						ga.setIs_use("1");// 将相应的权限进行修改
						gateUserAuthService.getGateuserauthdao().update(ga, false);// 将人员设备权限表修改为不可用
					}
					if (user_time == 1) {// 但是这次考勤，仍然要统计
						return true;
					} else {// 这次考勤不统计
						return false;
					}
				} else {// 次数减一
						// 1、1，赋值为0
					UserCards uc1 = new UserCards();
					uc1.setId(uc.getId());
					user_time = user_time - 1;
					uc1.setUse_times(String.valueOf(user_time));
					userCardsService.getUserCardsDao().update(uc1, false);// 将相应的次数进行
					return true;// 但是这次考勤，仍然要统计
				}
			} else {
				return true;// 但是这次考勤，仍然要统计
			}
		}
	}

}
