package gate.cn.com.lanlyc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.DateUtils;
import cn.com.lanlyc.base.util.HttpUtils;
import cn.com.lanlyc.base.util.ImageSlimUtil;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserAuth;
import gate.cn.com.lanlyc.core.po.Setting;
import gate.cn.com.lanlyc.core.po.UserCards;
import gate.cn.com.lanlyc.core.po.WorkersTypes;
import gate.cn.com.lanlyc.core.service.SettingService;

@Controller
@RequestMapping("/UserCards")
public class UserCardsController extends BaseController {

	private static String TOKEN = "";
	SettingService settingservice = null;

	Setting system_setting = null;
	private int request_time = 0;

	@RequestMapping("/add")
	@ResponseBody
	/**
	 * 新增用户授权考勤权限 所需要的数据有（ic卡、ic卡使用次数图片、ic卡到期时间、人脸数据、人脸使用次数、人脸使用次数、人脸到期时间
	 * 、id卡、id卡使用次数、id卡到期时间、用户id、设备id列表）
	 * 
	 * 访问url UserCards/add?paramJson={ "ic_card":"11111" "use_times":""
	 * "end_time":"" "face_auth":"1" "id_card":"111" “user_id”:""
	 * "gate_id":"111,555,555,333" ”is_tmp“：“” }
	 * 
	 * @param user_id
	 *            用户id
	 * @param gate_id
	 *            设备id列表
	 * @return {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response add(String ic_card, String use_times, String end_time, String face_auth, String id_card,
			String gate_id, String user_id, String is_tmp)

	{

		try {
			HttpServletRequest request = this.getRequest();
			// * 新增用户授权考勤权限 所需要的数据有（ic卡、ic卡使用次数图片、ic卡到期时间、人脸数据、人脸使用次数、人脸使用次数、人脸到期时间
			String outcome = this.getService().getUserCardsService().addAuth(ic_card, use_times, end_time, face_auth,
					id_card, gate_id, user_id, is_tmp, request);
			if (outcome.equals("2")) {
				Response response = Response.ERROR(-1, "卡号已存在");
				return response;
			} else if (outcome.equals("1")) {
				try {
					if (StringUtils.isNotEmpty(ic_card)) {
						// 给云端上报开卡

						this.OpenCard(ic_card, id_card);
					}
				} catch (Exception e) {

				}

				Response response = Response.OK();
				return response;
			} else if (outcome.equals("3")) {
				Response response = Response.ERROR(-1, "权限设备匹配有误，请重新选择");
				return response;
			} else {
				Response response = Response.ERROR(-1, outcome.toString().substring(1) + "无法连接，请检查相应的设备");

				return response;
			}
		} catch (Exception e) {
			// 异常处理
			HttpServletRequest request = this.getRequest();
			String outcome = this.getService().getUserCardsService().txAction(ic_card, use_times, end_time, face_auth,
					id_card, gate_id, user_id, is_tmp, request);
			Response response = Response.PARAM_ERROR();
			return response;
		}

	}

	/**
	 * 删除用户授权考勤权限访问url UserCards/del?id="1111111,111111,111111,111111"
	 * 
	 * @param id
	 *            "1111111,111111,1111111,111111" 代表用户授权考勤权限对应的id列表
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	@RequestMapping("/del")
	@ResponseBody
	public Response del(String id) {
		// 删除可以批量删除
		try {
			String ids = id;
			String[] ids1 = ids.split(",");
			for (String id_split : ids1) {

				// 根据人员在权限表上的类型，删除人员在相应的表中的数据

				List<GateUserAuth> listga = this.getService().getUserCardsService().getUserCardsDao()
						.query_del(id_split);

				// 可以获取user_id、和设备类型
				for (GateUserAuth ga : listga) {
					String user_id = ga.getUser_id();
					String gate_id = ga.getGate_id();
					// 删除 设备的权限
					boolean res = this.getService().getGateuserauthservie().deluserAuth(user_id, gate_id);
					// 如果相关设备中人员删除成功
					if (res) {
						String id1 = ga.getId();

						this.getService().getGateuserauthservie().getGateuserauthdao().delete(id1);
					}
				}

				this.getService().getUserCardsService().getUserCardsDao().delete(id_split);
			}
			Response response = Response.OK();
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/update")
	@ResponseBody
	/**
	 * 修改用户授权考勤权限访问url （修改相应的权限、能修改的就是在该权限上的设备、到期时间、是否将卡变为临时卡）
	 * 1、将该设备上的权限删除。或者添加相应的权限 2、修改的话只针对对应的人员 3、
	 * UserCards/update?paramJson={"id":"111111","is_use":"0"}
	 * 
	 * @param paramJson
	 *            {"id":"111111","name":"工种名称"} 代表人员设备表对应的id和需要修改的使用状态（0代表可用、1代表不可用）
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response update(String paramJson) {
		// 修改相应的权限
		try {
			JSONObject json = JSONObject.parseObject(paramJson);

			// 修改的话，就只针对一个单方面的权限进行修改，
			String id = json.getString("ic_card");
			// 修改用户的ic卡号\修改相应的人脸识别机的照片\id身份证号（不过一般不会对身份证行修改）
			String user_auth_card = json.containsKey("ic_card") ? json.getString("ic_card") : "";
			// 修改用户的使用次数（如果原来不是临时卡，将卡修改为临时卡）
			String use_times = json.containsKey("ic_use_times") ? json.getString("ic_use_times") : "";
			// 修改使用期限
			String end_time = json.containsKey("ic_end_time") ? json.getString("ic_end_time") : "";
			// 如果需要临时卡变为非临时卡，就需要传这个参数
			String is_tmp = json.containsKey("ic_temp") ? json.getString("ic_temp") : "";
			// 修改的设备id列表（就是现在可用的设备id列表）
			String gate_id = json.containsKey("gate_id") ? json.getString("gate_id") : "";
			// 获取原来对象的相应的信息
			UserCards uc = this.getService().getUserCardsService().getUserCardsDao().get(id);
			// 获取考勤类型
			String type = uc.getType();
			// 获取用户id
			String user_id = uc.getUser_id();

			// 新建一个相应的对象
			UserCards uc1 = new UserCards();
			uc1.setId(id);
			if (type.equals("hanwang")) {
				if (!user_auth_card.equals("")) {

					String[] face_datas = user_auth_card.split(",");
					String face_data_all = "";
					for (String face : face_datas) {
						// 将图片的相应路径转化为64为code码
						String face_data_part = ImageSlimUtil.getImageStr(face);
						face_data_all += face_data_part + ",";
					}
					String face_data_all_left = face_data_all.substring(0, face_data_all.length() - 1);
					uc1.setUser_auth_card(face_data_all_left);
				}
				if (!use_times.equals("")) {
					String use_time = use_times;
					uc1.setUse_times(use_time);
					uc1.setIs_tmp("1");
				}
				if (!end_time.equals("")) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = "1970-01-06 11:45:55";
					Date date = format.parse(end_time);
					System.out.print("Format To times:" + date.getTime());
					String time1 = String.valueOf(date.getTime());
					uc1.setEnd_time(time1);
				}
				// 一般只有在将临时卡变为非临时卡时，才会传此参数
				if (!is_tmp.equals("")) {
					uc.setIs_tmp(is_tmp);
				}
				this.getService().getUserCardsService().getUserCardsDao().update(uc);
			}
			// 根据人员在权限表上的类型，删除人员在相应的表中的数据

			List<GateUserAuth> listga = this.getService().getUserCardsService().getUserCardsDao().query_del(id);

			// 可以获取user_id、和设备类型
			for (GateUserAuth ga : listga) {
				String user_id1 = ga.getUser_id();
				String gate_id1 = ga.getGate_id();
				// 删除 设备的权限
				boolean res = this.getService().getGateuserauthservie().deluserAuth(user_id1, gate_id1);
				// 如果相关设备中人员删除成功
				if (res) {
					String id1 = ga.getId();

					this.getService().getGateuserauthservie().getGateuserauthdao().delete(id1);
				}
			}
			// 相关的设备的添加
			String[] ids1 = gate_id.split(",");
			for (String id_split : ids1) {
				GateUserAuth ga = new GateUserAuth();
				String id1 = DataUtils.getUUID();
				ga.setId(id1);
				ga.setUser_id(user_id);
				ga.setGate_id(id_split);
				ga.setIs_use("0");// "0表示可以使用、1表示不能进出"
				this.getService().getGateuserauthservie().getGateuserauthdao().save(ga);
				// 然后就是将相应的设备队人员进行相应的添加
				// 先根据设备id找到相应的设备信息，并将人员的信息写写入到设备中
				HttpServletRequest request = this.getRequest();
				boolean res = this.getService().getGateuserauthservie().addsomepeopleAuth(id_split, user_id, request);
			}

			Response response = Response.OK();
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/query")
	@ResponseBody
	/**
	 * 查询用户授权考勤权限的相关数据 访问url
	 * 
	 * UserCards/query?paramJson={"user_name":"杨威","gate_name":"闸机","gate_type":"","is_use":""}
	 * 
	 * @param paramJson
	 *            {"user_name":"杨威","gate_name":"闸机","gate_type":"","is_use":""}
	 *            代表用户姓名（进行模糊查询）、闸机名称（进行模糊查询）、闸机类型（条件查询）、该人在闸机上是否有进出权限(条件查询)
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response query(String paramJson, int length, int start, int draw) {
		try {
			// 1、根据user_name 找到所有符合条件的user_id

			// 2、根据gate_type 考勤类型的 条件查询

			// 3、根据是否是临时卡，进行数据的筛选

			// 4、根据卡的时限进行筛选 （给两个时间，进行相应的筛选）

			JSONObject json = JSONObject.parseObject(paramJson);
			int current_page = start / length + 1;
			int page_num = length;
			UserCards uc = new UserCards();

			String user_name = json.containsKey("user_name") ? json.getString("user_name") : "";
			String gate_type = json.containsKey("gate_type") ? json.getString("gate_type") : "";
			String is_tmp = json.containsKey("is_tmp") ? json.getString("is_tmp") : "";
			String start_time = json.containsKey("start_time") ? json.getString("start_time") : "";
			String end_time = json.containsKey("end_time") ? json.getString("end_time") : "";

			// select * from gate_t_user_cards where user_id in (select id from gate_t_user
			// where name like %user_name%) and type=gate_type and is_tmp=is_tmp and
			// end_time >= start_time and end_time<=end_time order_by user_id,id;

			int currentPage = start / length + 1;
			Page<UserCards> page;
			// 获取所有符合条件的数据
			page = this.getService().getUserCardsService().getUserCardsDao().queryList(current_page, page_num,
					user_name, gate_type, is_tmp, start_time, end_time);

			List list1 = page.getResultRows();
			List list2 = new ArrayList<Map>();

			for (Object obj : list1) {
				BeanMap map1 = new org.apache.commons.beanutils.BeanMap(obj);
				HashMap<String, Object> map2 = new HashMap<String, Object>();
				Set<String> keys = map1.keySet();
				String user_id = "";
				String type = "";
				for (String key : keys) {
					if (key.equals("user_id")) {
						GateUser dt = this.getService().getGateuserservice().getGateuser().get((String) map1.get(key));
						map2.put(key, map1.get(key));
						map2.put("user_name", dt.getName());
						user_id = (String) map1.get(key);
					} else if (key.equals("type")) {
						// GateList
						// gatelist=this.getService().getGatelistservice().getGatelistdao().getGateListInfo((String)map1.get(key));
						map2.put(key, map1.get(key));
						if (map1.get(key).equals("hanwang")) {
							map2.put("type_name", "人脸识别");
						} else {
							map2.put("type_name", "其他考勤类型");
						}
						type = (String) map1.get(key);
						List<GateList> gatelist = this.getService().getUserCardsService().getUserCardsDao()
								.getGateListInfo(user_id, type);
						map2.put("gatelist", gatelist);
					} else if (key.equals("is_use")) {
						if (((String) map1.get(key)).equals("0")) {
							map2.put(key, "有出入权限");
						} else {
							map2.put(key, "权限被禁");
						}
					} else {
						map2.put(key, map1.get(key));
					}
				}
				list2.add(map2);
			}
			page.setResultRows(list2);
			Response response = Response.OK();
			response.put("recordsTotal", page.getTotalRow());
			response.put("recordsFiltered", page.getTotalRow());
			response.setData(page.getResultRows());
			response.put("recordsFiltered", page.getTotalRow());
			response.put("draw", currentPage);
			response.put("total", page.getTotalRow());
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/querybyuser_id")
	@ResponseBody
	/**
	 * 查询一个人的所有 权限 访问url
	 * 
	 * UserCards/querybyuser_id?user_id="1111"
	 * 
	 * @param paramJson
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response querybyuser_id(String user_id) {
		try {

			// 根据user_id获取相应的UserCards列表

			List<UserCards> uc = this.getService().getUserCardsService().getUserCardsDao().getUserCarduser_id(user_id);
			Response response = Response.OK();
			String face = "0";
			String ic = "0";
			String ic_card = null;
			String id = "0";
			String use_times = null;
			String end_time = null;
			String is_tmp = null;
			if (uc != null) {
				for (UserCards uc_s : uc) {
					use_times = uc_s.getUse_times();
					end_time = uc_s.getEnd_time();
					is_tmp = uc_s.getIs_tmp();

					if (uc_s.getType().equals("7")) {
						face = "1";
					} else if (uc_s.getType().equals("4")) {
						ic = "1";
						ic_card = uc_s.getUser_auth_card();
					} else if (uc_s.getType().equals("0")) {
						id = "1";
					}

				}
			}

			List<GateList> gl = this.getService().getGatelistservice().getGatelistdao().getGateListbyuser_id(user_id);

			response.put("face", face);
			response.put("ic", ic);
			response.put("id", id);
			response.put("use_times", use_times);
			response.put("end_time", end_time);
			response.put("is_tmp", is_tmp);
			response.put("ic_card", ic_card);
			response.put("gate_list", gl);
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	/**
	 * 开卡
	 * 
	 */
	public int OpenCard(String card_no, String cid) {

		String module = "opencard";
		settingservice = this.getService().getSettingservice();
		system_setting = settingservice.getSettingdao().getOneObject();

		String url = system_setting.getRequest_url() + "/gate/" + module;

		String TOKEN = system_setting.getToken();
		String region_id = system_setting.getRegion_id();
		String seq_no = MakeSeq();
		String parm = "region_id=" + region_id + "&seq_no=" + seq_no + "&TOKEN=" + TOKEN + "&card_no=" + card_no
				+ "&CID=" + cid;

		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);

		int error = json.getInteger("error");
		if (error == 0) {
			request_time = 0;
			TOKEN = json.getString("TOKEN");

			ChangeToken();
		} else {
			request_time = request_time + 1;
			this.login();
			if (request_time <= 3) {
				int error2 = this.OpenCard(card_no, cid);
			}

		}
		System.out.print(error);
		return error;

	}

	/**
	 * 登陆接口login
	 * 
	 */
	public String login() {
		System.out.println("登录");
		String module = "login";
		String url = system_setting.getRequest_url() + "/gate/" + module;
		String seq_no = MakeSeq();
		String parm = "region_id=" + system_setting.getRegion_id() + "&scrt_key=" + system_setting.getScrt_key()
				+ "&seq_no=" + seq_no;

		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);
		int error = json.getInteger("error");
		if (error == 0) {
			TOKEN = json.getString("TOKEN");
			this.ChangeLastLogintime();

		}

		return TOKEN;

	}

	/**
	 * 每次登陆成功后更新数据库的最后登录时间
	 * 
	 */
	private void ChangeLastLogintime() {
		if (system_setting != null) {
			// System.out.println("登陆后改变token");
			system_setting.setToken(TOKEN);
			String last_login_time = DateUtils.getCurrenTimeStamp();

			system_setting.setLast_login_time(last_login_time);
			settingservice.getSettingdao().update(system_setting);

		}
	}

	/**
	 * 
	 * 
	 * 
	 * @api {get} taskRemind/ChangeToken
	 * @apiGroup APIManager
	 * @apiName TODO(这里用一句话描述这个类的作用)
	 * @apiDescription更新DeviceToken
	 * @apiParam
	 * @apiSuccess
	 * @apiVersion 1.0.0
	 * @apiErrorExample {json} Error-Response: { "code" :"", "message": "请登录" }
	 */
	private void ChangeToken() {
		if (system_setting != null) {
			// System.out.println("请求后改变token");
			system_setting.setToken(TOKEN);
			settingservice.getSettingdao().update(system_setting);
		}
	}

	/**
	 * 生成带时间的请求编号
	 * 
	 * @return
	 */
	public String MakeSeq() {
		Date date = new Date();

		String time = DateUtils.dateToStr(date, 5);
		int radomInt = new Random().nextInt(999999);
		String seq = time + String.valueOf(radomInt);
		return seq;

	}

}
