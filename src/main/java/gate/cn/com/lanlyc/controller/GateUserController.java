package gate.cn.com.lanlyc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.ExcelUtiltest;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Codes;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateGroup;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserClass;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import gate.cn.com.lanlyc.core.po.WorkersTypes;
import net.sf.json.JSONObject;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_DECCHANSTATUS.objectInfo;

/**
 * 
 * @author yangwei time 2017-8-23 09:01:07 Function 实现用户方面的逻辑操作
 * 
 */

@Controller
@RequestMapping("/GateUser")
public class GateUserController extends BaseController {

	@RequestMapping("/addGateUser")
	@ResponseBody
	/**
	 * 新增相关用户
	 * 
	 * 访问url GateUser/addgateUser?paramJson={ "cid":"111111" ,"name":"杨威",
	 * "mobile":"13018036794", "gender":"1" ,//（1男2女）
	 * "avatar_img":"1.jpg",//头像是存在服务器上的，那么存的是相应的文件名称 "workers_type":"1"
	 * "team":"杨威的班组" ,"punish_record":"12",//表示惩罚次数
	 * "user_class_id":"111111",//人员类别id }
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response addGateUser(String paramJson) {
		try {
			JSONObject json = JSONObject.fromObject(paramJson);
			// 1、增加人员（四个基本方法）
			String outcome = this.getService().getGateuserservice().add_gate_user(json);
			Response response = Response.newResponse();
			if (outcome != null) {
				this.addUserOperationlog(outcome);
				response = Response.OK();
			} else {
				response = Response.PARAM_ERROR();
			}
			return response;

		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	/**
	 * 删除一个用户 访问url
	 * GateUser/deleteGateuser??paramJson={"id":"1111111,111111,111111,111111"}
	 * 
	 * @param paramJson
	 *            {"id":"1111111,111111,1111111,111111"} 代表人员对应的id列表
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	@RequestMapping("/deleteGateuser")
	@ResponseBody
	public Response deleteGateuser(String id) {
		// 删除可以批量删除
		try {
			String outcome = this.getService().getGateuserservice().del_gate_user(id);
			Response response = Response.OK();
			this.addUserOperationlog(outcome);
			return response;
		} catch (Exception e) {
			// 如果相应的人员删除失败，就将相应的人员权限进行修改
			HttpServletRequest request = this.getRequest();
			this.getService().getUserCardsService().txActionCard(id, "3", request);
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/updateGateuser")
	@ResponseBody
	/**
	 * 通过id 修改某一个用户 访问url GateUser/updateGateuser?paramJson={
	 * "id":"32596b6a09f64a6b80c1dc78f1ea4a87", "cid":"456896626332566",
	 * "name":"何Ⅸ", "mobile":"13018036794", "gender":"2", "sate":"2" ,"avatar_img" =
	 * "2.jpg" ,"workers_type" = "2" ,"team" = "大帅班" ,"punish_record" = "21" }
	 * 
	 * @param paramJson
	 *            {} 代表人员类别对应的id和需要修改的人员类别名称
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response updateGateuser(String paramJson) {
		try {
			JSONObject json = JSONObject.fromObject(paramJson);

			String outcome = this.getService().getGateuserservice().update_gate_user(json);
			Response response = Response.newResponse();
			if (outcome != null) {
				this.addUserOperationlog(outcome);
				response = Response.OK();
			} else {
				response = Response.PARAM_ERROR();
			}
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	/**
	 * 获取所有用户列表 访问url GateUser/queryUserslist
	 * 
	 * @param paramJson(以下是相应的json字段)
	 * 
	 * @param_current_page(传int) 当前页码
	 * @param page_num（传int）
	 *            每页的数据的条数
	 * @param name
	 *            姓名（对姓名进行模糊查询）
	 * @param cid
	 *            身份证（对身份证进行模糊查询）
	 * @param mobile
	 *            手机号（对手机号进行模糊查询）
	 * @param card_id
	 *            卡号（对卡号进行模糊查询）
	 * @param type
	 *            人员类别（条件查询）
	 * @param sate
	 *            开卡状态（条件查询）
	 * @param age
	 *            年龄（条件查询）
	 * @param gender
	 *            性别（条件查询）
	 * @param card_type
	 *            开卡类型（条件查询）
	 * @param workers_type
	 *            工种（条件查询）
	 * @param effective_time
	 *            有效期（条件查询）
	 * @param order
	 *            根据某个字段进行数据排序
	 * @param order_by
	 *            正序还是反序
	 * @return
	 */
	@RequestMapping("/queryUserslist")
	@ResponseBody
	public Response getuserslist(String paramJson, int length, int start, int draw) {
		// String current_page, // Integer.parseInt(str) String转为int的数据格式
		// String page_num, String name, String cid, String mobile, String card_id,
		// String type, String sate,
		// String age, String gender, String card_type, String workers_type, String
		// effective_time, String order,
		// String order_by
		JSONObject json = JSONObject.fromObject(paramJson);
		int current_page = start / length + 1;
		int page_num = length;

		Page<GateUserInfoView> outcome = this.getService().getGateuserservice().list_gate_user(json, current_page,
				page_num);
		Response response = Response.OK();
		response.put("recordsTotal", outcome.getTotalRow());
		response.put("recordsFiltered", outcome.getTotalRow());
		response.setData(outcome.getResultRows());
		response.put("recordsFiltered", outcome.getTotalRow());
		response.put("draw", draw);
		response.put("total", outcome.getTotalRow());
		return response;
	}

	/**
	 * 获取所有用户列表 访问url GateUser/queryUserslist
	 * 
	 * @param paramJson(以下是相应的json字段)
	 * 
	 * @param_current_page(传int) 当前页码
	 * @param page_num（传int）
	 *            每页的数据的条数
	 * @param name
	 *            姓名（对姓名进行模糊查询）
	 * @param cid
	 *            身份证（对身份证进行模糊查询）
	 * @param mobile
	 *            手机号（对手机号进行模糊查询）
	 * @param card_id
	 *            卡号（对卡号进行模糊查询）
	 * @param type
	 *            人员类别（条件查询）
	 * @param sate
	 *            开卡状态（条件查询）
	 * @param age
	 *            年龄（条件查询）
	 * @param gender
	 *            性别（条件查询）
	 * @param card_type
	 *            开卡类型（条件查询）
	 * @param workers_type
	 *            工种（条件查询）
	 * @param effective_time
	 *            有效期（条件查询）
	 * @param order
	 *            根据某个字段进行数据排序
	 * @param order_by
	 *            正序还是反序
	 * @return
	 */
	@RequestMapping("/queryUsersByIc")
	@ResponseBody
	public Response getusersByIc(String paramJson, int length, int start, int draw) {
		// String current_page, // Integer.parseInt(str) String转为int的数据格式
		// String page_num, String name, String cid, String mobile, String card_id,
		// String type, String sate,
		// String age, String gender, String card_type, String workers_type, String
		// effective_time, String order,
		// String order_by
		JSONObject json = JSONObject.fromObject(paramJson);
		int current_page = start / length + 1;
		int page_num = length;

		Page<GateUserInfoView> outcome = this.getService().getGateuserservice().list_gate_user_byIC(json, current_page,
				page_num);
		Response response = Response.OK();
		response.put("recordsTotal", outcome.getTotalRow());
		response.put("recordsFiltered", outcome.getTotalRow());
		response.setData(outcome.getResultRows());
		response.put("recordsFiltered", outcome.getTotalRow());
		response.put("draw", draw);
		response.put("total", outcome.getTotalRow());
		return response;
	}

	@RequestMapping("/queryUserexcel")
	@ResponseBody
	public void getuserslistexcel(HttpServletRequest request, HttpServletResponse response1, String paramJson) {
		// String current_page, // Integer.parseInt(str) String转为int的数据格式
		// String page_num, String name, String cid, String mobile, String card_id,
		// String type, String sate,
		// String age, String gender, String card_type, String workers_type, String
		// effective_time, String order,
		// String order_by
		JSONObject json = JSONObject.fromObject(paramJson);
		this.getService().getGateuserservice().Excel_gate_user(json, request, response1);

	}

	@RequestMapping("/testMobile")
	@ResponseBody
	/**
	 * 验证新增用户的手机号码是否重复 访问url GateUser/testMobile?paramJson={"mobile":"13018036794"}
	 * 
	 * @param paramJson
	 *            {"mobile":"13018036794"} 传相应的手机号码
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 * 
	 */
	public Response testMobile(String mobile, String user_id) {
		Map ref = this.getService().getGateuserservice().test_mobile(mobile, user_id);

		if (ref.containsKey("id")) {
			Response response = Response.set("code", Codes.PHONE_EXISTS);
			response.put("message", "手机号已存在");
			response.put("valid", false);
			return response;
		} else {
			Response response = Response.OK();
			response.put("valid", true);
			return response;
		}

	}

	@RequestMapping("/testcid")
	@ResponseBody
	/**
	 * 验证新增用户的身份证是否重复 访问url GateUser/testcid?cid=420621199408257738
	 * 
	 * @param cid
	 *            身份证
	 * @return JSON
	 */
	public Response testcid(String cid, String user_id) {
		Map ref = this.getService().getGateuserservice().test_cid(cid, user_id);

		if (ref.containsKey("id")) {
			Response response = Response.set("code", Codes.PHONE_EXISTS);
			response.put("message", "身份证号码已存在");
			response.put("valid", false);
			return response;
		} else {
			Response response = Response.OK();
			response.put("valid", true);
			return response;
		}

	}

	/**
	 * 更改一个人员的开卡状态 访问url
	 * GateUser/sategateuser?id=f5db32a8ef8642e688407ff68de4373c&sate=1
	 * 共有开卡、销卡（这个弄完后还要对相应的人员卡表中的数据进行操作） 停卡、复卡的几个属性（只需要对人员的信息表进行处理就可以了）
	 * 
	 * @param id
	 * @param sate
	 * @return
	 */
	@RequestMapping("/sategateuser")
	@ResponseBody
	public Response sategateuser(String id, String sate) {
		// 可以批量进行相应的修改
		try {
			String[] ids = id.split(",");
			for (String id_split : ids) {
				// GateUser GU = new GateUser();
				// GU.setId(id_split);
				// GU.setSate(sate);
				// int dtres = this.getService().getGateuserservice().getGateuser().update(GU);
				// System.out.println(dtres);
				this.getService().getGateuserservice().getGateuser().updateSate(id_split, sate);

			}
			Response response = Response.OK();
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/querybyid")
	@ResponseBody
	/**
	 * 通过id来查询信息 访问url GateUser/querybyid?id=“11111”
	 * 
	 * @param paramJson
	 *            id="111"
	 * 
	 * @return response
	 */
	public Response querybyid(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		GateUser dt = this.getService().getGateuserservice().getGateuser().get(id);

		Response response = Response.OK();
		response.put("data", dt);
		return response;

	}

	/**
	 * 
	 * 
	 * 
	 * @param users
	 *            用户ids
	 * @param status
	 *            设置用户处理状态 1恢复卡 2停卡 3 销卡
	 * @return
	 */
	@RequestMapping("/Setusercardsstatus")
	@ResponseBody
	public Response Setusercardsstatus(String user_ids, String status) {
		try {

			if (user_ids == null || "".equals(user_ids)) {
				Response response = Response.PARAM_ERROR();
				return response;
			}
			boolean is_success = true;
			if (status.equals("2")) {

				Object out_come = this.getService().getGateuserauthservie().setUserAuthPause(user_ids);

				if (out_come instanceof String) {
					Response response = Response.ERROR(-1, out_come.toString().substring(1) + "无法连接，请检查相应的设备");

					return response;
				}
				if (out_come instanceof Boolean) {
					if (!(Boolean) out_come) {
						Response response = Response.ERROR(-1, "处理失败");

						return response;
					}
				}

			}
			if (status.equals("1")) {
				HttpServletRequest request = this.getRequest();
				Object out_come = this.getService().getGateuserauthservie().setUserAuthNormal(user_ids, request);
				if (out_come instanceof String) {
					Response response = Response.ERROR(-1, out_come.toString().substring(1) + "无法连接，请检查相应的设备");

					return response;
				}
				if (out_come instanceof Boolean) {
					if (!(Boolean) out_come) {
						Response response = Response.ERROR(-1, "处理失败");

						return response;
					}
				}
			}
			if (status.equals("3")) {
				Object out_come = this.getService().getUserCardsService().ClearUserCards(user_ids);
				if (out_come instanceof String) {
					Response response = Response.ERROR(-1, out_come.toString().substring(1) + "无法连接，请检查相应的设备");

					return response;
				}
				if (out_come instanceof Boolean) {
					if (!(Boolean) out_come) {
						Response response = Response.ERROR(-1, "处理失败");

						return response;
					}
				}
			}
			String[] ids = user_ids.split(",");
			for (String id_split : ids) {
				// GateUser GU = new GateUser();
				// GU.setId(id_split);
				// GU.setSate(sate);
				// int dtres = this.getService().getGateuserservice().getGateuser().update(GU);
				// System.out.println(dtres);
				this.getService().getGateuserservice().getGateuser().updateSate(id_split, status);

			}
			Response response = Response.OK();

			return response;
		} catch (Exception e) {
			// 有关（停卡、复卡、销卡）设备和权限一致的事务处理
			HttpServletRequest request = this.getRequest();
			this.getService().getUserCardsService().txActionCard(user_ids, status, request);

			Response response = Response.ERROR(-1, "处理失败");

			return response;
		}
	}
}
