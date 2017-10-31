package gate.cn.com.lanlyc.core.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.dao.GateListDao;
import gate.cn.com.lanlyc.core.dao.UserCardsDao;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateListView;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.UserCards;

@Service
@Transactional
public class GateListService {
	@Autowired
	private GateListDao gatelistdao;

	@Autowired
	private UserCardsService userCardsService;

	@Autowired
	private GateUserAuthService gateUserAuthService;

	@Autowired
	private WgService wgService;

	public GateListDao getGatelistdao() {
		return gatelistdao;
	}

	public void setGatelistdao(GateListDao gatelistdao) {
		this.gatelistdao = gatelistdao;
	}

	/**
	 * 获取所有蓝领英才的考勤设置
	 * 
	 */
	public List<GateList> getAllllycGate() {
		List<GateList> GateList = this.gatelistdao.getAllllycGate();
		return GateList;
	}

	/**
	 * 获取汉王考勤机
	 * 
	 */
	public List<GateListView> getAllhangwangGate(String sn) {
		List<GateListView> GateList = this.gatelistdao.getAllllycGate(sn);
		return GateList;

	}

	/**
	 * 根据当前时间，进行相应的设备权限处理处理
	 * 
	 */
	public Boolean GateTimeHandle() {// 返回处理结果
										// 1、根据当前时间获取，所有没有权限了的人员权限对象

		long currentTime = new Date().getTime();

		List<UserCards> uclist = userCardsService.GateCards(currentTime);

		if (uclist == null) {
			return true;
		} else {
			for (UserCards uc : uclist) {
				// 根据user_id和类型，获取相应的人员设备对象
				List<GateUserAuth> user_auth_list = gateUserAuthService.getUserAuthbytype(uc.getUser_id(), uc.getType(),
						"0");
				for (GateUserAuth ga : user_auth_list) {
					gateUserAuthService.delSomeUserInGate(ga.getUser_id(), ga.getGate_id());
					ga.setIs_use("1");// 将相应的权限进行修改
					gateUserAuthService.getGateuserauthdao().update(ga, false);// 将人员设备权限表修改为不可用
				}

			}
		}
		return true;
	}

	// 添加微耕的设备
	public Boolean addweigeng(JSONObject jsonobject, String paramJson) {
		// 1、先验证相应的设备
		String controllerIP = jsonobject.getString("connect_ip");
		long controllerSN = Long.parseLong(jsonobject.getString("sn"));
		int status = wgService.query_controller_status(controllerIP, controllerSN);
		if (status == 0) {
			GateList gatelist = JSON.parseObject(paramJson, new TypeReference<GateList>() {
			});
			String id = DataUtils.getUUID();
			gatelist.setId(id);
			int out_come = this.gatelistdao.save(gatelist);
			if (out_come == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	// 将相应的微耕设备中的权限进行删除
	public Boolean delAuthFromWeigeng(GateList gatelist) {
		// 先获取所有的具有相应的ic权限的人员权限对象
		List<UserCards> outCome = userCardsService.getUserCardsDao().getByType("4");
		if (outCome != null) {// 如果有相应的人员的信息
			for (UserCards sUC : outCome) {// 遍历相应的人员权限 的数据
				String[] weigengAuth = gateUserAuthService.getUserAuthOfWeigengBySn(sUC.getUser_id(), gatelist.getSN(),
						"weigeng");

				if (weigengAuth[Integer.parseInt(gatelist.getConnect_id()) - 1].equals("1")) {
					weigengAuth[Integer.parseInt(gatelist.getConnect_id()) - 1] = "0";
					// 下放权限
					int add_outcome = wgService.give_auth(gatelist.getConnect_ip(), Long.parseLong(gatelist.getSN()),
							weigengAuth, sUC.getUser_auth_card());
					if (add_outcome != 1) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
