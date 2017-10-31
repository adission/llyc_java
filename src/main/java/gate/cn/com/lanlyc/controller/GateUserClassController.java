package gate.cn.com.lanlyc.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Codes;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateUserClass;
import net.sf.json.JSONObject;

/**
 * 
 * @author yangwei time 2017-8-22 17:40:14 Function 实现人员类别的相关操作
 * 
 */

@Controller
@RequestMapping("/GateUserClass")
public class GateUserClassController extends BaseController {

	@RequestMapping("/addUserClass")
	@ResponseBody
	/**
	 * 新增人员类别
	 * 
	 * 访问url GateUserClass/addUserClass?paramJson={"name":"工作人员"}
	 * 
	 * @param paramJson
	 *            {"name":"工作人员"} 对应人员类别的名称
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 * 
	 */
	public Response addUserClass(String paramJson) {
		try {
			JSONObject json = JSONObject.fromObject(paramJson);
			String name = json.getString("class_name");
			// 进行相应人员类别的名称验证
			if (name.equals("") || name == null) {
				Response response = Response.ERROR(-1, "人员类别名称不能为空！");
				return response;
			} else {
				GateUserClass outCome = this.getService().getGateuserclassservice().testUserClass(name, null);
				if (outCome != null) {
					Response response = Response.ERROR(-1, "人员类别名称不能重复！");
					return response;
				}
			}

			GateUserClass GUClass = new GateUserClass();
			String id = DataUtils.getUUID();
			GUClass.setId(id);
			GUClass.setName(name);
			String res = new String("新增人员类别：" + name);
			int change = this.getService().getGateuserclassservice().getGateuserclass().save(GUClass);
			this.addUserOperationlog(res);
			if (change != 0) {
				// 这里做大屏数据的修改
				Boolean change1 = this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao()
						.ChangeClass();
				System.out.println("大屏修改数据" + "change1");
			} else {
				Response response1 = Response.PARAM_ERROR();
				return response1;
			}

			Response response = Response.OK();
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	/**
	 * 删除一个人员类别 访问url
	 * GateUserClass/deleteUserClass?paramJson={"id":"1111111,111111,111111,111111"}
	 * 
	 * @param paramJson
	 *            {"id":"1111111,111111,1111111,111111"} 代表人员类别对应的id列表
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	@RequestMapping("/deleteUserClass")
	@ResponseBody
	public Response deleteUserClass(String id) {
		// 删除可以批量删除
		try {
			GateUserClass default_user_class = this.getService().getGateuserclassservice().getGateuserclass()
					.getDefaultClass();
			if (default_user_class == null) {// 如果没有默认的人员类别
				Response response = Response.newResponse();
				response.put("code", "1001");
				response.put("message", "请先添加么默认类别，默认工种名称必须以\"其他人员\"命名");
				return response;
			}

			String ids = id;
			String[] ids1 = ids.split(",");
			List user_class_list = Arrays.asList(ids1);
			if (user_class_list.contains(default_user_class.getId())) {
				Response response = Response.newResponse();
				response.put("code", "1001");
				response.put("message", "默认类别不可删除");
				return response;
			}
			for (String id_split : ids1) {

				// 将相应的人员类别在删除时默认为相应的默认人员类别
				Boolean change_outcome = this.getService().getGateuserservice().changeUserClassToDefault(id_split);

				int change = this.getService().getGateuserclassservice().getGateuserclass().delete(id_split);
				if (change != 0) {
					// 这里做大屏数据的修改
					Boolean change1 = this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao()
							.ChangeClass();
					System.out.println("大屏修改数据" + "change1");
				} else {
					Response response = Response.PARAM_ERROR();
					return response;
				}

			}
			Response response = Response.OK();
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/updateUserClass")
	@ResponseBody
	/**
	 * 修改某个人员类别 访问url
	 * GateUserClass/updateUserClass?paramJson={"id":"111111","name":"管理人员"}
	 * 
	 * @param paramJson
	 *            {"id":"111111","name":"管理人员"} 代表人员类别对应的id和需要修改的人员类别名称
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response updateUserClass(String paramJson) {
		try {
			JSONObject json = JSONObject.fromObject(paramJson);
			String id = json.getString("id");
			String name = json.getString("class_name");

			// 进行相应人员类别的名称验证
			if (name.equals("") || name == null) {
				Response response = Response.ERROR(-1, "人员类别名称不能为空！");
				return response;
			} else {
				GateUserClass outCome = this.getService().getGateuserclassservice().testUserClass(name, id);
				if (outCome != null) {
					Response response = Response.ERROR(-1, "人员类别名称不能重复！");
					return response;
				}
			}

			// -----------此处用于相应的校验----------------
			GateUserClass default_user_class = this.getService().getGateuserclassservice().getGateuserclass()
					.getDefaultClass();
			if (id.equals(default_user_class.getId())) {
				if (!name.equals(default_user_class.getName())) {
					Response response = Response.newResponse();
					response.put("code", "1001");
					response.put("message", "默认类别名称必须以\"其他类别\"命名,不能修改");
					return response;
				}
			}

			// ---------------------------------------

			GateUserClass GUClass = new GateUserClass();
			GUClass.setId(id);
			GUClass.setName(name);
			int change = this.getService().getGateuserclassservice().getGateuserclass().update(GUClass, false);
			if (change != 0) {
				// 这里做大屏数据的修改
				Boolean change1 = this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao()
						.ChangeClass();
				System.out.println("大屏修改数据" + "change1");
			} else {
				Response response = Response.PARAM_ERROR();
				return response;
			}
			Response response = Response.OK();
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/queryUserClass")
	@ResponseBody
	/**
	 * 查询人员类别的 访问url
	 * GateUserClass/queryUserClass?paramJson={"current_page":"1","page_num":"5","order":"name","order_by":"DESC"}
	 * 
	 * @param paramJson
	 *            {"current_page":1,"page_num":5,"order":"name","order_by":"DESC"}
	 *            参数{当前页面（必传）、页面数据量（必传）、排序方式（选传）、正序还是反序（默认是asc（正序））（选传）}
	 * @return response
	 */
	public Response queryUserClass(String paramJson, int length, int start, int draw) {
		try {
			JSONObject json = JSONObject.fromObject(paramJson);
			int current_page = start / length + 1;
			int page_num = length;
			String order = "";
			String order_by = "";
			String class_name = "";
			if (json.containsKey("order")) {
				order = json.getString("order");
			}
			if (json.containsKey("order_by")) {
				order_by = json.getString("order_by");
			}
			if (json.containsKey("class_name")) {
				class_name = json.getString("class_name");
			}
			Page<GateUserClass> page;
			page = this.getService().getGateuserclassservice().getGateuserclass().queryList(current_page, page_num,
					class_name, order, order_by);
			Response response = Response.OK();
			response.put("recordsTotal", page.getTotalRow());
			response.put("recordsFiltered", page.getTotalRow());
			response.setData(page.getResultRows());
			response.put("recordsFiltered", page.getTotalRow());
			response.put("draw", draw);
			response.put("total", page.getTotalRow());
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/queryUserClassAll")
	@ResponseBody
	/**
	 * 查询所有工种的 访问url GateUserClass/queryUserClassAll?token
	 * 
	 * @return response
	 */
	public Response queryUserClassAll() {
		try {
			List<GateUserClass> gu = this.getService().getGateuserclassservice().getGateuserclass().getAll();
			Response response = Response.OK();
			response.put("data", gu);
			return response;

		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/queryUserClassbyid")
	@ResponseBody
	/**
	 * 通过id来查询信息 访问url GateUserClass/queryUserClassbyid?id=“11111”
	 * 
	 * @param paramJson
	 *            id="111"
	 * 
	 * @return response
	 */
	public Response queryUserClassbyid(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		GateUserClass dt = this.getService().getGateuserclassservice().getGateuserclass().get(id);

		Response response = Response.OK();
		response.put("data", dt);
		return response;

	}

	@RequestMapping("/testClassname")
	@ResponseBody
	/**
	 * 工种名称的查重 GateUser/querybyid?id=“11111”
	 * 
	 * @param paramJson
	 *            id="111"
	 * 
	 * @return response
	 */
	public Response testClassname(String name, String id) {
		if (name == null || "".equals(name)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		List<GateUserClass> dt = this.getService().getGateuserclassservice().getGateuserclass().classnametest(name, id);

		if (dt != null) {
			Response response = Response.newResponse();
			response.put("code", Codes.ROLE_EXISTS);
			response.put("message", "人员类别名称重复");
			return response;
		}

		Response response = Response.OK();
		response.put("data", dt);
		return response;
	}

}
