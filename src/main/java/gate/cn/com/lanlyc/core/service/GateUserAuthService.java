package gate.cn.com.lanlyc.core.service;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang.StringUtils;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.ImageSlimUtil;
import gate.cn.com.lanlyc.core.dao.GateListDao;
import gate.cn.com.lanlyc.core.dao.GateUserAuthDao;
import gate.cn.com.lanlyc.core.dao.UserCardsDao;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.GateUserAuthView;
import gate.cn.com.lanlyc.core.po.UserCards;

@Service
public class GateUserAuthService {

	@Autowired
	private GateUserAuthDao gateuserauthdao;
	@Autowired
	private GateListDao gatelistdao;
	@Autowired
	private ServiceContainer service;
	@Autowired
	private UserCardsDao userCardsDao;
	@Autowired
	private UserCardsService userCardsService;

	public GateUserAuthDao getGateuserauthdao() {
		return gateuserauthdao;
	}

	public void setGateuserauthdao(GateUserAuthDao gateuserauthdao) {
		this.gateuserauthdao = gateuserauthdao;
	}

	@Autowired
	private GateListService gatelistservice;
	@Autowired
	private GateUserService gateuserservice;
	@Autowired
	private FaceService faceservice;
	@Autowired
	private GateBoardService gateboardservice;
	@Autowired
	private UserCardsService UserCardsService;
	@Autowired
	private WgService wgService;

	/**
	 * 给一些人员添加设备权限
	 * 
	 * @param gate_id
	 * @param user_ids
	 * @return
	 */
	@Transactional
	public boolean addsomepeopleAuth(String gate_id, String user_ids, HttpServletRequest request) {
		if (StringUtils.isEmpty(gate_id) || StringUtils.isEmpty(user_ids))
			return false;
		// 人员开始初始设置权限
		GateList gatelist = gatelistservice.getGatelistdao().get(gate_id);
		String[] userids = user_ids.split(",");
		if (gatelist.getType().equals("hanwang")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {
				// 如果人员未开卡 则调用人员自己本身的图片
				GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
				// String urlStr = gateuserinfo.getAvatar_img();

				// HttpServletRequest request=this.getRequest();
				// String face = ImageSlimUtil.getImageFileBase64code(urlStr, request);
				List<String> faces = new ArrayList<String>();
				// faces.add(face);
				String ip = gatelist.getConnect_ip();
				String port = gatelist.getConnect_port();
				String password = gatelist.getPassword();
				String user_name = gateuserinfo.getName();

				// 则调用硬件接口 写入人员信息
				String is_add = faceservice.addFace(String.valueOf(gateuserinfo.getGonghao()), user_name, ip, port,
						password, faces);
				String out_com = "";
				if (is_add.equals("0")) {
					out_com = "设备中未授权";
				} else {
					out_com = "设备中已授权";
				}
				System.out.println("汉王闸机添加人员写入成功 " + out_com);
				GateUserAuth userauth = this.gateuserauthdao.getUserAuth(user_id, gate_id);

				List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);

				if (userauth == null) {
					boolean xiafa_auth = false;
					if (ucardsList != null && ucardsList.size() != 0) {// 人员已开卡

						for (UserCards ucards : ucardsList) {
							if ("7".equals(ucards.getType())) {
								xiafa_auth = true;
							}
						}

					}
					if (xiafa_auth) {
						GateUserAuth modelList = new GateUserAuth();
						modelList.setGate_id(gate_id);
						String id = DataUtils.getUUID();
						modelList.setId(id);
						modelList.setUser_id(user_id);
						modelList.setIs_use("0");
						if (is_add.equals("0")) {
							int res = this.gateuserauthdao.save(modelList);
							int num = gateuserservice.getGateuser().updateSate(user_id, "1");
						} else {
							modelList.setFace_data(is_add);
							int res = this.gateuserauthdao.save(modelList);
						}
					}
				}
			}
		} else if (gatelist.getType().equals("lanlyc")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {

				GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
				String com = gatelist.getConnect_port();
				String connect_id = gatelist.getConnect_id();
				List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);
				String id_card = "";
				String IC_id = "";
				if (ucardsList != null && ucardsList.size() != 0) {// 人员已开卡
					for (UserCards ucards : ucardsList) {
						if ("0".equals(ucards.getType()))
							id_card = ucards.getUser_auth_card();
						else if ("4".equals(ucards.getType()))
							IC_id = ucards.getUser_auth_card();

					}
				} else {// 如果人员未开卡 则调用人员自己本身的身份证信息
					id_card = gateuserinfo.getCid();
					IC_id = "";
				}

				// 人员人脸或者IC卡已经开卡,但是没有身份证，则调用人员自己本身的身份证信息
				if (id_card.equals(""))
					id_card = gateuserinfo.getCid();

				// 如果已经给用户下发过权限,下传前删除用户之前的授权信息
				GateUserAuth userauth = this.gateuserauthdao.getUserAuth(user_id, gate_id);
				// if (userauth != null) {
				// boolean delFlag = new GateBoardService().deltePersonAuth(com,
				// gatelist.getGate_no(), user_id, id_card, IC_id);
				// }
				boolean is_success = gateboardservice.WritePersonAuth(com, connect_id, gatelist.getGate_no(), user_id,
						id_card, IC_id);
				System.out.println("闸机版是否添加人员写入成功 " + is_success);
				if (!is_success)
					return false;

				if (userauth == null) {// 如果没有给用户下发过权限，并且下传授权成功，给gate_t_user_cards和gate_t_user_cards_auth添加数据
					GateUserAuth modelList = new GateUserAuth();
					modelList.setGate_id(gate_id);
					String id = DataUtils.getUUID();
					modelList.setId(id);
					modelList.setUser_id(user_id);
					modelList.setIs_use("0");
					int res = this.gateuserauthdao.save(modelList);

					UserCards ucards = new UserCards();
					ucards.setId(DataUtils.getUUID());
					ucards.setType("0");
					ucards.setUser_auth_card(id_card);
					ucards.setUser_id(user_id);
					ucards.setUse_times("");
					if (!this.userCardsDao.isExistQueryByUSerIDAndCardAndType(ucards.getUser_id(),
							ucards.getUser_auth_card(), ucards.getType()))
						this.userCardsDao.save(ucards);

					int num = gateuserservice.getGateuser().updateSate(user_id, "1");
				}
			}

			// 则调用硬件接口 批量写入人员信息
		} else if (gatelist.getType().equals("weigeng")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {
				GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
				String ControllerIP = gatelist.getConnect_ip();
				String ControllerSN = gatelist.getSN();
				String tongdaoNo = gatelist.getConnect_id();
				List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);
				String id_card = "";
				String IC_id = "";
				if (ucardsList != null && ucardsList.size() != 0) {// 人员已开卡
					for (UserCards ucards : ucardsList) {
						if ("0".equals(ucards.getType()))
							id_card = ucards.getUser_auth_card();
						else if ("4".equals(ucards.getType()))
							IC_id = ucards.getUser_auth_card();

					}
				} else {// 如果人员未开卡 则调用人员自己本身的身份证信息
					id_card = gateuserinfo.getCid();
					IC_id = "";
				}

				// 人员人脸或者IC卡已经开卡,但是没有身份证，则调用人员自己本身的身份证信息
				if (id_card.equals(""))
					id_card = gateuserinfo.getCid();
				Boolean is_exist = getGalistBySnAndTondao(ControllerSN, tongdaoNo, "weigeng", user_id);
				if (!is_exist) {
					String[] weigengAuth = this.getUserAuthOfWeigengBySn(user_id, ControllerSN, "weigeng");
					weigengAuth[Integer.parseInt(tongdaoNo) - 1] = "1";
					// 下放权限
					int add_outcome = wgService.give_auth(ControllerIP, Long.parseLong(ControllerSN), weigengAuth,
							IC_id);
					if (add_outcome != 1) {
						return false;
					} else {
						GateUserAuth getmodel = getUserAuth(user_id, gate_id);
						if (getmodel == null) {
							GateUserAuth modelList = new GateUserAuth();
							modelList.setGate_id(gate_id);
							String id = DataUtils.getUUID();
							modelList.setId(id);
							modelList.setUser_id(user_id);
							modelList.setIs_use("0");
							int res = this.gateuserauthdao.save(modelList);
						}

					}
				}

			}
			// 则调用硬件接口 批量写入人员信息
		}
		return true;

	}

	// 根据用户id和控制板编号，查询该人员在该控制板的进出权限，先实现微耕的功能
	@Transactional
	public String[] getUserAuthOfWeigengBySn(String user_id, String SN, String type) {
		// 如果设备对应的类型是微耕
		String[] auth = new String[4];
		if (type.equals("weigeng")) {
			// 微耕有四个通道
			for (int i = 0; i <= 3; i++) {
				Boolean outcome = this.getGalistBySnAndTondao(SN, String.valueOf(i + 1), type, user_id);
				if (outcome) {
					auth[i] = "1";
				} else {
					auth[i] = "0";
				}
			}
		}
		return auth;
	}

	// 根据控制板编号和通道号获取相应的设备
	private Boolean getGalistBySnAndTondao(String sn, String tongdao, String type, String user_id) {
		if (type.equals("weigeng")) {
			GateList gateList = gatelistdao.getGalistBySnAndTondaoDao(sn, tongdao, type);
			if (gateList == null) {
				
				return false;
			} else {
				GateUserAuth gateUserAuth = gateuserauthdao.getAuthBygateidandUserid(gateList.getId(), user_id);
				if (gateUserAuth == null) {
					return false;
				} else {
					return true;
				}

			}
		}
		return false;
	}

	/**
	 * 获取人员在闸机的权限
	 * 
	 * @param user_id
	 * @param gate_id
	 * @return
	 */
	@Transactional
	public GateUserAuth getUserAuth(String user_id, String gate_id) {
		GateUserAuth gateuserauth = this.gateuserauthdao.getUserAuth(user_id, gate_id);
		return gateuserauth;
	}

	/**
	 * 判断一个人员在一个控制板和指定通道下是否有权限
	 * 
	 * @param user_id
	 * @param COM
	 * @param connect_id
	 * @return
	 */
	@Transactional
	public String GateBoradHasUserAuth(String user_id, String COM, String connect_id) {
		boolean res = this.gateuserauthdao.GateBoradHasUserAuth(user_id, COM, connect_id);
		String result = "0";
		if (res) {
			result = "1";
		}
		return result;

	}

	/**
	 * 查询某一个闸机下有权限的人员列表
	 * 
	 * @param gate_id
	 * @return
	 */
	@Transactional
	public List<GateUserAuth> getGateUserAuth(String gate_id) {
		List<GateUserAuth> gateuserauth = this.gateuserauthdao.getGateUserAuth(gate_id);
		return gateuserauth;
	}

	@Transactional
	public boolean deluserAuth(String user_ids, String gate_id) {
		GateList gatelist = gatelistservice.getGatelistdao().get(gate_id);
		String[] userids = user_ids.split(",");
		boolean is_successful = false;
		if (gatelist.getType().equals("hanwang")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {
				// 如果人员未开卡 则调用人员自己本身的图片
				GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
				String urlStr = gateuserinfo.getAvatar_img();

				// HttpServletRequest request=this.getRequest();

				List<String> faces = new ArrayList<String>();

				String ip = gatelist.getConnect_ip();
				String port = gatelist.getConnect_port();
				String password = gatelist.getPassword();
				String user_name = gateuserinfo.getName();
				String gonggao = String.valueOf(gateuserinfo.getGonghao());
				// 则调用硬件接口 写入人员信息
				boolean is_add = faceservice.deleteFace(gonggao, ip, port, password);

				GateUserAuth userauth = this.gateuserauthdao.getUserAuth(user_id, gate_id);
				if (userauth != null) {

					String id = userauth.getId();

					int res = this.gateuserauthdao.delete(id);
					if (res > 0) {
						is_successful = true;
					}
				}

			}

		} else if (gatelist.getType().equals("lanlyc")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {
				GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
				String com = gatelist.getConnect_port();
				String connect_id = gatelist.getConnect_id();
				List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);
				String id_card = "";
				String IC_id = "";
				if (ucardsList != null && ucardsList.size() != 0) {// 人员已开卡
					for (UserCards ucards : ucardsList) {
						if ("0".equals(ucards.getType()))
							id_card = ucards.getUser_auth_card();
						else if ("4".equals(ucards.getType()))
							IC_id = ucards.getUser_auth_card();

					}
				} else {// 如果人员未开卡 则调用人员自己本身的身份证信息
					if (gateuserinfo != null)
						id_card = gateuserinfo.getCid();
				}

				boolean is_success = gateboardservice.deltePersonAuth(com, gatelist.getGate_no(), "", id_card, IC_id);
				System.out.println("删除闸机某个人员授权成功 " + is_success);
				GateUserAuth userauth = this.gateuserauthdao.getUserAuth(user_id, gate_id);
				if (userauth != null) {
					String id = userauth.getId();
					int res = this.gateuserauthdao.delete(id);
					if (res > 0) {
						is_successful = true;
					}
				}
			}
			// 则调用硬件接口 批量写入人员信息
		} else if (gatelist.getType().equals("weigeng")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {
				boolean is_success = del_auth_in_weigeng(user_id, gate_id);
				System.out.println("删除闸机某个人员授权成功 " + is_success);
				GateUserAuth userauth = this.gateuserauthdao.getUserAuth(user_id, gate_id);
				if (userauth != null) {
					String id = userauth.getId();
					int res = this.gateuserauthdao.delete(id);
					if (res > 0) {
						is_successful = true;
					}
				}
			}
			// 则调用硬件接口 批量写入人员信息
		}
		return is_successful;

	}

	/*
	 * 删除用户在所有闸机的权限
	 * 
	 * 
	 */
	@Transactional
	public boolean deluserAuth(String user_ids) {
		String[] userids = user_ids.split(",");
		boolean is_successful = true;
		for (String user_id : userids) {
			List<GateUserAuthView> userauth = this.gateuserauthdao.getUserGateAuth(user_id);

			for (GateUserAuthView user_gate : userauth) {
				String gate_id = user_gate.getGate_id();
				if (null == gate_id)// 闸机已经不存在了
					break;
				if (user_gate.getType_value().equals("hanwang")) {

					// 如果人员未开卡 则调用人员自己本身的图片
					GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
					String urlStr = gateuserinfo.getAvatar_img();

					// HttpServletRequest request=this.getRequest();

					List<String> faces = new ArrayList<String>();

					String ip = user_gate.getConnect_ip();
					String port = user_gate.getConnect_port();
					String password = user_gate.getPassword();
					String user_name = user_gate.getName();
					String gonggao = user_gate.getGonghao();
					// 则调用硬件接口 写入人员信息
					boolean is_add = faceservice.deleteFace(gonggao, ip, port, password);

					String id = user_gate.getId();

					int res = this.gateuserauthdao.delete(id);
					if (res > 0) {
						is_successful = true;
					}

				} else if (user_gate.getType_value().equals("lanlyc")) {
					// for 循环每个人 然后根据每个人有不同的开卡权限

					GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
					String com = user_gate.getConnect_port();

					String id_card = user_gate.getId_card();
					String connect_id = user_gate.getConnect_id();
					String IC_id = "";

					List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);

					if (ucardsList != null && ucardsList.size() != 0) {// 人员已开卡
						for (UserCards ucards : ucardsList) {
							if ("0".equals(ucards.getType()))
								id_card = ucards.getUser_auth_card();
							else if ("4".equals(ucards.getType()))
								IC_id = ucards.getUser_auth_card();

						}
					} else {// 如果人员未开卡 则调用人员自己本身的身份证信息
						if (gateuserinfo != null)
							id_card = gateuserinfo.getCid();
					}
					GateList gateList = this.gatelistservice.getGatelistdao().get(gate_id);

					boolean is_success = gateboardservice.deltePersonAuth(com, gateList.getGate_no(), "", id_card,
							IC_id);
					System.out.println("闸机版删除人员授权成功 " + is_success);

					String id = user_gate.getId();
					int res = this.gateuserauthdao.delete(id);
					if (res > 0) {
						is_successful = true;
					}
					// 则调用硬件接口 批量写入人员信息
				} else if (user_gate.getType_value().equals("weigeng")) {
					// 删除人员在微耕的权限
					del_auth_in_weigeng(user_id, gate_id);
					String id = user_gate.getId();
					int res = this.gateuserauthdao.delete(id);
					if (res > 0) {
						is_successful = true;
					}
				}
			}
		}
		return is_successful;

	}

	/**
	 * 删除用户在某个闸机上的权限
	 * 
	 * @param user_ids
	 * @param gate_id
	 * @return
	 */
	@Transactional
	public boolean delSomeUserInGate(String user_ids, String gate_id) {
		GateList gatelist = gatelistservice.getGatelistdao().get(gate_id);
		String[] userids = user_ids.split(",");
		boolean is_successful = false;
		if (gatelist.getType().equals("hanwang")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {
				// 如果人员未开卡 则调用人员自己本身的图片
				GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
				String urlStr = gateuserinfo.getAvatar_img();

				// HttpServletRequest request=this.getRequest();

				List<String> faces = new ArrayList<String>();

				String ip = gatelist.getConnect_ip();
				String port = gatelist.getConnect_port();
				String password = gatelist.getPassword();
				String user_name = gateuserinfo.getName();
				String gonggao = String.valueOf(gateuserinfo.getGonghao());
				// 则调用硬件接口 写入人员信息
				boolean is_add = faceservice.deleteFace(gonggao, ip, port, password);

				GateUserAuth userauth = this.gateuserauthdao.getUserAuth(user_id, gate_id);
				if (userauth != null) {

					String id = userauth.getId();

					int res = this.gateuserauthdao.delete(id);
					if (res > 0) {
						is_successful = true;
					}
				}

			}

		} else if (gatelist.getType().equals("lanlyc")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {
				GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
				String com = gatelist.getConnect_port();
				String connect_id = gatelist.getConnect_id();
				List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);
				String id_card = "";
				String IC_id = "";
				if (ucardsList != null && ucardsList.size() != 0) {// 人员已开卡
					for (UserCards ucards : ucardsList) {
						if ("0".equals(ucards.getType()))
							id_card = ucards.getUser_auth_card();
						else if ("4".equals(ucards.getType()))
							IC_id = ucards.getUser_auth_card();

					}
				} else {// 如果人员未开卡 则调用人员自己本身的身份证信息
					if (gateuserinfo != null)
						id_card = gateuserinfo.getCid();
				}

				boolean is_success = gateboardservice.delUserInGate(gate_id, user_id, id_card, IC_id);
				System.out.println("删除闸机某个人员授权成功 " + is_success);
				GateUserAuth userauth = this.gateuserauthdao.getUserAuth(user_id, gate_id);
				if (userauth != null) {
					String id = userauth.getId();
					int res = this.gateuserauthdao.delete(id);
					if (res > 0) {
						is_successful = true;
					}
				}

			}

			// 则调用硬件接口 批量写入人员信息
		} else if (gatelist.getType().equals("weigeng")) {
			// for 循环每个人 然后根据每个人有不同的开卡权限
			for (String user_id : userids) {
				GateUser gateuserinfo = gateuserservice.getGateuser().get(user_id);
				String ControllerIP = gatelist.getConnect_ip();
				String ControllerSN = gatelist.getSN();
				String tongdaoNo = gatelist.getConnect_id();
				List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);
				String id_card = "";
				String IC_id = "";
				if (ucardsList != null && ucardsList.size() != 0) {// 人员已开卡
					for (UserCards ucards : ucardsList) {
						if ("0".equals(ucards.getType()))
							id_card = ucards.getUser_auth_card();
						else if ("4".equals(ucards.getType()))
							IC_id = ucards.getUser_auth_card();

					}
				} else {// 如果人员未开卡 则调用人员自己本身的身份证信息
					id_card = gateuserinfo.getCid();
					IC_id = "";
				}

				// 人员人脸或者IC卡已经开卡,但是没有身份证，则调用人员自己本身的身份证信息
				if (id_card.equals(""))
					id_card = gateuserinfo.getCid();
				Boolean is_exist = getGalistBySnAndTondao(ControllerSN, tongdaoNo, "weigeng", user_id);
				if (is_exist) {
					String[] weigengAuth = this.getUserAuthOfWeigengBySn(user_id, ControllerSN, "weigeng");
					weigengAuth[Integer.parseInt(tongdaoNo) - 1] = "0";
					// 下放权限
					int add_outcome = wgService.give_auth(ControllerIP, Long.parseLong(ControllerSN), weigengAuth,
							IC_id);
					if (add_outcome != 1) {
						return false;
					} else {
						GateUserAuth getmodel = getUserAuth(user_id, gate_id);
						if (getmodel != null) {
							int res = this.gateuserauthdao.delete(getmodel.getId());
						}
					}
				}

			}
			// 则调用硬件接口 批量写入人员信息
		}
		return is_successful;
	}

	/**
	 * 设置多个用户停卡
	 * 
	 * 
	 */
	public Object setUserAuthPause(String user_ids) {
		String test_outcome = testdevice(user_ids);
		if (!test_outcome.equals("")) {
			return test_outcome;
		}
		String[] userids = user_ids.split(",");
		boolean is_success = true;
		for (String user_id : userids) {
			List<GateUserAuthView> userauth = this.gateuserauthdao.getUserGateAuth(user_id);

			for (GateUserAuthView user_gate : userauth) {
				GateUserAuth tmp_userauth = new GateUserAuth();
				tmp_userauth.setId(user_gate.getId());
				tmp_userauth.setGate_id(user_gate.getGate_id());
				tmp_userauth.setUser_id(user_gate.getUser_id());
				tmp_userauth.setIs_use("1");
				// 删除设备中的该人员信息
				// 如果设备为汉王设备
				boolean is_del = true;
				if (user_gate.getType_value().equals("hanwang")) {
					String ip = user_gate.getConnect_ip();
					String port = user_gate.getConnect_port();
					String password = user_gate.getPassword();
					String gonghao = user_gate.getGonghao();
					is_del = faceservice.deleteFace(gonghao, ip, port, password);
				}
				if (user_gate.getType_value().equals("lanlyc")) {
					String com = user_gate.getConnect_port();
					String gate_id = user_gate.getGate_no();
					String id_card = user_gate.getId_card();
					String IC_id = "";
					is_del = gateboardservice.deltePersonAuth(com, gate_id, "", id_card, IC_id);
				}
				if (user_gate.getType_value().equals("weigeng")) {
					// 删除人员在微耕上的权限
					del_auth_in_weigeng(user_id, user_gate.getGate_id());
				}

				if (!is_del) {
					is_success = false;
					userCardsService.txException();
				}

				int num = this.gateuserauthdao.update(tmp_userauth, false);
				if (num <= 0) {
					is_success = false;
					userCardsService.txException();
				}
			}
		}
		return is_success;

	}

	// 此方法是相应的人员，所涉及的相关的人员
	public String testdevice(String user_ids) {
		// TODO Auto-generated method stub
		String[] user_id_list = user_ids.split(",");
		List<String> gate_list = new ArrayList<String>();

		for (String user_id : user_id_list) {// 获取改人员在用的所有的权限
			List<GateUserAuth> gateUserAuth = this.getUserAuthbyUser(user_id, null);
			for (GateUserAuth ga : gateUserAuth) {
				if (!gate_list.contains(ga.getGate_id())) {
					gate_list.add(ga.getGate_id());
				}
			}
		}
		String test_outcome = "";
		for (String gate_id : gate_list) { // 遍历所有的设备id，并检验设备是否存在（即通信成功）
			test_outcome += gateexist(gate_id);
		}
		return test_outcome;
	}

	// 检验设备是否可以通信
	public String gateexist(String gate_id) {
		// TODO Auto-generated method stub
		String gate_name = "";
		GateList ga = gatelistdao.get(gate_id);
		if (ga.getType().equals("hanwang")) {
			if (!faceservice.existFace(ga.getConnect_ip(), ga.getConnect_port(), ga.getPassword()).equals("ok")) {
				gate_name += "," + ga.getGate_name();
			}
		} else if (ga.getType().equals("lanlyc")) {
			try {
				if (gateboardservice.isOnline(ga.getConnect_port(), ga.getGate_no()) == false) {
					gate_name += "," + ga.getGate_name();
				}
			} catch (Exception e) {
				gate_name += "," + ga.getGate_name();
			}

		} else if (ga.getType().equals("weigeng")) {
			try {
				int status = wgService.query_controller_status(ga.getConnect_ip(), Long.valueOf(ga.getSN()));
				if (status != 0) {
					gate_name += "," + ga.getGate_name();
				}
			} catch (Exception e) {
				gate_name += "," + ga.getGate_name();
			}
		}
		return gate_name;
	}

	// 删除人员在微耕上的权限
	@Transactional
	public Boolean del_auth_in_weigeng(String user_id, String gate_id) {
		GateList gateList = gatelistdao.get(gate_id);
		String ControllerIP = gateList.getConnect_ip();
		String ControllerSN = gateList.getSN();
		String tongdaoNo = gateList.getConnect_id();
		String IC_id = "";
		List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);
		for (UserCards userCards : ucardsList) {
			if (userCards.getType().equals("4")) {
				IC_id = userCards.getUser_auth_card();
			}
		}
		if (IC_id != "") {
			String[] weigengAuth = this.getUserAuthOfWeigengBySn(user_id, ControllerSN, "weigeng");
			weigengAuth[Integer.parseInt(tongdaoNo) - 1] = "0";
			// 下放权限
			int add_outcome = wgService.give_auth(ControllerIP, Long.parseLong(ControllerSN), weigengAuth, IC_id);
		}
		return true;
	}

	// 增加人员在微耕上的权限
	@Transactional
	public void add_auth_in_weigeng(String user_id, String gate_id) {
		GateList gateList = gatelistdao.get(gate_id);
		String ControllerIP = gateList.getConnect_ip();
		String ControllerSN = gateList.getSN();
		String tongdaoNo = gateList.getConnect_id();
		String IC_id = "";
		List<UserCards> ucardsList = UserCardsService.getUserCardsDao().getUserCardsByUserid(user_id);
		for (UserCards userCards : ucardsList) {
			if (userCards.getType().equals("4")) {
				IC_id = userCards.getUser_auth_card();
			}
		}
		if (IC_id != "") {
			String[] weigengAuth = this.getUserAuthOfWeigengBySn(user_id, ControllerSN, "weigeng");
			weigengAuth[Integer.parseInt(tongdaoNo) - 1] = "1";
			// 下放权限
			int add_outcome = wgService.give_auth(ControllerIP, Long.parseLong(ControllerSN), weigengAuth, IC_id);
		}
	}

	/**
	 * 设置多个用户恢复卡
	 * 
	 * 
	 */
	public Object setUserAuthNormal(String user_ids, HttpServletRequest request) {
		String test_outcome = testdevice(user_ids);
		if (!test_outcome.equals("")) {
			return test_outcome;
		}
		String[] userids = user_ids.split(",");
		boolean is_success = true;
		for (String user_id : userids) {
			List<GateUserAuthView> userauth = this.gateuserauthdao.getUserGateAuth(user_id);

			for (GateUserAuthView user_gate : userauth) {
				GateUserAuth tmp_userauth = new GateUserAuth();
				tmp_userauth.setId(user_gate.getId());
				tmp_userauth.setGate_id(user_gate.getGate_id());
				tmp_userauth.setUser_id(user_gate.getUser_id());
				tmp_userauth.setIs_use("0");
				boolean is_add = true;
				// 如果设备为汉王设备
				if (user_gate.getType_value().equals("hanwang")) {
					String ip = user_gate.getConnect_ip();
					String port = user_gate.getConnect_port();
					String password = user_gate.getPassword();
					String user_name = user_gate.getName();

					GateUserAuth gu_exist = this.getGateuserauthdao().getUserAuth(user_gate.getUser_id(),
							user_gate.getGate_id());

					String faces = gu_exist.getFace_data();

					is_add = faceservice.revertFaceAuth(user_gate.getGonghao(), user_name, ip, port, password, faces);
				}
				if (user_gate.getType_value().equals("lanlyc")) {
					String com = user_gate.getConnect_port();
					String gate_id = user_gate.getGate_no();
					String id_card = user_gate.getId_card();
					String connect_id = user_gate.getConnect_id();
					String IC_id = "";
					is_add = gateboardservice.WritePersonAuth(com, connect_id, gate_id, "", id_card, IC_id);
				}
				if (!is_add) {
					userCardsService.txException();
				}

				// 设备中的添加人员信息
				boolean tmp_issuccess = this.addsomepeopleAuth(user_gate.getGate_id(), user_gate.getUser_id(), request);

				int num = this.gateuserauthdao.update(tmp_userauth);
				if (num <= 0) {
					is_success = false;
					userCardsService.txException();
				}
			}
		}
		return is_success;

	}

	// 获取该人员，某种类型的所有权限的闸机权限
	@Transactional
	public List<GateUserAuth> getUserAuthbytype(String user_id, String type, String is_use) {
		// TODO Auto-generated method stub
		List<GateUserAuth> gateuserauth = this.gateuserauthdao.getGateUserAuthbytype(user_id, type, is_use);
		return gateuserauth;

	}

	// 获取该人员下所有的设备权限对象
	@Transactional
	public List<GateUserAuth> getUserAuthbyUser(String user_id, String is_use) {
		// TODO Auto-generated method stub
		List<GateUserAuth> gateuserauth = this.gateuserauthdao.getUserAuthbyUser(user_id, is_use);
		return gateuserauth;

	}
}
