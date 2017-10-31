package gate.cn.com.lanlyc.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Codes;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.WorkersTypes;

@Controller
@RequestMapping("/WorkersTypes")
public class WorkersTypesController extends BaseController {
	@RequestMapping("/add")
	@ResponseBody
	/**
	 * 新增工种
	 * 
	 * 访问url WorkersTypes/add?paramJson={"name":"一个工种" "value":"333"
	 * "order_by":"200"}
	 * 
	 * @param name
	 *            工种的名称
	 * @param value
	 *            工种对应的值
	 * @param order_by
	 *            排序方式
	 * @return {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response add(String paramJson) {
		try {
			JSONObject object = JSONObject.parseObject(paramJson);
			if (!object.containsKey("work_name") || !object.containsKey("order_by")) {
				Response response = Response.PARAM_ERROR();
				return response;
			}
			String name = object.getString("work_name");
			// String value=object.getString("value");
			String order_by = object.getString("order_by");

			Response response = Response.OK();
			WorkersTypes wt = new WorkersTypes();
			String id = DataUtils.getUUID();

			wt.setId(id);
			wt.setName(name);
			// wt.setValue(value);
			wt.setOrder_by(order_by);
			String res = new String("新增工种：" + name);
			int change = this.getService().getWorkerstypesservice().getWorkersTypes().save(wt);
			this.addUserOperationlog(res);
			if (change != 0) {
				// 这里做大屏数据的修改
				Boolean change1 = this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao()
						.change();
				System.out.println("大屏修改数据" + "change1");
			} else {
				Response response1 = Response.PARAM_ERROR();
				return response1;
			}
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}

	}

	/**
	 * 删除工种 访问url WorkersTypes/del?paramJson={"id":"1111111,111111,111111,111111"}
	 * 
	 * @param paramJson
	 *            {"id":"1111111,111111,1111111,111111"} 代表工种对应的id列表
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Response deleteUserClass(String id) {
		// 删除可以批量删除
		try {
			WorkersTypes default_work_type = this.getService().getWorkerstypesservice().getWorkersTypes()
					.getDefaultWork();
			if (default_work_type == null) {// 如果没有默认的工种
				Response response = Response.newResponse();
				response.put("code", "1001");
				response.put("message", "请先添加么默认工种，默认工种名称必须以\"其他工种\"命名");
				return response;
			}

			String ids = id;
			String[] ids1 = ids.split(",");
			List work_typr_list = Arrays.asList(ids1);
			if (work_typr_list.contains(default_work_type.getId())) {
				Response response = Response.newResponse();
				response.put("code", "1001");
				response.put("message", "默认工种不可删除");
				return response;
			}

			StringBuffer res = new StringBuffer("删除工种：");
			for (String id_split : ids1) {
				WorkersTypes wt = this.getService().getWorkerstypesservice().getWorkersTypes().get(id_split);
				res.append(wt.getName() + " ");
				// 将相应的人员的工种对应的value进行相应的修改
				// 获取所有的相应的人员的
				Boolean change_outcome = this.getService().getGateuserservice().changeUserWorkTypeToDefault(id_split);

				int change = this.getService().getWorkerstypesservice().getWorkersTypes().delete(id_split);
				// 将相应的人员的工种对应的value进行相应的修改

				if (change != 0) {
					// 这里做大屏数据的修改
					Boolean change1 = this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao()
							.change();
					System.out.println("大屏修改数据" + "change1");
				} else {
					Response response = Response.PARAM_ERROR();
					return response;
				}

			}
			Response response = Response.OK();
			this.addUserOperationlog(res.toString());
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/update")
	@ResponseBody
	/**
	 * 修改某个工种 访问url WorkersTypes/update?paramJson={"id":"111111","name":"工种名称"}
	 * 
	 * @param paramJson
	 *            {"id":"111111","name":"工种名称"} 代表人员类别对应的id和需要修改的人员类别名称
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response updateUserClass(String paramJson) {
		try {
			JSONObject json = JSONObject.parseObject(paramJson);
			String id = json.getString("id");
			String name = json.getString("work_name");
			String order_by = json.containsKey("order_by") ? json.getString("order_by") : "";
			// -----------此处用于相应的校验----------------
			WorkersTypes default_work_type = this.getService().getWorkerstypesservice().getWorkersTypes()
					.getDefaultWork();
			if (id.equals(default_work_type.getId())) {
				if (!name.equals(default_work_type.getName())) {
					Response response = Response.newResponse();
					response.put("code", "1001");
					response.put("message", "默认工种名称必须以\"其他工种\"命名,不能修改");
					return response;
				}
			}

			// ---------------------------------------
			WorkersTypes wt = new WorkersTypes();
			wt.setId(id);
			wt.setName(name);
			if (order_by != "") {
				wt.setOrder_by(order_by);
			}
			int change = this.getService().getWorkerstypesservice().getWorkersTypes().update(wt, false);

			if (change != 0) {
				// 这里做大屏数据的修改
				Boolean change1 = this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao()
						.change();
				System.out.println("大屏修改数据" + "change1");
				String res = "修改工种：" + name;
				this.addUserOperationlog(res);
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

	@RequestMapping("/query")
	@ResponseBody
	/**
	 * 查询工种 访问url WorkersTypes/query?paramJson={"current_page":"1","page_num":"5",
	 * "name":"kd","order":"name","order_by":"DESC"}
	 * 
	 * @param paramJson
	 *            {"current_page":1,"page_num":5,
	 *            "name":"kd","order":"name","order_by":"DESC"}
	 *            参数{当前页面（必传）、页面数据量（必传）、排序方式（选传）、正序还是反序（默认是asc（正序））（选传）}
	 * @return {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	public Response queryUserClass(String paramJson, int length, int start, int draw) {
		try {
			JSONObject json = JSONObject.parseObject(paramJson);
			int current_page = start / length + 1;
			int page_num = length;

			String order = json.containsKey("order") ? json.getString("order") : "";
			String order_by = json.containsKey("order_by") ? json.getString("order_by") : "";
			String name = json.containsKey("work_name") ? json.getString("work_name") : "";

			int currentPage = start / length + 1;
			Page<WorkersTypes> page;
			page = this.getService().getWorkerstypesservice().getWorkersTypes().queryList(current_page, page_num, name,
					order, order_by);
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

	@RequestMapping("/queryAll")
	@ResponseBody
	/**
	 * 查询所有工种的 访问url WorkersTypes/queryAll?token
	 * 
	 * @return response
	 */
	public Response queryAll() {
		try {
			List<WorkersTypes> wt = this.getService().getWorkerstypesservice().getWorkersTypes().getAll();
			Response response = Response.OK();
			response.put("data", wt);
			return response;

		} catch (Exception e) {
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
		WorkersTypes dt = this.getService().getWorkerstypesservice().getWorkersTypes().get(id);

		Response response = Response.OK();
		response.put("data", dt);
		return response;

	}

	@RequestMapping("/testWorkname")
	@ResponseBody
	/**
	 * 工种名称的查重 GateUser/querybyid?id=“11111”
	 * 
	 * @param paramJson
	 *            id="111"
	 * 
	 * @return response
	 */
	public Response testWork(String name, String id) {
		if (name == null || "".equals(name)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		List<WorkersTypes> dt = this.getService().getWorkerstypesservice().getWorkersTypes().worknametest(name, id);

		if (dt != null) {
			Response response = Response.newResponse();
			response.put("code", Codes.ROLE_EXISTS);
			response.put("message", "工种名称重复");
			return response;
		}

		Response response = Response.OK();
		response.put("data", dt);
		return response;
	}

}
