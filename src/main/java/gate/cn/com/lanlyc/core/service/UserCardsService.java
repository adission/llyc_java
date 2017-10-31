package gate.cn.com.lanlyc.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.dao.UserCardsDao;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.UserCards;

@Service("UserCardsService")
public class UserCardsService {
	@Autowired
	private UserCardsDao userCardsDao;
	@Autowired
	private GateUserAuthService gateuserauthservice;
	@Autowired
	private GateUserService gateUserService;
	@Autowired
	private GateListService gateListService;
	@Autowired
	private FaceService faceService;
	@Autowired
	private GateBoardService gateBoardService;

	public UserCardsDao getUserCardsDao() {
		return userCardsDao;
	}

	public void setUserCardsDao(UserCardsDao userCardsDao) {
		this.userCardsDao = userCardsDao;
	}

	/**
	 * 用户销卡
	 * 
	 */
	public Object ClearUserCards(String user_ids) {
		String test_outcome = gateuserauthservice.testdevice(user_ids);
		if (!test_outcome.equals("")) {
			return test_outcome;
		}
		boolean is_success = true;
		// 通过用户id 获取所有授权记录
		String[] userids = user_ids.split(",");

		List<UserCards> us = this.userCardsDao.getUserCardsByUserids(user_ids);
		Collection<String> ids = new ArrayList<String>();
		for (UserCards id : us) {
			ids.add(id.getId());

		}
		// 将用户的所有授权删除
		Boolean is_del = gateuserauthservice.deluserAuth(user_ids);
		if (!is_del) {
			this.txException();
		}
		this.userCardsDao.delete(ids);

		return is_success;
	}

	// 根据时间获取相应的权限
	@Transactional
	public List<UserCards> GateCards(long currentTime) {
		// TODO Auto-generated method stub
		List<UserCards> us = this.userCardsDao.getUserCardsByTime(currentTime);
		return us;
	}

	// * 新增用户授权考勤权限 所需要的数据有
	public String addAuth(String ic_card, String use_times, String end_time, String face_auth, String id_card,
			String gate_id, String user_id, String is_tmp, HttpServletRequest request) {
		// TODO Auto-generated method stub
		// 先获取相关人员对象
		// 此方法用于验证权限设备的一致性
		Boolean testOutcome = testAuthDeviceMatch(ic_card, face_auth, id_card, gate_id);
		if (!testOutcome) {
			return "3";
		}

		String[] gate_cl = gate_id.split(",");
		List<String> gate_cll = new ArrayList<String>(Arrays.asList(gate_cl));
		// 1、先获取该人的所有的权限设备对象
		List<GateUserAuth> gateUserAuth_cl = gateuserauthservice.getUserAuthbyUser(user_id, null);
		if (gateUserAuth_cl.size() > 0) {
			for (GateUserAuth gateUserAuth : gateUserAuth_cl) {
				if (!gate_cll.contains(gateUserAuth.getGate_id())) {
					gate_cll.add(gateUserAuth.getGate_id());
				}
			}
		}

		String test_outcome = "";
		if (gate_cll.size() > 0) {
			for (String gat_id : gate_cll) { // 遍历所有的设备id，并检验设备是否存在（即通信成功）
				test_outcome += gateuserauthservice.gateexist(gat_id);
			}
			if (!test_outcome.equals("")) {
				return test_outcome;
			}
		}

		GateUser gu = gateUserService.getGateuser().get(user_id);
		if (gu.getSate().equals("0") || gu.getSate().equals("3")) {
			gu.setSate("1");
			gateUserService.getGateuser().update(gu, false);
		} else if (gu.getSate().equals("2")) {// 如果在停卡的状态下进行相关权限的操作，那么先对其进行复卡操作
			Boolean is_success = (Boolean) gateuserauthservice.setUserAuthNormal(user_id, request);
			gu.setSate("1");
			gateUserService.getGateuser().update(gu, false);
		}

		// 对ic卡的权限进行处理
		UserCards gu_lis = this.getUserCardsDao().getUserCarduser_id_type(user_id, "4");

		if (ic_card.equals("0") && gu_lis != null) {// 此次分配权限，之前有ic权限，现在没有了
			this.getUserCardsDao().delete(gu_lis.getId());
		} else if ((!ic_card.equals("0")) && gu_lis == null) {// 此次分配权限，之前没有，现在有了

			UserCards userCards = this.getUserCardsDao().getUserCardByUserAuthCard(ic_card);
			if (userCards != null) {
				return "2";// ic卡号重复
			}
			UserCards uc = new UserCards();
			String id = DataUtils.getUUID();
			uc.setId(id);
			uc.setUser_auth_card(ic_card);
			uc.setType("4");
			uc.setUser_id(user_id);
			if (!use_times.equals("")) {
				uc.setUse_times(use_times);
			}
			if (!end_time.equals("")) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				String time1 = "";
				try {
					date = format.parse(end_time);
					System.out.print("Format To times:" + date.getTime());
					time1 = String.valueOf(date.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				uc.setEnd_time(time1);
			}
			uc.setIs_tmp(is_tmp);

			this.getUserCardsDao().save(uc);
		} else if ((!ic_card.equals("0")) && gu_lis != null) {// 此次分配权限，之前有，现在也有
			Boolean ic_card_change = false;
			if (ic_card.equals(gu_lis.getUser_auth_card())) {
				ic_card_change = true;
			}

			if (!ic_card_change) {// 如果ic发生修改的情况
				UserCards userCards = this.getUserCardsDao().getUserCardByUserAuthCard(ic_card);
				if (userCards != null) {// 如果修改后的ic卡号和别人重复
					return "2";// ic卡号重复
				}
			}
			// 对相应数据进行修改
			UserCards uc = new UserCards();
			uc.setId(gu_lis.getId());
			uc.setUser_auth_card(ic_card);
			uc.setType("4");
			uc.setUser_id(user_id);
			if (!use_times.equals("")) {
				uc.setUse_times(use_times);
			} else {
				uc.setUse_times(null);
			}
			if (!end_time.equals("")) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				String time1 = "";
				try {
					date = format.parse(end_time);
					System.out.print("Format To times:" + date.getTime());
					time1 = String.valueOf(date.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				uc.setEnd_time(time1);
			} else {
				uc.setEnd_time(null);
			}
			uc.setIs_tmp(is_tmp);
			this.getUserCardsDao().update(uc, false);
		}
		// 对人脸的权限处理
		UserCards gu_lis1 = this.getUserCardsDao().getUserCarduser_id_type(user_id, "7");

		if (face_auth.equals("0") && gu_lis1 != null) {// 此次分配权限，之前有人脸权限，现在没有了
			this.getUserCardsDao().delete(gu_lis1.getId());
		} else if ((!face_auth.equals("0")) && gu_lis1 == null) {// 此次分配权限，之前没有，现在有了
			// 此次分配权限，之前没有，现在有了
			UserCards uc = new UserCards();
			String id = DataUtils.getUUID();
			uc.setId(id);
			uc.setType("7");
			uc.setUser_id(user_id);
			uc.setUser_auth_card(String.valueOf(gu.getGonghao()));
			if (!use_times.equals("")) {
				uc.setUse_times(use_times);
			}
			if (!end_time.equals("")) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				String time1 = "";
				try {
					date = format.parse(end_time);
					System.out.print("Format To times:" + date.getTime());
					time1 = String.valueOf(date.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				uc.setEnd_time(time1);
			}
			uc.setIs_tmp(is_tmp);
			this.getUserCardsDao().save(uc);

		} else if ((!face_auth.equals("0")) && gu_lis1 != null) {// 此次分配权限，之前有，现在也有

			// 对相应数据进行修改
			UserCards uc = new UserCards();
			uc.setId(gu_lis1.getId());
			uc.setUser_auth_card(String.valueOf(gu.getGonghao()));
			uc.setType("7");
			uc.setUser_id(user_id);
			if (!use_times.equals("")) {
				uc.setUse_times(use_times);
			} else {
				uc.setUse_times(null);
			}
			if (!end_time.equals("")) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				String time1 = "";
				try {
					date = format.parse(end_time);
					System.out.print("Format To times:" + date.getTime());
					time1 = String.valueOf(date.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				uc.setEnd_time(time1);
			} else {
				uc.setEnd_time(null);
			}
			uc.setIs_tmp(is_tmp);

			this.getUserCardsDao().update(uc, false);
		}
		// 对身份证的权限处理
		UserCards gu_lis11 = this.getUserCardsDao().getUserCarduser_id_type(user_id, "0");

		if (id_card.equals("0") && gu_lis11 != null) {// 此次分配权限，之前有身份证权限，现在没有了
			this.getUserCardsDao().delete(gu_lis11.getId());
		} else if ((!id_card.equals("0")) && gu_lis11 == null) {// 此次分配权限，之前没有，现在有了
			// 此次分配权限，之前没有，现在有了

			// 此次分配权限，之前没有，现在有了
			UserCards uc = new UserCards();
			String id = DataUtils.getUUID();
			uc.setId(id);
			uc.setType("0");
			uc.setUser_id(user_id);
			uc.setUser_auth_card(gu.getCid());
			if (!use_times.equals("")) {
				uc.setUse_times(use_times);
			}
			if (!end_time.equals("")) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				String time1 = "";
				try {
					date = format.parse(end_time);
					System.out.print("Format To times:" + date.getTime());
					time1 = String.valueOf(date.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				uc.setEnd_time(time1);
			}
			uc.setIs_tmp(is_tmp);
			this.getUserCardsDao().save(uc);

		} else if ((!id_card.equals("0")) && gu_lis11 != null) {// 此次分配权限，之前有，现在也有
			// 对相应数据进行修改
			UserCards uc = new UserCards();
			uc.setId(gu_lis11.getId());
			uc.setUser_auth_card(gu.getCid());
			uc.setType("0");
			uc.setUser_id(user_id);
			if (!use_times.equals("")) {
				uc.setUse_times(use_times);
			} else {
				uc.setUse_times(null);
			}
			if (!end_time.equals("")) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				String time1 = "";
				try {
					date = format.parse(end_time);
					System.out.print("Format To times:" + date.getTime());
					time1 = String.valueOf(date.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				uc.setEnd_time(time1);
			} else {
				uc.setEnd_time(null);
			}
			uc.setIs_tmp(is_tmp);

			this.getUserCardsDao().update(uc, false);
		}

		//
		// 结下来是添加用户和设备对应表中的数据，并在设备中赋予相应的权限
		//
		String[] ids1 = gate_id.split(",");// 需要赋予的权限闸机列表
		List<String> ids1_list_Auth = new ArrayList<String>(Arrays.asList(ids1));//
		List<String> ids1_list_new = new ArrayList<String>();//
		// 1、先获取该人的所有的权限设备对象
		List<GateUserAuth> gateUserAuth = gateuserauthservice.getUserAuthbyUser(user_id, null);
		// ids1_list_new 获取一个list是原来的权限列表
		for (GateUserAuth gua : gateUserAuth) {
			if (!ids1_list_new.contains(gua.getGate_id())) {
				ids1_list_new.add(gua.getGate_id());
			}
		}
		// 这里逻辑有些问题
		// 遍历设备权限对象
		// 原来权限中多出来的数据进行相应的删除
		for (GateUserAuth oldAuth : gateUserAuth) {
			if (!ids1_list_Auth.contains(oldAuth.getGate_id())) {// 如果不包含相应的人员设备权限表
				// 1、首先设备中删除相应的人员权限
				// 删除 设备的权限
				boolean res = gateuserauthservice.deluserAuth(user_id, oldAuth.getGate_id());
				// 如果相关设备中人员删除成功
				if (!res) {// 下传授权失败，删除数据库gate_t_user_cards中人员对应的记录
					throwException();
				}
			}
		}

		// 现在权限中多出来的数据进行相应的添加
		for (String id_split : ids1) {
			// 然后就是将相应的设备队人员进行相应的添加
			// 先根据设备id找到相应的设备信息，并将人员的信息写写入到设备中
			if (!ids1_list_new.contains(id_split)) {
				// HttpServletRequest request = this.getRequest();
				boolean res = gateuserauthservice.addsomepeopleAuth(id_split, user_id, request);

				if (!res) {// 下传授权失败，删除数据库gate_t_user_cards中人员对应的记录
					// this.getService().getUserCardsService().getUserCardsDao().delUserCardByUserID(user_id);
					throwException();
				}
			}
		}
		return "1";
	}

	// 此方法用于验证权限设备的一致性
	// true，代表权限和设备是匹配的

	private Boolean testAuthDeviceMatch(String ic_card, String face_auth, String id_card, String gate_id) {
		// TODO Auto-generated method stub
		Boolean existIcAuth = false;
		Boolean existIdAuth = false;
		Boolean existFaceAuth = false;
		Boolean lanlycDevice = false;
		Boolean weigengDevice = false;
		Boolean hanwangDevice = false;
		if (!ic_card.equals("0")) {
			existIcAuth = true;
		}
		if (!id_card.equals("0")) {
			existIdAuth = true;
		}
		if (!face_auth.equals("0")) {
			existFaceAuth = true;
		}
		if (gate_id != null && (!gate_id.equals(""))) {
			String[] gateList = gate_id.split(",");
			for (String gateIdCulmn : gateList) {
				GateList gateListCulmn = gateListService.getGatelistdao().get(gateIdCulmn);
				if (gateListCulmn.getType().equals("lanlyc")) {
					lanlycDevice = true;
				}
				if (gateListCulmn.getType().equals("weigeng")) {
					weigengDevice = true;
				}
				if (gateListCulmn.getType().equals("hanwang")) {
					hanwangDevice = true;
				}
			}
		} else {
			return false;
		}

		if (existFaceAuth) {// 如果有人脸，则必有汉王
			if (!hanwangDevice) {
				return false;
			}
		} else {// 没有人脸，就没有汉王
			if (hanwangDevice) {
				return false;
			}
		}

		if (existIcAuth) {// 如果有ic权限，则必有微耕和lanlyc中的一种
			if (existIdAuth) {// 同时有id权限
				if (!lanlycDevice) {
					return false;
				}
			} else {// 有ic，没有id时（lanlyc和微耕至少有一个）
				if ((!lanlycDevice) && (!weigengDevice)) {
					return false;
				}
			}
		} else {// 如果没有ic权限，就不能有微耕
			if (existIdAuth) {// 有id权限
				if (!lanlycDevice) {
					return false;
				}
			} else {// 没有ic，没有id时（lanlyc和微耕都不可以有）
				if ((lanlycDevice) || (weigengDevice)) {
					return false;
				}
			}
		}
		return true;
	}

	// 写一个小方法用于抛出异常,目的用于，将事务回滚
	private void throwException() {
		// TODO Auto-generated method stub
		String[] str = null;
		for (String s : str) {
			System.out.println("-----------");
		}
	}

	// 当权限分配失败后的相应的事物处理 （主要是涉及设备方面和数据库的统一）
	@Transactional
	public String txAction(String ic_card, String use_times, String end_time, String face_auth, String id_card,
			String gate_id, String user_id, String is_tmp, HttpServletRequest request) {
		// TODO Auto-generated method stub
		// 1、获取相应的现在数据表中的应该有相应的权限的权限的设备id
		// 获取相应的人员设备权限列表（我们姑且认为，原来的权限是对的）
		String[] gate_ids = gate_id.split(",");
		// 这个列表是相应的新增的设备权限
		List<String> gate_new_list = Arrays.asList(gate_ids);
		List<String> gate_old_list = new ArrayList<String>();
		List<String> type_list = new ArrayList<String>();
		List<GateUserAuth> gulist = gateuserauthservice.getUserAuthbyUser(user_id, "0");
		List<UserCards> uclist = this.getUserCardsDao().getUserCarduser_id(user_id);

		if (gulist != null) {
			// 将原来的设备权限放入一个列表中
			for (GateUserAuth gu : gulist) {
				gate_old_list.add(gu.getGate_id());
			}
		}
		if (uclist != null) {
			// 将原来的设备权限放入一个列表中
			for (UserCards uc : uclist) {
				type_list.add(uc.getType());
			}
		}

		// 2、对旧的权限进行处理（如果旧的权限gate_id，新的权限中没有对应的gate_id，验证旧的闸机中是否还有相应的权限，如果没有，进行相应添加，如果有，不处理）
		GateUser gu = gateUserService.getGateuser().get(user_id);// 相应的人员信息
		if (gate_old_list.size() > 0) {
			for (String gate_id_old : gate_old_list) {
				if (!gate_new_list.contains(gate_id_old)) {// 如果旧的权限gate_id，新的权限中没有对应的gate_id
					GateUserAuth gu_exist = gateuserauthservice.getUserAuth(user_id, gate_id_old);
					// 写如相应的人员权限
					// 1、先获取相应的闸机类型
					GateList gl = gateListService.getGatelistdao().get(gate_id_old);// 相应设备信息

					if (gl.getType().equals("lanlyc")) {// 如果是相应的闸机,对相应的权限进行写入
						if (type_list.contains("0") && type_list.contains("4")) {
							UserCards uc = this.getUserCardsDao().getUserCarduser_id_type(user_id, "4");
							gateBoardService.WritePersonAuth(gl.getConnect_port(), gl.getConnect_id(), gl.getId(),
									user_id, gu.getCid(), uc.getUser_auth_card());

						} else if (type_list.contains("0") && (!type_list.contains("4"))) {

							gateBoardService.WritePersonAuth(gl.getConnect_port(), gl.getConnect_id(), gl.getId(),
									user_id, gu.getCid(), null);

						} else if (type_list.contains("4") && (!type_list.contains("0"))) {
							UserCards uc = this.getUserCardsDao().getUserCarduser_id_type(user_id, "4");
							gateBoardService.WritePersonAuth(gl.getConnect_port(), gl.getConnect_id(), gl.getId(),
									user_id, null, uc.getUser_auth_card());

						}
					} else if (gl.getType().equals("hanwang")) {// 如果是相应的汉王设备
						if (type_list.contains("7")) {
							// 验证一个id在该设备中是否有相应的权限_这个id对应是工号
							// public boolean TestAuth(String ip, String port, String password,String id)
							Boolean is_exist = faceService.TestAuth(gl.getConnect_ip(), gl.getConnect_port(),
									gl.getPassword(), String.valueOf(gu.getGonghao()));

							if (!is_exist) {// 如果没有相应的人脸数据

								String faces = gu_exist.getFace_data();
								if (faces != "" && faces != null) {
									Boolean is_add = faceService.revertFaceAuth(String.valueOf(gu.getGonghao()),
											gu.getName(), gl.getConnect_ip(), gl.getConnect_port(), gl.getPassword(),
											faces);
								}

							}
						}
					}
				}
			}
		}

		if (gate_new_list.size() > 0) {
			// 3、对新的权限进行处理（如果新的权限gate_id,旧的权限中没有对应的gate_id,验证新的闸机中是否有相应的权限，如果有，进行相应删除，如果没有，不处理）
			for (String gate_id_new : gate_new_list) {
				if (!gate_old_list.contains(gate_id_new)) {// 如果新的权限gate_id,旧的的权限没有对应的gate_id

					// GateUserAuth gu_exist = gateuserauthservice.getUserAuth(user_id,
					// gate_id_new);

					// 删除相应的人员权限
					// 1、先获取相应的闸机类型
					GateList gl = gateListService.getGatelistdao().get(gate_id_new);// 相应设备信息

					if (gl.getType().equals("lanlyc")) {// 如果是相应的闸机,对相应的权限进行删除

						// if (type_list.contains("0") && type_list.contains("4")) {
						// UserCards uc = this.getUserCardsDao().getUserCarduser_id_type(user_id, "4");
						// public boolean deltePersonAuth(String com,String gate_id,String
						// user_id,String id_card,String IC_id)
						if (!ic_card.equals("0")) {
							gateBoardService.deltePersonAuth(gl.getConnect_port(), gate_id_new, user_id, gu.getCid(),
									ic_card);
						} else {
							gateBoardService.deltePersonAuth(gl.getConnect_port(), gate_id_new, user_id, gu.getCid(),
									null);
						}
					} else if (gl.getType().equals("hanwang")) {// 如果是相应的汉王设备
						if (type_list.contains("7")) {
							// 验证一个id在该设备中是否有相应的权限_这个id对应是工号
							// public boolean TestAuth(String ip, String port, String password,String id)
							Boolean is_exist = faceService.TestAuth(gl.getConnect_ip(), gl.getConnect_port(),
									gl.getPassword(), String.valueOf(gu.getGonghao()));

							if (is_exist) {// 如果有相应的人脸数据
								Boolean delete = faceService.deleteFace(String.valueOf(gu.getGonghao()),
										gl.getConnect_ip(), gl.getConnect_port(), gl.getPassword());
							}
						}
					}

				}
			}
		}

		return null;
	}

	// 有关（停卡、复卡、销卡）设备和权限一致的事务处理
	// 设置用户处理状态 1恢复卡 2停卡 3 销卡
	@Transactional
	public void txActionCard(String user_ids, String status, HttpServletRequest request) {
		String[] user_id_list = user_ids.split(",");
		for (String user_id : user_id_list) {
			// TODO Auto-generated method stub
			// 复卡失败——我们要调用停卡操作
			GateUser gu = gateUserService.getGateuser().get(user_id);// 相应的人员信息
			if (status.equals("1")) {
				// 人员权限列表
				List<UserCards> uclist = this.getUserCardsDao().getUserCarduser_id(user_id);
				// 人员设备列表
				List<GateUserAuth> gulist = gateuserauthservice.getUserAuthbyUser(user_id, null);
				String ic_card = "";
				String id_card = "";
				String face_date = "";
				for (UserCards uc : uclist) {
					if (uc.getType().equals("4")) {
						ic_card = uc.getUser_auth_card();
					}
					if (uc.getType().equals("0")) {
						id_card = uc.getUser_auth_card();
					}
					if (uc.getType().equals("7")) {
						face_date = uc.getUser_auth_card();
					}
				}

				for (GateUserAuth gua : gulist) {
					// 1、先获取相应的闸机类型
					GateList gl = gateListService.getGatelistdao().get(gua.getGate_id());

					if (gl.getType().equals("lanlyc")) {// 如果是相应的闸机,对相应的权限进行删除
						if ((!ic_card.equals("")) && (!id_card.equals(""))) {
							gateBoardService.deltePersonAuth(gl.getConnect_port(), gua.getGate_id(), user_id, id_card,
									ic_card);
						} else if ((ic_card.equals("")) && (!id_card.equals(""))) {
							gateBoardService.deltePersonAuth(gl.getConnect_port(), gua.getGate_id(), user_id, id_card,
									null);
						} else if ((!ic_card.equals("")) && (id_card.equals(""))) {
							gateBoardService.deltePersonAuth(gl.getConnect_port(), gua.getGate_id(), user_id, null,
									ic_card);
						}
					} else if (gl.getType().equals("hanwang")) {// 如果是相应的汉王设备
						if (!face_date.equals("")) {
							// 验证一个id在该设备中是否有相应的权限_这个id对应是工号
							Boolean is_exist = faceService.TestAuth(gl.getConnect_ip(), gl.getConnect_port(),
									gl.getPassword(), String.valueOf(gu.getGonghao()));

							if (is_exist) {// 如果有相应的人脸数据
								Boolean delete = faceService.deleteFace(String.valueOf(gu.getGonghao()),
										gl.getConnect_ip(), gl.getConnect_port(), gl.getPassword());
							}
						}
					}
				}

			}
			// 停卡失败—和销卡失败是同样的结果—我们要调用复卡操作
			if (status.equals("2") || status.equals("3")) {
				// 就是将所有的可以用的设备，进行相应权限的添加

				// 人员权限列表
				List<UserCards> uclist = this.getUserCardsDao().getUserCarduser_id(user_id);
				// 人员设备列表
				List<GateUserAuth> gulist = gateuserauthservice.getUserAuthbyUser(user_id, "0");
				String ic_card = "";
				String id_card = "";
				String face_date = "";
				for (UserCards uc : uclist) {
					if (uc.getType().equals("4")) {
						ic_card = uc.getUser_auth_card();
					}
					if (uc.getType().equals("0")) {
						id_card = uc.getUser_auth_card();
					}
					if (uc.getType().equals("7")) {
						face_date = uc.getUser_auth_card();
					}
				}

				for (GateUserAuth gua : gulist) {
					// 1、先获取相应的闸机类型
					GateList gl = gateListService.getGatelistdao().get(gua.getGate_id());

					if (gl.getType().equals("lanlyc")) {// 如果是相应的闸机,对相应的权限进行添加
						if ((!ic_card.equals("")) && (!id_card.equals(""))) {
							gateBoardService.WritePersonAuth(gl.getConnect_port(), gl.getConnect_id(), gl.getId(),
									user_id, id_card, ic_card);
						} else if ((ic_card.equals("")) && (!id_card.equals(""))) {
							gateBoardService.WritePersonAuth(gl.getConnect_port(), gl.getConnect_id(), gl.getId(),
									user_id, id_card, null);
						} else if ((!ic_card.equals("")) && (id_card.equals(""))) {
							gateBoardService.WritePersonAuth(gl.getConnect_port(), gl.getConnect_id(), gl.getId(),
									user_id, null, ic_card);
						}

					} else if (gl.getType().equals("hanwang")) {// 如果是相应的汉王设备
						if (!face_date.equals("")) {
							// 验证一个id在该设备中是否有相应的权限_这个id对应是工号
							Boolean is_exist = faceService.TestAuth(gl.getConnect_ip(), gl.getConnect_port(),
									gl.getPassword(), String.valueOf(gu.getGonghao()));

							if (!is_exist) {// 如果没有相应的人脸数据
								Boolean is_add = faceService.revertFaceAuth(String.valueOf(gu.getGonghao()),
										gu.getName(), gl.getConnect_ip(), gl.getConnect_port(), gl.getPassword(),
										face_date);
							}
						}
					}
				}

			}
		}

	}

	// 用于事务的回滚
	@Transactional
	public void txException() {
		String[] str = null;
		for (String s : str) {
			System.out.println("-----------");
		}
	}

}
